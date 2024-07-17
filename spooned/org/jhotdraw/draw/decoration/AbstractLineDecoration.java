/* @(#)AbstractLineDecoration.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.decoration;
/**
 * This abstract class can be extended to implement a {@link LineDecoration}.
 */
public abstract class AbstractLineDecoration implements org.jhotdraw.draw.decoration.LineDecoration {
    private static final long serialVersionUID = 1L;

    /**
     * If this is true, the decoration is filled.
     */
    private boolean isFilled;

    /**
     * If this is true, the decoration is stroked.
     */
    private boolean isStroked;

    /**
     * If this is true, the stroke color is used to fill the decoration.
     */
    private boolean isSolid;

    /**
     * Constructs an arrow tip with the given angle and radius.
     */
    public AbstractLineDecoration(boolean isFilled, boolean isStroked, boolean isSolid) {
        this.isFilled = isFilled;
        this.isStroked = isStroked;
        this.isSolid = isSolid;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isStroked() {
        return isStroked;
    }

    public boolean isSolid() {
        return isSolid;
    }

    /**
     * Draws the arrow tip in the direction specified by the given two Points. (template method)
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g, org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2) {
        java.awt.geom.Path2D.Double path = getTransformedDecoratorPath(f, p1, p2, org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
        java.awt.Color color;
        if (isFilled) {
            if (isSolid) {
                color = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR);
            } else {
                color = f.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
            }
            if (color != null) {
                g.setColor(color);
                g.fill(path);
            }
        }
        if (isStroked) {
            color = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR);
            if (color != null) {
                g.setColor(color);
                g.setStroke(org.jhotdraw.draw.AttributeKeys.getStroke(f, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
                g.draw(path);
            }
        }
    }

    /**
     * Returns the drawing area of the decorator.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, double factor) {
        java.awt.geom.Path2D.Double path = getTransformedDecoratorPath(f, p1, p2, org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor));
        java.awt.geom.Rectangle2D b = path.getBounds2D();
        java.awt.geom.Rectangle2D.Double area = new java.awt.geom.Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        if (isStroked) {
            double strokeWidth = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH);
            int strokeJoin = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN);
            double miterLimit = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT) * strokeWidth;
            double grow;
            if (strokeJoin == java.awt.BasicStroke.JOIN_MITER) {
                grow = ((int) (1 + ((strokeWidth / 2) * miterLimit)));
            } else {
                grow = ((int) (1 + (strokeWidth / 2)));
            }
            org.jhotdraw.geom.Geom.grow(area, grow * factor, grow * factor);
        } else {
            org.jhotdraw.geom.Geom.grow(area, factor, factor);// grow due to antialiasing

        }
        return area;
    }

    @java.lang.Override
    public double getDecorationRadius(org.jhotdraw.draw.figure.Figure f, double factor) {
        double strokeWidth = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH);
        double scaleFactor;
        if (strokeWidth > 1.0F) {
            scaleFactor = 1.0 + ((strokeWidth - 1.0) / 2.0);
        } else {
            scaleFactor = 1.0;
        }
        scaleFactor /= factor;
        return getDecoratorPathRadius(f) * scaleFactor;
    }

    private java.awt.geom.Path2D.Double getTransformedDecoratorPath(org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, double factor) {
        java.awt.geom.Path2D.Double path = getDecoratorPath(f);
        double strokeWidth = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH);
        java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
        transform.translate(p1.x, p1.y);
        transform.rotate(java.lang.Math.atan2(p1.x - p2.x, p2.y - p1.y));
        // transform.rotate(Math.PI / 2);
        if (strokeWidth > 1.0F) {
            transform.scale(1.0 + ((strokeWidth - 1.0) / 2.0), 1.0 + ((strokeWidth - 1.0) / 2.0));
        }
        transform.scale(1 / factor, 1 / factor);
        path.transform(transform);
        return path;
    }

    public void setFilled(boolean b) {
        isFilled = b;
    }

    public void setStroked(boolean b) {
        isStroked = b;
    }

    public void setSolid(boolean b) {
        isSolid = b;
    }

    /**
     * Hook method to calculate the path of the decorator.
     */
    protected abstract java.awt.geom.Path2D.Double getDecoratorPath(org.jhotdraw.draw.figure.Figure f);

    /**
     * Hook method to calculate the radius of the decorator path.
     */
    protected abstract double getDecoratorPathRadius(org.jhotdraw.draw.figure.Figure f);
}