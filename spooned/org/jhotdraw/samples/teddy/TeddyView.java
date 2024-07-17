/* @(#)TeddyView.java

Copyright (c) 2006 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy;
/**
 * Provides a view on a text document.
 *
 * <p>See {@link org.jhotdraw.api.app.View} interface on how this view interacts with an
 * application.
 */
// End of variables declaration//GEN-END:variables
public class TeddyView extends org.jhotdraw.app.AbstractView {
    private static final long serialVersionUID = 1L;

    private static java.util.prefs.Preferences prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(org.jhotdraw.samples.teddy.TeddyView.class);

    protected javax.swing.JTextPane editor;

    private static class EditorPanel extends javax.swing.JPanel implements javax.swing.Scrollable {
        private static final long serialVersionUID = 1L;

        private javax.swing.text.JTextComponent editor;

        private boolean isLineWrap;

        public void setEditor(javax.swing.text.JTextComponent newValue) {
            editor = newValue;
            removeAll();
            setLayout(new java.awt.BorderLayout());
            add(editor);
            setBackground(javax.swing.UIManager.getColor("TextField.background"));
            setOpaque(true);
        }

        public void setLineWrap(boolean newValue) {
            isLineWrap = newValue;
            editor.revalidate();
            editor.repaint();
        }

        public boolean getLineWrap() {
            return isLineWrap;
        }

        @java.lang.Override
        public java.awt.Dimension getPreferredScrollableViewportSize() {
            // System.out.println("EditorViewport: "+editor.getPreferredScrollableViewportSize());
            return editor.getPreferredScrollableViewportSize();
        }

        @java.lang.Override
        public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return editor.getScrollableUnitIncrement(visibleRect, orientation, direction);
        }

