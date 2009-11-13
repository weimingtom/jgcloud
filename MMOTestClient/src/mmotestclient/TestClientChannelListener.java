package mmotestclient;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * When we receive a message on a channel from the server, this is the class
 * that handles it. It's created by the ServerLocationUpdater.joinedChannel
 * method every time the server throws us a new channel. So it would need a
 * rethink in a mult-channel environment.
 *
 * Thinking about it, this should probably be implemented in the
 * ServerLocationUpdater thread instead!
 *
 * @author Richard Hawkes
 */
public class TestClientChannelListener implements ClientChannelListener {

    private static Logger logger = Logger.getLogger(TestClientChannelListener.class.getName());
    private static final String MESSAGE_CHARSET = "UTF-8";
    private ClientInspector client;
    private String playerName;

    public TestClientChannelListener(ClientInspector client, String playerName) {
        this.client = client;
        this.playerName = playerName;
    }

    public void receivedMessage(ClientChannel cc, ByteBuffer message) {
        String decodedMessage = decodeMessage(message);
        logger.info("Received message! " + decodedMessage);

        String[] fields = decodedMessage.split(",");
        String player = fields[0].trim();
        int xPos = Integer.parseInt(fields[1].trim());
        int yPos = Integer.parseInt(fields[2].trim());

        // As the channel also receives the location message it sends itself, we
        // only update other player locations.
        if (!player.equals(playerName)) {
            client.updatePlayerLocation(player, xPos, yPos);
        }
    }

    public void leftChannel(ClientChannel cc) {
        logger.info("Left channel " + cc.getName());
    }

    private static String decodeMessage(ByteBuffer buf) {
        try {
            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);
            return new String(bytes, MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            logger.severe("Required character set " + MESSAGE_CHARSET + " not found: " + ex);
            return null;
        }
    }
}
