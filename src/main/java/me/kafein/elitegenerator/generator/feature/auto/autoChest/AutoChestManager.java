package me.kafein.elitegenerator.generator.feature.auto.autoChest;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.announcer.AnnouncerManager;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoChestManager {

    final private Map<UUID, UUID> autoChestPlayers = new HashMap<>();

    final private String message = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings).getString("settings.generator.autoChestTimeLeftMessage");

    private GeneratorManager generatorManager = null;

    final private Plugin plugin;

    public AutoChestManager(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public UUID getGeneratorUUIDWithPlayer(final UUID playerUUID) {
        return autoChestPlayers.get(playerUUID);
    }

    public void addAutoChestPlayer(final UUID playerUUID, final UUID generatorUUID) {

        autoChestPlayers.put(playerUUID, generatorUUID);

        new BukkitRunnable() {

            private Generator generator = getGeneratorManager().getGenerator(generatorUUID);
            private int time = 30;

            @Override
            public void run() {

                final Player player = Bukkit.getPlayer(playerUUID);

                if (player == null || time <= 0 || !getGeneratorManager().containsGeneratorUUID(generatorUUID) || generator.isAutoChestEnabled()) {
                    removeAutoChestPlayer(playerUUID);
                    cancel();
                    return;
                }

                time--;
                AnnouncerManager.getActionBarAnnounce().send(player, ChatColor.translateAlternateColorCodes('&', message.replace("%time%", Integer.toString(time))));

            }

        }.runTaskTimerAsynchronously(plugin, 20L, 20L);

    }

    public void removeAutoChestPlayer(final UUID playerUUID) {
        autoChestPlayers.remove(playerUUID);
    }

    public boolean containsAutoChestPlayer(final UUID playerUUID) {
        return autoChestPlayers.containsKey(playerUUID);
    }

    public GeneratorManager getGeneratorManager() {
        if (generatorManager == null) generatorManager = EliteGenerator.getInstance().getGeneratorManager();
        return generatorManager;
    }

}
