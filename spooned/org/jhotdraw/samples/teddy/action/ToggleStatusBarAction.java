/* @(#)ToggleStatusBarAction.java

Copyright (c) 2005 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy.action;
/**
 * ToggleStatusBarAction.
 */
public class ToggleStatusBarAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.toggleStatusBar";

    private org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.teddy.Labels");

    public ToggleStatusBarAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        labels.configureAction(this, org.jhotdraw.samples.teddy.action.ToggleStatusBarAction.ID);
        setPropertyName("statusBarVisible");
    }

    @java.lang.Override
    public org.jhotdraw.samples.teddy.TeddyView getActiveView() {
        return ((org.jhotdraw.samples.teddy.TeddyView) (super.getActiveView()));
    }

    @java.lang.Override
    protected void updateView() {
        putValue(javax.swing.Action.SELECTED_KEY, (getActiveView() != null) && getActiveView().isStatusBarVisible());
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        getActiveView().setStatusBarVisible(!getActiveView().isStatusBarVisible());
    }
}