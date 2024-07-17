/* @(#)MDIApplication.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import org.jhotdraw.action.window.FocusWindowAction;
import org.jhotdraw.action.window.ToggleToolBarAction;
import org.jhotdraw.api.app.ApplicationModel;
import org.jhotdraw.api.app.View;
/**
 * {@code MDIApplication} handles the lifecycle of multiple {@link View}s using a Windows multiple
 * document interface (MDI).
 *
 * <p>This user interface created by this application follows the guidelines given in the <a
 * href="http://msdn.microsoft.com/en-us/library/aa511258.aspx" >Windows User Experience Interaction
 * Guidelines</a>.
 *
 * <p>An application consists of a parent {@code JFrame} which holds a {@code JDesktopPane}. The
 * views reside in {@code JInternalFrame}s inside of the {@code JDesktopPane}. The parent frame also
 * contains a menu bar, toolbars and palette windows for the views.
 *
 * <p>The life cycle of the application is tied to the parent {@code JFrame}. Closing the parent
 * {@code JFrame} quits the application.
 *
 * <p>The parent frame has the following standard menus:
 *
 * <pre>
 * File Edit Window Help</pre>
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
 *  -
 *  Exit ({@link ExitAction#ID})
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
 *  -
 *  Settings ({@link AbstractPreferencesAction#ID})
 * </pre>
 *
 * The <b>window menu</b> has the following standard menu items:
 *
 * <pre>
 *  Arrange Cascade ({@link ArrangeWindowsAction#CASCADE_ID})
 *  Arrange Vertical ({@link ArrangeWindowsAction#VERTICAL_ID})
 *  Arrange Horizontal ({@link ArrangeWindowsAction#HORIZONTAL_ID})
 *  -
 *  "Filename" ({@link FocusWindowAction})
 *  -
 *  "Toolbar" ({@link ToggleToolBarAction})
 * </pre>
 *
 * The <b>help menu</b> has the following standard menu items:
 *
 * <pre>
 *  About ({@link AboutAction#ID})
 * </pre>
 *
 * The menus provided by the {@code ApplicationModel} are inserted between the file menu and the
 * window menu. In case the application model supplies a menu with the title "Edit" or "Help", the
 * standard menu items are added with a seperator to the end of the menu.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class MDIApplication extends org.jhotdraw.app.AbstractApplication {
    private static final long serialVersionUID = 1L;

    private javax.swing.JFrame parentFrame;

    private javax.swing.JScrollPane scrollPane;

    private org.jhotdraw.gui.JMDIDesktopPane desktopPane;

    private java.util.prefs.Preferences prefs;

    private java.util.LinkedList<javax.swing.Action> toolBarActions;

    public MDIApplication() {
    }

    @java.lang.Override
    public void init() {
        super.init();
        initLookAndFeel();
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getModel() == null ? getClass() : getModel().getClass());
        initLabels();
        parentFrame = new javax.swing.JFrame(getName());
        parentFrame.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        parentFrame.setPreferredSize(new java.awt.Dimension(600, 400));
        desktopPane = new org.jhotdraw.gui.JMDIDesktopPane();
        desktopPane.setTransferHandler(new org.jhotdraw.app.MDIApplication.DropFileTransferHandler());
        scrollPane = new javax.swing.JScrollPane();
        scrollPane.setViewportView(desktopPane);
        toolBarActions = new java.util.LinkedList<>();
        setActionMap(createModelActionMap(model));
        parentFrame.getContentPane().add(wrapDesktopPane(scrollPane, toolBarActions));
        parentFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(final java.awt.event.WindowEvent evt) {
                getAction(null, org.jhotdraw.app.action.app.ExitAction.ID).actionPerformed(new java.awt.event.ActionEvent(parentFrame, java.awt.event.ActionEvent.ACTION_PERFORMED, "windowClosing"));
            }
        });
        parentFrame.setJMenuBar(createMenuBar(null));
        org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "parentFrame", parentFrame);
        parentFrame.setVisible(true);
    }

    protected javax.swing.ActionMap createModelActionMap(org.jhotdraw.api.app.ApplicationModel mo) {
        javax.swing.ActionMap rootMap = new javax.swing.ActionMap();
        rootMap.put(org.jhotdraw.app.action.app.AboutAction.ID, new org.jhotdraw.app.action.app.AboutAction(this));
        rootMap.put(org.jhotdraw.app.action.app.ExitAction.ID, new org.jhotdraw.app.action.app.ExitAction(this));
        rootMap.put(org.jhotdraw.app.action.file.ClearRecentFilesMenuAction.ID, new org.jhotdraw.app.action.file.ClearRecentFilesMenuAction(this));
        rootMap.put(org.jhotdraw.action.window.MaximizeWindowAction.ID, new org.jhotdraw.action.window.MaximizeWindowAction(this, null));
        rootMap.put(org.jhotdraw.action.window.MinimizeWindowAction.ID, new org.jhotdraw.action.window.MinimizeWindowAction(this, null));
        rootMap.put(org.jhotdraw.action.window.ArrangeWindowsAction.VERTICAL_ID, new org.jhotdraw.action.window.ArrangeWindowsAction(desktopPane, org.jhotdraw.api.gui.Arrangeable.Arrangement.VERTICAL));
        rootMap.put(org.jhotdraw.action.window.ArrangeWindowsAction.HORIZONTAL_ID, new org.jhotdraw.action.window.ArrangeWindowsAction(desktopPane, org.jhotdraw.api.gui.Arrangeable.Arrangement.HORIZONTAL));
        rootMap.put(org.jhotdraw.action.window.ArrangeWindowsAction.CASCADE_ID, new org.jhotdraw.action.window.ArrangeWindowsAction(desktopPane, org.jhotdraw.api.gui.Arrangeable.Arrangement.CASCADE));
        javax.swing.ActionMap moMap = mo.createActionMap(this, null);
        moMap.setParent(rootMap);
        return moMap;
    }

    @java.lang.Override
    protected javax.swing.ActionMap createViewActionMap(org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap intermediateMap = new javax.swing.ActionMap();
        intermediateMap.put(org.jhotdraw.action.window.FocusWindowAction.ID, new org.jhotdraw.action.window.FocusWindowAction(v));
        javax.swing.ActionMap vMap = model.createActionMap(this, v);
        vMap.put(org.jhotdraw.action.edit.UndoAction.ID, v.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID));
        vMap.put(org.jhotdraw.action.edit.RedoAction.ID, v.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID));
        vMap.setParent(intermediateMap);
        intermediateMap.setParent(getActionMap(null));
        return vMap;
    }

    @java.lang.Override
    public void launch(java.lang.String[] args) {
        super.launch(args);
    }

    @java.lang.Override
    public void configure(java.lang.String[] args) {
        java.lang.System.setProperty("apple.laf.useScreenMenuBar", "false");
        java.lang.System.setProperty("com.apple.macos.useScreenMenuBar", "false");
        java.lang.System.setProperty("apple.awt.graphics.UseQuartz", "false");
        java.lang.System.setProperty("swing.aatext", "true");
    }

    protected void initLookAndFeel() {
        try {
            java.lang.String lafName = javax.swing.UIManager.getSystemLookAndFeelClassName();
            javax.swing.UIManager.setLookAndFeel(lafName);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        if (javax.swing.UIManager.getString("OptionPane.css") == null) {
            javax.swing.UIManager.put("OptionPane.css", (((("<head>" + "<style type=\"text/css\">") + "b { font: 13pt \"Dialog\" }") + "p { font: 11pt \"Dialog\"; margin-top: 8px }") + "</style>") + "</head>");
        }
    }

    @java.lang.Override
    public void show(final org.jhotdraw.api.app.View v) {
        if (!v.isShowing()) {
            v.setShowing(true);
            final javax.swing.JInternalFrame f = new javax.swing.JInternalFrame();
            f.setDefaultCloseOperation(javax.swing.JInternalFrame.DO_NOTHING_ON_CLOSE);
            f.setClosable(getAction(v, org.jhotdraw.app.action.file.CloseFileAction.ID) != null);
            f.setMaximizable(true);
            f.setResizable(true);
            f.setIconifiable(false);
            f.setSize(new java.awt.Dimension(400, 400));
            updateViewTitle(v, f);
            org.jhotdraw.util.prefs.PreferencesUtil.installInternalFramePrefsHandler(prefs, "view", f, desktopPane);
            java.awt.Point loc = new java.awt.Point(desktopPane.getInsets().left, desktopPane.getInsets().top);
            boolean moved;
            do {
                moved = false;
                for (org.jhotdraw.api.app.View aView : views()) {
                    if (((aView != v) && aView.isShowing()) && javax.swing.SwingUtilities.getRootPane(aView.getComponent()).getParent().getLocation().equals(loc)) {
                        java.awt.Point offset = javax.swing.SwingUtilities.convertPoint(javax.swing.SwingUtilities.getRootPane(aView.getComponent()), 0, 0, javax.swing.SwingUtilities.getRootPane(aView.getComponent()).getParent());
                        loc.x += java.lang.Math.max(offset.x, offset.y);
                        loc.y += java.lang.Math.max(offset.x, offset.y);
                        moved = true;
                        break;
                    }
                }
            } while (moved );
            f.setLocation(loc);
            // paletteHandler.add(f, v);
            f.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @java.lang.Override
                public void internalFrameClosing(final javax.swing.event.InternalFrameEvent evt) {
                    getAction(v, org.jhotdraw.app.action.file.CloseFileAction.ID).actionPerformed(new java.awt.event.ActionEvent(f, java.awt.event.ActionEvent.ACTION_PERFORMED, "windowClosing"));
                }

                @java.lang.Override
                public void internalFrameClosed(final javax.swing.event.InternalFrameEvent evt) {
                    v.stop();
                }
            });
            v.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    java.lang.String name = evt.getPropertyName();
                    if ((((name == null) && (org.jhotdraw.api.app.View.HAS_UNSAVED_CHANGES_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.app.View.HAS_UNSAVED_CHANGES_PROPERTY))) || (((name == null) && (org.jhotdraw.api.app.View.URI_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.app.View.URI_PROPERTY)))) {
                        updateViewTitle(v, f);
                    }
                }
            });
            f.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    java.lang.String name = evt.getPropertyName();
                    if ("selected".equals(name)) {
                        if (evt.getNewValue().equals(java.lang.Boolean.TRUE)) {
                            setActiveView(v);
                        } else if (v == getActiveView()) {
                            setActiveView(null);
                        }
                    }
                }
            });
            // f.setJMenuBar(createMenuBar(v));
            f.getContentPane().add(v.getComponent());
            f.setVisible(true);
            desktopPane.add(f);
            if (desktopPane.getComponentCount() == 1) {
                try {
                    f.setMaximum(true);
                } catch (java.beans.PropertyVetoException ex) {
                    // ignore veto
                }
            }
            f.toFront();
            try {
                f.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                // Don't care.
            }
            v.getComponent().requestFocusInWindow();
            v.start();
        }
    }

    @java.lang.Override
    public void hide(org.jhotdraw.api.app.View v) {
        if (v.isShowing()) {
            javax.swing.JInternalFrame f = ((javax.swing.JInternalFrame) (javax.swing.SwingUtilities.getRootPane(v.getComponent()).getParent()));
            if (getActiveView() == v) {
                setActiveView(null);
            }
            f.setVisible(false);
            f.remove(v.getComponent());
            // Setting the JMenuBar to null triggers action disposal of
            // actions in the openRecentMenu and the windowMenu. This is
            // important to prevent memory leaks.
            f.setJMenuBar(null);
            desktopPane.remove(f);
            f.dispose();
        }
    }

    @java.lang.Override
    public boolean isSharingToolsAmongViews() {
        return true;
    }

    @java.lang.Override
    public java.awt.Component getComponent() {
        return parentFrame;
    }

    /**
     * Returns the wrapped desktop pane.
     */
    protected java.awt.Component wrapDesktopPane(java.awt.Component c, java.util.LinkedList<javax.swing.Action> toolBarActions) {
        if (getModel() != null) {
            int id = 0;
            for (javax.swing.JToolBar tb : new org.jhotdraw.util.ReversedList<>(getModel().createToolBars(this, null))) {
                id++;
                javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
                panel.add(tb, java.awt.BorderLayout.NORTH);
                panel.add(c, java.awt.BorderLayout.CENTER);
                c = panel;
                org.jhotdraw.util.prefs.PreferencesUtil.installToolBarPrefsHandler(prefs, "toolbar." + id, tb);
                toolBarActions.addFirst(new org.jhotdraw.action.window.ToggleToolBarAction(tb, tb.getName()));
            }
        }
        return c;
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
        maybeAddSeparator(m);
        mb.addExitItems(m, this, view);
        return m.getItemCount() == 0 ? null : m;
    }

    /**
     * Updates the title of a view and displays it in the given frame.
     *
     * @param v
     * 		The view.
     * @param f
     * 		The frame.
     */
    protected void updateViewTitle(org.jhotdraw.api.app.View v, javax.swing.JInternalFrame f) {
        java.net.URI uri = v.getURI();
        java.lang.String title;
        if (uri == null) {
            title = labels.getString("unnamedFile");
        } else {
            title = org.jhotdraw.net.URIUtil.getName(uri);
        }
        if (v.hasUnsavedChanges()) {
            title += "*";
        }
        v.setTitle(labels.getFormatted("internalFrame.title", title, getName(), v.getMultipleOpenId()));
        f.setTitle(v.getTitle());
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
        addAction(m, view, org.jhotdraw.action.window.ArrangeWindowsAction.CASCADE_ID);
        addAction(m, view, org.jhotdraw.action.window.ArrangeWindowsAction.VERTICAL_ID);
        addAction(m, view, org.jhotdraw.action.window.ArrangeWindowsAction.HORIZONTAL_ID);
        maybeAddSeparator(m);
        for (org.jhotdraw.api.app.View pr : views()) {
            addAction(m, view, org.jhotdraw.action.window.FocusWindowAction.ID);
        }
        if (toolBarActions.size() > 0) {
            maybeAddSeparator(m);
            for (javax.swing.Action a : toolBarActions) {
                javax.swing.JCheckBoxMenuItem cbmi = new javax.swing.JCheckBoxMenuItem(a);
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, a);
                addMenuItem(m, cbmi);
            }
        }
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addOtherWindowItems(m, this, view);
        addPropertyChangeListener(new org.jhotdraw.app.MDIApplication.WindowMenuHandler(windowMenu, view));
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
        maybeAddSeparator(m);
        mb.addPreferencesItems(m, this, view);
        removeTrailingSeparators(m);
        return m.getItemCount() == 0 ? null : m;
    }

    @java.lang.Override
    public javax.swing.JMenu createHelpMenu(org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m = new javax.swing.JMenu();
        labels.configureMenu(m, "help");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addHelpItems(m, this, view);
        mb.addAboutItems(m, this, view);
        return m.getItemCount() == 0 ? null : m;
    }

    /**
     * Updates the menu items in the "Window" menu.
     */
    private class WindowMenuHandler implements java.beans.PropertyChangeListener {
        private javax.swing.JMenu windowMenu;

        private org.jhotdraw.api.app.View view;

        public WindowMenuHandler(javax.swing.JMenu windowMenu, org.jhotdraw.api.app.View view) {
            this.windowMenu = windowMenu;
            this.view = view;
            MDIApplication.this.addPropertyChangeListener(this);
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
            m.removeAll();
            m.add(getAction(view, org.jhotdraw.action.window.ArrangeWindowsAction.CASCADE_ID));
            m.add(getAction(view, org.jhotdraw.action.window.ArrangeWindowsAction.VERTICAL_ID));
            m.add(getAction(view, org.jhotdraw.action.window.ArrangeWindowsAction.HORIZONTAL_ID));
            m.addSeparator();
            for (org.jhotdraw.api.app.View pr : views()) {
                if (getAction(pr, org.jhotdraw.action.window.FocusWindowAction.ID) != null) {
                    m.add(getAction(pr, org.jhotdraw.action.window.FocusWindowAction.ID));
                }
            }
            if (toolBarActions.size() > 0) {
                m.addSeparator();
                for (javax.swing.Action a : toolBarActions) {
                    javax.swing.JCheckBoxMenuItem cbmi = new javax.swing.JCheckBoxMenuItem(a);
                    org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, a);
                    m.add(cbmi);
                }
            }
        }
    }

    /**
     * This transfer handler opens a new view for each dropped file.
     */
    private class DropFileTransferHandler extends javax.swing.TransferHandler {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public boolean canImport(javax.swing.JComponent comp, java.awt.datatransfer.DataFlavor[] transferFlavors) {
            javax.swing.Action a = getAction(null, org.jhotdraw.app.action.app.OpenApplicationFileAction.ID);
            if (a == null) {
                return false;
            }
            for (java.awt.datatransfer.DataFlavor f : transferFlavors) {
                if (f.isFlavorJavaFileListType()) {
                    return true;
                }
            }
            return false;
        }

        @java.lang.Override
        public boolean importData(javax.swing.JComponent comp, java.awt.datatransfer.Transferable t) {
            javax.swing.Action a = getAction(null, org.jhotdraw.app.action.app.OpenApplicationFileAction.ID);
            if (a == null) {
                return false;
            }
            try {
                @java.lang.SuppressWarnings("unchecked")
                java.util.List<java.io.File> files = ((java.util.List<java.io.File>) (t.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor)));
                for (final java.io.File f : files) {
                    a.actionPerformed(new java.awt.event.ActionEvent(desktopPane, java.awt.event.ActionEvent.ACTION_PERFORMED, f.toString()));
                }
                return true;
            } catch (java.awt.datatransfer.UnsupportedFlavorException | java.io.IOException ex) {
                return false;
            }
        }
    }
}