/* @(#)PaletteSliderThumbIcon.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * An Icon with different visuals reflecting the state of the slider on which it draws on.
 */
public class PaletteSliderThumbIcon extends org.jhotdraw.gui.plaf.palette.MultiIcon {
    private static final int E = 0;

    private static final int EP = 1;

    private static final int D = 2;

    private static final int I = 3;

    private static final int DI = 4;

    private static final int FOCUS_RING = 5;

    /**
     * Creates a new instance. All icons must have the same dimensions. If an icon is null, an icon is
     * derived for the state from the other icons.
     */
    public PaletteSliderThumbIcon(javax.swing.Icon e, javax.swing.Icon ep, javax.swing.Icon d, javax.swing.Icon i, javax.swing.Icon di) {
        super(new javax.swing.Icon[]{ e, ep, d, i, di });
    }

    /**
     * Creates a new instance. All icons must have the same dimensions.
     *
     * <p>The array indices are used to represente the following states: [0] Enabled [1] Enabled
     * Pressed [2] Disabled [3] Enabled Inactive [4] Disabled Inactive [5] Focus Ring
     *
     * <p>If an array element is null, an icon is derived for the state from the other icons.
     */
    public PaletteSliderThumbIcon(java.awt.Image[] images) {
        super(images);
    }

    /**
     * Creates a new instance. All icons must have the same dimensions. If an icon is null, nothing is
     * drawn for this state.
     */
    public PaletteSliderThumbIcon(javax.swing.Icon[] icons) {
        super(icons);
    }

    public PaletteSliderThumbIcon(java.awt.Image tiledImage, int tileCount, boolean isTiledHorizontaly) {
        super(tiledImage, tileCount, isTiledHorizontaly);
    }

    /**
     * Creates a new instance. The icon representations are created lazily from the specified
     * resource.
     *
     * @param resource
     * 		A resource URL.
     * @param tileCount
     * 		The number of tiles.
     * @param isTiledHorizontaly
     * 		True if the image is to be tilled horizontally, false for vertically.
     */
    public PaletteSliderThumbIcon(java.lang.String resource, int tileCount, boolean isTiledHorizontaly) {
        super(org.jhotdraw.util.Images.createImage(org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.class.getResource(resource)), tileCount, isTiledHorizontaly);
    }

    @java.lang.Override
    protected void generateMissingIcons() {
        javax.swing.Icon[] oldIcons;
        if (icons.length != 6) {
            oldIcons = new javax.swing.Icon[6];
            java.lang.System.arraycopy(icons, 0, oldIcons, 0, java.lang.Math.min(icons.length, 6));
        } else {
            oldIcons = icons;
        }
        if (icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.EP] == null) {
            icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.EP] = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.E];
        }
        if (icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.D] == null) {
            icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.D] = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.E];
        }
        if (icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.I] == null) {
            icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.I] = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.E];
        }
        if (icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.DI] == null) {
            icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.DI] = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.D];
        }
    }

    @java.lang.Override
    protected javax.swing.Icon getIcon(java.awt.Component c) {
        javax.swing.Icon icon;
        boolean isActive = true;// QuaquaUtilities.isOnActiveWindow(c);

        if (c instanceof javax.swing.JSlider) {
            javax.swing.JSlider slider = ((javax.swing.JSlider) (c));
            if (isActive) {
                if (c.isEnabled()) {
                    if (slider.getModel().getValueIsAdjusting()) {
                        icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.EP];
                    } else {
                        icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.E];
                    }
                } else {
                    icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.D];
                }
            } else if (c.isEnabled()) {
                icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.I];
            } else {
                icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.DI];
            }
        } else if (isActive) {
            if (c.isEnabled()) {
                icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.E];
            } else {
                icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.D];
            }
        } else if (c.isEnabled()) {
            icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.I];
        } else {
            icon = icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.DI];
        }
        return icon;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);
        if (c.isFocusOwner()/* QuaquaUtilities.isFocused(c) */
         && (icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.FOCUS_RING] != null)) {
            icons[org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon.FOCUS_RING].paintIcon(c, g, x, y);
        }
    }
}