package it.revarmygaming.core.velocity;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginDescription;
import it.revarmygaming.core.Velocity;
import it.revarmygaming.velocity.common.Chat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RAGCoreCommand implements SimpleCommand {

    private final Velocity velocity;

    public RAGCoreCommand(Velocity velocity) {
        this.velocity = velocity;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        PluginDescription description = velocity.getServer().getPluginManager().getPlugin("ragcore").get().getDescription();
        if(args.length != 1){
            Chat.send("&eYou are using &6RAGCore &eversion &6" +
                    description.getVersion().orElse("0.0") +
                    "&e by &6" +
                    description.getAuthors().get(0), source, true);
            if(!source.hasPermission("ragcore.help")) {
                Chat.send("&7/ragc reload &f- &eReloads this plugin", source, true);
            }
        } else if(args[0].equals("reload")) {
            if (!source.hasPermission("ragcore.reload")) {
                Chat.send(velocity.getConfig().getString("messages.insufficient-perm"), source, true);
                return;
            }

            try {
                velocity.reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Chat.send(velocity.getConfig().getString("messages.plugin-reload"), source, true);
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if(invocation.arguments().length == 1){
            return Collections.singletonList("reload");
        }
        return ImmutableList.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }
}
