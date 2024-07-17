/**
 *
 * @(#)PaletteButtonBorder.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteButtonBorder.
 */
public class PaletteButtonBorder implements javax.swing.border.Border , javax.swing.plaf.UIResource {
    private static final float[] ENABLED_STOPS = new float[]{ 0.0F, 0.35F, 0.4F, 1.0F };

    private static final java.awt.Color[] ENABLED_STOP_COLOR = new java.awt.Color[]{ new java.awt.Color(0xf8f8f8), new java.awt.Color(0xeeeeee), new java.awt.Color(0xcacaca), new java.awt.Color(0xffffff) };

    private static final float[] SELECTED_STOPS = new float[]{ 0.0F, 0.1F, 0.9F, 1.0F };

    private static final java.awt.Color[] SELECTED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0x666666), new java.awt.Color(0xcccccc), new java.awt.Color(0x999999), new java.awt.Color(0xb1b1b1) };

    @java.lang.Override
    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
        if (c instanceof javax.swing.AbstractButton) {
            paintBorder(((javax.swing.AbstractButton) (c)), g, x, y, width, height);
        }
    }

    public void paintBorder(javax.swing.AbstractButton c, java.awt.Graphics gr, int x, int y, int width, int height) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        javax.swing.ButtonModel m = c.getModel();
        int borderColor;
        float[] stops;
        java.awt.Color[] stopColors;
        if (!m.isEnabled()) {
            borderColor = 0x80a5a5a5;
            stops = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.ENABLED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.ENABLED_STOP_COLOR;
        } else if (m.isSelected() || (m.isPressed() && m.isArmed())) {
            borderColor = 0xff333333;
            stops = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.SELECTED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.SELECTED_STOP_COLORS;
        } else {
            borderColor = 0xffa5a5a5;
            stops = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.ENABLED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteButtonBorder.ENABLED_STOP_COLOR;
        }
        java.lang.String segmentPosition = getSegmentPosition(c);
        if ("first".equals(segmentPosition) || "middle".equals(segmentPosition)) {
            width += 1;
        }
        g.setColor(new java.awt.Color(borderColor, true));
        g.drawRect(x, y, width - 1, height - 1);
        java.awt.LinearGradientPaint lgp = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(x, y), new java.awt.geom.Point2D.Float(x, (y + height) - 1), stops, stopColors, java.awt.MultipleGradientPaint.CycleMethod.REPEAT);
        g.setPaint(lgp);
        g.fillRect(x + 1, y + 1, width - 2, height - 2);
    }

    private java.lang.String getSegmentPosition(java.awt.Component c) {
        java.lang.String segmentPosition = null;
        if (c instanceof javax.swing.JComponent) {
            segmentPosition = ((java.lang.String) (((javax.swing.JComponent) (c)).getClientProperty("Palette.Component.segmentPosition")));
        }
        return segmentPosition == null ? "only" : segmentPosition;
    }

    @java.lang.Override
    public java.awt.Insets getBorderInsets(java.awt.Component c) {
        java.awt.Insets insets;
        java.lang.String segmentPosition = getSegmentPosition(c);
        if ("first".equals(segmentPosition) || "middle".equals(segmentPosition)) {
            insets = new java.awt.Insets(3, 3, 3, 2);
        } else {
            insets = new java.awt.Insets(3, 3, 3, 3);
        }
        return insets;
    }

    @java.lang.Override
    public boolean isBorderOpaque() {
        return true;
    }
}