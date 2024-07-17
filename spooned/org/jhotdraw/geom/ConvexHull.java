/* @(#)ConvexHull.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.geom;
/**
 * Provides utility methods for computing the convex hull from a set of points.
 */
public class ConvexHull {
    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points as a polygon object.
     */
    public static java.awt.Polygon getConvexHullPolygon(java.util.List<java.awt.Point> points) {
        java.awt.Polygon convexHull = new java.awt.Polygon();
        for (java.awt.Point p : org.jhotdraw.geom.ConvexHull.getConvexHull(points.toArray(new java.awt.Point[points.size()]))) {
            convexHull.addPoint(p.x, p.y);
        }
        return convexHull;
    }

    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points as a Polygon2D object.
     */
    public static java.awt.geom.Path2D.Double getConvexHullPath2D(java.util.List<java.awt.geom.Point2D.Double> points) {
        java.awt.geom.Path2D.Double convexHull = new java.awt.geom.Path2D.Double();
        boolean first = true;
        for (java.awt.Point p : org.jhotdraw.geom.ConvexHull.getConvexHull(points.toArray(new java.awt.Point[points.size()]))) {
            if (first) {
                convexHull.moveTo(p.x, p.y);
                first = false;
            } else {
                convexHull.lineTo(p.x, p.y);
            }
        }
        convexHull.closePath();
        return convexHull;
    }

    /**
     * Computes the convex hull from a shape.
     *
     * @param shape
     * 		an arbitray shape
     * @return convex hull of the points as a Polygon2D object.
     */
    public static java.awt.geom.Path2D.Double getConvexHullPath2D(java.awt.Shape shape) {
        java.util.List<java.awt.geom.Point2D.Double> points = new java.util.ArrayList<>();
        double[] coords = new double[6];
        for (java.awt.geom.PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            switch (i.currentSegment(coords)) {
                case java.awt.geom.PathIterator.SEG_CLOSE :
                    break;
                case java.awt.geom.PathIterator.SEG_MOVETO :
                case java.awt.geom.PathIterator.SEG_LINETO :
                    points.add(new java.awt.geom.Point2D.Double(coords[0], coords[1]));
                    break;
                case java.awt.geom.PathIterator.SEG_QUADTO :
                    points.add(new java.awt.geom.Point2D.Double(coords[0], coords[1]));
                    points.add(new java.awt.geom.Point2D.Double(coords[2], coords[3]));
                    break;
                case java.awt.geom.PathIterator.SEG_CUBICTO :
                    points.add(new java.awt.geom.Point2D.Double(coords[0], coords[1]));
                    points.add(new java.awt.geom.Point2D.Double(coords[2], coords[3]));
                    points.add(new java.awt.geom.Point2D.Double(coords[4], coords[5]));
                    break;
            }
        }
        java.awt.geom.Path2D.Double convexHull = new java.awt.geom.Path2D.Double();
        boolean first = true;
        for (java.awt.geom.Point2D.Double p : org.jhotdraw.geom.ConvexHull.getConvexHull2D(points.toArray(new java.awt.geom.Point2D.Double[points.size()]))) {
            if (first) {
                convexHull.moveTo(p.x, p.y);
                first = false;
            } else {
                convexHull.lineTo(p.x, p.y);
            }
        }
        convexHull.closePath();
        return convexHull;
    }

    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points
     */
    public static java.util.List<java.awt.Point> getConvexHull(java.util.List<java.awt.Point> points) {
        return java.util.Arrays.asList(org.jhotdraw.geom.ConvexHull.getConvexHull(points.toArray(new java.awt.Point[points.size()])));
    }

    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points
     */
    public static java.util.List<java.awt.geom.Point2D.Double> getConvexHull2D(java.util.List<java.awt.geom.Point2D.Double> points) {
        return java.util.Arrays.asList(org.jhotdraw.geom.ConvexHull.getConvexHull2D(points.toArray(new java.awt.geom.Point2D.Double[points.size()])));
    }

    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points
     */
    public static java.awt.Point[] getConvexHull(java.awt.Point[] points) {
        // Quickly return if no work is needed
        if (points.length < 3) {
            return points.clone();
        }
        // Sort points from left to right O(n log n)
        java.awt.Point[] sorted = points.clone();
        java.util.Arrays.sort(sorted, new java.util.Comparator<java.awt.Point>() {
            @java.lang.Override
            public int compare(java.awt.Point o1, java.awt.Point o2) {
                int v = o1.x - o2.x;
                return v == 0 ? o1.y - o2.y : v;
            }
        });
        java.awt.Point[] hull = new java.awt.Point[sorted.length + 2];
        // Process upper part of convex hull O(n)
        int upper = 0;// Number of points in upper part of convex hull

        hull[upper++] = sorted[0];
        hull[upper++] = sorted[1];
        for (int i = 2; i < sorted.length; i++) {
            hull[upper++] = sorted[i];
            while ((upper > 2) && (!org.jhotdraw.geom.ConvexHull.isRightTurn(hull[upper - 3], hull[upper - 2], hull[upper - 1]))) {
                hull[upper - 2] = hull[upper - 1];
                upper--;
            } 
        }
        // Process lower part of convex hull O(n)
        int lower = upper;// (lower - number + 1) = number of points in the lower part of the convex hull

        hull[lower++] = sorted[sorted.length - 2];
        for (int i = sorted.length - 3; i >= 0; i--) {
            hull[lower++] = sorted[i];
            while (((lower - upper) > 1) && (!org.jhotdraw.geom.ConvexHull.isRightTurn(hull[lower - 3], hull[lower - 2], hull[lower - 1]))) {
                hull[lower - 2] = hull[lower - 1];
                lower--;
            } 
        }
        lower -= 1;
        // Reduce array
        java.awt.Point[] convexHull = new java.awt.Point[lower];
        java.lang.System.arraycopy(hull, 0, convexHull, 0, lower);
        return convexHull;
    }

