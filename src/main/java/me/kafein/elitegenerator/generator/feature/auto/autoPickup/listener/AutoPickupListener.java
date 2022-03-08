package me.kafein.elitegenerator.generator.feature.auto.autoPickup.listener;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.AutoPickup;
import me.kafein.elitegenerator.generator.feature.auto.autoPickup.AutoPickupManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class AutoPickupListener implements Listener {

    final private AutoPickupManager autoPickupManager = EliteGenerator.getInstance().getGeneratorManager().getFeatureManager().getAutoPickupManager();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(final ItemSpawnEvent e) {

        if (e.getEntity() == null) return;

        final Location location = e.getLocation().getBlock().getLocation();

        if (!autoPickupManager.containsAutoPickup(location)) return;

        final AutoPickup autoPickup = autoPickupManager.getAutoPickup(location);

        e.setCancelled(true);

        autoPickup.setDropItem(e.getEntity().getItemStack());
        autoPickupManager.pickup(location);

    }

}
