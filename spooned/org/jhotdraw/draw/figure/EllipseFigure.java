/* @(#)EllipseFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@link Figure} with an elliptic shape.
 */
public class EllipseFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    protected java.awt.geom.Ellipse2D.Double ellipse;

    /**
     * Constructs a new {@code EllipseFigure}, initialized to location (0,&nbsp;0) and size
     * (0,&nbsp;0).
     */
    public EllipseFigure() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs and initializes an {@code EllipseFigure} from the specified coordinates.
     *
     * @param x
     * 		the x coordinate of the bounding rectangle
     * @param y
     * 		the y coordinate of the bounding rectangle
     * @param width
     * 		the width of the rectangle
     * @param height
     * 		the height of the rectangle
     */
    public EllipseFigure(double x, double y, double width, double height) {
        ellipse = new java.awt.geom.Ellipse2D.Double(x, y, width, height);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopEllipseConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        return new org.jhotdraw.draw.connector.ChopEllipseConnector(this);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (ellipse.getBounds2D()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (ellipse.getBounds2D()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale);
        org.jhotdraw.geom.Geom.grow(r, grow + 1, grow + 1);
        return r;
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        java.awt.geom.Ellipse2D.Double r = ((java.awt.geom.Ellipse2D.Double) (ellipse.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        if ((r.width > 0) && (r.height > 0)) {
            g.fill(r);
        }
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.Ellipse2D.Double r = ((java.awt.geom.Ellipse2D.Double) (ellipse.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        if ((r.width > 0) && (r.height > 0)) {
            g.draw(r);
        }
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        java.awt.geom.Ellipse2D.Double r = ((java.awt.geom.Ellipse2D.Double) (ellipse.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleDenominator);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        return r.contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        ellipse.x = java.lang.Math.min(anchor.x, lead.x);
        ellipse.y = java.lang.Math.min(anchor.y, lead.y);
        ellipse.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        ellipse.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		the transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.EllipseFigure clone() {
        org.jhotdraw.draw.figure.EllipseFigure that = ((org.jhotdraw.draw.figure.EllipseFigure) (super.clone()));
        that.ellipse = ((java.awt.geom.Ellipse2D.Double) (this.ellipse.clone()));
        return that;
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.Ellipse2D.Double e = ((java.awt.geom.Ellipse2D.Double) (geometry));
        ellipse.x = e.x;
        ellipse.y = e.y;
        ellipse.width = e.width;
        ellipse.height = e.height;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return ellipse.clone();
    }
}