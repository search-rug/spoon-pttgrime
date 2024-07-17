/* @(#)ConnectingFiguresSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to connect two text areas with an elbow connection.
 */
public class ConnectingFiguresSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            public void run() {
                // Create the two text areas
                org.jhotdraw.draw.figure.TextAreaFigure ta = new org.jhotdraw.draw.figure.TextAreaFigure();
                ta.setBounds(new java.awt.geom.Point2D.Double(10, 10), new java.awt.geom.Point2D.Double(100, 100));
                org.jhotdraw.draw.figure.TextAreaFigure tb = new org.jhotdraw.draw.figure.TextAreaFigure();
                tb.setBounds(new java.awt.geom.Point2D.Double(210, 110), new java.awt.geom.Point2D.Double(300, 200));
                // Create an elbow connection
                org.jhotdraw.draw.figure.ConnectionFigure cf = new org.jhotdraw.draw.figure.LineConnectionFigure();
                cf.setLiner(new org.jhotdraw.draw.liner.ElbowLiner());
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