/* @(#)ToolBarPrefsHandler.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util.prefs;
/**
 * ToolBarPrefsHandler.
 */
public class ToolBarPrefsHandler implements java.awt.event.ComponentListener , javax.swing.event.AncestorListener {
    private javax.swing.JToolBar toolbar;

    private java.lang.String prefsPrefix;

    private java.util.prefs.Preferences prefs;

    public ToolBarPrefsHandler(javax.swing.JToolBar toolbar, java.lang.String prefsPrefix, java.util.prefs.Preferences prefs) {
        this.toolbar = toolbar;
        this.prefsPrefix = prefsPrefix;
        this.prefs = prefs;
        java.lang.String constraint = prefs.get(prefsPrefix + ".constraint", java.awt.BorderLayout.NORTH);
        int orientation = (constraint.equals(java.awt.BorderLayout.NORTH) || constraint.equals(java.awt.BorderLayout.SOUTH)) ? javax.swing.JToolBar.HORIZONTAL : javax.swing.JToolBar.VERTICAL;
        toolbar.setOrientation(orientation);
        toolbar.getParent().add(constraint, toolbar);
        toolbar.setVisible(prefs.getBoolean(prefsPrefix + ".visible", true));
        /* if (prefs.getBoolean(prefsPrefix+".isFloating", false)) {
        makeToolBarFloat();
        }
         */
        toolbar.addComponentListener(this);
        toolbar.addAncestorListener(this);
    }

    /* XXX - This does not work
    private void makeToolBarFloat() {
    BasicToolBarUI ui = (BasicToolBarUI) toolbar.getUI();
    Window window = SwingUtilities.getWindowAncestor(toolbar);
    System.out.println("Window Ancestor:"+window+" instanceof Frame:"+(window instanceof Frame));
    ui.setFloating(true, new Point(
    prefs.getInt(prefsPrefix+".floatingX", 0),
    prefs.getInt(prefsPrefix+".floatingY", 0)
    ));
    window = SwingUtilities.getWindowAncestor(toolbar);
    window.setLocation(
    prefs.getInt(prefsPrefix+".floatingX", 0),
    prefs.getInt(prefsPrefix+".floatingY", 0)
    );
    window.toFront();
    }
     */
    @java.lang.Override
    public void componentHidden(java.awt.event.ComponentEvent e) {
        prefs.putBoolean(prefsPrefix + ".visible", false);
    }

    @java.lang.Override
    public void componentMoved(java.awt.event.ComponentEvent e) {
        locationChanged();
    }

    private void locationChanged() {
        // FIXME : use reflection to get hold of method 'isFloating'.
        if (toolbar.getUI() instanceof javax.swing.plaf.basic.BasicToolBarUI) {
            javax.swing.plaf.basic.BasicToolBarUI ui = ((javax.swing.plaf.basic.BasicToolBarUI) (toolbar.getUI()));
            boolean floating = ui.isFloating();
            prefs.putBoolean(prefsPrefix + ".isFloating", floating);
            if (floating) {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(toolbar);
                prefs.putInt(prefsPrefix + ".floatingX", window.getX());
                prefs.putInt(prefsPrefix + ".floatingY", window.getY());
            } else if (toolbar.getParent() != null) {
                int x = toolbar.getX();
                int y = toolbar.getY();
                java.awt.Insets insets = toolbar.getParent().getInsets();
                java.lang.String constraint;
                if ((x == insets.left) && (y == insets.top)) {
                    constraint = (toolbar.getOrientation() == javax.swing.JToolBar.HORIZONTAL) ? java.awt.BorderLayout.NORTH : java.awt.BorderLayout.WEST;
                } else {
                    constraint = (toolbar.getOrientation() == javax.swing.JToolBar.HORIZONTAL) ? java.awt.BorderLayout.SOUTH : java.awt.BorderLayout.EAST;
                }
                prefs.put(prefsPrefix + ".constraint", constraint);
            }
        } else if (toolbar.getParent() != null) {
            int x = toolbar.getX();
            int y = toolbar.getY();
            java.awt.Insets insets = toolbar.getParent().getInsets();
            java.lang.String constraint;
            if ((x == insets.left) && (y == insets.top)) {
                constraint = (toolbar.getOrientation() == javax.swing.JToolBar.HORIZONTAL) ? java.awt.BorderLayout.NORTH : java.awt.BorderLayout.WEST;
            } else {
                constraint = (toolbar.getOrientation() == javax.swing.JToolBar.HORIZONTAL) ? java.awt.BorderLayout.SOUTH : java.awt.BorderLayout.EAST;
            }
            prefs.put(prefsPrefix + ".constraint", constraint);
        }
    }

    @java.lang.Override
    public void componentResized(java.awt.event.ComponentEvent e) {
        locationChanged();
    }

    @java.lang.Override
    public void componentShown(java.awt.event.ComponentEvent e) {
        prefs.putBoolean(prefsPrefix + ".visible", true);
    }

    @java.lang.Override
    public void ancestorAdded(javax.swing.event.AncestorEvent event) {
        locationChanged();
    }

    @java.lang.Override
    public void ancestorMoved(javax.swing.event.AncestorEvent event) {
        if (toolbar.getUI() instanceof javax.swing.plaf.basic.BasicToolBarUI) {
            if (((javax.swing.plaf.basic.BasicToolBarUI) (toolbar.getUI())).isFloating()) {
                locationChanged();
            }
        }
    }

    @java.lang.Override
    public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
        if (toolbar.getUI() instanceof javax.swing.plaf.basic.BasicToolBarUI) {
            if (((javax.swing.plaf.basic.BasicToolBarUI) (toolbar.getUI())).isFloating()) {
                locationChanged();
            }
        }
    }
}