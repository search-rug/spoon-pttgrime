/* @(#)LayouterSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to layout two editable text figures and a line figure within a graphical
 * composite figure.
 */
public class LayouterSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Create a graphical composite figure.
                org.jhotdraw.draw.figure.GraphicalCompositeFigure composite = new org.jhotdraw.draw.figure.GraphicalCompositeFigure();
                // Add child figures to the composite figure
                composite.add(new org.jhotdraw.draw.figure.TextFigure("Above the line"));
                composite.add(new org.jhotdraw.draw.figure.LineFigure());
                composite.add(new org.jhotdraw.draw.figure.TextFigure("Below the line"));
                // Set a layouter and perform the layout
                composite.setLayouter(new org.jhotdraw.draw.layouter.VerticalLayouter());
                composite.layout(1.0);
                // Add the composite figure to a drawing
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.add(composite);
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