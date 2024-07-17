/* @(#)PaletteMenuItemUI.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteMenuItemUI.
 */
public class PaletteMenuItemUI extends javax.swing.plaf.basic.BasicMenuItemUI {
    public PaletteMenuItemUI() {
    }

    @java.lang.Override
    protected void installDefaults() {
        super.installDefaults();
        defaultTextIconGap = 0;// Should be from table

        // menuItem.setBorderPainted(false);
        // menuItem.setBorder(null);
        arrowIcon = null;
        checkIcon = null;
    }

    @java.lang.Override
    protected java.awt.Dimension getPreferredMenuItemSize(javax.swing.JComponent c, javax.swing.Icon checkIcon, javax.swing.Icon arrowIcon, int defaultTextIconGap) {
        javax.swing.JMenuItem b = ((javax.swing.JMenuItem) (c));
        javax.swing.Icon icon = b.getIcon();
        if (icon == null) {
            return new java.awt.Dimension(22, 22);
        }
        return new java.awt.Dimension(icon.getIconWidth() + 2, icon.getIconHeight() + 2);
    }

    @java.lang.Override
    public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
        javax.swing.JMenuItem b = ((javax.swing.JMenuItem) (c));
        // Paint background
        paintBackground(g, b, selectionBackground);
        // Paint the icon
        // ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        javax.swing.Icon icon = b.getIcon();
        if (icon != null) {
            icon.paintIcon(b, g, 1, 1);
        }
    }
}