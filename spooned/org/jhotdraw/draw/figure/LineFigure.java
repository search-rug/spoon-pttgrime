/* @(#)LineFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@link Figure} which draws a continuous bezier path between two points.
 */
public class LineFigure extends org.jhotdraw.draw.figure.BezierFigure {
    private static final long serialVersionUID = 1L;

    public LineFigure() {
        addNode(new org.jhotdraw.geom.path.BezierPath.Node(new java.awt.geom.Point2D.Double(0, 0)));
        addNode(new org.jhotdraw.geom.path.BezierPath.Node(new java.awt.geom.Point2D.Double(0, 0)));
        setConnectable(false);
    }

    // DRAWING
    // SHAPE AND BOUNDS
    // ATTRIBUTES
    // EDITING
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        switch (detailLevel) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this));
                for (int i = 0, n = path.size(); i < n; i++) {
                    handles.add(new org.jhotdraw.draw.handle.BezierNodeHandle(this, i));
                }
                break;
        }
        return handles;
    }

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        if ((evt.getClickCount() == 2) && (view.getHandleDetailLevel() == 0)) {
            willChange();
            final int index = splitSegment(p, ((float) (5.0F / view.getScaleFactor())));
            if (index != (-1)) {
                final org.jhotdraw.geom.path.BezierPath.Node newNode = getNode(index);
                fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public void redo() throws javax.swing.undo.CannotRedoException {
                        super.redo();
                        willChange();
                        addNode(index, newNode);
                        changed();
                    }

                    @java.lang.Override
                    public void undo() throws javax.swing.undo.CannotUndoException {
                        super.undo();
                        willChange();
                        removeNode(index);
                        changed();
                    }
                });
                changed();
                return true;
            }
        }
        return false;
    }
}