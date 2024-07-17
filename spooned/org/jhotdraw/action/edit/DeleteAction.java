/* @(#)DeleteAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
import javax.swing.text.*;
/**
 * Deletes the region at (or after) the caret position.
 *
 * <p>This action acts on the last {@link org.jhotdraw.gui.EditableComponent} / {@code JTextComponent} which had the focus when the {@code ActionEvent} was generated.
 *
 * <p>This action is called when the user selects the Delete item in the Edit menu. The menu item is
 * automatically created by the application.
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
 */
public class DeleteAction extends javax.swing.text.TextAction {
    private static final long serialVersionUID = 1L;

    /**
     * The ID for this action.
     */
    public static final java.lang.String ID = "edit.delete";

    /**
     * The target of the action or null if the action acts on the currently focused component.
     */
    private javax.swing.JComponent target;

    /**
     * This variable keeps a strong reference on the property change listener.
     */
    private java.beans.PropertyChangeListener propertyHandler;

    /**
     * Creates a new instance which acts on the currently focused component.
     */
    public DeleteAction() {
        this(null, org.jhotdraw.action.edit.DeleteAction.ID);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param target
     * 		The target of the action. Specify null for the currently focused component.
     */
    public DeleteAction(javax.swing.JComponent target) {
        this(target, org.jhotdraw.action.edit.DeleteAction.ID);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param target
     * 		The target of the action. Specify null for the currently focused component.
     */
    protected DeleteAction(javax.swing.JComponent target, java.lang.String id) {
        super(id);
        this.target = target;
        if (target != null) {
            // Register with a weak reference on the JComponent.
            propertyHandler = new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    if ("enabled".equals(evt.getPropertyName())) {
                        setEnabled(((java.lang.Boolean) (evt.getNewValue())));
                    }
                }
            };
            target.addPropertyChangeListener(new org.jhotdraw.beans.WeakPropertyChangeListener(propertyHandler));
        }
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.edit.DeleteAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JComponent c = target;
        if ((c == null) && (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() instanceof javax.swing.JComponent)) {
            c = ((javax.swing.JComponent) (java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner()));
        }
        if ((c != null) && c.isEnabled()) {
            if (c instanceof org.jhotdraw.api.gui.EditableComponent) {
                ((org.jhotdraw.api.gui.EditableComponent) (c)).delete();
            } else {
                deleteNextChar(evt);
            }
        }
    }

    /**
     * This method was copied from DefaultEditorKit.DeleteNextCharAction.actionPerformed(ActionEvent).
     */
    public void deleteNextChar(java.awt.event.ActionEvent e) {
        javax.swing.text.JTextComponent c = getTextComponent(e);
        boolean beep = true;
        if ((c != null) && c.isEditable()) {
            try {
                javax.swing.text.Document doc = c.getDocument();
                javax.swing.text.Caret caret = c.getCaret();
                int dot = caret.getDot();
                int mark = caret.getMark();
                if (dot != mark) {
                    doc.remove(java.lang.Math.min(dot, mark), java.lang.Math.abs(dot - mark));
                    beep = false;
                } else if (dot < doc.getLength()) {
                    doc.remove(dot, 1);
                    beep = false;
                }
            } catch (javax.swing.text.BadLocationException bl) {
                // allowed empty
            }
        }
        if (beep) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
}