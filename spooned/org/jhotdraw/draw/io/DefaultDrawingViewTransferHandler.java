/* @(#)DefaultDrawingViewTransferHandler.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.io;
/**
 * Delegates Clipboard Actions to the sending JComponent or reroutes to a possible DrawingView that
 * is provided from this component.
 */
public class DefaultDrawingViewTransferHandler extends javax.swing.TransferHandler {
    private static final long serialVersionUID = 1L;

    /**
     * We keep the exported figures in this list, so that we don't need to rely on figure selection,
     * when method exportDone is called.
     */
    private java.util.HashSet<org.jhotdraw.draw.figure.Figure> exportedFigures;

    public DefaultDrawingViewTransferHandler() {
    }

    @java.lang.Override
    public boolean importData(javax.swing.JComponent comp, java.awt.datatransfer.Transferable t) {
        return importData(comp, t, new java.util.HashSet<>(), null);
    }

    @java.lang.Override
    public boolean importData(javax.swing.TransferHandler.TransferSupport support) {
        return importData(((javax.swing.JComponent) (support.getComponent())), support.getTransferable(), new java.util.HashSet<>(), support.getDropLocation() == null ? null : support.getDropLocation().getDropPoint());
    }

    public org.jhotdraw.draw.DrawingView extractDrawingView(javax.swing.JComponent component) {
        if (component instanceof org.jhotdraw.draw.io.DrawingViewProvider provider) {
            return provider.getDrawingView();
        } else if (component instanceof org.jhotdraw.draw.DrawingView drawingView) {
            return drawingView;
        }
        return null;
    }

