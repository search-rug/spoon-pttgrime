/**
 *
 * @(#)BackdropBorder.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * BackdropBorder.
 */
public class BackdropBorder implements javax.swing.border.Border {
    private javax.swing.border.Border backgroundBorder;

    private javax.swing.border.Border foregroundBorder;

    public BackdropBorder(javax.swing.border.Border backgroundBorder) {
        this(null, backgroundBorder);
    }

    public BackdropBorder(javax.swing.border.Border foregroundBorder, javax.swing.border.Border backgroundBorder) {
        this.foregroundBorder = foregroundBorder;
        this.backgroundBorder = backgroundBorder;
    }

    public javax.swing.border.Border getBackdropBorder() {
        return backgroundBorder;
    }

    @java.lang.Override
    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
        if (foregroundBorder != null) {
            foregroundBorder.paintBorder(c, g, x, y, width, height);
        }
    }

    @java.lang.Override
    public java.awt.Insets getBorderInsets(java.awt.Component c) {
        if (foregroundBorder != null) {
            return foregroundBorder.getBorderInsets(c);
        } else {
            return backgroundBorder.getBorderInsets(c);
        }
    }

    @java.lang.Override
    public boolean isBorderOpaque() {
        return backgroundBorder.isBorderOpaque();
    }

    public static class UIResource extends org.jhotdraw.gui.plaf.palette.BackdropBorder implements javax.swing.plaf.UIResource {
        public UIResource(javax.swing.border.Border backgroundBorder) {
            this(null, backgroundBorder);
        }

        public UIResource(javax.swing.border.Border foregroundBorder, javax.swing.border.Border backgroundBorder) {
            super(foregroundBorder, backgroundBorder);
        }
    }
}