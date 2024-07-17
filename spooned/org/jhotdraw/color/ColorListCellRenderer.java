/**
 *
 * @(#)ColorListCellRenderer.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * ColorListCellRenderer.
 */
public class ColorListCellRenderer extends javax.swing.DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    private static class ColorIcon implements javax.swing.Icon {
        private java.awt.Color color;

        public void setColor(java.awt.Color newValue) {
            color = newValue;
        }

        @java.lang.Override
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            if (color != null) {
                g.setColor(new java.awt.Color(0x333333));
                g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
                g.setColor(java.awt.Color.WHITE);
                g.drawRect(x + 1, y + 1, getIconWidth() - 3, getIconHeight() - 3);
                g.setColor(color);
                g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            }
        }

        @java.lang.Override
        public int getIconWidth() {
            return 24;
        }

        @java.lang.Override
        public int getIconHeight() {
            return 18;
        }
    }

    private org.jhotdraw.color.ColorListCellRenderer.ColorIcon icon;

    public ColorListCellRenderer() {
        icon = new org.jhotdraw.color.ColorListCellRenderer.ColorIcon();
        setIcon(icon);
    }

    @java.lang.Override
    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof java.awt.Color) {
            java.awt.Color c = ((java.awt.Color) (value));
            icon.setColor(c);
            setToolTipText(org.jhotdraw.color.ColorUtil.toToolTipText(c));
            setText("");
        } else {
            icon.setColor(null);
            setText("");
        }
        setIcon(icon);
        return this;
    }
}