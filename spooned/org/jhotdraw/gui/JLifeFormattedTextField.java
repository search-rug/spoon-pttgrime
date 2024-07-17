/* @(#)JLifeFormattedTextField.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * A JFormattedTextField which updates its value while the user is editing the field.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class JLifeFormattedTextField extends javax.swing.JFormattedTextField {
    private static final long serialVersionUID = 1L;

    /**
     * Last valid value.
     */
    private java.lang.Object value;

    /**
     * The DocumentHandler listens for document changes while the user is editing the field.
     */
    private class DocumentHandler implements javax.swing.event.DocumentListener {
        @java.lang.Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            updateValue();
        }

        @java.lang.Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            updateValue();
        }

        @java.lang.Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            updateValue();
        }
    }

    /**
     * The DocumentHandler handles document changes while the user is editing the field.
     */
    private org.jhotdraw.gui.JLifeFormattedTextField.DocumentHandler documentHandler;

    /**
     * This variable is used to prevent endless update loops. We increase its value on each entry in
     * one of the update methods and decrease it on each exit.
     */
    private int updatingDepth;

    /**
     * Creates new instance.
     */
    public JLifeFormattedTextField() {
    }

    @java.lang.Override
    public void setDocument(javax.swing.text.Document newValue) {
        javax.swing.text.Document oldValue = getDocument();
        super.setDocument(newValue);
        if (documentHandler == null) {
            documentHandler = new org.jhotdraw.gui.JLifeFormattedTextField.DocumentHandler();
        }
        if (oldValue != null) {
            oldValue.removeDocumentListener(documentHandler);
        }
        if (newValue != null) {
            newValue.addDocumentListener(documentHandler);
        }
        updateValue();
    }

    @java.lang.Override
    public void setValue(java.lang.Object newValue) {
        java.lang.Object oldValue = this.value;
        if (((oldValue != null) && (newValue != null)) && oldValue.equals(newValue)) {
            return;
        }
        if ((newValue != null) && (getFormatterFactory() == null)) {
            setFormatterFactory(getDefaultFormatterFactory(newValue));
        }
        this.value = newValue;
        firePropertyChange("value", oldValue, newValue);
        updateText();
    }

    @java.lang.Override
    public java.lang.Object getValue() {
        return value;
    }

    /**
     * Updates the value from the text of the field.
     */
    protected void updateValue() {
        if ((updatingDepth++) == 0) {
            if (getFormatter() != null) {
                try {
                    java.lang.Object newValue = getFormatter().stringToValue(getText());
                    setValue(newValue);
                } catch (java.text.ParseException ex) {
                    // ex.printStackTrace(); // do nothing
                }
            }
        }
        updatingDepth--;
    }

    /**
     * Updates the text of the field from the value.
     */
    protected void updateText() {
        if ((updatingDepth++) == 0) {
            if (getFormatter() != null) {
                try {
                    java.lang.String newText = getFormatter().valueToString(getValue());
                    setText(newText);
                    if (!isFocusOwner()) {
                        // This is like selectAll(), but we set the
                        // cursor at the start of the field, because
                        // the start of the field contains the most
                        // significant part of the field content.
                        setCaretPosition(getDocument().getLength());
                        moveCaretPosition(0);
                    }
                } catch (java.text.ParseException ex) {
                    // ex.printStackTrace(); do nothing
                }
            }
        }
        updatingDepth--;
    }

    /**
     * Returns an AbstractFormatterFactory suitable for the passed in Object type.
     */
    private javax.swing.JFormattedTextField.AbstractFormatterFactory getDefaultFormatterFactory(java.lang.Object type) {
        if (type instanceof java.text.DateFormat) {
            return new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(((java.text.DateFormat) (type))));
        }
        if (type instanceof java.text.NumberFormat) {
            return new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(((java.text.NumberFormat) (type))));
        }
        if (type instanceof java.text.Format) {
            return new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.InternationalFormatter(((java.text.Format) (type))));
        }
        if (type instanceof java.util.Date) {
            return new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter());
        }
        if (type instanceof java.lang.Number) {
            javax.swing.JFormattedTextField.AbstractFormatter displayFormatter = new javax.swing.text.NumberFormatter();
            ((javax.swing.text.NumberFormatter) (displayFormatter)).setValueClass(type.getClass());
            javax.swing.JFormattedTextField.AbstractFormatter editFormatter = new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.#"));
            ((javax.swing.text.NumberFormatter) (editFormatter)).setValueClass(type.getClass());
            return new javax.swing.text.DefaultFormatterFactory(displayFormatter, displayFormatter, editFormatter);
        }
        return new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DefaultFormatter());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

}