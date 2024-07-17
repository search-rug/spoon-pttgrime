/* @(#)JAttributeTextFieldBeanInfo.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.gui;
/**
 * BeanInfo for JAttributeTextField.
 */
public class JAttributeTextFieldBeanInfo extends java.beans.SimpleBeanInfo {
    // Bean descriptor information will be obtained from introspection.//GEN-FIRST:BeanDescriptor
    private static java.beans.BeanDescriptor beanDescriptor = null;

    private static java.beans.BeanDescriptor getBdescriptor() {
        // GEN-HEADEREND:BeanDescriptor
        // Here you can add code for customizing the BeanDescriptor.
        if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.beanDescriptor == null) {
            org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.beanDescriptor = new java.beans.BeanDescriptor(org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.class);
        }
        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.beanDescriptor.setValue("isContainer", java.lang.Boolean.FALSE);
        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.beanDescriptor.setDisplayName("JAttributeTextField");
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.beanDescriptor;
    }// GEN-LAST:BeanDescriptor


    // Properties information will be obtained from introspection.//GEN-FIRST:Properties
    private static java.beans.PropertyDescriptor[] properties = null;

    private static java.beans.PropertyDescriptor[] getPdescriptor() {
        // GEN-HEADEREND:Properties
        // Here you can add code for customizing the properties array.
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.properties;
    }// GEN-LAST:Properties


    // Event set information will be obtained from introspection.//GEN-FIRST:Events
    private static java.beans.EventSetDescriptor[] eventSets = null;

    private static java.beans.EventSetDescriptor[] getEdescriptor() {
        // GEN-HEADEREND:Events
        // Here you can add code for customizing the event sets array.
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.eventSets;
    }// GEN-LAST:Events


    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static java.beans.MethodDescriptor[] methods = null;

    private static java.beans.MethodDescriptor[] getMdescriptor() {
        // GEN-HEADEREND:Methods
        // Here you can add code for customizing the methods array.
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.methods;
    }// GEN-LAST:Methods


    private static java.awt.Image iconColor16 = null;// GEN-BEGIN:IconsDef


    private static java.awt.Image iconColor32 = null;

    private static java.awt.Image iconMono16 = null;

    private static java.awt.Image iconMono32 = null;// GEN-END:IconsDef


    private static java.lang.String iconNameC16 = null;// GEN-BEGIN:Icons


    private static java.lang.String iconNameC32 = null;

    private static java.lang.String iconNameM16 = null;

    private static java.lang.String iconNameM32 = null;// GEN-END:Icons


    private static int defaultPropertyIndex = -1;// GEN-BEGIN:Idx


    private static int defaultEventIndex = -1;// GEN-END:Idx


    // GEN-FIRST:Superclass
    // Here you can add code for customizing the Superclass BeanInfo.
    // GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable properties of this bean. May return null if the
    information should be obtained by automatic analysis.
     */
    @java.lang.Override
    public java.beans.BeanDescriptor getBeanDescriptor() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable properties supported by this
    bean. May return null if the information should be obtained by automatic analysis.
    <p>If a property is indexed, then its entry in the result array will belong to the
    IndexedPropertyDescriptor subclass of PropertyDescriptor. A client of
    getPropertyDescriptors can use "instanceof" to check if a given PropertyDescriptor is an
    IndexedPropertyDescriptor.
     */
    @java.lang.Override
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return An array of EventSetDescriptors describing the kinds of events fired by this bean. May
    return null if the information should be obtained by automatic analysis.
     */
    @java.lang.Override
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return An array of MethodDescriptors describing the methods implemented by this bean. May
    return null if the information should be obtained by automatic analysis.
     */
    @java.lang.Override
    public java.beans.MethodDescriptor[] getMethodDescriptors() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will mostly commonly be
     * initially chosen for update by human's who are customizing the bean.
     *
     * @return Index of default property in the PropertyDescriptor array returned by
    getPropertyDescriptors.
    <p>Returns -1 if there is no default property.
     */
    @java.lang.Override
    public int getDefaultPropertyIndex() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will mostly commonly be used by
     * human's when using the bean.
     *
     * @return Index of default event in the EventSetDescriptor array returned by
    getEventSetDescriptors.
    <p>Returns -1 if there is no default event.
     */
    @java.lang.Override
    public int getDefaultEventIndex() {
        return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to represent the bean in toolboxes,
     * toolbars, etc. Icon images will typically be GIFs, but may in future include other formats.
     *
     * <p>Beans aren't required to provide icons and may return null from this method.
     *
     * <p>There are four possible flavors of icons (16x16 color, 32x32 color, 16x16 mono, 32x32 mono).
     * If a bean choses to only support a single icon we recommend supporting 16x16 color.
     *
     * <p>We recommend that icons have a "transparent" background so they can be rendered onto an
     * existing background.
     *
     * @param iconKind
     * 		The kind of icon requested. This should be one of the constant values
     * 		ICON_COLOR_16x16, ICON_COLOR_32x32, ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return An image object representing the requested icon. May return null if no suitable icon is
    available.
     */
    @java.lang.Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case java.beans.BeanInfo.ICON_COLOR_16x16 :
                if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameC16 == null) {
                    return null;
                } else {
                    if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor16 == null) {
                        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor16 = loadImage(org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameC16);
                    }
                    return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor16;
                }
            case java.beans.BeanInfo.ICON_COLOR_32x32 :
                if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameC32 == null) {
                    return null;
                } else {
                    if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor32 == null) {
                        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor32 = loadImage(org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameC32);
                    }
                    return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconColor32;
                }
            case java.beans.BeanInfo.ICON_MONO_16x16 :
                if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameM16 == null) {
                    return null;
                } else {
                    if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono16 == null) {
                        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono16 = loadImage(org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameM16);
                    }
                    return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono16;
                }
            case java.beans.BeanInfo.ICON_MONO_32x32 :
                if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameM32 == null) {
                    return null;
                } else {
                    if (org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono32 == null) {
                        org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono32 = loadImage(org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconNameM32);
                    }
                    return org.jhotdraw.draw.gui.JAttributeTextFieldBeanInfo.iconMono32;
                }
            default :
                return null;
        }
    }
}