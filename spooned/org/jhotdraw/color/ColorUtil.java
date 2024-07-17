/* @(#)ColorUtil.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
/**
 * A utility class for {@code Color} and {@code ColorSpace} objects.
 */
public class ColorUtil {
    private static org.jhotdraw.text.ColorToolTipTextFormatter formatter;

    private static final java.awt.color.ColorSpace SRGB = java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_sRGB);

    /**
     * Prevent instance creation.
     */
    private ColorUtil() {
    }

    /**
     * Returns the color components in the specified color space from a {@code Color} object.
     */
    public static float[] fromColor(java.awt.color.ColorSpace colorSpace, java.awt.Color c) {
        if (org.jhotdraw.color.ColorUtil.isEqual(c.getColorSpace(), colorSpace)) {
            float[] components = c.getComponents(null);
            return components;
        } else {
            return c.getComponents(colorSpace, null);
        }
    }

    /**
     * Returns a color object from color components in the specified color space.
     */
    public static java.awt.Color toColor(java.awt.color.ColorSpace colorSpace, float... components) {
        return new org.jhotdraw.color.CompositeColor(colorSpace, components, 1.0F);
    }

    /**
     * Returns the color components in the specified color space from an rgb value.
     */
    public static float[] fromRGB(java.awt.color.ColorSpace colorSpace, int rgb) {
        return org.jhotdraw.color.ColorUtil.fromRGB(colorSpace, (rgb >>> 16) & 0xff, (rgb >>> 8) & 0xff, rgb & 0xff);
    }

    /**
     * Returns the color components in the specified color space from RGB values.
     */
    public static float[] fromRGB(java.awt.color.ColorSpace colorSpace, int r, int g, int b) {
        return colorSpace.fromRGB(new float[]{ r / 255.0F, g / 255.0F, b / 255.0F });
    }

    /**
     * Returns an rgb value from color components in the specified color space.
     */
    public static int toRGB24(java.awt.color.ColorSpace colorSpace, float... components) {
        return org.jhotdraw.color.ColorUtil.CStoRGB24(colorSpace, components, new float[3]);
    }

    public static int CStoRGB24(java.awt.color.ColorSpace colorSpace, float[] components, float[] rgb) {
        org.jhotdraw.color.ColorUtil.CStoRGB(colorSpace, components, rgb);
        // If the color is not displayable in RGB, we return transparent black.
        if ((((((rgb[0] < 0.0F) || (rgb[1] < 0.0F)) || (rgb[2] < 0.0F)) || (rgb[0] > 1.0F)) || (rgb[1] > 1.0F)) || (rgb[2] > 1.0F)) {
            return 0;
        }
        return ((0xff000000 | (((int) (rgb[0] * 255.0F)) << 16)) | (((int) (rgb[1] * 255.0F)) << 8)) | ((int) (rgb[2] * 255.0F));
    }

    /**
     * Returns a tool tip text for the specified color with information in the color space of the
     * color.
     */
    public static java.lang.String toToolTipText(java.awt.Color c) {
        if (org.jhotdraw.color.ColorUtil.formatter == null) {
            org.jhotdraw.color.ColorUtil.formatter = new org.jhotdraw.text.ColorToolTipTextFormatter();
        }
        try {
            return org.jhotdraw.color.ColorUtil.formatter.valueToString(c);
        } catch (java.text.ParseException ex) {
            java.lang.InternalError error = new java.lang.InternalError("Unable to generate tool tip text from color " + c);
            error.initCause(ex);
            throw error;
        }
    }

    /**
     * Returns true, if the two color spaces are equal.
     */
    public static boolean isEqual(java.awt.color.ColorSpace a, java.awt.color.ColorSpace b) {
        if ((a instanceof java.awt.color.ICC_ColorSpace) && (b instanceof java.awt.color.ICC_ColorSpace)) {
            java.awt.color.ICC_ColorSpace aicc = ((java.awt.color.ICC_ColorSpace) (a));
            java.awt.color.ICC_ColorSpace bicc = ((java.awt.color.ICC_ColorSpace) (b));
            java.awt.color.ICC_Profile ap = aicc.getProfile();
            java.awt.color.ICC_Profile bp = bicc.getProfile();
            return ap.equals(bp);
        } else {
            return a.equals(b);
        }
    }

    /**
     * Returns the name of the color space. If the color space is an {@code ICC_ColorSpace} the name
     * is retrieved from the "desc" data element of the color profile.
     *
     * @param a
     * 		A ColorSpace.
     * @return The name.
     */
    public static java.lang.String getName(java.awt.color.ColorSpace a) {
        if (a instanceof org.jhotdraw.color.NamedColorSpace) {
            return ((org.jhotdraw.color.NamedColorSpace) (a)).getName();
        }
        if (a instanceof java.awt.color.ICC_ColorSpace) {
            java.awt.color.ICC_ColorSpace icc = ((java.awt.color.ICC_ColorSpace) (a));
            java.awt.color.ICC_Profile p = icc.getProfile();
            // Get the name from the profile description tag
            byte[] desc = p.getData(0x64657363);
            if (desc != null) {
                java.io.DataInputStream in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(desc));
                try {
                    int magic = in.readInt();
                    int reserved = in.readInt();
                    if (magic != 0x64657363) {
                        throw new java.io.IOException("Illegal magic:" + java.lang.Integer.toHexString(magic));
                    }
                    if (reserved != 0x0) {
                        throw new java.io.IOException("Illegal reserved:" + java.lang.Integer.toHexString(reserved));
                    }
                    long nameLength = in.readInt() & 0xffffffffL;
                    java.lang.StringBuilder buf = new java.lang.StringBuilder();
                    for (int i = 0; i < (nameLength - 1); i++) {
                        buf.append(((char) (in.readUnsignedByte())));
                    }
                    return buf.toString();
                } catch (java.io.IOException e) {
                    // fall back
                    e.printStackTrace();
                }
            }
        }
        if (a instanceof java.awt.color.ICC_ColorSpace) {
            // Fall back if no description is available
            java.lang.StringBuilder buf = new java.lang.StringBuilder();
            for (int i = 0; i < a.getNumComponents(); i++) {
                if (buf.length() > 0) {
                    buf.append("-");
                }
                buf.append(a.getName(i));
            }
            return buf.toString();
        } else {
            return a.getClass().getSimpleName();
        }
    }

    /**
     * Blackens the specified color by casting a black shadow of the specified amount on the color.
     */
    public static java.awt.Color shadow(java.awt.Color c, int amount) {
        return new java.awt.Color(java.lang.Math.max(0, c.getRed() - amount), java.lang.Math.max(0, c.getGreen() - amount), java.lang.Math.max(0, c.getBlue() - amount), c.getAlpha());
    }

    /**
     * Faster toRGB method which uses the provided output array.
     */
    public static void CStoRGB(java.awt.color.ColorSpace cs, float[] colorvalue, float[] rgb) {
        if (cs.isCS_sRGB()) {
            java.lang.System.arraycopy(colorvalue, 0, rgb, 0, 3);
        } else if (cs instanceof org.jhotdraw.color.NamedColorSpace) {
            org.jhotdraw.color.ColorUtil.CStoRGB(((org.jhotdraw.color.NamedColorSpace) (cs)), colorvalue, rgb);
        } else {
            float[] tmp = cs.toRGB(colorvalue);
            java.lang.System.arraycopy(tmp, 0, rgb, 0, tmp.length);
        }
    }

    /**
     * Faster fromRGB method which uses the provided output array.
     */
    public static float[] CSfromRGB(java.awt.color.ColorSpace cs, float[] rgb, float[] colorvalue) {
        if (cs instanceof org.jhotdraw.color.NamedColorSpace) {
            org.jhotdraw.color.ColorUtil.CSfromRGB(((org.jhotdraw.color.NamedColorSpace) (cs)), rgb, colorvalue);
        } else {
            float[] tmp = cs.fromRGB(rgb);
            java.lang.System.arraycopy(tmp, 0, colorvalue, 0, tmp.length);
        }
        return colorvalue;
    }

    /**
     * Faster toCIEXYZ method which uses the provided output array.
     */
    public static float[] CStoCIEXYZ(java.awt.color.ColorSpace cs, float[] colorvalue, float[] xyz) {
        if (cs instanceof org.jhotdraw.color.NamedColorSpace) {
            org.jhotdraw.color.ColorUtil.CStoCIEXYZ(((org.jhotdraw.color.NamedColorSpace) (cs)), colorvalue, xyz);
        } else {
            float[] tmp = cs.toCIEXYZ(colorvalue);
            java.lang.System.arraycopy(tmp, 0, xyz, 0, tmp.length);
        }
        return xyz;
    }

    /**
     * Faster fromCIEXYZ method which uses the provided output array.
     */
    public static float[] CSfromCIEXYZ(java.awt.color.ColorSpace cs, float[] xyz, float[] colorvalue) {
        if (cs instanceof org.jhotdraw.color.NamedColorSpace) {
            org.jhotdraw.color.ColorUtil.CSfromCIEXYZ(((org.jhotdraw.color.NamedColorSpace) (cs)), xyz, colorvalue);
        } else {
            float[] tmp = cs.toRGB(xyz);
            java.lang.System.arraycopy(tmp, 0, colorvalue, 0, tmp.length);
        }
        return colorvalue;
    }

    /**
     * Faster toRGB method which uses the provided output array.
     */
    public static float[] CStoRGB(org.jhotdraw.color.NamedColorSpace cs, float[] colorvalue, float[] rgb) {
        cs.toRGB(colorvalue, rgb);
        return rgb;
    }

    /**
     * Faster CIEXYZtoRGB method which uses the provided output array.
     */
    public static float[] CIEXYZtoRGB(float[] xyz, float[] rgb) {
        float[] tmp = org.jhotdraw.color.ColorUtil.SRGB.fromCIEXYZ(xyz);
        java.lang.System.arraycopy(tmp, 0, rgb, 0, 3);
        return rgb;
    }

    /**
     * Faster RGBtoCIEXYZ method which uses the provided output array.
     */
    public static float[] RGBtoCIEXYZ(float[] rgb, float[] xyz) {
        float[] tmp = org.jhotdraw.color.ColorUtil.SRGB.toCIEXYZ(rgb);
        java.lang.System.arraycopy(tmp, 0, xyz, 0, 3);
        return xyz;
    }

    /**
     * Faster fromRGB method which uses the provided output array.
     */
    public static void CSfromRGB(org.jhotdraw.color.NamedColorSpace cs, float[] rgb, float[] colorvalue) {
        cs.fromRGB(rgb, colorvalue);
    }

    /**
     * Faster toCIEXYZ method which uses the provided output array.
     */
    public static void CStoCIEXYZ(org.jhotdraw.color.NamedColorSpace cs, float[] colorvalue, float[] xyz) {
        cs.toCIEXYZ(colorvalue, xyz);
    }

    /**
     * Faster fromCIEXYZ method which uses the provided output array.
     */
    public static void CSfromCIEXYZ(org.jhotdraw.color.NamedColorSpace cs, float[] xyz, float[] colorvalue) {
        cs.fromCIEXYZ(xyz, xyz);
    }
}