/* @(#)OpenApplicationFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * Opens a file for which an open-request was sent to the application.
 *
 * <p>The file name is passed in the action command of the action event.
 *
 * <p>This action is called when the user drops a file on the dock icon of {@code DefaultOSXApplication} or onto the desktop area of {@code DefaultMDIApplication}.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}. <hr> <b>Features</b>
 *
 * <p>
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code OpenApplicationFileAction} prevents opening an URI which is
 * opened in another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 *
 * <p>
 *
 * <p><em>Open last URI on launch</em><br>
 * {@code OpenApplicationFileAction} supplies data for this feature by calling {@link Application#addRecentURI} when it successfully loaded a file. See {@link org.jhotdraw.app} for a
 * description of the feature.
 */
public class OpenApplicationFileAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.openFile";

    private javax.swing.JFileChooser fileChooser;

    private int entries;

    public OpenApplicationFileAction(org.jhotdraw.api.app.Application app) {
        super(app);
        putValue(javax.swing.Action.NAME, "OSX Open File");
    }

    /**
     * Opens a new view.
     *
     * <p>The file name is passed in the action command of the action event.
     *
     * <p>
     */
    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.Application app = getApplication();
        final java.lang.String filename = evt.getActionCommand();
        if (app.isEnabled()) {
            java.net.URI uri = new java.io.File(filename).toURI();
            // Prevent same URI from being opened more than once
            if (!app.getModel().isAllowMultipleViewsPerURI()) {
                for (org.jhotdraw.api.app.View v : app.getViews()) {
                    if ((v.getURI() != null) && v.getURI().equals(uri)) {
                        v.getComponent().requestFocus();
                        return;
                    }
                }
            }
            app.setEnabled(false);
            // Search for an empty view
            org.jhotdraw.api.app.View emptyView = app.getActiveView();
            if (((emptyView == null) || (emptyView.getURI() != null)) || emptyView.hasUnsavedChanges()) {
                emptyView = null;
            }
            final org.jhotdraw.api.app.View p;
            if (emptyView == null) {
                p = app.createView();
                app.add(p);
                app.show(p);
            } else {
                p = emptyView;
            }
            openView(p, uri);
        }
    }

    protected void openView(final org.jhotdraw.api.app.View view, final java.net.URI uri) {
        final org.jhotdraw.api.app.Application app = getApplication();
        app.setEnabled(true);
        // If there is another view with the same URI we set the multiple open
        // id of our view to max(multiple open id) + 1.
        int multipleOpenId = 1;
        for (org.jhotdraw.api.app.View aView : app.views()) {
            if (((aView != view) && (aView.getURI() != null)) && aView.getURI().equals(uri)) {
                multipleOpenId = java.lang.Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
            }
        }
        view.setMultipleOpenId(multipleOpenId);
        view.setEnabled(false);
        // Open the file
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                boolean exists = true;
                try {
                    java.io.File f = new java.io.File(uri);
                    exists = f.exists();
                } catch (java.lang.IllegalArgumentException e) {
                    // The URI does not denote a file, thus we can not check whether the file exists.
                }
                if (exists) {
                    view.read(uri, null);
                } else {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    throw new java.io.IOException(labels.getFormatted("file.open.fileDoesNotExist.message", org.jhotdraw.net.URIUtil.getName(uri)));
                }
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    view.setURI(uri);
                    app.addRecentURI(uri);
                    java.awt.Frame w = ((java.awt.Frame) (javax.swing.SwingUtilities.getWindowAncestor(view.getComponent())));
                    if (w != null) {
                        w.setExtendedState(w.getExtendedState() & (~java.awt.Frame.ICONIFIED));
                        w.toFront();
                    }
                    view.setEnabled(true);
                    view.getComponent().requestFocus();
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.app.OpenApplicationFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    java.lang.String message = (ex.getMessage() != null) ? ex.getMessage() : ex.toString();
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.open.couldntOpen.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + (message == null ? "" : message), javax.swing.JOptionPane.ERROR_MESSAGE, new org.jhotdraw.gui.event.SheetListener() {
                        @java.lang.Override
                        public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                            view.setEnabled(true);
                        }
                    });
                }
            }
        }.execute();
    }
}