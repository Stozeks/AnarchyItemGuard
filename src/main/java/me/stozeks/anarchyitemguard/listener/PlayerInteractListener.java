package me.stozeks.anarchyitemguard.listener;

import me.stozeks.anarchyitemguard.core.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final AnarchyItemGuardPlugin plugin;
    private final ItemManager itemManager;
    private final RegionManager regionManager;

    public PlayerInteractListener(
            AnarchyItemGuardPlugin plugin,
            ItemManager itemManager,
            RegionManager regionManager
    ) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || !itemManager.isBlocked(item)) {
            return;
        }

        Location interactionLocation =
                getInteractionLocation(event);

        boolean insideForeignRegion =
                regionManager.isInsideForeignRegion(
                        event.getPlayer(),
                        interactionLocation
                );

        if (!insideForeignRegion) {
            return;
        }

        event.setCancelled(true);

        String message = plugin.getConfig().getString(
                "messages.blocked-in-region",
                "&cYou cannot use this item inside another player's region."
        );

        event.getPlayer().sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }

    private Location getInteractionLocation(
            PlayerInteractEvent event
    ) {
        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock != null) {
            return clickedBlock.getLocation();
        }

        return event.getPlayer().getLocation();
    }
}