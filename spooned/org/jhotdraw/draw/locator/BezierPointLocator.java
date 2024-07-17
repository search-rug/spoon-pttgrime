/* @(#)BezierPointLocator.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.locator;
/**
 * A {@link Locator} which locates a node on the bezier path of a BezierFigure.
 */
public class BezierPointLocator extends org.jhotdraw.draw.locator.AbstractLocator {
    private static final long serialVersionUID = 1L;

    private final int index;

    private final int coord;

    public BezierPointLocator(int index) {
        this.index = index;
        this.coord = 0;
    }

    public BezierPointLocator(int index, int coord) {
        this.index = index;
        this.coord = index;
    }

    @java.lang.Override
    public Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, double scale) {
        org.jhotdraw.draw.figure.BezierFigure plf = ((org.jhotdraw.draw.figure.BezierFigure) (owner));
        if (index < plf.getNodeCount()) {
            return new Position(plf.getPoint(index, coord));
        }
        return new Position(new java.awt.geom.Point2D.Double(0, 0));
    }
}