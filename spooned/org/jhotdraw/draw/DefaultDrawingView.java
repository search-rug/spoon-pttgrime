/* @(#)DefaultDrawingView.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
/**
 * A default implementation of {@link DrawingView} suited for viewing drawings with a small number
 * of figures.
 *
 * <p>
 *
 * <p>FIXME - Implement clone Method. FIXME - Use double buffering for the drawing to improve
 * performance.
 */
public class DefaultDrawingView extends javax.swing.JComponent implements org.jhotdraw.draw.DrawingView , org.jhotdraw.api.gui.EditableComponent {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.Drawing drawing;

    /**
     * Holds the selected figures in an ordered put. The ordering reflects the sequence that was used
     * to select the figures.
     */
    private final java.util.Set<org.jhotdraw.draw.figure.Figure> SELECTED_FIGURES = new java.util.LinkedHashSet<>();

    private final java.util.Set<org.jhotdraw.draw.figure.Figure> UNMODIFIABLE_SELECTED_FIGURES = java.util.Collections.unmodifiableSet(SELECTED_FIGURES);

    private java.util.List<org.jhotdraw.draw.handle.Handle> selectionHandles = new java.util.ArrayList<>();

    private boolean isConstrainerVisible = false;

    private org.jhotdraw.draw.constrainer.Constrainer visibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer(8, 8);

    private org.jhotdraw.draw.constrainer.Constrainer invisibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer();

    private org.jhotdraw.draw.handle.Handle secondaryHandleOwner;

    private org.jhotdraw.draw.handle.Handle activeHandle;

    private java.util.List<org.jhotdraw.draw.handle.Handle> secondaryHandles = new java.util.ArrayList<>();

    private boolean handlesAreValid = true;

    private transient java.awt.Dimension cachedPreferredSize;

    private double scaleFactor = 1;

    private java.awt.Point translation = new java.awt.Point(0, 0);

    private int detailLevel;

    private org.jhotdraw.draw.DrawingEditor editor;

    private javax.swing.JLabel emptyDrawingLabel;

    protected java.awt.image.BufferedImage backgroundTile;

    private org.jhotdraw.draw.event.FigureListener handleInvalidator = new org.jhotdraw.draw.event.FigureListenerAdapter() {
        @java.lang.Override
        public void figureHandlesChanged(org.jhotdraw.draw.event.FigureEvent e) {
            invalidateHandles();
        }
    };

    private transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    public static final java.lang.String DRAWING_DOUBLE_BUFFERED_PROPERTY = "drawingDoubleBuffered";

    /**
     * Whether the drawing is double buffered
     */
    private boolean isDrawingDoubleBuffered = true;

    /**
     * The drawingBuffer holds a rendered image of the drawing (in view coordinates).
     */
    private java.awt.image.VolatileImage drawingBufferV;

    /**
     * The drawingBuffer holds a rendered image of the drawing (in view coordinates).
     */
    private java.awt.image.BufferedImage drawingBufferNV;

    /**
     * Holds the drawing area (in view coordinates) which is in the drawing buffer.
     */
    private java.awt.Rectangle bufferedArea = new java.awt.Rectangle();

    /**
     * Holds the drawing area (in view coordinates) which has not been redrawn yet in the drawing
     * buffer.
     */
    private java.awt.Rectangle dirtyArea = new java.awt.Rectangle(0, 0, -1, -1);

    private boolean paintEnabled = true;

    private static final boolean IS_WINDOWS;

    static {
        boolean b = false;
        try {
            if (java.lang.System.getProperty("os.name").toLowerCase().startsWith("win")) {
                b = true;
            }
        } catch (java.lang.Throwable t) {
            // allow empty
        }
        IS_WINDOWS = b;
    }

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

    /**
     * Draws the background of the drawing view.
     */
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
        public void drawingChanged(org.jhotdraw.draw.event.DrawingEvent e) {
            repaintDrawingArea(e.getInvalidatedArea());
            invalidateDimension();
        }

