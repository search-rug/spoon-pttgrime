/* @(#)AbstractCompositeFigure.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * This abstract class can be extended to implement a {@link CompositeFigure}.
 * AbstractCompositeFigure.
 */
public abstract class AbstractAttributedCompositeFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure implements org.jhotdraw.draw.figure.CompositeFigure {
    private static final long serialVersionUID = 1L;

    /**
     * A Layouter determines how the children of the CompositeFigure are laid out graphically.
     */
    protected org.jhotdraw.draw.layouter.Layouter layouter;

    protected java.util.List<org.jhotdraw.draw.figure.Figure> children = new java.util.ArrayList<>();

    /**
     * Caches the drawing area to improve the performance of method {@link #getDrawingArea}.
     */
    protected transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    /**
     * Caches the bounds to improve the performance of method {@link #getBounds}.
     */
    protected transient java.awt.geom.Rectangle2D.Double cachedBounds;

    /**
     * Handles figure changes in the children.
     */
    protected org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure.EventHandler eventHandler;

    protected class EventHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements javax.swing.event.UndoableEditListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public void figureRequestRemove(org.jhotdraw.draw.event.FigureEvent e) {
            remove(e.getFigure());
        }

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
            if (!isChanging()) {
                java.awt.geom.Rectangle2D.Double invalidatedArea = getDrawingArea();
                invalidatedArea.add(e.getInvalidatedArea());
                // We call invalidate/validate here, because we must layout
                // the figure again.
                invalidate();
                validate();
                // Forward the figureChanged event to listeners on AbstractCompositeFigure.
                invalidatedArea.add(getDrawingArea());
                fireFigureChanged(invalidatedArea);
            }
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent e) {
            fireAreaInvalidated(e);
        }

        @java.lang.Override
        public void undoableEditHappened(javax.swing.event.UndoableEditEvent e) {
            fireUndoableEditHappened(e.getEdit());
        }

        @java.lang.Override
        public void attributeChanged(org.jhotdraw.draw.event.FigureEvent e) {
            invalidate();
        }

        @java.lang.Override
        public void figureAdded(org.jhotdraw.draw.event.FigureEvent e) {
            invalidate();
        }

        @java.lang.Override
        public void figureRemoved(org.jhotdraw.draw.event.FigureEvent e) {
            invalidate();
        }
    }

    public AbstractAttributedCompositeFigure() {
        eventHandler = createEventHandler();
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        if (detailLevel == 0) {
            handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, true, false));
            org.jhotdraw.draw.handle.TransformHandleKit.addScaleMoveTransformHandles(this, handles);
        }
        return handles;
    }

    protected org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure.EventHandler createEventHandler() {
        return new org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure.EventHandler();
    }

    @java.lang.Override
    public boolean add(org.jhotdraw.draw.figure.Figure figure) {
        add(getChildCount(), figure);
        return true;
    }

    @java.lang.Override
    public void add(int index, org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(index, figure);
        if (getDrawing() != null) {
            figure.addNotify(getDrawing());
        }
        fireFigureAdded(figure, index);
        invalidate();
    }

    public void addAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        addAll(getChildCount(), figures);
    }

    public final void addAll(int index, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            basicAdd(index++, f);
            if (getDrawing() != null) {
                f.addNotify(getDrawing());
            }
            fireFigureAdded(f, index);
        }
        invalidate();
    }

    @java.lang.Override
    public void basicAdd(org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(getChildCount(), figure);
    }

    public void basicAddAll(int index, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> newFigures) {
        for (org.jhotdraw.draw.figure.Figure f : newFigures) {
            basicAdd(index++, f);
        }
    }

    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.Drawing drawing) {
        super.addNotify(drawing);
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            child.addNotify(drawing);
        }
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing drawing) {
        super.removeNotify(drawing);
        // Copy children collection to avoid concurrent modification exception
        for (org.jhotdraw.draw.figure.Figure child : new java.util.ArrayList<>(getChildren())) {
            child.removeNotify(drawing);
        }
    }

    @java.lang.Override
    public boolean remove(final org.jhotdraw.draw.figure.Figure figure) {
        int index = children.indexOf(figure);
        if (index == (-1)) {
            return false;
        } else {
            basicRemoveChild(index);
            if (getDrawing() != null) {
                figure.removeNotify(getDrawing());
            }
            fireFigureRemoved(figure, index);
            return true;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure removeChild(int index) {
        org.jhotdraw.draw.figure.Figure removed = basicRemoveChild(index);
        if (getDrawing() != null) {
            removed.removeNotify(getDrawing());
        }
        return removed;
    }

    /**
     * Removes all specified children.
     *
     * @see #add
     */
    public void removeAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        willChange();
        for (org.jhotdraw.draw.figure.Figure f : new java.util.ArrayList<org.jhotdraw.draw.figure.Figure>(figures)) {
            remove(f);
        }
        changed();
    }

    /**
     * Removes all children.
     *
     * @see #add
     */
    @java.lang.Override
    public void removeAllChildren() {
        removeAll(getChildren());
    }

    /**
     * Removes all children.
     *
     * @see #add
     */
    @java.lang.Override
    public void basicRemoveAllChildren() {
        for (org.jhotdraw.draw.figure.Figure f : new java.util.ArrayList<>(getChildren())) {
            basicRemove(f);
        }
    }

    /**
     * Removes all children.
     *
     * @see #add
     */
    public void basicRemoveAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            basicRemove(f);
        }
    }

    /**
     * Sends a figure to the back of the composite figure.
     *
     * @param figure
     * 		that is part of this composite figure
     */
    public void sendToBack(org.jhotdraw.draw.figure.Figure figure) {
        if (basicRemove(figure) != (-1)) {
            basicAdd(0, figure);
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    /**
     * Brings a figure to the front of the drawing.
     *
     * @param figure
     * 		that is part of the drawing
     */
    public void bringToFront(org.jhotdraw.draw.figure.Figure figure) {
        if (basicRemove(figure) != (-1)) {
            basicAdd(figure);
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    /**
     * Transforms the figure.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
            f.transform(tx);
        }
        invalidate();
        // invalidate();
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        java.awt.geom.Rectangle2D.Double oldBounds = getBounds();
        java.awt.geom.Rectangle2D.Double newBounds = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(anchor.x, lead.x), java.lang.Math.min(anchor.y, lead.y), java.lang.Math.abs(anchor.x - lead.x), java.lang.Math.abs(anchor.y - lead.y));
        double sx = newBounds.width / oldBounds.width;
        double sy = newBounds.height / oldBounds.height;
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(-oldBounds.x, -oldBounds.y);
        if (((((((!java.lang.Double.isNaN(sx)) && (!java.lang.Double.isNaN(sy))) && (!java.lang.Double.isInfinite(sx))) && (!java.lang.Double.isInfinite(sy))) && ((sx != 1.0) || (sy != 1.0))) && (!(sx < 1.0E-4))) && (!(sy < 1.0E-4))) {
            transform(tx);
            tx.setToIdentity();
            tx.scale(sx, sy);
            transform(tx);
            tx.setToIdentity();
        }
        tx.translate(newBounds.x, newBounds.y);
        transform(tx);
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the children.
     */
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildrenFrontToBack() {
        return children.size() == 0 ? new java.util.ArrayList<>() : new org.jhotdraw.util.ReversedList<>(getChildren());
    }

    @java.lang.Override
    public boolean contains(org.jhotdraw.draw.figure.Figure f) {
        return children.contains(f);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        if (getDrawingArea(scaleDenominator).contains(p)) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                try {
                    p = ((java.awt.geom.Point2D.Double) (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, new java.awt.geom.Point2D.Double())));
                } catch (java.awt.geom.NoninvertibleTransformException ex) {
                    java.lang.InternalError error = new java.lang.InternalError(ex.getMessage());
                    error.initCause(ex);
                    throw error;
                }
            }
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                if (child.isVisible() && child.contains(p, scaleDenominator)) {
                    return true;
                }
            }
        }
        return false;
    }

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

    public org.jhotdraw.draw.figure.Figure findChild(java.awt.geom.Point2D.Double p) {
        if (getBounds().contains(p)) {
            org.jhotdraw.draw.figure.Figure found = null;
            for (org.jhotdraw.draw.figure.Figure child : getChildrenFrontToBack()) {
                if (child.isVisible() && child.contains(p)) {
                    return child;
                }
            }
        }
        return null;
    }

    public int findChildIndex(java.awt.geom.Point2D.Double p) {
        org.jhotdraw.draw.figure.Figure child = findChild(p);
        return child == null ? -1 : children.indexOf(child);
    }

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

    /**
     * A layout algorithm is used to define how the child components should be laid out in relation to
     * each other. The task for layouting the child components for presentation is delegated to a
     * Layouter which can be plugged in at runtime.
     */
    @java.lang.Override
    public void layout(double scale) {
        // Note: We increase and below decrease the changing depth here,
        // because we want to ignore change events from our children
        // why we lay them out.
        changingDepth++;
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            if (child instanceof org.jhotdraw.draw.figure.CompositeFigure) {
                org.jhotdraw.draw.figure.CompositeFigure cf = ((org.jhotdraw.draw.figure.CompositeFigure) (child));
                cf.layout(scale);
            }
        }
        changingDepth--;
        if (getLayouter() != null) {
            java.awt.geom.Rectangle2D.Double bounds = getBounds(scale);
            java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(bounds.x, bounds.y);
            java.awt.geom.Rectangle2D.Double r = getLayouter().layout(this, p, p, scale);
            setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
            invalidate();
        }
    }

    /**
     * Set a Layouter object which encapsulated a layout algorithm for this figure. Typically, a
     * Layouter accesses the child components of this figure and arranges their graphical
     * presentation. It is a good idea to set the Layouter in the protected initialize() method so it
     * can be recreated if a GraphicalCompositeFigure is read and restored from a StorableInput
     * stream.
     *
     * @param newLayouter
     * 		encapsulation of a layout algorithm.
     */
    @java.lang.Override
    public void setLayouter(org.jhotdraw.draw.layouter.Layouter newLayouter) {
        this.layouter = newLayouter;
    }

    @java.lang.Override
    public org.jhotdraw.geom.Dimension2DDouble getPreferredSize(double scale) {
        if (this.layouter != null) {
            java.awt.geom.Rectangle2D.Double r = layouter.calculateLayout(this, getStartPoint(), getEndPoint(), scale);
            return new org.jhotdraw.geom.Dimension2DDouble(r.width, r.height);
        } else {
            return super.getPreferredSize(scale);
        }
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
        if (clipBounds != null) {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                if (child.isVisible() && child.getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)).intersects(clipBounds)) {
                    child.draw(g);
                }
            }
        } else {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                if (child.isVisible()) {
                    child.draw(g);
                }
            }
        }
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        @java.lang.SuppressWarnings("unchecked")
        java.util.List list = ((java.util.ArrayList) (geometry));
        java.util.Iterator<java.lang.Object> i = list.iterator();
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            child.restoreTransformTo(i.next());
        }
        invalidate();
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        java.util.List<java.lang.Object> list = new java.util.ArrayList<>();
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            list.add(child.getTransformRestoreData());
        }
        return list;
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        children.add(index, figure);
        figure.addFigureListener(eventHandler);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure basicRemoveChild(int index) {
        org.jhotdraw.draw.figure.Figure figure = children.remove(index);
        figure.removeFigureListener(eventHandler);
        invalidate();
        return figure;
    }

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

    @java.lang.Override
    public org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure clone() {
        org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure that = ((org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure) (super.clone()));
        that.attributes = org.jhotdraw.draw.figure.Attributes.from(attributes, that::fireAttributeChanged, org.jhotdraw.draw.figure.Attributes.attrSupplier(() -> that.getChildren()));
        that.children = new java.util.ArrayList<>();
        that.eventHandler = that.createEventHandler();
        for (org.jhotdraw.draw.figure.Figure thisChild : this.children) {
            org.jhotdraw.draw.figure.Figure thatChild = thisChild.clone();
            that.children.add(thatChild);
            thatChild.removeFigureListener(this.eventHandler);
            thatChild.addFigureListener(that.eventHandler);
        }
        return that;
    }

    @java.lang.Override
    protected void validate() {
        super.validate();
        layout(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    @java.lang.Override
    protected void invalidate() {
        cachedBounds = null;
        cachedDrawingArea = null;
    }

    @java.lang.Override
    public void willChange() {
        super.willChange();
        for (org.jhotdraw.draw.figure.Figure child : children) {
            child.willChange();
        }
    }

    @java.lang.Override
    public void changed() {
        for (org.jhotdraw.draw.figure.Figure child : children) {
            child.changed();
        }
        super.changed();
    }

    @java.lang.Override
    public int basicRemove(org.jhotdraw.draw.figure.Figure child) {
        int index = children.indexOf(child);
        if (index != (-1)) {
            basicRemoveChild(index);
        }
        return index;
    }

    @java.lang.Override
    public int indexOf(org.jhotdraw.draw.figure.Figure child) {
        return children.indexOf(child);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double factor) {
        if (cachedDrawingArea == null) {
            if (getChildCount() == 0) {
                cachedDrawingArea = new java.awt.geom.Rectangle2D.Double();
            } else {
                for (org.jhotdraw.draw.figure.Figure f : children) {
                    if (cachedDrawingArea == null) {
                        cachedDrawingArea = f.getDrawingArea(factor);
                    } else {
                        cachedDrawingArea.add(f.getDrawingArea(factor));
                    }
                }
            }
        }
        return new java.awt.geom.Rectangle2D.Double(cachedDrawingArea.x, cachedDrawingArea.y, cachedDrawingArea.width, cachedDrawingArea.height);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        if (cachedBounds == null) {
            if (getChildCount() == 0) {
                cachedBounds = new java.awt.geom.Rectangle2D.Double();
            } else {
                for (org.jhotdraw.draw.figure.Figure f : children) {
                    if (cachedBounds == null) {
                        cachedBounds = f.getBounds(scale);
                    } else {
                        cachedBounds.add(f.getBounds(scale));
                    }
                }
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedBounds.clone()));
    }

    /**
     * tool method to process a listener and create its event object lazily.
     */
    protected void fireCompositeFigureEvent(java.util.function.BiConsumer<org.jhotdraw.draw.event.CompositeFigureListener, org.jhotdraw.draw.event.CompositeFigureEvent> listenerConsumer, java.util.function.Supplier<org.jhotdraw.draw.event.CompositeFigureEvent> eventSupplier) {
        org.jhotdraw.draw.event.CompositeFigureEvent event = null;
        if (listenerList.getListenerCount() == 0) {
            return;
        }
        for (org.jhotdraw.draw.event.CompositeFigureListener listener : listenerList.getListeners(org.jhotdraw.draw.event.CompositeFigureListener.class)) {
            if (event == null) {
                event = eventSupplier.get();
            }
            listenerConsumer.accept(listener, event);
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureAdded(org.jhotdraw.draw.figure.Figure f, int zIndex) {
        fireCompositeFigureEvent((listener, event) -> listener.figureAdded(event), () -> new org.jhotdraw.draw.event.CompositeFigureEvent(this, f, f.getDrawingArea(), zIndex));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureRemoved(org.jhotdraw.draw.figure.Figure f, int zIndex) {
        fireCompositeFigureEvent((listener, event) -> listener.figureRemoved(event), () -> new org.jhotdraw.draw.event.CompositeFigureEvent(this, f, f.getDrawingArea(), zIndex));
    }

    @java.lang.Override
    public void removeCompositeFigureListener(org.jhotdraw.draw.event.CompositeFigureListener listener) {
        listenerList.remove(org.jhotdraw.draw.event.CompositeFigureListener.class, listener);
    }

    @java.lang.Override
    public void addCompositeFigureListener(org.jhotdraw.draw.event.CompositeFigureListener listener) {
        listenerList.add(org.jhotdraw.draw.event.CompositeFigureListener.class, listener);
    }

    private org.jhotdraw.draw.figure.Attributes attributes = new org.jhotdraw.draw.figure.Attributes(this::fireAttributeChanged, org.jhotdraw.draw.figure.Attributes.attrSupplier(() -> this.getChildren()));

    @java.lang.Override
    public org.jhotdraw.draw.figure.Attributes attr() {
        return attributes;
    }

    public void drawFigure(java.awt.Graphics2D g) {
        drawChildren(g);
        if (attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) != null) {
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR));
            drawFill(g);
        }
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) != null) && (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) >= 0.0)) {
            g.setStroke(org.jhotdraw.draw.AttributeKeys.getStroke(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR));
            drawStroke(g);
        }
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR) != null) {
            if ((attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_COLOR) != null) && (attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_OFFSET) != null)) {
                org.jhotdraw.geom.Dimension2DDouble d = attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_OFFSET);
                g.translate(d.width, d.height);
                g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_SHADOW_COLOR));
                drawText(g);
                g.translate(-d.width, -d.height);
            }
            g.setColor(attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR));
            drawText(g);
        }
    }

    protected void drawChildren(java.awt.Graphics2D g) {
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            child.draw(g);
        }
    }

    public java.awt.Stroke getStroke() {
        return org.jhotdraw.draw.AttributeKeys.getStroke(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    @java.lang.Override
    public double getStrokeMiterLimitFactor() {
        java.lang.Number value = ((java.lang.Number) (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT)));
        return value != null ? value.doubleValue() : 10.0F;
    }

    public java.awt.geom.Rectangle2D.Double getFigureDrawBounds() {
        double width = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this)) / 2.0;
        if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
            width *= attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
        }
        width++;
        java.awt.geom.Rectangle2D.Double r = getBounds();
        org.jhotdraw.geom.Geom.grow(r, width, width);
        return r;
    }
}