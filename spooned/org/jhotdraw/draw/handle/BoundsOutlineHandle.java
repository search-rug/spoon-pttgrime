/* @(#)BoundsOutlineHandle.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.Figure;
/**
 * A non-interactive {@link Handle} which draws the bounds of a {@link Figure} to make adjustments
 * easier.
 */
public class BoundsOutlineHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke1Enabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke2Enabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke1Disabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke2Disabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor1Enabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor2Enabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor1Disabled;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor2Disabled;

    /**
     * Creates a bounds outline handle for resizing a component.
     *
     * @param owner
     */
    public BoundsOutlineHandle(org.jhotdraw.draw.figure.Figure owner) {
        this(owner, false, false);
    }

    /**
     * Creates a bounds outline handle for resizing or transforming a component.
     *
     * @param owner
     */
    public BoundsOutlineHandle(org.jhotdraw.draw.figure.Figure owner, boolean isTransformHandle, boolean isHoverHandle) {
        super(owner);
        if (isTransformHandle) {
            if (isHoverHandle) {
                stroke1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_1_HOVER;
                strokeColor1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_1_HOVER;
                stroke2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_2_HOVER;
                strokeColor2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_2_HOVER;
            } else {
                stroke1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_1;
                strokeColor1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_1;
                stroke2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_2;
                strokeColor2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_2;
            }
            stroke1Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_1_DISABLED;
            strokeColor1Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_1_DISABLED;
            stroke2Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_STROKE_2_DISABLED;
            strokeColor2Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_BOUNDS_COLOR_2_DISABLED;
        } else {
            if (isHoverHandle) {
                stroke1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_1_HOVER;
                strokeColor1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_1_HOVER;
                stroke2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_2_HOVER;
                strokeColor2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_2_HOVER;
            } else {
                stroke1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_1;
                strokeColor1Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_1;
                stroke2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_2;
                strokeColor2Enabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_2;
            }
            stroke1Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_1_DISABLED;
            strokeColor1Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_1_DISABLED;
            stroke2Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_STROKE_2_DISABLED;
            strokeColor2Disabled = org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_BOUNDS_COLOR_2_DISABLED;
        }
    }

    /**
     * Creates a bounds outline handle for resizing or transforming a component.
     *
     * @param owner
     */
    public BoundsOutlineHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke1Enabled, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor1Enabled, org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke2Enabled, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor2Enabled, org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke1Disabled, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor1Disabled, org.jhotdraw.draw.AttributeKey<java.awt.Stroke> stroke2Disabled, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColor2Disabled) {
        super(owner);
        this.stroke1Enabled = stroke1Enabled;
        this.strokeColor1Enabled = strokeColor1Enabled;
        this.stroke2Enabled = stroke2Enabled;
        this.strokeColor2Enabled = strokeColor2Enabled;
        this.stroke1Disabled = stroke1Disabled;
        this.strokeColor1Disabled = strokeColor1Disabled;
        this.stroke2Disabled = stroke2Disabled;
        this.strokeColor2Disabled = strokeColor2Disabled;
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        java.awt.Shape bounds = getOwner().getBounds(view.getScaleFactor());
        if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            bounds = getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds);
        }
        bounds = view.getDrawingToViewTransform().createTransformedShape(bounds);
        java.awt.Rectangle r = bounds.getBounds();
        r.grow(2, 2);
        return r;
    }

    @java.lang.Override
    public boolean contains(java.awt.Point p) {
        return false;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.Shape bounds = getOwner().getBounds(view.getScaleFactor());
        if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            bounds = getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds);
        }
        bounds = view.getDrawingToViewTransform().createTransformedShape(bounds);
        java.awt.Stroke stroke1;
        java.awt.Color strokeColor1;
        java.awt.Stroke stroke2;
        java.awt.Color strokeColor2;
        if (getEditor().getTool().supportsHandleInteraction()) {
            stroke1 = getEditor().getHandleAttribute(stroke1Enabled);
            strokeColor1 = getEditor().getHandleAttribute(strokeColor1Enabled);
            stroke2 = getEditor().getHandleAttribute(stroke2Enabled);
            strokeColor2 = getEditor().getHandleAttribute(strokeColor2Enabled);
        } else {
            stroke1 = getEditor().getHandleAttribute(stroke1Disabled);
            strokeColor1 = getEditor().getHandleAttribute(strokeColor1Disabled);
            stroke2 = getEditor().getHandleAttribute(stroke2Disabled);
            strokeColor2 = getEditor().getHandleAttribute(strokeColor2Disabled);
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