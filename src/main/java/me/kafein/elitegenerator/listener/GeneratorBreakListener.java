package me.kafein.elitegenerator.listener;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.event.GeneratorBreakEvent;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.AutoPickup;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.AutoPickupManager;
import me.kafein.elitegenerator.generator.feature.oreGen.OreGenManager;
import me.kafein.elitegenerator.generator.feature.regen.RegenManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class GeneratorBreakListener implements Listener {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private FeatureManager featureManager = generatorManager.getFeatureManager();
    final private RegenManager regenManager = featureManager.getRegenManager();
    final private OreGenManager oreGenManager = featureManager.getOreGenManager();
    final private AutoPickupManager autoPickupManager = featureManager.getAutoPickupManager();

    final private Random random = new Random();

    final private Plugin plugin;

    public GeneratorBreakListener(final Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(final GeneratorBreakEvent e) {

        final Generator generator = e.getGenerator();

        AutoPickup autoPickup;

        if (e.isAutoBreak()) {

            if (e.isAutoChest()) {

                if (!e.isAutoPickup() || generator.getAutoChest().isChestBreaked()) {

                    generator.clearAutoChest();
                    autoPickup = new AutoPickup(null, null);

                } else autoPickup = new AutoPickup(generator.getAutoChest().getInventory(), null);

            } else {

                autoPickup = new AutoPickup(null, null);

            }

        } else {

            final ItemStack itemStack = (e.getPlayer().getItemInHand() == null ? null : e.getPlayer().getItemInHand());

            if (e.isAutoChest()) {

                if (!e.isAutoPickup() || generator.getAutoChest().isChestBreaked()) {

                    generator.clearAutoChest();
                    autoPickup = new AutoPickup(null, itemStack);

                } else autoPickup = new AutoPickup(generator.getAutoChest().getInventory(), itemStack);

            } else {

                if (e.isAutoPickup()) {

                    autoPickup = new AutoPickup(e.getPlayer().getInventory(), itemStack);

                } else {

                    autoPickup = new AutoPickup(null, itemStack);

                }

            }

        }

        final Location location = e.getBlock().getLocation();

        autoPickup.setAutoSmeltEnabled(e.isAutoSmelt());
        autoPickupManager.addAutoPickup(location, autoPickup);

        Material material = oreGenManager.getOreGenForGenerator(generator).randomMaterial(random);

        regenManager.addRegenGenerator(location, material);

    }

}
