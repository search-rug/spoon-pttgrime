/* @(#)AppletApplication.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.api.app.View;
/**
 * {@code AppletApplication} handles the lifecycle of a single {@link View} inside of a Java Applet.
 *
 * <p>FIXME - To be implemented.
 */
public class AppletApplication extends org.jhotdraw.app.AbstractApplication {
    private static final long serialVersionUID = 1L;

    private javax.swing.JApplet applet;

    private org.jhotdraw.api.app.View view;

    /**
     * Creates a new instance of AppletApplication
     */
    public AppletApplication(javax.swing.JApplet applet) {
        this.applet = applet;
    }

    @java.lang.Override
    public void init() {
        super.init();
        initLabels();
        setActionMap(model.createActionMap(this, null));
        model.initApplication(this);
    }

    @java.lang.Override
    public void show(org.jhotdraw.api.app.View v) {
        this.view = v;
        applet.getContentPane().removeAll();
        applet.getContentPane().add(v.getComponent());
        v.start();
        v.activate();
    }

    @java.lang.Override
    public void hide(org.jhotdraw.api.app.View v) {
        v.deactivate();
        v.stop();
        applet.getContentPane().removeAll();
        this.view = null;
    }

    @java.lang.Override
    public org.jhotdraw.api.app.View getActiveView() {
        return view;
    }

    @java.lang.Override
    public boolean isSharingToolsAmongViews() {
        return false;
    }

    @java.lang.Override
    public java.awt.Component getComponent() {
        return applet;
    }

    @java.lang.Override
    protected javax.swing.ActionMap createViewActionMap(org.jhotdraw.api.app.View p) {
        return new javax.swing.ActionMap();
    }

    @java.lang.Override
    public javax.swing.JMenu createFileMenu(org.jhotdraw.api.app.View v) {
        return null;
    }

    @java.lang.Override
    public javax.swing.JMenu createEditMenu(org.jhotdraw.api.app.View v) {
        return null;
    }

    @java.lang.Override
    public javax.swing.JMenu createViewMenu(org.jhotdraw.api.app.View v) {
        return null;
    }

    @java.lang.Override
    public javax.swing.JMenu createWindowMenu(org.jhotdraw.api.app.View v) {
        return null;
    }

    @java.lang.Override
    public javax.swing.JMenu createHelpMenu(org.jhotdraw.api.app.View v) {
        return null;
    }
}