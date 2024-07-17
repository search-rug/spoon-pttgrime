/* @(#)TextOverflowHandle.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * The TextOverflowHandle indicates when the text does not fit into the bounds of a TextAreaFigure.
 *
 * @author Werner Randelshofer
 * @version $Id: TextOverflowHandle.java -1 $
 */
public class TextOverflowHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    public TextOverflowHandle(org.jhotdraw.draw.figure.TextHolderFigure owner) {
        super(owner);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextHolderFigure getOwner() {
        return ((org.jhotdraw.draw.figure.TextHolderFigure) (super.getOwner()));
    }

    @java.lang.Override
    public boolean contains(java.awt.Point p) {
        return false;
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if (getOwner().isTextOverflow()) {
            drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.OVERFLOW_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.OVERFLOW_HANDLE_STROKE_COLOR));
            g.setColor(getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.OVERFLOW_HANDLE_STROKE_COLOR));
            java.awt.Rectangle r = basicGetBounds();
            g.drawLine(r.x + 1, r.y + 1, (r.x + r.width) - 2, (r.y + r.height) - 2);
            g.drawLine((r.x + r.width) - 2, r.y + 1, r.x + 1, (r.y + r.height) - 2);
        }
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        java.awt.geom.Rectangle2D.Double b = getOwner().getBounds();
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(b.x + b.width, b.y + b.height);
        org.jhotdraw.draw.figure.Figure o = getOwner();
        if (o.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            o.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
        }
        java.awt.Rectangle r = new java.awt.Rectangle(view.drawingToView(p));
        int h = getHandlesize();
        r.x -= h;
        r.y -= h;
        r.width = r.height = h;
        return r;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return getOwner().isTextOverflow() ? org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("handle.textOverflow.toolTipText") : null;
    }
}