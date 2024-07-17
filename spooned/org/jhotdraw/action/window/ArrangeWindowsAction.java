/* @(#)ArrangeWindowsAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.window;
import org.jhotdraw.api.gui.Arrangeable;
/**
 * Changes the arrangement of an {@link Arrangeable} object.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>FIXME - Register as PropertyChangeListener on Arrangeable.
 */
public class ArrangeWindowsAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String VERTICAL_ID = "window.arrangeVertical";

    public static final java.lang.String HORIZONTAL_ID = "window.arrangeHorizontal";

    public static final java.lang.String CASCADE_ID = "window.arrangeCascade";

    private org.jhotdraw.api.gui.Arrangeable arrangeable;

    private org.jhotdraw.api.gui.Arrangeable.Arrangement arrangement;

    public ArrangeWindowsAction(org.jhotdraw.api.gui.Arrangeable arrangeable, org.jhotdraw.api.gui.Arrangeable.Arrangement arrangement) {
        this.arrangeable = arrangeable;
        this.arrangement = arrangement;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        java.lang.String labelID;
        switch (arrangement) {
            case VERTICAL :
                labelID = org.jhotdraw.action.window.ArrangeWindowsAction.VERTICAL_ID;
                break;
            case HORIZONTAL :
                labelID = org.jhotdraw.action.window.ArrangeWindowsAction.HORIZONTAL_ID;
                break;
            case CASCADE :
            default :
                labelID = org.jhotdraw.action.window.ArrangeWindowsAction.CASCADE_ID;
                break;
        }
        labels.configureAction(this, labelID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        arrangeable.setArrangement(arrangement);
    }
}