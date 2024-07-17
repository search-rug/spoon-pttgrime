/* @(#)LabeledConnectionSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to connect two rectangles with a labeled connection, that has a labels at
 * both ends.
 */
public class LabeledLineConnectionFigureSample {
    public LabeledLineConnectionFigureSample() {
    }

    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Create the two rectangle figures
                org.jhotdraw.draw.figure.RectangleFigure ta = new org.jhotdraw.draw.figure.RectangleFigure();
                ta.setBounds(new java.awt.geom.Point2D.Double(10, 10), new java.awt.geom.Point2D.Double(100, 100));
                org.jhotdraw.draw.figure.RectangleFigure tb = new org.jhotdraw.draw.figure.RectangleFigure();
                tb.setBounds(new java.awt.geom.Point2D.Double(210, 110), new java.awt.geom.Point2D.Double(300, 200));
                // Create a labeled line connection
                org.jhotdraw.draw.figure.LabeledLineConnectionFigure cf = new org.jhotdraw.draw.figure.LabeledLineConnectionFigure();
                cf.setLiner(new org.jhotdraw.draw.liner.ElbowLiner());
                cf.setLayouter(new org.jhotdraw.draw.layouter.LocatorLayouter());
                // Create labels and add them to both ends of the labeled line connection
                org.jhotdraw.draw.figure.TextFigure startLabel = new org.jhotdraw.draw.figure.TextFigure();
                startLabel.attr().set(org.jhotdraw.draw.layouter.LocatorLayouter.LAYOUT_LOCATOR, new org.jhotdraw.draw.locator.BezierLabelLocator(0, (-java.lang.Math.PI) / 4, 8));
                startLabel.setEditable(false);
                startLabel.setText("start");
                cf.add(startLabel);
                org.jhotdraw.draw.figure.TextFigure endLabel = new org.jhotdraw.draw.figure.TextFigure();
                endLabel.attr().set(org.jhotdraw.draw.layouter.LocatorLayouter.LAYOUT_LOCATOR, new org.jhotdraw.draw.locator.BezierLabelLocator(1, java.lang.Math.PI + (java.lang.Math.PI / 4), 8));
                endLabel.setEditable(false);
                endLabel.setText("end");
                cf.add(endLabel);
                // Connect the figures
                cf.setStartConnector(ta.findConnector(org.jhotdraw.geom.Geom.center(ta.getBounds()), cf));
                cf.setEndConnector(tb.findConnector(org.jhotdraw.geom.Geom.center(tb.getBounds()), cf));
                // Add all figures to a drawing
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.add(ta);
                drawing.add(tb);
                drawing.add(cf);
                // Show the drawing
                javax.swing.JFrame f = new javax.swing.JFrame("My Drawing");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
                view.setDrawing(drawing);
                f.getContentPane().add(view.getComponent());
                f.setVisible(true);
            }
        });
    }
}