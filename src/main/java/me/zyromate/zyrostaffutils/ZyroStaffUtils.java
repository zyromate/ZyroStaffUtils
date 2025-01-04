package me.zyromate.zyrostaffutils;

import me.zyromate.zyrostaffutils.Cmds.ReloadCommand;
import me.zyromate.zyrostaffutils.Cmds.StaffChatCommand;
import me.zyromate.zyrostaffutils.Cmds.SoftFreezeCommand;
import me.zyromate.zyrostaffutils.Listeners.CaneBreakListener;
import me.zyromate.zyrostaffutils.Listeners.FlyBoostLimiter;
import me.zyromate.zyrostaffutils.Listeners.StaffChatListener;
import me.zyromate.zyrostaffutils.Listeners.YPositionCheckListener;
import me.zyromate.zyrostaffutils.Listeners.SoftFreezeListener;
import me.zyromate.zyrostaffutils.Listeners.CommandListener;
import me.zyromate.zyrostaffutils.Cmds.HeadRotateCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ZyroStaffUtils extends JavaPlugin {
    // Managers
    private HeadRotateCommand headRotateManager;
    private CommandListener antiSkilledManager;
    private SoftFreezeCommand softFreezeCommand;

    // Listeners
    private YPositionCheckListener yPositionCheckListener;
    private FlyBoostLimiter flyBoostLimiter;
    private StaffChatListener staffChatListener;
    private CaneBreakListener caneBreakListener;
    private SoftFreezeListener softFreezeListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerComponents();
        logPluginStatus("enabled");
    }

    @Override
    public void onDisable() {
        logPluginStatus("disabled");
    }

    private void registerComponents() {
        // Initialize managers
        headRotateManager = new HeadRotateCommand(this);
        headRotateManager.init();

        antiSkilledManager = new CommandListener(this);
        antiSkilledManager.init();

        softFreezeCommand = new SoftFreezeCommand(this);

        // Register commands
        StaffChatCommand staffChatCommand = new StaffChatCommand(this);
        getCommand("zyrostaffutils").setExecutor(new ReloadCommand(this));
        getCommand("staffchat").setExecutor(staffChatCommand);
        getCommand("sc").setExecutor(staffChatCommand);
        getCommand("sct").setExecutor(staffChatCommand);
        getCommand("softfreeze").setExecutor(softFreezeCommand);

        // Initialize listeners
        yPositionCheckListener = new YPositionCheckListener(this);
        flyBoostLimiter = new FlyBoostLimiter(this);
        staffChatListener = new StaffChatListener(this);
        caneBreakListener = new CaneBreakListener(this);
        softFreezeListener = new SoftFreezeListener(softFreezeCommand);

        // Register listeners
        getServer().getPluginManager().registerEvents(yPositionCheckListener, this);
        getServer().getPluginManager().registerEvents(flyBoostLimiter, this);
        getServer().getPluginManager().registerEvents(staffChatListener, this);
        getServer().getPluginManager().registerEvents(caneBreakListener, this);
        getServer().getPluginManager().registerEvents(softFreezeListener, this);
    }

    private void logPluginStatus(String status) {
        getLogger().info("---------------------------");
        getLogger().info("Author: Zyromate");
        getLogger().info("Plugin: ZyroStaffUtils");
        getLogger().info("Status: " + status);
        getLogger().info("---------------------------");
    }

    // Getter methods for accessing managers and listeners
    public CommandListener getAntiSkilledManager() {
        return antiSkilledManager;
    }

    public HeadRotateCommand getHeadRotateManager() {
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

    public SoftFreezeCommand getSoftFreezeCommand() {
        return softFreezeCommand;
    }
}