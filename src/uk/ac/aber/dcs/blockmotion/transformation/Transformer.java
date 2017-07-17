package uk.ac.aber.dcs.blockmotion.transformation;

import uk.ac.aber.dcs.blockmotion.model.IFrame;

/**
 * All transformer classes must implement this interace
 * @author Chris Loftus
 * @version 1.0, 9th March 2017
 */
public interface Transformer {
    /**
     * Transforms the provided frame
     * @param frame the frame to transform
     * @throws IllegalArgumentException if the frame is null.
     */
    public abstract void transform(IFrame frame);
}
