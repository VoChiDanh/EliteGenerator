package me.kafein.elitegenerator.hook;

import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.hook.hologram.impl.DecentHologramsHook;
import me.kafein.elitegenerator.hook.hologram.impl.HolographicDisplaysHook;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import me.kafein.elitegenerator.hook.skyblock.impl.ASkyBlockHook;
import me.kafein.elitegenerator.hook.skyblock.impl.BSkyBlockHook;
import me.kafein.elitegenerator.hook.skyblock.impl.FabledSkyBlockHook;
import me.kafein.elitegenerator.hook.skyblock.impl.SuperiorSkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;


public class HookManager {

    final private Plugin plugin;
    private HologramHook hologramHook;
    private SkyBlockHook skyBlockHook;

    public HookManager(final Plugin plugin) {
        this.plugin = plugin;
        loadHooks();
    }

    private void loadHooks() {

        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("HolographicDisplays")) hologramHook = new HolographicDisplaysHook(plugin);
        else if (pluginManager.isPluginEnabled("DecentHolograms")) hologramHook = new DecentHologramsHook();

        if (pluginManager.isPluginEnabled("ASkyBlock")) skyBlockHook = new ASkyBlockHook();
        else if (pluginManager.isPluginEnabled("BentoBox")) skyBlockHook = new BSkyBlockHook();
        else if (pluginManager.isPluginEnabled("SuperiorSkyblock2")) skyBlockHook = new SuperiorSkyBlockHook();
        else if (pluginManager.isPluginEnabled("FabledSkyBlock")) skyBlockHook = new FabledSkyBlockHook();

    }

    @Nullable
    public HologramHook getHologramHook() {
        return hologramHook;
    }

    public boolean hasHologramHook() {
        return hologramHook != null;
    }

    @Nullable
    public SkyBlockHook getSkyBlockHook() {
        return skyBlockHook;
    }

    public boolean hasSkyBlockHook() {
        return skyBlockHook != null;
    }

}
