/* @(#)UndoRedoManager.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.undo;
/**
 * Same as javax.swing.UndoManager but provides actions for undo and redo operations.
 */
public class UndoRedoManager extends javax.swing.undo.UndoManager {
    // javax.swing.undo.UndoManager {
    private static final long serialVersionUID = 1L;

    protected java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);

    /**
     * The resource bundle used for internationalisation.
     */
    private static org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * This flag is set to true when at least one significant UndoableEdit has been added to the
     * manager since the last call to discardAllEdits.
     */
    private boolean hasSignificantEdits = false;

    /**
     * This flag is set to true when an undo or redo operation is in progress. The UndoRedoManager
     * ignores all incoming UndoableEdit events while this flag is true.
     */
    private boolean undoOrRedoInProgress;

    /**
     * Sending this UndoableEdit event to the UndoRedoManager disables the Undo and Redo functions of
     * the manager.
     */
    public static final javax.swing.undo.UndoableEdit DISCARD_ALL_EDITS = new javax.swing.undo.AbstractUndoableEdit() {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public boolean canUndo() {
            return false;
        }

        @java.lang.Override
        public boolean canRedo() {
            return false;
        }
    };

    /**
     * Undo Action for use in a menu bar.
     */
    private class UndoAction extends javax.swing.AbstractAction {
        private static final long serialVersionUID = 1L;

        public UndoAction() {
            org.jhotdraw.undo.UndoRedoManager.labels.configureAction(this, "edit.undo");
            setEnabled(false);
        }

        /**
         * Invoked when an action occurs.
         */
        @java.lang.Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                undo();
            } catch (javax.swing.undo.CannotUndoException e) {
                java.lang.System.err.println("Cannot undo: " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Redo Action for use in a menu bar.
     */
    private class RedoAction extends javax.swing.AbstractAction {
        private static final long serialVersionUID = 1L;

        public RedoAction() {
            org.jhotdraw.undo.UndoRedoManager.labels.configureAction(this, "edit.redo");
            setEnabled(false);
        }

        /**
         * Invoked when an action occurs.
         */
        @java.lang.Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                redo();
            } catch (javax.swing.undo.CannotRedoException e) {
                java.lang.System.out.println("Cannot redo: " + e);
            }
        }
    }

    /**
     * The undo action instance.
     */
    private org.jhotdraw.undo.UndoRedoManager.UndoAction undoAction;

    /**
     * The redo action instance.
     */
    private org.jhotdraw.undo.UndoRedoManager.RedoAction redoAction;

    public static org.jhotdraw.util.ResourceBundleUtil getLabels() {
        if (org.jhotdraw.undo.UndoRedoManager.labels == null) {
            org.jhotdraw.undo.UndoRedoManager.labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.undo.Labels");
        }
        return org.jhotdraw.undo.UndoRedoManager.labels;
    }

    /**
     * Creates new UndoRedoManager
     */
    public UndoRedoManager() {
        org.jhotdraw.undo.UndoRedoManager.getLabels();
        undoAction = new org.jhotdraw.undo.UndoRedoManager.UndoAction();
        redoAction = new org.jhotdraw.undo.UndoRedoManager.RedoAction();
    }

    public void setLocale(java.util.Locale l) {
        org.jhotdraw.undo.UndoRedoManager.labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.undo.Labels", l);
    }

    /**
     * Discards all edits.
     */
    @java.lang.Override
    public void discardAllEdits() {
        super.discardAllEdits();
        updateActions();
        setHasSignificantEdits(false);
    }

    public void setHasSignificantEdits(boolean newValue) {
        boolean oldValue = hasSignificantEdits;
        hasSignificantEdits = newValue;
        firePropertyChange("hasSignificantEdits", oldValue, newValue);
    }

    /**
     * Returns true if at least one significant UndoableEdit has been added since the last call to
     * discardAllEdits.
     */
    public boolean hasSignificantEdits() {
        return hasSignificantEdits;
    }

    /**
     * If inProgress, inserts anEdit at indexOfNextAdd, and removes any old edits that were at
     * indexOfNextAdd or later. The die method is called on each edit that is removed is sent, in the
     * reverse of the order the edits were added. Updates indexOfNextAdd.
     *
     * <p>If not inProgress, acts as a CompoundEdit
     *
     * <p>Regardless of inProgress, if undoOrRedoInProgress, calls die on each edit that is sent.
     *
     * @see CompoundEdit#end
     * @see CompoundEdit#addEdit
     */
    @java.lang.Override
    public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
        if (undoOrRedoInProgress) {
            anEdit.die();
            return true;
        }
        boolean success = super.addEdit(anEdit);
        updateActions();
        if ((success && anEdit.isSignificant()) && (editToBeUndone() == anEdit)) {
            setHasSignificantEdits(true);
        }
        return success;
    }

    /**
     * Gets the undo action for use as an Undo menu item.
     */
    public javax.swing.Action getUndoAction() {
        return undoAction;
    }

    /**
     * Gets the redo action for use as a Redo menu item.
     */
    public javax.swing.Action getRedoAction() {
        return redoAction;
    }

    /**
     * Updates the properties of the UndoAction and of the RedoAction.
     */
    private void updateActions() {
        java.lang.String label;
        org.jhotdraw.undo.UndoRedoManager.LOG.fine((((((("UndoRedoManager@" + hashCode()) + ".updateActions ") + editToBeUndone()) + " canUndo=") + canUndo()) + " canRedo=") + canRedo());
        if (canUndo()) {
            undoAction.setEnabled(true);
            label = getUndoPresentationName();
        } else {
            undoAction.setEnabled(false);
            label = org.jhotdraw.undo.UndoRedoManager.labels.getString("edit.undo.text");
        }
        undoAction.putValue(javax.swing.Action.NAME, label);
        undoAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, label);
        if (canRedo()) {
            redoAction.setEnabled(true);
            label = getRedoPresentationName();
        } else {
            redoAction.setEnabled(false);
            label = org.jhotdraw.undo.UndoRedoManager.labels.getString("edit.redo.text");
        }
        redoAction.putValue(javax.swing.Action.NAME, label);
        redoAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, label);
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.undo.UndoRedoManager.class.getName());

    /**
     * Undoes the last edit event. The UndoRedoManager ignores all incoming UndoableEdit events, while
     * undo is in progress.
     */
    @java.lang.Override
    public void undo() throws javax.swing.undo.CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.undo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    /**
     * Redoes the last undone edit event. The UndoRedoManager ignores all incoming UndoableEdit
     * events, while redo is in progress.
     */
    @java.lang.Override
    public void redo() throws javax.swing.undo.CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.redo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    /**
     * Undoes or redoes the last edit event. The UndoRedoManager ignores all incoming UndoableEdit
     * events, while undo or redo is in progress.
     */
    @java.lang.Override
    public void undoOrRedo() throws javax.swing.undo.CannotUndoException, javax.swing.undo.CannotRedoException {
        undoOrRedoInProgress = true;
        try {
            super.undoOrRedo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(java.lang.String propertyName, int oldValue, int newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}