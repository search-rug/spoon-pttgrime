/**
 *
 * @(#)AbstractRotateHandle.java <p>Copyright (c) 1996-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.Figure;
/**
 * This abstract class can be extended to implement a {@link Handle} which can rotate a {@link Figure}.
 */
public abstract class AbstractRotateHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private java.awt.Point location;

    private java.lang.Object restoreData;

    private java.awt.geom.AffineTransform transform;

    private java.awt.geom.Point2D.Double center;

    private double startTheta;

    private double startLength;

    public AbstractRotateHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle h) {
        return false;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels.getString("handle.rotate.toolTipText");
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if (getEditor().getTool().supportsHandleInteraction()) {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ROTATE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ROTATE_HANDLE_STROKE_COLOR));
        } else {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ROTATE_HANDLE_FILL_COLOR_DISABLED), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ROTATE_HANDLE_STROKE_COLOR_DISABLED));
        }
    }

    protected java.awt.geom.Rectangle2D.Double getTransformedBounds() {
        org.jhotdraw.draw.figure.Figure owner = getOwner();
        java.awt.geom.Rectangle2D.Double bounds = owner.getBounds();
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            java.awt.geom.Rectangle2D r = owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds).getBounds2D();
            bounds.x = r.getX();
            bounds.y = r.getY();
            bounds.width = r.getWidth();
            bounds.height = r.getHeight();
        }
        return bounds;
    }

    protected java.lang.Object getRestoreData() {
        return restoreData;
    }

    protected double getStartTheta() {
        return startTheta;
    }

    protected abstract java.awt.geom.Point2D.Double getCenter();

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        location = new java.awt.Point(anchor.x, anchor.y);
        restoreData = getOwner().getTransformRestoreData();
        transform = new java.awt.geom.AffineTransform();
        center = getCenter();
        java.awt.geom.Point2D.Double anchorPoint = view.viewToDrawing(anchor);
        startTheta = org.jhotdraw.geom.Geom.angle(center.x, center.y, anchorPoint.x, anchorPoint.y);
        startLength = org.jhotdraw.geom.Geom.length(center.x, center.y, anchorPoint.x, anchorPoint.y);
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        location = new java.awt.Point(lead.x, lead.y);
        java.awt.geom.Point2D.Double leadPoint = view.viewToDrawing(lead);
        double stepTheta = org.jhotdraw.geom.Geom.angle(center.x, center.y, leadPoint.x, leadPoint.y);
        double currentTheta = (view.getConstrainer() == null) ? stepTheta - startTheta : view.getConstrainer().constrainAngle(stepTheta - startTheta, getOwner());
        transform.setToIdentity();
        transform.translate(center.x, center.y);
        transform.rotate(currentTheta);
        transform.translate(-center.x, -center.y);
        getOwner().willChange();
        getOwner().restoreTransformTo(restoreData);
        if (getOwner() instanceof org.jhotdraw.draw.figure.Rotation rotateOwner) {
            rotateOwner.setRotation(currentTheta);
        } else {
            getOwner().transform(transform);
        }
        getOwner().changed();
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), restoreData, getOwner().getTransformRestoreData()));
        fireAreaInvalidated(getDrawingArea());
        location = null;
        invalidate();
        fireAreaInvalidated(getDrawingArea());
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        org.jhotdraw.draw.figure.Figure f = getOwner();
        center = getCenter();
        if (f.isTransformable()) {
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                case java.awt.event.KeyEvent.VK_LEFT :
                    tx.rotate(((-1.0) / 180.0) * java.lang.Math.PI, center.x, center.y);
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                case java.awt.event.KeyEvent.VK_RIGHT :
                    tx.rotate((1.0 / 180.0) * java.lang.Math.PI, center.x, center.y);
                    evt.consume();
                    break;
            }
            f.willChange();
            f.transform(tx);
            f.changed();
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
        }
    }
}