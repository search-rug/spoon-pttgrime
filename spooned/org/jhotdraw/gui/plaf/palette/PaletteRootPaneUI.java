/**
 *
 * @(#)PaletteRootPaneUI.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteRootPaneUI.
 */
public class PaletteRootPaneUI extends javax.swing.plaf.basic.BasicRootPaneUI {
    private static javax.swing.plaf.RootPaneUI rootPaneUI = new org.jhotdraw.gui.plaf.palette.PaletteRootPaneUI();

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return org.jhotdraw.gui.plaf.palette.PaletteRootPaneUI.rootPaneUI;
    }

    @java.lang.Override
    public void installUI(javax.swing.JComponent c) {
        super.installUI(c);
        c.setLayout(new org.jhotdraw.gui.plaf.palette.PaletteRootPaneUI.PaletteRootLayout(((javax.swing.JRootPane) (c))));
    }

    /**
     * A custom layout manager that is responsible for the layout of layeredPane, glassPane, and
     * menuBar.
     *
     * <p><strong>Warning:</strong> Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is appropriate for short term storage
     * or RMI between applications running the same version of Swing. As of 1.4, support for long term
     * storage of all JavaBeans<sup><font size="-2">TM</font></sup> has been added to the <code>
     * java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
     */
    protected static class PaletteRootLayout implements java.awt.LayoutManager2 , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private javax.swing.JRootPane rootPane;

        public PaletteRootLayout(javax.swing.JRootPane rootPane) {
            this.rootPane = rootPane;
        }

        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent
         * 		the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's preferred size
         */
        @java.lang.Override
        public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
            java.awt.Dimension rd;
            java.awt.Dimension mbd;
            java.awt.Insets i = rootPane.getInsets();
            java.awt.Container contentPane = rootPane.getContentPane();
            javax.swing.JMenuBar menuBar = rootPane.getJMenuBar();
            if (contentPane.isVisible()) {
                rd = contentPane.getPreferredSize();
            } else {
                rd = new java.awt.Dimension(0, contentPane.getPreferredSize().height);
            }
            if ((menuBar != null) && menuBar.isVisible()) {
                mbd = menuBar.getPreferredSize();
            } else {
                mbd = new java.awt.Dimension(0, 0);
            }
            return new java.awt.Dimension((java.lang.Math.max(rd.width, mbd.width) + i.left) + i.right, ((rd.height + mbd.height) + i.top) + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent
         * 		the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's minimum size
         */
        @java.lang.Override
        public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
            java.awt.Dimension rd;
            java.awt.Dimension mbd;
            java.awt.Insets i = rootPane.getInsets();
            java.awt.Container contentPane = rootPane.getContentPane();
            javax.swing.JMenuBar menuBar = rootPane.getJMenuBar();
            if ((contentPane != null) && contentPane.isVisible()) {
                rd = contentPane.getMinimumSize();
            } else if (contentPane != null) {
                rd = new java.awt.Dimension(0, contentPane.getPreferredSize().height);
            } else {
                rd = new java.awt.Dimension(0, 0);
            }
            if ((menuBar != null) && menuBar.isVisible()) {
                mbd = menuBar.getMinimumSize();
            } else {
                mbd = new java.awt.Dimension(0, 0);
            }
            return new java.awt.Dimension((java.lang.Math.max(rd.width, mbd.width) + i.left) + i.right, ((rd.height + mbd.height) + i.top) + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target
         * 		the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's maximum size
         */
        @java.lang.Override
        public java.awt.Dimension maximumLayoutSize(java.awt.Container target) {
            java.awt.Dimension rd;
            java.awt.Dimension mbd;
            java.awt.Insets i = rootPane.getInsets();
            java.awt.Container contentPane = rootPane.getContentPane();
            javax.swing.JMenuBar menuBar = rootPane.getJMenuBar();
            if ((menuBar != null) && menuBar.isVisible()) {
                mbd = menuBar.getMaximumSize();
            } else {
                mbd = new java.awt.Dimension(0, 0);
            }
            if ((contentPane != null) && contentPane.isVisible()) {
                rd = contentPane.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                rd = new java.awt.Dimension(java.lang.Integer.MAX_VALUE, (((java.lang.Integer.MAX_VALUE - i.top) - i.bottom) - mbd.height) - 1);
            }
            return new java.awt.Dimension((java.lang.Math.min(rd.width, mbd.width) + i.left) + i.right, ((rd.height + mbd.height) + i.top) + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified container.
         *
         * @param parent
         * 		the Container for which this layout manager is being used
         */
        @java.lang.Override
        public void layoutContainer(java.awt.Container parent) {
            java.awt.Rectangle b = parent.getBounds();
            java.awt.Insets i = rootPane.getInsets();
            javax.swing.JLayeredPane layeredPane = rootPane.getLayeredPane();
            java.awt.Component glassPane = rootPane.getGlassPane();
            java.awt.Container contentPane = rootPane.getContentPane();
            javax.swing.JMenuBar menuBar = rootPane.getJMenuBar();
            int contentY = 0;
            int w = (b.width - i.right) - i.left;
            int h = (b.height - i.top) - i.bottom;
            if (layeredPane != null) {
                layeredPane.setBounds(i.left, i.top, w, h);
            }
            if (glassPane != null) {
                glassPane.setBounds(i.left, i.top, w, h);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our children.
            if ((menuBar != null) && menuBar.isVisible()) {
                java.awt.Dimension mbd = menuBar.getPreferredSize();
                menuBar.setBounds(0, 0, w, mbd.height);
                contentY += mbd.height;
            }
            if (contentPane != null) {
                contentPane.setBounds(0, contentY, w, h - contentY);
            }
        }

        @java.lang.Override
        public void addLayoutComponent(java.lang.String name, java.awt.Component comp) {
        }

        @java.lang.Override
        public void removeLayoutComponent(java.awt.Component comp) {
        }

        @java.lang.Override
        public void addLayoutComponent(java.awt.Component comp, java.lang.Object constraints) {
        }

        @java.lang.Override
        public float getLayoutAlignmentX(java.awt.Container target) {
            return 0.0F;
        }

        @java.lang.Override
        public float getLayoutAlignmentY(java.awt.Container target) {
            return 0.0F;
        }

        @java.lang.Override
        public void invalidateLayout(java.awt.Container target) {
        }
    }
}