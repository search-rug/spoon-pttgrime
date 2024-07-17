/* @(#)PreferencesUtil.java

Copyright (c) 2005-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util.prefs;
/**
 * {@code PreferencesUtil} provides utility methods for {@code java.util.prefs.Preferences}, and can
 * be used as a proxy when the system preferences are not available due to security restrictions.
 */
public class PreferencesUtil extends java.util.prefs.Preferences {
    private java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();

    private boolean isUserNode;

    private static java.util.HashMap<java.lang.Package, java.util.prefs.Preferences> systemNodes;

    private static java.util.HashMap<java.lang.Package, java.util.prefs.Preferences> userNodes;

    public PreferencesUtil(boolean isUserNode) {
        this.isUserNode = isUserNode;
    }

    @java.lang.Override
    public void put(java.lang.String key, java.lang.String value) {
        map.put(key, value);
    }

    @java.lang.Override
    public java.lang.String get(java.lang.String key, java.lang.String def) {
        return ((java.lang.String) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void remove(java.lang.String key) {
        map.remove(key);
    }

    @java.lang.Override
    public void clear() throws java.util.prefs.BackingStoreException {
        map.clear();
    }

    @java.lang.Override
    public void putInt(java.lang.String key, int value) {
        map.put(key, value);
    }

    @java.lang.Override
    public int getInt(java.lang.String key, int def) {
        return ((java.lang.Integer) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void putLong(java.lang.String key, long value) {
        map.put(key, value);
    }

    @java.lang.Override
    public long getLong(java.lang.String key, long def) {
        return ((java.lang.Long) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void putBoolean(java.lang.String key, boolean value) {
        map.put(key, value);
    }

    @java.lang.Override
    public boolean getBoolean(java.lang.String key, boolean def) {
        return ((java.lang.Boolean) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void putFloat(java.lang.String key, float value) {
        map.put(key, value);
    }

    @java.lang.Override
    public float getFloat(java.lang.String key, float def) {
        return ((java.lang.Float) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void putDouble(java.lang.String key, double value) {
        map.put(key, value);
    }

    @java.lang.Override
    public double getDouble(java.lang.String key, double def) {
        return ((java.lang.Double) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public void putByteArray(java.lang.String key, byte[] value) {
        map.put(key, value);
    }

    @java.lang.Override
    public byte[] getByteArray(java.lang.String key, byte[] def) {
        return ((byte[]) (map.containsKey(key) ? map.get(key) : def));
    }

    @java.lang.Override
    public java.lang.String[] keys() throws java.util.prefs.BackingStoreException {
        return map.keySet().toArray(new java.lang.String[map.keySet().size()]);
    }

    @java.lang.Override
    public java.lang.String[] childrenNames() throws java.util.prefs.BackingStoreException {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    @java.lang.Override
    public java.util.prefs.Preferences parent() {
        return null;
    }

    @java.lang.Override
    public java.util.prefs.Preferences node(java.lang.String pathName) {
        return null;
    }

    @java.lang.Override
    public boolean nodeExists(java.lang.String pathName) throws java.util.prefs.BackingStoreException {
        return false;
    }

    @java.lang.Override
    public void removeNode() throws java.util.prefs.BackingStoreException {
        // empty
    }

    @java.lang.Override
    public java.lang.String name() {
        return "Dummy";
    }

    @java.lang.Override
    public java.lang.String absolutePath() {
        return "Dummy";
    }

    @java.lang.Override
    public boolean isUserNode() {
        return isUserNode;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Dummy";
    }

    @java.lang.Override
    public void flush() throws java.util.prefs.BackingStoreException {
        clear();
    }

    @java.lang.Override
    public void sync() throws java.util.prefs.BackingStoreException {
    }

    @java.lang.Override
    public void addPreferenceChangeListener(java.util.prefs.PreferenceChangeListener pcl) {
    }

    @java.lang.Override
    public void removePreferenceChangeListener(java.util.prefs.PreferenceChangeListener pcl) {
    }

    @java.lang.Override
    public void addNodeChangeListener(java.util.prefs.NodeChangeListener ncl) {
    }

    @java.lang.Override
    public void removeNodeChangeListener(java.util.prefs.NodeChangeListener ncl) {
    }

    @java.lang.Override
    public void exportNode(java.io.OutputStream os) throws java.io.IOException, java.util.prefs.BackingStoreException {
    }

    @java.lang.Override
    public void exportSubtree(java.io.OutputStream os) throws java.io.IOException, java.util.prefs.BackingStoreException {
    }

    /**
     * Gets the system node for the package of the class if permitted, gets a proxy otherwise.
     *
     * @return system node or a proxy.
     */
    public static java.util.prefs.Preferences systemNodeForPackage(java.lang.Class<?> c) {
        if (org.jhotdraw.util.prefs.PreferencesUtil.systemNodes != null) {
            if (!org.jhotdraw.util.prefs.PreferencesUtil.systemNodes.containsKey(c.getPackage())) {
                org.jhotdraw.util.prefs.PreferencesUtil.systemNodes.put(c.getPackage(), new org.jhotdraw.util.prefs.PreferencesUtil(false));
            }
            return org.jhotdraw.util.prefs.PreferencesUtil.systemNodes.get(c.getPackage());
        }
        try {
            return java.util.prefs.Preferences.systemNodeForPackage(c);
        } catch (java.lang.Throwable t) {
            if (org.jhotdraw.util.prefs.PreferencesUtil.systemNodes == null) {
                org.jhotdraw.util.prefs.PreferencesUtil.systemNodes = new java.util.HashMap<>();
            }
            return org.jhotdraw.util.prefs.PreferencesUtil.systemNodeForPackage(c);
        }
    }

    /**
     * Gets the user node for the package of the class if permitted, gets a proxy otherwise.
     *
     * @return user node or a proxy.
     */
    public static java.util.prefs.Preferences userNodeForPackage(java.lang.Class<?> c) {
        if (org.jhotdraw.util.prefs.PreferencesUtil.userNodes != null) {
            if (!org.jhotdraw.util.prefs.PreferencesUtil.userNodes.containsKey(c.getPackage())) {
                org.jhotdraw.util.prefs.PreferencesUtil.userNodes.put(c.getPackage(), new org.jhotdraw.util.prefs.PreferencesUtil(false));
            }
            return org.jhotdraw.util.prefs.PreferencesUtil.userNodes.get(c.getPackage());
        }
        try {
            return java.util.prefs.Preferences.userNodeForPackage(c);
        } catch (java.lang.Throwable t) {
            if (org.jhotdraw.util.prefs.PreferencesUtil.userNodes == null) {
                org.jhotdraw.util.prefs.PreferencesUtil.userNodes = new java.util.HashMap<>();
            }
            return org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(c);
        }
    }

    public static void installPrefsHandler(java.util.prefs.Preferences prefs, java.lang.String string, javax.swing.JTabbedPane tabbedPane) {
        throw new java.lang.UnsupportedOperationException("Not yet implemented");
    }

    private PreferencesUtil() {
    }

    /**
     * Installs a frame preferences handler. On first run, sets the window to its preferred size at
     * the top left corner of the screen. On subsequent runs, sets the window the last size and
     * location where the user had placed it before.
     *
     * <p>If no preferences are stored yet for this window, a default size of 400 x 300 pixels is
     * used.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param window
     * 		The window for which to track preferences.
     */
    public static void installFramePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, java.awt.Window window) {
        org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, name, window, new java.awt.Dimension(400, 300));
    }

    /**
     * Installs a frame preferences handler. On first run, sets the window to its preferred size at
     * the top left corner of the screen. On subsequent runs, sets the window the last size and
     * location where the user had placed it before.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param window
     * 		The window for which to track preferences.
     * @param defaultSize
     * 		This size is used when no prefences are stored yet for this window.
     */
    public static void installFramePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, java.awt.Window window, java.awt.Dimension defaultSize) {
        java.awt.GraphicsConfiguration conf = window.getGraphicsConfiguration();
        java.awt.Rectangle screenBounds = conf.getBounds();
        java.awt.Insets screenInsets = window.getToolkit().getScreenInsets(conf);
        screenBounds.x += screenInsets.left;
        screenBounds.y += screenInsets.top;
        screenBounds.width -= screenInsets.left + screenInsets.right;
        screenBounds.height -= screenInsets.top + screenInsets.bottom;
        window.pack();
        java.awt.Dimension preferredSize = window.getPreferredSize();
        boolean resizable = true;
        if (window instanceof java.awt.Frame) {
            resizable = ((java.awt.Frame) (window)).isResizable();
        } else if (window instanceof java.awt.Dialog) {
            resizable = ((java.awt.Dialog) (window)).isResizable();
        }
        java.awt.Rectangle bounds;
        if (resizable) {
            bounds = new java.awt.Rectangle(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0), java.lang.Math.max(defaultSize.width, prefs.getInt(name + ".width", preferredSize.width)), java.lang.Math.max(defaultSize.height, prefs.getInt(name + ".height", preferredSize.height)));
        } else {
            bounds = new java.awt.Rectangle(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0), window.getWidth(), window.getHeight());
        }
        if (!screenBounds.contains(bounds)) {
            bounds.x = screenBounds.x + ((screenBounds.width - bounds.width) / 2);
            bounds.y = screenBounds.y + ((screenBounds.height - bounds.height) / 3);
            java.awt.Rectangle.intersect(screenBounds, bounds, bounds);
        }
        window.setBounds(bounds);
        window.addComponentListener(new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                prefs.putInt(name + ".x", evt.getComponent().getX());
                prefs.putInt(name + ".y", evt.getComponent().getY());
            }

            @java.lang.Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                prefs.putInt(name + ".width", evt.getComponent().getWidth());
                prefs.putInt(name + ".height", evt.getComponent().getHeight());
            }
        });
    }

    /**
     * Installs a palette preferences handler. On first run, sets the palette to its preferred
     * location at the top left corner of the screen. On subsequent runs, sets the palette the last
     * location where the user had placed it before.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param window
     * 		The window for which to track preferences.
     */
    public static void installPalettePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, java.awt.Window window) {
        org.jhotdraw.util.prefs.PreferencesUtil.installPalettePrefsHandler(prefs, name, window, 0);
    }

    public static void installPalettePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, java.awt.Window window, int x) {
        java.awt.GraphicsConfiguration conf = window.getGraphicsConfiguration();
        java.awt.Rectangle screenBounds = conf.getBounds();
        java.awt.Insets screenInsets = window.getToolkit().getScreenInsets(conf);
        screenBounds.x += screenInsets.left;
        screenBounds.y += screenInsets.top;
        screenBounds.width -= screenInsets.left + screenInsets.right;
        screenBounds.height -= screenInsets.top + screenInsets.bottom;
        java.awt.Dimension preferredSize = window.getPreferredSize();
        java.awt.Rectangle bounds = new java.awt.Rectangle(prefs.getInt(name + ".x", x + screenBounds.x), prefs.getInt(name + ".y", 0 + screenBounds.y), preferredSize.width, preferredSize.height);
        if (!screenBounds.contains(bounds)) {
            bounds.x = screenBounds.x;
            bounds.y = screenBounds.y;
        }
        window.setBounds(bounds);
        window.addComponentListener(new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                prefs.putInt(name + ".x", evt.getComponent().getX());
                prefs.putInt(name + ".y", evt.getComponent().getY());
            }
        });
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                prefs.putBoolean(name + ".visible", false);
            }

            @java.lang.Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                prefs.putBoolean(name + ".visible", true);
            }
        });
    }

    /**
     * Installs an intenal frame preferences handler. On first run, sets the frame to its preferred
     * size at the top left corner of the desktop pane. On subsequent runs, sets the frame the last
     * size and location where the user had placed it before.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param window
     * 		The window for which to track preferences.
     */
    public static void installInternalFramePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, javax.swing.JInternalFrame window, javax.swing.JDesktopPane desktop) {
        java.awt.Rectangle screenBounds = desktop.getBounds();
        screenBounds.setLocation(0, 0);
        java.awt.Insets screenInsets = desktop.getInsets();
        screenBounds.x += screenInsets.left;
        screenBounds.y += screenInsets.top;
        screenBounds.width -= screenInsets.left + screenInsets.right;
        screenBounds.height -= screenInsets.top + screenInsets.bottom;
        java.awt.Dimension preferredSize = window.getPreferredSize();
        java.awt.Dimension minSize = window.getMinimumSize();
        java.awt.Rectangle bounds = new java.awt.Rectangle(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0), java.lang.Math.max(minSize.width, prefs.getInt(name + ".width", preferredSize.width)), java.lang.Math.max(minSize.height, prefs.getInt(name + ".height", preferredSize.height)));
        if (!screenBounds.contains(bounds)) {
            bounds.x = screenBounds.x + ((screenBounds.width - bounds.width) / 2);
            bounds.y = screenBounds.y + ((screenBounds.height - bounds.height) / 2);
            java.awt.Rectangle.intersect(screenBounds, bounds, bounds);
        }
        window.setBounds(bounds);
        window.addComponentListener(new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                prefs.putInt(name + ".x", evt.getComponent().getX());
                prefs.putInt(name + ".y", evt.getComponent().getY());
            }

            @java.lang.Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                prefs.putInt(name + ".width", evt.getComponent().getWidth());
                prefs.putInt(name + ".height", evt.getComponent().getHeight());
            }
        });
    }

    /**
     * Installs a toolbar preferences handler. On first run, sets the toolbar to BorderLayout.TOP. On
     * subsequent runs, set the toolbar to the last BorderLayout location.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param toolbar
     * 		The JToolBar for which to track preferences.
     */
    public static void installToolBarPrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, javax.swing.JToolBar toolbar) {
        new org.jhotdraw.util.prefs.ToolBarPrefsHandler(toolbar, name, prefs);
    }

    /**
     * Installs a JTabbedPane preferences handler. On first run, sets the JTabbedPane to its preferred
     * tab.
     *
     * @param prefs
     * 		Preferences for storing/retrieving preferences values.
     * @param name
     * 		Base name of the preference.
     * @param tabbedPane
     * 		The JTabbedPane for which to track preferences.
     */
    public static void installTabbedPanePrefsHandler(final java.util.prefs.Preferences prefs, final java.lang.String name, final javax.swing.JTabbedPane tabbedPane) {
        int selectedTab = prefs.getInt(name, 0);
        try {
            tabbedPane.setSelectedIndex(selectedTab);
        } catch (java.lang.IndexOutOfBoundsException e) {
            // empty allowed
        }
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            @java.lang.Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                prefs.putInt(name, tabbedPane.getSelectedIndex());
            }
        });
    }
}