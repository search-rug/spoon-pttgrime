/* @(#)SaveFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
import org.jhotdraw.api.app.ApplicationModel;
import org.jhotdraw.api.gui.URIChooser;
/**
 * Saves the changes in the active view. If the active view has not an URI, an {@code URIChooser} is
 * presented.
 *
 * <p>This action is called when the user selects the Save item in the File menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link ApplicationModel#initApplication}. <hr> <b>Features</b>
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code SaveFileAction} prevents saving to an URI which is opened in
 * another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 *
 * <p><em>Open last URI on launch</em><br>
 * {@code SaveFileAction} supplies data for this feature by calling {@link Application#addRecentURI}
 * when it successfully saved a file. See {@link org.jhotdraw.app} for a description of the feature.
 */
public class SaveFileAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.save";

    private boolean saveAs;

    private java.awt.Component oldFocusOwner;

    public SaveFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        this(app, view, false);
    }

    public SaveFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, boolean saveAs) {
        super(app, view);
        this.saveAs = saveAs;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.SaveFileAction.ID);
    }

    protected org.jhotdraw.api.gui.URIChooser getChooser(org.jhotdraw.api.app.View view) {
        org.jhotdraw.api.gui.URIChooser chsr = ((org.jhotdraw.api.gui.URIChooser) (view.getComponent().getClientProperty("saveChooser")));
        if (chsr == null) {
            chsr = getApplication().getModel().createSaveChooser(getApplication(), view);
            view.getComponent().putClientProperty("saveChooser", chsr);
        }
        return chsr;
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.View view = getActiveView();
        if (view == null) {
            return;
        }
        if (view.isEnabled()) {
            oldFocusOwner = javax.swing.SwingUtilities.getWindowAncestor(view.getComponent()).getFocusOwner();
            view.setEnabled(false);
            if (((!saveAs) && (view.getURI() != null)) && view.canSaveTo(view.getURI())) {
                saveViewToURI(view, view.getURI(), null);
            } else {
                org.jhotdraw.api.gui.URIChooser fileChooser = getChooser(view);
                org.jhotdraw.gui.JSheet.showSaveSheet(fileChooser, view.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                        if (evt.getOption() == javax.swing.JFileChooser.APPROVE_OPTION) {
                            final java.net.URI uri = evt.getChooser().getSelectedURI();
                            // Prevent same URI from being opened more than once
                            if (!getApplication().getModel().isAllowMultipleViewsPerURI()) {
                                for (org.jhotdraw.api.app.View v : getApplication().getViews()) {
                                    if (((v != view) && (v.getURI() != null)) && v.getURI().equals(uri)) {
                                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                                        org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), labels.getFormatted("file.saveAs.couldntSaveIntoOpenFile.message", evt.getFileChooser().getSelectedFile().getName()));
                                        view.setEnabled(true);
                                        return;
                                    }
                                }
                            }
                            saveViewToURI(view, uri, evt.getChooser());
                        } else {
                            view.setEnabled(true);
                            if (oldFocusOwner != null) {
                                oldFocusOwner.requestFocus();
                            }
                        }
                    }
                });
            }
        }
    }

    protected void saveViewToURI(final org.jhotdraw.api.app.View view, final java.net.URI file, final org.jhotdraw.api.gui.URIChooser chooser) {
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                view.write(file, chooser);
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    view.setURI(file);
                    view.markChangesAsSaved();
                    int multiOpenId = 1;
                    for (org.jhotdraw.api.app.View p : view.getApplication().views()) {
                        if (((p != view) && (p.getURI() != null)) && p.getURI().equals(file)) {
                            multiOpenId = java.lang.Math.max(multiOpenId, p.getMultipleOpenId() + 1);
                        }
                    }
                    getApplication().addRecentURI(file);
                    view.setMultipleOpenId(multiOpenId);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.SaveFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    failed(ex);
                }
                finished();
            }

            protected void failed(java.lang.Throwable value) {
                value.printStackTrace();
                java.lang.String message = (value.getMessage() != null) ? value.getMessage() : value.toString();
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                org.jhotdraw.gui.JSheet.showMessageSheet(getActiveView().getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.save.couldntSave.message", org.jhotdraw.net.URIUtil.getName(file))) + "</b><p>") + (message == null ? "" : message), javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            protected void finished() {
                view.setEnabled(true);
                javax.swing.SwingUtilities.getWindowAncestor(view.getComponent()).toFront();
                if (oldFocusOwner != null) {
                    oldFocusOwner.requestFocus();
                }
            }
        }.execute();
    }
}