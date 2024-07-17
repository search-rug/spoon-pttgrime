/* @(#)FillToolBar.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * FillToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class FillToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public FillToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
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
                // Fill color
                java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.put(defaultAttributes, null);
                /* btn = ButtonFactory.createSelectionColorButton(editor,
                FILL_COLOR, ButtonFactory.HSB_COLORS_AS_RGB, ButtonFactory.HSB_COLORS_AS_RGB_COLUMN_COUNT,
                "attribute.fillColor", labels, defaultAttributes, new Rectangle(3, 3, 10, 10), disposables);
                 */
                btn = org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, "attribute.fillColor", labels, defaultAttributes, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                // ((JPopupButton) btn).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(btn, gbc);
                // Opacity slider
                org.jhotdraw.gui.JPopupButton opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.fillOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.fillOpacity.largeIcon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.weighty = 1.0F;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY, opacitySlider, editor));
                break;
            case 2 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                // Abort if no editor is put
                if (editor == null) {
                    break;
                }
                javax.swing.JPanel p1 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p2 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p3 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p1.setOpaque(false);
                p2.setOpaque(false);
                p3.setOpaque(false);
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                p.removeAll();
                labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                // Fill color field and button
                defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.put(defaultAttributes, null);
                org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color> colorField = new org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Color>();
                colorField.setColumns(7);
                colorField.setToolTipText(labels.getString("attribute.fillColor.toolTipText"));
                colorField.putClientProperty("Palette.Component.segmentPosition", "first");
                colorField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(colorField))));
                colorField.setFormatterFactory(org.jhotdraw.text.ColorFormatter.createFormatterFactory(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
                colorField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.awt.Color>(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, defaultAttributes, colorField, editor, true));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p1.add(colorField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, "attribute.fillColor", labels, defaultAttributes, new java.awt.Rectangle(3, 3, 10, 10), org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.class, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                // ((JPopupButton) btn).setAction(null, null);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p1.add(btn, gbc);
                // Opacity field with slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> opacityField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                opacityField.setColumns(4);
                opacityField.setToolTipText(labels.getString("attribute.fillOpacity.toolTipText"));
                opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
                opacityField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(opacityField))));
                org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(0.0, 100.0, 100.0, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                opacityField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
                opacityField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY, opacityField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p2.add(opacityField, gbc);
                opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
                opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.fillOpacity");
                opacityPopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(opacityPopupButton))));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                opacityPopupButton.setIcon(new org.jhotdraw.samples.svg.gui.SelectionOpacityIcon(editor, org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null, org.jhotdraw.util.Images.createImage(getClass(), labels.getString("attribute.fillOpacity.largeIcon")), new java.awt.Rectangle(5, 5, 6, 6), new java.awt.Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.weighty = 1.0F;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p2.add(opacityPopupButton, gbc);
                opacitySlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(opacitySlider))));
                opacitySlider.setScaleFactor(100.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY, opacitySlider, editor));
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
                gbc.weighty = 1.0F;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(p3, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "fill";
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