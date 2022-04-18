package me.kafein.elitegenerator.storage;

import me.kafein.elitegenerator.storage.impl.JsonStorage;
import org.bukkit.plugin.Plugin;

public class StorageManager {

    final private Plugin plugin;
    private Storage storage;

    public StorageManager(final Plugin plugin) {
        this.plugin = plugin;
        loadStorage();
    }

    public Storage get() {
        return storage;
    }

    private boolean loadStorage() {

        storage = new JsonStorage(plugin);

        return true;

    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

}
