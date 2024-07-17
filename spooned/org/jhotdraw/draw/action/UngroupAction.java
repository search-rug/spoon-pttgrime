/* @(#)UngroupAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * UngroupAction.
 */
public class UngroupAction extends org.jhotdraw.draw.action.GroupAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.ungroupSelection";

    private org.jhotdraw.draw.figure.CompositeFigure prototype;

    public UngroupAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor, new org.jhotdraw.draw.figure.GroupFigure(), false);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.UngroupAction.ID);
        updateEnabledState();
    }

    public UngroupAction(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.figure.CompositeFigure prototype) {
        super(editor, prototype, false);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.UngroupAction.ID);
        updateEnabledState();
    }
}