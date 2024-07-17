/* @(#)AboutAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * Displays a dialog showing information about the application.
 *
 * <p>This action is called when the user selects the "About" menu item. The menu item is
 * automatically created by the application. {@link OSXApplication} places the menu item in the
 * "Application" menu, SDIApplication and {@link MDIApplication} in the "Help" menu.
 *
 * <p>This action is automatically created by the application and put into the {@code ApplicationModel} before {@link ApplicationModel#initApplication} is called.
 */
public class AboutAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.about";

    public AboutAction(org.jhotdraw.api.app.Application app) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.app.AboutAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.api.app.Application app = getApplication();
        java.awt.Component c = app.getComponent();
        // This ensures that we open the option pane on the center of the screen
        // on Mac OS X.
        if ((c == null) || c.getBounds().isEmpty()) {
            c = null;
        }
        javax.swing.JOptionPane.showMessageDialog(c, (((((((((((((((((((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<p><b>") + app.getName()) + (app.getVersion() == null ? "" : " " + app.getVersion())) + "</b><br>") + app.getCopyright().replace("\n", "<br>")) + "<br><br>Running on") + "<br>  Java: ") + java.lang.System.getProperty("java.version")) + ", ") + java.lang.System.getProperty("java.vendor")) + "<br>  JVM: ") + java.lang.System.getProperty("java.vm.version")) + ", ") + java.lang.System.getProperty("java.vm.vendor")) + "<br>  OS: ") + java.lang.System.getProperty("os.name")) + " ") + java.lang.System.getProperty("os.version")) + ", ") + java.lang.System.getProperty("os.arch"), "About", javax.swing.JOptionPane.PLAIN_MESSAGE);
    }
}