package it.revarmygaming.spigot.nms;

import it.revarmygaming.spigot.common.Chat;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Title {

    private String title;
    private String subtitle;
    private final int fadeInTime;
    private final int fadeOutTime;
    private final int showTime;

    public Title(String title, String subtitle, int fadeInTime, int fadeOutTime, int showTime, boolean translate){
        if(translate){
            this.title = Chat.getTranslated(title);
            this.subtitle = Chat.getTranslated(subtitle);
        } else {
            this.title = title;
            this.subtitle = subtitle;
        }
        this.fadeInTime = fadeInTime;
        this.fadeOutTime = fadeOutTime;
        this.showTime = showTime;
    }

    public Title(String title, String subtitle, int fadeInTime, int fadeOutTime, int showTime){
        this(title, subtitle, fadeInTime, fadeOutTime, showTime, true);
    }

    public boolean send(Player player) {
        String version = NMSUtils.getServerVersion();

        if (NMSUtils.isVersionBigger(1.12f)) {
            player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
            return true;
        } else if (version.startsWith("v1_7")) {
            return false;
        } else {
            try {
                Constructor<?> constructor = NMSUtils.getNMSClass("PacketPlayOutTitle").getConstructor(NMSUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], NMSUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);

                Object chatTitle = NMSUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
                Object titlePacket = constructor.newInstance(NMSUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeInTime, showTime, fadeOutTime);

                Object chatSubtitle = NMSUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Object subtitlePacket = constructor.newInstance(NMSUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatSubtitle, fadeInTime, showTime, fadeOutTime);

                NMSUtils.sendPacket(player, titlePacket);
                NMSUtils.sendPacket(player, subtitlePacket);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }
}
