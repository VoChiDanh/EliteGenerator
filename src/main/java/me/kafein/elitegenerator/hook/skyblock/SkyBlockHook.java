package me.kafein.elitegenerator.hook.skyblock;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class SkyBlockHook implements Listener {

    private GeneratorManager generatorManager;

    public void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    }

    protected GeneratorManager getGeneratorManager() {
        return generatorManager;
    }

    public abstract World getIslandWorld();

    public abstract boolean hasIsland(final UUID playerUUID);

    public abstract Location getIslandCenterLocation(final UUID playerUUID);

    public abstract long getIslandLevel(final UUID playerUUID);

    @Nullable
    public abstract UUID getIslandOwner(final UUID playerUUID);

    public abstract List<UUID> getIslandMembers(final UUID playerUUID);

}
