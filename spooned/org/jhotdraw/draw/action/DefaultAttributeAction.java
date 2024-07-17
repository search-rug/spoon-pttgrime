/* @(#)DefaultAttributeAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * DefaultAttributeAction.
 *
 * <p>XXX - should listen to changes in the default attributes of its DrawingEditor.
 */
public class DefaultAttributeAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.AttributeKey<?>[] keys;

    private java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes;

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?> key) {
        this(editor, key, null, null);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?> key, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes) {
        this(editor, new org.jhotdraw.draw.AttributeKey<?>[]{ key }, null, null, fixedAttributes);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?>[] keys) {
        this(editor, keys, null, null);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?> key, javax.swing.Icon icon) {
        this(editor, key, null, icon);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?> key, java.lang.String name) {
        this(editor, key, name, null);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?> key, java.lang.String name, javax.swing.Icon icon) {
        this(editor, new org.jhotdraw.draw.AttributeKey<?>[]{ key }, name, icon);
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?>[] keys, java.lang.String name, javax.swing.Icon icon) {
        this(editor, keys, name, icon, new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>());
    }

    public DefaultAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<?>[] keys, java.lang.String name, javax.swing.Icon icon, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes) {
        super(editor);
        this.keys = keys.clone();
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        setEnabled(true);
        editor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DefaultAttributeAction.this.keys[0].getKey())) {
                    putValue("attribute_" + DefaultAttributeAction.this.keys[0].getKey(), evt.getNewValue());
                }
            }
        });
        this.fixedAttributes = fixedAttributes;
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if ((getView() != null) && (getView().getSelectionCount() > 0)) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            org.jhotdraw.undo.CompositeEdit edit = new org.jhotdraw.undo.CompositeEdit(labels.getString("drawAttributeChange"));
            fireUndoableEditHappened(edit);
            changeAttribute();
            fireUndoableEditHappened(edit);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    public void changeAttribute() {
        org.jhotdraw.undo.CompositeEdit edit = new org.jhotdraw.undo.CompositeEdit("attributes");
        fireUndoableEditHappened(edit);
        org.jhotdraw.draw.DrawingEditor editor = getEditor();
        for (org.jhotdraw.draw.figure.Figure figure : getView().getSelectedFigures()) {
            figure.willChange();
            for (org.jhotdraw.draw.AttributeKey<?> key : keys) {
                figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (key)), editor.getDefaultAttribute(key));
            }
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : fixedAttributes.entrySet()) {
                figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
            figure.changed();
        }
        fireUndoableEditHappened(edit);
    }

    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        // setEnabled(getView().getSelectionCount() > 0);
    }
}