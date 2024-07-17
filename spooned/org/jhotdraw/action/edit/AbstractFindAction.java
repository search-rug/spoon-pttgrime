/* @(#)AbstractFindAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.action.edit;
/**
 * Presents a find dialog to the user and then highlights the found items in the active view.
 *
 * <p>This action is called when the user selects the Find item in the Edit menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link ApplicationModel#initApplication}.
 */
public abstract class AbstractFindAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "edit.find";

    public AbstractFindAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, org.jhotdraw.action.edit.AbstractFindAction.ID);
    }
}