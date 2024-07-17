/* @(#)OrientationHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A {@link Handle} to change the value of the figure attribute {@link org.jhotdraw.draw.AttributeKeys#ORIENTATION}.
 */
public class OrientationHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private java.awt.Rectangle centerBox;

    private org.jhotdraw.draw.AttributeKeys.Orientation oldValue;

    private org.jhotdraw.draw.AttributeKeys.Orientation newValue;

    public OrientationHandle(org.jhotdraw.draw.figure.TriangleFigure owner) {
        super(owner);
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle h) {
        return false;
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        org.jhotdraw.draw.figure.Figure owner = getOwner();
        java.awt.geom.Rectangle2D.Double r = owner.getBounds();
        java.awt.geom.Point2D.Double p;
        double offset = getHandlesize();
        switch (owner.attr().get(org.jhotdraw.draw.AttributeKeys.ORIENTATION)) {
            case NORTH :
            default :
                p = new java.awt.geom.Point2D.Double(r.x + (r.width / 2.0), r.y + offset);
                break;
            case NORTH_EAST :
                p = new java.awt.geom.Point2D.Double((r.x + r.width) - offset, r.y + offset);
                break;
            case EAST :
                p = new java.awt.geom.Point2D.Double((r.x + r.width) - offset, r.y + (r.height / 2.0));
                break;
            case SOUTH_EAST :
                p = new java.awt.geom.Point2D.Double((r.x + r.width) - offset, (r.y + r.height) - offset);
                break;
            case SOUTH :
                p = new java.awt.geom.Point2D.Double(r.x + (r.width / 2.0), (r.y + r.height) - offset);
                break;
            case SOUTH_WEST :
                p = new java.awt.geom.Point2D.Double(r.x + offset, (r.y + r.height) - offset);
                break;
            case WEST :
                p = new java.awt.geom.Point2D.Double(r.x + offset, r.y + (r.height / 2.0));
                break;
            case NORTH_WEST :
                p = new java.awt.geom.Point2D.Double(r.x + offset, r.y + offset);
                break;
        }
        return p;
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        oldValue = getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.ORIENTATION);
        centerBox = view.drawingToView(getOwner().getBounds());
        centerBox.grow(centerBox.width / (-3), centerBox.height / (-3));
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        java.awt.Rectangle leadRect = new java.awt.Rectangle(lead);
        switch (org.jhotdraw.geom.Geom.outcode(centerBox, leadRect)) {
            case org.jhotdraw.geom.Geom.OUT_TOP :
            default :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.NORTH;
                break;
            case org.jhotdraw.geom.Geom.OUT_TOP | org.jhotdraw.geom.Geom.OUT_RIGHT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.NORTH_EAST;
                break;
            case org.jhotdraw.geom.Geom.OUT_RIGHT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.EAST;
                break;
            case org.jhotdraw.geom.Geom.OUT_BOTTOM | org.jhotdraw.geom.Geom.OUT_RIGHT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.SOUTH_EAST;
                break;
            case org.jhotdraw.geom.Geom.OUT_BOTTOM :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.SOUTH;
                break;
            case org.jhotdraw.geom.Geom.OUT_BOTTOM | org.jhotdraw.geom.Geom.OUT_LEFT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.SOUTH_WEST;
                break;
            case org.jhotdraw.geom.Geom.OUT_LEFT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.WEST;
                break;
            case org.jhotdraw.geom.Geom.OUT_TOP | org.jhotdraw.geom.Geom.OUT_LEFT :
                newValue = org.jhotdraw.draw.AttributeKeys.Orientation.NORTH_WEST;
                break;
        }
        getOwner().willChange();
        getOwner().attr().set(org.jhotdraw.draw.AttributeKeys.ORIENTATION, newValue);
        getOwner().changed();
        updateBounds();
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR));
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        if (newValue != oldValue) {
            fireUndoableEditHappened(new org.jhotdraw.draw.event.AttributeChangeEdit<>(getOwner(), org.jhotdraw.draw.AttributeKeys.ORIENTATION, oldValue, newValue));
        }
    }
}