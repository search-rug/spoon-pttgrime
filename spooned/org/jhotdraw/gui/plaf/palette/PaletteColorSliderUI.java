/* @(#)PaletteColorSliderUI.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteColorSliderUI.
 */
public class PaletteColorSliderUI extends org.jhotdraw.color.ColorSliderUI {
    public PaletteColorSliderUI(javax.swing.JSlider b) {
        super(b);
    }

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent b) {
        return new org.jhotdraw.gui.plaf.palette.PaletteColorSliderUI(((javax.swing.JSlider) (b)));
    }

    @java.lang.Override
    protected javax.swing.Icon getThumbIcon() {
        java.lang.String key;
        if (slider.getOrientation() == javax.swing.JSlider.HORIZONTAL) {
            key = "Slider.northThumb.small";
        } else {
            key = "Slider.westThumb.small";
        }
        javax.swing.Icon icon = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getIcon(key);
        if (icon == null) {
            throw new java.lang.InternalError(key + " missing in PaletteLookAndFeel");
        }
        return icon;
    }
}