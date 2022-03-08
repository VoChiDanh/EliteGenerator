package me.kafein.elitegenerator.generator.feature.upgrade.impl;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.upgrade.Upgrade;
import org.bukkit.entity.Player;

public class AutoBreakUpgrade extends Upgrade {

    @Override
    public void apply(Generator generator) {

        generator.setAutoBreakBuyed(true);

    }

}
