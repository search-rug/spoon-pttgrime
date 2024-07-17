/* @(#)PropertyChangeEdit.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.undo;
import javax.swing.undo.UndoableEdit;
/**
 * An {@code UndoableEdit} event which can undo a change of a JavaBeans property.
 */
public class PropertyChangeEdit extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    /**
     * The object to be provided as the "source" of the JavaBeans property.
     */
    private java.lang.Object source;

    /**
     * The name of the JavaBeans property.
     */
    private java.lang.String propertyName;

    /**
     * The old value of the JavaBeans property.
     */
    private java.lang.Object oldValue;

    /**
     * The new value of the JavaBeans property.
     */
    private java.lang.Object newValue;

    /**
     * The type of the property.
     */
    private java.lang.Class<?> type;

    private java.lang.String presentationName;

    /**
     * Creates a new PropertyChangeEdit.
     */
    public PropertyChangeEdit(java.lang.Object source, java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
        this(source, propertyName, org.jhotdraw.undo.PropertyChangeEdit.propertyNameToPresentationName(propertyName), oldValue, newValue);
    }

    /**
     * Creates a new PropertyChangeEdit.
     */
    public PropertyChangeEdit(java.lang.Object source, java.lang.String propertyName, java.lang.String presentationName, java.lang.Object oldValue, java.lang.Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.presentationName = presentationName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Returns the setter for the property.
     *
     * @return the setter method.
     */
    protected java.lang.reflect.Method getSetter() {
        try {
            java.beans.PropertyDescriptor desc = new java.beans.PropertyDescriptor(propertyName, source.getClass());
            return desc.getWriteMethod();
        } catch (java.lang.Exception e) {
            java.lang.InternalError ie = new java.lang.InternalError((("Couldn't find setter for property \"" + propertyName) + "\" in ") + source);
            ie.initCause(e);
            throw ie;
        }
    }

    /**
     * Undoes the change.
     */
    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotRedoException {
        super.undo();
        try {
            getSetter().invoke(source, oldValue);
        } catch (java.lang.Exception e) {
            java.lang.InternalError ie = new java.lang.InternalError((("Couldn't invoke setter for property \"" + propertyName) + "\" in ") + source);
            ie.initCause(e);
            throw ie;
        }
    }

    /**
     * Redoes the change.
     */
    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        try {
            getSetter().invoke(source, newValue);
        } catch (java.lang.Exception e) {
            java.lang.InternalError ie = new java.lang.InternalError((("Couldn't invoke setter for property \"" + propertyName) + "\" in ") + source);
            ie.initCause(e);
            throw ie;
        }
    }

    /**
     * Returns the presentation name of the undoable edit. This is the same as the property name,
     * unless you have set a different presentation name.
     */
    @java.lang.Override
    public java.lang.String getPresentationName() {
        return presentationName;
    }

    /**
     * Sets the presentation name of the undoable edit.
     */
    public void setPresentationName(java.lang.String presentationName) {
        this.presentationName = presentationName;
    }

    /**
     * Adds the specified edit to this one, if it is a {@code PropertyChangeEdit} from the same owner
     * and the same property.
     *
     * @param anEdit
     * 		Edit to be added.
     * @return True if added.
     */
    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.undo.PropertyChangeEdit) {
            org.jhotdraw.undo.PropertyChangeEdit that = ((org.jhotdraw.undo.PropertyChangeEdit) (anEdit));
            if ((that.source == this.source) && that.propertyName.equals(this.propertyName)) {
                this.newValue = that.newValue;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of this object useful for debugging.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((((((super.toString() + " [") + propertyName) + ",") + oldValue) + ",") + newValue) + "]";
    }

    /**
     * Converts a property name into a presentation name.
     */
    public static java.lang.String propertyNameToPresentationName(java.lang.String s) {
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        boolean wasUpperCase = true;
        for (int i = 0; i < s.length(); i++) {
            if (java.lang.Character.isUpperCase(s.charAt(i))) {
                if (!wasUpperCase) {
                    b.append(' ');
                }
                wasUpperCase = true;
            } else {
                wasUpperCase = false;
            }
            b.append(i == 0 ? java.lang.Character.toUpperCase(s.charAt(i)) : s.charAt(i));
        }
        return b.toString();
    }
}