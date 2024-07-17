/* @(#)SVGView.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
import org.jhotdraw.api.app.View;
/**
 * Provides a view on a SVG drawing.
 *
 * <p>See {@link View} interface on how this view interacts with an application.
 */
// End of variables declaration//GEN-END:variables
public class SVGView extends org.jhotdraw.app.AbstractView {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String DRAWING_PROPERTY = "drawing";

    public static final java.lang.String GRID_VISIBLE_PROPERTY = "gridVisible";

    protected org.jhotdraw.gui.JFileURIChooser exportChooser;

    /**
     * Each SVGView uses its own undo redo manager. This allows for undoing and redoing actions per
     * view.
     */
    private org.jhotdraw.undo.UndoRedoManager undo;

    private java.beans.PropertyChangeListener propertyHandler;

    /**
     * Creates a new View.
     */
    public SVGView() {
        initComponents();
        undo = svgPanel.getUndoRedoManager();
        org.jhotdraw.draw.Drawing oldDrawing = svgPanel.getDrawing();
        svgPanel.setDrawing(createDrawing());
        firePropertyChange(org.jhotdraw.samples.svg.SVGView.DRAWING_PROPERTY, oldDrawing, svgPanel.getDrawing());
        svgPanel.getDrawing().addUndoableEditListener(undo);
        initActions();
        undo.addPropertyChangeListener(propertyHandler = new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                setHasUnsavedChanges(undo.hasSignificantEdits());
            }
        });
    }

    @java.lang.Override
    public void dispose() {
        clear();
        undo.removePropertyChangeListener(propertyHandler);
        propertyHandler = null;
        svgPanel.dispose();
        super.dispose();
    }

    /**
     * Creates a new Drawing for this View.
     */
    protected org.jhotdraw.draw.Drawing createDrawing() {
        return svgPanel.createDrawing();
    }

    /**
     * Creates a Pageable object for printing the View.
     */
    public java.awt.print.Pageable createPageable() {
        return new org.jhotdraw.draw.print.DrawingPageable(svgPanel.getDrawing());
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return svgPanel.getEditor();
    }

    public void setEditor(org.jhotdraw.draw.DrawingEditor newValue) {
        svgPanel.setEditor(newValue);
    }

    public org.jhotdraw.undo.UndoRedoManager getUndoManager() {
        return undo;
    }

    /**
     * Initializes view specific actions.
     */
    private void initActions() {
        getActionMap().put(org.jhotdraw.action.edit.UndoAction.ID, undo.getUndoAction());
        getActionMap().put(org.jhotdraw.action.edit.RedoAction.ID, undo.getRedoAction());
    }

    @java.lang.Override
    protected void setHasUnsavedChanges(boolean newValue) {
        super.setHasUnsavedChanges(newValue);
        undo.setHasSignificantEdits(newValue);
    }

    /**
     * Writes the view to the specified uri.
     */
    @java.lang.Override
    public void write(java.net.URI uri, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        new org.jhotdraw.samples.svg.io.SVGOutputFormat().write(new java.io.File(uri), svgPanel.getDrawing());
    }

    /**
     * Reads the view from the specified uri.
     */
    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void read(final java.net.URI uri, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        try {
            org.jhotdraw.gui.JFileURIChooser fc = ((org.jhotdraw.gui.JFileURIChooser) (chooser));
            final org.jhotdraw.draw.Drawing drawing = createDrawing();
            // We start with the selected uri format in the uri chooser,
            // and then try out all formats we can import.
            // We need to try out all formats, because the user may have
            // chosen to load a uri without having used the uri chooser.
            java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat> fileFilterInputFormatMap = null;
            if (fc != null) {
                fileFilterInputFormatMap = ((java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat>) (fc.getClientProperty(org.jhotdraw.samples.svg.SVGApplicationModel.INPUT_FORMAT_MAP_CLIENT_PROPERTY)));
            }
            // private HashMap<javax.swing.filechooser.FileFilter, OutputFormat>
            // fileFilterOutputFormatMap;
            org.jhotdraw.draw.io.InputFormat selectedFormat = (fc == null) ? null : fileFilterInputFormatMap.get(fc.getFileFilter());
            boolean success = false;
            if (selectedFormat != null) {
                try {
                    selectedFormat.read(uri, drawing, true);
                    success = true;
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    // try with the next input format
                }
            }
            if (!success) {
                for (org.jhotdraw.draw.io.InputFormat sfi : drawing.getInputFormats()) {
                    if (sfi != selectedFormat) {
                        try {
                            sfi.read(uri, drawing, true);
                            success = true;
                            break;
                        } catch (java.lang.Exception e) {
                            // try with the next input format
                        }
                    }
                }
            }
            if (!success) {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                throw new java.io.IOException(labels.getFormatted("file.open.unsupportedFileFormat.message", org.jhotdraw.net.URIUtil.getName(uri)));
            }
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    org.jhotdraw.draw.Drawing oldDrawing = svgPanel.getDrawing();
                    svgPanel.setDrawing(drawing);
                    firePropertyChange(org.jhotdraw.samples.svg.SVGView.DRAWING_PROPERTY, oldDrawing, svgPanel.getDrawing());
                    undo.discardAllEdits();
                }
            });
        } catch (java.lang.InterruptedException e) {
            java.lang.InternalError error = new java.lang.InternalError();
            e.initCause(e);
            throw error;
        } catch (java.lang.reflect.InvocationTargetException e) {
            java.lang.InternalError error = new java.lang.InternalError();
            error.initCause(e);
            throw error;
        }
    }

    public org.jhotdraw.draw.Drawing getDrawing() {
        return svgPanel.getDrawing();
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        svgPanel.setEnabled(newValue);
        super.setEnabled(newValue);
    }

    /**
     * Clears the view.
     */
    @java.lang.Override
    public void clear() {
        final org.jhotdraw.draw.Drawing newDrawing = createDrawing();
        try {
            java.lang.Runnable r = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    org.jhotdraw.draw.Drawing oldDrawing = svgPanel.getDrawing();
                    svgPanel.setDrawing(newDrawing);
                    firePropertyChange(org.jhotdraw.samples.svg.SVGView.DRAWING_PROPERTY, oldDrawing, newDrawing);
                    if (oldDrawing != null) {
                        oldDrawing.removeAllChildren();
                        oldDrawing.removeUndoableEditListener(undo);
                    }
                    undo.discardAllEdits();
                    newDrawing.addUndoableEditListener(undo);
                }
            };
            if (javax.swing.SwingUtilities.isEventDispatchThread()) {
                r.run();
            } else {
                javax.swing.SwingUtilities.invokeAndWait(r);
            }
        } catch (java.lang.reflect.InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (java.lang.InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @java.lang.Override
    public boolean canSaveTo(java.net.URI file) {
        return file.getPath().endsWith(".svg") || file.getPath().endsWith(".svgz");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        svgPanel = new org.jhotdraw.samples.svg.SVGDrawingPanel();
        setLayout(new java.awt.BorderLayout());
        add(svgPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jhotdraw.samples.svg.SVGDrawingPanel svgPanel;
}