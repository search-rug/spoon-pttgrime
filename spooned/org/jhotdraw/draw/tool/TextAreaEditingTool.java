/* @(#)TextAreaEditingTool.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * A tool to edit existing figures that implement the TextHolderFigure interface, such as
 * TextAreaFigure.
 *
 * <p>To edit an existing text figure using the TextAreaEditingTool, the user does the following
 * mouse gesture on a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button over a Figure on the DrawingView.
 * </ol>
 *
 * <p>The TextAreaEditingTool then uses Figure.findFigureInside to find a Figure that implements the
 * TextHolderFigure interface and that is editable. Then it overlays a text area over the drawing
 * where the user can enter the text for the Figure. <hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure} interface define together
 * the contracts of a smaller framework inside of the JHotDraw framework for structured drawing
 * editors.<br>
 * Contract: {@link TextHolderFigure}, {@link TextCreationTool}, {@link TextAreaCreationTool},
 * {@link TextEditingTool}, {@link TextAreaEditingTool}, {@link FloatingTextField}, {@link FloatingTextArea}. <hr>
 *
 * @see TextHolderFigure
 * @see FloatingTextArea
 */
public class TextAreaEditingTool extends org.jhotdraw.draw.tool.AbstractTool implements java.awt.event.ActionListener {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.text.FloatingTextArea textArea;

    private org.jhotdraw.draw.figure.TextHolderFigure typingTarget;

    public TextAreaEditingTool(org.jhotdraw.draw.figure.TextHolderFigure typingTarget) {
        this.typingTarget = typingTarget;
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        endEdit();
        super.deactivate(editor);
    }

    /**
     * Creates a new figure at the mouse location. If editing is in progress, this finishes editing.
     */
    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if (typingTarget != null) {
            beginEdit(typingTarget);
            updateCursor(getView(), e.getPoint());
        }
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
    }

    protected void beginEdit(org.jhotdraw.draw.figure.TextHolderFigure textHolder) {
        if (textArea == null) {
            textArea = new org.jhotdraw.draw.text.FloatingTextArea();
            // textArea.addActionListener(this);
        }
        if ((textHolder != typingTarget) && (typingTarget != null)) {
            endEdit();
        }
        textArea.createOverlay(getView(), textHolder);
        textArea.setBounds(getFieldBounds(textHolder), textHolder.getText());
        textArea.requestFocus();
        typingTarget = textHolder;
    }

    private java.awt.geom.Rectangle2D.Double getFieldBounds(org.jhotdraw.draw.figure.TextHolderFigure figure) {
        java.awt.geom.Rectangle2D.Double r = figure.getDrawingArea();
        org.jhotdraw.geom.Insets2D.Double insets = figure.getInsets();
        insets.subtractTo(r);
        // FIXME - Find a way to determine the parameters for grow.
        // r.grow(1,2);
        // r.width += 16;
        r.x -= 1;
        r.y -= 2;
        r.width += 18;
        r.height += 4;
        return r;
    }

    protected void endEdit() {
        if (typingTarget != null) {
            typingTarget.willChange();
            final org.jhotdraw.draw.figure.TextHolderFigure editedFigure = typingTarget;
            final java.lang.String oldText = typingTarget.getText();
            final java.lang.String newText = textArea.getText();
            typingTarget.willChange();
            if (newText.length() > 0) {
                typingTarget.setText(newText);
            } else {
                typingTarget.setText("");
            }
            typingTarget.changed();
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
            textArea.endOverlay();
        }
        // view().checkDamage();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        endEdit();
        fireToolDone();
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}