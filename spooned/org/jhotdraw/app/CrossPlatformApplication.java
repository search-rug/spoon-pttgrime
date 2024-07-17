/* @(#)CrossPlatformApplication.java

Copyright (c) 2013 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.app;
/**
 * {@code CrossPlatformApplication}.
 */
public class CrossPlatformApplication extends org.jhotdraw.app.SDIApplication {
    private static final long serialVersionUID = 1L;

    @java.lang.Override
    public void init() {
        super.init();
        org.jhotdraw.util.ResourceBundleUtil.putPropertyNameModifier("os", "other", "default");
    }

    @java.lang.Override
    protected void initLookAndFeel() {
        try {
            java.lang.String lafName = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName();
            javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
            javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
            javax.swing.UIManager.setLookAndFeel(lafName);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        if (javax.swing.UIManager.getString("OptionPane.css") == null) {
            javax.swing.UIManager.put("OptionPane.css", (((("<head>" + "<style type=\"text/css\">") + "b { font: 13pt \"Dialog\" }") + "p { font: 11pt \"Dialog\"; margin-top: 8px }") + "</style>") + "</head>");
        }
    }
}