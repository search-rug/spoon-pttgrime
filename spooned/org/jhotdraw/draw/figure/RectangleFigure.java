/* @(#)RectangleFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
public class RectangleFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    protected java.awt.geom.Rectangle2D.Double rectangle;

    public RectangleFigure() {
        this(0, 0, 0, 0);
    }

    public RectangleFigure(double x, double y, double width, double height) {
        rectangle = new java.awt.geom.Rectangle2D.Double(x, y, width, height);
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        g.fill(r);
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        g.draw(r);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Rectangle2D.Double bounds = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        return bounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale) + 1.0;
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r;
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleDenominator) + 1.0;
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

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        rectangle.setRect(((java.awt.geom.Rectangle2D.Double) (geometry)));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return rectangle.clone();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.RectangleFigure clone() {
        org.jhotdraw.draw.figure.RectangleFigure that = ((org.jhotdraw.draw.figure.RectangleFigure) (super.clone()));
        that.rectangle = ((java.awt.geom.Rectangle2D.Double) (this.rectangle.clone()));
        return that;
    }
}