        @java.lang.Override
        public void figureAdded(org.jhotdraw.draw.event.DrawingEvent evt) {
            if ((drawing.getChildCount() == 1) && (getEmptyDrawingMessage() != null)) {
                repaint();
            } else {
                repaintDrawingArea(evt.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
            }
            invalidateDimension();
        }

        @java.lang.Override
        public void figureRemoved(org.jhotdraw.draw.event.DrawingEvent evt) {
            if ((drawing.getChildCount() == 0) && (getEmptyDrawingMessage() != null)) {
                repaint();
            } else {
                repaintDrawingArea(evt.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
            }
            removeFromSelection(evt.getFigure());
            invalidateDimension();
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.HandleEvent evt) {
            repaint(evt.getInvalidatedArea());
            invalidateDimension();
        }

        @java.lang.Override
        public void handleRequestSecondaryHandles(org.jhotdraw.draw.event.HandleEvent e) {
            secondaryHandleOwner = e.getHandle();
            secondaryHandles.clear();
            secondaryHandles.addAll(secondaryHandleOwner.createSecondaryHandles());
            for (org.jhotdraw.draw.handle.Handle h : secondaryHandles) {
                h.setView(DefaultDrawingView.this);
                h.addHandleListener(eventHandler);
            }
            repaint();
        }

        @java.lang.Override
        public void focusGained(java.awt.event.FocusEvent e) {
            // repaintHandles();
            if (editor != null) {
                editor.setActiveView(DefaultDrawingView.this);
            }
        }

        @java.lang.Override
        public void focusLost(java.awt.event.FocusEvent e) {
            // repaintHandles();
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
                    validateViewTranslation();
                    repaint();// must repaint everything

                }
                if (e.getInvalidatedArea() != null) {
                    repaintDrawingArea(e.getFigure().getDrawingArea(org.jhotdraw.draw.AttributeKeys.getScaleFactor(getDrawingToViewTransform())));
                } else {
                    repaintDrawingArea(viewToDrawing(getCanvasViewBounds()));
                }
            } else {
                // this view should not invalidate its area from foreign drawings changes
                // if (e.getInvalidatedArea() != null) {
                // repaintDrawingArea(e.getInvalidatedArea());
            }
        }
    }

    private org.jhotdraw.draw.DefaultDrawingView.EventHandler eventHandler;

    /**
     * Creates new instance.
     */
    public DefaultDrawingView() {
        initComponents();
        eventHandler = createEventHandler();
        setToolTipText("dummy");// Set a dummy tool tip text to turn tooltips on

        setFocusable(true);
        addFocusListener(eventHandler);
        setTransferHandler(new org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler());
        setBackground(new java.awt.Color(0xb0b0b0));
        setOpaque(true);
    }

    protected org.jhotdraw.draw.DefaultDrawingView.EventHandler createEventHandler() {
        return new org.jhotdraw.draw.DefaultDrawingView.EventHandler();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * <p>
     *
     * <p>WARNING: Do NOT modify this code. The content of this method is always regenerated by the
     * Form Editor.
     *
     * <p>
     *
     * <p>NOTE: To prevent undesired layout effects when using floating text fields, the
     * DefaultDrawingView must not use a layout manager.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents


    @java.lang.Override
    public org.jhotdraw.draw.Drawing getDrawing() {
        return drawing;
    }

    @java.lang.Override
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
     * grid, the drawing, the handles and the current tool.
     */
    @java.lang.Override
    public void paintComponent(java.awt.Graphics gr) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        setViewRenderingHints(g);
        drawBackground(g);
        drawCanvas(g);
        drawConstrainer(g);
        if (isDrawingDoubleBuffered()) {
            if (org.jhotdraw.draw.DefaultDrawingView.IS_WINDOWS) {
                drawDrawingNonvolatileBuffered(g);
            } else {
                drawDrawingVolatileBuffered(g);
            }
        } else {
            drawDrawing(g);
        }
        drawHandles(g);
        drawTool(g);
    }

    /**
     * Draws the drawing double buffered using a volatile image.
     */
    protected void drawDrawingVolatileBuffered(java.awt.Graphics2D g) {
        java.awt.Rectangle vr = getVisibleRect();
        java.awt.Point shift = new java.awt.Point(0, 0);
        if (bufferedArea.contains(vr) || ((bufferedArea.width >= vr.width) && (bufferedArea.height >= vr.height))) {
            // The visible rect fits into the buffered area, but may be shifted; shift the buffered area.
            shift.x = bufferedArea.x - vr.x;
            shift.y = bufferedArea.y - vr.y;
            if (shift.x > 0) {
                dirtyArea.add(new java.awt.Rectangle(bufferedArea.x - shift.x, vr.y, (shift.x + bufferedArea.width) - vr.width, bufferedArea.height));
            } else if (shift.x < 0) {
                dirtyArea.add(new java.awt.Rectangle(bufferedArea.x + vr.width, vr.y, ((-shift.x) + bufferedArea.width) - vr.width, bufferedArea.height));
            }
            if (shift.y > 0) {
                dirtyArea.add(new java.awt.Rectangle(vr.x, bufferedArea.y - shift.y, bufferedArea.width, (shift.y + bufferedArea.height) - vr.height));
            } else if (shift.y < 0) {
                dirtyArea.add(new java.awt.Rectangle(vr.x, bufferedArea.y + vr.height, bufferedArea.width, ((-shift.y) + bufferedArea.height) - vr.height));
            }
            bufferedArea.x = vr.x;
            bufferedArea.y = vr.y;
        } else {
            // The buffered drawing area does not match the visible rect;
            // resize it, and mark everything as dirty.
            bufferedArea.setBounds(vr);
            dirtyArea.setBounds(vr);
            if ((drawingBufferV != null) && ((drawingBufferV.getWidth() != vr.width) || (drawingBufferV.getHeight() != vr.height))) {
                // The dimension of the drawing buffer does not fit into the visible rect;
                // throw the buffer away.
                drawingBufferV.flush();
                drawingBufferV = null;
            }
        }
        // Update the contents of the buffer if necessary
        while (true) {
            int valid = (drawingBufferV == null) ? java.awt.image.VolatileImage.IMAGE_INCOMPATIBLE : drawingBufferV.validate(getGraphicsConfiguration());
            switch (valid) {
                case java.awt.image.VolatileImage.IMAGE_INCOMPATIBLE :
                    // old buffer doesn't work with new GraphicsConfig; (re-)create it
                    try {
                        drawingBufferV = getGraphicsConfiguration().createCompatibleVolatileImage(vr.width, vr.height, java.awt.Transparency.TRANSLUCENT);
                    } catch (java.lang.OutOfMemoryError e) {
                        drawingBufferV = null;
                    }
                    dirtyArea.setBounds(bufferedArea);
                    break;
                case java.awt.image.VolatileImage.IMAGE_RESTORED :
                    // image was restored, but buffer lost; redraw everything
                    dirtyArea.setBounds(bufferedArea);
                    break;
            }
            if (drawingBufferV == null) {
                // There is not enough memory available for a drawing buffer;
                // draw without buffering.
                drawDrawing(g);
                break;
            }
            if (!dirtyArea.isEmpty()) {
                // An area of the drawing buffer is dirty; repaint it
                java.awt.Graphics2D gBuf = drawingBufferV.createGraphics();
                setViewRenderingHints(gBuf);
                // For shifting and cleaning, we need to erase everything underneath
                gBuf.setComposite(java.awt.AlphaComposite.Src);
                // Perform shifting if needed
                if ((shift.x != 0) || (shift.y != 0)) {
                    gBuf.copyArea(java.lang.Math.max(0, -shift.x), java.lang.Math.max(0, -shift.y), drawingBufferV.getWidth() - java.lang.Math.abs(shift.x), drawingBufferV.getHeight() - java.lang.Math.abs(shift.y), shift.x, shift.y);
                    shift.x = shift.y = 0;
                }
                // Clip the dirty area
                gBuf.translate(-bufferedArea.x, -bufferedArea.y);
                gBuf.clip(dirtyArea);
                // Clear the dirty area
                gBuf.setBackground(new java.awt.Color(0x0, true));
                gBuf.clearRect(dirtyArea.x, dirtyArea.y, dirtyArea.width, dirtyArea.height);
                gBuf.setComposite(java.awt.AlphaComposite.SrcOver);
                // Repaint the dirty area
                drawDrawing(gBuf);
                gBuf.dispose();
            }
            if (!drawingBufferV.contentsLost()) {
                g.drawImage(drawingBufferV, bufferedArea.x, bufferedArea.y, null);
            }
            if (drawingBufferV.contentsLost()) {
                dirtyArea.setBounds(bufferedArea);
            } else {
                dirtyArea.setSize(-1, -1);
                break;
            }
        } 
    }

    /**
     * Draws the drawing double buffered using a buffered image.
     */
    protected void drawDrawingNonvolatileBuffered(java.awt.Graphics2D g) {
        java.awt.Rectangle vr = getVisibleRect();
        java.awt.Point shift = new java.awt.Point(0, 0);
        if (bufferedArea.contains(vr) || ((bufferedArea.width >= vr.width) && (bufferedArea.height >= vr.height))) {
            // The visible rect fits into the buffered area, but may be shifted; shift the buffered area.
            shift.x = bufferedArea.x - vr.x;
            shift.y = bufferedArea.y - vr.y;
            if (shift.x > 0) {
                dirtyArea.add(new java.awt.Rectangle(bufferedArea.x - shift.x, vr.y, (shift.x + bufferedArea.width) - vr.width, bufferedArea.height));
            } else if (shift.x < 0) {
                dirtyArea.add(new java.awt.Rectangle(bufferedArea.x + vr.width, vr.y, ((-shift.x) + bufferedArea.width) - vr.width, bufferedArea.height));
            }
            if (shift.y > 0) {
                dirtyArea.add(new java.awt.Rectangle(vr.x, bufferedArea.y - shift.y, bufferedArea.width, (shift.y + bufferedArea.height) - vr.height));
            } else if (shift.y < 0) {
                dirtyArea.add(new java.awt.Rectangle(vr.x, bufferedArea.y + vr.height, bufferedArea.width, ((-shift.y) + bufferedArea.height) - vr.height));
            }
            bufferedArea.x = vr.x;
            bufferedArea.y = vr.y;
        } else {
            // The buffered drawing area does not match the visible rect;
            // resize it, and mark everything as dirty.
            bufferedArea.setBounds(vr);
            dirtyArea.setBounds(vr);
            if ((drawingBufferNV != null) && ((drawingBufferNV.getWidth() != vr.width) || (drawingBufferNV.getHeight() != vr.height))) {
                // The dimension of the drawing buffer does not fit into the visible rect;
                // throw the buffer away.
                drawingBufferNV.flush();
                drawingBufferNV = null;
            }
        }
        // Update the contents of the buffer if necessary
        int valid = (drawingBufferNV == null) ? java.awt.image.VolatileImage.IMAGE_INCOMPATIBLE : java.awt.image.VolatileImage.IMAGE_OK;
        switch (valid) {
            case java.awt.image.VolatileImage.IMAGE_INCOMPATIBLE :
                // old buffer doesn't work with new GraphicsConfig; (re-)create it
                try {
                    drawingBufferNV = getGraphicsConfiguration().createCompatibleImage(vr.width, vr.height, java.awt.Transparency.TRANSLUCENT);
                } catch (java.lang.OutOfMemoryError e) {
                    drawingBufferNV = null;
                }
                dirtyArea.setBounds(bufferedArea);
                break;
        }
        if (drawingBufferNV == null) {
            // There is not enough memory available for a drawing buffer;
            // draw without buffering.
            drawDrawing(g);
            return;
        }
        if (!dirtyArea.isEmpty()) {
            // An area of the drawing buffer is dirty; repaint it
            java.awt.Graphics2D gBuf = drawingBufferNV.createGraphics();
            setViewRenderingHints(gBuf);
            // For shifting and cleaning, we need to erase everything underneath
            gBuf.setComposite(java.awt.AlphaComposite.Src);
            // Perform shifting if needed
            if ((shift.x != 0) || (shift.y != 0)) {
                gBuf.copyArea(java.lang.Math.max(0, -shift.x), java.lang.Math.max(0, -shift.y), drawingBufferNV.getWidth() - java.lang.Math.abs(shift.x), drawingBufferNV.getHeight() - java.lang.Math.abs(shift.y), shift.x, shift.y);
                shift.x = shift.y = 0;
            }
            // Clip the dirty area
            gBuf.translate(-bufferedArea.x, -bufferedArea.y);
            gBuf.clip(dirtyArea);
            // Clear the dirty area
            gBuf.setBackground(new java.awt.Color(0x0, true));
            gBuf.clearRect(dirtyArea.x, dirtyArea.y, dirtyArea.width, dirtyArea.height);
            gBuf.setComposite(java.awt.AlphaComposite.SrcOver);
            // Repaint the dirty area
            drawDrawing(gBuf);
            gBuf.dispose();
        }
        g.drawImage(drawingBufferNV, bufferedArea.x, bufferedArea.y, null);
        dirtyArea.setSize(-1, -1);
    }

    /**
     * Prints the drawing view. Uses high quality rendering hints for printing. Only prints the
     * drawing. Doesn't print the canvasColor, the grid, the handles and the tool.
     */
    @java.lang.Override
    public void printComponent(java.awt.Graphics gr) {
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
        int x = -translation.x;
        int y = -translation.y;
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
            tx.translate(-translation.x, -translation.y);
            tx.scale(scaleFactor, scaleFactor);
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
                tx.translate(-translation.x, -translation.y);
                tx.scale(scaleFactor, scaleFactor);
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
        dirtyArea.add(bufferedArea);
        firePropertyChange(org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY, oldValue, newValue);
        // Revalidate without flickering
        revalidate();
        validateViewTranslation();
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

    @java.lang.Override
    public void paint(java.awt.Graphics g) {
        if (paintEnabled) {
            super.paint(g);
        }
    }

    protected void repaintDrawingArea(java.awt.geom.Rectangle2D.Double r) {
        java.awt.Rectangle vr = drawingToView(r);
        vr.grow(2, 2);
        dirtyArea.add(vr);
        repaint(vr);
    }

    @java.lang.Override
    public void invalidate() {
        invalidateDimension();
        super.invalidate();
    }

    @java.lang.Override
    public void removeNotify() {
        super.removeNotify();
        if (drawingBufferNV != null) {
            drawingBufferNV.flush();
            drawingBufferNV = null;
        }
        if (drawingBufferV != null) {
            drawingBufferV.flush();
            drawingBufferV = null;
        }
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

    @java.lang.Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setCursor(java.awt.Cursor.getPredefinedCursor(b ? java.awt.Cursor.DEFAULT_CURSOR : java.awt.Cursor.WAIT_CURSOR));
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

    /**
     * Gets the current selection as a FigureSelection. A FigureSelection can be cut, copied, pasted.
     */
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
                if ((selectionHandles.size() == 0) && (detailLevel != 0)) {
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
        return getDrawing().findFigure(viewToDrawing(p), getScaleFactor());
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.figure.Figure> findFigures(java.awt.Rectangle r) {
        return getDrawing().findFigures(viewToDrawing(r));
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.figure.Figure> findFiguresWithin(java.awt.Rectangle r) {
        return getDrawing().findFiguresWithin(viewToDrawing(r));
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

    protected void invalidateDimension() {
        cachedPreferredSize = null;
        cachedDrawingArea = null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.Constrainer getConstrainer() {
        return isConstrainerVisible() ? visibleConstrainer : invisibleConstrainer;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredSize() {
        if (cachedPreferredSize == null) {
            java.awt.geom.Rectangle2D.Double r = getDrawingArea();
            java.lang.Double cw = (getDrawing() == null) ? null : getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH);
            java.lang.Double ch = (getDrawing() == null) ? null : getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT);
            java.awt.Insets insets = getInsets();
            if ((cw == null) || (ch == null)) {
                cachedPreferredSize = new java.awt.Dimension((((int) (java.lang.Math.ceil((java.lang.Math.max(0, r.x) + r.width) * scaleFactor))) + insets.left) + insets.right, (((int) (java.lang.Math.ceil((java.lang.Math.max(0, r.y) + r.height) * scaleFactor))) + insets.top) + insets.bottom);
            } else {
                cachedPreferredSize = new java.awt.Dimension((((int) (java.lang.Math.ceil(((-java.lang.Math.min(0, r.x)) + java.lang.Math.max((java.lang.Math.max(0, r.x) + r.width) + java.lang.Math.min(0, r.x), cw)) * scaleFactor))) + insets.left) + insets.right, (((int) (java.lang.Math.ceil(((-java.lang.Math.min(0, r.y)) + java.lang.Math.max((java.lang.Math.max(0, r.y) + r.height) + java.lang.Math.min(0, r.y), ch)) * scaleFactor))) + insets.top) + insets.bottom);
            }
        }
        return ((java.awt.Dimension) (cachedPreferredSize.clone()));
    }

    protected java.awt.geom.Rectangle2D.Double getDrawingArea() {
        if (cachedDrawingArea == null) {
            if (drawing != null) {
                cachedDrawingArea = drawing.getDrawingArea(getScaleFactor());
            } else {
                cachedDrawingArea = new java.awt.geom.Rectangle2D.Double();
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    /**
     * Side effect: Changes view Translation.
     */
    @java.lang.Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        validateViewTranslation();
    }

    /**
     * Updates the view translation taking into account the current dimension of the view JComponent,
     * the size of the drawing, and the scale factor.
     */
    private void validateViewTranslation() {
        if (getDrawing() == null) {
            translation.x = translation.y = 0;
            return;
        }
        java.awt.Point oldTranslation = ((java.awt.Point) (translation.clone()));
        int width = getWidth();
        int height = getHeight();
        java.awt.Insets insets = getInsets();
        java.awt.geom.Rectangle2D.Double da = getDrawingArea();
        java.awt.Rectangle r = new java.awt.Rectangle(((int) (da.x * scaleFactor)), ((int) (da.y * scaleFactor)), ((int) (da.width * scaleFactor)), ((int) (da.height * scaleFactor)));
        java.lang.Double cwd = getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH);
        java.lang.Double chd = getDrawing().attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT);
        if ((cwd == null) || (chd == null)) {
            // The canvas size is not explicitly specified.
            // Place the canvas at the top left
            translation.x = insets.top;
            translation.y = insets.left;
        } else {
            // The canvas size is explicitly specified.
            int cw;
            int ch;
            cw = ((int) (cwd * scaleFactor));
            ch = ((int) (chd * scaleFactor));
            // Place the canvas at the center
            if (cw < width) {
                translation.x = insets.left + ((((width - insets.left) - insets.right) - cw) / (-2));
            }
            if (ch < height) {
                translation.y = insets.top + ((((height - insets.top) - insets.bottom) - ch) / (-2));
            }
        }
        if (((r.y + r.height) - translation.y) > (height - insets.bottom)) {
            // We cut off the lower part of the drawing -> shift the canvas up
            translation.y = (r.y + r.height) - (height - insets.bottom);
        }
        if ((java.lang.Math.min(0, r.y) - translation.y) < insets.top) {
            // We cut off the upper part of the drawing -> shift the canvas down
            translation.y = java.lang.Math.min(0, r.y) - insets.top;
        }
        if (((r.x + r.width) - translation.x) > (width - insets.right)) {
            // We cut off the right part of the drawing -> shift the canvas left
            translation.x = (r.x + r.width) - (width - insets.right);
        }
        if ((java.lang.Math.min(0, r.x) - translation.x) < insets.left) {
            // We cut off the left part of the drawing -> shift the canvas right
            translation.x = java.lang.Math.min(0, r.x) - insets.left;
        }
        if (!oldTranslation.equals(translation)) {
            bufferedArea.translate(oldTranslation.x - translation.x, oldTranslation.y - translation.y);
            fireViewTransformChanged();
        }
    }

    /**
     * Converts drawing coordinates to view coordinates.
     */
    @java.lang.Override
    public java.awt.Point drawingToView(java.awt.geom.Point2D.Double p) {
        return new java.awt.Point(((int) (p.x * scaleFactor)) - translation.x, ((int) (p.y * scaleFactor)) - translation.y);
    }

    @java.lang.Override
    public java.awt.Rectangle drawingToView(java.awt.geom.Rectangle2D.Double r) {
        return new java.awt.Rectangle(((int) (r.x * scaleFactor)) - translation.x, ((int) (r.y * scaleFactor)) - translation.y, ((int) (r.width * scaleFactor)), ((int) (r.height * scaleFactor)));
    }

    /**
     * Converts view coordinates to drawing coordinates.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double viewToDrawing(java.awt.Point p) {
        return new java.awt.geom.Point2D.Double((p.x + translation.x) / scaleFactor, (p.y + translation.y) / scaleFactor);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double viewToDrawing(java.awt.Rectangle r) {
        return new java.awt.geom.Rectangle2D.Double((r.x + translation.x) / scaleFactor, (r.y + translation.y) / scaleFactor, r.width / scaleFactor, r.height / scaleFactor);
    }

    @java.lang.Override
    public javax.swing.JComponent getComponent() {
        return this;
    }

    @java.lang.Override
    public double getScaleFactor() {
        return scaleFactor;
    }

    @java.lang.Override
    public void setScaleFactor(double newValue) {
        double oldValue = scaleFactor;
        scaleFactor = newValue;
        validateViewTranslation();
        dirtyArea.setBounds(bufferedArea);
        invalidateHandles();
        revalidate();
        repaint();
        firePropertyChange("scaleFactor", oldValue, newValue);
    }

    protected void fireViewTransformChanged() {
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
    public java.awt.geom.AffineTransform getDrawingToViewTransform() {
        java.awt.geom.AffineTransform t = new java.awt.geom.AffineTransform();
        t.translate(-translation.x, -translation.y);
        t.scale(scaleFactor, scaleFactor);
        return t;
    }

    @java.lang.Override
    public void delete() {
        final java.util.List<org.jhotdraw.draw.figure.Figure> deletedFigures = drawing.sort(getSelectedFigures());
        // Abort, if not all of the selected figures may be removed from the
        // drawing
        for (org.jhotdraw.draw.figure.Figure f : deletedFigures) {
            if (!f.isRemovable()) {
                getToolkit().beep();
                return;
            }
        }
        // Get z-indices of deleted figures
        final int[] deletedFigureIndices = new int[deletedFigures.size()];
        for (int i = 0; i < deletedFigureIndices.length; i++) {
            deletedFigureIndices[i] = drawing.indexOf(deletedFigures.get(i));
        }
        clearSelection();
        getDrawing().removeAll(deletedFigures);
        getDrawing().fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
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
                org.jhotdraw.draw.Drawing d = getDrawing();
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
        java.util.Collection<org.jhotdraw.draw.figure.Figure> sorted = getDrawing().sort(getSelectedFigures());
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
        getDrawing().fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("edit.duplicate.text");
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                getDrawing().removeAll(duplicates);
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                getDrawing().addAll(duplicates);
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
     * Sets whether the drawing is double buffered.
     *
     * <p>
     *
     * <p>The default value is true.
     *
     * <p>
     *
     * <p>This is a bound property.
     *
     * <p>
     *
     * <p>If the drawing view is used for editing, you should leave this to true. If the drawing view
     * is used for viewing only, you should set this to false.
     */
    public void setDrawingDoubleBuffered(boolean newValue) {
        boolean oldValue = isDrawingDoubleBuffered;
        isDrawingDoubleBuffered = newValue;
        if ((!isDrawingDoubleBuffered) && (drawingBufferV != null)) {
            drawingBufferV.flush();
            drawingBufferV = null;
        }
        if ((!isDrawingDoubleBuffered) && (drawingBufferNV != null)) {
            drawingBufferNV.flush();
            drawingBufferNV = null;
        }
        firePropertyChange(org.jhotdraw.draw.DefaultDrawingView.DRAWING_DOUBLE_BUFFERED_PROPERTY, oldValue, newValue);
    }

    /**
     * Returns true, if the the drawing is double buffered.
     */
    public boolean isDrawingDoubleBuffered() {
        return isDrawingDoubleBuffered;
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
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

    @java.lang.Override
    public org.jhotdraw.draw.handle.Handle getActiveHandle() {
        return activeHandle;
    }
}