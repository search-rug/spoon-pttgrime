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
package org.jhotdraw.draw;
/**
 * Simple implementation of AbstractDrawingView.
 *
 * @author tw
 */
public abstract class DelegatorDrawingView extends org.jhotdraw.draw.AbstractDrawingView {
    private javax.swing.JComponent drawTo;

    public DelegatorDrawingView(javax.swing.JComponent drawTo) {
        this.drawTo = drawTo;
    }

    public DelegatorDrawingView() {
    }

    public void setDrawTo(javax.swing.JComponent drawTo) {
        this.drawTo = drawTo;
    }

    public javax.swing.JComponent getDrawTo() {
        return drawTo;
    }

    @java.lang.Override
    public abstract java.awt.geom.AffineTransform getDrawingToViewTransform();

    @java.lang.Override
    public void repaint(java.awt.Rectangle r) {
        drawTo.repaint(r);
    }

    @java.lang.Override
    public java.awt.Color getBackground() {
        return drawTo.getBackground();
    }

    @java.lang.Override
    public void repaint() {
        drawTo.repaint();
    }

    @java.lang.Override
    public int getWidth() {
        return drawTo.getWidth();
    }

    @java.lang.Override
    public int getHeight() {
        return drawTo.getHeight();
    }

    @java.lang.Override
    public void revalidate() {
        drawTo.revalidate();
    }

    @java.lang.Override
    public void setCursor(java.awt.Cursor c) {
        drawTo.setCursor(c);
    }

    @java.lang.Override
    public void requestFocus() {
        drawTo.requestFocus();
    }

    @java.lang.Override
    public javax.swing.JComponent getComponent() {
        return drawTo;
    }

    @java.lang.Override
    public double getScaleFactor() {
        return 1;
    }

    @java.lang.Override
    public void setScaleFactor(double newValue) {
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        drawTo.setEnabled(newValue);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return drawTo.isEnabled();
    }

    @java.lang.Override
    public void addMouseListener(java.awt.event.MouseListener l) {
        drawTo.addMouseListener(l);
    }

    @java.lang.Override
    public void removeMouseListener(java.awt.event.MouseListener l) {
        drawTo.removeMouseListener(l);
    }

    @java.lang.Override
    public void addKeyListener(java.awt.event.KeyListener l) {
        drawTo.addKeyListener(l);
    }

    @java.lang.Override
    public void removeKeyListener(java.awt.event.KeyListener l) {
        drawTo.removeKeyListener(l);
    }

    @java.lang.Override
    public void addMouseMotionListener(java.awt.event.MouseMotionListener l) {
        drawTo.addMouseMotionListener(l);
    }

    @java.lang.Override
    public void removeMouseMotionListener(java.awt.event.MouseMotionListener l) {
        drawTo.removeMouseMotionListener(l);
    }

    @java.lang.Override
    public void removeMouseWheelListener(java.awt.event.MouseWheelListener l) {
        drawTo.removeMouseWheelListener(l);
    }

    @java.lang.Override
    public void addMouseWheelListener(java.awt.event.MouseWheelListener l) {
        drawTo.addMouseWheelListener(l);
    }
}