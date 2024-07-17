/* @(#)AbstractApplicationModel.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * This abstract class can be extended to implement an {@link ApplicationModel}.
 */
public abstract class AbstractApplicationModel extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.api.app.ApplicationModel {
    private static final long serialVersionUID = 1L;

    protected java.lang.String name;

    protected java.lang.String version;

    protected java.lang.String copyright;

    protected java.lang.Class<?> viewClass;

    protected java.lang.String viewClassName;

    protected boolean allowMultipleViewsForURI = true;

    protected boolean openLastURIOnLaunch = false;

    public static final java.lang.String NAME_PROPERTY = "name";

    public static final java.lang.String VERSION_PROPERTY = "version";

    public static final java.lang.String COPYRIGHT_PROPERTY = "copyright";

    public static final java.lang.String VIEW_CLASS_NAME_PROPERTY = "viewClassName";

    public static final java.lang.String VIEW_CLASS_PROPERTY = "viewClass";

    public AbstractApplicationModel() {
    }

    public void setName(java.lang.String newValue) {
        java.lang.String oldValue = name;
        name = newValue;
        firePropertyChange(org.jhotdraw.app.AbstractApplicationModel.NAME_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return name;
    }

    public void setVersion(java.lang.String newValue) {
        java.lang.String oldValue = version;
        version = newValue;
        firePropertyChange(org.jhotdraw.app.AbstractApplicationModel.VERSION_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public java.lang.String getVersion() {
        return version;
    }

    public void setCopyright(java.lang.String newValue) {
        java.lang.String oldValue = copyright;
        copyright = newValue;
        firePropertyChange(org.jhotdraw.app.AbstractApplicationModel.COPYRIGHT_PROPERTY, oldValue, newValue);
    }

    @java.lang.Override
    public java.lang.String getCopyright() {
        return copyright;
    }

    /**
     * Use this method for best application startup performance.
     *
     * @param newValue
     * 		the class name
     */
    public void setViewClassName(java.lang.String newValue) {
        java.lang.String oldValue = viewClassName;
        viewClassName = newValue;
        firePropertyChange(org.jhotdraw.app.AbstractApplicationModel.VIEW_CLASS_NAME_PROPERTY, oldValue, newValue);
    }

    /**
     * Use this method only, if setViewClassName() does not suit you.
     *
     * @param newValue
     * 		the class
     */
    public void setViewClass(java.lang.Class<?> newValue) {
        java.lang.Class<?> oldValue = viewClass;
        viewClass = newValue;
        firePropertyChange(org.jhotdraw.app.AbstractApplicationModel.VIEW_CLASS_PROPERTY, oldValue, newValue);
    }

    public java.lang.Class<?> getViewClass() {
        if (viewClass == null) {
            if (viewClassName != null) {
                try {
                    viewClass = java.lang.Class.forName(viewClassName);
                } catch (java.lang.Exception e) {
                    java.lang.InternalError error = new java.lang.InternalError("unable to get view class");
                    error.initCause(e);
                    throw error;
                }
            }
        }
        return viewClass;
    }

    @java.lang.Override
    public org.jhotdraw.api.app.View createView() {
        try {
            return ((org.jhotdraw.api.app.View) (getViewClass().newInstance()));
        } catch (java.lang.Exception e) {
            java.lang.InternalError error = new java.lang.InternalError("unable to create view");
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Creates toolbars for the application.
     */
    @java.lang.Override
    public abstract java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View p);

    /**
     * This method is empty.
     */
    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View p) {
    }

    /**
     * This method is empty.
     */
    @java.lang.Override
    public void destroyView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View p) {
    }

    /**
     * This method is empty.
     */
    @java.lang.Override
    public void initApplication(org.jhotdraw.api.app.Application a) {
    }

    /**
     * This method is empty.
     */
    @java.lang.Override
    public void destroyApplication(org.jhotdraw.api.app.Application a) {
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.api.gui.URIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenDirectoryChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        c.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createSaveChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        return c;
    }

    /**
     * Returns createOpenChooser.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createImportChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        return createOpenChooser(a, v);
    }

    /**
     * Returns createSaveChooser.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createExportChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        return createSaveChooser(a, v);
    }

    /**
     * {@inheritDoc } The default value is true.
     */
    @java.lang.Override
    public boolean isOpenLastURIOnLaunch() {
        return openLastURIOnLaunch;
    }

    /**
     * {@inheritDoc } The default value is true.
     */
    @java.lang.Override
    public boolean isAllowMultipleViewsPerURI() {
        return allowMultipleViewsForURI;
    }

    /**
     * Whether the application may open multiple views for the same URI.
     *
     * <p>The default value is true.
     *
     * @param allowMultipleViewsForURI
     * 		the value
     */
    public void setAllowMultipleViewsForURI(boolean allowMultipleViewsForURI) {
        this.allowMultipleViewsForURI = allowMultipleViewsForURI;
    }

    /**
     * Whether the application should open the last opened URI on launch.
     *
     * <p>The default value is false.
     *
     * @param openLastURIOnLaunch
     */
    public void setOpenLastURIOnLaunch(boolean openLastURIOnLaunch) {
        this.openLastURIOnLaunch = openLastURIOnLaunch;
    }
}