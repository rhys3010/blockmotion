package uk.ac.aber.dcs.blockmotion.transformation;

/**
 * Subclass of the flip class to flip the footage along the Y axis
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class Y extends Flip implements Transformer {

    /**
     * Constructor to declare which way to flip the image
     */
    public Y(){
        super.axis = Axes.Y;
    }
}
