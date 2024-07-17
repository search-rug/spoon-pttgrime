/* @(#)Bezier.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.geom.path;
/**
 * Provides algorithms for fitting Bezier curves to a set of digitized points.
 *
 * <p>Source:<br>
 * Phoenix: An Interactive Curve Design System Based on the Automatic Fitting of Hand-Sketched
 * Curves.<br>
 * © Copyright by Philip J. Schneider 1988.<br>
 * A thesis submitted in partial fulfillment of the requirements for the degree of Master of
 * Science, University of Washington.
 *
 * <p>http://autotrace.sourceforge.net/Interactive_Curve_Design.ps.gz
 */
public class Bezier {
    /**
     * Prevent instance creation.
     */
    private Bezier() {
    }

    public static void main(java.lang.String[] args) {
        java.util.ArrayList<java.awt.geom.Point2D.Double> d = new java.util.ArrayList<>();
        d.add(new java.awt.geom.Point2D.Double(0, 0));
        d.add(new java.awt.geom.Point2D.Double(5, 1));
        d.add(new java.awt.geom.Point2D.Double(10, 0));
        d.add(new java.awt.geom.Point2D.Double(10, 10));
        d.add(new java.awt.geom.Point2D.Double(0, 10));
        d.add(new java.awt.geom.Point2D.Double(0, 0));
        java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>> segments = org.jhotdraw.geom.path.Bezier.splitAtCorners(d, (45 / 180.0) * java.lang.Math.PI, 2.0);
        for (java.util.ArrayList<java.awt.geom.Point2D.Double> seg : segments) {
            for (int i = 0; i < 2; i++) {
                seg = org.jhotdraw.geom.path.Bezier.reduceNoise(seg, 0.8);
            }
        }
    }

    /**
     * Fits a bezier path to the specified list of digitized points.
     *
     * <p>This is a convenience method for calling {@link #fitBezierPath}
     *
     * @param digitizedPoints
     * 		digited points.
     * @param error
     * 		the maximal allowed error between the bezier path and the digitized points.
     */
    public static org.jhotdraw.geom.path.BezierPath fitBezierPath(java.awt.geom.Point2D.Double[] digitizedPoints, double error) {
        return org.jhotdraw.geom.path.Bezier.fitBezierPath(java.util.Arrays.asList(digitizedPoints), error);
    }

    /**
     * Fits a bezier path to the specified list of digitized points.
     *
     * @param digitizedPoints
     * 		digited points.
     * @param error
     * 		the maximal allowed error between the bezier path and the digitized points.
     */
    public static org.jhotdraw.geom.path.BezierPath fitBezierPath(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints, double error) {
        // Split into segments at corners
        java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>> segments;
        segments = org.jhotdraw.geom.path.Bezier.splitAtCorners(digitizedPoints, (77 / 180.0) * java.lang.Math.PI, error * error);
        // Clean up the data in the segments
        for (int i = 0, n = segments.size(); i < n; i++) {
            java.util.ArrayList<java.awt.geom.Point2D.Double> seg = segments.get(i);
            seg = org.jhotdraw.geom.path.Bezier.removeClosePoints(seg, error * 2);
            seg = org.jhotdraw.geom.path.Bezier.reduceNoise(seg, 0.8);
            segments.set(i, seg);
        }
        // Create fitted bezier path
        org.jhotdraw.geom.path.BezierPath fittedPath = new org.jhotdraw.geom.path.BezierPath();
        // Quickly deal with empty dataset
        boolean isEmpty = false;
        for (java.util.ArrayList<java.awt.geom.Point2D.Double> seg : segments) {
            if (seg.isEmpty()) {
                isEmpty = false;
                break;
            }
        }
        if (!isEmpty) {
            // Process each segment of digitized points
            double errorSquared = error * error;
            for (java.util.ArrayList<java.awt.geom.Point2D.Double> seg : segments) {
                switch (seg.size()) {
                    case 0 :
                        break;
                    case 1 :
                        fittedPath.add(seg.get(0));
                        break;
                    case 2 :
                        if (fittedPath.size() == 0) {
                            fittedPath.add(seg.get(0));
                        }
                        fittedPath.lineTo(seg.get(1).x, seg.get(1).y);
                        break;
                    default :
                        if (fittedPath.size() == 0) {
                            fittedPath.add(seg.get(0));
                        }
                        /* Unit tangent vectors at endpoints */
                        java.awt.geom.Point2D.Double tHat1;
                        java.awt.geom.Point2D.Double tHat2;
                        tHat1 = org.jhotdraw.geom.path.Bezier.computeLeftTangent(seg, 0);
                        tHat2 = org.jhotdraw.geom.path.Bezier.computeRightTangent(seg, seg.size() - 1);
                        org.jhotdraw.geom.path.Bezier.fitCubic(seg, 0, seg.size() - 1, tHat1, tHat2, errorSquared, fittedPath);
                        break;
                }
            }
        }
        return fittedPath;
    }

