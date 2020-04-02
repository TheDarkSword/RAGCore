package it.revarmygaming.spigot.nms;

import it.revarmygaming.spigot.common.Chat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ActionBar {

    private final String message;

    public ActionBar(String message, boolean translate) {
        this.message = translate ? Chat.getTranslated(message) : message;
    }

    public ActionBar(String message) {
        this(message, true);
    }

    public boolean send(Player player) {
        if (NMSUtils.getServerVersion().startsWith("v1_7")) return false;

        if (NMSUtils.isVersionBigger(1.10f)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            return true;
        } else {
            try {
                String nmsVersion = NMSUtils.getServerVersion();

                Class<?> ppoc = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");

                Object packetPlayOutChat;
                Class<?> chat = Class.forName("net.minecraft.server." + nmsVersion + (nmsVersion.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
                Class<?> chatBaseComponent = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");

                Method method = null;
                if (nmsVersion.equalsIgnoreCase("v1_8_R1")) method = chat.getDeclaredMethod("a", String.class);

                Object object = nmsVersion.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(method.invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(new Class[]{String.class}).newInstance(message);
                packetPlayOutChat = ppoc.getConstructor(new Class[]{chatBaseComponent, Byte.TYPE}).newInstance(object, (byte) 2);

                NMSUtils.sendPacket(player, packetPlayOutChat);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }
}
