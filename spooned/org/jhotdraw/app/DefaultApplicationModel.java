/* @(#)DefaultApplicationModel.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
import org.jhotdraw.action.edit.ClearSelectionAction;
import org.jhotdraw.action.edit.CopyAction;
import org.jhotdraw.action.edit.CutAction;
import org.jhotdraw.action.edit.DeleteAction;
import org.jhotdraw.action.edit.DuplicateAction;
import org.jhotdraw.action.edit.PasteAction;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.action.edit.SelectAllAction;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.app.action.file.CloseFileAction;
import org.jhotdraw.app.action.file.NewFileAction;
import org.jhotdraw.app.action.file.OpenFileAction;
import org.jhotdraw.app.action.file.SaveFileAction;
import org.jhotdraw.app.action.file.SaveFileAsAction;
/**
 * An {@link ApplicationModel} which creates a default set of {@code Action}s and which does not
 * override any of the default menu bars nor create tool bars.
 *
 * <p>The following actions are created by the {@code createActionMap} method of this model:
 *
 * <ul>
 *   <li>{@link NewFileAction}
 *   <li>{@link OpenFileAction}
 *   <li>{@link SaveFileAction}
 *   <li>{@link SaveFileAsAction}
 *   <li>{@link CloseFileAction}
 *   <li>{@link UndoAction}
 *   <li>{@link RedoAction}
 *   <li>{@link CutAction}
 *   <li>{@link CopyAction}
 *   <li>{@link PasteAction}
 *   <li>{@link DeleteAction}
 *   <li>{@link DuplicateAction}
 *   <li>{@link SelectAllAction}
 *   <li>{@link ClearSelectionAction}
 * </ul>
 *
 * <p>The {@code createMenu...} methods of this model return null, resulting in a set of default
 * menu bars created by the {@link Application} which holds this model.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class DefaultApplicationModel extends org.jhotdraw.app.AbstractApplicationModel {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.api.app.MenuBuilder menuBuilder;

    /**
     * Does nothing.
     */
    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
    }

    /**
     * Returns an {@code ActionMap} with a default set of actions (See class comments).
     */
    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap m = new javax.swing.ActionMap();
        m.put(org.jhotdraw.app.action.file.NewFileAction.ID, new org.jhotdraw.app.action.file.NewFileAction(a));
        m.put(org.jhotdraw.app.action.file.OpenFileAction.ID, new org.jhotdraw.app.action.file.OpenFileAction(a));
        m.put(org.jhotdraw.app.action.file.SaveFileAction.ID, new org.jhotdraw.app.action.file.SaveFileAction(a, v));
        m.put(org.jhotdraw.app.action.file.SaveFileAsAction.ID, new org.jhotdraw.app.action.file.SaveFileAsAction(a, v));
        m.put(org.jhotdraw.app.action.file.CloseFileAction.ID, new org.jhotdraw.app.action.file.CloseFileAction(a, v));
        m.put(org.jhotdraw.action.edit.UndoAction.ID, new org.jhotdraw.action.edit.UndoAction(a, v));
        m.put(org.jhotdraw.action.edit.RedoAction.ID, new org.jhotdraw.action.edit.RedoAction(a, v));
        m.put(org.jhotdraw.action.edit.CutAction.ID, new org.jhotdraw.action.edit.CutAction());
        m.put(org.jhotdraw.action.edit.CopyAction.ID, new org.jhotdraw.action.edit.CopyAction());
        m.put(org.jhotdraw.action.edit.PasteAction.ID, new org.jhotdraw.action.edit.PasteAction());
        m.put(org.jhotdraw.action.edit.DeleteAction.ID, new org.jhotdraw.action.edit.DeleteAction());
        m.put(org.jhotdraw.action.edit.DuplicateAction.ID, new org.jhotdraw.action.edit.DuplicateAction());
        m.put(org.jhotdraw.action.edit.SelectAllAction.ID, new org.jhotdraw.action.edit.SelectAllAction());
        m.put(org.jhotdraw.action.edit.ClearSelectionAction.ID, new org.jhotdraw.action.edit.ClearSelectionAction());
        return m;
    }

    /**
     * Returns an empty unmodifiable list.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View p) {
        return java.util.Collections.emptyList();
    }

    /**
     * Creates the DefaultMenuBuilder.
     */
    protected org.jhotdraw.api.app.MenuBuilder createMenuBuilder() {
        return new org.jhotdraw.app.DefaultMenuBuilder();
    }

    @java.lang.Override
    public org.jhotdraw.api.app.MenuBuilder getMenuBuilder() {
        if (menuBuilder == null) {
            menuBuilder = createMenuBuilder();
        }
        return menuBuilder;
    }

    public void setMenuBuilder(org.jhotdraw.api.app.MenuBuilder newValue) {
        menuBuilder = newValue;
    }
}