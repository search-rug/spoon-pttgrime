/* @(#)LoadFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
import org.jhotdraw.api.gui.URIChooser;
/**
 * Lets the user save unsaved changes of the active view, then presents an {@code URIChooser} and
 * loads the selected URI into the active view.
 *
 * <p>This action is called when the user selects the Load item in the File menu. The menu item is
 * automatically created by the application. A Recent Files sub-menu is also automatically
 * generated.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>This action is designed for applications which do not automatically create a new view for each
 * opened file. This action goes together with {@link ClearFileAction}, {@link NewWindowAction},
 * {@link LoadFileAction}, {@link LoadDirectoryAction} and {@link CloseFileAction}. This action
 * should not be used together with {@link OpenFileAction}.
 *
 * <p><hr> <b>Features</b>
 *
 * <p><em>Open last URI on launch</em><br>
 * When the application is started, the last opened URI is opened in a view.<br>
 * {@code LoadFileAction} supplies data for this feature by calling {@link Application#addRecentURI}
 * when it successfully loaded a file. See {@link org.jhotdraw.app} for a description of the
 * feature.
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code LoadFileAction} prevents exporting to an URI which is opened
 * in another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 */
public class LoadFileAction extends org.jhotdraw.app.action.AbstractSaveUnsavedChangesAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.load";

    public LoadFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.LoadFileAction.ID);
    }

    @java.lang.Override
    protected org.jhotdraw.api.gui.URIChooser getChooser(org.jhotdraw.api.app.View view) {
        org.jhotdraw.api.gui.URIChooser chsr = ((org.jhotdraw.api.gui.URIChooser) (view.getComponent().getClientProperty("loadChooser")));
        if (chsr == null) {
            chsr = getApplication().getModel().createOpenChooser(getApplication(), view);
            view.getComponent().putClientProperty("loadChooser", chsr);
        }
        return chsr;
    }

    @java.lang.Override
    public void doIt(final org.jhotdraw.api.app.View view) {
        org.jhotdraw.api.gui.URIChooser fileChooser = getChooser(view);
        java.awt.Window wAncestor = javax.swing.SwingUtilities.getWindowAncestor(view.getComponent());
        final java.awt.Component oldFocusOwner = (wAncestor == null) ? null : wAncestor.getFocusOwner();
        org.jhotdraw.gui.JSheet.showOpenSheet(fileChooser, view.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
            @java.lang.Override
            public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                if (evt.getOption() == javax.swing.JFileChooser.APPROVE_OPTION) {
                    final java.net.URI uri = evt.getChooser().getSelectedURI();
                    // Prevent same URI from being opened more than once
                    if (!getApplication().getModel().isAllowMultipleViewsPerURI()) {
                        for (org.jhotdraw.api.app.View v : getApplication().getViews()) {
                            if (((v != view) && (v.getURI() != null)) && v.getURI().equals(uri)) {
                                v.getComponent().requestFocus();
                                return;
                            }
                        }
                    }
                    loadViewFromURI(view, uri, evt.getChooser());
                } else {
                    view.setEnabled(true);
                    if (oldFocusOwner != null) {
                        oldFocusOwner.requestFocus();
                    }
                }
            }
        });
    }

    public void loadViewFromURI(final org.jhotdraw.api.app.View view, final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        view.setEnabled(false);
        // Open the file
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                view.read(uri, chooser);
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    view.setURI(uri);
                    view.setEnabled(true);
                    getApplication().addRecentURI(uri);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.LoadFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    failed(ex);
                }
            }

            protected void failed(java.lang.Throwable value) {
                value.printStackTrace();
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.load.couldntLoad.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + (value == null ? "" : value), javax.swing.JOptionPane.ERROR_MESSAGE, new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                        view.clear();
                        view.setEnabled(true);
                    }
                });
            }
        }.execute();
    }
}