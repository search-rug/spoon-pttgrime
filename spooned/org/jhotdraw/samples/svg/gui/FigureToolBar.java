/* @(#)FigureToolBar.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * FigureToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class FigureToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * Creates new instance.
     */
    public FigureToolBar() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
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
        }
    }

    @java.lang.Override
    protected javax.swing.JComponent createDisclosedComponent(int state) {
        javax.swing.JPanel p = null;
        switch (state) {
            case 1 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setLayout(new java.awt.GridBagLayout());
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                // Opacity slider
                org.jhotdraw.gui.JPopupButton opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.figureOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.figureOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.weighty = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY, opacitySlider, editor));
                break;
            case 2 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setLayout(new java.awt.GridBagLayout());
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                // Opacity field with slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> opacityField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                opacityField.setColumns(4);
                opacityField.setToolTipText(labels.getString("attribute.figureOpacity.toolTipText"));
                opacityField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
                opacityField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(opacityField))));
                org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.0, 100.0, 100.0, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                opacityField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                opacityField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY, opacityField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.weightx = 1.0;
                p.add(opacityField, gbc);
                opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.figureOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.figureOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.weighty = 1;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY, opacitySlider, editor));
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "figure";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

}