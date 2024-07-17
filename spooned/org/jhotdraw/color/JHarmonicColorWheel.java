/**
 *
 * @(#)JHarmonicColorWheel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * JHarmonicColorWheel.
 *
 * <p>FIXME - This is an experimental class. Do not use it.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class JHarmonicColorWheel extends org.jhotdraw.color.JColorWheel {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String SELECTED_INDEX_PROPERTY = "selectedIndex";

    private org.jhotdraw.color.HarmonicColorModel harmonicModel;

    private int selectedIndex = -1;

    private float handleRadius = 4.0F;

    private float baseRadius = 7.0F;

    private class MouseHandler implements java.awt.event.MouseListener , java.awt.event.MouseMotionListener {
        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            update(e);
        }

        @java.lang.Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int closestIndex = -1;
            if ((harmonicModel != null) && (harmonicModel.size() > 0)) {
                int closestError = java.lang.Integer.MAX_VALUE;
                for (int i = 0, n = harmonicModel.size(); i < n; i++) {
                    java.awt.Color c = harmonicModel.get(i);
                    if (c != null) {
                        java.awt.Point p = getColorLocation(harmonicModel.get(i));
                        int error = ((p.x - x) * (p.x - x)) + ((p.y - y) * (p.y - y));
                        if (error < closestError) {
                            closestIndex = i;
                            closestError = error;
                        }
                    }
                }
                if (closestIndex != (-1)) {
                    if (closestError > 20) {
                        closestIndex = -1;
                    }
                }
            }
            setSelectedIndex(closestIndex);
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            // update(e);
        }

        private void update(java.awt.event.MouseEvent e) {
            if (selectedIndex != (-1)) {
                float[] hsb = getColorAt(e.getX(), e.getY());
                hsb[1] = harmonicModel.get(selectedIndex).getColorComponents(null)[1];
                // if (hsb != null) {
                harmonicModel.set(selectedIndex, new java.awt.Color(harmonicModel.getColorSpace(), hsb, 1.0F));
                // }
                repaint();
            }
        }
    }

    private org.jhotdraw.color.JHarmonicColorWheel.MouseHandler mouseHandler;

    private class ModelHandler implements java.beans.PropertyChangeListener , javax.swing.event.ListDataListener {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if (((name == null) && (org.jhotdraw.color.HarmonicColorModel.COLOR_SPACE_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.color.HarmonicColorModel.COLOR_SPACE_PROPERTY))) {
                model.setColorSpace(harmonicModel.getColorSpace());
                model.setComponent(1, 1.0F);
                colorWheelProducer = createWheelProducer(getWidth(), getHeight());
                colorWheelImage = null;
            }
            repaint();
        }

        @java.lang.Override
        public void intervalAdded(javax.swing.event.ListDataEvent e) {
            repaint();
        }

        @java.lang.Override
        public void intervalRemoved(javax.swing.event.ListDataEvent e) {
            repaint();
        }

        @java.lang.Override
        public void contentsChanged(javax.swing.event.ListDataEvent e) {
            repaint();
        }
    }

    private org.jhotdraw.color.JHarmonicColorWheel.ModelHandler modelHandler;

    /**
     * Creates new form.
     */
    public JHarmonicColorWheel() {
        super(org.jhotdraw.color.HSLPhysiologicColorSpace.getInstance());
        initComponents();
        setRadialComponentIndex(2);
        setVerticalComponentIndex(1);
        getModel().setComponent(1, 1.0F);
        setWheelInsets(new java.awt.Insets(5, 5, 5, 5));
        modelHandler = new org.jhotdraw.color.JHarmonicColorWheel.ModelHandler();
        org.jhotdraw.color.DefaultHarmonicColorModel p = new org.jhotdraw.color.DefaultHarmonicColorModel();
        setHarmonicColorModel(p);
        setToolTipText("");
    }

    public void setColorSpace(java.awt.color.ColorSpace newValue) {
        harmonicModel.setColorSpace(newValue);
        getModel().setColorSpace(newValue);
        getModel().setComponent(1, 1.0F);
    }

    public org.jhotdraw.color.HarmonicColorModel getHarmonicColorModel() {
        return harmonicModel;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.event.MouseEvent evt) {
        float[] hsb = getColorAt(evt.getX(), evt.getY());
        if (hsb == null) {
            return null;
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        buf.append(java.lang.Math.round(hsb[0] * 360));
        buf.append(",");
        buf.append(java.lang.Math.round(hsb[1] * 100.0F));
        buf.append(",");
        buf.append(java.lang.Math.round(hsb[2] * 100.0F));
        if (buf.length() > 0) {
            buf.insert(0, "<html>");
            return buf.toString();
        } else {
            return null;
        }
    }

    @java.lang.Override
    protected void installMouseListeners() {
        mouseHandler = new org.jhotdraw.color.JHarmonicColorWheel.MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setHarmonicColorModel(org.jhotdraw.color.HarmonicColorModel newValue) {
        org.jhotdraw.color.HarmonicColorModel oldValue = harmonicModel;
        if (oldValue != null) {
            oldValue.removePropertyChangeListener(modelHandler);
            oldValue.removeListDataListener(modelHandler);
        }
        harmonicModel = newValue;
        if (newValue != null) {
            newValue.addPropertyChangeListener(modelHandler);
            newValue.addListDataListener(modelHandler);
            colorWheelProducer = createWheelProducer(getWidth(), getHeight());
        }
    }

    @java.lang.Override
    public void paintComponent(java.awt.Graphics gr) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        super.paintComponent(g);
    }

    @java.lang.Override
    protected void paintThumb(java.awt.Graphics2D g) {
        paintTicks(g);
        if (harmonicModel != null) {
            java.awt.Point center = getCenter();
            java.awt.geom.Ellipse2D.Float oval = new java.awt.geom.Ellipse2D.Float(0, 0, 0, 0);
            float[] comp = null;
            for (int i = harmonicModel.size() - 1; i >= 0; i--) {
                if (harmonicModel.get(i) != null) {
                    java.awt.Point p = getColorLocation(harmonicModel.get(i));
                    g.setColor(java.awt.Color.black);
                    g.drawLine(center.x, center.y, p.x, p.y);
                }
            }
            for (int i = harmonicModel.size() - 1; i >= 0; i--) {
                if (harmonicModel.get(i) != null) {
                    java.awt.Point p = getColorLocation(harmonicModel.get(i));
                    java.awt.Color mixerColor = harmonicModel.get(i);
                    comp = org.jhotdraw.color.ColorUtil.fromColor(harmonicModel.getColorSpace(), mixerColor);
                    if (i == selectedIndex) {
                        g.setColor(java.awt.Color.white);
                        oval.x = p.x - baseRadius;
                        oval.y = p.y - baseRadius;
                        oval.width = baseRadius * 2.0F;
                        oval.height = baseRadius * 2.0F;
                        g.fill(oval);
                    }
                    g.setColor(mixerColor);
                    oval.x = p.x - handleRadius;
                    oval.y = p.y - handleRadius;
                    oval.width = handleRadius * 2.0F;
                    oval.height = handleRadius * 2.0F;
                    g.fill(oval);
                    g.setColor(java.awt.Color.black);
                    g.draw(oval);
                    if (i == harmonicModel.getBase()) {
                        oval.x = p.x - baseRadius;
                        oval.y = p.y - baseRadius;
                        oval.width = baseRadius * 2.0F;
                        oval.height = baseRadius * 2.0F;
                        g.draw(oval);
                    }
                    // g.drawString(i+"", p.x, p.y);
                }
            }
        }
    }

    protected void paintTicks(java.awt.Graphics2D g) {
        if (true) {
            return;
        }
        if (harmonicModel != null) {
            java.awt.Point center = getCenter();
            float radius = getRadius();
            java.awt.geom.Ellipse2D.Float oval = new java.awt.geom.Ellipse2D.Float(0, 0, 0, 0);
            int baseIndex = harmonicModel.getBase();
            java.awt.Color bc = harmonicModel.get(baseIndex);
            g.setColor(java.awt.Color.DARK_GRAY);
            for (int i = 0; i < 12; i++) {
                float angle = bc.getColorComponents(null)[0] + (i / 12.0F);
                float radial1 = radius;
                /* g.draw(new Line2D.Double(
                center.x + radius * Math.cos(angle * Math.PI * 2d),
                center.y - radius * Math.sin(angle * Math.PI * 2d),
                center.x + (radius + 2) * Math.cos(angle * Math.PI * 2d),
                center.y - (radius + 2) * Math.sin(angle * Math.PI * 2d)));
                 */
                g.fill(new java.awt.geom.Ellipse2D.Double((center.x + ((radius + 2) * java.lang.Math.cos((angle * java.lang.Math.PI) * 2.0))) - 2, (center.y - ((radius + 2) * java.lang.Math.sin((angle * java.lang.Math.PI) * 2.0))) - 2, 4, 4));
            }
            for (int i = 0, n = harmonicModel.size(); i < n; i++) {
                if (i != baseIndex) {
                    java.awt.Color dc = harmonicModel.get(i);
                    if (dc != null) {
                        float angle = dc.getColorComponents(null)[0];
                        float diff = java.lang.Math.abs(angle - bc.getColorComponents(null)[0]) * 12;
                        if (java.lang.Math.abs(diff - java.lang.Math.round(diff)) < 0.02F) {
                            g.draw(new java.awt.geom.Line2D.Double(center.x + ((radius + 6) * java.lang.Math.cos((angle * java.lang.Math.PI) * 2.0)), center.y - ((radius + 6) * java.lang.Math.sin((angle * java.lang.Math.PI) * 2.0)), center.x + ((radius - 2) * java.lang.Math.cos((angle * java.lang.Math.PI) * 2.0)), center.y - ((radius - 2) * java.lang.Math.sin((angle * java.lang.Math.PI) * 2.0))));
                        } else {
                            g.draw(new java.awt.geom.Line2D.Double(center.x + (radius * java.lang.Math.cos((angle * java.lang.Math.PI) * 2.0)), center.y - (radius * java.lang.Math.sin((angle * java.lang.Math.PI) * 2.0)), center.x + ((radius - 1) * java.lang.Math.cos((angle * java.lang.Math.PI) * 2.0)), center.y - ((radius - 1) * java.lang.Math.sin((angle * java.lang.Math.PI) * 2.0))));
                        }
                    }
                }
            }
        }
    }

    public void setSelectedIndex(int newValue) {
        int oldValue = selectedIndex;
        selectedIndex = newValue;
        firePropertyChange(org.jhotdraw.color.JHarmonicColorWheel.SELECTED_INDEX_PROPERTY, oldValue, newValue);
        repaint();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @java.lang.Override
    protected java.awt.Point getColorLocation(java.awt.Color c) {
        java.awt.Point p = colorWheelProducer.getColorLocation(c);
        p.x += wheelInsets.left;
        p.y += wheelInsets.top;
        return p;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.FlowLayout());
    }// </editor-fold>//GEN-END:initComponents

}