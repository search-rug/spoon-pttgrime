/**
 *
 * @(#)WheelsAndSlidersMain.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.samples.color;
/**
 * A demo of color wheels and color sliders using all kinds of color systems.
 */
// End of variables declaration//GEN-END:variables
public class WheelsAndSlidersMain extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private java.awt.Color color;

    private javax.swing.JLabel colorLabel;

    private java.util.LinkedList<org.jhotdraw.color.ColorSliderModel> models;

    private class Handler implements javax.swing.event.ChangeListener {
        private int adjusting;

        @java.lang.Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            if ((adjusting++) == 0) {
                org.jhotdraw.color.ColorSliderModel m = ((org.jhotdraw.color.ColorSliderModel) (e.getSource()));
                color = m.getColor();
                previewLabel.setBackground(color);
                for (org.jhotdraw.color.ColorSliderModel c : models) {
                    if (c != m) {
                        if (c.getColorSpace().equals(m.getColorSpace())) {
                            // If the color system is the same, directly set the components (=lossless)
                            for (int i = 0; i < m.getComponentCount(); i++) {
                                c.setComponent(i, m.getComponent(i));
                            }
                        } else {
                            // If the color system is different, set the RGB color (=lossy)
                            c.setColor(color);
                        }
                    }
                }
            }
            adjusting--;
        }
    }

    private org.jhotdraw.samples.color.WheelsAndSlidersMain.Handler handler;

    /**
     * Creates new form.
     */
    public WheelsAndSlidersMain() {
        initComponents();
        models = new java.util.LinkedList<org.jhotdraw.color.ColorSliderModel>();
        handler = new org.jhotdraw.samples.color.WheelsAndSlidersMain.Handler();
        previewLabel.setOpaque(true);
        // RGB panels
        chooserPanel.add(createSliderChooser(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_sRGB)));
        chooserPanel.add(createColorWheelChooser(java.awt.color.ICC_ColorSpace.getInstance(java.awt.color.ICC_ColorSpace.CS_sRGB), 0, 1, 2, org.jhotdraw.color.JColorWheel.Type.SQUARE));
        // chooserPanel.add(createColorWheelChooser(ICC_ColorSpace.getInstance(ICC_ColorSpace.CS_LINEAR_RGB), 0, 1, 2, JColorWheel.Type.SQUARE));
        // CMYK
        // chooserPanel.add(createSliderChooser(CMYKGenericColorSpace.getInstance()));
        chooserPanel.add(createSliderChooser(org.jhotdraw.color.CMYKNominalColorSpace.getInstance()));
        // Empty panel
        chooserPanel.add(new javax.swing.JPanel());
        // HSB, HSV, HSL, ... variants
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSBColorSpace.getInstance()));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSVColorSpace.getInstance()));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSLColorSpace.getInstance()));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSLColorSpace.getInstance(), 0, 2, 1));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSVPhysiologicColorSpace.getInstance()));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSLPhysiologicColorSpace.getInstance()));
        chooserPanel.add(createColorWheelChooser(org.jhotdraw.color.HSLPhysiologicColorSpace.getInstance(), 0, 2, 1));
        chooserPanel.add(new javax.swing.JPanel());
        // CIELAB
        java.awt.color.ColorSpace cs;
        cs = new org.jhotdraw.color.CIELABColorSpace();
        chooserPanel.add(createColorWheelChooser(cs, 1, 2, 0, org.jhotdraw.color.JColorWheel.Type.SQUARE));
        cs = new org.jhotdraw.color.CIELCHabColorSpace();
        chooserPanel.add(createColorWheelChooser(cs, 2, 1, 0, org.jhotdraw.color.JColorWheel.Type.POLAR));
        // CIEXYZ
        chooserPanel.add(createColorWheelChooser(java.awt.color.ICC_ColorSpace.getInstance(java.awt.color.ICC_ColorSpace.CS_CIEXYZ), 1, 0, 2, org.jhotdraw.color.JColorWheel.Type.SQUARE));
        chooserPanel.add(createColorWheelChooser(java.awt.color.ICC_ColorSpace.getInstance(java.awt.color.ICC_ColorSpace.CS_PYCC), 1, 2, 0, org.jhotdraw.color.JColorWheel.Type.SQUARE));
    }

    private javax.swing.JPanel createColorWheelChooser(java.awt.color.ColorSpace sys) {
        return createColorWheelChooser(sys, 0, 1, 2);
    }

    private javax.swing.JPanel createColorWheelChooser(java.awt.color.ColorSpace sys, int angularIndex, int radialIndex, int verticalIndex) {
        return createColorWheelChooser(sys, angularIndex, radialIndex, verticalIndex, org.jhotdraw.color.JColorWheel.Type.POLAR);
    }

    private javax.swing.JPanel createColorWheelChooser(java.awt.color.ColorSpace sys, int angularIndex, int radialIndex, int verticalIndex, org.jhotdraw.color.JColorWheel.Type type) {
        return createColorWheelChooser(sys, angularIndex, radialIndex, verticalIndex, type, false, false);
    }

    private javax.swing.JPanel createColorWheelChooser(java.awt.color.ColorSpace sys, int angularIndex, int radialIndex, int verticalIndex, org.jhotdraw.color.JColorWheel.Type type, boolean flipX, boolean flipY) {
        javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.BorderLayout());
        final org.jhotdraw.color.DefaultColorSliderModel m = new org.jhotdraw.color.DefaultColorSliderModel(sys);
        models.add(m);
        m.addChangeListener(handler);
        org.jhotdraw.color.JColorWheel w = new org.jhotdraw.color.JColorWheel();
        w.setType(type);
        w.setAngularComponentIndex(angularIndex);
        w.setRadialComponentIndex(radialIndex);
        w.setVerticalComponentIndex(verticalIndex);
        w.setFlipX(flipX);
        w.setFlipY(flipY);
        w.setModel(m);
        javax.swing.JSlider s = new javax.swing.JSlider(javax.swing.JSlider.VERTICAL);
        s.setMajorTickSpacing(10);
        s.setPaintLabels(true);
        s.setPaintTicks(true);
        m.configureSlider(verticalIndex, s);
        p.add(new javax.swing.JLabel((((((("<html>" + org.jhotdraw.color.ColorUtil.getName(sys)) + "<br>α:") + angularIndex) + " r:") + radialIndex) + " v:") + verticalIndex), java.awt.BorderLayout.NORTH);
        p.add(w, java.awt.BorderLayout.CENTER);
        p.add(s, java.awt.BorderLayout.EAST);
        javax.swing.JPanel pp = new javax.swing.JPanel();
        p.add(pp, java.awt.BorderLayout.SOUTH);
        for (int i = 0; i < m.getComponentCount(); i++) {
            final int comp = i;
            final javax.swing.JTextField tf = new javax.swing.JTextField();
            tf.setEditable(false);
            tf.setColumns(4);
            javax.swing.event.ChangeListener cl = new javax.swing.event.ChangeListener() {
                java.text.NumberFormat df = java.text.NumberFormat.getNumberInstance();

                @java.lang.Override
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    df.setMaximumFractionDigits(3);
                    tf.setText(df.format(m.getComponent(comp)));
                }
            };
            cl.stateChanged(null);
            m.addChangeListener(cl);
            pp.add(tf);
        }
        return p;
    }

    private javax.swing.JPanel createSliderChooser(java.awt.color.ColorSpace sys) {
        return createSliderChooser(sys, false);
    }

    private javax.swing.JPanel createSliderChooser(java.awt.color.ColorSpace sys, boolean vertical) {
        javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.GridBagLayout());
        final org.jhotdraw.color.DefaultColorSliderModel m = new org.jhotdraw.color.DefaultColorSliderModel(sys);
        models.add(m);
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        if (!vertical) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            p.add(new javax.swing.JLabel("<html>" + org.jhotdraw.color.ColorUtil.getName(sys)), gbc);
        }
        m.addChangeListener(handler);
        for (int i = 0; i < m.getComponentCount(); i++) {
            final int comp = i;
            javax.swing.JSlider s = new javax.swing.JSlider(javax.swing.JSlider.HORIZONTAL);
            s.setMajorTickSpacing(50);
            s.setPaintTicks(true);
            s.setOrientation(vertical ? javax.swing.JSlider.VERTICAL : javax.swing.JSlider.HORIZONTAL);
            m.configureSlider(comp, s);
            if (vertical) {
                gbc.gridx = i;
                gbc.gridy = 0;
            } else {
                gbc.gridy = i + 1;
                gbc.gridx = 0;
            }
            p.add(s, gbc);
            final javax.swing.JTextField tf = new javax.swing.JTextField();
            tf.setEditable(false);
            tf.setColumns(4);
            javax.swing.event.ChangeListener cl = new javax.swing.event.ChangeListener() {
                java.text.NumberFormat df = java.text.NumberFormat.getNumberInstance();

                @java.lang.Override
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    df.setMaximumFractionDigits(3);
                    tf.setText(df.format(m.getComponent(comp)));
                }
            };
            cl.stateChanged(null);
            m.addChangeListener(cl);
            if (vertical) {
                gbc.gridx = i;
                gbc.gridy = 1;
            } else {
                gbc.gridy = i + 1;
                gbc.gridx = 1;
            }
            p.add(tf, gbc);
        }
        return p;
    }

    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                javax.swing.JFrame f = new javax.swing.JFrame("Color Wheels, Squares and Sliders");
                f.add(new org.jhotdraw.samples.color.WheelsAndSlidersMain());
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        chooserPanel = new javax.swing.JPanel();
        previewLabel = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        chooserPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chooserPanel.setLayout(new java.awt.GridLayout(0, 4, 10, 10));
        add(chooserPanel, java.awt.BorderLayout.CENTER);
        previewLabel.setText("Selected Color");
        add(previewLabel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chooserPanel;

    private javax.swing.JLabel previewLabel;
}