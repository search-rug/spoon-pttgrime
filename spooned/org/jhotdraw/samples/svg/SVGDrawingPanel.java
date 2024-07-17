/* @(#)JSVGDrawingAppletPanel.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * JSVGDrawingAppletPanel.
 */
// End of variables declaration//GEN-END:variables
public class SVGDrawingPanel extends javax.swing.JPanel implements org.jhotdraw.api.app.Disposable {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.undo.UndoRedoManager undoManager;

    private org.jhotdraw.draw.DrawingEditor editor;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    private java.util.prefs.Preferences prefs;

    private java.awt.event.ContainerListener containerHandler;

    public org.jhotdraw.undo.UndoRedoManager getUndoRedoManager() {
        return undoManager;
    }

    public void setUndoRedoManager(org.jhotdraw.undo.UndoRedoManager undo) {
        if ((undoManager != null) && (getView().getDrawing() != null)) {
            getView().getDrawing().removeUndoableEditListener(undoManager);
        }
        undoManager = undo;
        if ((undoManager != null) && (getView().getDrawing() != null)) {
            getView().getDrawing().addUndoableEditListener(undoManager);
        }
    }

    private class ItemChangeHandler implements java.awt.event.ItemListener {
        private javax.swing.JToolBar toolbar;

        private java.lang.String prefkey;

        public ItemChangeHandler(javax.swing.JToolBar toolbar, java.lang.String prefkey) {
            this.toolbar = toolbar;
            this.prefkey = prefkey;
        }

