package it.revarmygaming.core;

import it.revarmygaming.commonapi.Reference;
import it.revarmygaming.commonapi.yaml.Configuration;
import it.revarmygaming.core.spigot.RAGCoreCommand;
import it.revarmygaming.spigot.menus.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Spigot extends JavaPlugin {

    public static Plugin plugin;
    public static Configuration config;

    @Override
    public void onEnable() {
        plugin = this;
        if(!getDataFolder().exists())getDataFolder().mkdir();
        config = new Configuration(new File(getDataFolder(), "config.yml"), getResource("config.yml"), true);
        try {
            config.autoload();
        } catch (IOException e) {
            e.printStackTrace();

            Bukkit.getPluginManager().disablePlugin(this);
        }
        boolean bungeeMode = false;

        if(config != null) {
            Reference.debug = config.getBoolean("debug-mode");
            Reference.logDBQueries = config.getBoolean("query-logging");
            Reference.logLevel = config.getByte("log-level");
            bungeeMode = config.getBoolean("bungee-mode");
        }

        if(!bungeeMode) {

        }

        getCommand("ragcore").setExecutor(new RAGCoreCommand());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
