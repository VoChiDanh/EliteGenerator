package me.kafein.elitegenerator.hook;

import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import me.kafein.elitegenerator.hook.skyblock.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;

public class HookManager {

    private SkyBlockHook skyBlockHook;

    public HookManager() {
        loadSkyBlockHook();
    }

    private void loadSkyBlockHook() {

        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("ASkyBlock")) skyBlockHook = new ASkyBlockHook();
        else if (pluginManager.isPluginEnabled("BentoBox")) skyBlockHook = new BSkyBlockHook();
        else if (pluginManager.isPluginEnabled("SuperiorSkyblock2")) skyBlockHook = new SuperiorSkyBlockHook();
        else if (pluginManager.isPluginEnabled("IridiumSkyblock")) skyBlockHook = new IridiumSkyBlockHook();
        else if (pluginManager.isPluginEnabled("FabledSkyBlock")) skyBlockHook = new FabledSkyBlockHook();

    }

    @Nullable
    public SkyBlockHook getSkyBlockHook() {
        return skyBlockHook;
    }

    public boolean hasSkyBlockHook() {
        return skyBlockHook != null;
    }

}
