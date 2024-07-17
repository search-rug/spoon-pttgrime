/* @(#)EditorSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to create an editor that can edit figures on a drawing using the
 * DelegationSelectionTool.
 */
public class EditorSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Create a simple drawing consisting of three
                // text areas and an elbow connection.
                org.jhotdraw.draw.figure.TextAreaFigure ta = new org.jhotdraw.draw.figure.TextAreaFigure();
                ta.setBounds(new java.awt.geom.Point2D.Double(10, 10), new java.awt.geom.Point2D.Double(100, 100));
                org.jhotdraw.draw.figure.TextAreaFigure tb = new org.jhotdraw.draw.figure.TextAreaFigure();
                tb.setBounds(new java.awt.geom.Point2D.Double(220, 120), new java.awt.geom.Point2D.Double(310, 210));
                org.jhotdraw.draw.figure.TextAreaFigure tc = new org.jhotdraw.draw.figure.TextAreaFigure();
                tc.setBounds(new java.awt.geom.Point2D.Double(220, 10), new java.awt.geom.Point2D.Double(310, 100));
                org.jhotdraw.draw.figure.ConnectionFigure cf = new org.jhotdraw.draw.figure.LineConnectionFigure();
                cf.setLiner(new org.jhotdraw.draw.liner.ElbowLiner());
                cf.setStartConnector(ta.findConnector(org.jhotdraw.geom.Geom.center(ta.getBounds()), cf));
                cf.setEndConnector(tb.findConnector(org.jhotdraw.geom.Geom.center(tb.getBounds()), cf));
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.add(ta);
                drawing.add(tb);
                drawing.add(tc);
                drawing.add(cf);
                // Create a frame with a drawing view and a drawing editor
                javax.swing.JFrame f = new javax.swing.JFrame("My Drawing");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
                view.setDrawing(drawing);
                f.getContentPane().add(view.getComponent());
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.add(view);
                editor.setTool(new org.jhotdraw.draw.tool.DelegationSelectionTool());
                f.setVisible(true);
            }
        });
    }
}