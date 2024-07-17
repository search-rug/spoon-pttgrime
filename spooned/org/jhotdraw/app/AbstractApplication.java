/* @(#)AbstractApplication.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.api.app.Application;
import java.util.prefs.*;
/**
 * This abstract class can be extended to implement an {@link Application}.
 *
 * <p>{@code AbstractApplication} supports the command line parameter {@code -open filename} to open
 * views for specific URI's upon launch of the application.
 *
 * <p><hr> <b>Features</b>
 *
 * <p><em>Open last URI on launch</em><br>
 * When the application is started, the last opened URI is opened in a view.<br>
 * The following methods participate in this feature:<br>
 * Data suppliers {@link #addRecentURI}, {@link #getRecentURIs}.<br>
 * Behavior: {@link #start}.<br>
 * See {@link org.jhotdraw.app} for a list of participating classes.
 *
 * <p><em>Allow multiple views for URI</em><br>
 * Allows opening the same URI in multiple views. When the feature is disabled, opening multiple
 * views is prevented, and saving to a file for which a view is currently open is prevented.<br>
 * The following methods participate in this feature:<br>
 * Data suppliers {@link #getViews}. See {@link org.jhotdraw.app} for a list of participating
 * classes.
 */
public abstract class AbstractApplication extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.api.app.Application {
    private static final long serialVersionUID = 1L;

    private java.util.LinkedList<org.jhotdraw.api.app.View> views = new java.util.LinkedList<>();

    private java.util.Collection<org.jhotdraw.api.app.View> unmodifiableViews;

    private boolean isEnabled = true;

    protected org.jhotdraw.util.ResourceBundleUtil labels;

    protected org.jhotdraw.api.app.ApplicationModel model;

    private java.util.prefs.Preferences prefs;

    private org.jhotdraw.api.app.View activeView;

    public static final java.lang.String VIEW_COUNT_PROPERTY = "viewCount";

    private java.util.LinkedList<java.net.URI> recentURIs = new java.util.LinkedList<>();

    private static final int MAX_RECENT_FILES_COUNT = 10;

    private javax.swing.ActionMap actionMap;

    private org.jhotdraw.api.gui.URIChooser openChooser;

    private org.jhotdraw.api.gui.URIChooser saveChooser;

    private org.jhotdraw.api.gui.URIChooser importChooser;

    private org.jhotdraw.api.gui.URIChooser exportChooser;

    public AbstractApplication() {
    }

