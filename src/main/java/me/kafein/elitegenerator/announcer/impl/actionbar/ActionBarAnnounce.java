package me.kafein.elitegenerator.announcer.impl.actionbar;

import me.kafein.elitegenerator.EliteGenerator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ActionBarAnnounce {

    final private String version = EliteGenerator.getInstance().getVersion();

    private Class<?> packetPlayOutChatClass;
    private Class<?> chatComponentTextClass;
    private Class<?> iChatBaseComponentClass;

    public ActionBarAnnounce() {

        try {

            packetPlayOutChatClass = EliteGenerator.getInstance().getNMSClass("PacketPlayOutChat");
            chatComponentTextClass = EliteGenerator.getInstance().getNMSClass("ChatComponentText");
            iChatBaseComponentClass = EliteGenerator.getInstance().getNMSClass("IChatBaseComponent");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void send(final Player player, final String announce) {
        try {
            Object packet;
            try {
                Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
                Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                Object chatMessageType = null;
                for (Object obj : chatMessageTypes) {
                    if (obj.toString().equals("GAME_INFO")) {
                        chatMessageType = obj;
                    }
                }
                Object chatComponentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(announce);
                packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatComponentText, chatMessageType);
            } catch (ClassNotFoundException e) {
                Object chatComponentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(announce);
                packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatComponentText, (byte) 2);
            }
            EliteGenerator.getInstance().sendPacket(player, packet);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Player player, String announce, Sound sound) {
        send(player, announce);
        player.playSound(player, sound, 1, 1);
    }

}
