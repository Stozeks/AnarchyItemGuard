package me.stozeks.anarchyitemguard;

import me.stozeks.anarchyitemguard.listener.PlayerInteractListener;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnarchyItemGuardPlugin extends JavaPlugin {

    private ItemManager itemManager;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        itemManager = new ItemManager(this);
        regionManager = new RegionManager();

        PlayerInteractListener playerInteractListener =
                new PlayerInteractListener(
                        this,
                        itemManager,
                        regionManager
                );

        getServer()
                .getPluginManager()
                .registerEvents(playerInteractListener, this);

        getLogger().info("AnarchyItemGuard успешно включён.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnarchyItemGuard выключен.");
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}