/* @(#)TextAreaCreationTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * A tool to create new or edit existing figures that implement the TextHolderFigure interface, such
 * as TextAreaFigure. The figure to be created is specified by a prototype.
 *
 * <p>To create a figure using the TextAreaCreationTool, the user does the following mouse gestures
 * on a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button over the DrawingView. This defines the start point of the Figure
 *       bounds.
 *   <li>Drag the mouse while keeping the mouse button pressed, and then release the mouse button.
 *       This defines the end point of the Figure bounds.
 * </ol>
 *
 * When the user has performed these mouse gesture, the TextAreaCreationTool overlays a text area
 * over the drawing where the user can enter the text for the Figure.
 *
 * <p>To edit an existing text figure using the TextAreaCreationTool, the user does the following
 * mouse gesture on a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button over a Figure on the DrawingView.
 * </ol>
 *
 * <p>The TextAreaCreationTool then uses Figure.findFigureInside to find a Figure that implements
 * the TextHolderFigure interface and that is editable. Then it overlays a text area over the
 * drawing where the user can enter the text for the Figure.
 *
 * <p>XXX - Maybe this class should be split up into a CreateTextAreaTool and a EditTextAreaTool.
 * <hr> <b>Design Patterns</b>
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
public class TextAreaCreationTool extends org.jhotdraw.draw.tool.CreationTool implements java.awt.event.ActionListener {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.text.FloatingTextArea textArea;

    private org.jhotdraw.draw.figure.TextHolderFigure typingTarget;

    /**
     * Rubberband color of the tool. When this is null, the tool does not draw a rubberband.
     */
    private java.awt.Color rubberbandColor = null;

    public TextAreaCreationTool(org.jhotdraw.draw.figure.TextHolderFigure prototype) {
        super(prototype);
    }

    public TextAreaCreationTool(org.jhotdraw.draw.figure.TextHolderFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        super(prototype, attributes);
    }

    /**
     * Sets the rubberband color for the tool. Setting this to null, disables the rubberband.
     *
     * @param c
     * 		Rubberband color or null.
     */
    public void setRubberbandColor(java.awt.Color c) {
        rubberbandColor = c;
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
        // Note: The search sequence used here, must be
        // consistent with the search sequence used by the
        // HandleTracker, SelectAreaTracker, DelegationSelectionTool, SelectionTool.
        if (typingTarget != null) {
            endEdit();
            if (isToolDoneAfterCreation()) {
                fireToolDone();
            }
        } else {
            super.mousePressed(e);
        }
    }

    /**
     * This method allows subclasses to do perform additonal user interactions after the new figure
     * has been created. The implementation of this class just invokes fireToolDone.
     */
    @java.lang.Override
    protected void creationFinished(org.jhotdraw.draw.figure.Figure createdFigure) {
        getView().clearSelection();
        getView().addToSelection(createdFigure);
        beginEdit(((org.jhotdraw.draw.figure.TextHolderFigure) (createdFigure)));
    }

    /* public void mouseDragged(java.awt.event.MouseEvent e) {
    }
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if ((createdFigure != null) && (rubberbandColor != null)) {
            g.setColor(rubberbandColor);
            g.draw(getView().drawingToView(createdFigure.getBounds()));
        }
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
            if (newText.length() > 0) {
                typingTarget.setText(newText);
            } else if (createdFigure != null) {
                getDrawing().remove(getAddedFigure());
                // XXX - Fire undoable edit here!!
            } else {
                typingTarget.setText("");
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
            textArea.endOverlay();
        }
        // view().checkDamage();
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        endEdit();
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }
}