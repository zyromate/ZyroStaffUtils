package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class YPositionCheckListener implements Listener {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;

    public YPositionCheckListener(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        plugin.saveDefaultConfig();
        chatUtils.sendInitialization("Anti-Void");
    }

    private boolean isActivated(FileConfiguration config) {
        return config.getBoolean("Anti-Void.is-activated", true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        CompletableFuture.runAsync(() -> {
            FileConfiguration config = plugin.getConfig();

            if (!isActivated(config)) return;
            if (isInBlockedWorld(config, location.getWorld().getName())) return;

            if (location.getY() <= -1) {
                Bukkit.getScheduler().runTask(plugin, () -> teleportPlayerToConfiguredLocation(player, config));
            }
        });
    }

    private boolean isInBlockedWorld(FileConfiguration config, String worldName) {
        List<String> blockedWorlds = config.getStringList("Anti-Void.blocked-worlds");
        return blockedWorlds.contains(worldName);
    }

    private void teleportPlayerToConfiguredLocation(Player player, FileConfiguration config) {
        String worldName = config.getString("Anti-Void.teleport-location.world");
        double x = config.getDouble("Anti-Void.teleport-location.x");
        double y = config.getDouble("Anti-Void.teleport-location.y");
        double z = config.getDouble("Anti-Void.teleport-location.z");

        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            Location teleportLocation = new Location(world, x, y, z);
            player.teleport(teleportLocation);
        } else {
            plugin.getLogger().warning("The world specified in the config does not exist: " + worldName);
        }
    }
}
