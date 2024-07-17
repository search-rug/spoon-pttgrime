/* @(#)AbstractBean.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.beans;
/**
 * This abstract class can be extended to implement a JavaBean.
 *
 * <p>Implements the methods required for adding and removing property change listeners.
 */
public class AbstractBean extends java.lang.Object implements java.io.Serializable , java.lang.Cloneable {
    private static final long serialVersionUID = 1L;

    protected java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Adds a {@code PropertyChangeListener} which can optionally be wrapped into a {@code WeakPropertyChangeListener}.
     *
     * @param listener
     * 		the listener
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Adds a {@code PropertyChangeListener} which can optionally be wrapped into a {@code WeakPropertyChangeListener}.
     *
     * @param propertyName
     * 		the property name
     * @param listener
     * 		the listener
     */
    public void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a {@code PropertyChangeListener}. If the listener was added wrapped into a {@code WeakPropertyChangeListener}, the {@code WeakPropertyChangeListener} is removed.
     *
     * @param listener
     * 		the listener
     */
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

    /**
     * Removes a {@code PropertyChangeListener}. If the listener was added wrapped into a {@code WeakPropertyChangeListener}, the {@code WeakPropertyChangeListener} is removed.
     *
     * @param propertyName
     * 		the property name
     * @param listener
     * 		the listener
     */
    public void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        // Removes a property change listener from our list.
        // We need a somewhat complex procedure here in case a listener
        // has been registered using addPropertyChangeListener(propertyName, new
        // WeakPropertyChangeListener(listener));
        for (java.beans.PropertyChangeListener l : propertySupport.getPropertyChangeListeners(propertyName)) {
            if (l == listener) {
                propertySupport.removePropertyChangeListener(propertyName, l);
                break;
            }
            if (l instanceof org.jhotdraw.beans.WeakPropertyChangeListener) {
                org.jhotdraw.beans.WeakPropertyChangeListener wl = ((org.jhotdraw.beans.WeakPropertyChangeListener) (l));
                java.beans.PropertyChangeListener target = wl.getTarget();
                if (target == listener) {
                    propertySupport.removePropertyChangeListener(propertyName, l);
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
    public org.jhotdraw.beans.AbstractBean clone() {
        org.jhotdraw.beans.AbstractBean that;
        try {
            that = ((org.jhotdraw.beans.AbstractBean) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new java.lang.InternalError("Clone failed", ex);
        }
        that.propertySupport = new java.beans.PropertyChangeSupport(that);
        return that;
    }

    public java.beans.PropertyChangeListener[] getPropertyChangeListeners() {
        return propertySupport.getPropertyChangeListeners();
    }
}