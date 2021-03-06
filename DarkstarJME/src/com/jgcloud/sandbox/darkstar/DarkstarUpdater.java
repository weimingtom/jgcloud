package com.jgcloud.sandbox.darkstar;

import com.jgcloud.sandbox.jme.PlayerDetails;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * This is the class that will interact with the Darkstar server.
 *
 * @author Richard Hawkes
 */
public class DarkstarUpdater implements Runnable, SimpleClientListener, ClientChannelListener {
    private static Logger logger = Logger.getLogger(DarkstarUpdater.class.getName());

    private Node myTank; // Reference to the current players tank :)

    private String player = "testUser" + new Random().nextInt(10000);
    private String password = "ignored"; // Currently, no password authentication is used by the server

    /**
     * As other player locations are sent from the server, this queue will get
     * updated. It is then the job of the main JME thread to retrieve these
     * messages and update the screen.
     */
    public static Queue<PlayerDetails> playerDetailsQueue = new ConcurrentLinkedQueue<PlayerDetails>();

    /**
     * This class is going to be a singleton. I'm sicking of passing references
     * everywhere, so I'm going to create a static getter that will initially
     * instantiate it, and then in future return references to it.
     */
    private static DarkstarUpdater instance;
    
    /** 
     * A reference to the SimpleClient to allow logins and logouts.
     */
    private SimpleClient simpleClient;

    /**
     * I am going to use a synchronized collection for the client channels,
     * just in case the server decides to fire two channels at us at the same
     * time.
     */
    private Map<String,ClientChannel> playerChannels = Collections.synchronizedMap(new HashMap<String,ClientChannel>());

    /**
     * Specifies whether or not to update the server with our current location
     * and rotation. Once set to false, the thread will stop running altogether.
     */
    private boolean updateServerWithMyLocation = true;


    /** Store whether or not the player is logged in. Used by the update thread
     * to ensure it doesn't try to send messages that won't arrive.
     */
    private boolean playerLoggedIn = false;

    /**
     * Main constructor. Requires a reference to the players tank, so that we
     * can get its location/rotation on a regular basis. That's all we need
     * right now.
     *
     * Note that this is a "protected" constructor. That is because it is a
     * singleton class. See the getInstance(...) methods.
     *
     * @param myTank A reference to the players tank node.
     */
    protected DarkstarUpdater(Node myTank) {
        this.myTank = myTank;
        serverLogin();
        logger.info("DarkstarUpdater initialized.");
    }


    /**
     * Call to get the instance of this class. This is the method that MUST BE
     * CALLED FIRST, because it creates the reference to the myTank node. Future
     * calls can use the emtpty getInstance() method, because the myTank
     * reference will subsequently be in place.
     *
     * This is probably a bad way to implement a singleton! Searching the web
     * shows that it's certainly a hot topic.
     *
     * @param myTank The players tank node.
     *
     * @return The DarkstarUpdater reference.
     */
    public static DarkstarUpdater getInstance(Node myTank) {
        if (instance != null) {
            throw new RuntimeException("You should be calling the empty getInstance() method, because the myTank node has already been specified.");
        }

        instance = new DarkstarUpdater(myTank);

        return instance;
    }

    /**
     * Returns the DarkstarUpdager instance. Note that this method MUST BE 
     * CALLED after the initial call to getInstance(Node myTank) call.
     * 
     * @return The DarkstarUpdater reference.
     */
    public static DarkstarUpdater getInstance() {
            return instance;
    }

    
    /**
     * Initiates the login to the server.
     */
    private void serverLogin() {
        simpleClient = new SimpleClient(this);

        Properties connectProps = new Properties();
        connectProps.put("host", DarkstarConstants.DARKSTAR_SERVER_HOST);
        connectProps.put("port", DarkstarConstants.DARKSTAR_SERVER_PORT);

        // Now start the connection to the server
        try {
            logger.info("Logging in to host " + connectProps.getProperty("host") + ":" + connectProps.getProperty("port"));
            simpleClient.login(connectProps);
        } catch (IOException ex) {
            logger.severe("Unable to attempt login to Darkstar server: " + ex);
            playerLoggedIn = false; // I think it will already be false, but just in case.
        }
    }


    public void serverLogout() {
        if (playerLoggedIn) {
            simpleClient.logout(false);
        }
    }


