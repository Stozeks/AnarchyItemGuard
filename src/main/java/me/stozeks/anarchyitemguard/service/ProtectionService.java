package me.stozeks.anarchyitemguard.service;

import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import me.stozeks.anarchyitemguard.util.DebugLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtectionService {

    private static final String BYPASS_PERMISSION =
            "anarchyitemguard.bypass";

    private final ItemManager itemManager;
    private final RegionManager regionManager;
    private final DebugLogger debugLogger;

    public ProtectionService(
            ItemManager itemManager,
            RegionManager regionManager,
            DebugLogger debugLogger
    ) {
        this.itemManager = itemManager;
        this.regionManager = regionManager;
        this.debugLogger = debugLogger;
    }

    public boolean shouldBlockItemUse(
            Player player,
            ItemStack item,
            Location interactionLocation
    ) {
        if (player == null
                || item == null
                || interactionLocation == null) {
            debugLogger.debug(
                    "Protection check skipped because required data was missing."
            );

            return false;
        }

        debugLogger.debug(
                "Checking item use for player "
                        + player.getName()
                        + " at "
                        + formatLocation(interactionLocation)
                        + "."
        );

        if (player.hasPermission(BYPASS_PERMISSION)) {
            debugLogger.debug(
                    "Player "
                            + player.getName()
                            + " has bypass permission. Interaction allowed."
            );

            return false;
        }

        if (!itemManager.isBlocked(item)) {
            debugLogger.debug(
                    "Held item is not configured as blocked. Interaction allowed."
            );

            return false;
        }

        debugLogger.debug(
                "Held item matched a configured blocked item."
        );

        boolean insideForeignRegion =
                regionManager.isInsideForeignRegion(
                        player,
                        interactionLocation
                );

        if (!insideForeignRegion) {
            debugLogger.debug(
                    "Player is not inside a foreign region. Interaction allowed."
            );

            return false;
        }

        debugLogger.debug(
                "Player is inside a foreign region. Interaction blocked."
        );

        return true;
    }

    private String formatLocation(Location location) {
        String worldName =
                location.getWorld() == null
                        ? "unknown"
                        : location.getWorld().getName();

        return worldName
                + " ["
                + location.getBlockX()
                + ", "
                + location.getBlockY()
                + ", "
                + location.getBlockZ()
                + "]";
    }
}