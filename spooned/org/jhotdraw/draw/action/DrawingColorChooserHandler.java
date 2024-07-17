/**
 *
 * @(#)DrawingColorChooserHandler.java <p>Copyright (c) 2010 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * DrawingColorChooserHandler.
 */
public class DrawingColorChooserHandler extends org.jhotdraw.draw.action.AbstractDrawingViewAction implements javax.swing.event.ChangeListener {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.draw.AttributeKey<java.awt.Color> key;

    protected javax.swing.JColorChooser colorChooser;

    protected javax.swing.JPopupMenu popupMenu;

    protected int isUpdating;

    // protected Map<AttributeKey, Object> attributes;
    public DrawingColorChooserHandler(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, javax.swing.JColorChooser colorChooser, javax.swing.JPopupMenu popupMenu) {
        super(editor);
        this.key = key;
        this.colorChooser = colorChooser;
        this.popupMenu = popupMenu;
        // colorChooser.addActionListener(this);
        colorChooser.getSelectionModel().addChangeListener(this);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        /* if (evt.getActionCommand() == JColorChooser.APPROVE_SELECTION) {
        applySelectedColorToFigures();
        } else if (evt.getActionCommand() == JColorChooser.CANCEL_SELECTION) {
        }
         */
        popupMenu.setVisible(false);
    }

    protected void applySelectedColorToFigures() {
        final org.jhotdraw.draw.Drawing drawing = getView().getDrawing();
        java.awt.Color selectedColor = colorChooser.getColor();
        if ((selectedColor != null) && (selectedColor.getAlpha() == 0)) {
            selectedColor = null;
        }
        final java.lang.Object restoreData = drawing.attr().getAttributesRestoreData();
        drawing.willChange();
        drawing.attr().set(key, selectedColor);
        drawing.changed();
        getEditor().setDefaultAttribute(key, selectedColor);
        final java.awt.Color undoValue = selectedColor;
        javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                return org.jhotdraw.draw.AttributeKeys.FONT_FACE.getPresentationName();
                /* String name = (String) getValue(Actions.UNDO_PRESENTATION_NAME_KEY);
                if (name == null) {
                name = (String) getValue(AbstractAction.NAME);
                }
                if (name == null) {
                ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                name = labels.getString("attribute.text");
                }
                return name;
                 */
            }

            @java.lang.Override
            public void undo() {
                super.undo();
                drawing.willChange();
                drawing.attr().restoreAttributesTo(restoreData);
                drawing.changed();
            }

            @java.lang.Override
            public void redo() {
                super.redo();
                // restoreData.add(figure.getAttributesRestoreData());
                drawing.willChange();
                drawing.attr().set(key, undoValue);
                drawing.changed();
            }
        };
        fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    protected void updateEnabledState() {
        setEnabled((getEditor() != null) && getEditor().isEnabled());
        if (((getView() != null) && (colorChooser != null)) && (popupMenu != null)) {
            colorChooser.setEnabled(getView().getSelectionCount() > 0);
            popupMenu.setEnabled(getView().getSelectionCount() > 0);
            isUpdating++;
            java.awt.Color drawingColor = getView().getDrawing().attr().get(key);
            colorChooser.setColor(drawingColor == null ? new java.awt.Color(0, true) : drawingColor);
            isUpdating--;
        }
    }

    @java.lang.Override
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        if ((isUpdating++) == 0) {
            applySelectedColorToFigures();
        }
        isUpdating--;
    }
}