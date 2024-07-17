/* @(#)SelectSameAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * SelectSameAction.
 */
public class IncreaseHandleDetailLevelAction extends org.jhotdraw.draw.action.AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.increaseHandleDetailLevel";

    public IncreaseHandleDetailLevelAction(org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.draw.action.IncreaseHandleDetailLevelAction.ID);
        // putValue(AbstractAction.NAME, labels.getString("editSelectSame"));
        // putValue(AbstractAction.MNEMONIC_KEY, labels.getString("editSelectSameMnem"));
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        increaseHandleDetaiLevel();
    }

    public void increaseHandleDetaiLevel() {
        org.jhotdraw.draw.DrawingView view = getView();
        if (view != null) {
            view.setHandleDetailLevel(view.getHandleDetailLevel() + 1);
        }
    }
}