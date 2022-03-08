package me.kafein.elitegenerator.hook.skyblock.impl;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.UserManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IridiumSkyBlockHook extends SkyBlockHook {

    final private UserManager userManager = IridiumSkyblock.getInstance().getUserManager();
    final private IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();

    @Override
    public World getIslandWorld() {
        return islandManager.getWorld();
    }

    @Override
    public boolean hasIsland(UUID playerUUID) {
        return userManager.getUserByUUID(playerUUID).get().getIsland().isPresent();
    }

    @Override
    public Location getIslandCenterLocation(UUID playerUUID) {
        final User user = userManager.getUserByUUID(playerUUID).get();
        final Island island = user.getIsland().get();
        return island.getCenter(getIslandWorld());
    }

    @Override
    public long getIslandLevel(UUID playerUUID) {
        final User user = userManager.getUserByUUID(playerUUID).get();
        final Island island = user.getIsland().get();
        return island.getLevel();
    }

    @Override
    public UUID getIslandOwner(UUID playerUUID) {
        final User user = userManager.getUserByUUID(playerUUID).get();
        final Island island = user.getIsland().get();
        return island.getOwner().getUuid();
    }

    @Override
    public List<UUID> getIslandMembers(UUID playerUUID) {
        final User user = userManager.getUserByUUID(playerUUID).get();
        final Island island = user.getIsland().get();
        final List<UUID> memberList = new ArrayList<>();
        island.getMembers().forEach(member -> memberList.add(member.getUuid()));
        return memberList;
    }


    @EventHandler
    public void onDelete(final IslandDeleteEvent e) {

        final Location location = e.getIsland().getCenter(getIslandWorld());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onReset(final IslandRegenEvent e) {

        final Location location = e.getIsland().getCenter(getIslandWorld());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            getGeneratorManager().deleteGenerator(uuid);

        }

    }

    @EventHandler
    public void onChangeOwner(final UserPromoteEvent e) {

        final Location location = e.getIsland().getCenter(getIslandWorld());

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            if (e.getNewRank() == IslandRank.OWNER) generator.changeOwnerUUID(e.getIsland().getOwner().getUuid());
            else if (e.getNewRank() == IslandRank.MEMBER) generator.addGeneratorMember(new GeneratorMember(e.getUser().getUuid()));

        }

    }

    @EventHandler
    public void onKick(final UserKickEvent e) {

        final UUID playerUUID = e.getUser().getUuid();

        final Location location = getIslandCenterLocation(playerUUID);

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

    @EventHandler
    public void onLeave(final UserLeaveEvent e) {

        final UUID playerUUID = e.getUser().getUuid();

        final Location location = getIslandCenterLocation(playerUUID);

        if (!getGeneratorManager().containsGeneratorIslandLocation(location)) return;

        for (UUID uuid : getGeneratorManager().getGenerators(location)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);

            generator.removeGeneratorMember(playerUUID);

        }

    }

}
