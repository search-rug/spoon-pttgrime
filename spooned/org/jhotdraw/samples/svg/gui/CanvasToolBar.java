/* @(#)CanvasToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * CanvasToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class CanvasToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    /**
     * Creates new instance.
     */
    public CanvasToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
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
                // Fill color
                btn = org.jhotdraw.gui.action.ButtonFactory.createDrawingColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, "attribute.canvasFillColor", labels, null, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                disposables.add(new org.jhotdraw.draw.event.DrawingComponentRepainter(editor, btn));
                ((org.jhotdraw.gui.JPopupButton) (btn)).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(btn, gbc);
                // Opacity slider
                org.jhotdraw.gui.JPopupButton opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, opacitySlider, editor));
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.canvasFillOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.DrawingOpacityIcon(editor, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, null, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.canvasFillOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                disposables.add(new org.jhotdraw.draw.event.DrawingComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(opacityPopupButton, gbc);
                // Width and height fields
                javax.swing.JLabel widthLabel;
                javax.swing.JLabel heightLabel;
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> widthField;
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> heightField;
                widthLabel = new javax.swing.JLabel();
                heightLabel = new javax.swing.JLabel();
                widthField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                heightField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                widthLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(widthLabel))));
                widthLabel.setLabelFor(widthField);
                widthLabel.setToolTipText(labels.getString("attribute.canvasWidth.toolTipText"));
                widthLabel.setText(labels.getString("attribute.canvasWidth.text"));// NOI18N

                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(widthLabel, gbc);
                widthField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(widthField))));
                widthField.setColumns(3);
                widthField.setToolTipText(labels.getString("attribute.canvasWidth.toolTipText"));
                org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(1.0, 4096.0, 1.0, true);
                formatter.setUsesScientificNotation(false);
                widthField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                widthField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH, widthField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(widthField, gbc);
                heightLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(heightLabel))));
                heightLabel.setLabelFor(widthField);
                heightLabel.setToolTipText(labels.getString("attribute.canvasHeight.toolTipText"));
                heightLabel.setText(labels.getString("attribute.canvasHeight.text"));// NOI18N

                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(heightLabel, gbc);
                heightField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(widthField))));
                heightField.setColumns(3);
                heightField.setToolTipText(labels.getString("attribute.canvasHeight.toolTipText"));
                formatter = new org.jhotdraw.formatter.JavaNumberFormatter(1.0, 4096.0, 1.0, true);
                formatter.setUsesScientificNotation(false);
                heightField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                heightField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT, heightField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                gbc.gridwidth = 2;
                p.add(heightField, gbc);
                break;
            case 2 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                javax.swing.JPanel p1 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p2 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p3 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p1.setOpaque(false);
                p2.setOpaque(false);
                p3.setOpaque(false);
                p.removeAll();
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                // Fill color field with button
                org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color> colorField = new org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color>();
                colorField.setColumns(7);
                colorField.setToolTipText(labels.getString("attribute.canvasFillColor.toolTipText"));
                colorField.putClientProperty("Palette.Component.segmentPosition", "first");
                colorField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(colorField))));
                colorField.setFormatterFactory(org.jhotdraw.text.ColorFormatter.createFormatterFactory());
                colorField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.awt.Color>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, colorField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridwidth = 2;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p1.add(colorField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createDrawingColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, "attribute.canvasFillColor", labels, null, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                disposables.add(new org.jhotdraw.draw.event.DrawingComponentRepainter(editor, btn));
                ((org.jhotdraw.gui.JPopupButton) (btn)).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p1.add(btn, gbc);
                // Opacity field with slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> opacityField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                opacityField.setColumns(4);
                opacityField.setToolTipText(labels.getString("attribute.figureOpacity.toolTipText"));
                opacityField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
                opacityField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(opacityField))));
                formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.0, 100.0, 100.0, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                opacityField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                opacityField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, opacityField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p1.add(opacityField, gbc);
                opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, opacitySlider, editor));
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.canvasFillOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.DrawingOpacityIcon(editor, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, null, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.canvasFillOpacity.icon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                disposables.add(new org.jhotdraw.draw.event.DrawingComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p1.add(opacityPopupButton, gbc);
                // Width and height fields
                widthLabel = new javax.swing.JLabel();
                heightLabel = new javax.swing.JLabel();
                widthField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                heightField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                widthLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(widthLabel))));
                widthLabel.setLabelFor(widthField);
                widthLabel.setToolTipText(labels.getString("attribute.canvasWidth.toolTipText"));
                widthLabel.setText(labels.getString("attribute.canvasWidth.text"));// NOI18N

                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p3.add(widthLabel, gbc);
                widthField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(widthField))));
                widthField.setColumns(3);
                widthField.setToolTipText(labels.getString("attribute.canvasWidth.toolTipText"));
                widthField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(1.0, 4096.0, 1.0, true));
                widthField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH, widthField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p3.add(widthField, gbc);
                heightLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(heightLabel))));
                heightLabel.setLabelFor(widthField);
                heightLabel.setToolTipText(labels.getString("attribute.canvasHeight.toolTipText"));
                heightLabel.setText(labels.getString("attribute.canvasHeight.text"));// NOI18N

                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 3;
                gbc.gridy = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p3.add(heightLabel, gbc);
                heightField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(widthField))));
                heightField.setColumns(3);
                heightField.setToolTipText(labels.getString("attribute.canvasHeight.toolTipText"));
                heightField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(1.0, 4096.0, 1.0, true));
                heightField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT, heightField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 4;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p3.add(heightField, gbc);
                // Add horizontal strips
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(p1, gbc);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(p2, gbc);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(p3, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "canvas";
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