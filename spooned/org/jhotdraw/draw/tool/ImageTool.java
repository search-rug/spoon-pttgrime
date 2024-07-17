/* @(#)ImageTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.ImageHolderFigure;
/**
 * A tool to create new figures that implement the ImageHolderFigure interface, such as ImageFigure.
 * The figure to be created is specified by a prototype.
 *
 * <p>Immediately, after the ImageTool has been activated, it opens a JFileChooser, letting the user
 * specify an image file. The the user then performs the following mouse gesture:
 *
 * <ol>
 *   <li>Press the mouse button and drag the mouse over the DrawingView. This defines the bounds of
 *       the created figure.
 * </ol>
 *
 * <hr> <b>Design Patterns</b>
 *
 * <p><em>Prototype</em><br>
 * The {@code ImageTool} creates new figures by cloning a prototype {@code ImageHolderFigure}
 * object.<br>
 * Prototype: {@link ImageHolderFigure}; Client: {@link ImageTool}. <hr>
 */
public class ImageTool extends org.jhotdraw.draw.tool.CreationTool {
    private static final long serialVersionUID = 1L;

    protected java.awt.FileDialog fileDialog;

    protected javax.swing.JFileChooser fileChooser;

    protected boolean useFileDialog;

    public ImageTool(org.jhotdraw.draw.figure.ImageHolderFigure prototype) {
        super(prototype);
    }

    public ImageTool(org.jhotdraw.draw.figure.ImageHolderFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        super(prototype, attributes);
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
                        get();// will throw an ExecutionException if in doInBackground something went wrong.

                        if (createdFigure == null) {
                            ((org.jhotdraw.draw.figure.ImageHolderFigure) (prototype)).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                        } else {
                            ((org.jhotdraw.draw.figure.ImageHolderFigure) (createdFigure)).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                        }
                    } catch (java.io.IOException ex) {
                        javax.swing.JOptionPane.showMessageDialog(v.getComponent(), ex.getMessage(), null, javax.swing.JOptionPane.ERROR_MESSAGE);
                    } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                        javax.swing.JOptionPane.showMessageDialog(v.getComponent(), ex.getMessage(), null, javax.swing.JOptionPane.ERROR_MESSAGE);
                        getDrawing().remove(createdFigure);
                        fireToolDone();
                    }
                }
            }.execute();
        } else // getDrawing().remove(createdFigure);
        if (isToolDoneAfterCreation()) {
            fireToolDone();
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