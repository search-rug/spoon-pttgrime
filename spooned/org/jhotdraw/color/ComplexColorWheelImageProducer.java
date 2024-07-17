/* @(#)ColorWheelImageProducer.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.color.ColorSpace;
/**
 * Produces the image of a {@link JColorWheel} by interpreting two components of a {@code ColorSpace} as complex numbers (real and imaginary).
 *
 * @see JColorWheel
 * @author Werner Randelshofer
 * @version $Id: ColorWheelImageProducer.java 527 2009-06-07 14:28:19Z rawcoder $
 */
public class ComplexColorWheelImageProducer extends org.jhotdraw.color.AbstractColorWheelImageProducer {
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

    private boolean flipX;

    private boolean flipY;

    public ComplexColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h) {
        this(sys, w, h, false, false);
    }

    public ComplexColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h, boolean flipX, boolean flipY) {
        super(sys, w, h);
        this.flipX = flipX;
        this.flipY = flipY;
    }

    protected void generateLookupTables() {
        radials = new float[w * h];
        angulars = new float[w * h];
        alphas = new int[w * h];
        float radius = getRadius();
        java.awt.geom.Point2D.Float center = getCenter();
        // blend is used to create a linear alpha gradient of two extra pixels
        float blend = ((radius + 2.0F) / radius) - 1.0F;
        // Center of the color wheel circle
        float maxR = colorSpace.getMaxValue(radialIndex);
        float minR = colorSpace.getMinValue(radialIndex);
        float extentR = maxR - minR;
        float maxA = colorSpace.getMaxValue(angularIndex);
        float minA = colorSpace.getMinValue(angularIndex);
        float extentA = maxA - minA;
        int side = java.lang.Math.min(w, h);// side length

        float cx = center.x;
        float cy = center.y;
        float extentX = side - 1;
        float extentY = extentX;
        for (int x = 0; x < w; x++) {
            float kx = (x - cx) / radius;
            if (flipX) {
                kx = -kx;
            }
            float squarekx = kx * kx;
            for (int y = 0; y < h; y++) {
                float ky = (y - cy) / radius;
                if (flipY) {
                    ky = -ky;
                }
                int index = x + (y * w);
                float r = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky))));
                if (r <= 1.0F) {
                    alphas[index] = 0xff000000;
                    // radials[index] = radiusRatio;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, r - 1.0F)) * 255) / blend)) << 24;
                    // radials[index] = maxR;
                }
                if (alphas[index] != 0) {
                    // angulars[index] = (float) (Math.atan2(ky, kx));
                }
                double angle = java.lang.Math.atan2(ky, kx);
                // distort from disk to box
                float scale = ((float) (java.lang.Math.max(java.lang.Math.abs(java.lang.Math.sin(angle)), java.lang.Math.abs(java.lang.Math.cos(angle)))));
                // we don't want too much distortion at the center of the disk
                scale = (1 - r) + (scale * r);
                // perform distortion
                radials[index] = java.lang.Math.max(minR, java.lang.Math.min(((((kx / scale) + 1) / 2) * extentR) + minR, maxR));
                angulars[index] = java.lang.Math.max(minA, java.lang.Math.min(((((ky / scale) + 1) / 2) * extentA) + minA, maxA));
            }
        }
        isLookupValid = true;
    }

    @java.lang.Override
    public boolean needsGeneration() {
        return !isPixelsValid;
    }

    @java.lang.Override
    public void regenerateColorWheel() {
        if (!isPixelsValid) {
            generateColorWheel();
        }
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
    public java.awt.Point getColorLocation(float[] components) {
        float radius = getRadius();
        java.awt.geom.Point2D.Float center = getCenter();
        float radial = (((components[radialIndex] - colorSpace.getMinValue(radialIndex)) / (colorSpace.getMaxValue(radialIndex) - colorSpace.getMinValue(radialIndex))) * 2) - 1;
        float angular = (((components[angularIndex] - colorSpace.getMinValue(angularIndex)) / (colorSpace.getMaxValue(angularIndex) - colorSpace.getMinValue(angularIndex))) * 2) - 1;
        if (flipX) {
            radial = -radial;
        }
        if (flipY) {
            angular = -angular;
        }
        radial = java.lang.Math.max(-1, java.lang.Math.min(radial, 1));
        angular = java.lang.Math.max(-1, java.lang.Math.min(angular, 1));
        double a = java.lang.Math.atan2(radial, angular);
        double sina = java.lang.Math.sin(a);
        double cosa = java.lang.Math.cos(a);
        double d = java.lang.Math.max(java.lang.Math.abs(sina), java.lang.Math.abs(cosa));
        double dx = (java.lang.Math.abs(sina) > java.lang.Math.abs(cosa)) ? sina : cosa;
        double bx = (java.lang.Math.abs(sina) > java.lang.Math.abs(cosa)) ? radial : angular;
        double r;
        if ((d == 1) && (dx != 0)) {
            r = bx / dx;
        } else {
            r = ((d * dx) - java.lang.Math.sqrt((d * dx) * (((((-4) * bx) * d) + (4 * bx)) + (d * dx)))) / ((2 * (d - 1)) * dx);
            if (r < 0) {
                r = ((d * dx) + java.lang.Math.sqrt((d * dx) * (((((-4) * bx) * d) + (4 * bx)) + (d * dx)))) / ((2 * (d - 1)) * dx);
            }
        }
        java.awt.Point p = new java.awt.Point(((int) (((r * sina) * radius) + center.x)), ((int) (((r * cosa) * radius) + center.y)));
        return p;
    }

    @java.lang.Override
    public float[] getColorAt(int x, int y) {
        float radius = getRadius();
        java.awt.geom.Point2D.Float center = getCenter();
        float maxR = colorSpace.getMaxValue(radialIndex);
        float minR = colorSpace.getMinValue(radialIndex);
        float extentR = maxR - minR;
        float maxA = colorSpace.getMaxValue(angularIndex);
        float minA = colorSpace.getMinValue(angularIndex);
        float extentA = maxA - minA;
        int side = java.lang.Math.min(w, h);// side length

        float cx = center.x;
        float cy = center.y;
        float extentX = side - 1;
        float extentY = extentX;
        float radial;
        float angular;
        float kx = (x - cx) / radius;
        if (flipX) {
            kx = -kx;
        }
        float squarekx = kx * kx;
        float ky = (y - cy) / radius;
        if (flipY) {
            ky = -ky;
        }
        int index = x + (y * w);
        float r = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky))));
        double angle = java.lang.Math.atan2(ky, kx);
        // distort from disk to box
        float scale = ((float) (java.lang.Math.max(java.lang.Math.abs(java.lang.Math.sin(angle)), java.lang.Math.abs(java.lang.Math.cos(angle)))));
        // we don't want too much distortion at the center of the disk
        scale = (1 - r) + (scale * r);
        // perform distortion
        radial = ((((kx / scale) + 1) / 2) * extentR) + minR;
        angular = ((((ky / scale) + 1) / 2) * extentA) + minA;
        float[] rav = new float[3];
        rav[angularIndex] = angular;
        rav[radialIndex] = radial;
        rav[verticalIndex] = verticalValue;
        return rav;
    }

    public float[] getColorAtOld(int x, int y) {
        int side = java.lang.Math.min(w - 1, h - 1);// side length

        int xOffset = (w - side) / 2;
        int yOffset = (h - side) / 2;
        float radial = (x - xOffset) / ((float) (side));
        float angular = (y - yOffset) / ((float) (side));
        if (flipX) {
            radial = 1.0F - radial;
        }
        if (!flipY) {
            angular = 1.0F - angular;
        }
        float[] rav = new float[3];
        rav[angularIndex] = (angular * (colorSpace.getMaxValue(angularIndex) - colorSpace.getMinValue(angularIndex))) + colorSpace.getMinValue(angularIndex);
        rav[radialIndex] = (radial * (colorSpace.getMaxValue(radialIndex) - colorSpace.getMinValue(radialIndex))) + colorSpace.getMinValue(radialIndex);
        rav[verticalIndex] = verticalValue;
        int xy = x + (y * w);
        java.lang.System.out.println((((((("ComplexColorWheelImageProducer.getColorAt " + rav[angularIndex]) + ",") + rav[radialIndex]) + " ~ ") + angulars[xy]) + ",") + radials[xy]);
        rav[angularIndex] = angulars[xy];
        rav[radialIndex] = radials[xy];
        java.awt.Point p = getColorLocation(rav);
        java.lang.System.out.println((((((("ComplexColorWheelImageProducer.getColorAt( " + x) + ",") + y) + " => ") + p.x) + ",") + p.y);
        return rav;
    }
}