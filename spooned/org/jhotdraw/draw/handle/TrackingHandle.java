/* @(#)BezierNodeHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * Simple tracking handle that changes in some way the owner Figure. This change is injected using
 * some lambdas.
 */
public class TrackingHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private final java.util.function.Supplier<java.awt.geom.Point2D.Double> readLocation;

    private final java.util.function.Consumer<java.awt.geom.Point2D.Double> writeLocation;

    private final java.lang.Runnable deleteLocation;

    private final java.lang.Runnable insertLocation;

    private org.jhotdraw.undo.CompositeEdit edit;

    public TrackingHandle(org.jhotdraw.draw.figure.Figure owner, java.util.function.Supplier<java.awt.geom.Point2D.Double> readLocation, java.util.function.Consumer<java.awt.geom.Point2D.Double> writeLocation) {
        this(owner, readLocation, writeLocation, null, null);
    }

    public TrackingHandle(org.jhotdraw.draw.figure.Figure owner, java.util.function.Supplier<java.awt.geom.Point2D.Double> readLocation, java.util.function.Consumer<java.awt.geom.Point2D.Double> writeLocation, java.lang.Runnable deleteLocation, java.lang.Runnable insertLocation) {
        super(owner);
        this.readLocation = java.util.Objects.requireNonNull(readLocation);
        this.writeLocation = java.util.Objects.requireNonNull(writeLocation);
        this.deleteLocation = deleteLocation;
        this.insertLocation = insertLocation;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_STROKE_COLOR));
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        java.awt.geom.Point2D.Double location = readLocation.get();
        if (location != null) {
            if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(location, location);
            }
            return location;
        } else {
            return null;
        }
    }

    private java.awt.geom.Point2D.Double oldPoint = null;

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.draw.figure.Figure figure = getOwner();
        view.getDrawing().fireUndoableEditHappened(edit = new org.jhotdraw.undo.CompositeEdit("Punkt verschieben"));
        oldPoint = readLocation.get();
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.Figure figure = getOwner();
        figure.willChange();
        java.awt.geom.Point2D.Double p = (view.getConstrainer() == null) ? view.viewToDrawing(lead) : view.getConstrainer().constrainPoint(view.viewToDrawing(lead));
        if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        writeLocation.accept(p);
        figure.changed();
    }

    private void fireAreaInvalidated(java.awt.geom.Point2D.Double p) {
        java.awt.geom.Rectangle2D.Double dr = new java.awt.geom.Rectangle2D.Double(p.x, p.y, 0, 0);
        java.awt.Rectangle vr = view.drawingToView(dr);
        vr.grow(getHandlesize(), getHandlesize());
        fireAreaInvalidated(vr);
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        final org.jhotdraw.draw.figure.Figure f = getOwner();
        java.awt.geom.Point2D.Double newPoint = readLocation.get();
        view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.TrackingEdit(f, writeLocation, oldPoint, newPoint));
        view.getDrawing().fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        org.jhotdraw.draw.figure.Figure f = getOwner();
        oldPoint = readLocation.get();
        if ((evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE)) {
            java.awt.Rectangle invalidatedArea = getDrawingArea();
            f.willChange();
            deleteLocation.run();
            f.changed();
            fireHandleRequestRemove(invalidatedArea);
            fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    f.willChange();
                    deleteLocation.run();
                    f.changed();
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    super.undo();
                    f.willChange();
                    insertLocation.run();
                    f.changed();
                }
            });
            evt.consume();
            // At this point, the handle is no longer valid, and
            // handles at higher node indices have become invalid too.
            fireHandleRequestRemove(invalidatedArea);
        } else {
            java.awt.geom.Point2D.Double newPoint = null;
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    newPoint = new java.awt.geom.Point2D.Double(oldPoint.x, oldPoint.y - 1.0);
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    newPoint = new java.awt.geom.Point2D.Double(oldPoint.x, oldPoint.y + 1.0);
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    newPoint = new java.awt.geom.Point2D.Double(oldPoint.x - 1.0, oldPoint.y);
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    newPoint = new java.awt.geom.Point2D.Double(oldPoint.x + 1.0, oldPoint.y);
                    break;
            }
            if (newPoint != null) {
                f.willChange();
                writeLocation.accept(newPoint);
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.TrackingEdit(f, writeLocation, oldPoint, newPoint));
                evt.consume();
            }
        }
    }
}