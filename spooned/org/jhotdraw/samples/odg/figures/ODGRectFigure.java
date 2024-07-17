/* @(#)ODGRect.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * ODGRect.
 */
public class ODGRectFigure extends org.jhotdraw.samples.odg.figures.ODGAttributedFigure implements org.jhotdraw.samples.odg.figures.ODGFigure {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.RoundRectangle2D.Double roundrect;

    /**
     * This is used to perform faster drawing.
     */
    private transient java.awt.Shape cachedTransformedShape;

    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;

    public ODGRectFigure() {
        this(0, 0, 0, 0);
    }

    public ODGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, 0, 0);
    }

    public ODGRectFigure(double x, double y, double width, double height, double rx, double ry) {
        roundrect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, rx, ry);
        org.jhotdraw.samples.odg.ODGAttributeKeys.setDefaults(this);
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
        if ((getArcHeight() == 0.0) && (getArcWidth() == 0.0)) {
            g.draw(roundrect.getBounds2D());
        } else {
            g.draw(roundrect);
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

    public double getArcWidth() {
        return roundrect.arcwidth / 2.0;
    }

    public double getArcHeight() {
        return roundrect.archeight / 2.0;
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
            double g = org.jhotdraw.samples.odg.ODGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2;
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
            cachedHitShape = new org.jhotdraw.geom.GrowStroke(((float) (org.jhotdraw.samples.odg.ODGAttributeKeys.getStrokeTotalWidth(this, 1.0))) / 2.0F, ((float) (org.jhotdraw.samples.odg.ODGAttributeKeys.getStrokeTotalMiterLimit(this, 1.0)))).createStrokedShape(getTransformedShape());
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
            if ((attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.odg.Gradient g = org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT, g);
            }
            if ((attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.odg.Gradient g = org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT, g);
            }
        }
    }

    // ATTRIBUTES
    public void setArc(double w, double h) {
        roundrect.arcwidth = java.lang.Math.max(0.0, java.lang.Math.min(roundrect.width, w * 2.0));
        roundrect.archeight = java.lang.Math.max(0.0, java.lang.Math.min(roundrect.height, h * 2.0));
    }

    public void setArc(org.jhotdraw.geom.Dimension2DDouble arc) {
        roundrect.arcwidth = java.lang.Math.max(0.0, java.lang.Math.min(roundrect.width, arc.width * 2.0));
        roundrect.archeight = java.lang.Math.max(0.0, java.lang.Math.min(roundrect.height, arc.height * 2.0));
    }

    public org.jhotdraw.geom.Dimension2DDouble getArc() {
        return new org.jhotdraw.geom.Dimension2DDouble(roundrect.arcwidth / 2.0, roundrect.archeight / 2.0);
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        invalidateTransformedShape();
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        roundrect = ((java.awt.geom.RoundRectangle2D.Double) (((java.awt.geom.RoundRectangle2D.Double) (restoreData[0])).clone()));
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.odg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.odg.Gradient) (restoreData[3])));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return new java.lang.Object[]{ roundrect.clone(), org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    // EDITING
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case 0 :
                org.jhotdraw.draw.handle.ResizeHandleKit.addResizeHandles(this, handles);
                handles.add(new org.jhotdraw.samples.odg.figures.ODGRectRadiusHandle(this));
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
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return null;// ODG does not support connectors

    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        return null;// ODG does not support connectors

    }

    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.samples.odg.figures.ODGRectFigure clone() {
        org.jhotdraw.samples.odg.figures.ODGRectFigure that = ((org.jhotdraw.samples.odg.figures.ODGRectFigure) (super.clone()));
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