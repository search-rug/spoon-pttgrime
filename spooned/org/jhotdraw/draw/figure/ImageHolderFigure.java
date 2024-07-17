/* @(#)ImageHolderFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * The interface of a {@link Figure} which has some editable image contents.
 *
 * <p>The {@link org.jhotdraw.draw.tool.ImageTool} can be used to create figures which implement
 * this interface.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Prototype</em><br>
 * The image tool creates new figures by cloning a prototype figure object. That's the reason why
 * {@code Figure} extends the {@code Cloneable} interface. <br>
 * Prototype: {@link ImageHolderFigure}; Client: {@link org.jhotdraw.draw.tool.ImageTool}.
 *
 * <p><em>Prototype</em><br>
 * The image input format creates new image holder figures by cloning a prototype figure object and
 * assigning an image to it, which was read from data input. That's the reason why {@code Figure}
 * extends the {@code Cloneable} interface. <br>
 * Prototype: {@link ImageHolderFigure}; Client: {@link org.jhotdraw.draw.io.ImageInputFormat}. <hr>
 */
public interface ImageHolderFigure extends org.jhotdraw.draw.figure.Figure {
    /**
     * Loads an image from a File. By convention this method is never invoked on the AWT Event
     * Dispatcher Thread.
     */
    public void loadImage(java.io.File f) throws java.io.IOException;

    /**
     * Loads an image from an Input Stream. By convention this method is never invoked on the AWT
     * Event Dispatcher Thread.
     */
    public void loadImage(java.io.InputStream in) throws java.io.IOException;

    /**
     * Gets the buffered image from the figure.
     */
    public java.awt.image.BufferedImage getBufferedImage();

    /**
     * Sets the buffered image for the figure.
     */
    public void setBufferedImage(java.awt.image.BufferedImage image);

    /**
     * Sets the image.
     *
     * @param imageData
     * 		The image data. If this is null, a buffered image must be provided.
     * @param bufferedImage
     * 		An image constructed from the imageData. If this is null, imageData must
     * 		be provided.
     */
    public void setImage(byte[] imageData, java.awt.image.BufferedImage bufferedImage) throws java.io.IOException;

    /**
     * Gets the image data.
     *
     * @return imageData The image data, or null, if the ImageHolderFigure does not have an image.
     */
    public byte[] getImageData();
}