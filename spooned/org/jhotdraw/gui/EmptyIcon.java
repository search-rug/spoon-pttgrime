/**
 *
 * @(#)EmptyIcon.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * EmptyIcon.
 */
public class EmptyIcon implements javax.swing.Icon {
    private int width;

    private int height;

    public EmptyIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
    }

    @java.lang.Override
    public int getIconWidth() {
        return width;
    }

    @java.lang.Override
    public int getIconHeight() {
        return height;
    }
}