    /**
     * This is the thread that will update the server every 100ms (we hope) with
     * this players location and rotation.
     */
    public void run() {
        while (updateServerWithMyLocation) {
            // Ensure we're logged in and that we have joined the
            // PLAYER_LOCATIONS channel. Otherwise, we're going to come a
            // cropper when trying to send the message to the server.

            if (playerLoggedIn && playerChannels.containsKey(DarkstarConstants.PLAYER_LOCATIONS_CHANNEL)) {
                String message = createPlayerLocationMessage();

                logger.fine(message);

                // Now send the message to the server.
                try {
                    playerChannels.get(DarkstarConstants.PLAYER_LOCATIONS_CHANNEL).send(encodeString(message));
                } catch (IOException ex) {
                    logger.severe("Unable to send message to " + DarkstarConstants.PLAYER_LOCATIONS_CHANNEL + ": " + ex);
                }
            }

            try { Thread.sleep(DarkstarConstants.LOCATION_UPDATE_INTERVAL_MS); } catch(Exception ex) { }
        }
    }


    /**
     * Creates the player details that are going to be sent to the server. This
     * is a comma-separated list.
     *
     * @return The players location details.
     */
    private String createPlayerLocationMessage() {
        Vector3f tankTranslation = myTank.getLocalTranslation();
        Quaternion tankRotation = myTank.getLocalRotation();

        // I'm going to use a StringBuffer to create the message string. This
        // might be a bit of a time/memory saver because it's going to be
        // running a lot!
        StringBuffer message = new StringBuffer();
        message.append("PN=").append(this.player).append(","); // Player name
        message.append("CT=").append(System.currentTimeMillis()).append(","); // Current time on the current PC
        message.append("TX=").append(tankTranslation.getX()).append(","); // Tank X location
        message.append("TY=").append(tankTranslation.getY()).append(","); // Tank Y location
        message.append("TZ=").append(tankTranslation.getZ()).append(","); // Tank Z location
        message.append("RX=").append(tankRotation.x).append(","); // Tank X rotation
        message.append("RY=").append(tankRotation.y).append(","); // Tank Y rotation
        message.append("RZ=").append(tankRotation.z).append(","); // Tank Z rotation
        message.append("RW=").append(tankRotation.w); // Tank W rotation ?

        return message.toString();
    }


