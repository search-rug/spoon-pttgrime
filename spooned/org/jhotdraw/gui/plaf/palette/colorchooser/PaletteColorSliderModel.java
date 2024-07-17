/* @(#)PaletteColorSliderModel.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette.colorchooser;
/**
 * PaletteColorSliderModel.
 */
public class PaletteColorSliderModel extends org.jhotdraw.color.DefaultColorSliderModel {
    private static final long serialVersionUID = 1L;

    PaletteColorSliderModel(java.awt.color.ColorSpace colorSpace) {
        super(colorSpace);
    }

    /**
     * Configures a JSlider for this model. If the JSlider is already configured for another model, it
     * is unconfigured first.
     */
    @java.lang.Override
    public void configureSlider(int componentIndex, javax.swing.JSlider slider) {
        if (slider.getClientProperty("colorSliderModel") != null) {
            ((org.jhotdraw.color.DefaultColorSliderModel) (slider.getClientProperty("colorSliderModel"))).unconfigureSlider(slider);
        }
        if (!(slider.getUI() instanceof org.jhotdraw.gui.plaf.palette.PaletteColorSliderUI)) {
            slider.setUI(((org.jhotdraw.gui.plaf.palette.PaletteColorSliderUI) (org.jhotdraw.gui.plaf.palette.PaletteColorSliderUI.createUI(slider))));
        }
        javax.swing.BoundedRangeModel brm = getBoundedRangeModel(componentIndex);
        slider.setModel(brm);
        slider.putClientProperty("colorSliderModel", this);
        slider.putClientProperty("colorComponentIndex", componentIndex);
        addColorSlider(slider);
    }
}