/* @(#)ODGBezierFigure.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * ODGBezierFigure is not an actual ODG element, it is used by ODGPathFigure to represent a single
 * BezierPath segment within an ODG path.
 */
public class ODGBezierFigure extends org.jhotdraw.draw.figure.BezierFigure {
    private static final long serialVersionUID = 1L;

    private transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    public ODGBezierFigure() {
        this(false);
    }

    public ODGBezierFigure(boolean isClosed) {
        super(isClosed);
        attr().set(org.jhotdraw.draw.AttributeKeys.UNCLOSED_PATH_FILLED, true);
    }

    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(org.jhotdraw.samples.odg.figures.ODGPathFigure pathFigure, int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case 0 :
                for (int i = 0, n = path.size(); i < n; i++) {
                    handles.add(new org.jhotdraw.draw.handle.BezierNodeHandle(this, i, pathFigure));
                }
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                break;
            default :
                break;
        }
        return handles;
    }

    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        /* && view.getHandleDetailLevel() == 0 */
        if (evt.getClickCount() == 2) {
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
                evt.consume();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || ((tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION) != tx.getType())) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, tx);
            } else {
                java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, t);
            }
        } else {
            super.transform(tx);
        }
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                cachedDrawingArea = path.getBounds2D();
            } else {
                org.jhotdraw.geom.path.BezierPath p2 = path.clone();
                p2.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
                cachedDrawingArea = p2.getBounds2D();
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    /**
     * Transforms all coords of the figure by the current TRANSFORM attribute and then sets the
     * TRANSFORM attribute to null.
     */
    public void flattenTransform() {
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            path.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
            attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, null);
        }
        invalidate();
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedDrawingArea = null;
    }
}