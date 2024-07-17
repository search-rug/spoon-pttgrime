/* @(#)TransformRestoreEdit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.figure.Figure;
/**
 * An {@code UndoableEdit} event which can undo a lossy transform of a single {@link Figure} by
 * restoring the figure using its transform restore data.
 *
 * <p>This object is useful for undoing lossy transformations, such as the rotation, scaling or
 * shearing of a figure.
 *
 * <p>The transform restore data may consume a lot of memory. Undos of lossless transforms, such as
 * translations of a figure, should use {@link TransformEdit}.
 */
public class TransformRestoreEdit extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.Figure owner;

    private java.lang.Object oldTransformRestoreData;

    private java.lang.Object newTransformRestoreData;

    public TransformRestoreEdit(org.jhotdraw.draw.figure.Figure owner, java.lang.Object oldTransformRestoreData, java.lang.Object newTransformRestoreData) {
        this.owner = owner;
        this.oldTransformRestoreData = oldTransformRestoreData;
        this.newTransformRestoreData = newTransformRestoreData;
    }

    @java.lang.Override
    public java.lang.String getPresentationName() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels.getString("edit.transform.text");
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        owner.willChange();
        owner.restoreTransformTo(oldTransformRestoreData);
        owner.changed();
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        owner.willChange();
        owner.restoreTransformTo(newTransformRestoreData);
        owner.changed();
    }
}