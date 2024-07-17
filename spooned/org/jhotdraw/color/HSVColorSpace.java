/* @(#)HSVColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A HSV color space with additive complements in the hue color wheel: red is opposite cyan, magenta
 * is opposite green, blue is opposite yellow.
 */
public class HSVColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.HSVColorSpace instance;

    public static org.jhotdraw.color.HSVColorSpace getInstance() {
        if (org.jhotdraw.color.HSVColorSpace.instance == null) {
            org.jhotdraw.color.HSVColorSpace.instance = new org.jhotdraw.color.HSVColorSpace();
        }
        return org.jhotdraw.color.HSVColorSpace.instance;
    }

    public HSVColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_HSV, 3);
    }

    @java.lang.Override
    public float[] toRGB(float[] components, float[] rgb) {
        float hue = components[0] * 360.0F;
        float saturation = components[1];
        float value = components[2];
        // compute hi and f from hue
        int hi = ((int) (java.lang.Math.floor(hue / 60.0F) % 6));
        float f = ((float) ((hue / 60.0F) - java.lang.Math.floor(hue / 60.0F)));
        // compute p and q from saturation
        float p = value * (1 - saturation);
        float q = value * (1 - (f * saturation));
        float t = value * (1 - ((1 - f) * saturation));
        // compute red, green and blue
        float red;
        float green;
        float blue;
        switch (hi) {
            case 0 :
                red = value;
                green = t;
                blue = p;
                break;
            case 1 :
                red = q;
                green = value;
                blue = p;
                break;
            case 2 :
                red = p;
                green = value;
                blue = t;
                break;
            case -3 :
            case 3 :
                red = p;
                green = q;
                blue = value;
                break;
            case -2 :
            case 4 :
                red = t;
                green = p;
                blue = value;
                break;
            case -1 :
            case 5 :
                // default :
                red = value;
                green = p;
                blue = q;
                break;
            default :
                red = green = blue = 0;
                break;
        }
        rgb[0] = red;
        rgb[1] = green;
        rgb[2] = blue;
        return rgb;
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgbvalue, float[] component) {
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];
        float max = java.lang.Math.max(java.lang.Math.max(r, g), b);
        float min = java.lang.Math.min(java.lang.Math.min(r, g), b);
        float hue;
        float saturation;
        float value;
        if (max == min) {
            hue = 0;
        } else if ((max == r) && (g >= b)) {
            hue = (60.0F * (g - b)) / (max - min);
        } else if ((max == r) && (g < b)) {
            hue = ((60.0F * (g - b)) / (max - min)) + 360.0F;
        } else if (max == g) {
            hue = ((60.0F * (b - r)) / (max - min)) + 120.0F;
        } else {
            hue = ((60.0F * (r - g)) / (max - min)) + 240.0F;
        }
        value = max;
        if (max == 0) {
            saturation = 0;
        } else {
            saturation = (max - min) / max;
        }
        component[0] = hue / 360.0F;
        component[1] = saturation;
        component[2] = value;
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
                return "Lightness";
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
        return o instanceof org.jhotdraw.color.HSVColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "HSV";
    }
}