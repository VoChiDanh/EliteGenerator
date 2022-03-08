package me.kafein.elitegenerator.generator.feature.oreGen;

import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.*;

public class OreGen {

    final private Map<Material, Double> materials = new HashMap<>();

    final private int level;

    public OreGen(final int level) {
        this.level = level;
    }

    public Material randomMaterial(final Random random) {

        Material material = null;

        for (Map.Entry<Material, Double> entry : materials.entrySet()) {
            double randomVar = random.nextDouble() * 100;
            if (randomVar - entry.getValue() > 0) continue;
            material = entry.getKey();
            break;
        }

        return (material == null ? randomMaterial(random) : material);

    }

    @Nullable
    public Double getChance(final Material material) {
        return materials.get(material);
    }

    public void addMaterial(final Material material, final double chance) {
        materials.put(material, chance);
    }

    public void removeMaterial(final Material material) {
        materials.remove(material);
    }

    public boolean containsMaterial(final Material material) {
        return materials.containsKey(material);
    }

    public int getLevel() {
        return level;
    }

}
