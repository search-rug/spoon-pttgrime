/* @(#)SVGText.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGText.
 *
 * <p>XXX - At least on Mac OS X - Always draw text using TextLayout.getOutline(), because outline
 * layout does not match with TextLayout.draw() output. Cache outline to improve performance. <br>
 * 2.1 2007-05-13 Fixed transformation issues. <br>
 * 2.0 2007-04-14 Adapted for new AttributeKeys.TRANSFORM support. <br>
 * 1.0 July 8, 2006 Created.
 */
public class SVGTextFigure extends org.jhotdraw.samples.svg.figures.SVGAttributedFigure implements org.jhotdraw.draw.figure.TextHolderFigure , org.jhotdraw.samples.svg.figures.SVGFigure {
    private static final long serialVersionUID = 1L;

    protected java.awt.geom.Point2D.Double[] coordinates = new java.awt.geom.Point2D.Double[]{ new java.awt.geom.Point2D.Double() };

    protected double[] rotates = new double[]{ 0 };

    private boolean editable = true;

    /**
     * This is used to perform faster drawing and hit testing.
     */
    private transient java.awt.Shape cachedTextShape;

    private transient java.awt.geom.Rectangle2D.Double cachedBounds;

    private transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    public SVGTextFigure() {
        this("Text");
    }

