package me.kafein.elitegenerator.listener;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.event.GeneratorBreakEvent;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class IABlockListener implements Listener {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    final private PluginManager pluginManager = Bukkit.getPluginManager();

    @EventHandler
    public void onCustomBreak(CustomBlockBreakEvent e) {
        final Block block = e.getBlock();
        if (!generatorManager.containsGeneratorLocation(block.getLocation())) return;
        final Player player = e.getPlayer();
        final Generator generator = generatorManager.getGenerator(block.getLocation());

        final GeneratorBreakEvent generatorBreakEvent = new GeneratorBreakEvent(
                player
                , generator
                , block
                , false
                , false
                , false
                , false
                , false);

        pluginManager.callEvent(generatorBreakEvent);
    }

}
