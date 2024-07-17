/* @(#)ImageTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * A tool to create new figures from an input file. If the file holds a bitmap image, this tool
 * creates a SVGImageFigure. If the file holds a SVG or a SVGZ image, ths tool creates a
 * SVGGroupFigure.
 *
 * <p>Immediately, after the ImageTool has been activated, it opens a JFileChooser, letting the user
 * specify a file. The the user then performs the following mouse gesture:
 *
 * <ol>
 *   <li>Press the mouse button and drag the mouse over the DrawingView. This defines the bounds of
 *       the created Figure.
 * </ol>
 */
public class SVGCreateFromFileTool extends org.jhotdraw.draw.tool.CreationTool {
    private static final long serialVersionUID = 1L;

    protected java.awt.FileDialog fileDialog;

    protected javax.swing.JFileChooser fileChooser;

    protected org.jhotdraw.draw.figure.CompositeFigure groupPrototype;

    protected org.jhotdraw.draw.figure.ImageHolderFigure imagePrototype;

    protected boolean useFileDialog;

    public SVGCreateFromFileTool(org.jhotdraw.draw.figure.ImageHolderFigure imagePrototype, org.jhotdraw.draw.figure.CompositeFigure groupPrototype) {
        super(imagePrototype);
        this.groupPrototype = groupPrototype;
        this.imagePrototype = imagePrototype;
    }

    public SVGCreateFromFileTool(org.jhotdraw.draw.figure.ImageHolderFigure imagePrototype, org.jhotdraw.draw.figure.CompositeFigure groupPrototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        super(imagePrototype, attributes);
        this.groupPrototype = groupPrototype;
        this.imagePrototype = imagePrototype;
    }

    public void setUseFileDialog(boolean newValue) {
        useFileDialog = newValue;
        if (useFileDialog) {
            fileChooser = null;
        } else {
            fileDialog = null;
        }
    }

    public boolean isUseFileDialog() {
        return useFileDialog;
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
        final org.jhotdraw.draw.DrawingView v = getView();
        if (v == null) {
            return;
        }
        final java.io.File file;
        if (useFileDialog) {
            getFileDialog().setVisible(true);
            if (getFileDialog().getFile() != null) {
                file = new java.io.File(getFileDialog().getDirectory(), getFileDialog().getFile());
            } else {
                file = null;
            }
        } else if (getFileChooser().showOpenDialog(v.getComponent()) == javax.swing.JFileChooser.APPROVE_OPTION) {
            file = getFileChooser().getSelectedFile();
        } else {
            file = null;
        }
        if (file != null) {
            if (file.getName().toLowerCase().endsWith(".svg") || file.getName().toLowerCase().endsWith(".svgz")) {
                prototype = groupPrototype.clone();
                new javax.swing.SwingWorker<org.jhotdraw.draw.Drawing, org.jhotdraw.draw.Drawing>() {
                    @java.lang.Override
                    protected org.jhotdraw.draw.Drawing doInBackground() throws java.lang.Exception {
                        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
                        org.jhotdraw.draw.io.InputFormat in = (file.getName().toLowerCase().endsWith(".svg")) ? new org.jhotdraw.samples.svg.io.SVGInputFormat() : new org.jhotdraw.samples.svg.io.SVGZInputFormat();
                        in.read(file.toURI(), drawing);
                        return drawing;
                    }

                    @java.lang.Override
                    protected void done() {
                        try {
                            org.jhotdraw.draw.Drawing drawing = get();
                            org.jhotdraw.draw.figure.CompositeFigure parent;
                            if (createdFigure == null) {
                                parent = ((org.jhotdraw.draw.figure.CompositeFigure) (prototype));
                                for (org.jhotdraw.draw.figure.Figure f : drawing.getChildren()) {
                                    parent.basicAdd(f);
                                }
                            } else {
                                parent = ((org.jhotdraw.draw.figure.CompositeFigure) (createdFigure));
                                parent.willChange();
                                for (org.jhotdraw.draw.figure.Figure f : drawing.getChildren()) {
                                    parent.add(f);
                                }
                                parent.changed();
                            }
                        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.SVGCreateFromFileTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            failed(ex);
                        }
                    }

                    protected void failed(java.lang.Throwable t) {
                        javax.swing.JOptionPane.showMessageDialog(v.getComponent(), t.getMessage(), null, javax.swing.JOptionPane.ERROR_MESSAGE);
                        getDrawing().remove(createdFigure);
                        fireToolDone();
                    }
                }.execute();
            } else {
                prototype = imagePrototype;
                final org.jhotdraw.draw.figure.ImageHolderFigure loaderFigure = ((org.jhotdraw.draw.figure.ImageHolderFigure) (prototype.clone()));
                new javax.swing.SwingWorker() {
                    @java.lang.Override
                    protected java.lang.Object doInBackground() throws java.lang.Exception {
                        loaderFigure.loadImage(file);
                        return null;
                    }

                    @java.lang.Override
                    protected void done() {
                        try {
                            get();
                            try {
                                if (createdFigure == null) {
                                    ((org.jhotdraw.draw.figure.ImageHolderFigure) (prototype)).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                                } else {
                                    ((org.jhotdraw.draw.figure.ImageHolderFigure) (createdFigure)).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                                }
                            } catch (java.io.IOException ex) {
                                javax.swing.JOptionPane.showMessageDialog(v.getComponent(), ex.getMessage(), null, javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.SVGCreateFromFileTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            failed(ex);
                        }
                    }

                    protected void failed(java.lang.Throwable t) {
                        javax.swing.JOptionPane.showMessageDialog(v.getComponent(), t.getMessage(), null, javax.swing.JOptionPane.ERROR_MESSAGE);
                        getDrawing().remove(createdFigure);
                        fireToolDone();
                    }
                }.execute();
            }
        } else // getDrawing().remove(createdFigure);
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    @java.lang.Override
    protected org.jhotdraw.draw.figure.Figure createFigure() {
        if (prototype instanceof org.jhotdraw.draw.figure.CompositeFigure) {
            // we must not apply default attributs to the composite figure,
            // because this would change the look of the figures that we
            // read from the SVG file.
            return prototype.clone();
        } else {
            return super.createFigure();
        }
    }

    private javax.swing.JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new javax.swing.JFileChooser();
        }
        return fileChooser;
    }

    private java.awt.FileDialog getFileDialog() {
        if (fileDialog == null) {
            fileDialog = new java.awt.FileDialog(new java.awt.Frame());
        }
        return fileDialog;
    }
}