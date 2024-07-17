/* @(#)FocusWindowAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.window;
/**
 * Requests focus for a Frame.
 */
public class FocusWindowAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "window.focus";

    private org.jhotdraw.api.app.View view;

    private java.beans.PropertyChangeListener ppc;

    public FocusWindowAction(org.jhotdraw.api.app.View view) {
        this.view = view;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.window.FocusWindowAction.ID);
        // setEnabled(false);
        setEnabled(view != null);
        ppc = new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                java.lang.String name = evt.getPropertyName();
                if (name.equals(org.jhotdraw.api.app.View.TITLE_PROPERTY)) {
                    putValue(javax.swing.Action.NAME, evt.getNewValue());
                }
            }
        };
        if (view != null) {
            view.addPropertyChangeListener(ppc);
        }
    }

    public void dispose() {
        setView(null);
    }

    public void setView(org.jhotdraw.api.app.View newValue) {
        if (view != null) {
            view.removePropertyChangeListener(ppc);
        }
        view = newValue;
        if (view != null) {
            view.addPropertyChangeListener(ppc);
        }
    }

    @java.lang.Override
    public java.lang.Object getValue(java.lang.String key) {
        if (javax.swing.Action.NAME.equals(key) && (view != null)) {
            return getTitle();
        } else {
            return super.getValue(key);
        }
    }

    private java.lang.String getTitle() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        java.lang.String title = labels.getString("unnamedFile");
        if (view != null) {
            java.net.URI uri = view.getURI();
            if (uri == null) {
                title = labels.getString("unnamedFile");
            } else {
                title = org.jhotdraw.net.URIUtil.getName(uri);
            }
            if (view.hasUnsavedChanges()) {
                title += "*";
            }
            title = labels.getFormatted("internalFrame.title", title, view.getApplication() == null ? "" : view.getApplication().getName(), view.getMultipleOpenId());
        }
        return title;
    }

    private javax.swing.JFrame getFrame() {
        return ((javax.swing.JFrame) (javax.swing.SwingUtilities.getWindowAncestor(view.getComponent())));
    }

    private java.awt.Component getRootPaneContainer() {
        return javax.swing.SwingUtilities.getRootPane(view.getComponent()).getParent();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        /* JFrame frame = getFrame();
        if (frame != null) {
        frame.setExtendedState(frame.getExtendedState() & ~Frame.ICONIFIED);
        frame.toFront();
        frame.requestFocus();
        JRootPane rp = SwingUtilities.getRootPane(view.getComponent());
        if (rp != null && (rp.getParent() instanceof JInternalFrame)) {
        ((JInternalFrame) rp.getParent()).toFront();
        }
        view.getComponent().requestFocus();
        } else {
        Toolkit.getDefaultToolkit().beep();
        }
         */
        java.awt.Component rpContainer = getRootPaneContainer();
        if (rpContainer instanceof java.awt.Frame) {
            java.awt.Frame frame = ((java.awt.Frame) (rpContainer));
            frame.setExtendedState(frame.getExtendedState() & (~java.awt.Frame.ICONIFIED));
            frame.toFront();
        } else if (rpContainer instanceof javax.swing.JInternalFrame) {
            javax.swing.JInternalFrame frame = ((javax.swing.JInternalFrame) (rpContainer));
            frame.toFront();
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                // Don't care.
            }
        }
        view.getComponent().requestFocusInWindow();
    }
}