/* @(#)ChopDiamondConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
import org.jhotdraw.draw.figure.DiamondFigure;
/**
 * A {@link Connector} which locates a connection point at the bounds of any figure which has a
 * diamond shape, such as {@link DiamondFigure}.
 *
 * <p>
 */
public class ChopDiamondConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    public ChopDiamondConnector() {
        // only used for Storable implementation
    }

    public ChopDiamondConnector(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    /**
     * Return an appropriate connection point on the edge of a diamond figure
     */
    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        target = getConnectorTarget(target);
        java.awt.geom.Rectangle2D.Double r = target.getBounds();
        if (target.attr().get(org.jhotdraw.draw.figure.DiamondFigure.IS_QUADRATIC)) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double growx;
        double growy;
        // FIXME - This code is wrong. Copy correct code from DiamondFigure.
        switch (target.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT)) {
            case INSIDE :
                growx = growy = 0.0F;
                break;
            case OUTSIDE :
                double lineLength = java.lang.Math.sqrt((r.width * r.width) + (r.height * r.height));
                double scale = (org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(target, org.jhotdraw.draw.AttributeKeys.scaleFromContext(target)) * 2.0) / lineLength;
                growx = scale * r.height;
                growy = scale * r.width;
                // growy = getStrokeTotalWidth() * SQRT2;
                break;
            case CENTER :
            default :
                lineLength = java.lang.Math.sqrt((r.width * r.width) + (r.height * r.height));
                scale = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(target, org.jhotdraw.draw.AttributeKeys.scaleFromContext(target)) / lineLength;
                growx = scale * r.height;
                growy = scale * r.width;
                // growx = growy = getStrokeTotalWidth() / 2d * SQRT2;
                break;
        }
        org.jhotdraw.geom.Geom.grow(r, growx, growy);
        // Center point
        java.awt.geom.Point2D.Double c1 = new java.awt.geom.Point2D.Double(r.x + (r.width / 2), r.y + (r.height / 2));
        java.awt.geom.Point2D.Double p2 = new java.awt.geom.Point2D.Double(r.x + (r.width / 2), r.y + r.height);
        java.awt.geom.Point2D.Double p4 = new java.awt.geom.Point2D.Double(r.x + (r.width / 2), r.y);
        // If overlapping, just return the opposite corners
        if (r.contains(from)) {
            if ((from.y > r.y) && (from.y < (r.y + (r.height / 2)))) {
                return p2;
            } else {
                return p4;
            }
        }
        // Calculate angle to determine quadrant
        double ang = org.jhotdraw.geom.Geom.pointToAngle(r, from);
        // Dermine line points
        java.awt.geom.Point2D.Double p1 = new java.awt.geom.Point2D.Double(r.x + r.width, r.y + (r.height / 2));
        java.awt.geom.Point2D.Double p3 = new java.awt.geom.Point2D.Double(r.x, r.y + (r.height / 2));
        java.awt.geom.Point2D.Double rp = null;// This will be returned

        // Get the intersection with edges
        if ((ang > 0) && (ang < 1.57)) {
            rp = org.jhotdraw.geom.Geom.intersect(p1.x, p1.y, p2.x, p2.y, c1.x, c1.y, from.x, from.y);
        } else if ((ang > 1.575) && (ang < 3.14)) {
            rp = org.jhotdraw.geom.Geom.intersect(p2.x, p2.y, p3.x, p3.y, c1.x, c1.y, from.x, from.y);
        } else if ((ang > (-3.14)) && (ang < (-1.575))) {
            rp = org.jhotdraw.geom.Geom.intersect(p3.x, p3.y, p4.x, p4.y, c1.x, c1.y, from.x, from.y);
        } else if ((ang > (-1.57)) && (ang < 0)) {
            rp = org.jhotdraw.geom.Geom.intersect(p4.x, p4.y, p1.x, p1.y, c1.x, c1.y, from.x, from.y);
        }
        // No proper edge found, we should send one of four corners
        if (rp == null) {
            rp = org.jhotdraw.geom.Geom.angleToPoint(r, ang);
        }
        return rp;
    }
}