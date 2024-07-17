/* @(#)PolarColorWheelImageProducer.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.color.ColorSpace;
/**
 * Produces the image of a {@link JColorWheel} by interpreting two components of a {@code ColorSpace} as polar coordinates (angle and radius).
 */
public class PolarColorWheelImageProducer extends org.jhotdraw.color.AbstractColorWheelImageProducer {
    /**
     * Lookup table for angular component values.
     */
    protected float[] angulars;

    /**
     * Lookup table for radial component values.
     */
    protected float[] radials;

    /**
     * Lookup table for alphas. The alpha value is used for antialiasing the color wheel.
     */
    protected int[] alphas;

    public PolarColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h) {
        super(sys, w, h);
    }

    protected void generateLookupTables() {
        radials = new float[w * h];
        angulars = new float[w * h];
        alphas = new int[w * h];
        float radius = getRadius();
        // blend is used to create a linear alpha gradient of two extra pixels
        float blend = ((radius + 2.0F) / radius) - 1.0F;
        // Center of the color wheel circle
        int cx = w / 2;
        int cy = h / 2;
        float maxR = colorSpace.getMaxValue(radialIndex);
        float minR = colorSpace.getMinValue(radialIndex);
        float extentR = maxR - minR;
        float maxA = colorSpace.getMaxValue(angularIndex);
        float minA = colorSpace.getMinValue(angularIndex);
        float extentA = maxA - minA;
        for (int x = 0; x < w; x++) {
            int kx = x - cx;// Kartesian coordinates of x

            int squarekx = kx * kx;// Square of kartesian x

            for (int y = 0; y < h; y++) {
                int ky = cy - y;// Kartesian coordinates of y

                int index = x + (y * w);
                float radiusRatio = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky)) / radius));
                if (radiusRatio <= 1.0F) {
                    alphas[index] = 0xff000000;
                    radials[index] = (radiusRatio * extentR) + minR;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, radiusRatio - 1.0F)) * 255) / blend)) << 24;
                    radials[index] = maxR;
                }
                if (alphas[index] != 0) {
                    angulars[index] = (((float) ((java.lang.Math.atan2(ky, kx) / java.lang.Math.PI) / 2.0)) * extentA) + minA;
                }
            }
        }
        isLookupValid = true;
    }

    @java.lang.Override
    public void generateColorWheel() {
        if (!isLookupValid) {
            generateLookupTables();
        }
        float[] components = new float[colorSpace.getNumComponents()];
        float[] rgb = new float[3];
        for (int index = 0; index < pixels.length; index++) {
            if (alphas[index] != 0) {
                components[angularIndex] = angulars[index];
                components[radialIndex] = radials[index];
                components[verticalIndex] = verticalValue;
                pixels[index] = alphas[index] | (0xffffff & org.jhotdraw.color.ColorUtil.CStoRGB24(colorSpace, components, rgb));
            }
        }
        newPixels();
        isPixelsValid = true;
    }

    @java.lang.Override
    public java.awt.Point getColorLocation(java.awt.Color c) {
        float[] hsb = org.jhotdraw.color.ColorUtil.fromColor(colorSpace, c);
        return getColorLocation(hsb);
    }

    @java.lang.Override
    public java.awt.Point getColorLocation(float[] components) {
        float radial = (components[radialIndex] - colorSpace.getMinValue(radialIndex)) / (colorSpace.getMaxValue(radialIndex) - colorSpace.getMinValue(radialIndex));
        float angular = (components[angularIndex] - colorSpace.getMinValue(angularIndex)) / (colorSpace.getMaxValue(angularIndex) - colorSpace.getMinValue(angularIndex));
        float radius = java.lang.Math.min(w, h) / 2.0F;
        radial = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, radial));
        java.awt.Point p = new java.awt.Point((w / 2) + ((int) ((radius * radial) * java.lang.Math.cos((angular * java.lang.Math.PI) * 2.0))), (h / 2) - ((int) ((radius * radial) * java.lang.Math.sin((angular * java.lang.Math.PI) * 2.0))));
        return p;
    }

    @java.lang.Override
    public float[] getColorAt(int x, int y) {
        x -= w / 2;
        y -= h / 2;
        float r = ((float) (java.lang.Math.sqrt((x * x) + (y * y))));
        float theta = ((float) (java.lang.Math.atan2(y, -x)));
        float angular = ((float) (0.5 + ((theta / java.lang.Math.PI) / 2.0)));
        float radial = java.lang.Math.min(1.0F, r / getRadius());
        float[] hsb = new float[3];
        hsb[angularIndex] = (angular * (colorSpace.getMaxValue(angularIndex) - colorSpace.getMinValue(angularIndex))) + colorSpace.getMinValue(angularIndex);
        hsb[radialIndex] = (radial * (colorSpace.getMaxValue(radialIndex) - colorSpace.getMinValue(radialIndex))) + colorSpace.getMinValue(radialIndex);
        hsb[verticalIndex] = verticalValue;
        return hsb;
    }
}