/* @(#)PickAttributesAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * PickAttributesAction.
 */
public class PickAttributesAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private java.util.Set<org.jhotdraw.draw.AttributeKey<?>> excludedAttributes = new java.util.HashSet<>(java.util.Arrays.asList(new org.jhotdraw.draw.AttributeKey<?>[]{ org.jhotdraw.draw.AttributeKeys.TRANSFORM, org.jhotdraw.draw.AttributeKeys.TEXT }));

    public PickAttributesAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, "edit.pickAttributes");
        updateEnabledState();
    }

    /**
     * Set of attributes that is excluded when applying default attributes. By default, the TRANSFORM
     * attribute is excluded.
     */
    public void setExcludedAttributes(java.util.Set<org.jhotdraw.draw.AttributeKey<?>> a) {
        this.excludedAttributes = a;
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        pickAttributes();
    }

    @java.lang.SuppressWarnings("unchecked")
    public void pickAttributes() {
        org.jhotdraw.draw.DrawingEditor editor = getEditor();
        java.util.Collection<org.jhotdraw.draw.figure.Figure> selection = getView().getSelectedFigures();
        if (selection.size() > 0) {
            org.jhotdraw.draw.figure.Figure figure = selection.iterator().next();
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : figure.attr().getAttributes().entrySet()) {
                if (!excludedAttributes.contains(entry.getKey())) {
                    editor.setDefaultAttribute(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                }
            }
        }
    }

    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        setEnabled(getView().getSelectionCount() == 1);
    }
}