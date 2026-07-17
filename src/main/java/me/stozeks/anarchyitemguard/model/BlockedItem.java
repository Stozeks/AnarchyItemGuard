package me.stozeks.anarchyitemguard.model;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockedItem {

    private final String id;
    private final Material material;
    private final String displayName;
    private final List<String> lore;

    public BlockedItem(
            String id,
            Material material,
            String displayName,
            List<String> lore
    ) {
        this.id = id;
        this.material = material;
        this.displayName = translateColors(displayName);
        this.lore = translateColors(lore);
    }

    public boolean matches(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (itemStack.getType() != material) {
            return false;
        }

        boolean requiresDisplayName =
                displayName != null && !displayName.isEmpty();

        boolean requiresLore =
                lore != null && !lore.isEmpty();

        if (!requiresDisplayName && !requiresLore) {
            return true;
        }

        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        if (requiresDisplayName) {
            if (!itemMeta.hasDisplayName()) {
                return false;
            }

            if (!displayName.equals(itemMeta.getDisplayName())) {
                return false;
            }
        }

        if (requiresLore) {
            if (!itemMeta.hasLore()) {
                return false;
            }

            List<String> itemLore = itemMeta.getLore();

            if (itemLore == null || !lore.equals(itemLore)) {
                return false;
            }
        }

        return true;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    private String translateColors(String text) {
        if (text == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private List<String> translateColors(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> translatedLines = new ArrayList<>();

        for (String line : lines) {
            translatedLines.add(translateColors(line));
        }

        return Collections.unmodifiableList(translatedLines);
    }
}