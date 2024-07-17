/* @(#)SVGRect.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGRect.
 */
public class SVGRectFigure extends org.jhotdraw.samples.svg.figures.SVGAttributedFigure implements org.jhotdraw.samples.svg.figures.SVGFigure {
    private static final long serialVersionUID = 1L;

    /**
     * Identifies the {@code arcWidth} JavaBeans property.
     */
    public static final java.lang.String ARC_WIDTH_PROPERTY = "arcWidth";

    /**
     * Identifies the {@code arcHeight} JavaBeans property.
     */
    public static final java.lang.String ARC_HEIGHT_PROPERTY = "arcHeight";

    /**
     * The variable acv is used for generating the locations of the control points for the rounded
     * rectangle using path.curveTo.
     */
    private static final double ACV;

    static {
        double angle = java.lang.Math.PI / 4.0;
        double a = 1.0 - java.lang.Math.cos(angle);
        double b = java.lang.Math.tan(angle);
        double c = (java.lang.Math.sqrt(1.0 + (b * b)) - 1) + a;
        double cv = (((4.0 / 3.0) * a) * b) / c;
        ACV = 1.0 - cv;
    }

    /**
     */
    private java.awt.geom.RoundRectangle2D.Double roundrect;

    /**
     * This is used to perform faster drawing.
     */
    private transient java.awt.Shape cachedTransformedShape;

    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;

    public SVGRectFigure() {
        this(0, 0, 0, 0);
    }

    public SVGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, 0, 0);
    }

    public SVGRectFigure(double x, double y, double width, double height, double rx, double ry) {
        roundrect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, rx, ry);
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        if ((getArcHeight() == 0.0) && (getArcWidth() == 0.0)) {
            g.fill(roundrect.getBounds2D());
        } else {
            g.fill(roundrect);
        }
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        if ((roundrect.archeight == 0) && (roundrect.arcwidth == 0)) {
            g.draw(roundrect.getBounds2D());
        } else {
            // We have to generate the path for the round rectangle manually,
            // because the path of a Java RoundRectangle is drawn counter clockwise
            // whereas an SVG rect needs to be drawn clockwise.
            java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
            double aw = roundrect.arcwidth / 2.0;
            double ah = roundrect.archeight / 2.0;
            p.moveTo(roundrect.x + aw, ((float) (roundrect.y)));
            p.lineTo((roundrect.x + roundrect.width) - aw, ((float) (roundrect.y)));
            p.curveTo((roundrect.x + roundrect.width) - (aw * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), ((float) (roundrect.y)), roundrect.x + roundrect.width, ((float) (roundrect.y + (ah * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV))), roundrect.x + roundrect.width, roundrect.y + ah);
            p.lineTo(roundrect.x + roundrect.width, (roundrect.y + roundrect.height) - ah);
            p.curveTo(roundrect.x + roundrect.width, (roundrect.y + roundrect.height) - (ah * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), (roundrect.x + roundrect.width) - (aw * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), roundrect.y + roundrect.height, (roundrect.x + roundrect.width) - aw, roundrect.y + roundrect.height);
            p.lineTo(roundrect.x + aw, roundrect.y + roundrect.height);
            p.curveTo(roundrect.x + (aw * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), roundrect.y + roundrect.height, roundrect.x, (roundrect.y + roundrect.height) - (ah * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), ((float) (roundrect.x)), (roundrect.y + roundrect.height) - ah);
            p.lineTo(((float) (roundrect.x)), roundrect.y + ah);
            p.curveTo(roundrect.x, roundrect.y + (ah * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), roundrect.x + (aw * org.jhotdraw.samples.svg.figures.SVGRectFigure.ACV), ((float) (roundrect.y)), ((float) (roundrect.x + aw)), ((float) (roundrect.y)));
            p.closePath();
            g.draw(p);
        }
    }

    // SHAPE AND BOUNDS
    public double getX() {
        return roundrect.x;
    }

    public double getY() {
        return roundrect.y;
    }

    public double getWidth() {
        return roundrect.width;
    }

    public double getHeight() {
        return roundrect.height;
    }

    /**
     * Gets the arc width.
     */
    public double getArcWidth() {
        return roundrect.arcwidth;
    }

    /**
     * Gets the arc height.
     */
    public double getArcHeight() {
        return roundrect.archeight;
    }

    /**
     * Sets the arc width.
     */
    public void setArcWidth(double newValue) {
        double oldValue = roundrect.arcwidth;
        roundrect.arcwidth = newValue;
    }

    /**
     * Sets the arc height.
     */
    public void setArcHeight(double newValue) {
        double oldValue = roundrect.archeight;
        roundrect.archeight = newValue;
    }

    /**
     * Convenience method for setting both the arc width and the arc height.
     */
    public void setArc(double width, double height) {
        setArcWidth(width);
        setArcHeight(height);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (roundrect.getBounds2D()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D rx = getTransformedShape().getBounds2D();
        java.awt.geom.Rectangle2D.Double r = (rx instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rx)) : new java.awt.geom.Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
            double g = (org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2.0) + 1.0;
            org.jhotdraw.geom.Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2.0;
            if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                width *= attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP) != java.awt.BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            org.jhotdraw.geom.Geom.grow(r, width, width);
        }
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
        roundrect.x = java.lang.Math.min(anchor.x, lead.x);
        roundrect.y = java.lang.Math.min(anchor.y, lead.y);
        roundrect.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        roundrect.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
        invalidate();
    }

    private void invalidateTransformedShape() {
        cachedTransformedShape = null;
        cachedHitShape = null;
    }

    private java.awt.Shape getTransformedShape() {
        if (cachedTransformedShape == null) {
            if ((getArcHeight() == 0) || (getArcWidth() == 0)) {
                cachedTransformedShape = roundrect.getBounds2D();
            } else {
                cachedTransformedShape = ((java.awt.Shape) (roundrect.clone()));
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                cachedTransformedShape = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

    private java.awt.Shape getHitShape() {
        if (cachedHitShape == null) {
            if ((attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) != null) || (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null)) {
                cachedHitShape = new org.jhotdraw.geom.GrowStroke(((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(this, 1.0))) / 2.0F, ((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalMiterLimit(this, 1.0)))).createStrokedShape(getTransformedShape());
            } else {
                cachedHitShape = org.jhotdraw.samples.svg.SVGAttributeKeys.getHitStroke(this, 1.0).createStrokedShape(getTransformedShape());
            }
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
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || // (tx.getType() & (AffineTransform.TYPE_TRANSLATION |
        // AffineTransform.TYPE_MASK_SCALE)) != tx.getType()) {
        ((tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION) != tx.getType())) {
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
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        invalidateTransformedShape();
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        roundrect = ((java.awt.geom.RoundRectangle2D.Double) (((java.awt.geom.RoundRectangle2D.Double) (restoreData[0])).clone()));
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[3])));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return new java.lang.Object[]{ roundrect.clone(), org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this) };
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
                handles.add(new org.jhotdraw.samples.svg.figures.SVGRectRadiusHandle(this));
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

    // CLONING
    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGRectFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGRectFigure that = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (super.clone()));
        that.roundrect = ((java.awt.geom.RoundRectangle2D.Double) (this.roundrect.clone()));
        that.cachedTransformedShape = null;
        that.cachedHitShape = null;
        return that;
    }

    @java.lang.Override
    public boolean isEmpty() {
        java.awt.geom.Rectangle2D.Double b = getBounds();
        return (b.width <= 0) || (b.height <= 0);
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        invalidateTransformedShape();
    }
}