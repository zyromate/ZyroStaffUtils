package me.zyromate.zyrostaffutils.Cmds;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;

    public ReloadCommand(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("zyrostaffutils.reload")) {
                String noPerm = plugin.getConfig().getString("invalid-permission");
                chatUtils.sendMessage(player, noPerm);
                return true;
            }

            reloadPlugin(player);
        } else {
            reloadPlugin(sender);
        }

        return true;
    }

    private void reloadPlugin(CommandSender sender) {
        plugin.reloadConfig();

        String reloadMessage = plugin.getConfig().getString("plugin-reload");
        if (reloadMessage != null) {
            chatUtils.sendMessage(sender, reloadMessage);
        } else {
            sender.sendMessage("ZyroStaffUtils has been reloaded.");
            chatUtils.onReload();
        }
    }
}
