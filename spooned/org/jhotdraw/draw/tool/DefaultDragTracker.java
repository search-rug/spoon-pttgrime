/* @(#)DefaultDragTracker.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * <code>DefaultDragTracker</code> implements interactions with the content area of a <code>Figure
 * </code>.
 *
 * <p>The <code>DefaultDragTracker</code> handles one of the three states of the <code>SelectionTool
 * </code>. It comes into action, when the user presses the mouse button over the content area of a
 * <code>Figure</code>.
 *
 * <p>Design pattern:<br>
 * Name: Chain of Responsibility.<br>
 * Role: Handler.<br>
 * Partners: {@link SelectionTool} as Handler, {@link SelectAreaTracker} as Handler, {@link HandleTracker} as Handler.
 *
 * <p>Design pattern:<br>
 * Name: State.<br>
 * Role: State.<br>
 * Partners: {@link SelectAreaTracker} as State, {@link SelectionTool} as Context, {@link HandleTracker} as State.
 *
 * @see SelectionTool
 */
public class DefaultDragTracker extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.tool.DragTracker {
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

    private java.util.HashSet<org.jhotdraw.draw.figure.Figure> transformedFigures;

    public DefaultDragTracker(org.jhotdraw.draw.figure.Figure figure) {
        anchorFigure = figure;
    }

    public DefaultDragTracker() {
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
            transformedFigures = new java.util.HashSet<>();
            for (org.jhotdraw.draw.figure.Figure f : view.getSelectedFigures()) {
                if (f.isTransformable()) {
                    transformedFigures.add(f);
                    if (dragRect == null) {
                        dragRect = f.getBounds();
                    } else {
                        dragRect.add(f.getBounds());
                    }
                }
            }
            if (dragRect != null) {
                anchorPoint = previousPoint = view.viewToDrawing(anchor);
                anchorOrigin = previousOrigin = new java.awt.geom.Point2D.Double(dragRect.x, dragRect.y);
            }
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView view = getView();
        if (!transformedFigures.isEmpty()) {
            if (isDragging == false) {
                isDragging = true;
                updateCursor(editor.findView(((java.awt.Container) (evt.getSource()))), new java.awt.Point(evt.getX(), evt.getY()));
            }
            java.awt.geom.Point2D.Double currentPoint = view.viewToDrawing(new java.awt.Point(evt.getX(), evt.getY()));
            dragRect.x += currentPoint.x - previousPoint.x;
            dragRect.y += currentPoint.y - previousPoint.y;
            java.awt.geom.Rectangle2D.Double constrainedRect = ((java.awt.geom.Rectangle2D.Double) (dragRect.clone()));
            if (view.getConstrainer() != null) {
                view.getConstrainer().constrainRectangle(constrainedRect);
            }
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(constrainedRect.x - previousOrigin.x, constrainedRect.y - previousOrigin.y);
            for (org.jhotdraw.draw.figure.Figure f : transformedFigures) {
                f.willChange();
                f.transform(tx);
                f.changed();
            }
            previousPoint = currentPoint;
            previousOrigin = new java.awt.geom.Point2D.Double(constrainedRect.x, constrainedRect.y);
        }
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        super.mouseReleased(evt);
        org.jhotdraw.draw.DrawingView view = getView();
        if ((transformedFigures != null) && (!transformedFigures.isEmpty())) {
            isDragging = false;
            int x = evt.getX();
            int y = evt.getY();
            updateCursor(editor.findView(((java.awt.Container) (evt.getSource()))), new java.awt.Point(x, y));
            java.awt.geom.Point2D.Double newPoint = view.viewToDrawing(new java.awt.Point(x, y));
            org.jhotdraw.draw.figure.Figure dropTarget = getDrawing().findFigureExcept(newPoint, transformedFigures);
            if (dropTarget != null) {
                boolean snapBack = dropTarget.handleDrop(newPoint, transformedFigures, view);
                if (snapBack) {
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate(anchorOrigin.x - previousOrigin.x, anchorOrigin.y - previousOrigin.y);
                    for (org.jhotdraw.draw.figure.Figure f : transformedFigures) {
                        f.willChange();
                        f.transform(tx);
                        f.changed();
                    }
                    java.awt.Rectangle r = new java.awt.Rectangle(anchor.x, anchor.y, 0, 0);
                    r.add(evt.getX(), evt.getY());
                    maybeFireBoundsInvalidated(r);
                    fireToolDone();
                    return;
                }
            }
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate((-anchorOrigin.x) + previousOrigin.x, (-anchorOrigin.y) + previousOrigin.y);
            if (!tx.isIdentity()) {
                getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(transformedFigures, tx));
            }
        }
        java.awt.Rectangle r = new java.awt.Rectangle(anchor.x, anchor.y, 0, 0);
        r.add(evt.getX(), evt.getY());
        maybeFireBoundsInvalidated(r);
        transformedFigures = null;
        fireToolDone();
    }

    @java.lang.Override
    public void setDraggedFigure(org.jhotdraw.draw.figure.Figure f) {
        anchorFigure = f;
    }
}