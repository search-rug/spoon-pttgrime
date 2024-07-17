/* @(#)AttributeChangeEdit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.figure.Figure;
/**
 * An {@code UndoableEdit} event which can undo a change of a {@link Figure} attribute.
 */
public class AttributeChangeEdit<T> extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.Figure owner;

    private org.jhotdraw.draw.AttributeKey<T> name;

    private T oldValue;

    private T newValue;

    public AttributeChangeEdit(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.AttributeKey<T> name, T oldValue, T newValue) {
        this.owner = owner;
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @java.lang.Override
    public java.lang.String getPresentationName() {
        // FIXME - Localize me
        return "Eigenschaft ändern";
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        owner.willChange();
        owner.attr().set(name, newValue);
        owner.changed();
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        owner.willChange();
        owner.attr().set(name, oldValue);
        owner.changed();
    }
}