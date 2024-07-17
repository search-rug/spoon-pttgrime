/* @(#)SVGEllipse.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGEllipse represents a SVG ellipse and a SVG circle element.
 */
public class SVGEllipseFigure extends org.jhotdraw.samples.svg.figures.SVGAttributedFigure implements org.jhotdraw.samples.svg.figures.SVGFigure {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Ellipse2D.Double ellipse;

    /**
     * This is used to perform faster drawing and hit testing.
     */
    private transient java.awt.Shape cachedTransformedShape;

    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;

    public SVGEllipseFigure() {
        this(0, 0, 0, 0);
    }

    public SVGEllipseFigure(double x, double y, double width, double height) {
        ellipse = new java.awt.geom.Ellipse2D.Double(x, y, width, height);
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        if ((ellipse.width > 0) && (ellipse.height > 0)) {
            g.fill(ellipse);
        }
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        if ((ellipse.width > 0) && (ellipse.height > 0)) {
            g.draw(ellipse);
        }
    }

    // SHAPE AND BOUNDS
    public double getX() {
        return ellipse.x;
    }

    public double getY() {
        return ellipse.y;
    }

    public double getWidth() {
        return ellipse.getWidth();
    }

    public double getHeight() {
        return ellipse.getHeight();
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (ellipse.getBounds2D()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D rx = getTransformedShape().getBounds2D();
        java.awt.geom.Rectangle2D.Double r = (rx instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rx)) : new java.awt.geom.Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
            double g = (org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2.0) + 1;
            org.jhotdraw.geom.Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2.0;
            width *= java.lang.Math.max(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).getScaleX(), attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).getScaleY()) + 1;
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

    private java.awt.Shape getTransformedShape() {
        if (cachedTransformedShape == null) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                cachedTransformedShape = ellipse;
            } else {
                cachedTransformedShape = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(ellipse);
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

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        ellipse.x = java.lang.Math.min(anchor.x, lead.x);
        ellipse.y = java.lang.Math.min(anchor.y, lead.y);
        ellipse.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        ellipse.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
        invalidate();
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		the transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || ((tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION) != tx.getType())) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, tx);
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
        invalidate();
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        ellipse = ((java.awt.geom.Ellipse2D.Double) (((java.awt.geom.Ellipse2D.Double) (restoreData[0])).clone()));
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[3])));
        invalidate();
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return new java.lang.Object[]{ ellipse.clone(), org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    // ATTRIBUTES
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

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGEllipseFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGEllipseFigure that = ((org.jhotdraw.samples.svg.figures.SVGEllipseFigure) (super.clone()));
        that.ellipse = ((java.awt.geom.Ellipse2D.Double) (this.ellipse.clone()));
        that.cachedTransformedShape = null;
        return that;
    }

    // EVENT HANDLING
    @java.lang.Override
    public boolean isEmpty() {
        java.awt.geom.Rectangle2D.Double b = getBounds();
        return (b.width <= 0) || (b.height <= 0);
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedTransformedShape = null;
        cachedHitShape = null;
    }
}