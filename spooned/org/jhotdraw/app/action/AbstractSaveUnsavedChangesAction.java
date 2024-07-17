/* @(#)AbstractSaveUnsavedChangesAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action;
/**
 * This abstract class can be extended to implement an {@code Action} that asks to save unsaved
 * changes of a {@link org.jhotdraw.api.app.View} before a destructive action is performed.
 *
 * <p>If the view has no unsaved changes, method {@code doIt} is invoked immediately. If unsaved
 * changes are present, a dialog is shown asking whether the user wants to discard the changes,
 * cancel or save the changes before doing it. If the user chooses to discard the changes, {@code doIt} is invoked immediately. If the user chooses to cancel, the action is aborted. If the user
 * chooses to save the changes, the view is saved, and {@code doIt} is only invoked after the view
 * was successfully saved.
 */
public abstract class AbstractSaveUnsavedChangesAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    private java.awt.Component oldFocusOwner;

    public AbstractSaveUnsavedChangesAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.api.app.Application app = getApplication();
        org.jhotdraw.api.app.View av = getActiveView();
        if (av == null) {
            if (isMayCreateView()) {
                av = app.createView();
                app.add(av);
                app.show(av);
            } else {
                return;
            }
        }
        final org.jhotdraw.api.app.View v = av;
        if (v.isEnabled()) {
            final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
            java.awt.Window wAncestor = javax.swing.SwingUtilities.getWindowAncestor(v.getComponent());
            oldFocusOwner = (wAncestor == null) ? null : wAncestor.getFocusOwner();
            v.setEnabled(false);
            if (v.hasUnsavedChanges()) {
                java.net.URI unsavedURI = v.getURI();
                javax.swing.JOptionPane pane = new javax.swing.JOptionPane((((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.saveBefore.doYouWantToSave.message", unsavedURI == null ? labels.getString("unnamedFile") : org.jhotdraw.net.URIUtil.getName(unsavedURI))) + "</b><p>") + labels.getString("file.saveBefore.doYouWantToSave.details"), javax.swing.JOptionPane.WARNING_MESSAGE);
                java.lang.Object[] options = new java.lang.Object[]{ labels.getString("file.saveBefore.saveOption.text"), labels.getString("file.saveBefore.cancelOption.text"), labels.getString("file.saveBefore.dontSaveOption.text") };
                pane.setOptions(options);
                pane.setInitialValue(options[0]);
                pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 2);
                org.jhotdraw.gui.JSheet.showSheet(pane, v.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                    @java.lang.Override
                    public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                        java.lang.Object value = evt.getValue();
                        if ((value == null) || value.equals(labels.getString("file.saveBefore.cancelOption.text"))) {
                            v.setEnabled(true);
                        } else if (value.equals(labels.getString("file.saveBefore.dontSaveOption.text"))) {
                            doIt(v);
                            v.setEnabled(true);
                        } else if (value.equals(labels.getString("file.saveBefore.saveOption.text"))) {
                            saveView(v);
                        }
                    }
                });
            } else {
                doIt(v);
                v.setEnabled(true);
                if (oldFocusOwner != null) {
                    oldFocusOwner.requestFocus();
                }
            }
        }
    }

    protected org.jhotdraw.api.gui.URIChooser getChooser(org.jhotdraw.api.app.View view) {
        org.jhotdraw.api.gui.URIChooser chsr = ((org.jhotdraw.api.gui.URIChooser) (view.getComponent().getClientProperty("saveChooser")));
        if (chsr == null) {
            chsr = getApplication().getModel().createSaveChooser(getApplication(), view);
            view.getComponent().putClientProperty("saveChooser", chsr);
        }
        return chsr;
    }

    protected void saveView(final org.jhotdraw.api.app.View v) {
        if (v.getURI() == null) {
            org.jhotdraw.api.gui.URIChooser chooser = getChooser(v);
            // int option = fileChooser.showSaveDialog(this);
            org.jhotdraw.gui.JSheet.showSaveSheet(chooser, v.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                @java.lang.Override
                public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                    if (evt.getOption() == javax.swing.JFileChooser.APPROVE_OPTION) {
                        saveViewToURI(v, evt.getChooser().getSelectedURI(), evt.getChooser());
                    } else {
                        v.setEnabled(true);
                        if (oldFocusOwner != null) {
                            oldFocusOwner.requestFocus();
                        }
                    }
                }
            });
        } else {
            saveViewToURI(v, v.getURI(), null);
        }
    }

    protected void saveViewToURI(final org.jhotdraw.api.app.View v, final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                v.write(uri, chooser);
                return null;
            }

            @java.lang.Override
            protected void done() {
                try {
                    get();
                    v.setURI(uri);
                    v.markChangesAsSaved();
                    doIt(v);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.AbstractSaveUnsavedChangesAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    java.lang.String message = (ex.getMessage() != null) ? ex.getMessage() : ex.toString();
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(getActiveView().getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("file.save.couldntSave.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + (message == null ? "" : message), javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                v.setEnabled(true);
                if (oldFocusOwner != null) {
                    oldFocusOwner.requestFocus();
                }
            }
        }.execute();
    }

    protected abstract void doIt(org.jhotdraw.api.app.View p);
}