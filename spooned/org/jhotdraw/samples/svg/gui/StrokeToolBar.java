/* @(#)StrokeToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * StrokeToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class StrokeToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public StrokeToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("stroke.toolbar"));
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
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is put
                if (editor == null) {
                    break;
                }
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                // Stroke color
                java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.put(defaultAttributes, null);
                btn = org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, "attribute.strokeColor", labels, defaultAttributes, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                ((org.jhotdraw.gui.JPopupButton) (btn)).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(btn, gbc);
                // Opacity slider
                org.jhotdraw.gui.JPopupButton opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.strokeOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY, null, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.strokeOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY, opacitySlider, editor));
                // Create stroke width popup slider
                org.jhotdraw.gui.JPopupButton strokeWidthPopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider strokeWidthSlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 50, 1);
                strokeWidthSlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(strokeWidthSlider))));
                strokeWidthPopupButton.add(strokeWidthSlider);
                labels.configureToolBarButton(strokeWidthPopupButton, "attribute.strokeWidth");
                strokeWidthPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(strokeWidthPopupButton))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 0;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(strokeWidthPopupButton, gbc);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, strokeWidthSlider, editor));
                // Create stroke dashes buttons
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, new double[][]{ null, new double[]{ 4.0, 4.0 }, new double[]{ 2.0, 2.0 }, new double[]{ 4.0, 2.0 }, new double[]{ 2.0, 4.0 }, new double[]{ 8.0, 2.0 }, new double[]{ 6.0, 2.0, 2.0, 2.0 } }, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                break;
            case 2 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is put
                if (editor == null) {
                    break;
                }
                labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                // Stroke color field and button
                defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.put(defaultAttributes, null);
                org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color> colorField = new org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color>();
                colorField.setColumns(7);
                colorField.setToolTipText(labels.getString("attribute.strokeColor.toolTipText"));
                colorField.putClientProperty("Palette.Component.segmentPosition", "first");
                colorField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(colorField))));
                colorField.setFormatterFactory(org.jhotdraw.text.ColorFormatter.createFormatterFactory(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
                colorField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.awt.Color>(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, defaultAttributes, colorField, editor, true));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridwidth = 3;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(colorField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, "attribute.strokeColor", labels, defaultAttributes, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                ((org.jhotdraw.gui.JPopupButton) (btn)).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 3;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(btn, gbc);
                // Opacity field with slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> opacityField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                opacityField.setColumns(4);
                opacityField.setToolTipText(labels.getString("attribute.strokeOpacity.toolTipText"));
                opacityField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
                opacityField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(opacityField))));
                opacityField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
                org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.0, 100.0, 100.0, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                opacityField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY, opacityField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(opacityField, gbc);
                opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.strokeOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY, null, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.strokeOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.weighty = 1.0F;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY, opacitySlider, editor));
                // Create stroke width field with popup slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> strokeWidthField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                strokeWidthField.setColumns(2);
                strokeWidthField.setToolTipText(labels.getString("attribute.strokeWidth.toolTipText"));
                strokeWidthField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.LEFT);
                strokeWidthField.putClientProperty("Palette.Component.segmentPosition", "first");
                strokeWidthField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(strokeWidthField))));
                formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.0, 100.0, 1.0);
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                strokeWidthField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, strokeWidthField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                p.add(strokeWidthField, gbc);
                strokeWidthPopupButton = new org.jhotdraw.gui.JPopupButton();
                strokeWidthSlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 50, 1);
                strokeWidthSlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(strokeWidthSlider))));
                strokeWidthPopupButton.add(strokeWidthSlider);
                labels.configureToolBarButton(strokeWidthPopupButton, "attribute.strokeWidth");
                strokeWidthPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(strokeWidthPopupButton))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(strokeWidthPopupButton, gbc);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, strokeWidthSlider, editor));
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 4;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 4;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                // Create dash offset field and dashes button
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> dashOffsetField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                dashOffsetField.setColumns(1);
                dashOffsetField.setToolTipText(labels.getString("attribute.strokeDashPhase.toolTipText"));
                dashOffsetField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.LEFT);
                // dashOffsetField.putClientProperty("Palette.Component.segmentPosition", "first");
                dashOffsetField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(dashOffsetField))));
                dashOffsetField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(-1000.0, 1000.0, 1.0));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE, dashOffsetField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.gridwidth = 2;
                p.add(dashOffsetField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, new double[][]{ null, new double[]{ 4.0, 4.0 }, new double[]{ 2.0, 2.0 }, new double[]{ 4.0, 2.0 }, new double[]{ 2.0, 4.0 }, new double[]{ 8.0, 2.0 }, new double[]{ 6.0, 2.0, 2.0, 2.0 } }, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 4;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "stroke";
    }

    @java.lang.Override
    protected int getDefaultDisclosureState() {
        return 1;
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