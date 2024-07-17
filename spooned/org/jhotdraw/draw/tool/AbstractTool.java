/* @(#)AbstractTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * This abstract class can be extended to implement a {@link Tool}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Proxy</em><br>
 * To remove the need for null-handling, {@code AbstractTool} makes use of a proxy for {@code DrawingEditor}. Subject: {@link DrawingEditor}; Proxy: {@link DrawingEditorProxy}; Client: {@link AbstractTool}. <hr>
 */
public abstract class AbstractTool extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.draw.tool.Tool {
    private static final long serialVersionUID = 1L;

    /**
     * This is set to true, if this is the active tool of the editor.
     */
    private boolean isActive;

    /**
     * This is set to true, while the tool is doing some work. This prevents the currentView from
     * being changed when a mouseEnter event is received.
     */
    protected boolean isWorking;

    protected org.jhotdraw.draw.DrawingEditor editor;

    protected java.awt.Point anchor = new java.awt.Point();

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    private org.jhotdraw.draw.DrawingEditorProxy editorProxy;

    /* private PropertyChangeListener editorHandler;
    private PropertyChangeListener viewHandler;
     */
    /**
     * The input map of the tool.
     */
    private javax.swing.InputMap inputMap;

    /**
     * The action map of the tool.
     */
    private javax.swing.ActionMap actionMap;

    public AbstractTool() {
        editorProxy = new org.jhotdraw.draw.DrawingEditorProxy();
        setInputMap(createInputMap());
        setActionMap(createActionMap());
    }

    public void addUndoableEditListener(javax.swing.event.UndoableEditListener l) {
        listenerList.add(javax.swing.event.UndoableEditListener.class, l);
    }

    public void removeUndoableEditListener(javax.swing.event.UndoableEditListener l) {
        listenerList.remove(javax.swing.event.UndoableEditListener.class, l);
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        this.editor = editor;
        editorProxy.setTarget(editor);
        isActive = true;
        // Repaint all handles
        for (org.jhotdraw.draw.DrawingView v : editor.getDrawingViews()) {
            v.repaintHandles();
        }
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        this.editor = editor;
        editorProxy.setTarget(null);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    protected org.jhotdraw.draw.DrawingView getView() {
        return editor.getActiveView();
    }

    protected org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    protected org.jhotdraw.draw.Drawing getDrawing() {
        return getView().getDrawing();
    }

    protected java.awt.geom.Point2D.Double viewToDrawing(java.awt.Point p) {
        return constrainPoint(getView().viewToDrawing(p));
    }

    protected java.awt.geom.Point2D.Double constrainPoint(java.awt.Point p, org.jhotdraw.draw.figure.Figure... figure) {
        return constrainPoint(getView().viewToDrawing(p), figure);
    }

    protected java.awt.geom.Point2D.Double constrainPoint(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure... figure) {
        if (getView() == null) {
            return p;
        }
        return getView().getConstrainer() == null ? p : getView().getConstrainer().constrainPoint(p, figure);
    }

    /**
     * Sets the InputMap for the Tool.
     *
     * @see #keyPressed
     * @see #setActionMap
     */
    public void setInputMap(javax.swing.InputMap newValue) {
        inputMap = newValue;
    }

    /**
     * Gets the input map of the Tool
     */
    public javax.swing.InputMap getInputMap() {
        return inputMap;
    }

    /**
     * Sets the ActionMap for the Tool.
     *
     * @see #keyPressed
     */
    public void setActionMap(javax.swing.ActionMap newValue) {
        actionMap = newValue;
    }

    /**
     * Gets the action map of the Tool
     */
    public javax.swing.ActionMap getActionMap() {
        return actionMap;
    }

    /**
     * Deletes the selection. Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    @java.lang.Override
    public void editDelete() {
        getView().getDrawing().removeAll(getView().getSelectedFigures());
    }

    /**
     * Cuts the selection into the clipboard. Depending on the tool, this could be selected figures,
     * selected points or selected text.
     */
    @java.lang.Override
    public void editCut() {
    }

    /**
     * Copies the selection into the clipboard. Depending on the tool, this could be selected figures,
     * selected points or selected text.
     */
    @java.lang.Override
    public void editCopy() {
    }

    /**
     * Duplicates the selection. Depending on the tool, this could be selected figures, selected
     * points or selected text.
     */
    @java.lang.Override
    public void editDuplicate() {
    }

    /**
     * Pastes the contents of the clipboard. Depending on the tool, this could be selected figures,
     * selected points or selected text.
     */
    @java.lang.Override
    public void editPaste() {
    }

    @java.lang.Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
        fireToolDone();
    }

