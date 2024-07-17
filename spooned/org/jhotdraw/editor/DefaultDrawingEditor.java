/* @(#)DefaultDrawingEditor.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.editor;
import org.jhotdraw.draw.DrawingEditor;
/**
 * A default implementation of {@link DrawingEditor}.
 *
 * <p>XXX - DefaultDrawingEditor should not publicly implement ToolListener.
 */
public class DefaultDrawingEditor extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.draw.DrawingEditor {
    private static final long serialVersionUID = 1L;

    private java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes = new java.util.HashMap<>();

    private java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> handleAttributes = new java.util.HashMap<>();

    private org.jhotdraw.draw.tool.Tool tool;

    private java.util.HashSet<org.jhotdraw.draw.DrawingView> views;

    private org.jhotdraw.draw.DrawingView activeView;

    private boolean isEnabled = true;

    private org.jhotdraw.editor.DefaultDrawingEditor.ToolHandler toolHandler;

    private class ToolHandler extends org.jhotdraw.draw.event.ToolAdapter {
        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.ToolEvent evt) {
            java.awt.Rectangle r = evt.getInvalidatedArea();
            evt.getView().getComponent().repaint(r.x, r.y, r.width, r.height);
        }

        @java.lang.Override
        public void toolStarted(org.jhotdraw.draw.event.ToolEvent evt) {
            setActiveView(evt.getView());
        }

