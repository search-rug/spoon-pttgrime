/* @(#)NumberedParagraphView.java

Copyright (c) 2005 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy.text;
/**
 * NumberedParagraphView.
 */
public class NumberedParagraphView extends javax.swing.text.ParagraphView {
    public static final short NUMBERS_WIDTH = 30;

    private static java.awt.Font numberFont = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10);

    private org.jhotdraw.samples.teddy.text.NumberedViewFactory viewFactory;

    public NumberedParagraphView(javax.swing.text.Element e, org.jhotdraw.samples.teddy.text.NumberedViewFactory viewFactory) {
        super(e);
        this.viewFactory = viewFactory;
    }

    /**
     * Gets the left inset.
     *
     * @return the inset &gt;= 0
     */
    @java.lang.Override
    protected short getLeftInset() {
        short left = super.getLeftInset();
        return viewFactory.isLineNumbersVisible() ? ((short) (left + org.jhotdraw.samples.teddy.text.NumberedParagraphView.NUMBERS_WIDTH)) : left;
    }

    @java.lang.Override
    public void paintChild(java.awt.Graphics g, java.awt.Rectangle r, int n) {
        super.paintChild(g, r, n);
        if (viewFactory.isLineNumbersVisible()) {
            if (n == 0) {
                g.setColor(java.awt.Color.gray);
                int lineAscent = g.getFontMetrics().getAscent();
                g.setFont(org.jhotdraw.samples.teddy.text.NumberedParagraphView.numberFont);
                // int numberAscent = g.getFontMetrics().getAscent();
                int lineNumber = getDocument().getDefaultRootElement().getElementIndex(getStartOffset());
                int numberX = r.x - getLeftInset();
                // int numberY = r.y + g.getFontMetrics().getAscent();
                int numberY = r.y + lineAscent;
                g.drawString(java.lang.Integer.toString(lineNumber + 1), numberX, numberY);
            }
        }
    }
}