package it.revarmygaming.commonapi.redis.messanger;

import java.util.UUID;

/**
 * Represents a message sent received via a {@link Messenger}.
 */
public interface Message {

    /**
     * Gets the unique id associated with this message.
     *
     * <p>This ID is used to ensure a single server instance doesn't process
     * the same message twice.</p>
     *
     * @return the id of the message
     */
    UUID getId();

    /**
     * Gets the identifier of packet.
     *
     * <p>This is used to know what you need to execute.</p>
     *
     * @return the identifier of the message
     */
    String getAction();

    /**
     * Gets the data.
     *
     * @return the identifier of the message
     */
    Object getData();

    /**
     * Gets the type of data.
     *
     * <p>This is used to get the class for checking or for using reflection.</p>
     *
     * @return the identifier of the message
     */
    String getDataType();

}