        @java.lang.Override
        public void boundsInvalidated(org.jhotdraw.draw.event.ToolEvent evt) {
            org.jhotdraw.draw.Drawing d = evt.getView().getDrawing();
            for (org.jhotdraw.draw.DrawingView v : views) {
                if (v.getDrawing() == d) {
                    javax.swing.JComponent c = v.getComponent();
                    c.revalidate();
                }
            }
        }
    }

    /**
     * The input map of the drawing editor.
     */
    private javax.swing.InputMap inputMap;

    /**
     * The action map of the drawing editor.
     */
    private javax.swing.ActionMap actionMap;

    /**
     * The focus handler.
     */
    private java.awt.event.FocusListener focusHandler = new java.awt.event.FocusListener() {
        @java.lang.Override
        public void focusGained(java.awt.event.FocusEvent e) {
            setActiveView(findView(((java.awt.Container) (e.getSource()))));
        }

        @java.lang.Override
        public void focusLost(java.awt.event.FocusEvent e) {
            /* if (! e.isTemporary()) {
            setFocusedView(null);
            }
             */
        }
    };

    public DefaultDrawingEditor() {
        toolHandler = new org.jhotdraw.editor.DefaultDrawingEditor.ToolHandler();
        setDefaultAttribute(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.white);
        setDefaultAttribute(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, java.awt.Color.black);
        setDefaultAttribute(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR, java.awt.Color.black);
        views = new java.util.HashSet<>();
        inputMap = createInputMap();
        actionMap = createActionMap();
    }

    @java.lang.Override
    public void setTool(org.jhotdraw.draw.tool.Tool newValue) {
        org.jhotdraw.draw.tool.Tool oldValue = tool;
        if (newValue == tool) {
            return;
        }
        if (tool != null) {
            for (org.jhotdraw.draw.DrawingView v : views) {
                v.removeMouseListener(tool);
                v.removeMouseMotionListener(tool);
                v.removeKeyListener(tool);
                if (tool instanceof java.awt.event.MouseWheelListener) {
                    v.removeMouseWheelListener(((java.awt.event.MouseWheelListener) (tool)));
                }
            }
            tool.deactivate(this);
            tool.removeToolListener(toolHandler);
        }
        tool = newValue;
        if (tool != null) {
            tool.activate(this);
            for (org.jhotdraw.draw.DrawingView v : views) {
                v.addMouseListener(tool);
                v.addMouseMotionListener(tool);
                v.addKeyListener(tool);
                if (tool instanceof java.awt.event.MouseWheelListener) {
                    v.addMouseWheelListener(((java.awt.event.MouseWheelListener) (tool)));
                }
            }
            tool.addToolListener(toolHandler);
        }
        firePropertyChange(org.jhotdraw.draw.DrawingEditor.TOOL_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public void setActiveView(org.jhotdraw.draw.DrawingView newValue) {
        org.jhotdraw.draw.DrawingView oldValue = activeView;
        activeView = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool() {
        return tool;
    }

    @java.lang.Override
    public org.jhotdraw.draw.DrawingView getActiveView() {
        return activeView;
    }

    private void updateActiveView() {
        org.jhotdraw.draw.DrawingView aView = null;
        for (org.jhotdraw.draw.DrawingView v : views) {
            if (v.getComponent().isFocusOwner()) {
                setActiveView(v);
                return;
            }
            aView = v;
        }
        setActiveView(aView);
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void applyDefaultAttributesTo(org.jhotdraw.draw.figure.Figure f) {
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : defaultAttributes.entrySet()) {
            f.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
    }

    @java.lang.Override
    public <T> T getDefaultAttribute(org.jhotdraw.draw.AttributeKey<T> key) {
        if (defaultAttributes.containsKey(key)) {
            return key.get(defaultAttributes);
        } else {
            return key.getDefaultValue();
        }
    }

    @java.lang.Override
    public <T> void setDefaultAttribute(org.jhotdraw.draw.AttributeKey<T> key, T newValue) {
        java.lang.Object oldValue = defaultAttributes.put(key, newValue);
        firePropertyChange(org.jhotdraw.draw.DrawingEditor.DEFAULT_ATTRIBUTE_PROPERTY_PREFIX + key.getKey(), oldValue, newValue);
    }

    @java.lang.Override
    public void remove(org.jhotdraw.draw.DrawingView view) {
        view.getComponent().removeFocusListener(focusHandler);
        views.remove(view);
        if (tool != null) {
            view.removeMouseListener(tool);
            view.removeMouseMotionListener(tool);
            view.removeKeyListener(tool);
        }
        view.removeNotify(this);
        if (activeView == view) {
            view = (views.size() > 0) ? views.iterator().next() : null;
        }
        updateActiveView();
    }

    @java.lang.Override
    public void add(org.jhotdraw.draw.DrawingView view) {
        views.add(view);
        view.addNotify(this);
        view.getComponent().addFocusListener(focusHandler);
        if (tool != null) {
            view.addMouseListener(tool);
            view.addMouseMotionListener(tool);
            view.addKeyListener(tool);
        }
        updateActiveView();
    }

    @java.lang.Override
    public void setCursor(java.awt.Cursor c) {
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.DrawingView> getDrawingViews() {
        return java.util.Collections.unmodifiableCollection(views);
    }

    @java.lang.Override
    public org.jhotdraw.draw.DrawingView findView(java.awt.Container c) {
        for (org.jhotdraw.draw.DrawingView v : views) {
            if (v.getComponent() == c) {
                return v;
            }
        }
        return null;
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        if (newValue != isEnabled) {
            boolean oldValue = isEnabled;
            isEnabled = newValue;
            firePropertyChange("enabled", oldValue, newValue);
        }
    }

    @java.lang.Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @java.lang.Override
    public java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> getDefaultAttributes() {
        @java.lang.SuppressWarnings("cast")
        java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> m = ((java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>) (java.util.Collections.unmodifiableMap(defaultAttributes)));
        return m;
    }

    @java.lang.Override
    public <T> void setHandleAttribute(org.jhotdraw.draw.AttributeKey<T> key, T value) {
        handleAttributes.put(key, value);
    }

    @java.lang.Override
    public <T> T getHandleAttribute(org.jhotdraw.draw.AttributeKey<T> key) {
        if (handleAttributes.containsKey(key)) {
            return key.get(handleAttributes);
        } else {
            return key.getDefaultValue();
        }
    }

    @java.lang.Override
    public void setInputMap(javax.swing.InputMap newValue) {
        javax.swing.InputMap oldValue = inputMap;
        inputMap = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingEditor.INPUT_MAP_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public javax.swing.InputMap getInputMap() {
        return inputMap;
    }

    @java.lang.Override
    public void setActionMap(javax.swing.ActionMap newValue) {
        javax.swing.ActionMap oldValue = actionMap;
        actionMap = newValue;
        firePropertyChange(org.jhotdraw.draw.DrawingEditor.ACTION_MAP_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public javax.swing.ActionMap getActionMap() {
        return actionMap;
    }

    /**
     * Override this method to create a tool-specific input map, which overrides the input map of the
     * drawing editor.
     *
     * <p>The implementation of this class creates an input map for the following action ID's:
     *
     * <ul>
     *   <li>DeleteAction
     *   <li>SelectAllAction
     *   <li>IncreaseHandleDetailLevelAction
     *   <li>MoveConstrainedAction.West, .East, .North, .South
     *   <li>MoveAction.West, .East, .North, .South
     *   <li>CutAction
     *   <li>CopyAction
     *   <li>PasteAction
     * </ul>
     */
    protected javax.swing.InputMap createInputMap() {
        javax.swing.InputMap m = new javax.swing.InputMap();
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0), org.jhotdraw.action.edit.DeleteAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0), org.jhotdraw.action.edit.DeleteAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, 0), org.jhotdraw.action.edit.SelectAllAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.action.edit.SelectAllAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.META_DOWN_MASK), org.jhotdraw.action.edit.SelectAllAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0), org.jhotdraw.draw.action.IncreaseHandleDetailLevelAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0), org.jhotdraw.draw.action.MoveConstrainedAction.West.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0), org.jhotdraw.draw.action.MoveConstrainedAction.East.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0), org.jhotdraw.draw.action.MoveConstrainedAction.North.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0), org.jhotdraw.draw.action.MoveConstrainedAction.South.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.ALT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.West.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.ALT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.East.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.ALT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.North.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.ALT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.South.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.SHIFT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.West.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.SHIFT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.East.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.SHIFT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.North.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.SHIFT_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.South.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.West.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.East.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.North.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.draw.action.MoveAction.South.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.action.edit.CopyAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.META_DOWN_MASK), org.jhotdraw.action.edit.CopyAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.action.edit.PasteAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.META_DOWN_MASK), org.jhotdraw.action.edit.PasteAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK), org.jhotdraw.action.edit.CutAction.ID);
        m.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.META_DOWN_MASK), org.jhotdraw.action.edit.CutAction.ID);
        return m;
    }

    /**
     * Override this method to create a tool-specific action map, which overrides the action map of
     * the drawing editor.
     *
     * <p>The implementation of this class creates an action map which maps the following action ID's
     * to the classes which define them:
     *
     * <ul>
     *   <li>DeleteAction
     *   <li>SelectAllAction
     *   <li>IncreaseHandleDetailLevelAction
     *   <li>MoveConstrainedAction.West, .East, .North, .South
     *   <li>MoveAction.West, .East, .North, .South
     *   <li>CutAction
     *   <li>CopyAction
     *   <li>PasteAction
     * </ul>
     */
    protected javax.swing.ActionMap createActionMap() {
        javax.swing.ActionMap m = new javax.swing.ActionMap();
        m.put(org.jhotdraw.action.edit.DeleteAction.ID, new org.jhotdraw.action.edit.DeleteAction());
        m.put(org.jhotdraw.action.edit.SelectAllAction.ID, new org.jhotdraw.action.edit.SelectAllAction());
        m.put(org.jhotdraw.draw.action.IncreaseHandleDetailLevelAction.ID, new org.jhotdraw.draw.action.IncreaseHandleDetailLevelAction(this));
        m.put(org.jhotdraw.draw.action.MoveAction.East.ID, new org.jhotdraw.draw.action.MoveAction.East(this));
        m.put(org.jhotdraw.draw.action.MoveAction.West.ID, new org.jhotdraw.draw.action.MoveAction.West(this));
        m.put(org.jhotdraw.draw.action.MoveAction.North.ID, new org.jhotdraw.draw.action.MoveAction.North(this));
        m.put(org.jhotdraw.draw.action.MoveAction.South.ID, new org.jhotdraw.draw.action.MoveAction.South(this));
        m.put(org.jhotdraw.draw.action.MoveConstrainedAction.East.ID, new org.jhotdraw.draw.action.MoveConstrainedAction.East(this));
        m.put(org.jhotdraw.draw.action.MoveConstrainedAction.West.ID, new org.jhotdraw.draw.action.MoveConstrainedAction.West(this));
        m.put(org.jhotdraw.draw.action.MoveConstrainedAction.North.ID, new org.jhotdraw.draw.action.MoveConstrainedAction.North(this));
        m.put(org.jhotdraw.draw.action.MoveConstrainedAction.South.ID, new org.jhotdraw.draw.action.MoveConstrainedAction.South(this));
        m.put(org.jhotdraw.action.edit.CutAction.ID, new org.jhotdraw.action.edit.CutAction());
        m.put(org.jhotdraw.action.edit.CopyAction.ID, new org.jhotdraw.action.edit.CopyAction());
        m.put(org.jhotdraw.action.edit.PasteAction.ID, new org.jhotdraw.action.edit.PasteAction());
        return m;
    }
}