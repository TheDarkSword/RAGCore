package it.revarmygaming.core;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import it.revarmygaming.commonapi.yaml.Configuration;
import it.revarmygaming.core.velocity.RAGCoreCommand;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
@Plugin(id = "ragcore", name = "RAGCore", version = "2.0",
        description = "First Velocity Plugin", authors = {"TheDarkSword"})
public class Velocity {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private Configuration config;

    @Inject
    public Velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory){
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event){
        config = new Configuration(new File(dataDirectory.toFile(), "config.yml"), getResourceAsStream("config.yml"), true);

        try {
            config.autoload();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.getCommandManager().register(server.getCommandManager()
                .metaBuilder("ragcore")
                .aliases("ragc").build(), new RAGCoreCommand(this));
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() throws IOException {
        config.autoload();
    }

    public final InputStream getResourceAsStream(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }
}
