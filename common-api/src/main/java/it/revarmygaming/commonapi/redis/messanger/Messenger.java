package it.revarmygaming.commonapi.redis.messanger;

import java.io.*;
import java.util.Base64;

/**
 * Represents an object which dispatches {@link OutgoingMessage}s.
 */
public interface Messenger extends AutoCloseable {

    /**
     * Performs the necessary action to dispatch the message using the means
     * of the messenger.
     *
     * <p>The outgoing message instance is guaranteed to be an instance of one
     * of the interfaces extending {@link Message} in the
     * 'api.messenger.message.type' package.</p>
     *
     * <p>3rd party implementations are encouraged to implement this method with consideration
     * that new types may be added in the future.</p>
     *
     * <p>This call is always made async.</p>
     *
     * @param outgoingMessage the outgoing message
     */
    void sendOutgoingMessage(OutgoingMessage outgoingMessage);

    /**
     * Performs the necessary action to gracefully shutdown the messenger.
     */
    @Override
    default void close() {

    }

    default Message deserialize(String encodedString) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(encodedString.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Message) objectInputStream.readObject();
    }
}
