package me.kafein.elitegenerator.generator.feature.boost.task;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.boost.Boost;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;

public class BoostRunnable implements Runnable {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private HologramHook hologramHook = EliteGenerator.getInstance().getHookManager().getHologramHook();

    final private Plugin plugin;

    public BoostRunnable(final Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {

        if (generatorManager.generatorListSize() < 0) return;

        final Iterator<Generator> generatorIterator = generatorManager.getGeneratorsIterator();

        while (generatorIterator.hasNext()) {

            final Generator generator = generatorIterator.next();

            if (!generator.hasBoost()) continue;

            final Boost boost = generator.getBoost();

            if (boost.getTime() <= 0) {
                generator.clearBoost();
                Bukkit.getScheduler().runTask(plugin, () -> hologramHook.reloadHologram(generator));
            } else {
                boost.removeTime(1);
                hologramHook.reloadBoostLine(generator);
            }

        }

    }

}
