/* @(#)OSXApplication.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.action.window.FocusWindowAction;
import org.jhotdraw.api.app.ApplicationModel;
import org.jhotdraw.api.app.View;
import org.jhotdraw.app.action.window.TogglePaletteAction;
/**
 * {@code OSXApplication} handles the lifecycle of multiple {@link View}s using a Mac OS X
 * application interface.
 *
 * <p>This user interface created by this application follows the guidelines given in the <a
 * href="http://developer.apple.com/mac/library/documentation/UserExperience/Conceptual/AppleHIGuidelines/"
 * >Apple Human Interface Guidelines</a>.
 *
 * <p>An application of this type can open multiple {@link View}s. Each view is shown in a separate
 * {@code JFrame}.
 *
 * <p>Conceptually all views share a global 'screen menu bar'. In Swing this is implemented as
 * multiple JMenuBar instances. There is one JMenuBar for each opened JFrame, and a special JMenuBar
 * which is shown when all views of the application are closed.
 *
 * <p>The application also provides floating toolbars and palette windows for the views.
 *
 * <p>In order for the screen menu bar and the floating palettes to function properly, it is
 * essential that all code which opens JFrame's, JDialog's or JWindow's calls addWindow/Palette and
 * removeWindow/Palette on the application object.
 *
 * <p>The life cycle of the application is tied to the screen menu bar. Choosing the quit action in
 * the screen menu bar quits the application.
 *
 * <p>The screen menu bar has the following standard menus:
 *
 * <pre>
 * "Application-Name" &nbsp; File &nbsp; Edit &nbsp; Window</pre>
 *
 * The first menu, is the <b>application menu</b>. It has the following standard menu items:
 *
 * <pre>
 *  About "Application-Name" ({@link AboutAction#ID})
 *  -
 *  Preferences... ({@link AbstractPreferencesAction#ID})
 *  -
 *  Services
 *  -
 *  Hide "Application-Name"
 *  Hide Others
 *  Show All
 *  -
 *  Quit "Application-Name" ({@link ExitAction#ID})
 * </pre>
 *
 * The <b>file menu</b> has the following standard menu items:
 *
 * <pre>
 *  Clear ({@link ClearFileAction#ID}})
 *  New ({@link NewFileAction#ID}})
 *  New Window ({@link NewWindowAction#ID}})
 *  Load... ({@link LoadFileAction#ID}})
 *  Open... ({@link OpenFileAction#ID}})
 *  Load Directory... ({@link LoadDirectoryAction#ID}})
 *  Open Directory... ({@link OpenDirectoryAction#ID}})
 *  Load Recent &gt; "Filename" ({@link org.jhotdraw.app.action.file.LoadRecentFileAction#ID})
 *  Open Recent &gt; "Filename" ({@link org.jhotdraw.app.action.file.OpenRecentFileAction#ID})
 *  -
 *  Close ({@link CloseFileAction#ID})
 *  Save ({@link SaveFileAction#ID})
 *  Save As... ({@link SaveFileAsAction#ID})
 *  Export... ({@link ExportFileAction#ID})
 *  Print... ({@link PrintFileAction#ID})
 * </pre>
 *
 * The <b>edit menu</b> has the following standard menu items:
 *
 * <pre>
 *  Undo ({@link UndoAction#ID}})
 *  Redo ({@link RedoAction#ID}})
 *  -
 *  Cut ({@link CutAction#ID}})
 *  Copy ({@link CopyAction#ID}})
 *  Paste ({@link PasteAction#ID}})
 *  Duplicate ({@link DuplicateAction#ID}})
 *  Delete... ({@link DeleteAction#ID}})
 *  -
 *  Select All ({@link SelectAllAction#ID}})
 *  Clear Selection ({@link ClearSelectionAction#ID}})
 *  -
 *  Find ({@link AbstractFindAction#ID}})
 * </pre>
 *
 * The <b>window menu</b> has the following standard menu items:
 *
 * <pre>
 *  Minimize ({@link MinimizeWindowAction#ID})
 *  Zoom ({@link MaximizeWindowAction#ID})
 *  -
 *  "Filename" ({@link FocusWindowAction})
 *  -
 *  "Palette" ({@link TogglePaletteAction})
 * </pre>
 *
 * The menus provided by the {@code ApplicationModel} are inserted between the file menu and the
 * window menu. In case the application model supplies a menu with the title "Help", it is inserted
 * after the window menu.
 */
