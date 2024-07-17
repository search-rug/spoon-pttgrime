/* @(#)HSLPhysiologicColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A HSL color space with physiologic opposites in the hue color wheel: red is opposite green and
 * yellow is opposite blue.
 *
 * @author Werner Randelshofer
 * @version $Id: HSLPhysiologicColorSpace.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class HSLPhysiologicColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.HSLPhysiologicColorSpace instance;

    public static org.jhotdraw.color.HSLPhysiologicColorSpace getInstance() {
        if (org.jhotdraw.color.HSLPhysiologicColorSpace.instance == null) {
            org.jhotdraw.color.HSLPhysiologicColorSpace.instance = new org.jhotdraw.color.HSLPhysiologicColorSpace();
        }
        return org.jhotdraw.color.HSLPhysiologicColorSpace.instance;
    }

    public HSLPhysiologicColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_HSV, 3);
    }

    @java.lang.Override
    public float[] toRGB(float[] components, float[] rgb) {
        float hue = components[0];
        float saturation = components[1];
        float lightness = components[2];
        // normalize hue
        hue = hue - ((float) (java.lang.Math.floor(hue)));
        if (hue < 0) {
            hue = 1.0F + hue;
        }
        // normalize saturation
        if (saturation > 1.0F) {
            saturation = 1.0F;
        } else if (saturation < 0.0F) {
            saturation = 0.0F;
        }
        // normalize value
        if (lightness > 1.0F) {
            lightness = 1.0F;
        } else if (lightness < 0.0F) {
            lightness = 0.0F;
        }
        float hueDeg = hue * 360.0F;
        if (hueDeg < 0) {
            hueDeg += 360.0F;
        }
        // compute hi and f from hue
        // float f;
        float hk = hue - ((float) (java.lang.Math.floor(hue)));// / 360f;

        if (hueDeg < 120.0F) {
            // red to yellow
            hk /= 2.0F;
        } else if (hueDeg < 160.0F) {
            // yellow to green
            hk = (((hk - (120.0F / 360.0F)) * 3.0F) / 2.0F) + (60.0F / 360.0F);
        } else if (hueDeg < 220.0F) {
            // green to cyan
            hk = (hk - (160.0F / 360.0F)) + (120.0F / 360.0F);
        } else if (hueDeg < 280.0F) {
            // cyan to blue
            hk = (hk - (220.0F / 360.0F)) + (180.0F / 360.0F);
        } else if (hueDeg < 340.0F) {
            // blue to purple
            hk = (hk - (280.0F / 360.0F)) + (240.0F / 360.0F);
        } else {
            // purple to red
            hk = ((hk - (340.0F / 360.0F)) * 3.0F) + (300.0F / 360.0F);
        }
        // compute p and q from saturation and lightness
        float q;
        if (lightness < 0.5F) {
            q = lightness * (1.0F + saturation);
        } else {
            q = (lightness + saturation) - (lightness * saturation);
        }
        float p = (2.0F * lightness) - q;
        // compute red, green and blue
        float red = hk + (1.0F / 3.0F);
        float green = hk;
        float blue = hk - (1.0F / 3.0F);
        if (red < 0) {
            red = red + 1.0F;
        }
        if (green < 0) {
            green = green + 1.0F;
        }
        if (blue < 0) {
            blue = blue + 1.0F;
        }
        if (red > 1) {
            red = red - 1.0F;
        }
        if (green > 1) {
            green = green - 1.0F;
        }
        if (blue > 1) {
            blue = blue - 1.0F;
        }
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
        float luminance;
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
        return o instanceof org.jhotdraw.color.HSLPhysiologicColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "physiologic HSL";
    }
}