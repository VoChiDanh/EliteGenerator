package me.kafein.elitegenerator;

import lombok.Getter;
import me.kafein.elitegenerator.command.GeneratorCMD;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.listener.AutoPickupListener;
import me.kafein.elitegenerator.hook.HookManager;
import me.kafein.elitegenerator.hook.VaultHook;
import me.kafein.elitegenerator.listener.BlockListener;
import me.kafein.elitegenerator.listener.GeneratorBreakListener;
import me.kafein.elitegenerator.listener.InteractListener;
import me.kafein.elitegenerator.listener.PlayerListener;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.listener.InventoryListener;
import me.kafein.elitegenerator.storage.StorageManager;
import me.kafein.elitegenerator.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EliteGenerator extends JavaPlugin {

    @Getter
    private static EliteGenerator instance;

    @Getter
    private HookManager hookManager;
    @Getter
    private FileManager fileManager;
    @Getter
    private StorageManager storageManager;
    @Getter
    private GeneratorManager generatorManager;
    @Getter
    private MenuManager menuManager;
    @Getter
    private UserManager userManager;

    @Override
    public void onEnable() {

        instance = this;

        final PluginManager pluginManager = Bukkit.getPluginManager();

        fileManager = new FileManager(this);
        hookManager = new HookManager(this);

        if (!VaultHook.setupEconomy(this, pluginManager)) {
            getLogger().warning("Vault is not exists!");
            pluginManager.disablePlugin(this);
            return;
        } else if (!hookManager.hasHologramHook()) {
            getLogger().warning("Hologram API is not exists!");
            pluginManager.disablePlugin(this);
            return;
        } else if (!hookManager.hasSkyBlockHook()) {
            getLogger().warning("SkyBlock API is not exists!");
            pluginManager.disablePlugin(this);
            return;
        }

        storageManager = new StorageManager(this);
        generatorManager = new GeneratorManager(this);
        userManager = new UserManager();
        menuManager = new MenuManager(this);

        hookManager.getSkyBlockHook().register(this);

        getCommand("elitegenerator").setExecutor(new GeneratorCMD());

        registerListeners(pluginManager);

    }

    @Override
    public void onDisable() {

        generatorManager.getFeatureManager().getRegenManager().shutdown();

        generatorManager.saveGenerators();

        userManager.saveUsers();

    }

    private void registerListeners(final PluginManager pluginManager) {

        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new BlockListener(), this);
        pluginManager.registerEvents(new GeneratorBreakListener(this), this);
        pluginManager.registerEvents(new InteractListener(), this);
        pluginManager.registerEvents(new AutoPickupListener(), this);

    }

}
