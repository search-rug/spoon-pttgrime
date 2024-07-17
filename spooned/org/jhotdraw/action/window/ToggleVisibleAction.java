/* @(#)ToggleVisibleAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.window;
/**
 * Toggles the visible state of a Component. Is selected, when the Component is visible.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ToggleVisibleAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    private java.awt.Component component;

    public ToggleVisibleAction(java.awt.Component c, java.lang.String name) {
        this.component = c;
        putValue(javax.swing.Action.NAME, name);
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, c.isVisible());
        c.addComponentListener(new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, component.isVisible());
            }

            @java.lang.Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, component.isVisible());
            }
        });
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        component.setVisible(!component.isVisible());
    }
}