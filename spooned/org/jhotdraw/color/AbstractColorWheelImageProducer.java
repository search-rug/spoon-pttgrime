/* @(#)AbstractColorWheelImageProducer.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * AbstractColorWheelImageProducer.
 */
public abstract class AbstractColorWheelImageProducer extends java.awt.image.MemoryImageSource {
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

    public AbstractColorWheelImageProducer(java.awt.color.ColorSpace sys, int w, int h) {
        super(w, h, null, 0, w);
        this.colorSpace = sys;
        pixels = new int[w * h];
        this.w = w;
        this.h = h;
        setAnimated(true);
        newPixels(pixels, java.awt.image.ColorModel.getRGBdefault(), 0, w);
    }

    public void setRadialComponentIndex(int newValue) {
        radialIndex = newValue;
        isPixelsValid = false;
    }

    public void setAngularComponentIndex(int newValue) {
        angularIndex = newValue;
        isPixelsValid = false;
    }

    public void setVerticalComponentIndex(int newValue) {
        verticalIndex = newValue;
        isPixelsValid = false;
    }

    public void setVerticalValue(float newValue) {
        isPixelsValid = isPixelsValid && (verticalValue == newValue);
        verticalValue = newValue;
    }

    public boolean needsGeneration() {
        return !isPixelsValid;
    }

    public void regenerateColorWheel() {
        if (!isPixelsValid) {
            generateColorWheel();
        }
    }

    public float getRadius() {
        return (java.lang.Math.min(w, h) * 0.5F) - 2;
    }

    public java.awt.geom.Point2D.Float getCenter() {
        return new java.awt.geom.Point2D.Float(w * 0.5F, h * 0.5F);
    }

    protected abstract void generateColorWheel();

    public java.awt.Point getColorLocation(java.awt.Color c) {
        float[] components = org.jhotdraw.color.ColorUtil.fromColor(colorSpace, c);
        return getColorLocation(components);
    }

    public abstract java.awt.Point getColorLocation(float[] components);

    public abstract float[] getColorAt(int x, int y);
}