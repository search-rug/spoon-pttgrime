/* @(#)CombinePathsAction.java

Copyright (c) 2006-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.action;
/**
 * CombinePathsAction.
 *
 * <p>FIXME - Transforms are lost during Undo/Redo.
 */
public class CombineAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.combinePaths";

    private org.jhotdraw.draw.figure.CompositeFigure prototype;

    /**
     * If this variable is true, this action groups figures. If this variable is false, this action
     * ungroups figures.
     */
    private boolean isCombineAction;

    private org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

    public CombineAction(org.jhotdraw.draw.DrawingEditor editor) {
        this(editor, new org.jhotdraw.samples.svg.figures.SVGPathFigure(true), true);
    }

    public CombineAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.samples.svg.figures.SVGPathFigure prototype) {
        this(editor, prototype, true);
    }

    public CombineAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.samples.svg.figures.SVGPathFigure prototype, boolean isGroupingAction) {
        super(editor);
        this.prototype = prototype;
        this.isCombineAction = isGroupingAction;
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        labels.configureAction(this, org.jhotdraw.samples.svg.action.CombineAction.ID);
        updateEnabledState();
    }

    @java.lang.Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(isCombineAction ? canGroup() : canUngroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        boolean canCombine = getView().getSelectionCount() > 1;
        if (canCombine) {
            for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                if (!(f instanceof org.jhotdraw.samples.svg.figures.SVGPathFigure)) {
                    canCombine = false;
                    break;
                }
            }
        }
        return canCombine;
    }

    protected boolean canUngroup() {
        return ((((getView() != null) && (getView().getSelectionCount() == 1)) && (prototype != null)) && getView().getSelectedFigures().iterator().next().getClass().equals(prototype.getClass())) && (((org.jhotdraw.draw.figure.CompositeFigure) (getView().getSelectedFigures().iterator().next())).getChildCount() > 1);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (isCombineAction) {
            combineActionPerformed(e);
        } else {
            splitActionPerformed(e);
        }
    }

    public void combineActionPerformed(java.awt.event.ActionEvent e) {
        final org.jhotdraw.draw.DrawingView view = getView();
        org.jhotdraw.draw.Drawing drawing = view.getDrawing();
        if (canGroup()) {
            final java.util.List<org.jhotdraw.draw.figure.Figure> ungroupedPaths = drawing.sort(view.getSelectedFigures());
            final int[] ungroupedPathsIndices = new int[ungroupedPaths.size()];
            final int[] ungroupedPathsChildCounts = new int[ungroupedPaths.size()];
            int i = 0;
            for (org.jhotdraw.draw.figure.Figure f : ungroupedPaths) {
                ungroupedPathsIndices[i] = drawing.indexOf(f);
                ungroupedPathsChildCounts[i] = ((org.jhotdraw.draw.figure.CompositeFigure) (f)).getChildCount();
                // System.out.print("CombineAction indices[" + i + "] = " + ungroupedPathsIndices[i]);
                // System.out.println(" childCount[" + i + "] = " + ungroupedPathsChildCounts[i]);
                i++;
            }
            final org.jhotdraw.draw.figure.CompositeFigure group = ((org.jhotdraw.draw.figure.CompositeFigure) (prototype.clone()));
            combinePaths(view, group, ungroupedPaths, ungroupedPathsIndices[0]);
            javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    return labels.getTextProperty("edit.combinePaths");
                }

                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    combinePaths(view, group, ungroupedPaths, ungroupedPathsIndices[0]);
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    super.undo();
                    splitPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);
                }

                @java.lang.Override
                public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
                    return super.addEdit(anEdit);
                }
            };
            fireUndoableEditHappened(edit);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    public void splitActionPerformed(java.awt.event.ActionEvent e) {
        final org.jhotdraw.draw.DrawingView view = getView();
        org.jhotdraw.draw.Drawing drawing = view.getDrawing();
        if (canUngroup()) {
            final org.jhotdraw.draw.figure.CompositeFigure group = ((org.jhotdraw.draw.figure.CompositeFigure) (view.getSelectedFigures().iterator().next()));
            final java.util.LinkedList<org.jhotdraw.draw.figure.Figure> ungroupedPaths = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>();
            final int[] ungroupedPathsIndices = new int[group.getChildCount()];
            final int[] ungroupedPathsChildCounts = new int[group.getChildCount()];
            int i = 0;
            int index = drawing.indexOf(group);
            for (org.jhotdraw.draw.figure.Figure f : group.getChildren()) {
                org.jhotdraw.samples.svg.figures.SVGPathFigure path = new org.jhotdraw.samples.svg.figures.SVGPathFigure(true);
                for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : group.attr().getAttributes().entrySet()) {
                    path.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                }
                ungroupedPaths.add(path);
                ungroupedPathsIndices[i] = index + i;
                ungroupedPathsChildCounts[i] = 1;
                i++;
            }
            splitPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);
            javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    return labels.getTextProperty("edit.splitPath");
                }

                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    splitPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    super.undo();
                    combinePaths(view, group, ungroupedPaths, ungroupedPathsIndices[0]);
                }
            };
            fireUndoableEditHappened(edit);
        }
    }

    public void splitPath(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group, java.util.List<org.jhotdraw.draw.figure.Figure> ungroupedPaths, int[] ungroupedPathsIndices, int[] ungroupedPathsChildCounts) {
        view.clearSelection();
        java.util.Iterator<org.jhotdraw.draw.figure.Figure> groupedFigures = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>(group.getChildren()).iterator();
        group.basicRemoveAllChildren();
        view.getDrawing().remove(group);
        org.jhotdraw.samples.svg.figures.SVGPathFigure pathFigure = ((org.jhotdraw.samples.svg.figures.SVGPathFigure) (group));
        pathFigure.flattenTransform();
        for (int i = 0; i < ungroupedPaths.size(); i++) {
            org.jhotdraw.draw.figure.CompositeFigure path = ((org.jhotdraw.draw.figure.CompositeFigure) (ungroupedPaths.get(i)));
            view.getDrawing().add(ungroupedPathsIndices[i], path);
            path.willChange();
            for (int j = 0; j < ungroupedPathsChildCounts[i]; j++) {
                org.jhotdraw.draw.figure.Figure child = groupedFigures.next();
                child.willChange();
                path.basicAdd(child);
            }
            path.changed();
        }
        view.addToSelection(ungroupedPaths);
    }

    @java.lang.SuppressWarnings("unchecked")
    public void combinePaths(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group, java.util.Collection<org.jhotdraw.draw.figure.Figure> figures, int groupIndex) {
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(groupIndex, group);
        group.willChange();
        group.basicRemoveAllChildren();
        // Verify if all figures have the same transform
        java.awt.geom.AffineTransform tx = figures.iterator().next().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            java.awt.geom.AffineTransform ftx = f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
            if ((ftx == tx) || (((ftx != null) && (tx != null)) && ftx.equals(tx))) {
            } else {
                tx = null;
                break;
            }
        }
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : figures.iterator().next().attr().getAttributes().entrySet()) {
            group.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
        // In case all figures have the same transforms, we set it here.
        // In case the transforms are different, we set null here.
        group.attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, tx);
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            org.jhotdraw.samples.svg.figures.SVGPathFigure path = ((org.jhotdraw.samples.svg.figures.SVGPathFigure) (f));
            // In case the transforms are different, we flatten it in the figures.
            if (tx == null) {
                path.flattenTransform();
            }
            java.util.List<org.jhotdraw.draw.figure.Figure> children = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>(path.getChildren());
            path.basicRemoveAllChildren();
            for (org.jhotdraw.draw.figure.Figure child : children) {
                org.jhotdraw.samples.svg.figures.SVGBezierFigure bez = ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child));
                child.willChange();
                group.basicAdd(child);
            }
        }
        group.changed();
        view.addToSelection(group);
    }
}