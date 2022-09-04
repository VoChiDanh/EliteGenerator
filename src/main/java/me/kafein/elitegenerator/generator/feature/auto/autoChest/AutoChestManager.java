package me.kafein.elitegenerator.generator.feature.auto.autoChest;

import lombok.SneakyThrows;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.util.reflection.ReflectionUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
    final private Plugin plugin;
    private GeneratorManager generatorManager = null;

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

            private final Generator generator = getGeneratorManager().getGenerator(generatorUUID);
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

                final String text = ChatColor.translateAlternateColorCodes('&', message.replace("%time%", Integer.toString(time)));
                try {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
                } catch (NoSuchMethodError e) {
                    send1_8(player, text);
                }


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

    @SneakyThrows
    private void send1_8(final Player player, final String message) {

        ReflectionUtils.PackageType packageType = ReflectionUtils.PackageType.MINECRAFT_SERVER;

        Object packet;
        try {
            Class<?> chatMessageTypeClass = packageType.getClass("ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object chatComponentText = packageType.getClass("").getConstructor(new Class<?>[]{String.class}).newInstance(message);
            packet = packageType.getClass("PacketPlayOutChat").getConstructor(new Class<?>[]{packageType.getClass("IChatBaseComponent"), chatMessageTypeClass}).newInstance(chatComponentText, chatMessageType);
        } catch (ClassNotFoundException e) {
            Object chatComponentText = packageType.getClass("ChatComponentText").getConstructor(new Class<?>[]{String.class}).newInstance(message);
            packet = packageType.getClass("PacketPlayOutChat").getConstructor(new Class<?>[]{packageType.getClass("IChatBaseComponent"), byte.class}).newInstance(chatComponentText, (byte) 2);
        }

        final Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
        final Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
        playerConnection.getClass().getMethod("sendPacket", packageType.getClass("Packet")).invoke(playerConnection, packet);

    }

}
