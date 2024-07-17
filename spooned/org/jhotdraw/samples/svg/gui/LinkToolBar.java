/* @(#)LinkToolBar.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * LinkToolBar.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class LinkToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.event.SelectionComponentDisplayer displayer;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * Creates new instance.
     */
    public LinkToolBar() {
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
                // Link field
                javax.swing.JLabel linkLabel;
                javax.swing.JScrollPane scrollPane;
                org.jhotdraw.draw.gui.JAttributeTextArea<java.lang.String> linkField;
                linkLabel = new javax.swing.JLabel();
                linkLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(linkLabel))));
                linkLabel.setToolTipText(labels.getString("attribute.figureLink.toolTipText"));
                linkLabel.setText(labels.getString("attribute.figureLink.text"));// NOI18N

                linkLabel.setFont(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                scrollPane = new javax.swing.JScrollPane();
                linkField = new org.jhotdraw.draw.gui.JAttributeTextArea<java.lang.String>();
                linkLabel.setLabelFor(linkField);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.insets = new java.awt.Insets(-2, 0, -2, 0);
                gbc.anchor = java.awt.GridBagConstraints.SOUTHWEST;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                p.add(linkLabel, gbc);
                scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.putClientProperty("JComponent.sizeVariant", "small");
                scrollPane.setBorder(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getBorder("ScrollPane.border"));
                linkField.setToolTipText(labels.getString("attribute.figureLink.toolTipText"));
                linkField.setColumns(8);
                linkField.setLineWrap(true);
                linkField.setRows(2);
                linkField.setWrapStyleWord(true);
                linkField.setFont(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                linkField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DefaultFormatter()));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.String>(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK, linkField, editor, false));
                scrollPane.setViewportView(linkField);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                p.add(scrollPane, gbc);
                // Target field
                javax.swing.JLabel targetLabel;
                org.jhotdraw.draw.gui.JAttributeTextField<java.lang.String> targetField;
                targetLabel = new javax.swing.JLabel();
                targetLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(targetLabel))));
                targetLabel.setToolTipText(labels.getString("attribute.figureLinkTarget.toolTipText"));
                targetLabel.setText(labels.getString("attribute.figureLinkTarget.text"));// NOI18N

                // targetLabel.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                targetField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.String>();
                targetLabel.setLabelFor(targetField);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(targetLabel, gbc);
                targetField.setToolTipText(labels.getString("attribute.figureLinkTarget.toolTipText"));
                targetField.setColumns(4);
                // targetField.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                targetField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DefaultFormatter()));
                targetField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(targetField))));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.String>(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET, targetField, editor, false));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(targetField, gbc);
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
                // Link field
                scrollPane = new javax.swing.JScrollPane();
                linkField = new org.jhotdraw.draw.gui.JAttributeTextArea<java.lang.String>();
                scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.putClientProperty("JComponent.sizeVariant", "small");
                scrollPane.setBorder(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getBorder("ScrollPane.border"));
                linkField.setToolTipText(labels.getString("attribute.figureLink.toolTipText"));
                linkField.setColumns(12);
                linkField.setLineWrap(true);
                linkField.setRows(2);
                linkField.setWrapStyleWord(true);
                linkField.setFont(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                linkField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DefaultFormatter()));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.String>(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK, linkField, editor, false));
                scrollPane.setViewportView(linkField);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(0, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                p.add(scrollPane, gbc);
                // Target field
                targetLabel = new javax.swing.JLabel();
                targetLabel.setUI(((javax.swing.plaf.LabelUI) (org.jhotdraw.gui.plaf.palette.PaletteLabelUI.createUI(targetLabel))));
                targetLabel.setToolTipText(labels.getString("attribute.figureLinkTarget.toolTipText"));
                targetLabel.setText(labels.getString("attribute.figureLinkTarget.text"));// NOI18N

                // targetLabel.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                targetField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.String>();
                targetLabel.setLabelFor(targetField);
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.BOTH;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(targetLabel, gbc);
                targetField.setToolTipText(labels.getString("attribute.figureLinkTarget.toolTipText"));
                targetField.setColumns(7);
                // targetField.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
                targetField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DefaultFormatter()));
                targetField.setUI(((javax.swing.plaf.TextUI) (org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI.createUI(targetField))));
                disposables.add(new org.jhotdraw.draw.event.FigureAttributeEditorHandler<java.lang.String>(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET, targetField, editor, true));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                p.add(targetField, gbc);
                break;
        }
        return p;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "link";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

}