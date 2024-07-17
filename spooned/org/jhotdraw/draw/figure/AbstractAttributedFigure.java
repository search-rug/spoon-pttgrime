/* @(#)AbstractAttributedFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * This abstract class can be extended to implement a {@link Figure} which has its own attribute
 * set.
 *
 * @author Werner Randelshofer
 * @version $Id: AbstractAttributedFigure.java 778 2012-04-13 15:37:19Z rawcoder $
 */
public abstract class AbstractAttributedFigure implements org.jhotdraw.draw.figure.Figure , java.lang.Cloneable {
    private static final long serialVersionUID = 1L;

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    private org.jhotdraw.draw.Drawing drawing;

    private boolean isSelectable = true;

    private boolean isRemovable = true;

    private boolean isVisible = true;

    private boolean isTransformable = true;

    private boolean isConnectable = true;

    private org.jhotdraw.draw.figure.Attributes attributes = new org.jhotdraw.draw.figure.Attributes(this::fireAttributeChanged);

    @java.lang.Override
    public org.jhotdraw.draw.figure.Attributes attr() {
        return attributes;
    }

    /**
     * This variable is used to prevent endless change loops. We increase its value on each invocation
     * of willChange() and decrease it on each invocation of changed().
     */
    protected int changingDepth = 0;

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
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

