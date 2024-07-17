/**
 *
 * @(#)PaletteButtonUI.java <p>Copyright (c) 1996-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
import javax.swing.plaf.basic.*;
/**
 * ButtonUI for palette components.
 */
public class PaletteButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
    // Shared UI object
    private static final org.jhotdraw.gui.plaf.palette.PaletteButtonUI BUTTON_UI = new org.jhotdraw.gui.plaf.palette.PaletteButtonUI();

    // ********************************
    // Create PLAF
    // ********************************
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return org.jhotdraw.gui.plaf.palette.PaletteButtonUI.BUTTON_UI;
    }

    @java.lang.Override
    protected void installDefaults(javax.swing.AbstractButton b) {
        super.installDefaults(b);
        // load shared instance defaults
        java.lang.String pp = getPropertyPrefix();
        javax.swing.LookAndFeel.installProperty(b, "opaque", java.lang.Boolean.FALSE);
        if ((b.getMargin() == null) || (b.getMargin() instanceof javax.swing.plaf.UIResource)) {
            b.setMargin(new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));
        }
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(b, pp + "background", pp + "foreground", pp + "font");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(b, pp + "border");
        java.lang.Object rollover = javax.swing.UIManager.get(pp + "rollover");
        if (rollover != null) {
            javax.swing.LookAndFeel.installProperty(b, "rolloverEnabled", rollover);
        }
        b.setFocusable(false);
    }

    @java.lang.Override
    public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
        javax.swing.AbstractButton button = ((javax.swing.AbstractButton) (c));
        if (button.isBorderPainted() && (c.getBorder() instanceof org.jhotdraw.gui.plaf.palette.BackdropBorder)) {
            org.jhotdraw.gui.plaf.palette.BackdropBorder bb = ((org.jhotdraw.gui.plaf.palette.BackdropBorder) (c.getBorder()));
            bb.getBackdropBorder().paintBorder(c, g, 0, 0, c.getWidth(), c.getHeight());
        }
        super.paint(g, c);
    }
}