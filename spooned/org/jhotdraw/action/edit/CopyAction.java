/* @(#)CopyAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
/**
 * Copies the selected region and place its contents into the system clipboard.
 *
 * <p>This action acts on the last {@link org.jhotdraw.gui.EditableComponent} / {@code JTextComponent} which had the focus when the {@code ActionEvent} was generated.
 *
 * <p>This action is called when the user selects the Copy item in the Edit menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 */
public class CopyAction extends org.jhotdraw.action.edit.AbstractSelectionAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.copy";

    /**
     * Creates a new instance which acts on the currently focused component.
     */
    public CopyAction() {
        this(null);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param target
     * 		The target of the action. Specify null for the currently focused component.
     */
    public CopyAction(javax.swing.JComponent target) {
        super(target);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.edit.CopyAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JComponent c = target;
        if ((c == null) && (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() instanceof javax.swing.JComponent)) {
            c = ((javax.swing.JComponent) (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner()));
        }
        // Note: copying is allowed for disabled components
        if (c != null) {
            c.getTransferHandler().exportToClipboard(c, org.jhotdraw.datatransfer.ClipboardUtil.getClipboard(), javax.swing.TransferHandler.COPY);
        }
    }
}