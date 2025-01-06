package me.zyromate.zyrostaffutils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtils {
    private final JavaPlugin plugin;

    public ChatUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendConsoleMessage(String message) {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendInitialization(String feature) {
        plugin.getLogger().info("---------------------------");
        plugin.getLogger().info("Feature: " + feature);
        plugin.getLogger().info("Successfully loaded");
        plugin.getLogger().info("---------------------------");
    }

    public void onReload() {
        plugin.getLogger().info("---------------------------");
        plugin.getLogger().info("ZyroStaffUtils             ");
        plugin.getLogger().info("Successfully been reloaded");
        plugin.getLogger().info("---------------------------");
    }

    public void sendMessageToStaff(String message) {
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', message);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("zyrostaffutils.staff.alerts")) {
                onlinePlayer.sendMessage(formattedMessage);
            }
        }
    }

}
