/* @(#)FileIconsSample.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * Example showing how to lay out composite figures.
 */
public class FileIconsSample {
    public static void main(java.lang.String[] args) {
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Let the user choose a directory
                javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
                fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
                fc.setDialogTitle("Choose a directory");
                if (fc.showOpenDialog(null) != javax.swing.JFileChooser.APPROVE_OPTION) {
                    java.lang.System.exit(0);
                }
                // Create a drawing
                org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                // Add a figure for each file to the drawing
                java.io.File dir = fc.getSelectedFile();
                java.io.File[] files = dir.listFiles();
                javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
                int maxColumn = java.lang.Math.max(((int) (java.lang.Math.sqrt(files.length))), 1);
                double tx = 0;
                double ty = 0;
                double rowHeight = 0;
                int i = 0;
                for (java.io.File f : files) {
                    // Create an image figure for the file icon
                    javax.swing.Icon icon = fsv.getSystemIcon(f);
                    java.awt.image.BufferedImage bimg = new java.awt.image.BufferedImage(icon.getIconWidth(), icon.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D g = bimg.createGraphics();
                    icon.paintIcon(null, g, 0, 0);
                    g.dispose();
                    org.jhotdraw.draw.figure.ImageFigure imf = new org.jhotdraw.draw.figure.ImageFigure();
                    imf.setBufferedImage(bimg);
                    imf.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
                    imf.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double(icon.getIconWidth(), icon.getIconHeight()));
                    // Creata TextAreaFigure for the file name
                    // We limit its width to 100 Pixels
                    org.jhotdraw.draw.figure.TextAreaFigure tef = new org.jhotdraw.draw.figure.TextAreaFigure(f.getName());
                    org.jhotdraw.geom.Dimension2DDouble dim = tef.getPreferredTextSize(100);
                    org.jhotdraw.geom.Insets2D.Double insets = tef.getInsets();
                    tef.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double((java.lang.Math.max(100, dim.width) + insets.left) + insets.right, (dim.height + insets.top) + insets.bottom));
                    tef.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
                    tef.attr().set(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
                    tef.attr().set(org.jhotdraw.draw.AttributeKeys.TEXT_ALIGNMENT, org.jhotdraw.draw.AttributeKeys.Alignment.CENTER);
                    // Alternatively, you could just create a TextFigure
                    /* TextFigure tef = new TextFigure(f.getName()); */
                    // Create a GraphicalCompositeFigure with vertical layout
                    // and add the icon and the text figure to it
                    org.jhotdraw.draw.figure.GraphicalCompositeFigure gcf = new org.jhotdraw.draw.figure.GraphicalCompositeFigure();
                    gcf.setLayouter(new org.jhotdraw.draw.layouter.VerticalLayouter());
                    gcf.attr().set(org.jhotdraw.draw.AttributeKeys.COMPOSITE_ALIGNMENT, org.jhotdraw.draw.AttributeKeys.Alignment.CENTER);
                    gcf.add(imf);
                    gcf.add(tef);
                    gcf.layout(1.0);
                    // Lay out the graphical composite figures on the drawing
                    if (((i++) % maxColumn) == 0) {
                        ty += rowHeight + 20;
                        tx = 0;
                        rowHeight = 0;
                    }
                    java.awt.geom.Rectangle2D.Double b = gcf.getBounds();
                    rowHeight = java.lang.Math.max(rowHeight, b.height);
                    java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
                    at.translate(tx, ty);
                    gcf.transform(at);
                    tx += b.width + 20;
                    drawing.add(gcf);
                }
                // Show the drawing
                javax.swing.JFrame f = new javax.swing.JFrame("Contents of directory " + dir.getName());
                f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 300);
                org.jhotdraw.draw.DrawingView view = new org.jhotdraw.draw.DefaultDrawingView();
                view.setDrawing(drawing);
                f.getContentPane().add(view.getComponent());
                org.jhotdraw.draw.DrawingEditor editor = new org.jhotdraw.editor.DefaultDrawingEditor();
                editor.setTool(new org.jhotdraw.draw.tool.DelegationSelectionTool());
                editor.add(view);
                editor.setActiveView(view);
                f.setVisible(true);
            }
        });
    }
}