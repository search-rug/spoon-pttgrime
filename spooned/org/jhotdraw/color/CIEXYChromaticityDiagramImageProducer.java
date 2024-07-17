/* @(#)CIEXYChromaticityDiagramImageProducer.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * Produces a CIE xy Chromaticity Diagram.
 *
 * <p>The diagram shows a projection of the CIE XYZ cube on a xy plane. The projection is based on
 * the following equations:
 *
 * <p>x = X / (X + Y + Z), y = Y / (X + Y + Z), z = 1 - x - y. The equations can be rewritten as:
 *
 * <p>X = (x*(Y+Z)/(1-x), Y = (y*(X+Z)/(1-y).
 */
public class CIEXYChromaticityDiagramImageProducer extends java.awt.image.MemoryImageSource {
    private static final float EPS = 0.0F;// 0.000001f;


    private static final float CEPS = 0.0F;

    protected int[] pixels;

    protected int w;

    protected int h;

    protected java.awt.color.ColorSpace colorSpace;

    protected int radialIndex = 1;

    protected int angularIndex = 0;

    protected int verticalIndex = 2;

    protected boolean isPixelsValid = false;

    protected float verticalValue = 1.0F;

    protected boolean isLookupValid = false;

    public enum OutsideGamutHandling {

        CLAMP,
        LEAVE_OUTSIDE;
    }

    /**
     * By default, clamps non-displayable RGB values.
     */
    private org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.OutsideGamutHandling outsideGamutHandling = org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.OutsideGamutHandling.LEAVE_OUTSIDE;

    public CIEXYChromaticityDiagramImageProducer(int w, int h) {
        super(w, h, null, 0, w);
        this.colorSpace = java.awt.color.ICC_ColorSpace.getInstance(java.awt.color.ICC_ColorSpace.CS_CIEXYZ);
        pixels = new int[w * h];
        this.w = w;
        this.h = h;
        setAnimated(true);
        newPixels(pixels, java.awt.image.ColorModel.getRGBdefault(), 0, w);
    }

    public boolean needsGeneration() {
        return !isPixelsValid;
    }

    public void regenerateDiagram() {
        if (!isPixelsValid) {
            generateImage();
        }
    }

