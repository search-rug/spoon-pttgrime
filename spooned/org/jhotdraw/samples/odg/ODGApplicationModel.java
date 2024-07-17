/* @(#)ODGApplicationModel.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * Provides meta-data and factory methods for an application.
 *
 * <p>See {@link ApplicationModel} on how this class interacts with an application.
 */
public class ODGApplicationModel extends org.jhotdraw.app.DefaultApplicationModel {
    private static final long serialVersionUID = 1L;

    private static final double[] SCALE_FACTORS = new double[]{ 5, 4, 3, 2, 1.5, 1.25, 1, 0.75, 0.5, 0.25, 0.1 };

    /**
     * This editor is shared by all views.
     */
    private org.jhotdraw.editor.DefaultDrawingEditor sharedEditor;

    public ODGApplicationModel() {
        setViewClass(org.jhotdraw.samples.odg.ODGView.class);
    }

    public org.jhotdraw.editor.DefaultDrawingEditor getSharedEditor() {
        if (sharedEditor == null) {
            sharedEditor = new org.jhotdraw.editor.DefaultDrawingEditor();
        }
        return sharedEditor;
    }

    public static java.util.Collection<javax.swing.Action> createDrawingActions(org.jhotdraw.draw.DrawingEditor editor) {
        java.util.LinkedList<javax.swing.Action> a = new java.util.LinkedList<javax.swing.Action>();
        a.add(new org.jhotdraw.action.edit.CutAction());
        a.add(new org.jhotdraw.action.edit.CopyAction());
        a.add(new org.jhotdraw.action.edit.PasteAction());
        a.add(new org.jhotdraw.action.edit.SelectAllAction());
        a.add(new org.jhotdraw.draw.action.SelectSameAction(editor));
        return a;
    }

    public static java.util.Collection<javax.swing.Action> createSelectionActions(org.jhotdraw.draw.DrawingEditor editor) {
        java.util.LinkedList<javax.swing.Action> a = new java.util.LinkedList<javax.swing.Action>();
        a.add(new org.jhotdraw.action.edit.DuplicateAction());
        a.add(null);// separator

        a.add(new org.jhotdraw.draw.action.GroupAction(editor, new org.jhotdraw.samples.odg.figures.ODGGroupFigure()));
        a.add(new org.jhotdraw.draw.action.UngroupAction(editor, new org.jhotdraw.samples.odg.figures.ODGGroupFigure()));
        a.add(new org.jhotdraw.samples.svg.action.CombineAction(editor));
        a.add(new org.jhotdraw.samples.svg.action.SplitAction(editor));
        a.add(null);// separator

        a.add(new org.jhotdraw.draw.action.BringToFrontAction(editor));
        a.add(new org.jhotdraw.draw.action.SendToBackAction(editor));
        return a;
    }

