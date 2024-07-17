/* @(#)ImageOutputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * An output format for exporting drawings using one of the image formats supported by
 * javax.imageio.
 */
public class ImageOutputFormat implements org.jhotdraw.draw.io.OutputFormat {
    /**
     * Format description used for the file filter.
     */
    private java.lang.String description;

    /**
     * File name extension used for the file filter.
     */
    private java.lang.String fileExtension;

    /**
     * Image IO image format name.
     */
    private java.lang.String formatName;

    /**
     * The image type must match the output format, for example, PNG supports
     * BufferedImage.TYPE_INT_ARGB whereas GIF needs BufferedImage.TYPE_
     */
    private int imageType;

    /**
     * Creates a new image output format for Portable Network Graphics PNG.
     */
    public ImageOutputFormat() {
        this("PNG", "Portable Network Graphics (PNG)", "png", java.awt.image.BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Creates a new image output format for the specified image format.
     *
     * @param formatName
     * 		The format name for the javax.imageio.ImageIO object.
     * @param description
     * 		The format description to be used for the file filter.
     * @param fileExtension
     * 		The file extension to be used for file filter.
     * @param bufferedImageType
     * 		The BufferedImage type used to produce the image. The value of this
     * 		parameter must match with the format name.
     */
    public ImageOutputFormat(java.lang.String formatName, java.lang.String description, java.lang.String fileExtension, int bufferedImageType) {
        this.formatName = formatName;
        this.description = description;
        this.fileExtension = fileExtension;
        this.imageType = bufferedImageType;
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtension);
    }

    @java.lang.Override
    public java.lang.String getFileExtension() {
        return fileExtension;
    }

    @java.lang.Override
    public void write(java.net.URI uri, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        write(new java.io.File(uri), drawing);
    }

    /**
     * Writes the drawing to the specified file. This method ensures that all figures of the drawing
     * are visible on the image.
     */
    public void write(java.io.File file, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        try (java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file))) {
            write(out, drawing);
        }
    }

    /**
     * Writes the drawing to the specified output stream. This method ensures that all figures of the
     * drawing are visible on the image.
     */
    @java.lang.Override
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        write(out, drawing, drawing.getChildren(), null, null);
    }

    /**
     * Writes the drawing to the specified output stream. This method applies the specified transform
     * to the drawing, and draws it on an image of the specified size.
     */
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing, java.awt.geom.AffineTransform drawingTransform, java.awt.Dimension imageSize) throws java.io.IOException {
        write(out, drawing, drawing.getChildren(), drawingTransform, imageSize);
    }

    /**
     * Writes the drawing to the specified output stream. This method ensures that all figures of the
     * drawing are visible on the image.
     */
    @java.lang.Override
    public java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor) throws java.io.IOException {
        return new org.jhotdraw.datatransfer.ImageTransferable(toImage(drawing, figures, scaleFactor, true));
    }

    /**
     * Writes the figures to the specified output stream. This method ensures that all figures of the
     * drawing are visible on the image.
     */
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures) throws java.io.IOException {
        write(out, drawing, figures, null, null);
    }

    /**
     * Writes the figures to the specified output stream. This method applies the specified transform
     * to the drawing, and draws it on an image of the specified size.
     */
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, java.awt.geom.AffineTransform drawingTransform, java.awt.Dimension imageSize) throws java.io.IOException {
        java.awt.image.BufferedImage img;
        if ((drawingTransform == null) || (imageSize == null)) {
            img = toImage(drawing, figures, 1.0, false);
        } else {
            img = toImage(drawing, figures, drawingTransform, imageSize);
        }
        javax.imageio.ImageIO.write(img, formatName, out);
        img.flush();
    }

    /**
     * Creates a BufferedImage from the specified list of figures.
     *
     * <p>The images are drawn using the specified scale factor. If some figures have a drawing area
     * located at negative coordinates, then the drawing coordinates are translated, so that all
     * figures are visible on the image.
     *
     * @param drawing
     * 		The drawing.
     * @param figures
     * 		A list of figures of the drawing.
     * @param scaleFactor
     * 		The scale factor used when drawing the figures.
     * @param clipToFigures
     * 		If this is true, the image is clipped to the figures. If this is false,
     * 		the image includes the drawing area,
     */
    public java.awt.image.BufferedImage toImage(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor, boolean clipToFigures) {
        // Return a transparent 1-pixel image if the drawing is empty.
        if (drawing.getChildCount() == 0) {
            return new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        }
        // Determine the draw bounds of the figures
        java.awt.geom.Rectangle2D.Double drawBounds = null;
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            if (drawBounds == null) {
                drawBounds = f.getDrawingArea();
            } else {
                drawBounds.add(f.getDrawingArea());
            }
        }
        if (clipToFigures) {
            java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
            transform.translate((-drawBounds.x) * scaleFactor, (-drawBounds.y) * scaleFactor);
            transform.scale(scaleFactor, scaleFactor);
            return toImage(drawing, figures, transform, new java.awt.Dimension(((int) (drawBounds.width * scaleFactor)), ((int) (drawBounds.height * scaleFactor))));
        } else {
            java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
            if (drawBounds.x < 0) {
                transform.translate((-drawBounds.x) * scaleFactor, 0);
            }
            if (drawBounds.y < 0) {
                transform.translate(0, (-drawBounds.y) * scaleFactor);
            }
            transform.scale(scaleFactor, scaleFactor);
            return toImage(drawing, figures, transform, new java.awt.Dimension(((int) ((java.lang.Math.max(0, drawBounds.x) + drawBounds.width) * scaleFactor)), ((int) ((java.lang.Math.max(0, drawBounds.y) + drawBounds.height) * scaleFactor))));
        }
    }

    /**
     * Creates a BufferedImage from the specified list of figures.
     *
     * @param drawing
     * 		The drawing.
     * @param figures
     * 		A list of figures of the drawing.
     * @param transform
     * 		The AffineTransform to be used when drawing the figures.
     * @param imageSize
     * 		The width and height of the image.
     */
    public java.awt.image.BufferedImage toImage(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, java.awt.geom.AffineTransform transform, java.awt.Dimension imageSize) {
        // Create the buffered image and clear it
        java.awt.Color background = drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR);
        double opacity = drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY);
        if (background == null) {
            background = new java.awt.Color(0xff, 0xff, 0xff, 0x0);
        } else {
            background = new java.awt.Color(background.getRed(), background.getGreen(), background.getBlue(), ((int) (background.getAlpha() * opacity)));
        }
        java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(java.lang.Math.max(1, imageSize.width), java.lang.Math.max(1, imageSize.height), background.getAlpha() == 255 ? java.awt.image.BufferedImage.TYPE_INT_RGB : java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = buf.createGraphics();
        // Clear the buffered image with the background color
        java.awt.Composite savedComposite = g.getComposite();
        g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC));
        g.setColor(background);
        g.fillRect(0, 0, buf.getWidth(), buf.getHeight());
        g.setComposite(savedComposite);
        // Draw the figures onto the buffered image
        setRenderingHints(g);
        g.transform(transform);
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            f.draw(g);
        }
        g.dispose();
        // Convert the image, if it does not have the specified image type
        if (imageType != java.awt.image.BufferedImage.TYPE_INT_ARGB) {
            java.awt.image.BufferedImage buf2 = new java.awt.image.BufferedImage(buf.getWidth(), buf.getHeight(), imageType);
            g = buf2.createGraphics();
            setRenderingHints(g);
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC));
            g.drawImage(buf, 0, 0, null);
            g.dispose();
            buf.flush();
            buf = buf2;
        }
        return buf;
    }

    protected void setRenderingHints(java.awt.Graphics2D g) {
        g.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}