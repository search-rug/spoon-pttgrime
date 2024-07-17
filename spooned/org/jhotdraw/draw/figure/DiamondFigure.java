/* @(#)DiamondFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@link Figure} with a diamond shape.
 *
 * <p>The diamond vertices are located at the midpoints of its enclosing rectangle.
 */
// EVENT HANDLING
public class DiamondFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    /**
     * If the attribute IS_QUADRATIC is put to true, all sides of the diamond have the same length.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> IS_QUADRATIC = new org.jhotdraw.draw.AttributeKey<>("isQuadratic", java.lang.Boolean.class, false);

    /**
     * The bounds of the diamond figure.
     */
    private java.awt.geom.Rectangle2D.Double rectangle;

    public DiamondFigure() {
        this(0, 0, 0, 0);
    }

    public DiamondFigure(double x, double y, double width, double height) {
        rectangle = new java.awt.geom.Rectangle2D.Double(x, y, width, height);
        /* setFillColor(Color.white);
        setStrokeColor(Color.black);
         */
    }

    // DRAWING
    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        if (attr().get(org.jhotdraw.draw.figure.DiamondFigure.IS_QUADRATIC)) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        if (grow != 0.0) {
            double w = r.width / 2.0;
            double h = r.height / 2.0;
            double lineLength = java.lang.Math.sqrt((w * w) + (h * h));
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            double growx;
            double growy;
            growx = ((yb * yb) / xa) + xa;
            growy = ((xa * xa) / yb) + yb;
            org.jhotdraw.geom.Geom.grow(r, growx, growy);
        }
        java.awt.geom.Path2D.Double diamond = new java.awt.geom.Path2D.Double();
        diamond.moveTo(r.x + (r.width / 2), r.y);
        diamond.lineTo(r.x + r.width, r.y + (r.height / 2));
        diamond.lineTo(r.x + (r.width / 2), r.y + r.height);
        diamond.lineTo(r.x, r.y + (r.height / 2));
        diamond.closePath();
        g.fill(diamond);
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        if (attr().get(org.jhotdraw.draw.figure.DiamondFigure.IS_QUADRATIC)) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        if (grow != 0.0) {
            double growx;
            double growy;
            double w = r.width / 2.0;
            double h = r.height / 2.0;
            double lineLength = java.lang.Math.sqrt((w * w) + (h * h));
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            growx = ((yb * yb) / xa) + xa;
            growy = ((xa * xa) / yb) + yb;
            org.jhotdraw.geom.Geom.grow(r, growx, growy);
        }
        java.awt.geom.Path2D.Double diamond = new java.awt.geom.Path2D.Double();
        diamond.moveTo(r.x + (r.width / 2), r.y);
        diamond.lineTo(r.x + r.width, r.y + (r.height / 2));
        diamond.lineTo(r.x + (r.width / 2), r.y + r.height);
        diamond.lineTo(r.x, r.y + (r.height / 2));
        diamond.closePath();
        g.draw(diamond);
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Rectangle2D.Double bounds = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        return bounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scaleD) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        if (attr().get(org.jhotdraw.draw.figure.DiamondFigure.IS_QUADRATIC)) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleD);
        if (grow != 0.0) {
            double w = r.width / 2.0;
            double h = r.height / 2.0;
            double lineLength = java.lang.Math.sqrt((w * w) + (h * h));
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            double growx;
            double growy;
            growx = ((yb * yb) / xa) + xa;
            growy = ((xa * xa) / yb) + yb;
            org.jhotdraw.geom.Geom.grow(r, growx, growy);
        }
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        if (attr().get(org.jhotdraw.draw.figure.DiamondFigure.IS_QUADRATIC)) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        // if (r.contains(p)) {
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, scaleDenominator);
        if (grow != 0.0) {
            double w = r.width / 2.0;
            double h = r.height / 2.0;
            double lineLength = java.lang.Math.sqrt((w * w) + (h * h));
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            double growx;
            double growy;
            growx = ((yb * yb) / xa) + xa;
            growy = ((xa * xa) / yb) + yb;
            org.jhotdraw.geom.Geom.grow(r, growx, growy);
        }
        java.awt.geom.Path2D.Double diamond = new java.awt.geom.Path2D.Double();
        diamond.moveTo(r.x + (r.width / 2), r.y);
        diamond.lineTo(r.x + r.width, r.y + (r.height / 2));
        diamond.lineTo(r.x + (r.width / 2), r.y + r.height);
        diamond.lineTo(r.x, r.y + (r.height / 2));
        diamond.closePath();
        return diamond.contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        rectangle.x = java.lang.Math.min(anchor.x, lead.x);
        rectangle.y = java.lang.Math.min(anchor.y, lead.y);
        rectangle.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        rectangle.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
    }

    /**
     * Moves the Figure to a new location.
     *
     * @param tx
     * 		the transformation matrix.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (geometry));
        rectangle.x = r.x;
        rectangle.y = r.y;
        rectangle.width = r.width;
        rectangle.height = r.height;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return rectangle.clone();
    }

    // ATTRIBUTES
    // EDITING
    // CONNECTING
    /**
     * Returns the Figures connector for the specified location. By default a ChopDiamondConnector is
     * returned.
     *
     * @see ChopDiamondConnector
     */
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopDiamondConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStart) {
        return new org.jhotdraw.draw.connector.ChopDiamondConnector(this);
    }

    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.draw.figure.DiamondFigure clone() {
        org.jhotdraw.draw.figure.DiamondFigure that = ((org.jhotdraw.draw.figure.DiamondFigure) (super.clone()));
        that.rectangle = ((java.awt.geom.Rectangle2D.Double) (this.rectangle.clone()));
        return that;
    }
}