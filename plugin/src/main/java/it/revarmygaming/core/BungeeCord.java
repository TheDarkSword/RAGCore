package it.revarmygaming.core;

import it.revarmygaming.commonapi.Reference;
import it.revarmygaming.commonapi.yaml.Configuration;
import it.revarmygaming.core.bungeecord.RAGCoreCommand;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class BungeeCord extends Plugin {

    public static Plugin plugin;
    public static Configuration config;

    @Override
    public void onEnable() {
        plugin = this;
        config = new Configuration(new File(getDataFolder(), "config.yml"), getResource("config.yml"), true);
        try {
            config.autoload();
        } catch (IOException e) {
            e.printStackTrace();

            onDisable();
        }

        if(config != null) {
            Reference.debug = config.getBoolean("debug-mode");
            Reference.logDBQueries = config.getBoolean("query-logging");
            Reference.logLevel = config.getByte("log-level");
        }

        getProxy().getPluginManager().registerCommand(this, new RAGCoreCommand());
    }

    @Override
    public void onDisable() {

    }

    @Nullable
    public InputStream getResource(@NotNull String filename) {
        try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }
}
