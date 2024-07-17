/* @(#)SelectSameAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * SelectSameAction.
 */
public class SelectSameAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.selectSame";

    public SelectSameAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.SelectSameAction.ID);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        selectSame();
    }

    public void selectSame() {
        java.util.HashSet<java.lang.Class<?>> selectedClasses = new java.util.HashSet<>();
        for (org.jhotdraw.draw.figure.Figure selected : getView().getSelectedFigures()) {
            selectedClasses.add(selected.getClass());
        }
        for (org.jhotdraw.draw.figure.Figure f : getDrawing().getChildren()) {
            if (selectedClasses.contains(f.getClass())) {
                getView().addToSelection(f);
            }
        }
    }
}