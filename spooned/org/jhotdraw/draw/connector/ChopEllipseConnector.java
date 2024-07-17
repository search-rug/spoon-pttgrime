/* @(#)ChopEllipseConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
/**
 * A {@link Connector} which locates a connection point at the bounds of any figure which has an
 * elliptic shape, such as {@link org.jhotdraw.draw.EllipseFigure}.
 *
 * <p>
 */
public class ChopEllipseConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    public ChopEllipseConnector() {
    }

    public ChopEllipseConnector(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    private java.awt.Color getStrokeColor(org.jhotdraw.draw.figure.Figure f) {
        return f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR);
    }

    private float getStrokeWidth(org.jhotdraw.draw.figure.Figure f) {
        java.lang.Double w = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH);
        return w == null ? 1.0F : w.floatValue();
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        target = getConnectorTarget(target);
        java.awt.geom.Rectangle2D.Double r = target.getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(target));
        if (getStrokeColor(target) != null) {
            double grow;
            switch (target.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT)) {
                case CENTER :
                default :
                    grow = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(target, org.jhotdraw.draw.AttributeKeys.scaleFromContext(target)) / 2.0;
                    break;
                case OUTSIDE :
                    grow = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(target, org.jhotdraw.draw.AttributeKeys.scaleFromContext(target));
                    break;
                case INSIDE :
                    grow = 0.0F;
                    break;
            }
            org.jhotdraw.geom.Geom.grow(r, grow, grow);
        }
        double angle = org.jhotdraw.geom.Geom.pointToAngle(r, from);
        return org.jhotdraw.geom.Geom.ovalAngleToPoint(r, angle);
    }
}