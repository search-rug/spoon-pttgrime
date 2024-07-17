/* @(#)SVGImage.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGImage.
 */
public class SVGImageFigure extends org.jhotdraw.samples.svg.figures.SVGAttributedFigure implements org.jhotdraw.samples.svg.figures.SVGFigure , org.jhotdraw.draw.figure.ImageHolderFigure {
    private static final long serialVersionUID = 1L;

    /**
     * This rectangle describes the bounds into which we draw the image.
     */
    private java.awt.geom.Rectangle2D.Double rectangle;

    /**
     * This is used to perform faster drawing.
     */
    private transient java.awt.Shape cachedTransformedShape;

    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;

    /**
     * The image data. This can be null, if the image was created from a BufferedImage.
     */
    private byte[] imageData;

    /**
     * The buffered image. This can be null, if we haven't yet parsed the imageData.
     */
    private java.awt.image.BufferedImage bufferedImage;

    public SVGImageFigure() {
        this(0, 0, 0, 0);
    }

    public SVGImageFigure(double x, double y, double width, double height) {
        rectangle = new java.awt.geom.Rectangle2D.Double(x, y, width, height);
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        // super.draw(g);
        double opacity = attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY);
        opacity = java.lang.Math.min(java.lang.Math.max(0.0, opacity), 1.0);
        if (opacity != 0.0) {
            java.awt.Composite savedComposite = g.getComposite();
            if (opacity != 1.0) {
                g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, ((float) (opacity))));
            }
            java.awt.image.BufferedImage image = getBufferedImage();
            if (image != null) {
                if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                    // FIXME - We should cache the transformed image.
                    // Drawing a transformed image appears to be very slow.
                    java.awt.Graphics2D gx = ((java.awt.Graphics2D) (g.create()));
                    // Use same rendering hints like parent graphics
                    gx.setRenderingHints(g.getRenderingHints());
                    gx.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
                    gx.drawImage(image, ((int) (rectangle.x)), ((int) (rectangle.y)), ((int) (rectangle.width)), ((int) (rectangle.height)), null);
                    gx.dispose();
                } else {
                    g.drawImage(image, ((int) (rectangle.x)), ((int) (rectangle.y)), ((int) (rectangle.width)), ((int) (rectangle.height)), null);
                }
            } else {
                java.awt.Shape shape = getTransformedShape();
                g.setColor(java.awt.Color.red);
                g.setStroke(new java.awt.BasicStroke());
                g.draw(shape);
            }
            if (opacity != 1.0) {
                g.setComposite(savedComposite);
            }
        }
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
    }

    // SHAPE AND BOUNDS
    public double getX() {
        return rectangle.x;
    }

    public double getY() {
        return rectangle.y;
    }

    public double getWidth() {
        return rectangle.width;
    }

    public double getHeight() {
        return rectangle.height;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D rx = getTransformedShape().getBounds2D();
        java.awt.geom.Rectangle2D.Double r = (rx instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rx)) : new java.awt.geom.Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        return getHitShape().contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        invalidateTransformedShape();
        rectangle.x = java.lang.Math.min(anchor.x, lead.x);
        rectangle.y = java.lang.Math.min(anchor.y, lead.y);
        rectangle.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        rectangle.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
    }

    private void invalidateTransformedShape() {
        cachedTransformedShape = null;
        cachedHitShape = null;
    }

    private java.awt.Shape getTransformedShape() {
        if (cachedTransformedShape == null) {
            cachedTransformedShape = ((java.awt.Shape) (rectangle.clone()));
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                cachedTransformedShape = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

    private java.awt.Shape getHitShape() {
        if (cachedHitShape == null) {
            cachedHitShape = new org.jhotdraw.geom.GrowStroke(((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(this, 1.0))) / 2.0F, ((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalMiterLimit(this, 1.0)))).createStrokedShape(getTransformedShape());
        }
        return cachedHitShape;
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		The transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        invalidateTransformedShape();
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || ((tx.getType() & (java.awt.geom.AffineTransform.TYPE_TRANSLATION | java.awt.geom.AffineTransform.TYPE_MASK_SCALE)) != tx.getType())) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, ((java.awt.geom.AffineTransform) (tx.clone())));
            } else {
                java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, t);
            }
        } else {
            java.awt.geom.Point2D.Double anchor = getStartPoint();
            java.awt.geom.Point2D.Double lead = getEndPoint();
            setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
        }
    }

    // ATTRIBUTES
    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        invalidateTransformedShape();
        java.lang.Object[] o = ((java.lang.Object[]) (geometry));
        rectangle = ((java.awt.geom.Rectangle2D.Double) (((java.awt.geom.Rectangle2D.Double) (o[0])).clone()));
        if (o[1] == null) {
            attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, null);
        } else {
            attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, ((java.awt.geom.AffineTransform) (((java.awt.geom.AffineTransform) (o[1])).clone())));
        }
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return new java.lang.Object[]{ rectangle.clone(), attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) };
    }

    // EDITING
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, false, true));
                break;
            case 0 :
                org.jhotdraw.draw.handle.ResizeHandleKit.addResizeHandles(this, handles);
                handles.add(new org.jhotdraw.samples.svg.figures.LinkHandle(this));
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                break;
            default :
                break;
        }
        return handles;
    }

    @java.lang.Override
    public java.util.Collection<javax.swing.Action> getActions(java.awt.geom.Point2D.Double p) {
        final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        java.util.LinkedList<javax.swing.Action> actions = new java.util.LinkedList<javax.swing.Action>();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.TRANSFORM.setUndoable(SVGImageFigure.this, null));
                    changed();
                }
            });
        }
        if (bufferedImage != null) {
            if ((rectangle.width != bufferedImage.getWidth()) || (rectangle.height != bufferedImage.getHeight())) {
                actions.add(new javax.swing.AbstractAction(labels.getString("edit.setToImageSize.text")) {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        java.lang.Object geometry = getTransformRestoreData();
                        willChange();
                        rectangle = new java.awt.geom.Rectangle2D.Double(rectangle.x - ((bufferedImage.getWidth() - rectangle.width) / 2.0), rectangle.y - ((bufferedImage.getHeight() - rectangle.height) / 2.0), bufferedImage.getWidth(), bufferedImage.getHeight());
                        fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(SVGImageFigure.this, geometry, getTransformRestoreData()));
                        changed();
                    }
                });
            }
            double imageRatio = bufferedImage.getHeight() / ((double) (bufferedImage.getWidth()));
            double figureRatio = rectangle.height / rectangle.width;
            if (java.lang.Math.abs(imageRatio - figureRatio) > 0.001) {
                actions.add(new javax.swing.AbstractAction(labels.getString("edit.adjustHeightToImageAspect.text")) {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        java.lang.Object geometry = getTransformRestoreData();
                        willChange();
                        double newHeight = (bufferedImage.getHeight() * rectangle.width) / bufferedImage.getWidth();
                        rectangle = new java.awt.geom.Rectangle2D.Double(rectangle.x, rectangle.y - ((newHeight - rectangle.height) / 2.0), rectangle.width, newHeight);
                        fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(SVGImageFigure.this, geometry, getTransformRestoreData()));
                        changed();
                    }
                });
                actions.add(new javax.swing.AbstractAction(labels.getString("edit.adjustWidthToImageAspect.text")) {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        java.lang.Object geometry = getTransformRestoreData();
                        willChange();
                        double newWidth = (bufferedImage.getWidth() * rectangle.height) / bufferedImage.getHeight();
                        rectangle = new java.awt.geom.Rectangle2D.Double(rectangle.x - ((newWidth - rectangle.width) / 2.0), rectangle.y, newWidth, rectangle.height);
                        fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(SVGImageFigure.this, geometry, getTransformRestoreData()));
                        changed();
                    }
                });
            }
        }
        return actions;
    }

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGImageFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGImageFigure that = ((org.jhotdraw.samples.svg.figures.SVGImageFigure) (super.clone()));
        that.rectangle = ((java.awt.geom.Rectangle2D.Double) (this.rectangle.clone()));
        that.cachedTransformedShape = null;
        that.cachedHitShape = null;
        return that;
    }

    @java.lang.Override
    public boolean isEmpty() {
        java.awt.geom.Rectangle2D.Double b = getBounds();
        return ((b.width <= 0) || (b.height <= 0)) || ((imageData == null) && (bufferedImage == null));
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        invalidateTransformedShape();
    }

    /**
     * Sets the image.
     *
     * <p>Note: For performance reasons this method stores a reference to the imageData array instead
     * of cloning it. Do not modify the imageData array after invoking this method.
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
     * of cloning it. Do not modify the imageData array after invoking this method.
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
            // System.out.println("recreateing bufferedImage");
            try {
                bufferedImage = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(imageData));
            } catch (java.lang.Throwable e) {
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
        java.io.InputStream in = new java.io.FileInputStream(file);
        try {
            loadImage(in);
        } catch (java.lang.Throwable t) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            java.io.IOException e = new java.io.IOException(labels.getFormatted("file.failedToLoadImage.message", file.getName()));
            e.initCause(t);
            throw e;
        } finally {
            in.close();
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
        java.awt.image.BufferedImage img;
        try {
            img = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(baos.toByteArray()));
        } catch (java.lang.Throwable t) {
            img = null;
        }
        if (img == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            throw new java.io.IOException(labels.getFormatted("file.failedToLoadImage.message", in.toString()));
        }
        imageData = baos.toByteArray();
        bufferedImage = img;
    }
}