/* @(#)TransformEdit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.figure.Figure;
/**
 * An {@code UndoableEdit} event which can undo a lossless transform of {@link Figure}s by applying
 * the inverse of the transform to the figures.
 *
 * <p>This object is useful for undoing lossless transformations, such as the translation of
 * figures.
 *
 * <p>If a lossy transforms is performed, such as rotation, scaling or shearing, then undos should
 * be performed with {@link TransformRestoreEdit} instead.
 */
public class TransformEdit extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    private java.util.Collection<org.jhotdraw.draw.figure.Figure> figures;

    private java.awt.geom.AffineTransform tx;

    public TransformEdit(org.jhotdraw.draw.figure.Figure figure, java.awt.geom.AffineTransform tx) {
        figures = new java.util.ArrayList<>();
        figures.add(figure);
        this.tx = ((java.awt.geom.AffineTransform) (tx.clone()));
    }

    public TransformEdit(java.util.Collection<org.jhotdraw.draw.figure.Figure> figures, java.awt.geom.AffineTransform tx) {
        this.figures = figures;
        this.tx = ((java.awt.geom.AffineTransform) (tx.clone()));
    }

    @java.lang.Override
    public java.lang.String getPresentationName() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels.getString("edit.transform.text");
    }

    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.TransformEdit) {
            org.jhotdraw.draw.event.TransformEdit that = ((org.jhotdraw.draw.event.TransformEdit) (anEdit));
            if (that.figures == this.figures) {
                this.tx.concatenate(that.tx);
                that.die();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.TransformEdit) {
            org.jhotdraw.draw.event.TransformEdit that = ((org.jhotdraw.draw.event.TransformEdit) (anEdit));
            if (that.figures == this.figures) {
                this.tx.preConcatenate(that.tx);
                that.die();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            f.willChange();
            f.transform(tx);
            f.changed();
        }
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        try {
            java.awt.geom.AffineTransform inverse = tx.createInverse();
            for (org.jhotdraw.draw.figure.Figure f : figures) {
                f.willChange();
                f.transform(inverse);
                f.changed();
            }
        } catch (java.awt.geom.NoninvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((getClass().getName() + '@') + hashCode()) + " tx:") + tx;
    }
}