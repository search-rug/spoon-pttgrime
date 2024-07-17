/* @(#)TogglePropertiesPanelAction.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.action;
/**
 * TogglePropertiesPanelAction.
 */
public class TogglePropertiesPanelAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public TogglePropertiesPanelAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        setPropertyName("propertiesPanelVisible");
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");
        putValue(javax.swing.AbstractAction.NAME, labels.getString("propertiesPanel"));
    }

    /**
     * This method is invoked, when the property changed and when the view changed.
     */
    @java.lang.Override
    protected void updateView() {
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, (getActiveView() != null) && (!getActiveView().isPropertiesPanelVisible()));
    }

    @java.lang.Override
    public org.jhotdraw.samples.odg.ODGView getActiveView() {
        return ((org.jhotdraw.samples.odg.ODGView) (super.getActiveView()));
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        getActiveView().setPropertiesPanelVisible(!getActiveView().isPropertiesPanelVisible());
    }
}