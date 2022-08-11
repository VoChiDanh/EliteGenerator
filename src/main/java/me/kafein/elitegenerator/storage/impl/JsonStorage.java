package me.kafein.elitegenerator.storage.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChest;
import me.kafein.elitegenerator.generator.feature.boost.Boost;
import me.kafein.elitegenerator.generator.feature.calendar.CalendarSerializer;
import me.kafein.elitegenerator.storage.Storage;
import me.kafein.elitegenerator.user.User;
import me.kafein.elitegenerator.util.json.JsonObject;
import me.kafein.elitegenerator.util.location.LocationSerializer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class JsonStorage implements Storage {

    final private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    final private Plugin plugin;

    public JsonStorage(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public User loadUser(UUID userUUID) {

        final File file = new File(plugin.getDataFolder(), "storage/user/" + userUUID.toString() + ".json");

        if (!file.exists()) {

            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final User user = new User(userUUID);

            final JsonObject jsonObject = new JsonObject(gson);
            jsonObject.addObject("user", user);
            jsonObject.saveToFile(file);

            return user;

        }

        User user = new User(userUUID);

        final JsonObject jsonObject = new JsonObject(gson, file).getIJsonObject("user");
        List<String> list = jsonObject.getObject("generators", new TypeToken<List<String>>(){}.getType());
        list.forEach(uuidString ->{
            UUID uuid = UUID.fromString(uuidString);
            user.addGenerator(uuid);
        });

        return user;

    }

    @Override
    public void saveUser(User user) {

        final UUID userUUID = user.getUserUUID();

        final File file = new File(plugin.getDataFolder(), "storage/user/" + userUUID.toString() + ".json");
        if (!file.exists()) {

            plugin.getLogger().warning("Player file is not found! '" + userUUID.toString() + "'");
            return;

        }

        JsonObject jsonObject = new JsonObject(gson);
        jsonObject.addObject("user", user);
        jsonObject.saveToFile(file);

    }

    @Override
    public void putGeneratorToUser(UUID userUUID, UUID generatorUUID) {

        CompletableFuture.runAsync(() -> {
            User user = loadUser(userUUID);
            user.addGenerator(generatorUUID);
            saveUser(user);
        });

    }

    @Nullable
    @Override
    public Generator loadGenerator(final UUID generatorUUID) {

        final File file = new File(plugin.getDataFolder(), "storage/generator/" + generatorUUID.toString() + ".json");
        if (!file.exists()) return null;

        final JsonObject jsonObject = new JsonObject(gson, file);

        final Generator generator = new Generator(
                LocationSerializer.deserialize(jsonObject.getString("islandLocation"))
                , UUID.fromString(jsonObject.getString("generatorUUID"))
                , jsonObject.getString("generatorName")
                , LocationSerializer.deserialize(jsonObject.getString("generatorLocation"))
                , jsonObject.getNumber("level").intValue());

        if (jsonObject.has("hologramEnabled")) generator.setHologramEnabled(jsonObject.getBoolean("hologramEnabled"));
        generator.setCreateDate(jsonObject.getString("createDate"));
        generator.changeOwnerUUID(UUID.fromString(jsonObject.getString("ownerUUID")));

        generator.setAutoBreakBuyed(jsonObject.getBoolean("autoBreakBuyed"));
        generator.setAutoBreakEnabled(jsonObject.getBoolean("autoBreakEnabled"));
        generator.setAutoPickupBuyed(jsonObject.getBoolean("autoPickupBuyed"));
        generator.setAutoPickupEnabled(jsonObject.getBoolean("autoPickupEnabled"));
        generator.setAutoSmeltBuyed(jsonObject.getBoolean("autoSmeltBuyed"));
        generator.setAutoSmeltEnabled(jsonObject.getBoolean("autoSmeltEnabled"));
        generator.setAutoChestBuyed(jsonObject.getBoolean("autoChestBuyed"));
        final String autoChestLocation = jsonObject.getString("autoChestLocation");
        if (!autoChestLocation.equals("none")) generator.setAutoChest(new AutoChest(LocationSerializer.deserialize(autoChestLocation)));

        final String boost = jsonObject.getString("boost");
        if (!boost.equals("none")) {
            final String[] splitter = boost.split("_");
            final int level = Integer.parseInt(splitter[0]);
            final long time = CalendarSerializer.deserialize(Long.parseLong(splitter[1]));
            if (time > 0) generator.setBoost(new Boost(level, time));
        }

        generator.setGeneratorMembers(jsonObject.getObject("members", new TypeToken<Map<UUID, GeneratorMember>>(){}.getType()));

        return generator;

    }

    @Override
    public void saveGenerator(final Generator generator) {

        final File file = new File(plugin.getDataFolder(), "storage/generator/" + generator.getGeneratorUUID().toString() + ".json");
        if (!file.exists()) {

            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        final JsonObject jsonObject = new JsonObject(gson);

        jsonObject.add("generatorUUID", generator.getGeneratorUUID().toString());
        jsonObject.add("generatorName", generator.getGeneratorName());
        jsonObject.add("createDate", generator.getCreateDate());
        jsonObject.add("generatorLocation", LocationSerializer.serialize(generator.getGeneratorLocation(), true));
        jsonObject.add("islandLocation", LocationSerializer.serialize(generator.getIslandLocation(), true));
        jsonObject.add("ownerUUID", generator.getOwnerUUID().toString());

        jsonObject.add("level", generator.getLevel());
        jsonObject.add("boost", generator.hasBoost() ? generator.getBoost().getLevel() + "_" + CalendarSerializer.serialize(generator.getBoost().getTime()) : "none");
        jsonObject.add("hologramEnabled", generator.isHologramEnabled());
        jsonObject.add("autoBreakBuyed", generator.isAutoBreakBuyed());
        jsonObject.add("autoBreakEnabled", generator.isAutoBreakEnabled());
        jsonObject.add("autoPickupBuyed", generator.isAutoPickupBuyed());
        jsonObject.add("autoPickupEnabled", generator.isAutoPickupEnabled());
        jsonObject.add("autoSmeltBuyed", generator.isAutoSmeltBuyed());
        jsonObject.add("autoSmeltEnabled", generator.isAutoSmeltEnabled());
        jsonObject.add("autoChestBuyed", generator.isAutoChestBuyed());
        jsonObject.add("autoChestLocation", generator.isAutoChestEnabled() ? LocationSerializer.serialize(generator.getAutoChest().getChestLocation(), true) : "none");

        jsonObject.addObject("members", generator.getGeneratorMembersMap());

        jsonObject.saveToFile(file);

    }

    @Override
    public void deleteGenerator(UUID generatorUUID) {

        final File file = new File(plugin.getDataFolder(), "storage/generator/" + generatorUUID.toString() + ".json");
        if (file.exists()) file.delete();

    }

}
