package testdarkstarserver;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * This is the class that (after login) keeps tags on the users communications
 * with the server. It's bloody annoying that I've stuck it in a seperate class,
 * because (once again) I have to bounce those references between the
 * AppListener in order to get this class to do anything !
 *
 * @author Richard Hawkes
 */
public class DarkstarClientSessionListener implements ClientSessionListener, Serializable {

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    /** The version of the serialized form of this class. */
    private static final long serialVersionUID = 1L;

    /** The {@link Logger} for this class. */
    private static final Logger logger = Logger.getLogger(DarkstarClientSessionListener.class.getName());

    /** The session this {@code ClientSessionListener} is listening to. */
    private final ManagedReference<ClientSession> sessionRef;

    /** The name of the {@code ClientSession} for this listener. */
    private final String sessionName;

    /**
     * Main constructor. Gets called by Darkstar Server on the creation of a
     * new client session.
     *
     * @param session The session that just connected :)
     */
    public DarkstarClientSessionListener(ClientSession session) {
        if (session == null) {
            throw new NullPointerException("null session");
        }

        this.sessionRef = AppContext.getDataManager().createReference(session);
        this.sessionName = session.getName();
    }

    /**
     * If the server receives a message from a client, this method's called.
     * Remember, that a seperate instance of this is created for each session
     * that gets connected.
     *
     * @param message The message sent !
     */
    public void receivedMessage(ByteBuffer message) {
        logger.info("Received message from " + sessionName + ": " + decodeMessage(message));
    }


    /**
     * Decodes an incoming message from a client.
     *
     * @param message The message sent from a client.
     *
     * @return The decoded version of the message in readable String format.
     */
    private static String decodeMessage(ByteBuffer message) {
        ByteBuffer duplicateBuf = message.duplicate();

        try {
            byte[] bytes = new byte[duplicateBuf.remaining()];
            duplicateBuf.get(bytes);
            return new String(bytes, MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            logger.severe("Required character set " + MESSAGE_CHARSET + " not found: " + ex);
            return null;
        }
    }


    /**
     * If the client is disconnected, this gets called.
     *
     * @param graceful Was it rudely removed?
     */
    public void disconnected(boolean graceful) {
        logger.info("Session " + sessionName + " has logged out. graceful=" + graceful);
    }


    /**
     * Returns the reference to the session for this listener.
     *
     * @return the reference to the session for this listener.
     */
    protected ClientSession getSession() {
        // We created the ref with a non-null session, so no need to check it.
        return sessionRef.get();
    }
}
