/* @(#)SeparatorLineFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert.figures;
/**
 * A horizontal line with a preferred size of 1,1.
 */
public class SeparatorLineFigure extends org.jhotdraw.draw.figure.RectangleFigure {
    private static final long serialVersionUID = 1L;

    public SeparatorLineFigure() {
    }

    @java.lang.Override
    public org.jhotdraw.geom.Dimension2DDouble getPreferredSize(double scale) {
        double width = java.lang.Math.ceil(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.get(this));
        return new org.jhotdraw.geom.Dimension2DDouble(width, width);
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        // no fill
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        g.draw(new java.awt.geom.Line2D.Double(r.x, r.y, (r.x + r.width) - 1, r.y));
    }
}