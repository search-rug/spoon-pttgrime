/* @(#)LineDecoration.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.decoration;
/**
 * A <em>line decoration</em> can be used to draw a decoration at the start or end of a line.
 *
 * <p>Typically a line decoration is set as an attribute value to a {@link org.jhotdraw.draw.BezierFigure} using the attribute keys {@code org.jhotdraw.draw.AttributeKeys.START_DECORATION} and {@code org.jhotdraw.draw.AttributeKeys.END_DECORATION}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Decorator</em><br>
 * The start and end point of a {@code BezierFigure} can be decorated with a line decoration.<br>
 * Component: {@link org.jhotdraw.draw.BezierFigure}; Decorator: {@link org.jhotdraw.draw.decoration.LineDecoration}. <hr>
 */
public interface LineDecoration extends java.lang.Cloneable , java.io.Serializable {
    /**
     * Draws the decoration in the direction specified by the two Points.
     */
    public void draw(java.awt.Graphics2D g, org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2);

    /**
     * Returns the radius of the decorator. This is used to crop the end of the line, to prevent it
     * from being drawn over the decorator.
     */
    public abstract double getDecorationRadius(org.jhotdraw.draw.figure.Figure f, double factor);

    /**
     * Returns the drawing bounds of the decorator.
     */
    public java.awt.geom.Rectangle2D.Double getDrawingArea(org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, double factor);
}