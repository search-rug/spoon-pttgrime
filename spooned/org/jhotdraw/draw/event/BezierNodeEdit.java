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
 *
 * @version $Id: BezierNodeEdit.java -1 $
 * @author Werner Randelshofer
 */
public class BezierNodeEdit extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.BezierFigure owner;

    private int index;

    private org.jhotdraw.geom.path.BezierPath.Node oldValue;

    private org.jhotdraw.geom.path.BezierPath.Node newValue;

    public BezierNodeEdit(org.jhotdraw.draw.figure.BezierFigure owner, int index, org.jhotdraw.geom.path.BezierPath.Node oldValue, org.jhotdraw.geom.path.BezierPath.Node newValue) {
        this.owner = owner;
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @java.lang.Override
    public java.lang.String getPresentationName() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        if (oldValue.mask != newValue.mask) {
            return labels.getString("edit.bezierNode.changeType.text");
        } else {
            return labels.getString("edit.bezierNode.movePoint.text");
        }
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        owner.willChange();
        owner.setNode(index, newValue);
        owner.changed();
        if (oldValue.mask != newValue.mask) {
        }
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        owner.willChange();
        owner.setNode(index, oldValue);
        owner.changed();
    }

    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.BezierNodeEdit) {
            org.jhotdraw.draw.event.BezierNodeEdit that = ((org.jhotdraw.draw.event.BezierNodeEdit) (anEdit));
            if ((that.owner == this.owner) && (that.index == this.index)) {
                this.newValue = that.newValue;
                return true;
            }
        }
        return false;
    }
}