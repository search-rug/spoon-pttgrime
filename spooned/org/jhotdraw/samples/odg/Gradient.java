/* @(#)Gradient.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
/**
 * Represents an SVG Gradient.
 */
public interface Gradient extends java.lang.Cloneable {
    public java.awt.Paint getPaint(org.jhotdraw.draw.figure.Figure f, double opacity);

    public boolean isRelativeToFigureBounds();

    public void transform(java.awt.geom.AffineTransform tx);

    public java.lang.Object clone();

    public void makeRelativeToFigureBounds(org.jhotdraw.draw.figure.Figure f);
}