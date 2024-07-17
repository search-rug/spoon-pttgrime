/* @(#)MoveHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A handle that changes the location of the owning figure, if the figure is transformable.
 */
public class MoveHandle extends org.jhotdraw.draw.handle.LocatorHandle {
    /**
     * The previously handled x and y coordinates.
     */
    private java.awt.geom.Point2D.Double oldPoint;

    public MoveHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator locator) {
        super(owner, locator);
    }

    /**
     * Creates handles for each corner of a figure and adds them to the provided collection.
     */
    public static void addMoveHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(org.jhotdraw.draw.handle.MoveHandle.southEast(f));
        handles.add(org.jhotdraw.draw.handle.MoveHandle.southWest(f));
        handles.add(org.jhotdraw.draw.handle.MoveHandle.northEast(f));
        handles.add(org.jhotdraw.draw.handle.MoveHandle.northWest(f));
    }

    /**
     * Draws this handle.
     *
     * <p>If the figure is transformable, the handle is drawn as a filled rectangle. If the figure is
     * not transformable, the handle is drawn as an unfilled rectangle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if (getOwner().isTransformable()) {
            drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.MOVE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.MOVE_HANDLE_STROKE_COLOR));
        } else {
            drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_STROKE_COLOR));
        }
    }

    /**
     * Returns a cursor for the handle.
     *
     * @return Returns a move cursor, if the figure is transformable. Returns a default cursor
    otherwise.
     */
    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.MOVE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        oldPoint = (view.getConstrainer() == null) ? view.viewToDrawing(anchor) : view.getConstrainer().constrainPoint(view.viewToDrawing(anchor));
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.Figure f = getOwner();
        if (f.isTransformable()) {
            java.awt.geom.Point2D.Double newPoint = (view.getConstrainer() == null) ? view.viewToDrawing(lead) : view.getConstrainer().constrainPoint(view.viewToDrawing(lead));
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(newPoint.x - oldPoint.x, newPoint.y - oldPoint.y);
            f.willChange();
            f.transform(tx);
            f.changed();
            oldPoint = newPoint;
        }
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        if (getOwner().isTransformable()) {
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(lead.x - anchor.x, lead.y - anchor.y);
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(getOwner(), tx));
        }
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        org.jhotdraw.draw.figure.Figure f = getOwner();
        if (f.isTransformable()) {
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    tx.translate(0, -1);
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    tx.translate(0, +1);
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    tx.translate(-1, 0);
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    tx.translate(+1, 0);
                    evt.consume();
                    break;
            }
            f.willChange();
            f.transform(tx);
            f.changed();
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
        }
    }

    public static org.jhotdraw.draw.handle.Handle south(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.south());
    }

    public static org.jhotdraw.draw.handle.Handle southEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.southEast());
    }

    public static org.jhotdraw.draw.handle.Handle southWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.southWest());
    }

    public static org.jhotdraw.draw.handle.Handle north(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.north());
    }

    public static org.jhotdraw.draw.handle.Handle northEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.northEast());
    }

    public static org.jhotdraw.draw.handle.Handle northWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.northWest());
    }

    public static org.jhotdraw.draw.handle.Handle east(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.east());
    }

    public static org.jhotdraw.draw.handle.Handle west(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.MoveHandle(owner, org.jhotdraw.draw.locator.RelativeLocator.west());
    }
}