package me.kafein.elitegenerator.generator.feature.auto.autoSmelt;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AutoSmeltManager {

    final private Map<Material, AutoSmelt> autoSmelts = new HashMap<>();

    public AutoSmeltManager() {

        final FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);

        for (String from : fileConfig.getConfigurationSection("settings.smeltItems").getKeys(false)) {
            final AutoSmelt autoSmelt = new AutoSmelt(Material.getMaterial(from)
                    , Material.getMaterial(fileConfig.getString("settings.smeltItems." + from)));
            autoSmelts.put(autoSmelt.getFrom(), autoSmelt);
        }

    }

    @Nullable
    public AutoSmelt getAutoSmelt(final Material material) {
        return autoSmelts.get(material);
    }

    public void addAutoSmelt(final AutoSmelt autoSmelt) {
        autoSmelts.put(autoSmelt.getFrom(), autoSmelt);
    }

    public void removeAutoSmelt(final Material material) {
        autoSmelts.remove(material);
    }

    public boolean containsAutoSmelt(final Material material) {
        return autoSmelts.containsKey(material);
    }

}
