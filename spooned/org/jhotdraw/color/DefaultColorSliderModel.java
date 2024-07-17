/**
 *
 * @(#)DefaultColorSliderModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * DefaultColorSliderModel.
 */
public class DefaultColorSliderModel extends org.jhotdraw.color.AbstractColorSlidersModel {
    private static final long serialVersionUID = 1L;

    protected java.awt.color.ColorSpace colorSpace;

    /**
     * JSlider's associated to this model.
     */
    protected java.util.List<javax.swing.JSlider> sliders = new java.util.ArrayList<>();

    /**
     * Components of the color model.
     */
    protected javax.swing.DefaultBoundedRangeModel[] componentModels;

    /**
     * Creates a color slider model with an ICC sRGB color space.
     */
    public DefaultColorSliderModel() {
        setColorSpace(java.awt.color.ICC_ColorSpace.getInstance(java.awt.color.ICC_ColorSpace.CS_sRGB));
    }

    /**
     * Creates a color slider model with the specified color space.
     */
    public DefaultColorSliderModel(java.awt.color.ColorSpace sys) {
        setColorSpace(sys);
    }

    @java.lang.Override
    public void setColorSpace(java.awt.color.ColorSpace newValue) {
        java.awt.color.ColorSpace oldValue = colorSpace;
        colorSpace = newValue;
        componentModels = new javax.swing.DefaultBoundedRangeModel[colorSpace.getNumComponents()];
        for (int i = 0; i < componentModels.length; i++) {
            componentModels[i] = new javax.swing.DefaultBoundedRangeModel();
            if ((colorSpace.getMaxValue(i) - colorSpace.getMinValue(i)) >= 10.0F) {
                componentModels[i].setMinimum(((int) (colorSpace.getMinValue(i))));
                componentModels[i].setMaximum(((int) (colorSpace.getMaxValue(i))));
            } else {
                componentModels[i].setMinimum(((int) (colorSpace.getMinValue(i) * 100.0F)));
                componentModels[i].setMaximum(((int) (colorSpace.getMaxValue(i) * 100.0F)));
            }
            final int componentIndex = i;
            componentModels[i].addChangeListener(new javax.swing.event.ChangeListener() {
                @java.lang.Override
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    fireColorChanged(componentIndex);
                    fireStateChanged();
                }
            });
        }
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
        if (!(slider.getUI() instanceof org.jhotdraw.color.ColorSliderUI)) {
            slider.setUI(((org.jhotdraw.color.ColorSliderUI) (org.jhotdraw.color.ColorSliderUI.createUI(slider))));
        }
        slider.setModel(getBoundedRangeModel(componentIndex));
        slider.putClientProperty("colorSliderModel", this);
        slider.putClientProperty("colorComponentIndex", componentIndex);
        addColorSlider(slider);
    }

    /**
     * Unconfigures a JSlider from this model.
     */
    @java.lang.Override
    public void unconfigureSlider(javax.swing.JSlider slider) {
        if (slider.getClientProperty("colorSliderModel") == this) {
            // XXX - This creates a NullPointerException ??
            // slider.setUI((SliderUI) UIManager.getUI(slider));
            slider.setModel(new javax.swing.DefaultBoundedRangeModel());
            slider.putClientProperty("colorSliderModel", null);
            slider.putClientProperty("colorComponentIndex", null);
            removeColorSlider(slider);
        }
    }

    /**
     * Returns the bounded range model of the specified color componentIndex.
     */
    @java.lang.Override
    public javax.swing.DefaultBoundedRangeModel getBoundedRangeModel(int componentIndex) {
        return componentModels[componentIndex];
    }

    /**
     * Returns the value of the specified color componentIndex.
     */
    public int getSliderValue(int componentIndex) {
        return componentModels[componentIndex].getValue();
    }

    /**
     * Sets the value of the specified color componentIndex.
     */
    public void setSliderValue(int componentIndex, int value) {
        componentModels[componentIndex].setValue(value);
    }

    public void addColorSlider(javax.swing.JSlider slider) {
        sliders.add(slider);
    }

    public void removeColorSlider(javax.swing.JSlider slider) {
        sliders.remove(slider);
    }

    protected void fireColorChanged(int componentIndex) {
        java.lang.Integer index = componentIndex;
        java.awt.Color value = getColor();
        for (javax.swing.JSlider slider : sliders) {
            slider.putClientProperty("colorComponentChange", index);
            slider.putClientProperty("colorComponentValue", value);
        }
    }

    @java.lang.Override
    public java.awt.color.ColorSpace getColorSpace() {
        return colorSpace;
    }

    @java.lang.Override
    public int getComponentCount() {
        return colorSpace.getNumComponents();
    }

    @java.lang.Override
    public java.awt.Color getColor() {
        float[] c = new float[getComponentCount()];
        int i = 0;
        for (javax.swing.DefaultBoundedRangeModel brm : componentModels) {
            c[i] = (((brm.getValue() - brm.getMinimum()) / ((float) (brm.getMaximum() - brm.getMinimum()))) * (colorSpace.getMaxValue(i) - colorSpace.getMinValue(i))) + colorSpace.getMinValue(i);
            i++;
        }
        try {
            return org.jhotdraw.color.ColorUtil.toColor(colorSpace, c);
        } catch (java.lang.IllegalArgumentException e) {
            for (i = 0; i < c.length; i++) {
                java.lang.System.err.println((((((i + "=") + c[i]) + " ") + colorSpace.getMinValue(i)) + "..") + colorSpace.getMaxValue(i));
            }
            throw e;
        }
    }

    @java.lang.Override
    public int getInterpolatedRGB(int i, float componentValue) {
        float[] c = new float[java.lang.Math.max(3, getComponentCount())];
        int j = 0;
        for (javax.swing.DefaultBoundedRangeModel brm : componentModels) {
            c[j] = (((brm.getValue() - brm.getMinimum()) / ((float) (brm.getMaximum() - brm.getMinimum()))) * (colorSpace.getMaxValue(j) - colorSpace.getMinValue(j))) + colorSpace.getMinValue(j);
            j++;
        }
        c[i] = componentValue;
        return org.jhotdraw.color.ColorUtil.CStoRGB24(colorSpace, c, c);
    }

    @java.lang.Override
    public void setComponent(int i, float newValue) {
        javax.swing.BoundedRangeModel brm = componentModels[i];
        brm.setValue(((int) (((newValue - colorSpace.getMinValue(i)) / (colorSpace.getMaxValue(i) - colorSpace.getMinValue(i))) * (brm.getMaximum() - brm.getMinimum()))) + brm.getMinimum());
    }

    @java.lang.Override
    public float getComponent(int i) {
        javax.swing.BoundedRangeModel brm = componentModels[i];
        return (((brm.getValue() - brm.getMinimum()) / ((float) (brm.getMaximum() - brm.getMinimum()))) * (colorSpace.getMaxValue(i) - colorSpace.getMinValue(i))) + colorSpace.getMinValue(i);
    }

    @java.lang.Override
    public void setColor(java.awt.Color newValue) {
        float[] c = org.jhotdraw.color.ColorUtil.fromColor(colorSpace, newValue);
        int i = 0;
        for (javax.swing.DefaultBoundedRangeModel brm : componentModels) {
            brm.setValue(((int) ((((c[i] - colorSpace.getMinValue(i)) / (colorSpace.getMaxValue(i) - colorSpace.getMinValue(i))) * (brm.getMaximum() - brm.getMinimum())) + brm.getMinimum())));
            i++;
        }
    }

    @java.lang.Override
    public float[] getComponents() {
        float[] c = new float[getComponentCount()];
        for (int i = 0; i < c.length; i++) {
            javax.swing.BoundedRangeModel brm = componentModels[i];
            c[i] = (((brm.getValue() - brm.getMinimum()) / ((float) (brm.getMaximum() - brm.getMinimum()))) * (colorSpace.getMaxValue(i) - colorSpace.getMinValue(i))) + colorSpace.getMinValue(i);
        }
        return c;
    }
}