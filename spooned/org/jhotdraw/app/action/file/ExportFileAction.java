/* @(#)ExportFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
import org.jhotdraw.api.app.ApplicationModel;
import org.jhotdraw.api.gui.URIChooser;
/**
 * Presents a file chooser to the user and then exports the contents of the active view to the
 * chosen file.
 *
 * <p>This action requires that {@link ApplicationModel#createExportChooser} creates an appropriate
 * {@link URIChooser}.
 *
 * <p>This action is called when the user selects the Export item in the File menu. The menu item is
 * automatically created by the application.
 *
 * <p>When the {@code proposeFileName} property is set on the action, the action will propose the
 * file name without an extension in the URI chooser. Otherwise, the file name will be left empty.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link ApplicationModel#initApplication}. <hr>
 * <b>Features</b>
 *
 * <p><em>Allow multiple views for URI</em><br>
 * When the feature is disabled, {@code ExportFileAction} prevents exporting to an URI which is
 * opened in another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 */
public class ExportFileAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.export";

    private java.awt.Component oldFocusOwner;

    private boolean proposeFileName;

    public ExportFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        this(app, view, false);
    }

    public ExportFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, boolean proposeFileName) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.ExportFileAction.ID);
        this.proposeFileName = proposeFileName;
    }

    /**
     * Whether the export file action shall propose a file name or shall leave the filename empty.
     *
     * @return True if filename is proposed.
     */
    public boolean isProposeFileName() {
        return proposeFileName;
    }

    /**
     * Whether the export file action shall propose a file name or shall leave the filename empty.
     *
     * @param newValue
     * 		True if filename shall be proposed.
     */
    public void setProposeFileName(boolean newValue) {
        this.proposeFileName = newValue;
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.View view = getActiveView();
        if (view.isEnabled()) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
            oldFocusOwner = javax.swing.SwingUtilities.getWindowAncestor(view.getComponent()).getFocusOwner();
            view.setEnabled(false);
            try {
                org.jhotdraw.api.gui.URIChooser fileChooser = getApplication().getExportChooser(view);
                if (proposeFileName) {
                    // => try to propose file name without extension
                    java.net.URI proposedURI = view.getURI();
                    if (proposedURI != null) {
                        try {
                            java.net.URI selectedURI = fileChooser.getSelectedURI();
                            java.io.File selectedFolder;
                            if (selectedURI == null) {
                                java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getApplication().getModel().getClass());
                                try {
                                    selectedURI = new java.net.URI(prefs.get("recentExportFile", new java.io.File(proposedURI).getParentFile().toURI().toString()));
                                    selectedFolder = new java.io.File(selectedURI).getParentFile();
                                } catch (java.net.URISyntaxException ex) {
                                    // selectedURI is null
                                    selectedFolder = new java.io.File(proposedURI).getParentFile();
                                }
                            } else {
                                selectedFolder = new java.io.File(selectedURI).getParentFile();
                            }
                            java.io.File file = new java.io.File(selectedFolder, new java.io.File(proposedURI).getName());
                            java.lang.String name = file.getName();
                            int p = name.lastIndexOf('.');
                            if (p != (-1)) {
                                name = name.substring(0, p);
                                file = new java.io.File(selectedFolder, name);
                                proposedURI = file.toURI();
                            }
                        } catch (java.lang.IllegalArgumentException e) {
                            // allowed empty
                        }
                    }
                    fileChooser.setSelectedURI(proposedURI);
                }
                org.jhotdraw.gui.JSheet.showSheet(fileChooser, view.getComponent(), labels.getString("filechooser.export"), new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                        if (evt.getOption() == javax.swing.JFileChooser.APPROVE_OPTION) {
                            java.net.URI uri = evt.getChooser().getSelectedURI();
                            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(getApplication().getModel().getClass());
                            prefs.put("recentExportFile", uri.toString());
                            if (evt.getChooser() instanceof org.jhotdraw.gui.JFileURIChooser) {
                                exportView(view, uri, evt.getChooser());
                            } else {
                                exportView(view, uri, null);
                            }
                        } else {
                            view.setEnabled(true);
                            if (oldFocusOwner != null) {
                                oldFocusOwner.requestFocus();
                            }
                        }
                    }
                });
            } catch (java.lang.Error err) {
                view.setEnabled(true);
                throw err;
            } catch (java.lang.Throwable err) {
                view.setEnabled(true);
                err.printStackTrace();
            }
        }
    }

    protected void exportView(final org.jhotdraw.api.app.View view, final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                view.write(uri, chooser);
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.ExportFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    java.lang.System.out.flush();
                    ex.printStackTrace();
                    // FIXME localize this error messsage
                    org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), ((((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>Couldn't export to the file \"") + org.jhotdraw.net.URIUtil.getName(uri)) + "\".<p>") + "Reason: ") + ex, javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                view.setEnabled(true);
                javax.swing.SwingUtilities.getWindowAncestor(view.getComponent()).toFront();
                if (oldFocusOwner != null) {
                    oldFocusOwner.requestFocus();
                }
            }
        }.execute();
    }
}