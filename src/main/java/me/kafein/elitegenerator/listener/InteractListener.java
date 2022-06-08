package me.kafein.elitegenerator.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChest;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChestManager;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.impl.GeneratorMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private AutoChestManager autoChestManager = generatorManager.getFeatureManager().getAutoChestManager();
    final private MenuManager menuManager = EliteGenerator.getInstance().getMenuManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(final PlayerInteractEvent e) {

        final Player player = e.getPlayer();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (!autoChestManager.containsAutoChestPlayer(player.getUniqueId())) return;

            if (e.getClickedBlock().getType() != Material.CHEST) return;

            final Generator generator = generatorManager.getGenerator(autoChestManager.getGeneratorUUIDWithPlayer(player.getUniqueId()));
            generator.setAutoChest(new AutoChest(e.getClickedBlock().getLocation()));

            autoChestManager.removeAutoChestPlayer(player.getUniqueId());

            return;

        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (player.isSneaking()) {

                final Location location = e.getClickedBlock().getLocation();

                if (generatorManager.containsGeneratorLocation(location)) {
                    final GeneratorMenu generatorMenu = (GeneratorMenu) menuManager.getMenu(MenuManager.MenuType.GENERATOR);
                    generatorMenu.openMenu(player, generatorManager.getGenerator(location));
                    e.setCancelled(true);
                    return;
                }

            }

            final ItemStack itemStack = e.getItem();

            if (itemStack == null) return;
            if (!itemStack.hasItemMeta()) return;

            final ItemMeta itemMeta = itemStack.getItemMeta();

            if (!itemMeta.hasDisplayName()) return;

            final NBTItem nbtItem = new NBTItem(itemStack);
            if (!nbtItem.hasKey("generatorItem")) return;

            e.setCancelled(true);

            final boolean placed = generatorManager.placeGenerator(
                    faceSerializer(e.getClickedBlock().getLocation(), e.getBlockFace())
                    , player
                    , nbtItem.getInteger("level")
                    , nbtItem.getBoolean("autoBreak")
                    , nbtItem.getBoolean("autoPickup")
                    , nbtItem.getBoolean("autoSmelt")
                    , nbtItem.getBoolean("autoChest"));

            if (placed) {
                if (itemStack.getAmount() == 1) player.getInventory().remove(itemStack);
                else itemStack.setAmount(itemStack.getAmount() - 1);
            }

            player.updateInventory();

        }

    }

    public Location faceSerializer(final Location location, final BlockFace blockFace) {

        switch (blockFace) {
            case UP:
                return (location.add(0, 1, 0));
            case NORTH:
                return (location.add(0, 0, -1));
            case WEST:
                return (location.add(-1, 0, 0));
            case EAST:
                return (location.add(1, 0, 0));
            case SOUTH:
                return (location.add(0, 0, 1));
        }

        return location;

    }

}
