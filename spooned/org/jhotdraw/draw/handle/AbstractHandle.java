/* @(#)AbstractHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * This abstract class can be extended to implement a {@link Handle}.
 */
public abstract class AbstractHandle implements org.jhotdraw.draw.handle.Handle {
    protected final org.jhotdraw.draw.event.FigureListener FIGURE_LISTENER = new org.jhotdraw.draw.event.FigureListenerAdapter() {
        /**
         * Sent when a region used by the figure needs to be repainted. The implementation of this
         * method assumes that the handle is located on the bounds of the figure or inside the
         * figure. If the handle is located elsewhere this method must be reimpleted by the
         * subclass.
         */
        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent evt) {
            updateBounds();
        }

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent evt) {
            updateBounds();
        }
    };

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    protected org.jhotdraw.draw.DrawingView view;

    private java.awt.Rectangle bounds;

    private final org.jhotdraw.draw.figure.Figure owner;

    private java.lang.String toolTipText;

    public AbstractHandle(org.jhotdraw.draw.figure.Figure owner) {
        if (owner == null) {
            throw new java.lang.IllegalArgumentException("owner must not be null");
        }
        this.owner = owner;
        owner.addFigureListener(FIGURE_LISTENER);
    }

    @java.lang.Override
    public void addHandleListener(org.jhotdraw.draw.event.HandleListener l) {
        listenerList.add(org.jhotdraw.draw.event.HandleListener.class, l);
    }

    @java.lang.Override
    public boolean contains(java.awt.Point p) {
        return getBounds().contains(p);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createSecondaryHandles() {
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public void dispose() {
        owner.removeFigureListener(FIGURE_LISTENER);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_STROKE_COLOR));
    }

    /**
     * Gets and caches actual handles bounds. The computation itself is done in basicGetBounds.
     */
    @java.lang.Override
    public final java.awt.Rectangle getBounds() {
        if (bounds == null) {
            bounds = basicGetBounds();
        }
        return new java.awt.Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR);
    }

    @java.lang.Override
    public java.awt.Rectangle getDrawingArea() {
        java.awt.Rectangle r = getBounds();
        r.grow(2, 2);// grow by two pixels to take antialiasing into account

        return r;
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return view.getEditor();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getOwner() {
        return owner;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return toolTipText;
    }

    public void setToolTipText(java.lang.String newValue) {
        toolTipText = newValue;
    }

    public org.jhotdraw.draw.DrawingView getView() {
        return view;
    }

    @java.lang.Override
    public void setView(org.jhotdraw.draw.DrawingView view) {
        this.view = view;
    }

    @java.lang.Override
    public void invalidate() {
        bounds = null;
    }

    /**
     * Returns true, if the given handle is an instance of the same class or of a subclass of this
     * handle,.
     */
    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle handle) {
        return getClass().isAssignableFrom(handle.getClass());
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
    }

    @java.lang.Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
    }

    @java.lang.Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
    }

    @java.lang.Override
    public void removeHandleListener(org.jhotdraw.draw.event.HandleListener l) {
        listenerList.remove(org.jhotdraw.draw.event.HandleListener.class, l);
    }

    @java.lang.Override
    public void trackDoubleClick(java.awt.Point p, int modifiersEx) {
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
    public void viewTransformChanged() {
        invalidate();
    }

    protected java.awt.Rectangle basicGetBounds() {
        java.awt.Rectangle r = new java.awt.Rectangle(getScreenLocation());
        int h = getHandlesize();
        r.x -= h / 2;
        r.y -= h / 2;
        r.width = r.height = h;
        return r;
    }

    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        return null;
    }

    protected java.awt.Point getScreenLocation() {
        return java.util.Optional.ofNullable(getDrawingLocation()).map(pnt -> view.drawingToView(pnt)).orElseGet(() -> new java.awt.Point(10, 10));
    }

    protected void drawCircle(java.awt.Graphics2D g, java.awt.Color fill, java.awt.Color stroke) {
        java.awt.Rectangle r = getBounds();
        if (fill != null) {
            g.setColor(fill);
            g.fillOval(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
        }
        if (stroke != null) {
            g.setStroke(new java.awt.BasicStroke());
            g.setColor(stroke);
            g.drawOval(r.x, r.y, r.width - 1, r.height - 1);
            if (getView().getActiveHandle() == this) {
                g.fillOval(r.x + 2, r.y + 2, r.width - 4, r.height - 4);
            }
        }
    }

    protected void drawDiamond(java.awt.Graphics2D g, java.awt.Color fill, java.awt.Color stroke) {
        if (stroke != null) {
            java.awt.Rectangle r = getBounds();
            r.grow(1, 1);
            java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
            p.moveTo(r.x + (r.width / 2.0F), r.y);
            p.lineTo(r.x + r.width, r.y + (r.height / 2.0F));
            p.lineTo(r.x + (r.width / 2.0F), r.y + r.height);
            p.lineTo(r.x, r.y + (r.height / 2.0F));
            p.closePath();
            g.setColor(stroke);
            g.fill(p);
        }
        if (fill != null) {
            java.awt.Rectangle r = getBounds();
            java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
            p.moveTo(r.x + (r.width / 2.0F), r.y);
            p.lineTo(r.x + r.width, r.y + (r.height / 2.0F));
            p.lineTo(r.x + (r.width / 2.0F), r.y + r.height);
            p.lineTo(r.x, r.y + (r.height / 2.0F));
            p.closePath();
            g.setColor(fill);
            g.fill(p);
        }
        if ((stroke != null) && (getView().getActiveHandle() == this)) {
            java.awt.Rectangle r = getBounds();
            r.grow(-1, -1);
            java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
            p.moveTo(r.x + (r.width / 2.0F), r.y);
            p.lineTo(r.x + r.width, r.y + (r.height / 2.0F));
            p.lineTo(r.x + (r.width / 2.0F), r.y + r.height);
            p.lineTo(r.x, r.y + (r.height / 2.0F));
            p.closePath();
            g.setColor(stroke);
            g.fill(p);
        }
    }

    protected void drawRectangle(java.awt.Graphics2D g, java.awt.Color fill, java.awt.Color stroke) {
        if (fill != null) {
            java.awt.Rectangle r = getBounds();
            g.setColor(fill);
            r.x += 1;
            r.y += 1;
            r.width -= 2;
            r.height -= 2;
            g.fill(r);
        }
        g.setStroke(new java.awt.BasicStroke());
        if (stroke != null) {
            java.awt.Rectangle r = getBounds();
            r.width -= 1;
            r.height -= 1;
            g.setColor(stroke);
            g.draw(r);
            if (getView().getActiveHandle() == this) {
                r.x += 2;
                r.y += 2;
                r.width -= 3;
                r.height -= 3;
                g.fill(r);
            }
        }
    }

    protected void fireAreaInvalidated(java.awt.Rectangle invalidatedArea) {
        fireHandleEvent((listener, event) -> listener.areaInvalidated(event), () -> new org.jhotdraw.draw.event.HandleEvent(this, invalidatedArea));
    }

    /**
     * Wrapper around multiple types of event firing. This is used to define all kinds of event
     * methods below.
     *
     * @param listenerConsumer
     * 		lambda to call the right listener method with the right event
     * @param eventSupplier
     * 		creates if needed an event instance for the listener
     */
    protected void fireHandleEvent(java.util.function.BiConsumer<org.jhotdraw.draw.event.HandleListener, org.jhotdraw.draw.event.HandleEvent> listenerConsumer, java.util.function.Supplier<org.jhotdraw.draw.event.HandleEvent> eventSupplier) {
        org.jhotdraw.draw.event.HandleEvent event = null;
        if (listenerList.getListenerCount() == 0) {
            return;
        }
        for (org.jhotdraw.draw.event.HandleListener listener : listenerList.getListeners(org.jhotdraw.draw.event.HandleListener.class)) {
            if (event == null) {
                event = eventSupplier.get();
            }
            listenerConsumer.accept(listener, event);
        }
    }

    protected void fireHandleRequestRemove(java.awt.Rectangle invalidatedArea) {
        fireHandleEvent((listener, event) -> listener.handleRequestRemove(event), () -> new org.jhotdraw.draw.event.HandleEvent(this, invalidatedArea));
    }

    protected void fireHandleRequestSecondaryHandles() {
        fireHandleEvent((listener, event) -> listener.handleRequestSecondaryHandles(event), () -> new org.jhotdraw.draw.event.HandleEvent(this, null));
    }

    protected void fireUndoableEditHappened(javax.swing.undo.UndoableEdit edit) {
        view.getDrawing().fireUndoableEditHappened(edit);
    }

    protected int getHandlesize() {
        return getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_SIZE);
    }

    protected void updateBounds() {
        java.awt.Rectangle newBounds = basicGetBounds();
        if ((bounds == null) || (!newBounds.equals(bounds))) {
            if (bounds != null) {
                fireAreaInvalidated(getDrawingArea());
            }
            bounds = newBounds;
            fireAreaInvalidated(getDrawingArea());
        }
    }
}