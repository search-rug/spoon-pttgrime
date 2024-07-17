/* @(#)UndoAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
/**
 * Undoes the last user action.
 *
 * <p>This action requires that the View returns a project specific undo action when invoking
 * getActionMap("redo") on a View.
 *
 * <p>This action is called when the user selects the Undo item in the Edit menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 */
public class UndoAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.undo";

    private org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");

    private java.beans.PropertyChangeListener redoActionPropertyListener = new java.beans.PropertyChangeListener() {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if (((name == null) && (javax.swing.AbstractAction.NAME == null)) || ((name != null) && name.equals(javax.swing.AbstractAction.NAME))) {
                putValue(javax.swing.AbstractAction.NAME, evt.getNewValue());
            } else if ("enabled".equals(name)) {
                updateEnabledState();
            }
        }
    };

    public UndoAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        labels.configureAction(this, org.jhotdraw.action.edit.UndoAction.ID);
    }

    protected void updateEnabledState() {
        boolean isEnabled = false;
        javax.swing.Action realAction = getRealUndoAction();
        if ((realAction != null) && (realAction != this)) {
            isEnabled = realAction.isEnabled();
        }
        setEnabled(isEnabled);
    }

    @java.lang.Override
    protected void updateView(org.jhotdraw.api.app.View oldValue, org.jhotdraw.api.app.View newValue) {
        super.updateView(oldValue, newValue);
        if (((newValue != null) && (newValue.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID) != null)) && (newValue.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID) != this)) {
            putValue(javax.swing.AbstractAction.NAME, newValue.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID).getValue(javax.swing.AbstractAction.NAME));
            updateEnabledState();
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void installViewListeners(org.jhotdraw.api.app.View p) {
        super.installViewListeners(p);
        javax.swing.Action undoActionInView = p.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID);
        if ((undoActionInView != null) && (undoActionInView != this)) {
            undoActionInView.addPropertyChangeListener(redoActionPropertyListener);
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void uninstallViewListeners(org.jhotdraw.api.app.View p) {
        super.uninstallViewListeners(p);
        javax.swing.Action undoActionInView = p.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID);
        if ((undoActionInView != null) && (undoActionInView != this)) {
            undoActionInView.removePropertyChangeListener(redoActionPropertyListener);
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        javax.swing.Action realUndoAction = getRealUndoAction();
        if ((realUndoAction != null) && (realUndoAction != this)) {
            realUndoAction.actionPerformed(e);
        }
    }

    private javax.swing.Action getRealUndoAction() {
        return getActiveView() == null ? null : getActiveView().getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID);
    }
}