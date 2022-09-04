package me.kafein.elitegenerator.event;

import me.kafein.elitegenerator.generator.Generator;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GeneratorDeleteEvent extends Event implements Cancellable {

    final private static HandlerList handlerList = new HandlerList();

    final private Generator generator;
    private boolean cancelled;

    public GeneratorDeleteEvent(final Generator generator, final boolean cancelled) {
        this.generator = generator;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Generator getGenerator() {
        return generator;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
