/* @(#)LinearGradient.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * Represents an SVG LinearGradient.
 */
public class LinearGradient implements org.jhotdraw.samples.svg.Gradient {
    private double x1;

    private double y1;

    private double x2;

    private double y2;

    private boolean isRelativeToFigureBounds = true;

    private double[] stopOffsets;

    private java.awt.Color[] stopColors;

    private double[] stopOpacities;

    private java.awt.geom.AffineTransform transform;

    private int spreadMethod;

    public LinearGradient() {
    }

    public LinearGradient(double x1, double y1, double x2, double y2, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.stopOffsets = stopOffsets.clone();
        this.stopColors = stopColors.clone();
        this.stopOpacities = stopOpacities.clone();
        this.isRelativeToFigureBounds = isRelativeToFigureBounds;
        this.transform = tx;
    }

    public void setGradientVector(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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
    public boolean isRelativeToFigureBounds() {
        return isRelativeToFigureBounds;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
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

    public java.awt.geom.AffineTransform getTransform() {
        return transform;
    }

    @java.lang.Override
    public java.awt.Paint getPaint(org.jhotdraw.draw.figure.Figure f, double opacity) {
        // No stops, like fill = none
        if (stopColors.length == 0) {
            return new java.awt.Color(0x0, true);
        }
        // Compute colors and fractions for the paint
        java.awt.Color[] colors = new java.awt.Color[stopColors.length];
        float[] fractions = new float[stopColors.length];
        float previousFraction = 0;
        for (int i = 0; i < stopColors.length; i++) {
            // Each fraction must be larger or equal the previous fraction.
            fractions[i] = java.lang.Math.min(1.0F, java.lang.Math.max(previousFraction, ((float) (stopOffsets[i]))));
            colors[i] = new java.awt.Color((stopColors[i].getRGB() & 0xffffff) | (((int) ((opacity * stopOpacities[i]) * 255)) << 24), true);
            previousFraction = fractions[i];
        }
        // Compute the dimensions and transforms for the paint
        java.awt.geom.Point2D.Double p1;
        java.awt.geom.Point2D.Double p2;
        p1 = new java.awt.geom.Point2D.Double(x1, y1);
        p2 = new java.awt.geom.Point2D.Double(x2, y2);
        java.awt.geom.AffineTransform t = transform;
        if (isRelativeToFigureBounds) {
            t = ((java.awt.geom.AffineTransform) (t.clone()));
            java.awt.geom.Rectangle2D.Double bounds = f.getBounds();
            t.translate(bounds.x, bounds.y);
            t.scale(bounds.width, bounds.height);
        }
        // Construct a solid color, if only one stop color is given, or if
        // transform is not invertible
        if ((stopColors.length == 1) || (t.getDeterminant() == 0)) {
            return colors[0];
        }
        // Construct a gradient
        java.awt.LinearGradientPaint gp;
        gp = new java.awt.LinearGradientPaint(p1, p2, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, t);
        return gp;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        buf.append("LinearGradient@");
        buf.append(hashCode());
        buf.append('(');
        for (int i = 0; i < stopOffsets.length; i++) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append(stopOffsets[i]);
            buf.append('=');
            buf.append(stopOpacities[i]);
            buf.append(' ');
            buf.append(java.lang.Integer.toHexString(stopColors[i].getRGB()));
        }
        buf.append(')');
        return buf.toString();
    }

    public void setTransform(java.awt.geom.AffineTransform tx) {
        transform = tx;
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
            org.jhotdraw.samples.svg.LinearGradient that = ((org.jhotdraw.samples.svg.LinearGradient) (super.clone()));
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
    public void makeRelativeToFigureBounds(org.jhotdraw.draw.figure.Figure f) {
        if (!isRelativeToFigureBounds) {
            isRelativeToFigureBounds = true;
            java.awt.geom.Rectangle2D.Double bounds = f.getBounds();
            x1 = (x1 - bounds.x) / bounds.width;
            y1 = (y1 - bounds.y) / bounds.height;
            x2 = (x2 - bounds.x) / bounds.width;
            y2 = (y2 - bounds.y) / bounds.height;
        }
    }

    @java.lang.Override
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(x1);
        bits += java.lang.Double.doubleToLongBits(y1) * 31;
        bits += java.lang.Double.doubleToLongBits(x2) * 35;
        bits += java.lang.Double.doubleToLongBits(y2) * 39;
        bits += stopColors[0].hashCode() * 43;
        bits += stopColors[stopColors.length - 1].hashCode() * 47;
        return ((int) (bits)) ^ ((int) (bits >> 32));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof org.jhotdraw.samples.svg.LinearGradient) {
            return equals(((org.jhotdraw.samples.svg.LinearGradient) (o)));
        } else {
            return false;
        }
    }

    public boolean equals(org.jhotdraw.samples.svg.LinearGradient that) {
        return ((((((((x1 == that.x1) && (y1 == that.y1)) && (x2 == that.x2)) && (y2 == that.y2)) && (isRelativeToFigureBounds == that.isRelativeToFigureBounds)) && java.util.Arrays.equals(stopOffsets, that.stopOffsets)) && java.util.Arrays.equals(stopOpacities, that.stopOpacities)) && java.util.Arrays.equals(stopColors, that.stopColors)) && transform.equals(that.transform);
    }
}