    /**
     * Fits a bezier path to the specified list of digitized points.
     *
     * <p>This is a convenience method for calling {@link #fitBezierPath}.
     *
     * @param digitizedPoints
     * 		digited points.
     * @param error
     * 		the maximal allowed error between the bezier path and the digitized points.
     */
    public static org.jhotdraw.geom.path.BezierPath fitBezierPath(org.jhotdraw.geom.path.BezierPath digitizedPoints, double error) {
        java.util.ArrayList<java.awt.geom.Point2D.Double> d = new java.util.ArrayList<>(digitizedPoints.size());
        for (org.jhotdraw.geom.path.BezierPath.Node n : digitizedPoints.nodes()) {
            d.add(new java.awt.geom.Point2D.Double(n.x[0], n.y[0]));
        }
        return org.jhotdraw.geom.path.Bezier.fitBezierPath(d, error);
    }

    /**
     * Removes points which are closer together than the specified minimal distance.
     *
     * <p>The minimal distance should be chosen dependent on the size and resolution of the display
     * device, and on the sampling rate. A good value for mouse input on a display with 100% Zoom
     * factor is 2.
     *
     * <p>The purpose of this method, is to remove points, which add no additional information about
     * the shape of the curve from the list of digitized points.
     *
     * <p>The cleaned up set of digitized points gives better results, when used as input for method
     * {@link #splitAtCorners}.
     *
     * @param digitizedPoints
     * 		Digitized points
     * @param minDistance
     * 		minimal distance between two points. If minDistance is 0, this method only
     * 		removes sequences of coincident points.
     * @return Digitized points with a minimal distance.
     */
    public static java.util.ArrayList<java.awt.geom.Point2D.Double> removeClosePoints(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints, double minDistance) {
        if (minDistance == 0) {
            return org.jhotdraw.geom.path.Bezier.removeCoincidentPoints(digitizedPoints);
        } else {
            double squaredDistance = minDistance * minDistance;
            java.util.ArrayList<java.awt.geom.Point2D.Double> cleaned = new java.util.ArrayList<>();
            if (digitizedPoints.size() > 0) {
                java.awt.geom.Point2D.Double prev = digitizedPoints.get(0);
                cleaned.add(prev);
                for (java.awt.geom.Point2D.Double p : digitizedPoints) {
                    if (org.jhotdraw.geom.path.Bezier.v2SquaredDistanceBetween2Points(prev, p) > squaredDistance) {
                        cleaned.add(p);
                        prev = p;
                    }
                }
                if (!prev.equals(digitizedPoints.get(digitizedPoints.size() - 1))) {
                    cleaned.set(cleaned.size() - 1, digitizedPoints.get(digitizedPoints.size() - 1));
                }
            }
            return cleaned;
        }
    }

    /**
     * Removes sequences of coincident points.
     *
     * <p>The purpose of this method, is to clean up a list of digitized points for later processing
     * using method {@link #splitAtCorners}.
     *
     * <p>Use this method only, if you know that the digitized points contain no quantization errors -
     * which is never the case, unless you want to debug the curve fitting algorithm of this class.
     *
     * @param digitizedPoints
     * 		Digitized points
     * @return Digitized points without subsequent duplicates.
     */
    private static java.util.ArrayList<java.awt.geom.Point2D.Double> removeCoincidentPoints(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints) {
        java.util.ArrayList<java.awt.geom.Point2D.Double> cleaned = new java.util.ArrayList<>();
        if (digitizedPoints.size() > 0) {
            java.awt.geom.Point2D.Double prev = digitizedPoints.get(0);
            cleaned.add(prev);
            for (java.awt.geom.Point2D.Double p : digitizedPoints) {
                if (!prev.equals(p)) {
                    cleaned.add(p);
                    prev = p;
                }
            }
        }
        return cleaned;
    }

