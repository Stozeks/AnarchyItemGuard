package me.stozeks.anarchyitemguard.listener;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.manager.ItemManager;
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

    public PlayerInteractListener(
            AnarchyItemGuardPlugin plugin,
            ItemManager itemManager
    ) {
        this.plugin = plugin;
        this.itemManager = itemManager;
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

        event.setCancelled(true);

        String message = plugin.getConfig().getString(
                "messages.blocked-item",
                "&cЭтот предмет запрещено использовать."
        );

        event.getPlayer().sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }
}