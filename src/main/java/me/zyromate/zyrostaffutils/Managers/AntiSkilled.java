package me.zyromate.zyrostaffutils.Managers;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiSkilled implements Listener {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private ChatUtils chatUtils;

    public AntiSkilled(JavaPlugin plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        this.config = plugin.getConfig();
    }

    public void init() {
        if (!isActivated()) return;
        chatUtils.sendInitialization("Anti-Skilled");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    private boolean isActivated() {
        return config.getBoolean("AntiSkilled.is-activated");
    }

    public void reloadSettings() {
        config = plugin.getConfig();
        chatUtils = new ChatUtils(plugin);
        if (!isActivated()) return;
        chatUtils.onReload("Anti-Skilled");
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().trim();

        if (!command.equalsIgnoreCase("/achat gg")) return;

        event.setCancelled(true);

        if (player.hasPermission("zyrostaffutils.antiskilled.bypass")) {
            String bypassMessage = config.getString("AntiSkilled.bypass-message");
            chatUtils.sendMessage(player, bypassMessage);
            return;
        }


        banPlayer(player);
    }

    private void banPlayer(Player player) {
        String banMessage = config.getString("AntiSkilled.ban-message");
        int banDuration = config.getInt("AntiSkilled.ban-duration");

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "tempban " + player.getName() + " " + banDuration + "m " + banMessage);
    }
}
