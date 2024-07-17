/* @(#)ColorSliderUI.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import javax.swing.plaf.basic.*;
/**
 * A UI delegate for color sliders. The track of the slider visualizes how changing the value of the
 * slider affects the color.
 */
public class ColorSliderUI extends javax.swing.plaf.basic.BasicSliderUI {
    private static final java.awt.Color FOREGROUND = new java.awt.Color(0x949494);

    private static final java.awt.Color TRACK_BACKGROUND = new java.awt.Color(0xffffff);

    private org.jhotdraw.color.ColorTrackImageProducer colorTrackImageProducer;

    private java.awt.Image colorTrackImage;

    private static final java.awt.Dimension PREFERRED_HORIZONTAL_SIZE = new java.awt.Dimension(160, 4);

    private static final java.awt.Dimension PREFERRED_VERTICAL_SIZE = new java.awt.Dimension(4, 160);

    private static final java.awt.Dimension MINIMUM_HORIZONTAL_SIZE = new java.awt.Dimension(16, 4);

    private static final java.awt.Dimension MINIMUM_VERTICAL_SIZE = new java.awt.Dimension(4, 16);

    public ColorSliderUI(javax.swing.JSlider b) {
        super(b);
    }

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent b) {
        if (null == javax.swing.UIManager.getIcon("Slider.northThumb.small")) {
            javax.swing.UIManager.put("Slider.northThumb.small", new org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon(org.jhotdraw.util.Images.createImage(org.jhotdraw.color.ColorSliderUI.class, "/org/jhotdraw/color/images/Slider.northThumbs.small.png"), 6, true));
        }
        if (null == javax.swing.UIManager.getIcon("Slider.westThumb.small")) {
            javax.swing.UIManager.put("Slider.westThumb.small", new org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon(org.jhotdraw.util.Images.createImage(org.jhotdraw.color.ColorSliderUI.class, "/org/jhotdraw/color/images/Slider.westThumbs.small.png"), 6, true));
        }
        return new org.jhotdraw.color.ColorSliderUI(((javax.swing.JSlider) (b)));
    }

    @java.lang.Override
    protected void installDefaults(javax.swing.JSlider slider) {
        super.installDefaults(slider);
        focusInsets = new java.awt.Insets(0, 0, 0, 0);
        slider.setOpaque(false);
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            slider.setBorder(new javax.swing.border.EmptyBorder(0, 1, -1, 1));
        } else {
            slider.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 1));
        }
        // slider.setRequestFocusEnabled(QuaquaManager.getBoolean("Slider.requestFocusEnabled"));
        slider.setRequestFocusEnabled(true);
    }

    @java.lang.Override
    protected java.awt.Dimension getThumbSize() {
        javax.swing.Icon thumb = getThumbIcon();
        return new java.awt.Dimension(thumb.getIconWidth(), thumb.getIconHeight());
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredSize(javax.swing.JComponent c) {
        recalculateIfInsetsChanged();
        java.awt.Dimension d;
        if (slider.getOrientation() == javax.swing.JSlider.VERTICAL) {
            d = new java.awt.Dimension(getPreferredVerticalSize());
            d.width += javax.swing.plaf.basic.BasicSliderUI.insetCache.left + javax.swing.plaf.basic.BasicSliderUI.insetCache.right;
            d.width += javax.swing.plaf.basic.BasicSliderUI.focusInsets.left + javax.swing.plaf.basic.BasicSliderUI.focusInsets.right;
            d.width += (javax.swing.plaf.basic.BasicSliderUI.trackRect.width + javax.swing.plaf.basic.BasicSliderUI.tickRect.width) + javax.swing.plaf.basic.BasicSliderUI.labelRect.width;
        } else {
            d = new java.awt.Dimension(getPreferredHorizontalSize());
            d.height += javax.swing.plaf.basic.BasicSliderUI.insetCache.top + javax.swing.plaf.basic.BasicSliderUI.insetCache.bottom;
            d.height += javax.swing.plaf.basic.BasicSliderUI.focusInsets.top + javax.swing.plaf.basic.BasicSliderUI.focusInsets.bottom;
            d.height += (javax.swing.plaf.basic.BasicSliderUI.trackRect.height + javax.swing.plaf.basic.BasicSliderUI.tickRect.height) + javax.swing.plaf.basic.BasicSliderUI.labelRect.height;
        }
        return d;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredHorizontalSize() {
        return org.jhotdraw.color.ColorSliderUI.PREFERRED_HORIZONTAL_SIZE;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredVerticalSize() {
        return org.jhotdraw.color.ColorSliderUI.PREFERRED_VERTICAL_SIZE;
    }

    @java.lang.Override
    public java.awt.Dimension getMinimumHorizontalSize() {
        return org.jhotdraw.color.ColorSliderUI.MINIMUM_HORIZONTAL_SIZE;
    }

    @java.lang.Override
    public java.awt.Dimension getMinimumVerticalSize() {
        return org.jhotdraw.color.ColorSliderUI.MINIMUM_VERTICAL_SIZE;
    }

    @java.lang.Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            javax.swing.plaf.basic.BasicSliderUI.thumbRect.y -= 3;
        } else {
            javax.swing.plaf.basic.BasicSliderUI.thumbRect.x -= 3;
        }
    }

    /* public void paint( Graphics g, JComponent c )   {
    g.setColor(Color.green);
    g.fillRect(0,0,c.getWidth(), c.getHeight());
    super.paint(g,c);
    }
     */
    protected javax.swing.Icon getThumbIcon() {
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            return javax.swing.UIManager.getIcon("Slider.northThumb.small");
        } else {
            return javax.swing.UIManager.getIcon("Slider.westThumb.small");
        }
    }

    @java.lang.Override
    public void paintThumb(java.awt.Graphics g) {
        java.awt.Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;
        getThumbIcon().paintIcon(slider, g, knobBounds.x, knobBounds.y);
        /* g.setColor(Color.green);
        ((Graphics2D) g).draw(knobBounds);
         */
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
            pad = trackBuffer;// - thumbRect.width / 2 + 2;

            cx = (trackBounds.x - pad) + 1;
            cy = trackBounds.y;
            // cy = (trackBounds.height / 2) - 4;
            cw = (trackBounds.width + (pad * 2)) - 2;
            ch = trackBounds.height - 1;
        } else {
            pad = trackBuffer;
            // cx = (trackBounds.width / 2) - 4;
            // cx = (trackBounds.width / 2);
            // cx = thumbRect.x + 2;
            cx = trackBounds.x;
            // cy = pad;
            cy = javax.swing.plaf.basic.BasicSliderUI.contentRect.y + 2;
            cw = trackBounds.width - 1;
            // ch = trackBounds.height;
            ch = (trackBounds.height + (pad * 2)) - 5;
        }
        g.setColor(org.jhotdraw.color.ColorSliderUI.TRACK_BACKGROUND);
        g.fillRect(cx, cy, cw, ch);
        g.setColor(org.jhotdraw.color.ColorSliderUI.FOREGROUND);
        g.drawRect(cx, cy, cw - 1, ch - 1);
        paintColorTrack(g, cx + 2, cy + 2, cw - 4, ch - 4, trackBuffer);
    }

    @java.lang.Override
    public void paintTicks(java.awt.Graphics g) {
        java.awt.Rectangle tickBounds = tickRect;
        int i;
        int maj;
        int min;
        int max;
        int w = tickBounds.width;
        int h = tickBounds.height;
        int centerEffect;
        int tickHeight;
        /* g.setColor(slider.getBackground());
        g.fillRect(tickBounds.x, tickBounds.y, tickBounds.width, tickBounds.height);
         */
        g.setColor(org.jhotdraw.color.ColorSliderUI.FOREGROUND);
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            g.translate(0, tickBounds.y);
            int value = slider.getMinimum();
            int xPos = 0;
            if (slider.getMinorTickSpacing() > 0) {
                while (value <= slider.getMaximum()) {
                    xPos = xPositionForValue(value);
                    paintMinorTickForHorizSlider(g, tickBounds, xPos);
                    value += slider.getMinorTickSpacing();
                } 
            }
            if (slider.getMajorTickSpacing() > 0) {
                value = slider.getMinimum();
                while (value <= slider.getMaximum()) {
                    xPos = xPositionForValue(value);
                    paintMajorTickForHorizSlider(g, tickBounds, xPos);
                    value += slider.getMajorTickSpacing();
                } 
            }
            g.translate(0, -tickBounds.y);
        } else {
            g.translate(tickBounds.x, 0);
            int value = slider.getMinimum();
            int yPos = 0;
            if (slider.getMinorTickSpacing() > 0) {
                int offset = 0;
                if (!slider.getComponentOrientation().isLeftToRight()) {
                    offset = tickBounds.width - (tickBounds.width / 2);
                    g.translate(offset, 0);
                }
                while (value <= slider.getMaximum()) {
                    yPos = yPositionForValue(value);
                    paintMinorTickForVertSlider(g, tickBounds, yPos);
                    value += slider.getMinorTickSpacing();
                } 
                if (!slider.getComponentOrientation().isLeftToRight()) {
                    g.translate(-offset, 0);
                }
            }
            if (slider.getMajorTickSpacing() > 0) {
                value = slider.getMinimum();
                if (!slider.getComponentOrientation().isLeftToRight()) {
                    g.translate(2, 0);
                }
                while (value <= slider.getMaximum()) {
                    yPos = yPositionForValue(value);
                    paintMajorTickForVertSlider(g, tickBounds, yPos);
                    value += slider.getMajorTickSpacing();
                } 
                if (!slider.getComponentOrientation().isLeftToRight()) {
                    g.translate(-2, 0);
                }
            }
            g.translate(-tickBounds.x, 0);
        }
        /* g.setColor(Color.red);
        ((Graphics2D) g).draw(tickBounds);
         */
    }

    @java.lang.Override
    protected void paintMajorTickForHorizSlider(java.awt.Graphics g, java.awt.Rectangle tickBounds, int x) {
        g.drawLine(x, 0, x, tickBounds.height - 1);
    }

    @java.lang.Override
    protected void paintMinorTickForHorizSlider(java.awt.Graphics g, java.awt.Rectangle tickBounds, int x) {
        // g.drawLine( x, 0, x, tickBounds.height / 2 - 1 );
        g.drawLine(x, 0, x, tickBounds.height - 1);
    }

    @java.lang.Override
    protected void paintMinorTickForVertSlider(java.awt.Graphics g, java.awt.Rectangle tickBounds, int y) {
        g.drawLine(tickBounds.width / 2, y, (tickBounds.width / 2) - 1, y);
    }

    @java.lang.Override
    protected void paintMajorTickForVertSlider(java.awt.Graphics g, java.awt.Rectangle tickBounds, int y) {
        g.drawLine(0, y, tickBounds.width - 1, y);
    }

    @java.lang.Override
    public void paintFocus(java.awt.Graphics g) {
    }

    public void paintColorTrack(java.awt.Graphics g, int x, int y, int width, int height, int buffer) {
        // g.setColor(Color.black);
        // g.fillRect(x, y, width, height);
        if (((colorTrackImageProducer == null) || (colorTrackImageProducer.getWidth() != width)) || (colorTrackImageProducer.getHeight() != height)) {
            if (colorTrackImage != null) {
                colorTrackImage.flush();
            }
            colorTrackImageProducer = new org.jhotdraw.color.ColorTrackImageProducer(width, height, buffer + 2, slider.getOrientation() == javax.swing.JSlider.HORIZONTAL);
            if (slider.getClientProperty("colorSliderModel") != null) {
                colorTrackImageProducer.setColorSliderModel(((org.jhotdraw.color.ColorSliderModel) (slider.getClientProperty("colorSliderModel"))));
            }
            if (slider.getClientProperty("colorComponentIndex") != null) {
                colorTrackImageProducer.setColorComponentIndex(((java.lang.Integer) (slider.getClientProperty("colorComponentIndex"))));
            }
            colorTrackImageProducer.generateColorTrack();
            colorTrackImage = slider.createImage(colorTrackImageProducer);
        } else if (colorTrackImageProducer.needsGeneration()) {
            // To keep the UI responsive, we only perform the time consuming
            // regeneration of the color track if we don't already have
            // a latency of more than a 10th of a second on the most recent event.
            long latency = java.lang.System.currentTimeMillis() - java.awt.EventQueue.getMostRecentEventTime();
            if (latency > 100) {
                slider.repaint();
            } else {
                colorTrackImageProducer.regenerateColorTrack();
            }
        }
        if (colorTrackImage != null) {
            g.drawImage(colorTrackImage, x, y, slider);
        }
    }

    @java.lang.Override
    protected void calculateTrackRect() {
        int centerSpacing = 0;// used to center sliders added using BorderLayout.CENTER (bug 4275631)

        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            centerSpacing = javax.swing.plaf.basic.BasicSliderUI.thumbRect.height;
            if (slider.getPaintTicks()) {
                centerSpacing += getTickLength();
            }
            if (slider.getPaintLabels()) {
                centerSpacing += getHeightOfTallestLabel();
            }
            javax.swing.plaf.basic.BasicSliderUI.trackRect.x = (javax.swing.plaf.basic.BasicSliderUI.contentRect.x + trackBuffer) + 1;
            // trackRect.y = contentRect.y + (contentRect.height - centerSpacing - 1)/2;
            javax.swing.plaf.basic.BasicSliderUI.trackRect.height = 14;
            javax.swing.plaf.basic.BasicSliderUI.trackRect.y = (javax.swing.plaf.basic.BasicSliderUI.contentRect.y + javax.swing.plaf.basic.BasicSliderUI.contentRect.height) - javax.swing.plaf.basic.BasicSliderUI.trackRect.height;
            javax.swing.plaf.basic.BasicSliderUI.trackRect.width = (javax.swing.plaf.basic.BasicSliderUI.contentRect.width - (trackBuffer * 2)) - 1;
        } else {
            /* centerSpacing = thumbRect.width;
            if (! QuaquaUtilities.isLeftToRight(slider)) {
            if ( slider.getPaintTicks() ) centerSpacing += getTickLength();
            if ( slider.getPaintLabels() ) centerSpacing += getWidthOfWidestLabel();
            } else {
            if ( slider.getPaintTicks() ) centerSpacing -= getTickLength();
            if ( slider.getPaintLabels() ) centerSpacing -= getWidthOfWidestLabel();
            }
            trackRect.x = contentRect.x + (contentRect.width - centerSpacing - 1)/2 + 2;
             */
            javax.swing.plaf.basic.BasicSliderUI.trackRect.width = 14;
            javax.swing.plaf.basic.BasicSliderUI.trackRect.x = javax.swing.plaf.basic.BasicSliderUI.contentRect.x + ((javax.swing.plaf.basic.BasicSliderUI.contentRect.width - javax.swing.plaf.basic.BasicSliderUI.trackRect.width) / 2);
            javax.swing.plaf.basic.BasicSliderUI.trackRect.y = javax.swing.plaf.basic.BasicSliderUI.contentRect.y + trackBuffer;
            javax.swing.plaf.basic.BasicSliderUI.trackRect.height = (javax.swing.plaf.basic.BasicSliderUI.contentRect.height - (trackBuffer * 2)) + 1;
        }
    }

    @java.lang.Override
    protected void calculateTickRect() {
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            javax.swing.plaf.basic.BasicSliderUI.tickRect.x = javax.swing.plaf.basic.BasicSliderUI.trackRect.x;
            // tickRect.y = trackRect.y + trackRect.height;
            javax.swing.plaf.basic.BasicSliderUI.tickRect.y = javax.swing.plaf.basic.BasicSliderUI.trackRect.y - getTickLength();
            javax.swing.plaf.basic.BasicSliderUI.tickRect.width = javax.swing.plaf.basic.BasicSliderUI.trackRect.width;
            javax.swing.plaf.basic.BasicSliderUI.tickRect.height = getTickLength();
            if (!slider.getPaintTicks()) {
                --javax.swing.plaf.basic.BasicSliderUI.tickRect.y;
                javax.swing.plaf.basic.BasicSliderUI.tickRect.height = 0;
            }
        } else {
            /* if(! QuaquaUtilities.isLeftToRight(slider)) {
            tickRect.x = trackRect.x + trackRect.width;
            tickRect.width = getTickLength();
            }
            else {
            tickRect.width = getTickLength();
            tickRect.x = trackRect.x - tickRect.width;
            }
             */
            javax.swing.plaf.basic.BasicSliderUI.tickRect.width = getTickLength();
            javax.swing.plaf.basic.BasicSliderUI.tickRect.x = javax.swing.plaf.basic.BasicSliderUI.contentRect.x;// trackRect.x - tickRect.width - 1;

            javax.swing.plaf.basic.BasicSliderUI.tickRect.y = javax.swing.plaf.basic.BasicSliderUI.trackRect.y;
            javax.swing.plaf.basic.BasicSliderUI.tickRect.height = javax.swing.plaf.basic.BasicSliderUI.trackRect.height;
            if (!slider.getPaintTicks()) {
                --javax.swing.plaf.basic.BasicSliderUI.tickRect.x;
                javax.swing.plaf.basic.BasicSliderUI.tickRect.width = 0;
            }
        }
    }

    /**
     * Gets the height of the tick area for horizontal sliders and the width of the tick area for
     * vertical sliders. BasicSliderUI uses the returned value to determine the tick area rectangle.
     * If you want to give your ticks some room, make this larger than you need and paint your ticks
     * away from the sides in paintTicks().
     */
    @java.lang.Override
    protected int getTickLength() {
        return 4;
    }

    @java.lang.Override
    protected java.beans.PropertyChangeListener createPropertyChangeListener(javax.swing.JSlider slider) {
        return new org.jhotdraw.color.ColorSliderUI.CSUIPropertyChangeHandler();
    }

    public class CSUIPropertyChangeHandler extends javax.swing.plaf.basic.BasicSliderUI.PropertyChangeHandler {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent e) {
            java.lang.String propertyName = e.getPropertyName();
            if ("Frame.active".equals(propertyName)) {
                // calculateGeometry();
                slider.repaint();
            } else if ("colorSliderModel".equals(propertyName)) {
                if (colorTrackImageProducer != null) {
                    colorTrackImageProducer.setColorSliderModel(((org.jhotdraw.color.ColorSliderModel) (e.getNewValue())));
                    if (colorTrackImageProducer.needsGeneration()) {
                        slider.repaint();
                    }
                }
            } else if ("snapToTicks".equals(propertyName)) {
                if (colorTrackImageProducer != null) {
                    colorTrackImageProducer.markAsDirty();
                    slider.repaint();
                }
            } else if ("colorComponentIndex".equals(propertyName)) {
                if ((colorTrackImageProducer != null) && (e.getNewValue() != null)) {
                    colorTrackImageProducer.setColorComponentIndex(((java.lang.Integer) (e.getNewValue())));
                    if (colorTrackImageProducer.needsGeneration()) {
                        slider.repaint();
                    }
                }
            } else if ("colorComponentChange".equals(propertyName)) {
                java.lang.Integer value = ((java.lang.Integer) (e.getNewValue()));
                if ((value != null) && (colorTrackImageProducer != null)) {
                    colorTrackImageProducer.componentChanged(value);
                    if (colorTrackImageProducer.needsGeneration()) {
                        slider.repaint();
                    }
                }
            } else if ("colorComponentValue".equals(propertyName)) {
                java.lang.Integer value = ((java.lang.Integer) (slider.getClientProperty("colorComponentChange")));
                if ((value != null) && (colorTrackImageProducer != null)) {
                    colorTrackImageProducer.componentChanged(value);
                    if (colorTrackImageProducer.needsGeneration()) {
                        slider.repaint();
                    }
                }
            } else if ("orientation".equals(propertyName)) {
                if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
                    slider.setBorder(new javax.swing.border.EmptyBorder(0, 1, -1, 1));
                } else {
                    slider.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 1));
                }
            }
            super.propertyChange(e);
        }
    }

    @java.lang.Override
    protected org.jhotdraw.color.ColorSliderUI.TrackListener createTrackListener(javax.swing.JSlider slider) {
        return new org.jhotdraw.color.ColorSliderUI.TrackListener();
    }

    /**
     * Track mouse movements.
     *
     * <p>This inner class is marked &quot;public&quot; due to a compiler bug. This class should be
     * treated as a &quot;protected&quot; inner class. Instantiate it only within subclasses of <Foo>.
     */
    {
    }

    public class TrackListener extends javax.swing.plaf.basic.BasicSliderUI.TrackListener {
        /**
         * If the mouse is pressed above the "thumb" component then reduce the scrollbars value by one
         * page ("page up"), otherwise increase it by one page. If there is no thumb then page up if the
         * mouse is in the upper half of the track.
         */
        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }
            currentMouseX = e.getX();
            currentMouseY = e.getY();
            if (slider.isRequestFocusEnabled()) {
                slider.requestFocus();
            }
            // Clicked inside the Thumb area?
            if (thumbRect.contains(currentMouseX, currentMouseY)) {
                super.mousePressed(e);
            } else {
                switch (slider.getOrientation()) {
                    case javax.swing.JSlider.VERTICAL :
                        slider.setValue(valueForYPosition(currentMouseY));
                        break;
                    case javax.swing.JSlider.HORIZONTAL :
                        slider.setValue(valueForXPosition(currentMouseX));
                        break;
                }
                // FIXME:
                // We should set isDragging to false here. Unfortunately,
                // we can not access this variable in class BasicSliderUI.
            }
        }
    }
}