    /**
     * Splits the digitized points into multiple segments at each corner point.
     *
     * <p>Corner points are both contained as the last point of a segment and the first point of a
     * subsequent segment.
     *
     * @param digitizedPoints
     * 		Digitized points
     * @param maxAngle
     * 		maximal angle in radians between the current point and its predecessor and
     * 		successor up to which the point does not break the digitized list into segments.
     * 		Recommended value 44° = 44 * 180d / Math.PI
     * @return Segments of digitized points, each segment having less than maximal angle between
    points.
     */
    public static java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>> splitAtCorners(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints, double maxAngle, double minDistance) {
        java.util.ArrayList<java.lang.Integer> cornerIndices = org.jhotdraw.geom.path.Bezier.findCorners(digitizedPoints, maxAngle, minDistance);
        java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>> segments = new java.util.ArrayList<>(cornerIndices.size() + 1);
        if (cornerIndices.size() == 0) {
            segments.add(new java.util.ArrayList<>(digitizedPoints));
        } else {
            segments.add(new java.util.ArrayList<>(digitizedPoints.subList(0, cornerIndices.get(0) + 1)));
            for (int i = 1; i < cornerIndices.size(); i++) {
                segments.add(new java.util.ArrayList<>(digitizedPoints.subList(cornerIndices.get(i - 1), cornerIndices.get(i) + 1)));
            }
            segments.add(new java.util.ArrayList<>(digitizedPoints.subList(cornerIndices.get(cornerIndices.size() - 1), digitizedPoints.size())));
        }
        return segments;
    }

    /**
     * Finds corners in the provided point list, and returns their indices.
     *
     * @param digitizedPoints
     * 		List of digitized points.
     * @param minAngle
     * 		Minimal angle for corner points
     * @param minDistance
     * 		Minimal distance between a point and adjacent points for corner detection
     * @return list of corner indices.
     */
    public static java.util.ArrayList<java.lang.Integer> findCorners(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints, double minAngle, double minDistance) {
        java.util.ArrayList<java.lang.Integer> cornerIndices = new java.util.ArrayList<>();
        double squaredDistance = minDistance * minDistance;
        int previousCorner = -1;
        double previousCornerAngle = 0;
        for (int i = 1, n = digitizedPoints.size(); i < (n - 1); i++) {
            java.awt.geom.Point2D.Double p = digitizedPoints.get(i);
            // search for a preceding point for corner detection
            java.awt.geom.Point2D.Double prev = null;
            boolean intersectsPreviousCorner = false;
            for (int j = i - 1; j >= 0; j--) {
                if ((j == previousCorner) || (org.jhotdraw.geom.path.Bezier.v2SquaredDistanceBetween2Points(digitizedPoints.get(j), p) >= squaredDistance)) {
                    prev = digitizedPoints.get(j);
                    intersectsPreviousCorner = j < previousCorner;
                    break;
                }
            }
            if (prev == null) {
                continue;
            }
            // search for a succeeding point for corner detection
            java.awt.geom.Point2D.Double next = null;
            for (int j = i + 1; j < n; j++) {
                if (org.jhotdraw.geom.path.Bezier.v2SquaredDistanceBetween2Points(digitizedPoints.get(j), p) >= squaredDistance) {
                    next = digitizedPoints.get(j);
                    break;
                }
            }
            if (next == null) {
                continue;
            }
            double aPrev = java.lang.Math.atan2(prev.y - p.y, prev.x - p.x);
            double aNext = java.lang.Math.atan2(next.y - p.y, next.x - p.x);
            double angle = java.lang.Math.abs(aPrev - aNext);
            if ((angle < (java.lang.Math.PI - minAngle)) || (angle > (java.lang.Math.PI + minAngle))) {
                if (intersectsPreviousCorner) {
                    cornerIndices.set(cornerIndices.size() - 1, i);
                } else {
                    cornerIndices.add(i);
                }
                previousCorner = i;
                previousCornerAngle = angle;
            }
        }
        return cornerIndices;
    }

