/* Copyright (C) 2015 JHotDraw.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
 */
package org.jhotdraw.draw;
/**
 * Implementation of DrawingView using no JComponent as a backend. The displaying container comes
 * with a delegator class. Therefore we have a Swing independend implementaion.
 */
public abstract class AbstractDrawingView implements org.jhotdraw.draw.DrawingView , org.jhotdraw.api.gui.EditableComponent {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.draw.AbstractDrawingView.class.getName());

    private org.jhotdraw.draw.Drawing drawing;

    private final transient java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    private final transient javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    /**
     * Holds the selected figures in an ordered put. The ordering reflects the sequence that was used
     * to select the figures.
     */
    private final java.util.Set<org.jhotdraw.draw.figure.Figure> SELECTED_FIGURES = new java.util.LinkedHashSet<>();

    private final java.util.Set<org.jhotdraw.draw.figure.Figure> UNMODIFIABLE_SELECTED_FIGURES = java.util.Collections.unmodifiableSet(SELECTED_FIGURES);

    private final java.util.List<org.jhotdraw.draw.handle.Handle> selectionHandles = new java.util.ArrayList<>();

    private boolean isConstrainerVisible = false;

    private org.jhotdraw.draw.constrainer.Constrainer visibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer(8, 8);

    private org.jhotdraw.draw.constrainer.Constrainer invisibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer();

    private org.jhotdraw.draw.handle.Handle secondaryHandleOwner;

    private org.jhotdraw.draw.handle.Handle activeHandle;

    private final java.util.List<org.jhotdraw.draw.handle.Handle> secondaryHandles = new java.util.ArrayList<>();

    private boolean handlesAreValid = true;

    private int detailLevel;

    private org.jhotdraw.draw.DrawingEditor editor;

    private javax.swing.JLabel emptyDrawingLabel;

    private boolean paintBackground = true;

    protected java.awt.image.BufferedImage backgroundTile;

    private final org.jhotdraw.draw.event.FigureListener handleInvalidator = new org.jhotdraw.draw.event.FigureListenerAdapter() {
        @java.lang.Override
        public void figureHandlesChanged(org.jhotdraw.draw.event.FigureEvent e) {
            invalidateHandles();
        }
    };

    public boolean isPaintBackground() {
        return paintBackground;
    }

    public void setPaintBackground(boolean paintBackground) {
        this.paintBackground = paintBackground;
    }

    private boolean paintEnabled = true;

    @java.lang.Override
    public void repaintHandles() {
        validateHandles();
        java.awt.Rectangle r = null;
        for (org.jhotdraw.draw.handle.Handle h : getSelectionHandles()) {
            if (r == null) {
                r = h.getDrawingArea();
            } else {
                r.add(h.getDrawingArea());
            }
        }
        for (org.jhotdraw.draw.handle.Handle h : getSecondaryHandles()) {
            if (r == null) {
                r = h.getDrawingArea();
            } else {
                r.add(h.getDrawingArea());
            }
        }
        if (r != null) {
            repaint(r);
        }
    }

    protected void drawBackground(java.awt.Graphics2D g) {
        if (drawing == null) {
            // there is no drawing and thus no canvas
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if ((drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH) == null) || (drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT) == null)) {
            // the canvas is infinitely large
            java.awt.Color canvasColor = drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR);
            double canvasOpacity = drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY);
            if (canvasColor != null) {
                if (canvasOpacity == 1) {
                    g.setColor(new java.awt.Color(canvasColor.getRGB()));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    java.awt.Point r = drawingToView(new java.awt.geom.Point2D.Double(0, 0));
                    g.setPaint(getBackgroundPaint(r.x, r.y));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(new java.awt.Color((canvasColor.getRGB() & 0xfffff) | (((int) (canvasOpacity * 256)) << 24), true));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            } else {
                java.awt.Point r = drawingToView(new java.awt.geom.Point2D.Double(0, 0));
                g.setPaint(getBackgroundPaint(r.x, r.y));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        } else {
            // the canvas has a fixed size
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            java.awt.Rectangle r = drawingToView(new java.awt.geom.Rectangle2D.Double(0, 0, drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH), drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT)));
            g.setPaint(getBackgroundPaint(r.x, r.y));
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    @java.lang.Override
    public boolean isSelectionEmpty() {
        return SELECTED_FIGURES.isEmpty();
    }

    protected class EventHandler implements org.jhotdraw.draw.event.DrawingListener , org.jhotdraw.draw.event.HandleListener , java.awt.event.FocusListener {
        @java.lang.Override
        public void figureAdded(org.jhotdraw.draw.event.DrawingEvent evt) {
            if ((drawing.getChildCount() == 1) && (getEmptyDrawingMessage() != null)) {
                repaint();
            } else {
                repaintDrawingArea(evt.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
            }
        }

        @java.lang.Override
        public void figureRemoved(org.jhotdraw.draw.event.DrawingEvent evt) {
            if ((drawing.getChildCount() == 0) && (getEmptyDrawingMessage() != null)) {
                repaint();
            } else {
                repaintDrawingArea(evt.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
            }
            removeFromSelection(evt.getFigure());
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.HandleEvent evt) {
            repaint(evt.getInvalidatedArea());
        }

        @java.lang.Override
        public void handleRequestSecondaryHandles(org.jhotdraw.draw.event.HandleEvent e) {
            secondaryHandleOwner = e.getHandle();
            secondaryHandles.clear();
            secondaryHandles.addAll(secondaryHandleOwner.createSecondaryHandles());
            for (org.jhotdraw.draw.handle.Handle h : secondaryHandles) {
                h.setView(AbstractDrawingView.this);
                h.addHandleListener(eventHandler);
            }
            repaint();
        }

        @java.lang.Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (editor != null) {
                editor.setActiveView(AbstractDrawingView.this);
            }
        }

        @java.lang.Override
        public void focusLost(java.awt.event.FocusEvent e) {
        }

        @java.lang.Override
        public void handleRequestRemove(org.jhotdraw.draw.event.HandleEvent e) {
            selectionHandles.remove(e.getHandle());
            e.getHandle().dispose();
            invalidateHandles();
            repaint(e.getInvalidatedArea());
        }

        @java.lang.Override
        public void drawingAttributeChanged(org.jhotdraw.draw.event.DrawingEvent e) {
            if (e.getSource() == drawing) {
                org.jhotdraw.draw.AttributeKey<?> a = e.getAttribute();
                if (a.equals(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT) || a.equals(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH)) {
                    repaint();
                }
                if (e.getInvalidatedArea() != null) {
                    repaintDrawingArea(e.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
                } else {
                    repaintDrawingArea(viewToDrawing(getCanvasViewBounds()));
                }
            } else {
                // this view should not invalidate its area from foreign drawings changes
                // if (e.getInvalidatedArea() != null) {
                // repaintDrawingArea(
                // e.getFigure()
                // 
                // .getDrawingArea(AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
                // }
            }
        }

        @java.lang.Override
        public void drawingChanged(org.jhotdraw.draw.event.DrawingEvent e) {
            repaintDrawingArea(e.getInvalidatedArea());
        }
    }

    private final org.jhotdraw.draw.AbstractDrawingView.EventHandler eventHandler = new org.jhotdraw.draw.AbstractDrawingView.EventHandler();

    public AbstractDrawingView() {
        com.sun.java.accessibility.util.AWTEventMonitor.addFocusListener(eventHandler);
    }

    @java.lang.Override
    public org.jhotdraw.draw.Drawing getDrawing() {
        return drawing;
    }

    public java.lang.String getToolTipText(java.awt.event.MouseEvent evt) {
        if ((getEditor() != null) && (getEditor().getTool() != null)) {
            return getEditor().getTool().getToolTipText(this, evt);
        }
        return null;
    }

    public void setEmptyDrawingMessage(java.lang.String newValue) {
        java.lang.String oldValue = (emptyDrawingLabel == null) ? null : emptyDrawingLabel.getText();
        if (newValue == null) {
            emptyDrawingLabel = null;
        } else {
            emptyDrawingLabel = new javax.swing.JLabel(newValue);
            emptyDrawingLabel.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        }
        firePropertyChange("emptyDrawingMessage", oldValue, newValue);
        repaint();
    }

    public java.lang.String getEmptyDrawingMessage() {
        return emptyDrawingLabel == null ? null : emptyDrawingLabel.getText();
    }

    /**
     * Paints the drawing view. Uses rendering hints for fast painting. Paints the canvasColor, the
     * grid, the drawing, the handles and the current tool. TODO: Rename to reflect this is not a
     * Swing or AWT method.
     */
    public void paintComponent(java.awt.Graphics gr) {
        if (!isPaintEnabled()) {
            return;
        }
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        setViewRenderingHints(g);
        if (isPaintBackground()) {
            drawBackground(g);
        }
        drawCanvas(g);
        drawConstrainer(g);
        drawDrawing(g);
        drawHandles(g);
        drawTool(g);
    }

    /**
     * Prints the drawing view. Uses high quality rendering hints for printing. Only prints the
     * drawing. Doesn't print the canvasColor, the grid, the handles and the tool. TODO: rename to
     * reflect that this is not swing or awt
     */
    public void printComponent(java.awt.Graphics gr) {
        if (!isPaintEnabled()) {
            return;
        }
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        // Set rendering hints for quality
        g.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawDrawing(g);
    }

    protected void setViewRenderingHints(java.awt.Graphics2D g) {
        // Set rendering hints for speed
        g.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Returns the bounds of the canvas on the drawing view.
     *
     * @return The current bounds of the canvas on the drawing view.
     */
    protected java.awt.Rectangle getCanvasViewBounds() {
        // Position of the zero coordinate point on the view
        int x = 0;
        int y = 0;
        int w = getWidth();
        int h = getHeight();
        if (getDrawing() != null) {
            java.lang.Double cw = getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH);
            java.lang.Double ch = getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT);
            if ((cw != null) && (ch != null)) {
                java.awt.Point lowerRight = drawingToView(new java.awt.geom.Point2D.Double(cw, ch));
                w = lowerRight.x - x;
                h = lowerRight.y - y;
            }
        }
        return new java.awt.Rectangle(x, y, w, h);
    }

    /**
     * Draws the canvas. If the {@code AttributeKeys.CANVAS_FILL_OPACITY} is not fully opaque, the
     * canvas area is filled with the background paint before the {@code AttributeKeys.CANVAS_FILL_COLOR} is drawn.
     */
    protected void drawCanvas(java.awt.Graphics2D gr) {
        if (drawing != null) {
            java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr.create()));
            java.awt.geom.AffineTransform tx = g.getTransform();
            g.setTransform(tx);
            drawing.setFontRenderContext(g.getFontRenderContext());
            drawing.drawCanvas(g);
            g.dispose();
        }
    }

    protected void drawConstrainer(java.awt.Graphics2D g) {
        if (getConstrainer() != null) {
            java.awt.Shape clip = g.getClip();
            java.awt.Rectangle r = getCanvasViewBounds();
            g.clipRect(r.x, r.y, r.width, r.height);
            getConstrainer().draw(g, this);
            g.setClip(clip);
        }
    }

    protected void drawDrawing(java.awt.Graphics2D gr) {
        if (drawing != null) {
            if ((drawing.getChildCount() == 0) && (emptyDrawingLabel != null)) {
                emptyDrawingLabel.setBounds(0, 0, getWidth(), getHeight());
                emptyDrawingLabel.paint(gr);
            } else {
                java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr.create()));
                java.awt.geom.AffineTransform tx = g.getTransform();
                if (getDrawingToViewTransform() != null) {
                    tx.concatenate(getDrawingToViewTransform());
                }
                g.setTransform(tx);
                drawing.setFontRenderContext(g.getFontRenderContext());
                drawing.draw(g);
                g.dispose();
            }
        }
    }

    protected void drawHandles(java.awt.Graphics2D g) {
        if ((editor != null) && (editor.getActiveView() == this)) {
            validateHandles();
            for (org.jhotdraw.draw.handle.Handle h : getSelectionHandles()) {
                h.draw(g);
            }
            for (org.jhotdraw.draw.handle.Handle h : getSecondaryHandles()) {
                h.draw(g);
            }
        }
    }

    protected void drawTool(java.awt.Graphics2D g) {
        if (((editor != null) && (editor.getActiveView() == this)) && (editor.getTool() != null)) {
            editor.getTool().draw(g);
        }
    }

    @java.lang.Override
    public void setDrawing(org.jhotdraw.draw.Drawing newValue) {
        org.jhotdraw.draw.Drawing oldValue = drawing;
        if (this.drawing != null) {
            this.drawing.removeDrawingListener(eventHandler);
            clearSelection();
        }
        this.drawing = newValue;
        if (this.drawing != null) {
            this.drawing.addDrawingListener(eventHandler);
        }
        firePropertyChange(org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY, oldValue, newValue);
        // Revalidate without flickering
        revalidate();
        paintEnabled = false;
        javax.swing.Timer t = new javax.swing.Timer(10, new java.awt.event.ActionListener() {
            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                repaint();
                paintEnabled = true;
            }
        });
        t.setRepeats(false);
        t.start();
    }

    protected void repaintDrawingArea(java.awt.geom.Rectangle2D.Double r) {
        java.awt.Rectangle vr = drawingToView(r);
        vr.grow(2, 2);
        repaint(vr);
    }

    /**
     * Adds a figure to the current selection.
     */
    @java.lang.Override
    public void addToSelection(org.jhotdraw.draw.figure.Figure figure) {
        java.util.Set<org.jhotdraw.draw.figure.Figure> oldSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        if (SELECTED_FIGURES.add(figure)) {
            figure.addFigureListener(handleInvalidator);
            java.util.Set<org.jhotdraw.draw.figure.Figure> newSelection = new java.util.HashSet<>(SELECTED_FIGURES);
            java.awt.Rectangle invalidatedArea = null;
            if (handlesAreValid && (getEditor() != null)) {
                for (org.jhotdraw.draw.handle.Handle h : figure.createHandles(detailLevel)) {
                    h.setView(this);
                    selectionHandles.add(h);
                    h.addHandleListener(eventHandler);
                    if (invalidatedArea == null) {
                        invalidatedArea = h.getDrawingArea();
                    } else {
                        invalidatedArea.add(h.getDrawingArea());
                    }
                }
            }
            fireSelectionChanged(oldSelection, newSelection);
            if (invalidatedArea != null) {
                repaint(invalidatedArea);
            }
        }
    }

    /**
     * Adds a collection of figures to the current selection.
     */
    @java.lang.Override
    public void addToSelection(java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
        java.util.Set<org.jhotdraw.draw.figure.Figure> oldSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        java.util.Set<org.jhotdraw.draw.figure.Figure> newSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        boolean selectionChanged = false;
        java.awt.Rectangle invalidatedArea = null;
        for (org.jhotdraw.draw.figure.Figure figure : figures) {
            if (SELECTED_FIGURES.add(figure)) {
                selectionChanged = true;
                newSelection.add(figure);
                figure.addFigureListener(handleInvalidator);
                if (handlesAreValid && (getEditor() != null)) {
                    for (org.jhotdraw.draw.handle.Handle h : figure.createHandles(detailLevel)) {
                        h.setView(this);
                        selectionHandles.add(h);
                        h.addHandleListener(eventHandler);
                        if (invalidatedArea == null) {
                            invalidatedArea = h.getDrawingArea();
                        } else {
                            invalidatedArea.add(h.getDrawingArea());
                        }
                    }
                }
            }
        }
        if (selectionChanged) {
            fireSelectionChanged(oldSelection, newSelection);
            if (invalidatedArea != null) {
                repaint(invalidatedArea);
            }
        }
    }

    /**
     * Removes a figure from the selection.
     */
    @java.lang.Override
    public void removeFromSelection(org.jhotdraw.draw.figure.Figure figure) {
        java.util.Set<org.jhotdraw.draw.figure.Figure> oldSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        if (SELECTED_FIGURES.remove(figure)) {
            java.util.Set<org.jhotdraw.draw.figure.Figure> newSelection = new java.util.HashSet<>(SELECTED_FIGURES);
            invalidateHandles();
            figure.removeFigureListener(handleInvalidator);
            fireSelectionChanged(oldSelection, newSelection);
            repaint();
        }
    }

    /**
     * If a figure isn't selected it is added to the selection. Otherwise it is removed from the
     * selection.
     */
    @java.lang.Override
    public void toggleSelection(org.jhotdraw.draw.figure.Figure figure) {
        if (SELECTED_FIGURES.contains(figure)) {
            removeFromSelection(figure);
        } else {
            addToSelection(figure);
        }
    }

    /**
     * Selects all selectable figures.
     */
    @java.lang.Override
    public void selectAll() {
        java.util.Set<org.jhotdraw.draw.figure.Figure> oldSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        SELECTED_FIGURES.clear();
        for (org.jhotdraw.draw.figure.Figure figure : drawing.getChildren()) {
            if (figure.isSelectable()) {
                SELECTED_FIGURES.add(figure);
            }
        }
        java.util.Set<org.jhotdraw.draw.figure.Figure> newSelection = new java.util.HashSet<>(SELECTED_FIGURES);
        invalidateHandles();
        fireSelectionChanged(oldSelection, newSelection);
        repaint();
    }

    /**
     * Clears the current selection.
     */
    @java.lang.Override
    public void clearSelection() {
        if (getSelectionCount() > 0) {
            java.util.Set<org.jhotdraw.draw.figure.Figure> oldSelection = new java.util.HashSet<>(SELECTED_FIGURES);
            SELECTED_FIGURES.clear();
            java.util.Set<org.jhotdraw.draw.figure.Figure> newSelection = new java.util.HashSet<>(SELECTED_FIGURES);
            invalidateHandles();
            fireSelectionChanged(oldSelection, newSelection);
        }
    }

    /**
     * Test whether a given figure is selected.
     */
    @java.lang.Override
    public boolean isFigureSelected(org.jhotdraw.draw.figure.Figure checkFigure) {
        return SELECTED_FIGURES.contains(checkFigure);
    }

    @java.lang.Override
    public java.util.Set<org.jhotdraw.draw.figure.Figure> getSelectedFigures() {
        return UNMODIFIABLE_SELECTED_FIGURES;
    }

    /**
     * Gets the number of selected figures.
     */
    @java.lang.Override
    public int getSelectionCount() {
        return SELECTED_FIGURES.size();
    }

    /**
     * Gets the currently active selection handles.
     */
    private java.util.List<org.jhotdraw.draw.handle.Handle> getSelectionHandles() {
        validateHandles();
        return java.util.Collections.unmodifiableList(selectionHandles);
    }

    /**
     * Gets the currently active secondary handles.
     */
    private java.util.List<org.jhotdraw.draw.handle.Handle> getSecondaryHandles() {
        validateHandles();
        return java.util.Collections.unmodifiableList(secondaryHandles);
    }

    /**
     * Invalidates the handles.
     */
    private void invalidateHandles() {
        if (handlesAreValid) {
            handlesAreValid = false;
            java.awt.Rectangle invalidatedArea = null;
            for (org.jhotdraw.draw.handle.Handle handle : selectionHandles) {
                handle.removeHandleListener(eventHandler);
                if (invalidatedArea == null) {
                    invalidatedArea = handle.getDrawingArea();
                } else {
                    invalidatedArea.add(handle.getDrawingArea());
                }
                handle.dispose();
            }
            for (org.jhotdraw.draw.handle.Handle handle : secondaryHandles) {
                handle.removeHandleListener(eventHandler);
                if (invalidatedArea == null) {
                    invalidatedArea = handle.getDrawingArea();
                } else {
                    invalidatedArea.add(handle.getDrawingArea());
                }
                handle.dispose();
            }
            selectionHandles.clear();
            secondaryHandles.clear();
            setActiveHandle(null);
            if (invalidatedArea != null) {
                repaint(invalidatedArea);
            }
        }
    }

    /**
     * Validates the handles.
     */
    private void validateHandles() {
        // Validate handles only, if they are invalid, and if
        // the DrawingView has a DrawingEditor.
        if ((!handlesAreValid) && (getEditor() != null)) {
            handlesAreValid = true;
            selectionHandles.clear();
            java.awt.Rectangle invalidatedArea = null;
            while (true) {
                for (org.jhotdraw.draw.figure.Figure figure : getSelectedFigures()) {
                    for (org.jhotdraw.draw.handle.Handle handle : figure.createHandles(detailLevel)) {
                        handle.setView(this);
                        selectionHandles.add(handle);
                        handle.addHandleListener(eventHandler);
                        if (invalidatedArea == null) {
                            invalidatedArea = handle.getDrawingArea();
                        } else {
                            invalidatedArea.add(handle.getDrawingArea());
                        }
                    }
                }
                if (selectionHandles.isEmpty() && (detailLevel != 0)) {
                    // No handles are available at the desired detail level.
                    // Retry with detail level 0.
                    detailLevel = 0;
                    continue;
                }
                break;
            } 
            if (invalidatedArea != null) {
                repaint(invalidatedArea);
            }
        }
    }

    /**
     * Finds a handle at a given coordinates.
     *
     * @return A handle, null if no handle is found.
     */
    @java.lang.Override
    public org.jhotdraw.draw.handle.Handle findHandle(java.awt.Point p) {
        validateHandles();
        for (org.jhotdraw.draw.handle.Handle handle : new org.jhotdraw.util.ReversedList<>(getSecondaryHandles())) {
            if (handle.contains(p)) {
                return handle;
            }
        }
        for (org.jhotdraw.draw.handle.Handle handle : new org.jhotdraw.util.ReversedList<>(getSelectionHandles())) {
            if (handle.contains(p)) {
                return handle;
            }
        }
        return null;
    }

    /**
     * Gets compatible handles.
     *
     * @return A collection containing the handle and all compatible handles.
     */
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> getCompatibleHandles(org.jhotdraw.draw.handle.Handle master) {
        validateHandles();
        java.util.HashSet<org.jhotdraw.draw.figure.Figure> owners = new java.util.HashSet<>();
        java.util.List<org.jhotdraw.draw.handle.Handle> compatibleHandles = new java.util.ArrayList<>();
        owners.add(master.getOwner());
        compatibleHandles.add(master);
        for (org.jhotdraw.draw.handle.Handle handle : getSelectionHandles()) {
            if ((!owners.contains(handle.getOwner())) && handle.isCombinableWith(master)) {
                owners.add(handle.getOwner());
                compatibleHandles.add(handle);
            }
        }
        return compatibleHandles;
    }

    /**
     * Finds a figure at a given coordinates.
     *
     * @return A figure, null if no figure is found.
     */
    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.Point p) {
        return drawing == null ? null : drawing.findFigure(viewToDrawing(p), getScaleFactor());
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.figure.Figure> findFigures(java.awt.Rectangle r) {
        return drawing == null ? java.util.Collections.emptyList() : drawing.findFigures(viewToDrawing(r));
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.figure.Figure> findFiguresWithin(java.awt.Rectangle r) {
        return drawing == null ? java.util.Collections.emptyList() : drawing.findFiguresWithin(viewToDrawing(r));
    }

    @java.lang.Override
    public void addFigureSelectionListener(org.jhotdraw.draw.event.FigureSelectionListener fsl) {
        listenerList.add(org.jhotdraw.draw.event.FigureSelectionListener.class, fsl);
    }

    @java.lang.Override
    public void removeFigureSelectionListener(org.jhotdraw.draw.event.FigureSelectionListener fsl) {
        listenerList.remove(org.jhotdraw.draw.event.FigureSelectionListener.class, fsl);
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type. Also
     * notify listeners who listen for {@link EditableComponent#SELECTION_EMPTY_PROPERTY}.
     */
    protected void fireSelectionChanged(java.util.Set<org.jhotdraw.draw.figure.Figure> oldValue, java.util.Set<org.jhotdraw.draw.figure.Figure> newValue) {
        if (listenerList.getListenerCount() > 0) {
            org.jhotdraw.draw.event.FigureSelectionEvent event = null;
            // Notify all listeners that have registered interest for
            // Guaranteed to return a non-null array
            java.lang.Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == org.jhotdraw.draw.event.FigureSelectionListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new org.jhotdraw.draw.event.FigureSelectionEvent(this, oldValue, newValue);
                    }
                    ((org.jhotdraw.draw.event.FigureSelectionListener) (listeners[i + 1])).selectionChanged(event);
                }
            }
        }
        firePropertyChange(org.jhotdraw.api.gui.EditableComponent.SELECTION_EMPTY_PROPERTY, oldValue.isEmpty(), newValue.isEmpty());
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.Constrainer getConstrainer() {
        return isConstrainerVisible() ? visibleConstrainer : invisibleConstrainer;
    }

    protected java.awt.geom.Rectangle2D.Double getDrawingArea() {
        return ((java.awt.geom.Rectangle2D.Double) (drawing.getDrawingArea().clone()));
    }

    /**
     * Converts drawing coordinates to view coordinates.
     */
    @java.lang.Override
    public java.awt.Point drawingToView(java.awt.geom.Point2D.Double p) {
        java.awt.geom.AffineTransform transform = getDrawingToViewTransform();
        java.awt.geom.Point2D pnt = transform.transform(p, null);
        return new java.awt.Point(((int) (pnt.getX())), ((int) (pnt.getY())));
    }

    /**
     * Minimal rectangle that encloses drawing rectangle (could be rotated).
     *
     * @param r
     * @return  */
    @java.lang.Override
    public java.awt.Rectangle drawingToView(java.awt.geom.Rectangle2D.Double r) {
        java.awt.Point pnt = drawingToView(new java.awt.geom.Point2D.Double(r.getMinX(), r.getMinY()));
        java.awt.Rectangle rect = new java.awt.Rectangle(pnt.x, pnt.y, 1, 1);
        rect.add(drawingToView(new java.awt.geom.Point2D.Double(r.getMaxX(), r.getMinY())));
        rect.add(drawingToView(new java.awt.geom.Point2D.Double(r.getMaxX(), r.getMaxY())));
        rect.add(drawingToView(new java.awt.geom.Point2D.Double(r.getMinX(), r.getMaxY())));
        return rect;
    }

    /**
     * Converts view coordinates to drawing coordinates.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double viewToDrawing(java.awt.Point p) {
        try {
            java.awt.geom.AffineTransform transform = getDrawingToViewTransform().createInverse();
            java.awt.geom.Point2D.Double pint = new java.awt.geom.Point2D.Double();
            transform.transform(p, pint);
            return pint;
        } catch (java.awt.geom.NoninvertibleTransformException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.draw.AbstractDrawingView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Rectangles are not rotated. Therefore we deliver the smallest rectangle that encloses the
     * rotated target rectangle.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double viewToDrawing(java.awt.Rectangle r) {
        java.awt.geom.Point2D.Double pnt = viewToDrawing(new java.awt.Point(((int) (r.getMinX())), ((int) (r.getMinY()))));
        java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double(pnt.x, pnt.y, 0, 0);
        rect.add(viewToDrawing(new java.awt.Point(((int) (r.getMaxX())), ((int) (r.getMaxY())))));
        return rect;
    }

    public void fireViewTransformChanged() {
        for (org.jhotdraw.draw.handle.Handle handle : selectionHandles) {
            handle.viewTransformChanged();
        }
        for (org.jhotdraw.draw.handle.Handle handle : secondaryHandles) {
            handle.viewTransformChanged();
        }
    }

    @java.lang.Override
    public void setHandleDetailLevel(int newValue) {
        if (newValue != detailLevel) {
            detailLevel = newValue;
            invalidateHandles();
            validateHandles();
        }
    }

    @java.lang.Override
    public int getHandleDetailLevel() {
        return detailLevel;
    }

    @java.lang.Override
    public abstract java.awt.geom.AffineTransform getDrawingToViewTransform();

    @java.lang.Override
    public void delete() {
        final java.util.List<org.jhotdraw.draw.figure.Figure> deletedFigures = drawing.sort(getSelectedFigures());
        for (org.jhotdraw.draw.figure.Figure f : deletedFigures) {
            if (!f.isRemovable()) {
                return;
            }
        }
        // Get z-indices of deleted figures
        final int[] deletedFigureIndices = new int[deletedFigures.size()];
        for (int i = 0; i < deletedFigureIndices.length; i++) {
            deletedFigureIndices[i] = drawing.indexOf(deletedFigures.get(i));
        }
        clearSelection();
        drawing.removeAll(deletedFigures);
        drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("edit.delete.text");
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                clearSelection();
                org.jhotdraw.draw.Drawing d = drawing;
                for (int i = 0; i < deletedFigureIndices.length; i++) {
                    d.add(deletedFigureIndices[i], deletedFigures.get(i));
                }
                addToSelection(deletedFigures);
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                for (int i = 0; i < deletedFigureIndices.length; i++) {
                    drawing.remove(deletedFigures.get(i));
                }
            }
        });
    }

    @java.lang.Override
    public void duplicate() {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> sorted = drawing.sort(getSelectedFigures());
        java.util.HashMap<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> originalToDuplicateMap = new java.util.HashMap<>(sorted.size());
        clearSelection();
        final java.util.ArrayList<org.jhotdraw.draw.figure.Figure> duplicates = new java.util.ArrayList<>(sorted.size());
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(5, 5);
        for (org.jhotdraw.draw.figure.Figure f : sorted) {
            org.jhotdraw.draw.figure.Figure d = f.clone();
            d.transform(tx);
            duplicates.add(d);
            originalToDuplicateMap.put(f, d);
            drawing.add(d);
        }
        for (org.jhotdraw.draw.figure.Figure f : duplicates) {
            f.remap(originalToDuplicateMap, false);
        }
        addToSelection(duplicates);
        drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("edit.duplicate.text");
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                drawing.removeAll(duplicates);
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                drawing.addAll(duplicates);
            }
        });
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.DrawingEditor editor) {
        this.editor = null;
        repaint();
    }

    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.draw.DrawingEditor oldValue = editor;
        this.editor = editor;
        firePropertyChange("editor", oldValue, editor);
        invalidateHandles();
        repaint();
    }

    @java.lang.Override
    public void setVisibleConstrainer(org.jhotdraw.draw.constrainer.Constrainer newValue) {
        org.jhotdraw.draw.constrainer.Constrainer oldValue = visibleConstrainer;
        visibleConstrainer = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingView.VISIBLE_CONSTRAINER_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.Constrainer getVisibleConstrainer() {
        return visibleConstrainer;
    }

    @java.lang.Override
    public void setInvisibleConstrainer(org.jhotdraw.draw.constrainer.Constrainer newValue) {
        org.jhotdraw.draw.constrainer.Constrainer oldValue = invisibleConstrainer;
        invisibleConstrainer = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingView.INVISIBLE_CONSTRAINER_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.Constrainer getInvisibleConstrainer() {
        return invisibleConstrainer;
    }

    @java.lang.Override
    public void setConstrainerVisible(boolean newValue) {
        boolean oldValue = isConstrainerVisible;
        isConstrainerVisible = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingView.CONSTRAINER_VISIBLE_PROPERTY, oldValue, newValue);
        repaint();
    }

    @java.lang.Override
    public boolean isConstrainerVisible() {
        return isConstrainerVisible;
    }

    /**
     * Returns a paint for drawing the background of the drawing area.
     *
     * @return Paint.
     */
    protected java.awt.Paint getBackgroundPaint(int x, int y) {
        if (backgroundTile == null) {
            backgroundTile = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g = backgroundTile.createGraphics();
            g.setColor(java.awt.Color.white);
            g.fillRect(0, 0, 16, 16);
            g.setColor(new java.awt.Color(0xdfdfdf));
            g.fillRect(0, 0, 8, 8);
            g.fillRect(8, 8, 8, 8);
            g.dispose();
        }
        return new java.awt.TexturePaint(backgroundTile, new java.awt.Rectangle(x, y, backgroundTile.getWidth(), backgroundTile.getHeight()));
    }

    @java.lang.Override
    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    @java.lang.Override
    public void setActiveHandle(org.jhotdraw.draw.handle.Handle newValue) {
        org.jhotdraw.draw.handle.Handle oldValue = activeHandle;
        if (oldValue != null) {
            repaint(oldValue.getDrawingArea());
        }
        activeHandle = newValue;
        if (newValue != null) {
            repaint(newValue.getDrawingArea());
        }
        firePropertyChange(org.jhotdraw.draw.DrawingView.ACTIVE_HANDLE_PROPERTY, oldValue, newValue);
    }

    private void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.draw.handle.Handle getActiveHandle() {
        return activeHandle;
    }

    public boolean isPaintEnabled() {
        return paintEnabled;
    }

    @java.lang.Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @java.lang.Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public abstract void repaint(java.awt.Rectangle r);

    public abstract java.awt.Color getBackground();

    public abstract void repaint();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void revalidate();
}