package me.kafein.elitegenerator.hook;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy econ = null;
    private static Permission perms = null;

    public static boolean setup(Plugin plugin, PluginManager pluginManager) {
        if (pluginManager.getPlugin("Vault") == null) return false;
        if (!setupEconomy(plugin)) return false;
        if (!setupPermissions(plugin)) return false;
        return true;
    }

    private static boolean setupEconomy(final Plugin plugin) {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    private static boolean setupPermissions(final Plugin plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermission() {
        return perms;
    }

}
