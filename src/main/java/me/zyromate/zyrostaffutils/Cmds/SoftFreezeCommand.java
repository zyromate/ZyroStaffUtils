package me.zyromate.zyrostaffutils.Cmds;

import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SoftFreezeCommand implements CommandExecutor {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;
    private final Set<UUID> softFrozenPlayers = new HashSet<>();

    public SoftFreezeCommand(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        startReminderTask();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("zyrostaffutils.softfreeze.use")) {
            chatUtils.sendMessage(player, plugin.getConfig().getString("SoftFreeze.invalid-permission"));
            return true;
        }

        if (args.length != 1) {
            chatUtils.sendMessage(player, plugin.getConfig().getString("SoftFreeze.usage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            chatUtils.sendMessage(player, plugin.getConfig().getString("SoftFreeze.player-not-online"));
            return true;
        }

        toggleSoftFreeze(player, target);
        return true;
    }

    private void toggleSoftFreeze(Player sender, Player target) {
        UUID targetUUID = target.getUniqueId();

        if (softFrozenPlayers.contains(targetUUID)) {
            softFrozenPlayers.remove(targetUUID);
            chatUtils.sendMessage(sender, plugin.getConfig().getString("SoftFreeze.unfrozen-message").replace("%player%", target.getName()));
            chatUtils.sendMessage(target, plugin.getConfig().getString("SoftFreeze.unfrozen-target-message"));
        } else {
            softFrozenPlayers.add(targetUUID);
            chatUtils.sendMessage(sender, plugin.getConfig().getString("SoftFreeze.frozen-message").replace("%player%", target.getName()));
            chatUtils.sendMessage(target, plugin.getConfig().getString("SoftFreeze.frozen-target-message"));
        }
    }

    private void startReminderTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : softFrozenPlayers) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        chatUtils.sendMessage(player, plugin.getConfig().getString("SoftFreeze.reminder-message"));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 300L); // 300L = 15 seconds
    }

    public boolean isSoftFrozen(UUID uuid) {
        return softFrozenPlayers.contains(uuid);
    }

    public void handlePlayerLogout(Player player) {
        if (isSoftFrozen(player.getUniqueId())) {
            String banMessage = plugin.getConfig().getString("SoftFreeze.ban-message");
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(player.getName(), banMessage, null, null);
            player.kickPlayer(banMessage);
        }
    }
}