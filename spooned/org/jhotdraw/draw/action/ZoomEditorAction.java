/* @(#)ZoomEditorAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * Zooms either the current view or all views of a DrawingEditor.
 */
public class ZoomEditorAction extends org.jhotdraw.draw.action.AbstractDrawingEditorAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "zoomEditor";

    private double scaleFactor;

    private javax.swing.AbstractButton button;

    private java.lang.String label;

    private boolean updateAllViews;

    public ZoomEditorAction(org.jhotdraw.draw.DrawingEditor editor, double scaleFactor, javax.swing.AbstractButton button) {
        this(editor, scaleFactor, button, true);
    }

    public ZoomEditorAction(org.jhotdraw.draw.DrawingEditor editor, double scaleFactor, javax.swing.AbstractButton button, boolean updateAllViews) {
        super(editor);
        this.scaleFactor = scaleFactor;
        this.button = button;
        this.updateAllViews = updateAllViews;
        label = ((int) (scaleFactor * 100)) + " %";
        putValue(javax.swing.Action.DEFAULT, label);
        putValue(javax.swing.Action.NAME, label);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (button != null) {
            button.setText(label);
        }
        if (updateAllViews) {
            for (org.jhotdraw.draw.DrawingView v : getEditor().getDrawingViews()) {
                v.setScaleFactor(scaleFactor);
            }
        } else {
            getView().setScaleFactor(scaleFactor);
        }
    }
}