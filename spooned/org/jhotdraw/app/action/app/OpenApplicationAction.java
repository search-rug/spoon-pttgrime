/* @(#)OpenApplicationAction.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * Handles an open application request from Mac OS X (this action does nothing).
 *
 * <p>This action is called when {@code DefaultOSXApplication} receives an Open Application event
 * from the Mac OS X Finder or another Mac OS X application.
 *
 * <p>This action is automatically created by {@code DefaultOSXApplication} and put into the {@code ApplicationModel} before {@link org.jhotdraw.app.ApplicationModel#initApplication} is called.
 */
public class OpenApplicationAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.openApplication";

    public OpenApplicationAction(org.jhotdraw.api.app.Application app) {
        super(app);
        putValue(javax.swing.Action.NAME, "OSX Open Application");
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}