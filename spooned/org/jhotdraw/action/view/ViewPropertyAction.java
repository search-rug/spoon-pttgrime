/* @(#)ViewPropertyAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.view;
/**
 * ViewPropertyAction.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ViewPropertyAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    private java.lang.String propertyName;

    private java.lang.Class<?>[] parameterClass;

    private java.lang.Object propertyValue;

    private java.lang.String setterName;

    private java.lang.String getterName;

    private java.beans.PropertyChangeListener viewListener = new java.beans.PropertyChangeListener() {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (propertyName.equals(evt.getPropertyName())) {
                // Strings get interned
                updateSelectedState();
            }
        }
    };

    public ViewPropertyAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, java.lang.String propertyName, java.lang.Object propertyValue) {
        this(app, view, propertyName, propertyValue.getClass(), propertyValue);
    }

    public ViewPropertyAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view, java.lang.String propertyName, java.lang.Class<?> propertyClass, java.lang.Object propertyValue) {
        super(app, view);
        this.propertyName = propertyName;
        this.parameterClass = new java.lang.Class<?>[]{ propertyClass };
        this.propertyValue = propertyValue;
        setterName = ("set" + java.lang.Character.toUpperCase(propertyName.charAt(0))) + propertyName.substring(1);
        getterName = (((propertyClass == java.lang.Boolean.TYPE) || (propertyClass == java.lang.Boolean.class) ? "is" : "get") + java.lang.Character.toUpperCase(propertyName.charAt(0))) + propertyName.substring(1);
        updateSelectedState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.api.app.View p = getActiveView();
        try {
            p.getClass().getMethod(setterName, parameterClass).invoke(p, new java.lang.Object[]{ propertyValue });
        } catch (java.lang.Throwable e) {
            java.lang.InternalError error = new java.lang.InternalError((("Method invocation failed. setter:" + setterName) + " object:") + p);
            error.initCause(e);
            throw error;
        }
    }

    @java.lang.Override
    protected void installViewListeners(org.jhotdraw.api.app.View p) {
        super.installViewListeners(p);
        p.addPropertyChangeListener(viewListener);
        updateSelectedState();
    }

    /**
     * Installs listeners on the view object.
     */
    @java.lang.Override
    protected void uninstallViewListeners(org.jhotdraw.api.app.View p) {
        super.uninstallViewListeners(p);
        p.removePropertyChangeListener(viewListener);
    }

    private void updateSelectedState() {
        boolean isSelected = false;
        org.jhotdraw.api.app.View p = getActiveView();
        if (p != null) {
            try {
                java.lang.Object value = p.getClass().getMethod(getterName, ((java.lang.Class[]) (null))).invoke(p);
                isSelected = (value == propertyValue) || (((value != null) && (propertyValue != null)) && value.equals(propertyValue));
            } catch (java.lang.Throwable e) {
                java.lang.InternalError error = new java.lang.InternalError((("Method invocation failed. getter:" + getterName) + " object:") + p);
                error.initCause(e);
                throw error;
            }
        }
        putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, isSelected);
    }
}