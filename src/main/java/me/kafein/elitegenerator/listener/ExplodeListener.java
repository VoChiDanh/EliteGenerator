package me.kafein.elitegenerator.listener;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeListener implements Listener {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        e.blockList().removeIf(block -> generatorManager.containsGeneratorLocation(block.getLocation()));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        e.getBlocks().forEach(block -> {
            if (generatorManager.containsGeneratorLocation(block.getLocation())) e.setCancelled(true);
        });
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        e.getBlocks().forEach(block -> {
            if (generatorManager.containsGeneratorLocation(block.getLocation())) e.setCancelled(true);
        });
    }

    @EventHandler
    public void onSLimefunExplode(BlockExplodeEvent e) {
        e.blockList().removeIf(block -> generatorManager.containsGeneratorLocation(block.getLocation()));
    }

}
