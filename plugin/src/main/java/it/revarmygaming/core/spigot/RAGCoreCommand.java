package it.revarmygaming.core.spigot;

import it.revarmygaming.core.Spigot;
import it.revarmygaming.spigot.common.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RAGCoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 1) {
            Chat.send("&eYou are using &6RAGCore &eversion &6" +
                    Spigot.plugin.getDescription().getVersion() +
                    "&e by &6" +
                    Spigot.plugin.getDescription().getAuthors().get(0), sender, true);
            if(!sender.hasPermission("ragcore.help")) {
                Chat.send("&7/ragc reload &f- &eReloads this plugin", sender, true);
            }
        }
        else {
            if(!sender.hasPermission("ragcore.reload")) {
                Chat.send(Spigot.config.getString("messages.insufficient-perm"), sender, true);
                return true;
            }

            Spigot.plugin.onDisable();
            Spigot.plugin.onEnable();
            Chat.send(Spigot.config.getString("messages.plugin-reload"), sender, true);
        }
        return true;
    }
}
