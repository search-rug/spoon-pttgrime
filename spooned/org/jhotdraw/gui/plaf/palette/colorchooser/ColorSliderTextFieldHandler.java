/* @(#)ColorSliderTextFieldHandler.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette.colorchooser;
/**
 * This handler adjusts the value of a component in the color slider model, when the user enters
 * text into the text field.
 */
public class ColorSliderTextFieldHandler implements javax.swing.event.DocumentListener , javax.swing.event.ChangeListener {
    protected javax.swing.JTextField textField;

    protected org.jhotdraw.color.ColorSliderModel ccModel;

    protected int component;

    public ColorSliderTextFieldHandler(javax.swing.JTextField textField, org.jhotdraw.color.ColorSliderModel ccModel, int component) {
        this.textField = textField;
        this.ccModel = ccModel;
        this.component = component;
        textField.getDocument().addDocumentListener(this);
        ccModel.getBoundedRangeModel(component).addChangeListener(this);
    }

    @java.lang.Override
    public void changedUpdate(javax.swing.event.DocumentEvent evt) {
        docChanged();
    }

    @java.lang.Override
    public void removeUpdate(javax.swing.event.DocumentEvent evt) {
        docChanged();
    }

    @java.lang.Override
    public void insertUpdate(javax.swing.event.DocumentEvent evt) {
        docChanged();
    }

    protected void docChanged() {
        if (textField.hasFocus()) {
            javax.swing.BoundedRangeModel brm = ccModel.getBoundedRangeModel(component);
            try {
                int value = java.lang.Integer.decode(textField.getText());
                if ((brm.getMinimum() <= value) && (value <= brm.getMaximum())) {
                    brm.setValue(value);
                }
            } catch (java.lang.NumberFormatException e) {
                // Don't change value if it isn't numeric.
            }
        }
    }

    @java.lang.Override
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        if (!textField.hasFocus()) {
            textField.setText(java.lang.Integer.toString(ccModel.getBoundedRangeModel(component).getValue()));
        }
    }
}