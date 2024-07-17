/* @(#)CIELCHabColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * The 1976 CIE L*CHa*b* color space (CIELCH).
 *
 * <p>The L* coordinate of an object is the lightness intensity as measured on a scale from 0 to
 * 100, where 0 represents black and 100 represents white.
 *
 * <p>The C and H coordinates are projections of the a* and b* colors of the CIE L*a*b* color space
 * into polar coordinates.
 *
 * <pre>
 * a = C * cos(H)
 * b = C * sin(H)
 * </pre>
 */
public class CIELCHabColorSpace extends org.jhotdraw.color.AbstractNamedColorSpace {
    private static final long serialVersionUID = 1L;

    /**
     * The XYZ coordinates of the CIE Standard Illuminant D65 reference white.
     */
    private static final double[] D65 = new double[]{ 0.9505, 1.0, 1.089 };

    private double Xr;

    /**
     * The Y coordinate of the D50 reference white.
     */
    private double Yr;

    /**
     * The Z coordinate of the D50 reference white.
     */
    private double Zr;

    private static final double EPS = 216.0 / 24389.0;

    private static final double K = 24389.0 / 27.0;

    private static final java.awt.color.ColorSpace SRGB = java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_sRGB);

    /**
     * By default, clamps non-displayable RGB values.
     */
    private boolean isClampRGB = true;

    public CIELCHabColorSpace() {
        super(java.awt.color.ColorSpace.TYPE_Lab, 3);
        Xr = org.jhotdraw.color.CIELCHabColorSpace.D65[0];
        Yr = org.jhotdraw.color.CIELCHabColorSpace.D65[1];
        Zr = org.jhotdraw.color.CIELCHabColorSpace.D65[2];
    }

    @java.lang.Override
    public float[] toRGB(float[] colorvalue, float[] rgb) {
        float[] ciexyz = rgb;// reuse array

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
        if (isClampRGB) {
            Rs = java.lang.Math.min(1, java.lang.Math.max(0, Rs));
            Gs = java.lang.Math.min(1, java.lang.Math.max(0, Gs));
            Bs = java.lang.Math.min(1, java.lang.Math.max(0, Bs));
        }
        rgb[0] = ((float) (Rs));
        rgb[1] = ((float) (Gs));
        rgb[2] = ((float) (Bs));
        return rgb;
        // return sRGB.fromCIEXYZ(ciexyz);
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgb, float[] colorvalue) {
        return fromCIEXYZ(org.jhotdraw.color.ColorUtil.RGBtoCIEXYZ(rgb, colorvalue), colorvalue);
    }

    /**
     * Lab to XYZ.
     *
     * <pre>
     * X = xr*Xr;
     * Y = yr*Yr;
     * Z = zr*Zr;
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
        double C = colorvalue[1];
        double H = (colorvalue[2] / 180) * java.lang.Math.PI;
        double a = C * java.lang.Math.cos(H);
        double b = C * java.lang.Math.sin(H);
        double fy = (L + 16.0) / 116.0;
        double fx = (a / 500.0) + fy;
        double fz = fy - (b / 200.0);
        double xr;
        double yr;
        double zr;
        double fxp3 = (fx * fx) * fx;
        if (fxp3 > org.jhotdraw.color.CIELCHabColorSpace.EPS) {
            xr = fxp3;
        } else {
            xr = ((116.0 * fx) - 16.0) / org.jhotdraw.color.CIELCHabColorSpace.K;
        }
        if (L > (org.jhotdraw.color.CIELCHabColorSpace.K * org.jhotdraw.color.CIELCHabColorSpace.EPS)) {
            yr = (L + 16.0) / 116.0;
            yr = (yr * yr) * yr;
        } else {
            yr = L / org.jhotdraw.color.CIELCHabColorSpace.K;
        }
        double fzp3 = (fz * fz) * fz;
        if (fzp3 > org.jhotdraw.color.CIELCHabColorSpace.EPS) {
            zr = fzp3;
        } else {
            zr = ((116.0 * fz) - 16.0F) / org.jhotdraw.color.CIELCHabColorSpace.K;
        }
        double X = xr * Xr;
        double Y = yr * Yr;
        double Z = zr * Zr;
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
     * xr = X / Xr
     * yr = Y / Yr
     * zr = Z / Zr
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
    public float[] fromCIEXYZ(float[] xyz, float[] colorvalue) {
        double X = xyz[0];
        double Y = xyz[1];
        double Z = xyz[2];
        double xr = X / Xr;
        double yr = Y / Yr;
        double zr = Z / Zr;
        double fx;
        double fy;
        double fz;
        if (xr > org.jhotdraw.color.CIELCHabColorSpace.EPS) {
            fx = java.lang.Math.pow(xr, 1.0 / 3.0);
        } else {
            fx = ((org.jhotdraw.color.CIELCHabColorSpace.K * xr) + 16.0) / 116.0;
        }
        if (yr > org.jhotdraw.color.CIELCHabColorSpace.EPS) {
            fy = java.lang.Math.pow(yr, 1.0 / 3.0);
        } else {
            fy = ((org.jhotdraw.color.CIELCHabColorSpace.K * yr) + 16.0) / 116.0;
        }
        if (zr > org.jhotdraw.color.CIELCHabColorSpace.EPS) {
            fz = java.lang.Math.pow(zr, 1.0 / 3.0);
        } else {
            fz = ((org.jhotdraw.color.CIELCHabColorSpace.K * zr) + 16) / 116;
        }
        double L = (116.0 * fy) - 16;
        double a = 500.0 * (fx - fy);
        double b = 200.0 * (fy - fz);
        double C = java.lang.Math.sqrt((a * a) + (b * b));
        double H = java.lang.Math.atan2(b, a);
        colorvalue[0] = ((float) (L));
        colorvalue[1] = ((float) (C));
        colorvalue[2] = ((float) ((H * 180) / java.lang.Math.PI));
        return colorvalue;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "CIE 1976 L*CHa*b*";
    }

    @java.lang.Override
    public float getMinValue(int component) {
        switch (component) {
            case 0 :
                return 0.0F;
            case 1 :
                return 0.0F;
            case 2 :
                return 0.0F;
        }
        throw new java.lang.IllegalArgumentException("Illegal component:" + component);
    }

    @java.lang.Override
    public float getMaxValue(int component) {
        switch (component) {
            case 0 :
                return 100.0F;
            case 1 :
                return 127.0F;
            case 2 :
                return 360.0F;
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

    public void setClampRGBValues(boolean b) {
        isClampRGB = b;
    }

    public boolean isClampRGBValues() {
        return isClampRGB;
    }

    public static void main(java.lang.String[] arg) {
        org.jhotdraw.color.CIELCHabColorSpace cs = new org.jhotdraw.color.CIELCHabColorSpace();
        float[] lchab = cs.fromRGB(new float[]{ 1, 1, 1 });
        java.lang.System.out.println((((("rgb->LCHab:" + lchab[0]) + ",") + lchab[1]) + ",") + lchab[2]);
        float[] xyz = cs.toCIEXYZ(new float[]{ 0.75F, 0.25F, 0.1F });
        java.lang.System.out.println((((("    lab->xyz:" + xyz[0]) + ",") + xyz[1]) + ",") + xyz[2]);
        lchab = cs.fromCIEXYZ(xyz);
        java.lang.System.out.println((((("R xyz->LCHab:" + lchab[0]) + ",") + lchab[1]) + ",") + lchab[2]);
        lchab = cs.fromCIEXYZ(new float[]{ 1, 1, 1 });
        java.lang.System.out.println((((("xyz->LCHab:" + lchab[0]) + ",") + lchab[1]) + ",") + lchab[2]);
        lchab = cs.fromCIEXYZ(new float[]{ 0.5F, 1, 1 });
        java.lang.System.out.println((((("xyz->LCHab:" + lchab[0]) + ",") + lchab[1]) + ",") + lchab[2]);
    }
}