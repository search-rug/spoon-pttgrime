/* @(#)RotateHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A Handle to rotate a Figure.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class RotateHandle extends org.jhotdraw.draw.handle.AbstractRotateHandle {
    public RotateHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getCenter() {
        java.awt.geom.Rectangle2D.Double bounds = getTransformedBounds();
        return new java.awt.geom.Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        // This handle is placed above the figure.
        // We move it up by a handlesizes, so that it won't overlap with
        // the handles from TransformHandleKit.
        java.awt.geom.Rectangle2D.Double bounds = getTransformedBounds();
        java.awt.geom.Point2D.Double origin = new java.awt.geom.Point2D.Double(bounds.getCenterX(), bounds.y - (getHandlesize() / view.getScaleFactor()));
        return origin;
    }
}