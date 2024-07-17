/* @(#)CreationToolSample.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to create a drawing editor with a creation tool for figures with pre-defined
 * attribute values: the example editor creates green rectangles.
 */
public class CreationToolSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                // Create a drawing view with a default drawing, and
                // input/output formats for basic clipboard support.
                org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
                org.jhotdraw.draw.DefaultDrawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.addInputFormat(new org.jhotdraw.io.SerializationInputFormat());
                drawing.addOutputFormat(new org.jhotdraw.io.SerializationOutputFormat());
                view.setDrawing(drawing);
                // Create a common drawing editor for the views
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.add(view);
                // Create a tool bar
                javax.swing.JToolBar tb = new javax.swing.JToolBar();
                // Add a selection tool to the toolbar.
                org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor);
                // Add a creation tool for green rectangles to the toolbar.
                java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, java.awt.Color.GREEN);
                org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.RectangleFigure(), a), "edit.createRectangle", labels);
                tb.setOrientation(javax.swing.JToolBar.VERTICAL);
                // Put all together into a JFrame
                javax.swing.JFrame f = new javax.swing.JFrame("Editor with Creation Tool");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                // Set up the content pane
                // Place the toolbar on the left
                f.getContentPane().add(tb, java.awt.BorderLayout.WEST);
                // Place the drawing view inside a scroll pane in the center
                javax.swing.JScrollPane sp = new javax.swing.JScrollPane(view.getComponent());
                sp.setPreferredSize(new java.awt.Dimension(200, 200));
                f.getContentPane().add(sp, java.awt.BorderLayout.CENTER);
                f.setVisible(true);
            }
        });
    }
}