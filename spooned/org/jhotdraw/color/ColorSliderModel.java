/**
 *
 * @(#)ColorSliderModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * ColorSliderModel.
 *
 * <p>Colors are represented as arrays of color components represented as BoundedRangeModel's. Each
 * BoundedRangeModel can be visualized using a JSlider having a ColorSliderUI.
 */
public interface ColorSliderModel {
    /**
     * Returns the ColorSpace used by this ColorSliderModel.
     *
     * @return ColorSpace.
     */
    public java.awt.color.ColorSpace getColorSpace();

    /**
     * Changes the ColorSpace used by this ColorSliderModel.
     *
     * @param newValue
     * 		ColorSpace.
     */
    public void setColorSpace(java.awt.color.ColorSpace newValue);

    /**
     * Returns the number of components used by this ColorSliderModel.
     *
     * @return Component count.
     */
    public int getComponentCount();

    /**
     * Returns the BoundedRangeModel used for the specified component index.
     *
     * @param componentIndex
     * 		.
     * @return BoundedRangeModel.
     */
    public javax.swing.BoundedRangeModel getBoundedRangeModel(int componentIndex);

    /**
     * Returns an RGB value based on the value of the specified component index and value, based on
     * the values of all other components of the model.
     *
     * @param componentIndex
     * @param componentValue
     * @return RGB value.
     */
    public int getInterpolatedRGB(int componentIndex, float componentValue);

    /**
     * Sets a value for an individual component.
     *
     * @param componentIndex
     * @param newValue
     */
    public void setComponent(int componentIndex, float newValue);

    /**
     * Gets a value of an individual component.
     *
     * @param componentIndex
     * @return Value
     */
    public float getComponent(int componentIndex);

    /**
     * Gets all component values.
     *
     * @return Values.
     */
    public float[] getComponents();

    public void addChangeListener(javax.swing.event.ChangeListener l);

    public void removeChangeListener(javax.swing.event.ChangeListener l);

    /**
     * Configures a JSlider.
     */
    public void configureSlider(int componentIndex, javax.swing.JSlider slider);

    /**
     * Unconfigures a JSlider.
     */
    public void unconfigureSlider(javax.swing.JSlider slider);

    /**
     * Returns the color value of the model. This is a convenience method for calling
     * getCompositeColor().getColor().
     *
     * @return color.
     */
    public java.awt.Color getColor();

    /**
     * Sets the color value of the model. This is a convenience method for calling
     * setCompositeColor(new CompositeColor(getColorSpace(), color.getRGB());
     *
     * @param newValue
     * 		.
     */
    public void setColor(java.awt.Color newValue);
}