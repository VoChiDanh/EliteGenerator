package me.kafein.elitegenerator.generator.feature.regen;

import org.bukkit.Location;
import org.bukkit.Material;

public class Regen {

    final private Location location;
    final private Material material;
    private int delay;

    public Regen(final Location location, final Material material, final int delay) {
        this.location = location;
        this.material = material;
        this.delay = delay;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
