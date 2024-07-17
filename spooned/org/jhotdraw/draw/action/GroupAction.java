/* @(#)GroupAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * GroupAction.
 */
public class GroupAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.groupSelection";

    private org.jhotdraw.draw.figure.CompositeFigure prototype;

    /**
     * If this variable is true, this action groups figures. If this variable is false, this action
     * ungroups figures.
     */
    private boolean isGroupingAction;

    public GroupAction(org.jhotdraw.draw.DrawingEditor editor) {
        this(editor, new org.jhotdraw.draw.figure.GroupFigure(), true);
    }

    public GroupAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.figure.CompositeFigure prototype) {
        this(editor, prototype, true);
    }

    public GroupAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.figure.CompositeFigure prototype, boolean isGroupingAction) {
        super(editor);
        this.prototype = prototype;
        this.isGroupingAction = isGroupingAction;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.GroupAction.ID);
        updateEnabledState();
    }

    @java.lang.Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(isGroupingAction ? canGroup() : canUngroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        return (getView() != null) && (getView().getSelectionCount() > 1);
    }

    protected boolean canUngroup() {
        return (((getView() != null) && (getView().getSelectionCount() == 1)) && (prototype != null)) && getView().getSelectedFigures().iterator().next().getClass().equals(prototype.getClass());
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (isGroupingAction) {
            if (canGroup()) {
                final org.jhotdraw.draw.DrawingView view = getView();
                final java.util.List<org.jhotdraw.draw.figure.Figure> ungroupedFigures = new java.util.ArrayList<>(view.getSelectedFigures());
                final org.jhotdraw.draw.figure.CompositeFigure group = ((org.jhotdraw.draw.figure.CompositeFigure) (prototype.clone()));
                javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public java.lang.String getPresentationName() {
                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                        return labels.getString("edit.groupSelection.text");
                    }

                    @java.lang.Override
                    public void redo() throws javax.swing.undo.CannotRedoException {
                        super.redo();
                        groupFigures(view, group, ungroupedFigures);
                    }

                    @java.lang.Override
                    public void undo() throws javax.swing.undo.CannotUndoException {
                        ungroupFigures(view, group);
                        super.undo();
                    }

                    @java.lang.Override
                    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
                        return super.addEdit(anEdit);
                    }
                };
                groupFigures(view, group, ungroupedFigures);
                fireUndoableEditHappened(edit);
            }
        } else if (canUngroup()) {
            final org.jhotdraw.draw.DrawingView view = getView();
            final org.jhotdraw.draw.figure.CompositeFigure group = ((org.jhotdraw.draw.figure.CompositeFigure) (getView().getSelectedFigures().iterator().next()));
            final java.util.List<org.jhotdraw.draw.figure.Figure> ungroupedFigures = new java.util.ArrayList<>();
            javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    return labels.getString("edit.ungroupSelection.text");
                }

                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    ungroupFigures(view, group);
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    groupFigures(view, group, ungroupedFigures);
                    super.undo();
                }
            };
            ungroupedFigures.addAll(ungroupFigures(view, group));
            fireUndoableEditHappened(edit);
        }
    }

    public java.util.Collection<org.jhotdraw.draw.figure.Figure> ungroupFigures(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group) {
        java.util.List<org.jhotdraw.draw.figure.Figure> figures = new java.util.ArrayList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }

    public void groupFigures(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group, java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> sorted = view.getDrawing().sort(figures);
        int index = view.getDrawing().indexOf(sorted.iterator().next());
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(index > view.getDrawing().getChildCount() ? 0 : index, group);
        group.willChange();
        for (org.jhotdraw.draw.figure.Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }
}