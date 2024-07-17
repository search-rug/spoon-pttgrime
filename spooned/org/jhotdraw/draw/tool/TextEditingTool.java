/* @(#)TextEditingTool.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * A tool to edit figures which implement the {@code TextHolderFigure} interface, such as {@code TextFigure}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure} interface define together
 * the contracts of a smaller framework inside of the JHotDraw framework for structured drawing
 * editors.<br>
 * Contract: {@link TextHolderFigure}, {@link TextCreationTool}, {@link TextAreaCreationTool},
 * {@link TextEditingTool}, {@link TextAreaEditingTool}, {@link FloatingTextField}, {@link FloatingTextArea}.
 *
 * <p><em>Prototype</em><br>
 * The text creation tools create new figures by cloning a prototype {@code TextHolderFigure}
 * object.<br>
 * Prototype: {@link TextHolderFigure}; Client: {@link TextCreationTool}, {@link TextAreaCreationTool}. <hr>
 */
public class TextEditingTool extends org.jhotdraw.draw.tool.AbstractTool implements java.awt.event.ActionListener {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.text.FloatingTextField textField;

    private org.jhotdraw.draw.figure.TextHolderFigure typingTarget;

    public TextEditingTool(org.jhotdraw.draw.figure.TextHolderFigure typingTarget) {
        this.typingTarget = typingTarget;
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        endEdit();
        super.deactivate(editor);
    }

    /**
     * If the pressed figure is a TextHolderFigure it can be edited.
     */
    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if (typingTarget != null) {
            beginEdit(typingTarget);
            updateCursor(getView(), e.getPoint());
        }
    }

    protected void beginEdit(org.jhotdraw.draw.figure.TextHolderFigure textHolder) {
        if (textField == null) {
            textField = new org.jhotdraw.draw.text.FloatingTextField();
            textField.addActionListener(this);
        }
        if ((textHolder != typingTarget) && (typingTarget != null)) {
            endEdit();
        }
        textField.createOverlay(getView(), textHolder);
        textField.requestFocus();
        typingTarget = textHolder;
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
    }

    protected void endEdit() {
        if (typingTarget != null) {
            typingTarget.willChange();
            final org.jhotdraw.draw.figure.TextHolderFigure editedFigure = typingTarget;
            final java.lang.String oldText = typingTarget.getText();
            final java.lang.String newText = textField.getText();
            if (newText.length() > 0) {
                typingTarget.willChange();
                typingTarget.setText(newText);
                typingTarget.changed();
            }
            javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    return labels.getString("attribute.text.text");
                }

                @java.lang.Override
                public void undo() {
                    super.undo();
                    editedFigure.willChange();
                    editedFigure.setText(oldText);
                    editedFigure.changed();
                }

                @java.lang.Override
                public void redo() {
                    super.redo();
                    editedFigure.willChange();
                    editedFigure.setText(newText);
                    editedFigure.changed();
                }
            };
            getDrawing().fireUndoableEditHappened(edit);
            typingTarget.changed();
            typingTarget = null;
            textField.endOverlay();
        }
        // view().checkDamage();
    }

    @java.lang.Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            fireToolDone();
        }
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        endEdit();
        fireToolDone();
    }

    public boolean isEditing() {
        return typingTarget != null;
    }

    @java.lang.Override
    public void updateCursor(org.jhotdraw.draw.DrawingView view, java.awt.Point p) {
        if (view.isEnabled()) {
            view.setCursor(java.awt.Cursor.getPredefinedCursor(isEditing() ? java.awt.Cursor.DEFAULT_CURSOR : java.awt.Cursor.CROSSHAIR_CURSOR));
        } else {
            view.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}