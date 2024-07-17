/* @(#)AttributeToggler.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * AttributeToggler toggles an attribute of the selected figures between two different values. If
 * the name of a compatible JTextComponent action is specified, the toggler checks if the current
 * permant focus owner is a JTextComponent, and if it is, it will apply the text action to the
 * JTextComponent.
 */
public class AttributeToggler<T> extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingEditor editor;

    private org.jhotdraw.draw.AttributeKey<T> key;

    private T value1;

    private T value2;

    private javax.swing.Action compatibleTextAction;

    public AttributeToggler(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value1, T value2) {
        this(editor, key, value1, value2, null);
    }

    public AttributeToggler(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value1, T value2, javax.swing.Action compatibleTextAction) {
        this.editor = editor;
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
        this.compatibleTextAction = compatibleTextAction;
    }

    public org.jhotdraw.draw.DrawingView getView() {
        return editor.getActiveView();
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (compatibleTextAction != null) {
            java.awt.Component focusOwner = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
            if ((focusOwner != null) && (focusOwner instanceof javax.swing.text.JTextComponent)) {
                compatibleTextAction.actionPerformed(evt);
                return;
            }
        }
        // Determine the new value
        java.util.Iterator<org.jhotdraw.draw.figure.Figure> i = getView().getSelectedFigures().iterator();
        T toggleValue = value1;
        if (i.hasNext()) {
            org.jhotdraw.draw.figure.Figure f = i.next();
            java.lang.Object attr = f.attr().get(key);
            if (((value1 == null) && (attr == null)) || (((value1 != null) && (attr != null)) && attr.equals(value1))) {
                toggleValue = value2;
            }
        }
        final T newValue = toggleValue;
        // --
        final java.util.ArrayList<org.jhotdraw.draw.figure.Figure> selectedFigures = new java.util.ArrayList<>(getView().getSelectedFigures());
        final java.util.ArrayList<java.lang.Object> restoreData = new java.util.ArrayList<>(selectedFigures.size());
        for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
            restoreData.add(figure.attr().getAttributesRestoreData());
            figure.willChange();
            figure.attr().set(key, newValue);
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
                    figure.attr().set(key, newValue);
                    figure.changed();
                }
            }
        };
        getView().getDrawing().fireUndoableEditHappened(edit);
    }
}