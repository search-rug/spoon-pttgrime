/* @(#)OSXOpenFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
import org.jhotdraw.app.PrintableView;
import org.jhotdraw.app.action.file.PrintFileAction;
/**
 * Prints a file for which a print request was sent to the application.
 *
 * <p>The file name is passed in the action command of the action event.
 *
 * <p>This action is called when {@code DefaultOSXApplication} receives a print request from another
 * application. The file name is passed in the action command of the action event.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}. The views created by {@code ApplicationModel}
 * must implement the {@link PrintableView} interface.
 *
 * <p>You should also create a {@link PrintFileAction} when you create this action.
 */
public class PrintApplicationFileAction extends org.jhotdraw.app.action.file.PrintFileAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.printFile";

    private javax.swing.JFileChooser fileChooser;

    private int entries;

    public PrintApplicationFileAction(org.jhotdraw.api.app.Application app) {
        super(app, null);
        putValue(javax.swing.Action.NAME, "OSX Print File");
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.Application app = getApplication();
        final java.lang.String filename = evt.getActionCommand();
        org.jhotdraw.api.app.View v = app.createView();
        if (!(v instanceof org.jhotdraw.app.PrintableView)) {
            return;
        }
        final org.jhotdraw.app.PrintableView p = ((org.jhotdraw.app.PrintableView) (v));
        p.setEnabled(false);
        app.add(p);
        // app.show(p);
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                p.read(new java.io.File(filename).toURI(), null);
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    p.setURI(new java.io.File(filename).toURI());
                    p.setEnabled(false);
                    if ("true".equals(java.lang.System.getProperty("apple.awt.graphics.UseQuartz", "false"))) {
                        printQuartz(p);
                    } else {
                        printJava2D(p);
                    }
                    p.setEnabled(true);
                    app.dispose(p);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.app.PrintApplicationFileAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    app.dispose(p);
                    javax.swing.JOptionPane.showMessageDialog(null, (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.open.couldntOpen.message", new java.io.File(filename).getName())) + "</b><p>") + ex, "", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}