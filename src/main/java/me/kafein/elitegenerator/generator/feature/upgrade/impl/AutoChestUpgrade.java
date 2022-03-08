package me.kafein.elitegenerator.generator.feature.upgrade.impl;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.upgrade.Upgrade;

public class AutoChestUpgrade extends Upgrade {

    @Override
    public void apply(Generator generator) {
        generator.setAutoChestBuyed(true);
    }

}
