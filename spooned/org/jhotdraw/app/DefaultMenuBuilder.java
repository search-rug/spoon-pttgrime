/* @(#)DefaultMenuBuilder.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.action.edit.AbstractFindAction;
import org.jhotdraw.action.edit.ClearSelectionAction;
import org.jhotdraw.action.edit.CopyAction;
import org.jhotdraw.action.edit.CutAction;
import org.jhotdraw.action.edit.DeleteAction;
import org.jhotdraw.action.edit.DuplicateAction;
import org.jhotdraw.action.edit.PasteAction;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.action.edit.SelectAllAction;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.app.action.app.AboutAction;
import org.jhotdraw.app.action.app.AbstractPreferencesAction;
import org.jhotdraw.app.action.app.ExitAction;
import org.jhotdraw.app.action.file.ClearFileAction;
import org.jhotdraw.app.action.file.CloseFileAction;
import org.jhotdraw.app.action.file.ExportFileAction;
import org.jhotdraw.app.action.file.LoadDirectoryAction;
import org.jhotdraw.app.action.file.LoadFileAction;
import org.jhotdraw.app.action.file.NewFileAction;
import org.jhotdraw.app.action.file.NewWindowAction;
import org.jhotdraw.app.action.file.OpenDirectoryAction;
import org.jhotdraw.app.action.file.OpenFileAction;
import org.jhotdraw.app.action.file.PrintFileAction;
import org.jhotdraw.app.action.file.SaveFileAction;
import org.jhotdraw.app.action.file.SaveFileAsAction;
/**
 * {@code DefaultMenuBuilder}.
 *
 * @author Werner Randelshofer
 * @version 1.0 2010-11-14 Created.
 */
public class DefaultMenuBuilder implements org.jhotdraw.api.app.MenuBuilder {
    /**
     * Whether icons in menu items shall be removed.
     */
    public boolean suppressIcons;

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link AbstractPreferencesAction}
     * </ul>
     */
    @java.lang.Override
    public void addPreferencesItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.app.AbstractPreferencesAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link ExitAction}
     * </ul>
     */
    @java.lang.Override
    public void addExitItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.app.ExitAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link ClearFileAction}
     * </ul>
     */
    @java.lang.Override
    public void addClearFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.ClearFileAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link NewWindowAction}
     * </ul>
     */
    @java.lang.Override
    public void addNewWindowItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.NewWindowAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link NewFileAction}
     * </ul>
     */
    @java.lang.Override
    public void addNewFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.NewFileAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link LoadFileAction}
     *   <li>{@link LoadDirectoryAction}
     * </ul>
     */
    @java.lang.Override
    public void addLoadFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.LoadFileAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.app.action.file.LoadDirectoryAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link OpenFileAction}
     *   <li>{@link OpenDirectoryAction}
     * </ul>
     */
    @java.lang.Override
    public void addOpenFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.OpenFileAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.app.action.file.OpenDirectoryAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link CloseFileAction}
     * </ul>
     */
    @java.lang.Override
    public void addCloseFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.CloseFileAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link SaveFileAction}
     *   <li>{@link SaveFileAsAction}
     * </ul>
     */
    @java.lang.Override
    public void addSaveFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.SaveFileAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.app.action.file.SaveFileAsAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link ExportFileAction}
     * </ul>
     */
    @java.lang.Override
    public void addExportFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.ExportFileAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link PrintFileAction}
     * </ul>
     */
    @java.lang.Override
    public void addPrintFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.file.PrintFileAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addOtherFileItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link UndoAction}
     *   <li>{@link RedoAction}
     * </ul>
     */
    @java.lang.Override
    public void addUndoItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.action.edit.UndoAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.RedoAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link CutAction}
     *   <li>{@link CopyAction}
     *   <li>{@link PasteAction}
     *   <li>{@link DuplicateAction}
     *   <li>{@link DeleteAction}
     * </ul>
     */
    @java.lang.Override
    public void addClipboardItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.action.edit.CutAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.CopyAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.PasteAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.DuplicateAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.DeleteAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link SelectAllAction}
     *   <li>{@link ClearSelectionAction}
     * </ul>
     */
    @java.lang.Override
    public void addSelectionItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.action.edit.SelectAllAction.ID))) {
            add(m, a);
        }
        if (null != (a = am.get(org.jhotdraw.action.edit.ClearSelectionAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link AbstractFindAction}
     * </ul>
     */
    @java.lang.Override
    public void addFindItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.action.edit.AbstractFindAction.ID))) {
            add(m, a);
        }
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addOtherEditItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addOtherViewItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addOtherMenus(java.util.List<javax.swing.JMenu> m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addOtherWindowItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void addHelpItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
    }

    /**
     * Adds items for the following actions to the menu:
     *
     * <ul>
     *   <li>{@link AboutAction}
     * </ul>
     */
    @java.lang.Override
    public void addAboutItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap am = app.getActionMap(v);
        javax.swing.Action a;
        if (null != (a = am.get(org.jhotdraw.app.action.app.AboutAction.ID))) {
            add(m, a);
        }
    }

    public boolean isSuppressIcons() {
        return suppressIcons;
    }

    public void setSuppressIcons(boolean suppressIcons) {
        this.suppressIcons = suppressIcons;
    }

    /**
     * Adds an action to a menu. Returns the menu item that was added. This method is invoked for each
     * action that is added to a menu. Override this method to customize the menu item that is being
     * created.
     *
     * @param m
     * 		the menu
     * @param a
     * 		the action
     * @return the added menu item
     */
    protected javax.swing.JMenuItem add(javax.swing.JMenu m, javax.swing.Action a) {
        javax.swing.JMenuItem item = m.add(a);
        if (suppressIcons) {
            item.setIcon(null);
        }
        return item;
    }
}