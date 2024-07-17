/* @(#)ChopRoundRectangleConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
import org.jhotdraw.draw.figure.RoundRectangleFigure;
/**
 * A {@link Connector} which locates a connection point at the bounds of a {@link RoundRectangleFigure}.
 *
 * <p>
 */
public class ChopRoundRectangleConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    public ChopRoundRectangleConnector() {
    }

    public ChopRoundRectangleConnector(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        target = getConnectorTarget(target);
        org.jhotdraw.draw.figure.RoundRectangleFigure rrf = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (target));
        java.awt.geom.Rectangle2D.Double outer = rrf.getBounds();
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
                grow = 0;
                break;
        }
        org.jhotdraw.geom.Geom.grow(outer, grow, grow);
        java.awt.geom.Rectangle2D.Double inner = ((java.awt.geom.Rectangle2D.Double) (outer.clone()));
        double gw = (-(rrf.getArcWidth() + (grow * 2))) / 2;
        double gh = (-(rrf.getArcHeight() + (grow * 2))) / 2;
        inner.x -= gw;
        inner.y -= gh;
        inner.width += gw * 2;
        inner.height += gh * 2;
        java.awt.geom.Point2D.Double p = org.jhotdraw.geom.Geom.angleToPoint(outer, org.jhotdraw.geom.Geom.pointToAngle(outer, from));
        if ((p.x == outer.x) || (p.x == (outer.x + outer.width))) {
            p.y = java.lang.Math.min(java.lang.Math.max(p.y, inner.y), inner.y + inner.height);
        } else {
            p.x = java.lang.Math.min(java.lang.Math.max(p.x, inner.x), inner.x + inner.width);
        }
        return p;
    }
}