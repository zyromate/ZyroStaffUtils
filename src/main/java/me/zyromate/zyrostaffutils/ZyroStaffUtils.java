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

    // Commands
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
        reloadConfigWithUTF8();
        registerManagers();  // Initialize and configure managers
        registerCommands();  // Register commands
        registerListeners(); // Register event listeners

        logPluginStatus("enabled");
    }

    @Override
    public void onDisable() {
        logPluginStatus("disabled");
    }

    /**
     * Initializes and sets up all managers.
     */
    private void registerManagers() {
        // HeadRotate manager
        headRotateManager = new HeadRotateCommand(this);
        headRotateManager.init();

        // Command listener manager
        antiSkilledManager = new CommandListener(this);
        antiSkilledManager.init();

        // Shared SoftFreezeCommand instance
        softFreezeCommand = new SoftFreezeCommand(this);
    }

    /**
     * Registers all commands with their executors.
     */
    private void registerCommands() {
        StaffChatCommand staffChatCommand = new StaffChatCommand(this);

        // Register commands
        getCommand("zyrostaffutils").setExecutor(new ReloadCommand(this));
        getCommand("staffchat").setExecutor(staffChatCommand);
        getCommand("sc").setExecutor(staffChatCommand);
        getCommand("sct").setExecutor(staffChatCommand);
        getCommand("softfreeze").setExecutor(softFreezeCommand);
    }

    /**
     * Registers all listeners for handling events.
     */
    private void registerListeners() {
        // Listener initializations
        yPositionCheckListener = new YPositionCheckListener(this);
        flyBoostLimiter = new FlyBoostLimiter(this);
        staffChatListener = new StaffChatListener(this);
        caneBreakListener = new CaneBreakListener(this);
        softFreezeListener = new SoftFreezeListener(softFreezeCommand); // Pass shared command instance

        // Register events
        getServer().getPluginManager().registerEvents(yPositionCheckListener, this);
        getServer().getPluginManager().registerEvents(flyBoostLimiter, this);
        getServer().getPluginManager().registerEvents(staffChatListener, this);
        getServer().getPluginManager().registerEvents(caneBreakListener, this);
        getServer().getPluginManager().registerEvents(softFreezeListener, this);
    }

    /**
     * Logs the current plugin status (enabled/disabled).
     *
     * @param status The plugin status as a string.
     */
    private void logPluginStatus(String status) {
        getLogger().info("---------------------------");
        getLogger().info("Author: Zyromate");
        getLogger().info("Plugin: ZyroStaffUtils");
        getLogger().info("Status: " + status);
        getLogger().info("---------------------------");
    }

    private void reloadConfigWithUTF8() {
        try {
            // Read the config.yml as a string with UTF-8 encoding
            InputStream resourceStream = getResource("config.yml");
            if (resourceStream != null) {
                String configContent = readStreamAsUTF8(resourceStream);
                getConfig().loadFromString(configContent);
            } else {
                getLogger().warning("Could not find config.yml resource in the plugin jar.");
            }
        } catch (Exception e) {
            getLogger().warning("Failed to load config.yml with UTF-8 encoding. Falling back to default.");
            e.printStackTrace();
        }
    }

    private String readStreamAsUTF8(InputStream stream) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
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