    /**
     * Reduces noise from the digitized points, by applying an approximation of a gaussian filter to
     * the data.
     *
     * <p>The filter does the following for each point P, with weight 0.5:
     *
     * <p>x[i] = 0.5*x[i] + 0.25*x[i-1] + 0.25*x[i+1]; y[i] = 0.5*y[i] + 0.25*y[i-1] + 0.25*y[i+1];
     *
     * @param digitizedPoints
     * 		Digitized points
     * @param weight
     * 		Weight of the current point
     * @return Digitized points with reduced noise.
     */
    public static java.util.ArrayList<java.awt.geom.Point2D.Double> reduceNoise(java.util.List<java.awt.geom.Point2D.Double> digitizedPoints, double weight) {
        java.util.ArrayList<java.awt.geom.Point2D.Double> cleaned = new java.util.ArrayList<>();
        if (digitizedPoints.size() > 0) {
            java.awt.geom.Point2D.Double prev = digitizedPoints.get(0);
            cleaned.add(prev);
            double pnWeight = (1.0 - weight) / 2.0;// weight of previous and next

            for (int i = 1, n = digitizedPoints.size() - 1; i < n; i++) {
                java.awt.geom.Point2D.Double cur = digitizedPoints.get(i);
                java.awt.geom.Point2D.Double next = digitizedPoints.get(i + 1);
                cleaned.add(new java.awt.geom.Point2D.Double(((cur.x * weight) + (pnWeight * prev.x)) + (pnWeight * next.x), ((cur.y * weight) + (pnWeight * prev.y)) + (pnWeight * next.y)));
                prev = cur;
            }
            if (digitizedPoints.size() > 1) {
                cleaned.add(digitizedPoints.get(digitizedPoints.size() - 1));
            }
        }
        return cleaned;
    }

    /**
     * Fit one or multiple subsequent cubic bezier curves to a (sub)set of digitized points. The
     * digitized points represent a smooth curve without corners.
     *
     * @param d
     * 		Array of digitized points. Must not contain subsequent coincident points.
     * @param first
     * 		Indice of first point in d.
     * @param last
     * 		Indice of last point in d.
     * @param tHat1
     * 		Unit tangent vectors at start point.
     * @param tHat2
     * 		Unit tanget vector at end point.
     * @param errorSquared
     * 		User-defined errorSquared squared.
     * @param bezierPath
     * 		Path to which the bezier curve segments are added.
     */
    private static void fitCubic(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int first, int last, java.awt.geom.Point2D.Double tHat1, java.awt.geom.Point2D.Double tHat2, double errorSquared, org.jhotdraw.geom.path.BezierPath bezierPath) {
        java.awt.geom.Point2D.Double[] bezCurve;
        /* Control points of fitted Bezier curve */
        double[] u;
        /* Parameter values for point */
        double maxError;
        /* Maximum fitting errorSquared */
        int[] splitPoint = new int[1];
        /* Point to split point set at.
        This is an array of size one, because we need it as an input/output parameter.
         */
        int nPts;
        /* Number of points in subset */
        double iterationError;
        /* Error below which you try iterating */
        int maxIterations = 4;
        /* Max times to try iterating */
        java.awt.geom.Point2D.Double tHatCenter;
        /* Unit tangent vector at splitPoint */
        int i;
        // clone unit tangent vectors, so that we can alter their coordinates
        // without affecting the input values.
        tHat1 = ((java.awt.geom.Point2D.Double) (tHat1.clone()));
        tHat2 = ((java.awt.geom.Point2D.Double) (tHat2.clone()));
        iterationError = errorSquared * errorSquared;
        nPts = (last - first) + 1;
        /* Use heuristic if region only has two points in it */
        if (nPts == 2) {
            double dist = org.jhotdraw.geom.path.Bezier.v2DistanceBetween2Points(d.get(last), d.get(first)) / 3.0;
            bezCurve = new java.awt.geom.Point2D.Double[4];
            for (i = 0; i < bezCurve.length; i++) {
                bezCurve[i] = new java.awt.geom.Point2D.Double();
            }
            bezCurve[0] = d.get(first);
            bezCurve[3] = d.get(last);
            org.jhotdraw.geom.path.Bezier.v2Add(bezCurve[0], org.jhotdraw.geom.path.Bezier.v2Scale(tHat1, dist), bezCurve[1]);
            org.jhotdraw.geom.path.Bezier.v2Add(bezCurve[3], org.jhotdraw.geom.path.Bezier.v2Scale(tHat2, dist), bezCurve[2]);
            bezierPath.curveTo(bezCurve[1].x, bezCurve[1].y, bezCurve[2].x, bezCurve[2].y, bezCurve[3].x, bezCurve[3].y);
            return;
        }
        /* Parameterize points, and attempt to fit curve */
        u = org.jhotdraw.geom.path.Bezier.chordLengthParameterize(d, first, last);
        bezCurve = org.jhotdraw.geom.path.Bezier.generateBezier(d, first, last, u, tHat1, tHat2);
        /* Find max deviation of points to fitted curve */
        maxError = org.jhotdraw.geom.path.Bezier.computeMaxError(d, first, last, bezCurve, u, splitPoint);
        if (maxError < errorSquared) {
            org.jhotdraw.geom.path.Bezier.addCurveTo(bezCurve, bezierPath, errorSquared, (first == 0) && (last == (d.size() - 1)));
            return;
        }
        /* If errorSquared not too large, try some reparameterization */
        /* and iteration */
        if (maxError < iterationError) {
            double[] uPrime;
            /* Improved parameter values */
            for (i = 0; i < maxIterations; i++) {
                uPrime = org.jhotdraw.geom.path.Bezier.reparameterize(d, first, last, u, bezCurve);
                bezCurve = org.jhotdraw.geom.path.Bezier.generateBezier(d, first, last, uPrime, tHat1, tHat2);
                maxError = org.jhotdraw.geom.path.Bezier.computeMaxError(d, first, last, bezCurve, uPrime, splitPoint);
                if (maxError < errorSquared) {
                    org.jhotdraw.geom.path.Bezier.addCurveTo(bezCurve, bezierPath, errorSquared, (first == 0) && (last == (d.size() - 1)));
                    return;
                }
                u = uPrime;
            }
        }
        /* Fitting failed -- split at max errorSquared point and fit recursively */
        tHatCenter = org.jhotdraw.geom.path.Bezier.computeCenterTangent(d, splitPoint[0]);
        if (first < splitPoint[0]) {
            org.jhotdraw.geom.path.Bezier.fitCubic(d, first, splitPoint[0], tHat1, tHatCenter, errorSquared, bezierPath);
        } else {
            bezierPath.lineTo(d.get(splitPoint[0]).x, d.get(splitPoint[0]).y);
            // System.err.println("Can't split any further " + first + ".." + splitPoint[0]);
        }
        org.jhotdraw.geom.path.Bezier.v2Negate(tHatCenter);
        if (splitPoint[0] < last) {
            org.jhotdraw.geom.path.Bezier.fitCubic(d, splitPoint[0], last, tHatCenter, tHat2, errorSquared, bezierPath);
        } else {
            bezierPath.lineTo(d.get(last).x, d.get(last).y);
            // System.err.println("Can't split any further " + splitPoint[0] + ".." + last);
        }
    }