    @java.lang.Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
    }

    /**
     * The Tool uses the InputMap to determine what to do, when a key is pressed. If the corresponding
     * value of the InputMap is a String, the ActionMap of the tool is used, to find the action to be
     * performed. If the corresponding value of the InputMap is a ActionListener, the actionPerformed
     * method of the ActionListener is performed.
     */
    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        if (!evt.isConsumed()) {
            if (evt.getSource() instanceof java.awt.Container) {
                editor.setActiveView(editor.findView(((java.awt.Container) (evt.getSource()))));
            }
            java.lang.Object obj = null;
            if (inputMap != null) {
                // Lookup the input map of the tool
                obj = inputMap.get(javax.swing.KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers(), false));
            }
            if (obj == null) {
                // Fall back to the input map of the drawing editor
                javax.swing.InputMap im = editor.getInputMap();
                if (im != null) {
                    obj = im.get(javax.swing.KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers(), false));
                }
            }
            java.awt.event.ActionListener al = null;
            if (obj instanceof java.awt.event.ActionListener) {
                al = ((java.awt.event.ActionListener) (obj));
            } else if (obj != null) {
                // Lookup the action map of the tool
                if (actionMap != null) {
                    al = actionMap.get(obj);
                }
                if (al == null) {
                    // Fall back to the action map of the drawing editor
                    al = editor.getActionMap().get(obj);
                }
            }
            if (al != null) {
                evt.consume();
                al.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "tool", evt.getWhen(), evt.getModifiers()));
                fireToolDone();
            }
        }
    }

    /**
     * Override this method to create a tool-specific input map, which overrides the input map of the
     * drawing edtior.
     *
     * <p>The implementation of this class returns null.
     */
    protected javax.swing.InputMap createInputMap() {
        return null;
    }

    /**
     * Override this method to create a tool-specific action map, which overrides the action map of
     * the drawing edtior.
     *
     * <p>The implementation of this class returns null.
     */
    protected javax.swing.ActionMap createActionMap() {
        return null;
    }

    @java.lang.Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
    }

    @java.lang.Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        /* if (! isWorking) {
        editor.setActiveView(editor.findView((Container) evt.getSource()));
        }
         */
    }

    @java.lang.Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView view = editor.findView(((java.awt.Container) (evt.getSource())));
        view.requestFocus();
        anchor = new java.awt.Point(evt.getX(), evt.getY());
        isWorking = true;
        fireToolStarted(view);
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        isWorking = false;
    }

    @java.lang.Override
    public void addToolListener(org.jhotdraw.draw.event.ToolListener l) {
        listenerList.add(org.jhotdraw.draw.event.ToolListener.class, l);
    }

    @java.lang.Override
    public void removeToolListener(org.jhotdraw.draw.event.ToolListener l) {
        listenerList.remove(org.jhotdraw.draw.event.ToolListener.class, l);
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireToolStarted(org.jhotdraw.draw.DrawingView view) {
        org.jhotdraw.draw.event.ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.ToolEvent(this, view, new java.awt.Rectangle(0, 0, -1, -1));
                }
                ((org.jhotdraw.draw.event.ToolListener) (listeners[i + 1])).toolStarted(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireToolDone() {
        org.jhotdraw.draw.event.ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.ToolEvent(this, getView(), new java.awt.Rectangle(0, 0, -1, -1));
                }
                ((org.jhotdraw.draw.event.ToolListener) (listeners[i + 1])).toolDone(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(java.awt.geom.Rectangle2D.Double r) {
        java.awt.Point p1 = getView().drawingToView(new java.awt.geom.Point2D.Double(r.x, r.y));
        java.awt.Point p2 = getView().drawingToView(new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
        fireAreaInvalidated(new java.awt.Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(java.awt.Rectangle invalidatedArea) {
        org.jhotdraw.draw.event.ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.ToolEvent(this, getView(), invalidatedArea);
                }
                ((org.jhotdraw.draw.event.ToolListener) (listeners[i + 1])).areaInvalidated(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     *
     * <p>Note: This method only fires an event, if the invalidated area is outside of the canvas
     * bounds.
     */
    protected void maybeFireBoundsInvalidated(java.awt.Rectangle invalidatedArea) {
        org.jhotdraw.draw.Drawing d = getDrawing();
        java.awt.geom.Rectangle2D.Double canvasBounds = new java.awt.geom.Rectangle2D.Double(0, 0, 0, 0);
        if (d.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH) != null) {
            canvasBounds.width += d.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH);
        }
        if (d.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT) != null) {
            canvasBounds.height += d.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT);
        }
        if (!canvasBounds.contains(invalidatedArea)) {
            fireBoundsInvalidated(invalidatedArea);
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireBoundsInvalidated(java.awt.Rectangle invalidatedArea) {
        org.jhotdraw.draw.event.ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new org.jhotdraw.draw.event.ToolEvent(this, getView(), invalidatedArea);
                }
                ((org.jhotdraw.draw.event.ToolListener) (listeners[i + 1])).boundsInvalidated(event);
            }
        }
    }

    protected void fireFigureCreated(org.jhotdraw.draw.figure.Figure figure) {
        org.jhotdraw.draw.event.FigureCreatedEvent event = null;
        java.lang.Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.draw.event.ToolListener.class) {
                if (event == null) {
                    event = new org.jhotdraw.draw.event.FigureCreatedEvent(this, getView(), figure);
                }
                ((org.jhotdraw.draw.event.ToolListener) (listeners[i + 1])).figureCreated(event);
            }
        }
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
    }

    public void updateCursor(org.jhotdraw.draw.DrawingView view, java.awt.Point p) {
        if (view.isEnabled()) {
            org.jhotdraw.draw.handle.Handle handle = view.findHandle(p);
            if (handle != null) {
                view.setCursor(handle.getCursor());
            } else {
                java.awt.geom.Point2D.Double point = view.viewToDrawing(p);
                org.jhotdraw.draw.Drawing drawing = view.getDrawing();
                org.jhotdraw.draw.figure.Figure figure = drawing.findFigure(point, view.getScaleFactor());
                while ((figure != null) && (!figure.isSelectable())) {
                    figure = drawing.findFigureBehind(point, view.getScaleFactor(), figure);
                } 
                if (figure != null) {
                    view.setCursor(figure.getCursor(view.viewToDrawing(p), view.getScaleFactor()));
                } else {
                    view.setCursor(java.awt.Cursor.getDefaultCursor());
                }
            }
        } else {
            view.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        }
    }

    @java.lang.Override
    public java.lang.String getToolTipText(org.jhotdraw.draw.DrawingView view, java.awt.event.MouseEvent evt) {
        return null;
    }

    /**
     * Returns true, if this tool lets the user interact with handles.
     *
     * <p>Handles may draw differently, if interaction is not possible.
     *
     * @return True, if this tool supports interaction with the handles.
     */
    @java.lang.Override
    public boolean supportsHandleInteraction() {
        return false;
    }

    protected org.jhotdraw.draw.figure.Figure processCreatedFigureBeforeAddingToDocument(org.jhotdraw.draw.figure.Figure figure) {
        return figure;
    }
}