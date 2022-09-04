package me.kafein.elitegenerator.generator.feature.regen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegenManager {

    final private Map<Location, Regen> regenGenerators = new ConcurrentHashMap<>();
    final private Plugin plugin;
    private boolean isRunnableStarted;

    public RegenManager(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void shutdown() {

        for (Map.Entry<Location, Regen> entry : regenGenerators.entrySet()) {

            entry.getKey().getBlock().setType(entry.getValue().getMaterial());

            regenGenerators.remove(entry.getKey());

        }

    }

    @Nullable
    public Regen getRegenMaterial(final Location location) {
        return regenGenerators.get(location);
    }

    public void addRegenGenerator(final Location location, final Material material) {
        regenGenerators.put(location, new Regen(location, material, 20));
        if (!isRunnableStarted) {
            isRunnableStarted = true;
            new RegenRunnable(plugin).start();
        }
    }

    public void removeRegenGenerator(final Location location) {
        regenGenerators.remove(location);
    }

    public boolean containsRegenGenerator(final Location location) {
        return regenGenerators.containsKey(location);
    }

    public Iterator<Regen> getRegenGenerators() {
        return regenGenerators.values().iterator();
    }

    public boolean isRegenGeneratorsIsEmpty() {
        return regenGenerators.isEmpty();
    }

}
