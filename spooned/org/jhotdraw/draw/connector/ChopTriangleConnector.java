/* @(#)ChopTriangleConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
import org.jhotdraw.draw.figure.TriangleFigure;
/**
 * A {@link Connector} which locates a connection point at the bounds of a {@link TriangleFigure}.
 *
 * <p>
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ChopTriangleConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    public ChopTriangleConnector() {
    }

    public ChopTriangleConnector(org.jhotdraw.draw.figure.TriangleFigure owner) {
        super(owner);
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        org.jhotdraw.draw.figure.TriangleFigure bf = ((org.jhotdraw.draw.figure.TriangleFigure) (getConnectorTarget(target)));
        return bf.chop(from);
    }
}