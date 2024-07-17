/* @(#)BezierOutlineHandle.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.BezierFigure;
/**
 * A non-interactive {@link Handle} which draws the outline of a {@link BezierFigure} to make
 * adjustments easier.
 */
public class BezierOutlineHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    /**
     * Set this to true, if the handle is used for marking a figure over which the mouse pointer is
     * hovering.
     */
    private boolean isHoverHandle = false;

    public BezierOutlineHandle(org.jhotdraw.draw.figure.BezierFigure owner) {
        this(owner, false);
    }

    public BezierOutlineHandle(org.jhotdraw.draw.figure.BezierFigure owner, boolean isHoverHandle) {
        super(owner);
        this.isHoverHandle = isHoverHandle;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.BezierFigure getOwner() {
        return ((org.jhotdraw.draw.figure.BezierFigure) (super.getOwner()));
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        return view.drawingToView(getOwner().getDrawingArea());
    }

    @java.lang.Override
    public boolean contains(java.awt.Point p) {
        return false;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        org.jhotdraw.draw.figure.BezierFigure owner = getOwner();
        java.awt.Shape bounds = owner.getBezierPath();
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            bounds = owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds);
        }
        bounds = view.getDrawingToViewTransform().createTransformedShape(bounds);
        java.awt.Stroke stroke1;
        java.awt.Color strokeColor1;
        java.awt.Stroke stroke2;
        java.awt.Color strokeColor2;
        if (getEditor().getTool().supportsHandleInteraction()) {
            if (isHoverHandle) {
                stroke1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_1_HOVER);
                strokeColor1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_1_HOVER);
                stroke2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_2_HOVER);
                strokeColor2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_2_HOVER);
            } else {
                stroke1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_1);
                strokeColor1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_1);
                stroke2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_2);
                strokeColor2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_2);
            }
        } else {
            stroke1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_1_DISABLED);
            strokeColor1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_1_DISABLED);
            stroke2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_STROKE_2_DISABLED);
            strokeColor2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_PATH_COLOR_2_DISABLED);
        }
        if ((stroke1 != null) && (strokeColor1 != null)) {
            g.setStroke(stroke1);
            g.setColor(strokeColor1);
            g.draw(bounds);
        }
        if ((stroke2 != null) && (strokeColor2 != null)) {
            g.setStroke(stroke2);
            g.setColor(strokeColor2);
            g.draw(bounds);
        }
    }
}