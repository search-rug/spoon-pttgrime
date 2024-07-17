/* @(#)CompositeFigureEdit.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.undo.CompositeEdit;
/**
 * A {@link CompositeEdit} which invokes {@code figure.willChange} and {@code figure.changed} when
 * undoing or redoing a change.
 */
public class CompositeFigureEdit extends org.jhotdraw.undo.CompositeEdit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.Figure figure;

    /**
     * Creates a new {@code CompositeFigureEdit} which uses CompoundEdit.getPresentatioName and is
     * significant.
     *
     * @see javax.swing.undo.CompoundEdit#getPresentationName()
     */
    public CompositeFigureEdit(org.jhotdraw.draw.figure.Figure figure) {
        this.figure = figure;
    }

    /**
     * Creates new CompositeFigureEdit which uses the specified significance.
     *
     * @see javax.swing.undo.CompoundEdit#getPresentationName()
     */
    public CompositeFigureEdit(org.jhotdraw.draw.figure.Figure figure, boolean isSignificant) {
        super(isSignificant);
        this.figure = figure;
    }

    /**
     * Creates new CompositeFigureEdit which uses the specified presentation name.
     *
     * @see javax.swing.undo.CompoundEdit#getPresentationName()
     */
    public CompositeFigureEdit(org.jhotdraw.draw.figure.Figure figure, java.lang.String presentationName) {
        super(presentationName);
        this.figure = figure;
    }

    /**
     * Creates new CompositeEdit. Which uses the given presentation name. If the presentation name is
     * null, then CompoundEdit.getPresentatioName is used.
     *
     * @see javax.swing.undo.CompoundEdit#getPresentationName()
     */
    public CompositeFigureEdit(org.jhotdraw.draw.figure.Figure figure, java.lang.String presentationName, boolean isSignificant) {
        super(presentationName, isSignificant);
        this.figure = figure;
    }

    @java.lang.Override
    public void undo() {
        if (!canUndo()) {
            throw new javax.swing.undo.CannotUndoException();
        }
        figure.willChange();
        super.undo();
        figure.changed();
    }

    @java.lang.Override
    public void redo() {
        if (!canRedo()) {
            throw new javax.swing.undo.CannotRedoException();
        }
        figure.willChange();
        super.redo();
        figure.changed();
    }
}