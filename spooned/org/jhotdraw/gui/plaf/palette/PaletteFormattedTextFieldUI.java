/**
 *
 * @(#)PaletteTextFieldUI.java <p>Copyright (c) 2009-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
import javax.swing.plaf.basic.*;
/**
 * PaletteFormattedTextFieldUI.
 */
public class PaletteFormattedTextFieldUI extends javax.swing.plaf.basic.BasicFormattedTextFieldUI {
    private java.awt.Color errorIndicatorForeground;

    /**
     * Creates a UI for a JTextField.
     *
     * @param c
     * 		the text field
     * @return the UI
     */
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return new org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI();
    }

    /**
     * Creates a view (FieldView) based on an element.
     *
     * @param elem
     * 		the element
     * @return the view
     */
    @java.lang.Override
    public javax.swing.text.View create(javax.swing.text.Element elem) {
        /* We create our own view here. This view always uses the
        text alignment that was specified by the text component. Even
        then, when the text is longer than in the text component.

        Draws a wavy line if the value of the field is not valid.
         */
        return new javax.swing.text.FieldView(elem) {
            @java.lang.Override
            public void paint(java.awt.Graphics gr, java.awt.Shape a) {
                java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
                javax.swing.JFormattedTextField editor = ((javax.swing.JFormattedTextField) (getComponent()));
                if (!editor.isEditValid()) {
                    java.awt.Rectangle r = ((java.awt.Rectangle) (a));
                    g.setColor(errorIndicatorForeground);
                    g.setStroke(new java.awt.BasicStroke(2.5F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0, new float[]{ 3.0F, 3.0F }, 0.5F));
                    g.draw(new java.awt.geom.Line2D.Float(r.x, (r.y + r.height) - 0.5F, (r.x + r.width) - 1, (r.y + r.height) - 0.5F));
                }
                super.paint(g, a);
            }
        };
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
        errorIndicatorForeground = plaf.getColor(prefix + ".errorIndicatorForeground");
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