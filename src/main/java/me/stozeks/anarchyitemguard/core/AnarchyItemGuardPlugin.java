package me.stozeks.anarchyitemguard.core;

import me.stozeks.anarchyitemguard.command.AigCommand;
import me.stozeks.anarchyitemguard.listener.PlayerInteractListener;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import me.stozeks.anarchyitemguard.service.ProtectionService;
import me.stozeks.anarchyitemguard.util.DebugLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnarchyItemGuardPlugin extends JavaPlugin {

    private ItemManager itemManager;
    private RegionManager regionManager;
    private ProtectionService protectionService;
    private DebugLogger debugLogger;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        itemManager = new ItemManager(this);
        regionManager = new RegionManager();
        debugLogger = new DebugLogger(this);
        protectionService = new ProtectionService(
                itemManager,
                regionManager
        );

        registerListeners();
        registerCommands();

        getLogger().info("AnarchyItemGuard enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnarchyItemGuard disabled.");
    }

    private void registerListeners() {
        PlayerInteractListener playerInteractListener =
                new PlayerInteractListener(
                        this,
                        protectionService
                );

        getServer()
                .getPluginManager()
                .registerEvents(playerInteractListener, this);
    }

    private void registerCommands() {
        PluginCommand aigCommand = getCommand("aig");

        if (aigCommand == null) {
            getLogger().severe(
                    "Command /aig is missing from plugin.yml."
            );

            getServer()
                    .getPluginManager()
                    .disablePlugin(this);

            return;
        }

        AigCommand commandHandler =
                new AigCommand(this, itemManager);

        aigCommand.setExecutor(commandHandler);
        aigCommand.setTabCompleter(commandHandler);
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public ProtectionService getProtectionService() {
        return protectionService;
    }
    public DebugLogger getDebugLogger() {
        return debugLogger;
    }
}
