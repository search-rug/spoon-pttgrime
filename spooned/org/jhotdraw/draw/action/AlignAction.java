/* @(#)AlignAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * Aligns the selected figures.
 *
 * <p>XXX - Fire edit events
 */
public abstract class AlignAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");

    public AlignAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        updateEnabledState();
    }

    @java.lang.Override
    public void updateEnabledState() {
        if (getView() != null) {
            setEnabled(getView().isEnabled() && (getView().getSelectionCount() > 1));
        } else {
            setEnabled(false);
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        org.jhotdraw.undo.CompositeEdit edit = new org.jhotdraw.undo.CompositeEdit(labels.getString("edit.align.text"));
        fireUndoableEditHappened(edit);
        alignFigures(getView().getSelectedFigures(), getSelectionBounds());
        fireUndoableEditHappened(edit);
    }

    protected abstract void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds);

    /**
     * Returns the bounds of the selected figures.
     */
    protected java.awt.geom.Rectangle2D.Double getSelectionBounds() {
        java.awt.geom.Rectangle2D.Double bounds = null;
        for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
            if (bounds == null) {
                bounds = f.getBounds();
            } else {
                bounds.add(f.getBounds());
            }
        }
        return bounds;
    }

    public static class North extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public North(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignNorth");
        }

        public North(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignNorth");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double y = selectionBounds.y;
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate(0, y - b.y);
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }

    public static class East extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public East(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignEast");
        }

        public East(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignEast");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double x = selectionBounds.x + selectionBounds.width;
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate((x - b.x) - b.width, 0);
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }

    public static class West extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public West(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignWest");
        }

        public West(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignWest");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double x = selectionBounds.x;
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate(x - b.x, 0);
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }

    public static class South extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public South(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignSouth");
        }

        public South(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignSouth");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double y = selectionBounds.y + selectionBounds.height;
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate(0, (y - b.y) - b.height);
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }

    public static class Vertical extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public Vertical(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignVertical");
        }

        public Vertical(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignVertical");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double y = selectionBounds.y + (selectionBounds.height / 2);
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate(0, (y - b.y) - (b.height / 2));
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }

    public static class Horizontal extends org.jhotdraw.draw.action.AlignAction {
        private static final long serialVersionUID = 1L;

        public Horizontal(org.jhotdraw.draw.DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignHorizontal");
        }

        public Horizontal(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
            super(editor);
            labels.configureAction(this, "edit.alignHorizontal");
        }

        @java.lang.Override
        protected void alignFigures(java.util.Collection<org.jhotdraw.draw.figure.Figure> selectedFigures, java.awt.geom.Rectangle2D.Double selectionBounds) {
            double x = selectionBounds.x + (selectionBounds.width / 2);
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (f.isTransformable()) {
                    f.willChange();
                    java.awt.geom.Rectangle2D.Double b = f.getBounds();
                    java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                    tx.translate((x - b.x) - (b.width / 2), 0);
                    f.transform(tx);
                    f.changed();
                    fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformEdit(f, tx));
                }
            }
        }
    }
}