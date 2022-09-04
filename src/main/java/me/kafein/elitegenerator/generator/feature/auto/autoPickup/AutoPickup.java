package me.kafein.elitegenerator.generator.feature.auto.autoPickup;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class AutoPickup {

    final private Inventory inventory;
    final private ItemStack handItem;

    private ItemStack dropItem;
    private boolean autoSmeltEnabled;

    public AutoPickup(final Inventory inventory, final ItemStack handItem) {
        this.inventory = inventory;
        this.handItem = handItem;
    }

    @Nullable
    public Inventory getInventory() {
        return inventory;
    }

    @Nullable
    public ItemStack getHandItem() {
        return handItem;
    }

    public ItemStack getDropItem() {
        return dropItem;
    }

    public void setDropItem(ItemStack dropItem) {
        this.dropItem = dropItem;
    }

    public boolean isAutoSmeltEnabled() {
        return autoSmeltEnabled;
    }

    public void setAutoSmeltEnabled(boolean autoSmeltEnabled) {
        this.autoSmeltEnabled = autoSmeltEnabled;
    }

}