    private void addCreationButtonsTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor) {
        // AttributeKeys for the entitie sets
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;
        org.jhotdraw.util.ResourceBundleUtil drawLabels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor, org.jhotdraw.samples.odg.ODGApplicationModel.createDrawingActions(editor), org.jhotdraw.samples.odg.ODGApplicationModel.createSelectionActions(editor));
        tb.addSeparator();
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.odg.figures.ODGRectFigure(), attributes), "edit.createRectangle", drawLabels);
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
        attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, java.awt.Color.black);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.odg.figures.ODGPathFigure(), attributes), "edit.createLine", drawLabels);
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.black);
        attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
        attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
        attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
    }

    /**
     * Creates toolbar buttons and adds them to the specified JToolBar
     */
    private void addAttributesButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        javax.swing.JButton b;
        b = bar.add(new org.jhotdraw.draw.action.PickAttributesAction(editor));
        b.setFocusable(false);
        b = bar.add(new org.jhotdraw.draw.action.ApplyAttributesAction(editor));
        b.setFocusable(false);
        bar.addSeparator();
        addColorButtonsTo(bar, editor);
        bar.addSeparator();
        addStrokeButtonsTo(bar, editor);
        bar.addSeparator();
        org.jhotdraw.gui.action.ButtonFactory.addFontButtonsTo(bar, editor);
    }

    private void addColorButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.put(defaultAttributes, ((org.jhotdraw.samples.odg.Gradient) (null)));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, org.jhotdraw.gui.action.ButtonFactory.WEBSAVE_COLORS, org.jhotdraw.gui.action.ButtonFactory.WEBSAVE_COLORS_COLUMN_COUNT, "attribute.strokeColor", labels, defaultAttributes));
        defaultAttributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.put(defaultAttributes, ((org.jhotdraw.samples.odg.Gradient) (null)));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, org.jhotdraw.gui.action.ButtonFactory.WEBSAVE_COLORS, org.jhotdraw.gui.action.ButtonFactory.WEBSAVE_COLORS_COLUMN_COUNT, "attribute.fillColor", labels, defaultAttributes));
    }

    private void addStrokeButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeWidthButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor));
    }

    /**
     * Creates toolbars for the application.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View pr) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.samples.odg.ODGView p = ((org.jhotdraw.samples.odg.ODGView) (pr));
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
        tb.setName(labels.getString("window.drawToolBar.title"));
        list.add(tb);
        tb = new javax.swing.JToolBar();
        addAttributesButtonsTo(tb, editor);
        tb.setName(labels.getString("window.attributesToolBar.title"));
        list.add(tb);
        tb = new javax.swing.JToolBar();
        org.jhotdraw.gui.action.ButtonFactory.addAlignmentButtonsTo(tb, editor);
        tb.setName(labels.getString("window.alignmentToolBar.title"));
        list.add(tb);
        return list;
    }

    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View p) {
        if (a.isSharingToolsAmongViews()) {
            ((org.jhotdraw.samples.odg.ODGView) (p)).setEditor(getSharedEditor());
        }
    }

    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        javax.swing.ActionMap m = super.createActionMap(a, v);
        org.jhotdraw.util.ResourceBundleUtil drawLabels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        javax.swing.AbstractAction aa;
        m.put(org.jhotdraw.app.action.file.ExportFileAction.ID, new org.jhotdraw.app.action.file.ExportFileAction(a, v));
        m.put("view.toggleGrid", aa = new org.jhotdraw.action.view.ToggleViewPropertyAction(a, v, org.jhotdraw.samples.odg.ODGView.GRID_VISIBLE_PROPERTY));
        drawLabels.configureAction(aa, "view.toggleGrid");
        for (double sf : org.jhotdraw.samples.odg.ODGApplicationModel.SCALE_FACTORS) {
            m.put(((int) (sf * 100)) + "%", aa = new org.jhotdraw.action.view.ViewPropertyAction(a, v, "scaleFactor", java.lang.Double.TYPE, new java.lang.Double(sf)));
            aa.putValue(javax.swing.Action.NAME, ((int) (sf * 100)) + " %");
        }
        return m;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View view) {
        final org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat> fileFilterInputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat>();
        c.putClientProperty("ffInputFormatMap", fileFilterInputFormatMap);
        javax.swing.filechooser.FileFilter firstFF = null;
        if (view == null) {
            view = new org.jhotdraw.samples.odg.ODGView();
        }
        for (org.jhotdraw.draw.io.InputFormat format : ((org.jhotdraw.samples.odg.ODGView) (view)).getDrawing().getInputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            if (firstFF == null) {
                firstFF = ff;
            }
            fileFilterInputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
        }
        c.setFileFilter(firstFF);
        c.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("fileFilterChanged".equals(evt.getPropertyName())) {
                    org.jhotdraw.draw.io.InputFormat inputFormat = fileFilterInputFormatMap.get(evt.getNewValue());
                    c.setAccessory(null);
                }
            }
        });
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createSaveChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View view) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat> fileFilterOutputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat>();
        c.putClientProperty("ffOutputFormatMap", fileFilterOutputFormatMap);
        if (view == null) {
            view = new org.jhotdraw.samples.odg.ODGView();
        }
        for (org.jhotdraw.draw.io.OutputFormat format : ((org.jhotdraw.samples.odg.ODGView) (view)).getDrawing().getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            break;// only add the first uri filter

        }
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createExportChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View view) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat> fileFilterOutputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat>();
        c.putClientProperty("ffExportFormatMap", fileFilterOutputFormatMap);
        if (view == null) {
            view = new org.jhotdraw.samples.odg.ODGView();
        }
        for (org.jhotdraw.draw.io.OutputFormat format : ((org.jhotdraw.samples.odg.ODGView) (view)).getDrawing().getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            break;// only add the first uri filter

        }
        return c;
    }
}