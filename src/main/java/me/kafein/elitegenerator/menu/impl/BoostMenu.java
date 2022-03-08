package me.kafein.elitegenerator.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.hologram.HologramManager;
import me.kafein.elitegenerator.generator.feature.upgrade.UpgradeManager;
import me.kafein.elitegenerator.generator.feature.upgrade.impl.BoostUpgrade;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class BoostMenu extends Menu {

    final private FileManager fileManager = EliteGenerator.getInstance().getFileManager();
    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private FeatureManager featureManager = generatorManager.getFeatureManager();
    final private HologramManager hologramManager = featureManager.getHologramManager();
    final private UpgradeManager upgradeManager = featureManager.getUpgradeManager();

    public BoostMenu(String title, int slot, FileConfig fileConfig) {
        super(title, slot, fileConfig);
    }

    @Override
    public void loadItems() {

    }

    public void openMenu(Player player, Generator generator) {

        final Inventory inventory = this.clone();

        final FileConfig fileConfig = getFileConfig();

        for (String itemName : fileConfig.getConfigurationSection("menu.items").getKeys(false)) {

            final String prefix = "menu.items." + itemName + ".";

            final ItemBuilder itemBuilder = loadItem(itemName, player.getName(), generator)
                    .setString("generator", generator.getGeneratorUUID().toString())
                    .setInteger("boostLevel", Integer.parseInt(itemName.substring(itemName.lastIndexOf("_") + 1)));

            inventory.setItem(fileConfig.getInt(prefix + "slot"), itemBuilder.toItemStack());

        }

        if (fileConfig.getBoolean("menu.fill")) fill(inventory);

        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));

    }

    @EventHandler
    public void onClick(MenuClickEvent e) {

        if (!e.getMenu().getTitle().equals(getTitle())) return;

        final NBTItem nbtItem = new NBTItem(e.getItem());
        if (!nbtItem.hasKey("boostLevel")) return;

        final Player player = e.getPlayer();

        final Generator generator = generatorManager.getGenerator(UUID.fromString(nbtItem.getString("generator")));
        if (generator == null) {
            player.closeInventory();
            return;
        }

        if (generator.hasBoost()) return;

        final int boostLevel = nbtItem.getInteger("boostLevel");

        final BoostUpgrade boostUpgrade = upgradeManager.getBoostUpgrade(boostLevel);
        if (boostUpgrade == null) return;
        if (!boostUpgrade.hasRequirements(player, generator)) {
            player.closeInventory();
            player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
            return;
        }

        final int duration = fileManager.getFile(FileManager.ConfigFile.settings).getInt("settings.generator.upgrade.boost-upgrade." + boostLevel + ".duration");

        boostUpgrade.applyRequirements(player, generator);
        boostUpgrade.apply(generator, boostLevel, duration);
        player.sendMessage(fileManager.getMessage("generator.upgrade.generatorLevelUpgrade")
                .replace("%level%", Integer.toString(boostLevel))
                .replace("%duration%", Integer.toString(duration)));

        hologramManager.reloadHologram(generator);

    }

    @Override
    public void onOpen(MenuOpenEvent e) {

    }

    @Override
    public void onClose(MenuCloseEvent e) {

    }

}
