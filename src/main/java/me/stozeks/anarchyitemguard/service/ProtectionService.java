package me.stozeks.anarchyitemguard.service;

import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtectionService {

    private static final String BYPASS_PERMISSION =
            "anarchyitemguard.bypass";

    private final ItemManager itemManager;
    private final RegionManager regionManager;

    public ProtectionService(
            ItemManager itemManager,
            RegionManager regionManager
    ) {
        this.itemManager = itemManager;
        this.regionManager = regionManager;
    }

    public boolean shouldBlockItemUse(
            Player player,
            ItemStack item,
            Location interactionLocation
    ) {
        if (player == null
                || item == null
                || interactionLocation == null) {
            return false;
        }

        if (player.hasPermission(BYPASS_PERMISSION)) {
            return false;
        }

        if (!itemManager.isBlocked(item)) {
            return false;
        }

        return regionManager.isInsideForeignRegion(
                player,
                interactionLocation
        );
    }
}