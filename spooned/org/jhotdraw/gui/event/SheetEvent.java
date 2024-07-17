/* @(#)SheetEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.event;
/**
 * SheetEvent.
 */
public class SheetEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;

    private java.lang.Object pane;

    private int option;

    private java.lang.Object value;

    private java.lang.Object inputValue;

    public SheetEvent(org.jhotdraw.gui.JSheet source) {
        super(source);
    }

    public SheetEvent(org.jhotdraw.gui.JSheet source, javax.swing.JFileChooser fileChooser, int option, java.lang.Object value) {
        super(source);
        this.pane = fileChooser;
        this.option = option;
        this.value = value;
    }

    public SheetEvent(org.jhotdraw.gui.JSheet source, org.jhotdraw.api.gui.URIChooser chooser, int option, java.lang.Object value) {
        super(source);
        this.pane = chooser;
        this.option = option;
        this.value = value;
    }

    public SheetEvent(org.jhotdraw.gui.JSheet source, javax.swing.JOptionPane optionPane, int option, java.lang.Object value, java.lang.Object inputValue) {
        super(source);
        this.pane = optionPane;
        this.option = option;
        this.value = value;
        this.inputValue = inputValue;
    }

    /**
     * Returns the pane on the sheet. This is either a JFileChooser, a URIChooser or a JOptionPane.
     */
    public java.lang.Object getPane() {
        return pane;
    }

    /**
     * Returns the JFileChooser pane on the sheet.
     */
    public javax.swing.JFileChooser getFileChooser() {
        return ((javax.swing.JFileChooser) (pane));
    }

    /**
     * Returns the URIChooser pane on the sheet.
     */
    public org.jhotdraw.api.gui.URIChooser getChooser() {
        return ((org.jhotdraw.api.gui.URIChooser) (pane));
    }

    /**
     * Returns the JOptionPane pane on the sheet.
     */
    public javax.swing.JOptionPane getOptionPane() {
        return ((javax.swing.JOptionPane) (pane));
    }

    /**
     * Returns the option that the JFileChooser or JOptionPane returned.
     */
    public int getOption() {
        return option;
    }

    /**
     * Returns the value that the JFileChooser or JOptionPane returned.
     */
    public java.lang.Object getValue() {
        return value;
    }

    /**
     * Returns the input value that the JOptionPane returned, if it wants input.
     */
    public java.lang.Object getInputValue() {
        return inputValue;
    }
}