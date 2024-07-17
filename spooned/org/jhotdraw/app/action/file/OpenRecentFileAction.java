/* @(#)OpenRecentFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Loads the specified URI into an empty view. If no empty view is available, a new view is created.
 *
 * <p>This action is called when the user selects an item in the Recent Files submenu of the File
 * menu. The action and the menu item is automatically created by the application, when the {@code ApplicationModel} provides a {@code OpenFileAction}. <hr> <b>Features</b>
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code OpenRecentFileAction} prevents opening an URI which is
 * opened in another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 *
 * <p><em>Open last URI on launch</em><br>
 * {@code OpenRecentFileAction} supplies data for this feature by calling {@link Application#addRecentURI} when it successfully opened a file. See {@link org.jhotdraw.app} for a
 * description of the feature.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class OpenRecentFileAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.openRecent";

    private java.net.URI uri;

    public OpenRecentFileAction(org.jhotdraw.api.app.Application app, java.net.URI uri) {
        super(app);
        this.uri = uri;
        putValue(javax.swing.Action.NAME, org.jhotdraw.net.URIUtil.getName(uri));
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.Application app = getApplication();
        if (app.isEnabled()) {
            // Prevent same URI from being opened more than once
            if (!getApplication().getModel().isAllowMultipleViewsPerURI()) {
                for (org.jhotdraw.api.app.View vw : getApplication().getViews()) {
                    if ((vw.getURI() != null) && vw.getURI().equals(uri)) {
                        vw.getComponent().requestFocus();
                        return;
                    }
                }
            }
            app.setEnabled(false);
            // Search for an empty view
            org.jhotdraw.api.app.View emptyView = app.getActiveView();
            if (((emptyView == null) || (!emptyView.isEmpty())) || (!emptyView.isEnabled())) {
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
            openView(p);
        }
    }

    protected void openView(final org.jhotdraw.api.app.View view) {
        final org.jhotdraw.api.app.Application app = getApplication();
        app.setEnabled(true);
        // If there is another view with the same URI we set the multiple open
        // id of our view to max(multiple open id) + 1.
        int multipleOpenId = 1;
        for (org.jhotdraw.api.app.View aView : app.views()) {
            if ((aView != view) && aView.isEmpty()) {
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
                    java.awt.Frame w = ((java.awt.Frame) (javax.swing.SwingUtilities.getWindowAncestor(view.getComponent())));
                    if (w != null) {
                        w.setExtendedState(w.getExtendedState() & (~java.awt.Frame.ICONIFIED));
                        w.toFront();
                    }
                    app.addRecentURI(uri);
                    view.setEnabled(true);
                    view.getComponent().requestFocus();
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.OpenRecentFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    failed(ex);
                }
            }

            protected void failed(java.lang.Throwable value) {
                value.printStackTrace();
                java.lang.String message = (value.getMessage() != null) ? value.getMessage() : value.toString();
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.open.couldntOpen.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + (message == null ? "" : message), javax.swing.JOptionPane.ERROR_MESSAGE, new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                        view.setEnabled(true);
                    }
                });
            }
        }.execute();
    }
}