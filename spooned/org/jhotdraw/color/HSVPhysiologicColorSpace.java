/* @(#)HSVPhysiologicColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A HSV color space with physiologic opposites in the hue color wheel: red is opposite green and
 * yellow is opposite blue.
 */
public class HSVPhysiologicColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.HSVPhysiologicColorSpace instance;

    public static org.jhotdraw.color.HSVPhysiologicColorSpace getInstance() {
        if (org.jhotdraw.color.HSVPhysiologicColorSpace.instance == null) {
            org.jhotdraw.color.HSVPhysiologicColorSpace.instance = new org.jhotdraw.color.HSVPhysiologicColorSpace();
        }
        return org.jhotdraw.color.HSVPhysiologicColorSpace.instance;
    }

    public HSVPhysiologicColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_HSV, 3);
    }

    @java.lang.Override
    public float[] toRGB(float[] components, float[] rgb) {
        float hue = components[0];
        float saturation = components[1];
        float value = components[2];
        // normalize hue
        hue = hue - ((float) (java.lang.Math.floor(hue)));
        if (hue < 0) {
            hue -= 1.0F;
        }
        // normalize saturation
        if (saturation > 1.0F) {
            saturation = 1.0F;
        } else if (saturation < 0.0F) {
            saturation = 0.0F;
        }
        // normalize value
        if (value > 1.0F) {
            value = 1.0F;
        } else if (value < 0.0F) {
            value = 0.0F;
        }
        // compute hi and f from hue
        int hi;
        float f;
        float hueDeg = hue * 360.0F;
        if (hueDeg < 120.0F) {
            // red to yellow
            hi = 0;
            f = hueDeg / 120.0F;
        } else if (hueDeg < 160.0F) {
            // yellow to green
            hi = 1;
            f = (hueDeg - 120.0F) / 40.0F;
        } else if (hueDeg < 220.0F) {
            // green to cyan
            hi = 2;
            f = (hueDeg - 160.0F) / 60.0F;
        } else if (hueDeg < 280.0F) {
            // cyan to blue
            hi = 3;
            f = (hueDeg - 220.0F) / 60.0F;
        } else if (hueDeg < 340.0F) {
            // blue to purple
            hi = 4;
            f = (hueDeg - 280.0F) / 60.0F;
        } else {
            // purple to red
            f = (hueDeg - 340.0F) / 20.0F;
            hi = 5;
        }
        // compute p, q, t from saturation
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
            // red to yellow
            hue = (120.0F * (g - b)) / (max - min);
        } else if (max == r) {
            // red to purple
            hue = ((20.0F * (g - b)) / (max - min)) + 360.0F;
        } else if ((max == g) && (r >= b)) {
            // yellow to green
            hue = (((40.0F * (b - r)) / (max - min)) + 120.0F) + 40.0F;
        } else if (max == g) {
            // green to cyan
            hue = (((60.0F * (b - r)) / (max - min)) + 120.0F) + 40.0F;
        } else if (g >= r) {
            // cyan to blue
            hue = (((60.0F * (r - g)) / (max - min)) + 240.0F) + 40.0F;
        } else {
            // blue to purple
            hue = (((60.0F * (r - g)) / (max - min)) + 240.0F) + 40.0F;
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
        return o instanceof org.jhotdraw.color.HSVPhysiologicColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "physiologic HSV";
    }
}