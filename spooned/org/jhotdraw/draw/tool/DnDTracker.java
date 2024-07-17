/* @(#)DnDTracker.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * This is a tracker which supports drag and drop of figures between drawing views and any other
 * component or application which support drag and drop.
 *
 * <p>DnDTracker can be used stand-alone or instead of {@code DragTracker} in the {@code SelectionTool} or the {@code DelegationSelectionTool}.
 *
 * <p>To get a drag image using drag and drop, the drawing needs to provide an image output format.
 *
 * <p>Drag and Drop is about information moving, not images or objects. Its about moving a figure to
 * another application and that application understanding both its shape, color, attributes, and
 * everything about it - not necessarily how it looks.
 */
public class DnDTracker extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.tool.DragTracker {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.draw.figure.Figure anchorFigure;

    /**
     * The drag rectangle encompasses the bounds of all dragged figures.
     */
    protected java.awt.geom.Rectangle2D.Double dragRect;

    /**
     * The previousOrigin holds the origin of all dragged figures of the previous mouseDragged event.
     * This coordinate is constrained using the Constrainer of the DrawingView.
     */
    protected java.awt.geom.Point2D.Double previousOrigin;

    /**
     * The anchorOrigin holds the origin of all dragged figures of the mousePressed event.
     */
    protected java.awt.geom.Point2D.Double anchorOrigin;

    /**
     * The previousPoint holds the location of the mouse of the previous mouseDragged event. This
     * coordinate is not constrained using the Constrainer of the DrawingView.
     */
    protected java.awt.geom.Point2D.Double previousPoint;

    /**
     * The anchorPoint holds the location of the mouse of the mousePressed event. This coordinate is
     * not constrained using the Constrainer of the DrawingView.
     */
    protected java.awt.geom.Point2D.Double anchorPoint;

    private boolean isDragging;

    public DnDTracker() {
    }

    public DnDTracker(org.jhotdraw.draw.figure.Figure figure) {
        anchorFigure = figure;
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        updateCursor(editor.findView(((java.awt.Container) (evt.getSource()))), evt.getPoint());
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        super.mousePressed(evt);
        org.jhotdraw.draw.DrawingView view = getView();
        if (evt.isShiftDown()) {
            view.setHandleDetailLevel(0);
            view.toggleSelection(anchorFigure);
            if (!view.isFigureSelected(anchorFigure)) {
                anchorFigure = null;
            }
        } else if (!view.isFigureSelected(anchorFigure)) {
            view.setHandleDetailLevel(0);
            view.clearSelection();
            view.addToSelection(anchorFigure);
        }
        if (!view.getSelectedFigures().isEmpty()) {
            dragRect = null;
            for (org.jhotdraw.draw.figure.Figure f : view.getSelectedFigures()) {
                if (dragRect == null) {
                    dragRect = f.getBounds();
                } else {
                    dragRect.add(f.getBounds());
                }
            }
            anchorPoint = previousPoint = view.viewToDrawing(anchor);
            anchorOrigin = previousOrigin = new java.awt.geom.Point2D.Double(dragRect.x, dragRect.y);
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        org.jhotdraw.draw.DrawingView v = getView();
        org.jhotdraw.draw.figure.Figure f = v.findFigure(e.getPoint());
        if (f != null) {
            if (!v.getSelectedFigures().contains(f)) {
                v.clearSelection();
                v.addToSelection(f);
            }
            v.getComponent().getTransferHandler().exportAsDrag(v.getComponent(), e, java.awt.dnd.DnDConstants.ACTION_MOVE);
        }
        fireToolDone();
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        updateCursor(editor.findView(((java.awt.Container) (evt.getSource()))), evt.getPoint());
        fireToolDone();
    }

    @java.lang.Override
    public void setDraggedFigure(org.jhotdraw.draw.figure.Figure f) {
        anchorFigure = f;
    }
}