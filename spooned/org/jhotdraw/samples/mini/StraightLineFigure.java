/* @(#)StraightLineFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
import org.jhotdraw.draw.figure.AbstractAttributedFigure;
/**
 * Example showing the minimal amount of code needed to implement a {@code Figure} by extending
 * {@code AbstractAttributedFigure}.
 */
public class StraightLineFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Line2D.Double line;

    public StraightLineFigure() {
        line = new java.awt.geom.Line2D.Double();
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        g.draw(line);
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform ty) {
        java.awt.geom.Point2D.Double p1 = ((java.awt.geom.Point2D.Double) (line.getP1()));
        java.awt.geom.Point2D.Double p2 = ((java.awt.geom.Point2D.Double) (line.getP2()));
        line.setLine(ty.transform(p1, p1), ty.transform(p2, p2));
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double start, java.awt.geom.Point2D.Double end) {
        line.setLine(start, end);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (line.getBounds2D()));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return line.clone();
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object restoreData) {
        line = ((java.awt.geom.Line2D.Double) (((java.awt.geom.Line2D.Double) (restoreData)).clone()));
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        return org.jhotdraw.geom.Geom.lineContainsPoint(line.x1, line.y1, line.x2, line.y2, p.x, p.y, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scaleDenominator));
    }

    @java.lang.Override
    public org.jhotdraw.samples.mini.StraightLineFigure clone() {
        org.jhotdraw.samples.mini.StraightLineFigure that = ((org.jhotdraw.samples.mini.StraightLineFigure) (super.clone()));
        that.line = ((java.awt.geom.Line2D.Double) (this.line.clone()));
        return that;
    }
}