package me.kafein.elitegenerator.hook.hologram.impl;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.util.placeholder.PlaceHolder;
import org.bukkit.Location;

import java.util.List;

public class DecentHologramsHook implements HologramHook {

    final private FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);
    final private List<String> classicHologramTexts = fileConfig.getStringList("settings.generator.hologram.classic-hologram");
    final private List<String> boostedHologramTexts = fileConfig.getStringList("settings.generator.hologram.boosted-hologram");
    private int boostTimeLine;

    public DecentHologramsHook() {
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
        HologramPage hologramPage;
        if (generator.hasBoost()) {
            location.add(0.5, fileConfig.getDouble("settings.generator.hologram.boosted-hologram-height"), 0.5);
            hologram = DHAPI.getHologram(generator.getGeneratorUUID().toString());
            if (hologram == null)
                hologram = DHAPI.createHologram(generator.getGeneratorUUID().toString(), location, false);
            hologramPage = hologram.getPage(0);
            boostedHologramTexts.forEach(e -> {
                hologramPage.addLine(new HologramLine(hologramPage, location, PlaceHolder.replace(e, generator)));
            });
        } else {
            location.add(0.5, fileConfig.getDouble("settings.generator.hologram.classic-hologram-height"), 0.5);
            hologram = DHAPI.getHologram(generator.getGeneratorUUID().toString());
            if (hologram == null)
                hologram = DHAPI.createHologram(generator.getGeneratorUUID().toString(), location, false);
            hologramPage = hologram.getPage(0);
            classicHologramTexts.forEach(e -> {
                hologramPage.addLine(new HologramLine(hologramPage, location, PlaceHolder.replace(e, generator)));
            });
        }
        generator.setHologram(hologram);
    }

    public void reloadHologram(final Generator generator) {
        if (!generator.isHologramEnabled()) return;
        if (generator.hasHologram()) deleteHologram(generator);
        generator.setHologram(null);
        loadHologram(generator);
    }

    @Override
    public void deleteHologram(Generator generator) {
        if (generator.hasHologram()) {
            final Hologram hologram = (Hologram) generator.getHologram();
            hologram.destroy();
        }
    }

    public void reloadBoostLine(final Generator generator) {
        if (!generator.isHologramEnabled()) return;
        final Hologram hologram = (Hologram) generator.getHologram();
        final HologramPage hologramPage = hologram.getPage(0);
        final HologramLine hologramLine = hologramPage.getLine(boostTimeLine);
        hologramLine.setContent(PlaceHolder.replace(boostedHologramTexts.get(boostTimeLine), generator));
    }

}
