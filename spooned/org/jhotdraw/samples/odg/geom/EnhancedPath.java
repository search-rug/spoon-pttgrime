/* @(#)EnhancedPath.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.geom;
/**
 * Represents an ODG Enhanced Path.
 *
 * <p>The coordinates of a EnhancedPath.Segment can reference a formula or a modifier.
 */
public class EnhancedPath extends java.util.ArrayList<org.jhotdraw.samples.odg.geom.EnhancedPath.Segment> implements java.awt.Shape {
    private static final long serialVersionUID = 1L;

    public enum SegType {

        /* moveto x y */
        MOVETO(2),
        /* lineto x y */
        LINETO(2),
        /* curveto x1 y1 x2 y2 x y */
        CURVETO(6),
        /* quadto x1 y1 x y */
        QUADTO(4),
        /* closepath */
        CLOSE(0),
        /* ellipseto x y w h t0 t1 */
        ELLIPSETO(6),
        /* (counter-clockwise) arcto x1 y1 x2 y2 x3 y3 x y */
        ARCTO(8),
        /* clockwise arcto x1 y1 x2 y2 x3 y3 x y */
        CLOCKWISE_ARCTO(8),
        /* elliptical-quadrantx x y */
        QUADRANT_XTO(2),
        /* elliptical-quadranty x y */
        QUADRANT_YTO(2);

        /**
         * len is the number of parameters needed by a segment.
         */
        private int len;

        SegType(int len) {
            this.len = len;
        }

        int getLen() {
            return len;
        }
    }

    /**
     * We cache a Path2D.Double instance to speed up Shape operations.
     */
    private transient java.awt.geom.Path2D.Double generalPath;

    /**
     * We cache a Rectangle2D.Double instance to speed up getBounds operations.
     */
    private transient java.awt.geom.Rectangle2D.Double bounds;

    /**
     * The winding rule for filling the bezier path.
     */
    private int windingRule = java.awt.geom.Path2D.Double.WIND_EVEN_ODD;

    @java.lang.Override
    public java.awt.Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D getBounds2D() {
        if (bounds == null) {
        }
        return ((java.awt.geom.Rectangle2D.Double) (bounds.clone()));
    }

    @java.lang.Override
    public boolean contains(double x, double y, double w, double h) {
        validatePath();
        return generalPath.contains(x, y, w, h);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D p) {
        validatePath();
        return generalPath.contains(p);
    }

    @java.lang.Override
    public boolean contains(double x, double y) {
        validatePath();
        return generalPath.contains(x, y);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Rectangle2D r) {
        validatePath();
        return generalPath.contains(r);
    }

    @java.lang.Override
    public boolean intersects(java.awt.geom.Rectangle2D r) {
        validatePath();
        return generalPath.intersects(r);
    }

    @java.lang.Override
    public boolean intersects(double x, double y, double w, double h) {
        validatePath();
        return generalPath.intersects(x, y, w, h);
    }

