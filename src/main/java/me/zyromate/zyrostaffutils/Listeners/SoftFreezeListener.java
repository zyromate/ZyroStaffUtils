package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
import me.zyromate.zyrostaffutils.ZyroStaffUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SoftFreezeListener implements Listener {

    private final SoftFreezeCommand softFreezeCommand;

    public SoftFreezeListener(SoftFreezeCommand softFreezeCommand) {
        this.softFreezeCommand = softFreezeCommand;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        softFreezeCommand.handlePlayerLogout(event.getPlayer());
    }
    public SoftFreezeListener(ZyroStaffUtils plugin) {
        this.softFreezeCommand = new SoftFreezeCommand(plugin);
    }

}