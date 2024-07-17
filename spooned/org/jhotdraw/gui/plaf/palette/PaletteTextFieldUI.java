/**
 *
 * @(#)PaletteTextFieldUI.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteTextFieldUI.
 */
public class PaletteTextFieldUI extends javax.swing.plaf.basic.BasicTextFieldUI {
    /**
     * Creates a UI for a JTextField.
     *
     * @param c
     * 		the text field
     * @return the UI
     */
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return new org.jhotdraw.gui.plaf.palette.PaletteTextFieldUI();
    }

    /**
     * Initializes component properties, e.g. font, foreground, background, caret color, selection
     * color, selected text color, disabled text color, and border color. The font, foreground, and
     * background properties are only set if their current value is either null or a UIResource, other
     * properties are set if the current value is null.
     *
     * @see #uninstallDefaults
     * @see #installUI
     */
    @java.lang.Override
    protected void installDefaults() {
        javax.swing.text.JTextComponent editor = getComponent();
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel plaf = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance();
        java.lang.String prefix = getPropertyPrefix();
        java.awt.Font f = editor.getFont();
        if ((f == null) || (f instanceof javax.swing.plaf.UIResource)) {
            editor.setFont(plaf.getFont(prefix + ".font"));
        }
        java.awt.Color bg = editor.getBackground();
        if ((bg == null) || (bg instanceof javax.swing.plaf.UIResource)) {
            editor.setBackground(plaf.getColor(prefix + ".background"));
        }
        java.awt.Color fg = editor.getForeground();
        if ((fg == null) || (fg instanceof javax.swing.plaf.UIResource)) {
            editor.setForeground(plaf.getColor(prefix + ".foreground"));
        }
        java.awt.Color color = editor.getCaretColor();
        if ((color == null) || (color instanceof javax.swing.plaf.UIResource)) {
            editor.setCaretColor(plaf.getColor(prefix + ".caretForeground"));
        }
        java.awt.Color s = editor.getSelectionColor();
        if ((s == null) || (s instanceof javax.swing.plaf.UIResource)) {
            editor.setSelectionColor(plaf.getColor(prefix + ".selectionBackground"));
        }
        java.awt.Color sfg = editor.getSelectedTextColor();
        if ((sfg == null) || (sfg instanceof javax.swing.plaf.UIResource)) {
            editor.setSelectedTextColor(plaf.getColor(prefix + ".selectionForeground"));
        }
        java.awt.Color dfg = editor.getDisabledTextColor();
        if ((dfg == null) || (dfg instanceof javax.swing.plaf.UIResource)) {
            editor.setDisabledTextColor(plaf.getColor(prefix + ".inactiveForeground"));
        }
        javax.swing.border.Border b = editor.getBorder();
        if ((b == null) || (b instanceof javax.swing.plaf.UIResource)) {
            editor.setBorder(plaf.getBorder(prefix + ".border"));
        }
        java.awt.Insets margin = editor.getMargin();
        if ((margin == null) || (margin instanceof javax.swing.plaf.UIResource)) {
            editor.setMargin(plaf.getInsets(prefix + ".margin"));
        }
        editor.setOpaque(plaf.getBoolean(prefix + ".opaque"));
    }

    @java.lang.Override
    protected void paintSafely(java.awt.Graphics gr) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintSafely(g);
    }

    @java.lang.Override
    public void paintBackground(java.awt.Graphics g) {
        javax.swing.text.JTextComponent c = getComponent();
        if (c.getBorder() instanceof org.jhotdraw.gui.plaf.palette.BackdropBorder) {
            org.jhotdraw.gui.plaf.palette.BackdropBorder bb = ((org.jhotdraw.gui.plaf.palette.BackdropBorder) (c.getBorder()));
            bb.getBackdropBorder().paintBorder(c, g, 0, 0, c.getWidth(), c.getHeight());
        } else {
            super.paintBackground(g);
        }
    }
}