/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmotestclient;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Richard
 */
public class GUICanvas extends Canvas {

    private int xPos = 50;
    private int yPos = 50;
    private List<Point> squares = new ArrayList<Point>();
    public Map<String,Point> remotePlayerLocations = new HashMap<String,Point>();

    public GUICanvas() {
        squares.add(new Point(xPos, yPos));

        new Thread(new Runnable() {
            public void run() {
               while (true) {
                    try {
                        Thread.sleep(50);
                        repaint();
                    } catch(Exception ex) {

                    }
                }
            }
        }).start();
    }

    private int moveDistance = 12;
    private int squareSize = 10;

    public void paint(Graphics g) {
        super.paint(g);

//        for (Point p : squares) {
//            g.drawRect(p.x, p.y, squareSize, squareSize);
//        }

        Point lastSquare = squares.get(squares.size() - 1);
        g.fillRect(lastSquare.x, lastSquare.y, squareSize, squareSize);

        for (String remotePlayer : remotePlayerLocations.keySet()) {
            Point location = remotePlayerLocations.get(remotePlayer);
            g.fillRect((int)location.getX(), (int)location.getY(), squareSize, squareSize);
        }
    }

    public void moveUp() {
        yPos -= moveDistance;
        squares.add(new Point(xPos, yPos));
//        repaint();
    }

    public void moveDown() {
        yPos += moveDistance;
        squares.add(new Point(xPos, yPos));
//        repaint();
    }

    public void moveLeft() {
        xPos -= moveDistance;
        squares.add(new Point(xPos, yPos));
//        repaint();
    }

    public void moveRight() {
        xPos += moveDistance;
        squares.add(new Point(xPos, yPos));
//        repaint();
    }

    /**
     * @return the xPos
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * @return the yPos
     */
    public int getYPos() {
        return yPos;
    }
}
