/* @(#)RelativeLocator.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.locator;
import org.jhotdraw.draw.figure.DecoratedFigure;
/**
 * A locator that specfies a point that is relative to the bounds of a figures decorator.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Strategy</em><br>
 * {@code RelativeDecoratorLocator} encapsulates a strategy for locating a point on a decorator.<br>
 * Component: {@link DecoratedFigure}; Strategy: {@link RelativeDecoratorLocator}. <hr>
 *
 * <p><hr>
 */
public class RelativeDecoratorLocator extends org.jhotdraw.draw.locator.RelativeLocator {
    private static final long serialVersionUID = 1L;

    private boolean isQuadratic;

    public RelativeDecoratorLocator() {
    }

    public RelativeDecoratorLocator(double relativeX, double relativeY) {
        super(relativeX, relativeY);
    }

    public RelativeDecoratorLocator(double relativeX, double relativeY, boolean isQuadratic) {
        super(relativeX, relativeY);
        this.isQuadratic = isQuadratic;
    }

    @java.lang.Override
    public Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, double scale) {
        java.awt.geom.Rectangle2D.Double r;
        if ((owner instanceof org.jhotdraw.draw.figure.DecoratedFigure) && (((org.jhotdraw.draw.figure.DecoratedFigure) (owner)).getDecorator() != null)) {
            r = ((org.jhotdraw.draw.figure.DecoratedFigure) (owner)).getDecorator().getBounds();
        } else {
            r = owner.getBounds();
        }
        if (isQuadratic) {
            double side = java.lang.Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        return new Position(new java.awt.geom.Point2D.Double(r.x + (r.width * relativeX), r.y + (r.height * relativeY)));
    }

    public static org.jhotdraw.draw.locator.Locator east() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(1.0, 0.5);
    }

    /**
     * North.
     */
    public static org.jhotdraw.draw.locator.Locator north() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.5, 0.0);
    }

    /**
     * West.
     */
    public static org.jhotdraw.draw.locator.Locator west() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.0, 0.5);
    }

    /**
     * North east.
     */
    public static org.jhotdraw.draw.locator.Locator northEast() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(1.0, 0.0);
    }

    /**
     * North west.
     */
    public static org.jhotdraw.draw.locator.Locator northWest() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.0, 0.0);
    }

    /**
     * South.
     */
    public static org.jhotdraw.draw.locator.Locator south() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.5, 1.0);
    }

    /**
     * South east.
     */
    public static org.jhotdraw.draw.locator.Locator southEast() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(1.0, 1.0);
    }

    /**
     * South west.
     */
    public static org.jhotdraw.draw.locator.Locator southWest() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.0, 1.0);
    }

    /**
     * Center.
     */
    public static org.jhotdraw.draw.locator.Locator center() {
        return new org.jhotdraw.draw.locator.RelativeDecoratorLocator(0.5, 0.5);
    }
}