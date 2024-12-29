package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.configuration.file.FileConfiguration;

public class FlyBoostLimiter implements Listener {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;
    private double maxXBoostLimit;
    private double maxYBoostLimit;
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

    public void reloadSettings() {
        config = plugin.getConfig();
        if (!config.getBoolean("flyboostlimiter.is-activated")) return;
        chatUtils.onReload("FlyBoostLimiter");
    }

    private boolean isActivated() {
        return config.getBoolean("flyboostlimiter.is-activated");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isActivated()) return;

        Player player = event.getPlayer();

        if (player.hasPermission("zyrostaffutils.flybooster.bypass") || !player.isFlying()) return;

        handlePlayerMovement(event, player);
    }

    private void handlePlayerMovement(PlayerMoveEvent event, Player player) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || from == null) return;

        double deltaX = Math.abs(to.getX() - from.getX());
        double deltaY = Math.abs(to.getY() - from.getY());

        if (deltaX > maxXBoostLimit || deltaY > maxYBoostLimit) {
            teleportPlayerBack(player, from);
        }
    }

    private void teleportPlayerBack(Player player, Location from) {
        player.teleport(from);
        String exceedMessage = config.getString("flyboostlimiter.exceed-message");
        chatUtils.sendMessage(player, exceedMessage);
        String NiggaAlert = config.getString("flyboostlimiter.alert");
        chatUtils.sendMessageToStaff(player, NiggaAlert.replace("%player%", player.getDisplayName()));
    }
}