    @java.lang.Override
    public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform at) {
        validatePath();
        return generalPath.getPathIterator(at);
    }

    @java.lang.Override
    public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform at, double flatness) {
        validatePath();
        return generalPath.getPathIterator(at, flatness);
    }

    /**
     * Defines a vertex (node) of the bezier path.
     *
     * <p>A vertex consists of three control points: C0, C1 and C2.
     *
     * <ul>
     *   <li>The bezier path always passes through C0.
     *   <li>C1 is used to control the curve towards C0.
     *   <li>C2 is used to control the curve going away from C0.
     * </ul>
     */
    public static class Segment implements java.lang.Cloneable {
        /**
         * The type of the segment.
         */
        public org.jhotdraw.samples.odg.geom.EnhancedPath.SegType type;

        /**
         * Control points x and y coordinates.
         */
        public double[] coords = new double[8];

        /**
         * Modifiers and formulas.
         */
        public java.lang.String[] modifiers = new java.lang.String[8];

        public Segment() {
            type = org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.LINETO;
        }

        /**
         * Creates a segment.
         */
        public Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType type, java.lang.Object... coordOrModifier) {
            this.type = type;
            for (int i = 0; i < coordOrModifier.length; i++) {
                if (coordOrModifier[i] instanceof java.lang.Double) {
                    coords[i] = ((java.lang.Double) (coordOrModifier[i]));
                } else {
                    modifiers[i] = ((java.lang.String) (coordOrModifier[i]));
                }
            }
        }

        public Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.Segment that) {
            setTo(that);
        }

        public void setTo(org.jhotdraw.samples.odg.geom.EnhancedPath.Segment that) {
            this.type = that.type;
            java.lang.System.arraycopy(that.coords, 0, this.coords, 0, that.type.getLen());
            java.lang.System.arraycopy(that.modifiers, 0, this.modifiers, 0, that.type.getLen());
        }

        @java.lang.Override
        public java.lang.Object clone() {
            try {
                org.jhotdraw.samples.odg.geom.EnhancedPath.Segment that = ((org.jhotdraw.samples.odg.geom.EnhancedPath.Segment) (super.clone()));
                that.coords = this.coords.clone();
                that.modifiers = this.modifiers.clone();
                return that;
            } catch (java.lang.CloneNotSupportedException e) {
                java.lang.InternalError error = new java.lang.InternalError();
                error.initCause(e);
                throw error;
            }
        }

        @java.lang.Override
        public int hashCode() {
            return ((type.hashCode() << 24) | (java.util.Arrays.hashCode(coords) & 0xfff0000)) | (java.util.Arrays.hashCode(modifiers) & 0xffff);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (o instanceof org.jhotdraw.samples.odg.geom.EnhancedPath.Segment) {
                org.jhotdraw.samples.odg.geom.EnhancedPath.Segment that = ((org.jhotdraw.samples.odg.geom.EnhancedPath.Segment) (o));
                return ((that.type == this.type) && java.util.Arrays.equals(that.coords, this.coords)) && java.util.Arrays.equals(that.modifiers, this.modifiers);
            }
            return false;
        }
    }

    /**
     * Recomputes the EnhancedPath, if it is invalid.
     */
    public void validatePath() {
        if (generalPath == null) {
            generalPath = toGeneralPath();
        }
    }

    /**
     * This must be called after the EnhancedPath has been changed.
     */
    public void invalidatePath() {
        generalPath = null;
        bounds = null;
    }

    /**
     * Converts the EnhancedPath into a Path2D.Double.
     */
    public java.awt.geom.Path2D.Double toGeneralPath() {
        java.awt.geom.Path2D.Double gp = new java.awt.geom.Path2D.Double();
        // XXX implement me
        return gp;
    }

    /**
     * Opens a new path segment at the specified position.
     */
    public void moveTo(java.lang.Object xm1, java.lang.Object ym1) {
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.MOVETO, xm1, ym1));
    }

    /**
     * Adds a line to the current path segment. This is only allowed, when the current path segment is
     * open.
     */
    public void lineTo(java.lang.Object x1, java.lang.Object y1) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("lineTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.LINETO, x1, y1));
    }

    /**
     * Closes the current path segment. This is only allowed, when the current path segment is open.
     */
    public void close() {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("close is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE));
    }

    /**
     * Adds a quadratic curve to the current path segment. This is only allowed, when the current path
     * segment is open.
     */
    public void quadTo(java.lang.Object x1, java.lang.Object y1, java.lang.Object x2, java.lang.Object y2) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("quadTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.QUADTO, x1, y1, x2, y2));
    }

    /**
     * Adds a cubic curve to the current path segment. This is only allowed, when the current path
     * segment is open.
     */
    public void curveTo(java.lang.Object x1, java.lang.Object y1, java.lang.Object x2, java.lang.Object y2, java.lang.Object x3, java.lang.Object y3) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("curveTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CURVETO, x1, y1, x2, y2, x3, y3));
    }

    /**
     * (x1, y1) and (x2, y2) is defining the bounding box of a ellipse. A line is then drawn from the
     * current point to the start angle of the arc that is specified by the radial vector of point
     * (x3, y3) and then counter clockwise to the end-angle that is specified by point (x4, y4).
     */
    public void arcTo(java.lang.Object x1, java.lang.Object y1, java.lang.Object x2, java.lang.Object y2, java.lang.Object x3, java.lang.Object y3, java.lang.Object x4, java.lang.Object y4) {
        if (size() == 0) {
            throw new java.awt.geom.IllegalPathStateException("arcTo only allowed when not empty");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.ARCTO, x1, y1, x2, y2, x3, y3, x4, y4));
    }

    public void clockwiseArcTo(java.lang.Object x1, java.lang.Object y1, java.lang.Object x2, java.lang.Object y2, java.lang.Object x3, java.lang.Object y3, java.lang.Object x4, java.lang.Object y4) {
        if (size() == 0) {
            throw new java.awt.geom.IllegalPathStateException("clockwiseArcTo only allowed when not empty");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOCKWISE_ARCTO, x1, y1, x2, y2, x3, y3, x4, y4));
    }

    /**
     * Draws a segment of an ellipse. The ellipse is specified by the center(x, y), the size(w, h) and
     * the start-angle t0 and end-angle t1.
     */
    public void ellipseTo(java.lang.Object x, java.lang.Object y, java.lang.Object w, java.lang.Object h, java.lang.Object t0, java.lang.Object t1) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("ellipseTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.ELLIPSETO, x, y, w, h, t0, t1));
    }

    public void quadrantXTo(java.lang.Object x, java.lang.Object y) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("quadrantXTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.QUADRANT_XTO, x, y));
    }

    public void quadrantYTo(java.lang.Object x, java.lang.Object y) {
        if ((size() == 0) || (get(size() - 1).type == org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.CLOSE)) {
            throw new java.awt.geom.IllegalPathStateException("quadrantYTo is only allowed when a path segment is open");
        }
        add(new org.jhotdraw.samples.odg.geom.EnhancedPath.Segment(org.jhotdraw.samples.odg.geom.EnhancedPath.SegType.QUADRANT_YTO, x, y));
    }

    /**
     * Sets winding rule for filling the bezier path.
     *
     * @param newValue
     * 		Must be Path2D.Double.WIND_EVEN_ODD or Path2D.Double.WIND_NON_ZERO.
     */
    public void setWindingRule(int newValue) {
        if (newValue != windingRule) {
            invalidatePath();
            int oldValue = windingRule;
            this.windingRule = newValue;
        }
    }

    /**
     * Gets winding rule for filling the bezier path.
     *
     * @return Path2D.Double.WIND_EVEN_ODD or Path2D.Double.WIND_NON_ZERO.
     */
    public int getWindingRule() {
        return windingRule;
    }
}