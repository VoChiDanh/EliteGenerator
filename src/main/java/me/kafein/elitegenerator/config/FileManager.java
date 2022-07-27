package me.kafein.elitegenerator.config;

import me.kafein.elitegenerator.util.ColorSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class FileManager {

    final private Plugin plugin;

    private FileConfig settings;
    private FileConfig language;

    public FileManager(final Plugin plugin) {
        this.plugin = plugin;
        loadFiles();
    }

    public boolean loadFiles() {

        final File settingsFile = new File(plugin.getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) plugin.saveResource("settings.yml", true);
        settings = new FileConfig(YamlConfiguration.loadConfiguration(settingsFile));

        final String language = settings.getString("settings.language");
        final File languageFile = new File(plugin.getDataFolder(), "language/language_" + language + ".yml");
        if (!languageFile.exists()) {
            switch (language) {
                case "en":
                    plugin.saveResource("language/language_en.yml", true);
                    break;
                case "tr":
                    plugin.saveResource("language/language_tr.yml", true);
                    break;
                default:
                    try {
                        copyResource(languageFile, "language/language_en.yml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        this.language = new FileConfig(YamlConfiguration.loadConfiguration(languageFile));

        return true;
    }

    public FileConfig getFile(final ConfigFile configFile) {
        switch (configFile) {
            case settings:
                return settings;
            case language:
                return language;
        }
        return null;
    }

    public String getMessage(final String property) {
        return ColorSerializer.serialize(getFile(ConfigFile.language).getString("language." + property)
                .replace("%prefix%", getFile(ConfigFile.language).getString("language.prefix")));
    }

    public List<String> getMessageList(final String property) {
        final List<String> list = getFile(ConfigFile.language).getStringList("language." + property);
        list.replaceAll(ColorSerializer::serialize);
        return list;
    }

    public void copyResource(final File file, final String resource) throws IOException {

        final InputStream in = plugin.getResource(resource);

        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();

    }

    public enum ConfigFile {
        settings, language
    }

}
