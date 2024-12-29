package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

import java.util.List;

public class YPositionCheckListener implements Listener {

    private final ZyroStaffUtils plugin;
    private FileConfiguration config;
    private final ChatUtils chatUtils;

    public YPositionCheckListener(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        chatUtils.sendInitialization("Anti-Void");
    }

    public void reloadSettings() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        if (!isActivated()) return;
        chatUtils.onReload("Anti-Void");
    }

    private boolean isActivated() {
        // Make sure to check for null before accessing the config
        return config != null && config.getBoolean("Anti-Void.is-activated", true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isActivated()) return;

        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (isInBlockedWorld(location.getWorld().getName())) {
            return;
        }

        if (location.getY() <= -1) {
            teleportPlayerToConfiguredLocation(player);
        }
    }

    private boolean isInBlockedWorld(String worldName) {
        List<String> blockedWorlds = config.getStringList("Anti-Void.blocked-worlds");
        return blockedWorlds.contains(worldName);
    }

    private void teleportPlayerToConfiguredLocation(Player player) {
        String worldName = config.getString("Anti-Void.teleport-location.world");
        double x = config.getDouble("Anti-Void.teleport-location.x");
        double y = config.getDouble("Anti-Void.teleport-location.y");
        double z = config.getDouble("Anti-Void.teleport-location.z");

        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            Location teleportLocation = new Location(world, x, y, z);
            player.teleport(teleportLocation);
            plugin.getLogger().info("Teleported player " + player.getName() + " to " + teleportLocation);
        } else {
            plugin.getLogger().warning("The world specified in the config does not exist: " + worldName);
        }
    }
}
