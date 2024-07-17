/* @(#)JColorWheel.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
import java.awt.color.ColorSpace;
/**
 * The {@code JColorWheel} displays a wheel made of two components of a {@code ColorSpace}.
 *
 * <p>The user can click at the wheel to pick a color.
 *
 * <p>The {@code JColorWheel} should be used together with a color slider for the remaining color
 * component(s) of the color system.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class JColorWheel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private boolean isFlipX;

    private boolean isFlipY;

    public enum Type {

        POLAR,
        SQUARE,
        DISK,
        COMPLEX;
    }

    private org.jhotdraw.color.JColorWheel.Type type = org.jhotdraw.color.JColorWheel.Type.POLAR;

    private java.awt.color.ColorSpace sys;

    protected java.awt.Insets wheelInsets;

    protected java.awt.Image colorWheelImage;

    protected org.jhotdraw.color.AbstractColorWheelImageProducer colorWheelProducer;

    protected org.jhotdraw.color.ColorSliderModel model;

    /**
     * Radial color component index.
     */
    protected int radialIndex = 1;

    /**
     * Angular color component index.
     */
    protected int angularIndex = 0;

    /**
     * Vertical color component index.
     */
    protected int verticalIndex = 2;

    private class MouseHandler extends java.awt.event.MouseAdapter implements java.awt.event.MouseMotionListener {
        @java.lang.Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            update(e);
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            update(e);
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            update(e);
        }

        private void update(java.awt.event.MouseEvent e) {
            float[] hsb = getColorAt(e.getX(), e.getY());
            model.setComponent(angularIndex, hsb[angularIndex]);
            model.setComponent(radialIndex, hsb[radialIndex]);
            // FIXME - We should only repaint the damaged area
            repaint();
        }
    }

    private org.jhotdraw.color.JColorWheel.MouseHandler mouseHandler;

    private class ModelHandler implements javax.swing.event.ChangeListener {
        @java.lang.Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            repaint();
        }
    }

    private org.jhotdraw.color.JColorWheel.ModelHandler modelHandler;

    public JColorWheel() {
        this(org.jhotdraw.color.HSBColorSpace.getInstance());
    }

    public JColorWheel(java.awt.color.ColorSpace sys) {
        this.sys = sys;
        wheelInsets = new java.awt.Insets(0, 0, 0, 0);
        model = new org.jhotdraw.color.DefaultColorSliderModel(sys);
        initComponents();
        colorWheelProducer = createWheelProducer(0, 0);
        modelHandler = new org.jhotdraw.color.JColorWheel.ModelHandler();
        model.addChangeListener(modelHandler);
        installMouseListeners();
        setOpaque(false);
    }

    public void setType(org.jhotdraw.color.JColorWheel.Type type) {
        this.type = type;
        colorWheelProducer = createWheelProducer(0, 0);
    }

    protected void installMouseListeners() {
        mouseHandler = new org.jhotdraw.color.JColorWheel.MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setModel(org.jhotdraw.color.ColorSliderModel m) {
        if (model != null) {
            model.removeChangeListener(modelHandler);
        }
        model = m;
        if (model != null) {
            model.addChangeListener(modelHandler);
            colorWheelProducer = createWheelProducer(getWidth(), getHeight());
            repaint();
        }
    }

    public void setRadialComponentIndex(int newValue) {
        radialIndex = newValue;
        colorWheelImage = null;
        repaint();
    }

    public void setAngularComponentIndex(int newValue) {
        angularIndex = newValue;
        colorWheelImage = null;
        repaint();
    }

    public void setVerticalComponentIndex(int newValue) {
        verticalIndex = newValue;
        colorWheelImage = null;
        repaint();
    }

    public void setWheelInsets(java.awt.Insets newValue) {
        wheelInsets = newValue;
        repaint();
    }

    public java.awt.Insets getWheelInsets() {
        return wheelInsets;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredSize() {
        return new java.awt.Dimension(100, 100);
    }

    public org.jhotdraw.color.ColorSliderModel getModel() {
        return model;
    }

    @java.lang.Override
    public void paintComponent(java.awt.Graphics gr) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        paintWheel(g);
        paintThumb(g);
    }

    public void setFlipX(boolean newValue) {
        isFlipX = newValue;
        colorWheelProducer = createWheelProducer(0, 0);
    }

    public void setFlipY(boolean newValue) {
        isFlipY = newValue;
        colorWheelProducer = createWheelProducer(0, 0);
    }

    public boolean isFlipX() {
        return isFlipX;
    }

    public boolean isFlipY() {
        return isFlipY;
    }

    protected org.jhotdraw.color.AbstractColorWheelImageProducer createWheelProducer(int w, int h) {
        org.jhotdraw.color.AbstractColorWheelImageProducer p;
        switch (type) {
            case POLAR :
            default :
                p = new org.jhotdraw.color.PolarColorWheelImageProducer(model.getColorSpace(), w, h);
                break;
            case SQUARE :
                p = new org.jhotdraw.color.ColorSquareImageProducer(model.getColorSpace(), w, h, isFlipX, isFlipY);
                break;
            case DISK :
                p = new org.jhotdraw.color.DiskColorWheelImageProducer(model.getColorSpace(), w, h, isFlipX, isFlipY);
                break;
            case COMPLEX :
                p = new org.jhotdraw.color.ComplexColorWheelImageProducer(model.getColorSpace(), w, h, isFlipX, isFlipY);
                break;
        }
        p.setAngularComponentIndex(angularIndex);
        p.setRadialComponentIndex(radialIndex);
        p.setVerticalComponentIndex(verticalIndex);
        return p;
    }

    protected void paintWheel(java.awt.Graphics2D g) {
        int w = (getWidth() - wheelInsets.left) - wheelInsets.right;
        int h = (getHeight() - wheelInsets.top) - wheelInsets.bottom;
        if (((colorWheelImage == null) || (colorWheelImage.getWidth(this) != w)) || (colorWheelImage.getHeight(this) != h)) {
            if (colorWheelImage != null) {
                colorWheelImage.flush();
            }
            colorWheelProducer = createWheelProducer(w, h);
            colorWheelImage = createImage(colorWheelProducer);
        }
        colorWheelProducer.setVerticalValue(model.getComponent(verticalIndex));
        if (colorWheelProducer.needsGeneration()) {
            // To keep the UI responsive, we only perform the time consuming
            // regeneration of the color track if we don't already have
            // a latency of more than a 10th of a second on the most recent event.
            long latency = java.lang.System.currentTimeMillis() - java.awt.EventQueue.getMostRecentEventTime();
            if (latency > 100) {
                repaint();
            } else {
                colorWheelProducer.regenerateColorWheel();
            }
        }
        g.drawImage(colorWheelImage, wheelInsets.left, wheelInsets.top, this);
    }

    protected void paintThumb(java.awt.Graphics2D g) {
        java.awt.Point p = getThumbLocation();
        g.setColor(java.awt.Color.white);
        g.fillRect(p.x - 1, p.y - 1, 2, 2);
        g.setColor(java.awt.Color.black);
        g.drawRect(p.x - 2, p.y - 2, 3, 3);
    }

    protected java.awt.Point getCenter() {
        int w = (getWidth() - wheelInsets.left) - wheelInsets.right;
        int h = (getHeight() - wheelInsets.top) - wheelInsets.bottom;
        return new java.awt.Point(wheelInsets.left + (w / 2), wheelInsets.top + (h / 2));
    }

    protected float getRadius() {
        return colorWheelProducer.getRadius();
    }

    protected java.awt.Point getThumbLocation() {
        return getColorLocation(model.getComponents());
    }

    protected java.awt.Point getColorLocation(java.awt.Color c) {
        java.awt.Point p = colorWheelProducer.getColorLocation(c);
        p.x += wheelInsets.left;
        p.y += wheelInsets.top;
        return p;
    }

    protected java.awt.Point getColorLocation(float[] components) {
        java.awt.Point p = colorWheelProducer.getColorLocation(components);
        p.x += wheelInsets.left;
        p.y += wheelInsets.top;
        return p;
    }

    protected float[] getColorAt(int x, int y) {
        float[] cc = colorWheelProducer.getColorAt(x - wheelInsets.left, y - wheelInsets.top);
        return cc;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

}