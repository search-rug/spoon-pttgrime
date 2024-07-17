/* @(#)BorderRectangle2D.DoubleFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * BorderRectangle2D.DoubleFigure.
 *
 * @deprecated This class should be in one of the samples package
 */
@java.lang.Deprecated
public class BorderRectangleFigure extends org.jhotdraw.draw.figure.RectangleFigure {
    private static final long serialVersionUID = 1L;

    protected javax.swing.border.Border border;

    protected static final javax.swing.JComponent BORDER_COMPONENT = new javax.swing.JPanel();

    public BorderRectangleFigure(javax.swing.border.Border border) {
        this.border = border;
    }

    public void drawFigure(java.awt.Graphics2D g) {
        java.awt.Rectangle bounds = getBounds().getBounds();
        border.paintBorder(org.jhotdraw.samples.mini.BorderRectangleFigure.BORDER_COMPONENT, g, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}