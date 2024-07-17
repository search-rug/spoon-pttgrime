/* @(#)GraphicalCompositeFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * The GraphicalCompositeFigure fills in the gap between a CompositeFigure and other figures which
 * mainly have a presentation purpose. The GraphicalCompositeFigure can be configured with any
 * Figure which takes over the task for rendering the graphical presentation for a CompositeFigure.
 * Therefore, the GraphicalCompositeFigure manages contained figures like a CompositeFigure does,
 * but delegates its graphical presentation to another (graphical) figure which purpose it is to
 * draw the container for all contained figures.
 *
 * <p>The GraphicalCompositeFigure adds to the {@link CompositeFigure} by containing a presentation
 * figure by default which can not be removed. Normally, the {@code CompositeFigure} can not be seen
 * without containing a figure because it has no mechanism to draw itself. It instead relies on its
 * contained figures to draw themselves thereby giving the {@code CompositeFigure} its appearance.
 * However, the <b>GraphicalCompositeFigure</b>'s presentation figure can draw itself even when the
 * <b>GraphicalCompositeFigure</b> contains no other figures. The <b>GraphicalCompositeFigure</b>
 * also uses a {@link org.jhotdraw.draw.layouter.Layouter} to lay out its child figures.
 *
 * @author Wolfram Kaiser (original code), Werner Randelshofer (this derived version)
 * @version $Id$
 */
public class GraphicalCompositeFigure extends org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure {
    private static final long serialVersionUID = 1L;

    /**
     * Figure which performs all presentation tasks for this BasicCompositeFigure as CompositeFigures
     * usually don't have an own presentation but present only the sum of all its children.
     */
    private org.jhotdraw.draw.figure.Figure presentationFigure;

    /**
     * Handles figure changes in the children.
     */
    private org.jhotdraw.draw.figure.GraphicalCompositeFigure.PresentationFigureHandler presentationFigureHandler = new org.jhotdraw.draw.figure.GraphicalCompositeFigure.PresentationFigureHandler(this);

    private static class PresentationFigureHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements javax.swing.event.UndoableEditListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private org.jhotdraw.draw.figure.GraphicalCompositeFigure owner;

        private PresentationFigureHandler(org.jhotdraw.draw.figure.GraphicalCompositeFigure owner) {
            this.owner = owner;
        }

