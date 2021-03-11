package it.revarmygaming.commonapi.redis.messanger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * Encapsulates the LuckPerms system which accepts incoming {@link Message}s
 * from implementations of {@link Messenger}.
 */
public interface IncomingMessageConsumer {

    /**
     * Consumes a message instance.
     *
     * <p>The boolean returned from this method indicates whether or not the
     * platform accepted the message. Some implementations which have multiple
     * distribution channels may wish to use this result to dispatch the same
     * message back to additional receivers.</p>
     *
     * <p>The implementation will usually return <code>false</code> if a message
     * with the same ping id has already been processed.</p>
     *
     * @param message the message
     * @return true if the message was accepted by the plugin
     */
    boolean consumeIncomingMessage(Message message);

    /**
     * Consumes a message in an encoded string format.
     *
     * <p>This method will decode strings obtained by calling
     * {@link OutgoingMessage#asEncodedString()}. This means that basic
     * implementations can successfully implement {@link Messenger} without
     * providing their own serialisation.</p>
     *
     * <p>The boolean returned from this method indicates whether or not the
     * platform accepted the message. Some implementations which have multiple
     * distribution channels may wish to use this result to dispatch the same
     * message back to additional receivers.</p>
     *
     * <p>The implementation will usually return <code>false</code> if a message
     * with the same ping id has already been processed.</p>
     *
     * @param encodedString the encoded string
     * @return true if the message was accepted by the plugin
     */
    boolean consumeIncomingMessageAsString(String encodedString);

    default Message deserialize(String encodedString) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(encodedString.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Message) objectInputStream.readObject();
    }
}
