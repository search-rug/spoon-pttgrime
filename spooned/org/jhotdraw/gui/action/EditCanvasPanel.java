/**
 *
 * @(#)EditCanvasPanel.java <p>Copyright (c) 1996-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.action;
/**
 * The EditCanvasPanel can be used to edit the attributes of a Drawing.
 *
 * @see org.jhotdraw.draw.Drawing
 */
// End of variables declaration//GEN-END:variables
@java.lang.SuppressWarnings({ "unchecked", "rawtypes" })
public class EditCanvasPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    private org.jhotdraw.draw.Drawing drawing;

    private org.jhotdraw.draw.gui.JAttributeSlider opacitySlider;

    private javax.swing.JColorChooser colorChooser;

    private org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double> opacityFieldHandler;

    private org.jhotdraw.draw.event.DrawingAttributeEditorHandler<java.lang.Double> opacitySliderHandler;

    /**
     * Creates new form.
     */
    public EditCanvasPanel() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        initComponents();
        colorButton.putClientProperty("Quaqua.Button.style", "colorWell");
        opacitySlider = new org.jhotdraw.draw.gui.JAttributeSlider(javax.swing.JSlider.VERTICAL, 0, 100, 100);
        opacityPopupButton.add(opacitySlider);
        opacityPopupButton.putClientProperty("JButton.buttonType", "toolbar");
        add(opacityPopupButton);
        javax.swing.text.NumberFormatter nf = new javax.swing.text.NumberFormatter();
        nf.setMaximum(1.0);
        nf.setMinimum(0.0);
        opacityField.setFormatterFactory(org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(0.0, 1.0, 100.0));
        opacityFieldHandler = new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, opacityField, null);
        opacitySliderHandler = new org.jhotdraw.draw.event.DrawingAttributeEditorHandler<>(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY, opacitySlider, null);
    }

    private javax.swing.JColorChooser getColorChooser() {
        if (colorChooser == null) {
            colorChooser = new javax.swing.JColorChooser();
        }
        return colorChooser;
    }

    /**
     * Sets the GridConstrainer to be edited by this panel.
     *
     * @param newValue
     * 		The GridConstrainer.
     */
    public void setDrawing(org.jhotdraw.draw.Drawing newValue) {
        drawing = newValue;
        // XXX - This does not work, we must pass the drawing editor here!
        opacitySliderHandler.setDrawing(drawing);
        opacityFieldHandler.setDrawing(drawing);
        updatePanel();
    }

    /**
     * Updates the drawing due to changes made on this panel.
     */
    private void updateDrawing() {
        if (drawing != null) {
            java.awt.Color oldColor = drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR);
            drawing.willChange();
            drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, colorButton.getBackground());
            drawing.fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                @java.lang.Override
                public java.lang.String getPresentationName() {
                    return org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR.getPresentationName();
                }

                @java.lang.Override
                public void undo() {
                    super.undo();
                    drawing.willChange();
                    drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, oldColor);
                    drawing.changed();
                }

                @java.lang.Override
                public void redo() {
                    super.redo();
                    drawing.willChange();
                    drawing.attr().set(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR, colorButton.getBackground());
                    drawing.changed();
                }
            });
            drawing.changed();
        }
    }

    /**
     * Updates the panel due to changes made on the drawing.
     */
    private void updatePanel() {
        if (drawing != null) {
            colorButton.setBackground(drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR));
        }
    }

    /**
     * Returns the GridConstrainer currently being edited by this panel.
     *
     * @return The GridConstrainer.
     */
    public org.jhotdraw.draw.Drawing getDrawing() {
        return drawing;
    }

    public static void main(java.lang.String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        javax.swing.JFrame f = new javax.swing.JFrame("Drawing Settings2");
        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        f.add(new org.jhotdraw.gui.action.EditCanvasPanel());
        f.pack();
        f.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bgColorButtonGroup = new javax.swing.ButtonGroup();
        colorLabel = new javax.swing.JLabel();
        colorButton = new javax.swing.JButton();
        opacityLabel = new javax.swing.JLabel();
        opacityField = new org.jhotdraw.draw.gui.JAttributeTextField<java.lang.Double>();
        opacityPopupButton = new org.jhotdraw.gui.JPopupButton();
        setLayout(new java.awt.GridBagLayout());
        colorLabel.setText(labels.getString("attribute.canvasFillColor.text"));// NOI18N

        colorLabel.setToolTipText(labels.getString("attribute.backgroundColor.toolTipText"));// NOI18N

        add(colorLabel, new java.awt.GridBagConstraints());
        colorButton.setText(" ");
        colorButton.setToolTipText(labels.getString("attribute.backgroundColor.toolTipText"));// NOI18N

        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonPerformed(evt);
            }
        });
        add(colorButton, new java.awt.GridBagConstraints());
        opacityLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jhotdraw/draw/action/images/attributeOpacity.png")));// NOI18N

        opacityLabel.setToolTipText(labels.getString("attribute.opacity.toolTipText"));// NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(opacityLabel, gridBagConstraints);
        opacityField.setColumns(3);
        add(opacityField, new java.awt.GridBagConstraints());
        opacityPopupButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jhotdraw/draw/action/images/popupIcon.png")));// NOI18N

        opacityPopupButton.setToolTipText(labels.getString("attribute.opacity.toolTipText"));// NOI18N

        add(opacityPopupButton, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents


    private void colorButtonPerformed(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_colorButtonPerformed
        if (drawing != null) {
            java.awt.Color color = org.jhotdraw.gui.Dialogs.showColorChooserDialog(colorChooser, this, labels.getString("attribute.backgroundColor"), drawing.attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR));
            colorButton.setBackground(color);
            updateDrawing();
        }
    }// GEN-LAST:event_colorButtonPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgColorButtonGroup;

    private javax.swing.JButton colorButton;

    private javax.swing.JLabel colorLabel;

    private org.jhotdraw.draw.gui.JAttributeTextField opacityField;

    private javax.swing.JLabel opacityLabel;

    private org.jhotdraw.gui.JPopupButton opacityPopupButton;
}