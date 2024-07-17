/* @(#)ClearRecentFilesMenuAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Clears (empties) the Recent Files sub-menu in the File menu.
 *
 * <p>This action is called when the user selects the Clear Recent Files item in the Recent Files
 * sub-menu of the File menu. The action and the menu item is automatically created by the
 * application, when the {@code ApplicationModel} provides a {@code LoadFileAction} or a {@code OpenFileAction}.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ClearRecentFilesMenuAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.clearRecentFiles";

    private java.beans.PropertyChangeListener applicationListener;

    public ClearRecentFilesMenuAction(org.jhotdraw.api.app.Application app) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.ClearRecentFilesMenuAction.ID);
        updateEnabled();
    }

    /**
     * Installs listeners on the application object.
     */
    @java.lang.Override
    protected void installApplicationListeners(org.jhotdraw.api.app.Application app) {
        super.installApplicationListeners(app);
        if (applicationListener == null) {
            applicationListener = createApplicationListener();
        }
        app.addPropertyChangeListener(applicationListener);
    }

    private java.beans.PropertyChangeListener createApplicationListener() {
        return new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (((evt.getPropertyName() == null) && (org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY))) {
                    // Strings get interned
                    updateEnabled();
                }
            }
        };
    }

    /**
     * Installs listeners on the application object.
     */
    @java.lang.Override
    protected void uninstallApplicationListeners(org.jhotdraw.api.app.Application app) {
        super.uninstallApplicationListeners(app);
        app.removePropertyChangeListener(applicationListener);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        getApplication().clearRecentURIs();
    }

    private void updateEnabled() {
        setEnabled(getApplication().getRecentURIs().size() > 0);
    }
}