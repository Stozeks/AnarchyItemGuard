package me.stozeks.anarchyitemguard.listener;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import me.stozeks.anarchyitemguard.region.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        Material material = item.getType();

        if (!itemManager.isBlocked(material)) {
            return;
        }

        boolean insideForeignRegion =
                regionManager.isInsideForeignRegion(
                        event.getPlayer(),
                        event.getPlayer().getLocation()
                );

        if (!insideForeignRegion) {
            return;
        }

        event.setCancelled(true);

        String message = plugin.getConfig().getString(
                "messages.blocked-in-region",
                "&cВы не можете использовать этот предмет в чужом регионе."
        );

        event.getPlayer().sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }
}