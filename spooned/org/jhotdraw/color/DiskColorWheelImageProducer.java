/* @(#)DiskColorWheelImageProducer.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.color.ColorSpace;
/**
 * Produces the image of a {@link JColorWheel} by interpreting two components of a {@code ColorSpace} as x,y Cartesian coordinates within a disk boundary.
 *
 * @see JColorWheel
 * @author Werner Randelshofer
 * @version $Id: DiskColorWheelImageProducer.java 527 2009-06-07 14:28:19Z rawcoder $
 */
public class DiskColorWheelImageProducer extends org.jhotdraw.color.AbstractColorWheelImageProducer {
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

    public DiskColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h) {
        this(sys, w, h, false, false);
    }

    public DiskColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h, boolean flipX, boolean flipY) {
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
        float cx = center.x;
        float cy = center.y;
        /* int xOffset = (w - side) / 2;
        int yOffset = (h - side) / 2 * w;
        float extentX = side - 1;
        float extentY = extentX;
         */
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
                float radiusRatio = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky))));
                if (radiusRatio <= 1.0F) {
                    alphas[index] = 0xff000000;
                    // radials[index] = radiusRatio;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, radiusRatio - 1.0F)) * 255) / blend)) << 24;
                    // radials[index] = maxR;
                }
                if (alphas[index] != 0) {
                    // angulars[index] = (float) (Math.atan2(ky, kx));
                }
                double angle = java.lang.Math.atan2(ky, kx);
                // scale from disk to box
                double scale = 1.0 / java.lang.Math.max(java.lang.Math.abs(java.lang.Math.sin(angle)), java.lang.Math.abs(java.lang.Math.cos(angle)));
                scale = 1.0;
                radials[index] = ((float) (((((kx * scale) + 1) / 2) * extentR) + minR));
                angulars[index] = ((float) (((((ky * scale) + 1) / 2) * extentA) + minA));
            }
        }
        isLookupValid = true;
    }

    protected void generateLookupTablesOLD() {
        radials = new float[w * h];
        angulars = new float[w * h];
        alphas = new int[w * h];
        float radius = getRadius();
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

        int cx = side / 2;
        int cy = side / 2;
        int xOffset = (w - side) / 2;
        int yOffset = ((h - side) / 2) * w;
        float extentX = side - 1;
        float extentY = extentX;
        for (int x = 0; x < side; x++) {
            float kx = (x - cx) / radius;
            if (flipX) {
                kx = -kx;
            }
            float squarekx = kx * kx;
            for (int y = 0; y < side; y++) {
                float ky = (cy - y) / radius;
                if (flipY) {
                    ky = -ky;
                }
                int index = ((x + (y * w)) + xOffset) + yOffset;
                float radiusRatio = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky))));
                if (radiusRatio <= 1.0F) {
                    alphas[index] = 0xff000000;
                    // radials[index] = radiusRatio;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, radiusRatio - 1.0F)) * 255) / blend)) << 24;
                    // radials[index] = maxR;
                }
                if (alphas[index] != 0) {
                    // angulars[index] = (float) (Math.atan2(ky, kx));
                }
                float angle = ((float) (java.lang.Math.atan2(ky, kx)));
                float scale = ((float) (java.lang.Math.max(java.lang.Math.abs(java.lang.Math.sin(angle)), java.lang.Math.abs(java.lang.Math.cos(angle))))) + 0.1F;
                radials[index] = ((((kx / scale) + 1.0F) / 2.0F) * extentR) + minR;
                angulars[index] = ((((ky / scale) + 1.0F) / 2.0F) * extentA) + minA;
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
        // clamp to disk
        float r = ((float) (java.lang.Math.sqrt((angular * angular) + (radial * radial))));
        if (r > 1.0F) {
            angular /= r;
            radial /= r;
        }
        java.awt.Point p = new java.awt.Point(((int) ((radius * radial) + center.x)), ((int) ((radius * angular) + center.y)));
        return p;
    }

    @java.lang.Override
    public float[] getColorAt(int x, int y) {
        float radius = getRadius();
        java.awt.geom.Point2D.Float center = getCenter();
        float radial = (x - center.x) / radius;
        float angular = (y - center.y) / radius;
        if (flipX) {
            angular = -angular;
        }
        if (flipY) {
            radial = -radial;
        }
        // clamp to disk
        float r = ((float) (java.lang.Math.sqrt((angular * angular) + (radial * radial))));
        if (r > 1.0F) {
            angular /= r;
            radial /= r;
        }
        float[] hsb = new float[3];
        hsb[angularIndex] = (((angular + 1.0F) / 2.0F) * (colorSpace.getMaxValue(angularIndex) - colorSpace.getMinValue(angularIndex))) + colorSpace.getMinValue(angularIndex);
        hsb[radialIndex] = (((radial + 1.0F) / 2.0F) * (colorSpace.getMaxValue(radialIndex) - colorSpace.getMinValue(radialIndex))) + colorSpace.getMinValue(radialIndex);
        hsb[verticalIndex] = verticalValue;
        return hsb;
    }
}