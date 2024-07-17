/* @(#)AbstractAttributeEditorHandler.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.DrawingView;
/**
 * AbstractAttributeEditorHandler mediates between an AttributeEditor and the currently selected
 * Figure's in a DrawingEditor.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * Selection changes of {@code DrawingView} are observed by user interface components:<br>
 * Subject: {@link org.jhotdraw.draw.DrawingView}; Observer: {@link FigureSelectionListener};
 * Concrete-Observer: {@link AbstractAttributeEditorHandler}, {@link SelectionComponentDisplayer},
 * {@link SelectionComponentRepainter}. <hr>
 */
public abstract class AbstractAttributeEditorHandler<T> implements org.jhotdraw.api.app.Disposable {
    protected org.jhotdraw.draw.DrawingEditor editor;

    protected org.jhotdraw.draw.DrawingView view;

    protected org.jhotdraw.draw.DrawingView activeView;

    protected org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor;

    protected org.jhotdraw.draw.AttributeKey<T> attributeKey;

    protected int updateDepth;

    protected java.util.List<java.lang.Object> attributeRestoreData = new java.util.ArrayList<>();

    protected java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes;

    /**
     * If this variable is put to true, the attribute editor updates the default values of the drawing
     * editor.
     */
    private boolean isUpdateDrawingEditorDefaults;

    /**
     * To this figures we have registered the EventHandler as FigureListener and as
     * PropertyChangeListener.
     */
    private java.util.Set<org.jhotdraw.draw.figure.Figure> figuresOfInterest;

    protected class EventHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements org.jhotdraw.draw.event.FigureSelectionListener , java.beans.PropertyChangeListener {
        @java.lang.Override
        public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
            attributeRestoreData = null;
            if (figuresOfInterest != null) {
                for (org.jhotdraw.draw.figure.Figure f : figuresOfInterest) {
                    f.removeFigureListener(this);
                }
            }
            figuresOfInterest = getEditedFigures();
            for (org.jhotdraw.draw.figure.Figure f : figuresOfInterest) {
                f.addFigureListener(this);
            }
            updateAttributeEditor();
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.Object src = evt.getSource();
            java.lang.String name = evt.getPropertyName();
            if ((src == editor) && (((name == null) && (org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY)))) {
                updateActiveView();
            } else if ((src == editor) && name.equals(org.jhotdraw.draw.DrawingEditor.DEFAULT_ATTRIBUTE_PROPERTY_PREFIX + attributeKey.getKey())) {
                updateAttributeEditor();
            } else if ((src == attributeEditor) && (((name == null) && (org.jhotdraw.api.gui.AttributeEditor.ATTRIBUTE_VALUE_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.AttributeEditor.ATTRIBUTE_VALUE_PROPERTY)))) {
                updateFigures();
            } else if ((src == activeView) && (((name == null) && (org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY)))) {
                updateActiveView();
            } else if ((figuresOfInterest != null) && figuresOfInterest.contains(src)) {
                updateFigures();
            }
        }

