package me.kafein.elitegenerator.event;

import me.kafein.elitegenerator.generator.Generator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

public class GeneratorBreakEvent extends Event {

    final private static HandlerList handlerList = new HandlerList();

    final private Player player;
    final private Generator generator;
    final private Block block;
    final private boolean isAutoBreak;
    final private boolean isAutoPickup;
    final private boolean isAutoSmelt;
    final private boolean isAutoChest;
    private boolean cancelled;

    public GeneratorBreakEvent(
            final Player player
            , final Generator generator
            , final Block block
            , final boolean isAutoBreak
            , final boolean isAutoPickup
            , final boolean isAutoSmelt
            , final boolean isAutoChest
            , final boolean cancelled) {

        this.player = player;
        this.generator = generator;
        this.block = block;
        this.isAutoBreak = isAutoBreak;
        this.isAutoPickup = isAutoPickup;
        this.isAutoSmelt = isAutoSmelt;
        this.isAutoChest = isAutoChest;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public Generator getGenerator() {
        return generator;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isAutoBreak() {
        return isAutoBreak;
    }

    public boolean isAutoPickup() {
        return isAutoPickup;
    }

    public boolean isAutoSmelt() {
        return isAutoSmelt;
    }

    public boolean isAutoChest() {
        return isAutoChest;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
