/* @(#)MaximizeWindowAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.window;
/**
 * Maximizes the window of the active view.
 */
public class MaximizeWindowAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "window.maximize";

    public MaximizeWindowAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.window.MaximizeWindowAction.ID);
    }

    private javax.swing.JFrame getFrame() {
        return ((javax.swing.JFrame) (javax.swing.SwingUtilities.getWindowAncestor(getActiveView().getComponent())));
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFrame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(frame.getExtendedState() ^ java.awt.Frame.MAXIMIZED_BOTH);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
}