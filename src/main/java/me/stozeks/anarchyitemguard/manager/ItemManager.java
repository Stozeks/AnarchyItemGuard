package me.stozeks.anarchyitemguard.manager;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import org.bukkit.Material;

import java.util.EnumSet;
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

        for (String materialName : plugin.getConfig().getStringList("blocked-items")) {
            Material material = Material.matchMaterial(materialName);

            if (material != null) {
                blockedItems.add(material);
            } else {
                plugin.getLogger().warning(
                        "Unknown material in config.yml: " + materialName
                );
            }
        }
    }

    public boolean isBlocked(Material material) {
        return blockedItems.contains(material);
    }
}

