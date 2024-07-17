/* @(#)QuaquaLabelUI.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
import javax.swing.plaf.basic.*;
/**
 * QuaquaLabelUI.
 */
public class PaletteLabelUI extends javax.swing.plaf.basic.BasicLabelUI {
    // CHECKSTYLE:OFF
    protected static final org.jhotdraw.gui.plaf.palette.PaletteLabelUI labelUI = new org.jhotdraw.gui.plaf.palette.PaletteLabelUI();

    // CHECKSTYLE:ON
    /* These rectangles/insets are allocated once for this shared LabelUI
    implementation.  Re-using rectangles rather than allocating
    them in each getPreferredSize call sped up the method substantially.
     */
    private static java.awt.Rectangle iconR = new java.awt.Rectangle();

    private static java.awt.Rectangle textR = new java.awt.Rectangle();

    private static java.awt.Rectangle viewR = new java.awt.Rectangle();

    private static java.awt.Insets viewInsets = new java.awt.Insets(0, 0, 0, 0);

    /**
     * Preferred spacing between labels and other components. Pixels from colon and associated
     * controls (RadioButton, CheckBox) / private static final Insets associatedRegularSpacing = new
     * Insets(8,8,8,8); private static final Insets associatedSmallSpacing = new Insets(6,6,6,6);
     * private static final Insets associatedMiniSpacing = new Insets(5,5,5,5);
     */
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return org.jhotdraw.gui.plaf.palette.PaletteLabelUI.labelUI;
    }

    @java.lang.Override
    protected void installDefaults(javax.swing.JLabel b) {
        super.installDefaults(b);
        // load shared instance defaults
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(b, "Label.border");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(b, "Label.background", "Label.foreground", "Label.font");
        // FIXME - Very, very dirty trick to achieve small labels on sliders
        // This hack should be removed, when we implement a SliderUI
        // on our own.
        if (b.getClass().getName().endsWith("LabelUIResource")) {
            b.setFont(javax.swing.UIManager.getFont("Slider.labelFont"));
        }
    }

    @java.lang.Override
    public void paint(java.awt.Graphics gr, javax.swing.JComponent c) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        java.lang.Object oldHints = org.jhotdraw.gui.plaf.palette.PaletteUtilities.beginGraphics(g);
        // Paint background again so that the texture paint is drawn
        /* if (c.isOpaque()) {
        g.setPaint(PaintableColor.getPaint(c.getBackground(), c));
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
         */
        // Paint background border
        /* Border b = c.getBorder();
        if (b != null && b instanceof BackgroundBorder) {
        ((BackgroundBorder) b).getBackgroundBorder().paintBorder(c, g, 0, 0, c.getWidth(), c.getHeight());
        }
         */
        super.paint(g, c);
        org.jhotdraw.gui.plaf.palette.PaletteUtilities.endGraphics(g, oldHints);
    }

    /**
     * Paint label with disabled text color.
     *
     * @see #paint
     * @see #paintEnabledText
     */
    @java.lang.Override
    protected void paintDisabledText(javax.swing.JLabel l, java.awt.Graphics g, java.lang.String s, int textX, int textY) {
        java.awt.Color c = javax.swing.UIManager.getColor("Label.disabledForeground");
        g.setColor(c != null ? c : l.getForeground());
        int accChar = -1;// l.getDisplayedMnemonicIndex();

        org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawString(g, s, accChar, textX, textY);
    }

    @java.lang.Override
    protected void paintEnabledText(javax.swing.JLabel l, java.awt.Graphics g, java.lang.String s, int textX, int textY) {
        int mnemIndex = l.getDisplayedMnemonicIndex();
        java.lang.String style = ((java.lang.String) (l.getClientProperty("Quaqua.Label.style")));
        if (style != null) {
            if ("emboss".equals(style) && (javax.swing.UIManager.getColor("Label.embossForeground") != null)) {
                g.setColor(javax.swing.UIManager.getColor("Label.embossForeground"));
                org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawString(g, s, mnemIndex, textX, textY + 1);
            } else if ("shadow".equals(style) && (javax.swing.UIManager.getColor("Label.shadowForeground") != null)) {
                g.setColor(javax.swing.UIManager.getColor("Label.shadowForeground"));
                org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawString(g, s, mnemIndex, textX, textY + 1);
            }
        }
        g.setColor(l.getForeground());
        org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawString(g, s, mnemIndex, textX, textY);
        // SwingUtilities2.drawStringUnderlineCharAt(l, g, s, mnemIndex,
        // textX, textY);
    }

    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel(). This method is here so that a
     * subclass could do Label specific layout and to shorten the method name a little.
     *
     * @see SwingUtilities#layoutCompoundLabel
     */
    @java.lang.Override
    protected java.lang.String layoutCL(javax.swing.JLabel label, java.awt.FontMetrics fontMetrics, java.lang.String text, javax.swing.Icon icon, java.awt.Rectangle viewR, java.awt.Rectangle iconR, java.awt.Rectangle textR) {
        return javax.swing.SwingUtilities.layoutCompoundLabel(((javax.swing.JComponent) (label)), fontMetrics, text, icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewR, iconR, textR, label.getIconTextGap());
    }

    @java.lang.Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        java.lang.String name = evt.getPropertyName();
        if ("JComponent.sizeVariant".equals(name)) {
            // QuaquaUtilities.applySizeVariant((JLabel) evt.getSource());
        } else {
            super.propertyChange(evt);
        }
    }
}