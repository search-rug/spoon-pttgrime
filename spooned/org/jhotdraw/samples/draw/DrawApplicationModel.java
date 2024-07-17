/* @(#)DrawApplicationModel.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.draw;
import org.jhotdraw.api.app.ApplicationModel;
/**
 * Provides factory methods for creating views, menu bars and toolbars.
 *
 * <p>See {@link ApplicationModel} on how this class interacts with an application.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class DrawApplicationModel extends org.jhotdraw.app.DefaultApplicationModel {
    private static final long serialVersionUID = 1L;

    /**
     * This editor is shared by all views.
     */
    private org.jhotdraw.editor.DefaultDrawingEditor sharedEditor;

    public DrawApplicationModel() {
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
            ((org.jhotdraw.samples.draw.DrawView) (p)).setEditor(getSharedEditor());
        }
    }

    /**
     * Creates toolbars for the application. This class always returns an empty list. Subclasses may
     * return other values.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View pr) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.samples.draw.DrawView p = ((org.jhotdraw.samples.draw.DrawView) (pr));
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
        org.jhotdraw.gui.action.ButtonFactory.addAttributesButtonsTo(tb, editor);
        tb.setName(labels.getString("window.attributesToolBar.title"));
        list.add(tb);
        tb = new javax.swing.JToolBar();
        org.jhotdraw.gui.action.ButtonFactory.addAlignmentButtonsTo(tb, editor);
        tb.setName(labels.getString("window.alignmentToolBar.title"));
        list.add(tb);
        return list;
    }

    private void addCreationButtonsTo(javax.swing.JToolBar tb, org.jhotdraw.draw.DrawingEditor editor) {
        addDefaultCreationButtonsTo(tb, editor, org.jhotdraw.gui.action.ButtonFactory.createDrawingActions(editor), org.jhotdraw.gui.action.ButtonFactory.createSelectionActions(editor));
    }

    public void addDefaultCreationButtonsTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor, java.util.Collection<javax.swing.Action> drawingActions, java.util.Collection<javax.swing.Action> selectionActions) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor, drawingActions, selectionActions);
        tb.addSeparator();
        org.jhotdraw.draw.figure.AbstractAttributedFigure af;
        org.jhotdraw.draw.tool.CreationTool ct;
        org.jhotdraw.draw.tool.ConnectionTool cnt;
        org.jhotdraw.draw.figure.ConnectionFigure lc;
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.RectangleFigure()), "edit.createRectangle", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.RoundRectangleFigure()), "edit.createRoundRectangle", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.EllipseFigure()), "edit.createEllipse", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.DiamondFigure()), "edit.createDiamond", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.TriangleFigure()), "edit.createTriangle", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.LineFigure()), "edit.createLine", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, ct = new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.draw.figure.LineFigure()), "edit.createArrow", labels);
        af = ((org.jhotdraw.draw.figure.AbstractAttributedFigure) (ct.getPrototype()));
        af.attr().set(org.jhotdraw.draw.AttributeKeys.END_DECORATION, new org.jhotdraw.draw.decoration.ArrowTip(0.35, 12, 11.3));
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.ConnectionTool(new org.jhotdraw.draw.figure.LineConnectionFigure()), "edit.createLineConnection", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, cnt = new org.jhotdraw.draw.tool.ConnectionTool(new org.jhotdraw.draw.figure.LineConnectionFigure()), "edit.createElbowConnection", labels);
        lc = cnt.getPrototype();
        lc.setLiner(new org.jhotdraw.draw.liner.ElbowLiner());
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, cnt = new org.jhotdraw.draw.tool.ConnectionTool(new org.jhotdraw.draw.figure.LineConnectionFigure()), "edit.createCurvedConnection", labels);
        lc = cnt.getPrototype();
        lc.setLiner(new org.jhotdraw.draw.liner.CurvedLiner());
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.BezierTool(new org.jhotdraw.draw.figure.BezierFigure()), "edit.createScribble", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.BezierTool(new org.jhotdraw.draw.figure.BezierFigure(true)), "edit.createPolygon", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.TextCreationTool(new org.jhotdraw.draw.figure.TextFigure()), "edit.createText", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.TextAreaCreationTool(new org.jhotdraw.draw.figure.TextAreaFigure()), "edit.createTextArea", labels);
        org.jhotdraw.gui.action.ButtonFactory.addToolTo(tb, editor, new org.jhotdraw.draw.tool.ImageTool(new org.jhotdraw.draw.figure.ImageFigure()), "edit.createImage", labels);
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createOpenChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        c.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Drawing .xml", "xml"));
        return c;
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.URIChooser createSaveChooser(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        org.jhotdraw.gui.JFileURIChooser c = new org.jhotdraw.gui.JFileURIChooser();
        c.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Drawing .xml", "xml"));
        return c;
    }
}