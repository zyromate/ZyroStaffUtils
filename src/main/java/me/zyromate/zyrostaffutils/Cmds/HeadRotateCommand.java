package me.zyromate.zyrostaffutils.Cmds;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class HeadRotateCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private ChatUtils chatUtils;
    private boolean isInitialized = false;

    public HeadRotateCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        init();
    }

    public void init() {
        if (isInitialized) return;
        isInitialized = true;

        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        if (!isActivated()) return;

        chatUtils = new ChatUtils(plugin);

        if (plugin.getCommand("rotate") != null) {
            plugin.getCommand("rotate").setExecutor(this);
        }

        chatUtils.sendInitialization("HeadRotate");
    }
    private boolean isActivated() {
        return config.getBoolean("HeadRotate.is-activated");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("rotate")) return true;
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console is not allowed to use Head Rotate. Too bad");
            return true;
        }

        Player player = (Player) sender;

        CompletableFuture.runAsync(() -> {
            if (args.length != 1) {
                if (config.isList("HeadRotate.usage")) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (String line : config.getStringList("HeadRotate.usage")) {
                            chatUtils.sendMessage(player, line);
                        }
                    });
                } else {
                    String usage = config.getString("HeadRotate.usage", "Usage: /rotate <player>");
                    Bukkit.getScheduler().runTask(plugin, () -> chatUtils.sendMessage(player, usage));
                }
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    rotatePlayerHead(target);
                    String rotateSuccess = config.getString("HeadRotate.success-rotate");
                    chatUtils.sendMessage(player, rotateSuccess.replace("%player%", target.getDisplayName()));
                });
            } else {
                String playerNotFound = config.getString("HeadRotate.player-not-online");
                Bukkit.getScheduler().runTask(plugin, () -> chatUtils.sendMessage(player, playerNotFound));
            }
        });

        return true;
    }



    private void rotatePlayerHead(Player target) {
        Random random = new Random();
        float randomYaw = random.nextFloat() * 360;
        float randomPitch = -90 + random.nextFloat() * 180;

        Location currentLocation = target.getLocation();
        currentLocation.setYaw(randomYaw);
        currentLocation.setPitch(randomPitch);

        target.teleport(currentLocation);
    }
}
