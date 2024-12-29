package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class CaneBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final ChatUtils chatUtils;
    private final FileConfiguration config;

    private final HashMap<UUID, Long> caneBreakTime;

    public CaneBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        this.config = plugin.getConfig();
        this.caneBreakTime = new HashMap<>();

        if (isActivated()) chatUtils.sendInitialization("Anti-CaneNuke");
    }

    @EventHandler
    public void onCaneBroken(BlockBreakEvent event) {
        if (!isActivated()) return;

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material blockType = event.getBlock().getType();

        if (blockType != Material.SUGAR_CANE) return;

        long currentTime = System.currentTimeMillis();
        long lastBreakTime = caneBreakTime.getOrDefault(playerUUID, 0L);

        long timeSinceLastBreak = currentTime - lastBreakTime;
        int cooldownTime = config.getInt("Anti-CaneNuke.cooldown-time") * 1000;
        int caneLimit = config.getInt("Anti-CaneNuke.cane-limit");
        int resetTime = config.getInt("Anti-CaneNuke.reset-time") * 20;

        if (timeSinceLastBreak < cooldownTime) {
            long timeLeft = (cooldownTime - timeSinceLastBreak) / 1000;
            chatUtils.sendMessage(player, config.getString("Anti-CaneNuke.cooldown-message")
                        .replace("%time%", String.valueOf(timeLeft)));
            plugin.getLogger().info("Cooldown active for player: " + player.getName() + ", time left: " + timeLeft + " seconds.");
            event.setCancelled(true);
            return;
        }

        if ((int) (lastBreakTime == 0 ? 1 : (currentTime - lastBreakTime) / resetTime) > caneLimit) {
            chatUtils.sendMessage(player, config.getString("Anti-CaneNuke.limit-exceeded-message"));
            plugin.getLogger().info(player.getName() + " exceeded cane limit and has been put on cooldown.");
            event.setCancelled(true);
            return;
        }

        caneBreakTime.put(playerUUID, currentTime);
    }

    private boolean isActivated() {
        return config.getBoolean("Anti-CaneNuke.is-activated");
    }

    public void reloadSettings() {
        plugin.reloadConfig();
        if (isActivated()) {
            chatUtils.onReload("Anti-CaneNuke");
        } else {
            plugin.getLogger().info("Anti-CaneNuke is deactivated.");
        }
    }
}
