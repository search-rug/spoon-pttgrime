/* @(#)ViewSourceAction.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.action;
/**
 * ViewSourceAction.
 */
public class ViewSourceAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.viewSource";

    /**
     * We store the dialog as a client property in the view.
     */
    private static final java.lang.String DIALOG_CLIENT_PROPERTY = "view.viewSource.dialog";

    public ViewSourceAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        labels.configureAction(this, org.jhotdraw.samples.svg.action.ViewSourceAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        final org.jhotdraw.samples.svg.SVGView v = ((org.jhotdraw.samples.svg.SVGView) (getActiveView()));
        org.jhotdraw.draw.Drawing drawing = v.getDrawing();
        final javax.swing.JDialog dialog;
        if (v.getClientProperty(org.jhotdraw.samples.svg.action.ViewSourceAction.DIALOG_CLIENT_PROPERTY) == null) {
            dialog = new javax.swing.JDialog(javax.swing.SwingUtilities.getWindowAncestor(v.getComponent()));
            v.putClientProperty(org.jhotdraw.samples.svg.action.ViewSourceAction.DIALOG_CLIENT_PROPERTY, dialog);
            dialog.setTitle(labels.getFormatted("view.viewSource.titleText", v.getTitle()));
            dialog.setResizable(true);
            dialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            final javax.swing.JTextArea ta = new javax.swing.JTextArea();
            ta.setWrapStyleWord(true);
            ta.setLineWrap(true);
            javax.swing.JScrollPane sp = new javax.swing.JScrollPane(ta);
            // sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            dialog.getContentPane().add(sp);
            dialog.setSize(400, 400);
            dialog.setLocationByPlatform(true);
            updateSource(drawing, ta);
            final javax.swing.event.UndoableEditListener undoableEditHandler = new javax.swing.event.UndoableEditListener() {
                @java.lang.Override
                public void undoableEditHappened(javax.swing.event.UndoableEditEvent e) {
                    updateSource(v.getDrawing(), ta);
                }
            };
            v.getDrawing().addUndoableEditListener(undoableEditHandler);
            final java.beans.PropertyChangeListener propertyChangeHandler = new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    if (evt.getPropertyName() == org.jhotdraw.samples.svg.SVGView.DRAWING_PROPERTY) {
                        org.jhotdraw.draw.Drawing oldDrawing = ((org.jhotdraw.draw.Drawing) (evt.getOldValue()));
                        if (oldDrawing != null) {
                            oldDrawing.removeUndoableEditListener(undoableEditHandler);
                        }
                        org.jhotdraw.draw.Drawing newDrawing = ((org.jhotdraw.draw.Drawing) (evt.getNewValue()));
                        if (newDrawing != null) {
                            newDrawing.addUndoableEditListener(undoableEditHandler);
                        }
                        if (newDrawing != null) {
                            updateSource(newDrawing, ta);
                        }
                    } else if (evt.getPropertyName() == org.jhotdraw.api.app.View.TITLE_PROPERTY) {
                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                        dialog.setTitle(labels.getFormatted("view.viewSource.titleText", v.getTitle()));
                    }
                }
            };
            v.addPropertyChangeListener(propertyChangeHandler);
            final org.jhotdraw.api.app.Disposable disposable = new org.jhotdraw.api.app.Disposable() {
                @java.lang.Override
                public void dispose() {
                    if (v.getDrawing() != null) {
                        v.getDrawing().removeUndoableEditListener(undoableEditHandler);
                    }
                    v.removePropertyChangeListener(propertyChangeHandler);
                    getApplication().removeWindow(dialog);
                    v.putClientProperty(org.jhotdraw.samples.svg.action.ViewSourceAction.DIALOG_CLIENT_PROPERTY, null);
                    v.removeDisposable(this);
                }
            };
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @java.lang.Override
                public void windowClosed(java.awt.event.WindowEvent evt) {
                    disposable.dispose();
                }
            });
            v.addDisposable(disposable);
        } else {
            dialog = ((javax.swing.JDialog) (v.getClientProperty(org.jhotdraw.samples.svg.action.ViewSourceAction.DIALOG_CLIENT_PROPERTY)));
            javax.swing.JTextArea ta = ((javax.swing.JTextArea) (((javax.swing.JScrollPane) (dialog.getContentPane().getComponent(0))).getViewport().getView()));
            updateSource(drawing, ta);
        }
        java.util.prefs.Preferences prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
        org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "viewSource", dialog);
        getApplication().addWindow(dialog, v);
        dialog.setVisible(true);
    }

    private void updateSource(org.jhotdraw.draw.Drawing drawing, javax.swing.JTextArea textArea) {
        org.jhotdraw.samples.svg.io.SVGOutputFormat format = new org.jhotdraw.samples.svg.io.SVGOutputFormat();
        format.setPrettyPrint(true);
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        try {
            format.write(buf, drawing);
            java.lang.String source = buf.toString("UTF-8");
            textArea.setText(source);
        } catch (java.io.IOException ex) {
            textArea.setText(ex.toString());
        }
    }
}