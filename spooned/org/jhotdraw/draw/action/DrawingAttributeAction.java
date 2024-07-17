/* @(#)AttributeAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * AttributeAction.
 */
public class DrawingAttributeAction extends org.jhotdraw.draw.action.AbstractDrawingViewAction {
    private static final long serialVersionUID = 1L;

    protected java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;

    public <T> DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value) {
        this(editor, key, value, null, null);
    }

    public <T> DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, javax.swing.Icon icon) {
        this(editor, key, value, null, icon);
    }

    public <T> DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name) {
        this(editor, key, value, name, null);
    }

    public <T> DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name, javax.swing.Icon icon) {
        this(editor, key, value, name, icon, null);
    }

    public <T> DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<T> key, T value, java.lang.String name, javax.swing.Icon icon, javax.swing.Action compatibleTextAction) {
        super(editor);
        this.attributes = new java.util.HashMap<>();
        attributes.put(key, value);
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        setEnabled(true);
    }

    public DrawingAttributeAction(org.jhotdraw.draw.DrawingEditor editor, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name, javax.swing.Icon icon) {
        super(editor);
        this.attributes = attributes;
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        updateEnabledState();
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        final java.util.ArrayList<java.lang.Object> restoreData = new java.util.ArrayList<>();
        final org.jhotdraw.draw.Drawing drawing = getView().getDrawing();
        restoreData.add(drawing.attr().getAttributesRestoreData());
        drawing.willChange();
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : attributes.entrySet()) {
            drawing.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
        drawing.changed();
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
                drawing.willChange();
                drawing.attr().restoreAttributesTo(iRestore.next());
                drawing.changed();
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public void redo() {
                super.redo();
                // restoreData.add(drawing.getAttributesRestoreData());
                drawing.willChange();
                for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : attributes.entrySet()) {
                    drawing.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                }
                drawing.changed();
            }
        };
        fireUndoableEditHappened(edit);
    }
}