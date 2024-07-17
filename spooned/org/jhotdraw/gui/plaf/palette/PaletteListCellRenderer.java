/* @(#)PaletteListCellRenderer.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteListCellRenderer.
 */
public class PaletteListCellRenderer extends javax.swing.DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    /**
     * An empty <code>Border</code>. This field might not be used. To change the <code>Border</code>
     * used by this renderer override the <code>getListCellRendererComponent</code> method and set the
     * border of the returned component directly.
     */
    private static final javax.swing.border.Border SAFE_NO_FOCUS_BORDER = new javax.swing.border.EmptyBorder(1, 1, 1, 1);

    private static final javax.swing.border.Border DEFAULT_NO_FOCUS_BORDER = new javax.swing.border.EmptyBorder(1, 1, 1, 1);

    protected javax.swing.border.Border noFocusBorder = org.jhotdraw.gui.plaf.palette.PaletteListCellRenderer.DEFAULT_NO_FOCUS_BORDER;

    /**
     * Constructs a default renderer object for an item in a list.
     */
    public PaletteListCellRenderer() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
    }

    private javax.swing.border.Border getNoFocusBorder() {
        javax.swing.border.Border border = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getBorder("List.cellNoFocusBorder");
        if (java.lang.System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }
            return org.jhotdraw.gui.plaf.palette.PaletteListCellRenderer.SAFE_NO_FOCUS_BORDER;
        } else {
            if ((border != null) && ((noFocusBorder == null) || (noFocusBorder == org.jhotdraw.gui.plaf.palette.PaletteListCellRenderer.DEFAULT_NO_FOCUS_BORDER))) {
                return border;
            }
            return noFocusBorder;
        }
    }

    @java.lang.Override
    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus) {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel plaf = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance();
        setComponentOrientation(list.getComponentOrientation());
        java.awt.Color bg = null;
        java.awt.Color fg = null;
        javax.swing.JList.DropLocation dropLocation = list.getDropLocation();
        if (((dropLocation != null) && (!dropLocation.isInsert())) && (dropLocation.getIndex() == index)) {
            bg = plaf.getColor("List.dropCellBackground");
            fg = plaf.getColor("List.dropCellForeground");
            isSelected = true;
        }
        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value instanceof javax.swing.Icon) {
            setIcon(((javax.swing.Icon) (value)));
            setText("");
        } else {
            setIcon(null);
            setText(value == null ? "" : value.toString());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        javax.swing.border.Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = plaf.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = plaf.getBorder("List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        setBorder(border);
        return this;
    }
}