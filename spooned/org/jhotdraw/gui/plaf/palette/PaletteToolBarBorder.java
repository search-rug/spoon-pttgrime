/* @(#)PaletteToolBarBorder.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteToolBarBorder.
 */
public class PaletteToolBarBorder extends javax.swing.border.AbstractBorder implements javax.swing.SwingConstants {
    private static final long serialVersionUID = 1L;

    private static final float[] ENABLED_STOPS = new float[]{ 0.0F, 0.5F, 1.0F };

    private static final java.awt.Color[] ENABLED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0xf8f8f8), new java.awt.Color(0xc8c8c8), new java.awt.Color(0xf8f8f8) };

    private static final float[] SELECTED_STOPS = new float[]{ 0.0F, 0.1F, 0.9F, 1.0F };

    private static final java.awt.Color[] SELECTED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0x666666), new java.awt.Color(0xcccccc), new java.awt.Color(0x999999), new java.awt.Color(0xb1b1b1) };

    private static final java.awt.Color BRIGHT = new java.awt.Color(0x999999, true);

    private static final java.awt.Color DARK = new java.awt.Color(0x8c8c8c);

    private static final java.awt.Color DIVIDER = new java.awt.Color(0x9f9f9f);

    /* private static final Color dark = new Color(0x999999);
    private static final Color bright = new Color(0xb3b3b3);
    /*
     */
    /* private static final Color dark = new Color(0x808080);
    private static final Color bright = new Color(0xcccccc);
     */
    @java.lang.Override
    public void paintBorder(java.awt.Component component, java.awt.Graphics gr, int x, int y, int w, int h) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if ((component instanceof javax.swing.JToolBar) && (((javax.swing.JToolBar) (component)).getUI() instanceof org.jhotdraw.gui.plaf.palette.PaletteToolBarUI)) {
            javax.swing.JToolBar c = ((javax.swing.JToolBar) (component));
            if (c.isFloatable()) {
                int hx = x;
                int hy = y;
                int hw = w;
                int hh = h;
                if (c.getOrientation() == javax.swing.SwingConstants.HORIZONTAL) {
                    if (c.getComponentOrientation().isLeftToRight()) {
                        int barW = 18;
                        int barH = h;
                        int barX = 0;
                        int barY = 0;
                        int borderColor = 0xffa5a5a5;
                        float[] stops = org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.ENABLED_STOPS;
                        java.awt.Color[] stopColors = org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.ENABLED_STOP_COLORS;
                        g.setColor(new java.awt.Color(borderColor, true));
                        g.drawRect(barX, barY, barW - 1, barH - 1);
                        java.awt.LinearGradientPaint lgp = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(1, 1), new java.awt.geom.Point2D.Float(19, 1), stops, stopColors, java.awt.MultipleGradientPaint.CycleMethod.REPEAT);
                        g.setPaint(lgp);
                        g.fillRect(barX + 1, barX + 1, barW - 2, barH - 2);
                        // paint the icon
                        javax.swing.Icon icon = ((javax.swing.Icon) (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.TOOLBAR_ICON_PROPERTY)));
                        if (icon != null) {
                            icon.paintIcon(component, gr, barX + ((barW - icon.getIconWidth()) / 2), ((barY + barH) - 4) - icon.getIconHeight());
                        }
                        int textIconGap = (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.TOOLBAR_ICON_PROPERTY) instanceof java.lang.Integer) ? ((java.lang.Integer) (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.TOOLBAR_ICON_PROPERTY))) : 2;
                        java.lang.String theTitle = c.getName();
                        if (theTitle != null) {
                            java.awt.FontMetrics fm = g.getFontMetrics();
                            int titleW;
                            titleW = barH - 8;
                            if (icon != null) {
                                titleW -= icon.getIconHeight() + textIconGap;
                            }
                            theTitle = clippedText(theTitle, fm, titleW);
                            java.awt.geom.AffineTransform savedTransform = g.getTransform();
                            java.awt.geom.AffineTransform t = g.getTransform();
                            t.rotate(java.lang.Math.PI / (-2.0), (barX + 2) + fm.getAscent(), titleW + 4);
                            g.setTransform(t);
                            g.setColor(java.awt.Color.black);
                            g.drawString(theTitle, (barX + 2) + fm.getAscent(), titleW + 4);
                            g.setTransform(savedTransform);
                        }
                    } else {
                        g.setColor(org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.BRIGHT);
                        g.fillRect((hw - hx) - 3, hy + 2, 1, hh - 4);
                        g.fillRect((hw - hx) - 5, hy + 2, 1, hh - 4);
                        g.setColor(org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.DARK);
                        g.fillRect((hw - hx) - 2, hy + 2, 1, hh - 4);
                        g.fillRect((hw - hx) - 6, hy + 2, 1, hh - 4);
                    }
                } else {
                    // vertical
                    g.setColor(org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.BRIGHT);
                    g.fillRect(hx + 2, hy + 2, hw - 4, 1);
                    g.fillRect(hx + 2, hy + 5, hw - 4, 1);
                    g.setColor(org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder.DARK);
                    g.fillRect(hx + 2, hy + 3, hw - 4, 1);
                    g.fillRect(hx + 2, hy + 6, hw - 4, 1);
                }
            }
        }
    }

    @java.lang.Override
    public java.awt.Insets getBorderInsets(java.awt.Component c) {
        return getBorderInsets(c, new java.awt.Insets(0, 0, 0, 0));
    }

    /**
     * These insets are used by PaletteToolBarUI, to determine if the toolbar should be dragged.
     *
     * @param c
     * 		JToolBar.
     * @return Return drag insets.
     */
    public java.awt.Insets getDragInsets(java.awt.Component c) {
        return new java.awt.Insets(0, 18, 0, 0);
    }

    @java.lang.Override
    public java.awt.Insets getBorderInsets(java.awt.Component component, java.awt.Insets newInsets) {
        if (newInsets == null) {
            newInsets = new java.awt.Insets(0, 0, 0, 0);
        }
        javax.swing.JComponent c = ((javax.swing.JComponent) (component));
        if (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.TOOLBAR_INSETS_OVERRIDE_PROPERTY) instanceof java.awt.Insets) {
            java.awt.Insets override = ((java.awt.Insets) (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.TOOLBAR_INSETS_OVERRIDE_PROPERTY)));
            newInsets.top = override.top;
            newInsets.left = override.left;
            newInsets.bottom = override.bottom;
            newInsets.right = override.right;
            return newInsets;
        }
        newInsets.top = 0;
        newInsets.left = 18;
        newInsets.bottom = 0;
        newInsets.right = 0;
        return newInsets;
    }

    /**
     * Convenience method to clip the passed in text to the specified size.
     */
    private java.lang.String clippedText(java.lang.String text, java.awt.FontMetrics fm, int availTextWidth) {
        if ((text == null) || "".equals(text)) {
            return "";
        }
        int textWidth = javax.swing.SwingUtilities.computeStringWidth(fm, text);
        java.lang.String clipString = "…";
        if (textWidth > availTextWidth) {
            int totalWidth = javax.swing.SwingUtilities.computeStringWidth(fm, clipString);
            int nChars;
            for (nChars = 0; nChars < text.length(); nChars++) {
                totalWidth += fm.charWidth(text.charAt(nChars));
                if (totalWidth > availTextWidth) {
                    break;
                }
            }
            text = text.substring(0, nChars) + clipString;
        }
        return text;
    }

    public static class UIResource extends org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder implements javax.swing.plaf.UIResource {
        private static final long serialVersionUID = 1L;
    }
}