public class OSXApplication extends org.jhotdraw.app.AbstractApplication {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.app.osx.OSXPaletteHandler paletteHandler;

    private java.util.prefs.Preferences prefs;

    private java.util.LinkedList<javax.swing.Action> paletteActions;

    /**
     * The "invisible" frame is used to hold the frameless menu bar on Mac OS X.
     */
    private javax.swing.JFrame invisibleFrame;

    public OSXApplication() {
    }

    @java.lang.Override
    public void init() {
        super.init();
        org.jhotdraw.util.ResourceBundleUtil.putPropertyNameModifier("os", "mac", "default");
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getModel() == null ? getClass() : getModel().getClass());
        initLookAndFeel();
        paletteHandler = new org.jhotdraw.app.osx.OSXPaletteHandler(this);
        initLabels();
        paletteActions = new java.util.LinkedList<>();
        setActionMap(createModelActionMap(model));
        initPalettes(paletteActions);
        initScreenMenuBar();
    }

    @java.lang.Override
    public void launch(java.lang.String[] args) {
        java.lang.System.setProperty("apple.awt.graphics.UseQuartz", "false");
        super.launch(args);
    }

    @java.lang.Override
    public void configure(java.lang.String[] args) {
        java.lang.System.setProperty("apple.laf.useScreenMenuBar", "true");
        java.lang.System.setProperty("com.apple.macos.useScreenMenuBar", "true");
    }

    protected void initLookAndFeel() {
        try {
            java.lang.String lafName = ((java.lang.String) (org.jhotdraw.util.Methods.invokeStatic("ch.randelshofer.quaqua.QuaquaManager", "getLookAndFeelClassName")));
            javax.swing.UIManager.setLookAndFeel(lafName);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        if (javax.swing.UIManager.getString("OptionPane.css") == null) {
            javax.swing.UIManager.put("OptionPane.css", (((("<head>" + "<style type=\"text/css\">") + "b { font: 13pt \"Dialog\" }") + "p { font: 11pt \"Dialog\"; margin-top: 8px }") + "</style>") + "</head>");
        }
    }

    @java.lang.Override
    public void dispose(org.jhotdraw.api.app.View p) {
        org.jhotdraw.action.window.FocusWindowAction a = ((org.jhotdraw.action.window.FocusWindowAction) (getAction(p, org.jhotdraw.action.window.FocusWindowAction.ID)));
        if (a != null) {
            a.dispose();
        }
        super.dispose(p);
    }

    @java.lang.Override
    public void addPalette(java.awt.Window palette) {
        paletteHandler.addPalette(palette);
    }

    @java.lang.Override
    public void removePalette(java.awt.Window palette) {
        paletteHandler.removePalette(palette);
    }

    @java.lang.Override
    public void addWindow(java.awt.Window window, final org.jhotdraw.api.app.View view) {
        if (window instanceof javax.swing.JFrame) {
            ((javax.swing.JFrame) (window)).setJMenuBar(createMenuBar(view));
        } else if (window instanceof javax.swing.JDialog) {
            // ((JDialog) window).setJMenuBar(createMenuBar(null));
        }
        paletteHandler.add(window, view);
    }

    @java.lang.Override
    public void removeWindow(java.awt.Window window) {
        if (window instanceof javax.swing.JFrame) {
            // Unlink all menu items from action objects
            javax.swing.JMenuBar mb = ((javax.swing.JFrame) (window)).getJMenuBar();
            java.util.Stack<javax.swing.JMenu> s = new java.util.Stack<>();
            for (int i = 0, n = mb.getMenuCount(); i < n; ++i) {
                if (mb.getMenu(i) != null) {
                    s.push(mb.getMenu(i));
                }
            }
            while (!s.isEmpty()) {
                javax.swing.JPopupMenu m = s.pop().getPopupMenu();
                for (int i = 0, n = m.getComponentCount(); i < n; ++i) {
                    if (m.getComponent(i) instanceof javax.swing.JMenu) {
                        s.push(((javax.swing.JMenu) (m.getComponent(i))));
                    } else if (m.getComponent(i) instanceof javax.swing.AbstractButton) {
                        ((javax.swing.AbstractButton) (m.getComponent(i))).setAction(null);
                    }
                }
            } 
            // We explicitly set the JMenuBar to null to facilitate garbage
            // collection
            ((javax.swing.JFrame) (window)).setJMenuBar(null);
        }
        paletteHandler.remove(window);
    }

    @java.lang.Override
    public void show(org.jhotdraw.api.app.View view) {
        if (!view.isShowing()) {
            view.setShowing(true);
            javax.swing.JFrame f = new javax.swing.JFrame();
            f.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
            f.setSize(new java.awt.Dimension(600, 400));
            updateViewTitle(view, f);
            org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "view", f);
            java.awt.Point loc = f.getLocation();
            boolean moved;
            do {
                moved = false;
                for (org.jhotdraw.api.app.View aView : views()) {
                    if (((aView != view) && aView.isShowing()) && javax.swing.SwingUtilities.getWindowAncestor(aView.getComponent()).getLocation().equals(loc)) {
                        loc.x += 22;
                        loc.y += 22;
                        moved = true;
                        break;
                    }
                }
            } while (moved );
            f.setLocation(loc);
            new org.jhotdraw.app.OSXApplication.FrameHandler(f, view);
            addWindow(f, view);
            f.getContentPane().add(view.getComponent());
            f.setVisible(true);
            view.start();
        }
    }

    /**
     * Updates the title of a view and displays it in the given frame.
     *
     * @param v
     * 		The view.
     * @param f
     * 		The frame.
     */
    protected void updateViewTitle(org.jhotdraw.api.app.View v, javax.swing.JFrame f) {
        java.lang.String title;
        java.net.URI uri = v.getURI();
        if (uri == null) {
            title = labels.getString("unnamedFile");
        } else {
            title = org.jhotdraw.net.URIUtil.getName(uri);
        }
        v.setTitle(labels.getFormatted("frame.title", title, getName(), v.getMultipleOpenId()));
        f.setTitle(v.getTitle());
        // Adds a proxy icon for the file to the title bar
        // See http://developer.apple.com/technotes/tn2007/tn2196.html#WINDOW_DOCUMENTFILE
        if (((uri != null) && (uri.getScheme() != null)) && "file".equals(uri.getScheme())) {
            f.getRootPane().putClientProperty("Window.documentFile", new java.io.File(uri));
        } else {
            f.getRootPane().putClientProperty("Window.documentFile", null);
        }
    }

    @java.lang.Override
    public void hide(org.jhotdraw.api.app.View p) {
        if (p.isShowing()) {
            javax.swing.JFrame f = ((javax.swing.JFrame) (javax.swing.SwingUtilities.getWindowAncestor(p.getComponent())));
            if (getActiveView() == p) {
                setActiveView(null);
            }
            f.setVisible(false);
            removeWindow(f);
            f.remove(p.getComponent());
            f.dispose();
        }
    }

    /**
     * Creates a menu bar.
     */
    protected javax.swing.JMenuBar createMenuBar(org.jhotdraw.api.app.View v) {
        javax.swing.JMenuBar mb = new javax.swing.JMenuBar();
        // Get menus from application model
        javax.swing.JMenu fileMenu = null;
        javax.swing.JMenu editMenu = null;
        javax.swing.JMenu helpMenu = null;
        javax.swing.JMenu viewMenu = null;
        javax.swing.JMenu windowMenu = null;
        java.lang.String fileMenuText = labels.getString("file.text");
        java.lang.String editMenuText = labels.getString("edit.text");
        java.lang.String viewMenuText = labels.getString("view.text");
        java.lang.String windowMenuText = labels.getString("window.text");
        java.lang.String helpMenuText = labels.getString("help.text");
        java.util.LinkedList<javax.swing.JMenu> ll = new java.util.LinkedList<>();
        getModel().getMenuBuilder().addOtherMenus(ll, this, v);
        for (javax.swing.JMenu mm : ll) {
            java.lang.String text = mm.getText();
            if (text == null) {
                mm.setText("-null-");
            } else if (text.equals(fileMenuText)) {
                fileMenu = mm;
                continue;
            } else if (text.equals(editMenuText)) {
                editMenu = mm;
                continue;
            } else if (text.equals(viewMenuText)) {
                viewMenu = mm;
                continue;
            } else if (text.equals(windowMenuText)) {
                windowMenu = mm;
                continue;
            } else if (text.equals(helpMenuText)) {
                helpMenu = mm;
                continue;
            }
            mb.add(mm);
        }
        // Create missing standard menus
        if (fileMenu == null) {
            fileMenu = createFileMenu(v);
        }
        if (editMenu == null) {
            editMenu = createEditMenu(v);
        }
        if (viewMenu == null) {
            viewMenu = createViewMenu(v);
        }
        if (windowMenu == null) {
            windowMenu = createWindowMenu(v);
        }
        if (helpMenu == null) {
            helpMenu = createHelpMenu(v);
        }
        // Insert standard menus into menu bar
        if (fileMenu != null) {
            mb.add(fileMenu, 0);
        }
        if (editMenu != null) {
            mb.add(editMenu, java.lang.Math.min(1, mb.getComponentCount()));
        }
        if (viewMenu != null) {
            mb.add(viewMenu, java.lang.Math.min(2, mb.getComponentCount()));
        }
        if (windowMenu != null) {
            mb.add(windowMenu);
        }
        if (helpMenu != null) {
            mb.add(helpMenu);
        }
        return mb;
    }

    @java.lang.Override
    public javax.swing.JMenu createViewMenu(final org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m = new javax.swing.JMenu();
        labels.configureMenu(m, "view");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addOtherViewItems(m, this, view);
        return m.getItemCount() > 0 ? m : null;
    }

    @java.lang.Override
    public javax.swing.JMenu createWindowMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m;
        javax.swing.JMenuItem mi;
        m = new javax.swing.JMenu();
        javax.swing.JMenu windowMenu = m;
        labels.configureMenu(m, "window");
        m.addSeparator();
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addOtherWindowItems(m, this, view);
        new org.jhotdraw.app.OSXApplication.WindowMenuHandler(windowMenu, view);
        return m.getItemCount() == 0 ? null : m;
    }

    @java.lang.Override
    public javax.swing.JMenu createFileMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m;
        m = new javax.swing.JMenu();
        labels.configureMenu(m, "file");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addClearFileItems(m, this, view);
        mb.addNewFileItems(m, this, view);
        mb.addNewWindowItems(m, this, view);
        mb.addLoadFileItems(m, this, view);
        mb.addOpenFileItems(m, this, view);
        if ((((getAction(view, org.jhotdraw.app.action.file.LoadFileAction.ID) != null) || (getAction(view, org.jhotdraw.app.action.file.OpenFileAction.ID) != null)) || (getAction(view, org.jhotdraw.app.action.file.LoadDirectoryAction.ID) != null)) || (getAction(view, org.jhotdraw.app.action.file.OpenDirectoryAction.ID) != null)) {
            m.add(createOpenRecentFileMenu(view));
        }
        maybeAddSeparator(m);
        mb.addCloseFileItems(m, this, view);
        mb.addSaveFileItems(m, this, view);
        mb.addExportFileItems(m, this, view);
        mb.addPrintFileItems(m, this, view);
        mb.addOtherFileItems(m, this, view);
        return m.getItemCount() == 0 ? null : m;
    }

    @java.lang.Override
    public javax.swing.JMenu createEditMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m;
        javax.swing.JMenuItem mi;
        javax.swing.Action a;
        m = new javax.swing.JMenu();
        labels.configureMenu(m, "edit");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addUndoItems(m, this, view);
        maybeAddSeparator(m);
        mb.addClipboardItems(m, this, view);
        maybeAddSeparator(m);
        mb.addSelectionItems(m, this, view);
        maybeAddSeparator(m);
        mb.addFindItems(m, this, view);
        maybeAddSeparator(m);
        mb.addOtherEditItems(m, this, view);
        removeTrailingSeparators(m);
        return m.getItemCount() == 0 ? null : m;
    }

    @java.lang.Override
    public javax.swing.JMenu createHelpMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m = new javax.swing.JMenu();
        labels.configureMenu(m, "help");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addHelpItems(m, this, view);
        return m.getItemCount() == 0 ? null : m;
    }

    protected void initScreenMenuBar() {
        setScreenMenuBar(createMenuBar(null));
        paletteHandler.add(((javax.swing.JFrame) (getComponent())), null);
        javax.swing.Action a;
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.OpenApplicationAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setOpenApplicationHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.ReOpenApplicationAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setReOpenApplicationHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.OpenApplicationFileAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setOpenFileHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.PrintApplicationFileAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setPrintFileHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.AboutAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setAboutHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.AbstractPreferencesAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setPreferencesHandler(a);
        }
        if (null != (a = getAction(null, org.jhotdraw.app.action.app.ExitAction.ID))) {
            org.jhotdraw.app.osx.OSXAdapter.setQuitHandler(a);
        }
    }

    protected void initPalettes(final java.util.LinkedList<javax.swing.Action> paletteActions) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.util.LinkedList<javax.swing.JFrame> palettes = new java.util.LinkedList<>();
            java.util.LinkedList<javax.swing.JToolBar> toolBars = new java.util.LinkedList<>(getModel().createToolBars(this, null));
            int i = 0;
            int x = 0;
            for (javax.swing.JToolBar tb : toolBars) {
                i++;
                tb.setFloatable(false);
                tb.setOrientation(javax.swing.JToolBar.VERTICAL);
                tb.setFocusable(false);
                javax.swing.JFrame d = new javax.swing.JFrame();
                // Note: Client properties must be set before heavy-weight
                // peers are created
                d.getRootPane().putClientProperty("Window.style", "small");
                d.getRootPane().putClientProperty("Quaqua.RootPane.isVertical", java.lang.Boolean.FALSE);
                d.getRootPane().putClientProperty("Quaqua.RootPane.isPalette", java.lang.Boolean.TRUE);
                d.setFocusable(false);
                d.setResizable(false);
                d.getContentPane().setLayout(new java.awt.BorderLayout());
                d.getContentPane().add(tb, java.awt.BorderLayout.CENTER);
                d.setAlwaysOnTop(true);
                d.setUndecorated(true);
                d.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.FRAME);
                d.getRootPane().setFont(new java.awt.Font("Lucida Grande", java.awt.Font.PLAIN, 11));
                d.setJMenuBar(createMenuBar(null));
                d.pack();
                d.setFocusableWindowState(false);
                org.jhotdraw.util.prefs.PreferencesUtil.installPalettePrefsHandler(prefs, "toolbar." + i, d, x);
                x += d.getWidth();
                org.jhotdraw.app.action.window.TogglePaletteAction tpa = new org.jhotdraw.app.action.window.TogglePaletteAction(this, d, tb.getName());
                palettes.add(d);
                if (prefs.getBoolean(("toolbar." + i) + ".visible", true)) {
                    addPalette(d);
                    tpa.putValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY, true);
                }
                paletteActions.add(tpa);
            }
            firePropertyChange("paletteCount", 0, palettes.size());
        });
    }

    @java.lang.Override
    public boolean isSharingToolsAmongViews() {
        return true;
    }

    /**
     * Returns the Frame which holds the frameless JMenuBar.
     */
    @java.lang.Override
    public java.awt.Component getComponent() {
        if (invisibleFrame == null) {
            invisibleFrame = new javax.swing.JFrame();
            invisibleFrame.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
            invisibleFrame.setUndecorated(true);
            // Move it way off screen
            invisibleFrame.setLocation(10000, 10000);
            // make the frame transparent and shadowless
            // see https://developer.apple.com/mac/library/technotes/tn2007/tn2196.html
            invisibleFrame.getRootPane().putClientProperty("Window.alpha", 0.0F);
            invisibleFrame.getRootPane().putClientProperty("Window.shadow", false);
            // make it visible, so the menu bar will show
            invisibleFrame.setVisible(true);
        }
        return invisibleFrame;
    }

    protected void setScreenMenuBar(javax.swing.JMenuBar mb) {
        ((javax.swing.JFrame) (getComponent())).setJMenuBar(mb);
        // pack it (without calling pack, the screen menu bar won't work for some reason)
        invisibleFrame.pack();
    }

    protected javax.swing.ActionMap createModelActionMap(org.jhotdraw.api.app.ApplicationModel mo) {
        javax.swing.ActionMap rootMap = new javax.swing.ActionMap();
        rootMap.put(org.jhotdraw.app.action.app.AboutAction.ID, new org.jhotdraw.app.action.app.AboutAction(this));
        rootMap.put(org.jhotdraw.app.action.app.ExitAction.ID, new org.jhotdraw.app.action.app.ExitAction(this));
        rootMap.put(org.jhotdraw.app.action.app.OpenApplicationAction.ID, new org.jhotdraw.app.action.app.OpenApplicationAction(this));
        rootMap.put(org.jhotdraw.app.action.app.OpenApplicationFileAction.ID, new org.jhotdraw.app.action.app.OpenApplicationFileAction(this));
        rootMap.put(org.jhotdraw.app.action.app.ReOpenApplicationAction.ID, new org.jhotdraw.app.action.app.ReOpenApplicationAction(this));
        rootMap.put(org.jhotdraw.app.action.file.ClearRecentFilesMenuAction.ID, new org.jhotdraw.app.action.file.ClearRecentFilesMenuAction(this));
        rootMap.put(org.jhotdraw.action.window.MaximizeWindowAction.ID, new org.jhotdraw.action.window.MaximizeWindowAction(this, null));
        rootMap.put(org.jhotdraw.action.window.MinimizeWindowAction.ID, new org.jhotdraw.action.window.MinimizeWindowAction(this, null));
        javax.swing.ActionMap moMap = mo.createActionMap(this, null);
        moMap.setParent(rootMap);
        return moMap;
    }

    @java.lang.Override
    protected javax.swing.ActionMap createViewActionMap(org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap intermediateMap = new javax.swing.ActionMap();
        intermediateMap.put(org.jhotdraw.action.window.FocusWindowAction.ID, new org.jhotdraw.action.window.FocusWindowAction(v));
        intermediateMap.put(org.jhotdraw.action.window.MaximizeWindowAction.ID, new org.jhotdraw.action.window.MaximizeWindowAction(this, v));
        intermediateMap.put(org.jhotdraw.action.window.MinimizeWindowAction.ID, new org.jhotdraw.action.window.MinimizeWindowAction(this, v));
        javax.swing.ActionMap vMap = model.createActionMap(this, v);
        vMap.setParent(intermediateMap);
        intermediateMap.setParent(getActionMap(null));
        return vMap;
    }

    /**
     * Updates the menu items in the "Window" menu.
     */
    private class WindowMenuHandler implements java.beans.PropertyChangeListener , org.jhotdraw.api.app.Disposable {
        private javax.swing.JMenu windowMenu;

        private org.jhotdraw.api.app.View view;

        public WindowMenuHandler(javax.swing.JMenu windowMenu, org.jhotdraw.api.app.View view) {
            this.windowMenu = windowMenu;
            this.view = view;
            OSXApplication.this.addPropertyChangeListener(this);
            if (view != null) {
                view.addDisposable(this);
            }
            updateWindowMenu();
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if ((((name == null) && (org.jhotdraw.app.AbstractApplication.VIEW_COUNT_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.app.AbstractApplication.VIEW_COUNT_PROPERTY))) || "paletteCount".equals(name)) {
                updateWindowMenu();
            }
        }

        protected void updateWindowMenu() {
            javax.swing.JMenu m = windowMenu;
            javax.swing.JMenuItem mi;
            // FIXME - We leak memory here!!
            m.removeAll();
            mi = m.add(getAction(view, org.jhotdraw.action.window.MinimizeWindowAction.ID));
            mi.setIcon(null);
            mi = m.add(getAction(view, org.jhotdraw.action.window.MaximizeWindowAction.ID));
            mi.setIcon(null);
            m.addSeparator();
            for (org.jhotdraw.api.app.View pr : views()) {
                if (getAction(pr, org.jhotdraw.action.window.FocusWindowAction.ID) != null) {
                    mi = m.add(getAction(pr, org.jhotdraw.action.window.FocusWindowAction.ID));
                }
            }
            if (paletteActions.size() > 0) {
                m.addSeparator();
                for (javax.swing.Action a : paletteActions) {
                    javax.swing.JCheckBoxMenuItem cbmi = new javax.swing.JCheckBoxMenuItem(a);
                    org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, a);
                    cbmi.setIcon(null);
                    m.add(cbmi);
                }
            }
            org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
            mb.addOtherWindowItems(m, OSXApplication.this, view);
        }

        @java.lang.Override
        public void dispose() {
            windowMenu.removeAll();
            removePropertyChangeListener(this);
            view = null;
        }
    }

    /**
     * Updates the modifedState of the frame.
     */
    private class FrameHandler extends java.awt.event.WindowAdapter implements java.beans.PropertyChangeListener , org.jhotdraw.api.app.Disposable {
        private javax.swing.JFrame frame;

        private org.jhotdraw.api.app.View view;

        public FrameHandler(javax.swing.JFrame frame, org.jhotdraw.api.app.View view) {
            this.frame = frame;
            this.view = view;
            view.addPropertyChangeListener(this);
            frame.addWindowListener(this);
            view.addDisposable(this);
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if (name.equals(org.jhotdraw.api.app.View.HAS_UNSAVED_CHANGES_PROPERTY)) {
                frame.getRootPane().putClientProperty("windowModified", view.hasUnsavedChanges());
            } else if (name.equals(org.jhotdraw.api.app.View.URI_PROPERTY) || name.equals(org.jhotdraw.api.app.View.TITLE_PROPERTY)) {
                updateViewTitle(view, frame);
            }
        }

        @java.lang.Override
        public void windowClosing(final java.awt.event.WindowEvent evt) {
            getAction(view, org.jhotdraw.app.action.file.CloseFileAction.ID).actionPerformed(new java.awt.event.ActionEvent(evt.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, "windowClosing"));
        }

        @java.lang.Override
        public void windowClosed(final java.awt.event.WindowEvent evt) {
        }

        @java.lang.Override
        public void windowIconified(java.awt.event.WindowEvent e) {
            if (view == getActiveView()) {
                setActiveView(null);
            }
            view.stop();
        }

        @java.lang.Override
        public void windowDeiconified(java.awt.event.WindowEvent e) {
            view.start();
        }

        @java.lang.Override
        public void dispose() {
            frame.removeWindowListener(this);
            view.removePropertyChangeListener(this);
        }

        @java.lang.Override
        public void windowGainedFocus(java.awt.event.WindowEvent e) {
            setActiveView(view);
        }
    }

    private static class QuitHandler {
        /**
         * This method is invoked, when the user has selected the Quit menu item.
         *
         * @return Returns true if the application has no unsaved changes and can be closed.
         */
        public boolean handleQuit() {
            return false;
        }
    }
}