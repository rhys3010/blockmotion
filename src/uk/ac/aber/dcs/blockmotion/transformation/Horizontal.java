package uk.ac.aber.dcs.blockmotion.transformation;

/**
 * Subclass of the Slide transformation, for sliding horizontally
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class Horizontal extends Slide implements Transformer{

    /**
     * Constructor for horizontal sliding
     * @param dir
     */
    public Horizontal(Directions dir){
        // Change direction of superclass transformation
        if(dir == Directions.LEFT){
            super.dirX = 1;

        }else if(dir == Directions.RIGHT){
            super.dirX = -1;

        }else{
            super.dirX = 0;
        }

    }



}
