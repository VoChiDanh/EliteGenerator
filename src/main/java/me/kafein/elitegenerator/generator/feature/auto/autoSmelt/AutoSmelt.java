package me.kafein.elitegenerator.generator.feature.auto.autoSmelt;

import org.bukkit.Material;

public class AutoSmelt {

    final private Material from;
    final private Material to;

    public AutoSmelt(final Material from, final Material to) {
        this.from = from;
        this.to = to;
    }

    public Material getFrom() {
        return from;
    }

    public Material getTo() {
        return to;
    }

}
