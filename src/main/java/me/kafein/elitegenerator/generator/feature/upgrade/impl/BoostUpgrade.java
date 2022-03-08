package me.kafein.elitegenerator.generator.feature.upgrade.impl;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.boost.Boost;
import me.kafein.elitegenerator.generator.feature.upgrade.Upgrade;

public class BoostUpgrade extends Upgrade {

    @Override
    public void apply(Generator generator) {}

    public void apply(Generator generator, int level, long time) {
        generator.setBoost(new Boost(level, time));
    }

}