    /**
     * Initializes the application after it has been configured.
     */
    @java.lang.Override
    public void init() {
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getModel() == null ? getClass() : getModel().getClass());
        int count = prefs.getInt("recentFileCount", 0);
        for (int i = 0; i < count; i++) {
            java.lang.String path = prefs.get("recentFile." + i, null);
            if (path != null) {
                try {
                    recentURIs.add(new java.net.URI(path));
                } catch (java.net.URISyntaxException ex) {
                    // Silently don't add this URI
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void start(java.util.List<java.net.URI> uris) {
        if (uris.isEmpty()) {
            final org.jhotdraw.api.app.View v = createView();
            add(v);
            v.setEnabled(false);
            show(v);
            // Set the start view immediately active, so that
            // ApplicationOpenFileAction picks it up on Mac OS X.
            setActiveView(v);
            new javax.swing.SwingWorker() {
                @java.lang.Override
                protected java.lang.Object doInBackground() throws java.lang.Exception {
                    v.clear();
                    return null;
                }

                @java.lang.Override
                protected void done() {
                    v.setEnabled(true);
                }
            }.execute();
        } else {
            for (final java.net.URI uri : uris) {
                final org.jhotdraw.api.app.View v = createView();
                add(v);
                v.setEnabled(false);
                show(v);
                // Set the start view immediately active, so that
                // ApplicationOpenFileAction picks it up on Mac OS X.
                setActiveView(v);
                new javax.swing.SwingWorker() {
                    @java.lang.Override
                    protected java.lang.Object doInBackground() throws java.lang.Exception {
                        v.read(uri, null);
                        return null;
                    }

                    @java.lang.Override
                    protected void done() {
                        try {
                            get();
                            v.setURI(uri);
                        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                            java.util.logging.Logger.getLogger(org.jhotdraw.app.AbstractApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            v.clear();
                        }
                        v.setEnabled(true);
                    }
                }.execute();
            }
        }
    }

    @java.lang.Override
    public final org.jhotdraw.api.app.View createView() {
        org.jhotdraw.api.app.View v = basicCreateView();
        v.setActionMap(createViewActionMap(v));
        return v;
    }

    @java.lang.Override
    public void setModel(org.jhotdraw.api.app.ApplicationModel newValue) {
        org.jhotdraw.api.app.ApplicationModel oldValue = model;
        model = newValue;
        firePropertyChange("model", oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.api.app.ApplicationModel getModel() {
        return model;
    }

    protected org.jhotdraw.api.app.View basicCreateView() {
        return model.createView();
    }

    /**
     * Sets the active view. Calls deactivate on the previously active view, and then calls activate
     * on the given view.
     *
     * @param newValue
     * 		Active view, can be null.
     */
    public void setActiveView(org.jhotdraw.api.app.View newValue) {
        org.jhotdraw.api.app.View oldValue = activeView;
        if (activeView != null) {
            activeView.deactivate();
        }
        activeView = newValue;
        if (activeView != null) {
            activeView.activate();
        }
        firePropertyChange(org.jhotdraw.api.app.Application.ACTIVE_VIEW_PROPERTY, oldValue, newValue);
    }

    /**
     * Gets the active view.
     *
     * @return The active view can be null.
     */
    @java.lang.Override
    public org.jhotdraw.api.app.View getActiveView() {
        return activeView;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return model.getName();
    }

    @java.lang.Override
    public java.lang.String getVersion() {
        return model.getVersion();
    }

    @java.lang.Override
    public java.lang.String getCopyright() {
        return model.getCopyright();
    }

    @java.lang.Override
    public void stop() {
        for (org.jhotdraw.api.app.View p : new java.util.LinkedList<>(views())) {
            dispose(p);
        }
    }

    @java.lang.Override
    public void destroy() {
        stop();
        model.destroyApplication(this);
        java.lang.System.exit(0);
    }

    @java.lang.Override
    public void remove(org.jhotdraw.api.app.View v) {
        hide(v);
        if (v == getActiveView()) {
            setActiveView(null);
        }
        int oldCount = views.size();
        views.remove(v);
        v.setApplication(null);
        firePropertyChange(org.jhotdraw.app.AbstractApplication.VIEW_COUNT_PROPERTY, oldCount, views.size());
    }

    @java.lang.Override
    public void add(org.jhotdraw.api.app.View v) {
        if (v.getApplication() != this) {
            int oldCount = views.size();
            views.add(v);
            v.setApplication(this);
            v.init();
            model.initView(this, v);
            firePropertyChange(org.jhotdraw.app.AbstractApplication.VIEW_COUNT_PROPERTY, oldCount, views.size());
        }
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.api.app.View> getViews() {
        return java.util.Collections.unmodifiableList(views);
    }

    protected abstract javax.swing.ActionMap createViewActionMap(org.jhotdraw.api.app.View p);

    @java.lang.Override
    public void dispose(org.jhotdraw.api.app.View view) {
        remove(view);
        model.destroyView(this, view);
        view.dispose();
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.api.app.View> views() {
        if (unmodifiableViews == null) {
            unmodifiableViews = java.util.Collections.unmodifiableCollection(views);
        }
        return unmodifiableViews;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        boolean oldValue = isEnabled;
        isEnabled = newValue;
        firePropertyChange("enabled", oldValue, newValue);
    }

    public java.awt.Container createContainer() {
        return new javax.swing.JFrame();
    }

    /**
     * Launches the application.
     *
     * @param args
     * 		This implementation supports the command-line parameter "-open" which can be
     * 		followed by one or more filenames or URI's.
     */
    @java.lang.Override
    public void launch(java.lang.String[] args) {
        configure(args);
        // Get URI's from command line
        final java.util.List<java.net.URI> uris = getOpenURIsFromMainArgs(args);
        javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                init();
                // Call this right after init.
                model.initApplication(AbstractApplication.this);
                // Get start URIs
                final java.util.LinkedList<java.net.URI> startUris;
                if (uris.isEmpty()) {
                    startUris = new java.util.LinkedList<>();
                    if (model.isOpenLastURIOnLaunch() && (!recentURIs.isEmpty())) {
                        startUris.add(recentURIs.getFirst());
                    }
                } else {
                    startUris = new java.util.LinkedList<>(uris);
                }
                // Start with start URIs
                start(startUris);
            }
        });
    }

    /**
     * Parses the arguments to the main method and returns a list of URI's for which views need to be
     * opened upon launch of the application.
     *
     * <p>This implementation supports the command-line parameter "-open" which can be followed by one
     * or more filenames or URI's.
     *
     * <p>This method is invoked from the {@code Application.launch} method.
     *
     * @param args
     * 		Arguments to the main method.
     * @return A list of URI's parsed from the arguments. Returns an empty list if no URI's shall be
    opened.
     */
    protected java.util.List<java.net.URI> getOpenURIsFromMainArgs(java.lang.String[] args) {
        java.util.LinkedList<java.net.URI> uris = new java.util.LinkedList<>();
        for (int i = 0; i < args.length; ++i) {
            if ("-open".equals(args[i])) {
                for (++i; i < args.length; ++i) {
                    if (args[i].startsWith("-")) {
                        break;
                    }
                    java.net.URI uri;
                    uri = new java.io.File(args[i]).toURI();
                    uris.add(uri);
                }
            }
        }
        return uris;
    }

    protected void initLabels() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
    }

    /**
     * Configures the application using the provided arguments array.
     */
    @java.lang.Override
    public void configure(java.lang.String[] args) {
    }

    @java.lang.Override
    public void removePalette(java.awt.Window palette) {
    }

    @java.lang.Override
    public void addPalette(java.awt.Window palette) {
    }

    @java.lang.Override
    public void removeWindow(java.awt.Window window) {
    }

    @java.lang.Override
    public void addWindow(java.awt.Window window, org.jhotdraw.api.app.View p) {
    }

    protected javax.swing.Action getAction(org.jhotdraw.api.app.View view, java.lang.String actionID) {
        return getActionMap(view).get(actionID);
    }

    /**
     * Adds the specified action as a menu item to the supplied menu.
     *
     * @param m
     * 		the menu
     * @param view
     * 		the view
     * @param actionID
     * 		the action id
     */
    protected void addAction(javax.swing.JMenu m, org.jhotdraw.api.app.View view, java.lang.String actionID) {
        addAction(m, getAction(view, actionID));
    }

    /**
     * Adds the specified action as a menu item to the supplied menu.
     *
     * @param m
     * 		the menu
     * @param a
     * 		the action
     */
    protected void addAction(javax.swing.JMenu m, javax.swing.Action a) {
        if (a != null) {
            if (m.getClientProperty("needsSeparator") == java.lang.Boolean.TRUE) {
                m.addSeparator();
                m.putClientProperty("needsSeparator", null);
            }
            javax.swing.JMenuItem mi;
            mi = m.add(a);
            mi.setIcon(null);
            mi.setToolTipText(null);
        }
    }

    /**
     * Adds the specified action as a menu item to the supplied menu.
     *
     * @param m
     * 		the menu
     * @param mi
     * 		the menu item
     */
    protected void addMenuItem(javax.swing.JMenu m, javax.swing.JMenuItem mi) {
        if (mi != null) {
            if (m.getClientProperty("needsSeparator") == java.lang.Boolean.TRUE) {
                m.addSeparator();
                m.putClientProperty("needsSeparator", null);
            }
            m.add(mi);
        }
    }

    /**
     * Adds a separator to the supplied menu. The separator will only be added, if the previous item
     * is not a separator.
     *
     * @param m
     * 		the menu
     */
    protected void maybeAddSeparator(javax.swing.JMenu m) {
        javax.swing.JPopupMenu pm = m.getPopupMenu();
        if ((pm.getComponentCount() > 0) && (!(pm.getComponent(pm.getComponentCount() - 1) instanceof javax.swing.JSeparator))) {
            m.addSeparator();
        }
    }

    protected void removeTrailingSeparators(javax.swing.JMenu m) {
        javax.swing.JPopupMenu pm = m.getPopupMenu();
        for (int i = pm.getComponentCount() - 1; (i > 0) && (pm.getComponent(i) instanceof javax.swing.JSeparator); i--) {
            pm.remove(i);
        }
    }

    @java.lang.Override
    public java.util.List<java.net.URI> getRecentURIs() {
        return java.util.Collections.unmodifiableList(recentURIs);
    }

    @java.lang.Override
    public void clearRecentURIs() {
        @java.lang.SuppressWarnings("unchecked")
        java.util.List<java.net.URI> oldValue = ((java.util.List<java.net.URI>) (recentURIs.clone()));
        recentURIs.clear();
        prefs.putInt("recentFileCount", recentURIs.size());
        firePropertyChange(org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY, java.util.Collections.unmodifiableList(oldValue), java.util.Collections.unmodifiableList(recentURIs));
    }

    @java.lang.Override
    public void addRecentURI(java.net.URI uri) {
        @java.lang.SuppressWarnings("unchecked")
        java.util.List<java.net.URI> oldValue = ((java.util.List<java.net.URI>) (recentURIs.clone()));
        if (recentURIs.contains(uri)) {
            recentURIs.remove(uri);
        }
        recentURIs.addFirst(uri);
        if (recentURIs.size() > org.jhotdraw.app.AbstractApplication.MAX_RECENT_FILES_COUNT) {
            recentURIs.removeLast();
        }
        prefs.putInt("recentFileCount", recentURIs.size());
        int i = 0;
        for (java.net.URI f : recentURIs) {
            prefs.put("recentFile." + i, f.toString());
            i++;
        }
        firePropertyChange(org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY, oldValue, 0);
        firePropertyChange(org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY, java.util.Collections.unmodifiableList(oldValue), java.util.Collections.unmodifiableList(recentURIs));
    }

    protected javax.swing.JMenu createOpenRecentFileMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenuItem mi;
        javax.swing.JMenu m;
        m = new javax.swing.JMenu();
        labels.configureMenu(m, (getAction(view, org.jhotdraw.app.action.file.LoadFileAction.ID) != null) || (getAction(view, org.jhotdraw.app.action.file.LoadDirectoryAction.ID) != null) ? "file.loadRecent" : "file.openRecent");
        m.setIcon(null);
        m.add(getAction(view, org.jhotdraw.app.action.file.ClearRecentFilesMenuAction.ID));
        new org.jhotdraw.app.AbstractApplication.OpenRecentMenuHandler(m, view);
        return m;
    }

    /**
     * Updates the menu items in the "Open Recent" file menu.
     */
    private class OpenRecentMenuHandler implements java.beans.PropertyChangeListener , org.jhotdraw.api.app.Disposable {
        private javax.swing.JMenu openRecentMenu;

        private java.util.LinkedList<javax.swing.Action> openRecentActions = new java.util.LinkedList<>();

        private org.jhotdraw.api.app.View view;

        public OpenRecentMenuHandler(javax.swing.JMenu openRecentMenu, org.jhotdraw.api.app.View view) {
            this.openRecentMenu = openRecentMenu;
            this.view = view;
            if (view != null) {
                view.addDisposable(this);
            }
            updateOpenRecentMenu();
            addPropertyChangeListener(this);
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if (((name == null) && (org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.app.Application.RECENT_URIS_PROPERTY))) {
                updateOpenRecentMenu();
            }
        }

        /**
         * Updates the "File &gt; Open Recent" menu.
         */
        protected void updateOpenRecentMenu() {
            if (openRecentMenu.getItemCount() > 0) {
                javax.swing.JMenuItem clearRecentFilesItem = openRecentMenu.getItem(openRecentMenu.getItemCount() - 1);
                openRecentMenu.remove(openRecentMenu.getItemCount() - 1);
                // Dispose the actions and the menu items that are currently in the menu
                for (javax.swing.Action action : openRecentActions) {
                    if (action instanceof org.jhotdraw.api.app.Disposable) {
                        ((org.jhotdraw.api.app.Disposable) (action)).dispose();
                    }
                }
                openRecentActions.clear();
                openRecentMenu.removeAll();
                // Create new actions and add them to the menu
                if ((getAction(view, org.jhotdraw.app.action.file.LoadFileAction.ID) != null) || (getAction(view, org.jhotdraw.app.action.file.LoadDirectoryAction.ID) != null)) {
                    for (java.net.URI f : getRecentURIs()) {
                        org.jhotdraw.app.action.file.LoadRecentFileAction action = new org.jhotdraw.app.action.file.LoadRecentFileAction(AbstractApplication.this, view, f);
                        openRecentMenu.add(action);
                        openRecentActions.add(action);
                    }
                } else {
                    for (java.net.URI f : getRecentURIs()) {
                        org.jhotdraw.app.action.file.OpenRecentFileAction action = new org.jhotdraw.app.action.file.OpenRecentFileAction(AbstractApplication.this, f);
                        openRecentMenu.add(action);
                        openRecentActions.add(action);
                    }
                }
                if (getRecentURIs().size() > 0) {
                    openRecentMenu.addSeparator();
                }
                // Add a separator and the clear recent files item.
                openRecentMenu.add(clearRecentFilesItem);
            }
        }

        @java.lang.Override
        public void dispose() {
            removePropertyChangeListener(this);
            // Dispose the actions and the menu items that are currently in the menu
            for (javax.swing.Action action : openRecentActions) {
                if (action instanceof org.jhotdraw.api.app.Disposable) {
                    ((org.jhotdraw.api.app.Disposable) (action)).dispose();
                }
            }
            openRecentActions.clear();
        }
    }

    /**
     * Gets an open chooser for the specified view or for the application.
     *
     * <p>If the chooser has an accessory panel, it can access the view using the client property
     * "view" on the component of the chooser. It can access the application using the client property
     * "application" on the chooser.
     *
     * @param v
     * 		The view. Specify null to get a chooser for the application.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser getOpenChooser(org.jhotdraw.api.app.View v) {
        if (v == null) {
            if (openChooser == null) {
                openChooser = model.createOpenChooser(this, null);
                openChooser.getComponent().putClientProperty("application", this);
                java.util.List<java.net.URI> ruris = getRecentURIs();
                if (ruris.size() > 0) {
                    try {
                        openChooser.setSelectedURI(ruris.get(0));
                    } catch (java.lang.IllegalArgumentException e) {
                        // Ignore illegal values in recent URI list.
                    }
                }
            }
            return openChooser;
        } else {
            org.jhotdraw.api.gui.URIChooser chooser = ((org.jhotdraw.api.gui.URIChooser) (v.getComponent().getClientProperty("openChooser")));
            if (chooser == null) {
                chooser = model.createOpenChooser(this, v);
                v.getComponent().putClientProperty("openChooser", chooser);
                chooser.getComponent().putClientProperty("view", v);
                chooser.getComponent().putClientProperty("application", this);
                java.util.List<java.net.URI> ruris = getRecentURIs();
                if (ruris.size() > 0) {
                    try {
                        chooser.setSelectedURI(ruris.get(0));
                    } catch (java.lang.IllegalArgumentException e) {
                        // Ignore illegal values in recent URI list.
                    }
                }
            }
            return chooser;
        }
    }

    /**
     * Gets a save chooser for the specified view or for the application.
     *
     * <p>If the chooser has an accessory panel, it can access the view using the client property
     * "view" on the component of the chooser. It can access the application using the client property
     * "application" on the chooser.
     *
     * @param v
     * 		The view. Specify null to get a chooser for the application.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser getSaveChooser(org.jhotdraw.api.app.View v) {
        if (v == null) {
            if (saveChooser == null) {
                saveChooser = model.createSaveChooser(this, null);
                saveChooser.getComponent().putClientProperty("application", this);
            }
            return saveChooser;
        } else {
            org.jhotdraw.api.gui.URIChooser chooser = ((org.jhotdraw.api.gui.URIChooser) (v.getComponent().getClientProperty("saveChooser")));
            if (chooser == null) {
                chooser = model.createSaveChooser(this, v);
                v.getComponent().putClientProperty("saveChooser", chooser);
                chooser.getComponent().putClientProperty("view", v);
                chooser.getComponent().putClientProperty("application", this);
                try {
                    chooser.setSelectedURI(v.getURI());
                } catch (java.lang.IllegalArgumentException e) {
                    // ignore illegal values
                }
            }
            return chooser;
        }
    }

    /**
     * Gets an import chooser for the specified view or for the application.
     *
     * <p>If the chooser has an accessory panel, it can access the view using the client property
     * "view" on the component of the chooser. It can access the application using the client property
     * "application" on the chooser.
     *
     * @param v
     * 		The view. Specify null to get a chooser for the application.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser getImportChooser(org.jhotdraw.api.app.View v) {
        if (v == null) {
            if (importChooser == null) {
                importChooser = model.createImportChooser(this, null);
                importChooser.getComponent().putClientProperty("application", this);
            }
            return importChooser;
        } else {
            org.jhotdraw.api.gui.URIChooser chooser = ((org.jhotdraw.api.gui.URIChooser) (v.getComponent().getClientProperty("importChooser")));
            if (chooser == null) {
                chooser = model.createImportChooser(this, v);
                v.getComponent().putClientProperty("importChooser", chooser);
                chooser.getComponent().putClientProperty("view", v);
                chooser.getComponent().putClientProperty("application", this);
            }
            return chooser;
        }
    }

    /**
     * Gets an export chooser for the specified view or for the application.
     *
     * <p>If the chooser has an accessory panel, it can access the view using the client property
     * "view" on the component of the chooser. It can access the application using the client property
     * "application" on the chooser.
     *
     * @param v
     * 		The view. Specify null to get a chooser for the application.
     */
    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser getExportChooser(org.jhotdraw.api.app.View v) {
        if (v == null) {
            if (exportChooser == null) {
                exportChooser = model.createExportChooser(this, null);
                exportChooser.getComponent().putClientProperty("application", this);
            }
            return exportChooser;
        } else {
            org.jhotdraw.api.gui.URIChooser chooser = ((org.jhotdraw.api.gui.URIChooser) (v.getComponent().getClientProperty("exportChooser")));
            if (chooser == null) {
                chooser = model.createExportChooser(this, v);
                v.getComponent().putClientProperty("exportChooser", chooser);
                chooser.getComponent().putClientProperty("view", v);
                chooser.getComponent().putClientProperty("application", this);
            }
            return chooser;
        }
    }

    /**
     * Sets the application-wide action map.
     *
     * @param m
     * 		the map
     */
    public void setActionMap(javax.swing.ActionMap m) {
        actionMap = m;
    }

    /**
     * Gets the action map.
     *
     * @return the map
     */
    @java.lang.Override
    public javax.swing.ActionMap getActionMap(org.jhotdraw.api.app.View v) {
        return v == null ? actionMap : v.getActionMap();
    }
}