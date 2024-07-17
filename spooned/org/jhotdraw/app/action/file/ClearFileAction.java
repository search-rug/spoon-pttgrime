/* @(#)ClearFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Clears (empties) the contents of the active view.
 *
 * <p>This action is called when the user selects the Clear item in the File menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>This action is designed for applications which do not automatically create a new view for each
 * opened file. This action goes together with {@link NewWindowAction}, {@link LoadFileAction},
 * {@link LoadDirectoryAction} and {@link CloseFileAction}. This action should not be used together
 * with {@code NewFileAction}.
 */
public class ClearFileAction extends org.jhotdraw.app.action.AbstractSaveUnsavedChangesAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.clear";

    public ClearFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, "file.clear");
    }

    @java.lang.Override
    public void doIt(final org.jhotdraw.api.app.View view) {
        view.setEnabled(false);
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                view.clear();
                return null;
            }

            @java.lang.Override
            protected void done() {
                view.setEnabled(true);
            }
        }.execute();
    }
}