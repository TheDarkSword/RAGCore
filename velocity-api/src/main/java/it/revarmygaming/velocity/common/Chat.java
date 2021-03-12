package it.revarmygaming.velocity.common;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

public class Chat {

    /**
     * Converts a raw string with color codes to a colored string.
     *
     * @param msg the raw string
     * @return the colored string
     */
    public static Component getTranslated(String msg) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
    }

    /**
     * Converts a string list with color codes to a colored string list.
     *
     * @param msg the string list
     * @return the colored string list
     */
    public static List<Component> getTranslated(List<String> msg) {
        return msg.stream().map(var -> LegacyComponentSerializer.legacyAmpersand().deserialize(var)).collect(Collectors.toList());
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
    public static Component getTranslated(String msg, CommandSource sender, String... permissions) {
        for(String perm : permissions) {
            if(sender.hasPermission(perm)) return getTranslated(msg);
        }
        return Component.text(msg);
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
    public static List<Component> getTranslated(List<String> msg, CommandSource sender, String... permissions) {
        for(String perm : permissions) {
            if(sender.hasPermission(perm)) return getTranslated(msg);
        }
        return msg.stream().map(Component::text).collect(Collectors.toList());
    }

    /**
     * Removes all color codes from the string.
     *
     * @param msg the input string
     * @return the output string without color codes
     */
    public static Component getDiscolored(String msg) {
        msg = msg.replaceAll("&[0-9a-fA-Fk-oK-OrR]", "");
        msg = msg.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
        return Component.text(msg);
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
    public static void send(String msg, CommandSource receiver) {
        send(msg, receiver, false);
    }

    /**
     * Sends a message to a command sender.
     *
     * @param msg       the message
     * @param receiver  the receiver
     * @param translate whether the colors have to be translated
     */
    public static void send(String msg, CommandSource receiver, boolean translate) {
        receiver.sendMessage(translate ? getTranslated(msg) : Component.text(msg));
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
    public static void send(String msg, CommandSource receiver, CommandSource sender, String... permissions) {
        receiver.sendMessage(getTranslated(msg, sender, permissions));
    }
}
