/* @(#)NullHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A handle that doesn't change the owned figure. Its only purpose is to show feedback that a figure
 * is selected.
 */
public class NullHandle extends org.jhotdraw.draw.handle.LocatorHandle {
    public NullHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator locator) {
        super(owner, locator);
    }

    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getDefaultCursor();
    }

    /**
     * Creates handles for each lead of a figure and adds them to the provided collection.
     */
    public static void addLeadHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(new org.jhotdraw.draw.handle.NullHandle(f, new org.jhotdraw.draw.locator.RelativeLocator(0.0F, 0.0F)));
        handles.add(new org.jhotdraw.draw.handle.NullHandle(f, new org.jhotdraw.draw.locator.RelativeLocator(0.0F, 1.0F)));
        handles.add(new org.jhotdraw.draw.handle.NullHandle(f, new org.jhotdraw.draw.locator.RelativeLocator(1.0F, 0.0F)));
        handles.add(new org.jhotdraw.draw.handle.NullHandle(f, new org.jhotdraw.draw.locator.RelativeLocator(1.0F, 1.0F)));
    }

    /**
     * Draws this handle. Null Handles are drawn as unfilled rectangles.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_STROKE_COLOR));
    }
}