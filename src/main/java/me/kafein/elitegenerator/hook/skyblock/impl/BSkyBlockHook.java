package me.kafein.elitegenerator.hook.skyblock.impl;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResetEvent;
import world.bentobox.bentobox.api.events.team.TeamJoinEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.PlayersManager;
import world.bentobox.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BSkyBlockHook extends SkyBlockHook {

    final private String[] islandWorlds = {"bskyblock_world", "acidisland_world", "oneblock_world", "caveblock-world", "boxed_world"};
    final private PlayersManager playersManager = BentoBox.getInstance().getPlayers();
    final private IslandsManager islandsManager = BentoBox.getInstance().getIslandsManager();
    final private AddonsManager addonsManager = BentoBox.getInstance().getAddonsManager();

    @Override
    public World getIslandWorld() {
        for (String worldName : islandWorlds) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) return world;
        }
        return null;
    }

    @Override
    public boolean hasIsland(UUID playerUUID) {
        return islandsManager.hasIsland(getIslandWorld(), playerUUID);
    }

    @Override
    public Location getIslandCenterLocation(final UUID playerUUID) {
        return islandsManager.getIsland(getIslandWorld(), playersManager.getUser(playerUUID)).getCenter();
    }

    @Override
    public long getIslandLevel(UUID playerUUID) {
        return addonsManager.getAddonByName("Level").map(l -> ((Level) l).getIslandLevel(getIslandWorld(), playerUUID)).orElse(0L);
    }

    @Override
    public UUID getIslandOwner(Location location) {
        Optional<Island> island = islandsManager.getIslandAt(location);
        return island.map(Island::getOwner).orElse(null);
    }

    @Override
    public List<UUID> getIslandMembers(UUID playerUUID) {
        return new ArrayList<>(islandsManager.getIsland(getIslandWorld(), playersManager.getUser(playerUUID)).getMemberSet());
    }

    @EventHandler
    public void onDelete(final IslandDeleteEvent e) {

        final Location location = e.getIsland().getCenter();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onReset(final IslandResetEvent e) {

        final Location location = e.getOldIsland().getCenter();

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onChangeOwner(final TeamSetownerEvent e) {

        final Location location = getIslandCenterLocation(e.getNewOwner());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            generator.changeOwnerUUID(e.getNewOwner());

        }

    }

    @EventHandler
    public void onJoin(final TeamJoinEvent e) {

        final UUID playerUUID = e.getPlayerUUID();

        final Location location = getIslandCenterLocation(e.getOwner());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.addGeneratorMember(new GeneratorMember(playerUUID));

        }

    }

    @EventHandler
    public void onLeave(final TeamLeaveEvent e) {

        final UUID playerUUID = e.getPlayerUUID();

        final Location location = getIslandCenterLocation(e.getOwner());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

    @EventHandler
    public void onKick(final TeamKickEvent e) {

        final UUID playerUUID = e.getPlayerUUID();

        final Location location = getIslandCenterLocation(e.getOwner());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

}
