/* @(#)CIELABColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * The 1976 CIE L*a*b* color space (CIELAB).
 *
 * <p>The L* coordinate of an object is the lightness intensity as measured on a scale from 0 to
 * 100, where 0 represents black and 100 represents white.
 *
 * <p>The a* coordinate of an object represents the position of the object’s color on a pure green
 * and pure red scale, where -127 represents pure green and +127 represents pure red.
 *
 * <p>The b* coordinate represents the position of the object’s color on a pure blue and pure yellow
 * scale, where -127 represents pure blue and +127 represents pure yellow.
 *
 * <p>The distance that can be calculated between two colors, is directly proportional to the
 * difference between the two colors as perceived by the human eye.
 *
 * <p>The above description has been derived from <a
 * href="http://www.optelvision.com/documents/optel-vision-s-explanation-on-cielab-color-space.pdf">
 * http://www.optelvision.com/documents/optel-vision-s-explanation-on-cielab-color-space.pdf </a>
 */
public class CIELABColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    /**
     * The XYZ coordinates of the CIE Standard Illuminant D65 reference white.
     */
    private static final double[] D65 = new double[]{ 0.9505, 1.0, 1.089 };

    /**
     * The X coordinate of the reference white.
     */
    private double Xw;

    /**
     * The Y coordinate of the reference white.
     */
    private double Yw;

    /**
     * The Z coordinate of the reference white.
     */
    private double Zw;

    /**
     * Epsilon
     */
    private static final double EPS = 216.0 / 24389.0;

    private static final double K = 24389.0 / 27.0;

    public enum OutsideGamutHandling {

        CLAMP,
        LEAVE_OUTSIDE;
    }

    /**
     * By default, clamps non-displayable RGB values.
     */
    private org.jhotdraw.color.CIELABColorSpace.OutsideGamutHandling outsideGamutHandling = org.jhotdraw.color.CIELABColorSpace.OutsideGamutHandling.CLAMP;

    public CIELABColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_Lab, 3);
        Xw = org.jhotdraw.color.CIELABColorSpace.D65[0];
        Yw = org.jhotdraw.color.CIELABColorSpace.D65[1];
        Zw = org.jhotdraw.color.CIELABColorSpace.D65[2];
    }

    @java.lang.Override
    public float[] toRGB(float[] colorvalue, float[] rgb) {
        float[] ciexyz = rgb;
        toCIEXYZ(colorvalue, ciexyz);
        // Convert to sRGB as described in
        // http://www.w3.org/Graphics/Color/sRGB.html
        double X = ciexyz[0];
        double Y = ciexyz[1];
        double Z = ciexyz[2];
        double Rs = ((3.241 * X) + ((-1.5374) * Y)) + ((-0.4986) * Z);
        double Gs = (((-0.9692) * X) + (1.876 * Y)) + ((-0.0416) * Z);
        double Bs = ((0.0556 * X) + ((-0.204) * Y)) + (1.057 * Z);
        if (Rs <= 0.00304) {
            Rs = 12.92 * Rs;
        } else {
            Rs = (1.055 * java.lang.Math.pow(Rs, 1.0 / 2.4)) - 0.055;
        }
        if (Gs <= 0.00304) {
            Gs = 12.92 * Gs;
        } else {
            Gs = (1.055 * java.lang.Math.pow(Gs, 1.0 / 2.4)) - 0.055;
        }
        if (Bs <= 0.00304) {
            Bs = 12.92 * Bs;
        } else {
            Bs = (1.055 * java.lang.Math.pow(Bs, 1.0 / 2.4)) - 0.055;
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
        return rgb;
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgb, float[] component) {
        org.jhotdraw.color.ColorUtil.RGBtoCIEXYZ(rgb, rgb);
        return fromCIEXYZ(rgb, component);
    }

    /**
     * Lab to XYZ.
     *
     * <pre>
     * X = xr*Xw;
     * Y = yr*Yw;
     * Z = zr*Zw;
     * </pre>
     *
     * where
     *
     * <pre>
     * xr = fx^3, if fx^3 &gt; eps
     *    = (116*fx - 16)/k, if fx^3 &lt;= eps
     *
     * yr = ((L+16)/116)^3, if L &gt; k*eps
     *    = L/k, if L &lt;= k*eps
     *
     * zr = fz^3, if fz^3 &gt; eps
     *    = (116*fz - 16)/k, if fz^3 &lt;= eps
     *
     * fx = a/500+fy
     *
     * fz = fy - b / 200
     *
     * fy = (L+16)/116
     *
     * eps = 216/24389
     * k = 24389/27
     * </pre>
     *
     * Source: <a href="http://www.brucelindbloom.com/index.html?Equations.html"
     * >http://www.brucelindbloom.com/index.html?Equations.html</a>
     *
     * @param colorvalue
     * 		Lab color value.
     * @return CIEXYZ color value.
     */
    @java.lang.Override
    public float[] toCIEXYZ(float[] colorvalue, float[] xyz) {
        double L = colorvalue[0];
        double a = colorvalue[1];
        double b = colorvalue[2];
        double fy = (L + 16.0) / 116.0;
        double fx = (a / 500.0) + fy;
        double fz = fy - (b / 200.0);
        double xr;
        double yr;
        double zr;
        double fxp3 = (fx * fx) * fx;
        if (fxp3 > org.jhotdraw.color.CIELABColorSpace.EPS) {
            xr = fxp3;
        } else {
            xr = ((116.0 * fx) - 16.0) / org.jhotdraw.color.CIELABColorSpace.K;
        }
        if (L > (org.jhotdraw.color.CIELABColorSpace.K * org.jhotdraw.color.CIELABColorSpace.EPS)) {
            yr = (L + 16.0) / 116.0;
            yr = (yr * yr) * yr;
        } else {
            yr = L / org.jhotdraw.color.CIELABColorSpace.K;
        }
        double fzp3 = (fz * fz) * fz;
        if (fzp3 > org.jhotdraw.color.CIELABColorSpace.EPS) {
            zr = fzp3;
        } else {
            zr = ((116.0 * fz) - 16.0F) / org.jhotdraw.color.CIELABColorSpace.K;
        }
        double X = xr * Xw;
        double Y = yr * Yw;
        double Z = zr * Zw;
        xyz[0] = ((float) (X));
        xyz[1] = ((float) (Y));
        xyz[2] = ((float) (Z));
        return xyz;
    }

    /**
     * XYZ to Lab.
     *
     * <pre>
     * L = 116*fy - 16
     * a = 500 * (fx - fy)
     * b = 200 * (fy - fz)
     * </pre>
     *
     * where
     *
     * <pre>
     * fx = xr^(1/3), if xr &gt; eps
     *    = (k*xr + 16) / 116 if xr &lt;= eps
     *
     * fy = yr^(1/3), if yr &gt; eps
     *    = (k*yr + 16) / 116 if yr &lt;= eps
     *
     * fz = zr^(1/3), if zr &gt; eps
     *    = (k*zr + 16) / 116 if zr &lt;= eps
     *
     * xr = X / Xw
     * yr = Y / Yw
     * zr = Z / Zw
     *
     * eps = 216/24389
     * k = 24389/27
     * </pre>
     *
     * Source: <a href="http://www.brucelindbloom.com/index.html?Equations.html"
     * >http://www.brucelindbloom.com/index.html?Equations.html</a>
     *
     * @param colorvalue
     * 		CIEXYZ color value.
     * @return Lab color value.
     */
    @java.lang.Override
    public float[] fromCIEXYZ(float[] colorvalue, float[] xyz) {
        double X = colorvalue[0];
        double Y = colorvalue[1];
        double Z = colorvalue[2];
        double xr = X / Xw;
        double yr = Y / Yw;
        double zr = Z / Zw;
        double fx;
        double fy;
        double fz;
        if (xr > org.jhotdraw.color.CIELABColorSpace.EPS) {
            fx = java.lang.Math.pow(xr, 1.0 / 3.0);
        } else {
            fx = ((org.jhotdraw.color.CIELABColorSpace.K * xr) + 16.0) / 116.0;
        }
        if (yr > org.jhotdraw.color.CIELABColorSpace.EPS) {
            fy = java.lang.Math.pow(yr, 1.0 / 3.0);
        } else {
            fy = ((org.jhotdraw.color.CIELABColorSpace.K * yr) + 16.0) / 116.0;
        }
        if (zr > org.jhotdraw.color.CIELABColorSpace.EPS) {
            fz = java.lang.Math.pow(zr, 1.0 / 3.0);
        } else {
            fz = ((org.jhotdraw.color.CIELABColorSpace.K * zr) + 16) / 116;
        }
        double L = (116.0 * fy) - 16;
        double a = 500.0 * (fx - fy);
        double b = 200.0 * (fy - fz);
        xyz[0] = ((float) (L));
        xyz[1] = ((float) (a));
        xyz[2] = ((float) (b));
        return xyz;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "CIE 1976 L*a*b*";
    }

    @java.lang.Override
    public float getMinValue(int component) {
        switch (component) {
            case 0 :
                return 0.0F;
            case 1 :
            case 2 :
                return -128.0F;
        }
        throw new java.lang.IllegalArgumentException("Illegal component:" + component);
    }

    @java.lang.Override
    public float getMaxValue(int component) {
        switch (component) {
            case 0 :
                return 100.0F;
            case 1 :
            case 2 :
                return 127.0F;
        }
        throw new java.lang.IllegalArgumentException("Illegal component:" + component);
    }

    @java.lang.Override
    public java.lang.String getName(int component) {
        switch (component) {
            case 0 :
                return "L*";
            case 1 :
                return "a*";
            case 2 :
                return "b*";
        }
        throw new java.lang.IllegalArgumentException("Illegal component:" + component);
    }

    public void setOutsideGamutHandling(org.jhotdraw.color.CIELABColorSpace.OutsideGamutHandling b) {
        outsideGamutHandling = b;
    }

    public org.jhotdraw.color.CIELABColorSpace.OutsideGamutHandling getOutsideGamutHandling() {
        return outsideGamutHandling;
    }

    public static void main(java.lang.String[] arg) {
        org.jhotdraw.color.CIELABColorSpace cs = new org.jhotdraw.color.CIELABColorSpace();
        float[] lab = cs.fromRGB(new float[]{ 1, 1, 1 });
        java.lang.System.out.println((((("rgb->lab:" + lab[0]) + ",") + lab[1]) + ",") + lab[2]);
        float[] xyz = cs.toCIEXYZ(new float[]{ 0.75F, 0.25F, 0.1F });
        java.lang.System.out.println((((("    lab->xyz:" + xyz[0]) + ",") + xyz[1]) + ",") + xyz[2]);
        lab = cs.fromCIEXYZ(xyz);
        java.lang.System.out.println((((("R xyz->LCHab:" + lab[0]) + ",") + lab[1]) + ",") + lab[2]);
        lab = cs.fromCIEXYZ(new float[]{ 1, 1, 1 });
        java.lang.System.out.println((((("xyz->lab:" + lab[0]) + ",") + lab[1]) + ",") + lab[2]);
        lab = cs.fromCIEXYZ(new float[]{ 0.5F, 1, 1 });
        java.lang.System.out.println((((("xyz->lab:" + lab[0]) + ",") + lab[1]) + ",") + lab[2]);
    }
}