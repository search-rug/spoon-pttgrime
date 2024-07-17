/* @(#)ToggleToolBarAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.window;
/**
 * ToggleToolBarAction.
 */
public class ToggleToolBarAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    private javax.swing.JToolBar toolBar;

    private java.beans.PropertyChangeListener propertyHandler;

    public ToggleToolBarAction(javax.swing.JToolBar toolBar, java.lang.String label) {
        super(label);
        propertyHandler = new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                java.lang.String name = evt.getPropertyName();
                if ("visible".equals(name)) {
                    putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, evt.getNewValue());
                }
            }
        };
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, true);
        setToolBar(toolBar);
    }

    @java.lang.Override
    public void putValue(java.lang.String key, java.lang.Object newValue) {
        super.putValue(key, newValue);
        if (org.jhotdraw.util.ActionUtil.SELECTED_KEY.equals(key)) {
            if (toolBar != null) {
                toolBar.setVisible(((java.lang.Boolean) (newValue)));
            }
        }
    }

    public void setToolBar(javax.swing.JToolBar newValue) {
        if (toolBar != null) {
            toolBar.removePropertyChangeListener(propertyHandler);
        }
        toolBar = newValue;
        if (toolBar != null) {
            toolBar.addPropertyChangeListener(propertyHandler);
            putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, toolBar.isVisible());
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (toolBar != null) {
            putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, !toolBar.isVisible());
        }
    }
}