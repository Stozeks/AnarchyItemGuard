package me.stozeks.anarchyitemguard.listener;

import me.stozeks.anarchyitemguard.core.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.service.ProtectionService;
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
    private final ProtectionService protectionService;

    public PlayerInteractListener(
            AnarchyItemGuardPlugin plugin,
            ProtectionService protectionService
    ) {
        this.plugin = plugin;
        this.protectionService = protectionService;
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
        Location interactionLocation = getInteractionLocation(event);

        boolean shouldBlock =
                protectionService.shouldBlockItemUse(
                        event.getPlayer(),
                        item,
                        interactionLocation
                );

        if (!shouldBlock) {
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