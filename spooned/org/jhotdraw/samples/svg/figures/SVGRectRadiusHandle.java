/* @(#)SVGRectRadiusHandle.java

Copyright (c) 2006-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * A Handle to manipulate the radius of a round lead rectangle.
 */
public class SVGRectRadiusHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private static final int OFFSET = 6;

    private org.jhotdraw.geom.Dimension2DDouble originalArc2D;

    public SVGRectRadiusHandle(org.jhotdraw.draw.figure.Figure owner) {
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

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        java.awt.Rectangle r = new java.awt.Rectangle(locate());
        r.grow((getHandlesize() / 2) + 1, (getHandlesize() / 2) + 1);
        return r;
    }

    private java.awt.Point locate() {
        org.jhotdraw.samples.svg.figures.SVGRectFigure owner = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (getOwner()));
        java.awt.geom.Rectangle2D.Double r = owner.getBounds();
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(r.x + owner.getArcWidth(), r.y + owner.getArcHeight());
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
        }
        return view.drawingToView(p);
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.samples.svg.figures.SVGRectFigure svgRect = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (getOwner()));
        originalArc2D = new org.jhotdraw.geom.Dimension2DDouble(svgRect.getArcWidth(), svgRect.getArcHeight());
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.samples.svg.figures.SVGRectFigure owner = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (getOwner()));
        owner.willChange();
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                org.jhotdraw.samples.svg.figures.SVGRectRadiusHandle.LOG.throwing(org.jhotdraw.samples.svg.figures.SVGRectRadiusHandle.class.getName(), "trackStep", ex);
            }
        }
        java.awt.geom.Rectangle2D.Double r = owner.getBounds();
        owner.setArc(java.lang.Math.min(owner.getWidth(), java.lang.Math.max(0, p.x - r.x)), java.lang.Math.min(owner.getHeight(), java.lang.Math.max(0, p.y - r.y)));
        owner.changed();
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.figures.SVGRectRadiusHandle.class.getName());

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        final org.jhotdraw.samples.svg.figures.SVGRectFigure svgRect = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (getOwner()));
        final org.jhotdraw.geom.Dimension2DDouble oldValue = originalArc2D;
        final org.jhotdraw.geom.Dimension2DDouble newValue = new org.jhotdraw.geom.Dimension2DDouble(svgRect.getArcWidth(), svgRect.getArcHeight());
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.draw.event.CompositeFigureEdit edit = new org.jhotdraw.draw.event.CompositeFigureEdit(svgRect, labels.getString("attribute.roundRectRadius"));
        edit.setVerbose(true);
        fireUndoableEditHappened(edit);
        fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(svgRect, org.jhotdraw.samples.svg.figures.SVGRectFigure.ARC_WIDTH_PROPERTY, oldValue.width, newValue.width));
        fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(svgRect, org.jhotdraw.samples.svg.figures.SVGRectFigure.ARC_HEIGHT_PROPERTY, oldValue.height, newValue.height));
        fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        org.jhotdraw.samples.svg.figures.SVGRectFigure owner = ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (getOwner()));
        org.jhotdraw.geom.Dimension2DDouble oldArc = new org.jhotdraw.geom.Dimension2DDouble(owner.getArcWidth(), owner.getArcHeight());
        org.jhotdraw.geom.Dimension2DDouble newArc = new org.jhotdraw.geom.Dimension2DDouble(owner.getArcWidth(), owner.getArcHeight());
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP :
                if (newArc.height > 0) {
                    newArc.height = java.lang.Math.max(0, newArc.height - 1);
                }
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DOWN :
                newArc.height = java.lang.Math.min(owner.getHeight(), newArc.height + 1);
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_LEFT :
                if (newArc.width > 0) {
                    newArc.width = java.lang.Math.max(0, newArc.width - 1);
                }
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_RIGHT :
                newArc.width = java.lang.Math.min(owner.getWidth(), newArc.width + 1);
                evt.consume();
                break;
        }
        if (!newArc.equals(oldArc)) {
            owner.willChange();
            owner.setArc(newArc.width, newArc.height);
            owner.changed();
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            org.jhotdraw.draw.event.CompositeFigureEdit edit = new org.jhotdraw.draw.event.CompositeFigureEdit(owner, labels.getString("attribute.roundRectRadius"));
            fireUndoableEditHappened(edit);
            fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.samples.svg.figures.SVGRectFigure.ARC_WIDTH_PROPERTY, oldArc.width, newArc.width));
            fireUndoableEditHappened(new org.jhotdraw.undo.PropertyChangeEdit(owner, org.jhotdraw.samples.svg.figures.SVGRectFigure.ARC_HEIGHT_PROPERTY, oldArc.height, newArc.height));
            fireUndoableEditHappened(edit);
        }
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("handle.roundRectangleRadius.toolTipText");
    }
}