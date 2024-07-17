/* @(#)BeansBinding.java

Copyright (c) 2013 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.beans;
/**
 * Can bind a property of a JavaBean to a property of another JavaBean.
 *
 * <p>The binding can be unidirectional or bidirectional.
 *
 * @author Werner Randelshofer
 * @version 1.0 2013-06-13 Created.
 */
public class BeansBinding {
    private java.lang.String sourceProperty;

    private java.lang.String targetProperty;

    private java.lang.Object source;

    private java.lang.Object target;

    private boolean bidirectional;

    private java.lang.reflect.Method targetWriteMethod;

    private java.lang.reflect.Method sourceWriteMethod;

    private java.lang.reflect.Method sourceReadMethod;

    private class Handler implements java.beans.PropertyChangeListener {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == source) {
                if (sourceProperty.equals(evt.getPropertyName())) {
                    if (target != null) {
                        try {
                            getTargetWriteMethod().invoke(target, evt.getNewValue());
                        } catch (java.lang.Exception ex) {
                            java.lang.InternalError ie = new java.lang.InternalError((("Could not set property \"" + targetProperty) + "\" on ") + target);
                            ie.initCause(ex);
                            throw ie;
                        }
                    }
                }
            } else if (bidirectional && (evt.getSource() == target)) {
                if (targetProperty.equals(evt.getPropertyName())) {
                    if (source != null) {
                        try {
                            getSourceWriteMethod().invoke(source, evt.getNewValue());
                        } catch (java.lang.Exception ex) {
                            java.lang.InternalError ie = new java.lang.InternalError((("Could not set property \"" + targetProperty) + "\" on ") + target);
                            ie.initCause(ex);
                            throw ie;
                        }
                    }
                }
            }
        }
    }

    private org.jhotdraw.beans.BeansBinding.Handler handler = new org.jhotdraw.beans.BeansBinding.Handler();

    /**
     * Creates a bidirectional binding from a source bean to a target bean. Updates the value of the
     * target bean.
     *
     * @param source
     * 		The source bean.
     * @param sourceProperty
     * 		The name of the source property.
     * @param target
     * 		The target bean.
     * @param targetProperty
     * 		The name of the target property.
     */
    public void bind(java.lang.Object source, java.lang.String sourceProperty, java.lang.Object target, java.lang.String targetProperty) {
        setSource(source, sourceProperty);
        setTarget(target, targetProperty);
        bidirectional = true;
        updateTarget();
    }

    /**
     * Removes the binding.
     */
    public void unbind() {
        setSource(null, sourceProperty);
        setTarget(null, targetProperty);
    }

    private void addPropertyChangeListener(java.lang.Object bean, java.beans.PropertyChangeListener listener) {
        try {
            java.lang.reflect.Method m = bean.getClass().getMethod("addPropertyChangeListener", java.beans.PropertyChangeListener.class);
            m.invoke(bean, listener);
        } catch (java.lang.Exception ex) {
            java.lang.InternalError ie = new java.lang.InternalError("Could not add property change listener to " + bean);
            ie.initCause(ex);
            throw ie;
        }
    }

    private void removePropertyChangeListener(java.lang.Object bean, java.beans.PropertyChangeListener listener) {
        try {
            java.lang.reflect.Method m = bean.getClass().getMethod("removePropertyChangeListener", java.beans.PropertyChangeListener.class);
            m.invoke(bean, listener);
        } catch (java.lang.Exception ex) {
            java.lang.InternalError ie = new java.lang.InternalError("Could not remove property change listener from " + bean);
            ie.initCause(ex);
            throw ie;
        }
    }

    /**
     * Sets the source bean.
     *
     * @param source
     * @param sourceProperty
     */
    private void setSource(java.lang.Object source, java.lang.String sourceProperty) {
        if (this.source != null) {
            removePropertyChangeListener(this.source, handler);
        }
        this.source = source;
        this.sourceProperty = sourceProperty;
        sourceWriteMethod = null;
        sourceReadMethod = null;
        if (this.source != null) {
            addPropertyChangeListener(this.source, handler);
        }
    }

    private void setTarget(java.lang.Object target, java.lang.String targetProperty) {
        if (this.target != null) {
            removePropertyChangeListener(this.target, handler);
        }
        this.target = target;
        this.targetProperty = targetProperty;
        targetWriteMethod = null;
        if (this.target != null) {
            addPropertyChangeListener(this.target, handler);
        }
    }

    public void updateTarget() {
        try {
            java.lang.Object value = getSourceReadMethod().invoke(source);
            getTargetWriteMethod().invoke(target, value);
        } catch (java.lang.Exception ex) {
            java.lang.InternalError ie = new java.lang.InternalError("Could not update target from source.");
            ie.initCause(ex);
            throw ie;
        }
    }

    private java.lang.reflect.Method getTargetWriteMethod() {
        if (targetWriteMethod == null) {
            try {
                java.beans.PropertyDescriptor pd = new java.beans.PropertyDescriptor(targetProperty, target.getClass());
                targetWriteMethod = pd.getWriteMethod();
            } catch (java.beans.IntrospectionException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Could not create target property descriptor for " + target);
                ie.initCause(ex);
                throw ie;
            }
        }
        return targetWriteMethod;
    }

    private java.lang.reflect.Method getSourceWriteMethod() {
        if (sourceWriteMethod == null) {
            try {
                java.beans.PropertyDescriptor pd = new java.beans.PropertyDescriptor(sourceProperty, source.getClass());
                sourceWriteMethod = pd.getWriteMethod();
            } catch (java.beans.IntrospectionException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Could not create source property descriptor for " + source);
                ie.initCause(ex);
                throw ie;
            }
        }
        return sourceWriteMethod;
    }

    private java.lang.reflect.Method getSourceReadMethod() {
        if (sourceReadMethod == null) {
            try {
                java.beans.PropertyDescriptor pd = new java.beans.PropertyDescriptor(sourceProperty, source.getClass());
                sourceReadMethod = pd.getReadMethod();
            } catch (java.beans.IntrospectionException ex) {
                java.lang.InternalError ie = new java.lang.InternalError("Could not create source property descriptor for " + source);
                ie.initCause(ex);
                throw ie;
            }
        }
        return sourceReadMethod;
    }
}