    public double getStrokeMiterLimitFactor() {
        java.lang.Number value = ((java.lang.Number) (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT)));
        return value != null ? value.doubleValue() : 10.0F;
    }

    @java.lang.Override
    public final java.awt.geom.Rectangle2D.Double getDrawingArea() {
        return getDrawingArea(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    @java.lang.Override
    public final java.awt.geom.Rectangle2D.Double getBounds() {
        return getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        // double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(this, factor);
        // double width = strokeTotalWidth / 2d;
        // if (attr().get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
        // width *= attr().get(STROKE_MITER_LIMIT);
        // } else if (attr().get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
        // width += strokeTotalWidth * 2;
        // }
        // width++;
        java.awt.geom.Rectangle2D.Double r = getBounds(scale);
        double grow = (org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scale) * 1.1) + 1;
        org.jhotdraw.geom.Geom.grow(r, grow, grow);
        return r;
    }

    /**
     * This method is called by method draw() to draw the fill area of the figure.
     * AbstractAttributedFigure configures the Graphics2D object with the FILL_COLOR attribute before
     * calling this method. If the FILL_COLOR attribute is null, this method is not called.
     */
    protected void drawFill(java.awt.Graphics2D g) {
    }

    /**
     * This method is called by method draw() to draw the lines of the figure . AttributedFigure
     * configures the Graphics2D object with the STROKE_COLOR attribute before calling this method. If
     * the STROKE_COLOR attribute is null, this method is not called.
     */
    protected void drawStroke(java.awt.Graphics2D g) {
    }

    /**
     * This method is called by method draw() to draw the text of the figure .
     * AbstractAttributedFigure configures the Graphics2D object with the TEXT_COLOR attribute before
     * calling this method. If the TEXT_COLOR attribute is null, this method is not called.
     */
    protected void drawText(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.AbstractAttributedFigure clone() {
        org.jhotdraw.draw.figure.AbstractAttributedFigure that;
        try {
            that = ((org.jhotdraw.draw.figure.AbstractAttributedFigure) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new java.lang.InternalError("clone failed", ex);
        }
        that.attributes = org.jhotdraw.draw.figure.Attributes.from(attributes, that::fireAttributeChanged);
        that.listenerList = new javax.swing.event.EventListenerList();
        that.drawing = null;// Clones need to be explictly added to a drawing

        return that;
    }

    @java.lang.Override
    public void addFigureListener(org.jhotdraw.draw.event.FigureListener l) {
        if (java.util.stream.Stream.of(listenerList.getListeners(org.jhotdraw.draw.event.FigureListener.class)).noneMatch(listener -> listener.equals(l))) {
            listenerList.add(org.jhotdraw.draw.event.FigureListener.class, l);
        }
    }

    @java.lang.Override
    public void removeFigureListener(org.jhotdraw.draw.event.FigureListener l) {
        listenerList.remove(org.jhotdraw.draw.event.FigureListener.class, l);
    }

    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.Drawing d) {
        this.drawing = d;
        fireFigureAdded();
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing d) {
        fireFigureRemoved();
        this.drawing = null;
    }

    public org.jhotdraw.draw.Drawing getDrawing() {
        return drawing;
    }

    private boolean modified = false;

    public final boolean isModified() {
        return modified;
    }

    public void setModified() {
        modified = true;
    }

    public void resetModified() {
        modified = false;
    }

    // protected Object getLock() {
    // return (getDrawing() == null) ? this : getDrawing().getLock();
    // }
    /**
     * tool method to process a listener and create its event object lazily.
     */
    protected void fireFigureEvent(java.util.function.BiConsumer<org.jhotdraw.draw.event.FigureListener, org.jhotdraw.draw.event.FigureEvent> listenerConsumer, java.util.function.Supplier<org.jhotdraw.draw.event.FigureEvent> eventSupplier) {
        org.jhotdraw.draw.event.FigureEvent event = null;
        if (listenerList.getListenerCount() == 0) {
            return;
        }
        for (org.jhotdraw.draw.event.FigureListener listener : listenerList.getListeners(org.jhotdraw.draw.event.FigureListener.class)) {
            if (event == null) {
                event = eventSupplier.get();
            }
            listenerConsumer.accept(listener, event);
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    public void fireAreaInvalidated() {
        fireAreaInvalidated(getDrawingArea());
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(java.awt.geom.Rectangle2D.Double invalidatedArea) {
        fireFigureEvent((listener, event) -> listener.areaInvalidated(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, invalidatedArea));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(org.jhotdraw.draw.event.FigureEvent event) {
        for (org.jhotdraw.draw.event.FigureListener listener : listenerList.getListeners(org.jhotdraw.draw.event.FigureListener.class)) {
            listener.areaInvalidated(event);
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureRequestRemove() {
        fireFigureEvent((listener, event) -> listener.figureRequestRemove(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this))));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureAdded() {
        fireFigureEvent((listener, event) -> listener.figureAdded(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this))));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureRemoved() {
        fireFigureEvent((listener, event) -> listener.figureRemoved(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this))));
    }

    public void fireFigureChanged() {
        fireFigureChanged(getDrawingArea());
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureChanged(java.awt.geom.Rectangle2D.Double changedArea) {
        fireFigureEvent((listener, event) -> listener.figureChanged(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, changedArea));
    }

    protected void fireFigureChanged(org.jhotdraw.draw.event.FigureEvent event) {
        fireFigureEvent((listener, evt) -> listener.figureChanged(evt), () -> event);
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected <T> void fireAttributeChanged(org.jhotdraw.draw.AttributeKey<T> attribute, T oldValue, T newValue) {
        fireFigureEvent((listener, event) -> listener.attributeChanged(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, attribute, oldValue, newValue));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireFigureHandlesChanged() {
        fireFigureEvent((listener, event) -> listener.figureHandlesChanged(event), () -> new org.jhotdraw.draw.event.FigureEvent(this, getDrawingArea()));
    }

    /**
     * Notify all UndoableEditListener of the Drawing, to which this Figure has been added to. If this
     * Figure is not part of a Drawing, the event is lost.
     */
    protected void fireUndoableEditHappened(javax.swing.undo.UndoableEdit edit) {
        if (getDrawing() != null) {
            getDrawing().fireUndoableEditHappened(edit);
        }
    }

    @java.lang.Override
    public void remap(java.util.Map<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> oldToNew, boolean disconnectIfNotInMap) {
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        switch (detailLevel) {
            case -1 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, false, true));
                break;
            case 0 :
                org.jhotdraw.draw.handle.ResizeHandleKit.addResizeHandles(this, handles);
                break;
        }
        return handles;
    }

    @java.lang.Override
    public java.awt.Cursor getCursor(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        if (contains(p, scaleDenominator)) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
        } else {
            return java.awt.Cursor.getDefaultCursor();
        }
    }

    public final void setBounds(java.awt.geom.Rectangle2D.Double bounds) {
        setBounds(new java.awt.geom.Point2D.Double(bounds.x, bounds.y), new java.awt.geom.Point2D.Double(bounds.x + bounds.width, bounds.y + bounds.height));
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        java.awt.geom.Point2D.Double oldAnchor = getStartPoint();
        java.awt.geom.Point2D.Double oldLead = getEndPoint();
        if ((!oldAnchor.equals(anchor)) || (!oldLead.equals(lead))) {
            willChange();
            setBounds(anchor, lead);
            changed();
            fireUndoableEditHappened(new org.jhotdraw.draw.event.SetBoundsEdit(this, oldAnchor, oldLead, anchor, lead));
        }
    }

    /**
     * Invalidates cached data of the Figure. This method must execute fast, because it can be called
     * very often.
     */
    protected void invalidate() {
    }

    protected boolean isChanging() {
        return changingDepth != 0;
    }

    protected int getChangingDepth() {
        return changingDepth;
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p) {
        return contains(p, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
    }

    /**
     * Informs that a figure is about to change something that affects the contents of its display
     * box.
     */
    @java.lang.Override
    public void willChange() {
        if (changingDepth == 0) {
            fireAreaInvalidated();
            invalidate();
        }
        changingDepth++;
    }

    protected void validate() {
    }

    /**
     * Informs that a figure changed the area of its display box.
     */
    @java.lang.Override
    public void changed() {
        if (changingDepth == 1) {
            validate();
            fireFigureChanged(getDrawingArea());
        } else if (changingDepth < 1) {
            throw new java.lang.IllegalStateException("changed was called without a prior call to willChange. " + changingDepth);
        }
        modified = true;
        changingDepth--;
    }

    /**
     * Returns the Figures connector for the specified location. By default a ChopBoxConnector is
     * returned.
     *
     * @see ChopRectangleConnector
     */
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopRectangleConnector(this);
    }

    @java.lang.Override
    public boolean includes(org.jhotdraw.draw.figure.Figure figure) {
        return figure == this;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureInside(java.awt.geom.Point2D.Double p) {
        return contains(p) ? this : null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStart) {
        return new org.jhotdraw.draw.connector.ChopRectangleConnector(this);
    }

    /**
     * Returns a collection of actions which are presented to the user in a popup menu.
     *
     * <p>The collection may contain null entries. These entries are used interpreted as separators in
     * the popup menu.
     */
    @java.lang.Override
    public java.util.Collection<javax.swing.Action> getActions(java.awt.geom.Point2D.Double p) {
        return java.util.Collections.emptyList();
    }

    /**
     * Returns a specialized tool for the given coordinate.
     *
     * <p>Returns null, if no specialized tool is available.
     */
    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool(java.awt.geom.Point2D.Double p) {
        return null;
    }

    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        return false;
    }

    @java.lang.Override
    public boolean handleDrop(java.awt.geom.Point2D.Double p, java.util.Collection<org.jhotdraw.draw.figure.Figure> droppedFigures, org.jhotdraw.draw.DrawingView view) {
        return false;
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getEndPoint() {
        java.awt.geom.Rectangle2D.Double r = getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
        return new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height);
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getStartPoint() {
        java.awt.geom.Rectangle2D.Double r = getBounds(org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
        return new java.awt.geom.Point2D.Double(r.x, r.y);
    }

    /* public Rectangle2D.Double getHitBounds() {
    return getBounds();
    }
     */
    @java.lang.Override
    public org.jhotdraw.geom.Dimension2DDouble getPreferredSize(double scale) {
        java.awt.geom.Rectangle2D.Double r = getBounds(scale);
        return new org.jhotdraw.geom.Dimension2DDouble(r.width, r.height);
    }

    /**
     * Checks whether this figure is connectable. By default {@code AbstractFigure} can be connected.
     */
    @java.lang.Override
    public boolean isConnectable() {
        return isConnectable;
    }

    public void setConnectable(boolean newValue) {
        boolean oldValue = isConnectable;
        isConnectable = newValue;
    }

    /**
     * Checks whether this figure is selectable. By default {@code AbstractFigure} can be selected.
     */
    @java.lang.Override
    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean newValue) {
        boolean oldValue = isSelectable;
        isSelectable = newValue;
    }

    /**
     * Checks whether this figure is removable. By default {@code AbstractFigure} can be removed.
     */
    @java.lang.Override
    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean newValue) {
        boolean oldValue = isRemovable;
        isRemovable = newValue;
    }

    /**
     * Checks whether this figure is transformable. By default {@code AbstractFigure} can be
     * transformed.
     */
    @java.lang.Override
    public boolean isTransformable() {
        return isTransformable;
    }

    public void setTransformable(boolean newValue) {
        boolean oldValue = isTransformable;
        isTransformable = newValue;
    }

    /**
     * Checks whether this figure is visible. By default {@code AbstractFigure} is visible.
     */
    @java.lang.Override
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean newValue) {
        if (newValue != isVisible) {
            willChange();
            isVisible = newValue;
            changed();
        }
    }

    protected java.awt.font.FontRenderContext getFontRenderContext() {
        java.awt.font.FontRenderContext frc = null;
        if (frc == null) {
            frc = new java.awt.font.FontRenderContext(new java.awt.geom.AffineTransform(), true, true);
        }
        return frc;
    }

    @java.lang.Override
    public void requestRemove() {
        fireFigureRequestRemove();
    }

    /**
     * AbstractFigure always returns 0. Override this method if your figure needs to be on a different
     * layer.
     */
    @java.lang.Override
    public int getLayer() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.geom.Point2D.Double p) {
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        buf.append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
        buf.append('@');
        buf.append(hashCode());
        return buf.toString();
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.connector.Connector> getConnectors(org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        java.util.List<org.jhotdraw.draw.connector.Connector> connectors = new java.util.ArrayList<>();
        connectors.add(new org.jhotdraw.draw.connector.ChopRectangleConnector(this));
        return connectors;
    }
}