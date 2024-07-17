/* @(#)ImageInputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
import org.jhotdraw.draw.figure.Figure;
/**
 * An input format for importing drawings using one of the image formats supported by javax.imageio.
 *
 * <p>This class uses the prototype design pattern. A ImageHolderFigure figure is used as a
 * prototype for creating a figure that holds the imported image.
 *
 * <p>If the drawing is replaced using the loaded image, the size of the drawing is set to match the
 * size of the image using the attributes {@code AttributeKeys.CANVAS_WIDTH} and {@code AttributeKeys.CANVAS_HEIGHT}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Prototype</em><br>
 * The image input format creates new image holder figures by cloning a prototype figure object and
 * assigning an image to it, which was read from data input. That's the reason why {@code Figure}
 * extends the {@code Cloneable} interface. <br>
 * Prototype: {@link org.jhotdraw.draw.ImageHolderFigure}; Client: {@link ImageInputFormat}. <hr>
 *
 * @author Werner Randelshor
 * @version $Id$
 */
public class ImageInputFormat implements org.jhotdraw.draw.io.InputFormat {
    /**
     * The prototype for creating a figure that holds the imported image.
     */
    private org.jhotdraw.draw.figure.ImageHolderFigure prototype;

    /**
     * Format description used for the file filter.
     */
    private java.lang.String description;

    /**
     * File name extension used for the file filter.
     */
    private java.lang.String[] fileExtensions;

    /**
     * Image IO image format name.
     */
    private java.lang.String formatName;

    /**
     * The mime types which must be matched.
     */
    private java.lang.String[] mimeTypes;

    /**
     * Creates a new image input format for all formats supported by {@code javax.imageio.ImageIO}.
     */
    public ImageInputFormat(org.jhotdraw.draw.figure.ImageHolderFigure prototype) {
        this(prototype, "Image", "Image", javax.imageio.ImageIO.getReaderFileSuffixes(), javax.imageio.ImageIO.getReaderMIMETypes());
    }

    /**
     * Creates a new image input format for the specified image format.
     *
     * @param formatName
     * 		The format name for the javax.imageio.ImageIO object.
     * @param description
     * 		The format description to be used for the file filter.
     * @param fileExtension
     * 		The file extension to be used for the file filter.
     * @param mimeType
     * 		The mime type used for filtering data flavors from Transferable objects.
     */
    public ImageInputFormat(org.jhotdraw.draw.figure.ImageHolderFigure prototype, java.lang.String formatName, java.lang.String description, java.lang.String fileExtension, java.lang.String mimeType) {
        this(prototype, formatName, description, new java.lang.String[]{ fileExtension }, new java.lang.String[]{ mimeType });
    }

    /**
     * Creates a new image input format for the specified image format.
     *
     * @param formatName
     * 		The format name for the javax.imageio.ImageIO object.
     * @param description
     * 		The format description to be used for the file filter.
     * @param fileExtensions
     * 		The file extensions to be used for the file filter.
     * @param mimeTypes
     * 		The mime typse used for filtering data flavors from Transferable objects.
     */
    public ImageInputFormat(org.jhotdraw.draw.figure.ImageHolderFigure prototype, java.lang.String formatName, java.lang.String description, java.lang.String[] fileExtensions, java.lang.String[] mimeTypes) {
        this.prototype = prototype;
        this.formatName = formatName;
        this.description = description;
        this.fileExtensions = fileExtensions.clone();
        this.mimeTypes = mimeTypes.clone();
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtensions);
    }

    public java.lang.String[] getFileExtensions() {
        return fileExtensions.clone();
    }

    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        org.jhotdraw.draw.figure.ImageHolderFigure figure = createImageHolder(in);
        if (replace) {
            drawing.removeAllChildren();
            drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH, figure.getBounds().width);
            drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT, figure.getBounds().height);
        }
        drawing.basicAdd(figure);
    }

    public org.jhotdraw.draw.figure.ImageHolderFigure createImageHolder(java.io.InputStream in) throws java.io.IOException {
        org.jhotdraw.draw.figure.ImageHolderFigure figure = ((org.jhotdraw.draw.figure.ImageHolderFigure) (prototype.clone()));
        figure.loadImage(in);
        figure.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double(figure.getBufferedImage().getWidth(), figure.getBufferedImage().getHeight()));
        return figure;
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        if (java.awt.datatransfer.DataFlavor.imageFlavor.match(flavor)) {
            return true;
        }
        for (java.lang.String mimeType : mimeTypes) {
            if (flavor.isMimeTypeEqual(mimeType)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void read(java.awt.datatransfer.Transferable t, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        java.awt.datatransfer.DataFlavor importFlavor = null;
        SearchLoop : for (java.awt.datatransfer.DataFlavor flavor : t.getTransferDataFlavors()) {
            if (java.awt.datatransfer.DataFlavor.imageFlavor.match(flavor)) {
                importFlavor = flavor;
                break SearchLoop;
            }
            for (java.lang.String mimeType : mimeTypes) {
                if (flavor.isMimeTypeEqual(mimeType)) {
                    importFlavor = flavor;
                    break SearchLoop;
                }
            }
        }
        java.lang.Object data = t.getTransferData(importFlavor);
        java.awt.Image img = null;
        if (data instanceof java.awt.Image) {
            img = ((java.awt.Image) (data));
        } else if (data instanceof java.io.InputStream) {
            img = javax.imageio.ImageIO.read(((java.io.InputStream) (data)));
        }
        if (img == null) {
            throw new java.io.IOException("Unsupported data format " + importFlavor);
        }
        org.jhotdraw.draw.figure.ImageHolderFigure figure = ((org.jhotdraw.draw.figure.ImageHolderFigure) (prototype.clone()));
        figure.setBufferedImage(org.jhotdraw.util.Images.toBufferedImage(img));
        figure.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double(figure.getBufferedImage().getWidth(), figure.getBufferedImage().getHeight()));
        java.util.List<org.jhotdraw.draw.figure.Figure> list = java.util.List.of(figure);
        if (replace) {
            drawing.removeAllChildren();
            drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH, figure.getBounds().width);
            drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT, figure.getBounds().height);
        }
        drawing.addAll(list);
    }
}