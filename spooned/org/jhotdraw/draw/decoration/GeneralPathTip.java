/* @(#)GeneralPathLineDecoration.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.decoration;
/**
 * A {@link LineDecoration} which draws a general path.
 */
public class GeneralPathTip extends org.jhotdraw.draw.decoration.AbstractLineDecoration {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Path2D.Double path;

    double decorationRadius;

    public GeneralPathTip(java.awt.geom.Path2D.Double path, double decorationRadius) {
        this(path, decorationRadius, false, true, false);
    }

    public GeneralPathTip(java.awt.geom.Path2D.Double path, double decorationRadius, boolean isFilled, boolean isStroked, boolean isSolid) {
        super(isFilled, isStroked, isSolid);
        this.path = path;
        this.decorationRadius = decorationRadius;
    }

    @java.lang.Override
    protected java.awt.geom.Path2D.Double getDecoratorPath(org.jhotdraw.draw.figure.Figure f) {
        return ((java.awt.geom.Path2D.Double) (path.clone()));
    }

    @java.lang.Override
    protected double getDecoratorPathRadius(org.jhotdraw.draw.figure.Figure f) {
        return decorationRadius;
    }
}