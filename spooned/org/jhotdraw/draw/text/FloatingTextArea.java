/* @(#)FloatingTextArea.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.text;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * A <em>floating text area</em> that is used to edit a {@link TextHolderFigure}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure} interface define together
 * the contracts of a smaller framework inside of the JHotDraw framework for structured drawing
 * editors.<br>
 * Contract: {@link TextHolderFigure}, {@link org.jhotdraw.draw.tool.TextCreationTool}, {@link org.jhotdraw.draw.tool.TextAreaCreationTool}, {@link org.jhotdraw.draw.tool.TextEditingTool},
 * {@link org.jhotdraw.draw.tool.TextAreaEditingTool}, {@link FloatingTextField}, {@link FloatingTextArea}. <hr>
 *
 * @author Werner Randelshofer
 * @version $Id: FloatingTextArea.java -1 $
 */
public class FloatingTextArea {
    /**
     * A scroll pane to allow for vertical scrolling while editing
     */
    protected javax.swing.JScrollPane editScrollContainer;

    /**
     * The actual editor
     */
    protected javax.swing.JTextArea textArea;

    /**
     * The drawing view.
     */
    protected org.jhotdraw.draw.DrawingView view;

    private org.jhotdraw.draw.figure.TextHolderFigure editedFigure;

    private org.jhotdraw.draw.event.FigureListener figureHandler = new org.jhotdraw.draw.event.FigureListenerAdapter() {
        @java.lang.Override
        public void attributeChanged(org.jhotdraw.draw.event.FigureEvent e) {
            updateWidget();
        }
    };

    /**
     * Constructor for the FloatingTextArea object
     */
    public FloatingTextArea() {
        textArea = new javax.swing.JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        editScrollContainer = new javax.swing.JScrollPane(textArea, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editScrollContainer.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        editScrollContainer.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black));
    }

    /**
     * Creates the overlay within the given container.
     *
     * @param view
     * 		the DrawingView
     */
    public void createOverlay(org.jhotdraw.draw.DrawingView view) {
        createOverlay(view, null);
    }

    public void requestFocus() {
        textArea.requestFocus();
    }

    /**
     * Creates the overlay for the given Container using a specific font.
     *
     * @param view
     * 		the DrawingView
     * @param figure
     * 		the figure holding the text
     */
    public void createOverlay(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.TextHolderFigure figure) {
        view.getComponent().add(editScrollContainer, 0);
        editedFigure = figure;
        this.view = view;
        if (editedFigure != null) {
            editedFigure.addFigureListener(figureHandler);
            updateWidget();
        }
    }

    protected void updateWidget() {
        java.awt.Font f = editedFigure.getFont();
        // FIXME - Should scale with fractional value!
        f = f.deriveFont(f.getStyle(), ((float) (editedFigure.getFontSize() * view.getScaleFactor())));
        textArea.setFont(f);
        textArea.setForeground(editedFigure.getTextColor());
        textArea.setBackground(editedFigure.getFillColor());
        // textArea.setBounds(getFieldBounds(editedFigure));
    }

    /**
     * Positions and sizes the overlay.
     *
     * @param r
     * 		the bounding Rectangle2D.Double for the overlay
     * @param text
     * 		the text to edit
     */
    public void setBounds(java.awt.geom.Rectangle2D.Double r, java.lang.String text) {
        textArea.setText(text);
        editScrollContainer.setBounds(view.drawingToView(r));
        editScrollContainer.setVisible(true);
        textArea.setCaretPosition(0);
        textArea.requestFocus();
    }

    /**
     * Gets the text contents of the overlay.
     *
     * @return The text value
     */
    public java.lang.String getText() {
        return textArea.getText();
    }

    /**
     * Gets the preferred size of the overlay.
     *
     * @param cols
     * 		Description of the Parameter
     * @return The preferredSize value
     */
    public java.awt.Dimension getPreferredSize(int cols) {
        return new java.awt.Dimension(textArea.getWidth(), textArea.getHeight());
    }

    /**
     * Removes the overlay.
     */
    public void endOverlay() {
        view.getComponent().requestFocus();
        if (editScrollContainer != null) {
            editScrollContainer.setVisible(false);
            view.getComponent().remove(editScrollContainer);
            java.awt.Rectangle bounds = editScrollContainer.getBounds();
            view.getComponent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if (editedFigure != null) {
            editedFigure.removeFigureListener(figureHandler);
            editedFigure = null;
        }
    }
}