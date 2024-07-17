/* @(#)QuaquaPanelUI.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PalettePanelUI.
 */
public class PalettePanelUI extends javax.swing.plaf.basic.BasicPanelUI {
    // Shared UI object
    private static javax.swing.plaf.PanelUI panelUI;

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        if (org.jhotdraw.gui.plaf.palette.PalettePanelUI.panelUI == null) {
            org.jhotdraw.gui.plaf.palette.PalettePanelUI.panelUI = new org.jhotdraw.gui.plaf.palette.PalettePanelUI();
        }
        return org.jhotdraw.gui.plaf.palette.PalettePanelUI.panelUI;
    }

    @java.lang.Override
    protected void installDefaults(javax.swing.JPanel p) {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(p, "Panel.background", "Panel.foreground", "Panel.font");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(p, "Panel.border");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installProperty(p, "opaque", org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().get("Panel.opaque"));
    }

    @java.lang.Override
    protected void uninstallDefaults(javax.swing.JPanel p) {
        super.uninstallDefaults(p);
    }

    public static boolean isInTabbedPane(java.awt.Component comp) {
        if (comp == null) {
            return false;
        }
        java.awt.Container parent = comp.getParent();
        while (parent != null) {
            if (parent instanceof javax.swing.JTabbedPane) {
                return true;
            } else if (parent instanceof javax.swing.JRootPane) {
                return false;
            } else if (parent instanceof javax.swing.RootPaneContainer) {
                return false;
            } else if (parent instanceof java.awt.Window) {
                return false;
            }
            parent = parent.getParent();
        } 
        return false;
    }

    @java.lang.Override
    public void paint(java.awt.Graphics gr, javax.swing.JComponent c) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        /* Border backgroundBorder = null;
        Insets insets = new Insets(0,0,0,0);
        if (backgroundBorder != null) {
        backgroundBorder.paintBorder(c, gr, insets.left, insets.top, c.getWidth() - insets.left - insets.right, c.getHeight() - insets.top - insets.bottom);
        }
         */
    }
}