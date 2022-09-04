package me.kafein.elitegenerator.hook.skyblock.impl;

import com.songoda.skyblock.SkyBlock;
import com.songoda.skyblock.api.event.island.IslandDeleteEvent;
import com.songoda.skyblock.api.event.island.IslandKickEvent;
import com.songoda.skyblock.api.event.island.IslandOwnershipTransferEvent;
import com.songoda.skyblock.api.event.player.PlayerIslandJoinEvent;
import com.songoda.skyblock.api.event.player.PlayerIslandLeaveEvent;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandEnvironment;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.island.IslandWorld;
import com.songoda.skyblock.world.WorldManager;
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

public class FabledSkyBlockHook extends SkyBlockHook {

    final private IslandManager islandManager = SkyBlock.getInstance().getIslandManager();
    final private WorldManager worldManager = SkyBlock.getInstance().getWorldManager();

    @Override
    public World getIslandWorld() {
        return worldManager.getWorld(IslandWorld.Normal);
    }

    @Override
    public boolean hasIsland(UUID playerUUID) {
        return SkyBlock.getInstance().getVisitManager().hasIsland(playerUUID);
    }

    @Override
    public Location getIslandCenterLocation(UUID playerUUID) {
        return islandManager.getIsland(Bukkit.getOfflinePlayer(playerUUID)).getLocation(IslandWorld.Normal, IslandEnvironment.Island);
    }

    @Override
    public long getIslandLevel(UUID playerUUID) {
        return islandManager.getIsland(Bukkit.getOfflinePlayer(playerUUID)).getLevel().getLevel();
    }

    @Override
    public UUID getIslandOwner(Location location) {
        Island island = islandManager.getIslandAtLocation(location);
        if (island == null) return null;
        return island.getOwnerUUID();
    }

    @Override
    public List<UUID> getIslandMembers(UUID playerUUID) {
        final List<UUID> list = new ArrayList<>(islandManager.getIsland(Bukkit.getOfflinePlayer(playerUUID)).getCoopPlayers().keySet());
        list.add(playerUUID);
        return list;
    }

    @Override
    public List<UUID> getIslandMembers(Location location) {
        Island island = islandManager.getIslandAtLocation(location);
        final List<UUID> list = new ArrayList<>(island.getCoopPlayers().keySet());
        return list;
    }

    @EventHandler
    public void onDelete(final IslandDeleteEvent e) {

        final Location location = getIslandCenterLocation(e.getIsland().getOwnerUUID());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onChangeOwner(final IslandOwnershipTransferEvent e) {

        final Location location = getIslandCenterLocation(e.getIsland().getOwnerUUID());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            generator.changeOwnerUUID(e.getOwner().getUniqueId());

        }

    }

    @EventHandler
    public void onJoin(final PlayerIslandJoinEvent e) {

        final UUID playerUUID = e.getPlayer().getUniqueId();

        final Location location = getIslandCenterLocation(e.getIsland().getOwnerUUID());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.addGeneratorMember(new GeneratorMember(playerUUID));

        }

    }

    @EventHandler
    public void onLeave(final PlayerIslandLeaveEvent e) {

        final UUID playerUUID = e.getPlayer().getUniqueId();

        final Location location = getIslandCenterLocation(e.getIsland().getOwnerUUID());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

    @EventHandler
    public void onKick(final IslandKickEvent e) {

        final UUID playerUUID = e.getKicked().getUniqueId();

        final Location location = getIslandCenterLocation(e.getIsland().getOwnerUUID());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

}
