package me.kafein.elitegenerator.menu.event;

import me.kafein.elitegenerator.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuCloseEvent extends Event {

    final private static HandlerList handlerList = new HandlerList();

    final private InventoryCloseEvent openEvent;
    final private Menu menu;
    final private Player player;

    public MenuCloseEvent(final InventoryCloseEvent openEvent, final Menu menu, final Player player) {
        this.openEvent = openEvent;
        this.menu = menu;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public InventoryCloseEvent getCloseEvent() {
        return openEvent;
    }

    public Menu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
