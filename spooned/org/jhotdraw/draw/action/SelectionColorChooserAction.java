/* @(#)SelectionColorChooserAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * This is like EditorColorChooserAction, but the JColorChooser is initialized with the color of the
 * currently selected Figures.
 *
 * <p>The behavior for choosing the initial color of the JColorChooser matches with {@link SelectionColorIcon}.
 */
public class SelectionColorChooserAction extends org.jhotdraw.draw.action.EditorColorChooserAction {
    private static final long serialVersionUID = 1L;

    public SelectionColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key) {
        this(editor, key, null, null);
    }

    public SelectionColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, javax.swing.Icon icon) {
        this(editor, key, null, icon);
    }

    public SelectionColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name) {
        this(editor, key, name, null);
    }

    public SelectionColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon) {
        this(editor, key, name, icon, new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>());
    }

    public SelectionColorChooserAction(org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.lang.String name, javax.swing.Icon icon, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> fixedAttributes) {
        super(editor, key, name, icon, fixedAttributes);
    }

    @java.lang.Override
    protected java.awt.Color getInitialColor() {
        java.awt.Color initialColor = null;
        org.jhotdraw.draw.DrawingView v = getEditor().getActiveView();
        if ((v != null) && (v.getSelectedFigures().size() == 1)) {
            org.jhotdraw.draw.figure.Figure f = v.getSelectedFigures().iterator().next();
            initialColor = f.attr().get(key);
        }
        if (initialColor == null) {
            initialColor = super.getInitialColor();
        }
        return initialColor;
    }
}