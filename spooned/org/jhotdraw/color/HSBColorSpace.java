/* @(#)HSBColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A HSB color space with additive complements in the hue color wheel: red is opposite cyan, magenta
 * is opposite green, blue is opposite yellow.
 */
public class HSBColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.HSBColorSpace instance;

    public static org.jhotdraw.color.HSBColorSpace getInstance() {
        if (org.jhotdraw.color.HSBColorSpace.instance == null) {
            org.jhotdraw.color.HSBColorSpace.instance = new org.jhotdraw.color.HSBColorSpace();
        }
        return org.jhotdraw.color.HSBColorSpace.instance;
    }

    public HSBColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_HSV, 3);
    }

    @java.lang.Override
    public float[] toRGB(float[] c, float[] component) {
        int rgb = java.awt.Color.HSBtoRGB(c[0], c[1], c[2]);
        component[0] = ((rgb & 0xff0000) >> 16) / 255.0F;
        component[1] = ((rgb & 0xff00) >> 8) / 255.0F;
        component[2] = (rgb & 0xff) / 255.0F;
        return component;
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgb, float[] component) {
        java.awt.Color.RGBtoHSB(((int) (rgb[0] * 255)), ((int) (rgb[1] * 255)), ((int) (rgb[2] * 255)), component);
        return component;
    }

    @java.lang.Override
    public java.lang.String getName(int idx) {
        switch (idx) {
            case 0 :
                return "Hue";
            case 1 :
                return "Saturation";
            case 2 :
                return "Brightness";
            default :
                throw new java.lang.IllegalArgumentException("index must be between 0 and 2:" + idx);
        }
    }

    @java.lang.Override
    public float getMaxValue(int component) {
        return 1.0F;
    }

    @java.lang.Override
    public float getMinValue(int component) {
        return 0.0F;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        return o instanceof org.jhotdraw.color.HSBColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "HSB";
    }
}