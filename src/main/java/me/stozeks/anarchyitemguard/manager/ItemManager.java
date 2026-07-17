package me.stozeks.anarchyitemguard.manager;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ItemManager {

    private final AnarchyItemGuardPlugin plugin;
    private final Set<Material> blockedItems = EnumSet.noneOf(Material.class);

    public ItemManager(AnarchyItemGuardPlugin plugin) {
        this.plugin = plugin;
        loadBlockedItems();
    }

    private void loadBlockedItems() {
        blockedItems.clear();

        List<String> configuredItems =
                plugin.getConfig().getStringList("blocked-items");

        for (String itemName : configuredItems) {
            Material material = Material.matchMaterial(itemName);

            if (material == null) {
                plugin.getLogger().warning(
                        "Неизвестный материал в config.yml: " + itemName
                );

                continue;
            }

            blockedItems.add(material);
        }

        plugin.getLogger().info(
                "Загружено запрещённых предметов: " + blockedItems.size()
        );
    }

    public boolean isBlocked(Material material) {
        return blockedItems.contains(material);
    }

    public void reload() {
        loadBlockedItems();
    }
}