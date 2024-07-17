/* @(#)DrawToolsPane.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * DrawToolsPane.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class ToolsToolBar extends org.jhotdraw.samples.svg.gui.AbstractToolBar {
    private static final long serialVersionUID = 1L;

    /**
     * Creates new instance.
     */
    public ToolsToolBar() {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("tools.toolbar"));
    }

    @java.lang.Override
    protected javax.swing.JComponent createDisclosedComponent(int state) {
        javax.swing.JPanel p = null;
        switch (state) {
            case 1 :
                p = new javax.swing.JPanel();
                p.setOpaque(false);
                p.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 8));
                // Abort if no editor is set
                if (editor == null) {
                    break;
                }
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
                java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
                p.setLayout(layout);
                java.awt.GridBagConstraints gbc;
                javax.swing.AbstractButton btn;
                org.jhotdraw.draw.tool.CreationTool creationTool;
                org.jhotdraw.samples.svg.PathTool pathTool;
                org.jhotdraw.draw.tool.TextCreationTool textTool;
                org.jhotdraw.draw.tool.TextAreaCreationTool textAreaTool;
                org.jhotdraw.samples.svg.SVGCreateFromFileTool imageTool;
                java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;
                btn = org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(this, editor, org.jhotdraw.gui.action.ButtonFactory.createDrawingActions(editor, disposables), createSelectionActions(editor));
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                btn.addMouseListener(new org.jhotdraw.samples.svg.gui.ToolsToolBar.SelectionToolButtonHandler(editor));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                p.add(btn, gbc);
                labels.configureToolBarButton(btn, "selectionTool");
                attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, creationTool = new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.svg.figures.SVGRectFigure(), attributes), "createRectangle", labels);
                creationTool.setToolDoneAfterCreation(false);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, creationTool = new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.svg.figures.SVGEllipseFigure(), attributes), "createEllipse", labels);
                creationTool.setToolDoneAfterCreation(false);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, pathTool = new org.jhotdraw.samples.svg.PathTool(new org.jhotdraw.samples.svg.figures.SVGPathFigure(), new org.jhotdraw.samples.svg.figures.SVGBezierFigure(true), attributes), "createPolygon", labels);
                pathTool.setToolDoneAfterCreation(false);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 1;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
                attributes.put(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED, false);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, creationTool = new org.jhotdraw.draw.tool.CreationTool(new org.jhotdraw.samples.svg.figures.SVGPathFigure(), attributes), "createLine", labels);
                creationTool.setToolDoneAfterCreation(false);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(btn, gbc);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, pathTool = new org.jhotdraw.samples.svg.PathTool(new org.jhotdraw.samples.svg.figures.SVGPathFigure(), new org.jhotdraw.samples.svg.figures.SVGBezierFigure(false), attributes), "createScribble", labels);
                pathTool.setToolDoneAfterCreation(false);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.insets = new java.awt.Insets(0, 3, 0, 0);
                p.add(btn, gbc);
                attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.black);
                attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, textTool = new org.jhotdraw.draw.tool.TextCreationTool(new org.jhotdraw.samples.svg.figures.SVGTextFigure(), attributes), "createText", labels);
                textTool.setToolDoneAfterCreation(true);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 0, 0, 0);
                p.add(btn, gbc);
                textAreaTool = new org.jhotdraw.draw.tool.TextAreaCreationTool(new org.jhotdraw.samples.svg.figures.SVGTextAreaFigure(), attributes);
                textAreaTool.setRubberbandColor(java.awt.Color.BLACK);
                textAreaTool.setToolDoneAfterCreation(true);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, textAreaTool, "createTextArea", labels);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
                attributes.put(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
                attributes.put(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
                btn = org.jhotdraw.gui.action.ButtonFactory.addToolTo(this, editor, imageTool = new org.jhotdraw.samples.svg.SVGCreateFromFileTool(new org.jhotdraw.samples.svg.figures.SVGImageFigure(), new org.jhotdraw.samples.svg.figures.SVGGroupFigure(), attributes), "createImage", labels);
                imageTool.setToolDoneAfterCreation(true);
                imageTool.setUseFileDialog(true);
                btn.setUI(((org.jhotdraw.gui.plaf.palette.PaletteButtonUI) (org.jhotdraw.gui.plaf.palette.PaletteButtonUI.createUI(btn))));
                gbc = new java.awt.GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 2;
                gbc.insets = new java.awt.Insets(3, 3, 0, 0);
                p.add(btn, gbc);
                break;
        }
        return p;
    }

    public java.util.Collection<javax.swing.Action> createSelectionActions(org.jhotdraw.draw.DrawingEditor editor) {
        java.util.LinkedList<javax.swing.Action> list = new java.util.LinkedList<javax.swing.Action>();
        org.jhotdraw.draw.action.AbstractSelectedAction a;
        list.add(new org.jhotdraw.action.edit.DuplicateAction());
        list.add(null);// separator

        list.add(a = new org.jhotdraw.draw.action.GroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        disposables.add(a);
        list.add(a = new org.jhotdraw.draw.action.UngroupAction(editor, new org.jhotdraw.samples.svg.figures.SVGGroupFigure()));
        disposables.add(a);
        list.add(a = new org.jhotdraw.samples.svg.action.CombineAction(editor));
        disposables.add(a);
        list.add(a = new org.jhotdraw.samples.svg.action.SplitAction(editor));
        disposables.add(a);
        list.add(null);// separator

        list.add(a = new org.jhotdraw.draw.action.BringToFrontAction(editor));
        disposables.add(a);
        list.add(a = new org.jhotdraw.draw.action.SendToBackAction(editor));
        disposables.add(a);
        return list;
    }

    @java.lang.Override
    protected java.lang.String getID() {
        return "tools";
    }

    @java.lang.Override
    protected int getDefaultDisclosureState() {
        return 1;
    }

    private static class SelectionToolButtonHandler extends java.awt.event.MouseAdapter {
        private org.jhotdraw.draw.DrawingEditor editor;

        private boolean wasSelectedOnPressed = false;

        public SelectionToolButtonHandler(org.jhotdraw.draw.DrawingEditor editor) {
            this.editor = editor;
        }

        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (wasSelectedOnPressed) {
                org.jhotdraw.draw.DrawingView view = editor.getActiveView();
                if (view != null) {
                    view.setHandleDetailLevel(view.getHandleDetailLevel() + 1);
                }
            }
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            // Note: we blindly assume here that selection changes occur on mouse release!!
            wasSelectedOnPressed = ((javax.swing.AbstractButton) (e.getSource())).isSelected();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents

}