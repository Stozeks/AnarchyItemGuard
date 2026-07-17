package me.stozeks.anarchyitemguard.manager;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.model.BlockedItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager {

    private final AnarchyItemGuardPlugin plugin;
    private final List<BlockedItem> blockedItems = new ArrayList<>();

    public ItemManager(AnarchyItemGuardPlugin plugin) {
        this.plugin = plugin;
        loadBlockedItems();
    }

    private void loadBlockedItems() {
        blockedItems.clear();

        ConfigurationSection blockedItemsSection =
                plugin.getConfig().getConfigurationSection("blocked-items");

        if (blockedItemsSection == null) {
            plugin.getLogger().warning(
                    "Секция blocked-items отсутствует в config.yml."
            );

            return;
        }

        for (String itemId : blockedItemsSection.getKeys(false)) {
            ConfigurationSection itemSection =
                    blockedItemsSection.getConfigurationSection(itemId);

            if (itemSection == null) {
                plugin.getLogger().warning(
                        "Не удалось загрузить настройку предмета: " + itemId
                );

                continue;
            }

            String materialName =
                    itemSection.getString("material");

            if (materialName == null || materialName.trim().isEmpty()) {
                plugin.getLogger().warning(
                        "У предмета " + itemId + " не указан material."
                );

                continue;
            }

            Material material =
                    Material.matchMaterial(materialName);

            if (material == null) {
                plugin.getLogger().warning(
                        "Неизвестный материал у предмета "
                                + itemId
                                + ": "
                                + materialName
                );

                continue;
            }

            String displayName =
                    itemSection.getString("display-name");

            List<String> lore =
                    itemSection.getStringList("lore");

            BlockedItem blockedItem = new BlockedItem(
                    itemId,
                    material,
                    displayName,
                    lore
            );

            blockedItems.add(blockedItem);
        }

        plugin.getLogger().info(
                "Загружено запрещённых предметов: "
                        + blockedItems.size()
        );
    }

    public boolean isBlocked(ItemStack itemStack) {
        for (BlockedItem blockedItem : blockedItems) {
            if (blockedItem.matches(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public List<BlockedItem> getBlockedItems() {
        return Collections.unmodifiableList(blockedItems);
    }

    public void reload() {
        loadBlockedItems();
    }
}