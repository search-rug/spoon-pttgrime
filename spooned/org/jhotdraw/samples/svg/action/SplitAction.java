/* @(#)SplitPathsAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.action;
/**
 * SplitPathsAction.
 */
public class SplitAction extends org.jhotdraw.samples.svg.action.CombineAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.splitPath";

    private org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

    public SplitAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor, new org.jhotdraw.samples.svg.figures.SVGPathFigure(), false);
        labels.configureAction(this, org.jhotdraw.samples.svg.action.SplitAction.ID);
    }

    public SplitAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.samples.svg.figures.SVGPathFigure prototype) {
        super(editor, prototype, false);
        labels.configureAction(this, org.jhotdraw.samples.svg.action.SplitAction.ID);
    }
}