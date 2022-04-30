package me.kafein.elitegenerator.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {

    public static String serialize(final Location location, boolean withWorld) {
        return (withWorld ? location.getWorld().getName() + ": " : "")
                + location.getBlockX() + ", "
                + location.getBlockY() + ", "
                + location.getBlockZ();
    }

    public static Location deserialize(final String var) {
        final String[] worldSplitter = var.split(": ");
        final String[] locSplitter = worldSplitter[1].split(", ");
        return new Location(Bukkit.getWorld(worldSplitter[0])
                , Integer.parseInt(locSplitter[0])
                , Integer.parseInt(locSplitter[1])
                , Integer.parseInt(locSplitter[2]));
    }

}
