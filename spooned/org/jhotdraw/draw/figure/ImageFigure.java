/* @(#)ImageFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A default implementation of {@link ImageHolderFigure} which can hold a buffered image.
 */
public class ImageFigure extends org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure implements org.jhotdraw.draw.figure.ImageHolderFigure {
    private static final long serialVersionUID = 1L;

    /**
     * This rectangle describes the bounds into which we draw the image.
     */
    private java.awt.geom.Rectangle2D.Double rectangle;

    /**
     * The image data. This can be null, if the image was created from a BufferedImage.
     */
    private byte[] imageData;

    /**
     * The buffered image. This can be null, if we haven't yet parsed the imageData.
     */
    private transient java.awt.image.BufferedImage bufferedImage;

    public ImageFigure() {
        this(0, 0, 0, 0);
    }

    public ImageFigure(double x, double y, double width, double height) {
        rectangle = new java.awt.geom.Rectangle2D.Double(x, y, width, height);
    }

    // DRAWING
    @java.lang.Override
    protected void drawFigure(java.awt.Graphics2D g) {
        if (attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) != null) {
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR));
            drawFill(g);
        }
        drawImage(g);
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) != null) && (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) > 0.0)) {
            g.setStroke(org.jhotdraw.draw.AttributeKeys.getStroke(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR));
            drawStroke(g);
        }
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR) != null) {
            if ((attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_COLOR) != null) && (attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_OFFSET) != null)) {
                org.jhotdraw.geom.Dimension2DDouble d = attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_OFFSET);
                g.translate(d.width, d.height);
                g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_COLOR));
                drawText(g);
                g.translate(-d.width, -d.height);
            }
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR));
            drawText(g);
        }
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        g.fill(r);
    }

    protected void drawImage(java.awt.Graphics2D g) {
        java.awt.image.BufferedImage image = getBufferedImage();
        if (image != null) {
            g.drawImage(image, ((int) (rectangle.x)), ((int) (rectangle.y)), ((int) (rectangle.width)), ((int) (rectangle.height)), null);
        } else {
            g.setStroke(new java.awt.BasicStroke());
            g.setColor(java.awt.Color.red);
            g.draw(rectangle);
            g.draw(new java.awt.geom.Line2D.Double(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height));
            g.draw(new java.awt.geom.Line2D.Double(rectangle.x + rectangle.width, rectangle.y, rectangle.x, rectangle.y + rectangle.height));
        }
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        g.draw(r);
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Rectangle2D.Double bounds = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        return bounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getFigureDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale);
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean figureContains(java.awt.geom.Point2D.Double p, double scale) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale) + 1.0;
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r.contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        rectangle.x = java.lang.Math.min(anchor.x, lead.x);
        rectangle.y = java.lang.Math.min(anchor.y, lead.y);
        rectangle.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        rectangle.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		The transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    // ATTRIBUTES
    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        rectangle.setRect(((java.awt.geom.Rectangle2D.Double) (geometry)));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
    }

    // EDITING
    @java.lang.Override
    public java.util.Collection<javax.swing.Action> getActions(java.awt.geom.Point2D.Double p) {
        return java.util.Collections.emptyList();
    }

    // CONNECTING
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        // XXX - This doesn't work with a transformed rect
        return new org.jhotdraw.draw.connector.ChopRectangleConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        // XXX - This doesn't work with a transformed rect
        return new org.jhotdraw.draw.connector.ChopRectangleConnector(this);
    }

    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.draw.figure.ImageFigure clone() {
        org.jhotdraw.draw.figure.ImageFigure that = ((org.jhotdraw.draw.figure.ImageFigure) (super.clone()));
        that.rectangle = ((java.awt.geom.Rectangle2D.Double) (this.rectangle.clone()));
        return that;
    }

    /**
     * Sets the image.
     *
     * @param imageData
     * 		The image data. If this is null, a buffered image must be provided.
     * @param bufferedImage
     * 		An image constructed from the imageData. If this is null, imageData must
     * 		be provided.
     */
    @java.lang.Override
    public void setImage(byte[] imageData, java.awt.image.BufferedImage bufferedImage) {
        willChange();
        this.imageData = imageData;
        this.bufferedImage = bufferedImage;
        changed();
    }

    /**
     * Sets the image data. This clears the buffered image.
     *
     * <p>Note: For performance reasons this method stores a reference to the imageData array instead
     * of cloning it. Do not modify the image data array after invoking this method.
     */
    public void setImageData(byte[] imageData) {
        willChange();
        this.imageData = imageData;
        this.bufferedImage = null;
        changed();
    }

    /**
     * Sets the buffered image. This clears the image data.
     */
    @java.lang.Override
    public void setBufferedImage(java.awt.image.BufferedImage image) {
        willChange();
        this.imageData = null;
        this.bufferedImage = image;
        changed();
    }

    /**
     * Gets the buffered image. If necessary, this method creates the buffered image from the image
     * data.
     */
    @java.lang.Override
    public java.awt.image.BufferedImage getBufferedImage() {
        if ((bufferedImage == null) && (imageData != null)) {
            try {
                bufferedImage = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(imageData));
            } catch (java.io.IOException e) {
                e.printStackTrace();
                // If we can't create a buffered image from the image data,
                // there is no use to keep the image data and try again, so
                // we drop the image data.
                imageData = null;
            }
        }
        return bufferedImage;
    }

    /**
     * Gets the image data. If necessary, this method creates the image data from the buffered image.
     *
     * <p>Note: For performance reasons this method returns a reference to the internally used image
     * data array instead of cloning it. Do not modify this array.
     */
    @java.lang.Override
    public byte[] getImageData() {
        if ((bufferedImage != null) && (imageData == null)) {
            try {
                java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(bufferedImage, "PNG", bout);
                bout.close();
                imageData = bout.toByteArray();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                // If we can't create image data from the buffered image,
                // there is no use to keep the buffered image and try again, so
                // we drop the buffered image.
                bufferedImage = null;
            }
        }
        return imageData;
    }

    @java.lang.Override
    public void loadImage(java.io.File file) throws java.io.IOException {
        try (java.io.InputStream in = new java.io.FileInputStream(file)) {
            loadImage(in);
        } catch (java.lang.Throwable t) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            java.io.IOException e = new java.io.IOException(labels.getFormatted("file.failedToLoadImage.message", file.getName()));
            e.initCause(t);
            throw e;
        }
    }

    @java.lang.Override
    public void loadImage(java.io.InputStream in) throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int bytesRead;
        while ((bytesRead = in.read(buf)) > 0) {
            baos.write(buf, 0, bytesRead);
        } 
        java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(baos.toByteArray()));
        if (img == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            throw new java.io.IOException(labels.getFormatted("file.failedToLoadImage.message", in.toString()));
        }
        imageData = baos.toByteArray();
        bufferedImage = img;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        // The call to getImageData() ensures that we have serializable data
        // in the imageData array.
        getImageData();
        out.defaultWriteObject();
    }
}