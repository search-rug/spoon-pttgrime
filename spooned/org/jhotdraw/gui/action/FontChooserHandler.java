/**
 *
 * @(#)FontChooserHandler.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.action;
/**
 * FontChooserHandler.
 */
public class FontChooserHandler extends org.jhotdraw.draw.action.AbstractSelectedAction implements java.beans.PropertyChangeListener {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.draw.AttributeKey<java.awt.Font> key;

    protected org.jhotdraw.gui.JFontChooser fontChooser;

    protected javax.swing.JPopupMenu popupMenu;

    protected int isUpdating;

    // protected Map<AttributeKey, Object> attributes;
    public FontChooserHandler(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Font> key, org.jhotdraw.gui.JFontChooser fontChooser, javax.swing.JPopupMenu popupMenu) {
        super(editor);
        this.key = key;
        this.fontChooser = fontChooser;
        this.popupMenu = popupMenu;
        fontChooser.addActionListener(this);
        fontChooser.addPropertyChangeListener(this);
        updateEnabledState();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (((evt.getActionCommand() == null) && (org.jhotdraw.gui.JFontChooser.APPROVE_SELECTION == null)) || ((evt.getActionCommand() != null) && evt.getActionCommand().equals(org.jhotdraw.gui.JFontChooser.APPROVE_SELECTION))) {
            applySelectedFontToFigures();
        } else if (((evt.getActionCommand() == null) && (org.jhotdraw.gui.JFontChooser.CANCEL_SELECTION == null)) || ((evt.getActionCommand() != null) && evt.getActionCommand().equals(org.jhotdraw.gui.JFontChooser.CANCEL_SELECTION))) {
        }
        popupMenu.setVisible(false);
    }

    protected void applySelectedFontToFigures() {
        final java.util.ArrayList<org.jhotdraw.draw.figure.Figure> selectedFigures = new java.util.ArrayList<>(getView().getSelectedFigures());
        final java.util.ArrayList<java.lang.Object> restoreData = new java.util.ArrayList<>(selectedFigures.size());
        for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
            restoreData.add(figure.attr().getAttributesRestoreData());
            figure.willChange();
            figure.attr().set(key, fontChooser.getSelectedFont());
            figure.changed();
        }
        getEditor().setDefaultAttribute(key, fontChooser.getSelectedFont());
        final java.awt.Font undoValue = fontChooser.getSelectedFont();
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
                java.util.Iterator<java.lang.Object> iRestore = restoreData.iterator();
                for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
                    figure.willChange();
                    figure.attr().restoreAttributesTo(iRestore.next());
                    figure.changed();
                }
            }

            @java.lang.Override
            public void redo() {
                super.redo();
                for (org.jhotdraw.draw.figure.Figure figure : selectedFigures) {
                    // restoreData.add(figure.getAttributesRestoreData());
                    figure.willChange();
                    figure.attr().set(key, undoValue);
                    figure.changed();
                }
            }
        };
        fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    protected void updateEnabledState() {
        setEnabled(getEditor().isEnabled());
        if (((getView() != null) && (fontChooser != null)) && (popupMenu != null)) {
            fontChooser.setEnabled(getView().getSelectionCount() > 0);
            popupMenu.setEnabled(getView().getSelectionCount() > 0);
            isUpdating++;
            /* && fontChooser.isShowing() */
            if (getView().getSelectionCount() > 0) {
                for (org.jhotdraw.draw.figure.Figure f : getView().getSelectedFigures()) {
                    if (f instanceof org.jhotdraw.draw.figure.TextHolderFigure) {
                        org.jhotdraw.draw.figure.TextHolderFigure thf = ((org.jhotdraw.draw.figure.TextHolderFigure) (f));
                        fontChooser.setSelectedFont(thf.getFont());
                        break;
                    }
                }
            }
            isUpdating--;
        }
    }

    @java.lang.Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if ((isUpdating++) == 0) {
            if (((evt.getPropertyName() == null) && (org.jhotdraw.gui.JFontChooser.SELECTED_FONT_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.gui.JFontChooser.SELECTED_FONT_PROPERTY))) {
                applySelectedFontToFigures();
            }
        }
        isUpdating--;
    }
}