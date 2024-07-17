/**
 *
 * @(#)PaletteSliderUI.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteSliderUI.
 */
public class PaletteSliderUI extends javax.swing.plaf.basic.BasicSliderUI {
    private static final float[] ENABLED_STOPS = new float[]{ 0.0F, 0.35F, 0.351F, 1.0F };

    private static final java.awt.Color[] ENABLED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0xf3f3f3), new java.awt.Color(0xcccccc), new java.awt.Color(0xbababa), new java.awt.Color(0xf3f3f3) };

    private static final float[] DISABLED_STOPS = new float[]{ 0.0F, 0.35F, 0.351F, 1.0F };

    private static final java.awt.Color[] DISABLED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0xf3f3f3), new java.awt.Color(0xeeeeee), new java.awt.Color(0xcacaca), new java.awt.Color(0xf3f3f3) };

    private static final float[] SELECTED_STOPS = new float[]{ 0.0F, 0.2F, 1.0F };

    private static final java.awt.Color[] SELECTED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0x999999), new java.awt.Color(0xaaaaaa), new java.awt.Color(0x666666) };

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent b) {
        return new org.jhotdraw.gui.plaf.palette.PaletteSliderUI(((javax.swing.JSlider) (b)));
    }

    public PaletteSliderUI(javax.swing.JSlider slider) {
        super(slider);
    }

    @java.lang.Override
    protected void installDefaults(javax.swing.JSlider slider) {
        super.installDefaults(slider);
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(slider, "Slider.border");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColors(slider, "Slider.background", "Slider.foreground");
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredHorizontalSize() {
        java.awt.Dimension horizDim = ((java.awt.Dimension) (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().get("Slider.horizontalSize")));
        if (horizDim == null) {
            horizDim = new java.awt.Dimension(100, 21);
        }
        return horizDim;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredVerticalSize() {
        java.awt.Dimension vertDim = ((java.awt.Dimension) (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().get("Slider.verticalSize")));
        if (vertDim == null) {
            vertDim = new java.awt.Dimension(21, 100);
        }
        return vertDim;
    }

    @java.lang.Override
    public void paint(java.awt.Graphics gr, javax.swing.JComponent c) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @java.lang.Override
    public void paintFocus(java.awt.Graphics g) {
        // empty
    }

    @java.lang.Override
    public void paintTrack(java.awt.Graphics g) {
        int cx;
        int cy;
        int cw;
        int ch;
        int pad;
        java.awt.Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            pad = trackBuffer;
            // cx = pad;
            cy = (trackBounds.height / 2) - 2;
            cw = trackBounds.width;
            g.translate(trackBounds.x, trackBounds.y + cy);
            g.setColor(getShadowColor());
            g.drawLine(0, 0, cw - 1, 0);
            g.drawLine(0, 1, 0, 2);
            g.setColor(getHighlightColor());
            g.drawLine(0, 3, cw, 3);
            g.drawLine(cw, 0, cw, 3);
            g.setColor(java.awt.Color.black);
            g.drawLine(1, 1, cw - 2, 1);
            g.translate(-trackBounds.x, -(trackBounds.y + cy));
        } else {
            pad = trackBuffer;
            cx = (trackBounds.width / 2) - 2;
            // cy = pad;
            ch = trackBounds.height;
            g.setColor(new java.awt.Color(slider.isEnabled() ? 0x888888 : 0xaaaaaa));
            g.drawRoundRect(trackBounds.x + cx, trackBounds.y, 5, ch, 5, 5);
        }
    }

    @java.lang.Override
    public void paintThumb(java.awt.Graphics gr) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        java.awt.Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;
        g.translate(knobBounds.x, knobBounds.y);
        float[] stops;
        java.awt.Color[] stopColors;
        if (slider.isEnabled()) {
            g.setColor(slider.getBackground());
            if (slider.getModel().getValueIsAdjusting()) {
                stops = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.SELECTED_STOPS;
                stopColors = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.SELECTED_STOP_COLORS;
            } else {
                stops = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.ENABLED_STOPS;
                stopColors = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.ENABLED_STOP_COLORS;
            }
        } else {
            g.setColor(slider.getBackground().darker());
            stops = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.ENABLED_STOPS;
            stopColors = org.jhotdraw.gui.plaf.palette.PaletteSliderUI.ENABLED_STOP_COLORS;
        }
        java.lang.Boolean paintThumbArrowShape = ((java.lang.Boolean) (slider.getClientProperty("Slider.paintThumbArrowShape")));
        if (((!slider.getPaintTicks()) && (paintThumbArrowShape == null)) || (paintThumbArrowShape == java.lang.Boolean.FALSE)) {
            // "plain" version
            java.awt.LinearGradientPaint lgp = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(2, 2), new java.awt.geom.Point2D.Float(2, (2 + h) - 4), stops, stopColors, java.awt.MultipleGradientPaint.CycleMethod.REPEAT);
            g.setPaint(lgp);
            g.fillOval(2, 2, w - 4, h - 4);
            g.setColor(new java.awt.Color(0x444444));
            g.drawOval(1, 1, w - 3, h - 3);
        } else if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            int cw = w / 2;
            g.fillRect(1, 1, w - 3, (h - 1) - cw);
            java.awt.Polygon p = new java.awt.Polygon();
            p.addPoint(1, h - cw);
            p.addPoint(cw - 1, h - 1);
            p.addPoint(w - 2, (h - 1) - cw);
            g.fillPolygon(p);
            g.setColor(getHighlightColor());
            g.drawLine(0, 0, w - 2, 0);
            g.drawLine(0, 1, 0, (h - 1) - cw);
            g.drawLine(0, h - cw, cw - 1, h - 1);
            g.setColor(java.awt.Color.black);
            g.drawLine(w - 1, 0, w - 1, (h - 2) - cw);
            g.drawLine(w - 1, (h - 1) - cw, (w - 1) - cw, h - 1);
            g.setColor(getShadowColor());
            g.drawLine(w - 2, 1, w - 2, (h - 2) - cw);
            g.drawLine(w - 2, (h - 1) - cw, (w - 1) - cw, h - 2);
        } else {
            // vertical
            int cw = h / 2;
            if (slider.getComponentOrientation().isLeftToRight()) {
                g.fillRect(1, 1, (w - 1) - cw, h - 3);
                java.awt.Polygon p = new java.awt.Polygon();
                p.addPoint((w - cw) - 1, 0);
                p.addPoint(w - 1, cw);
                p.addPoint((w - 1) - cw, h - 2);
                g.fillPolygon(p);
                g.setColor(getHighlightColor());
                g.drawLine(0, 0, 0, h - 2);// left

                g.drawLine(1, 0, (w - 1) - cw, 0);// top

                g.drawLine((w - cw) - 1, 0, w - 1, cw);// top slant

                g.setColor(java.awt.Color.black);
                g.drawLine(0, h - 1, (w - 2) - cw, h - 1);// bottom

                g.drawLine((w - 1) - cw, h - 1, w - 1, (h - 1) - cw);// bottom slant

                g.setColor(getShadowColor());
                g.drawLine(1, h - 2, (w - 2) - cw, h - 2);// bottom

                g.drawLine((w - 1) - cw, h - 2, w - 2, (h - cw) - 1);// bottom slant

            } else {
                g.fillRect(5, 1, (w - 1) - cw, h - 3);
                java.awt.Polygon p = new java.awt.Polygon();
                p.addPoint(cw, 0);
                p.addPoint(0, cw);
                p.addPoint(cw, h - 2);
                g.fillPolygon(p);
                g.setColor(getHighlightColor());
                g.drawLine(cw - 1, 0, w - 2, 0);// top

                g.drawLine(0, cw, cw, 0);// top slant

                g.setColor(java.awt.Color.black);
                g.drawLine(0, (h - 1) - cw, cw, h - 1);// bottom slant

                g.drawLine(cw, h - 1, w - 1, h - 1);// bottom

                g.setColor(getShadowColor());
                g.drawLine(cw, h - 2, w - 2, h - 2);// bottom

                g.drawLine(w - 1, 1, w - 1, h - 2);// right

            }
        }
        g.translate(-knobBounds.x, -knobBounds.y);
    }

    @java.lang.Override
    protected java.awt.Dimension getThumbSize() {
        java.awt.Dimension size = new java.awt.Dimension();
        if (slider.getOrientation() == javax.swing.JSlider.VERTICAL) {
            size.width = 15;
            size.height = 15;
        } else {
            size.width = 15;
            size.height = 15;
        }
        return size;
    }
}