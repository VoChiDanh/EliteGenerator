package me.kafein.elitegenerator.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import me.kafein.elitegenerator.generator.feature.upgrade.Upgrade;
import me.kafein.elitegenerator.generator.feature.upgrade.UpgradeManager;
import me.kafein.elitegenerator.generator.feature.upgrade.UpgradeType;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class UpgradeMenu extends Menu {

    final private FileManager fileManager = EliteGenerator.getInstance().getFileManager();
    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private FeatureManager featureManager = generatorManager.getFeatureManager();
    final private HologramHook hologramHook = EliteGenerator.getInstance().getHookManager().getHologramHook();
    final private UpgradeManager upgradeManager = featureManager.getUpgradeManager();

    public UpgradeMenu(String title, int slot, FileConfig fileConfig) {
        super(title, slot, fileConfig);
    }

    public UpgradeMenu(String title, InventoryType inventoryType, FileConfig fileConfig) {
        super(title, inventoryType, fileConfig);
    }

    @Override
    public void loadItems() {
    }

    public void openMenu(Player player, Generator generator) {
        if (!isEnabled()) return;

        final Inventory inventory = this.clone();

        final FileConfig fileConfig = getFileConfig();

        for (String itemName : fileConfig.getConfigurationSection("menu.items").getKeys(false)) {

            final String prefix = "menu.items." + itemName + ".";

            final ItemBuilder itemBuilder = loadItem(
                    (itemName.equalsIgnoreCase("level") ? itemName + ".level_" + generator.getLevel() : itemName),
                    player.getName(), generator)
                    .setString("generator", generator.getGeneratorUUID().toString());

            inventory.setItem(fileConfig.getInt(prefix + "slot"), itemBuilder.toItemStack());

        }

        if (fileConfig.getBoolean("menu.fill")) fill(inventory);

        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));

    }

    @EventHandler
    public void onClick(MenuClickEvent e) {

        if (!e.getMenu().getTitle().equals(getTitle())) return;

        final Player player = e.getPlayer();

        final FileConfig fileConfig = getFileConfig();

        final NBTItem nbtItem = new NBTItem(e.getItem());

        if (!nbtItem.hasKey("generator")) return;

        final Generator generator = generatorManager.getGenerator(UUID.fromString(nbtItem.getString("generator")));
        if (generator == null) {
            player.closeInventory();
            return;
        }

        if (e.getSlot() == fileConfig.getInt("menu.items.level.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            final int nextLevel = generator.getLevel() + 1;

            final Upgrade upgrade = upgradeManager.getLevelUpgrade(nextLevel);

            if (upgrade == null) return;

            if (!upgrade.hasRequirements(player, generator)) {
                player.closeInventory();
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
                return;
            }

            upgrade.applyRequirements(player, generator);
            upgrade.apply(generator);
            hologramHook.reloadHologram(generator);
            openMenu(player, generator);
            player.sendMessage(fileManager.getMessage("generator.upgrade.generatorLevelUpgrade")
                    .replace("%level%", Integer.toString(nextLevel)));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.boost.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            final BoostMenu boostMenu = (BoostMenu) getMenuManager().getMenu(MenuManager.MenuType.BOOST);
            boostMenu.openMenu(player, generator);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoBreak.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (generator.isAutoBreakBuyed()) return;

            final Upgrade upgrade = upgradeManager.getUpgrade(UpgradeType.AUTOBREAK);

            if (!upgrade.hasRequirements(player, generator)) {
                player.closeInventory();
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
                return;
            }

            upgrade.applyRequirements(player, generator);
            upgrade.apply(generator);
            hologramHook.reloadHologram(generator);
            openMenu(player, generator);
            player.sendMessage(fileManager.getMessage("generator.upgrade.generatorAutoBreakUpgrade"));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoPickup.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (generator.isAutoPickupBuyed()) return;

            final Upgrade upgrade = upgradeManager.getUpgrade(UpgradeType.AUTOPICKUP);

            if (!upgrade.hasRequirements(player, generator)) {
                player.closeInventory();
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
                return;
            }

            upgrade.applyRequirements(player, generator);
            upgrade.apply(generator);
            hologramHook.reloadHologram(generator);
            openMenu(player, generator);
            player.sendMessage(fileManager.getMessage("generator.upgrade.generatorAutoPickupUpgrade"));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoSmelt.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (generator.isAutoSmeltBuyed()) return;

            final Upgrade upgrade = upgradeManager.getUpgrade(UpgradeType.AUTOSMELT);

            if (!upgrade.hasRequirements(player, generator)) {
                player.closeInventory();
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
                return;
            }

            upgrade.applyRequirements(player, generator);
            upgrade.apply(generator);
            hologramHook.reloadHologram(generator);
            openMenu(player, generator);
            player.sendMessage(fileManager.getMessage("generator.upgrade.generatorAutoSmeltUpgrade"));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoChest.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (generator.isAutoChestBuyed()) return;

            final Upgrade upgrade = upgradeManager.getUpgrade(UpgradeType.AUTOCHEST);

            if (!upgrade.hasRequirements(player, generator)) {
                player.closeInventory();
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotUpgraded"));
                return;
            }

            upgrade.applyRequirements(player, generator);
            upgrade.apply(generator);
            hologramHook.reloadHologram(generator);
            openMenu(player, generator);
            player.sendMessage(fileManager.getMessage("generator.upgrade.generatorAutoChestUpgrade"));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.close.slot")) {

            final GeneratorMenu generatorMenu = (GeneratorMenu) EliteGenerator.getInstance().getMenuManager().getMenu(MenuManager.MenuType.GENERATOR);
            generatorMenu.openMenu(e.getPlayer(), generator);

        }

    }

    @Override
    public void onOpen(MenuOpenEvent e) {
    }

    @Override
    public void onClose(MenuCloseEvent e) {
    }

}

