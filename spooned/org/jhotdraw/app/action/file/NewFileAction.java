/* @(#)NewFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
/**
 * Creates a new view.
 *
 * <p>This action is called when the user selects the New item in the File menu.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>This action is designed for applications which automatically create a new view for each opened
 * file. This action goes together with {@link OpenFileAction} and {@link CloseFileAction}. It
 * should not be used together with {@link NewWindowAction}.
 */
public class NewFileAction extends org.jhotdraw.action.AbstractApplicationAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.new";

    public NewFileAction(org.jhotdraw.api.app.Application app) {
        this(app, org.jhotdraw.app.action.file.NewFileAction.ID);
    }

    public NewFileAction(org.jhotdraw.api.app.Application app, java.lang.String id) {
        super(app);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, id);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.api.app.Application app = getApplication();
        final org.jhotdraw.api.app.View newView = app.createView();
        int multiOpenId = 1;
        for (org.jhotdraw.api.app.View existingP : app.views()) {
            if (existingP.getURI() == null) {
                multiOpenId = java.lang.Math.max(multiOpenId, existingP.getMultipleOpenId() + 1);
            }
        }
        newView.setMultipleOpenId(multiOpenId);
        app.add(newView);
        newView.execute(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                newView.clear();
            }
        });
        app.show(newView);
    }
}