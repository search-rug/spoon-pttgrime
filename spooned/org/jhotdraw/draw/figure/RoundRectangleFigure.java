/* @(#)RoundRectangleFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
import org.jhotdraw.draw.handle.RoundRectangleRadiusHandle;
/**
 * A {@link Figure} with a rounded rectangular shape.
 *
 * <p>This figure has two JavaBeans properties {@code arcWidth} and {@code arcHeight} which specify
 * the corner radius.
 *
 * <p>This figure creates a {@link RoundRectangleRadiusHandle} which allows to interactively change
 * the corner radius.
 */
// EVENT HANDLING
public class RoundRectangleFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    /**
     * Identifies the {@code arcWidth} JavaBeans property.
     */
    public static final java.lang.String ARC_WIDTH_PROPERTY = "arcWidth";

    /**
     * Identifies the {@code arcHeight} JavaBeans property.
     */
    public static final java.lang.String ARC_HEIGHT_PROPERTY = "arcHeight";

    protected java.awt.geom.RoundRectangle2D.Double roundrect;

    public static final double DEFAULT_ARC = 20;

    public RoundRectangleFigure() {
        this(0, 0, 0, 0);
    }

    public RoundRectangleFigure(double x, double y, double width, double height) {
        roundrect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, org.jhotdraw.draw.figure.RoundRectangleFigure.DEFAULT_ARC, org.jhotdraw.draw.figure.RoundRectangleFigure.DEFAULT_ARC);
    }

    // DRAWING
    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        java.awt.geom.RoundRectangle2D.Double r = ((java.awt.geom.RoundRectangle2D.Double) (roundrect.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        if ((r.width > 0) && (r.height > 0)) {
            g.fill(r);
        }
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.RoundRectangle2D.Double r = ((java.awt.geom.RoundRectangle2D.Double) (roundrect.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        if ((r.width > 0) && (r.height > 0)) {
            g.draw(r);
        }
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (roundrect.getBounds2D()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (roundrect.getBounds2D()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale) + 1;
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r;
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

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        java.awt.geom.RoundRectangle2D.Double r = ((java.awt.geom.RoundRectangle2D.Double) (roundrect.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleDenominator);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        return r.contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        roundrect.x = java.lang.Math.min(anchor.x, lead.x);
        roundrect.y = java.lang.Math.min(anchor.y, lead.y);
        roundrect.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        roundrect.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
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

    // EDITING
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.Collection<org.jhotdraw.draw.handle.Handle> handles = super.createHandles(detailLevel);
        handles.add(new org.jhotdraw.draw.handle.RoundRectangleRadiusHandle(this));
        return handles;
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.RoundRectangle2D.Double r = ((java.awt.geom.RoundRectangle2D.Double) (geometry));
        roundrect.x = r.x;
        roundrect.y = r.y;
        roundrect.width = r.width;
        roundrect.height = r.height;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return roundrect.clone();
    }

    // CONNECTING
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopRoundRectangleConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        return new org.jhotdraw.draw.connector.ChopRoundRectangleConnector(this);
    }

    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.draw.figure.RoundRectangleFigure clone() {
        org.jhotdraw.draw.figure.RoundRectangleFigure that = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (super.clone()));
        that.roundrect = ((java.awt.geom.RoundRectangle2D.Double) (this.roundrect.clone()));
        return that;
    }
}