/* @(#)ODGRectRadiusHandle.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * A Handle to manipulate the radius of a round lead rectangle.
 */
public class ODGRectRadiusHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private static final int OFFSET = 6;

    private org.jhotdraw.geom.Dimension2DDouble originalArc2D;

    org.jhotdraw.undo.CompositeEdit edit;

    public ODGRectRadiusHandle(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawDiamond(g, java.awt.Color.yellow, java.awt.Color.black);
    }

    @java.lang.Override
    protected java.awt.Rectangle basicGetBounds() {
        java.awt.Rectangle r = new java.awt.Rectangle(locate());
        r.grow((getHandlesize() / 2) + 1, (getHandlesize() / 2) + 1);
        return r;
    }

    private java.awt.Point locate() {
        org.jhotdraw.samples.odg.figures.ODGRectFigure owner = ((org.jhotdraw.samples.odg.figures.ODGRectFigure) (getOwner()));
        java.awt.geom.Rectangle2D.Double r = owner.getBounds();
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(r.x + owner.getArcWidth(), r.y + owner.getArcHeight());
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
        }
        return view.drawingToView(p);
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.samples.odg.figures.ODGRectFigure odgRect = ((org.jhotdraw.samples.odg.figures.ODGRectFigure) (getOwner()));
        originalArc2D = odgRect.getArc();
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.samples.odg.figures.ODGRectFigure odgRect = ((org.jhotdraw.samples.odg.figures.ODGRectFigure) (getOwner()));
        odgRect.willChange();
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        if (odgRect.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                odgRect.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                org.jhotdraw.samples.odg.figures.ODGRectRadiusHandle.LOG.throwing(org.jhotdraw.samples.odg.figures.ODGRectRadiusHandle.class.getName(), "trackStep", ex);
            }
        }
        java.awt.geom.Rectangle2D.Double r = odgRect.getBounds();
        odgRect.setArc(p.x - r.x, p.y - r.y);
        odgRect.changed();
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.samples.odg.figures.ODGRectRadiusHandle.class.getName());

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        final org.jhotdraw.samples.odg.figures.ODGRectFigure odgRect = ((org.jhotdraw.samples.odg.figures.ODGRectFigure) (getOwner()));
        final org.jhotdraw.geom.Dimension2DDouble oldValue = originalArc2D;
        final org.jhotdraw.geom.Dimension2DDouble newValue = odgRect.getArc();
        view.getDrawing().fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");
                return labels.getString("arc");
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                odgRect.willChange();
                odgRect.setArc(oldValue);
                odgRect.changed();
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                odgRect.willChange();
                odgRect.setArc(newValue);
                odgRect.changed();
            }
        });
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("handle.roundRectangleRadius.toolTipText");
    }
}