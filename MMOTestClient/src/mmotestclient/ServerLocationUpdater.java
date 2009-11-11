/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Richard
 */
public class ServerLocationUpdater implements Runnable, SimpleClientListener {
    private SimpleClient simpleClient;

    private static Logger logger = Logger.getLogger(ServerLocationUpdater.class.getName());
    private ClientInspector clientInspector;
    private boolean clientLoggedIn = false;

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    public ServerLocationUpdater(ClientInspector clientInspector) {
        this.clientInspector = clientInspector;
        this.simpleClient = new SimpleClient(this);
        login();
    }

    public void login() {
        Properties connectProps = new Properties();
        connectProps.put("host", "192.168.1.101");
        connectProps.put("port", "1139");
        
        try {
            simpleClient.login(connectProps);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to login to server: " + ex);
        }
    }

    public void run() {
        while (true) {
            logger.log(Level.INFO, "x=" + clientInspector.getXPos() + " y=" + clientInspector.getYPos());

            try {
                if (clientLoggedIn) {
                    String message = "x=" + clientInspector.getXPos() +",y=" + clientInspector.getYPos();
                    simpleClient.send(ByteBuffer.wrap(message.getBytes()));
                }
            } catch (IOException ex) {
                logger.severe("Unable to send message to server: " + ex);
                clientLoggedIn = false;
            }
            
            try { Thread.sleep(500); } catch (Exception ex) { }
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

    public PasswordAuthentication getPasswordAuthentication() {
        String player = "guest-" + new Random().nextInt(1000);
        logger.log(Level.INFO, "Logging in as " + player);
        String password = "guest";
        return new PasswordAuthentication(player, password.toCharArray());
    }

    public void loggedIn() {
        logger.info("Successfully logged in :-)");
        clientLoggedIn = true;
    }

    public void loginFailed(String reason) {
        logger.severe("Unable to login:" + reason);
    }

    public ClientChannelListener joinedChannel(ClientChannel arg0) {
//        throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    public void receivedMessage(ByteBuffer message) {
        logger.info("Received message from server: " + message);
    }

    public void reconnecting() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reconnected() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disconnected(boolean arg0, String arg1) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
