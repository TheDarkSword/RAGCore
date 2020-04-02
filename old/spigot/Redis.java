package it.revarmygaming.spigot.redis;

import it.revarmygaming.commonapi.Reference;
import it.revarmygaming.commonapi.redis.RedisMessage;
import it.revarmygaming.commonapi.redis.Subscriber;
import it.revarmygaming.spigot.common.Chat;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;

public class Redis {

    private final Plugin plugin;
    private final String host;
    private final int port;
    private String password;
    private Jedis rxRedis;
    private Jedis txRedis;
    private HashMap<String, Thread> running = new HashMap<>();

    public Redis(Plugin plugin, String host, int port, String password) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public Redis(Plugin plugin, String host, int port) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
    }

    /**
     * Return the redis host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Return the redis port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the redis password for authentication.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the redis password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establish the connection with the redis network.
     */
    public final void connect() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, host, port);
        rxRedis = pool.getResource();
        txRedis = pool.getResource();

        if(password != null && !password.isEmpty()) {
            rxRedis.auth(password);
            txRedis.auth(password);
        }
    }

    /**
     * Close the connection with the redis network.
     */
    public final void close() {
        unregisterSubscribers();
        rxRedis.getClient().close();
        rxRedis.close();
        txRedis.getClient().close();
        txRedis.close();
    }

    /**
     * Register a {@link Subscriber} for one or more channel/s.
     *
     * @param subscriber the {@link Subscriber} class
     * @param channels   the channel/s
     */
    public final void registerSubscriber(Subscriber subscriber, String... channels) {
        for(String channel : channels) {
            Thread thread = new Thread(() -> {
                try {
                    Chat.getLogger("&7[RAGC] &b" + plugin.getName() + "&3 subscribing to Redis channel &b" + channel + "&3.");
                    rxRedis.subscribe(subscriber, channel);
                } catch (Exception e) {
                    if(Reference.logLevel >= 1) Chat.getLogger("&7[RAGC] &4" + plugin.getName() + " caught an exception: " + e.getMessage() + "!", "severe");
                    if(Reference.debug) e.printStackTrace();
                }
            });
            thread.start();
            running.put(channel, thread);
        }
    }

    /**
     * Unregister a subscriber for a channel.
     *
     * @param channel the channel you want to unsubscribe
     */
    public final void unregisterSubscriber(String channel) {
        running.get(channel).interrupt();
        running.remove(channel);
        rxRedis.getClient().unsubscribe(channel);
    }

    /**
     * Unregister all subscribers.
     */
    public final void unregisterSubscribers() {
        for(String channel : running.keySet()) unregisterSubscriber(channel);
    }

    /**
     * Publish a message to the chosen channel.
     *
     * @param channel the channel in which you send the message
     * @param message the message or serialized object you want to send
     */
    public void publish(String channel, String message) {
        txRedis.publish(channel, message);
    }

    /**
     * Publish a {@link RedisMessage} to the chosen channel.
     *
     * @param channel the channel in which you send the message
     * @param message the message you want to send
     */
    public void publish(String channel, RedisMessage message) {
        try {
            txRedis.publish(channel, serialize(message));
        } catch (IOException e) {
            if(Reference.logLevel >= 1) Chat.getLogger("&7[RAGC] &4" + plugin.getName() + " caught an exception: " + e.getMessage() + "!", "severe");
            if(Reference.debug) e.printStackTrace();
        }
    }

    /**
     * Serialize a serializable object and return it as a String.
     *
     * @param object the serializable input object
     * @return the serialized object
     * @throws IOException
     */
    public static String serialize(Object object) throws IOException {
        if(!(object instanceof Serializable)) throw new NotSerializableException(object.getClass().getName() + "object is not serializable");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Deserialize a String and return the Object.
     *
     * @param serializedMsg the serialized object in form of a String
     * @return the object deserialized
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(String serializedMsg) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(serializedMsg.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * Deserialize a String and return the {@link RedisMessage}.
     *
     * @param serializedMsg the serialized {@link RedisMessage} in form of a String.
     * @return the {@link RedisMessage} deserialized
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final RedisMessage deserializeRedisMessage(String serializedMsg) throws IOException, ClassNotFoundException {
        return (RedisMessage) deserialize(serializedMsg);
    }
}
