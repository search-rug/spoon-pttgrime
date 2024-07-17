/* @(#)LinkHandle.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * The LinkHandle indicates when a figure has a link.
 */
public class LinkHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    public LinkHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
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
        org.jhotdraw.draw.figure.Figure o = getOwner();
        if ((o.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK) != null) && (o.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK).trim().length() > 0)) {
            g.setColor(getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.OVERFLOW_HANDLE_STROKE_COLOR));
            java.awt.Rectangle r = basicGetBounds();
            g.drawLine((r.x + (r.width / 2)) - 1, r.y, r.x, r.y);
            g.drawLine(r.x, r.y, r.x, (r.y + r.height) - 1);
            g.drawLine(r.x, (r.y + r.height) - 1, (r.x + (r.width / 2)) - 1, (r.y + r.height) - 1);
            g.drawLine(r.x + (r.width / 3), r.y + (r.height / 2), (r.x + r.width) - 1, r.y + (r.height / 2));
            g.drawLine((r.x + r.width) - 1, r.y + (r.height / 2), ((int) ((r.x + (r.width * 0.75)) - 1)), ((int) (r.y + (r.height * 0.25))));
            g.drawLine((r.x + r.width) - 1, r.y + (r.height / 2), ((int) ((r.x + (r.width * 0.75)) - 1)), ((int) (r.y + (r.height * 0.75))));
        }
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        org.jhotdraw.draw.figure.Figure o = getOwner();
        java.awt.geom.Rectangle2D.Double b = o.getBounds();
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(b.x + b.width, b.y + b.height);
        if (o.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            o.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
        }
        java.awt.Rectangle r = new java.awt.Rectangle(view.drawingToView(p));
        int h = getHandlesize();
        r.x -= h * 4;
        r.y -= h;
        r.width = h * 2;
        r.height = h;
        return r;
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return getOwner().attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK) != null ? org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels").getString("handle.link.toolTipText") : null;
    }
}