/* @(#)JPopupButton.java

Copyright (c) 2006-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * JPopupButton provides a popup menu.
 */
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class JPopupButton extends javax.swing.JButton {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String CLOSE_AUTOMATICALLY_PROPERTY = "closeAutomatically";

    public static final java.lang.String COLUMN_COUNT_PROPERTY = "columnCount";

    public static final java.lang.String ITEM_FONT_PROPERTY = "itemFont";

    private javax.swing.JPopupMenu popupMenu;

    private int columnCount = 1;

    private javax.swing.Action action;

    private java.awt.Rectangle actionArea;

    private java.awt.Font itemFont;

    public static final java.awt.Font ITEM_FONT = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10);

    private int popupAnchor = javax.swing.SwingConstants.SOUTH_WEST;

    /**
     * The time when the popup became invisible.
     */
    private long popupBecameInvisible;

    /**
     * Whether the popup menu closes automatically, when another popup menu is opened.
     */
    private boolean isCloseAutomatically;

    private class Handler implements java.beans.PropertyChangeListener , javax.swing.event.PopupMenuListener , java.awt.event.AWTEventListener {
        // Property change listener
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if ("enabled".equals(evt.getPropertyName())) {
                setEnabled(((java.lang.Boolean) (evt.getNewValue())));
            } else {
                repaint();
            }
        }

        // Popup menu listener
        @java.lang.Override
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
        }

        @java.lang.Override
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
            popupBecameInvisible = java.lang.System.currentTimeMillis();
        }

        @java.lang.Override
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
        }

        // AWT event listener
        @java.lang.Override
        public void eventDispatched(java.awt.AWTEvent ev) {
            if ((!(ev instanceof java.awt.event.MouseEvent)) || (!(ev.getSource() instanceof java.awt.Component))) {
                // We are interested in MouseEvents only
                return;
            }
            java.awt.Component src = ((java.awt.Component) (ev.getSource()));
            // Close popup only on mouse press on a component which has
            // the same window ancestor as our popup, but is not in the
            // popup layer of the window.
            if (ev.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
                if (javax.swing.SwingUtilities.getWindowAncestor(src) == javax.swing.SwingUtilities.getWindowAncestor(JPopupButton.this)) {
                    javax.swing.JLayeredPane srcLP = ((javax.swing.JLayeredPane) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JLayeredPane.class, src)));
                    java.awt.Component srcLPChild = src;
                    while (srcLPChild.getParent() != srcLP) {
                        srcLPChild = srcLPChild.getParent();
                    } 
                    if (srcLP.getLayer(srcLPChild) < javax.swing.JLayeredPane.POPUP_LAYER) {
                        popupMenu.setVisible(false);
                    }
                }
            } else {
            }
        }
    }

    private org.jhotdraw.gui.JPopupButton.Handler handler = new org.jhotdraw.gui.JPopupButton.Handler();

    /**
     * Creates new form JToolBarMenu
     */
    public JPopupButton() {
        initComponents();
        setFocusable(false);
        itemFont = org.jhotdraw.gui.JPopupButton.ITEM_FONT;
    }

    /**
     * Sets the font used for popup menu items.
     */
    public void setItemFont(java.awt.Font newValue) {
        java.awt.Font oldValue = itemFont;
        itemFont = newValue;
        if (popupMenu != null) {
            updateItemFont(popupMenu);
        }
        firePropertyChange(org.jhotdraw.gui.JPopupButton.ITEM_FONT_PROPERTY, oldValue, newValue);
    }

    /**
     * Updates the font of the popup menu.
     */
    private void updateItemFont(javax.swing.MenuElement menu) {
        menu.getComponent().setFont(itemFont);
        for (javax.swing.MenuElement child : menu.getSubElements()) {
            updateItemFont(child);
        }
    }

    /**
     * Sets an action which is invoked when the user clicks on the specified click area.
     *
     * @param action
     * 		An action.
     * @param actionClickArea
     * 		The click area.
     */
    public void setAction(javax.swing.Action action, java.awt.Rectangle actionClickArea) {
        if (this.action != null) {
            this.action.removePropertyChangeListener(handler);
        }
        this.action = action;
        this.actionArea = actionClickArea;
        if (action != null) {
            action.addPropertyChangeListener(handler);
        }
    }

    /**
     * Returns the number of columns of the popup menu.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Sets the number of columns of the popup menu.
     */
    public void setColumnCount(int newValue, boolean isVertical) {
        int oldValue = columnCount;
        columnCount = newValue;
        getPopupMenu().setLayout(new org.jhotdraw.gui.VerticalGridLayout(0, getColumnCount(), isVertical));
        firePropertyChange(org.jhotdraw.gui.JPopupButton.COLUMN_COUNT_PROPERTY, oldValue, newValue);
    }

    /**
     * Adds an {@code Action} to the popup menu.
     *
     * <p>The {@code Action} is represented by a {@code JMenuItem}.
     */
    public javax.swing.AbstractButton add(javax.swing.Action action) {
        javax.swing.JMenuItem item = getPopupMenu().add(action);
        if (getColumnCount() > 1) {
            item.setUI(new org.jhotdraw.gui.plaf.palette.PaletteMenuItemUI());
        }
        item.setFont(itemFont);
        return item;
    }

    /**
     * Adds a sub-menu to the popup menu.
     */
    public void add(javax.swing.JMenu submenu) {
        updateItemFont(submenu);
    }

    /**
     * Adds a {@code JComponent} to the popup menu.
     *
     * <p>If the component can open popup menus of its own, for example if contains combo boxes, then
     * you should set {@link JComponentPopup} as the popup menu before adding the component to this
     * popup button. This will prevent the popup menu from closing automatically.
     *
     * <p>Example:
     *
     * <pre>
     * JPopupButton pb=new JPopupButton();
     * pb.setPopupMenu(new JComponentPopup());
     * pb.add(a component);
     * </pre>
     */
    public void add(javax.swing.JComponent submenu) {
        getPopupMenu().add(submenu);
    }

    /**
     * Adds a menu item to the popup menu.
     */
    public void add(javax.swing.JMenuItem item) {
        getPopupMenu().add(item);
        item.setFont(itemFont);
    }

    /**
     * Adds a separator to the popup menu.
     */
    public void addSeparator() {
        getPopupMenu().addSeparator();
    }

    /**
     * Removes all items from the popup menu.
     */
    @java.lang.Override
    public void removeAll() {
        getPopupMenu().removeAll();
    }

    public void setPopupMenu(javax.swing.JPopupMenu popupMenu) {
        if (this.popupMenu != null) {
            popupMenu.removePopupMenuListener(handler);
        }
        this.popupMenu = popupMenu;
        if (this.popupMenu != null) {
            popupMenu.addPopupMenuListener(handler);
        }
    }

    public javax.swing.JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new javax.swing.JPopupMenu();
            popupMenu.setLayout(new org.jhotdraw.gui.VerticalGridLayout(0, getColumnCount()));
            popupMenu.addPopupMenuListener(handler);
            popupMenu.setLightWeightPopupEnabled(false);
        }
        return popupMenu;
    }

    public void setPopupAlpha(float newValue) {
        float oldValue = getPopupAlpha();
        getPopupMenu().putClientProperty("Quaqua.PopupMenu.windowAlpha", newValue);
        firePropertyChange("popupAlpha", oldValue, newValue);
    }

    public float getPopupAlpha() {
        java.lang.Float value = ((java.lang.Float) (getPopupMenu().getClientProperty("Quaqua.PopupMenu.windowAlpha")));
        return value == null ? 0.948F : value;
    }

    /**
     * Gets the popup anchor.
     *
     * @return SwingConstants.SOUTH_WEST or SOUTH_EAST.
     */
    public int getPopupAnchor() {
        return popupAnchor;
    }

    /**
     * Sets the popup anchor.
     *
     * <p>
     *
     * <ul>
     *   <li>SOUTH_WEST places the popup below the button and aligns it with its left bound.
     *   <li>SOUTH_EAST places the popup below the button and aligns it with its right bound.
     * </ul>
     *
     * @param newValue
     * 		SwingConstants.SOUTH_WEST or SOUTH_EAST.
     */
    public void setPopupAnchor(int newValue) {
        popupAnchor = newValue;
    }

    protected void togglePopup(java.awt.event.MouseEvent evt) {
        if (((popupMenu != null) && popupMenu.isShowing()) || (popupBecameInvisible >= evt.getWhen())) {
            popupMenu.setVisible(false);
        } else {
            showPopup(evt);
        }
    }

    protected void showPopup(java.awt.event.MouseEvent evt) {
        // Add your handling code here:
        if ((popupMenu != null) && ((actionArea == null) || (!actionArea.contains(evt.getX() - getInsets().left, evt.getY() - getInsets().top)))) {
            int x;
            int y;
            switch (popupAnchor) {
                case javax.swing.SwingConstants.SOUTH_EAST :
                    x = getWidth() - popupMenu.getPreferredSize().width;
                    y = getHeight();
                    break;
                case javax.swing.SwingConstants.SOUTH_WEST :
                default :
                    x = 0;
                    y = getHeight();
                    break;
            }
            if (getParent() instanceof javax.swing.JToolBar) {
                javax.swing.JToolBar toolbar = ((javax.swing.JToolBar) (getParent()));
                if (toolbar.getOrientation() == javax.swing.JToolBar.VERTICAL) {
                    y = 0;
                    if (toolbar.getX() > toolbar.getParent().getInsets().left) {
                        x = -popupMenu.getPreferredSize().width;
                    } else {
                        x = getWidth();
                    }
                } else if (toolbar.getY() > toolbar.getParent().getInsets().top) {
                    y = -popupMenu.getPreferredSize().height;
                }
            }
            popupMenu.show(this, x, y);
            popupMenu.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                performAction(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents


    private void performAction(java.awt.event.MouseEvent evt) {
        // GEN-FIRST:event_performAction
        // Add your handling code here:
        if ((actionArea != null) && actionArea.contains(evt.getX() - getInsets().left, evt.getY() - getInsets().top)) {
            action.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, null, evt.getWhen(), evt.getModifiers()));
        }
    }// GEN-LAST:event_performAction


    private void handleMousePressed(java.awt.event.MouseEvent evt) {
        // GEN-FIRST:event_handleMousePressed
        togglePopup(evt);
    }// GEN-LAST:event_handleMousePressed

}