package me.kafein.elitegenerator.menu.listener;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.PluginManager;

public class InventoryListener implements Listener {

    final private PluginManager pluginManager = Bukkit.getPluginManager();
    final private MenuManager menuManager = EliteGenerator.getInstance().getMenuManager();

    @EventHandler
    public void onClick(final InventoryClickEvent e) {

        if (!menuManager.containsMenuTitle(e.getView().getTitle())) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) return;

        final Player player = (Player) e.getWhoClicked();

        if (e.getInventory().equals(player.getInventory())) return;

        final Menu menu = menuManager.getMenu(menuManager.getMenuName(e.getView().getTitle()));

        final MenuClickEvent menuClickEvent = new MenuClickEvent(e, menu, player, e.getCurrentItem(), e.getSlot(), e.getClick());
        pluginManager.callEvent(menuClickEvent);

    }

    @EventHandler
    public void onOpen(final InventoryOpenEvent e) {

        if (!menuManager.containsMenuTitle(e.getView().getTitle())) return;

        final Player player = (Player) e.getPlayer();

        final Menu menu = menuManager.getMenu(menuManager.getMenuName(e.getView().getTitle()));

        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(e, menu, player);
        pluginManager.callEvent(menuOpenEvent);
        menuManager.addMenuPlayer(player.getUniqueId(), menu.getTitle());

    }

    @EventHandler
    public void onClose(final InventoryCloseEvent e) {

        if (!menuManager.containsMenuTitle(e.getView().getTitle())) return;

        final Player player = (Player) e.getPlayer();

        final Menu menu = menuManager.getMenu(menuManager.getMenuName(e.getView().getTitle()));

        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(e, menu, player);
        pluginManager.callEvent(menuCloseEvent);
        menuManager.removeMenuPlayer(player.getUniqueId());

    }

}