    /**
     * Returns true, if the three given points make a right turn.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @param p3
     * 		third point
     * @return true if right turn.
     */
    public static boolean isRightTurn(java.awt.Point p1, java.awt.Point p2, java.awt.Point p3) {
        if (p1.equals(p2) || p2.equals(p3)) {
            // no right turn if points are at same location
            return false;
        }
        double val = (((p2.x * p3.y) + (p1.x * p2.y)) + (p3.x * p1.y)) - (((p2.x * p1.y) + (p3.x * p2.y)) + (p1.x * p3.y));
        return val > 0;
    }

    /**
     * Computes the convex hull from a set of points.
     *
     * @param points
     * @return convex hull of the points
     */
    public static java.awt.geom.Point2D.Double[] getConvexHull2D(java.awt.geom.Point2D.Double[] points) {
        // Quickly return if no work is needed
        if (points.length < 3) {
            return points.clone();
        }
        // Sort points from left to right O(n log n)
        java.awt.geom.Point2D.Double[] sorted = points.clone();
        java.util.Arrays.sort(sorted, new java.util.Comparator<java.awt.geom.Point2D.Double>() {
            @java.lang.Override
            public int compare(java.awt.geom.Point2D.Double o1, java.awt.geom.Point2D.Double o2) {
                double v = o1.x - o2.x;
                if (v == 0) {
                    v = o1.y - o2.y;
                }
                return v > 0 ? 1 : v < 0 ? -1 : 0;
            }
        });
        java.awt.geom.Point2D.Double[] hull = new java.awt.geom.Point2D.Double[sorted.length + 2];
        // Process upper part of convex hull O(n)
        int upper = 0;// Number of points in upper part of convex hull

        hull[upper++] = sorted[0];
        hull[upper++] = sorted[1];
        for (int i = 2; i < sorted.length; i++) {
            hull[upper++] = sorted[i];
            while ((upper > 2) && (!org.jhotdraw.geom.ConvexHull.isRightTurn2D(hull[upper - 3], hull[upper - 2], hull[upper - 1]))) {
                hull[upper - 2] = hull[upper - 1];
                upper--;
            } 
        }
        // Process lower part of convex hull O(n)
        int lower = upper;// (lower - number + 1) = number of points in the lower part of the convex hull

        hull[lower++] = sorted[sorted.length - 2];
        for (int i = sorted.length - 3; i >= 0; i--) {
            hull[lower++] = sorted[i];
            while (((lower - upper) > 1) && (!org.jhotdraw.geom.ConvexHull.isRightTurn2D(hull[lower - 3], hull[lower - 2], hull[lower - 1]))) {
                hull[lower - 2] = hull[lower - 1];
                lower--;
            } 
        }
        lower -= 1;
        // Reduce array
        java.awt.geom.Point2D.Double[] convexHull = new java.awt.geom.Point2D.Double[lower];
        java.lang.System.arraycopy(hull, 0, convexHull, 0, lower);
        return convexHull;
    }

    /**
     * Returns true, if the three given points make a right turn.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @param p3
     * 		third point
     * @return true if right turn.
     */
    public static boolean isRightTurn2D(java.awt.geom.Point2D.Double p1, java.awt.geom.Point2D.Double p2, java.awt.geom.Point2D.Double p3) {
        if (p1.equals(p2) || p2.equals(p3)) {
            // no right turn if points are at same location
            return false;
        }
        double val = (((p2.x * p3.y) + (p1.x * p2.y)) + (p3.x * p1.y)) - (((p2.x * p1.y) + (p3.x * p2.y)) + (p1.x * p3.y));
        return val > 0;
    }
}