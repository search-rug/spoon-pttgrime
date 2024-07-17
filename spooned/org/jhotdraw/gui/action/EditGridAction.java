/* @(#)EditGridAction.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.action;
/**
 * EditGridAction.
 *
 * <p>XXX - We shouldn't have a dependency to the application framework from within the drawing
 * framework.
 */
public class EditGridAction extends org.jhotdraw.draw.action.AbstractDrawingViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.editGrid";

    private javax.swing.JDialog dialog;

    private org.jhotdraw.gui.action.EditGridPanel settingsPanel;

    private java.beans.PropertyChangeListener propertyChangeHandler;

    private org.jhotdraw.api.app.Application app;

    public EditGridAction(org.jhotdraw.api.app.Application app, org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        this.app = app;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.gui.action.EditGridAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        getDialog().setVisible(true);
    }

    @java.lang.Override
    protected void updateViewState() {
        if ((getView() != null) && (settingsPanel != null)) {
            settingsPanel.setConstrainer(((org.jhotdraw.draw.constrainer.GridConstrainer) (getView().getVisibleConstrainer())));
        }
    }

    protected org.jhotdraw.api.app.Application getApplication() {
        return app;
    }

    protected javax.swing.JDialog getDialog() {
        if (dialog == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            dialog = new javax.swing.JDialog();
            dialog.setTitle(labels.getString("editGrid"));
            dialog.setResizable(false);
            settingsPanel = new org.jhotdraw.gui.action.EditGridPanel();
            dialog.add(settingsPanel);
            dialog.pack();
            java.util.prefs.Preferences prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
            org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "editGrid", dialog);
            getApplication().addWindow(dialog, null);
        }
        settingsPanel.setConstrainer(((org.jhotdraw.draw.constrainer.GridConstrainer) (getView().getVisibleConstrainer())));
        return dialog;
    }
}