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
package org.jhotdraw.draw.figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 *
 * @author tw
 */
public class AbstractFigureTest {
    @org.junit.jupiter.api.Test
    public void testChangedWithoutWillChange() {
        org.junit.jupiter.api.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> new org.jhotdraw.draw.figure.AbstractFigureImpl().changed());
    }

    @org.junit.jupiter.api.Test
    public void testWillChangeChangedEvents() {
        org.jhotdraw.draw.figure.AbstractAttributedFigure figure = new org.jhotdraw.draw.figure.AbstractFigureTest.AbstractFigureImpl();
        org.junit.jupiter.api.Assertions.assertEquals(figure.getChangingDepth(), 0);
        figure.willChange();
        org.junit.jupiter.api.Assertions.assertEquals(figure.getChangingDepth(), 1);
        figure.willChange();
        org.junit.jupiter.api.Assertions.assertEquals(figure.getChangingDepth(), 2);
        figure.changed();
        org.junit.jupiter.api.Assertions.assertEquals(figure.getChangingDepth(), 1);
        figure.changed();
        org.junit.jupiter.api.Assertions.assertEquals(figure.getChangingDepth(), 0);
    }

    public class AbstractFigureImpl extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
        @java.lang.Override
        public void draw(java.awt.Graphics2D g) {
        }

        @java.lang.Override
        public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
            return null;
        }

        @java.lang.Override
        public boolean contains(java.awt.geom.Point2D.Double p) {
            return true;
        }

        @java.lang.Override
        public java.lang.Object getTransformRestoreData() {
            return null;
        }

        @java.lang.Override
        public void restoreTransformTo(java.lang.Object restoreData) {
        }

        @java.lang.Override
        public void transform(java.awt.geom.AffineTransform tx) {
        }

        @java.lang.Override
        public java.awt.geom.Rectangle2D.Double getDrawingArea(double factor) {
            return null;
        }

        @java.lang.Override
        public org.jhotdraw.draw.figure.Attributes attr() {
            return null;
        }

        @java.lang.Override
        protected void drawFill(java.awt.Graphics2D g) {
        }

        @java.lang.Override
        protected void drawStroke(java.awt.Graphics2D g) {
        }

        @java.lang.Override
        public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
            return false;
        }
    }
}