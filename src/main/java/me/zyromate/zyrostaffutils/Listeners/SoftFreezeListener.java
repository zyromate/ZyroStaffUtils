package me.zyromate.zyrostaffutils.Listeners;

import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
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
        if (!softFreezeCommand.getConfig().getBoolean("SoftFreeze.is-activated")) return;
        softFreezeCommand.handlePlayerLogout(event.getPlayer());
    }
}
