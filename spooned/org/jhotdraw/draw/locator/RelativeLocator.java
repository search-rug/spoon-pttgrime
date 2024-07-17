/* @(#)RelativeLocator.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.locator;
/**
 * A locator that specfies a point that is relative to the bounds of a figure.
 */
public class RelativeLocator extends org.jhotdraw.draw.locator.AbstractLocator {
    private static final long serialVersionUID = 1L;

    /**
     * Relative x-coordinate on the bounds of the figure. The value 0 is on the left boundary of the
     * figure, the value 1 on the right boundary.
     */
    protected double relativeX;

    /**
     * Relative y-coordinate on the bounds of the figure. The value 0 is on the top boundary of the
     * figure, the value 1 on the bottom boundary.
     */
    protected double relativeY;

    /**
     * If this is set to true, if the locator is transforming with the figure.
     */
    protected boolean isTransform;

    public RelativeLocator() {
        this(0, 0, false);
    }

    public RelativeLocator(double relativeX, double relativeY) {
        this(relativeX, relativeY, false);
    }

    /**
     *
     * @param relativeX
     * 		x-position relative to bounds expressed as a value between 0 and 1.
     * @param relativeY
     * 		y-position relative to bounds expressed as a value between 0 and 1.
     * @param isTransform
     * 		Set this to true, if the locator shall honor the TRANSFORM attribute of the
     * 		Figure.
     */
    public RelativeLocator(double relativeX, double relativeY, boolean isTransform) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.isTransform = isTransform;
    }

    @java.lang.Override
    public Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, double scale) {
        java.awt.geom.Rectangle2D.Double bounds = owner.getBounds(scale);
        if ((owner instanceof org.jhotdraw.draw.figure.DecoratedFigure) && (((org.jhotdraw.draw.figure.DecoratedFigure) (owner)).getDecorator() != null)) {
            org.jhotdraw.geom.Insets2D.Double insets = owner.attr().get(org.jhotdraw.draw.AttributeKeys.DECORATOR_INSETS);
            if (insets != null) {
                insets.addTo(bounds);
            }
        }
        java.awt.geom.Point2D.Double location;
        if (isTransform) {
            location = new java.awt.geom.Point2D.Double(bounds.x + (bounds.width * relativeX), bounds.y + (bounds.height * relativeY));
            if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(location, location);
            }
        } else {
            if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                java.awt.geom.Rectangle2D r = owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds).getBounds2D();
                bounds.x = r.getX();
                bounds.y = r.getY();
                bounds.width = r.getWidth();
                bounds.height = r.getHeight();
            }
            location = new java.awt.geom.Point2D.Double(bounds.x + (bounds.width * relativeX), bounds.y + (bounds.height * relativeY));
        }
        return new Position(location);
    }

    /**
     * Non-transforming East.
     */
    public static org.jhotdraw.draw.locator.Locator east() {
        return org.jhotdraw.draw.locator.RelativeLocator.east(false);
    }

    /**
     * East.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator east(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(1.0, 0.5, isTransform);
    }

    /**
     * Non-transforming North.
     */
    public static org.jhotdraw.draw.locator.Locator north() {
        return org.jhotdraw.draw.locator.RelativeLocator.north(false);
    }

    /**
     * North.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator north(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.5, 0.0, isTransform);
    }

    /**
     * Non-transforming West.
     */
    public static org.jhotdraw.draw.locator.Locator west() {
        return org.jhotdraw.draw.locator.RelativeLocator.west(false);
    }

    /**
     * West.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator west(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.0, 0.5, isTransform);
    }

    /**
     * Non-transforming North east.
     */
    public static org.jhotdraw.draw.locator.Locator northEast() {
        return org.jhotdraw.draw.locator.RelativeLocator.northEast(false);
    }

    /**
     * Norht East.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator northEast(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(1.0, 0.0, isTransform);
    }

    /**
     * Non-transforming North west.
     */
    public static org.jhotdraw.draw.locator.Locator northWest() {
        return org.jhotdraw.draw.locator.RelativeLocator.northWest(false);
    }

    /**
     * North West.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator northWest(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.0, 0.0, isTransform);
    }

    /**
     * Non-transforming South.
     */
    public static org.jhotdraw.draw.locator.Locator south() {
        return org.jhotdraw.draw.locator.RelativeLocator.south(false);
    }

    /**
     * South.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator south(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.5, 1.0, isTransform);
    }

    /**
     * Non-transforming South east.
     */
    public static org.jhotdraw.draw.locator.Locator southEast() {
        return org.jhotdraw.draw.locator.RelativeLocator.southEast(false);
    }

    /**
     * South East.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator southEast(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(1.0, 1.0, isTransform);
    }

    /**
     * Non-transforming South west.
     */
    public static org.jhotdraw.draw.locator.Locator southWest() {
        return org.jhotdraw.draw.locator.RelativeLocator.southWest(false);
    }

    /**
     * South West.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator southWest(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.0, 1.0, isTransform);
    }

    /**
     * Non-transforming Center.
     */
    public static org.jhotdraw.draw.locator.Locator center() {
        return org.jhotdraw.draw.locator.RelativeLocator.center(false);
    }

    /**
     * Center.
     *
     * @param isTransform
     * 		Set this to true, if RelativeLocator shall honour the
     * 		AttributesKey.TRANSFORM attribute of the Figure.
     */
    public static org.jhotdraw.draw.locator.Locator center(boolean isTransform) {
        return new org.jhotdraw.draw.locator.RelativeLocator(0.5, 0.5, isTransform);
    }

    // @Override
    // public void write(DOMOutput out) {
    // out.addAttribute("relativeX", relativeX, 0.5);
    // out.addAttribute("relativeY", relativeY, 0.5);
    // }
    // 
    // @Override
    // public void read(DOMInput in) {
    // relativeX = in.getAttribute("relativeX", 0.5);
    // relativeY = in.getAttribute("relativeY", 0.5);
    // }
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final org.jhotdraw.draw.locator.RelativeLocator other = ((org.jhotdraw.draw.locator.RelativeLocator) (obj));
        if (this.relativeX != other.relativeX) {
            return false;
        }
        if (this.relativeY != other.relativeY) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = 7;
        hash = (71 * hash) + ((int) (java.lang.Double.doubleToLongBits(this.relativeX) ^ (java.lang.Double.doubleToLongBits(this.relativeX) >>> 32)));
        hash = (71 * hash) + ((int) (java.lang.Double.doubleToLongBits(this.relativeY) ^ (java.lang.Double.doubleToLongBits(this.relativeY) >>> 32)));
        return hash;
    }
}