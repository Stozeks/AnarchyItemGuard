package me.stozeks.anarchyitemguard;

import org.bukkit.plugin.java.JavaPlugin;

public final class AnarchyItemGuardPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("AnarchyItemGuard has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnarchyItemGuard has been ondisable.");

    }
}


