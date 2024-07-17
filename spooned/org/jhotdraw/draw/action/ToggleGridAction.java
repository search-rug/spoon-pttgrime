/* @(#)ToggleGridAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * Toggles the grid of the current view.
 */
public class ToggleGridAction extends org.jhotdraw.draw.action.AbstractDrawingViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.toggleGrid";

    public ToggleGridAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.ToggleGridAction.ID);
        updateViewState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        org.jhotdraw.draw.DrawingView view = getView();
        if (view != null) {
            view.setConstrainerVisible(!view.isConstrainerVisible());
        }
    }

    @java.lang.Override
    protected void updateViewState() {
        org.jhotdraw.draw.DrawingView view = getView();
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, (view != null) && view.isConstrainerVisible());
    }
}