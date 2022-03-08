package me.kafein.elitegenerator.generator.feature.oreGen;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.requirement.impl.MoneyRequirement;
import me.kafein.elitegenerator.generator.feature.requirement.impl.PermissionRequirement;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class OreGenManager {

    final private Map<Integer, OreGen> levelOreGenMap = new HashMap<>();
    final private Map<Integer, OreGen> boostedOreGenMap = new HashMap<>();

    public OreGenManager() {

        load("level");
        load("boost");

    }

    private void load(final String loc) {

        final FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);

        String prefix = "settings.generator.upgrade.";

        for (String oreGenLevel : fileConfig.getConfigurationSection(prefix + loc + "-upgrade").getKeys(false)) {

            final OreGen oreGen = new OreGen(Integer.parseInt(oreGenLevel));

            for (String material : fileConfig.getStringList(prefix + loc + "-upgrade." + oreGenLevel + ".material")) {
                final String[] splitter = material.split(": ");
                oreGen.addMaterial(Material.getMaterial(splitter[0]), Double.parseDouble(splitter[1]));
            }

            switch (loc) {
                case "level":
                    levelOreGenMap.put(oreGen.getLevel(), oreGen);
                    break;
                case "boost":
                    boostedOreGenMap.put(oreGen.getLevel(), oreGen);
                    break;
            }

        }

    }

    public OreGen getOreGenForGenerator(final Generator generator) {
        if (generator.hasBoost()) return boostedOreGenMap.get(generator.getBoost().getLevel());
        return levelOreGenMap.get(generator.getLevel());
    }

    @Nullable
    public OreGen getOreGen(final int level, final boolean isBoosted) {
        if (isBoosted) return boostedOreGenMap.get(level);
        return levelOreGenMap.get(level);
    }

    public boolean containsOreGen(final int level, final boolean isBoosted) {
        if (isBoosted) return boostedOreGenMap.containsKey(level);
        return levelOreGenMap.containsKey(level);
    }

}