    public SVGTextFigure(java.lang.String text) {
        setText(text);
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @java.lang.Override
    protected void drawText(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        g.fill(getTextShape());
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        g.draw(getTextShape());
    }

    // SHAPE AND BOUNDS
    public void setCoordinates(java.awt.geom.Point2D.Double[] coordinates) {
        this.coordinates = coordinates.clone();
        invalidate();
    }

    public java.awt.geom.Point2D.Double[] getCoordinates() {
        java.awt.geom.Point2D.Double[] c = new java.awt.geom.Point2D.Double[coordinates.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = ((java.awt.geom.Point2D.Double) (coordinates[i].clone()));
        }
        return c;
    }

    public void setRotates(double[] rotates) {
        this.rotates = rotates.clone();
        invalidate();
    }

    public double[] getRotates() {
        return rotates.clone();
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        if (cachedBounds == null) {
            cachedBounds = new java.awt.geom.Rectangle2D.Double();
            cachedBounds.setRect(getTextShape().getBounds2D());
            java.lang.String text = getText();
            if ((text == null) || (text.length() == 0)) {
                text = " ";
            }
            java.awt.font.FontRenderContext frc = getFontRenderContext();
            java.util.HashMap<java.awt.font.TextAttribute, java.lang.Object> textAttributes = new java.util.HashMap<java.awt.font.TextAttribute, java.lang.Object>();
            textAttributes.put(java.awt.font.TextAttribute.FONT, getFont());
            if (attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE)) {
                textAttributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON);
            }
            java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(text, textAttributes, frc);
            cachedBounds.setRect(coordinates[0].x, coordinates[0].y - textLayout.getAscent(), textLayout.getAdvance(), textLayout.getAscent());
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(coordinates[0].x, coordinates[0].y);
            switch (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ANCHOR)) {
                case END :
                    cachedBounds.x -= textLayout.getAdvance();
                    break;
                case MIDDLE :
                    cachedBounds.x -= textLayout.getAdvance() / 2.0;
                    break;
                case START :
                    break;
            }
            tx.rotate(rotates[0]);
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedBounds.clone()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            java.awt.geom.Rectangle2D rx = getTextShape().getBounds2D();
            java.awt.geom.Rectangle2D.Double r = (rx instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rx)) : new java.awt.geom.Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
            double g = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) + 1;
            org.jhotdraw.geom.Geom.grow(r, g, g);
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                cachedDrawingArea = r;
            } else {
                cachedDrawingArea = new java.awt.geom.Rectangle2D.Double();
                cachedDrawingArea.setRect(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(r).getBounds2D());
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                p = ((java.awt.geom.Point2D.Double) (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, new java.awt.geom.Point2D.Double())));
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        return getTextShape().getBounds2D().contains(p);
    }

    private java.awt.Shape getTextShape() {
        if (cachedTextShape == null) {
            java.lang.String text = getText();
            if ((text == null) || (text.length() == 0)) {
                text = " ";
            }
            java.awt.font.FontRenderContext frc = getFontRenderContext();
            java.util.HashMap<java.awt.font.TextAttribute, java.lang.Object> textAttributes = new java.util.HashMap<java.awt.font.TextAttribute, java.lang.Object>();
            textAttributes.put(java.awt.font.TextAttribute.FONT, getFont());
            if (attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE)) {
                textAttributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON);
            }
            java.awt.font.TextLayout textLayout = new java.awt.font.TextLayout(text, textAttributes, frc);
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(coordinates[0].x, coordinates[0].y);
            switch (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ANCHOR)) {
                case END :
                    tx.translate(-textLayout.getAdvance(), 0);
                    break;
                case MIDDLE :
                    tx.translate((-textLayout.getAdvance()) / 2.0, 0);
                    break;
                case START :
                    break;
            }
            tx.rotate(rotates[0]);
            /* if (get(TRANSFORM) != null) {
            tx.preConcatenate(get(TRANSFORM));
            }
             */
            cachedTextShape = tx.createTransformedShape(textLayout.getOutline(tx));
            cachedTextShape = textLayout.getOutline(tx);
        }
        return cachedTextShape;
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        coordinates = new java.awt.geom.Point2D.Double[]{ new java.awt.geom.Point2D.Double(anchor.x, anchor.y) };
        rotates = new double[]{ 0.0 };
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		the transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || (tx.getType() != (tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION))) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, ((java.awt.geom.AffineTransform) (tx.clone())));
            } else {
                java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, t);
            }
        } else {
            for (int i = 0; i < coordinates.length; i++) {
                tx.transform(coordinates[i], coordinates[i]);
            }
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT, g);
            }
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT, g);
            }
        }
        invalidate();
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[0])));
        java.awt.geom.Point2D.Double[] restoredCoordinates = ((java.awt.geom.Point2D.Double[]) (restoreData[1]));
        for (int i = 0; i < this.coordinates.length; i++) {
            coordinates[i] = ((java.awt.geom.Point2D.Double) (restoredCoordinates[i].clone()));
        }
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[3])));
        invalidate();
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        java.awt.geom.Point2D.Double[] restoredCoordinates = this.coordinates.clone();
        for (int i = 0; i < this.coordinates.length; i++) {
            restoredCoordinates[i] = ((java.awt.geom.Point2D.Double) (this.coordinates[i].clone()));
        }
        return new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), restoredCoordinates, org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    // ATTRIBUTES
    /**
     * Gets the text shown by the text figure.
     */
    @java.lang.Override
    public java.lang.String getText() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.TEXT);
    }

    /**
     * Sets the text shown by the text figure.
     */
    @java.lang.Override
    public void setText(java.lang.String newText) {
        attr().set(org.jhotdraw.draw.AttributeKeys.TEXT, newText);
    }

    @java.lang.Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean b) {
        this.editable = b;
    }

    @java.lang.Override
    public int getTextColumns() {
        // return (getText() == null) ? 4 : Math.min(getText().length(), 4);
        return 4;
    }

    @java.lang.Override
    public java.awt.Font getFont() {
        return org.jhotdraw.samples.svg.SVGAttributeKeys.getFont(this);
    }

    @java.lang.Override
    public java.awt.Color getTextColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
        // return get(TEXT_COLOR);
    }

    @java.lang.Override
    public java.awt.Color getFillColor() {
        return (attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) == null) || attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR).equals(java.awt.Color.white) ? java.awt.Color.black : java.awt.Color.WHITE;
        // return get(FILL_COLOR);
    }

    @java.lang.Override
    public void setFontSize(float size) {
        // put(FONT_SIZE,  new Double(size));
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(0, size);
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            try {
                tx.inverseTransform(p, p);
                java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(0, 0);
                tx.inverseTransform(p0, p0);
                p.y -= p0.y;
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        attr().set(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, java.lang.Math.abs(p.y));
    }

    @java.lang.Override
    public float getFontSize() {
        // return get(FONT_SIZE).floatValue();
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(0, attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE));
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            tx.transform(p, p);
            java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(0, 0);
            tx.transform(p0, p0);
            p.y -= p0.y;
            /* try {
            tx.inverseTransform(p, p);
            } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
            }
             */
        }
        return ((float) (java.lang.Math.abs(p.y)));
    }

    // EDITING
    // CONNECTING
    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedTextShape = null;
        cachedBounds = null;
        cachedDrawingArea = null;
    }

    @java.lang.Override
    public org.jhotdraw.geom.Dimension2DDouble getPreferredSize(double scale) {
        java.awt.geom.Rectangle2D.Double b = getBounds(scale);
        return new org.jhotdraw.geom.Dimension2DDouble(b.width, b.height);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, false, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northEast()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southEast()));
                handles.add(new org.jhotdraw.draw.handle.FontSizeHandle(this));
                handles.add(new org.jhotdraw.samples.svg.figures.LinkHandle(this));
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                break;
        }
        return handles;
    }

    // EDITING
    /**
     * Returns a specialized tool for the given coordinate.
     *
     * <p>Returns null, if no specialized tool is available.
     */
    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool(java.awt.geom.Point2D.Double p) {
        if (isEditable() && contains(p)) {
            org.jhotdraw.draw.tool.TextEditingTool tool = new org.jhotdraw.draw.tool.TextEditingTool(this);
            return tool;
        }
        return null;
    }

    @java.lang.Override
    public double getBaseline() {
        return coordinates[0].y - getBounds().y;
    }

    /**
     * Gets the number of characters used to expand tabs.
     */
    @java.lang.Override
    public int getTabSize() {
        return 8;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextHolderFigure getLabelFor() {
        return this;
    }

    @java.lang.Override
    public org.jhotdraw.geom.Insets2D.Double getInsets() {
        return new org.jhotdraw.geom.Insets2D.Double();
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGTextFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGTextFigure that = ((org.jhotdraw.samples.svg.figures.SVGTextFigure) (super.clone()));
        that.coordinates = new java.awt.geom.Point2D.Double[this.coordinates.length];
        for (int i = 0; i < this.coordinates.length; i++) {
            that.coordinates[i] = ((java.awt.geom.Point2D.Double) (this.coordinates[i].clone()));
        }
        that.rotates = this.rotates.clone();
        that.cachedBounds = null;
        that.cachedDrawingArea = null;
        that.cachedTextShape = null;
        return that;
    }

    @java.lang.Override
    public boolean isEmpty() {
        return (getText() == null) || (getText().length() == 0);
    }

    @java.lang.Override
    public boolean isTextOverflow() {
        return false;
    }
}