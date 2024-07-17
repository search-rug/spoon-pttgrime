/**
 *
 * @(#)HarmonicColorWheelImageProducer.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * HarmonicColorWheelImageProducer.
 *
 * @author Werner Randelshofer
 * @version $Id: HSVHarmonicColorWheelImageProducer.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class HSVHarmonicColorWheelImageProducer extends org.jhotdraw.color.PolarColorWheelImageProducer {
    private float wheelScaleFactor;

    private float[] brights;

    private boolean isDiscrete = true;

    public HSVHarmonicColorWheelImageProducer(int w, int h) {
        super(org.jhotdraw.color.HSVPhysiologicColorSpace.getInstance(), w, h);
    }

    @java.lang.Override
    protected void generateLookupTables() {
        wheelScaleFactor = 1.35F;
        isDiscrete = false;
        if (isDiscrete) {
            generateDiscreteLookupTables();
        } else {
            generateContiguousLookupTables();
        }
    }

    protected void generateContiguousLookupTables() {
        radials = new float[w * h];
        angulars = new float[w * h];
        brights = new float[w * h];
        alphas = new int[w * h];
        float radius = getRadius();
        // blend is used to create a linear alpha gradient of two extra pixels
        float blend = ((radius + 2.0F) / radius) - 1.0F;
        // Center of the color wheel circle
        int cx = w / 2;
        int cy = h / 2;
        for (int x = 0; x < w; x++) {
            int kx = x - cx;// Kartesian coordinates of x

            int squarekx = kx * kx;// Square of kartesian x

            for (int y = 0; y < h; y++) {
                int ky = cy - y;// Kartesian coordinates of y

                int index = x + (y * w);
                float r = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky)))) / radius;
                float sat = r * wheelScaleFactor;
                if (r <= 1.0F) {
                    alphas[index] = 0xff000000;
                    // radials[index] = Math.min(1f, sat * 2f);
                    // brights[index] = Math.min(1f, 2f - sat * 2f);
                    radials[index] = java.lang.Math.min(1.0F, sat * 2.0F);
                    brights[index] = java.lang.Math.min(1.0F, 1.5F - sat);
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, r - 1.0F)) * 255) / blend)) << 24;
                    radials[index] = 1.0F;
                    // brights[index] = 0f;
                    brights[index] = java.lang.Math.max(0, java.lang.Math.min(1.0F, 1.5F - sat));
                }
                if (alphas[index] != 0) {
                    angulars[index] = ((float) ((java.lang.Math.atan2(ky, kx) / java.lang.Math.PI) / 2.0));
                }
            }
        }
    }

    protected void generateDiscreteLookupTables() {
        radials = new float[w * h];
        angulars = new float[w * h];
        brights = new float[w * h];
        alphas = new int[w * h];
        float radius = getRadius();
        // blend is used to create a linear alpha gradient of two extra pixels
        float blend = ((radius + 2.0F) / radius) - 1.0F;
        // Center of the color wheel circle
        int cx = w / 2;
        int cy = h / 2;
        for (int x = 0; x < w; x++) {
            int kx = x - cx;// Kartesian coordinates of x

            int squarekx = kx * kx;// Square of kartesian x

            for (int y = 0; y < h; y++) {
                int ky = cy - y;// Kartesian coordinates of y

                int index = x + (y * w);
                float r = ((float) (java.lang.Math.sqrt(squarekx + (ky * ky)))) / radius;
                float sat = r * wheelScaleFactor;
                if (r <= 1.0F) {
                    alphas[index] = 0xff000000;
                    radials[index] = ((float) (java.lang.Math.round(java.lang.Math.min(1.0F, sat * 2.0F) * 5.0F))) / 5.0F;
                    brights[index] = ((float) (java.lang.Math.round(java.lang.Math.min(1.0F, 1.5F - sat) * 10.0F))) / 10.0F;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, r - 1.0F)) * 255) / blend)) << 24;
                    radials[index] = 1.0F;
                    brights[index] = java.lang.Math.max(0, java.lang.Math.round(java.lang.Math.min(1.0F, 1.5F - (sat * 1.0F)) * 10.0F) / 10.0F);
                    // brights[index] = 0f;
                }
                if (alphas[index] != 0) {
                    angulars[index] = java.lang.Math.round(((float) ((java.lang.Math.atan2(ky, kx) / java.lang.Math.PI) / 2.0)) * 12.0F) / 12.0F;
                }
            }
        }
    }

    @java.lang.Override
    public void generateColorWheel() {
        float[] components = new float[3];
        float[] rgb = new float[3];
        for (int index = 0; index < pixels.length; index++) {
            if (alphas[index] != 0) {
                components[0] = angulars[index];
                components[1] = radials[index];
                components[2] = brights[index];
                pixels[index] = alphas[index] | (0xffffff & org.jhotdraw.color.ColorUtil.CStoRGB24(colorSpace, components, rgb));
            }
        }
        newPixels();
        isPixelsValid = false;
    }

    @java.lang.Override
    public java.awt.Point getColorLocation(java.awt.Color c) {
        float[] hsb = org.jhotdraw.color.ColorUtil.fromColor(colorSpace, c);
        return getColorLocation(hsb);
    }

    @java.lang.Override
    public java.awt.Point getColorLocation(float[] hsb) {
        float hue = hsb[0];
        float saturation = hsb[1];
        float brightness = hsb[2];
        float radius = java.lang.Math.min(w, h) / 2.0F;
        float radiusH = radius / 2.0F;
        saturation = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, saturation));
        brightness = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, brightness));
        java.awt.Point p;
        if (brightness == 1.0F) {
            p = new java.awt.Point((w / 2) + ((int) (((radiusH * saturation) * java.lang.Math.cos((hue * java.lang.Math.PI) * 2.0)) / wheelScaleFactor)), (h / 2) - ((int) (((radiusH * saturation) * java.lang.Math.sin((hue * java.lang.Math.PI) * 2.0)) / wheelScaleFactor)));
        } else {
            p = new java.awt.Point((w / 2) + ((int) ((((radius + radiusH) - (radius * brightness)) * java.lang.Math.cos((hue * java.lang.Math.PI) * 2.0)) / wheelScaleFactor)), (h / 2) - ((int) ((((radius + radiusH) - (radius * brightness)) * java.lang.Math.sin((hue * java.lang.Math.PI) * 2.0)) / wheelScaleFactor)));
        }
        return p;
    }

    @java.lang.Override
    public float[] getColorAt(int x, int y) {
        x -= w / 2;
        y -= h / 2;
        float r = ((float) (java.lang.Math.sqrt((x * x) + (y * y))));
        float theta = ((float) (java.lang.Math.atan2(-y, x)));
        float radius = java.lang.Math.min(w, h) / 2.0F;
        float[] hsb;
        float sat = (r / radius) * wheelScaleFactor;
        float hue = ((float) ((theta / java.lang.Math.PI) / 2.0));
        if (hue < 0) {
            hue += 1.0F;
        }
        hsb = new float[]{ hue, java.lang.Math.min(1.0F, sat * 2.0F), // Math.min(1f, 2f - sat * 2f)
        java.lang.Math.min(1.0F, 1.5F - sat) };
        return hsb;
    }
}