    /**
     * Adds the curve to the bezier path.
     *
     * @param bezCurve
     * @param bezierPath
     */
    private static void addCurveTo(java.awt.geom.Point2D.Double[] bezCurve, org.jhotdraw.geom.path.BezierPath bezierPath, double errorSquared, boolean connectsCorners) {
        org.jhotdraw.geom.path.BezierPath.Node lastNode = bezierPath.nodes().get(bezierPath.size() - 1);
        double error = java.lang.Math.sqrt(errorSquared);
        if ((connectsCorners && org.jhotdraw.geom.Geom.lineContainsPoint(lastNode.x[0], lastNode.y[0], bezCurve[3].x, bezCurve[3].y, bezCurve[1].x, bezCurve[1].y, error)) && org.jhotdraw.geom.Geom.lineContainsPoint(lastNode.x[0], lastNode.y[0], bezCurve[3].x, bezCurve[3].y, bezCurve[2].x, bezCurve[2].y, error)) {
            bezierPath.lineTo(bezCurve[3].x, bezCurve[3].y);
        } else {
            bezierPath.curveTo(bezCurve[1].x, bezCurve[1].y, bezCurve[2].x, bezCurve[2].y, bezCurve[3].x, bezCurve[3].y);
        }
    }

    /**
     * Approximate unit tangents at "left" endpoint of digitized curve.
     *
     * @param d
     * 		Digitized points.
     * @param end
     * 		Index to "left" end of region.
     */
    private static java.awt.geom.Point2D.Double computeLeftTangent(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int end) {
        java.awt.geom.Point2D.Double tHat1;
        tHat1 = org.jhotdraw.geom.path.Bezier.v2SubII(d.get(end + 1), d.get(end));
        tHat1 = org.jhotdraw.geom.path.Bezier.v2Normalize(tHat1);
        return tHat1;
    }

    /**
     * Approximate unit tangents at "right" endpoint of digitized curve.
     *
     * @param d
     * 		Digitized points.
     * @param end
     * 		Index to "right" end of region.
     */
    private static java.awt.geom.Point2D.Double computeRightTangent(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int end) {
        java.awt.geom.Point2D.Double tHat2;
        tHat2 = org.jhotdraw.geom.path.Bezier.v2SubII(d.get(end - 1), d.get(end));
        tHat2 = org.jhotdraw.geom.path.Bezier.v2Normalize(tHat2);
        return tHat2;
    }

