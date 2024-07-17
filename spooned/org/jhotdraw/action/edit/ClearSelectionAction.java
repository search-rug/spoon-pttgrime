/* @(#)ClearSelectionAction.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
import javax.swing.text.*;
/**
 * Clears (de-selects) the selected region.
 *
 * <p>This action acts on the last {@link org.jhotdraw.gui.EditableComponent} / {@code JTextComponent} which had the focus when the {@code ActionEvent} was generated.
 *
 * <p>This action is called when the user selects the Clear Selection item in the Edit menu. The
 * menu item is automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below work together: <br>
 * Contract: {@link org.jhotdraw.gui.EditableComponent}, {@code JTextComponent}.<br>
 * Client: {@link org.jhotdraw.action.edit.AbstractSelectionAction}, {@link org.jhotdraw.action.edit.DeleteAction}, {@link org.jhotdraw.action.edit.DuplicateAction}, {@link org.jhotdraw.action.edit.SelectAllAction}, {@link org.jhotdraw.action.edit.ClearSelectionAction}.
 * <hr>
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ClearSelectionAction extends org.jhotdraw.action.edit.AbstractSelectionAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.clearSelection";

    /**
     * Creates a new instance which acts on the currently focused component.
     */
    public ClearSelectionAction() {
        this(null);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param target
     * 		The target of the action. Specify null for the currently focused component.
     */
    public ClearSelectionAction(javax.swing.JComponent target) {
        super(target);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.edit.ClearSelectionAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JComponent c = target;
        if ((c == null) && (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() instanceof javax.swing.JComponent)) {
            c = ((javax.swing.JComponent) (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner()));
        }
        if ((c != null) && c.isEnabled()) {
            if (c instanceof org.jhotdraw.api.gui.EditableComponent) {
                ((org.jhotdraw.api.gui.EditableComponent) (c)).clearSelection();
            } else if (c instanceof javax.swing.text.JTextComponent) {
                javax.swing.text.JTextComponent tc = ((javax.swing.text.JTextComponent) (c));
                tc.select(tc.getSelectionStart(), tc.getSelectionStart());
            } else {
                c.getToolkit().beep();
            }
        }
    }

    @java.lang.Override
    protected void updateEnabled() {
        if (target != null) {
            setEnabled(target.isEnabled());
        }
    }
}