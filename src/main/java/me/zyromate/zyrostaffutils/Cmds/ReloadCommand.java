package me.zyromate.zyrostaffutils.Cmds;

import me.zyromate.zyrostaffutils.Listeners.CaneBreakListener;
import me.zyromate.zyrostaffutils.Listeners.FlyBoostLimiter;
import me.zyromate.zyrostaffutils.Listeners.StaffChatListener;
import me.zyromate.zyrostaffutils.Listeners.YPositionCheckListener;
import me.zyromate.zyrostaffutils.Managers.HeadRotate;
import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.Managers.AntiSkilled;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final ZyroStaffUtils plugin;
    private final AntiSkilled antiSkilled;
    private final HeadRotate headRotate;
    private final YPositionCheckListener yPositionCheckListener;
    private final FlyBoostLimiter flyBoostLimiter;
    private final StaffChatCommand staffChatCommand;
    private final StaffChatListener staffChatListener;
    private final CaneBreakListener caneBreakListener;
    private final ChatUtils chatUtils;

    public ReloadCommand(ZyroStaffUtils plugin, AntiSkilled antiSkilled, HeadRotate headRotate,
                         YPositionCheckListener yPositionCheckListener, FlyBoostLimiter flyBoostLimiter,
                         StaffChatCommand staffChatCommand, StaffChatListener staffChatListener,
                         CaneBreakListener caneBreakListener) {
        this.plugin = plugin;
        this.antiSkilled = antiSkilled;
        this.headRotate = headRotate;
        this.yPositionCheckListener = yPositionCheckListener;
        this.flyBoostLimiter = flyBoostLimiter;
        this.staffChatCommand = staffChatCommand;
        this.staffChatListener = staffChatListener;
        this.caneBreakListener = caneBreakListener;
        this.chatUtils = new ChatUtils(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("zyrostaffutils.reload")) {
                String noPerm = plugin.getConfig().getString("invalid-permission");
                chatUtils.sendMessage(player, noPerm);
                return true;
            }

            plugin.reloadConfig();
            antiSkilled.reloadSettings();            // Reload AntiSkilled settings
            headRotate.reloadSettings();             // Reload HeadRotate settings
            yPositionCheckListener.reloadSettings(); // Reload YPositionCheckListener (Anti-Void)
            flyBoostLimiter.reloadSettings();        // Reload FlyBoostLimiter settings
            staffChatCommand.reloadSettings();       // Reload StaffChatCommand
            staffChatListener.reloadSettings();      // Reload StaffChatListener
            caneBreakListener.reloadSettings();      // Reload CaneBreakListener (Anti-CaneNuke)

            String reloadMessage = plugin.getConfig().getString("plugin-reload");
            chatUtils.sendMessage(player, reloadMessage);
        } else {
            plugin.reloadConfig();
            antiSkilled.reloadSettings();            // Reload AntiSkilled settings
            headRotate.reloadSettings();             // Reload HeadRotate settings
            yPositionCheckListener.reloadSettings(); // Reload YPositionCheckListener (Anti-Void)
            flyBoostLimiter.reloadSettings();        // Reload FlyBoostLimiter settings
            staffChatCommand.reloadSettings();       // Reload StaffChatCommand
            staffChatListener.reloadSettings();      // Reload StaffChatListener
            caneBreakListener.reloadSettings();      // Reload CaneBreakListener (Anti-CaneNuke)

            sender.sendMessage("ZyroStaffUtils has been reloaded.");
        }

        return true;
    }
}

