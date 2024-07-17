/* @(#)ColorIcon.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * ColorIcon.
 */
public class ColorIcon implements javax.swing.Icon {
    private java.awt.Color fillColor;

    private static java.awt.image.BufferedImage noColorImage;

    private int width;

    private int height;

    private java.lang.String name;

    public ColorIcon(int rgb) {
        this(new java.awt.Color(rgb));
    }

    public ColorIcon(java.awt.Color color) {
        this(color, color == null ? org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getToolTipTextProperty("attribute.color.noColor") : (((color.getRed() + ",") + color.getGreen()) + ",") + color.getBlue(), 14, 14);
    }

    public ColorIcon(int rgb, java.lang.String name) {
        this(new java.awt.Color(rgb), name, 14, 14);
    }

    public ColorIcon(java.awt.Color color, java.lang.String name) {
        this(color, name, 14, 14);
    }

    public ColorIcon(java.awt.Color color, java.lang.String name, int width, int height) {
        this.fillColor = color;
        this.name = name;
        this.width = width;
        this.height = height;
        if (org.jhotdraw.draw.action.ColorIcon.noColorImage == null) {
            org.jhotdraw.draw.action.ColorIcon.noColorImage = org.jhotdraw.util.Images.toBufferedImage(org.jhotdraw.util.Images.createImage(org.jhotdraw.draw.action.ColorIcon.class, "/org/jhotdraw/draw/action/images/attribute.color.noColor.png"));
        }
    }

    public java.awt.Color getColor() {
        return fillColor;
    }

    public java.lang.String getName() {
        return name;
    }

    @java.lang.Override
    public int getIconWidth() {
        return width;
    }

    @java.lang.Override
    public int getIconHeight() {
        return height;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
        // Graphics2D g = (Graphics2D) gr;
        if ((fillColor == null) || (fillColor.getAlpha() == 0)) {
            if ((width == org.jhotdraw.draw.action.ColorIcon.noColorImage.getWidth()) && (height == org.jhotdraw.draw.action.ColorIcon.noColorImage.getHeight())) {
                g.drawImage(org.jhotdraw.draw.action.ColorIcon.noColorImage, x, y, c);
            } else {
                g.setColor(java.awt.Color.WHITE);
                g.fillRect(x + 1, y + 1, width - 2, height - 2);
                g.setColor(java.awt.Color.red);
                int[] xpoints = new int[]{ x + 2, (x + width) - 5, (x + width) - 3, (x + width) - 3, x + 4, x + 2 };
                int[] ypoints = new int[]{ (y + height) - 5, y + 2, y + 2, y + 4, (y + height) - 3, (y + height) - 3 };
                g.fillPolygon(xpoints, ypoints, xpoints.length);
            }
        } else {
            // g.setColor(Color.WHITE);
            // g.fillRect(x + 1, y + 1, width - 2, height - 2);
            g.setColor(fillColor);
            // g.fillRect(x + 2, y + 2, width - 4, height - 4);
            g.fillRect(x + 1, y + 1, width - 2, height - 2);
        }
        g.setColor(new java.awt.Color(0x666666));
        // Draw the rectangle using drawLine to work around a drawing bug in
        // Apples MRJ for Java 1.5
        // g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
        g.drawLine(x, y, (x + width) - 1, y);
        g.drawLine((x + width) - 1, y, (x + width) - 1, (y + width) - 1);
        g.drawLine((x + width) - 1, (y + height) - 1, x, (y + height) - 1);
        g.drawLine(x, (y + height) - 1, x, y);
    }
}