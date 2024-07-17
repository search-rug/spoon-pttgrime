/* @(#)MoveConstrainedAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * Moves the selected figures by one constrained unit.
 */
public abstract class MoveConstrainedAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.constrainer.TranslationDirection dir;

    public MoveConstrainedAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.constrainer.TranslationDirection dir) {
        super(editor);
        this.dir = dir;
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (getView().getSelectionCount() > 0) {
            java.awt.geom.Rectangle2D.Double r = null;
            java.util.HashSet<org.jhotdraw.draw.figure.Figure> transformedFigures = new java.util.HashSet<>();
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    transformedFigures.add(f);
                    if (r == null) {
                        r = f.getBounds();
                    } else {
                        r.add(f.getBounds());
                    }
                }
            }
            if (transformedFigures.isEmpty()) {
                return;
            }
            java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(r.x, r.y);
            if (getView().getConstrainer() != null) {
                getView().getConstrainer().translateRectangle(r, dir);
            } else {
                switch (dir) {
                    case NORTH :
                        r.y -= 1;
                        break;
                    case SOUTH :
                        r.y += 1;
                        break;
                    case WEST :
                        r.x -= 1;
                        break;
                    case EAST :
                        r.x += 1;
                        break;
                }
            }
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(r.x - p0.x, r.y - p0.y);
            for (org.jhotdraw.draw.figure.Figure f : transformedFigures) {
                f.willChange();
                f.transform(tx);
                f.changed();
            }
            org.jhotdraw.undo.CompositeEdit edit;
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(transformedFigures, tx));
        }
    }

    public static class East extends org.jhotdraw.draw.action.MoveConstrainedAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveConstrainedEast";

        public East(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, org.jhotdraw.draw.constrainer.TranslationDirection.EAST);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveConstrainedAction.East.ID);
        }
    }

    public static class West extends org.jhotdraw.draw.action.MoveConstrainedAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveConstrainedWest";

        public West(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, org.jhotdraw.draw.constrainer.TranslationDirection.WEST);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveConstrainedAction.West.ID);
        }
    }

    public static class North extends org.jhotdraw.draw.action.MoveConstrainedAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveConstrainedNorth";

        public North(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, org.jhotdraw.draw.constrainer.TranslationDirection.NORTH);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveConstrainedAction.North.ID);
        }
    }

    public static class South extends org.jhotdraw.draw.action.MoveConstrainedAction {
        private static final long serialVersionUID = 1L;

        public static final java.lang.String ID = "edit.moveConstrainedSouth";

        public South(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor, org.jhotdraw.draw.constrainer.TranslationDirection.SOUTH);
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            labels.configureAction(this, org.jhotdraw.draw.action.MoveConstrainedAction.South.ID);
        }
    }
}