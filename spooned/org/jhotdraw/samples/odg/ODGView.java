/* @(#)ODGView.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
import org.jhotdraw.api.app.View;
/**
 * Provides a view on a ODG drawing.
 *
 * <p>See {@link View} interface on how this view interacts with an application.
 */
// End of variables declaration//GEN-END:variables
public class ODGView extends org.jhotdraw.app.AbstractView {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String GRID_VISIBLE_PROPERTY = "gridVisible";

    protected org.jhotdraw.gui.JFileURIChooser exportChooser;

    /**
     * Each ODGView uses its own undo redo manager. This allows for undoing and redoing actions per
     * view.
     */
    private org.jhotdraw.undo.UndoRedoManager undo;

    /**
     * Depending on the type of an application, there may be one editor per view, or a single shared
     * editor for all views.
     */
    private org.jhotdraw.draw.DrawingEditor editor;

    private org.jhotdraw.draw.constrainer.GridConstrainer visibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer(10, 10);

    private org.jhotdraw.draw.constrainer.GridConstrainer invisibleConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer(1, 1);

    /**
     * Creates a new view.
     */
    public ODGView() {
        initComponents();
        scrollPane.setLayout(new org.jhotdraw.gui.PlacardScrollPaneLayout());
        scrollPane.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
        setEditor(new org.jhotdraw.editor.DefaultDrawingEditor());
        undo = new org.jhotdraw.undo.UndoRedoManager();
        view.setDrawing(createDrawing());
        view.getDrawing().addUndoableEditListener(undo);
        initActions();
        undo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                setHasUnsavedChanges(undo.hasSignificantEdits());
            }
        });
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        javax.swing.JPanel placardPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.AbstractButton pButton;
        pButton = org.jhotdraw.gui.action.ButtonFactory.createZoomButton(view);
        pButton.putClientProperty("Quaqua.Button.style", "placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin", new java.awt.Insets(0, 0, 0, 0));
        pButton.setFont(javax.swing.UIManager.getFont("SmallSystemFont"));
        placardPanel.add(pButton, java.awt.BorderLayout.WEST);
        pButton = org.jhotdraw.gui.action.ButtonFactory.createToggleGridButton(view);
        pButton.putClientProperty("Quaqua.Button.style", "placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin", new java.awt.Insets(0, 0, 0, 0));
        pButton.setFont(javax.swing.UIManager.getFont("SmallSystemFont"));
        labels.configureToolBarButton(pButton, "view.toggleGrid.placard");
        placardPanel.add(pButton, java.awt.BorderLayout.EAST);
        scrollPane.add(placardPanel, javax.swing.JScrollPane.LOWER_LEFT_CORNER);
        propertiesPanel.setVisible(preferences.getBoolean("propertiesPanelVisible", false));
        propertiesPanel.setView(view);
    }

    /**
     * Creates a new Drawing for this view.
     */
    protected org.jhotdraw.draw.Drawing createDrawing() {
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.samples.odg.ODGDrawing();
        java.util.LinkedList<org.jhotdraw.draw.io.InputFormat> inputFormats = new java.util.LinkedList<org.jhotdraw.draw.io.InputFormat>();
        inputFormats.add(new org.jhotdraw.samples.odg.io.ODGInputFormat());
        inputFormats.add(new org.jhotdraw.io.ImageInputFormat(new org.jhotdraw.samples.svg.figures.SVGImageFigure()));
        inputFormats.add(new org.jhotdraw.io.TextInputFormat(new org.jhotdraw.samples.svg.figures.SVGTextFigure()));
        drawing.setInputFormats(inputFormats);
        java.util.LinkedList<org.jhotdraw.draw.io.OutputFormat> outputFormats = new java.util.LinkedList<org.jhotdraw.draw.io.OutputFormat>();
        outputFormats.add(new org.jhotdraw.samples.svg.io.SVGOutputFormat());
        outputFormats.add(new org.jhotdraw.samples.svg.io.SVGZOutputFormat());
        outputFormats.add(new org.jhotdraw.io.ImageOutputFormat());
        outputFormats.add(new org.jhotdraw.io.ImageOutputFormat("JPG", "Joint Photographics Experts Group (JPEG)", "jpg", java.awt.image.BufferedImage.TYPE_INT_RGB));
        outputFormats.add(new org.jhotdraw.io.ImageOutputFormat("BMP", "Windows Bitmap (BMP)", "bmp", java.awt.image.BufferedImage.TYPE_BYTE_INDEXED));
        outputFormats.add(new org.jhotdraw.samples.svg.io.ImageMapOutputFormat());
        drawing.setOutputFormats(outputFormats);
        return drawing;
    }

    /**
     * Creates a Pageable object for printing the view.
     */
    public java.awt.print.Pageable createPageable() {
        return new org.jhotdraw.draw.print.DrawingPageable(view.getDrawing());
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    public void setEditor(org.jhotdraw.draw.DrawingEditor newValue) {
        org.jhotdraw.draw.DrawingEditor oldValue = editor;
        if (oldValue != null) {
            oldValue.remove(view);
        }
        editor = newValue;
        propertiesPanel.setEditor(editor);
        if (newValue != null) {
            newValue.add(view);
        }
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
    public void write(java.net.URI f, org.jhotdraw.api.gui.URIChooser fc) throws java.io.IOException {
        new org.jhotdraw.samples.svg.io.SVGOutputFormat().write(new java.io.File(f), view.getDrawing());
    }

    /**
     * Reads the view from the specified uri.
     */
    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void read(java.net.URI f, org.jhotdraw.api.gui.URIChooser fc) throws java.io.IOException {
        try {
            final org.jhotdraw.draw.Drawing drawing = createDrawing();
            java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat> fileFilterInputFormatMap = ((java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat>) (((org.jhotdraw.gui.JFileURIChooser) (fc)).getClientProperty("ffInputFormatMap")));
            org.jhotdraw.draw.io.InputFormat sf = fileFilterInputFormatMap.get(((org.jhotdraw.gui.JFileURIChooser) (fc)).getFileFilter());
            if (sf == null) {
                sf = drawing.getInputFormats().get(0);
            }
            sf.read(f, drawing, true);
            java.lang.System.out.println((("ODCView read(" + f) + ") drawing.childCount=") + drawing.getChildCount());
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    view.getDrawing().removeUndoableEditListener(undo);
                    view.setDrawing(drawing);
                    view.getDrawing().addUndoableEditListener(undo);
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
        return view.getDrawing();
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        view.setEnabled(newValue);
        super.setEnabled(newValue);
    }

    public void setPropertiesPanelVisible(boolean newValue) {
        boolean oldValue = propertiesPanel.isVisible();
        propertiesPanel.setVisible(newValue);
        firePropertyChange("propertiesPanelVisible", oldValue, newValue);
        preferences.putBoolean("propertiesPanelVisible", newValue);
        validate();
    }

    public boolean isPropertiesPanelVisible() {
        return propertiesPanel.isVisible();
    }

    public boolean isGridVisible() {
        return view.isConstrainerVisible();
    }

    public void setGridVisible(boolean newValue) {
        boolean oldValue = isGridVisible();
        view.setConstrainerVisible(newValue);
        firePropertyChange(org.jhotdraw.samples.odg.ODGView.GRID_VISIBLE_PROPERTY, oldValue, newValue);
    }

    public double getScaleFactor() {
        return view.getScaleFactor();
    }

    public void setScaleFactor(double newValue) {
        double oldValue = getScaleFactor();
        view.setScaleFactor(newValue);
        firePropertyChange("scaleFactor", oldValue, newValue);
    }

    /**
     * Clears the view.
     */
    @java.lang.Override
    public void clear() {
        final org.jhotdraw.draw.Drawing newDrawing = createDrawing();
        try {
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    view.getDrawing().removeUndoableEditListener(undo);
                    view.setDrawing(newDrawing);
                    view.getDrawing().addUndoableEditListener(undo);
                    undo.discardAllEdits();
                }
            });
        } catch (java.lang.reflect.InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (java.lang.InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @java.lang.Override
    public boolean canSaveTo(java.net.URI uri) {
        return uri.getPath().endsWith(".odg");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        view = new org.jhotdraw.draw.DefaultDrawingView();
        propertiesPanel = new org.jhotdraw.samples.odg.ODGPropertiesPanel();
        setLayout(new java.awt.BorderLayout());
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(view);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(propertiesPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jhotdraw.samples.odg.ODGPropertiesPanel propertiesPanel;

    private javax.swing.JScrollPane scrollPane;

    private org.jhotdraw.draw.DefaultDrawingView view;
}