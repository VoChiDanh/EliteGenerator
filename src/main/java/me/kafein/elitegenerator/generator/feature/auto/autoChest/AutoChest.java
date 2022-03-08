package me.kafein.elitegenerator.generator.feature.auto.autoChest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public class AutoChest {

    final private Location chestLocation;

    public AutoChest(final Location chestLocation) {
        this.chestLocation = chestLocation;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public Inventory getInventory() {
        return ((Chest) chestLocation.getBlock().getState()).getInventory();
    }

    public boolean isChestBreaked() {
        return !(chestLocation.getBlock().getType().equals(Material.CHEST)
                || chestLocation.getBlock().getType().equals(Material.TRAPPED_CHEST));
    }

}
