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
public class FontToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public FontToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("font.toolbar"));
        org.jhotdraw.gui.JFontChooser.loadAllFonts();
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
            displayer = new org.jhotdraw.draw.event.SelectionComponentDisplayer(editor, this) {
                @java.lang.Override
                public void updateVisibility() {
                    boolean newValue = ((editor != null) && (editor.getActiveView() != null)) && ((isVisibleIfCreationTool && ((editor.getTool() instanceof org.jhotdraw.draw.tool.TextCreationTool) || (editor.getTool() instanceof org.jhotdraw.draw.tool.TextAreaCreationTool))) || containsTextHolderFigure(editor.getActiveView().getSelectedFigures()));
                    javax.swing.JComponent component = getComponent();
                    if (component == null) {
                        dispose();
                        return;
                    }
                    component.setVisible(newValue);
                    // The following is needed to trick BoxLayout
                    if (newValue) {
                        component.setPreferredSize(null);
                    } else {
                        component.setPreferredSize(new java.awt.Dimension(0, 0));
                    }
                    component.revalidate();
                }

                private boolean containsTextHolderFigure(java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
                    for (org.jhotdraw.draw.figure.Figure f : figures) {
                        if (f instanceof org.jhotdraw.draw.figure.TextHolderFigure) {
                            return true;
                        } else if (f instanceof org.jhotdraw.draw.figure.CompositeFigure) {
                            if (containsTextHolderFigure(((org.jhotdraw.draw.figure.CompositeFigure) (f)).getChildren())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            };
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
                javax.swing.JPanel p1 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p2 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                javax.swing.JPanel p3 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p1.setOpaque(false);
                p2.setOpaque(false);
                p3.setOpaque(false);
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                // Font face field and popup button
                org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Font> faceField = new org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Font>();
                faceField.setColumns(2);
                faceField.setToolTipText(labels.getString("attribute.font.toolTipText"));
                faceField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                faceField.putClientProperty("Palette.Component.segmentPosition", "first");
                faceField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(faceField))));
                faceField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                faceField.setFormatterFactory(org.jhotdraw.formatter.FontFormatter.createFormatterFactory());
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.awt.Font>(org.jhotdraw.draw.AttributeKeys.FONT_FACE, faceField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridwidth = 2;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                p.add(faceField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor, org.jhotdraw.draw.AttributeKeys.FONT_FACE, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.WEST;
                p.add(btn, gbc);
                // Font size field with slider
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double> sizeField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                sizeField.setColumns(1);
                sizeField.setToolTipText(labels.getString("attribute.fontSize.toolTipText"));
                sizeField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                sizeField.putClientProperty("Palette.Component.segmentPosition", "first");
                sizeField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(sizeField))));
                sizeField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(0.0, 1000.0, 1.0));
                sizeField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, sizeField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridwidth = 2;
                gbc.weightx = 1.0F;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                p2.add(sizeField, gbc);
                org.jhotdraw.gui.JPopupButton sizePopupButton = new org.jhotdraw.gui.JPopupButton();
                org.jhotdraw.draw.gui.JAttributeSlider sizeSlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 12);
                sizePopupButton.add(sizeSlider);
                labels.configureToolBarButton(sizePopupButton, "attribute.fontSize");
                sizePopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(sizePopupButton))));
                sizePopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, sizePopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p2.add(sizePopupButton, gbc);
                sizeSlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(sizeSlider))));
                sizeSlider.setScaleFactor(1.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, sizeSlider, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                p.add(p2, gbc);
                // Font style buttons
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleBoldButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "first");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleItalicButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "middle");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleUnderlineButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "last");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.WEST;
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
                p1 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p2 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p3 = new javax.swing.JPanel(new java.awt.GridBagLayout());
                p1.setOpaque(false);
                p2.setOpaque(false);
                p3.setOpaque(false);
                labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                // Font face field and popup button
                faceField = new org.jhotdraw.draw.gui.JAttributeTextField<java.awt.Font>();
                faceField.setColumns(12);
                faceField.setToolTipText(labels.getString("attribute.font.toolTipText"));
                faceField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                faceField.putClientProperty("Palette.Component.segmentPosition", "first");
                faceField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(faceField))));
                faceField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                faceField.setFormatterFactory(org.jhotdraw.formatter.FontFormatter.createFormatterFactory());
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.awt.Font>(org.jhotdraw.draw.AttributeKeys.FONT_FACE, faceField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridwidth = 3;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                p.add(faceField, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor, org.jhotdraw.draw.AttributeKeys.FONT_FACE, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.WEST;
                p.add(btn, gbc);
                // Font size field with slider
                sizeField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
                sizeField.setColumns(1);
                sizeField.setToolTipText(labels.getString("attribute.fontSize.toolTipText"));
                sizeField.setHorizontalAlignment(org.jhotdraw.draw.gui.JAttributeTextField.RIGHT);
                sizeField.putClientProperty("Palette.Component.segmentPosition", "first");
                sizeField.setUI(((org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(sizeField))));
                sizeField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(0.0, 1000.0, 1.0));
                sizeField.setHorizontalAlignment(javax.swing.JTextField.LEADING);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, sizeField, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.gridwidth = 2;
                gbc.weightx = 1.0F;
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                p2.add(sizeField, gbc);
                sizePopupButton = new org.jhotdraw.gui.JPopupButton();
                sizeSlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 12);
                sizePopupButton.add(sizeSlider);
                labels.configureToolBarButton(sizePopupButton, "attribute.fontSize");
                sizePopupButton.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(sizePopupButton))));
                sizePopupButton.setPopupAnchor(javax.swing.SwingConstants.SOUTH_EAST);
                disposables.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, sizePopupButton));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 1;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p2.add(sizePopupButton, gbc);
                sizeSlider.setUI(((javax.swing.plaf.SliderUI) (org.jhotdraw.gui.plaf.palette.PaletteSliderUI.createUI(sizeSlider))));
                sizeSlider.setScaleFactor(1.0);
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.Double>(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, sizeSlider, editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                p.add(p2, gbc);
                // Font style buttons
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleBoldButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "first");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleItalicButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "middle");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.createFontStyleUnderlineButton(editor, labels, disposables);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.putClientProperty("Palette.Component.segmentPosition", "last");
                gbc = new java.awt.GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.anchor = java.awt.GridBagConstraints.WEST;
                p.add(btn, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "font";
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
    }// </editor-fold>//GEN-END:initComponents

}