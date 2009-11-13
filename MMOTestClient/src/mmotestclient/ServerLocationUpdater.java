package mmotestclient;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the thread that interacts with the Darkstar server while the GUI is
 * running. It makes a regular update (via a channel) to the server saying what
 * the x and y position of the cursor is. It can get that because we pass in the
 * ClientInspector to the constructor.
 *
 * It also handles messages received back on the channel. It will also get back
 * the message it sent, plus any other messages from other clients.
 * 
 * @author Richard Hawkes
 */
public class ServerLocationUpdater implements Runnable, SimpleClientListener {
    private SimpleClient simpleClient;

    private static Logger logger = Logger.getLogger(ServerLocationUpdater.class.getName());
    private ClientInspector clientInspector;
    private boolean clientLoggedIn = false;
    private String player = "guest-" + new Random().nextInt(10000);

    ClientChannel clientChannel;

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    public ServerLocationUpdater(ClientInspector clientInspector) {
        this.clientInspector = clientInspector;
        this.simpleClient = new SimpleClient(this);
        login();
    }

    public void login() {
        Properties connectProps = new Properties();
        connectProps.put("host", "localhost");
        connectProps.put("port", "1139");

        // Attempt to log in. If it fails, a call to the "disconnect" method is
        // called and will contain the reason. If successful, "loggedIn" will be
        // called.
        try {
            simpleClient.login(connectProps);
        } catch (IOException ex) {
            logger.severe("Error on login attempt: " + ex);
        }
    }

    public void run() {
        while (true) {
            logger.log(Level.INFO, "x=" + clientInspector.getXPos() + " y=" + clientInspector.getYPos());

            try {
                if (clientLoggedIn && clientChannel != null) {
                    String message = player + "," + clientInspector.getXPos() + "," + clientInspector.getYPos();
                    clientChannel.send(encodeString(message));
                }
            } catch (IOException ex) {
                logger.severe("Unable to send message to server: " + ex);
                clientLoggedIn = false;
            }
            
            try { Thread.sleep(2000); } catch (Exception ex) { }
        }
    }

    private static ByteBuffer encodeString(String s) {
        try {
            return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            logger.severe("Required character set " + MESSAGE_CHARSET + " not found" + e);
            return null;
        }
    }

    /**
     * After the "login" method is called, the server makes a callback to this
     * method. At the minute, the password isn't verified in any way.
     *
     * @return A PasswordAuthentication instance with the username and password
     *         set in it.
     */
    public PasswordAuthentication getPasswordAuthentication() {
        logger.log(Level.INFO, "Logging in as " + player);
        String password = "guest";
        return new PasswordAuthentication(player, password.toCharArray());
    }

    /**
     * Another method called by the server on successful login.
     */
    public void loggedIn() {
        logger.info("Successfully logged in :-)");
        clientLoggedIn = true;
    }

    /**
     * If the login failed for some reason (eg server down, incorrect
     * password), then this method is called.
     *
     * @param reason The reason that this client could not login.
     */
    public void loginFailed(String reason) {
        logger.severe("Unable to login:" + reason);
    }

    /**
     * If the server joins us to a channel, then this method is called.
     *
     * @param cc The ClientChannel instance that we've joined.
     *
     * @return We need to create a listener that will handle incoming messages
     *         as well as publish anything to the channel.
     */
    public ClientChannelListener joinedChannel(ClientChannel cc) {
        logger.info("Joined channel " + cc.getName());

        this.clientChannel = cc;
        
        // We must return a ClientChannelListener back to the server, so that
        // it has a way to send us messages. Next time round, I am going to
        // make this class implement ClientChannelListener as well so that we
        // don't have to pass so many references around!
        return new TestClientChannelListener(clientInspector, player);
    }

    /**
     * Yep, this is called when a message is sent by the server ;)
     *
     * @param message The message sent.
     */
    public void receivedMessage(ByteBuffer message) {
        logger.info("Received message from server: " + message);
    }

    /**
     * Dunno what this does, but I could probably guess.
     */
    public void reconnecting() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not sure what this is doing either!
     */
    public void reconnected() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This must be called when we either get booted, or the server goes down.
     *
     * @param graceful Was the server being polite?
     * @param reason The reason we got disconnected (probably!)
     */
    public void disconnected(boolean graceful, String reason) {
        logger.info("Connection lost, graceful=" + graceful + ": " + reason);
    }
}
