/* @(#)OpenFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
import org.jhotdraw.api.gui.URIChooser;
/**
 * Presents an {@code URIChooser} and loads the selected URI into an empty view. If no empty view is
 * available, a new view is created.
 *
 * <p>This action is called when the user selects the Open item in the File menu. The menu item is
 * automatically created by the application. A Recent Files sub-menu is also automatically
 * generated.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>This action is designed for applications which automatically create a new view for each opened
 * file. This action goes together with {@link NewFileAction}, {@link OpenDirectoryAction} and
 * {@link CloseFileAction}. This action should not be used together with {@link LoadFileAction}.
 * <hr> <b>Features</b>
 *
 * <p><em>Allow multiple views per URI</em><br>
 * When the feature is disabled, {@code OpenFileAction} prevents opening an URI which* is opened in
 * another view.<br>
 * See {@link org.jhotdraw.app} for a description of the feature.
 *
 * <p><em>Open last URI on launch</em><br>
 * {@code OpenFileAction} supplies data for this feature by calling {@link Application#addRecentURI}
 * when it successfully opened a file. See {@link org.jhotdraw.app} for a description of the
 * feature.
 */
public class OpenFileAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.open";

    public OpenFileAction(org.jhotdraw.api.app.Application app) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.OpenFileAction.ID);
    }

    protected org.jhotdraw.api.gui.URIChooser getChooser(org.jhotdraw.api.app.View view) {
        // Note: We pass null here, because we want the application-wide chooser
        return getApplication().getOpenChooser(null);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.Application app = getApplication();
        if (app.isEnabled()) {
            app.setEnabled(false);
            // Search for an empty view
            org.jhotdraw.api.app.View emptyView = app.getActiveView();
            if (((emptyView == null) || (!emptyView.isEmpty())) || (!emptyView.isEnabled())) {
                emptyView = null;
            }
            final org.jhotdraw.api.app.View view;
            boolean disposeView;
            if (emptyView == null) {
                view = app.createView();
                app.add(view);
                disposeView = true;
            } else {
                view = emptyView;
                disposeView = false;
            }
            org.jhotdraw.api.gui.URIChooser chooser = getChooser(view);
            chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            if (showDialog(chooser, app.getComponent()) == javax.swing.JFileChooser.APPROVE_OPTION) {
                app.show(view);
                java.net.URI uri = chooser.getSelectedURI();
                // Prevent same URI from being opened more than once
                if (!getApplication().getModel().isAllowMultipleViewsPerURI()) {
                    for (org.jhotdraw.api.app.View v : getApplication().getViews()) {
                        if ((v.getURI() != null) && v.getURI().equals(uri)) {
                            v.getComponent().requestFocus();
                            if (disposeView) {
                                app.dispose(view);
                            }
                            app.setEnabled(true);
                            return;
                        }
                    }
                }
                openViewFromURI(view, uri, chooser);
            } else {
                if (disposeView) {
                    app.dispose(view);
                }
                app.setEnabled(true);
            }
        }
    }

    protected void openViewFromURI(final org.jhotdraw.api.app.View view, final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        final org.jhotdraw.api.app.Application app = getApplication();
        app.setEnabled(true);
        view.setEnabled(false);
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
                    exists = new java.io.File(uri).exists();
                } catch (java.lang.IllegalArgumentException e) {
                    // allowed empty
                }
                if (exists) {
                    view.read(uri, chooser);
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
                    final org.jhotdraw.api.app.Application app = getApplication();
                    view.setURI(uri);
                    view.setEnabled(true);
                    java.awt.Frame w = ((java.awt.Frame) (javax.swing.SwingUtilities.getWindowAncestor(view.getComponent())));
                    if (w != null) {
                        w.setExtendedState(w.getExtendedState() & (~java.awt.Frame.ICONIFIED));
                        w.toFront();
                    }
                    view.getComponent().requestFocus();
                    app.addRecentURI(uri);
                    app.setEnabled(true);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.file.OpenFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    failed(ex);
                }
            }

            protected void failed(java.lang.Throwable value) {
                value.printStackTrace();
                view.setEnabled(true);
                app.setEnabled(true);
                java.lang.String message = (value.getMessage() != null) ? value.getMessage() : value.toString();
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.open.couldntOpen.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + (message == null ? "" : message), javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }.execute();
    }

    /**
     * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on
     * screen on Mac OS X.
     */
    public int showDialog(org.jhotdraw.api.gui.URIChooser chooser, java.awt.Component parent) {
        final java.awt.Component finalParent = parent;
        final int[] returnValue = new int[1];
        final javax.swing.JDialog dialog = createDialog(chooser, finalParent);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                returnValue[0] = javax.swing.JFileChooser.CANCEL_OPTION;
            }
        });
        chooser.addActionListener(new java.awt.event.ActionListener() {
            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if ("CancelSelection".equals(e.getActionCommand())) {
                    returnValue[0] = javax.swing.JFileChooser.CANCEL_OPTION;
                    dialog.setVisible(false);
                } else if ("ApproveSelection".equals(e.getActionCommand())) {
                    returnValue[0] = javax.swing.JFileChooser.APPROVE_OPTION;
                    dialog.setVisible(false);
                }
            }
        });
        returnValue[0] = javax.swing.JFileChooser.ERROR_OPTION;
        chooser.rescanCurrentDirectory();
        dialog.setVisible(true);
        // chooser.firePropertyChange("JFileChooserDialogIsClosingProperty", dialog, null);
        dialog.removeAll();
        dialog.dispose();
        return returnValue[0];
    }

    /**
     * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on
     * screen on Mac OS X.
     */
    protected javax.swing.JDialog createDialog(org.jhotdraw.api.gui.URIChooser chooser, java.awt.Component parent) throws java.awt.HeadlessException {
        java.lang.String title = chooser.getDialogTitle();
        if (chooser instanceof javax.swing.JFileChooser) {
            ((javax.swing.JFileChooser) (chooser)).getAccessibleContext().setAccessibleDescription(title);
        }
        javax.swing.JDialog dialog;
        java.awt.Window window = ((parent == null) || (parent instanceof java.awt.Window)) ? ((java.awt.Window) (parent)) : javax.swing.SwingUtilities.getWindowAncestor(parent);
        dialog = new javax.swing.JDialog(window, title, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setComponentOrientation(chooser.getComponent().getComponentOrientation());
        java.awt.Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new java.awt.BorderLayout());
        contentPane.add(chooser.getComponent(), java.awt.BorderLayout.CENTER);
        if (javax.swing.JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = javax.swing.UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.FILE_CHOOSER_DIALOG);
            }
        }
        // dialog.pack();
        java.util.prefs.Preferences prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getApplication().getModel().getClass());
        org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "openChooser", dialog);
        /* if (window.getBounds().isEmpty()) {
        Rectangle screenBounds = window.getGraphicsConfiguration().getBounds();
        dialog.setLocation(screenBounds.x + (screenBounds.width - dialog.getWidth()) / 2,
        screenBounds.y + (screenBounds.height - dialog.getHeight()) / 3);
        } else {
        dialog.setLocationRelativeTo(parent);
        }
         */
        return dialog;
    }
}