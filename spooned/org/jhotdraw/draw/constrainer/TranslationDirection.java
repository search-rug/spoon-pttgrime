/* @(#)TranslationDirection.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.constrainer;
/**
 * Specifies the possible directions for translations on a two-dimensional plane.
 *
 * <p>This enumeration is used by drawing tools and handles to perform constrained transforms of
 * figures on a drawing.
 *
 * @see Constrainer
 */
public enum TranslationDirection {

    NORTH,
    WEST,
    SOUTH,
    EAST,
    NORTH_WEST,
    SOUTH_WEST,
    NORTH_EAST,
    SOUTH_EAST;

    /**
     * Returns the direction from the provided start point to the end point. Returns null, if both
     * points are at the same location.
     */
    public static org.jhotdraw.draw.constrainer.TranslationDirection getDirection(java.awt.Point startPoint, java.awt.Point endPoint) {
        int dx = endPoint.x - startPoint.x;
        int dy = endPoint.y - startPoint.y;
        if (dx == 0) {
            if (dy == 0) {
                return null;
            } else if (dy > 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH;
            } else {
                return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH;
            }
        } else if (dx > 0) {
            if (dy == 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.EAST;
            } else if (dy > 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH_EAST;
            } else {
                return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH_EAST;
            }
        } else if (dy == 0) {
            return org.jhotdraw.draw.constrainer.TranslationDirection.WEST;
        } else if (dy > 0) {
            return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH_WEST;
        } else {
            return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH_WEST;
        }
    }

    /**
     * Returns the direction from the provided start point to the end point. Returns null, if both
     * points are at the same location.
     */
    public static org.jhotdraw.draw.constrainer.TranslationDirection getDirection(java.awt.geom.Point2D.Double startPoint, java.awt.geom.Point2D.Double endPoint) {
        double dx = endPoint.x - startPoint.x;
        double dy = endPoint.y - startPoint.y;
        if (dx == 0) {
            if (dy == 0) {
                return null;
            } else if (dy > 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH;
            } else {
                return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH;
            }
        } else if (dx > 0) {
            if (dy == 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.EAST;
            } else if (dy > 0) {
                return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH_EAST;
            } else {
                return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH_EAST;
            }
        } else if (dy == 0) {
            return org.jhotdraw.draw.constrainer.TranslationDirection.WEST;
        } else if (dy > 0) {
            return org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH_WEST;
        } else {
            return org.jhotdraw.draw.constrainer.TranslationDirection.NORTH_WEST;
        }
    }
}