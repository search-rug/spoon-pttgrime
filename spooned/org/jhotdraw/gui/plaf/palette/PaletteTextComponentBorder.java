/**
 *
 * @(#)PaletteTextComponentBorder.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteTextComponentBorder.
 */
public class PaletteTextComponentBorder implements javax.swing.border.Border , javax.swing.plaf.UIResource {
    private static final float[] ENABLED_STOPS = new float[]{ 0.0F, 0.2F };

    private static final java.awt.Color[] ENABLED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0xc8c8c8), new java.awt.Color(0xffffff) };

    @java.lang.Override
    public void paintBorder(java.awt.Component c, java.awt.Graphics gr, int x, int y, int width, int height) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        int borderColor;
        float[] stops;
        java.awt.Color[] stopColors;
        if (!c.isEnabled()) {
            borderColor = 0x80a5a5a5;
            stops = org.jhotdraw.gui.plaf.palette.PaletteTextComponentBorder.ENABLED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteTextComponentBorder.ENABLED_STOP_COLORS;
        } else {
            borderColor = 0xffa5a5a5;
            stops = org.jhotdraw.gui.plaf.palette.PaletteTextComponentBorder.ENABLED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteTextComponentBorder.ENABLED_STOP_COLORS;
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