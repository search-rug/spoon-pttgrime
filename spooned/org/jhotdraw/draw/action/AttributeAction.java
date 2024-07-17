/* @(#)AttributeAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * {@code AttributeAction} applies attribute values on the selected figures of the current {@code DrawingView} of a {@code DrawingEditor}.
 */
public class AttributeAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    protected java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;

    public <T> AttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value) {
        this(editor, key, value, null, null);
    }

    public <T> AttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, javax.swing.Icon icon) {
        this(editor, key, value, null, icon);
    }

    public <T> AttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name) {
        this(editor, key, value, name, null);
    }

    public <T> AttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name, javax.swing.Icon icon) {
        this(editor, key, value, name, icon, null);
    }

    public <T> AttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name, javax.swing.Icon icon, javax.swing.Action compatibleTextAction) {
        super(editor);
        this.attributes = new java.util.HashMap<>();
        attributes.put(key, value);
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        putValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY, key.getPresentationName());
        updateEnabledState();
    }

    public AttributeAction(org.jhotdraw.draw.DrawingEditor editor, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name, javax.swing.Icon icon) {
        super(editor);
        this.attributes = (attributes == null) ? new java.util.HashMap<>() : attributes;
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        applyAttributesTo(attributes, getView().getSelectedFigures());
    }

    /**
     * Applies the specified attributes to the currently selected figures of the drawing.
     *
     * @param a
     * 		The attributes.
     * @param figures
     * 		The figures to which the attributes are applied.
     */
    @java.lang.SuppressWarnings("unchecked")
    public void applyAttributesTo(final java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a, java.util.Set<org.jhotdraw.draw.figure.Figure> figures) {
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : a.entrySet()) {
            getEditor().setDefaultAttribute(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
        final java.util.ArrayList<org.jhotdraw.draw.figure.Figure> selectedFigures = new java.util.ArrayList<>(figures);
        final java.util.ArrayList<java.lang.Object> restoreData = new java.util.ArrayList<>(selectedFigures.size());
        for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
            restoreData.add(figure.attr().getAttributesRestoreData());
            figure.willChange();
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : a.entrySet()) {
                figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
            figure.changed();
        }
        javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                java.lang.String name = ((java.lang.String) (getValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY)));
                if (name == null) {
                    name = ((java.lang.String) (getValue(javax.swing.AbstractAction.NAME)));
                }
                if (name == null) {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    name = labels.getString("attribute.text");
                }
                return name;
            }

            @java.lang.Override
            public void undo() {
                super.undo();
                java.util.Iterator<java.lang.Object> iRestore = restoreData.iterator();
                for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
                    figure.willChange();
                    figure.attr().restoreAttributesTo(iRestore.next());
                    figure.changed();
                }
            }

            @java.lang.Override
            public void redo() {
                super.redo();
                for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
                    // restoreData.add(figure.getAttributesRestoreData());
                    figure.willChange();
                    for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : a.entrySet()) {
                        figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                    }
                    figure.changed();
                }
            }
        };
        getDrawing().fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    protected void updateEnabledState() {
        if (getEditor() != null) {
            setEnabled(getEditor().isEnabled());
        }
    }
}