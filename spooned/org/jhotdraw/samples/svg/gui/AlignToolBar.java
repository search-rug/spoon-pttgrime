/* @(#)AlignToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * AlignToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class AlignToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public AlignToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
    }

    @java.lang.Override
    public void setEditor(org.jhotdraw.draw.DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new org.jhotdraw.draw.event.SelectionComponentDisplayer(editor, this);
            displayer.setMinSelectionCount(2);
            displayer.setVisibleIfCreationTool(false);
        }
    }

    @java.lang.Override
    protected javax.swing.JComponent createDisclosedComponent(int state) {
        javax.swing.JPanel p = null;
        switch (state) {
            case 1 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                org.jhotdraw.draw.action.AbstractSelectedAction d;
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 0;
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.West(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.setText(null);
                p.add(btn, gbc);
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.East(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                btn.setText(null);
                p.add(btn, gbc);
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.North(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                btn.setText(null);
                p.add(btn, gbc);
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.South(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                btn.setText(null);
                p.add(btn, gbc);
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.Horizontal(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                btn.setText(null);
                p.add(btn, gbc);
                gbc.gridx = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.AlignAction.Vertical(editor, labels));
                disposables.add(d);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                btn.setText(null);
                p.add(btn, gbc);
                break;
        }
        return p;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents


    @java.lang.Override
    protected java.lang.String getID() {
        return "align";
    }
}