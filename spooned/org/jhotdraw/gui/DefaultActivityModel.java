/* @(#)DefaultActivityModel.java

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui;
import java.beans.PropertyChangeListener;
import org.jhotdraw.api.gui.ActivityModel;
import org.jhotdraw.beans.WeakPropertyChangeListener;
/**
 * Default implementation of {@link ActivityModel}.
 */
public class DefaultActivityModel extends javax.swing.DefaultBoundedRangeModel implements org.jhotdraw.api.gui.ActivityModel {
    private static final long serialVersionUID = 1L;

    private boolean canceled;

    private boolean closed;

    private boolean cancelable = true;

    private java.lang.Runnable doCancel;

    private final java.lang.String title;

    private java.lang.String note;

    private boolean isIndeterminate;

    private java.lang.String warning;

    private java.lang.String error;

    private java.util.Formatter formatter;

    private final java.lang.Object owner;

    protected java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Creates a new DefaultActivityModel.
     */
    public DefaultActivityModel(java.lang.Object owner, java.lang.String title, java.lang.String note, boolean isIndeterminate) {
        this(owner, title, note, 0, 100, isIndeterminate);
    }

    /**
     * Creates a new DefaultActivityModel.
     */
    public DefaultActivityModel(java.lang.Object owner, java.lang.String title, java.lang.String note, int min, int max) {
        this(owner, title, note, min, max, false);
    }

    /**
     * Creates a new DefaultActivityModel.
     */
    public DefaultActivityModel(java.lang.Object owner, java.lang.String title, java.lang.String note, int min, final int max, final boolean isIndeterminate) {
        super(min, 0, min, max);
        this.owner = owner;
        this.title = title;
        this.note = note;
        this.isIndeterminate = isIndeterminate;
        org.jhotdraw.gui.ActivityManager.getInstance().add(this);
    }

    /**
     * Creates a new indeterminate DefaultActivityModel.
     */
    public DefaultActivityModel(java.lang.Object owner, java.lang.String title) {
        this(owner, title, null, 0, 100, true);
    }

    /**
     * Set cancelable to false if the operation can not be canceled.
     */
    @java.lang.Override
    public void setCancelable(boolean newValue) {
        boolean oldValue = cancelable;
        cancelable = newValue;
        firePropertyChange(org.jhotdraw.api.gui.ActivityModel.CANCELABLE_PROPERTY, oldValue, newValue);
    }

    /**
     * The specified Runnable is executed when the user presses the cancel button.
     */
    @java.lang.Override
    public void setDoCancel(java.lang.Runnable doCancel) {
        this.doCancel = doCancel;
    }

    /**
     * Indicate that the operation is closed.
     */
    @java.lang.Override
    public void close() {
        if (!closed) {
            closed = true;
            firePropertyChange(org.jhotdraw.api.gui.ActivityModel.CLOSED_PROPERTY, false, true);
            org.jhotdraw.gui.ActivityManager.getInstance().remove(this);
        }
    }

    /**
     * Closes the progress view.
     */
    /**
     * Returns true if the user has hit the Cancel button in the progress dialog.
     */
    @java.lang.Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Returns true if the operation is completed.
     */
    @java.lang.Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * Cancels the operation. This method must be invoked from the user event dispatch thread.
     */
    @java.lang.Override
    public void cancel() {
        if (cancelable && (!canceled)) {
            canceled = true;
            firePropertyChange(org.jhotdraw.api.gui.ActivityModel.CANCELED_PROPERTY, false, true);
            if (doCancel != null) {
                doCancel.run();
            }
        }
    }

    /**
     * Specifies the additional note that is displayed along with the progress message. Used, for
     * example, to show which file the is currently being copied during a multiple-file copy.
     *
     * @param newValue
     * 		a String specifying the note to display
     * @see #getNote
     */
    @java.lang.Override
    public void setNote(java.lang.String newValue) {
        java.lang.String oldValue = note;
        this.note = newValue;
        firePropertyChange(org.jhotdraw.api.gui.ActivityModel.NOTE_PROPERTY, oldValue, newValue);
    }

    /**
     * Specifies the additional note that is displayed along with the progress message.
     *
     * @return a String specifying the note to display
     * @see #setNote
     */
    @java.lang.Override
    public java.lang.String getNote() {
        return note;
    }

    @java.lang.Override
    public void setWarning(java.lang.String newValue) {
        java.lang.String oldValue = warning;
        this.warning = newValue;
        firePropertyChange(org.jhotdraw.api.gui.ActivityModel.WARNING_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public java.lang.String getWarning() {
        return warning;
    }

    @java.lang.Override
    public void setError(java.lang.String newValue) {
        java.lang.String oldValue = error;
        this.error = newValue;
        firePropertyChange(org.jhotdraw.api.gui.ActivityModel.ERROR_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public java.lang.String getError() {
        return error;
    }

    @java.lang.Override
    public void setIndeterminate(boolean newValue) {
        boolean oldValue = isIndeterminate;
        isIndeterminate = newValue;
        firePropertyChange(org.jhotdraw.api.gui.ActivityModel.INDETERMINATE_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    @java.lang.Override
    public void printf(java.lang.String format, java.lang.Object... args) {
        if ((formatter == null) || (formatter.locale() != java.util.Locale.getDefault())) {
            formatter = new java.util.Formatter();
        }
        formatter.format(java.util.Locale.getDefault(), format, args);
        java.lang.StringBuilder buf = ((java.lang.StringBuilder) (formatter.out()));
        setNote(buf.toString());
    }

    @java.lang.Override
    public java.lang.Object getOwner() {
        return owner;
    }

    @java.lang.Override
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Adds a {@code PropertyChangeListener} which can optionally be wrapped into a {@code WeakPropertyChangeListener}.
     *
     * @param listener
     */
    @java.lang.Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Adds a {@code PropertyChangeListener} which can optionally be wrapped into a {@code WeakPropertyChangeListener}.
     *
     * @param listener
     */
    public void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a {@code PropertyChangeListener}. If the listener was added wrapped into a {@code WeakPropertyChangeListener}, the {@code WeakPropertyChangeListener} is removed.
     *
     * @param listener
     */
    @java.lang.Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        // Removes a property change listener from our list.
        // We need a somewhat complex procedure here in case a listener
        // has been registered using addPropertyChangeListener(new
        // WeakPropertyChangeListener(listener));
        for (java.beans.PropertyChangeListener l : propertySupport.getPropertyChangeListeners()) {
            if (l == listener) {
                propertySupport.removePropertyChangeListener(l);
                break;
            }
            if (l instanceof org.jhotdraw.beans.WeakPropertyChangeListener) {
                org.jhotdraw.beans.WeakPropertyChangeListener wl = ((org.jhotdraw.beans.WeakPropertyChangeListener) (l));
                java.beans.PropertyChangeListener target = wl.getTarget();
                if (target == listener) {
                    propertySupport.removePropertyChangeListener(l);
                    break;
                }
            }
        }
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

    @java.lang.Override
    public boolean isCancelable() {
        return cancelable;
    }
}