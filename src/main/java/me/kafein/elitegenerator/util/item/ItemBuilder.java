package me.kafein.elitegenerator.util.item;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.util.ColorSerializer;
import me.kafein.elitegenerator.util.material.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemBuilder extends ItemStack {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private NBTItem nbtItem;

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.nbtItem = new NBTItem(itemStack);
    }

    public ItemBuilder(final String materialName) {
        final Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial(materialName);
        if (!optionalXMaterial.isPresent()) throw new NullPointerException("Material is not exists!");
        this.itemStack = optionalXMaterial.get().parseItem();
        this.itemMeta = itemStack.getItemMeta();
        this.nbtItem = new NBTItem(itemStack);
    }

    public String getName() {
        return itemMeta.getDisplayName();
    }

    public ItemBuilder setName(final String var) {
        if (var == null) return this;
        itemMeta.setDisplayName(ColorSerializer.serialize(var));
        return this;
    }

    public List<String> getLore() {
        return itemMeta.getLore();
    }

    public ItemBuilder clearLore() {
        itemMeta.setLore(new ArrayList<>());
        return this;
    }

    public ItemBuilder setLore(final List<String> list) {
        if (list == null || list.isEmpty()) return this;
        list.replaceAll(ColorSerializer::serialize);
        itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder setLoreLine(final int line, final String var) {
        if (itemMeta.getLore() == null || itemMeta.getLore().size() < line) return this;
        itemMeta.getLore().set(line, ColorSerializer.serialize(var));
        return this;
    }

    public ItemBuilder addLore(final String... var) {
        if (var == null) return this;
        if (itemMeta.getLore() == null) {
            Arrays.stream(var).forEach(ColorSerializer::serialize);
            itemMeta.setLore(Arrays.asList(var));
        }else Arrays.stream(var).forEach(e -> itemMeta.getLore().add(ColorSerializer.serialize(e)));

        return this;
    }

    public ItemBuilder setEnchant(final List<String> enchantList) {

        if (enchantList == null || enchantList.isEmpty()) return this;

        for (String enchant : enchantList) {

            final String[] splitter = enchant.split(": ");
            final Enchantment enchantment = Enchantment.getByName(splitter[0]);
            if (enchantment == null) continue;
            itemMeta.addEnchant(enchantment, Integer.parseInt(splitter[1]), true);

        }

        return this;

    }

    public ItemBuilder setGlow(final boolean var) {
        if (var) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }else itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setSkullOwner(final String player) {

        try{
            SkullMeta im = (SkullMeta) itemMeta;
            im.setOwner(player);
        }catch(ClassCastException expected){}

        return this;

    }

    public ItemBuilder setString(final String key, final String value) {
        nbtItem.setString(key, value);
        return this;
    }

    public ItemBuilder setInteger(final String key, final int value) {
        nbtItem.setInteger(key, value);
        return this;
    }

    public ItemBuilder setBoolean(final String key, final boolean value) {
        nbtItem.setBoolean(key, value);
        return this;
    }

    public String getString(final String key) {
        return nbtItem.getString(key);
    }

    public int getInteger(final String key) {
        return nbtItem.getInteger(key);
    }

    public boolean getBoolean(final String key) {
        return nbtItem.getBoolean(key);
    }

    public NBTItem getNBTItem() {
        return nbtItem;
    }

    public ItemStack toItemStack() {
        itemStack.setItemMeta(itemMeta);
        nbtItem.mergeNBT(itemStack);
        return itemStack;
    }

}
