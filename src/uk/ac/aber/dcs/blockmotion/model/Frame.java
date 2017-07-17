package uk.ac.aber.dcs.blockmotion.model;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Rhys Evans (rhe24@aber.ac.uk)
 * @date 1/5/2017
 */
public class Frame implements IFrame{

    private int numRows;
    private char[][] frame;

    /**
     * Initialise the frame dimensions
     * @param nr
     */
    public Frame(int nr){
        numRows = nr;
        frame = new char[numRows][numRows];

    }


    /**
     * Add lines bottom up to be consistent with file
     * @param lines an array of lines
     */
    public void insertLines(String lines[]){
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numRows; j++){
                frame[i][j] = lines[numRows - 1 - i].charAt(j);
            }
        }

    }

    /**
     * Returns the number of rows in the frame
     * @return
     */
    public int getNumRows(){

        return numRows;
    }

    /**
     * Writes the frame to a given open outfile
     * (not in camel case because of typo in interface)
     * @param outfile the open file to write to
     */
    public void tofile(PrintWriter outfile) throws IOException{

        // Iterate through all positions and write the array to file
        for (int i = numRows-1; i >= 0; i--) {
            for (int j = 0; j < numRows; j++) {
                outfile.print(frame[i][j]);
            }
            // New line
            outfile.println("");
        }
    }

    /**
     * Returns the char at a given position
     * @param i the row
     * @param j the column
     * @return
     */
    public char getChar(int i, int j){

        return frame[i][j];

    }

    /**
     * Set character of specific frame position
     * @param i the row
     * @param j the column
     * @param ch the character to set
     */
    public void setChar(int i, int j, char ch){

        frame[i][j] = ch;

    }

    /**
     * Create and return an exact copy of the current frame.
     * @return frameCopy
     */
    public IFrame copy(){

        Frame frameCopy = new Frame(numRows);

        // Replace content of new frame with content of current frame
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numRows; j++){
                frameCopy.setChar(i, j, frame[i][j]);
            }
        }

        return frameCopy;
    }

    /**
     * Replace the contents of the current frame by the provided frame (f)
     * @param f the frame to insert
     */
    public void replace(IFrame f){

        numRows = f.getNumRows();

        // Replace content of current frame with that of the old frame
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numRows; j++){
                frame[i][j] = f.getChar(i, j);
            }
        }


    }

}
