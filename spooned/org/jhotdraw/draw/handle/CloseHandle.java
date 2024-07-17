/* @(#)CloseHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A {@link Handle} which requests to remove its owning figure when clicked.
 */
public class CloseHandle extends org.jhotdraw.draw.handle.LocatorHandle {
    private boolean pressed;

    public CloseHandle(org.jhotdraw.draw.figure.Figure owner) {
        this(owner, new org.jhotdraw.draw.locator.RelativeLocator(1.0, 0.0));
    }

    public CloseHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator locator) {
        super(owner, locator);
    }

    @java.lang.Override
    protected int getHandlesize() {
        return 9;
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawRectangle(g, pressed ? java.awt.Color.orange : java.awt.Color.white, java.awt.Color.black);
        java.awt.Rectangle r = getBounds();
        g.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
        g.drawLine(r.x + r.width, r.y, r.x, r.y + r.height);
    }

    /**
     * Returns a cursor for the handle.
     */
    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getDefaultCursor();
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        pressed = basicGetBounds().contains(lead);
        if (pressed) {
            getOwner().requestRemove();
        }
        fireAreaInvalidated(getDrawingArea());
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        pressed = true;
        fireAreaInvalidated(getDrawingArea());
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        boolean oldValue = pressed;
        pressed = basicGetBounds().contains(lead);
        if (oldValue != pressed) {
            fireAreaInvalidated(getDrawingArea());
        }
    }
}