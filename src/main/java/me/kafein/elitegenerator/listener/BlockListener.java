package me.kafein.elitegenerator.listener;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import me.jet315.minions.events.MinerBlockBreakEvent;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.event.GeneratorBreakEvent;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class BlockListener implements Listener {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    final private PluginManager pluginManager = Bukkit.getPluginManager();
    
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent e) {

        final Block block = e.getBlock();

        if (block.getType() == Material.NOTE_BLOCK) return;

        if (!generatorManager.containsGeneratorLocation(block.getLocation())) return;

        final Player player = e.getPlayer();
        final Generator generator = generatorManager.getGenerator(block.getLocation());

        if (!generator.containsGeneratorMember(player.getUniqueId())
                || !generator.containsMemberPermission(player.getUniqueId(), MemberPermission.BREAK_GENERATOR)) {
            e.setCancelled(true);
            return;
        }

        final GeneratorBreakEvent generatorBreakEvent = new GeneratorBreakEvent(
                player
                , generator
                , block
                , false
                , generator.isAutoPickupEnabled()
                , generator.isAutoSmeltEnabled()
                , generator.isAutoChestEnabled()
                , false);

        pluginManager.callEvent(generatorBreakEvent);

        if (generatorBreakEvent.isAutoPickup()) {
            player.giveExp(e.getExpToDrop());
            e.setExpToDrop(0);
        }

    }

    @EventHandler
    public void onCustomBreak (CustomBlockBreakEvent e) {

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
