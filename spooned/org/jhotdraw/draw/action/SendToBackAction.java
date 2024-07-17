/* @(#)SendToBackAction.java

Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
public class SendToBackAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.sendToBack";

    public SendToBackAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.SendToBackAction.ID);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final org.jhotdraw.draw.DrawingView view = getView();
        final java.util.List<org.jhotdraw.draw.figure.Figure> figures = new java.util.ArrayList<>(view.getSelectedFigures());
        org.jhotdraw.draw.action.SendToBackAction.sendToBack(view, figures);
        fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getTextProperty(org.jhotdraw.draw.action.SendToBackAction.ID);
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                org.jhotdraw.draw.action.SendToBackAction.sendToBack(view, figures);
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                org.jhotdraw.draw.action.BringToFrontAction.bringToFront(view, figures);
            }
        });
    }

    public static void sendToBack(org.jhotdraw.draw.DrawingView view, java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
        org.jhotdraw.draw.Drawing drawing = view.getDrawing();
        for (org.jhotdraw.draw.figure.Figure figure : figures) {
            // XXX Shouldn't the figures be sorted here back to front?
            drawing.sendToBack(figure);
        }
    }
}