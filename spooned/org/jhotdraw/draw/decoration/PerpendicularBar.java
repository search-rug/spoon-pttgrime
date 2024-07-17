/* @(#)PerpendicularBar.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.decoration;
/**
 * A {@link LineDecoration} which draws a perpendicular bar.
 *
 * @author Huw Jones
 */
public class PerpendicularBar extends org.jhotdraw.draw.decoration.AbstractLineDecoration {
    private static final long serialVersionUID = 1L;

    private double height;

    /**
     * Constructs a perpendicular line with a height of 10.
     */
    public PerpendicularBar() {
        this(10);
    }

    /**
     * Constructs a perpendicular line with the given height.
     */
    public PerpendicularBar(double height) {
        super(false, true, false);
        this.height = height;
    }

    /**
     * Calculates the path of the decorator...a simple line perpendicular to the figure.
     */
    @java.lang.Override
    protected java.awt.geom.Path2D.Double getDecoratorPath(org.jhotdraw.draw.figure.Figure f) {
        java.awt.geom.Path2D.Double path = new java.awt.geom.Path2D.Double();
        double halfHeight = height / 2;
        path.moveTo(+halfHeight, 0);
        path.lineTo(-halfHeight, 0);
        return path;
    }

    /**
     * Calculates the radius of the decorator path.
     */
    @java.lang.Override
    protected double getDecoratorPathRadius(org.jhotdraw.draw.figure.Figure f) {
        return 0.5;
    }
}