        @java.lang.Override
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            boolean b = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
            toolbar.setVisible(b);
            prefs.putBoolean(prefkey, b);
            validate();
        }
    }

    /**
     * Creates new instance.
     */
    public SVGDrawingPanel() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        try {
            prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
        } catch (java.lang.SecurityException e) {
            // prefs is null, because we are not permitted to read preferences
        }
        initComponents();
        toolsPane.setLayout(new org.jhotdraw.gui.ToolBarLayout());
        toolsPane.setBackground(new java.awt.Color(0xf0f0f0));
        toolsPane.setOpaque(true);
        viewToolBar.setView(view);
        undoManager = new org.jhotdraw.undo.UndoRedoManager();
        org.jhotdraw.draw.Drawing drawing = createDrawing();
        view.setDrawing(drawing);
        drawing.addUndoableEditListener(undoManager);
        // Try to install the DnDDrawingViewTransferHandler
        // Since this class only works on J2SE 6, we have to use reflection.
        try {
            view.setTransferHandler(((javax.swing.TransferHandler) (java.lang.Class.forName("org.jhotdraw.draw.DnDDrawingViewTransferHandler").newInstance())));
        } catch (java.lang.Exception e) {
            // bail silently
        }
        // Sort the toolbars according to the user preferences
        java.util.ArrayList<javax.swing.JToolBar> sortme = new java.util.ArrayList<javax.swing.JToolBar>();
        for (java.awt.Component c : toolsPane.getComponents()) {
            if (c instanceof javax.swing.JToolBar) {
                sortme.add(((javax.swing.JToolBar) (c)));
            }
        }
        java.util.Collections.sort(sortme, new java.util.Comparator<javax.swing.JToolBar>() {
            @java.lang.Override
            public int compare(javax.swing.JToolBar tb1, javax.swing.JToolBar tb2) {
                int i1 = prefs.getInt("toolBarIndex." + tb1.getName(), 0);
                int i2 = prefs.getInt("toolBarIndex." + tb2.getName(), 0);
                return i1 - i2;
            }
        });
        toolsPane.removeAll();
        for (javax.swing.JToolBar tb : sortme) {
            toolsPane.add(tb);
        }
        toolsPane.addContainerListener(containerHandler = new java.awt.event.ContainerListener() {
            @java.lang.Override
            public void componentAdded(java.awt.event.ContainerEvent e) {
                int i = 0;
                for (java.awt.Component c : toolsPane.getComponents()) {
                    if (c instanceof javax.swing.JToolBar) {
                        javax.swing.JToolBar tb = ((javax.swing.JToolBar) (c));
                        prefs.putInt("toolBarIndex." + tb.getName(), i);
                        i++;
                    }
                }
            }

            @java.lang.Override
            public void componentRemoved(java.awt.event.ContainerEvent e) {
            }
        });
        setEditor(new org.jhotdraw.editor.DefaultDrawingEditor());
    }

    @java.lang.Override
    public void dispose() {
        toolsPane.removeContainerListener(containerHandler);
        containerHandler = null;
        setEditor(null);
        for (java.beans.PropertyChangeListener pcl : view.getListeners(java.beans.PropertyChangeListener.class)) {
            view.removePropertyChangeListener(pcl);
        }
        view.setDrawing(null);
        actionToolBar.dispose();
        alignToolBar.dispose();
        arrangeToolBar.dispose();
        canvasToolBar.dispose();
        creationToolBar.dispose();
        figureToolBar.dispose();
        fillToolBar.dispose();
        fontToolBar.dispose();
        linkToolBar.dispose();
        strokeToolBar.dispose();
        viewToolBar.dispose();
        removeAll();
    }

    /**
     * Creates a new Drawing object which can be used with this {@code SVGDrawingPanel}.
     */
    public org.jhotdraw.draw.Drawing createDrawing() {
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.QuadTreeDrawing();
        java.util.LinkedList<org.jhotdraw.draw.io.InputFormat> inputFormats = new java.util.LinkedList<org.jhotdraw.draw.io.InputFormat>();
        inputFormats.add(new org.jhotdraw.samples.svg.io.SVGZInputFormat());
        inputFormats.add(new org.jhotdraw.io.ImageInputFormat(new org.jhotdraw.samples.svg.figures.SVGImageFigure(), "PNG", "Portable Network Graphics (PNG)", "png", "image/png"));
        inputFormats.add(new org.jhotdraw.io.ImageInputFormat(new org.jhotdraw.samples.svg.figures.SVGImageFigure(), "JPG", "Joint Photographics Experts Group (JPEG)", "jpg", "image/jpg"));
        inputFormats.add(new org.jhotdraw.io.ImageInputFormat(new org.jhotdraw.samples.svg.figures.SVGImageFigure(), "GIF", "Graphics Interchange Format (GIF)", "gif", "image/gif"));
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

    public void setDrawing(org.jhotdraw.draw.Drawing d) {
        undoManager.discardAllEdits();
        if (view.getDrawing() != null) {
            view.getDrawing().removeUndoableEditListener(undoManager);
        }
        view.setDrawing(d);
        d.addUndoableEditListener(undoManager);
    }

    public org.jhotdraw.draw.Drawing getDrawing() {
        return view.getDrawing();
    }

    public org.jhotdraw.draw.DrawingView getView() {
        return view;
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
        creationToolBar.setEditor(editor);
        fillToolBar.setEditor(editor);
        strokeToolBar.setEditor(editor);
        actionToolBar.setUndoManager(undoManager);
        actionToolBar.setEditor(editor);
        alignToolBar.setEditor(editor);
        arrangeToolBar.setEditor(editor);
        fontToolBar.setEditor(editor);
        figureToolBar.setEditor(editor);
        linkToolBar.setEditor(editor);
        org.jhotdraw.draw.DrawingView temp = (editor == null) ? null : editor.getActiveView();
        if (editor != null) {
            editor.setActiveView(view);
        }
        canvasToolBar.setEditor(editor);
        viewToolBar.setEditor(editor);
        if (editor != null) {
            editor.setActiveView(temp);
        }
    }

    /**
     * Reads a drawing from the specified file into the SVGDrawingPanel.
     *
     * <p>This method should be called from a worker thread. Calling it from the Event Dispatcher
     * Thread will block the user interface, until the drawing is read.
     */
    public void read(java.net.URI f) throws java.io.IOException {
        // Create a new drawing object
        org.jhotdraw.draw.Drawing newDrawing = createDrawing();
        if (newDrawing.getInputFormats().size() == 0) {
            throw new java.lang.InternalError("Drawing object has no input formats.");
        }
        // Try out all input formats until we succeed
        java.io.IOException firstIOException = null;
        for (org.jhotdraw.draw.io.InputFormat format : newDrawing.getInputFormats()) {
            try {
                format.read(f, newDrawing);
                final org.jhotdraw.draw.Drawing loadedDrawing = newDrawing;
                java.lang.Runnable r = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        // Set the drawing on the Event Dispatcher Thread
                        setDrawing(loadedDrawing);
                    }
                };
                if (javax.swing.SwingUtilities.isEventDispatchThread()) {
                    r.run();
                } else {
                    try {
                        javax.swing.SwingUtilities.invokeAndWait(r);
                    } catch (java.lang.InterruptedException ex) {
                        // suppress silently
                    } catch (java.lang.reflect.InvocationTargetException ex) {
                        java.lang.InternalError ie = new java.lang.InternalError("Error setting drawing.");
                        ie.initCause(ex);
                        throw ie;
                    }
                }
                // We get here if reading was successful.
                // We can return since we are done.
                return;
            } catch (java.io.IOException e) {
                // We get here if reading failed.
                // We only preserve the exception of the first input format,
                // because that's the one which is best suited for this drawing.
                if (firstIOException == null) {
                    firstIOException = e;
                }
            }
        }
        throw firstIOException;
    }

    /**
     * Reads a drawing from the specified file into the SVGDrawingPanel using the specified input
     * format.
     *
     * <p>This method should be called from a worker thread. Calling it from the Event Dispatcher
     * Thread will block the user interface, until the drawing is read.
     */
    public void read(java.net.URI f, org.jhotdraw.draw.io.InputFormat format) throws java.io.IOException {
        if (format == null) {
            read(f);
            return;
        }
        // Create a new drawing object
        org.jhotdraw.draw.Drawing newDrawing = createDrawing();
        if (newDrawing.getInputFormats().size() == 0) {
            throw new java.lang.InternalError("Drawing object has no input formats.");
        }
        format.read(f, newDrawing);
        final org.jhotdraw.draw.Drawing loadedDrawing = newDrawing;
        java.lang.Runnable r = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Set the drawing on the Event Dispatcher Thread
                setDrawing(loadedDrawing);
            }
        };
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(r);
            } catch (java.lang.InterruptedException ex) {
                // suppress silently
            } catch (java.lang.reflect.InvocationTargetException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Error setting drawing.");
                ie.initCause(ex);
                throw ie;
            }
        }
    }

    /**
     * Writes the drawing from the SVGDrawingPanel into a file.
     *
     * <p>This method should be called from a worker thread. Calling it from the Event Dispatcher
     * Thread will block the user interface, until the drawing is written.
     */
    public void write(java.net.URI uri) throws java.io.IOException {
        // Defensively clone the drawing object, so that we are not
        // affected by changes of the drawing while we write it into the file.
        final org.jhotdraw.draw.Drawing[] helper = new org.jhotdraw.draw.Drawing[1];
        java.lang.Runnable r = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                helper[0] = ((org.jhotdraw.draw.Drawing) (getDrawing().clone()));
            }
        };
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(r);
            } catch (java.lang.InterruptedException ex) {
                // suppress silently
            } catch (java.lang.reflect.InvocationTargetException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Error getting drawing.");
                ie.initCause(ex);
                throw ie;
            }
        }
        org.jhotdraw.draw.Drawing saveDrawing = helper[0];
        if (saveDrawing.getOutputFormats().size() == 0) {
            throw new java.lang.InternalError("Drawing object has no output formats.");
        }
        // Try out all output formats until we find one which accepts the
        // filename entered by the user.
        java.io.File f = new java.io.File(uri);
        for (org.jhotdraw.draw.io.OutputFormat format : saveDrawing.getOutputFormats()) {
            if (format.getFileFilter().accept(f)) {
                format.write(uri, saveDrawing);
                // We get here if writing was successful.
                // We can return since we are done.
                return;
            }
        }
        throw new java.io.IOException("No output format for " + f.getName());
    }

    /**
     * Writes the drawing from the SVGDrawingPanel into a file using the specified output format.
     *
     * <p>This method should be called from a worker thread. Calling it from the Event Dispatcher
     * Thread will block the user interface, until the drawing is written.
     */
    public void write(java.net.URI f, org.jhotdraw.draw.io.OutputFormat format) throws java.io.IOException {
        if (format == null) {
            write(f);
            return;
        }
        // Defensively clone the drawing object, so that we are not
        // affected by changes of the drawing while we write it into the file.
        final org.jhotdraw.draw.Drawing[] helper = new org.jhotdraw.draw.Drawing[1];
        java.lang.Runnable r = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                helper[0] = ((org.jhotdraw.draw.Drawing) (getDrawing().clone()));
            }
        };
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(r);
            } catch (java.lang.InterruptedException ex) {
                // suppress silently
            } catch (java.lang.reflect.InvocationTargetException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Error getting drawing.");
                ie.initCause(ex);
                throw ie;
            }
        }
        // Write drawing to file
        org.jhotdraw.draw.Drawing saveDrawing = helper[0];
        format.write(f, saveDrawing);
    }

    /**
     * Sets the actions for the "Action" popup menu in the toolbar.
     *
     * <p>This list may contain null items which are used to denote a separator in the popup menu.
     *
     * <p>Set this to null to set the drop down menus to the default actions.
     */
    public void setPopupActions(java.util.List<javax.swing.Action> actions) {
        actionToolBar.setPopupActions(actions);
    }

    /**
     * Gets the actions of the "Action" popup menu in the toolbar. This list may contain null items
     * which are used to denote a separator in the popup menu.
     *
     * @return An unmodifiable list with actions.
     */
    public java.util.List<javax.swing.Action> getPopupActions() {
        return actionToolBar.getPopupActions();
    }

    public javax.swing.JComponent getComponent() {
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        toolButtonGroup = new javax.swing.ButtonGroup();
        scrollPane = new javax.swing.JScrollPane();
        view = new org.jhotdraw.draw.DefaultDrawingView();
        toolsPanel = new javax.swing.JPanel();
        toolsScrollPane = new javax.swing.JScrollPane();
        toolsPane = new javax.swing.JPanel();
        creationToolBar = new org.jhotdraw.samples.svg.gui.ToolsToolBar();
        actionToolBar = new org.jhotdraw.samples.svg.gui.ActionsToolBar();
        fillToolBar = new org.jhotdraw.samples.svg.gui.FillToolBar();
        strokeToolBar = new org.jhotdraw.samples.svg.gui.StrokeToolBar();
        fontToolBar = new org.jhotdraw.samples.svg.gui.FontToolBar();
        arrangeToolBar = new org.jhotdraw.samples.svg.gui.ArrangeToolBar();
        alignToolBar = new org.jhotdraw.samples.svg.gui.AlignToolBar();
        figureToolBar = new org.jhotdraw.samples.svg.gui.FigureToolBar();
        linkToolBar = new org.jhotdraw.samples.svg.gui.LinkToolBar();
        canvasToolBar = new org.jhotdraw.samples.svg.gui.CanvasToolBar();
        viewToolBar = new org.jhotdraw.samples.svg.gui.ViewToolBar();
        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());
        scrollPane.setBorder(null);
        scrollPane.setViewportView(view);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        toolsPanel.setBackground(new java.awt.Color(255, 255, 255));
        toolsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        toolsPanel.setOpaque(true);
        toolsPanel.setLayout(new java.awt.GridBagLayout());
        toolsScrollPane.setBorder(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getBorder("Ribbon.border"));
        toolsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        toolsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        toolsScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));
        toolsPane.setForeground(new java.awt.Color(153, 153, 153));
        toolsPane.add(creationToolBar);
        toolsPane.add(actionToolBar);
        toolsPane.add(fillToolBar);
        strokeToolBar.setMargin(new java.awt.Insets(0, 10, 0, 0));
        toolsPane.add(strokeToolBar);
        toolsPane.add(fontToolBar);
        toolsPane.add(arrangeToolBar);
        toolsPane.add(alignToolBar);
        toolsPane.add(figureToolBar);
        toolsPane.add(linkToolBar);
        toolsPane.add(canvasToolBar);
        toolsPane.add(viewToolBar);
        toolsScrollPane.setViewportView(toolsPane);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        toolsPanel.add(toolsScrollPane, gridBagConstraints);
        add(toolsPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jhotdraw.samples.svg.gui.ActionsToolBar actionToolBar;

    private org.jhotdraw.samples.svg.gui.AlignToolBar alignToolBar;

    private org.jhotdraw.samples.svg.gui.ArrangeToolBar arrangeToolBar;

    private org.jhotdraw.samples.svg.gui.CanvasToolBar canvasToolBar;

    private org.jhotdraw.samples.svg.gui.ToolsToolBar creationToolBar;

    private org.jhotdraw.samples.svg.gui.FigureToolBar figureToolBar;

    private org.jhotdraw.samples.svg.gui.FillToolBar fillToolBar;

    private org.jhotdraw.samples.svg.gui.FontToolBar fontToolBar;

    private org.jhotdraw.samples.svg.gui.LinkToolBar linkToolBar;

    private javax.swing.JScrollPane scrollPane;

    private org.jhotdraw.samples.svg.gui.StrokeToolBar strokeToolBar;

    private javax.swing.ButtonGroup toolButtonGroup;

    private javax.swing.JPanel toolsPane;

    private javax.swing.JPanel toolsPanel;

    private javax.swing.JScrollPane toolsScrollPane;

    private org.jhotdraw.draw.DefaultDrawingView view;

    private org.jhotdraw.samples.svg.gui.ViewToolBar viewToolBar;
}