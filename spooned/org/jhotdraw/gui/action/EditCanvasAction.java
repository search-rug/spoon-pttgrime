/* @(#)EditCanvasAction.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.action;
/**
 * EditCanvasAction.
 *
 * <p>XXX - We shouldn't have a dependency to the application framework from within the drawing
 * framework.
 */
public class EditCanvasAction extends org.jhotdraw.draw.action.AbstractDrawingViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "view.editCanvas";

    private javax.swing.JFrame frame;

    private org.jhotdraw.gui.action.EditCanvasPanel settingsPanel;

    private java.beans.PropertyChangeListener propertyChangeHandler;

    private org.jhotdraw.api.app.Application app;

    public EditCanvasAction(org.jhotdraw.api.app.Application app, org.jhotdraw.draw.DrawingEditor editor) {
        super(editor);
        this.app = app;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, org.jhotdraw.gui.action.EditCanvasAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        getFrame().setVisible(true);
    }

    @java.lang.Override
    protected void updateViewState() {
        if ((getView() != null) && (settingsPanel != null)) {
            settingsPanel.setDrawing(getView().getDrawing());
        }
    }

    protected org.jhotdraw.api.app.Application getApplication() {
        return app;
    }

    protected javax.swing.JFrame getFrame() {
        if (frame == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            frame = new javax.swing.JFrame();
            frame.setTitle(labels.getString("window.editCanvas.title"));
            frame.setResizable(false);
            settingsPanel = new org.jhotdraw.gui.action.EditCanvasPanel();
            frame.add(settingsPanel);
            frame.pack();
            java.util.prefs.Preferences prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
            org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(prefs, "canvasSettings", frame);
            getApplication().addWindow(frame, null);
        }
        settingsPanel.setDrawing(getView().getDrawing());
        return frame;
    }
}