    /**
     * Encodes the message in a format that's agreeable to the server.
     *
     * @param message The string to encode.
     *
     * @return A ByteBuffer representation of the String.
     */
    private ByteBuffer encodeString(String message) {
        try {
            return ByteBuffer.wrap(message.getBytes(DarkstarConstants.MESSAGE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            logger.severe("Required character set " + DarkstarConstants.MESSAGE_CHARSET + " not found" + e);
            return null;
        }
    }

    /**
     * The server sends messages in ByteBuffer back to us (in the same format
     * that we send it). Therefore, we can reverse the process to review any
     * incoming messages
     *
     * @param message The message (in ByteBuffer format) sent by the server.
     *
     * @return A String representation (ie something more readable).
     */
    private String decodeMessage(ByteBuffer message) {
        // Don't touch the original message, but make a copy. This keeps the
        // original message intact.
        ByteBuffer duplicateMessage = message.duplicate();

        try {
            byte[] bytes = new byte[duplicateMessage.remaining()];
            duplicateMessage.get(bytes);
            return new String(bytes, DarkstarConstants.MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            logger.severe("Required character set " + DarkstarConstants.MESSAGE_CHARSET + " not found: " + ex);
            return null;
        }
    }


    /**
     * Parses the locations message from the server by splitting on commas, and
     * then creating key/value pairs by splitting on the equals sign.
     *
     * @param message The original message.
     *
     * @return A key/value pair map of the parsed message.
     */
    private Map<String,String> parseMessage(String message) {
        Map<String,String> messageDetails = new HashMap<String,String>();

        String[] rows = message.split(",");

        for (String row : rows) {
            String[] keyValue = row.split("=");
            messageDetails.put(keyValue[0], keyValue[1]);
        }

        return messageDetails;
    }


    //
    // Start of SimpleClientListener interface methods
    //

    /**
     * This is called by the Darkstar server after login() is called by us.
     * We are expected to return a username and password in a
     * PasswordAuthentication object. Right now, the server will accept any
     * username and password.
     *
     * @return A PasswordAuthentication object with a username and password.
     */
    public PasswordAuthentication getPasswordAuthentication() {
        logger.info("Logging in as player " + player);
        return new PasswordAuthentication(player, password.toCharArray());
    }


    /**
     * Gets called by the server if it's happy and we're logged in.
     */
    public void loggedIn() {
        logger.info("Login successful");
        playerLoggedIn = true;
    }

    /**
     * If login failed (connection or username/password issue), this method will
     * get called.
     *
     * @param reason The reason it couldn't connect.
     */
    public void loginFailed(String reason) {
        logger.severe("Login failed: " + reason);
        playerLoggedIn = false; // Should already be false, but just in case this gets called somewhere that I don't know about
    }


    /**
     * If the server joins us to a new channel, we will be informed. It expects
     * us to give it a ClientChannelListener object so it can communicate with
     * us on that channel.
     *
     * @param clientChannel Details of the channel we've just joined.
     *
     * @return The ClientChannelListener that will handle incoming channel messages.
     */
    public ClientChannelListener joinedChannel(ClientChannel clientChannel) {
        logger.info("Darkstar server has joined us to channel " + clientChannel.getName());
        playerChannels.put(clientChannel.getName(), clientChannel);
        
        // The current class is also responsible for handling incoming messages
        // from the Darkstar server.
        return this;
    }


    /**
     * Called by the server for a generl (non-channel) broadcast from the
     * server.
     *
     * @param message The message sent.
     */
    public void receivedMessage(ByteBuffer message) {
        logger.fine("Received a non-channel message from the server: " + decodeMessage(message));
    }


    /**
     * Don't know what this does, but I can guess !!!
     */
    public void reconnecting() {
        logger.info("Reconnecting to Darkstar server");
    }


    /**
     * Don't know what this does, but I can guess !!!
     */
    public void reconnected() {
        logger.info("Reconnected to Darkstar server");
    }

    //
    // End of SimpleClientListener interface methods
    //    



    //
    // Start of ClientChannelListener interface methods...
    //

    /**
     * Called by the server if we get disconnected, or if we've failed to login.
     *
     * @param graceful Was it a rude disconnection?
     * @param reason The reason we were disconnected.
     */
    public void disconnected(boolean graceful, String reason) {
        logger.severe("We were disconnected from the Darkstar server (graceful=" + graceful + "): " + reason);
        playerLoggedIn = false;
    }


    /**
     * Called by the server when it sends a message on a channel.
     *
     * @param clientChannel The channel details.
     * @param message The message itself.
     */
    public void receivedMessage(ClientChannel clientChannel, ByteBuffer message) {
        String decodedMessage = decodeMessage(message);

        logger.fine("Received message on channel " + clientChannel.getName() + ": " + decodedMessage);

        if (message == null || decodedMessage.length() == 0) {
            logger.severe("Something screwy going on. Message is null or empty.");
            return;
        }
        
        // If we're being sent updates of another players location, we will add
        // a new update to the playerDetails queue.
        if (clientChannel.getName().equals(DarkstarConstants.PLAYER_LOCATIONS_CHANNEL)) {
            Map<String,String> messageDetails = parseMessage(decodedMessage);
            
            final PlayerDetails pd = new PlayerDetails(
                messageDetails.get("PN"),
                Float.parseFloat(messageDetails.get("TX")),
                Float.parseFloat(messageDetails.get("TY")),
                Float.parseFloat(messageDetails.get("TZ")),
                Float.parseFloat(messageDetails.get("RX")),
                Float.parseFloat(messageDetails.get("RY")),
                Float.parseFloat(messageDetails.get("RZ")),
                Float.parseFloat(messageDetails.get("RW")),
                Long.parseLong(messageDetails.get("CT"))
            );

            // Add a location update to the main thread (provided it didn't get
            // sent by us!).
            if (! pd.getPlayerName().equals(this.player)) {
                playerDetailsQueue.add(pd);
            } else {
                // Aha, this message was sent by us. We can see how long a round
                // trip takes to and from the server...
                long timeWeSentMessageToTheServer = pd.getLastUpdateTimeMillis();
                long timeNow = System.currentTimeMillis();
                long roundTripTime = timeNow - timeWeSentMessageToTheServer;
                logger.fine("Round trip time=" + roundTripTime + "ms");
            }
        }
    }

    /**
     * I'm assuming this is something the server will call if we get
     * disconnected from a channel.
     *
     * @param clientChannel The channel we got disconnected from!
     */
    public void leftChannel(ClientChannel clientChannel) {
        logger.info("Left channel " + clientChannel);
    }

    //
    // End of ClientChannelListener interface methods...
    //
}
