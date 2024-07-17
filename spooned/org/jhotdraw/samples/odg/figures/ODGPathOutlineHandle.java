/* @(#)ODGPathOutlineHandle.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * A non-interactive {@link org.jhotdraw.draw.handle.Handle} which draws the outline of a {@link ODGPathFigure} to make adjustments easier.
 */
public class ODGPathOutlineHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private static final java.awt.Color HANDLE_FILL_COLOR = new java.awt.Color(0xa8ff);

    private static final java.awt.Color HANDLE_STROKE_COLOR = java.awt.Color.WHITE;

    public ODGPathOutlineHandle(org.jhotdraw.samples.odg.figures.ODGPathFigure owner) {
        super(owner);
    }

    @java.lang.Override
    public org.jhotdraw.samples.odg.figures.ODGPathFigure getOwner() {
        return ((org.jhotdraw.samples.odg.figures.ODGPathFigure) (super.getOwner()));
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
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
    }

    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.Shape bounds = getOwner().getPath();
        if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            bounds = getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds);
        }
        bounds = view.getDrawingToViewTransform().createTransformedShape(bounds);
        g.setColor(org.jhotdraw.samples.odg.figures.ODGPathOutlineHandle.HANDLE_FILL_COLOR);
        g.draw(bounds);
    }
}