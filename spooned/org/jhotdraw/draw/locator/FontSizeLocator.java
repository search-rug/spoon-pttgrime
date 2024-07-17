/* @(#)FontSizeLocator.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.locator;
/**
 * {@code FontSizeLocator} is used by {@link org.jhotdraw.draw.handle.FontSizeHandle} to locate its
 * position on the drawing.
 */
public class FontSizeLocator implements org.jhotdraw.draw.locator.Locator {
    public FontSizeLocator() {
    }

    /**
     * Locates a position on the provided figure.
     *
     * @return a Point2D.Double on the figure.
     */
    @java.lang.Override
    public Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, double scale) {
        java.awt.geom.Point2D.Double p = ((java.awt.geom.Point2D.Double) (owner.getStartPoint().clone()));
        if (owner instanceof org.jhotdraw.draw.figure.TextHolderFigure) {
            p.y += ((org.jhotdraw.draw.figure.TextHolderFigure) (owner)).getFontSize() / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(owner, scale);
            p.y += ((org.jhotdraw.draw.figure.TextHolderFigure) (owner)).getInsets().top;
        } else {
            p.y += owner.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE) / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(owner, scale);
        }
        if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
        }
        return new Position(p);
    }

    @java.lang.Override
    public Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.figure.Figure dependent, double scale) {
        return locate(owner, scale);
    }
}