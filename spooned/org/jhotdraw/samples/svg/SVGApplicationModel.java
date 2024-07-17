/* @(#)SVGApplicationModel.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * Provides meta-data and factory methods for an application.
 *
 * <p>See {@link ApplicationModel} on how this class interacts with an application.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class SVGApplicationModel extends org.jhotdraw.app.DefaultApplicationModel {
    private static final long serialVersionUID = 1L;

    /**
     * Client property on the URIFileChooser.
     */
    public static final java.lang.String INPUT_FORMAT_MAP_CLIENT_PROPERTY = "InputFormatMap";

    /**
     * Client property on the URIFileChooser.
     */
    public static final java.lang.String OUTPUT_FORMAT_MAP_CLIENT_PROPERTY = "OutputFormatMap";

    private static final double[] SCALE_FACTORS = new double[]{ 5, 4, 3, 2, 1.5, 1.25, 1, 0.75, 0.5, 0.25, 0.1 };

    private org.jhotdraw.draw.constrainer.GridConstrainer gridConstrainer;

    /**
     * This editor is shared by all views.
     */
    private org.jhotdraw.editor.DefaultDrawingEditor sharedEditor;

    public SVGApplicationModel() {
        gridConstrainer = new org.jhotdraw.draw.constrainer.GridConstrainer(12, 12);
    }

    public org.jhotdraw.editor.DefaultDrawingEditor getSharedEditor() {
        if (sharedEditor == null) {
            sharedEditor = new org.jhotdraw.editor.DefaultDrawingEditor();
        }
        return sharedEditor;
    }

    @java.lang.Override
    public void initView(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View view) {
        org.jhotdraw.samples.svg.SVGView v = ((org.jhotdraw.samples.svg.SVGView) (view));
        org.jhotdraw.draw.DrawingEditor editor;
        if (a.isSharingToolsAmongViews()) {
            v.setEditor(editor = getSharedEditor());
        } else {
            v.setEditor(editor = new org.jhotdraw.editor.DefaultDrawingEditor());
        }
        org.jhotdraw.draw.action.AbstractSelectedAction action;
        javax.swing.ActionMap m = view.getActionMap();
        m.put(org.jhotdraw.draw.action.SelectSameAction.ID, new org.jhotdraw.draw.action.SelectSameAction(editor));
        m.put(org.jhotdraw.draw.action.GroupAction.ID, new org.jhotdraw.draw.action.GroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        m.put(org.jhotdraw.draw.action.UngroupAction.ID, new org.jhotdraw.draw.action.UngroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        m.put(org.jhotdraw.samples.svg.action.CombineAction.ID, new org.jhotdraw.samples.svg.action.CombineAction(editor));
        m.put(org.jhotdraw.samples.svg.action.SplitAction.ID, new org.jhotdraw.samples.svg.action.SplitAction(editor));
        m.put(org.jhotdraw.draw.action.BringToFrontAction.ID, new org.jhotdraw.draw.action.BringToFrontAction(editor));
        m.put(org.jhotdraw.draw.action.SendToBackAction.ID, new org.jhotdraw.draw.action.SendToBackAction(editor));
        // view.addDisposable(action);
    }

    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View view) {
        org.jhotdraw.samples.svg.SVGView v = ((org.jhotdraw.samples.svg.SVGView) (view));
        javax.swing.ActionMap m = super.createActionMap(a, v);
        javax.swing.AbstractAction aa;
        m.put(org.jhotdraw.action.edit.ClearSelectionAction.ID, new org.jhotdraw.action.edit.ClearSelectionAction());
        m.put(org.jhotdraw.samples.svg.action.ViewSourceAction.ID, new org.jhotdraw.samples.svg.action.ViewSourceAction(a, v));
        m.put(org.jhotdraw.app.action.file.ExportFileAction.ID, new org.jhotdraw.app.action.file.ExportFileAction(a, v));
        if (v instanceof org.jhotdraw.samples.svg.SVGView) {
            org.jhotdraw.samples.svg.SVGView svgView = v;
            m.put(org.jhotdraw.action.edit.UndoAction.ID, svgView.getUndoManager().getUndoAction());
            m.put(org.jhotdraw.action.edit.RedoAction.ID, svgView.getUndoManager().getRedoAction());
        }
        org.jhotdraw.draw.DrawingEditor editor;
        if (a.isSharingToolsAmongViews()) {
            editor = getSharedEditor();
        } else {
            editor = (v == null) ? null : v.getEditor();
        }
        m.put(org.jhotdraw.draw.action.SelectSameAction.ID, new org.jhotdraw.draw.action.SelectSameAction(editor));
        m.put(org.jhotdraw.draw.action.GroupAction.ID, new org.jhotdraw.draw.action.GroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        m.put(org.jhotdraw.draw.action.UngroupAction.ID, new org.jhotdraw.draw.action.UngroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        m.put(org.jhotdraw.samples.svg.action.CombineAction.ID, new org.jhotdraw.samples.svg.action.CombineAction(editor));
        m.put(org.jhotdraw.samples.svg.action.SplitAction.ID, new org.jhotdraw.samples.svg.action.SplitAction(editor));
        m.put(org.jhotdraw.draw.action.BringToFrontAction.ID, new org.jhotdraw.draw.action.BringToFrontAction(editor));
        m.put(org.jhotdraw.draw.action.SendToBackAction.ID, new org.jhotdraw.draw.action.SendToBackAction(editor));
        return m;
    }

    /**
     * Creates the MenuBuilder.
     */
    @java.lang.Override
    protected org.jhotdraw.api.app.MenuBuilder createMenuBuilder() {
        return new org.jhotdraw.app.DefaultMenuBuilder() {
            @java.lang.Override
            public void addSelectionItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
                javax.swing.ActionMap am = app.getActionMap(v);
                super.addSelectionItems(m, app, v);
                m.add(am.get(org.jhotdraw.draw.action.SelectSameAction.ID));
            }

            @java.lang.Override
            public void addOtherEditItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
                javax.swing.ActionMap am = app.getActionMap(v);
                m.add(am.get(org.jhotdraw.draw.action.GroupAction.ID));
                m.add(am.get(org.jhotdraw.draw.action.UngroupAction.ID));
                m.add(am.get(org.jhotdraw.samples.svg.action.CombineAction.ID));
                m.add(am.get(org.jhotdraw.samples.svg.action.SplitAction.ID));
                m.addSeparator();
                m.add(am.get(org.jhotdraw.draw.action.BringToFrontAction.ID));
                m.add(am.get(org.jhotdraw.draw.action.SendToBackAction.ID));
            }

            @java.lang.Override
            public void addOtherViewItems(javax.swing.JMenu m, org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
                javax.swing.ActionMap am = app.getActionMap(v);
                m.add(am.get(org.jhotdraw.samples.svg.action.ViewSourceAction.ID));
            }
        };
    }

    /**
     * Overriden to create no toolbars.
     *
     * @param app
     * @param p
     * @return An empty list.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View p) {
        java.util.LinkedList<javax.swing.JToolBar> list = new java.util.LinkedList<javax.swing.JToolBar>();
        return list;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        final org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat> fileFilterInputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.InputFormat>();
        c.putClientProperty(org.jhotdraw.samples.svg.SVGApplicationModel.INPUT_FORMAT_MAP_CLIENT_PROPERTY, fileFilterInputFormatMap);
        javax.swing.filechooser.FileFilter firstFF = null;
        if (v == null) {
            v = new org.jhotdraw.samples.svg.SVGView();
        }
        org.jhotdraw.draw.Drawing d = ((org.jhotdraw.samples.svg.SVGView) (v)).getDrawing();
        if (d == null) {
            d = ((org.jhotdraw.samples.svg.SVGView) (v)).createDrawing();
        }
        for (org.jhotdraw.draw.io.InputFormat format : d.getInputFormats()) {
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
    public org.jhotdraw.api.gui.URIChooser createSaveChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat> fileFilterOutputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat>();
        c.putClientProperty(org.jhotdraw.samples.svg.SVGApplicationModel.OUTPUT_FORMAT_MAP_CLIENT_PROPERTY, fileFilterOutputFormatMap);
        if (v == null) {
            v = new org.jhotdraw.samples.svg.SVGView();
        }
        org.jhotdraw.draw.Drawing d = ((org.jhotdraw.samples.svg.SVGView) (v)).getDrawing();
        for (org.jhotdraw.draw.io.OutputFormat format : d.getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            break;// only add the first uri filter

        }
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createExportChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        final java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat> fileFilterOutputFormatMap = new java.util.HashMap<javax.swing.filechooser.FileFilter, org.jhotdraw.draw.io.OutputFormat>();
        c.putClientProperty("ffOutputFormatMap", fileFilterOutputFormatMap);
        if (v == null) {
            v = new org.jhotdraw.samples.svg.SVGView();
        }
        org.jhotdraw.draw.Drawing d = ((org.jhotdraw.samples.svg.SVGView) (v)).getDrawing();
        javax.swing.filechooser.FileFilter currentFilter = null;
        for (org.jhotdraw.draw.io.OutputFormat format : d.getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            // FIXME use preferences
            /* if (ff.getDescription().equals(preferences.get("viewExportFormat", ""))) {
            currentFilter = ff;
            }
             */
        }
        if (currentFilter != null) {
            c.setFileFilter(currentFilter);
        }
        return c;
    }
}