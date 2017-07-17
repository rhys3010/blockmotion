package uk.ac.aber.dcs.blockmotion.model;

import uk.ac.aber.dcs.blockmotion.transformation.Transformer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Rhys Evans (rhe24@aber.ac.uk)
 * @date 1/5/2017
 */
public class Footage implements IFootage {

    private int numRows;
    private int numFrames;
    private ArrayList<IFrame> frames;

    /**
     * Constructor for the Footage class
     */
    public Footage(){
        frames = new ArrayList<>();

    }

    /**
     * Return number of frames within the footage
     * @return
     */
    public int getNumFrames(){

        return numFrames;
    }

    /**
     * Return the number of rows within the footage
     * @return
     */
    public int getNumRows(){

        return numRows;
    }

    /**
     * Return a frame object from the arraylist
     * @param num the position of the frame starting from 0
     * @return
     */
    public IFrame getFrame(int num){

        return frames.get(num);
    }

    /**
     * Add frame to footage
     * @param f the frame to add
     */
    public void add(IFrame f){
        numFrames++;
        frames.add(f);
    }

    /**
     * Remove frame from footage
     * @param n
     */
    public void remove(int n){
        frames.remove(n);
        numFrames--;
    }

    /**
     * Load footage file (.txt)
     * @param fn
     * @throws IOException
     */
    public void load(String fn) throws IOException{
        // Clear currently loaded frames
        frames.clear();
        Scanner infile = new Scanner(new FileReader(fn));
        infile.useDelimiter("\r?\n|\r");

        // Read the number of frames and rows from file
        this.numFrames = infile.nextInt();
        infile.nextLine();
        this.numRows = infile.nextInt();
        infile.nextLine();

        // READ FRAME CHARACTERS FROM BOTTOM UP
        // Nested for loop to collect data about each frame.
        // EACH FRAME
        for(int i = numFrames-1; i >= 0; i--) {
            //Create frame
            Frame newFrame = new Frame(numRows);

            // Iterate through rows and cols to collect chars fill the array from bottom up
            // EACH ROW
            for (int j = numRows-1; j >= 0; j--) {
                String rowInput = infile.next();
                // EACH POSITION (shape is square therefore numRows = numCols)
                for (int k = numRows-1; k >= 0; k--) {
                    newFrame.setChar(j, k, rowInput.charAt(k));
                }

            }

            // Add frame to arraylist after collecting info
            frames.add(newFrame);
        }

        infile.close();
    }


    /**
     * Save footage file (.txt)
     * @param fn the file name
     * @throws IOException
     */
    public void save(String fn) throws IOException{
        PrintWriter outfile = new PrintWriter(new FileWriter(fn, false));

        outfile.println(numFrames);
        outfile.println(numRows);

        // Iterate through all frames and write to file
        for(IFrame f : frames){
            f.tofile(outfile);
        }

        outfile.close();
    }


    /**
     * Transform all frames in the footage
     * @param t the transformer
     */
    public void transform(Transformer t){
        //Iterate through all frames and apply transformation
        for(IFrame f:frames){
            t.transform(f);
        }
    }


}
