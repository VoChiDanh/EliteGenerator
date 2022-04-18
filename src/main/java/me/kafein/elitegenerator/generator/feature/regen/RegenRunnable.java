package me.kafein.elitegenerator.generator.feature.regen;

import me.kafein.elitegenerator.EliteGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;

public class RegenRunnable implements Runnable {

    final private RegenManager regenManager = EliteGenerator.getInstance().getGeneratorManager().getFeatureManager().getRegenManager();

    final private Plugin plugin;

    public RegenRunnable(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 1, 1);
    }

    @Override
    public void run() {

        if (regenManager.isRegenGeneratorsIsEmpty()) return;

        final Iterator<Regen> regenIterator = regenManager.getRegenGenerators();

        while (regenIterator.hasNext()) {

            final Regen regen = regenIterator.next();

            if (regen.getDelay() <= 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> regen.getLocation().getBlock().setType(regen.getMaterial()));
                regenManager.removeRegenGenerator(regen.getLocation());
                regenIterator.remove();
            } else regen.setDelay(regen.getDelay() - 1);

        }

    }

}
