/* @(#)TeddyApplicationModel.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * Provides meta-data and factory methods for an application.
 *
 * <p>See {@link ApplicationModel} on how this class interacts with an application.
 */
public class TeddyApplicationModel extends org.jhotdraw.app.DefaultApplicationModel {
    private static final long serialVersionUID = 1L;

    public TeddyApplicationModel() {
    }

    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap m = super.createActionMap(a, v);
        javax.swing.AbstractAction aa;
        m.put(org.jhotdraw.samples.teddy.action.FindAction.ID, new org.jhotdraw.samples.teddy.action.FindAction(a, v));
        m.put(org.jhotdraw.samples.teddy.action.ToggleLineWrapAction.ID, new org.jhotdraw.samples.teddy.action.ToggleLineWrapAction(a, v));
        m.put(org.jhotdraw.samples.teddy.action.ToggleStatusBarAction.ID, new org.jhotdraw.samples.teddy.action.ToggleStatusBarAction(a, v));
        m.put(org.jhotdraw.samples.teddy.action.ToggleLineNumbersAction.ID, new org.jhotdraw.samples.teddy.action.ToggleLineNumbersAction(a, v));
        m.put(org.jhotdraw.app.action.file.PrintFileAction.ID, null);
        return m;
    }

    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
    }

    /**
     * Creates the MenuBuilder.
     */
    @java.lang.Override
    protected org.jhotdraw.api.app.MenuBuilder createMenuBuilder() {
        return new org.jhotdraw.app.DefaultMenuBuilder() {
            @java.lang.Override
            public void addOtherViewItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
                javax.swing.ActionMap am = app.getActionMap(v);
                javax.swing.JCheckBoxMenuItem cbmi;
                cbmi = new javax.swing.JCheckBoxMenuItem(am.get(org.jhotdraw.samples.teddy.action.ToggleLineWrapAction.ID));
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get(org.jhotdraw.samples.teddy.action.ToggleLineWrapAction.ID));
                m.add(cbmi);
                cbmi = new javax.swing.JCheckBoxMenuItem(am.get(org.jhotdraw.samples.teddy.action.ToggleLineNumbersAction.ID));
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get(org.jhotdraw.samples.teddy.action.ToggleLineNumbersAction.ID));
                m.add(cbmi);
                cbmi = new javax.swing.JCheckBoxMenuItem(am.get(org.jhotdraw.samples.teddy.action.ToggleStatusBarAction.ID));
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get(org.jhotdraw.samples.teddy.action.ToggleStatusBarAction.ID));
                m.add(cbmi);
            }
        };
    }

    /**
     * Creates toolbars for the application. This class returns an empty list - we don't want toolbars
     * in a text editor.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View p) {
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public org.jhotdraw.gui.JFileURIChooser createOpenChooser(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View p) {
        org.jhotdraw.gui.JFileURIChooser chooser = new org.jhotdraw.gui.JFileURIChooser();
        chooser.setAccessory(new org.jhotdraw.samples.teddy.CharacterSetAccessory());
        return chooser;
    }

    @java.lang.Override
    public org.jhotdraw.gui.JFileURIChooser createSaveChooser(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View p) {
        org.jhotdraw.gui.JFileURIChooser chooser = new org.jhotdraw.gui.JFileURIChooser();
        chooser.setAccessory(new org.jhotdraw.samples.teddy.CharacterSetAccessory());
        return chooser;
    }
}