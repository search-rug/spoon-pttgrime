/* @(#)StrokeIcon.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * StrokeIcon.
 */
public class StrokeIcon implements javax.swing.Icon {
    private java.awt.Stroke stroke;

    public StrokeIcon(java.awt.Stroke stroke) {
        this.stroke = stroke;
    }

    @java.lang.Override
    public int getIconHeight() {
        return 12;
    }

    @java.lang.Override
    public int getIconWidth() {
        return 40;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics gr, int x, int y) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setStroke(stroke);
        g.setColor(c.isEnabled() ? java.awt.Color.black : java.awt.Color.GRAY);
        g.drawLine(x, y + (getIconHeight() / 2), x + getIconWidth(), y + (getIconHeight() / 2));
        /* g.setStroke(new BasicStroke());
        g.setColor(Color.red);
        g.drawLine(x, y, x + getIconWidth(), y + getIconHeight());
         */
    }
}