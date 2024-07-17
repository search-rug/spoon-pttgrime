/* @(#)EditorColorChooserAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * EditorColorChooserAction.
 *
 * <p>The behavior for choosing the initial color of the JColorChooser matches with {@link EditorColorIcon}.
 */
public class EditorColorChooserAction extends org.jhotdraw.draw.action.AttributeAction {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.draw.AttributeKey<java.awt.Color> key;

    protected static javax.swing.JColorChooser colorChooser;

    public EditorColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key) {
        this(editor, key, null, null);
        updateEnabledState();
    }

    public EditorColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, javax.swing.Icon icon) {
        this(editor, key, null, icon);
    }

    public EditorColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name) {
        this(editor, key, name, null);
    }

    public EditorColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon) {
        this(editor, key, name, icon, new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>());
    }

    public EditorColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes) {
        super(editor, fixedAttributes, name, icon);
        this.key = key;
        putValue(javax.swing.AbstractAction.NAME, name);
        putValue(javax.swing.AbstractAction.SMALL_ICON, icon);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (org.jhotdraw.draw.action.EditorColorChooserAction.colorChooser == null) {
            org.jhotdraw.draw.action.EditorColorChooserAction.colorChooser = new javax.swing.JColorChooser();
        }
        java.awt.Color initialColor = getInitialColor();
        // FIXME - Reuse colorChooser object instead of calling static method here.
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        java.awt.Color chosenColor = javax.swing.JColorChooser.showDialog(((java.awt.Component) (e.getSource())), labels.getString("attribute.color.text"), initialColor);
        if (chosenColor != null) {
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attr = new java.util.HashMap<>(attributes);
            attr.put(key, chosenColor);
            applyAttributesTo(attr, getView().getSelectedFigures());
        }
    }

    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        // setEnabled(getView().getSelectionCount() > 0);
    }

    protected java.awt.Color getInitialColor() {
        java.awt.Color initialColor = getEditor().getDefaultAttribute(key);
        if (initialColor == null) {
            initialColor = java.awt.Color.red;
        }
        return initialColor;
    }
}