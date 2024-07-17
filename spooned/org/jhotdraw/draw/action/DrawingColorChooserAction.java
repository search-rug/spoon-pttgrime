/* @(#)DrawingColorChooserAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * The DrawingColorChooserAction changes a color attribute of the Drawing object in the current view
 * of the DrawingEditor.
 *
 * <p>The behavior for choosing the initial color of the JColorChooser matches with {@link DrawingColorIcon}.
 */
public class DrawingColorChooserAction extends org.jhotdraw.draw.action.EditorColorChooserAction {
    private static final long serialVersionUID = 1L;

    public DrawingColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key) {
        this(editor, key, null, null);
    }

    public DrawingColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, javax.swing.Icon icon) {
        this(editor, key, null, icon);
    }

    public DrawingColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name) {
        this(editor, key, name, null);
    }

    public DrawingColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon) {
        this(editor, key, name, icon, new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>());
    }

    public DrawingColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes) {
        super(editor, key, name, icon, fixedAttributes);
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
            java.util.HashSet<org.jhotdraw.draw.figure.Figure> figures = new java.util.HashSet<>();
            figures.addAll(getView().getDrawing().getChildren());
            applyAttributesTo(attr, figures);
        }
    }

    @java.lang.Override
    protected java.awt.Color getInitialColor() {
        java.awt.Color initialColor = null;
        org.jhotdraw.draw.DrawingView v = getEditor().getActiveView();
        if (v != null) {
            org.jhotdraw.draw.Drawing f = v.getDrawing();
            initialColor = f.attr().get(key);
        }
        if (initialColor == null) {
            initialColor = super.getInitialColor();
        }
        return initialColor;
    }

    @java.lang.Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(getView().isEnabled());
        } else {
            setEnabled(false);
        }
    }
}