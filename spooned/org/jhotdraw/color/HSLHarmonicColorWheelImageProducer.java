/**
 *
 * @(#)HSLHarmonicColorWheelImageProducer.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * HSLHarmonicColorWheelImageProducer.
 *
 * @author Werner Randelshofer
 * @version $Id: HSLHarmonicColorWheelImageProducer.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class HSLHarmonicColorWheelImageProducer extends org.jhotdraw.color.PolarColorWheelImageProducer {
    private float[] brights;

    private boolean isDiscrete = true;

    public HSLHarmonicColorWheelImageProducer(int w, int h) {
        super(org.jhotdraw.color.HSLPhysiologicColorSpace.getInstance(), w, h);
    }

    public HSLHarmonicColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h) {
        super(sys, w, h);
    }

    @java.lang.Override
    protected void generateLookupTables() {
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
                float sat = r;
                if (r <= 1.0F) {
                    alphas[index] = 0xff000000;
                    radials[index] = 1.0F;
                    brights[index] = 1.0F - sat;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, r - 1.0F)) * 255) / blend)) << 24;
                    radials[index] = 1.0F;
                    brights[index] = 0;
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
                float sat = r;
                if (r <= 1.0F) {
                    alphas[index] = 0xff000000;
                    radials[index] = 1.0F;
                    brights[index] = ((float) (java.lang.Math.round((1.0F - sat) * 12.0F))) / 12.0F;
                } else {
                    alphas[index] = ((int) (((blend - java.lang.Math.min(blend, r - 1.0F)) * 255) / blend)) << 24;
                    radials[index] = 1.0F;
                    brights[index] = 0.0F;
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
        float brightness = hsb[2];
        float radius = java.lang.Math.min(w, h) / 2.0F;
        brightness = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, brightness));
        java.awt.Point p;
        p = new java.awt.Point((w / 2) + ((int) ((radius - (radius * brightness)) * java.lang.Math.cos((hue * java.lang.Math.PI) * 2.0))), (h / 2) - ((int) ((radius - (radius * brightness)) * java.lang.Math.sin((hue * java.lang.Math.PI) * 2.0))));
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
        float sat = r / radius;
        float hue = ((float) ((theta / java.lang.Math.PI) / 2.0));
        if (hue < 0) {
            hue += 1.0F;
        }
        hsb = new float[]{ hue, 1.0F, 1.0F - sat };
        return hsb;
    }
}