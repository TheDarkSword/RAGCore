package it.revarmygaming.bungeecord.common;

import it.revarmygaming.commonapi.LoggerMode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class Chat {

    /**
     * Converts a raw string with color codes to a colored string.
     *
     * @param msg the raw string
     * @return the colored string
     */
    public static String getTranslated(String msg) {
        msg = ChatColor.translateAlternateColorCodes('§', msg);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }

    /**
     * Converts a string list with color codes to a colored string list.
     *
     * @param msg the string list
     * @return the colored string list
     */
    public static List<String> getTranslated(List<String> msg) {
        return msg.stream().map(var -> ChatColor.translateAlternateColorCodes('§', var))
                .map(var -> ChatColor.translateAlternateColorCodes('&', var)).collect(Collectors.toList());
    }

    /**
     * Converts a raw string with color codes to a colored string
     * if the command sender has one of the permissions.
     *
     * @param msg         the raw string
     * @param sender      the command sender to check
     * @param permissions the permissions to check
     * @return if true: the colored string, if false: the raw string
     */
    public static String getTranslated(String msg, CommandSender sender, String... permissions) {
        return getTranslated(msg, sender, permissions);
    }

    /**
     * Converts a raw string with color codes to a colored string
     * if the player has one of the permissions.
     *
     * @param msg         the raw string
     * @param sender      the command sender to check
     * @param permissions the permissions to check
     * @return if true: the colored string, if false: the raw string
     */
    public static List<String> getTranslated(List<String> msg, CommandSender sender, String... permissions) {
        for(String perm : permissions) {
            if(sender.hasPermission(perm)) return getTranslated(msg);
        }
        return msg;
    }

    /**
     * Removes all color codes from the string.
     *
     * @param msg the input string
     * @return the output string without color codes
     */
    public static String getDiscolored(String msg) {
        msg = msg.replaceAll("&[0-9a-fA-Fk-oK-OrR]", "");
        msg = msg.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
        return msg;
    }

    /**
     * Converts a string array to a string.
     *
     * @param args   the array of strings
     * @param offset the starting offset
     * @return the string of array elements joined by the character ' '
     */
    public static String builder(String[] args, int offset) {
        StringBuilder builder = new StringBuilder();
        for(; offset < args.length; offset++) builder.append(args[offset]).append(" ");
        return builder.toString();
    }

    /**
     * Converts a string array to a string.
     *
     * @param args the array of strings
     * @return the string of array elements joined by the character ' '
     */
    public static String builder(String[] args) {
        return builder(args, 0);
    }

    /**
     * Sends a message to a command sender.
     *
     * @param msg       the message
     * @param receiver  the receiver
     */
    public static void send(String msg, CommandSender receiver) {
        send(msg, receiver, false);
    }

    /**
     * Sends a message to a command sender.
     *
     * @param msg       the message
     * @param receiver  the receiver
     * @param translate whether the colors have to be translated
     */
    public static void send(String msg, CommandSender receiver, boolean translate) {
        receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
    }

    /**
     * Sends a message to a command sender translated if the command sender
     * (sender) has one of the permissions.
     *
     * @param msg         the message
     * @param receiver    the receiver
     * @param sender      the command sender to check
     * @param permissions the permissions to check
     */
    public static void send(String msg, CommandSender receiver, CommandSender sender, String... permissions) {
        receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
    }

    /**
     * Prints a message in the server console with 3 modes: info, warning, severe.
     * Color codes are translated.
     *
     * @param msg the raw message
     * @param mode the mode. Valid values are: "info", "warning", "severe"
     */
    public static void getLogger(String msg, String mode) {
        switch(mode.toLowerCase()) {
            case "info":
                ProxyServer.getInstance().getLogger().info(Chat.getTranslated(msg));
                break;
            case "warning":
                ProxyServer.getInstance().getLogger().warning(Chat.getTranslated(msg));
                break;
            case "severe":
                ProxyServer.getInstance().getLogger().severe(Chat.getTranslated(msg));
                break;
            default:
                ProxyServer.getInstance().getLogger().warning(Chat.getTranslated(msg));
                send("&8[&4ERROR&8] &4Not existing getLogger mode! Valid values are: info, warning, severe. If you see this message there's an error in one plugin using RAGCore.", ProxyServer.getInstance().getConsole(), true);
                break;
        }
    }

    /**
     * Prints a message in the server console with 3 modes: info, warning, severe.
     * Color codes are translated.
     *
     * @param msg the raw message
     * @param mode the mode
     */
    public static void getLogger(String msg, LoggerMode mode) {
        getLogger(msg, mode.get());
    }

    /**
     * Prints a message in the server console. Default mode is "info".
     * Color codes are translated.
     *
     * @param msg the raw message
     */
    public static void getLogger(String msg) {
        getLogger(msg, LoggerMode.INFO.get());
    }
}
