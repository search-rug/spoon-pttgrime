/* @(#)AbstractFindAction.java

Copyright (c) 2005 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy.action;
/**
 * AbstractFindAction shows the find dialog.
 */
public class FindAction extends org.jhotdraw.action.edit.AbstractFindAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = org.jhotdraw.action.edit.AbstractFindAction.ID;

    private org.jhotdraw.samples.teddy.FindDialog findDialog;

    public FindAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        super(app, v);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (findDialog == null) {
            findDialog = new org.jhotdraw.samples.teddy.FindDialog(getApplication());
            if (getApplication() instanceof org.jhotdraw.app.OSXApplication) {
                findDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @java.lang.Override
                    public void windowClosing(java.awt.event.WindowEvent evt) {
                        if (findDialog != null) {
                            ((org.jhotdraw.app.OSXApplication) (getApplication())).removePalette(findDialog);
                            findDialog.setVisible(false);
                        }
                    }
                });
            }
        }
        findDialog.setVisible(true);
        if (getApplication() instanceof org.jhotdraw.app.OSXApplication) {
            ((org.jhotdraw.app.OSXApplication) (getApplication())).addPalette(findDialog);
        }
    }
}