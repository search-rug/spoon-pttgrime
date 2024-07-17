/* @(#)ApplyAttributesAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * ApplyAttributesAction.
 */
public class ApplyAttributesAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private java.util.Set<org.jhotdraw.draw.AttributeKey<?>> excludedAttributes = new java.util.HashSet<>(java.util.Arrays.asList(new org.jhotdraw.draw.AttributeKey<?>[]{ org.jhotdraw.draw.AttributeKeys.TRANSFORM, org.jhotdraw.draw.AttributeKeys.TEXT }));

    public ApplyAttributesAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, "edit.applyAttributes");
        updateEnabledState();
    }

    /**
     * Set of attributes that is excluded when applying default attributes.
     */
    public void setExcludedAttributes(java.util.Set<org.jhotdraw.draw.AttributeKey<?>> a) {
        this.excludedAttributes = new java.util.HashSet<>(a);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        applyAttributes();
    }

    @java.lang.SuppressWarnings("unchecked")
    public void applyAttributes() {
        org.jhotdraw.draw.DrawingEditor editor = getEditor();
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.undo.CompositeEdit edit = new org.jhotdraw.undo.CompositeEdit(labels.getString("edit.applyAttributes.text"));
        org.jhotdraw.draw.DrawingView view = getView();
        view.getDrawing().fireUndoableEditHappened(edit);
        for (org.jhotdraw.draw.figure.Figure figure : view.getSelectedFigures()) {
            figure.willChange();
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : editor.getDefaultAttributes().entrySet()) {
                if (!excludedAttributes.contains(entry.getKey())) {
                    figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                }
            }
            figure.changed();
        }
        view.getDrawing().fireUndoableEditHappened(edit);
    }

    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        setEnabled(getView().getSelectionCount() == 1);
    }
}