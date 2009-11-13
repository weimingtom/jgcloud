/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mmotestclient;

/**
 * A simple interface that the GUI will implement to give just the location of
 * the cursor. It's just a way of only exposing the x and y positions to the
 * ServerLocationUpdater thread.
 *
 * @author Richard Hawkes
 */
public interface ClientInspector {
    public int getXPos();
    public int getYPos();
    
    /**
     * Sends details of other player locations.
     * 
     * @param player The remote player name
     * @param xPos Their x position
     * @param yPos Their y position
     */
    public void updatePlayerLocation(String player, int xPos, int yPos);
}
