/* @(#)OSXOpenApplicationAction.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * If all views are iconified, de-iconifies a view and brings it to the front.
 *
 * <p>This action is called when the Mac OS X Finder or another Mac OS X application sends an Open
 * Application request to the application.
 *
 * <p>This action is automatically created by {@code DefaultOSXApplication} and put into the {@code ApplicationModel} before {@link org.jhotdraw.app.ApplicationModel#initApplication} is called.
 */
public class ReOpenApplicationAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.reOpen";

    public ReOpenApplicationAction(org.jhotdraw.api.app.Application app) {
        super(app);
        putValue(javax.swing.Action.NAME, "OSX ReOpen Application");
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        org.jhotdraw.api.app.Application a = getApplication();
        if ((a.getActiveView() == null) && (a.views().size() > 0)) {
            org.jhotdraw.api.app.View v = a.views().iterator().next();
            java.awt.Component c = javax.swing.SwingUtilities.getRootPane(v.getComponent()).getParent();
            if (c instanceof javax.swing.JFrame) {
                javax.swing.JFrame f = ((javax.swing.JFrame) (c));
                if ((f.getExtendedState() & javax.swing.JFrame.ICONIFIED) != 0) {
                    f.setExtendedState(f.getExtendedState() ^ javax.swing.JFrame.ICONIFIED);
                }
                f.requestFocus();
            }
        }
    }
}