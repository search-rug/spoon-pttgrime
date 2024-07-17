/* Copyright (C) 2023 JHotDraw.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
 */
package org.jhotdraw.geom.path;
/**
 * Path storage as a List of Nodes. This is simply a decorator around a real Path2D.Double.
 */
public class MutablePath2D implements java.awt.Shape {
    protected final java.util.List<org.jhotdraw.geom.path.MutablePath2D.Node> nodes = new java.util.ArrayList<>();

    private java.awt.geom.Path2D.Double pathCache = null;

    private int windingRule = java.awt.geom.PathIterator.WIND_NON_ZERO;

    public int size() {
        return nodes.size();
    }

    @java.lang.Override
    public boolean contains(double x, double y) {
        return toPath2D().contains(x, y);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D p) {
        return toPath2D().contains(p);
    }

    @java.lang.Override
    public boolean contains(double x, double y, double w, double h) {
        return toPath2D().contains(x, y, w, h);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Rectangle2D r) {
        return toPath2D().contains(r);
    }

    @java.lang.Override
    public java.awt.Rectangle getBounds() {
        return toPath2D().getBounds();
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D getBounds2D() {
        return toPath2D().getBounds2D();
    }

    @java.lang.Override
    public final java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform at) {
        return toPath2D().getPathIterator(at);
    }

    @java.lang.Override
    public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform at, double flatness) {
        return toPath2D().getPathIterator(at, flatness);
    }

    @java.lang.Override
    public boolean intersects(double x, double y, double w, double h) {
        return toPath2D().intersects(x, y, w, h);
    }

    @java.lang.Override
    public boolean intersects(java.awt.geom.Rectangle2D r) {
        return toPath2D().intersects(r);
    }

    public void lineTo(double x, double y) {
        nodes.add(org.jhotdraw.geom.path.MutablePath2D.Node.lineTo(x, y));
        invalidatePathCache();
    }

    public void moveTo(double x, double y) {
        if ((!nodes.isEmpty()) && (nodes.get(nodes.size() - 1).pointType == java.awt.geom.PathIterator.SEG_MOVETO)) {
            nodes.set(nodes.size() - 1, org.jhotdraw.geom.path.MutablePath2D.Node.moveTo(x, y));
        } else {
            nodes.add(org.jhotdraw.geom.path.MutablePath2D.Node.moveTo(x, y));
        }
        invalidatePathCache();
    }

    public void changeNode(int idx, java.util.function.Consumer<org.jhotdraw.geom.path.MutablePath2D.Node> change) {
        change.accept(nodes.get(idx));
        invalidatePathCache();
    }

    public void removeNode(int idx) {
        nodes.remove(idx);
        invalidatePathCache();
    }

    public java.awt.geom.Point2D.Double getNodePoint(int idx) {
        return nodes.get(idx).getPoint();
    }

    public java.awt.geom.Path2D.Double toPath2D() {
        if (pathCache != null) {
            return pathCache;
        }
        java.awt.geom.Path2D.Double path = new java.awt.geom.Path2D.Double(windingRule);
        for (org.jhotdraw.geom.path.var node : nodes) {
            switch (node.pointType) {
                case java.awt.geom.PathIterator.SEG_MOVETO ->
                    path.moveTo(node.values[0], node.values[1]);
                case java.awt.geom.PathIterator.SEG_LINETO ->
                    path.lineTo(node.values[0], node.values[1]);
                case java.awt.geom.PathIterator.SEG_CLOSE ->
                    path.closePath();
            }
        }
        pathCache = path;
        return path;
    }

    protected void invalidatePathCache() {
        pathCache = null;
    }

    public static class Node {
        final int pointType;

        final double values[];

        public static final int[] SIZE_FOR_POINTTYPE = new int[]{ 2, 2, 4, 6, 0 };

        Node(int pointType) {
            this.pointType = pointType;
            this.values = new double[org.jhotdraw.geom.path.MutablePath2D.Node.SIZE_FOR_POINTTYPE[pointType]];
        }

        public org.jhotdraw.geom.path.MutablePath2D.Node withPoint(double x, double y) {
            values[0] = x;
            values[1] = y;
            return this;
        }

        public java.awt.geom.Point2D.Double getPoint() {
            return new java.awt.geom.Point2D.Double(values[0], values[1]);
        }

        public static org.jhotdraw.geom.path.MutablePath2D.Node moveTo(double x, double y) {
            return new org.jhotdraw.geom.path.MutablePath2D.Node(java.awt.geom.PathIterator.SEG_MOVETO).withPoint(x, y);
        }

        public static org.jhotdraw.geom.path.MutablePath2D.Node lineTo(double x, double y) {
            return new org.jhotdraw.geom.path.MutablePath2D.Node(java.awt.geom.PathIterator.SEG_LINETO).withPoint(x, y);
        }
    }
}