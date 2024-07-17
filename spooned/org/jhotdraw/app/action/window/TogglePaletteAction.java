/* @(#)TogglePaletteAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.window;
/**
 * TogglePaletteAction.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class TogglePaletteAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    private java.awt.Window palette;

    private org.jhotdraw.app.OSXApplication app;

    private java.awt.event.WindowListener windowHandler;

    public TogglePaletteAction(org.jhotdraw.app.OSXApplication app, java.awt.Window palette, java.lang.String label) {
        super(label);
        this.app = app;
        windowHandler = new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, false);
            }
        };
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, false);
        setPalette(palette);
    }

    @java.lang.Override
    public void putValue(java.lang.String key, java.lang.Object newValue) {
        super.putValue(key, newValue);
        /* if (key == ActionUtil.SELECTED_KEY) {
        if (palette != null) {
        boolean b = (Boolean) newValue;
        if (b) {
        app.addPalette(palette);
        palette.setVisible(true);
        } else {
        app.removePalette(palette);
        palette.setVisible(false);
        }
        }
        }
         */
    }

    public void setPalette(java.awt.Window newValue) {
        if (palette != null) {
            palette.removeWindowListener(windowHandler);
        }
        palette = newValue;
        if (palette != null) {
            palette.addWindowListener(windowHandler);
            if (getValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY) == java.lang.Boolean.TRUE) {
                app.addPalette(palette);
                palette.setVisible(true);
            } else {
                app.removePalette(palette);
                palette.setVisible(false);
            }
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (palette != null) {
            // putValue(ActionUtil.SELECTED_KEY, ! palette.isVisible());
            boolean b = ((java.lang.Boolean) (getValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY)));
            if (b) {
                app.addPalette(palette);
                palette.setVisible(true);
            } else {
                app.removePalette(palette);
                palette.setVisible(false);
            }
        }
    }
}