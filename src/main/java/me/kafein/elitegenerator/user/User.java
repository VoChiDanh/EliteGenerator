package me.kafein.elitegenerator.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    final private UUID userUUID;
    final private List<UUID> generators = new ArrayList<>();

    public User(final UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Nullable
    public UUID getGenerator(final int generatorListIndex) {
        return generators.get(generatorListIndex);
    }

    public void addGenerator(final UUID generatorUUID) {
        generators.add(generatorUUID);
    }

    public void removeGenerator(final UUID generatorUUID) {
        generators.remove(generatorUUID);
    }

    public boolean containsGenerator(final UUID generatorUUID) {
        return generators.contains(generatorUUID);
    }

    public int getGeneratorListSize() {
        return generators.size();
    }

    public boolean hasGenerator() {
        return !generators.isEmpty();
    }

    public List<UUID> getGenerators() {
        return new ArrayList<>(generators);
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(userUUID);
    }

    public UUID getUserUUID() {
        return userUUID;
    }

}
