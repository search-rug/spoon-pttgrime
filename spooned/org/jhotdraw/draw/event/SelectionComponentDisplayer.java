/**
 *
 * @(#)SelectionComponentDisplayer.java <p>Copyright (c) 2006-2008 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.event;
import java.lang.ref.WeakReference;
/**
 * Calls setVisible(true/false) on components, which show attributes of the drawing editor and of
 * its views based on the current selection.
 *
 * <p>Holds a {@code WeakReference} on the component. Automatically disposes itself if the component
 * no longer exists.
 */
public class SelectionComponentDisplayer implements java.beans.PropertyChangeListener , org.jhotdraw.draw.event.FigureSelectionListener {
    protected org.jhotdraw.draw.DrawingView view;

    protected org.jhotdraw.draw.DrawingEditor editor;

    protected java.lang.ref.WeakReference<javax.swing.JComponent> weakRef;

    protected int minSelectionCount = 1;

    protected boolean isVisibleIfCreationTool = true;

    public SelectionComponentDisplayer(org.jhotdraw.draw.DrawingEditor editor, javax.swing.JComponent component) {
        this.editor = editor;
        this.weakRef = new java.lang.ref.WeakReference<>(component);
        if (editor.getActiveView() != null) {
            view = editor.getActiveView();
            view.addPropertyChangeListener(this);
            view.addFigureSelectionListener(this);
        }
        editor.addPropertyChangeListener(this);
        updateVisibility();
    }

    @java.lang.Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        java.lang.String name = evt.getPropertyName();
        if (((name == null) && (org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY))) {
            if (view != null) {
                view.removePropertyChangeListener(this);
                view.removeFigureSelectionListener(this);
            }
            view = ((org.jhotdraw.draw.DrawingView) (evt.getNewValue()));
            if (view != null) {
                view.addPropertyChangeListener(this);
                view.addFigureSelectionListener(this);
            }
            updateVisibility();
        } else if (((name == null) && (org.jhotdraw.draw.DrawingEditor.TOOL_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingEditor.TOOL_PROPERTY))) {
            updateVisibility();
        }
    }

    @java.lang.Override
    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        updateVisibility();
    }

    public void updateVisibility() {
        boolean newValue = ((editor != null) && (editor.getActiveView() != null)) && (((isVisibleIfCreationTool && (editor.getTool() != null)) && (!(editor.getTool() instanceof org.jhotdraw.draw.tool.SelectionTool))) || (editor.getActiveView().getSelectionCount() >= minSelectionCount));
        javax.swing.JComponent component = weakRef.get();
        if (component == null) {
            dispose();
            return;
        }
        if (newValue != component.isVisible()) {
            component.setVisible(newValue);
            // The following is needed to trick BoxLayout
            if (newValue) {
                component.setPreferredSize(null);
            } else {
                component.setPreferredSize(new java.awt.Dimension(0, 0));
            }
            component.revalidate();
        }
    }

    protected javax.swing.JComponent getComponent() {
        return weakRef.get();
    }

    public void dispose() {
        if (editor != null) {
            editor.removePropertyChangeListener(this);
            editor = null;
        }
        if (view != null) {
            view.removePropertyChangeListener(this);
            view.removeFigureSelectionListener(this);
            view = null;
        }
    }

    public void setMinSelectionCount(int newValue) {
        minSelectionCount = newValue;
        updateVisibility();
    }

    public void setVisibleIfCreationTool(boolean newValue) {
        isVisibleIfCreationTool = newValue;
    }
}