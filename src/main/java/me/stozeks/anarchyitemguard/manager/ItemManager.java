package me.stozeks.anarchyitemguard.manager;

import me.stozeks.anarchyitemguard.core.AnarchyItemGuardPlugin;
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
        reload();
    }

    public boolean reload() {
        ConfigurationSection blockedItemsSection =
                plugin.getConfig().getConfigurationSection("blocked-items");

        if (blockedItemsSection == null) {
            plugin.getLogger().severe(
                    "The blocked-items section is missing from config.yml."
            );

            return false;
        }

        List<BlockedItem> loadedItems = new ArrayList<>();

        for (String itemId : blockedItemsSection.getKeys(false)) {
            BlockedItem blockedItem =
                    loadBlockedItem(itemId, blockedItemsSection);

            if (blockedItem != null) {
                loadedItems.add(blockedItem);
            }
        }

        blockedItems.clear();
        blockedItems.addAll(loadedItems);

        plugin.getLogger().info(
                "Loaded " + blockedItems.size() + " blocked item(s)."
        );

        return true;
    }

    private BlockedItem loadBlockedItem(
            String itemId,
            ConfigurationSection blockedItemsSection
    ) {
        ConfigurationSection itemSection =
                blockedItemsSection.getConfigurationSection(itemId);

        if (itemSection == null) {
            plugin.getLogger().warning(
                    "Could not read blocked item section: " + itemId
            );

            return null;
        }

        Material material = loadMaterial(itemId, itemSection);

        if (material == null) {
            return null;
        }

        String displayName =
                itemSection.getString("display-name");

        List<String> lore =
                itemSection.getStringList("lore");

        String configuredPdcKey =
                itemSection.getString("pdc-key");

        String persistentDataValue =
                itemSection.getString("pdc-value");

        boolean hasPdcKey = isConfigured(configuredPdcKey);
        boolean hasPdcValue = isConfigured(persistentDataValue);

        if (hasPdcKey != hasPdcValue) {
            plugin.getLogger().warning(
                    "Blocked item '" + itemId
                            + "' must define both pdc-key and pdc-value."
            );

            return null;
        }

        NamespacedKey persistentDataKey = null;

        if (hasPdcKey) {
            persistentDataKey =
                    loadPersistentDataKey(itemId, configuredPdcKey);

            if (persistentDataKey == null) {
                return null;
            }
        }

        return new BlockedItem(
                itemId,
                material,
                displayName,
                lore,
                persistentDataKey,
                persistentDataValue
        );
    }

    private Material loadMaterial(
            String itemId,
            ConfigurationSection itemSection
    ) {
        String materialName =
                itemSection.getString("material");

        if (!isConfigured(materialName)) {
            plugin.getLogger().warning(
                    "Blocked item '" + itemId
                            + "' does not define a material."
            );

            return null;
        }

        Material material =
                Material.matchMaterial(
                        materialName.trim().toUpperCase(Locale.ROOT)
                );

        if (material == null) {
            plugin.getLogger().warning(
                    "Unknown material for blocked item '"
                            + itemId
                            + "': "
                            + materialName
            );
        }

        return material;
    }

    private NamespacedKey loadPersistentDataKey(
            String itemId,
            String configuredKey
    ) {
        String normalizedKey =
                configuredKey.trim().toLowerCase(Locale.ROOT);

        String[] keyParts = normalizedKey.split(":", 2);

        if (keyParts.length != 2
                || keyParts[0].isEmpty()
                || keyParts[1].isEmpty()) {
            plugin.getLogger().warning(
                    "Invalid pdc-key for blocked item '"
                            + itemId
                            + "': "
                            + configuredKey
                            + ". Expected namespace:key."
            );

            return null;
        }

        try {
            return new NamespacedKey(keyParts[0], keyParts[1]);
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning(
                    "Unsupported pdc-key for blocked item '"
                            + itemId
                            + "': "
                            + configuredKey
            );

            return null;
        }
    }

    private boolean isConfigured(String value) {
        return value != null && !value.trim().isEmpty();
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
        return Collections.unmodifiableList(
                new ArrayList<>(blockedItems)
        );
    }
}