/* @(#)HSBColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.color.ColorSpace;
/**
 * A {@code ColorSpace} for CMYK color components (cyan, magenta, yellow, black) with nominally
 * converted color components from/to an RGB color model.
 *
 * <p>This model may not be very useful. It assumes that the color components perfectly absorb the
 * desired wavelenghts.
 */
public class CMYKNominalColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.CMYKNominalColorSpace instance;

    public static org.jhotdraw.color.CMYKNominalColorSpace getInstance() {
        if (org.jhotdraw.color.CMYKNominalColorSpace.instance == null) {
            org.jhotdraw.color.CMYKNominalColorSpace.instance = new org.jhotdraw.color.CMYKNominalColorSpace();
        }
        return org.jhotdraw.color.CMYKNominalColorSpace.instance;
    }

    public CMYKNominalColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_CMYK, 4);
    }

    @java.lang.Override
    public float[] toRGB(float[] component, float[] rgb) {
        float cyan;
        float magenta;
        float yellow;
        float black;
        cyan = component[0];
        magenta = component[1];
        yellow = component[2];
        black = component[3];
        float red;
        float green;
        float blue;
        red = (1.0F - (cyan * (1.0F - black))) - black;
        green = (1.0F - (magenta * (1.0F - black))) - black;
        blue = (1.0F - (yellow * (1.0F - black))) - black;
        // clamp values
        red = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, red));
        green = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, green));
        blue = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, blue));
        rgb[0] = red;
        rgb[1] = green;
        rgb[2] = blue;
        return rgb;
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgbvalue, float[] colorvalue) {
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];
        float cyan;
        float magenta;
        float yellow;
        float black;
        cyan = 1.0F - r;
        magenta = 1.0F - g;
        yellow = 1.0F - b;
        if (java.lang.Math.min(java.lang.Math.min(cyan, magenta), yellow) >= 1.0F) {
            cyan = magenta = yellow = 0.0F;
            black = 1.0F;
        } else {
            black = java.lang.Math.min(java.lang.Math.min(cyan, magenta), yellow);
            if (black > 0.0F) {
                cyan = (cyan - black) / (1 - black);
                magenta = (magenta - black) / (1 - black);
                yellow = (yellow - black) / (1 - black);
            }
        }
        // clamp values
        cyan = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, cyan));
        yellow = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, yellow));
        magenta = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, magenta));
        black = java.lang.Math.min(1.0F, java.lang.Math.max(0.0F, black));
        colorvalue[0] = cyan;
        colorvalue[1] = magenta;
        colorvalue[2] = yellow;
        colorvalue[3] = black;
        return colorvalue;
    }

    @java.lang.Override
    public java.lang.String getName(int idx) {
        switch (idx) {
            case 0 :
                return "Cyan";
            case 1 :
                return "Magenta";
            case 2 :
                return "Yellow";
            case 3 :
                return "Black";
            default :
                throw new java.lang.IllegalArgumentException("index must be between 0 and 3:" + idx);
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
        return o instanceof org.jhotdraw.color.CMYKNominalColorSpace;
    }

    @java.lang.Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "nominal CMYK";
    }
}