    /**
     * Approximate unit tangents at "center" of digitized curve.
     *
     * @param d
     * 		Digitized points.
     * @param center
     * 		Index to "center" end of region.
     */
    private static java.awt.geom.Point2D.Double computeCenterTangent(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int center) {
        java.awt.geom.Point2D.Double V1;
        java.awt.geom.Point2D.Double V2;
        java.awt.geom.Point2D.Double tHatCenter = new java.awt.geom.Point2D.Double();
        V1 = org.jhotdraw.geom.path.Bezier.v2SubII(d.get(center - 1), d.get(center));
        V2 = org.jhotdraw.geom.path.Bezier.v2SubII(d.get(center), d.get(center + 1));
        tHatCenter.x = (V1.x + V2.x) / 2.0;
        tHatCenter.y = (V1.y + V2.y) / 2.0;
        tHatCenter = org.jhotdraw.geom.path.Bezier.v2Normalize(tHatCenter);
        return tHatCenter;
    }

    /**
     * Assign parameter values to digitized points using relative distances between points.
     *
     * @param d
     * 		Digitized points.
     * @param first
     * 		Indice of first point of region in d.
     * @param last
     * 		Indice of last point of region in d.
     */
    private static double[] chordLengthParameterize(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int first, int last) {
        int i;
        double[] u;
        /* Parameterization */
        u = new double[(last - first) + 1];
        u[0] = 0.0;
        for (i = first + 1; i <= last; i++) {
            u[i - first] = u[(i - first) - 1] + org.jhotdraw.geom.path.Bezier.v2DistanceBetween2Points(d.get(i), d.get(i - 1));
        }
        for (i = first + 1; i <= last; i++) {
            u[i - first] = u[i - first] / u[last - first];
        }
        return u;
    }

    /**
     * Given set of points and their parameterization, try to find a better parameterization.
     *
     * @param d
     * 		Array of digitized points.
     * @param first
     * 		Indice of first point of region in d.
     * @param last
     * 		Indice of last point of region in d.
     * @param u
     * 		Current parameter values.
     * @param bezCurve
     * 		Current fitted curve.
     */
    private static double[] reparameterize(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int first, int last, double[] u, java.awt.geom.Point2D.Double[] bezCurve) {
        int nPts = (last - first) + 1;
        int i;
        double[] uPrime;
        /* New parameter values */
        uPrime = new double[nPts];
        for (i = first; i <= last; i++) {
            uPrime[i - first] = org.jhotdraw.geom.path.Bezier.newtonRaphsonRootFind(bezCurve, d.get(i), u[i - first]);
        }
        return uPrime;
    }

    /**
     * Use Newton-Raphson iteration to find better root.
     *
     * @param Q
     * 		Current fitted bezier curve.
     * @param P
     * 		Digitized point.
     * @param u
     * 		Parameter value vor P.
     */
    private static double newtonRaphsonRootFind(java.awt.geom.Point2D.Double[] Q, java.awt.geom.Point2D.Double P, double u) {
        double numerator;
        double denominator;
        java.awt.geom.Point2D.Double[] Q1 = new java.awt.geom.Point2D.Double[3];
        java.awt.geom.Point2D.Double[] Q2 = new java.awt.geom.Point2D.Double[2];
        /* Q' and Q'' */
        java.awt.geom.Point2D.Double Q_u;
        java.awt.geom.Point2D.Double Q1_u;
        java.awt.geom.Point2D.Double Q2_u;
        /* u evaluated at Q, Q', & Q'' */
        double uPrime;
        /* Improved u */
        int i;
        /* Compute Q(u) */
        Q_u = org.jhotdraw.geom.path.Bezier.bezierII(3, Q, u);
        /* Generate control vertices for Q' */
        for (i = 0; i <= 2; i++) {
            Q1[i] = new java.awt.geom.Point2D.Double((Q[i + 1].x - Q[i].x) * 3.0, (Q[i + 1].y - Q[i].y) * 3.0);
        }
        /* Generate control vertices for Q'' */
        for (i = 0; i <= 1; i++) {
            Q2[i] = new java.awt.geom.Point2D.Double((Q1[i + 1].x - Q1[i].x) * 2.0, (Q1[i + 1].y - Q1[i].y) * 2.0);
        }
        /* Compute Q'(u) and Q''(u) */
        Q1_u = org.jhotdraw.geom.path.Bezier.bezierII(2, Q1, u);
        Q2_u = org.jhotdraw.geom.path.Bezier.bezierII(1, Q2, u);
        /* Compute f(u)/f'(u) */
        numerator = ((Q_u.x - P.x) * Q1_u.x) + ((Q_u.y - P.y) * Q1_u.y);
        denominator = (((Q1_u.x * Q1_u.x) + (Q1_u.y * Q1_u.y)) + ((Q_u.x - P.x) * Q2_u.x)) + ((Q_u.y - P.y) * Q2_u.y);
        /* u = u - f(u)/f'(u) */
        uPrime = u - (numerator / denominator);
        return uPrime;
    }

