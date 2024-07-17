/* @(#)CanvasToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * ViewToolBar.
 *
 * <p>Note: you must explicitly set the view before createDisclosedComponents is called for the
 * first time.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class ViewToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingView view;

    /**
     * Creates new instance.
     */
    public ViewToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
    }

    public void setView(org.jhotdraw.draw.DrawingView view) {
        this.view = view;
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
        org.jhotdraw.draw.constrainer.GridConstrainer constrainer = ((org.jhotdraw.draw.constrainer.GridConstrainer) (view.getVisibleConstrainer()));
        constrainer.setHeight(prefs.getDouble("view.gridSize", 8.0));
        constrainer.setWidth(prefs.getDouble("view.gridSize", 8.0));
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
                // Toggle Grid Button
                javax.swing.AbstractButton toggleGridButton;
                toggleGridButton = btn = org.jhotdraw.gui.action.ButtonFactory.createToggleGridButton(view);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "alignGrid");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.NONE;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                p.add(btn, gbc);
                // Zoom button
                btn = org.jhotdraw.gui.action.ButtonFactory.createZoomButton(view);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "view.zoomFactor");
                btn.setText("100 %");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.weighty = 1;
                gbc.weightx = 1;
                btn.setPreferredSize(new java.awt.Dimension(btn.getPreferredSize().width, toggleGridButton.getPreferredSize().height));
                p.add(btn, gbc);
                break;
            case 2 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                // Grid size field and toggle grid button
                org.jhotdraw.gui.JLifeFormattedTextField gridSizeField = new org.jhotdraw.gui.JLifeFormattedTextField();
                gridSizeField.setColumns(4);
                gridSizeField.setToolTipText(labels.getString("view.gridSize.toolTipText"));
                gridSizeField.setHorizontalAlignment(org.jhotdraw.gui.JLifeFormattedTextField.RIGHT);
                gridSizeField.putClientProperty("Palette.Component.segmentPosition", "first");
                gridSizeField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(gridSizeField))));
                gridSizeField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(0.0, 1000.0, 1.0, true));
                gridSizeField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                final org.jhotdraw.draw.constrainer.GridConstrainer constrainer = ((org.jhotdraw.draw.constrainer.GridConstrainer) (view.getVisibleConstrainer()));
                gridSizeField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    @java.lang.Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if ("value".equals(evt.getPropertyName())) {
                            if (evt.getNewValue() != null) {
                                constrainer.setWidth(((java.lang.Double) (evt.getNewValue())));
                                constrainer.setHeight(((java.lang.Double) (evt.getNewValue())));
                                prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
                                try {
                                    prefs.putDouble("view.gridSize", ((java.lang.Double) (evt.getNewValue())));
                                } catch (java.lang.IllegalStateException e) {
                                    // ignore
                                }
                                view.getComponent().repaint();
                            }
                        }
                    }
                });
                gridSizeField.setValue(constrainer.getHeight());
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(gridSizeField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createToggleGridButton(view);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "alignGrid");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.NONE;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                p.add(btn, gbc);
                // Zoom factor field and zoom button
                final org.jhotdraw.gui.JLifeFormattedTextField scaleFactorField = new org.jhotdraw.gui.JLifeFormattedTextField();
                scaleFactorField.setColumns(4);
                scaleFactorField.setToolTipText(labels.getString("view.zoomFactor.toolTipText"));
                scaleFactorField.setHorizontalAlignment(org.jhotdraw.gui.JLifeFormattedTextField.RIGHT);
                scaleFactorField.putClientProperty("Palette.Component.segmentPosition", "first");
                scaleFactorField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(scaleFactorField))));
                org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.01, 50.0, 100.0, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                scaleFactorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                scaleFactorField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                scaleFactorField.setValue(view.getScaleFactor());
                scaleFactorField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    @java.lang.Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if ("value".equals(evt.getPropertyName())) {
                            if (evt.getNewValue() != null) {
                                view.setScaleFactor(((java.lang.Double) (evt.getNewValue())));
                            }
                        }
                    }
                });
                view.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    @java.lang.Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if (evt.getPropertyName() == org.jhotdraw.draw.DrawingView.SCALE_FACTOR_PROPERTY) {
                            if (evt.getNewValue() != null) {
                                scaleFactorField.setValue(((java.lang.Double) (evt.getNewValue())));
                            }
                        }
                    }
                });
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(scaleFactorField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createZoomButton(view);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                labels.configureToolBarButton(btn, "view.zoomFactor");
                btn.setText("100 %");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.weighty = 1;
                btn.setPreferredSize(new java.awt.Dimension(btn.getPreferredSize().width, scaleFactorField.getPreferredSize().height));
                p.add(btn, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "view";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents

}