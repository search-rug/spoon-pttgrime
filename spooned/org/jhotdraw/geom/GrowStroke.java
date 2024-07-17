/* @(#)GrowStroke.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.geom;
/**
 * GrowStroke can be used to grow/shrink a figure by a specified line width. This only works with
 * closed convex paths having edges in clockwise direction.
 *
 * <p>Note: Although this is a Stroke object, it does not actually create a stroked shape, but one
 * that can be used for filling.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class GrowStroke extends org.jhotdraw.geom.DoubleStroke {
    private double grow;

    public GrowStroke(double grow, double miterLimit) {
        super(grow * 2.0, 1.0, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL, miterLimit, null, 0.0F);
        this.grow = grow;
    }

    @java.lang.Override
    public java.awt.Shape createStrokedShape(java.awt.Shape s) {
        org.jhotdraw.geom.path.BezierPath bp = new org.jhotdraw.geom.path.BezierPath();
        java.awt.geom.Path2D.Double left = new java.awt.geom.Path2D.Double();
        java.awt.geom.Path2D.Double right = new java.awt.geom.Path2D.Double();
        if (s instanceof java.awt.geom.Path2D.Double) {
            left.setWindingRule(((java.awt.geom.Path2D.Double) (s)).getWindingRule());
            right.setWindingRule(((java.awt.geom.Path2D.Double) (s)).getWindingRule());
        } else if (s instanceof org.jhotdraw.geom.path.BezierPath) {
            left.setWindingRule(((org.jhotdraw.geom.path.BezierPath) (s)).getWindingRule());
            right.setWindingRule(((org.jhotdraw.geom.path.BezierPath) (s)).getWindingRule());
        }
        double[] coords = new double[6];
        // FIXME - We only do a flattened path
        for (java.awt.geom.PathIterator i = s.getPathIterator(null, 0.1); !i.isDone(); i.next()) {
            int type = i.currentSegment(coords);
            switch (type) {
                case java.awt.geom.PathIterator.SEG_MOVETO :
                    if (bp.size() != 0) {
                        traceStroke(bp, left, right);
                    }
                    bp.clear();
                    bp.moveTo(coords[0], coords[1]);
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO :
                    if ((coords[0] != bp.nodes().get(bp.size() - 1).x[0]) || (coords[1] != bp.nodes().get(bp.size() - 1).y[0])) {
                        bp.lineTo(coords[0], coords[1]);
                    }
                    break;
                case java.awt.geom.PathIterator.SEG_QUADTO :
                    bp.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case java.awt.geom.PathIterator.SEG_CUBICTO :
                    bp.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case java.awt.geom.PathIterator.SEG_CLOSE :
                    bp.setClosed(true);
                    break;
            }
        }
        if (bp.size() > 1) {
            traceStroke(bp, left, right);
        }
        if (org.jhotdraw.geom.Geom.contains(left.getBounds2D(), right.getBounds2D())) {
            return grow > 0 ? left : right;
        } else {
            return grow > 0 ? right : left;
        }
    }
}