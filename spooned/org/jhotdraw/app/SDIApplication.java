/* @(#)SDIApplication.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.action.window.ToggleVisibleAction;
import org.jhotdraw.api.app.ApplicationModel;
import org.jhotdraw.api.app.View;
/**
 * {@code SDIApplication} handles the lifecycle of multiple {@link View}s using a Windows single
 * document interface (SDI).
 *
 * <p>This user interface created by this application follows the guidelines given in the <a
 * href="http://msdn.microsoft.com/en-us/library/aa511258.aspx" >Windows User Experience Interaction
 * Guidelines</a>.
 *
 * <p>An application of this type can open multiple {@link View}s. Each view is shown in a separate
 * {@code JFrame}.
 *
 * <p>Each JFrame contains a menu bar, toolbars and palette bars for the views.
 *
 * <p>The life cycle of the application is tied to the {@code JFrame}s. Closing the last {@code JFrame} quits the application.
 *
 * <p>SDIApplication handles the life cycle of a single document window being presented in a JFrame.
 * The JFrame provides all the functionality needed to work with the document, such as a menu bar,
 * tool bars and palette windows.
 *
 * <p>The life cycle of the application is tied to the JFrame. Closing the JFrame quits the
 * application.
 *
 * <p>The menu bar of a JFrame has the following standard menus:
 *
 * <pre>
 * File &nbsp; Edit &nbsp; View &nbsp; Help</pre>
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
 *  Save ({@link SaveFileAction#ID})
 *  Save As... ({@link SaveFileAsAction#ID})
 *  Export... ({@link ExportFileAction#ID})
 *  Print... ({@link PrintFileAction#ID})
 *  -
 *  Close ({@link CloseFileAction#ID})
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
 *  Preferences... ({@link AbstractPreferencesAction#ID})
 * </pre>
 *
 * The <b>view menu</b> has the following standard menu items:
 *
 * <pre>
 *  "Toolbar" ({@link ToggleVisibleAction})
 * </pre>
 *
 * The <b>view menu</b> has the following standard menu items:
 *
 * <pre>
 *  About ({@link AboutAction#ID})
 * </pre>
 *
 * The menus provided by the {@code ApplicationModel} are inserted between the file menu and the
 * window menu. In case the application model supplies a menu with the title "Help", it is inserted
 * after the window menu.
 */
public class SDIApplication extends org.jhotdraw.app.AbstractApplication {
    private static final long serialVersionUID = 1L;

    private java.util.prefs.Preferences prefs;

    public SDIApplication() {
    }

    @java.lang.Override
    public void launch(java.lang.String[] args) {
        java.lang.System.setProperty("apple.awt.graphics.UseQuartz", "false");
        super.launch(args);
    }

