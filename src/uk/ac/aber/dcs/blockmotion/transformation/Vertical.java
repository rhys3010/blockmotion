package uk.ac.aber.dcs.blockmotion.transformation;

/**
 * Subclass of Slide for vertical sliding
 * @author Rhys Evans (rhe24@aber.ac.uk)
 */
public class Vertical extends Slide implements  Transformer{

    public Vertical(Directions dir){
        if(dir == Directions.UP){
            super.dirY = 1;

        }else if(dir == Directions.DOWN){
            super.dirY = -1;

        }else{
            super.dirY = 0;
        }

    }

}
