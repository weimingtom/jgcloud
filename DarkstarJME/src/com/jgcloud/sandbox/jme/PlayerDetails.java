package com.jgcloud.sandbox.jme;

import com.jme.math.*;


/**
 *
 * @author Richard
 */
public class PlayerDetails {
    private String playerName;

    private Vector3f location;
    private Quaternion rotation;
    private long lastUpdateTimeMillis;

    public PlayerDetails(String playerName, Vector3f location, Quaternion rotation, long lastUpdateTimeMillis) {
        this.playerName = playerName;
        this.location = location;
        this.rotation = rotation;
        this.lastUpdateTimeMillis = lastUpdateTimeMillis;
    }

    public PlayerDetails(String playerName, float lx, float ly, float lz, float rx, float ry, float rz, float rw, long lastUpdateTimeMills) {
        this(
            playerName,
            new Vector3f(lx, ly, lz),
            new Quaternion(rx, ry, rz, rw),
            lastUpdateTimeMills
        );
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return the location
     */
    public Vector3f getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Vector3f location) {
        this.location = location;
    }

    /**
     * @return the rotation
     */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the lastUpdateTimeMillis
     */
    public long getLastUpdateTimeMillis() {
        return lastUpdateTimeMillis;
    }

    /**
     * @param lastUpdateTimeMillis the lastUpdateTimeMillis to set
     */
    public void setLastUpdateTimeMillis(long lastUpdateTimeMillis) {
        this.lastUpdateTimeMillis = lastUpdateTimeMillis;
    }


}