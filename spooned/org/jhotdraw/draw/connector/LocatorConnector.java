/* @(#)LocatorConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
/**
 * A LocatorConnector locates connection points with the help of a Locator. It supports the
 * definition of connection points to semantic locations.
 *
 * @see Locator
 * @see Connector
 */
public class LocatorConnector extends org.jhotdraw.draw.connector.AbstractConnector {
    private static final long serialVersionUID = 1L;

    /**
     * The standard size of the connector. The display box is centered around the located point.
     *
     * <p>FIXME - Why do we need a standard size?
     */
    public static final int SIZE = 2;

    private org.jhotdraw.draw.locator.Locator locator;

    public LocatorConnector() {
    }

    public LocatorConnector(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator l) {
        super(owner);
        locator = l;
    }

    public org.jhotdraw.draw.locator.Locator getLocator() {
        return locator;
    }

    public void setLocator(org.jhotdraw.draw.locator.Locator locator) {
        this.locator = locator;
    }

    protected java.awt.geom.Point2D.Double locate(org.jhotdraw.draw.figure.ConnectionFigure connection) {
        return locator.locate(getOwner(), org.jhotdraw.draw.AttributeKeys.scaleFromContext(connection)).location();
    }

    /**
     * Tests if a point is contained in the connector.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p) {
        return getBounds().contains(p);
    }

    /**
     * Gets the display box of the connector.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Point2D.Double p = locator.locate(getOwner(), scale).location();
        return new java.awt.geom.Rectangle2D.Double(p.x - (org.jhotdraw.draw.connector.LocatorConnector.SIZE / 2), p.y - (org.jhotdraw.draw.connector.LocatorConnector.SIZE / 2), org.jhotdraw.draw.connector.LocatorConnector.SIZE, org.jhotdraw.draw.connector.LocatorConnector.SIZE);
    }
}