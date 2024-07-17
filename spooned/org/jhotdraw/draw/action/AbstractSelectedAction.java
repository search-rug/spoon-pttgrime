/* @(#)AbstractSelectedAction.java

Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
import org.jhotdraw.beans.WeakPropertyChangeListener;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
/**
 * This abstract class can be extended to implement an {@code Action} that acts on behalf of the
 * selected figures of a {@link org.jhotdraw.draw.DrawingView}.
 *
 * <p>By default the enabled state of this action reflects the enabled state of the active {@code DrawingView}. If no drawing view is active, this action is disabled. When many actions listen to
 * the enabled state of the active drawing views this can considerably slow down the editor. If
 * updating the enabled state is not necessary, you can prevent the action from doing so using
 * {@link #setUpdateEnabledState}.
 *
 * <p>{@code AbstractDrawingEditorAction} listens using a {@link WeakPropertyChangeListener} on the
 * {@code DrawingEditor} and thus may become garbage collected if it is not referenced by any other
 * object.
 */
public abstract class AbstractSelectedAction extends javax.swing.AbstractAction implements org.jhotdraw.api.app.Disposable {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingEditor editor;

    private transient org.jhotdraw.draw.DrawingView activeView;

    private class EventHandler implements java.beans.PropertyChangeListener , org.jhotdraw.draw.event.FigureSelectionListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (((evt.getPropertyName() == null) && (org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY))) {
                if (activeView != null) {
                    activeView.removeFigureSelectionListener(this);
                    activeView.removePropertyChangeListener(this);
                }
                if (evt.getNewValue() != null) {
                    activeView = ((org.jhotdraw.draw.DrawingView) (evt.getNewValue()));
                    activeView.addFigureSelectionListener(this);
                    activeView.addPropertyChangeListener(this);
                }
                updateEnabledState();
            } else if ("enabled".equals(evt.getPropertyName())) {
                updateEnabledState();
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((AbstractSelectedAction.this + " ") + this.getClass()) + "@") + hashCode();
        }

        @java.lang.Override
        public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
            updateEnabledState();
        }
    }

    private org.jhotdraw.draw.action.AbstractSelectedAction.EventHandler eventHandler = new org.jhotdraw.draw.action.AbstractSelectedAction.EventHandler();

    /**
     * Creates an action which acts on the selected figures on the current view of the specified
     * editor.
     */
    public AbstractSelectedAction(org.jhotdraw.draw.DrawingEditor editor) {
        setEditor(editor);
        // updateEnabledState();
    }

    /**
     * Updates the enabled state of this action to reflect the enabled state of the active {@code DrawingView}. If no drawing view is active, this action is disabled.
     */
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(getView().isEnabled() && (getView().getSelectionCount() > 0));
        } else {
            setEnabled(false);
        }
    }

    @java.lang.Override
    public void dispose() {
        setEditor(null);
    }

    public void setEditor(org.jhotdraw.draw.DrawingEditor editor) {
        if (eventHandler != null) {
            unregisterEventHandler();
        }
        this.editor = editor;
        if ((editor != null) && (eventHandler != null)) {
            registerEventHandler();
            updateEnabledState();
        }
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    protected org.jhotdraw.draw.DrawingView getView() {
        return editor == null ? null : editor.getActiveView();
    }

    protected org.jhotdraw.draw.Drawing getDrawing() {
        return getView() == null ? null : getView().getDrawing();
    }

    protected void fireUndoableEditHappened(javax.swing.undo.UndoableEdit edit) {
        getDrawing().fireUndoableEditHappened(edit);
    }

    /**
     * By default, the enabled state of this action is updated to reflect the enabled state of the
     * active {@code DrawingView}. Since this is not always necessary, and since many listening
     * actions may considerably slow down the drawing editor, you can switch this behavior off here.
     *
     * @param newValue
     * 		Specify false to prevent automatic updating of the enabled state.
     */
    public void setUpdateEnabledState(boolean newValue) {
        // Note: eventHandler != null yields true, if we are currently updating
        // the enabled state.
        if ((eventHandler != null) != newValue) {
            if (newValue) {
                eventHandler = new org.jhotdraw.draw.action.AbstractSelectedAction.EventHandler();
                registerEventHandler();
            } else {
                unregisterEventHandler();
                eventHandler = null;
            }
        }
        if (newValue) {
            updateEnabledState();
        }
    }

    /**
     * Returns true, if this action automatically updates its enabled state to reflect the enabled
     * state of the active {@code DrawingView}.
     */
    public boolean isUpdatEnabledState() {
        return eventHandler != null;
    }

    /**
     * Unregisters the event handler from the drawing editor and the active drawing view.
     */
    private void unregisterEventHandler() {
        if (editor != null) {
            editor.removePropertyChangeListener(eventHandler);
        }
        if (activeView != null) {
            activeView.removeFigureSelectionListener(eventHandler);
            activeView.removePropertyChangeListener(eventHandler);
            activeView = null;
        }
    }

    /**
     * Registers the event handler from the drawing editor and the active drawing view.
     */
    private void registerEventHandler() {
        if (editor != null) {
            editor.addPropertyChangeListener(new org.jhotdraw.beans.WeakPropertyChangeListener(eventHandler));
            if (activeView != null) {
                activeView.removeFigureSelectionListener(eventHandler);
                activeView.removePropertyChangeListener(eventHandler);
            }
            activeView = editor.getActiveView();
            if (activeView != null) {
                activeView.addFigureSelectionListener(eventHandler);
                activeView.addPropertyChangeListener(eventHandler);
            }
        }
    }
}