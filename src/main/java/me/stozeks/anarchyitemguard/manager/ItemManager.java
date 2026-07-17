package me.stozeks.anarchyitemguard.manager;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.model.BlockedItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

            Material material = loadMaterial(itemId, itemSection);

            if (material == null) {
                continue;
            }

            String displayName =
                    itemSection.getString("display-name");

            List<String> lore =
                    itemSection.getStringList("lore");

            NamespacedKey persistentDataKey =
                    loadPersistentDataKey(itemId, itemSection);

            String persistentDataValue =
                    itemSection.getString("pdc-value");

            BlockedItem blockedItem = new BlockedItem(
                    itemId,
                    material,
                    displayName,
                    lore,
                    persistentDataKey,
                    persistentDataValue
            );

            blockedItems.add(blockedItem);
        }

        plugin.getLogger().info(
                "Загружено запрещённых предметов: "
                        + blockedItems.size()
        );
    }

    private Material loadMaterial(
            String itemId,
            ConfigurationSection itemSection
    ) {
        String materialName =
                itemSection.getString("material");

        if (materialName == null || materialName.trim().isEmpty()) {
            plugin.getLogger().warning(
                    "У предмета " + itemId + " не указан material."
            );

            return null;
        }

        Material material =
                Material.matchMaterial(materialName);

        if (material == null) {
            plugin.getLogger().warning(
                    "Неизвестный material у предмета "
                            + itemId
                            + ": "
                            + materialName
            );
        }

        return material;
    }

    private NamespacedKey loadPersistentDataKey(
            String itemId,
            ConfigurationSection itemSection
    ) {
        String configuredKey =
                itemSection.getString("pdc-key");

        if (configuredKey == null || configuredKey.trim().isEmpty()) {
            return null;
        }

        String[] keyParts = configuredKey
                .trim()
                .toLowerCase(Locale.ROOT)
                .split(":", 2);

        if (keyParts.length != 2
                || keyParts[0].isEmpty()
                || keyParts[1].isEmpty()) {
            plugin.getLogger().warning(
                    "Некорректный pdc-key у предмета "
                            + itemId
                            + ": "
                            + configuredKey
                            + ". Используйте формат namespace:key."
            );

            return null;
        }

        try {
            return new NamespacedKey(keyParts[0], keyParts[1]);
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning(
                    "Недопустимый pdc-key у предмета "
                            + itemId
                            + ": "
                            + configuredKey
            );

            return null;
        }
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