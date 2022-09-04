package me.kafein.elitegenerator.hook.skyblock.impl;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.events.*;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class ASkyBlockHook extends SkyBlockHook {

    final private ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();

    @Override
    public World getIslandWorld() {
        return aSkyBlockAPI.getIslandWorld();
    }

    @Override
    public boolean hasIsland(UUID playerUUID) {
        return aSkyBlockAPI.hasIsland(playerUUID);
    }

    @Override
    public Location getIslandCenterLocation(final UUID playerUUID) {
        return aSkyBlockAPI.getIslandOwnedBy(playerUUID).getCenter();
    }

    @Override
    public long getIslandLevel(UUID playerUUID) {
        return aSkyBlockAPI.getIslandLevel(playerUUID);
    }

    @Override
    public UUID getIslandOwner(Location location) {
        Island island = aSkyBlockAPI.getIslandAt(location);
        if (island == null) return null;
        return island.getOwner();
    }

    @Override
    public List<UUID> getIslandMembers(UUID playerUUID) {
        return aSkyBlockAPI.getIslandOwnedBy(playerUUID).getMembers();
    }

    @Override
    public List<UUID> getIslandMembers(Location location) {
        Island island = aSkyBlockAPI.getIslandAt(location);
        return island.getMembers();
    }

    @EventHandler
    public void onDelete(final IslandDeleteEvent e) {

        if (!getGeneratorManager().containsGeneratorIslandLocation(e.getLocation())) return;

        for (UUID uuid : getGeneratorManager().getGenerators(e.getLocation())) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onReset(final IslandResetEvent e) {

        if (!getGeneratorManager().containsGeneratorIslandLocation(e.getLocation())) return;

        for (UUID uuid : getGeneratorManager().getGenerators(e.getLocation())) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onChangeOwner(final IslandChangeOwnerEvent e) {

        if (!getGeneratorManager().containsGeneratorIslandLocation(e.getIslandLocation())) return;

        for (UUID uuid : getGeneratorManager().getGenerators(e.getIslandLocation())) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            generator.changeOwnerUUID(e.getNewOwner());

        }

    }

    @EventHandler
    public void onJoin(final TeamJoinEvent e) {

        final UUID playerUUID = e.getPlayer();

        final Location location = aSkyBlockAPI.getIslandLocation(e.getNewTeamLeader());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.addGeneratorMember(new GeneratorMember(playerUUID));

        }

    }

    @EventHandler
    public void onLeave(final TeamLeaveEvent e) {

        final UUID playerUUID = e.getPlayer();

        if (playerUUID.equals(e.getOldTeamLeader())) return;

        final Location location = aSkyBlockAPI.getIslandLocation(e.getOldTeamLeader());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            if (!generator.containsGeneratorMember(playerUUID)) continue;

            generator.removeGeneratorMember(playerUUID);

        }

    }

}
