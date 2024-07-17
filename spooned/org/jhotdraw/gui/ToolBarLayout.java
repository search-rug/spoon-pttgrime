/**
 *
 * @(#)ToolBarLayout.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * A layout which lays out components horizontally or vertically according to their preferred size.
 */
public class ToolBarLayout implements java.awt.LayoutManager2 , java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Specifies that components should be laid out left to right.
     */
    public static final int X_AXIS = 0;

    /**
     * Specifies that components should be laid out top to bottom.
     */
    public static final int Y_AXIS = 1;

    /**
     * Specifies the axis of the layout.
     */
    private int axis;

    /**
     * Creates a layout manager that will lay out components along the X-axis.
     */
    public ToolBarLayout() {
        this(org.jhotdraw.gui.ToolBarLayout.X_AXIS);
    }

    /**
     * Creates a layout manager that will lay out components along the given axis.
     *
     * @param axis
     * 		the axis to lay out components along. Can be one of: <code>BoxLayout.X_AXIS</code>,
     * 		<code>BoxLayout.Y_AXIS</code>,
     * @exception AWTError
     * 		if the value of <code>axis</code> is invalid
     */
    public ToolBarLayout(int axis) {
        this.axis = axis;
    }

    @java.lang.Override
    public void addLayoutComponent(java.awt.Component comp, java.lang.Object constraints) {
    }

    @java.lang.Override
    public java.awt.Dimension maximumLayoutSize(java.awt.Container target) {
        return preferredLayoutSize(target);
    }

    @java.lang.Override
    public float getLayoutAlignmentX(java.awt.Container target) {
        switch (axis) {
            case org.jhotdraw.gui.ToolBarLayout.Y_AXIS :
                return 0.0F;
            case org.jhotdraw.gui.ToolBarLayout.X_AXIS :
            default :
                return 0.0F;
        }
    }

    @java.lang.Override
    public float getLayoutAlignmentY(java.awt.Container target) {
        switch (axis) {
            case org.jhotdraw.gui.ToolBarLayout.Y_AXIS :
                return 0.0F;
            case org.jhotdraw.gui.ToolBarLayout.X_AXIS :
            default :
                return 0.0F;
        }
    }

    @java.lang.Override
    public void invalidateLayout(java.awt.Container target) {
    }

    @java.lang.Override
    public void addLayoutComponent(java.lang.String name, java.awt.Component comp) {
    }

    @java.lang.Override
    public void removeLayoutComponent(java.awt.Component comp) {
    }

    @java.lang.Override
    public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
        int w = 0;
        int h = 0;
        switch (axis) {
            case org.jhotdraw.gui.ToolBarLayout.Y_AXIS :
                for (java.awt.Component c : parent.getComponents()) {
                    java.awt.Dimension ps = c.getPreferredSize();
                    w = java.lang.Math.max(w, ps.width);
                    h += ps.height;
                }
                break;
            case org.jhotdraw.gui.ToolBarLayout.X_AXIS :
            default :
                for (java.awt.Component c : parent.getComponents()) {
                    java.awt.Dimension ps = c.getPreferredSize();
                    h = java.lang.Math.max(h, ps.height);
                    w += ps.width;
                }
        }
        java.awt.Insets i = parent.getInsets();
        return new java.awt.Dimension((w + i.left) + i.right, (h + i.top) + i.bottom);
    }

    @java.lang.Override
    public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
        return preferredLayoutSize(parent);
    }

    @java.lang.Override
    public void layoutContainer(java.awt.Container parent) {
        java.awt.Dimension ps = preferredLayoutSize(parent);
        java.awt.Insets insets = parent.getInsets();
        int w = (ps.width - insets.left) - insets.right;
        int h = (ps.height - insets.top) - insets.bottom;
        int x = insets.left;
        int y = insets.top;
        switch (axis) {
            case org.jhotdraw.gui.ToolBarLayout.Y_AXIS :
                for (java.awt.Component c : parent.getComponents()) {
                    ps = c.getPreferredSize();
                    c.setBounds(x, y, w, ps.height);
                    y += ps.height;
                }
                break;
            case org.jhotdraw.gui.ToolBarLayout.X_AXIS :
            default :
                for (java.awt.Component c : parent.getComponents()) {
                    ps = c.getPreferredSize();
                    c.setBounds(x, y, ps.width, h);
                    x += ps.width;
                }
        }
    }
}