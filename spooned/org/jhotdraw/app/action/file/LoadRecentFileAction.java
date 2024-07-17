/* @(#)LoadRecentFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Lets the user save unsaved changes of the active view, and then loads the specified URI into the
 * active view.
 *
 * <p>If there is no active view, this action creates a new view and thus acts the same like {@link OpenRecentFileAction}.
 *
 * <p>This action is called when the user selects an item in the Recent Files submenu of the File
 * menu. The action and the menu item is automatically created by the application, when the {@code ApplicationModel} provides a {@code LoadFileAction}. <hr> <b>Features</b>
 *
 * <p><em>Open last URI on launch</em><br>
 * {@code LoadRecentFileAction} supplies data for this feature by calling {@link Application#addRecentURI} when it successfully loaded a file. See {@link org.jhotdraw.app} for a
 * description of the feature.
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code LoadRecentFileAction} prevents loading an URI which is
 * opened in another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class LoadRecentFileAction extends org.jhotdraw.app.action.AbstractSaveUnsavedChangesAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.loadRecent";

    private java.net.URI uri;

    public LoadRecentFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, java.net.URI uri) {
        super(app, view);
        this.uri = uri;
        setMayCreateView(true);
        putValue(javax.swing.Action.NAME, org.jhotdraw.net.URIUtil.getName(uri));
    }

    @java.lang.Override
    public void doIt(org.jhotdraw.api.app.View v) {
        final org.jhotdraw.api.app.Application app = getApplication();
        // Prevent same URI from being opened more than once
        if (!getApplication().getModel().isAllowMultipleViewsPerURI()) {
            for (org.jhotdraw.api.app.View vw : getApplication().getViews()) {
                if ((vw.getURI() != null) && vw.getURI().equals(uri)) {
                    vw.getComponent().requestFocus();
                    return;
                }
            }
        }
        // Search for an empty view
        if (v == null) {
            org.jhotdraw.api.app.View emptyView = app.getActiveView();
            if (((emptyView == null) || (emptyView.getURI() != null)) || emptyView.hasUnsavedChanges()) {
                emptyView = null;
            }
            if (emptyView == null) {
                v = app.createView();
                app.add(v);
                app.show(v);
            } else {
                v = emptyView;
            }
        }
        final org.jhotdraw.api.app.View view = v;
        app.setEnabled(true);
        view.setEnabled(false);
        // If there is another view with the same file we set the multiple open
        // id of our view to max(multiple open id) + 1.
        int multipleOpenId = 1;
        for (org.jhotdraw.api.app.View aView : app.views()) {
            if (((aView != view) && (aView.getURI() != null)) && aView.getURI().equals(uri)) {
                multipleOpenId = java.lang.Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
            }
        }
        view.setMultipleOpenId(multipleOpenId);
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
                    throw new java.io.IOException(labels.getFormatted("file.load.fileDoesNotExist.message", org.jhotdraw.net.URIUtil.getName(uri)));
                }
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    final org.jhotdraw.api.app.Application app = getApplication();
                    view.setURI(uri);
                    app.addRecentURI(uri);
                    java.awt.Frame w = ((java.awt.Frame) (javax.swing.SwingUtilities.getWindowAncestor(view.getComponent())));
                    if (w != null) {
                        w.setExtendedState(w.getExtendedState() & (~java.awt.Frame.ICONIFIED));
                        w.toFront();
                    }
                    view.getComponent().requestFocus();
                    app.setEnabled(true);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.LoadRecentFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    failed(ex);
                }
                finished();
            }

            protected void failed(java.lang.Throwable error) {
                error.printStackTrace();
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.load.couldntLoad.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + error, javax.swing.JOptionPane.ERROR_MESSAGE, new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                        // app.dispose(view);
                    }
                });
            }

            protected void finished() {
                view.setEnabled(true);
            }
        }.execute();
    }
}