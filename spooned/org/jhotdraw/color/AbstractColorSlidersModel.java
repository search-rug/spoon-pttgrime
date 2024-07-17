/**
 *
 * @(#)AbstractColorSlidersModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * AbstractColorSlidersModel.
 */
public abstract class AbstractColorSlidersModel extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.color.ColorSliderModel {
    private static final long serialVersionUID = 1L;

    /**
     * ChangeListener's listening to changes in this model.
     */
    protected java.util.List<javax.swing.event.ChangeListener> listeners;

    @java.lang.Override
    public void addChangeListener(javax.swing.event.ChangeListener l) {
        if (listeners == null) {
            listeners = new java.util.ArrayList<>();
        }
        listeners.add(l);
    }

    @java.lang.Override
    public void removeChangeListener(javax.swing.event.ChangeListener l) {
        listeners.remove(l);
    }

    public void fireStateChanged() {
        if (listeners != null) {
            javax.swing.event.ChangeEvent event = new javax.swing.event.ChangeEvent(this);
            for (javax.swing.event.ChangeListener l : listeners) {
                l.stateChanged(event);
            }
        }
    }
}