/* @(#)RadialGradient.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * Represents an SVG RadialGradient.
 */
public class RadialGradient implements org.jhotdraw.samples.svg.Gradient {
    private double cx;

    private double cy;

    private double fx;

    private double fy;

    private double r;

    private boolean isRelativeToFigureBounds = true;

    private double[] stopOffsets;

    private java.awt.Color[] stopColors;

    private java.awt.geom.AffineTransform transform;

    private double[] stopOpacities;

    public RadialGradient() {
    }

    public RadialGradient(double cx, double cy, double fx, double fy, double r, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx) {
        this.cx = cx;
        this.cy = cy;
        this.fx = fx;
        this.fy = fy;
        this.r = r;
        this.stopOffsets = stopOffsets.clone();
        this.stopColors = stopColors.clone();
        this.stopOpacities = stopOpacities.clone();
        this.isRelativeToFigureBounds = isRelativeToFigureBounds;
        this.transform = tx;
    }

    public void setGradientCircle(double cx, double cy, double r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
    }

    public void setStops(double[] offsets, java.awt.Color[] colors, double[] stopOpacities) {
        this.stopOffsets = offsets.clone();
        this.stopColors = colors.clone();
        this.stopOpacities = stopOpacities.clone();
    }

    public void setRelativeToFigureBounds(boolean b) {
        isRelativeToFigureBounds = b;
    }

    @java.lang.Override
    public void makeRelativeToFigureBounds(org.jhotdraw.draw.figure.Figure f) {
        if (!isRelativeToFigureBounds) {
            isRelativeToFigureBounds = true;
            java.awt.geom.Rectangle2D.Double bounds = f.getBounds();
            cx = (cx - bounds.x) / bounds.width;
            cy = (cy - bounds.y) / bounds.height;
            r = r / java.lang.Math.sqrt(((bounds.width * bounds.width) / 2.0) + ((bounds.height * bounds.height) / 2.0));
        }
    }

    @java.lang.Override
    public java.awt.Paint getPaint(org.jhotdraw.draw.figure.Figure f, double opacity) {
        if ((stopColors.length == 0) || (r <= 0)) {
            return new java.awt.Color(0xa0a0a000, true);
        }
        // Compute colors and fractions for the paint
        java.awt.Color[] colors = new java.awt.Color[stopColors.length];
        float[] fractions = new float[stopColors.length];
        for (int i = 0; i < stopColors.length; i++) {
            fractions[i] = ((float) (stopOffsets[i]));
            colors[i] = new java.awt.Color((stopColors[i].getRGB() & 0xffffff) | (((int) ((opacity * stopOpacities[i]) * 255)) << 24), true);
        }
        // Compute the dimensions and transforms for the paint
        java.awt.geom.Point2D.Double cp;
        java.awt.geom.Point2D.Double fp;
        double rr;
        cp = new java.awt.geom.Point2D.Double(cx, cy);
        fp = new java.awt.geom.Point2D.Double(fx, fy);
        rr = r;
        java.awt.geom.AffineTransform t = transform;
        if (isRelativeToFigureBounds) {
            if (!t.isIdentity()) {
                java.lang.System.out.println((("RadialGradient " + hashCode()) + " t=") + t);
            }
            t = new java.awt.geom.AffineTransform();
            java.awt.geom.Rectangle2D.Double bounds = f.getBounds();
            t.translate(bounds.x, bounds.y);
            t.scale(bounds.width, bounds.height);
        }
        // Construct a solid color, if only one stop color is given, or if
        // transform is not invertible
        if ((stopColors.length == 1) || (t.getDeterminant() == 0)) {
            return colors[0];
        }
        // Construct the paint
        java.awt.RadialGradientPaint gp;
        gp = new java.awt.RadialGradientPaint(cp, ((float) (rr)), fp, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, t);
        return gp;
    }

    public double getCX() {
        return cx;
    }

    public double getCY() {
        return cy;
    }

    public double getFX() {
        return fx;
    }

    public double getFY() {
        return fy;
    }

    public double getR() {
        return r;
    }

    public double[] getStopOffsets() {
        return stopOffsets.clone();
    }

    public java.awt.Color[] getStopColors() {
        return stopColors.clone();
    }

    public double[] getStopOpacities() {
        return stopOpacities.clone();
    }

    @java.lang.Override
    public boolean isRelativeToFigureBounds() {
        return isRelativeToFigureBounds;
    }

    public void setTransform(java.awt.geom.AffineTransform tx) {
        transform = tx;
    }

    public java.awt.geom.AffineTransform getTransform() {
        return transform;
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if (transform == null) {
            transform = ((java.awt.geom.AffineTransform) (tx.clone()));
        } else {
            transform.preConcatenate(tx);
        }
    }

    @java.lang.Override
    public java.lang.Object clone() {
        try {
            org.jhotdraw.samples.svg.RadialGradient that = ((org.jhotdraw.samples.svg.RadialGradient) (super.clone()));
            that.stopOffsets = this.stopOffsets.clone();
            that.stopColors = this.stopColors.clone();
            that.stopOpacities = this.stopOpacities.clone();
            that.transform = ((java.awt.geom.AffineTransform) (this.transform.clone()));
            return that;
        } catch (java.lang.CloneNotSupportedException ex) {
            java.lang.InternalError e = new java.lang.InternalError();
            e.initCause(ex);
            throw e;
        }
    }

    @java.lang.Override
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(cx);
        bits += java.lang.Double.doubleToLongBits(cy) * 37;
        bits += stopColors[0].hashCode() * 43;
        bits += stopColors[stopColors.length - 1].hashCode() * 47;
        return ((int) (bits)) ^ ((int) (bits >> 32));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof org.jhotdraw.samples.svg.RadialGradient) {
            return equals(((org.jhotdraw.samples.svg.RadialGradient) (o)));
        } else {
            return false;
        }
    }

    public boolean equals(org.jhotdraw.samples.svg.RadialGradient that) {
        return (((((((((cx == that.cx) && (cy == that.cy)) && (fx == that.fx)) && (fy == that.fy)) && (r == that.r)) && (isRelativeToFigureBounds == that.isRelativeToFigureBounds)) && java.util.Arrays.equals(stopOffsets, that.stopOffsets)) && java.util.Arrays.equals(stopOpacities, that.stopOpacities)) && java.util.Arrays.equals(stopColors, that.stopColors)) && transform.equals(that.transform);
    }
}