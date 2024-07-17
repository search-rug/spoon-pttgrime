/* @(#)RoundRectRadiusHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.RoundRectangleFigure;
/**
 * A {@link Handle} to manipulate the corner radius of a {@link RoundRectangleFigure}.
 */
public class RoundRectangleRadiusHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private static final int OFFSET = 6;

    private java.awt.Point originalArc;

    public RoundRectangleRadiusHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if (getEditor().getTool().supportsHandleInteraction()) {
            drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR));
        } else {
            drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR_DISABLED), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR_DISABLED));
        }
    }

    private java.awt.Point locate() {
        org.jhotdraw.draw.figure.RoundRectangleFigure owner = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (getOwner()));
        java.awt.Rectangle r = view.drawingToView(owner.getBounds());
        java.awt.Point arc = view.drawingToView(new java.awt.geom.Point2D.Double(owner.getArcWidth(), owner.getArcHeight()));
        return new java.awt.Point((r.x + (arc.x / 2)) + org.jhotdraw.draw.handle.RoundRectangleRadiusHandle.OFFSET, (r.y + (arc.y / 2)) + org.jhotdraw.draw.handle.RoundRectangleRadiusHandle.OFFSET);
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.draw.figure.RoundRectangleFigure owner = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (getOwner()));
        originalArc = view.drawingToView(new java.awt.geom.Point2D.Double(owner.getArcWidth(), owner.getArcHeight()));
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        int dx = lead.x - anchor.x;
        int dy = lead.y - anchor.y;
        org.jhotdraw.draw.figure.RoundRectangleFigure owner = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (getOwner()));
        java.awt.Rectangle r = view.drawingToView(owner.getBounds());
        java.awt.Point viewArc = new java.awt.Point(org.jhotdraw.geom.Geom.range(0, r.width, 2 * ((originalArc.x / 2) + dx)), org.jhotdraw.geom.Geom.range(0, r.height, 2 * ((originalArc.y / 2) + dy)));
        java.awt.geom.Point2D.Double arc = view.viewToDrawing(viewArc);
        owner.willChange();
        owner.setArc(arc.x, arc.y);
        owner.changed();
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        int dx = lead.x - anchor.x;
        int dy = lead.y - anchor.y;
        org.jhotdraw.draw.figure.RoundRectangleFigure owner = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (getOwner()));
        java.awt.Rectangle r = view.drawingToView(owner.getBounds());
        java.awt.Point viewArc = new java.awt.Point(org.jhotdraw.geom.Geom.range(0, r.width, 2 * ((originalArc.x / 2) + dx)), org.jhotdraw.geom.Geom.range(0, r.height, 2 * ((originalArc.y / 2) + dy)));
        java.awt.geom.Point2D.Double oldArc = view.viewToDrawing(originalArc);
        java.awt.geom.Point2D.Double newArc = view.viewToDrawing(viewArc);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.draw.event.CompositeFigureEdit edit = new org.jhotdraw.draw.event.CompositeFigureEdit(owner, labels.getString("attribute.roundRectRadius"));
        fireUndoableEditHappened(edit);
        fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.draw.figure.RoundRectangleFigure.ARC_WIDTH_PROPERTY, oldArc.x, newArc.x));
        fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.draw.figure.RoundRectangleFigure.ARC_HEIGHT_PROPERTY, oldArc.y, newArc.y));
        fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        org.jhotdraw.draw.figure.RoundRectangleFigure owner = ((org.jhotdraw.draw.figure.RoundRectangleFigure) (getOwner()));
        java.awt.geom.Point2D.Double oldArc = new java.awt.geom.Point2D.Double(owner.getArcWidth(), owner.getArcHeight());
        java.awt.geom.Point2D.Double newArc = new java.awt.geom.Point2D.Double(owner.getArcWidth(), owner.getArcHeight());
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP :
                if (newArc.y > 0) {
                    newArc.y = java.lang.Math.max(0, newArc.y - 1);
                }
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DOWN :
                newArc.y += 1;
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_LEFT :
                if (newArc.x > 0) {
                    newArc.x = java.lang.Math.max(0, newArc.x - 1);
                }
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_RIGHT :
                newArc.x += 1;
                evt.consume();
                break;
        }
        if (!newArc.equals(oldArc)) {
            owner.willChange();
            owner.setArcWidth(newArc.x);
            owner.setArcHeight(newArc.y);
            owner.changed();
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            org.jhotdraw.draw.event.CompositeFigureEdit edit = new org.jhotdraw.draw.event.CompositeFigureEdit(owner, labels.getString("attribute.roundRectRadius"));
            fireUndoableEditHappened(edit);
            fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.draw.figure.RoundRectangleFigure.ARC_WIDTH_PROPERTY, oldArc.x, newArc.x));
            fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.draw.figure.RoundRectangleFigure.ARC_HEIGHT_PROPERTY, oldArc.y, newArc.y));
            fireUndoableEditHappened(edit);
        }
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("handle.roundRectangleRadius.toolTipText");
    }
}