    public void generateImage() {
        float wf = 0.8F / ((float) (w));
        float hf = 0.9F / ((float) (h));
        // float wf = 1f / (float) w;
        // float hf = 1f / (float) h;
        // Clear pixels
        java.util.Arrays.fill(pixels, 0);
        float[] rgb = new float[3];
        for (int iY = 0; iY <= 100; iY++) {
            float Y = (100 - iY) / 100.0F;
            float[] XYZ = new float[3];
            for (int ix = 0; ix < w; ix++) {
                float x = ix * wf;
                for (int iy = 0; iy < h; iy++) {
                    if (pixels[ix + (iy * w)] != 0) {
                        continue;
                    }
                    float y = 0.9F - (iy * hf);
                    float z = (1.0F - x) - y;
                    if (y == 0) {
                        XYZ[0] = XYZ[1] = XYZ[2] = 0;
                    } else {
                        XYZ[1] = Y;// Y=Y

                        XYZ[0] = (x * XYZ[1]) / y;// X=x*Y/y

                        XYZ[2] = (z * XYZ[1]) / y;// Z = (1-x-y)*Y/y

                    }
                    int alpha = ((((((XYZ[0] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS) && (XYZ[1] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS)) && (XYZ[2] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS)) && (XYZ[0] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS))) && (XYZ[1] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS))) && (XYZ[2] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.CEPS))) ? 255 : 0;
                    if (alpha == 255) {
                        // rgb = colorSpace.toRGB(XYZ);
                        // toRGB(XYZ,rgb);
                        toRGB(XYZ, rgb);
                        alpha = ((((((rgb[0] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS) && (rgb[1] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS)) && (rgb[2] >= org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS)) && (rgb[0] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS))) && (rgb[1] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS))) && (rgb[2] <= (1 - org.jhotdraw.color.CIEXYChromaticityDiagramImageProducer.EPS))) ? 255 : 0;
                        if (alpha == 255) {
                            // rgb = colorSpace.toRGB(XYZ);
                            // pixels[ix + iy * w] = (alpha << 24) | ((int) (rgb[0] *
                            // 255f) << 16) | ((int) (rgb[1] * 255f) << 8) | (int) (rgb[2] * 255f);
                            pixels[ix + (iy * w)] = (((alpha << 24) | ((0xff & ((int) (rgb[0] * 255.0F))) << 16)) | ((0xff & ((int) (rgb[1] * 255.0F))) << 8)) | (0xff & ((int) (rgb[2] * 255.0F)));
                        }
                    }
                }
            }
        }
    }

    public java.awt.Point getColorLocation(java.awt.Color c) {
        float[] components = org.jhotdraw.color.ColorUtil.fromColor(colorSpace, c);
        return getColorLocation(components);
    }

    public java.awt.Point getColorLocation(float[] components) {
        return null;
    }

    public float[] getColorAt(int x, int y) {
        return null;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public void toRGB(float[] ciexyz, float[] rgb) {
        double X = ciexyz[0];
        double Y = ciexyz[1];
        double Z = ciexyz[2];
        // sRGB conversion
        // Convert to sRGB as described in
        // http://www.w3.org/Graphics/Color/sRGB.html
        /* double Rs = 3.2410 * X + -1.5374 * Y + -0.4986 * Z;
        double Gs = -0.9692 * X + 1.8760 * Y + 0.0416 * Z;
        double Bs = 0.0556 * X + -0.2040 * Y + 1.0570 * Z;
        /* /
        // Convert to sRGB as described in
        // http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html
        double Rs = 3.2404542 * X + -1.5371385 * Y + -0.4985314 * Z;
        double Gs = -0.9692660 * X + 1.8760108 * Y + 0.0415560 * Z;
        double Bs = 0.0556434 * X + -0.2040259 * Y + 1.0572252 * Z;
        /* /
        // proPhoto RGB conversion http://www.colour.org/tc8-05/Docs/colorspace/PICS2000_RIMM-ROMM.pdf
        double Rs = 1.3460 * X + -0.2556 * Y + -0.0511 * Z;
        double Gs = -0.5446 * X + 1.5082 * Y + 0.0205 * Z;
        double Bs = 0.0 * X + 0.0 * Y + 1.2123 * Z;
        /
        // One to one 'conversion'
        double Rs = 1 * X + 0 * Y + 0 * Z;
        double Gs = 0 * X + 1 * Y + 0 * Z;
        double Bs = 0 * X + 0 * Y + 1 * Z;
         */
        // Convert to Wide Gamut RGB as described in
        // http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html
        double Rs = ((1.4628067 * X) + ((-0.1840623) * Y)) + ((-0.2743606) * Z);
        double Gs = (((-0.5217933) * X) + (1.4472381 * Y)) + (0.0677227 * Z);
        double Bs = ((0.0349342 * X) + ((-0.096893) * Y)) + (1.2884099 * Z);
        if (Rs <= 0.00304) {
            Rs = 12.92 * Rs;
        } else {
            Rs = (1.055 * java.lang.Math.pow(Rs, 1 / 2.4)) - 0.055;
        }
        if (Gs <= 0.00304) {
            Gs = 12.92 * Gs;
        } else {
            Gs = (1.055 * java.lang.Math.pow(Gs, 1 / 2.4)) - 0.055;
        }
        if (Bs <= 0.00304) {
            Bs = 12.92 * Bs;
        } else {
            Bs = (1.055 * java.lang.Math.pow(Bs, 1 / 2.4)) - 0.055;
        }
        switch (outsideGamutHandling) {
            case CLAMP :
                Rs = java.lang.Math.min(1, java.lang.Math.max(0, Rs));
                Gs = java.lang.Math.min(1, java.lang.Math.max(0, Gs));
                Bs = java.lang.Math.min(1, java.lang.Math.max(0, Bs));
                break;
        }
        rgb[0] = ((float) (Rs));
        rgb[1] = ((float) (Gs));
        rgb[2] = ((float) (Bs));
        // return new float[]{(float) Rs, (float) Gs, (float) Bs};
        // return sRGB.fromCIEXYZ(ciexyz);
    }
}