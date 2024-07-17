/* @(#)BezierNodeEdit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.figure.BezierFigure;
/**
 * An {@code UndoableEdit} event which can undo a change of a node in a {@link BezierFigure}.
 */
public class TrackingEdit extends javax.swing.undo.AbstractUndoableEdit {
    private final org.jhotdraw.draw.figure.Figure owner;

    private final java.awt.geom.Point2D.Double oldValue;

    private java.awt.geom.Point2D.Double newValue;

    private final java.util.function.Consumer<java.awt.geom.Point2D.Double> writeLocation;

    public TrackingEdit(org.jhotdraw.draw.figure.Figure owner, java.util.function.Consumer<java.awt.geom.Point2D.Double> writeLocation, java.awt.geom.Point2D.Double oldValue, java.awt.geom.Point2D.Double newValue) {
        this.owner = owner;
        this.writeLocation = writeLocation;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        owner.willChange();
        writeLocation.accept(newValue);
        owner.changed();
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        owner.willChange();
        writeLocation.accept(oldValue);
        owner.changed();
    }

    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.TrackingEdit that) {
            if ((that.owner == this.owner) && (that.writeLocation == this.writeLocation)) {
                this.newValue = that.newValue;
                return true;
            }
        }
        return false;
    }
}