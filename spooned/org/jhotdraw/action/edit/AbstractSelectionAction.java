/* @(#)AbstractSelectionAction.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
import org.jhotdraw.beans.WeakPropertyChangeListener;
/**
 * {@code AbstractSelectionAction} acts on the selection of a target component.
 *
 * <p>By default, the action is disabled when the target component is disabled or has no selection.
 * If the target component is null, updateEnabled does nothing. You can change this behavior by
 * overriding method {@code updateEnabled()}.
 *
 * <p>This action registers a {@link WeakPropertyChangeListener} on the component.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below work together: <br>
 * Contract: {@link org.jhotdraw.gui.EditableComponent}, {@code JTextComponent}.<br>
 * Client: {@link org.jhotdraw.action.edit.AbstractSelectionAction}, {@link org.jhotdraw.action.edit.DeleteAction}, {@link org.jhotdraw.action.edit.DuplicateAction}, {@link org.jhotdraw.action.edit.SelectAllAction}, {@link org.jhotdraw.action.edit.ClearSelectionAction}.
 * <hr>
 */
public abstract class AbstractSelectionAction extends javax.swing.AbstractAction {
    private static final long serialVersionUID = 1L;

    /**
     * The target of the action or null if the action acts on the currently focused component.
     */
    protected javax.swing.JComponent target;

    /**
     * This variable keeps a strong reference on the property change listener.
     */
    private java.beans.PropertyChangeListener propertyHandler;

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param target
     * 		The target of the action. Specify null for the currently focused component.
     */
    public AbstractSelectionAction(javax.swing.JComponent target) {
        this.target = target;
        if (target != null) {
            // Register with a weak reference on the JComponent.
            propertyHandler = new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    java.lang.String n = evt.getPropertyName();
                    if ("enabled".equals(n)) {
                        updateEnabled();
                    } else if (n.equals(org.jhotdraw.api.gui.EditableComponent.SELECTION_EMPTY_PROPERTY)) {
                        updateEnabled();
                    }
                }
            };
            target.addPropertyChangeListener(new org.jhotdraw.beans.WeakPropertyChangeListener(propertyHandler));
        }
    }

    protected void updateEnabled() {
        if (target instanceof org.jhotdraw.api.gui.EditableComponent) {
            setEnabled(target.isEnabled() && (!((org.jhotdraw.api.gui.EditableComponent) (target)).isSelectionEmpty()));
        } else if (target != null) {
            setEnabled(target.isEnabled());
        }
    }
}