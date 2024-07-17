/* @(#)ExitAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * Exits the application after letting the user review all unsaved views.
 *
 * <p>This action is called when the user selects the Exit item in the Application menu, or when the
 * application receives a Quit event from Mac OS X Finder. The menu item is automatically created by
 * the application.
 *
 * <p>This action is automatically created by the application and put into the {@code ApplicationModel} before {@link org.jhotdraw.app.ApplicationModel#initApplication} is called.
 */
public class ExitAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.exit";

    private java.awt.Component oldFocusOwner;

    private org.jhotdraw.api.app.View unsavedView;

    public ExitAction(org.jhotdraw.api.app.Application app) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.app.ExitAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final org.jhotdraw.api.app.Application app = getApplication();
        if (app.isEnabled()) {
            app.setEnabled(false);
            int unsavedViewsCount = 0;
            org.jhotdraw.api.app.View documentToBeReviewed = null;
            java.net.URI unsavedURI = null;
            for (org.jhotdraw.api.app.View p : app.views()) {
                if (p.hasUnsavedChanges()) {
                    if (p.isEnabled()) {
                        documentToBeReviewed = p;
                    }
                    unsavedURI = p.getURI();
                    unsavedViewsCount++;
                }
            }
            if ((unsavedViewsCount > 0) && (documentToBeReviewed == null)) {
                // Silently abort, if no view can be reviewed.
                app.setEnabled(true);
                return;
            }
            final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
            switch (unsavedViewsCount) {
                case 0 :
                    doExit();
                    break;
                case 1 :
                    unsavedView = documentToBeReviewed;
                    oldFocusOwner = javax.swing.SwingUtilities.getWindowAncestor(unsavedView.getComponent()).getFocusOwner();
                    unsavedView.setEnabled(false);
                    javax.swing.JOptionPane pane = new javax.swing.JOptionPane((((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getFormatted("application.exit.doYouWantToSave.message", unsavedURI == null ? labels.getString("unnamedFile") : org.jhotdraw.net.URIUtil.getName(unsavedURI))) + "</b><p>") + labels.getString("application.exit.doYouWantToSave.details"), javax.swing.JOptionPane.WARNING_MESSAGE);
                    java.lang.Object[] options = new java.lang.Object[]{ labels.getString("application.exit.saveOption"), labels.getString("application.exit.cancelOption"), labels.getString("application.exit.dontSaveOption") };
                    pane.setOptions(options);
                    pane.setInitialValue(options[0]);
                    pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 2);
                    org.jhotdraw.gui.JSheet.showSheet(pane, unsavedView.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                        @java.lang.Override
                        public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                            java.lang.Object value = evt.getValue();
                            if ((value == null) || value.equals(labels.getString("application.exit.cancelOption"))) {
                                unsavedView.setEnabled(true);
                                app.setEnabled(true);
                            } else if (value.equals(labels.getString("application.exit.dontSaveOption"))) {
                                doExit();
                                unsavedView.setEnabled(true);
                            } else if (value.equals(labels.getString("application.exit.saveOption"))) {
                                saveChanges();
                            }
                        }
                    });
                    break;
                default :
                    pane = new javax.swing.JOptionPane((((("<html>" + javax.swing.UIManager.get("OptionPane.css")) + "<b>") + labels.getFormatted("application.exit.doYouWantToReview.message", unsavedViewsCount)) + "</b><p>") + labels.getString("application.exit.doYouWantToReview.details"), javax.swing.JOptionPane.QUESTION_MESSAGE);
                    java.lang.Object[] options2 = new java.lang.Object[]{ labels.getString("application.exit.reviewChangesOption"), labels.getString("application.exit.cancelOption"), labels.getString("application.exit.discardChangesOption") };
                    pane.setOptions(options2);
                    pane.setInitialValue(options2[0]);
                    pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 2);
                    javax.swing.JDialog dialog = pane.createDialog(app.getComponent(), null);
                    java.awt.Rectangle screenBounds = dialog.getGraphicsConfiguration().getBounds();
                    if ((app.getComponent() == null) || (!screenBounds.contains(app.getComponent().getBounds()))) {
                        // place dialog in center and in upper third of screen
                        dialog.setLocation((screenBounds.width - dialog.getWidth()) / 2, (screenBounds.height - dialog.getHeight()) / 3);
                    }
                    dialog.setVisible(true);
                    java.lang.Object value = pane.getValue();
                    if ((value == null) || value.equals(labels.getString("application.exit.cancelOption"))) {
                        app.setEnabled(true);
                    } else if (value.equals(labels.getString("application.exit.discardChangesOption"))) {
                        doExit();
                        app.setEnabled(true);
                    } else if (value.equals(labels.getString("application.exit.reviewChangesOption"))) {
                        unsavedView = documentToBeReviewed;
                        reviewChanges();
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

    protected void saveChanges() {
        org.jhotdraw.api.app.View v = unsavedView;
        if (v.getURI() == null) {
            org.jhotdraw.api.gui.URIChooser chooser = getChooser(v);
            // int option = fileChooser.showSaveDialog(this);
            org.jhotdraw.gui.JSheet.showSaveSheet(chooser, v.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                @java.lang.Override
                public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                    if (evt.getOption() == javax.swing.JFileChooser.APPROVE_OPTION) {
                        final java.net.URI uri = evt.getChooser().getSelectedURI();
                        saveToFile(uri, evt.getChooser());
                    } else {
                        unsavedView.setEnabled(true);
                        if (oldFocusOwner != null) {
                            oldFocusOwner.requestFocus();
                        }
                        getApplication().setEnabled(true);
                    }
                }
            });
        } else {
            saveToFile(v.getURI(), null);
        }
    }

    protected void reviewChanges() {
        if (unsavedView.isEnabled()) {
            final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
            oldFocusOwner = javax.swing.SwingUtilities.getWindowAncestor(unsavedView.getComponent()).getFocusOwner();
            unsavedView.setEnabled(false);
            java.net.URI unsavedURI = unsavedView.getURI();
            javax.swing.JOptionPane pane = new javax.swing.JOptionPane(("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + labels.getFormatted("application.exit.doYouWantToSave.message", unsavedURI == null ? unsavedView.getTitle() : org.jhotdraw.net.URIUtil.getName(unsavedURI)), javax.swing.JOptionPane.WARNING_MESSAGE);
            java.lang.Object[] options = new java.lang.Object[]{ labels.getString("application.exit.saveOption"), labels.getString("application.exit.cancelOption"), labels.getString("application.exit.dontSaveOption") };
            pane.setOptions(options);
            pane.setInitialValue(options[0]);
            pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 2);
            org.jhotdraw.gui.JSheet.showSheet(pane, unsavedView.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                @java.lang.Override
                public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                    java.lang.Object value = evt.getValue();
                    if ((value == null) || value.equals(labels.getString("application.exit.cancelOption"))) {
                        unsavedView.setEnabled(true);
                        getApplication().setEnabled(true);
                    } else if (value.equals(labels.getString("application.exit.dontSaveOption"))) {
                        getApplication().dispose(unsavedView);
                        reviewNext();
                    } else if (value.equals(labels.getString("application.exit.saveOption"))) {
                        saveChangesAndReviewNext();
                    }
                }
            });
        } else {
            getApplication().setEnabled(true);
        }
    }

    protected void saveChangesAndReviewNext() {
        final org.jhotdraw.api.app.View v = unsavedView;
        if (v.getURI() == null) {
            org.jhotdraw.api.gui.URIChooser chooser = getChooser(v);
            org.jhotdraw.gui.JSheet.showSaveSheet(chooser, unsavedView.getComponent(), new org.jhotdraw.gui.event.SheetListener() {
                @java.lang.Override
                public void optionSelected(final org.jhotdraw.gui.event.SheetEvent evt) {
                    if (evt.getOption() == org.jhotdraw.api.gui.URIChooser.APPROVE_OPTION) {
                        final java.net.URI uri = evt.getChooser().getSelectedURI();
                        saveToFileAndReviewNext(uri, evt.getChooser());
                    } else {
                        v.setEnabled(true);
                        if (oldFocusOwner != null) {
                            oldFocusOwner.requestFocus();
                        }
                        getApplication().setEnabled(true);
                    }
                }
            });
        } else {
            saveToFileAndReviewNext(v.getURI(), null);
        }
    }

    protected void reviewNext() {
        int unsavedViewsCount = 0;
        org.jhotdraw.api.app.View documentToBeReviewed = null;
        for (org.jhotdraw.api.app.View p : getApplication().views()) {
            if (p.hasUnsavedChanges()) {
                if (p.isEnabled()) {
                    documentToBeReviewed = p;
                }
                unsavedViewsCount++;
            }
        }
        if (unsavedViewsCount == 0) {
            doExit();
        } else if (documentToBeReviewed != null) {
            unsavedView = documentToBeReviewed;
            reviewChanges();
        } else {
            getApplication().setEnabled(true);
            // System.out.println("exit silently aborted");
        }
    }

    protected void saveToFile(final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        final org.jhotdraw.api.app.View v = unsavedView;
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
                    doExit();
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.app.ExitAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(v.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.format("file.save.couldntSave.message", org.jhotdraw.net.URIUtil.getName(uri))) + "</b><p>") + ex, javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                v.setEnabled(true);
                if (oldFocusOwner != null) {
                    oldFocusOwner.requestFocus();
                }
                getApplication().setEnabled(true);
            }
        }.execute();
    }

    protected void saveToFileAndReviewNext(final java.net.URI uri, final org.jhotdraw.api.gui.URIChooser chooser) {
        final org.jhotdraw.api.app.View v = unsavedView;
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
                    getApplication().dispose(unsavedView);
                    reviewNext();
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    java.util.logging.Logger.getLogger(org.jhotdraw.app.action.app.ExitAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(v.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.format("file.save.couldntSave.message", uri)) + "</b><p>") + ex, javax.swing.JOptionPane.ERROR_MESSAGE);
                    v.setEnabled(true);
                    if (oldFocusOwner != null) {
                        oldFocusOwner.requestFocus();
                    }
                    getApplication().setEnabled(true);
                }
            }
        }.execute();
    }

    protected void doExit() {
        getApplication().destroy();
    }
}