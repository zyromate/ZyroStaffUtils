package me.zyromate.zyrostaffutils.Cmds;

import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    private final ZyroStaffUtils plugin;
    private final ChatUtils chatUtils;

    public StaffChatCommand(ZyroStaffUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("zyrostaffutils.staffchat.use")) {
            chatUtils.sendMessage(player, plugin.getConfig().getString("Staff-Chat.invalid-permission"));
            return true;
        }

        if (!plugin.getConfig().getBoolean("Staff-Chat.is-activated")) {
            chatUtils.sendMessage(player, ChatColor.RED + "Staff chat is currently disabled.");
            return true;
        }

        if (isStaffChatCommand(label)) {
            toggleStaffChat(player);
            return true;
        }

        return false;
    }

    private boolean isStaffChatCommand(String label) {
        return label.equalsIgnoreCase("staffchat") ||
                label.equalsIgnoreCase("sc") ||
                label.equalsIgnoreCase("sct");
    }

    private void toggleStaffChat(Player player) {
        boolean isToggled = plugin.getStaffChatListener().toggleStaffChat(player);
        String messageKey = isToggled ? "Staff-Chat.enabled" : "Staff-Chat.disabled";
        chatUtils.sendMessage(player, plugin.getConfig().getString(messageKey));
    }
}
