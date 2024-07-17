/* @(#)ConnectionStartHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.ConnectionFigure;
/**
 * A {@link Handle} which allows to connect the start of a {@link ConnectionFigure} to another
 * figure.
 *
 * @author Werner Randelshofer
 * @version $Id: ConnectionStartHandle.java -1 $
 */
public class ConnectionStartHandle extends org.jhotdraw.draw.handle.AbstractConnectionHandle {
    /**
     * Constructs the connection handle for the given start figure.
     */
    public ConnectionStartHandle(org.jhotdraw.draw.figure.ConnectionFigure owner) {
        super(owner);
    }

    /**
     * Sets the start of the connection.
     */
    @java.lang.Override
    protected void connect(org.jhotdraw.draw.connector.Connector c) {
        getOwner().setStartConnector(c);
    }

    /**
     * Disconnects the start figure.
     */
    @java.lang.Override
    protected void disconnect() {
        getOwner().setStartConnector(null);
    }

    @java.lang.Override
    protected org.jhotdraw.draw.connector.Connector getTarget() {
        return getOwner().getStartConnector();
    }

    /**
     * Sets the start point of the connection.
     */
    @java.lang.Override
    protected void setDrawingLocation(java.awt.geom.Point2D.Double p) {
        getOwner().willChange();
        getOwner().setStartPoint(p);
        getOwner().changed();
    }

    /**
     * Returns the start point of the connection.
     */
    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        return getOwner().getStartPoint();
    }

    @java.lang.Override
    protected boolean canConnect(org.jhotdraw.draw.connector.Connector existingEnd, org.jhotdraw.draw.connector.Connector targetEnd) {
        return getOwner().canConnect(targetEnd, existingEnd);
    }

    @java.lang.Override
    protected int getBezierNodeIndex() {
        return 0;
    }
}