        @java.lang.Override
        public void attributeChanged(org.jhotdraw.draw.event.FigureEvent e) {
            if (e.getAttribute() == attributeKey) {
                updateAttributeEditor();
            }
        }
    }

    private org.jhotdraw.draw.event.AbstractAttributeEditorHandler<T>.EventHandler eventHandler;

    private static class UndoableAttributeEdit<T> extends javax.swing.undo.AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;

        private java.util.Set<org.jhotdraw.draw.figure.Figure> editedFigures;

        private org.jhotdraw.draw.AttributeKey<T> attributeKey;

        private T editRedoValue;

        protected java.util.List<java.lang.Object> editUndoData;

        public UndoableAttributeEdit(java.util.Set<org.jhotdraw.draw.figure.Figure> editedFigures, org.jhotdraw.draw.AttributeKey<T> attributeKey, T editRedoValue, java.util.List<java.lang.Object> editUndoData) {
            this.editedFigures = editedFigures;
            this.attributeKey = attributeKey;
            this.editRedoValue = editRedoValue;
            this.editUndoData = editUndoData;
        }

        @java.lang.Override
        public java.lang.String getPresentationName() {
            return attributeKey.getPresentationName();
        }

        @java.lang.Override
        public void undo() throws javax.swing.undo.CannotRedoException {
            super.undo();
            java.util.Iterator<java.lang.Object> di = editUndoData.iterator();
            for (org.jhotdraw.draw.figure.Figure f : editedFigures) {
                f.willChange();
                f.attr().restoreAttributesTo(di.next());
                f.changed();
            }
        }

        @java.lang.Override
        public void redo() throws javax.swing.undo.CannotRedoException {
            super.redo();
            for (org.jhotdraw.draw.figure.Figure f : editedFigures) {
                f.attr().set(attributeKey, editRedoValue);
            }
        }

        @java.lang.Override
        public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
            if (anEdit instanceof org.jhotdraw.draw.event.AbstractAttributeEditorHandler.UndoableAttributeEdit) {
                return ((org.jhotdraw.draw.event.AbstractAttributeEditorHandler.UndoableAttributeEdit) (anEdit)).editUndoData == this.editUndoData;
            }
            return false;
        }
    }

    public AbstractAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor) {
        this(key, attributeEditor, drawingEditor, true);
    }

    public AbstractAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor, boolean updateDrawingEditorDefaults) {
        this(key, null, attributeEditor, drawingEditor, updateDrawingEditorDefaults);
    }

    @java.lang.SuppressWarnings("unchecked")
    public AbstractAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor, boolean updateDrawingEditorDefaults) {
        eventHandler = new EventHandler();
        this.defaultAttributes = ((java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>) ((defaultAttributes == null) ? java.util.Collections.emptyMap() : defaultAttributes));
        attributeEditor.setAttributeValue(key.getDefaultValue());
        setAttributeKey(key);
        setAttributeEditor(attributeEditor);
        setEditor(drawingEditor);
        isUpdateDrawingEditorDefaults = updateDrawingEditorDefaults;
    }

    /**
     * Attaches the FigureAttributeEditorHandler to the specified DrawingEditor.
     *
     * <p>The FigureAttributeEditorHandler listens to view changes and selection changes of the
     * drawing editor and calls setEnabled(boolean) and updateField(Set&lt;Figure&gt;) on the field
     * accordingly.
     *
     * @param newValue
     * 		a drawing editor.
     */
    public void setEditor(org.jhotdraw.draw.DrawingEditor newValue) {
        org.jhotdraw.draw.DrawingEditor oldValue = editor;
        if (editor != null) {
            editor.removePropertyChangeListener(eventHandler);
        }
        this.editor = newValue;
        if (editor != null) {
            editor.addPropertyChangeListener(new org.jhotdraw.beans.WeakPropertyChangeListener(eventHandler));
        }
        updateActiveView();
    }

    /**
     * Returns the DrawingEditor to which this FigureAttributeEditorHandler is attached.
     */
    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    /**
     * Attaches the FigureAttributeEditorHandler to the specified DrawingView.
     *
     * <p>If a non-null value is provided, the FigureAttributeEditorHandler listens only to selection
     * changes of the specified view. If a null value is provided, the FigureAttributeEditorHandler
     * listens to all views of the drawing editor.
     *
     * @param newValue
     * 		a drawing view.
     */
    public void setView(org.jhotdraw.draw.DrawingView newValue) {
        this.view = newValue;
        updateActiveView();
    }

    /**
     * Returns the DrawingView to which this FigureAttributeEditorHandler is attached. Returns null,
     * if the FigureAttributeEditorHandler is attached to all views of the DrawingEditor.
     */
    public org.jhotdraw.draw.DrawingView getView() {
        return view;
    }

    /**
     * Set this to true if you want the attribute editor to update the default values of the drawing
     * editor.
     *
     * @param newValue
     */
    public void setUpdateDrawingEditorDefaults(boolean newValue) {
        isUpdateDrawingEditorDefaults = newValue;
    }

    /**
     * Returns true if the attribute editor updates the default values of the drawing editor.
     */
    public boolean isUpdateDrawingEditorDefaults() {
        return isUpdateDrawingEditorDefaults;
    }

    protected org.jhotdraw.draw.DrawingView getActiveView() {
        if (getView() != null) {
            return getView();
        } else {
            return editor.getActiveView();
        }
    }

    /**
     * Attaches the FigureAttributeEditorHandler to the specified AttributeEditor.
     */
    public void setAttributeEditor(org.jhotdraw.api.gui.AttributeEditor<T> newValue) {
        if (attributeEditor != null) {
            attributeEditor.removePropertyChangeListener(eventHandler);
        }
        this.attributeEditor = newValue;
        if (attributeEditor != null) {
            attributeEditor.addPropertyChangeListener(eventHandler);
        }
    }

    /**
     * Returns the AttributeEditor to which this FigureAttributeEditorHandler is attached.
     */
    public org.jhotdraw.api.gui.AttributeEditor<T> getAttributeEditor() {
        return attributeEditor;
    }

    public org.jhotdraw.draw.AttributeKey<T> getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(org.jhotdraw.draw.AttributeKey<T> newValue) {
        attributeKey = newValue;
    }

    protected void updateActiveView() {
        org.jhotdraw.draw.DrawingView newValue = (view != null) ? view : (editor != null) && (editor.getActiveView() != null) ? editor.getActiveView() : null;
        org.jhotdraw.draw.DrawingView oldValue = activeView;
        if (activeView != null) {
            activeView.removePropertyChangeListener(eventHandler);
            activeView.removeFigureSelectionListener(eventHandler);
            if (figuresOfInterest != null) {
                for (org.jhotdraw.draw.figure.Figure f : figuresOfInterest) {
                    f.removeFigureListener(eventHandler);
                }
            }
        }
        activeView = newValue;
        if (activeView != null) {
            activeView.addPropertyChangeListener(eventHandler);
            activeView.addFigureSelectionListener(eventHandler);
            figuresOfInterest = getEditedFigures();
            for (org.jhotdraw.draw.figure.Figure f : figuresOfInterest) {
                f.addFigureListener(eventHandler);
            }
        }
        attributeRestoreData = null;
        updateAttributeEditor();
    }

    protected abstract java.util.Set<org.jhotdraw.draw.figure.Figure> getEditedFigures();

    protected void updateAttributeEditor() {
        if ((updateDepth++) == 0) {
            java.util.Set<org.jhotdraw.draw.figure.Figure> figures = getEditedFigures();
            if (editor == null) {
                attributeEditor.getComponent().setEnabled(false);
            } else if ((activeView == null) || figures.isEmpty()) {
                attributeEditor.getComponent().setEnabled(true);
                T value = editor.getDefaultAttribute(attributeKey);
                attributeEditor.setAttributeValue(value);
                attributeEditor.setMultipleValues(false);
            } else {
                attributeEditor.getComponent().setEnabled(true);
                T value = figures.iterator().next().attr().get(attributeKey);
                boolean isMultiple = false;
                for (org.jhotdraw.draw.figure.Figure f : figures) {
                    T v = f.attr().get(attributeKey);
                    if ((((v == null) || (value == null)) && (v != value)) || (((v != null) && (value != null)) && (!v.equals(value)))) {
                        isMultiple = true;
                        break;
                    }
                }
                attributeEditor.setAttributeValue(value);
                attributeEditor.setMultipleValues(isMultiple);
            }
        }
        updateDepth--;
    }

    @java.lang.SuppressWarnings("unchecked")
    protected void updateFigures() {
        if ((updateDepth++) == 0) {
            java.util.Set<org.jhotdraw.draw.figure.Figure> figures = getEditedFigures();
            if ((activeView == null) || figures.isEmpty()) {
            } else {
                T value = attributeEditor.getAttributeValue();
                if (attributeRestoreData == null) {
                    attributeRestoreData = new java.util.ArrayList<>();
                    for (org.jhotdraw.draw.figure.Figure f : figures) {
                        attributeRestoreData.add(f.attr().getAttributesRestoreData());
                    }
                }
                for (org.jhotdraw.draw.figure.Figure f : figures) {
                    f.willChange();
                    f.attr().set(attributeKey, value);
                    for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : defaultAttributes.entrySet()) {
                        f.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                    }
                    f.changed();
                }
                if ((editor != null) && isUpdateDrawingEditorDefaults) {
                    editor.setDefaultAttribute(attributeKey, value);
                }
                getActiveView().getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.AbstractAttributeEditorHandler.UndoableAttributeEdit<>(new java.util.HashSet<>(figures), attributeKey, value, attributeRestoreData));
                if (!attributeEditor.getValueIsAdjusting()) {
                    attributeRestoreData = null;
                }
            }
        }
        updateDepth--;
    }

    @java.lang.Override
    public void dispose() {
        setEditor(null);
    }
}