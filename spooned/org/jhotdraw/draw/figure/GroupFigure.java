/* @(#)GroupFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@link org.jhotdraw.draw.figure.Figure} which groups a collection of figures.
 */
public class GroupFigure extends org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure {
    private static final long serialVersionUID = 1L;

    public GroupFigure() {
        setConnectable(false);
    }

    /**
     * This is a default implementation that chops the point at the rectangle returned by getBounds()
     * of the figure.
     *
     * <p>Figures which have a non-rectangular shape need to override this method.
     *
     * <p>FIXME Invoke chop on each child and return the closest point.
     */
    public java.awt.geom.Point2D.Double chop(java.awt.geom.Point2D.Double from) {
        java.awt.geom.Rectangle2D.Double r = getBounds();
        return org.jhotdraw.geom.Geom.angleToPoint(r, org.jhotdraw.geom.Geom.pointToAngle(r, from));
    }

    /**
     * Returns true if all children of the group are transformable.
     */
    @java.lang.Override
    public boolean isTransformable() {
        for (org.jhotdraw.draw.figure.Figure f : children) {
            if (!f.isTransformable()) {
                return false;
            }
        }
        return true;
    }
}