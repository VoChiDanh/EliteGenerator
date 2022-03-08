package me.kafein.elitegenerator.generator.feature.auto.autoPickup;

import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.auto.autoSmelt.AutoSmelt;
import me.kafein.elitegenerator.generator.feature.auto.autoSmelt.AutoSmeltManager;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AutoPickupManager {

    final private AutoSmeltManager autoSmeltManager;

    final private Map<Location, AutoPickup> autoPickups = new HashMap<>();

    public AutoPickupManager(final FeatureManager featureManager) {
        autoSmeltManager = featureManager.getAutoSmeltManager();
    }

    public void pickup(final Location location) {

        final AutoPickup autoPickup = autoPickups.get(location);

        final ItemStack handItem = autoPickup.getHandItem();
        if (autoPickup.isAutoSmeltEnabled()
                && !(handItem != null
                && handItem.hasItemMeta()
                && handItem.getItemMeta().hasEnchants()
                && handItem.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))) {

            final AutoSmelt autoSmelt = autoSmeltManager.getAutoSmelt(autoPickup.getDropItem().getType());
            if (autoSmelt != null) autoPickup.setDropItem(new ItemStack(autoSmelt.getTo()));

        }

        if (autoPickup.getInventory() == null)
            location.getWorld().dropItem(location.clone().add(0, 1, 0), autoPickup.getDropItem());
        else {
            HashMap<Integer, ItemStack> items = autoPickup.getInventory().addItem(autoPickup.getDropItem());
            if (!items.isEmpty()) location.getWorld().dropItem(location.clone().add(0, 1, 0), autoPickup.getDropItem());
        }

    }

    @Nullable
    public AutoPickup getAutoPickup(final Location location) {
        return autoPickups.get(location);
    }

    public void addAutoPickup(final Location location, final AutoPickup autoPickup) {
        autoPickups.put(location, autoPickup);
    }

    public void removeAutoPickup(final Location location) {
        autoPickups.remove(location);
    }

    public boolean containsAutoPickup(final Location location) {
        return autoPickups.containsKey(location);
    }

}
