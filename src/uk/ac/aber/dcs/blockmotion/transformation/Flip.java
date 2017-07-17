package uk.ac.aber.dcs.blockmotion.transformation;

import uk.ac.aber.dcs.blockmotion.model.IFrame;

/**
 * Subclass of Transformation - handles vertical and horizontal rotations
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class Flip implements Transformer {

    // The Axis around which to flip the footage
    Axes axis;


    /**
     * Flip the footage along a given axis
     * @param frame the frame to transform
     * @throws IllegalArgumentException
     */
    public void transform(IFrame frame) throws IllegalArgumentException{

        IFrame frameCopy = frame.copy();

        // Flip the image vertically by 'mirroring' the image using the copied frame.
        for(int i = 0; i < frame.getNumRows(); i++){
            for(int j = 0; j < frame.getNumRows(); j++){

                // Flip Vertically
                if(axis == Axes.X) {
                    frameCopy.setChar(frameCopy.getNumRows() - i - 1, j, frame.getChar(i, j));
                }

                // Flip Horizontally
                else if(axis == Axes.Y){
                    frameCopy.setChar(i, frameCopy.getNumRows() - j - 1, frame.getChar(i, j));
                }
            }
        }

        // Replace contents of frame with the modified copied frame
        frame.replace(frameCopy);
    }

}
