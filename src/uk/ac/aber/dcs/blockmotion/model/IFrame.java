package uk.ac.aber.dcs.blockmotion.model;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * This interface represents a single frame.
 * YOU MUST NOT CHANGE THIS INTERFACE!
 * @author Chris Loftus
 * @version 1.0, 14th March 2017
 */
public interface IFrame {
    /**
     * Insert the given array of lines into the frame. Must be the same size
     * as the frame; i.e. same number of rows.
     * @param lines an array of lines
     * @throws IllegalArgumentException if lines size is not the same size of the frame
     */
    public abstract void insertLines(String lines[]);

    /**
     * The number of rows
     * @return the number of rows
     */
    public abstract int getNumRows();

    /**
     * Writes the frame to the given open outfile
     * @param outfile the open file to write to
     * @throws IllegalArgumentException if outfile is null or closed
     */
    public abstract void tofile(PrintWriter outfile) throws IOException;

    /**
     * Gets the character from the given position in the grid
     * @param i the row
     * @param j the column
     * @return the found character
     * @throws IllegalArgumentException if i,j do not represent a valid place in the frame
     */
    public abstract char getChar(int i, int j);

    /**
     * Sets the character in the given position on the grid
     * @param i the row
     * @param j the column
     * @param ch the character to set
     * @throws IllegalArgumentException if i,j do not represent a valid place in the frame
     */
    public abstract void setChar(int i, int j, char ch);

    /**
     * Returns a copy of the current frame
     * @return the exact copy as a distinct object
     */
    public abstract IFrame copy();

    /**
     * Replaces the contents of the currect frame by the provided frame
     * @param f the frame to insert
     * @throws IllegalArgumentException if f is a different size or null
     */
    public abstract void replace(IFrame f);
}
