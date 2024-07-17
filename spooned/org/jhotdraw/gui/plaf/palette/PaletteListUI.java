/* @(#)PaletteListUI.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteListUI.
 */
public class PaletteListUI extends javax.swing.plaf.basic.BasicListUI {
    /**
     * Returns a new instance of PaletteListUI. PaletteListUI delegates are allocated one per JList.
     *
     * @return A new ListUI implementation for the Windows look and feel.
     */
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent list) {
        return new org.jhotdraw.gui.plaf.palette.PaletteListUI();
    }

    @java.lang.Override
    protected void installDefaults() {
        super.installDefaults();
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(list, "List.border");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(list, "List.background", "List.foreground", "List.font");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installProperty(list, "opaque", java.lang.Boolean.TRUE);
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel plaf = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance();
        if (list.getCellRenderer() == null) {
            list.setCellRenderer(((javax.swing.ListCellRenderer) (plaf.get("List.cellRenderer"))));
        }
        java.awt.Color sbg = list.getSelectionBackground();
        if ((sbg == null) || (sbg instanceof javax.swing.plaf.UIResource)) {
            list.setSelectionBackground(plaf.getColor("List.selectionBackground"));
        }
        java.awt.Color sfg = list.getSelectionForeground();
        if ((sfg == null) || (sfg instanceof javax.swing.plaf.UIResource)) {
            list.setSelectionForeground(plaf.getColor("List.selectionForeground"));
        }
    }
}