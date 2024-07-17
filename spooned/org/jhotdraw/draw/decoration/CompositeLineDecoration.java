/* @(#)CompositeLineDecoration.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.decoration;
/**
 * A {@link LineDecoration} which can compose multiple individual line decorations.
 *
 * <p>An composite implementation of a line decoration. It allows more than one line decoration
 * shape to be rotated and moved to the end of the line. The shape is scaled by the stroke width.
 *
 * @author Huw Jones
 */
public class CompositeLineDecoration implements org.jhotdraw.draw.decoration.LineDecoration {
    private static final long serialVersionUID = 1L;

    private java.util.List<org.jhotdraw.draw.decoration.LineDecoration> decorations = new java.util.ArrayList<>();

    /**
     * Constructs a composite line decoration with no decorations.
     */
    public CompositeLineDecoration() {
    }

    /**
     * Constructs a composite line decoration with the two supplied decorations.
     */
    public CompositeLineDecoration(org.jhotdraw.draw.decoration.LineDecoration decoration1, org.jhotdraw.draw.decoration.LineDecoration decoration2) {
        addDecoration(decoration1);
        addDecoration(decoration2);
    }

    /**
     * Add another line decoration into the composite line decoration. The new decoration will be
     * appended to the existing decorations and is also the last drawn.
     */
    public void addDecoration(org.jhotdraw.draw.decoration.LineDecoration decoration) {
        if (decoration != null) {
            decorations.add(decoration);
        }
    }

    /**
     * Draws the arrow tip in the direction specified by the given two Points.. (template method)
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g, org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2) {
        for (org.jhotdraw.draw.decoration.LineDecoration decoration : decorations) {
            decoration.draw(g, f, p1, p2);
        }
    }

    /**
     * Returns the drawing area of the decorator.
     */
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(org.jhotdraw.draw.figure.Figure f, java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, double factor) {
        java.awt.geom.Rectangle2D.Double r = null;
        for (org.jhotdraw.draw.decoration.LineDecoration decoration : decorations) {
            java.awt.geom.Rectangle2D.Double aR = decoration.getDrawingArea(f, p1, p2, factor);
            if (r == null) {
                r = aR;
            } else {
                r.add(aR);
            }
        }
        return r;
    }

    /**
     * Returns the radius of the decorator. This is used to crop the end of the line, to prevent it
     * from being drawn it over the decorator.
     */
    @java.lang.Override
    public double getDecorationRadius(org.jhotdraw.draw.figure.Figure f, double factor) {
        double radius = 0;
        for (org.jhotdraw.draw.decoration.LineDecoration decoration : decorations) {
            radius = java.lang.Math.max(radius, decoration.getDecorationRadius(f, factor));
        }
        return radius;
    }
}