package me.zyromate.zyrostaffutils;

import me.zyromate.zyrostaffutils.Cmds.ReloadCommand;
import me.zyromate.zyrostaffutils.Cmds.StaffChatCommand;
import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
import me.zyromate.zyrostaffutils.Listeners.CaneBreakListener;
import me.zyromate.zyrostaffutils.Listeners.FlyBoostLimiter;
import me.zyromate.zyrostaffutils.Listeners.StaffChatListener;
import me.zyromate.zyrostaffutils.Listeners.YPositionCheckListener;
import me.zyromate.zyrostaffutils.Listeners.SoftFreezeListener;
import me.zyromate.zyrostaffutils.Managers.AntiSkilled;
import me.zyromate.zyrostaffutils.Managers.HeadRotate;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZyroStaffUtils extends JavaPlugin {

    private HeadRotate headRotateManager;
    private AntiSkilled antiSkilledManager;
    private YPositionCheckListener yPositionCheckListener;
    private FlyBoostLimiter flyBoostLimiter;
    private StaffChatListener staffChatListener;
    private CaneBreakListener caneBreakListener;
    private SoftFreezeListener softFreezeListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerManagers();
        registerListeners();
        registerCommands();

        logPluginStatus("enabled");
    }

    @Override
    public void onDisable() {
        logPluginStatus("disabled");
    }

    private void registerManagers() {
        headRotateManager = new HeadRotate(this);
        headRotateManager.init();

        antiSkilledManager = new AntiSkilled(this);
        antiSkilledManager.init();
    }

    private void registerListeners() {
        yPositionCheckListener = new YPositionCheckListener(this);
        flyBoostLimiter = new FlyBoostLimiter(this);
        staffChatListener = new StaffChatListener(this);
        caneBreakListener = new CaneBreakListener(this);
        softFreezeListener = new SoftFreezeListener(this);

        getServer().getPluginManager().registerEvents(yPositionCheckListener, this);
        getServer().getPluginManager().registerEvents(flyBoostLimiter, this);
        getServer().getPluginManager().registerEvents(staffChatListener, this);
        getServer().getPluginManager().registerEvents(caneBreakListener, this);
        getServer().getPluginManager().registerEvents(softFreezeListener, this);
    }

    private void registerCommands() {
        StaffChatCommand staffChatCommand = new StaffChatCommand(this);
        SoftFreezeCommand softFreezeCommand = new SoftFreezeCommand(this);

        getCommand("zyrostaffutils").setExecutor(
                new ReloadCommand(this, antiSkilledManager, headRotateManager, yPositionCheckListener, flyBoostLimiter,
                        staffChatCommand, staffChatListener, caneBreakListener));

        getCommand("staffchat").setExecutor(staffChatCommand);
        getCommand("sc").setExecutor(staffChatCommand);
        getCommand("sct").setExecutor(staffChatCommand);
        getCommand("softfreeze").setExecutor(softFreezeCommand);
    }

    private void logPluginStatus(String status) {
        getLogger().info("---------------------------");
        getLogger().info("Author: Zyromate");
        getLogger().info("Plugin: ZyroStaffUtils");
        getLogger().info("Status: " + status);
        getLogger().info("---------------------------");
    }

    public AntiSkilled getAntiSkilledManager() {
        return antiSkilledManager;
    }

    public HeadRotate getHeadRotateManager() {
        return headRotateManager;
    }

    public YPositionCheckListener getYPositionCheckListener() {
        return yPositionCheckListener;
    }

    public FlyBoostLimiter getFlyBoostLimiter() {
        return flyBoostLimiter;
    }

    public StaffChatListener getStaffChatListener() {
        return staffChatListener;
    }

    public CaneBreakListener getCaneBreakListener() {
        return caneBreakListener;
    }

    public SoftFreezeListener getSoftFreezeListener() {
        return softFreezeListener;
    }
}
