package me.zyromate.zyrostaffutils;

import me.zyromate.zyrostaffutils.Cmds.ReloadCommand;
import me.zyromate.zyrostaffutils.Cmds.StaffChatCommand;
import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
import me.zyromate.zyrostaffutils.Cmds.HeadRotateCommand;
import me.zyromate.zyrostaffutils.Listeners.CaneBreakListener;
import me.zyromate.zyrostaffutils.Listeners.FlyBoostLimiter;
import me.zyromate.zyrostaffutils.Listeners.StaffChatListener;
import me.zyromate.zyrostaffutils.Listeners.YPositionCheckListener;
import me.zyromate.zyrostaffutils.Listeners.SoftFreezeListener;
import me.zyromate.zyrostaffutils.Listeners.CommandListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZyroStaffUtils extends JavaPlugin {

    private StaffChatListener staffChatListener;
    private CaneBreakListener caneBreakListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        logPluginStatus("enabled");
    }

    @Override
    public void onDisable() {
        if (caneBreakListener != null) {
            caneBreakListener.shutdown();
        }
        logPluginStatus("disabled");
    }

    private void registerCommands() {
        getCommand("zyrostaffutils").setExecutor(new ReloadCommand(this));
        getCommand("staffchat").setExecutor(new StaffChatCommand(this));
        getCommand("sc").setExecutor(new StaffChatCommand(this));
        getCommand("sct").setExecutor(new StaffChatCommand(this));
        getCommand("softfreeze").setExecutor(new SoftFreezeCommand(this));
        getCommand("headrotate").setExecutor(new HeadRotateCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new YPositionCheckListener(this), this);
        getServer().getPluginManager().registerEvents(new FlyBoostLimiter(this), this);
        getServer().getPluginManager().registerEvents(new SoftFreezeListener(new SoftFreezeCommand(this)), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getServer().getPluginManager().registerEvents(caneBreakListener, this);
        getServer().getPluginManager().registerEvents(staffChatListener, this);

        staffChatListener = new StaffChatListener(this);
        caneBreakListener = new CaneBreakListener(this);

    }

    private void logPluginStatus(String status) {
        getLogger().info("---------------------------");
        getLogger().info("Author: Zyromate");
        getLogger().info("Plugin: ZyroStaffUtils");
        getLogger().info("Status: " + status);
        getLogger().info("---------------------------");
    }

    // Getter for StaffChatListener
    public StaffChatListener getStaffChatListener() {
        return staffChatListener;
    }
}
