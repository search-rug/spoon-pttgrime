/* @(#)MovableChildFiguresSample.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to create a graphical composite figure which holds component figures that can
 * be moved independently using handles.
 *
 * <p>This version uses the AbstractDrawingView.
 *
 * @author Tobias Warneke
 */
public class MovableChildFiguresSampleWithAbstractDrawingView {
    private static class LabeledEllipseFigure extends org.jhotdraw.draw.figure.GraphicalCompositeFigure {
        private static final long serialVersionUID = 1L;

        public LabeledEllipseFigure() {
            setPresentationFigure(new org.jhotdraw.draw.figure.EllipseFigure());
            org.jhotdraw.draw.figure.LabelFigure label = new org.jhotdraw.draw.figure.LabelFigure("Label");
            label.transform(new java.awt.geom.AffineTransform(0, 0, 0, 0, 25, 37));
            add(label);
        }

        /**
         * Return default handles from the presentation figure.
         */
        @java.lang.Override
        public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
            java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
            switch (detailLevel) {
                case 0 :
                    org.jhotdraw.draw.handle.MoveHandle.addMoveHandles(this, handles);
                    for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                        org.jhotdraw.draw.handle.MoveHandle.addMoveHandles(child, handles);
                        handles.add(new org.jhotdraw.draw.handle.DragHandle(child));
                    }
                    break;
                case 1 :
                    org.jhotdraw.draw.handle.ResizeHandleKit.addResizeHandles(this, handles);
                    break;
                default :
                    break;
            }
            return handles;
        }
    }

    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Create a simple drawing consisting of three
                // text areas and an elbow connection.
                org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure ta = new org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure();
                ta.setBounds(new java.awt.geom.Point2D.Double(10, 10), new java.awt.geom.Point2D.Double(100, 100));
                org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure tb = new org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure();
                tb.setBounds(new java.awt.geom.Point2D.Double(220, 120), new java.awt.geom.Point2D.Double(310, 210));
                org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure tc = new org.jhotdraw.samples.mini.MovableChildFiguresSampleWithAbstractDrawingView.LabeledEllipseFigure();
                tc.setBounds(new java.awt.geom.Point2D.Double(220, 10), new java.awt.geom.Point2D.Double(310, 100));
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                drawing.add(ta);
                drawing.add(tb);
                drawing.add(tc);
                // Create a frame with a drawing view and a drawing editor
                javax.swing.JFrame f = new javax.swing.JFrame("My Drawing");
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                final AbstractDrawingViewImpl view = new AbstractDrawingViewImpl();
                final javax.swing.JPanel drawPanel = new javax.swing.JPanel() {
                    @java.lang.Override
                    protected void printComponent(java.awt.Graphics g) {
                        view.printComponent(g);
                    }

                    @java.lang.Override
                    protected void paintComponent(java.awt.Graphics g) {
                        view.paintComponent(g);
                    }

                    @java.lang.Override
                    public void setBounds(int x, int y, int width, int height) {
                        super.setBounds(x, y, width, height);// To change body of generated methods, choose Tools | Templates.

                        view.fireViewTransformChanged();
                    }
                };
                drawPanel.setSize(500, 500);
                drawPanel.setOpaque(true);
                view.setDrawPanel(drawPanel);
                view.setDrawing(drawing);
                f.add(drawPanel);
                f.add(new javax.swing.JLabel("Press space bar to toggle handles."), java.awt.BorderLayout.SOUTH);
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.add(view);
                editor.setTool(new org.jhotdraw.draw.tool.DelegationSelectionTool());
                f.setVisible(true);
            }

            class AbstractDrawingViewImpl extends org.jhotdraw.draw.AbstractDrawingView {
                javax.swing.JComponent drawPanel = null;

                public void setDrawPanel(javax.swing.JComponent drawPanel) {
                    this.drawPanel = drawPanel;
                }

                @java.lang.Override
                public java.awt.geom.AffineTransform getDrawingToViewTransform() {
                    java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
                    transform.setToRotation(0.9, drawPanel.getWidth() / 2, drawPanel.getHeight() / 2);
                    return transform;
                }

                @java.lang.Override
                public void repaint(java.awt.Rectangle r) {
                    drawPanel.repaint(r);
                }

                @java.lang.Override
                public java.awt.Color getBackground() {
                    return drawPanel.getBackground();
                }

                @java.lang.Override
                public void repaint() {
                    drawPanel.repaint();
                }

                @java.lang.Override
                public int getWidth() {
                    return drawPanel.getWidth();
                }

                @java.lang.Override
                public int getHeight() {
                    return drawPanel.getHeight();
                }

                @java.lang.Override
                public void revalidate() {
                    drawPanel.revalidate();
                }

                @java.lang.Override
                public void requestFocus() {
                    drawPanel.requestFocus();
                }

                @java.lang.Override
                public javax.swing.JComponent getComponent() {
                    return drawPanel;
                }

                @java.lang.Override
                public double getScaleFactor() {
                    return 1;
                }

                @java.lang.Override
                public void setScaleFactor(double newValue) {
                }

                @java.lang.Override
                public void setEnabled(boolean newValue) {
                    drawPanel.setEnabled(newValue);
                }

                @java.lang.Override
                public boolean isEnabled() {
                    return drawPanel.isEnabled();
                }

                @java.lang.Override
                public void addMouseListener(java.awt.event.MouseListener l) {
                    drawPanel.addMouseListener(l);
                }

                @java.lang.Override
                public void removeMouseListener(java.awt.event.MouseListener l) {
                    drawPanel.removeMouseListener(l);
                }

                @java.lang.Override
                public void addKeyListener(java.awt.event.KeyListener l) {
                    drawPanel.addKeyListener(l);
                }

                @java.lang.Override
                public void removeKeyListener(java.awt.event.KeyListener l) {
                    drawPanel.removeKeyListener(l);
                }

                @java.lang.Override
                public void addMouseMotionListener(java.awt.event.MouseMotionListener l) {
                    drawPanel.addMouseMotionListener(l);
                }

                @java.lang.Override
                public void removeMouseMotionListener(java.awt.event.MouseMotionListener l) {
                    drawPanel.removeMouseMotionListener(l);
                }

                @java.lang.Override
                public void removeMouseWheelListener(java.awt.event.MouseWheelListener l) {
                    drawPanel.removeMouseWheelListener(l);
                }

                @java.lang.Override
                public void addMouseWheelListener(java.awt.event.MouseWheelListener l) {
                    drawPanel.addMouseWheelListener(l);
                }

                @java.lang.Override
                public void setCursor(java.awt.Cursor c) {
                    drawPanel.setCursor(c);
                }
            }
        });
    }
}