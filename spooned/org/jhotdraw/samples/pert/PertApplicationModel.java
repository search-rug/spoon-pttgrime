/* @(#)PertApplicationModel.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * Provides meta-data and factory methods for an application.
 *
 * <p>See {@link ApplicationModel} on how this class interacts with an application.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class PertApplicationModel extends org.jhotdraw.app.DefaultApplicationModel {
    private static final long serialVersionUID = 1L;

    private static final double[] SCALE_FACTORS = new double[]{ 5, 4, 3, 2, 1.5, 1.25, 1, 0.75, 0.5, 0.25, 0.1 };

    private static class ToolButtonListener implements java.awt.event.ItemListener {
        private org.jhotdraw.draw.tool.Tool tool;

        private org.jhotdraw.draw.DrawingEditor editor;

        public ToolButtonListener(org.jhotdraw.draw.tool.Tool t, org.jhotdraw.draw.DrawingEditor editor) {
            this.tool = t;
            this.editor = editor;
        }

        @java.lang.Override
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                editor.setTool(tool);
            }
        }
    }

    /**
     * This editor is shared by all views.
     */
    private org.jhotdraw.editor.DefaultDrawingEditor sharedEditor;

    private java.util.HashMap<java.lang.String, javax.swing.Action> actions;

    public PertApplicationModel() {
    }

    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap m = super.createActionMap(a, v);
        org.jhotdraw.util.ResourceBundleUtil drawLabels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        javax.swing.AbstractAction aa;
        m.put(org.jhotdraw.app.action.file.ExportFileAction.ID, new org.jhotdraw.app.action.file.ExportFileAction(a, v));
        m.put("view.toggleGrid", aa = new org.jhotdraw.action.view.ToggleViewPropertyAction(a, v, org.jhotdraw.samples.pert.PertView.GRID_VISIBLE_PROPERTY));
        drawLabels.configureAction(aa, "view.toggleGrid");
        for (double sf : org.jhotdraw.samples.pert.PertApplicationModel.SCALE_FACTORS) {
            m.put(((int) (sf * 100)) + "%", aa = new org.jhotdraw.action.view.ViewPropertyAction(a, v, org.jhotdraw.draw.DrawingView.SCALE_FACTOR_PROPERTY, java.lang.Double.TYPE, new java.lang.Double(sf)));
            aa.putValue(javax.swing.Action.NAME, ((int) (sf * 100)) + " %");
        }
        return m;
    }

    public org.jhotdraw.editor.DefaultDrawingEditor getSharedEditor() {
        if (sharedEditor == null) {
            sharedEditor = new org.jhotdraw.editor.DefaultDrawingEditor();
        }
        return sharedEditor;
    }

    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View p) {
        if (a.isSharingToolsAmongViews()) {
            ((org.jhotdraw.samples.pert.PertView) (p)).setEditor(getSharedEditor());
        }
    }

    private void addCreationButtonsTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor) {
        // AttributeKeys for the entitie sets
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
        org.jhotdraw.util.ResourceBundleUtil drawLabels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor);
        tb.addSeparator();
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.white);
        attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, java.awt.Color.black);
        attributes.put(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR, java.awt.Color.black);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.pert.figures.TaskFigure(), attributes), "edit.createTask", labels);
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, new java.awt.Color(0x99));
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.ConnectionTool(new org.jhotdraw.samples.pert.figures.DependencyFigure(), attributes), "edit.createDependency", labels);
        tb.addSeparator();
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.TextAreaCreationTool(new org.jhotdraw.draw.figure.TextAreaFigure()), "edit.createTextArea", drawLabels);
    }

    /**
     * Creates toolbars for the application. This class always returns an empty list. Subclasses may
     * return other values.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View pr) {
        org.jhotdraw.util.ResourceBundleUtil drawLabels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.samples.pert.PertView p = ((org.jhotdraw.samples.pert.PertView) (pr));
        org.jhotdraw.draw.DrawingEditor editor;
        if (p == null) {
            editor = getSharedEditor();
        } else {
            editor = p.getEditor();
        }
        java.util.LinkedList<javax.swing.JToolBar> list = new java.util.LinkedList<javax.swing.JToolBar>();
        javax.swing.JToolBar tb;
        tb = new javax.swing.JToolBar();
        addCreationButtonsTo(tb, editor);
        tb.setName(drawLabels.getString("window.drawToolBar.title"));
        list.add(tb);
        tb = new javax.swing.JToolBar();
        org.jhotdraw.gui.action.ButtonFactory.addAttributesButtonsTo(tb, editor);
        tb.setName(drawLabels.getString("window.attributesToolBar.title"));
        list.add(tb);
        tb = new javax.swing.JToolBar();
        org.jhotdraw.gui.action.ButtonFactory.addAlignmentButtonsTo(tb, editor);
        tb.setName(drawLabels.getString("window.alignmentToolBar.title"));
        list.add(tb);
        return list;
    }

    /**
     * Creates the MenuBuilder.
     */
    @java.lang.Override
    protected org.jhotdraw.api.app.MenuBuilder createMenuBuilder() {
        return new org.jhotdraw.app.DefaultMenuBuilder() {
            @java.lang.Override
            public void addOtherViewItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
                javax.swing.ActionMap am = app.getActionMap(v);
                javax.swing.JCheckBoxMenuItem cbmi;
                cbmi = new javax.swing.JCheckBoxMenuItem(am.get("view.toggleGrid"));
                org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get("view.toggleGrid"));
                m.add(cbmi);
                javax.swing.JMenu m2 = new javax.swing.JMenu("Zoom");
                for (double sf : org.jhotdraw.samples.pert.PertApplicationModel.SCALE_FACTORS) {
                    java.lang.String id = ((int) (sf * 100)) + "%";
                    cbmi = new javax.swing.JCheckBoxMenuItem(am.get(id));
                    org.jhotdraw.util.ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get(id));
                    m2.add(cbmi);
                }
                m.add(m2);
            }
        };
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        c.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Pert Diagram", "xml"));
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createSaveChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        c.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Pert Diagram", "xml"));
        return c;
    }
}