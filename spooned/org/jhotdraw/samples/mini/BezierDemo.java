/* @(#)BezierDemo.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
import org.jhotdraw.geom.path.Bezier;
/**
 * Demonstration of the curve fitting algorithm of class {@link Bezier}.
 */
// End of variables declaration//GEN-END:variables
public class BezierDemo extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private static class Example {
        double error;

        org.jhotdraw.geom.path.BezierPath digitized = new org.jhotdraw.geom.path.BezierPath();

        org.jhotdraw.geom.path.BezierPath bezier;

        java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>> segments;

        public void invalidate() {
            bezier = null;
            segments = null;
        }
    }

    private java.util.ArrayList<org.jhotdraw.samples.mini.BezierDemo.Example> examples = new java.util.ArrayList<org.jhotdraw.samples.mini.BezierDemo.Example>();

    private javax.swing.JDialog dumpDialog;

    private javax.swing.JTextArea dumpArea;

    private class MouseHandler implements java.awt.event.MouseMotionListener , java.awt.event.MouseListener {
        private org.jhotdraw.samples.mini.BezierDemo.Example example;

        @java.lang.Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            double zoomFactor = getZoomFactor();
            example.digitized.lineTo(e.getX() / zoomFactor, e.getY() / zoomFactor);
            example.invalidate();
            canvas.repaint();
        }

        @java.lang.Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            example = new org.jhotdraw.samples.mini.BezierDemo.Example();
            examples.add(example);
            example.error = getError();
            double zoomFactor = getZoomFactor();
            example.digitized.moveTo(e.getX() / zoomFactor, e.getY() / zoomFactor);
            canvas.repaint();
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

    private org.jhotdraw.samples.mini.BezierDemo.MouseHandler handler = new org.jhotdraw.samples.mini.BezierDemo.MouseHandler();

    private class Canvas extends javax.swing.JPanel {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public void paintComponent(java.awt.Graphics gr) {
            long start = java.lang.System.currentTimeMillis();
            super.paintComponent(gr);
            java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
            // Update examples
            for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                if (ex.bezier == null) {
                    ex.bezier = org.jhotdraw.geom.path.Bezier.fitBezierPath(ex.digitized, ex.error);
                }
                if (ex.segments == null) {
                    java.util.ArrayList<java.awt.geom.Point2D.Double> digitizedPoints = new java.util.ArrayList<java.awt.geom.Point2D.Double>();
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.digitized.nodes()) {
                        digitizedPoints.add(new java.awt.geom.Point2D.Double(node.x[0], node.y[0]));
                    }
                    // Split into segments at corners
                    ex.segments = new java.util.ArrayList<java.util.ArrayList<java.awt.geom.Point2D.Double>>();
                    ex.segments = org.jhotdraw.geom.path.Bezier.splitAtCorners(digitizedPoints, (77 / 180.0) * java.lang.Math.PI, getError() * 2);
                    // Clean up the data in the segments
                    for (int i = 0, n = ex.segments.size(); i < n; i++) {
                        java.util.ArrayList<java.awt.geom.Point2D.Double> seg = ex.segments.get(i);
                        seg = org.jhotdraw.geom.path.Bezier.removeClosePoints(seg, getError());
                        seg = org.jhotdraw.geom.path.Bezier.reduceNoise(seg, 0.8);
                        ex.segments.set(i, seg);
                    }
                }
            }
            g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.geom.AffineTransform identityTransform = g.getTransform();
            java.awt.geom.AffineTransform tx = g.getTransform();
            double zoomFactor = getZoomFactor();
            tx.scale(zoomFactor, zoomFactor);
            g.setTransform(tx);
            if (showPolylineCheck.isSelected()) {
                g.setColor(java.awt.Color.black);
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    g.draw(ex.digitized);
                }
            }
            if (showBezierCheck.isSelected()) {
                g.setColor(java.awt.Color.blue);
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    g.draw(ex.bezier);
                }
            }
            g.setTransform(identityTransform);
            if (showDigitizedCheck.isSelected()) {
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    g.setColor(java.awt.Color.white);
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.digitized.nodes()) {
                        g.fillRect(((int) ((node.x[0] * zoomFactor) - 2)), ((int) ((node.y[0] * zoomFactor) - 2)), 5, 5);
                    }
                }
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    g.setColor(java.awt.Color.black);
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.digitized.nodes()) {
                        g.fillRect(((int) ((node.x[0] * zoomFactor) - 1)), ((int) ((node.y[0] * zoomFactor) - 1)), 3, 3);
                    }
                }
            }
            if (showPreprocessedCheck.isSelected()) {
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    g.setColor(java.awt.Color.WHITE);
                    for (java.util.ArrayList<java.awt.geom.Point2D.Double> seg : ex.segments) {
                        for (int i = 0, n = seg.size(); i < n; i++) {
                            java.awt.geom.Point2D.Double node = seg.get(i);
                            g.fillRect(((int) ((node.x * zoomFactor) - 2)), ((int) ((node.y * zoomFactor) - 2)), 5, 5);
                        }
                    }
                    for (java.util.ArrayList<java.awt.geom.Point2D.Double> seg : ex.segments) {
                        for (int i = 0, n = seg.size(); i < n; i++) {
                            java.awt.geom.Point2D.Double node = seg.get(i);
                            g.setColor((i == 0) || (i == (n - 1)) ? java.awt.Color.RED : java.awt.Color.CYAN);
                            g.fillRect(((int) ((node.x * zoomFactor) - 1)), ((int) ((node.y * zoomFactor) - 1)), 3, 3);
                        }
                    }
                }
            }
            if (showControlsCheck.isSelected()) {
                for (org.jhotdraw.samples.mini.BezierDemo.Example ex : examples) {
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.bezier.nodes()) {
                        if (node.mask == org.jhotdraw.geom.path.BezierPath.C0_MASK) {
                        } else if ((node.mask == org.jhotdraw.geom.path.BezierPath.C1C2_MASK) && node.keepColinear) {
                            g.setColor(java.awt.Color.WHITE);
                            g.fillRect(((int) ((node.x[1] * zoomFactor) - 2)), ((int) ((node.y[1] * zoomFactor) - 2)), 5, 5);
                            g.fillRect(((int) ((node.x[2] * zoomFactor) - 2)), ((int) ((node.y[2] * zoomFactor) - 2)), 5, 5);
                            g.setColor(java.awt.Color.CYAN);
                            g.fillRect(((int) ((node.x[1] * zoomFactor) - 1)), ((int) ((node.y[1] * zoomFactor) - 1)), 3, 3);
                            g.draw(new java.awt.geom.Line2D.Double(node.x[1] * zoomFactor, node.y[1] * zoomFactor, node.x[0] * zoomFactor, node.y[0] * zoomFactor));
                            g.fillRect(((int) ((node.x[2] * zoomFactor) - 1)), ((int) ((node.y[2] * zoomFactor) - 1)), 3, 3);
                            g.draw(new java.awt.geom.Line2D.Double(node.x[2] * zoomFactor, node.y[2] * zoomFactor, node.x[0] * zoomFactor, node.y[0] * zoomFactor));
                        } else {
                            if ((node.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == org.jhotdraw.geom.path.BezierPath.C1_MASK) {
                                g.setColor(java.awt.Color.WHITE);
                                g.fillRect(((int) ((node.x[1] * zoomFactor) - 2)), ((int) ((node.y[1] * zoomFactor) - 2)), 5, 5);
                                g.setColor(java.awt.Color.MAGENTA);
                                g.fillRect(((int) ((node.x[1] * zoomFactor) - 1)), ((int) ((node.y[1] * zoomFactor) - 1)), 3, 3);
                                g.draw(new java.awt.geom.Line2D.Double(node.x[1] * zoomFactor, node.y[1] * zoomFactor, node.x[0] * zoomFactor, node.y[0] * zoomFactor));
                            }
                            if ((node.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) == org.jhotdraw.geom.path.BezierPath.C2_MASK) {
                                g.setColor(java.awt.Color.WHITE);
                                g.fillRect(((int) ((node.x[2] * zoomFactor) - 2)), ((int) ((node.y[2] * zoomFactor) - 2)), 5, 5);
                                g.setColor(java.awt.Color.MAGENTA);
                                g.fillRect(((int) ((node.x[2] * zoomFactor) - 1)), ((int) ((node.y[2] * zoomFactor) - 1)), 3, 3);
                                g.draw(new java.awt.geom.Line2D.Double(node.x[2] * zoomFactor, node.y[2] * zoomFactor, node.x[0] * zoomFactor, node.y[0] * zoomFactor));
                            }
                        }
                    }
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.bezier.nodes()) {
                        g.setColor(java.awt.Color.WHITE);
                        g.fillRect(((int) ((node.x[0] * zoomFactor) - 2)), ((int) ((node.y[0] * zoomFactor) - 2)), 5, 5);
                    }
                    for (org.jhotdraw.geom.path.BezierPath.Node node : ex.bezier.nodes()) {
                        g.setColor(node.keepColinear && (node.mask != org.jhotdraw.geom.path.BezierPath.C0_MASK) ? java.awt.Color.BLUE : java.awt.Color.RED);
                        g.fillRect(((int) ((node.x[0] * zoomFactor) - 1)), ((int) ((node.y[0] * zoomFactor) - 1)), 3, 3);
                    }
                }
            }
            long end = java.lang.System.currentTimeMillis();
            g.setColor(java.awt.Color.BLACK);
            g.drawString((end - start) + " ms", 5, g.getFontMetrics().getHeight());
        }
    }

    private org.jhotdraw.samples.mini.BezierDemo.Canvas canvas;

    /**
     * Creates new form BezierDemo
     */
    public BezierDemo() {
        initComponents();
        canvas = new org.jhotdraw.samples.mini.BezierDemo.Canvas();
        canvas.setOpaque(true);
        canvas.setBackground(java.awt.Color.WHITE);
        canvas.addMouseListener(handler);
        canvas.addMouseMotionListener(handler);
        add(canvas, java.awt.BorderLayout.CENTER);
        java.awt.geom.Point2D.Double[] d = // Digitized points
        new java.awt.geom.Point2D.Double[]{  }// Digitized points
        ;
        org.jhotdraw.geom.path.BezierPath digi = new org.jhotdraw.geom.path.BezierPath();
        digi.addPolyline(java.util.Arrays.asList(d));
        org.jhotdraw.samples.mini.BezierDemo.Example ex = new org.jhotdraw.samples.mini.BezierDemo.Example();
        examples.add(ex);
        ex.digitized = digi;
        ex.error = 2.0;
    }

    public static void main(java.lang.String[] arg) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            public void run() {
                javax.swing.JFrame f = new javax.swing.JFrame("Bezier Demo");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.add(new org.jhotdraw.samples.mini.BezierDemo());
                f.setPreferredSize(new java.awt.Dimension(400, 300));
                f.pack();
                f.setVisible(true);
            }
        });
    }

    private double getSquaredError() {
        double error = getError();
        return error * error;
    }

    private double getError() {
        double error = 2.0 / getZoomFactor();
        return error;
    }

    private double getZoomFactor() {
        return zoomSlider.getValue() / 100.0;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @java.lang.SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        toleranceLabel = new javax.swing.JLabel();
        zoomSlider = new javax.swing.JSlider();
        showDigitizedCheck = new javax.swing.JCheckBox();
        showPreprocessedCheck = new javax.swing.JCheckBox();
        showPolylineCheck = new javax.swing.JCheckBox();
        showBezierCheck = new javax.swing.JCheckBox();
        showControlsCheck = new javax.swing.JCheckBox();
        eraseButton = new javax.swing.JButton();
        dumpButton = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 20, 20, 20));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        toleranceLabel.setText("Zoom:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(toleranceLabel, gridBagConstraints);
        zoomSlider.setMaximum(800);
        zoomSlider.setMinimum(100);
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(zoomSlider, gridBagConstraints);
        showDigitizedCheck.setText("Show Source Points");
        showDigitizedCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(showDigitizedCheck, gridBagConstraints);
        showPreprocessedCheck.setText("Show Preprocessed Points");
        showPreprocessedCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(showPreprocessedCheck, gridBagConstraints);
        showPolylineCheck.setSelected(true);
        showPolylineCheck.setText("Show Polyline");
        showPolylineCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel1.add(showPolylineCheck, gridBagConstraints);
        showBezierCheck.setSelected(true);
        showBezierCheck.setText("Show Bezier Path");
        showBezierCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel1.add(showBezierCheck, gridBagConstraints);
        showControlsCheck.setText("Show Bezier Controls");
        showControlsCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(showControlsCheck, gridBagConstraints);
        eraseButton.setText("Erase");
        eraseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eraseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(eraseButton, gridBagConstraints);
        dumpButton.setText("Dump");
        dumpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dumpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(dumpButton, gridBagConstraints);
        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents


    private void eraseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_eraseButtonActionPerformed
        examples.clear();
        canvas.repaint();
    }// GEN-LAST:event_eraseButtonActionPerformed


    private void zoomChanged(javax.swing.event.ChangeEvent evt) {
        // GEN-FIRST:event_zoomChanged
        canvas.repaint();
    }// GEN-LAST:event_zoomChanged


    private void dumpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_dumpButtonActionPerformed
        if (dumpDialog == null) {
            dumpDialog = new javax.swing.JDialog();
            dumpDialog.setTitle("Dump");
            dumpArea = new javax.swing.JTextArea();
            dumpDialog.add(new javax.swing.JScrollPane(dumpArea));
            dumpDialog.setSize(400, 400);
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        buf.append("        Point2D.Double[] d = { //  Digitized points \n");
        for (int i = 0; i < examples.size(); i++) {
            for (org.jhotdraw.geom.path.BezierPath.Node node : examples.get(i).digitized.nodes()) {
                buf.append("            new Point2D.Double(");
                buf.append(node.x[0]);
                buf.append(",");
                buf.append(node.y[0]);
                buf.append("),\n");
            }
        }
        buf.append("        };\n");
        dumpArea.setText(buf.toString());
        dumpDialog.setVisible(true);
    }// GEN-LAST:event_dumpButtonActionPerformed


    private void checkboxPerformed(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_checkboxPerformed
        canvas.repaint();
    }// GEN-LAST:event_checkboxPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton dumpButton;

    private javax.swing.JButton eraseButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JCheckBox showBezierCheck;

    private javax.swing.JCheckBox showControlsCheck;

    private javax.swing.JCheckBox showDigitizedCheck;

    private javax.swing.JCheckBox showPolylineCheck;

    private javax.swing.JCheckBox showPreprocessedCheck;

    private javax.swing.JLabel toleranceLabel;

    private javax.swing.JSlider zoomSlider;
}