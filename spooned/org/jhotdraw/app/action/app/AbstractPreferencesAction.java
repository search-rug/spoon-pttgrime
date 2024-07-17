/* @(#)AbstractPreferencesAction.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.app;
/**
 * Displays a preferences dialog for the application.
 *
 * <p>This action is called when the user selects the Preferences item in the Application menu. The
 * menu item is automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create an action with this ID and
 * put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 */
public abstract class AbstractPreferencesAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "application.preferences";

    public AbstractPreferencesAction(org.jhotdraw.api.app.Application app) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.app.AbstractPreferencesAction.ID);
    }
}