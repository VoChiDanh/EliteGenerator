package me.kafein.elitegenerator.hook.skyblock.impl;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.config.SettingsManager;
import com.bgsoftware.superiorskyblock.api.events.*;
import com.bgsoftware.superiorskyblock.api.handlers.GridManager;
import com.bgsoftware.superiorskyblock.api.handlers.PlayersManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuperiorSkyBlockHook extends SkyBlockHook {

    final private PlayersManager playersManager = SuperiorSkyblockAPI.getSuperiorSkyblock().getPlayers();
    final private SettingsManager settingsManager = SuperiorSkyblockAPI.getSuperiorSkyblock().getSettings();
    private final GridManager gridManager = SuperiorSkyblockAPI.getSuperiorSkyblock().getGrid();
    final private String worldName = settingsManager.getWorlds().getWorldName();

    @Override
    public World getIslandWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public boolean hasIsland(UUID playerUUID) {
        return playersManager.getSuperiorPlayer(playerUUID).hasIsland();
    }

    @Override
    public Location getIslandCenterLocation(UUID playerUUID) {
        final Island island = playersManager.getSuperiorPlayer(playerUUID).getIsland();
        return island.getCenter(World.Environment.NORMAL).getBlock().getLocation();
    }

    @Override
    public long getIslandLevel(UUID playerUUID) {
        final Island island = playersManager.getSuperiorPlayer(playerUUID).getIsland();
        return island.getIslandLevel().longValue();
    }

    @Override
    public UUID getIslandOwner(Location location) {
        Island island = gridManager.getIslandAt(location);
        if (island == null) return null;
        return island.getOwner().getUniqueId();
    }

    @Override
    public List<UUID> getIslandMembers(UUID playerUUID) {
        final Island island = playersManager.getSuperiorPlayer(playerUUID).getIsland();
        final List<UUID> memberList = new ArrayList<>();
        island.getIslandMembers(true).forEach(member -> memberList.add(member.getUniqueId()));
        return memberList;
    }

    @EventHandler
    public void onDelete(final IslandDisbandEvent e) {

        final Location location = e.getIsland().getCenter(World.Environment.NORMAL).getBlock().getLocation();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onChangeOwner(final IslandTransferEvent e) {

        final Location location = e.getIsland().getCenter(World.Environment.NORMAL).getBlock().getLocation();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            generator.changeOwnerUUID(e.getNewOwner().getUniqueId());

        }

    }

    @EventHandler
    public void onJoin(final IslandJoinEvent e) {

        final UUID playerUUID = e.getPlayer().getUniqueId();

        final Location location = e.getIsland().getCenter(World.Environment.NORMAL).getBlock().getLocation();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.addGeneratorMember(new GeneratorMember(playerUUID));

        }

    }

    @EventHandler
    public void onLeave(final IslandQuitEvent e) {

        final UUID playerUUID = e.getPlayer().getUniqueId();

        final Location location = e.getIsland().getCenter(World.Environment.NORMAL).getBlock().getLocation();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

    @EventHandler
    public void onKick(final IslandKickEvent e) {

        final UUID playerUUID = e.getTarget().getUniqueId();

        final Location location = e.getIsland().getCenter(World.Environment.NORMAL).getBlock().getLocation();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

}
