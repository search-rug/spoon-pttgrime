/* @(#)ActionsToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * ActionsToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class ActionsToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.undo.UndoRedoManager undoManager;

    private java.util.ArrayList<javax.swing.Action> actions;

    private org.jhotdraw.gui.JPopupButton popupButton;

    /**
     * Creates new instance.
     */
    public ActionsToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
    }

    @java.lang.Override
    public void setEditor(org.jhotdraw.draw.DrawingEditor newValue) {
        if ((this.editor != null) && (undoManager != null)) {
            this.removePropertyChangeListener(getEventHandler());
        }
        this.editor = newValue;
        if ((editor != null) && (undoManager != null)) {
            init();
            updatePopupMenu();
            setDisclosureState(prefs.getInt(getID() + ".disclosureState", 1));
            this.addPropertyChangeListener(getEventHandler());
        }
    }

    public void setUndoManager(org.jhotdraw.undo.UndoRedoManager newValue) {
        if ((this.editor != null) && (newValue != null)) {
            this.removePropertyChangeListener(getEventHandler());
        }
        this.undoManager = newValue;
        if ((editor != null) && (newValue != null)) {
            init();
            setDisclosureState(prefs.getInt(getID() + ".disclosureState", 1));
            this.addPropertyChangeListener(getEventHandler());
        }
    }

    /**
     * Sets the actions for the "Action" popup menu in the toolbar.
     *
     * <p>This list may contain null items which are used to denote a separator in the popup menu.
     *
     * <p>Set this to null to set the drop down menus to the default actions.
     */
    public void setPopupActions(java.util.List<javax.swing.Action> actions) {
        if (actions == null) {
            this.actions = null;
        } else {
            this.actions = new java.util.ArrayList<javax.swing.Action>();
            this.actions.addAll(actions);
        }
    }

    /**
     * Gets the actions of the "Action" popup menu in the toolbar. This list may contain null items
     * which are used to denote a separator in the popup menu.
     *
     * @return An unmodifiable list with actions.
     */
    public java.util.List<javax.swing.Action> getPopupActions() {
        if (actions == null) {
            actions = new java.util.ArrayList<javax.swing.Action>();
        }
        return java.util.Collections.unmodifiableList(actions);
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
                // Preferences prefs = PreferencesUtil.userNodeForPackage(getClass());
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                btn = new javax.swing.JButton(undoManager.getUndoAction());
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.setText(null);
                labels.configureToolBarButton(btn, "edit.undo");
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 0;
                gbc.gridx = 0;
                p.add(btn, gbc);
                btn = new javax.swing.JButton(undoManager.getRedoAction());
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.setText(null);
                labels.configureToolBarButton(btn, "edit.redo");
                btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createPickAttributesButton(editor, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "attributesPick");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createApplyAttributesButton(editor, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "attributesApply");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                org.jhotdraw.gui.JPopupButton pb = new org.jhotdraw.gui.JPopupButton();
                pb.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(pb))));
                pb.setItemFont(javax.swing.UIManager.getFont("MenuItem.font"));
                labels.configureToolBarButton(pb, "actions");
                popupButton = pb;
                updatePopupMenu();
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(pb, gbc);
                break;
        }
        return p;
    }

    private void updatePopupMenu() {
        if (popupButton != null) {
            org.jhotdraw.draw.action.AbstractSelectedAction d;
            org.jhotdraw.gui.JPopupButton pb = popupButton;
            pb.removeAll();
            pb.add(new org.jhotdraw.action.edit.DuplicateAction());
            pb.addSeparator();
            pb.add(d = new org.jhotdraw.draw.action.GroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
            disposables.add(d);
            pb.add(d = new org.jhotdraw.draw.action.UngroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
            disposables.add(d);
            pb.addSeparator();
            pb.add(new org.jhotdraw.action.edit.CutAction());
            pb.add(new org.jhotdraw.action.edit.CopyAction());
            pb.add(new org.jhotdraw.action.edit.PasteAction());
            pb.add(new org.jhotdraw.action.edit.DeleteAction());
            pb.addSeparator();
            pb.add(new org.jhotdraw.action.edit.SelectAllAction());
            pb.add(d = new org.jhotdraw.draw.action.SelectSameAction(editor));
            disposables.add(d);
            pb.add(new org.jhotdraw.action.edit.ClearSelectionAction());
            if (!getPopupActions().isEmpty()) {
                pb.addSeparator();
                for (javax.swing.Action a : getPopupActions()) {
                    if (a == null) {
                        pb.addSeparator();
                    } else {
                        pb.add(a);
                    }
                }
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


    @java.lang.Override
    protected java.lang.String getID() {
        return "actions";
    }

    @java.lang.Override
    protected int getDefaultDisclosureState() {
        return 1;
    }
}