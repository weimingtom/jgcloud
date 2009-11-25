package com.jgcloud.sandbox.darkstar;

/**
 *
 * @author Richard Hawkes
 */
public class DarkstarConstants {
    
    /**
     * Constructor is private, as this is not meant to be instantiated.
     */
    private DarkstarConstants() { }

    public static final String DARKSTAR_SERVER_HOST = "192.168.1.101";
    public static final String DARKSTAR_SERVER_PORT = "1139";

    public static final String MESSAGE_CHARSET = "UTF-8";
    public static final String PLAYER_LOCATIONS_CHANNEL = "PLAYER_LOCATIONS";

    public static final long LOCATION_UPDATE_INTERVAL_MS = 500;
}
