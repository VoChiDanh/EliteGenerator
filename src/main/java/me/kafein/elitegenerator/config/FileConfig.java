package me.kafein.elitegenerator.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileConfig {

    final private FileConfiguration configuration;

    public FileConfig(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public FileConfig(final File file) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(final String key) {
        if (!containsKey(key)) return "??";
        return configuration.getString(key);
    }

    public int getInt(final String key) {
        if (!containsKey(key)) return 0;
        return configuration.getInt(key);
    }

    public long getLong(final String key) {
        if (!containsKey(key)) return 0;
        return configuration.getLong(key);
    }

    public double getDouble(final String key) {
        if (!containsKey(key)) return 0;
        return configuration.getDouble(key);
    }

    public boolean getBoolean(final String key) {
        if (!containsKey(key)) return false;
        return configuration.getBoolean(key);
    }

    public List<String> getStringList(final String key) {
        if (!containsKey(key)) return Collections.emptyList();
        return configuration.getStringList(key);
    }

    public ConfigurationSection getConfigurationSection(final String key) {
        return configuration.getConfigurationSection(key);
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public boolean containsKey(final String key) {
        return configuration.contains(key);
    }

}
