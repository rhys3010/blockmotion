package uk.ac.aber.dcs.blockmotion.transformation;

/**
 * Subclass of Flip to flip the image along the X axis
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class X extends Flip implements Transformer{

    /**
     * Constructor to declare which way to flip the image
     */
    public X(){
        super.axis = Axes.X;
    }
}
