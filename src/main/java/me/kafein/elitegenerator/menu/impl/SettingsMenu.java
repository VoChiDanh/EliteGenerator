package me.kafein.elitegenerator.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.auto.autoBreak.AutoBreakManager;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChestManager;
import me.kafein.elitegenerator.generator.feature.condition.GeneratorDeleteCondition;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.menu.impl.member.MembersMenu;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class SettingsMenu extends Menu {

    final private FileManager fileManager = EliteGenerator.getInstance().getFileManager();
    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private FeatureManager featureManager = generatorManager.getFeatureManager();
    final private HologramHook hologramHook = EliteGenerator.getInstance().getHookManager().getHologramHook();
    final private AutoChestManager autoChestManager = featureManager.getAutoChestManager();
    final private AutoBreakManager autoBreakManager = featureManager.getAutoBreakManager();

    public SettingsMenu(String title, int size, FileConfig fileConfig) {
        super(title, size, fileConfig);
    }

    @Override
    public void loadItems() {

    }

    public void openMenu(final Player player, final Generator generator) {

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

        final Player player = e.getPlayer();

        final FileConfig fileConfig = getFileConfig();

        final NBTItem nbtItem = new NBTItem(e.getItem());

        if (!nbtItem.hasKey("generator")) return;

        final Generator generator = generatorManager.getGenerator(UUID.fromString(nbtItem.getString("generator")));
        if (generator == null) {
            player.closeInventory();
            return;
        }

        if (e.getSlot() == fileConfig.getInt("menu.items.members.slot")) {

            if (!player.getUniqueId().equals(generator.getOwnerUUID())) return;

            final MembersMenu menu = (MembersMenu) getMenuManager().getMenu(MenuManager.MenuType.MEMBERS);
            menu.openMenu(player, generator, 1);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoBreak.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            final UUID generatorUUID = generator.getGeneratorUUID();

            if (!generator.isAutoBreakBuyed()) return;

            if (generator.isAutoBreakEnabled()) {
                autoBreakManager.removeAutoBreakerGenerator(generatorUUID);
                generator.setAutoBreakEnabled(false);
            } else {
                generator.setAutoBreakEnabled(true);
                autoBreakManager.addAutoBreakerGenerator(generatorUUID);
                autoBreakManager.startRunnable();
            }

            openMenu(player, generator);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoPickup.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (!generator.isAutoPickupBuyed()) return;

            if (generator.isAutoPickupEnabled()) generator.setAutoPickupEnabled(false);
            else generator.setAutoPickupEnabled(true);

            openMenu(player, generator);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoSmelt.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (!generator.isAutoSmeltBuyed()) return;

            if (generator.isAutoSmeltEnabled()) generator.setAutoSmeltEnabled(false);
            else generator.setAutoSmeltEnabled(true);

            openMenu(player, generator);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.autoChest.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (!generator.isAutoChestBuyed()) return;

            final UUID generatorUUID = generator.getGeneratorUUID();

            if (autoChestManager.containsAutoChestPlayer(player.getUniqueId())) return;

            if (generator.isAutoChestEnabled()) {
                generator.clearAutoChest();
            } else {
                generator.clearAutoChest();
                autoChestManager.addAutoChestPlayer(player.getUniqueId(), generatorUUID);
            }
            player.closeInventory();

        } else if (e.getSlot() == fileConfig.getInt("menu.items.hologram.slot")) {

            if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

            if (generator.isHologramEnabled()) {
                generator.setHologramEnabled(false);
                hologramHook.deleteHologram(generator);
            }
            else {
                generator.setHologramEnabled(true);
                hologramHook.loadHologram(generator);
            }

            openMenu(player, generator);

        } else if (e.getSlot() == fileConfig.getInt("menu.items.delete.slot")) {

            if (!player.getUniqueId().equals(generator.getOwnerUUID())) return;
            if (!GeneratorDeleteCondition.check(player, generator)) {
                player.sendMessage(fileManager.getMessage("generator.generatorIsNotDeleted"));
                return;
            }

            player.closeInventory();

            final ItemStack generatorItem = generatorManager.getGeneratorItem().create(
                    generator.getLevel(),
                    generator.isAutoBreakBuyed(),
                    generator.isAutoPickupBuyed(),
                    generator.isAutoSmeltBuyed(),
                    generator.isAutoChestBuyed());

            if (!generatorManager.deleteGenerator(generator.getGeneratorUUID())) return;

            final Map<Integer, ItemStack> itemStackMap = player.getInventory().addItem(generatorItem);
            if (!itemStackMap.isEmpty()) player.getWorld().dropItemNaturally(player.getLocation(), generatorItem);

            player.sendMessage(fileManager.getMessage("generator.generatorDeleted"));

        } else if (e.getSlot() == fileConfig.getInt("menu.items.close.slot")) {

            final GeneratorMenu generatorMenu = (GeneratorMenu) getMenuManager().getMenu(MenuManager.MenuType.GENERATOR);
            generatorMenu.openMenu(player, generator);

        }

    }

    @Override
    public void onOpen(MenuOpenEvent e) {
    }

    @EventHandler
    public void onClose(MenuCloseEvent e) {
    }

}