        @java.lang.Override
        public void figureRequestRemove(org.jhotdraw.draw.event.FigureEvent e) {
            owner.remove(e.getFigure());
        }

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
            if (!owner.isChanging()) {
                owner.willChange();
                owner.fireFigureChanged(e);
                owner.changed();
            }
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent e) {
            if (!owner.isChanging()) {
                owner.fireAreaInvalidated(e.getInvalidatedArea());
            }
        }

        @java.lang.Override
        public void undoableEditHappened(javax.swing.event.UndoableEditEvent e) {
            owner.fireUndoableEditHappened(e.getEdit());
        }
    }

    /**
     * Default constructor which uses nothing as presentation figure. This constructor is needed by
     * the Storable mechanism.
     */
    public GraphicalCompositeFigure() {
        this(null);
    }

    /**
     * Constructor which creates a GraphicalCompositeFigure with a given graphical figure for
     * presenting it.
     *
     * @param newPresentationFigure
     * 		figure which renders the container
     */
    public GraphicalCompositeFigure(org.jhotdraw.draw.figure.Figure newPresentationFigure) {
        super();
        setPresentationFigure(newPresentationFigure);
        initAttributeDependentSupplier();
    }

    private void initAttributeDependentSupplier() {
        attr().dependents(org.jhotdraw.draw.figure.Attributes.attrSupplier(() -> {
            var list = new java.util.ArrayList<>(this.getChildren());
            list.add(getPresentationFigure());
            return list;
        }));
    }

    /**
     * Return the logcal display area. This method is delegated to the encapsulated presentation
     * figure.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        if (getPresentationFigure() == null) {
            return super.getBounds(scale);
        }
        return getPresentationFigure().getBounds(scale);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scale) {
        boolean contains = super.contains(p, scale);
        if ((!contains) && (getPresentationFigure() != null)) {
            contains = getPresentationFigure().contains(p, scale);
        }
        return contains;
    }

    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.Drawing drawing) {
        super.addNotify(drawing);
        if (getPresentationFigure() != null) {
            getPresentationFigure().addNotify(drawing);
        }
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing drawing) {
        super.removeNotify(drawing);
        if (getPresentationFigure() != null) {
            getPresentationFigure().removeNotify(drawing);
        }
    }

    /**
     * Return the draw area. This method is delegated to the encapsulated presentation figure.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = super.getDrawingArea(scale);
        if (getPresentationFigure() != null) {
            r.add(getPresentationFigure().getDrawingArea(scale));
        }
        return r;
    }

    /**
     * Moves the figure. This is the method that subclassers override. Clients usually call
     * displayBox.
     */
    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        super.setBounds(anchor, lead);
        basicSetPresentationFigureBounds(anchor, lead);
        layout(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    @java.lang.Override
    public void layout(double scale) {
        if (getLayouter() != null) {
            java.awt.geom.Rectangle2D.Double bounds = getBounds(scale);
            java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(bounds.x, bounds.y);
            getLayouter().layout(this, p, p, scale);
            invalidate();
        }
    }

    protected void superBasicSetBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        super.setBounds(anchor, lead);
    }

    protected void basicSetPresentationFigureBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        if (getPresentationFigure() != null) {
            getPresentationFigure().setBounds(anchor, lead);
        }
    }

    /**
     * Standard presentation method which is delegated to the encapsulated presentation figure. The
     * presentation figure is moved as well as all contained figures.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if (getPresentationFigure() != null) {
            getPresentationFigure().transform(tx);
        }
        super.transform(tx);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawPresentationFigure(g);
        super.draw(g);
    }

    protected void drawPresentationFigure(java.awt.Graphics2D g) {
        if (getPresentationFigure() != null) {
            getPresentationFigure().draw(g);
        }
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        if (detailLevel == 0) {
            org.jhotdraw.draw.handle.MoveHandle.addMoveHandles(this, handles);
        }
        return handles;
    }

    /**
     * Set a figure which renders this BasicCompositeFigure. The presentation tasks for the
     * BasicCompositeFigure are delegated to this presentation figure.
     *
     * @param newPresentationFigure
     * 		figure takes over the presentation tasks
     */
    public void setPresentationFigure(org.jhotdraw.draw.figure.Figure newPresentationFigure) {
        if (this.presentationFigure != null) {
            this.presentationFigure.removeFigureListener(presentationFigureHandler);
            if (getDrawing() != null) {
                this.presentationFigure.removeNotify(getDrawing());
            }
        }
        this.presentationFigure = newPresentationFigure;
        if (this.presentationFigure != null) {
            this.presentationFigure.addFigureListener(presentationFigureHandler);
            if (getDrawing() != null) {
                this.presentationFigure.addNotify(getDrawing());
            }
        }
    }

    /**
     * Get a figure which renders this BasicCompositeFigure. The presentation tasks for the
     * BasicCompositeFigure are delegated to this presentation figure.
     *
     * @return figure takes over the presentation tasks
     */
    public org.jhotdraw.draw.figure.Figure getPresentationFigure() {
        return presentationFigure;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public org.jhotdraw.draw.figure.GraphicalCompositeFigure clone() {
        org.jhotdraw.draw.figure.GraphicalCompositeFigure that = ((org.jhotdraw.draw.figure.GraphicalCompositeFigure) (super.clone()));
        that.initAttributeDependentSupplier();
        that.presentationFigure = (this.presentationFigure == null) ? null : this.presentationFigure.clone();
        if (that.presentationFigure != null) {
            that.presentationFigure.removeFigureListener(this.presentationFigureHandler);
            that.presentationFigureHandler = new org.jhotdraw.draw.figure.GraphicalCompositeFigure.PresentationFigureHandler(that);
            that.presentationFigure.addFigureListener(that.presentationFigureHandler);
        }
        return that;
    }

    public void remap(java.util.HashMap<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> oldToNew, boolean disconnectIfNotInMap) {
        super.remap(oldToNew, disconnectIfNotInMap);
        if (presentationFigure != null) {
            presentationFigure.remap(oldToNew, disconnectIfNotInMap);
        }
    }

    /**
     * This is a default implementation that chops the point at the rectangle returned by getBounds()
     * of the figure.
     *
     * <p>Figures which have a non-rectangular shape need to override this method.
     *
     * <p>This method takes the following attributes into account: AttributeKeys.STROKE_COLOR,
     * AttributeKeys.STROKE_PLACEMENT, and AttributeKeys.StrokeTotalWidth.
     */
    public java.awt.geom.Point2D.Double chop(java.awt.geom.Point2D.Double from) {
        java.awt.geom.Rectangle2D.Double r = getBounds();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) != null) {
            double grow;
            switch (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT)) {
                case CENTER :
                default :
                    grow = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
                    break;
                case OUTSIDE :
                    grow = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
                    break;
                case INSIDE :
                    grow = 0.0;
                    break;
            }
            org.jhotdraw.geom.Geom.grow(r, grow, grow);
        }
        return org.jhotdraw.geom.Geom.angleToPoint(r, org.jhotdraw.geom.Geom.pointToAngle(r, from));
    }

    @java.lang.Override
    public void changed() {
        if (presentationFigure != null)
            presentationFigure.changed();

        super.changed();
    }

    @java.lang.Override
    public void willChange() {
        super.willChange();
        if (presentationFigure != null)
            presentationFigure.willChange();

    }
}