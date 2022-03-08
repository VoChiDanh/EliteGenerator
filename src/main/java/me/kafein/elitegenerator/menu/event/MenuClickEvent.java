package me.kafein.elitegenerator.menu.event;

import me.kafein.elitegenerator.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuClickEvent extends Event {

    final private static HandlerList handlerList = new HandlerList();

    final private InventoryClickEvent clickEvent;
    final private Menu menu;
    final private Player player;
    final private ItemStack item;
    final private int slot;
    final private ClickType clickType;

    public MenuClickEvent(final InventoryClickEvent clickEvent, final Menu menu, final Player player, final ItemStack item, final int slot, final ClickType clickType) {
        this.clickEvent = clickEvent;
        this.menu = menu;
        this.player = player;
        this.item = item;
        this.slot = slot;
        this.clickType = clickType;
    }

    public InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    public Menu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
