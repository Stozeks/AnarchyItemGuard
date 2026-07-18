package me.stozeks.anarchyitemguard.util;

import org.bukkit.plugin.java.JavaPlugin;

public class DebugLogger {

    private final JavaPlugin plugin;

    public DebugLogger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void debug(String message) {
        if (!plugin.getConfig().getBoolean("debug", false)) {
            return;
        }

        plugin.getLogger().info("[DEBUG] " + message);
    }
}