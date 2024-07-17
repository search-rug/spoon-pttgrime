/* @(#)TextFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@code TextHolderFigure} which holds a single line of text.
 *
 * <p>A DrawingEditor should provide the {@link org.jhotdraw.draw.tool.TextCreationTool} to create a
 * {@code TextFigure}.
 */
public class TextFigure extends org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure implements org.jhotdraw.draw.figure.TextHolderFigure , org.jhotdraw.draw.figure.Origin , org.jhotdraw.draw.figure.Rotation {
    private static final long serialVersionUID = 1L;

    protected java.awt.geom.Point2D.Double origin = new java.awt.geom.Point2D.Double();

    public static final java.awt.geom.Point2D.Double HOIZONTAL_DIRECTION = new java.awt.geom.Point2D.Double(1, 0);

    // always starting from 0,0
    protected java.awt.geom.Point2D.Double direction = new java.awt.geom.Point2D.Double(1, 0);

    protected boolean editable = true;

    // cache of the TextFigure's layout
    protected transient java.awt.font.TextLayout textLayout;

    protected double alignX;

    protected double alignY;

    public TextFigure() {
        this(org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("TextFigure.defaultText"));
    }

    public TextFigure(java.lang.String text) {
        setText(text);
    }

    // DRAWING
    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawText(java.awt.Graphics2D g) {
        if ((getText() != null) || isEditable()) {
            java.awt.font.TextLayout layout = getTextLayout(org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
            java.awt.Graphics2D g2 = ((java.awt.Graphics2D) (g.create()));
            try {
                double alignDeltaX = layout.getAdvance() * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_X);
                double alignDeltaY = (layout.getAscent() + layout.getDescent()) * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_Y);
                // g2.draw(getBounds(AttributeKeys.getScaleFactorFromGraphics(g)));
                // Test if world to screen transformation mirrors the text. If so it tries to
                // unmirror it.
                if ((g2.getTransform().getScaleY() * g2.getTransform().getScaleX()) < 0) {
                    java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
                    at.translate(0, origin.y + (layout.getAscent() / 2));
                    at.scale(1, -1);
                    at.translate(0, (-origin.y) - (layout.getAscent() / 2));
                    at.rotate(direction.x, -direction.y, origin.x, origin.y + layout.getAscent());
                    g2.transform(at);
                } else {
                    g2.transform(rotationMatrix());
                }
                // to avoid float imprecisions
                java.awt.geom.AffineTransform at2 = new java.awt.geom.AffineTransform();
                at2.translate(origin.x, origin.y);
                g2.transform(at2);
                layout.draw(g2, ((float) (-alignDeltaX)), ((float) ((+alignDeltaY) + layout.getAscent())));
            } finally {
                g2.dispose();
            }
        }
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double dirVector = new java.awt.geom.Point2D.Double(origin.x + direction.x, origin.y + direction.y);
        tx.transform(origin, origin);
        tx.transform(dirVector, dirVector);
        direction.x = dirVector.x - origin.x;
        direction.y = dirVector.y - origin.y;
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        origin = new java.awt.geom.Point2D.Double(anchor.x, anchor.y);
    }

    @java.lang.Override
    public void setRotation(double angle) {
        java.awt.geom.AffineTransform.getRotateInstance(angle).transform(org.jhotdraw.draw.figure.TextFigure.HOIZONTAL_DIRECTION, direction);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getOrigin() {
        return origin;
    }

    @java.lang.Override
    public void setOrigin(java.awt.geom.Point2D.Double origin) {
        this.origin = origin;
    }

    public java.awt.geom.Point2D.Double getDirection() {
        return direction;
    }

    public void setDirection(java.awt.geom.Point2D.Double direction) {
        this.direction = direction;
    }

    @java.lang.Override
    public boolean figureContains(java.awt.geom.Point2D.Double p, double scale) {
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(this, scale)) + 1.0;
        java.awt.geom.Rectangle2D.Double r = getBounds(scale);
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r.contains(p);
    }

    protected java.awt.font.TextLayout getTextLayout(double sizeFactor) {
        if ((textLayout == null) || attr().get(org.jhotdraw.draw.AttributeKeys.IS_STROKE_PIXEL_VALUE)) {
            java.lang.String text = getText();
            if ((text == null) || (text.length() == 0)) {
                text = " ";
            }
            java.awt.font.FontRenderContext frc = getFontRenderContext();
            java.util.HashMap<java.awt.font.TextAttribute, java.lang.Object> textAttributes = new java.util.HashMap<>();
            textAttributes.put(java.awt.font.TextAttribute.FONT, getFont().deriveFont(getFontSize() / ((float) (org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(this, sizeFactor)))));
            if (attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE)) {
                textAttributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            }
            textLayout = new java.awt.font.TextLayout(text, textAttributes, frc);
        }
        return textLayout;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.font.TextLayout layout = getTextLayout(scale);
        double alignDeltaX = layout.getAdvance() * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_X);
        double alignDeltaY = (layout.getAscent() + layout.getDescent()) * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_Y);
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(origin.x - alignDeltaX, origin.y - alignDeltaY, layout.getAdvance(), layout.getAscent() + layout.getDescent());
        r = ((java.awt.geom.Rectangle2D.Double) (rotationMatrix().createTransformedShape(r).getBounds2D()));
        return r;
    }

    @java.lang.Override
    public org.jhotdraw.geom.Dimension2DDouble getPreferredSize(double scale) {
        java.awt.geom.Rectangle2D.Double b = getBounds(scale);
        return new org.jhotdraw.geom.Dimension2DDouble(b.width, b.height);
    }

    @java.lang.Override
    public double getBaseline() {
        java.awt.font.TextLayout layout = getTextLayout(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
        return (origin.y + layout.getAscent()) - getBounds().y;
    }

    private java.awt.geom.AffineTransform rotationMatrix() {
        return java.awt.geom.AffineTransform.getRotateInstance(direction.x, direction.y, origin.x, origin.y);
    }

    /**
     * Gets the drawing area without taking the decorator into account.
     */
    @java.lang.Override
    protected java.awt.geom.Rectangle2D.Double getFigureDrawingArea(double factor) {
        if (getText() == null) {
            return getBounds(factor);
        } else {
            java.awt.font.TextLayout layout = getTextLayout(factor);
            double alignDeltaX = layout.getAdvance() * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_X);
            double alignDeltaY = (layout.getAscent() + layout.getDescent()) * attr().get(org.jhotdraw.draw.AttributeKeys.ALIGN_RELATIVE_Y);
            java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(origin.x - alignDeltaX, origin.y - alignDeltaY, layout.getAdvance(), layout.getAscent() + layout.getDescent());
            java.awt.geom.Rectangle2D lBounds = layout.getBounds();
            if ((!lBounds.isEmpty()) && (!java.lang.Double.isNaN(lBounds.getX()))) {
                r.add(new java.awt.geom.Rectangle2D.Double((lBounds.getX() + origin.x) - alignDeltaX, ((lBounds.getY() + origin.y) - alignDeltaY) + layout.getAscent(), lBounds.getWidth(), lBounds.getHeight()));
            }
            r = ((java.awt.geom.Rectangle2D.Double) (rotationMatrix().createTransformedShape(r).getBounds2D()));
            // grow by two pixels to take antialiasing into account
            org.jhotdraw.geom.Geom.grow(r, 2.0, 2.0);
            return r;
        }
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.Point2D.Double p = ((java.awt.geom.Point2D.Double) (geometry));
        origin.x = p.x;
        origin.y = p.y;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return origin.clone();
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
     * Sets the text shown by the text figure. This is a convenience method for calling {@code set(TEXT,newText)}.
     */
    @java.lang.Override
    public void setText(java.lang.String newText) {
        attr().set(org.jhotdraw.draw.AttributeKeys.TEXT, newText);
    }

    @java.lang.Override
    public int getTextColumns() {
        // return (getText() == null) ? 4 : Math.max(getText().length(), 4);
        return 4;
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
    public java.awt.Font getFont() {
        return org.jhotdraw.draw.AttributeKeys.getFont(this);
    }

    @java.lang.Override
    public java.awt.Color getTextColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR);
    }

    @java.lang.Override
    public java.awt.Color getFillColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
    }

    @java.lang.Override
    public void setFontSize(float size) {
        attr().set(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, java.lang.Double.valueOf(size));
    }

    @java.lang.Override
    public float getFontSize() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE).floatValue();
    }

    // EDITING
    @java.lang.Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean b) {
        this.editable = b;
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.Collection<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        switch (detailLevel) {
            case -1 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, false, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northEast()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southEast()));
                handles.add(new org.jhotdraw.draw.handle.FontSizeHandle(this));
                break;
            case 1 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this));
                handles.add(new org.jhotdraw.draw.handle.RotateHandle(this) {
                    @java.lang.Override
                    protected java.awt.geom.Point2D.Double getCenter() {
                        return TextFigure.this.getOrigin();
                    }
                });
                break;
        }
        return handles;
    }

    /**
     * Returns a specialized tool for the given coordinate.
     *
     * <p>Returns null, if no specialized tool is available.
     *
     * @param p
     * @return  */
    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool(java.awt.geom.Point2D.Double p) {
        if (isEditable() && contains(p)) {
            org.jhotdraw.draw.tool.TextEditingTool t = new org.jhotdraw.draw.tool.TextEditingTool(this);
            return t;
        }
        return null;
    }

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        textLayout = null;
    }

    @java.lang.Override
    protected void validate() {
        super.validate();
        textLayout = null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextFigure clone() {
        org.jhotdraw.draw.figure.TextFigure that = ((org.jhotdraw.draw.figure.TextFigure) (super.clone()));
        that.origin = ((java.awt.geom.Point2D.Double) (this.origin.clone()));
        that.direction = ((java.awt.geom.Point2D.Double) (this.direction.clone()));
        that.textLayout = null;
        return that;
    }

    @java.lang.Override
    public boolean isTextOverflow() {
        return false;
    }
}