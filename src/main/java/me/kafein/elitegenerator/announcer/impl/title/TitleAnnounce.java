package me.kafein.elitegenerator.announcer.impl.title;

import me.kafein.elitegenerator.EliteGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
public class TitleAnnounce {

    private Class<?> iChatBaseComponent;
    private Class<?> packetPlayerOutTitle;
    private Object outTitle;
    private Object outSubTitle;

    public TitleAnnounce() {
        try {
            iChatBaseComponent = EliteGenerator.getInstance().getNMSClass("IChatBaseComponent");
            packetPlayerOutTitle = EliteGenerator.getInstance().getNMSClass("PacketPlayOutTitle");
            outTitle = packetPlayerOutTitle.getDeclaredClasses()[0].getField("TITLE").get(null);
            outSubTitle = packetPlayerOutTitle.getDeclaredClasses()[0].getField("SUBTITLE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void send(final Player player, final String title, final String subtitle) {
        try {
            Object titleChat = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
            Object subtitleChat = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
            Constructor<?> titleConstructor = packetPlayerOutTitle.getConstructor(packetPlayerOutTitle.getDeclaredClasses()[0], iChatBaseComponent, int.class, int.class, int.class);
            Object titlePacket = titleConstructor.newInstance(outTitle, titleChat, 30, 50, 30);
            Object subtitlePacket = titleConstructor.newInstance(outSubTitle, subtitleChat, 30, 40, 30);
            EliteGenerator.getInstance().sendPacket(player, titlePacket);
            EliteGenerator.getInstance().sendPacket(player, subtitlePacket);
        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void send(Player player, String title, final String subtitle, Sound sound) {
        send(player, title, subtitle);
        player.playSound(player, sound, 1, 1);
    }

}
