package me.kafein.elitegenerator.hook.hologram.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.util.placeholder.PlaceHolder;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HolographicDisplaysHook implements HologramHook {

    final private FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);

    final private List<String> classicHologramTexts = fileConfig.getStringList("settings.generator.hologram.classic-hologram");
    final private List<String> boostedHologramTexts = fileConfig.getStringList("settings.generator.hologram.boosted-hologram");
    final private Plugin plugin;
    private int boostTimeLine;

    public HolographicDisplaysHook(final Plugin plugin) {
        this.plugin = plugin;
        for (int i = 0; i < boostedHologramTexts.size(); i++) {
            if (!boostedHologramTexts.get(i).contains("%generator_boost_time%")) continue;
            boostTimeLine = i;
            break;
        }
    }

    public void loadHologram(final Generator generator) {
        if (!generator.isHologramEnabled()) return;
        final Location location = generator.getGeneratorLocation().clone();
        Hologram hologram;
        if (generator.hasBoost()) {
            location.add(0.5, fileConfig.getDouble("settings.generator.hologram.boosted-hologram-height"), 0.5);
            hologram = HologramsAPI.createHologram(plugin, location);
            boostedHologramTexts.forEach(e -> hologram.appendTextLine(PlaceHolder.replace(e, generator)));
        }else {
            location.add(0.5, fileConfig.getDouble("settings.generator.hologram.classic-hologram-height"), 0.5);
            hologram = HologramsAPI.createHologram(plugin, location);
            classicHologramTexts.forEach(e -> hologram.appendTextLine(PlaceHolder.replace(e, generator)));
        }
        generator.setHologram(hologram);
    }

    public void reloadHologram(final Generator generator) {
        if (!generator.isHologramEnabled()) return;
        if (generator.hasHologram()) ((Hologram) generator.getHologram()).delete();
        generator.setHologram(null);
        loadHologram(generator);
    }

    @Override
    public void deleteHologram(Generator generator) {
        if (generator.hasHologram()) ((Hologram) generator.getHologram()).delete();
    }

    public void reloadBoostLine(final Generator generator) {
        if (!generator.isHologramEnabled()) return;
        final TextLine textLine = (TextLine) ((Hologram) generator.getHologram()).getLine(boostTimeLine);
        textLine.setText(PlaceHolder.replace(boostedHologramTexts.get(boostTimeLine), generator));
    }

}
