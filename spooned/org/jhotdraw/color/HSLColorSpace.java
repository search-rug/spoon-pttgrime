/* @(#)HSLColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A HSL color space with additive complements in the hue color wheel: red is opposite cyan, magenta
 * is opposite green, blue is opposite yellow.
 */
public class HSLColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.HSLColorSpace instance;

    public static org.jhotdraw.color.HSLColorSpace getInstance() {
        if (org.jhotdraw.color.HSLColorSpace.instance == null) {
            org.jhotdraw.color.HSLColorSpace.instance = new org.jhotdraw.color.HSLColorSpace();
        }
        return org.jhotdraw.color.HSLColorSpace.instance;
    }

    public HSLColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_HSV, 3);
    }

    @java.lang.Override
    public float[] toRGB(float[] components, float[] rgb) {
        float hue = components[0];
        float saturation = components[1];
        float lightness = components[2];
        // compute p and q from saturation and lightness
        float q;
        if (lightness < 0.5F) {
            q = lightness * (1.0F + saturation);
        } else {
            q = (lightness + saturation) - (lightness * saturation);
        }
        float p = (2.0F * lightness) - q;
        // normalize hue to -1..+1
        float hk = hue - ((float) (java.lang.Math.floor(hue)));// / 360f;

        // compute red, green and blue
        float red = hk + (1.0F / 3.0F);
        float green = hk;
        float blue = hk - (1.0F / 3.0F);
        // normalize rgb values
        if (red < 0) {
            red = red + 1.0F;
        } else if (red > 1) {
            red = red - 1.0F;
        }
        if (green < 0) {
            green = green + 1.0F;
        } else if (green > 1) {
            green = green - 1.0F;
        }
        if (blue < 0) {
            blue = blue + 1.0F;
        } else if (blue > 1) {
            blue = blue - 1.0F;
        }
        // adjust rgb values
        if (red < (1.0F / 6.0F)) {
            red = p + (((q - p) * 6) * red);
        } else if (red < 0.5F) {
            red = q;
        } else if (red < (2.0F / 3.0F)) {
            red = p + (((q - p) * 6) * ((2.0F / 3.0F) - red));
        } else {
            red = p;
        }
        if (green < (1.0F / 6.0F)) {
            green = p + (((q - p) * 6) * green);
        } else if (green < 0.5F) {
            green = q;
        } else if (green < (2.0F / 3.0F)) {
            green = p + (((q - p) * 6) * ((2.0F / 3.0F) - green));
        } else {
            green = p;
        }
        if (blue < (1.0F / 6.0F)) {
            blue = p + (((q - p) * 6) * blue);
        } else if (blue < 0.5F) {
            blue = q;
        } else if (blue < (2.0F / 3.0F)) {
            blue = p + (((q - p) * 6) * ((2.0F / 3.0F) - blue));
        } else {
            blue = p;
        }
        rgb[0] = org.jhotdraw.color.HSLColorSpace.clamp(red, 0, 1);
        rgb[1] = org.jhotdraw.color.HSLColorSpace.clamp(green, 0, 1);
        rgb[2] = org.jhotdraw.color.HSLColorSpace.clamp(blue, 0, 1);
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
        float luminance;
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
        luminance = (max + min) / 2.0F;
        if (max == min) {
            saturation = 0;
        } else if (luminance <= 0.5F) {
            saturation = (max - min) / (max + min);
        } else {
            saturation = (max - min) / (2 - (max + min));
        }
        component[0] = hue / 360.0F;
        component[1] = saturation;
        component[2] = luminance;
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
        return o instanceof org.jhotdraw.color.HSLColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "HSL";
    }

    private static float clamp(float v, float minv, float maxv) {
        return java.lang.Math.max(minv, java.lang.Math.min(v, maxv));
    }
}