/* @(#)Shapes.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.geom;
/**
 * Shapes.
 */
public class Shapes {
    private Shapes() {
    }

    /**
     * Returns true, if the outline of this bezier path contains the specified point.
     *
     * @param p
     * 		The point to be tested.
     * @param tolerance
     * 		The tolerance for the test.
     */
    public static boolean outlineContains(java.awt.Shape shape, java.awt.geom.Point2D.Double p, double tolerance) {
        double[] coords = new double[6];
        double prevX = 0;
        double prevY = 0;
        double moveX = 0;
        double moveY = 0;
        for (java.awt.geom.PathIterator i = new java.awt.geom.FlatteningPathIterator(shape.getPathIterator(new java.awt.geom.AffineTransform(), tolerance), tolerance); !i.isDone(); i.next()) {
            switch (i.currentSegment(coords)) {
                case java.awt.geom.PathIterator.SEG_CLOSE :
                    if (org.jhotdraw.geom.Geom.lineContainsPoint(prevX, prevY, moveX, moveY, p.x, p.y, tolerance)) {
                        return true;
                    }
                    break;
                case java.awt.geom.PathIterator.SEG_CUBICTO :
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO :
                    if (org.jhotdraw.geom.Geom.lineContainsPoint(prevX, prevY, coords[0], coords[1], p.x, p.y, tolerance)) {
                        return true;
                    }
                    break;
                case java.awt.geom.PathIterator.SEG_MOVETO :
                    moveX = coords[0];
                    moveY = coords[1];
                    break;
                case java.awt.geom.PathIterator.SEG_QUADTO :
                    break;
                default :
                    break;
            }
            prevX = coords[0];
            prevY = coords[1];
        }
        return false;
    }
}