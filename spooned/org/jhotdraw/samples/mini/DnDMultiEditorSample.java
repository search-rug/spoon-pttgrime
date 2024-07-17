/* @(#)MultiEditorSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to create a drawing editor which acts on four drawing views.
 */
public class DnDMultiEditorSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                // Create four drawing views, each one with its own drawing
                org.jhotdraw.draw.DrawingView view1 = new org.jhotdraw.draw.DefaultDrawingView();
                org.jhotdraw.draw.DrawingView view2 = new org.jhotdraw.draw.DefaultDrawingView();
                org.jhotdraw.draw.DrawingView view3 = new org.jhotdraw.draw.DefaultDrawingView();
                org.jhotdraw.draw.DrawingView view4 = new org.jhotdraw.draw.DefaultDrawingView();
                view1.setDrawing(org.jhotdraw.samples.mini.DnDMultiEditorSample.createDrawing());
                view2.setDrawing(org.jhotdraw.samples.mini.DnDMultiEditorSample.createDrawing());
                view3.setDrawing(org.jhotdraw.samples.mini.DnDMultiEditorSample.createDrawing());
                view4.setDrawing(org.jhotdraw.samples.mini.DnDMultiEditorSample.createDrawing());
                // Create a common drawing editor for the views
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.add(view1);
                editor.add(view2);
                editor.add(view3);
                editor.add(view4);
                // Create a tool bar with selection tool and a
                // creation tool for rectangle figures.
                javax.swing.JToolBar tb = new javax.swing.JToolBar();
                org.jhotdraw.draw.tool.SelectionTool selectionTool = new org.jhotdraw.draw.tool.SelectionTool();
                selectionTool.setDragTracker(new org.jhotdraw.draw.tool.DnDTracker());
                org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor, selectionTool);
                org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.RectangleFigure()), "edit.createRectangle", labels);
                tb.setOrientation(javax.swing.JToolBar.VERTICAL);
                // Put all together into a JFrame
                javax.swing.JFrame f = new javax.swing.JFrame("Multi-Editor");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                // Set up the content pane
                // Place the toolbar on the left
                // Place each drawing view into a scroll pane of its own
                // and put them into a larger scroll pane.
                javax.swing.JPanel innerPane = new javax.swing.JPanel();
                innerPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));
                javax.swing.JScrollPane sp;
                innerPane.add(sp = new javax.swing.JScrollPane(view1.getComponent()));
                sp.setPreferredSize(new java.awt.Dimension(200, 200));
                innerPane.add(sp = new javax.swing.JScrollPane(view2.getComponent()));
                sp.setPreferredSize(new java.awt.Dimension(200, 200));
                innerPane.add(sp = new javax.swing.JScrollPane(view3.getComponent()));
                sp.setPreferredSize(new java.awt.Dimension(200, 200));
                innerPane.add(sp = new javax.swing.JScrollPane(view4.getComponent()));
                sp.setPreferredSize(new java.awt.Dimension(200, 200));
                f.getContentPane().add(new javax.swing.JScrollPane(innerPane));
                f.getContentPane().add(tb, java.awt.BorderLayout.WEST);
                f.setVisible(true);
            }
        });
    }

    /**
     * Creates a drawing with input and output formats, so that drawing figures can be copied and
     * pasted between drawing views.
     *
     * @return a drawing
     */
    private static org.jhotdraw.draw.Drawing createDrawing() {
        // Create a default drawing with
        // input/output formats for basic clipboard support.
        org.jhotdraw.draw.DefaultDrawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        drawing.addInputFormat(new org.jhotdraw.io.SerializationInputFormat());
        drawing.addOutputFormat(new org.jhotdraw.io.SerializationOutputFormat());
        drawing.addOutputFormat(new org.jhotdraw.io.ImageOutputFormat());
        return drawing;
    }
}