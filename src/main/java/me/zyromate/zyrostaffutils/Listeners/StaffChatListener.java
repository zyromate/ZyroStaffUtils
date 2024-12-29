package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class StaffChatListener implements Listener {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;
    private final Set<Player> staffChatToggledPlayers;
    private FileConfiguration config;

    public StaffChatListener(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
        this.staffChatToggledPlayers = new HashSet<>();
        chatUtils.sendInitialization("Staff-Chat");
    }

    public boolean toggleStaffChat(Player player) {
        if (!plugin.getConfig().getBoolean("Staff-Chat.is-activated")) {
            System.out.println("Unknown Command type" + "help" + "for more help");
            return false;
        }

        if (staffChatToggledPlayers.contains(player)) {
            staffChatToggledPlayers.remove(player);
            return false;
        } else {
            staffChatToggledPlayers.add(player);
            return true;
        }
    }

    public boolean isStaffChatToggled(Player player) {
        return staffChatToggledPlayers.contains(player);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isStaffChatToggled(player)) {
            event.setCancelled(true);

            String staffChatPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff-Chat.prefix"));
            String formattedMessage = String.format("%s%s: %s", staffChatPrefix, player.getDisplayName(), event.getMessage());

            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("zyrostaffutils.staffchat.use")) {
                    chatUtils.sendMessage(onlinePlayer, formattedMessage);
                }
            }
        }
    }
    public void reloadSettings() {
        config = plugin.getConfig();
        staffChatToggledPlayers.clear();
            config = plugin.getConfig();
            if (!plugin.getConfig().getBoolean("Staff-Chat.is-activated")) return;
            chatUtils.onReload("Staff-Chat");
    }
}
