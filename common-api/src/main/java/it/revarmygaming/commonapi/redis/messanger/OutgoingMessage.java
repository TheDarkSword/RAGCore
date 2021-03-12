package it.revarmygaming.commonapi.redis.messanger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * Represents an outgoing {@link Message}.
 *
 * <p>Outgoing messages are ones which have been generated by this instance.
 * (in other words, they are implemented by LuckPerms)</p>
 *
 * <p>Note that all implementations of this interface are guaranteed to be an
 * instance of one of the interfaces extending {@link Message} in the
 * 'api.messenger.message.type' package.</p>
 */
public interface OutgoingMessage extends Message {

    /**
     * Gets an encoded string form of this message.
     *
     * <p>The format of this string is likely to change between versions and
     * should not be depended on.</p>
     *
     * <p>Implementations which want to use a standard method of serialisation
     * can send outgoing messages using the string returned by this method, and
     * pass on the message on the "other side" using
     * {@link IncomingMessageConsumer#consumeIncomingMessageAsString(String)}.</p>
     *
     * @param message is the message that will be sent
     *
     * @throws IOException throws from java.io.ObjectOutputStream
     * @return an encoded string form of the message
     */
    default String asEncodedString(Message message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
