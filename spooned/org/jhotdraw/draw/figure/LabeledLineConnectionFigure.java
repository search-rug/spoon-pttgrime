/* @(#)LabeledLineConnection.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A LineConnection with labels.
 *
 * <p>Usage:
 *
 * <pre>
 * LineConnectionFigure lcf = new LineConnectionFigure();
 * lcf.setLayouter(new LocatorLayouter());
 * TextFigure label = new TextFigure();
 * label.setText("Hello");
 * LocatorLayouter.LAYOUT_LOCATOR.set(label, new BezierLabelLocator(0, -Math.PI / 4, 8));
 * lcf.add(label);
 * </pre>
 */
public class LabeledLineConnectionFigure extends org.jhotdraw.draw.figure.LineConnectionFigure implements org.jhotdraw.draw.figure.CompositeFigure {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.layouter.Layouter layouter;

    private java.util.List<org.jhotdraw.draw.figure.Figure> children = new java.util.ArrayList<>();

    private transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    /**
     * Handles figure changes in the children.
     */
    private org.jhotdraw.draw.figure.LabeledLineConnectionFigure.ChildHandler childHandler = new org.jhotdraw.draw.figure.LabeledLineConnectionFigure.ChildHandler(this);

    private static class ChildHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements javax.swing.event.UndoableEditListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private org.jhotdraw.draw.figure.LabeledLineConnectionFigure owner;

