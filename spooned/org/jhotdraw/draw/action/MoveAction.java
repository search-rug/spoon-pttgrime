/* @(#)MoveAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * Moves the selected figures by one unit.
 */
public abstract class MoveAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private int dx;

    private int dy;

    public MoveAction(org.jhotdraw.draw.DrawingEditor editor, int dx, int dy) {
        super(editor);
        this.dx = dx;
        this.dy = dy;
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        org.jhotdraw.undo.CompositeEdit edit;
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(dx, dy);
        java.util.HashSet<org.jhotdraw.draw.figure.Figure> transformedFigures = new java.util.HashSet<>();
        for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
            if (f.isTransformable()) {
                transformedFigures.add(f);
                f.willChange();
                f.transform(tx);
                f.changed();
            }
        }
        fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(transformedFigures, tx));
    }

    public static class East extends org.jhotdraw.draw.action.MoveAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveEast";

        public East(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, 1, 0);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveAction.East.ID);
        }
    }

    public static class West extends org.jhotdraw.draw.action.MoveAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveWest";

        public West(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, -1, 0);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveAction.West.ID);
        }
    }

    public static class North extends org.jhotdraw.draw.action.MoveAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveNorth";

        public North(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, 0, -1);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveAction.North.ID);
        }
    }

    public static class South extends org.jhotdraw.draw.action.MoveAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveSouth";

        public South(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, 0, 1);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveAction.South.ID);
        }
    }
}