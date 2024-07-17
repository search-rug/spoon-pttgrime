/* @(#)ChopBezierConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
import org.jhotdraw.draw.figure.BezierFigure;
/**
 * A {@link Connector} which locates a connection point at the bounds of a {@link BezierFigure}.
 *
 * <p>
 *
 * <p>XXX - This connector does not take the stroke width of the polygon into account.
 */
public class ChopBezierConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    public ChopBezierConnector() {
    }

    public ChopBezierConnector(org.jhotdraw.draw.figure.BezierFigure owner) {
        super(owner);
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        org.jhotdraw.draw.figure.BezierFigure bf = ((org.jhotdraw.draw.figure.BezierFigure) (getConnectorTarget(target)));
        return bf.chop(from);
    }
}