package me.kafein.elitegenerator.generator;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.event.GeneratorDeleteEvent;
import me.kafein.elitegenerator.generator.feature.FeatureManager;
import me.kafein.elitegenerator.generator.feature.auto.autoBreak.AutoBreakManager;
import me.kafein.elitegenerator.generator.feature.boost.task.BoostRunnable;
import me.kafein.elitegenerator.generator.feature.calendar.CalendarSerializer;
import me.kafein.elitegenerator.generator.feature.item.GeneratorItem;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import me.kafein.elitegenerator.hook.VaultHook;
import me.kafein.elitegenerator.hook.hologram.HologramHook;
import me.kafein.elitegenerator.hook.skyblock.SkyBlockHook;
import me.kafein.elitegenerator.storage.Storage;
import me.kafein.elitegenerator.user.User;
import me.kafein.elitegenerator.user.UserManager;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GeneratorManager {

    final private FileManager fileManager = EliteGenerator.getInstance().getFileManager();
    final private Storage storage = EliteGenerator.getInstance().getStorageManager().get();
    final private HologramHook hologramHook = EliteGenerator.getInstance().getHookManager().getHologramHook();
    final private SkyBlockHook skyBlockHook = EliteGenerator.getInstance().getHookManager().getSkyBlockHook();
    final private Permission permission = VaultHook.getPermission();
    final private FeatureManager featureManager;
    private UserManager userManager;

    final private Map<UUID, Generator> generators = new HashMap<>();
    final private Map<Location, UUID> generatorLocations = new HashMap<>();
    final private Map<Location, List<UUID>> generatorIslands = new HashMap<>();
    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    private GeneratorItem generatorItem = new GeneratorItem(fileManager.getFile(FileManager.ConfigFile.settings));
    private Material firstBlockMaterial = Material.getMaterial(fileManager.getFile(FileManager.ConfigFile.settings).getString("settings.generator.generator-first-material"));
    private boolean boostRunnableStarted;

    final private Plugin plugin;

    public GeneratorManager(final Plugin plugin) {
        this.plugin = plugin;
        featureManager = new FeatureManager(plugin);
        FileConfig fileConfig = fileManager.getFile(FileManager.ConfigFile.settings);
        fileConfig.getStringList("settings.generator.default-member-permissions").forEach(perm -> {
            boolean value = fileConfig.getBoolean("settings.generator.default-member-permissions." + perm);
            MemberPermission memberPermission = MemberPermission.valueOf(perm);
            memberPermission.setValue(value);
        });
    }

    public CompletableFuture<Void> saveGenerators() {

        return CompletableFuture.runAsync(() -> {

            if (generators.isEmpty()) return;

            generators.keySet().forEach(this::saveGenerator);

        });

    }

    public boolean placeGenerator(final Location location, final Player owner, final int level, final boolean autoBreak, final boolean autoPickup, final boolean autoSmelt, final boolean autoChest) {

        final UUID ownerUUID = owner.getUniqueId();
        final UUID generatorUUID = UUID.randomUUID();

        String worldName = location.getWorld().getName();
        List<String> worlds = fileManager.getFile(FileManager.ConfigFile.settings).getStringList("settings.generator.whitelist-worlds");
        boolean verifiedWorld = false;
        for (String world : worlds) {
            if (world.equalsIgnoreCase("%skyblock-main-world%")) world = skyBlockHook.getIslandWorld().getName();
            if (world.equalsIgnoreCase(worldName)) verifiedWorld = true;
        }
        if (!verifiedWorld) return false;

        if (!skyBlockHook.hasIsland(ownerUUID)
                || skyBlockHook.getIslandMembers(location) == null
                || !skyBlockHook.getIslandMembers(location).contains(ownerUUID)) {
            owner.sendMessage(fileManager.getMessage("generator.thisIslandIsNotYour"));
            return false;
        }

        if (generatorLocations.containsKey(location)) {
            owner.sendMessage(fileManager.getMessage("generator.generatorIsNotPlaced"));
            return false;
        }

        final Location islandLocation = skyBlockHook.getIslandCenterLocation(ownerUUID).getBlock().getLocation();

        if (generatorIslands.containsKey(islandLocation)) {

            int generatorAmount = generatorIslands.get(islandLocation).size();
            int generatorAmountLimit = 0;

            Optional<String> optionalPlayerGroup = Optional.ofNullable(permission.getPrimaryGroup(owner));
            String playerGroup = optionalPlayerGroup.orElse("default");
            Set<String> limited_groups = fileManager.getFile(FileManager.ConfigFile.settings).getConfigurationSection("settings.generator.generator-island-limit").getKeys(false);
            if (limited_groups.contains(playerGroup)) {
                generatorAmountLimit = fileManager.getFile(FileManager.ConfigFile.settings).getInt("settings.generator.generator-island-limit." + playerGroup);
            } else {
                generatorAmountLimit = fileManager.getFile(FileManager.ConfigFile.settings).getInt("settings.generator.generator-island-limit.default");
            }

            if (generatorAmount >= generatorAmountLimit) {
                owner.sendMessage(fileManager.getMessage("generator.generatorIslandLimit")
                        .replace("%max_generator_amount%", Integer.toString(generatorAmountLimit))
                        .replace("%group%", playerGroup));
                return false;
            }

        }

        final Generator generator = new Generator(islandLocation, generatorUUID,
                fileManager.getFile(FileManager.ConfigFile.language).getString("language.generator.placeHolder.generatorName")
                        .replace("%owner%", owner.getName())
                , location, level);
        generator.changeOwnerUUID(ownerUUID);
        generator.setAutoBreakBuyed(autoBreak);
        generator.setAutoPickupBuyed(autoPickup);
        generator.setAutoSmeltBuyed(autoSmelt);
        generator.setAutoChestBuyed(autoChest);
        generator.setHologramEnabled(true);

        generator.setCreateDate(CalendarSerializer.nowDate());

        for (UUID uuid : skyBlockHook.getIslandMembers(ownerUUID)) {

            final GeneratorMember generatorMember = new GeneratorMember(uuid);

            if (uuid.equals(ownerUUID)) {
                generatorMember.addPermission(MemberPermission.BREAK_GENERATOR);
                generatorMember.addPermission(MemberPermission.OPEN_SETTINGS);
                generatorMember.addPermission(MemberPermission.CHANGE_SETTINGS);
            }

            generator.addGeneratorMember(generatorMember);
            User user = getUserManager().getUser(uuid);
            if (user == null) storage.putGeneratorToUser(uuid, generatorUUID);
            else {
                if (!user.containsGenerator(generatorUUID)) user.addGenerator(generatorUUID);
            }

        }

        loadGenerator(generator);
        owner.sendMessage(fileManager.getMessage("generator.generatorPlaced"));

        return true;

    }

    public boolean loadGenerator(final Generator generator) {

        final UUID generatorUUID = generator.getGeneratorUUID();

        if (generators.containsKey(generator.getGeneratorUUID())) return true;

        generators.put(generatorUUID, generator);
        generatorLocations.put(generator.getGeneratorLocation(), generatorUUID);

        final Location islandLocation = generator.getIslandLocation();
        if (!generatorIslands.containsKey(islandLocation)) generatorIslands.put(islandLocation, new ArrayList<>());
        generatorIslands.get(islandLocation).add(generatorUUID);

        Bukkit.getScheduler().runTask(plugin, () -> {
            generator.getGeneratorLocation().getBlock().setType(firstBlockMaterial);
            hologramHook.loadHologram(generator);
        });

        if (generator.isAutoBreakEnabled()) {
            final AutoBreakManager autoBreakManager = featureManager.getAutoBreakManager();
            autoBreakManager.addAutoBreakerGenerator(generatorUUID);
            autoBreakManager.startRunnable();
        }

        if (!boostRunnableStarted) {
            new BoostRunnable(plugin);
            boostRunnableStarted = true;
        }

        return true;

    }

    public boolean loadGenerator(final UUID generatorUUID) {

        if (generators.containsKey(generatorUUID)) return true;

        final Generator generator = storage.loadGenerator(generatorUUID);
        if (generator == null) return false;

        loadGenerator(generator);

        return true;

    }

    public void saveGenerator(final UUID generatorUUID) {

        final Generator generator = generators.get(generatorUUID);
        if (generator == null) return;

        hologramHook.deleteHologram(generator);

        storage.saveGenerator(generator);

        if (generator.isAutoBreakEnabled())
            featureManager.getAutoBreakManager().removeAutoBreakerGenerator(generatorUUID);

        generatorLocations.remove(generator.getGeneratorLocation());
        generatorIslands.get(generator.getIslandLocation()).remove(generatorUUID);
        generators.remove(generatorUUID);

    }

    @Nullable
    public Generator getGenerator(final UUID generatorUUID) {
        return generators.get(generatorUUID);
    }

    @Nullable
    public Generator getGenerator(final Location location) {
        return generators.get(generatorLocations.get(location));
    }

    @Nullable
    public List<UUID> getGenerators(final Location islandLocation) {
        return generatorIslands.get(islandLocation);
    }

    @Nullable
    public UUID getGeneratorUUID(final Location location) {
        return generatorLocations.get(location);
    }

    public boolean deleteGenerator(final UUID generatorUUID) {

        final Generator generator = generators.get(generatorUUID);

        final GeneratorDeleteEvent generatorDeleteEvent = new GeneratorDeleteEvent(generator, false);
        Bukkit.getPluginManager().callEvent(generatorDeleteEvent);

        if (generatorDeleteEvent.isCancelled()) return false;

        hologramHook.deleteHologram(generator);

        featureManager.getRegenManager().removeRegenGenerator(generator.getGeneratorLocation());
        generatorLocations.remove(generator.getGeneratorLocation());
        generatorIslands.get(generator.getIslandLocation()).remove(generatorUUID);
        if (generatorIslands.get(generator.getIslandLocation()).isEmpty()) generatorIslands.remove(generator.getIslandLocation());
        storage.deleteGenerator(generatorUUID);
        generators.remove(generatorUUID);

        generator.getGeneratorLocation().getBlock().setType(Material.AIR);
        Player p = Bukkit.getPlayer(generator.getOwnerUUID());
        if (p != null && p.isOnline()) {
            p.getInventory().addItem(generatorManager.getGeneratorItem().create(1, false, false, false, false));
        }

        return true;

    }

    public boolean containsGeneratorLocation(final Location generatorLocation) {
        return generatorLocations.containsKey(generatorLocation);
    }

    public boolean containsGeneratorUUID(final UUID generatorUUID) {
        return generators.containsKey(generatorUUID);
    }

    public boolean containsGeneratorIslandLocation(final Location location) {
        return generatorIslands.containsKey(location);
    }

    public int generatorListSize() {
        return generators.size();
    }

    public GeneratorItem getGeneratorItem() {
        return generatorItem;
    }

    public void reloadGeneratorItem() {
        generatorItem = new GeneratorItem(fileManager.getFile(FileManager.ConfigFile.settings));
    }

    public void reloadFirstBlockMaterial() {
        firstBlockMaterial = Material.getMaterial(fileManager.getFile(FileManager.ConfigFile.settings).getString("settings.generator.generator-first-material"));
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    private UserManager getUserManager() {
        if (userManager == null) userManager = EliteGenerator.getInstance().getUserManager();
        return userManager;
    }

    public Iterator<Generator> getGeneratorsIterator() {
        return generators.values().iterator();
    }

}
