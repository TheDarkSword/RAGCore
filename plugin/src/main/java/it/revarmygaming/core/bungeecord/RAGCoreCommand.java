package it.revarmygaming.core.bungeecord;

import it.revarmygaming.bungeecord.common.Chat;
import it.revarmygaming.core.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class RAGCoreCommand extends Command {

    public RAGCoreCommand() {
        super("ragcore", "", "ragc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 1) {
            Chat.send("&eYou are using &6RAGCore &eversion &6" +
                    BungeeCord.plugin.getDescription().getVersion() +
                    "&e by &6" +
                    BungeeCord.plugin.getDescription().getAuthor(), sender, true);
            if(!sender.hasPermission("ragcore.help")) {
                Chat.send("&7/mbc reload &f- &eReloads this plugin", sender, true);
            }
        } else {
            if (!sender.hasPermission("ragcore.reload")) {
                Chat.send(BungeeCord.config.getString("messages.insufficient-perm"), sender, true);
                return;
            }

            BungeeCord.plugin.onDisable();
            BungeeCord.plugin.onEnable();
            Chat.send(BungeeCord.config.getString("messages.plugin-reload"), sender, true);
        }
    }
}
