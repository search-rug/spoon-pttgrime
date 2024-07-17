/* Copyright (C) 2015 JHotDraw.

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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 *
 * @author toben
 */
public class BezierPathTest {
    @org.junit.jupiter.api.Test
    public void testToGeneralPath() {
        org.jhotdraw.geom.path.BezierPath instance = new org.jhotdraw.geom.path.BezierPath();
        java.awt.geom.Point2D.Double c0 = new java.awt.geom.Point2D.Double(4.0E-4, 0.002);
        instance.add(c0);
        c0 = new java.awt.geom.Point2D.Double(21.0004, 56.92827);
        instance.add(c0);
        java.awt.geom.Path2D.Double gp = instance.toGeneralPath();
        java.awt.geom.PathIterator pathIterator = gp.getPathIterator(null);
        double[] coords = new double[2];
        int i = 0;
        while (pathIterator.isDone() == false) {
            pathIterator.currentSegment(coords);
            for (int j = 0; j < 3; j++) {
                org.junit.jupiter.api.Assertions.assertEquals(coords[0], instance.nodes().get(i).getControlPoint(j).x);
                org.junit.jupiter.api.Assertions.assertEquals(coords[1], instance.nodes().get(i).getControlPoint(j).y);
            }
            i++;
            pathIterator.next();
        } 
    }

    /**
     * Test of toPolygonArray method, of class BezierPath.
     */
    @org.junit.jupiter.api.Test
    public void testToPolygonArray() {
        org.jhotdraw.geom.path.BezierPath instance = new org.jhotdraw.geom.path.BezierPath();
        java.awt.geom.Point2D.Double c0 = new java.awt.geom.Point2D.Double(4.0E-4, 0.002);
        instance.add(c0);
        c0 = new java.awt.geom.Point2D.Double(21.0004, 56.92827);
        instance.add(c0);
        java.awt.geom.Point2D.Double[] toPolygonArray = instance.toPolygonArray();
        org.junit.jupiter.api.Assertions.assertEquals(toPolygonArray.length, 2);
        for (int i = 0; i < toPolygonArray.length; i++) {
            for (int j = 0; j < 3; j++) {
                org.junit.jupiter.api.Assertions.assertEquals(toPolygonArray[i], instance.nodes().get(i).getControlPoint(j));
            }
        }
    }

    @org.junit.jupiter.api.Test
    public void testPathIterator() {
        org.jhotdraw.geom.path.BezierPath instance = new org.jhotdraw.geom.path.BezierPath();
        java.awt.geom.Point2D.Double c0 = new java.awt.geom.Point2D.Double(4.0E-4, 0.002);
        instance.add(c0);
        c0 = new java.awt.geom.Point2D.Double(21.0004, 56.92827);
        instance.add(c0);
        java.awt.geom.PathIterator pathIterator = instance.getPathIterator(null);
        double[] coords = new double[2];
        int i = 0;
        while (pathIterator.isDone() == false) {
            pathIterator.currentSegment(coords);
            for (int j = 0; j < 3; j++) {
                org.junit.jupiter.api.Assertions.assertEquals(coords[0], instance.nodes().get(i).getControlPoint(j).x);
                org.junit.jupiter.api.Assertions.assertEquals(coords[1], instance.nodes().get(i).getControlPoint(j).y);
            }
            i++;
            pathIterator.next();
        } 
    }

    @org.junit.jupiter.api.Test
    public void testPathIterator2() {
        org.jhotdraw.geom.path.BezierPath instance = new org.jhotdraw.geom.path.BezierPath();
        java.awt.geom.Point2D.Double c0 = new java.awt.geom.Point2D.Double(4.0E-4, 0.002);
        instance.add(c0);
        c0 = new java.awt.geom.Point2D.Double(21.0004, 56.92827);
        instance.add(c0);
        java.awt.geom.PathIterator pathIterator = instance.getPathIterator(null, 4);
        double[] coords = new double[2];
        int i = 0;
        while (pathIterator.isDone() == false) {
            pathIterator.currentSegment(coords);
            for (int j = 0; j < 3; j++) {
                org.junit.jupiter.api.Assertions.assertEquals(coords[0], instance.nodes().get(i).getControlPoint(j).x);
                org.junit.jupiter.api.Assertions.assertEquals(coords[1], instance.nodes().get(i).getControlPoint(j).y);
            }
            i++;
            pathIterator.next();
        } 
    }
}