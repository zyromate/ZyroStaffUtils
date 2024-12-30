package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
import me.zyromate.zyrostaffutils.Utils.ChatUtils;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SoftFreezeListener implements Listener {

    private final SoftFreezeCommand softFreezeCommand;
    private final ChatUtils chatUtils;

    public SoftFreezeListener(ZyroStaffUtils plugin) {
        this.softFreezeCommand = new SoftFreezeCommand(plugin);
        this.chatUtils = new ChatUtils(plugin);
        logInitialization(plugin);
    }

    private void logInitialization(ZyroStaffUtils plugin) {
        if (!plugin.getConfig().getBoolean("softfreeze.is-activated")) return;
        chatUtils.sendInitialization("SoftFreezeListener");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!softFreezeCommand.getConfig().getBoolean("softfreeze.is-activated")) return;
        softFreezeCommand.handlePlayerLogout(event.getPlayer());
    }
}
