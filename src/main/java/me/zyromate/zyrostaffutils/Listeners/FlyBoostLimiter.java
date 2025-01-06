package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class FlyBoostLimiter implements Listener {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;
    private double maxXBoostLimit;
    private double maxYBoostLimit;
    private final Set<Player> usersFlagging = new HashSet<>();
    private static final String PERMISSION = "zyrostaffutils.flybooster.bypass";
    private FileConfiguration config;

    public FlyBoostLimiter(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.chatUtils = new ChatUtils(plugin);
        config = plugin.getConfig();
        logInitialization();
        maxXBoostLimit = config.getDouble("flyboostlimiter.max-x-boost");
        maxYBoostLimit = config.getDouble("flyboostlimiter.max-y-boost");
    }

    private void logInitialization() {
        if (!isActivated()) return;
        chatUtils.sendInitialization("FlyBoostLimiter");
    }

    private boolean isActivated() {
        return config.getBoolean("flyboostlimiter.is-activated");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isActivated()) return;

        Player player = event.getPlayer();
        if (!player.isFlying()) return;

        Location oldLoc = event.getFrom();
        Location newLoc = event.getTo();

        if (newLoc == null) return;

        double horizontalDistance = Math.sqrt(
                Math.pow(newLoc.getX() - oldLoc.getX(), 2) +
                        Math.pow(newLoc.getZ() - oldLoc.getZ(), 2)
        );

        double verticalDistance = Math.abs(newLoc.getY() - oldLoc.getY());

        if (horizontalDistance >= maxXBoostLimit || verticalDistance >= maxYBoostLimit) {
            if (player.hasPermission(PERMISSION)) return;
            event.setTo(oldLoc);
            event.getPlayer().setVelocity(new Vector(0, 0, 0));
            usersFlagging.add(player);
            String exceedMessage = config.getString("flyboostlimiter.exceed-message");
            chatUtils.sendMessage(player, exceedMessage);
            String alertMessage = config.getString("flyboostlimiter.alert");
            chatUtils.sendMessageToStaff(alertMessage.replace("%player%", player.getDisplayName()));
        }
    }
}
