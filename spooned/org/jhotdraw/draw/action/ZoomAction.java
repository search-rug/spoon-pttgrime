/* @(#)ZoomAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * ZoomAction.
 */
public class ZoomAction extends org.jhotdraw.draw.action.AbstractDrawingViewAction {
    private static final long serialVersionUID = 1L;

    private double scaleFactor;

    private javax.swing.AbstractButton button;

    private java.lang.String label;

    public ZoomAction(org.jhotdraw.draw.DrawingEditor editor, double scaleFactor, javax.swing.AbstractButton button) {
        this(((org.jhotdraw.draw.DrawingView) (null)), scaleFactor, button);
        setEditor(editor);
    }

    public ZoomAction(org.jhotdraw.draw.DrawingView view, double scaleFactor, javax.swing.AbstractButton button) {
        super(view);
        this.scaleFactor = scaleFactor;
        this.button = button;
        label = ((int) (scaleFactor * 100)) + " %";
        putValue(javax.swing.Action.DEFAULT, label);
        putValue(javax.swing.Action.NAME, label);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (button != null) {
            button.setText(label);
        }
        final java.awt.Rectangle vRect = getView().getComponent().getVisibleRect();
        final double oldFactor = getView().getScaleFactor();
        getView().setScaleFactor(scaleFactor);
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                if (vRect != null) {
                    vRect.x = ((int) ((vRect.x / oldFactor) * scaleFactor));
                    vRect.y = ((int) ((vRect.y / oldFactor) * scaleFactor));
                    vRect.width = ((int) ((vRect.width / oldFactor) * scaleFactor));
                    vRect.height = ((int) ((vRect.height / oldFactor) * scaleFactor));
                    vRect.x += vRect.width / 3;
                    vRect.y += vRect.height / 3;
                    vRect.width /= 3;
                    vRect.height /= 3;
                    getView().getComponent().scrollRectToVisible(vRect);
                }
            }
        });
    }
}