package it.revarmygaming.spigot.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSUtils {

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean isVersionBigger(float check) {
        return check < Float.parseFloat(
                getServerVersion().substring(1, getServerVersion().length() - 3).replace("_", ".")
        );
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
