/* @(#)DragHandle.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A handle that changes the location of the owning figure, the handle covers all visible points of
 * the figure.
 *
 * <p>Usually, DragHandle is not needed, because of the {@link org.jhotdraw.draw.tool.DragTracker}
 * in the |@code SelectionTool}. Use a (subclass of) {@code DragHandle}, if you want to implement
 * figure specific drag behavior. A {@code CompositeFigure} can create {@code DragHandle}s for all
 * its child figures, to support dragging of child figures without having to decompose the
 * CompositeFigure.
 */
public class DragHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    /**
     * The previously handled x and y coordinates.
     */
    private java.awt.geom.Point2D.Double oldPoint;

    public DragHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    /**
     * Draws nothing. Drag Handles have no visual appearance of their own.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        oldPoint = (view.getConstrainer() == null) ? view.viewToDrawing(anchor) : view.getConstrainer().constrainPoint(view.viewToDrawing(anchor));
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.Figure f = getOwner();
        java.awt.geom.Point2D.Double newPoint = (view.getConstrainer() == null) ? view.viewToDrawing(lead) : view.getConstrainer().constrainPoint(view.viewToDrawing(lead));
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(newPoint.x - oldPoint.x, newPoint.y - oldPoint.y);
        f.willChange();
        f.transform(tx);
        f.changed();
        oldPoint = newPoint;
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(lead.x - anchor.x, lead.y - anchor.y);
        java.util.List<org.jhotdraw.draw.figure.Figure> draggedFigures = new java.util.ArrayList<>();
        draggedFigures.add(getOwner());
        java.awt.geom.Point2D.Double dropPoint = getView().viewToDrawing(lead);
        org.jhotdraw.draw.figure.Figure dropTarget = getView().getDrawing().findFigureExcept(dropPoint, draggedFigures);
        if (dropTarget != null) {
            boolean snapBack = dropTarget.handleDrop(dropPoint, draggedFigures, getView());
            if (snapBack) {
                tx = new java.awt.geom.AffineTransform();
                tx.translate(anchor.x - lead.x, anchor.y - lead.y);
                for (org.jhotdraw.draw.figure.Figure f : draggedFigures) {
                    f.willChange();
                    f.transform(tx);
                    f.changed();
                }
            } else {
                fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(getOwner(), tx));
            }
        } else {
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(getOwner(), tx));
        }
    }

    @java.lang.Override
    public boolean contains(java.awt.Point p) {
        return getOwner().contains(getView().viewToDrawing(p));
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        return getView().drawingToView(getOwner().getDrawingArea());
    }

    /**
     * Returns a cursor for the handle.
     */
    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
    }
}