/* @(#)SelectionToolSample.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * SelectionToolSample demonstrates how the <code>SelectionTool</code> works.
 *
 * <p>Internally, the <code>SelectionTool</code> uses three smaller tools (named as 'Tracker') to
 * fulfill its task. If the user presses the mouse button over an empty area of a drawing, the
 * <code>SelectAreaTracker</code> comes into action. If the user presses the mouse button over a
 * figure, the <code>DragTracker</code> comes into action. If the user presses the mouse button over
 * a handle, the <code>HandleTracker</code> comes into action.
 *
 * <p>You need to edit the source code as marked below.
 *
 * <p>With this program you can:
 *
 * <ol>
 *   <li>See how the <code>SelectionTool</code> interacts with a <code>LineFigure</code>.
 *   <li>See how the <code>SelectAreaTracker</code> interacts with a <code>LineFigure</code>.
 *   <li>See how the <code>DragTracker</code> interacts with a <code>LineFigure</code>.
 *   <li>See how the <code>HandleTracker</code> interacts with a <code>LineFigure</code>.
 * </ol>
 *
 * @author Pondus
 * @version $Id$
 */
public class SelectionToolSample {
    /**
     * Creates a new instance of SelectionToolSample
     */
    public SelectionToolSample() {
        org.jhotdraw.draw.figure.LineFigure lf = new org.jhotdraw.draw.figure.LineFigure();
        lf.setBounds(new java.awt.geom.Point2D.Double(40, 40), new java.awt.geom.Point2D.Double(200, 40));
        // Add all figures to a drawing
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        drawing.add(lf);
        // Show the drawing
        javax.swing.JFrame f = new javax.swing.JFrame("UltraMini");
        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 300);
        org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
        view.setDrawing(drawing);
        f.getContentPane().add(view.getComponent());
        // set up the drawing editor
        org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
        editor.add(view);
        // Activate the following line to see the SelectionTool in full
        // action.
        editor.setTool(new org.jhotdraw.draw.tool.SelectionTool());
        // Activate the following line to only see the SelectAreaTracker in
        // action.
        // editor.setTool(new SelectAreaTracker());
        // Activate the following line to only see the DragTracker in
        // action.
        // editor.setTool(new DragTracker(lf));
        // Activate the following lines to only see the HandleTracker in
        // action.
        // view.selectAll();
        // editor.setTool(new HandleTracker(view.findHandle(view.drawingToView(lf.getStartPoint()))));
        f.setVisible(true);
    }

    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                new org.jhotdraw.samples.mini.SelectionToolSample();
            }
        });
    }
}