package me.kafein.elitegenerator.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class GeneratorMenu extends Menu {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    public GeneratorMenu(String title, int slot, FileConfig fileConfig) {
        super(title, slot, fileConfig);
    }

    @Override
    public void loadItems() {

    }

    public void openMenu(final Player player, final Generator generator) {
        if (!isEnabled()) return;

        if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.OPEN_SETTINGS)) return;

        final Inventory inventory = this.clone();

        final FileConfig fileConfig = getFileConfig();

        for (String itemName : fileConfig.getConfigurationSection("menu.items").getKeys(false)) {

            final String prefix = "menu.items." + itemName + ".";

            final ItemBuilder itemBuilder = loadItem(itemName, player.getName(), generator)
                    .setString("generator", generator.getGeneratorUUID().toString());

            inventory.setItem(fileConfig.getInt(prefix + "slot"), itemBuilder.toItemStack());

        }

        if (fileConfig.getBoolean("menu.fill")) fill(inventory);

        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));

    }

    @EventHandler
    public void onClick(MenuClickEvent e) {

        if (!e.getMenu().getTitle().equals(getTitle())) return;

        final FileConfig fileConfig = getFileConfig();

        final Player player = e.getPlayer();

        final NBTItem nbtItem = new NBTItem(e.getItem());

        if (!nbtItem.hasKey("generator")) return;

        final Generator generator = generatorManager.getGenerator(UUID.fromString(nbtItem.getString("generator")));
        if (generator == null) {
            player.closeInventory();
            return;
        }

        if (e.getSlot() == fileConfig.getInt("menu.items.upgrade.slot")) {

            final UpgradeMenu upgradeMenu = (UpgradeMenu) getMenuManager().getMenu(MenuManager.MenuType.UPGRADE);
            upgradeMenu.openMenu(player, generator);

        }else if (e.getSlot() == fileConfig.getInt("menu.items.settings.slot")) {

            final SettingsMenu settingsMenu = (SettingsMenu) getMenuManager().getMenu(MenuManager.MenuType.SETTINGS);
            settingsMenu.openMenu(player, generator);

        }

    }

    @Override
    public void onOpen(MenuOpenEvent e) {}

    @Override
    public void onClose(MenuCloseEvent e) {}

}
