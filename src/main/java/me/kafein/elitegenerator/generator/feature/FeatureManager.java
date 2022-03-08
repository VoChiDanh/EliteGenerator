package me.kafein.elitegenerator.generator.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kafein.elitegenerator.generator.feature.auto.autoBreak.AutoBreakManager;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChestManager;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.AutoPickupManager;
import me.kafein.elitegenerator.generator.feature.auto.autoSmelt.AutoSmeltManager;
import me.kafein.elitegenerator.generator.feature.hologram.HologramManager;
import me.kafein.elitegenerator.generator.feature.oreGen.OreGenManager;
import me.kafein.elitegenerator.generator.feature.regen.RegenManager;
import me.kafein.elitegenerator.generator.feature.upgrade.UpgradeManager;
import org.bukkit.plugin.Plugin;

@AllArgsConstructor
@Getter
public class FeatureManager {

    final private UpgradeManager upgradeManager;
    final private HologramManager hologramManager;
    final private RegenManager regenManager;
    final private OreGenManager oreGenManager;
    final private AutoBreakManager autoBreakManager;
    final private AutoSmeltManager autoSmeltManager;
    final private AutoPickupManager autoPickupManager;
    final private AutoChestManager autoChestManager;

    final private Plugin plugin;

    public FeatureManager(final Plugin plugin) {
        this.plugin = plugin;
        upgradeManager = new UpgradeManager();
        hologramManager = new HologramManager(plugin);
        regenManager = new RegenManager(plugin);
        oreGenManager = new OreGenManager();
        autoBreakManager = new AutoBreakManager(plugin);
        autoSmeltManager = new AutoSmeltManager();
        autoPickupManager = new AutoPickupManager(this);
        autoChestManager = new AutoChestManager(plugin);
    }

}
