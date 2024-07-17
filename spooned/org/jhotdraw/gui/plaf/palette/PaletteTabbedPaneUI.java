/* @(#)PaletteTabbedPaneUI.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteTabbedPaneUI.
 */
public class PaletteTabbedPaneUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
    private static final float[] ENABLED_STOPS = new float[]{ 0.0F, 0.35F, 0.4F, 1.0F };

    private static final java.awt.Color[] ENABLED_STOP_COLORS = new java.awt.Color[]{ new java.awt.Color(0xf8f8f8), new java.awt.Color(0xeeeeee), new java.awt.Color(0xcacaca), new java.awt.Color(0xffffff) };

    private boolean tabsOverlapBorder;

    private java.awt.Color selectedColor;

    private boolean tabsOpaque = true;

    private boolean contentOpaque = true;

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return new org.jhotdraw.gui.plaf.palette.PaletteTabbedPaneUI();
    }

    @java.lang.Override
    protected void installDefaults() {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel laf = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance();
        javax.swing.plaf.metal.MetalTabbedPaneUI mtpui;
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(tabPane, "TabbedPane.background", "TabbedPane.foreground", "TabbedPane.font");
        highlight = laf.getColor("TabbedPane.light");
        lightHighlight = laf.getColor("TabbedPane.highlight");
        shadow = laf.getColor("TabbedPane.shadow");
        darkShadow = laf.getColor("TabbedPane.darkShadow");
        focus = laf.getColor("TabbedPane.focus");
        selectedColor = laf.getColor("TabbedPane.selected");// will probably not

        // work as expected since we don't override enough colors from BasicTabbedPaneUI
        textIconGap = laf.getInt("TabbedPane.textIconGap");
        tabInsets = laf.getInsets("TabbedPane.tabInsets");
        selectedTabPadInsets = laf.getInsets("TabbedPane.selectedTabPadInsets");
        tabAreaInsets = laf.getInsets("TabbedPane.tabAreaInsets");
        tabsOverlapBorder = laf.getBoolean("TabbedPane.tabsOverlapBorder");
        // will probably not
        // work as expected since we don't override enough colors from BasicTabbedPaneUI
        contentBorderInsets = laf.getInsets("TabbedPane.contentBorderInsets");
        tabRunOverlay = laf.getInt("TabbedPane.tabRunOverlay");
        tabsOpaque = laf.getBoolean("TabbedPane.tabsOpaque");// will probably not

        // work as expected since we don't override enough colors from BasicTabbedPaneUI
        contentOpaque = laf.getBoolean("TabbedPane.contentOpaque");// will probably not

        // work as expected since we don't override enough colors from BasicTabbedPaneUI
        java.lang.Object opaque = laf.get("TabbedPane.opaque");
        if (opaque == null) {
            opaque = java.lang.Boolean.FALSE;
        }
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installProperty(tabPane, "opaque", opaque);
        // Fix for 6711145 BasicTabbedPanuUI should not throw a NPE if these
        // keys are missing. So we are setting them to there default values here
        // if the keys are missing.
        if (tabInsets == null) {
            tabInsets = new java.awt.Insets(0, 4, 1, 4);
        }
        if (selectedTabPadInsets == null) {
            selectedTabPadInsets = new java.awt.Insets(2, 2, 2, 1);
        }
        if (tabAreaInsets == null) {
            tabAreaInsets = new java.awt.Insets(3, 2, 0, 2);
        }
        if (contentBorderInsets == null) {
            contentBorderInsets = new java.awt.Insets(2, 2, 3, 3);
        }
    }

    @java.lang.Override
    protected void paintTab(java.awt.Graphics g, int tabPlacement, java.awt.Rectangle[] rects, int tabIndex, java.awt.Rectangle iconRect, java.awt.Rectangle textRect) {
        java.awt.Rectangle tabRect = rects[tabIndex];
        int selectedIndex = tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;
        if (tabsOpaque || tabPane.isOpaque()) {
            paintTabBackground(g, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected);
        }
        paintTabBorder(g, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected);
        java.lang.String title = tabPane.getTitleAt(tabIndex);
        java.awt.Font font = (isSelected) ? org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont("TabbedPane.selectedFont") : tabPane.getFont();
        java.awt.FontMetrics metrics = org.jhotdraw.gui.plaf.palette.PaletteUtilities.getFontMetrics(tabPane, g, font);
        javax.swing.Icon icon = getIconForTab(tabIndex);
        layoutLabel(tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);
        if (tabPane.getTabComponentAt(tabIndex) == null) {
            java.lang.String clippedTitle = title;
            // XXX - Implement scrollable tab layout support.
            /* if (scrollableTabLayoutEnabled() && tabScroller.croppedEdge.isParamsSet() &&
            tabScroller.croppedEdge.getTabIndex() == tabIndex && isHorizontalTabPlacement()) {
            int availTextWidth = tabScroller.croppedEdge.getCropline() -
            (textRect.x - tabRect.x) - tabScroller.croppedEdge.getCroppedSideWidth();
            clippedTitle = SwingUtilities2.clipStringIfNecessary(null, metrics, title, availTextWidth);
            }
             */
            paintText(g, tabPlacement, font, metrics, tabIndex, clippedTitle, textRect, isSelected);
            paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);
        }
        paintFocusIndicator(g, tabPlacement, rects, tabIndex, iconRect, textRect, isSelected);
    }

    /**
     * this function draws the border around each tab note that this function does now draw the
     * background of the tab. that is done elsewhere
     */
    @java.lang.Override
    protected void paintTabBorder(java.awt.Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        g.setColor(isSelected ? darkShadow : shadow);
        switch (tabPlacement) {
            case javax.swing.SwingConstants.LEFT :
                g.drawLine(x + 1, (y + h) - 2, x + 1, (y + h) - 2);// bottom-left highlight

                g.drawLine(x, y + 2, x, (y + h) - 3);// left highlight

                g.drawLine(x + 1, y + 1, x + 1, y + 1);// top-left highlight

                g.drawLine(x + 2, y, (x + w) - 1, y);// top highlight

                g.drawLine(x + 2, (y + h) - 2, (x + w) - 1, (y + h) - 2);// bottom shadow

                g.drawLine(x + 2, (y + h) - 1, (x + w) - 1, (y + h) - 1);// bottom dark shadow

                break;
            case javax.swing.SwingConstants.RIGHT :
                g.drawLine(x, y, (x + w) - 3, y);// top highlight

                g.drawLine(x, (y + h) - 2, (x + w) - 3, (y + h) - 2);// bottom shadow

                g.drawLine((x + w) - 2, y + 2, (x + w) - 2, (y + h) - 3);// right shadow

                g.drawLine((x + w) - 2, y + 1, (x + w) - 2, y + 1);// top-right dark shadow

                g.drawLine((x + w) - 2, (y + h) - 2, (x + w) - 2, (y + h) - 2);// bottom-right dark shadow

                g.drawLine((x + w) - 1, y + 2, (x + w) - 1, (y + h) - 3);// right dark shadow

                g.drawLine(x, (y + h) - 1, (x + w) - 3, (y + h) - 1);// bottom dark shadow

                break;
            case javax.swing.SwingConstants.BOTTOM :
                g.drawLine(x, y, x, (y + h) - 2);// left highlight

                // g.drawLine(x+1, y+h-1, x+1, y+h-1); // bottom-left highlight
                g.drawLine(x + 1, (y + h) - 2, (x + w) - 1, (y + h) - 2);// bottom shadow

                g.drawLine((x + w) - 1, y, (x + w) - 1, (y + h) - 2);// right shadow

                /* g.drawLine(x+2, y+h-1, x+w-3, y+h-1); // bottom dark shadow
                g.drawLine(x+w-2, y+h-2, x+w-2, y+h-2); // bottom-right dark shadow
                g.drawLine(x+w-1, y, x+w-1, y+h-3); // right dark shadow
                 */
                break;
            case javax.swing.SwingConstants.TOP :
            default :
                g.drawLine(x, y + 2, x, (y + h) - 1);// left highlight

                g.drawLine(x + 1, y + 1, x + 1, y + 1);// top-left highlight

                g.drawLine(x + 2, y, (x + w) - 3, y);// top highlight

                g.drawLine((x + w) - 2, y + 2, (x + w) - 2, (y + h) - 1);// right shadow

                g.drawLine((x + w) - 1, y + 2, (x + w) - 1, (y + h) - 1);// right dark-shadow

                g.drawLine((x + w) - 2, y + 1, (x + w) - 2, y + 1);// top-right shadow

        }
    }

    @java.lang.Override
    protected void paintTabBackground(java.awt.Graphics gr, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        g.setColor((!isSelected) || (selectedColor == null) ? tabPane.getBackgroundAt(tabIndex) : selectedColor);
        switch (tabPlacement) {
            case javax.swing.SwingConstants.LEFT :
                g.fillRect(x + 1, y + 1, w - 1, h - 3);
                break;
            case javax.swing.SwingConstants.RIGHT :
                g.fillRect(x, y + 1, w - 2, h - 3);
                break;
            case javax.swing.SwingConstants.BOTTOM :
                if (!isSelected) {
                    java.awt.LinearGradientPaint lgp = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(x, y), new java.awt.geom.Point2D.Float(x, (y + h) - 1), org.jhotdraw.gui.plaf.palette.PaletteTabbedPaneUI.ENABLED_STOPS, org.jhotdraw.gui.plaf.palette.PaletteTabbedPaneUI.ENABLED_STOP_COLORS, java.awt.MultipleGradientPaint.CycleMethod.REPEAT);
                    g.setPaint(lgp);
                }
                g.fillRect(x + 1, y, w - 3, h - 1);
                break;
            case javax.swing.SwingConstants.TOP :
            default :
                g.fillRect(x + 1, y + 1, w - 3, h - 1);
        }
    }

    @java.lang.Override
    protected void paintText(java.awt.Graphics g, int tabPlacement, java.awt.Font font, java.awt.FontMetrics metrics, int tabIndex, java.lang.String title, java.awt.Rectangle textRect, boolean isSelected) {
        g.setFont(font);
        javax.swing.text.View v = getTextViewForTab(tabIndex);
        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                java.awt.Color fg = tabPane.getForegroundAt(tabIndex);
                if (isSelected && (fg instanceof javax.swing.plaf.UIResource)) {
                    java.awt.Color selectedFG = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getColor("TabbedPane.selectedForeground");
                    if (selectedFG != null) {
                        fg = selectedFG;
                    }
                }
                g.setColor(fg);
                org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
            } else {
                // tab disabled
                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                org.jhotdraw.gui.plaf.palette.PaletteUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x - 1, (textRect.y + metrics.getAscent()) - 1);
            }
        }
    }

    @java.lang.Override
    protected void paintContentBorder(java.awt.Graphics g, int tabPlacement, int selectedIndex) {
        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
        java.awt.Insets insets = tabPane.getInsets();
        java.awt.Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
        int x = insets.left;
        int y = insets.top;
        int w = (width - insets.right) - insets.left;
        int h = (height - insets.top) - insets.bottom;
        switch (tabPlacement) {
            case javax.swing.SwingConstants.LEFT :
                x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                if (tabsOverlapBorder) {
                    x -= tabAreaInsets.right;
                }
                w -= x - insets.left;
                break;
            case javax.swing.SwingConstants.RIGHT :
                w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                if (tabsOverlapBorder) {
                    w += tabAreaInsets.left;
                }
                break;
            case javax.swing.SwingConstants.BOTTOM :
                h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                if (tabsOverlapBorder) {
                    h += tabAreaInsets.top;
                }
                break;
            case javax.swing.SwingConstants.TOP :
            default :
                y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                if (tabsOverlapBorder) {
                    y -= tabAreaInsets.bottom;
                }
                h -= y - insets.top;
        }
        if ((tabPane.getTabCount() > 0) && (contentOpaque || tabPane.isOpaque())) {
            // Fill region behind content area
            java.awt.Color color = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getColor("TabbedPane.contentAreaColor");
            if (color != null) {
                g.setColor(color);
            } else if ((selectedColor == null) || (selectedIndex == (-1))) {
                g.setColor(tabPane.getBackground());
            } else {
                g.setColor(selectedColor);
            }
            g.fillRect(x, y, w, h);
        }
        boolean paintBorder = tabPane.getClientProperty("Palette.TabbedPane.paintContentBorder") != java.lang.Boolean.FALSE;
        if ((tabPlacement == javax.swing.SwingConstants.TOP) || paintBorder) {
            paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
        if ((tabPlacement == javax.swing.SwingConstants.LEFT) || paintBorder) {
            paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
        if ((tabPlacement == javax.swing.SwingConstants.BOTTOM) || paintBorder) {
            paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
        if ((tabPlacement == javax.swing.SwingConstants.RIGHT) || paintBorder) {
            paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
    }

    @java.lang.Override
    protected void paintContentBorderTopEdge(java.awt.Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        java.awt.Rectangle selRect = (selectedIndex < 0) ? null : getTabBounds(selectedIndex, calcRect);
        g.setColor(lightHighlight);
        // Draw unbroken line if tabs are not on TOP, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        if ((((tabPlacement != javax.swing.SwingConstants.TOP) || (selectedIndex < 0)) || (((selRect.y + selRect.height) + 1) < y)) || ((selRect.x < x) || (selRect.x > (x + w)))) {
            g.drawLine(x, y, (x + w) - 2, y);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine(x, y, selRect.x - 1, y);
            if ((selRect.x + selRect.width) < ((x + w) - 2)) {
                g.drawLine(selRect.x + selRect.width, y, (x + w) - 2, y);
            } else {
                g.setColor(shadow);
                g.drawLine((x + w) - 2, y, (x + w) - 2, y);
            }
        }
    }

    @java.lang.Override
    protected void paintContentBorderLeftEdge(java.awt.Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        java.awt.Rectangle selRect = (selectedIndex < 0) ? null : getTabBounds(selectedIndex, calcRect);
        g.setColor(lightHighlight);
        // Draw unbroken line if tabs are not on LEFT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        if ((((tabPlacement != javax.swing.SwingConstants.LEFT) || (selectedIndex < 0)) || (((selRect.x + selRect.width) + 1) < x)) || ((selRect.y < y) || (selRect.y > (y + h)))) {
            g.drawLine(x, y, x, (y + h) - 2);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine(x, y, x, selRect.y - 1);
            if ((selRect.y + selRect.height) < ((y + h) - 2)) {
                g.drawLine(x, selRect.y + selRect.height, x, (y + h) - 2);
            }
        }
    }

    @java.lang.Override
    protected void paintContentBorderBottomEdge(java.awt.Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        java.awt.Rectangle selRect = (selectedIndex < 0) ? null : getTabBounds(selectedIndex, calcRect);
        g.setColor(shadow);
        // Draw unbroken line if tabs are not on BOTTOM, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        if ((((tabPlacement != javax.swing.SwingConstants.BOTTOM) || (selectedIndex < 0)) || ((selRect.y - 1) > h)) || ((selRect.x < x) || (selRect.x > (x + w)))) {
            // g.drawLine(x + 1, y + h - 2, x + w - 1, y + h - 2);
            g.setColor(shadow);
            g.drawLine(x, (y + h) - 1, (x + w) - 1, (y + h) - 1);
        } else {
            // Break line to show visual connection to selected tab
            // g.drawLine(x + 1, y + h - 2, selRect.x - 1, y + h - 2);
            g.setColor(shadow);
            g.drawLine(x, (y + h) - 1, selRect.x, (y + h) - 1);
            if ((selRect.x + selRect.width) < ((x + w) - 2)) {
                // g.setColor(shadow);
                // g.drawLine(selRect.x + selRect.width, y+h-2, x+w-2, y+h-2);
                g.setColor(shadow);
                g.drawLine((selRect.x + selRect.width) - 1, (y + h) - 1, (x + w) - 1, (y + h) - 1);
            }
        }
    }

    @java.lang.Override
    protected void paintContentBorderRightEdge(java.awt.Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        java.awt.Rectangle selRect = (selectedIndex < 0) ? null : getTabBounds(selectedIndex, calcRect);
        g.setColor(shadow);
        // Draw unbroken line if tabs are not on RIGHT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        if ((((tabPlacement != javax.swing.SwingConstants.RIGHT) || (selectedIndex < 0)) || ((selRect.x - 1) > w)) || ((selRect.y < y) || (selRect.y > (y + h)))) {
            g.drawLine((x + w) - 2, y + 1, (x + w) - 2, (y + h) - 3);
            g.setColor(darkShadow);
            g.drawLine((x + w) - 1, y, (x + w) - 1, (y + h) - 1);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine((x + w) - 2, y + 1, (x + w) - 2, selRect.y - 1);
            g.setColor(darkShadow);
            g.drawLine((x + w) - 1, y, (x + w) - 1, selRect.y - 1);
            if ((selRect.y + selRect.height) < ((y + h) - 2)) {
                g.setColor(shadow);
                g.drawLine((x + w) - 2, selRect.y + selRect.height, (x + w) - 2, (y + h) - 2);
                g.setColor(darkShadow);
                g.drawLine((x + w) - 1, selRect.y + selRect.height, (x + w) - 1, (y + h) - 2);
            }
        }
    }

    @java.lang.Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        java.awt.Rectangle tabRect = rects[tabIndex];
        int nudge = 0;
        switch (tabPlacement) {
            case javax.swing.SwingConstants.LEFT :
                nudge = (isSelected) ? -1 : 1;
                break;
            case javax.swing.SwingConstants.RIGHT :
                nudge = (isSelected) ? 1 : -1;
                break;
            case javax.swing.SwingConstants.BOTTOM :
            case javax.swing.SwingConstants.TOP :
            default :
                nudge = tabRect.width % 2;
        }
        return nudge;
    }

    @java.lang.Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        java.awt.Rectangle tabRect = rects[tabIndex];
        int nudge = 0;
        switch (tabPlacement) {
            case javax.swing.SwingConstants.BOTTOM :
                // nudge = isSelected? 1 : -1;
                nudge = -1;
                break;
            case javax.swing.SwingConstants.LEFT :
            case javax.swing.SwingConstants.RIGHT :
                nudge = tabRect.height % 2;
                break;
            case javax.swing.SwingConstants.TOP :
            default :
                nudge = (isSelected) ? -1 : 1;
        }
        return nudge;
    }

    private boolean scrollableTabLayoutEnabled() {
        return tabPane.getTabLayoutPolicy() == javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;
    }

    @java.lang.Override
    protected java.awt.LayoutManager createLayoutManager() {
        if (tabPane.getTabLayoutPolicy() == javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT) {
            return super.createLayoutManager();
        }
        return new org.jhotdraw.gui.plaf.palette.PaletteTabbedPaneUI.TabbedPaneLayout();
    }

    @java.lang.Override
    protected java.awt.FontMetrics getFontMetrics() {
        // Font font = tabPane.getFont();
        java.awt.Font font = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont("TabbedPane.selectedFont");
        return tabPane.getFontMetrics(font);
    }

    protected class TabbedPaneLayout extends javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout {
        public TabbedPaneLayout() {
            PaletteTabbedPaneUI.this.super();
        }

        @java.lang.Override
        protected void normalizeTabRuns(int tabPlacement, int tabCount, int start, int max) {
            // Only normalize the runs for top & bottom;  normalizing
            // doesn't look right for Metal's vertical tabs
            // because the last run isn't padded and it looks odd to have
            // fat tabs in the first vertical runs, but slimmer ones in the
            // last (this effect isn't noticeable for horizontal tabs).
            if ((tabPlacement == javax.swing.SwingConstants.TOP) || (tabPlacement == javax.swing.SwingConstants.BOTTOM)) {
                super.normalizeTabRuns(tabPlacement, tabCount, start, max);
            }
        }

        // Don't rotate runs!
        @java.lang.Override
        protected void rotateTabRuns(int tabPlacement, int selectedRun) {
        }

        // Don't pad selected tab
        @java.lang.Override
        protected void padSelectedTab(int tabPlacement, int selectedIndex) {
        }
    }
}