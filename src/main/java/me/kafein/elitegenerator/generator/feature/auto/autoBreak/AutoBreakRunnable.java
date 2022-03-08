package me.kafein.elitegenerator.generator.feature.auto.autoBreak;

import me.kafein.elitegenerator.EliteGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoBreakRunnable extends BukkitRunnable {

    final private AutoBreakManager autoBreakManager = EliteGenerator.getInstance().getGeneratorManager().getFeatureManager().getAutoBreakManager();

    public void start(final Plugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 40L, 40L);
    }

    @Override
    public void run() {
        autoBreakManager.autoBreak();
    }

}