    /**
     * Find the maximum squared distance of digitized points to fitted curve.
     *
     * @param d
     * 		Digitized points.
     * @param first
     * 		Indice of first point of region in d.
     * @param last
     * 		Indice of last point of region in d.
     * @param bezCurve
     * 		Fitted Bezier curve
     * @param u
     * 		Parameterization of points
     * @param splitPoint
     * 		Point of maximum error (input/output parameter, must be an array of 1)
     */
    private static double computeMaxError(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int first, int last, java.awt.geom.Point2D.Double[] bezCurve, double[] u, int[] splitPoint) {
        int i;
        double maxDist;
        /* Maximum error */
        double dist;
        /* Current error */
        java.awt.geom.Point2D.Double P;
        /* Point on curve */
        java.awt.geom.Point2D.Double v;
        /* Vector from point to curve */
        splitPoint[0] = ((last - first) + 1) / 2;
        maxDist = 0.0;
        for (i = first + 1; i < last; i++) {
            P = org.jhotdraw.geom.path.Bezier.bezierII(3, bezCurve, u[i - first]);
            v = org.jhotdraw.geom.path.Bezier.v2SubII(P, d.get(i));
            dist = org.jhotdraw.geom.path.Bezier.v2SquaredLength(v);
            if (dist >= maxDist) {
                maxDist = dist;
                splitPoint[0] = i;
            }
        }
        return maxDist;
    }

    /**
     * Use least-squares method to find Bezier control points for region.
     *
     * @param d
     * 		Array of digitized points.
     * @param first
     * 		Indice of first point in d.
     * @param last
     * 		Indice of last point in d.
     * @param uPrime
     * 		Parameter values for region .
     * @param tHat1
     * 		Unit tangent vectors at start point.
     * @param tHat2
     * 		Unit tanget vector at end point.
     * @return A cubic bezier curve consisting of 4 control points.
     */
    private static java.awt.geom.Point2D.Double[] generateBezier(java.util.ArrayList<java.awt.geom.Point2D.Double> d, int first, int last, double[] uPrime, java.awt.geom.Point2D.Double tHat1, java.awt.geom.Point2D.Double tHat2) {
        java.awt.geom.Point2D.Double[] bezCurve;
        bezCurve = new java.awt.geom.Point2D.Double[4];
        for (int i = 0; i < bezCurve.length; i++) {
            bezCurve[i] = new java.awt.geom.Point2D.Double();
        }
        /* Use the Wu/Barsky heuristic */
        double dist = org.jhotdraw.geom.path.Bezier.v2DistanceBetween2Points(d.get(last), d.get(first)) / 3.0;
        bezCurve[0] = d.get(first);
        bezCurve[3] = d.get(last);
        org.jhotdraw.geom.path.Bezier.v2Add(bezCurve[0], org.jhotdraw.geom.path.Bezier.v2Scale(tHat1, dist), bezCurve[1]);
        org.jhotdraw.geom.path.Bezier.v2Add(bezCurve[3], org.jhotdraw.geom.path.Bezier.v2Scale(tHat2, dist), bezCurve[2]);
        return bezCurve;
    }

    /**
     * Evaluate a Bezier curve at a particular parameter value.
     *
     * @param degree
     * 		The degree of the bezier curve.
     * @param V
     * 		Array of control points.
     * @param t
     * 		Parametric value to find point for.
     */
    private static java.awt.geom.Point2D.Double bezierII(int degree, java.awt.geom.Point2D.Double[] V, double t) {
        int i;
        int j;
        java.awt.geom.Point2D.Double q;
        /* Point on curve at parameter t */
        java.awt.geom.Point2D.Double[] vTemp;
        /* Local copy of control points */
        /* Copy array */
        vTemp = new java.awt.geom.Point2D.Double[degree + 1];
        for (i = 0; i <= degree; i++) {
            vTemp[i] = ((java.awt.geom.Point2D.Double) (V[i].clone()));
        }
        /* Triangle computation */
        for (i = 1; i <= degree; i++) {
            for (j = 0; j <= (degree - i); j++) {
                vTemp[j].x = ((1.0 - t) * vTemp[j].x) + (t * vTemp[j + 1].x);
                vTemp[j].y = ((1.0 - t) * vTemp[j].y) + (t * vTemp[j + 1].y);
            }
        }
        q = vTemp[0];
        return q;
    }