    /**
     * Imports data and stores the transferred figures into the supplied transferFigures collection.
     */
    @java.lang.SuppressWarnings("unchecked")
    protected boolean importData(final javax.swing.JComponent comp, java.awt.datatransfer.Transferable t, final java.util.HashSet<org.jhotdraw.draw.figure.Figure> transferFigures, final java.awt.Point dropPoint) {
        boolean retValue;
        org.jhotdraw.draw.DrawingView view = extractDrawingView(comp);
        if (view != null) {
            final org.jhotdraw.draw.Drawing drawing = view.getDrawing();
            if ((drawing.getInputFormats() == null) || (drawing.getInputFormats().size() == 0)) {
                retValue = false;
            } else {
                retValue = false;
                try {
                    java.awt.datatransfer.DataFlavor[] transferFlavors = t.getTransferDataFlavors();
                    // Workaround for Mac OS X:
                    // The Apple JVM messes up the sequence of the data flavors.
                    if (java.lang.System.getProperty("os.name").toLowerCase().startsWith("mac")) {
                        // Search for a suitable input format
                        SearchLoop : for (org.jhotdraw.draw.io.InputFormat format : drawing.getInputFormats()) {
                            for (java.awt.datatransfer.DataFlavor flavor : transferFlavors) {
                                if (format.isDataFlavorSupported(flavor)) {
                                    java.util.List<org.jhotdraw.draw.figure.Figure> existingFigures = new java.util.ArrayList<>(drawing.getChildren());
                                    try {
                                        format.read(t, drawing, false);
                                        final java.util.List<org.jhotdraw.draw.figure.Figure> importedFigures = new java.util.ArrayList<>(drawing.getChildren());
                                        importedFigures.removeAll(existingFigures);
                                        view.clearSelection();
                                        view.addToSelection(importedFigures);
                                        transferFigures.addAll(importedFigures);
                                        moveToDropPoint(comp, transferFigures, dropPoint);
                                        drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                                            private static final long serialVersionUID = 1L;

                                            @java.lang.Override
                                            public java.lang.String getPresentationName() {
                                                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                                                return labels.getString("edit.paste.text");
                                            }

                                            @java.lang.Override
                                            public void undo() throws javax.swing.undo.CannotUndoException {
                                                super.undo();
                                                drawing.removeAll(importedFigures);
                                            }

                                            @java.lang.Override
                                            public void redo() throws javax.swing.undo.CannotRedoException {
                                                super.redo();
                                                drawing.addAll(importedFigures);
                                            }
                                        });
                                        retValue = true;
                                        break SearchLoop;
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                        // failed to read transferalbe, try with next InputFormat
                                    }
                                }
                            }
                        }
                    } else {
                        // Search for a suitable input format
                        SearchLoop : for (java.awt.datatransfer.DataFlavor flavor : transferFlavors) {
                            for (org.jhotdraw.draw.io.InputFormat format : drawing.getInputFormats()) {
                                if (format.isDataFlavorSupported(flavor)) {
                                    java.util.List<org.jhotdraw.draw.figure.Figure> existingFigures = new java.util.ArrayList<>(drawing.getChildren());
                                    try {
                                        format.read(t, drawing, false);
                                        final java.util.List<org.jhotdraw.draw.figure.Figure> importedFigures = new java.util.ArrayList<>(drawing.getChildren());
                                        importedFigures.removeAll(existingFigures);
                                        view.clearSelection();
                                        view.addToSelection(importedFigures);
                                        transferFigures.addAll(importedFigures);
                                        moveToDropPoint(comp, transferFigures, dropPoint);
                                        drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                                            private static final long serialVersionUID = 1L;

                                            @java.lang.Override
                                            public java.lang.String getPresentationName() {
                                                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                                                return labels.getString("edit.paste.text");
                                            }

                                            @java.lang.Override
                                            public void undo() throws javax.swing.undo.CannotUndoException {
                                                super.undo();
                                                drawing.removeAll(importedFigures);
                                            }

                                            @java.lang.Override
                                            public void redo() throws javax.swing.undo.CannotRedoException {
                                                super.redo();
                                                drawing.addAll(importedFigures);
                                            }
                                        });
                                        retValue = true;
                                        break SearchLoop;
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                        // failed to read transferalbe, try with next InputFormat
                                    }
                                }
                            }
                        }
                    }
                    // No input format found? Lets see if we got files - we
                    // can handle these
                    if ((retValue == false) && t.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor)) {
                        final java.util.List<java.io.File> files = ((java.util.List<java.io.File>) (t.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor)));
                        retValue = true;
                        final java.util.List<org.jhotdraw.draw.figure.Figure> existingFigures = new java.util.ArrayList<>(drawing.getChildren());
                        view.getEditor().setEnabled(false);
                        new javax.swing.SwingWorker<java.util.List<org.jhotdraw.draw.figure.Figure>, org.jhotdraw.draw.figure.Figure>() {
                            @java.lang.Override
                            protected java.util.List<org.jhotdraw.draw.figure.Figure> doInBackground() throws java.lang.Exception {
                                for (java.io.File file : files) {
                                    FileFormatLoop : for (org.jhotdraw.draw.io.InputFormat format : drawing.getInputFormats()) {
                                        if (file.isFile() && format.getFileFilter().accept(file)) {
                                            format.read(file.toURI(), drawing, false);
                                        }
                                    }
                                }
                                return new java.util.ArrayList<>(drawing.getChildren());
                            }

                            @java.lang.Override
                            protected void done() {
                                try {
                                    java.util.List<org.jhotdraw.draw.figure.Figure> importedFigures = get();
                                    importedFigures.removeAll(existingFigures);
                                    if (importedFigures.size() > 0) {
                                        view.clearSelection();
                                        view.addToSelection(importedFigures);
                                        transferFigures.addAll(importedFigures);
                                        moveToDropPoint(comp, transferFigures, dropPoint);
                                        drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                                            private static final long serialVersionUID = 1L;

                                            @java.lang.Override
                                            public java.lang.String getPresentationName() {
                                                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                                                return labels.getString("edit.paste.text");
                                            }

                                            @java.lang.Override
                                            public void undo() throws javax.swing.undo.CannotUndoException {
                                                super.undo();
                                                drawing.removeAll(importedFigures);
                                            }

                                            @java.lang.Override
                                            public void redo() throws javax.swing.undo.CannotRedoException {
                                                super.redo();
                                                drawing.addAll(importedFigures);
                                            }
                                        });
                                    }
                                    view.getEditor().setEnabled(true);
                                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                                    java.util.logging.Logger.getLogger(org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                                }
                            }
                        }.execute();
                    }
                } catch (java.lang.Throwable e) {
                    e.printStackTrace();
                }
            }
        } else {
            retValue = super.importData(comp, t);
        }
        return retValue;
    }

    protected void moveToDropPoint(javax.swing.JComponent component, java.util.HashSet<org.jhotdraw.draw.figure.Figure> transferFigures, java.awt.Point dropPoint) {
        if (dropPoint == null) {
            // This ugly code sequence is needed to ensure that the drawing view
            // repaints the area which contains the dropped figures.
            for (org.jhotdraw.draw.figure.Figure fig : transferFigures) {
                fig.willChange();
                fig.changed();
            }
        } else {
            final org.jhotdraw.draw.DrawingView view = extractDrawingView(component);
            java.awt.geom.Point2D.Double drawingDropPoint = view.viewToDrawing(dropPoint);
            // Set<Figure> transferFigures = view.getSelectedFigures();
            java.awt.geom.Rectangle2D.Double drawingArea = null;
            for (org.jhotdraw.draw.figure.Figure fig : transferFigures) {
                if (drawingArea == null) {
                    drawingArea = fig.getDrawingArea();
                } else {
                    drawingArea.add(fig.getDrawingArea());
                }
            }
            if (drawingArea != null) {
                java.awt.geom.AffineTransform t = new java.awt.geom.AffineTransform();
                t.translate(-drawingArea.x, -drawingArea.y);
                t.translate(drawingDropPoint.x, drawingDropPoint.y);
                // XXX - instead of centering, we have to translate by the drag image offset here
                t.translate(drawingArea.width / (-2.0), drawingArea.height / (-2.0));
                for (org.jhotdraw.draw.figure.Figure fig : transferFigures) {
                    fig.willChange();
                    fig.transform(t);
                    fig.changed();
                }
            }
        }
    }

    @java.lang.Override
    public int getSourceActions(javax.swing.JComponent c) {
        int retValue;
        org.jhotdraw.draw.DrawingView view = extractDrawingView(c);
        if (view != null) {
            retValue = ((view.getDrawing().getOutputFormats().size() > 0) && (view.getSelectionCount() > 0)) ? javax.swing.TransferHandler.COPY | javax.swing.TransferHandler.MOVE : javax.swing.TransferHandler.NONE;
        } else {
            retValue = super.getSourceActions(c);
        }
        return retValue;
    }

    @java.lang.Override
    protected java.awt.datatransfer.Transferable createTransferable(javax.swing.JComponent c) {
        java.awt.datatransfer.Transferable retValue;
        org.jhotdraw.draw.DrawingView view = extractDrawingView(c);
        if (view != null) {
            retValue = createTransferable(view, view.getSelectedFigures());
        } else {
            retValue = super.createTransferable(c);
        }
        return retValue;
    }

    protected java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.DrawingView view, java.util.Set<org.jhotdraw.draw.figure.Figure> transferFigures) {
        java.awt.datatransfer.Transferable retValue;
        org.jhotdraw.draw.Drawing drawing = view.getDrawing();
        exportedFigures = null;
        if ((drawing.getOutputFormats() == null) || (drawing.getOutputFormats().size() == 0)) {
            retValue = null;
        } else {
            java.util.List<org.jhotdraw.draw.figure.Figure> toBeCopied = drawing.sort(transferFigures);
            if (toBeCopied.size() > 0) {
                try {
                    org.jhotdraw.datatransfer.CompositeTransferable transfer = new org.jhotdraw.datatransfer.CompositeTransferable();
                    for (org.jhotdraw.draw.io.OutputFormat format : drawing.getOutputFormats()) {
                        java.awt.datatransfer.Transferable t = format.createTransferable(drawing, toBeCopied, view.getScaleFactor());
                        if (!transfer.isDataFlavorSupported(t.getTransferDataFlavors()[0])) {
                            transfer.add(t);
                        }
                    }
                    exportedFigures = new java.util.HashSet<>(transferFigures);
                    retValue = transfer;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    retValue = null;
                }
            } else {
                retValue = null;
            }
        }
        return retValue;
    }

    @java.lang.Override
    protected void exportDone(javax.swing.JComponent source, java.awt.datatransfer.Transferable data, int action) {
        final org.jhotdraw.draw.DrawingView view = extractDrawingView(source);
        if (view != null) {
            final org.jhotdraw.draw.Drawing drawing = view.getDrawing();
            if (action == javax.swing.TransferHandler.MOVE) {
                final java.util.List<org.jhotdraw.draw.event.DrawingEvent> deletionEvents = new java.util.ArrayList<>();
                final java.util.List<org.jhotdraw.draw.figure.Figure> selectedFigures = (exportedFigures == null) ? java.util.Collections.emptyList() : new java.util.ArrayList<>(exportedFigures);
                // Abort, if not all of the selected figures may be removed from the
                // drawing
                for (org.jhotdraw.draw.figure.Figure f : selectedFigures) {
                    if (!f.isRemovable()) {
                        source.getToolkit().beep();
                        return;
                    }
                }
                // view.clearSelection();
                org.jhotdraw.draw.event.DrawingListener removeListener = new org.jhotdraw.draw.event.DrawingListenerAdapter() {
                    @java.lang.Override
                    public void figureAdded(org.jhotdraw.draw.event.DrawingEvent e) {
                    }

                    @java.lang.Override
                    public void figureRemoved(org.jhotdraw.draw.event.DrawingEvent evt) {
                        deletionEvents.add(0, evt);
                    }
                };
                drawing.addDrawingListener(removeListener);
                drawing.removeAll(selectedFigures);
                drawing.removeDrawingListener(removeListener);
                drawing.removeAll(selectedFigures);
                drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public java.lang.String getPresentationName() {
                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                        return labels.getString("edit.delete.text");
                    }

                    @java.lang.Override
                    public void undo() throws javax.swing.undo.CannotUndoException {
                        super.undo();
                        view.clearSelection();
                        for (org.jhotdraw.draw.event.DrawingEvent evt : deletionEvents) {
                            drawing.add(evt.getFigureIndex(), evt.getFigure());
                        }
                        view.addToSelection(selectedFigures);
                    }

                    @java.lang.Override
                    public void redo() throws javax.swing.undo.CannotRedoException {
                        super.redo();
                        for (org.jhotdraw.draw.event.DrawingEvent evt : new org.jhotdraw.util.ReversedList<>(deletionEvents)) {
                            drawing.remove(evt.getFigure());
                        }
                    }
                });
            }
        } else {
            super.exportDone(source, data, action);
        }
        exportedFigures = null;
    }

    @java.lang.Override
    public void exportAsDrag(javax.swing.JComponent comp, java.awt.event.InputEvent e, int action) {
        final org.jhotdraw.draw.DrawingView view = extractDrawingView(comp);
        if (view != null) {
            java.util.HashSet<org.jhotdraw.draw.figure.Figure> transferFigures = new java.util.HashSet<>();
            java.awt.event.MouseEvent me = ((java.awt.event.MouseEvent) (e));
            org.jhotdraw.draw.figure.Figure f = view.findFigure(me.getPoint());
            if (view.getSelectedFigures().contains(f)) {
                transferFigures.addAll(view.getSelectedFigures());
            } else {
                transferFigures.add(f);
            }
            java.awt.geom.Rectangle2D.Double drawingArea = null;
            for (org.jhotdraw.draw.figure.Figure fig : transferFigures) {
                if (drawingArea == null) {
                    drawingArea = fig.getDrawingArea();
                } else {
                    drawingArea.add(fig.getDrawingArea());
                }
            }
            java.awt.Rectangle viewArea = view.drawingToView(drawingArea);
            java.awt.Point imageOffset = me.getPoint();
            imageOffset.x = viewArea.x - imageOffset.x;
            imageOffset.y = viewArea.y - imageOffset.y;
            int srcActions = getSourceActions(comp);
            org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler.SwingDragGestureRecognizer recognizer = new org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler.SwingDragGestureRecognizer(new org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler.DragHandler(createTransferable(view, transferFigures), imageOffset));
            recognizer.gestured(comp, me, srcActions, action);
            // XXX - What kind of drag gesture can we support for this??
        } else {
            super.exportAsDrag(comp, e, action);
        }
    }

    @java.lang.Override
    public javax.swing.Icon getVisualRepresentation(java.awt.datatransfer.Transferable t) {
        java.awt.Image image = null;
        try {
            image = ((java.awt.Image) (t.getTransferData(java.awt.datatransfer.DataFlavor.imageFlavor)));
        } catch (java.io.IOException | java.awt.datatransfer.UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }
        return image == null ? null : new javax.swing.ImageIcon(image);
    }

    @java.lang.Override
    public boolean canImport(javax.swing.JComponent comp, java.awt.datatransfer.DataFlavor[] transferFlavors) {
        boolean retValue;
        final org.jhotdraw.draw.DrawingView view = extractDrawingView(comp);
        if (view != null) {
            org.jhotdraw.draw.Drawing drawing = view.getDrawing();
            // Search for a suitable input format
            retValue = false;
            SearchLoop : for (org.jhotdraw.draw.io.InputFormat format : drawing.getInputFormats()) {
                for (java.awt.datatransfer.DataFlavor flavor : transferFlavors) {
                    if (flavor.isFlavorJavaFileListType() || format.isDataFlavorSupported(flavor)) {
                        retValue = true;
                        break SearchLoop;
                    }
                }
            }
        } else {
            retValue = super.canImport(comp, transferFlavors);
        }
        return retValue;
    }

    /**
     * This is the default drag handler for drag and drop operations that use the <code>
     * TransferHandler</code>.
     */
    private static class DragHandler implements java.awt.dnd.DragGestureListener , java.awt.dnd.DragSourceListener {
        private boolean scrolls;

        private java.awt.datatransfer.Transferable transferable;

        private java.awt.Point imageOffset;

        public DragHandler(java.awt.datatransfer.Transferable t, java.awt.Point imageOffset) {
            transferable = t;
            this.imageOffset = imageOffset;
        }

        // --- DragGestureListener methods -----------------------------------
        /**
         * a Drag gesture has been recognized
         */
        @java.lang.Override
        public void dragGestureRecognized(java.awt.dnd.DragGestureEvent dge) {
            javax.swing.JComponent c = ((javax.swing.JComponent) (dge.getComponent()));
            org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler th = ((org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler) (c.getTransferHandler()));
            java.awt.datatransfer.Transferable t = transferable;
            if (t != null) {
                scrolls = c.getAutoscrolls();
                c.setAutoscrolls(false);
                try {
                    // dge.startDrag(null, t, this);
                    javax.swing.Icon icon = th.getVisualRepresentation(t);
                    java.awt.Image dragImage;
                    if (icon instanceof javax.swing.ImageIcon) {
                        dragImage = ((javax.swing.ImageIcon) (icon)).getImage();
                    } else {
                        dragImage = new java.awt.image.BufferedImage(icon.getIconWidth(), icon.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                        java.awt.Graphics g = ((java.awt.image.BufferedImage) (dragImage)).createGraphics();
                        icon.paintIcon(c, g, 0, 0);
                        g.dispose();
                    }
                    dge.startDrag(null, dragImage, imageOffset, t, this);
                    return;
                } catch (java.lang.RuntimeException re) {
                    c.setAutoscrolls(scrolls);
                }
            }
            th.exportDone(c, t, javax.swing.TransferHandler.NONE);
        }

        // --- DragSourceListener methods -----------------------------------
        /**
         * as the hotspot enters a platform dependent drop site
         */
        @java.lang.Override
        public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
        }

        /**
         * as the hotspot moves over a platform dependent drop site
         */
        @java.lang.Override
        public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
        }

        /**
         * as the hotspot exits a platform dependent drop site
         */
        @java.lang.Override
        public void dragExit(java.awt.dnd.DragSourceEvent dsde) {
        }

        /**
         * as the operation completes
         */
        @java.lang.Override
        public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
            java.awt.dnd.DragSourceContext dsc = dsde.getDragSourceContext();
            javax.swing.JComponent c = ((javax.swing.JComponent) (dsc.getComponent()));
            org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler th = ((org.jhotdraw.draw.io.DefaultDrawingViewTransferHandler) (c.getTransferHandler()));
            if (dsde.getDropSuccess()) {
                th.exportDone(c, dsc.getTransferable(), dsde.getDropAction());
            } else {
                th.exportDone(c, dsc.getTransferable(), javax.swing.TransferHandler.NONE);
            }
            c.setAutoscrolls(scrolls);
        }

        @java.lang.Override
        public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde) {
        }
    }

    private static class SwingDragGestureRecognizer extends java.awt.dnd.DragGestureRecognizer {
        private static final long serialVersionUID = 1L;

        SwingDragGestureRecognizer(java.awt.dnd.DragGestureListener dgl) {
            super(java.awt.dnd.DragSource.getDefaultDragSource(), null, javax.swing.TransferHandler.NONE, dgl);
        }

        void gestured(javax.swing.JComponent c, java.awt.event.MouseEvent e, int srcActions, int action) {
            setComponent(c);
            setSourceActions(srcActions);
            appendEvent(e);
            fireDragGestureRecognized(action, e.getPoint());
        }

        /**
         * register this DragGestureRecognizer's Listeners with the Component
         */
        @java.lang.Override
        protected void registerListeners() {
        }

        /**
         * unregister this DragGestureRecognizer's Listeners with the Component
         *
         * <p>subclasses must override this method
         */
        @java.lang.Override
        protected void unregisterListeners() {
        }
    }
}