    @java.lang.Override
    public void init() {
        super.init();
        initLookAndFeel();
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getModel() == null ? getClass() : getModel().getClass());
        initLabels();
        setActionMap(createModelActionMap(model));
    }

    @java.lang.Override
    public void remove(org.jhotdraw.api.app.View p) {
        super.remove(p);
        if (views().size() == 0) {
            stop();
        }
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

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void show(final org.jhotdraw.api.app.View view) {
        if (!view.isShowing()) {
            view.setShowing(true);
            final javax.swing.JFrame f = new javax.swing.JFrame();
            f.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
            updateViewTitle(view, f);
            javax.swing.JPanel panel = ((javax.swing.JPanel) (wrapViewComponent(view)));
            f.add(panel);
            f.setSize(new java.awt.Dimension(600, 400));
            f.setJMenuBar(createMenuBar(view));
            org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "view", f);
            java.awt.Point loc = f.getLocation();
            boolean moved;
            do {
                moved = false;
                for (org.jhotdraw.api.app.View aView : views()) {
                    if (((aView != view) && (javax.swing.SwingUtilities.getWindowAncestor(aView.getComponent()) != null)) && javax.swing.SwingUtilities.getWindowAncestor(aView.getComponent()).getLocation().equals(loc)) {
                        loc.x += 22;
                        loc.y += 22;
                        moved = true;
                        break;
                    }
                }
            } while (moved );
            f.setLocation(loc);
            f.addWindowListener(new java.awt.event.WindowAdapter() {
                @java.lang.Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    getAction(view, org.jhotdraw.app.action.file.CloseFileAction.ID).actionPerformed(new java.awt.event.ActionEvent(f, java.awt.event.ActionEvent.ACTION_PERFORMED, "windowClosing"));
                }

                @java.lang.Override
                public void windowClosed(final java.awt.event.WindowEvent evt) {
                    view.stop();
                }

                @java.lang.Override
                public void windowGainedFocus(java.awt.event.WindowEvent e) {
                    setActiveView(view);
                }
            });
            view.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @java.lang.Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    java.lang.String name = evt.getPropertyName();
                    if (((name.equals(org.jhotdraw.api.app.View.HAS_UNSAVED_CHANGES_PROPERTY) || name.equals(org.jhotdraw.api.app.View.URI_PROPERTY)) || name.equals(org.jhotdraw.api.app.View.TITLE_PROPERTY)) || name.equals(org.jhotdraw.api.app.View.MULTIPLE_OPEN_ID_PROPERTY)) {
                        updateViewTitle(view, f);
                    }
                }
            });
            f.setVisible(true);
            view.start();
        }
    }

    /**
     * Returns the view component. Eventually wraps it into another component in order to provide
     * additional functionality.
     */
    protected java.awt.Component wrapViewComponent(org.jhotdraw.api.app.View p) {
        javax.swing.JComponent c = p.getComponent();
        if (getModel() != null) {
            java.util.LinkedList<javax.swing.Action> toolBarActions = new java.util.LinkedList<>();
            int id = 0;
            for (javax.swing.JToolBar tb : new org.jhotdraw.util.ReversedList<>(getModel().createToolBars(this, p))) {
                id++;
                javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
                panel.add(tb, java.awt.BorderLayout.NORTH);
                panel.add(c, java.awt.BorderLayout.CENTER);
                c = panel;
                org.jhotdraw.util.prefs.PreferencesUtil.installToolBarPrefsHandler(prefs, "toolbar." + id, tb);
                toolBarActions.addFirst(new org.jhotdraw.action.window.ToggleVisibleAction(tb, tb.getName()));
            }
            p.getComponent().putClientProperty("toolBarActions", toolBarActions);
        }
        return c;
    }

    @java.lang.Override
    public void hide(org.jhotdraw.api.app.View p) {
        if (p.isShowing()) {
            if (getActiveView() == p) {
                setActiveView(null);
            }
            p.setShowing(false);
            javax.swing.JFrame f = ((javax.swing.JFrame) (javax.swing.SwingUtilities.getWindowAncestor(p.getComponent())));
            f.setVisible(false);
            f.remove(p.getComponent());
            f.dispose();
        }
    }

    @java.lang.Override
    public void dispose(org.jhotdraw.api.app.View p) {
        super.dispose(p);
        if (views().size() == 0) {
            stop();
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
        mb.addSaveFileItems(m, this, view);
        mb.addExportFileItems(m, this, view);
        mb.addPrintFileItems(m, this, view);
        mb.addOtherFileItems(m, this, view);
        maybeAddSeparator(m);
        mb.addCloseFileItems(m, this, view);
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

    /**
     * Updates the title of a view and displays it in the given frame.
     *
     * @param view
     * 		The view.
     * @param f
     * 		The frame.
     */
    protected void updateViewTitle(org.jhotdraw.api.app.View view, javax.swing.JFrame f) {
        java.net.URI uri = view.getURI();
        java.lang.String title;
        if (uri == null) {
            title = labels.getString("unnamedFile");
        } else {
            title = org.jhotdraw.net.URIUtil.getName(uri);
        }
        if (view.hasUnsavedChanges()) {
            title += "*";
        }
        view.setTitle(labels.getFormatted("frame.title", title, getName(), view.getMultipleOpenId()));
        f.setTitle(view.getTitle());
    }

    @java.lang.Override
    public boolean isSharingToolsAmongViews() {
        return false;
    }

    @java.lang.Override
    public java.awt.Component getComponent() {
        org.jhotdraw.api.app.View p = getActiveView();
        return p == null ? null : p.getComponent();
    }

    @java.lang.Override
    public javax.swing.JMenu createWindowMenu(final org.jhotdraw.api.app.View view) {
        javax.swing.JMenu m = new javax.swing.JMenu();
        labels.configureMenu(m, "window");
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addOtherWindowItems(m, this, view);
        return m.getItemCount() > 0 ? m : null;
    }

    /**
     * Creates the view menu.
     *
     * @param view
     * 		The View
     * @return A JMenu or null, if the menu doesn't have any items.
     */
    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public javax.swing.JMenu createViewMenu(final org.jhotdraw.api.app.View view) {
        java.lang.Object object = view.getComponent().getClientProperty("toolBarActions");
        java.util.LinkedList<javax.swing.Action> viewActions = ((java.util.LinkedList<javax.swing.Action>) (object));
        javax.swing.JMenu m;
        javax.swing.JMenu m2;
        javax.swing.JMenuItem mi;
        javax.swing.JCheckBoxMenuItem cbmi;
        m = new javax.swing.JMenu();
        labels.configureMenu(m, "view");
        if ((viewActions != null) && (viewActions.size() > 0)) {
            m2 = (viewActions.size() == 1) ? m : new javax.swing.JMenu(labels.getString("toolBars"));
            for (javax.swing.Action a : viewActions) {
                cbmi = new javax.swing.JCheckBoxMenuItem(a);
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, a);
                m2.add(cbmi);
            }
            if (m2 != m) {
                m.add(m2);
            }
        }
        org.jhotdraw.api.app.MenuBuilder mb = model.getMenuBuilder();
        mb.addOtherViewItems(m, this, view);
        return m.getItemCount() > 0 ? m : null;
    }

    @java.lang.Override
    public javax.swing.JMenu createHelpMenu(org.jhotdraw.api.app.View p) {
        javax.swing.JMenu m;
        javax.swing.JMenuItem mi;
        m = new javax.swing.JMenu();
        labels.configureMenu(m, "help");
        m.add(getAction(p, org.jhotdraw.app.action.app.AboutAction.ID));
        return m;
    }

    protected javax.swing.ActionMap createModelActionMap(org.jhotdraw.api.app.ApplicationModel mo) {
        javax.swing.ActionMap rootMap = new javax.swing.ActionMap();
        rootMap.put(org.jhotdraw.app.action.app.AboutAction.ID, new org.jhotdraw.app.action.app.AboutAction(this));
        rootMap.put(org.jhotdraw.app.action.file.ClearRecentFilesMenuAction.ID, new org.jhotdraw.app.action.file.ClearRecentFilesMenuAction(this));
        javax.swing.ActionMap moMap = mo.createActionMap(this, null);
        moMap.setParent(rootMap);
        return moMap;
    }

    @java.lang.Override
    protected javax.swing.ActionMap createViewActionMap(org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap intermediateMap = new javax.swing.ActionMap();
        intermediateMap.put(org.jhotdraw.app.action.file.CloseFileAction.ID, new org.jhotdraw.app.action.file.CloseFileAction(this, v));
        javax.swing.ActionMap vMap = model.createActionMap(this, v);
        vMap.put(org.jhotdraw.action.edit.UndoAction.ID, v.getActionMap().get(org.jhotdraw.action.edit.UndoAction.ID));
        vMap.put(org.jhotdraw.action.edit.RedoAction.ID, v.getActionMap().get(org.jhotdraw.action.edit.RedoAction.ID));
        vMap.setParent(intermediateMap);
        intermediateMap.setParent(getActionMap(null));
        return vMap;
    }
}