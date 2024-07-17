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
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
/**
 *
 * @author tw
 */
public class MutablePath2DTest {
    @org.junit.jupiter.api.Test
    void testEmptyPath() {
        org.jhotdraw.geom.path.MutablePath2D path = new org.jhotdraw.geom.path.MutablePath2D();
        java.awt.geom.Path2D.Double toPath2D = path.toPath2D();
        org.assertj.core.api.Assertions.assertThat(toPath2D.getBounds2D()).extracting(b -> b.toString()).isEqualTo("java.awt.geom.Rectangle2D$Double[x=0.0,y=0.0,w=0.0,h=0.0]");
    }

    @org.junit.jupiter.api.Test
    void testSimpleLineBounds() {
        org.jhotdraw.geom.path.MutablePath2D path = new org.jhotdraw.geom.path.MutablePath2D();
        path.moveTo(10, 20);
        path.lineTo(30, 50);
        path.lineTo(40, 70);
        org.assertj.core.api.Assertions.assertThat(path.getBounds2D()).extracting(b -> b.toString()).isEqualTo("java.awt.geom.Rectangle2D$Double[x=10.0,y=20.0,w=30.0,h=50.0]");
    }

    @org.junit.jupiter.api.Test
    void testSimpleLine() {
        org.jhotdraw.geom.path.MutablePath2D path = new org.jhotdraw.geom.path.MutablePath2D();
        path.moveTo(10, 20);
        path.lineTo(30, 50);
        path.lineTo(40, 70);
        java.lang.String coordsTxt = "";
        java.awt.geom.PathIterator pathIterator = path.getPathIterator(null);
        double coords[] = new double[6];
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(coords);
            if (type == java.awt.geom.PathIterator.SEG_MOVETO) {
                coordsTxt += "*";
            }
            coordsTxt += ((("(" + coords[0]) + ",") + coords[1]) + ")";
            pathIterator.next();
        } 
        org.assertj.core.api.Assertions.assertThat(coordsTxt).isEqualTo("*(10.0,20.0)(30.0,50.0)(40.0,70.0)");
    }

    @org.junit.jupiter.api.Test
    void testSimpleLineWithNodeChange() {
        org.jhotdraw.geom.path.MutablePath2D path = new org.jhotdraw.geom.path.MutablePath2D();
        path.moveTo(10, 20);
        path.lineTo(30, 50);
        path.lineTo(40, 70);
        java.lang.String coordsTxt = "";
        java.awt.geom.PathIterator pathIterator = path.getPathIterator(null);
        double coords[] = new double[6];
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(coords);
            if (type == java.awt.geom.PathIterator.SEG_MOVETO) {
                coordsTxt += "*";
            }
            coordsTxt += ((("(" + coords[0]) + ",") + coords[1]) + ")";
            pathIterator.next();
        } 
        org.assertj.core.api.Assertions.assertThat(coordsTxt).isEqualTo("*(10.0,20.0)(30.0,50.0)(40.0,70.0)");
        path.changeNode(1, n -> n.withPoint(100, 30));
        coordsTxt = "";
        pathIterator = path.getPathIterator(null);
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(coords);
            if (type == java.awt.geom.PathIterator.SEG_MOVETO) {
                coordsTxt += "*";
            }
            coordsTxt += ((("(" + coords[0]) + ",") + coords[1]) + ")";
            pathIterator.next();
        } 
        org.assertj.core.api.Assertions.assertThat(coordsTxt).isEqualTo("*(10.0,20.0)(100.0,30.0)(40.0,70.0)");
    }
}