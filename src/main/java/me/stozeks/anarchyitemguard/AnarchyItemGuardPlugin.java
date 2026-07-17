package me.stozeks.anarchyitemguard;

import me.stozeks.anarchyitemguard.listener.PlayerInteractListener;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnarchyItemGuardPlugin extends JavaPlugin {

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        itemManager = new ItemManager(this);

        getServer().getPluginManager().registerEvents(
                new PlayerInteractListener(this, itemManager),
                this
        );

        getLogger().info("AnarchyItemGuard has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnarchyItemGuard has been disabled.");
    }
}
