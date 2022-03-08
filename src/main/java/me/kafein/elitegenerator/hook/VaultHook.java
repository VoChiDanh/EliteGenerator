package me.kafein.elitegenerator.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.NumberFormat;
import java.util.Locale;

public class VaultHook {

    private static Economy econ = null;

    public static boolean setupEconomy(final Plugin plugin, final PluginManager pluginManager) {
        if (pluginManager.getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static String formatMoney(double money){

        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);

        if (money < 1000.0D) {

            return format.format(money);

        } else if (money < 1000000.0D) {

            return format.format(money / 1000.0D) + "k";

        } else if (money < 1.0E9D) {

            return format.format(money / 1000000.0D) + "M";

        } else if (money < 1.0E12D) {

            return format.format(money / 1.0E9D) + "B";

        } else if (money < 1.0E15D) {

            return format.format(money / 1.0E12D) + "T";

        } else {

            return money < 1.0E18D ? format.format(money / 1.0E15D) + "Q" : String.valueOf(money);

        }

    }

}
