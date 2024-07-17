/* @(#)CloseFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Closes the active view after letting the user save unsaved changes. {@code DefaultSDIApplication}
 * automatically exits when the user closes the last view.
 *
 * <p>This action is called when the user selects the Close item in the File menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>You should include this action in applications which use at least one of the following
 * actions, so that the user can close views that he/she created: {@link NewFileAction}, {@link NewWindowAction}, {@link OpenFileAction}, {@link OpenDirectoryAction}.
 *
 * <p>
 */
public class CloseFileAction extends org.jhotdraw.app.action.AbstractSaveUnsavedChangesAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.close";

    public CloseFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.CloseFileAction.ID);
    }

    @java.lang.Override
    protected void doIt(org.jhotdraw.api.app.View view) {
        if ((view != null) && (view.getApplication() != null)) {
            org.jhotdraw.api.app.Application app = view.getApplication();
            app.dispose(view);
        }
    }
}