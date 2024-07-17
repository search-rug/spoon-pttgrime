/* @(#)SmartConnectionFigureSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to connect two text areas with an elbow connection.
 *
 * <p>The 'SmartConnectionFigure', that is used to connect the two areas draws with double stroke,
 * if the Figure at the start and at the end of the connection is the same.
 *
 * <p>In order to prevent editing of the stroke type by the user, the SmartConnectionFigure disables
 * the stroke type attribute. Unless it needs to be changed by the SmartConnectionFigure by itself.
 */
public class SmartConnectionFigureSample {
    private static class SmartConnectionFigure extends org.jhotdraw.draw.figure.LineConnectionFigure {
        private static final long serialVersionUID = 1L;

        public SmartConnectionFigure() {
            attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, false);
        }

        @java.lang.Override
        public void handleConnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
            attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, true);
            willChange();
            attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, start.getOwner() == end.getOwner() ? org.jhotdraw.draw.AttributeKeys.StrokeType.DOUBLE : org.jhotdraw.draw.AttributeKeys.StrokeType.BASIC);
            changed();
            attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, false);
        }

        @java.lang.Override
        public void handleDisconnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
            attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, true);
            willChange();
            attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, org.jhotdraw.draw.AttributeKeys.StrokeType.BASIC);
            changed();
            attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, false);
        }
    }

    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Create a simple drawing consisting of three
                // text areas and an elbow connection.
                org.jhotdraw.draw.figure.TextAreaFigure ta = new org.jhotdraw.draw.figure.TextAreaFigure();
                ta.setBounds(new java.awt.geom.Point2D.Double(10, 30), new java.awt.geom.Point2D.Double(100, 100));
                org.jhotdraw.draw.figure.TextAreaFigure tb = new org.jhotdraw.draw.figure.TextAreaFigure();
                tb.setBounds(new java.awt.geom.Point2D.Double(220, 130), new java.awt.geom.Point2D.Double(310, 210));
                org.jhotdraw.draw.figure.TextAreaFigure tc = new org.jhotdraw.draw.figure.TextAreaFigure();
                tc.setBounds(new java.awt.geom.Point2D.Double(220, 30), new java.awt.geom.Point2D.Double(310, 100));
                org.jhotdraw.draw.figure.ConnectionFigure cf = new org.jhotdraw.samples.mini.SmartConnectionFigureSample.SmartConnectionFigure();
                cf.setLiner(new org.jhotdraw.draw.liner.ElbowLiner());
                cf.setStartConnector(ta.findConnector(org.jhotdraw.geom.Geom.center(ta.getBounds()), cf));
                cf.setEndConnector(tb.findConnector(org.jhotdraw.geom.Geom.center(tb.getBounds()), cf));
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.add(ta);
                drawing.add(tb);
                drawing.add(tc);
                drawing.add(cf);
                // Show the drawing
                javax.swing.JFrame f = new javax.swing.JFrame("'Smart' ConnectionFigure Sample");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                // Set up the drawing view
                org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
                view.setDrawing(drawing);
                f.getContentPane().add(view.getComponent());
                // Set up the drawing editor
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.add(view);
                editor.setTool(new org.jhotdraw.draw.tool.DelegationSelectionTool());
                f.setVisible(true);
            }
        });
    }
}