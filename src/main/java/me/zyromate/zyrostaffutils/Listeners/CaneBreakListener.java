package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class CaneBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final ChatUtils chatUtils;
    private final FileConfiguration config;
    private final ConcurrentHashMap<UUID, Long> caneBreakTime;   // Tracks the last break time of each player
    private final ConcurrentHashMap<UUID, Integer> caneBreakCount;  // Tracks the cane breaks within the limit
    private final ConcurrentHashMap<UUID, Long> cooldownStartTime; // Tracks when cooldown started for each player

    private final int cooldownTime;    // Cooldown time in milliseconds
    private final int caneLimit;       // Maximum allowed cane breaks
    private final int resetTime;       // Time after which cane count is reset (in milliseconds)
    private final String cooldownMessage;
    private final String limitExceededMessage;

    public CaneBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        this.config = plugin.getConfig();
        this.caneBreakTime = new ConcurrentHashMap<>();
        this.caneBreakCount = new ConcurrentHashMap<>();
        this.cooldownStartTime = new ConcurrentHashMap<>();
        this.cooldownTime = config.getInt("Anti-CaneNuke.cooldown-time") * 1000; // Convert seconds to milliseconds
        this.caneLimit = config.getInt("Anti-CaneNuke.cane-limit");
        this.resetTime = config.getInt("Anti-CaneNuke.reset-time") * 50; // Convert ticks to milliseconds (20 ticks = 1 second)
        this.cooldownMessage = config.getString("Anti-CaneNuke.cooldown-message", "You are on cooldown for %time% seconds.");
        this.limitExceededMessage = config.getString("Anti-CaneNuke.limit-exceeded-message", "You have exceeded the cane break limit!");

        if (isActivated()) {
            chatUtils.sendInitialization("Anti-CaneNuke");
        }
    }

    @EventHandler
    public void onCaneBroken(BlockBreakEvent event) {
        if (!isActivated()) return;
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material blockType = event.getBlock().getType();
        if (blockType != Material.SUGAR_CANE_BLOCK) return;
        long currentTime = System.currentTimeMillis();
        if (cooldownStartTime.containsKey(playerUUID)) {
            long timeOnCooldown = currentTime - cooldownStartTime.get(playerUUID);
            if (timeOnCooldown < cooldownTime) {
                long timeLeft = (cooldownTime - timeOnCooldown) / 1000; // Convert to seconds
                String message = cooldownMessage.replace("%time%", String.valueOf(timeLeft));
                chatUtils.sendMessage(player, message);
                event.setCancelled(true);
                return;
            } else {
                cooldownStartTime.remove(playerUUID);
                caneBreakCount.put(playerUUID, 0);
            }
        }
        long lastBreakTime = caneBreakTime.getOrDefault(playerUUID, 0L);
        if (currentTime - lastBreakTime > resetTime) {
            caneBreakCount.put(playerUUID, 0);
        }
        int currentBreakCount = caneBreakCount.getOrDefault(playerUUID, 0) + 1;
        caneBreakCount.put(playerUUID, currentBreakCount);
        caneBreakTime.put(playerUUID, currentTime);
        if (currentBreakCount > caneLimit) {
            cooldownStartTime.put(playerUUID, currentTime);
            chatUtils.sendMessage(player, limitExceededMessage);
            event.setCancelled(true);
            return;
        }
    }

    private boolean isActivated() {
        return config.getBoolean("Anti-CaneNuke.is-activated");
    }
}
