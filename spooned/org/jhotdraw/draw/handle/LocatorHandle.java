/* @(#)LocatorHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A LocatorHandle implements a Handle by delegating the location requests to a Locator object.
 *
 * @see Locator
 */
public abstract class LocatorHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private org.jhotdraw.draw.locator.Locator locator;

    /**
     * Initializes the LocatorHandle with the given Locator.
     */
    public LocatorHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator l) {
        super(owner);
        locator = l;
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getDrawingLocation() {
        return locator.locate(getOwner(), view.getScaleFactor()).location();
    }
}