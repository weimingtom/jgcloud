package testdarkstarserver;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedReference;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The Darkstar startup class.
 *
 * @author Richard Hawkes
 */
public class TestDarkstarAppListener implements Serializable, AppListener, ChannelListener {

    private static final Logger logger = Logger.getLogger(TestDarkstarAppListener.class.getName());

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    public static final String PLAYER_LOCATIONS_CHANNEL = "PLAYER_LOCATIONS";

    /** The version of the serialized form of this class. */
    private static final long serialVersionUID = 1L;

    /** We maintain a reference to the channel (not the channel itself) */
    ManagedReference<Channel> commChannelRef;


    /**
     * Gets called the first time the Darkstar server starts up. Subsequent
     * startups will not call this method, unless the /data/... directory is
     * removed.
     *
     * @param props Not sure!
     */
    public void initialize(Properties props) {
        logger.info("Test Server initialized");
        Channel clientLocChannel = AppContext.getChannelManager().createChannel(PLAYER_LOCATIONS_CHANNEL, this, Delivery.RELIABLE);

        commChannelRef = AppContext.getDataManager().createReference(clientLocChannel);
    }


    /**
     * Called after a successful login. ie The password was good.
     *
     * @param session The new session that we can do things with.
     *
     * @return A ClientSessionListener that can communicate with the server.
     */
    public ClientSessionListener loggedIn(ClientSession session) {
        logger.info("Session " + session.getName() + " logged in");
        commChannelRef.get().join(session);
        return new DarkstarClientSessionListener(session);
    }


    /**
     * Called if a message is received on the server on any channel.
     *
     * @param channel The channel that the message was sent on
     * @param clientSession The session that sent the message
     * @param message The message itself
     */
    public void receivedMessage(Channel channel, ClientSession clientSession, ByteBuffer message) {
        logger.fine("Received message from " + clientSession.getName() + " on channel " + channel.getName() + ": " + decodeMessage(message));

        if (channel.getName().equals(PLAYER_LOCATIONS_CHANNEL)) {
            // We just send the message straight back out again (including to
            // the sender, so they will need to ignore messages from themselves).
            channel.send(clientSession, message);
        } else {
            // I don't know if it's ever possible for a client to send a message
            // on an unknown channel, but here's a warning if it ever does.
            logger.severe("Received a message on an unknown/unhandled channel (" + channel.getName() + ") from " + clientSession.getName());
        }
    }

    
    /**
     * Decodes an incoming message from a client.
     *
     * @param message The message sent from a client.
     *
     * @return The decoded version of the message in readable String format.
     */
    private static String decodeMessage(ByteBuffer buf) {
        // We will duplicate the original ByteBuffer message so that this method
        // doesn't alter it.
        ByteBuffer duplicateBuf = buf.duplicate();

        try {
            byte[] bytes = new byte[duplicateBuf.remaining()];
            duplicateBuf.get(bytes);
            return new String(bytes, MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            logger.severe("Required character set " + MESSAGE_CHARSET + " not found: " + ex);
            return null;
        }
    }
}