        @java.lang.Override
        public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return editor.getScrollableBlockIncrement(visibleRect, orientation, direction);
        }

        @java.lang.Override
        public boolean getScrollableTracksViewportWidth() {
            return isLineWrap;
        }

        @java.lang.Override
        public boolean getScrollableTracksViewportHeight() {
            return editor.getScrollableTracksViewportHeight();
        }
    }

    protected org.jhotdraw.samples.teddy.TeddyView.EditorPanel editorViewport;

    /**
     * The undo/redo manager.
     */
    protected org.jhotdraw.undo.UndoRedoManager undoManager;

    /**
     * The panel used for the find feature.
     */
    private org.jhotdraw.samples.teddy.FindDialog findDialog;

    /**
     * The Matcher used to perform find operation.
     */
    private org.jhotdraw.samples.teddy.regex.Matcher matcher;

    public TeddyView() {
        org.jhotdraw.samples.teddy.TeddyView.prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(org.jhotdraw.samples.teddy.TeddyView.class);
        initComponents();
        // Init preferences
        statusBar.setVisible(org.jhotdraw.samples.teddy.TeddyView.prefs.getBoolean("statusBarVisible", false));
        editor = createEditor();
        editorViewport = new org.jhotdraw.samples.teddy.TeddyView.EditorPanel();
        editorViewport.setEditor(editor);
        editorViewport.setLineWrap(org.jhotdraw.samples.teddy.TeddyView.prefs.getBoolean("lineWrap", true));
        scrollPane.setViewportView(editorViewport);
        editor.addCaretListener(new javax.swing.event.CaretListener() {
            @java.lang.Override
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TeddyView.this.caretUpdate(evt);
            }
        });
        scrollPane.getViewport().setBackground(editor.getBackground());
        scrollPane.getViewport().addMouseListener(new java.awt.event.MouseAdapter() {
            @java.lang.Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                editor.requestFocus();
            }
        });
        java.awt.Font font = getFont();
        javax.swing.text.MutableAttributeSet attrs = ((javax.swing.text.StyledEditorKit) (editor.getEditorKit())).getInputAttributes();
        javax.swing.text.StyleConstants.setFontFamily(attrs, font.getFamily());
        javax.swing.text.StyleConstants.setFontSize(attrs, font.getSize());
        javax.swing.text.StyleConstants.setItalic(attrs, (font.getStyle() & java.awt.Font.ITALIC) != 0);
        javax.swing.text.StyleConstants.setBold(attrs, (font.getStyle() & java.awt.Font.BOLD) != 0);
        org.jhotdraw.samples.teddy.text.NumberedEditorKit editorKit = new org.jhotdraw.samples.teddy.text.NumberedEditorKit();
        ((org.jhotdraw.samples.teddy.text.NumberedViewFactory) (editorKit.getViewFactory())).setLineNumbersVisible(org.jhotdraw.samples.teddy.TeddyView.prefs.getBoolean("lineNumbersVisible", false));
        editor.setEditorKit(editorKit);
        editor.setDocument(createDocument());
        setPreferredSize(new java.awt.Dimension(400, 400));
        undoManager = new org.jhotdraw.undo.UndoRedoManager();
        editor.getDocument().addUndoableEditListener(undoManager);
        undoManager.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                setHasUnsavedChanges(undoManager.hasSignificantEdits());
            }
        });
    }

    protected javax.swing.JTextPane createEditor() {
        return new javax.swing.JTextPane();
    }

    @java.lang.Override
    public void init() {
        initActions();
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        super.setEnabled(newValue);
        editor.setEnabled(newValue);
        scrollPane.setEnabled(newValue);
    }

    public void setStatusBarVisible(boolean newValue) {
        boolean oldValue = statusBar.isVisible();
        statusBar.setVisible(newValue);
        org.jhotdraw.samples.teddy.TeddyView.prefs.putBoolean("statusBarVisible", newValue);
        firePropertyChange("statusBarVisible", oldValue, newValue);
    }

    public boolean isStatusBarVisible() {
        return statusBar.isVisible();
    }

    public void setLineWrap(boolean newValue) {
        boolean oldValue = editorViewport.getLineWrap();
        editorViewport.setLineWrap(newValue);
        org.jhotdraw.samples.teddy.TeddyView.prefs.putBoolean("lineWrap", newValue);
        firePropertyChange("lineWrap", oldValue, newValue);
    }

    public boolean isLineWrap() {
        return editorViewport.getLineWrap();
    }

    private void initActions() {
        getActionMap().put(org.jhotdraw.action.edit.UndoAction.ID, undoManager.getUndoAction());
        getActionMap().put(org.jhotdraw.action.edit.RedoAction.ID, undoManager.getRedoAction());
    }

    @java.lang.Override
    public void read(java.net.URI f, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        java.lang.String characterSet;
        if (((chooser == null) || (!(chooser instanceof org.jhotdraw.gui.JFileURIChooser))) || (!(((org.jhotdraw.gui.JFileURIChooser) (chooser)).getAccessory() instanceof org.jhotdraw.samples.teddy.CharacterSetAccessory))) {
            characterSet = org.jhotdraw.samples.teddy.TeddyView.prefs.get("characterSet", "UTF-8");
        } else {
            characterSet = ((org.jhotdraw.samples.teddy.CharacterSetAccessory) (((org.jhotdraw.gui.JFileURIChooser) (chooser)).getAccessory())).getCharacterSet();
        }
        read(f, characterSet);
    }

    public void read(java.net.URI f, java.lang.String characterSet) throws java.io.IOException {
        final javax.swing.text.Document doc = readDocument(new java.io.File(f), characterSet);
        try {
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    editor.getDocument().removeUndoableEditListener(undoManager);
                    editor.setDocument(doc);
                    doc.addUndoableEditListener(undoManager);
                    undoManager.discardAllEdits();
                }
            });
        } catch (java.lang.InterruptedException e) {
            // ignore
        } catch (java.lang.reflect.InvocationTargetException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    @java.lang.Override
    public void write(java.net.URI f, org.jhotdraw.api.gui.URIChooser chooser) throws java.io.IOException {
        java.lang.String characterSet;
        java.lang.String lineSeparator;
        if (((chooser == null) || (!(chooser instanceof org.jhotdraw.gui.JFileURIChooser))) || (!(((org.jhotdraw.gui.JFileURIChooser) (chooser)).getAccessory() instanceof org.jhotdraw.samples.teddy.CharacterSetAccessory))) {
            characterSet = org.jhotdraw.samples.teddy.TeddyView.prefs.get("characterSet", "UTF-8");
            lineSeparator = org.jhotdraw.samples.teddy.TeddyView.prefs.get("lineSeparator", "\n");
        } else {
            characterSet = ((org.jhotdraw.samples.teddy.CharacterSetAccessory) (((org.jhotdraw.gui.JFileURIChooser) (chooser)).getAccessory())).getCharacterSet();
            lineSeparator = ((org.jhotdraw.samples.teddy.CharacterSetAccessory) (((org.jhotdraw.gui.JFileURIChooser) (chooser)).getAccessory())).getLineSeparator();
        }
        write(f, characterSet, lineSeparator);
    }

    public void write(java.net.URI f, java.lang.String characterSet, java.lang.String lineSeparator) throws java.io.IOException {
        writeDocument(editor.getDocument(), new java.io.File(f), characterSet, lineSeparator);
        try {
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    undoManager.setHasSignificantEdits(false);
                }
            });
        } catch (java.lang.InterruptedException e) {
            // ignore
        } catch (java.lang.reflect.InvocationTargetException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Reads a document from a file using the specified character set.
     */
    private javax.swing.text.Document readDocument(java.io.File f, java.lang.String characterSet) throws java.io.IOException {
        javax.swing.ProgressMonitorInputStream pin = new javax.swing.ProgressMonitorInputStream(this, "Reading " + f.getName(), new java.io.FileInputStream(f));
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(pin, characterSet));
        try {
            // PlainDocument doc = new PlainDocument();
            javax.swing.text.StyledDocument doc = createDocument();
            javax.swing.text.MutableAttributeSet attrs = ((javax.swing.text.StyledEditorKit) (editor.getEditorKit())).getInputAttributes();
            java.lang.String line;
            boolean isFirst = true;
            while ((line = in.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    doc.insertString(doc.getLength(), "\n", attrs);
                }
                doc.insertString(doc.getLength(), line, attrs);
            } 
            return doc;
        } catch (javax.swing.text.BadLocationException e) {
            throw new java.io.IOException(e.getMessage());
        } catch (java.lang.OutOfMemoryError e) {
            java.lang.System.err.println("out of memory!");
            throw new java.io.IOException("Out of memory.");
        } finally {
            in.close();
        }
    }

    @java.lang.Override
    public void clear() {
        final javax.swing.text.Document newDocument = createDocument();
        try {
            javax.swing.SwingUtilities.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    editor.getDocument().removeUndoableEditListener(undoManager);
                    editor.setDocument(newDocument);
                    newDocument.addUndoableEditListener(undoManager);
                    undoManager.discardAllEdits();
                }
            });
        } catch (java.lang.reflect.InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (java.lang.InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    protected javax.swing.text.StyledDocument createDocument() {
        javax.swing.text.DefaultStyledDocument doc = new javax.swing.text.DefaultStyledDocument();
        doc.setParagraphAttributes(0, 1, ((javax.swing.text.StyledEditorKit) (editor.getEditorKit())).getInputAttributes(), true);
        return doc;
    }

    /**
     * Writes a document into a file using the specified character set.
     */
    private void writeDocument(javax.swing.text.Document doc, java.io.File f, java.lang.String characterSet, java.lang.String lineSeparator) throws java.io.IOException {
        org.jhotdraw.samples.teddy.io.LFWriter out = new org.jhotdraw.samples.teddy.io.LFWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f), characterSet));
        out.setLineSeparator(lineSeparator);
        try {
            java.lang.String sequence;
            for (int i = 0; i < doc.getLength(); i += 256) {
                out.write(doc.getText(i, java.lang.Math.min(256, doc.getLength() - i)));
            }
        } catch (javax.swing.text.BadLocationException e) {
            throw new java.io.IOException(e.getMessage());
        } finally {
            out.close();
            undoManager.discardAllEdits();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        statusBar = new javax.swing.JPanel();
        caretInfoLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        setLayout(new java.awt.BorderLayout());
        statusBar.setLayout(new java.awt.BorderLayout());
        caretInfoLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        caretInfoLabel.setText("1:1");
        caretInfoLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 0, 3));
        statusBar.add(caretInfoLabel, java.awt.BorderLayout.CENTER);
        add(statusBar, java.awt.BorderLayout.SOUTH);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    public javax.swing.text.Document getDocument() {
        return editor.getDocument();
    }

    /**
     * Accessor for text area. This is used by Actions that need ot act on the text area of the View.
     */
    public void select(int start, int end) {
        editor.select(start, end);
        try {
            editor.scrollRectToVisible(editor.modelToView(start));
        } catch (javax.swing.text.BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accessor for text area. This is used by Actions that need ot act on the text area of the View.
     */
    public int getSelectionStart() {
        return editor.getSelectionStart();
    }

    /**
     * Accessor for text area. This is used by Actions that need ot act on the project text area.
     */
    public int getSelectionEnd() {
        return editor.getSelectionEnd();
    }

    /**
     * Determines the number of lines contained in the area.
     *
     * @return the number of lines &gt; 0
     */
    public int getLineCount() {
        javax.swing.text.Element map = getDocument().getDefaultRootElement();
        return map.getElementCount();
    }

    /**
     * Accessor for text area. This is used by Actions that need to act on the text area of the View.
     */
    public void replaceRange(java.lang.String str, int start, int end) {
        // editor.replaceRange(str, start, end);
        if (end < start) {
            throw new java.lang.IllegalArgumentException("end before start");
        }
        javax.swing.text.Document doc = getDocument();
        if (doc != null) {
            try {
                if (doc instanceof javax.swing.text.AbstractDocument) {
                    ((javax.swing.text.AbstractDocument) (doc)).replace(start, end - start, str, null);
                } else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (javax.swing.text.BadLocationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Accessor for text area. This is used by Actions that need ot act on the text area of the View.
     */
    public int getLineOfOffset(int offset) throws javax.swing.text.BadLocationException {
        // return editor.getLineOfOffset(offset);
        javax.swing.text.Document doc = getDocument();
        if (offset < 0) {
            throw new javax.swing.text.BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new javax.swing.text.BadLocationException("Can't translate offset to line", doc.getLength() + 1);
        } else {
            javax.swing.text.Element map = getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }

    /**
     * Accessor for text area. This is used by Actions that need ot act on the text area of the View.
     */
    public int getLineStartOffset(int line) throws javax.swing.text.BadLocationException {
        // return editor.getLineStartOffset(line);
        int lineCount = getLineCount();
        if (line < 0) {
            throw new javax.swing.text.BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new javax.swing.text.BadLocationException("No such line", getDocument().getLength() + 1);
        } else {
            javax.swing.text.Element map = getDocument().getDefaultRootElement();
            javax.swing.text.Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    public void fireEdit(javax.swing.undo.UndoableEdit edit) {
        undoManager.addEdit(edit);
    }

    private void caretUpdate(javax.swing.event.CaretEvent evt) {
        try {
            int pos = editor.getCaretPosition();
            int line = getLineOfOffset(pos);
            int lineStartOffset = getLineStartOffset(line);
            caretInfoLabel.setText(((line + 1) + ":") + ((pos - lineStartOffset) + 1));
        } catch (javax.swing.text.BadLocationException e) {
            caretInfoLabel.setText(e.toString());
        }
    }

    public void setLineNumbersVisible(boolean newValue) {
        org.jhotdraw.samples.teddy.text.NumberedViewFactory viewFactory = ((org.jhotdraw.samples.teddy.text.NumberedViewFactory) (editor.getEditorKit().getViewFactory()));
        boolean oldValue = viewFactory.isLineNumbersVisible();
        if (oldValue != newValue) {
            viewFactory.setLineNumbersVisible(newValue);
            org.jhotdraw.samples.teddy.TeddyView.prefs.putBoolean("lineNumbersVisible", newValue);
            firePropertyChange("lineNumbersVisible", oldValue, newValue);
            editor.revalidate();
            editor.repaint();
        }
    }

    public boolean isLineNumbersVisible() {
        org.jhotdraw.samples.teddy.text.NumberedViewFactory viewFactory = ((org.jhotdraw.samples.teddy.text.NumberedViewFactory) (editor.getEditorKit().getViewFactory()));
        return viewFactory.isLineNumbersVisible();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel caretInfoLabel;

    public javax.swing.JScrollPane scrollPane;

    public javax.swing.JPanel statusBar;
}