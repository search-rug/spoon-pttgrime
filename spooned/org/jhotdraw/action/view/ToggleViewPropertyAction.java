/* @(#)ProjectPropertyAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.view;
/**
 * ToggleViewPropertyAction.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ToggleViewPropertyAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    private final java.lang.String propertyName;

    private java.lang.Class<?>[] parameterClass;

    private java.lang.Object selectedPropertyValue;

    private java.lang.Object deselectedPropertyValue;

    private final java.lang.String setterName;

    private final java.lang.String getterName;

    private java.beans.PropertyChangeListener viewListener = new java.beans.PropertyChangeListener() {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (propertyName.equals(evt.getPropertyName())) {
                // Strings get interned
                updateView();
            }
        }
    };

    public ToggleViewPropertyAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, java.lang.String propertyName) {
        this(app, view, propertyName, java.lang.Boolean.TYPE, true, false);
    }

    public ToggleViewPropertyAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, java.lang.String propertyName, java.lang.Class<?> propertyClass, java.lang.Object selectedPropertyValue, java.lang.Object deselectedPropertyValue) {
        super(app, view);
        if (propertyName == null) {
            throw new java.lang.IllegalArgumentException("Parameter propertyName must not be null");
        }
        this.propertyName = propertyName;
        this.parameterClass = new java.lang.Class<?>[]{ propertyClass };
        this.selectedPropertyValue = selectedPropertyValue;
        this.deselectedPropertyValue = deselectedPropertyValue;
        setterName = ("set" + java.lang.Character.toUpperCase(propertyName.charAt(0))) + propertyName.substring(1);
        getterName = (((propertyClass == java.lang.Boolean.TYPE) || (propertyClass == java.lang.Boolean.class) ? "is" : "get") + java.lang.Character.toUpperCase(propertyName.charAt(0))) + propertyName.substring(1);
        updateView();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.api.app.View p = getActiveView();
        java.lang.Object value = getCurrentValue();
        java.lang.Object newValue = ((value == selectedPropertyValue) || (((value != null) && (selectedPropertyValue != null)) && value.equals(selectedPropertyValue))) ? deselectedPropertyValue : selectedPropertyValue;
        try {
            p.getClass().getMethod(setterName, parameterClass).invoke(p, new java.lang.Object[]{ newValue });
        } catch (java.lang.Throwable e) {
            java.lang.InternalError error = new java.lang.InternalError((("No " + setterName) + " method on ") + p);
            error.initCause(e);
            throw error;
        }
    }

    private java.lang.Object getCurrentValue() {
        org.jhotdraw.api.app.View p = getActiveView();
        if (p != null) {
            try {
                return p.getClass().getMethod(getterName, ((java.lang.Class[]) (null))).invoke(p);
            } catch (java.lang.Throwable e) {
                java.lang.InternalError error = new java.lang.InternalError((("No " + getterName) + " method on ") + p);
                error.initCause(e);
                throw error;
            }
        }
        return null;
    }

    @java.lang.Override
    protected void installViewListeners(org.jhotdraw.api.app.View p) {
        super.installViewListeners(p);
        p.addPropertyChangeListener(viewListener);
        updateView();
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void uninstallViewListeners(org.jhotdraw.api.app.View p) {
        super.uninstallViewListeners(p);
        p.removePropertyChangeListener(viewListener);
    }

    @java.lang.Override
    protected void updateView() {
        if (getterName == null) {
            // This happens, when updateView is called before the constructor
            // has been completed.
            return;
        }
        boolean isSelected = false;
        org.jhotdraw.api.app.View p = getActiveView();
        if (p != null) {
            try {
                java.lang.Object value = p.getClass().getMethod(getterName, ((java.lang.Class[]) (null))).invoke(p);
                isSelected = (value == selectedPropertyValue) || (((value != null) && (selectedPropertyValue != null)) && value.equals(selectedPropertyValue));
            } catch (java.lang.Throwable e) {
                java.lang.InternalError error = new java.lang.InternalError((((("No " + getterName) + " method on ") + p) + " for property ") + propertyName);
                error.initCause(e);
                throw error;
            }
        }
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, isSelected);
    }
}