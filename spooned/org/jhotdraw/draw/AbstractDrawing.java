/* @(#)AbstractDrawing.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
public abstract class AbstractDrawing implements org.jhotdraw.draw.Drawing {
    private static final java.lang.Object LOCK = new javax.swing.JPanel().getTreeLock();

    /**
     * Caches the bounds to improve the performance of method {@link #getBounds}.
     */
    protected transient java.awt.geom.Rectangle2D.Double cachedBounds;

    protected transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    protected int changingDepth = 0;

    protected final java.util.List<org.jhotdraw.draw.figure.Figure> CHILDREN = new java.util.ArrayList<>();

    protected final java.util.List<org.jhotdraw.draw.figure.Figure> UNMODIFIABLE_CHILDREN = java.util.Collections.unmodifiableList(CHILDREN);

    protected org.jhotdraw.draw.AbstractDrawing.EventHandler eventHandler = new org.jhotdraw.draw.AbstractDrawing.EventHandler();

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    private org.jhotdraw.draw.figure.Attributes attributes = new org.jhotdraw.draw.figure.Attributes(this::fireDrawingAttributeChanged);

    private transient java.awt.font.FontRenderContext fontRenderContext;

    private java.util.List<org.jhotdraw.draw.io.InputFormat> inputFormats = new java.util.ArrayList<>();

    private java.util.List<org.jhotdraw.draw.io.OutputFormat> outputFormats = new java.util.ArrayList<>();

    public AbstractDrawing() {
        eventHandler = createEventHandler();
    }

    @java.lang.Override
    public boolean add(org.jhotdraw.draw.figure.Figure figure) {
        add(getChildCount(), figure);
        return true;
    }

    @java.lang.Override
    public void add(int index, org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(index, figure);
        figure.addNotify(this);
        fireFigureAdded(figure, index);
        invalidate();
    }

    @java.lang.Override
    public void addAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        addAll(getChildCount(), figures);
    }

    @java.lang.Override
    public void addDrawingListener(org.jhotdraw.draw.event.DrawingListener listener) {
        listenerList.add(org.jhotdraw.draw.event.DrawingListener.class, listener);
    }

    @java.lang.Override
    public void addInputFormat(org.jhotdraw.draw.io.InputFormat format) {
        inputFormats.add(format);
    }

    @java.lang.Override
    public void addOutputFormat(org.jhotdraw.draw.io.OutputFormat format) {
        outputFormats.add(format);
    }

    @java.lang.Override
    public void addUndoableEditListener(javax.swing.event.UndoableEditListener l) {
        listenerList.add(javax.swing.event.UndoableEditListener.class, l);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Attributes attr() {
        return attributes;
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        CHILDREN.add(index, figure);
        figure.addFigureListener(eventHandler);
    }

    @java.lang.Override
    public void basicAdd(org.jhotdraw.draw.figure.Figure figure) {
        basicAdd(getChildCount(), figure);
    }

    @java.lang.Override
    public void basicAddAll(int index, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            basicAdd(index++, f);
        }
    }

    @java.lang.Override
    public void basicRemoveAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        for (org.jhotdraw.draw.figure.Figure f : new java.util.ArrayList<>(getChildren())) {
            basicRemove(f);
        }
    }

    @java.lang.Override
    public void bringToFront(org.jhotdraw.draw.figure.Figure figure) {
        if (basicRemove(figure) != (-1)) {
            basicAdd(figure);
            fireDrawingChanged(figure.getDrawingArea());
        }
    }

    @java.lang.Override
    public void changed() {
        if (changingDepth == 1) {
            validate();
            fireDrawingChanged(getDrawingArea());
        } else if (changingDepth < 1) {
            throw new java.lang.IllegalStateException("changed was called without a prior call to willChange. " + changingDepth);
        }
        changingDepth--;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public org.jhotdraw.draw.AbstractDrawing clone() {
        org.jhotdraw.draw.AbstractDrawing that;
        try {
            that = ((org.jhotdraw.draw.AbstractDrawing) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new java.lang.InternalError("clone failed", ex);
        }
        that.attributes = org.jhotdraw.draw.figure.Attributes.from(attributes, that::fireDrawingAttributeChanged);
        that.listenerList = new javax.swing.event.EventListenerList();
        that.inputFormats = (this.inputFormats == null) ? null : new java.util.ArrayList<>(this.inputFormats);
        that.outputFormats = (this.outputFormats == null) ? null : new java.util.ArrayList<>(this.outputFormats);
        return that;
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    @java.lang.Override
    public void fireUndoableEditHappened(javax.swing.undo.UndoableEdit edit) {
        javax.swing.event.UndoableEditEvent event = null;
        if (listenerList.getListenerCount() > 0) {
            // Notify all listeners that have registered interest for
            // Guaranteed to return a non-null array
            java.lang.Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (event == null) {
                    event = new javax.swing.event.UndoableEditEvent(this, edit);
                }
                if (listeners[i] == javax.swing.event.UndoableEditListener.class) {
                    ((javax.swing.event.UndoableEditListener) (listeners[i + 1])).undoableEditHappened(event);
                }
            }
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getChild(int index) {
        return CHILDREN.get(index);
    }

    @java.lang.Override
    public int getChildCount() {
        return CHILDREN.size();
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildren() {
        return UNMODIFIABLE_CHILDREN;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea() {
        return getDrawingArea(1.0);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double factor) {
        if (cachedDrawingArea == null) {
            if (getChildCount() == 0) {
                cachedDrawingArea = new java.awt.geom.Rectangle2D.Double();
            } else {
                for (org.jhotdraw.draw.figure.Figure f : CHILDREN) {
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
    public java.awt.font.FontRenderContext getFontRenderContext() {
        return fontRenderContext;
    }

    @java.lang.Override
    public void setFontRenderContext(java.awt.font.FontRenderContext frc) {
        fontRenderContext = frc;
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.io.InputFormat> getInputFormats() {
        return inputFormats;
    }

    @java.lang.Override
    public void setInputFormats(java.util.List<org.jhotdraw.draw.io.InputFormat> formats) {
        this.inputFormats = new java.util.ArrayList<>(formats);
    }

    /**
     * The drawing view synchronizes on the lock when drawing a drawing.
     */
    @java.lang.Override
    public java.lang.Object getLock() {
        return org.jhotdraw.draw.AbstractDrawing.LOCK;
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.io.OutputFormat> getOutputFormats() {
        return outputFormats;
    }

    @java.lang.Override
    public void setOutputFormats(java.util.List<org.jhotdraw.draw.io.OutputFormat> formats) {
        this.outputFormats = new java.util.ArrayList<>(formats);
    }

    @java.lang.Override
    public boolean remove(org.jhotdraw.draw.figure.Figure figure) {
        int index = CHILDREN.indexOf(figure);
        if (index == (-1)) {
            return false;
        } else {
            basicRemoveChild(index);
            figure.removeNotify(this);
            fireFigureRemoved(figure, index);
            return true;
        }
    }

    @java.lang.Override
    public void removeAll(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        willChange();
        for (org.jhotdraw.draw.figure.Figure f : new java.util.ArrayList<org.jhotdraw.draw.figure.Figure>(figures)) {
            remove(f);
        }
        changed();
    }

    @java.lang.Override
    public void removeAllChildren() {
        for (org.jhotdraw.draw.figure.Figure f : new java.util.ArrayList<>(getChildren())) {
            basicRemove(f);
        }
    }

    @java.lang.Override
    public void removeDrawingListener(org.jhotdraw.draw.event.DrawingListener listener) {
        listenerList.remove(org.jhotdraw.draw.event.DrawingListener.class, listener);
    }

    @java.lang.Override
    public void removeUndoableEditListener(javax.swing.event.UndoableEditListener l) {
        listenerList.remove(javax.swing.event.UndoableEditListener.class, l);
    }

    @java.lang.Override
    public void sendToBack(org.jhotdraw.draw.figure.Figure figure) {
        if (basicRemove(figure) != (-1)) {
            basicAdd(0, figure);
            fireDrawingChanged(figure.getDrawingArea());
        }
    }

    @java.lang.Override
    public void willChange() {
        if (changingDepth == 0) {
            invalidate();
        }
        changingDepth++;
    }

    protected int basicRemove(org.jhotdraw.draw.figure.Figure child) {
        int index = CHILDREN.indexOf(child);
        if (index != (-1)) {
            basicRemoveChild(index);
        }
        return index;
    }

    protected org.jhotdraw.draw.figure.Figure basicRemoveChild(int index) {
        org.jhotdraw.draw.figure.Figure figure = CHILDREN.remove(index);
        figure.removeFigureListener(eventHandler);
        invalidate();
        return figure;
    }

    protected org.jhotdraw.draw.AbstractDrawing.EventHandler createEventHandler() {
        return new org.jhotdraw.draw.AbstractDrawing.EventHandler();
    }

    protected <T> void fireDrawingAttributeChanged(org.jhotdraw.draw.AttributeKey<T> attribute, T oldValue, T newValue) {
        fireDrawingEvent((listener, event) -> listener.drawingChanged(event), () -> new org.jhotdraw.draw.event.DrawingEvent(this, attribute, oldValue, newValue));
    }

    protected void fireDrawingChanged(java.awt.geom.Rectangle2D.Double changedArea) {
        fireDrawingEvent((listener, event) -> listener.drawingChanged(event), () -> new org.jhotdraw.draw.event.DrawingEvent(this, changedArea));
    }

    protected void fireDrawingEvent(java.util.function.BiConsumer<org.jhotdraw.draw.event.DrawingListener, org.jhotdraw.draw.event.DrawingEvent> listenerConsumer, java.util.function.Supplier<org.jhotdraw.draw.event.DrawingEvent> eventSupplier) {
        org.jhotdraw.draw.event.DrawingEvent event = null;
        if (listenerList.getListenerCount() == 0) {
            return;
        }
        for (org.jhotdraw.draw.event.DrawingListener listener : listenerList.getListeners(org.jhotdraw.draw.event.DrawingListener.class)) {
            if (event == null) {
                event = eventSupplier.get();
            }
            listenerConsumer.accept(listener, event);
        }
    }

    protected void fireFigureAdded(org.jhotdraw.draw.figure.Figure figure, int index) {
        fireDrawingEvent((listener, event) -> listener.figureAdded(event), () -> new org.jhotdraw.draw.event.DrawingEvent(this, index, figure));
    }

    protected void fireFigureRemoved(org.jhotdraw.draw.figure.Figure figure, int index) {
        fireDrawingEvent((listener, event) -> listener.figureRemoved(event), () -> new org.jhotdraw.draw.event.DrawingEvent(this, index, figure));
    }

    protected int getChangingDepth() {
        return changingDepth;
    }

    protected void invalidate() {
        cachedBounds = null;
        cachedDrawingArea = null;
    }

    protected boolean isChanging() {
        return changingDepth != 0;
    }

    protected void validate() {
    }

    private final void addAll(int index, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> figures) {
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            basicAdd(index++, f);
            f.addNotify(this);
            fireFigureAdded(f, index);
        }
        invalidate();
    }

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
                fireDrawingChanged(invalidatedArea);
            }
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent e) {
            fireDrawingChanged(e.getInvalidatedArea());
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
}