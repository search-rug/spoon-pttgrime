/* @(#)SetBoundsEdit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * SetBoundsEdit.
 */
public class SetBoundsEdit extends javax.swing.undo.AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.AbstractAttributedFigure owner;

    private java.awt.geom.Point2D.Double oldAnchor;

    private java.awt.geom.Point2D.Double oldLead;

    private java.awt.geom.Point2D.Double newAnchor;

    private java.awt.geom.Point2D.Double newLead;

    public SetBoundsEdit(org.jhotdraw.draw.figure.AbstractAttributedFigure owner, java.awt.geom.Point2D.Double oldAnchor, java.awt.geom.Point2D.Double oldLead, java.awt.geom.Point2D.Double newAnchor, java.awt.geom.Point2D.Double newLead) {
        this.owner = owner;
        this.oldAnchor = oldAnchor;
        this.oldLead = oldLead;
        this.newAnchor = newAnchor;
        this.newLead = newLead;
    }

    @java.lang.Override
    public java.lang.String getPresentationName() {
        // XXX - Localize me
        return "Abmessungen ändern";
    }

    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.SetBoundsEdit) {
            org.jhotdraw.draw.event.SetBoundsEdit that = ((org.jhotdraw.draw.event.SetBoundsEdit) (anEdit));
            if (that.owner == this.owner) {
                this.newAnchor = that.newAnchor;
                this.newLead = that.newLead;
                that.die();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (anEdit instanceof org.jhotdraw.draw.event.SetBoundsEdit) {
            org.jhotdraw.draw.event.SetBoundsEdit that = ((org.jhotdraw.draw.event.SetBoundsEdit) (anEdit));
            if (that.owner == this.owner) {
                that.oldAnchor = this.oldAnchor;
                that.oldLead = this.oldLead;
                this.die();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotRedoException {
        super.redo();
        owner.willChange();
        owner.setBounds(newAnchor, newLead);
        owner.changed();
    }

    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        super.undo();
        owner.willChange();
        owner.setBounds(oldAnchor, oldLead);
        owner.changed();
    }
}