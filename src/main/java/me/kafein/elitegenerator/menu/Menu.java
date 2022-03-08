package me.kafein.elitegenerator.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import me.kafein.elitegenerator.util.placeholder.PlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public abstract class Menu implements InventoryHolder, Listener {

    private MenuManager menuManager = null;

    private Plugin plugin;

    private Inventory inventory;
    private InventoryType inventoryType;
    private String title;
    private int slot;
    private FileConfig fileConfig;

    public Menu(final String title, final int slot, final FileConfig fileConfig) {
        this.title = title;
        this.slot = slot;
        this.inventory = Bukkit.createInventory(null, slot, getTitle());
        this.inventoryType = InventoryType.CHEST;
        this.fileConfig = fileConfig;
    }

    public Menu(final String title, final InventoryType inventoryType, final FileConfig fileConfig) {
        this.title = title;
        this.slot = inventoryType.getDefaultSize();
        this.inventory = Bukkit.createInventory(null, inventoryType, getTitle());
        this.inventoryType = inventoryType;
        this.fileConfig = fileConfig;
    }

    public abstract void loadItems();

    public abstract void onClick(final MenuClickEvent e);

    public abstract void onOpen(final MenuOpenEvent e);

    public abstract void onClose(final MenuCloseEvent e);

    public ItemBuilder loadItem(final String property, final String playerName, final Generator generator) {

        final String prefix = "menu.items." + property + ".";

        ItemBuilder itemBuilder;

        final String materialName = fileConfig.getString(prefix + "material");

        if (materialName.startsWith("HEAD: ")) {

            Material material = Material.getMaterial("PLAYER_HEAD");

            if (material == null) {

                itemBuilder = new ItemBuilder(Material.getMaterial("SKULL_ITEM"), fileConfig.getInt(prefix + "amount"), 3);

            } else {

                itemBuilder = new ItemBuilder(material, fileConfig.getInt(prefix + "amount"), 0);

            }

            final String skullOwner = materialName.split(": ")[1];
            itemBuilder.setSkullOwner(skullOwner.equalsIgnoreCase("%player%") ? playerName : skullOwner);

        } else {

            itemBuilder = new ItemBuilder(
                    Material.getMaterial(materialName),
                    fileConfig.getInt(prefix + "amount"),
                    fileConfig.getInt(prefix + "data"));

        }

        if (fileConfig.containsKey(prefix + "name"))
            itemBuilder.setName(PlaceHolder.replace(fileConfig.getString(prefix + "name"), generator, playerName));

        if (fileConfig.containsKey(prefix + "lore")) {
            final List<String> loreList = fileConfig.getStringList(prefix + "lore");
            loreList.replaceAll(lore -> PlaceHolder.replace(lore, generator, playerName));
            itemBuilder.setLore(loreList);
        }

        itemBuilder.setGlow(fileConfig.getBoolean(prefix + "glow"));
        if (fileConfig.containsKey(prefix + "enchant"))
            itemBuilder.setEnchant(fileConfig.getStringList(prefix + "enchant"));

        return itemBuilder;

    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public String getDefaultTitle() {
        return title;
    }

    public void fill(final Inventory inventory) {
        final ItemBuilder itemBuilder;
        Material material = Material.getMaterial("GRAY_STAINED_GLASS_PANE");
        if (material == null) itemBuilder = new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE"), 1, 7);
        else itemBuilder = new ItemBuilder(material, 1, 0);
        for (int i = 0; i < slot; i++)
            if (inventory.getItem(i) == null) inventory.setItem(i, itemBuilder.toItemStack());
    }

    public ItemStack getFillItem() {
        final ItemBuilder itemBuilder;
        Material material = Material.getMaterial("GRAY_STAINED_GLASS_PANE");
        if (material == null) itemBuilder = new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE"), 1, 7);
        else itemBuilder = new ItemBuilder(material, 1, 0);
        return itemBuilder.toItemStack();
    }

    public void register(final Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Inventory clone() {
        return Bukkit.createInventory(null, slot, ChatColor.translateAlternateColorCodes('&', title));
    }

    protected MenuManager getMenuManager() {
        if (menuManager == null) menuManager = EliteGenerator.getInstance().getMenuManager();
        return menuManager;
    }

}