    /* -------------------------------------------------------------------------
    GraphicsGems.c
    2d and 3d Vector C Library
    by Andrew Glassner
    from "Graphics Gems", Academic Press, 1990
    -------------------------------------------------------------------------
     */
    /**
     * Return the distance between two points
     */
    private static double v2DistanceBetween2Points(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b) {
        return java.lang.Math.sqrt(org.jhotdraw.geom.path.Bezier.v2SquaredDistanceBetween2Points(a, b));
    }

    /**
     * Return the distance between two points
     */
    private static double v2SquaredDistanceBetween2Points(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (dx * dx) + (dy * dy);
    }

    /**
     * Scales the input vector to the new length and returns it.
     *
     * <p>This method alters the value of the input point!
     */
    private static java.awt.geom.Point2D.Double v2Scale(java.awt.geom.Point2D.Double v, double newlen) {
        double len = org.jhotdraw.geom.path.Bezier.v2Length(v);
        if (len != 0.0) {
            v.x *= newlen / len;
            v.y *= newlen / len;
        }
        return v;
    }

    /**
     * Scales the input vector by the specified factor and returns it.
     *
     * <p>This method alters the value of the input point!
     */
    private static java.awt.geom.Point2D.Double v2ScaleIII(java.awt.geom.Point2D.Double v, double s) {
        java.awt.geom.Point2D.Double result = new java.awt.geom.Point2D.Double();
        result.x = v.x * s;
        result.y = v.y * s;
        return result;
    }

    /**
     * Returns length of input vector.
     */
    private static double v2Length(java.awt.geom.Point2D.Double a) {
        return java.lang.Math.sqrt(org.jhotdraw.geom.path.Bezier.v2SquaredLength(a));
    }

    /**
     * Returns squared length of input vector.
     */
    private static double v2SquaredLength(java.awt.geom.Point2D.Double a) {
        return (a.x * a.x) + (a.y * a.y);
    }

    /**
     * Return vector sum c = a+b.
     *
     * <p>This method alters the value of c.
     */
    private static java.awt.geom.Point2D.Double v2Add(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b, java.awt.geom.Point2D.Double c) {
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        return c;
    }

    /**
     * Return vector sum = a+b.
     */
    private static java.awt.geom.Point2D.Double v2AddII(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b) {
        java.awt.geom.Point2D.Double c = new java.awt.geom.Point2D.Double();
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        return c;
    }

    /**
     * Negates the input vector and returns it.
     */
    private static java.awt.geom.Point2D.Double v2Negate(java.awt.geom.Point2D.Double v) {
        v.x = -v.x;
        v.y = -v.y;
        return v;
    }

    /**
     * Return the dot product of vectors a and b.
     */
    private static double v2Dot(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b) {
        return (a.x * b.x) + (a.y * b.y);
    }

    /**
     * Normalizes the input vector and returns it.
     */
    private static java.awt.geom.Point2D.Double v2Normalize(java.awt.geom.Point2D.Double v) {
        double len = org.jhotdraw.geom.path.Bezier.v2Length(v);
        if (len != 0.0) {
            v.x /= len;
            v.y /= len;
        }
        return v;
    }

    /**
     * Subtract Vector a from Vector b.
     *
     * @param a
     * 		Vector a - the value is not changed by this method
     * @param b
     * 		Vector b - the value is not changed by this method
     * @return Vector a subtracted by Vector v.
     */
    private static java.awt.geom.Point2D.Double v2SubII(java.awt.geom.Point2D.Double a, java.awt.geom.Point2D.Double b) {
        java.awt.geom.Point2D.Double c = new java.awt.geom.Point2D.Double();
        c.x = a.x - b.x;
        c.y = a.y - b.y;
        return c;
    }

    /**
     * B0, B1, B2, B3 : Bezier multipliers
     */
    private static double b0(double u) {
        double tmp = 1.0 - u;
        return (tmp * tmp) * tmp;
    }

    private static double b1(double u) {
        double tmp = 1.0 - u;
        return (3 * u) * (tmp * tmp);
    }

    private static double b2(double u) {
        double tmp = 1.0 - u;
        return ((3 * u) * u) * tmp;
    }

    private static double b3(double u) {
        return (u * u) * u;
    }
}