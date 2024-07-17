/* @(#)RedoAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
/**
 * Redoes the last user action on the active view.
 *
 * <p>This action requires that the View returns a project specific redo action when invoking
 * getActionMap("redo") on a View.
 *
 * <p>This action is called when the user selects the Redo item in the Edit menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 */
public class RedoAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.redo";

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

    public RedoAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        labels.configureAction(this, org.jhotdraw.action.edit.RedoAction.ID);
    }

    protected void updateEnabledState() {
        boolean isEnabled = false;
        javax.swing.Action realRedoAction = getRealRedoAction();
        if ((realRedoAction != null) && (realRedoAction != this)) {
            isEnabled = realRedoAction.isEnabled();
        }
        setEnabled(isEnabled);
    }

    @java.lang.Override
    protected void updateView(org.jhotdraw.api.app.View oldValue, org.jhotdraw.api.app.View newValue) {
        super.updateView(oldValue, newValue);
        if (((newValue != null) && (newValue.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID) != null)) && (newValue.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID) != this)) {
            putValue(javax.swing.AbstractAction.NAME, newValue.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID).getValue(javax.swing.AbstractAction.NAME));
            updateEnabledState();
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void installViewListeners(org.jhotdraw.api.app.View p) {
        super.installViewListeners(p);
        javax.swing.Action redoActionInView = p.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID);
        if ((redoActionInView != null) && (redoActionInView != this)) {
            redoActionInView.addPropertyChangeListener(redoActionPropertyListener);
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void uninstallViewListeners(org.jhotdraw.api.app.View p) {
        super.uninstallViewListeners(p);
        javax.swing.Action redoActionInView = p.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID);
        if ((redoActionInView != null) && (redoActionInView != this)) {
            redoActionInView.removePropertyChangeListener(redoActionPropertyListener);
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        javax.swing.Action realAction = getRealRedoAction();
        if ((realAction != null) && (realAction != this)) {
            realAction.actionPerformed(e);
        }
    }

    private javax.swing.Action getRealRedoAction() {
        return getActiveView() == null ? null : getActiveView().getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID);
    }
}