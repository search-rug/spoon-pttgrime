/* @(#)JComponentPopup.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
import javax.swing.JPopupMenu;
/**
 * This is an extension of the Swing {@code JPopupMenu} which can be used to display a {@code JComponent} in a popup menu.
 *
 * <p>Unlike {@code JPopupMenu}, the popup will stay open if the {@code JComponent} opens a popup
 * menu of its own.
 */
public class JComponentPopup extends javax.swing.JPopupMenu {
    private static final long serialVersionUID = 1L;

    /**
     * Wether we are permitted to listen on AWT events.
     */
    private boolean isAWTEventListenerPermitted = true;

    private class Handler implements java.awt.event.AWTEventListener {
        @java.lang.Override
        public void eventDispatched(java.awt.AWTEvent ev) {
            if ((!(ev instanceof java.awt.event.MouseEvent)) || (!(ev.getSource() instanceof java.awt.Component))) {
                // We are interested in MouseEvents only
                return;
            }
            java.awt.event.MouseEvent me = ((java.awt.event.MouseEvent) (ev));
            java.awt.Component src = ((java.awt.Component) (ev.getSource()));
            java.awt.Component invoker = JComponentPopup.this.getInvoker();
            if (ev.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
                // Close popup if the mouse press occured on a component which is
                // not descending from this popup menu, but has the same
                // window ancestor.
                if ((!javax.swing.SwingUtilities.isDescendingFrom(src, JComponentPopup.this)) && (javax.swing.SwingUtilities.getWindowAncestor(src) == javax.swing.SwingUtilities.getWindowAncestor(invoker))) {
                    javax.swing.JLayeredPane srcLP = ((javax.swing.JLayeredPane) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JLayeredPane.class, src)));
                    java.awt.Component srcLPChild = src;
                    while (srcLPChild.getParent() != srcLP) {
                        srcLPChild = srcLPChild.getParent();
                    } 
                    if (srcLP.getLayer(srcLPChild) < javax.swing.JLayeredPane.POPUP_LAYER) {
                        JComponentPopup.this.setVisible(false);
                    }
                }
            } else if (ev.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
                // Close popup if a double click occured on the popup component.
                if ((me.getClickCount() == 2) && javax.swing.SwingUtilities.isDescendingFrom(src, JComponentPopup.this)) {
                    JComponentPopup.this.setVisible(false);
                }
            }
        }
    }

    private org.jhotdraw.gui.JComponentPopup.Handler handler = new org.jhotdraw.gui.JComponentPopup.Handler();

    public JComponentPopup() {
        setLightWeightPopupEnabled(false);
    }

    @java.lang.Override
    public void menuSelectionChanged(boolean isIncluded) {
        if (isAWTEventListenerPermitted) {
            // Don't let the MenuSelectionManager hide this popup.
            return;
        } else {
            // Since we are not allowed to use an AWTEventListener we
            // grab the current AWT Event ourselves (hoping that this method
            // invocation is associated to it) and try to decide whether
            // we want to close the popup.
            // This will prevent undesired closing of the popup component when
            // a combo box is opened on the popup component.
            // After this happened though, menuSelectionChanged is not invoked
            // anymore and we lose the ability to close the popup component.
            java.awt.AWTEvent evt = java.awt.EventQueue.getCurrentEvent();
            if ((evt != null) && (evt.getSource() instanceof java.awt.Component)) {
                java.awt.Component src = ((java.awt.Component) (evt.getSource()));
                java.awt.Component invoker = getInvoker();
                if ((!javax.swing.SwingUtilities.isDescendingFrom(src, this)) && (javax.swing.SwingUtilities.getWindowAncestor(src) == javax.swing.SwingUtilities.getWindowAncestor(invoker))) {
                    javax.swing.JLayeredPane srcLP = ((javax.swing.JLayeredPane) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JLayeredPane.class, src)));
                    java.awt.Component srcLPChild = src;
                    while (srcLPChild.getParent() != srcLP) {
                        srcLPChild = srcLPChild.getParent();
                    } 
                    if (srcLP.getLayer(srcLPChild) < javax.swing.JLayeredPane.POPUP_LAYER) {
                        this.setVisible(false);
                    }
                }
            } else {
                super.menuSelectionChanged(isIncluded);
            }
        }
    }

    @java.lang.Override
    public void setVisible(boolean newValue) {
        // Attach/detach AWTEventListener on "visible" property change.
        if (isVisible() != newValue) {
            if (isAWTEventListenerPermitted) {
                try {
                    if (newValue) {
                        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(handler, java.awt.AWTEvent.MOUSE_EVENT_MASK);
                    } else {
                        java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(handler);
                    }
                } catch (java.security.AccessControlException e) {
                    // Unsigned Applets are not allowed to use an AWTEventListener.
                    isAWTEventListenerPermitted = false;
                }
            }
            super.setVisible(newValue);
        }
    }
}