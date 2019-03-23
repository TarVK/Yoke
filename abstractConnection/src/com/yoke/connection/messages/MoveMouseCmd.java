package com.yoke.connection.messages;

import com.yoke.connection.Message;


/**
 * A command representing mouse movements
 */
public class MoveMouseCmd extends Message {
    // Serialization ID
    private static final long serialVersionUID = 4796051523764478340L;

    // The amount of pixels to move on the x axis
    public int x;
    
    // The amount of pixels to move on the y axis
    public int y;
    
    // Whether to treat x and y as coordinates rather than offets
    public boolean absolute;
    
    /**
     * Creates a move mouse command
     * @param x  The number of pixels to move on the x axis
     * @param y  The number of pixels to move on the y axis
     */
    public MoveMouseCmd(int x, int y) {
        this(x, y, false);
    }
    

    /**
     * Creates a move mouse command
     * @param x  The number of pixels to move on the x axis
     * @param y  The number of pixels to move on the y axis
     * @param absolute  Whether the x and y represent absolute coordinates, rather than deltas
     */
    public MoveMouseCmd(int x, int y, boolean absolute) {
        this.x = x;
        this.y = y;
        this.absolute = absolute;
    }
}
