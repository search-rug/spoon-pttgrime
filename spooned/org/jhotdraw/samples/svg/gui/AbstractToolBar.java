/**
 *
 * @(#)AbstractToolBar.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * AbstractToolBar.
 */
/* abstract */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class AbstractToolBar extends org.jhotdraw.gui.JDisclosureToolBar implements org.jhotdraw.api.app.Disposable {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.draw.DrawingEditor editor;

    private javax.swing.JComponent[] panels;

    protected java.util.prefs.Preferences prefs;

    protected java.beans.PropertyChangeListener eventHandler;

    protected java.util.LinkedList<org.jhotdraw.api.app.Disposable> disposables = new java.util.LinkedList<org.jhotdraw.api.app.Disposable>();

    /**
     * Creates new form.
     */
    public AbstractToolBar() {
        initComponents();
        try {
            prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
        } catch (java.lang.SecurityException e) {
            // prefs is null, because we are not permitted to read preferences
        }
    }

    /**
     * This should be an abstract method, but the NetBeans GUI builder doesn't support abstract beans.
     *
     * @return The ID used to retrieve labels and store user preferences.
     */
    protected java.lang.String getID() {
        return "";
    }

    /**
     * This should be an abstract method, but the NetBeans GUI builder doesn't support abstract beans.
     */
    protected void init() {
    }

    protected java.beans.PropertyChangeListener getEventHandler() {
        if (eventHandler == null) {
            eventHandler = new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    java.lang.String name = evt.getPropertyName();
                    if (name == org.jhotdraw.gui.JDisclosureToolBar.DISCLOSURE_STATE_PROPERTY) {
                        try {
                            prefs.putInt(getID() + ".disclosureState", ((java.lang.Integer) (evt.getNewValue())));
                        } catch (java.lang.IllegalStateException e) {
                            // This happens, due to a bug in Apple's implementation
                            // of the Preferences class.
                            java.lang.System.err.println("Warning AbstractToolBar caught IllegalStateException of Preferences class");
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        return eventHandler;
    }

    public void setEditor(org.jhotdraw.draw.DrawingEditor editor) {
        if (this.editor != null) {
            this.removePropertyChangeListener(getEventHandler());
            for (org.jhotdraw.api.app.Disposable d : disposables) {
                d.dispose();
            }
            disposables.clear();
        }
        this.editor = editor;
        if (editor != null) {
            init();
            clearDisclosedComponents();
            setDisclosureState(java.lang.Math.max(0, java.lang.Math.min(getDisclosureStateCount(), prefs.getInt(getID() + ".disclosureState", getDefaultDisclosureState()))));
            this.addPropertyChangeListener(getEventHandler());
        }
    }

    public org.jhotdraw.draw.DrawingEditor getEditor() {
        return editor;
    }

    public void clearDisclosedComponents() {
        panels = null;
    }

    @java.lang.Override
    protected final javax.swing.JComponent getDisclosedComponent(int state) {
        if (panels == null) {
            panels = new javax.swing.JPanel[getDisclosureStateCount()];
            for (int i = 0; i < panels.length; i++) {
                panels[i] = new org.jhotdraw.samples.svg.gui.AbstractToolBar.ProxyPanel();
            }
        }
        return panels[state];
    }

    /* abstract */
    protected javax.swing.JComponent createDisclosedComponent(int state) {
        return null;
    }

    protected int getDefaultDisclosureState() {
        return 0;
    }

    @java.lang.Override
    public void dispose() {
        for (org.jhotdraw.api.app.Disposable d : disposables) {
            d.dispose();
        }
        disposables.clear();
    }

    private class ProxyPanel extends javax.swing.JPanel {
        private static final long serialVersionUID = 1L;

        private java.lang.Runnable runner;

        public ProxyPanel() {
            setOpaque(false);
            setBackground(java.awt.Color.GREEN);
            // The paint method is only called, if the proxy panel is at least
            // one pixel wide and high.
            setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 1));
        }

        @java.lang.Override
        public void paint(java.awt.Graphics g) {
            super.paint(g);
            final int state = getDisclosureState();
            if (runner == null) {
                runner = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        try {
                            panels[state] = createDisclosedComponent(state);
                        } catch (java.lang.Throwable t) {
                            t.printStackTrace();
                            panels[state] = null;
                        }
                        // long end = System.currentTimeMillis();
                        // System.out.println(AbstractToolBar.this.getClass()+" state:"+state+"
                        // elapsed:"+(end-start));
                        javax.swing.JComponent parent = ((javax.swing.JComponent) (getParent()));
                        if (parent != null) {
                            java.awt.GridBagLayout layout = ((java.awt.GridBagLayout) (parent.getLayout()));
                            java.awt.GridBagConstraints gbc = layout.getConstraints(org.jhotdraw.samples.svg.gui.AbstractToolBar.ProxyPanel.this);
                            parent.remove(org.jhotdraw.samples.svg.gui.AbstractToolBar.ProxyPanel.this);
                            if (getDisclosureState() == state) {
                                if (panels[state] != null) {
                                    parent.add(panels[state], gbc);
                                } else {
                                    javax.swing.JPanel empty = new javax.swing.JPanel(new java.awt.BorderLayout());
                                    empty.setOpaque(false);
                                    parent.add(empty, gbc);
                                }
                            }
                            parent.revalidate();
                            ((javax.swing.JComponent) (parent.getRootPane().getContentPane())).revalidate();
                        }
                    }
                };
                javax.swing.SwingUtilities.invokeLater(runner);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

}