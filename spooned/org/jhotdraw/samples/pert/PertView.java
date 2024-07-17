/* @(#)PertView.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert;
import org.jhotdraw.api.app.View;
/**
 * Provides a view on a Pert drawing.
 *
 * <p>See {@link View} interface on how this view interacts with an application.
 */
// End of variables declaration//GEN-END:variables
public class PertView extends org.jhotdraw.app.AbstractView {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String GRID_VISIBLE_PROPERTY = "gridVisible";

    /**
     * Each view uses its own undo redo manager. This allows for undoing and redoing actions per view.
     */
    private org.jhotdraw.undo.UndoRedoManager undo;

    /**
     * Depending on the type of an application, there may be one editor per view, or a single shared
     * editor for all views.
     */
    private org.jhotdraw.draw.DrawingEditor editor;

    /**
     * Creates a new view.
     */
    public PertView() {
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
        // setGridVisible(preferences.getBoolean("view.gridVisible", false));
        // setScaleFactor(preferences.getDouble("view.scaleFactor", 1d));
    }

    /**
     * Creates a new Drawing for this view.
     */
    protected org.jhotdraw.draw.Drawing createDrawing() {
        org.jhotdraw.draw.DefaultDrawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        java.util.LinkedList<org.jhotdraw.draw.io.InputFormat> inputFormats = new java.util.LinkedList<org.jhotdraw.draw.io.InputFormat>();
        inputFormats.add(new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.samples.pert.PertFactory()));
        drawing.setInputFormats(inputFormats);
        java.util.LinkedList<org.jhotdraw.draw.io.OutputFormat> outputFormats = new java.util.LinkedList<org.jhotdraw.draw.io.OutputFormat>();
        outputFormats.add(new org.jhotdraw.io.DOMStorableOutputFormat(new org.jhotdraw.samples.pert.PertFactory()));
        outputFormats.add(new org.jhotdraw.io.ImageOutputFormat());
        drawing.setOutputFormats(outputFormats);
        return drawing;
    }

    /**
     * Creates a Pageable object for printing this view.
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
        if (newValue != null) {
            newValue.add(view);
        }
    }

    public void setGridVisible(boolean newValue) {
        boolean oldValue = isGridVisible();
        view.setConstrainerVisible(newValue);
        firePropertyChange(org.jhotdraw.samples.pert.PertView.GRID_VISIBLE_PROPERTY, oldValue, newValue);
        preferences.putBoolean("view.gridVisible", newValue);
    }

    public boolean isGridVisible() {
        return view.isConstrainerVisible();
    }

    public double getScaleFactor() {
        return view.getScaleFactor();
    }

    public void setScaleFactor(double newValue) {
        double oldValue = getScaleFactor();
        view.setScaleFactor(newValue);
        firePropertyChange("scaleFactor", oldValue, newValue);
        preferences.putDouble("view.scaleFactor", newValue);
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
    public void write(java.net.URI f, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        org.jhotdraw.draw.Drawing drawing = view.getDrawing();
        org.jhotdraw.draw.io.OutputFormat outputFormat = drawing.getOutputFormats().get(0);
        outputFormat.write(f, drawing);
    }

    /**
     * Reads the view from the specified uri.
     */
    @java.lang.Override
    public void read(java.net.URI f, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        try {
            final org.jhotdraw.draw.Drawing drawing = createDrawing();
            org.jhotdraw.draw.io.InputFormat inputFormat = drawing.getInputFormats().get(0);
            inputFormat.read(f, drawing, true);
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
            e.initCause(e);
            throw error;
        }
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
        return uri.getPath().endsWith(".xml");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        view = new org.jhotdraw.draw.DefaultDrawingView();
        setLayout(new java.awt.BorderLayout());
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(view);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;

    private org.jhotdraw.draw.DefaultDrawingView view;
}