        private ChildHandler(org.jhotdraw.draw.figure.LabeledLineConnectionFigure owner) {
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

    public LabeledLineConnectionFigure() {
    }

    // DRAWING
    /**
     * Draw the figure. This method is delegated to the encapsulated presentation figure.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        super.draw(g);
        for (org.jhotdraw.draw.figure.Figure child : children) {
            if (child.isVisible()) {
                child.draw(g);
            }
        }
    }

    // SHAPE AND BOUNDS
    /**
     * Transforms the figure.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        super.transform(tx);
        for (org.jhotdraw.draw.figure.Figure f : children) {
            f.transform(tx);
        }
        invalidate();
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            cachedDrawingArea = super.getDrawingArea(scale);
            for (org.jhotdraw.draw.figure.Figure child : getChildrenFrontToBack()) {
                if (child.isVisible()) {
                    java.awt.geom.Rectangle2D.Double childBounds = child.getDrawingArea(scale);
                    if (!childBounds.isEmpty()) {
                        cachedDrawingArea.add(childBounds);
                    }
                }
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p) {
        if (getDrawingArea().contains(p)) {
            for (org.jhotdraw.draw.figure.Figure child : getChildrenFrontToBack()) {
                if (child.isVisible() && child.contains(p)) {
                    return true;
                }
            }
            return super.contains(p);
        }
        return false;
    }

    // EDITING
    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureInside(java.awt.geom.Point2D.Double p) {
        if (getDrawingArea().contains(p)) {
            org.jhotdraw.draw.figure.Figure found = null;
            for (org.jhotdraw.draw.figure.Figure child : getChildrenFrontToBack()) {
                if (child.isVisible()) {
                    found = child.findFigureInside(p);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }

    // CONNECTING
    @java.lang.Override
    public void updateConnection() {
        super.updateConnection();
        layout();
    }

    // COMPOSITE FIGURES
    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildren() {
        return java.util.Collections.unmodifiableList(children);
    }

    @java.lang.Override
    public int getChildCount() {
        return children.size();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getChild(int index) {
        return children.get(index);
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the children.
     */
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildrenFrontToBack() {
        return children == null ? new java.util.ArrayList<>() : new org.jhotdraw.util.ReversedList<>(children);
    }

    @java.lang.Override
    public boolean add(org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(figure);
        if (getDrawing() != null) {
            figure.addNotify(getDrawing());
        }
        return true;
    }

    @java.lang.Override
    public void add(int index, org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(index, figure);
        if (getDrawing() != null) {
            figure.addNotify(getDrawing());
        }
    }

    @java.lang.Override
    public void basicAdd(org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(children.size(), figure);
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        children.add(index, figure);
        figure.addFigureListener(childHandler);
        invalidate();
    }

    @java.lang.Override
    public boolean remove(final org.jhotdraw.draw.figure.Figure figure) {
        int index = children.indexOf(figure);
        if (index == (-1)) {
            return false;
        } else {
            willChange();
            basicRemoveChild(index);
            if (getDrawing() != null) {
                figure.removeNotify(getDrawing());
            }
            changed();
            return true;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure removeChild(int index) {
        willChange();
        org.jhotdraw.draw.figure.Figure figure = basicRemoveChild(index);
        if (getDrawing() != null) {
            figure.removeNotify(getDrawing());
        }
        changed();
        return figure;
    }

    @java.lang.Override
    public int basicRemove(final org.jhotdraw.draw.figure.Figure figure) {
        int index = children.indexOf(figure);
        if (index != (-1)) {
            basicRemoveChild(index);
        }
        return index;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure basicRemoveChild(int index) {
        org.jhotdraw.draw.figure.Figure figure = children.remove(index);
        figure.removeFigureListener(childHandler);
        return figure;
    }

    @java.lang.Override
    public void removeAllChildren() {
        willChange();
        while (children.size() > 0) {
            org.jhotdraw.draw.figure.Figure figure = basicRemoveChild(children.size() - 1);
            if (getDrawing() != null) {
                figure.removeNotify(getDrawing());
            }
        } 
        changed();
    }

    @java.lang.Override
    public void basicRemoveAllChildren() {
        while (children.size() > 0) {
            basicRemoveChild(children.size() - 1);
        } 
    }

    // LAYOUT
    /**
     * Get a Layouter object which encapsulated a layout algorithm for this figure. Typically, a
     * Layouter accesses the child components of this figure and arranges their graphical
     * presentation.
     *
     * @return layout strategy used by this figure
     */
    @java.lang.Override
    public org.jhotdraw.draw.layouter.Layouter getLayouter() {
        return layouter;
    }

    @java.lang.Override
    public void setLayouter(org.jhotdraw.draw.layouter.Layouter newLayouter) {
        this.layouter = newLayouter;
    }

    /**
     * A layout algorithm is used to define how the child components should be laid out in relation to
     * each other. The task for layouting the child components for presentation is delegated to a
     * Layouter which can be plugged in at runtime.
     */
    @java.lang.Override
    public void layout(double scale) {
        if (getLayouter() != null) {
            java.awt.geom.Rectangle2D.Double bounds = getBounds(scale);
            java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(bounds.x, bounds.y);
            getLayouter().layout(this, p, p, scale);
            invalidate();
        }
    }

    // EVENT HANDLING
    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedDrawingArea = null;
    }

    @java.lang.Override
    public void validate() {
        super.validate();
        layout();
    }

    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.Drawing drawing) {
        for (org.jhotdraw.draw.figure.Figure child : children) {
            child.addNotify(drawing);
        }
        super.addNotify(drawing);
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing drawing) {
        for (org.jhotdraw.draw.figure.Figure child : children) {
            child.removeNotify(drawing);
        }
        super.removeNotify(drawing);
    }

    @java.lang.Override
    public void removeCompositeFigureListener(org.jhotdraw.draw.event.CompositeFigureListener listener) {
        listenerList.remove(org.jhotdraw.draw.event.CompositeFigureListener.class, listener);
    }

    @java.lang.Override
    public void addCompositeFigureListener(org.jhotdraw.draw.event.CompositeFigureListener listener) {
        listenerList.add(org.jhotdraw.draw.event.CompositeFigureListener.class, listener);
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureAdded(org.jhotdraw.draw.figure.Figure f, int zIndex) {
        org.jhotdraw.draw.event.CompositeFigureEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.CompositeFigureListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.CompositeFigureEvent(this, f, f.getDrawingArea(), zIndex);
                }
                ((org.jhotdraw.draw.event.CompositeFigureListener) (listeners[i + 1])).figureAdded(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureRemoved(org.jhotdraw.draw.figure.Figure f, int zIndex) {
        org.jhotdraw.draw.event.CompositeFigureEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.CompositeFigureListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.CompositeFigureEvent(this, f, f.getDrawingArea(), zIndex);
                }
                ((org.jhotdraw.draw.event.CompositeFigureListener) (listeners[i + 1])).figureRemoved(event);
            }
        }
    }

    // CLONING
    @java.lang.Override
    public org.jhotdraw.draw.figure.LabeledLineConnectionFigure clone() {
        org.jhotdraw.draw.figure.LabeledLineConnectionFigure that = ((org.jhotdraw.draw.figure.LabeledLineConnectionFigure) (super.clone()));
        that.childHandler = new org.jhotdraw.draw.figure.LabeledLineConnectionFigure.ChildHandler(that);
        that.children = new java.util.ArrayList<>();
        for (org.jhotdraw.draw.figure.Figure thisChild : this.children) {
            org.jhotdraw.draw.figure.Figure thatChild = thisChild.clone();
            that.children.add(thatChild);
            thatChild.addFigureListener(that.childHandler);
        }
        return that;
    }

    @java.lang.Override
    public void remap(java.util.Map<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> oldToNew, boolean disconnectIfNotInMap) {
        super.remap(oldToNew, disconnectIfNotInMap);
        for (org.jhotdraw.draw.figure.Figure child : children) {
            child.remap(oldToNew, disconnectIfNotInMap);
        }
    }

    @java.lang.Override
    public boolean contains(org.jhotdraw.draw.figure.Figure f) {
        return children.contains(f);
    }

    @java.lang.Override
    public int indexOf(org.jhotdraw.draw.figure.Figure child) {
        return children.indexOf(child);
    }
}