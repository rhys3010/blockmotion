package uk.ac.aber.dcs.blockmotion.transformation;

import uk.ac.aber.dcs.blockmotion.model.IFrame;

/**
 * Subclass of transformation - handles all sliding transfomrations
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class Slide implements Transformer {

    // Variables to hold the direction at which the footage should slide (-1 OR 1)
    int dirX;
    int dirY;
    // Number to slide footage by. Set to 1 as default
    int slideN = 1;



    /**
     * Slide the given frame
     * @param frame
     * @throws IllegalArgumentException
     */
    public void transform(IFrame frame) throws IllegalArgumentException{

        IFrame frameCopy = frame.copy();

        for(int i = 0; i < frameCopy.getNumRows(); i++){
            for(int j = 0; j < frameCopy.getNumRows(); j++){

                // Variables to hold the position of the animation
                // in order to track when it runs off the screen.
                int xPos = j + (slideN * dirX);
                int yPos = i + (slideN * dirY);


                // Conditionals to check if the animation is running off the screen
                // and to wrap around if they are. Using modulus operator
                if(xPos >= frameCopy.getNumRows()){
                    xPos %= frameCopy.getNumRows();
                }
                else if(xPos < 0){
                    xPos += frameCopy.getNumRows();
                }

                if(yPos >= frameCopy.getNumRows()){
                    yPos %= frameCopy.getNumRows();
                }
                else if(yPos < 0){
                    yPos += frameCopy.getNumRows();
                }

                // Slide the frame in a given direction based on the contents of the original frame
                frameCopy.setChar(i, j, frame.getChar(yPos, xPos));
            }
        }

        frame.replace(frameCopy);
    }



    /**
     * Set the number to slide by
     * @param n
     */
    public void setSlideN(int n){
        this.slideN = n;
    }
}
