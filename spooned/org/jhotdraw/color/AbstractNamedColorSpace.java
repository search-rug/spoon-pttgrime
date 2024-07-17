/* @(#)AbstractNamedColorSpace.java

Copyright (c) 2013 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * {@code AbstractNamedColorSpace}.
 */
public abstract class AbstractNamedColorSpace extends java.awt.color.ColorSpace implements org.jhotdraw.color.NamedColorSpace {
    private static final long serialVersionUID = 1L;

    public AbstractNamedColorSpace(int type, int numcomponents) {
        super(type, numcomponents);
    }

    @java.lang.Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        return fromCIEXYZ(colorvalue, new float[getNumComponents()]);
    }

    @java.lang.Override
    public final float[] toRGB(float[] colorvalue) {
        return toRGB(colorvalue, new float[3]);
    }

    @java.lang.Override
    public float[] fromRGB(float[] rgb) {
        float[] tmp = new float[getNumComponents()];
        return fromRGB(rgb, new float[getNumComponents()]);
    }

    @java.lang.Override
    public final float[] toCIEXYZ(float[] colorvalue) {
        return toCIEXYZ(colorvalue, new float[3]);
    }

    @java.lang.Override
    public float[] toCIEXYZ(float[] colorvalue, float[] xyz) {
        return org.jhotdraw.color.ColorUtil.RGBtoCIEXYZ(toRGB(colorvalue, xyz), xyz);
    }

    @java.lang.Override
    public float[] fromCIEXYZ(float[] xyz, float[] colorvalue) {
        return fromRGB(org.jhotdraw.color.ColorUtil.CIEXYZtoRGB(xyz, colorvalue), colorvalue);
    }
}