/* @(#)SplitPathsAction.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.action;
/**
 * SplitPathsAction.
 */
public class SplitAction extends org.jhotdraw.draw.action.UngroupAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.splitPath";

    private org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");

    public SplitAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor, new org.jhotdraw.samples.odg.figures.ODGPathFigure());
        labels.configureAction(this, org.jhotdraw.samples.odg.action.SplitAction.ID);
    }

    @java.lang.Override
    protected boolean canUngroup() {
        if (super.canUngroup()) {
            return ((org.jhotdraw.draw.figure.CompositeFigure) (getView().getSelectedFigures().iterator().next())).getChildCount() > 1;
        }
        return false;
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.figure.Figure> ungroupFigures(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group) {
        java.util.LinkedList<org.jhotdraw.draw.figure.Figure> figures = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        java.util.LinkedList<org.jhotdraw.draw.figure.Figure> paths = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>();
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            org.jhotdraw.samples.odg.figures.ODGPathFigure path = new org.jhotdraw.samples.odg.figures.ODGPathFigure();
            path.removeAllChildren();
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : group.attr().getAttributes().entrySet()) {
                path.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
            path.add(f);
            view.getDrawing().basicAdd(path);
            paths.add(path);
        }
        view.getDrawing().remove(group);
        view.addToSelection(paths);
        return figures;
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void groupFigures(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.CompositeFigure group, java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> sorted = view.getDrawing().sort(figures);
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(group);
        group.willChange();
        ((org.jhotdraw.samples.odg.figures.ODGPathFigure) (group)).removeAllChildren();
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : figures.iterator().next().attr().getAttributes().entrySet()) {
            group.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
        for (org.jhotdraw.draw.figure.Figure f : sorted) {
            org.jhotdraw.samples.odg.figures.ODGPathFigure path = ((org.jhotdraw.samples.odg.figures.ODGPathFigure) (f));
            for (org.jhotdraw.draw.figure.Figure child : path.getChildren()) {
                group.basicAdd(child);
            }
        }
        group.changed();
        view.addToSelection(group);
    }
}