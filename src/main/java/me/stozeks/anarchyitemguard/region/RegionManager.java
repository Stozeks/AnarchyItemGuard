package me.stozeks.anarchyitemguard.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RegionManager {

    public boolean isInsideForeignRegion(Player player, Location location) {
        RegionContainer container =
                WorldGuard.getInstance()
                        .getPlatform()
                        .getRegionContainer();

        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions =
                query.getApplicableRegions(BukkitAdapter.adapt(location));

        UUID playerUuid = player.getUniqueId();
        boolean foundProtectedRegion = false;

        for (ProtectedRegion region : regions) {
            if (region.getId().equalsIgnoreCase("__global__")) {
                continue;
            }

            foundProtectedRegion = true;

            boolean isOwner =
                    region.getOwners().contains(playerUuid);

            boolean isMember =
                    region.getMembers().contains(playerUuid);

            if (!isOwner && !isMember) {
                return true;
            }
        }

        return false;
    }
}