/* @(#)ChopRectangleConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
/**
 * A {@link Connector} which locates a connection point at the bounds of any figure which has a
 * rectangular shape, such as {@link org.jhotdraw.draw.RectangleFigure}.
 *
 * @see Connector
 */
public class ChopRectangleConnector extends org.jhotdraw.draw.connector.AbstractConnector {
    private static final long serialVersionUID = 1L;

    public ChopRectangleConnector() {
    }

    public ChopRectangleConnector(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double findStart(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        org.jhotdraw.draw.figure.Figure startFigure = connection.getStartConnector().getOwner();
        java.awt.geom.Point2D.Double from;
        if ((connection.getNodeCount() <= 2) || (connection.getLiner() != null)) {
            if (connection.getEndConnector() == null) {
                from = connection.getEndPoint();
            } else {
                java.awt.geom.Rectangle2D.Double r1 = getConnectorTarget(connection.getEndConnector().getOwner()).getBounds();
                from = new java.awt.geom.Point2D.Double(r1.x + (r1.width / 2), r1.y + (r1.height / 2));
            }
        } else {
            from = connection.getPoint(1);
        }
        return chop(startFigure, from);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double findEnd(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        org.jhotdraw.draw.figure.Figure endFigure = connection.getEndConnector().getOwner();
        java.awt.geom.Point2D.Double from;
        if ((((connection.getNodeCount() <= 3) && (connection.getStartFigure() == connection.getEndFigure())) || (connection.getNodeCount() <= 2)) || (connection.getLiner() != null)) {
            if (connection.getStartConnector() == null) {
                from = connection.getStartPoint();
            } else if (connection.getStartFigure() == connection.getEndFigure()) {
                java.awt.geom.Rectangle2D.Double r1 = getConnectorTarget(connection.getStartConnector().getOwner()).getBounds();
                from = new java.awt.geom.Point2D.Double(r1.x + (r1.width / 2), r1.y);
            } else {
                java.awt.geom.Rectangle2D.Double r1 = getConnectorTarget(connection.getStartConnector().getOwner()).getBounds();
                from = new java.awt.geom.Point2D.Double(r1.x + (r1.width / 2), r1.y + (r1.height / 2));
            }
        } else {
            from = connection.getPoint(connection.getNodeCount() - 2);
        }
        return chop(endFigure, from);
    }

    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        target = getConnectorTarget(target);
        java.awt.geom.Rectangle2D.Double r = target.getBounds();
        if (target.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) != null) {
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
                    grow = 0.0;
                    break;
            }
            org.jhotdraw.geom.Geom.grow(r, grow, grow);
        }
        return org.jhotdraw.geom.Geom.angleToPoint(r, org.jhotdraw.geom.Geom.pointToAngle(r, from));
    }
}