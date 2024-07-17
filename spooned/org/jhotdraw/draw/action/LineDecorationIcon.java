/* @(#)LineDecorationIcon.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * LineDecorationIcon.
 */
public class LineDecorationIcon implements javax.swing.Icon {
    private org.jhotdraw.draw.figure.LineFigure lineFigure;

    public LineDecorationIcon(org.jhotdraw.draw.decoration.LineDecoration decoration, boolean isStartDecoration) {
        lineFigure = new org.jhotdraw.draw.figure.LineFigure();
        lineFigure.setBounds(new java.awt.geom.Point2D.Double(2, 8), new java.awt.geom.Point2D.Double(23, 8));
        if (isStartDecoration) {
            lineFigure.attr().set(org.jhotdraw.draw.AttributeKeys.START_DECORATION, decoration);
        } else {
            lineFigure.attr().set(org.jhotdraw.draw.AttributeKeys.END_DECORATION, decoration);
        }
        lineFigure.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, java.awt.Color.black);
    }

    @java.lang.Override
    public int getIconHeight() {
        return 16;
    }

    @java.lang.Override
    public int getIconWidth() {
        return 25;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics gr, int x, int y) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        lineFigure.draw(g);
    }
}