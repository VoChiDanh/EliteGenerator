package me.kafein.elitegenerator.generator.feature.auto.autoBreak;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.event.GeneratorBreakEvent;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoBreakManager {

    final private PluginManager pluginManager = Bukkit.getPluginManager();
    final private List<UUID> generatorList = new ArrayList<>();
    final private Plugin plugin;
    private GeneratorManager generatorManager = null;
    private boolean isRunnableStarted;

    public AutoBreakManager(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void startRunnable() {
        if (isRunnableStarted) return;
        new AutoBreakRunnable().start(plugin);
        isRunnableStarted = true;
    }

    public void autoBreak() {

        if (generatorList.isEmpty()) return;

        for (UUID uuid : new ArrayList<>(generatorList)) {

            final Generator generator = getGeneratorManager().getGenerator(uuid);
            if (generator == null) {
                generatorList.remove(uuid);
                continue;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                final Block block = generator.getGeneratorLocation().getBlock();
                if (block.isEmpty()) return;
                block.breakNaturally();
                final GeneratorBreakEvent generatorBreakEvent = new GeneratorBreakEvent(null
                        , generator
                        , block
                        , true
                        , generator.isAutoPickupEnabled()
                        , generator.isAutoSmeltEnabled()
                        , generator.isAutoChestEnabled()
                        , false);

                pluginManager.callEvent(generatorBreakEvent);
            });

        }

    }

    public void addAutoBreakerGenerator(final UUID generatorUUID) {

        generatorList.add(generatorUUID);

    }

    public void removeAutoBreakerGenerator(final UUID generatorUUID) {

        generatorList.remove(generatorUUID);

    }

    public boolean containsGenerator(final UUID generatorUUID) {
        return generatorList.contains(generatorUUID);
    }

    public boolean runnableStarted() {
        return isRunnableStarted;
    }

    private GeneratorManager getGeneratorManager() {
        if (generatorManager == null) generatorManager = EliteGenerator.getInstance().getGeneratorManager();
        return generatorManager;
    }

}
