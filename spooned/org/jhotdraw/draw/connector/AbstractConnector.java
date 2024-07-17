/* @(#)AbstractConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
/**
 * This abstract class can be extended to implement a {@link Connector}.
 *
 * @see Connector
 */
public class AbstractConnector implements org.jhotdraw.draw.connector.Connector {
    private static final long serialVersionUID = 1L;

    /**
     * The owner of the connector
     */
    private org.jhotdraw.draw.figure.Figure owner;

    /**
     * Whether we should connect to the figure or to its decorator.
     */
    private boolean isConnectToDecorator;

    /**
     * Whether the state of this connector is persistent. Set this to true only, when the user
     * interface allows to change the state of the connector.
     */
    private boolean isStatePersistent;

    /**
     * Constructs a connector that has no owner. It is only used internally to resurrect a connectors
     * from a StorableOutput. It should never be called directly.
     */
    public AbstractConnector() {
        owner = null;
    }

    /**
     * Constructs a connector with the given owner figure.
     */
    public AbstractConnector(org.jhotdraw.draw.figure.Figure owner) {
        this.owner = owner;
    }

    public void setConnectToDecorator(boolean newValue) {
        isConnectToDecorator = newValue;
    }

    public boolean isConnectToDecorator() {
        return isConnectToDecorator;
    }

    protected final org.jhotdraw.draw.figure.Figure getConnectorTarget(org.jhotdraw.draw.figure.Figure f) {
        return isConnectToDecorator && (((org.jhotdraw.draw.figure.DecoratedFigure) (f)).getDecorator() != null) ? ((org.jhotdraw.draw.figure.DecoratedFigure) (f)).getDecorator() : f;
    }

    /**
     * Tests if a point is contained in the connector. This implementation tests if the point is
     * contained by the figure object, which owns this connector.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p) {
        return getOwner().contains(p);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double findStart(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        return findPoint(connection);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double findEnd(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        return findPoint(connection);
    }

    /**
     * Gets the connection point. Override when the connector does not need to distinguish between the
     * start and end point of a connection.
     */
    protected java.awt.geom.Point2D.Double findPoint(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        return org.jhotdraw.geom.Geom.center(getBounds());
    }

    /**
     * Gets the connector's owner.
     */
    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getOwner() {
        return owner;
    }

    /**
     * Sets the connector's owner.
     */
    public void setOwner(org.jhotdraw.draw.figure.Figure newValue) {
        owner = newValue;
    }

    @java.lang.Override
    public java.lang.Object clone() {
        try {
            org.jhotdraw.draw.connector.AbstractConnector that = ((org.jhotdraw.draw.connector.AbstractConnector) (super.clone()));
            return that;
        } catch (java.lang.CloneNotSupportedException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.toString());
            // error.initCause(e); <- requires JDK 1.4
            throw error;
        }
    }

    /**
     * This is called, when the start location of the connection has been moved by the user. The user
     * has this probably done, to adjust the layout. The connector may use this as a hint to improve
     * the results for the next call to findEnd.
     */
    public void updateStartLocation(java.awt.geom.Point2D.Double p) {
    }

    /**
     * This is called, when the end location of the connection has been moved by the user. The user
     * has this probably done, to adjust the layout. The connector may use this as a hint to improve
     * the results for the next call to findStart.
     */
    public void updateEndLocation(java.awt.geom.Point2D.Double p) {
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getAnchor() {
        return org.jhotdraw.geom.Geom.center(getBounds());
    }

    @java.lang.Override
    public void updateAnchor(java.awt.geom.Point2D.Double p) {
    }

    @java.lang.Override
    public final java.awt.geom.Rectangle2D.Double getBounds() {
        return getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(owner));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return isConnectToDecorator() ? ((org.jhotdraw.draw.figure.DecoratedFigure) (getOwner())).getDecorator().getBounds(scale) : getOwner().getBounds(scale);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea() {
        java.awt.geom.Point2D.Double anchor = getAnchor();
        return new java.awt.geom.Rectangle2D.Double(anchor.x - 4, anchor.y - 4, 8, 8);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.geom.Point2D.Double anchor = getAnchor();
        java.awt.geom.Ellipse2D.Double e = new java.awt.geom.Ellipse2D.Double(anchor.x - 3, anchor.y - 3, 6, 6);
        g.setColor(java.awt.Color.BLUE);
        g.fill(e);
        // g.setColor(Color.BLACK);
        // g.draw(e);
    }
}