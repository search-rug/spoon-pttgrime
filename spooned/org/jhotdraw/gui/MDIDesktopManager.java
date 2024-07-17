/* @(#)JMDIDesktopPane.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * Private class used to replace the standard DesktopManager for JDesktopPane. Used to provide
 * scrollbar functionality.
 */
class MDIDesktopManager extends javax.swing.DefaultDesktopManager {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.gui.JMDIDesktopPane desktop;

    public MDIDesktopManager(org.jhotdraw.gui.JMDIDesktopPane newDesktop) {
        this.desktop = newDesktop;
    }

    @java.lang.Override
    public void endResizingFrame(javax.swing.JComponent f) {
        super.endResizingFrame(f);
        resizeDesktop();
    }

    @java.lang.Override
    public void endDraggingFrame(javax.swing.JComponent f) {
        super.endDraggingFrame(f);
        resizeDesktop();
    }

    public void setNormalSize() {
        javax.swing.JScrollPane scrollPane = getScrollPane();
        java.awt.Insets scrollInsets = getScrollPaneInsets();
        if (scrollPane != null) {
            java.awt.Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
                d.setSize((d.getWidth() - scrollInsets.left) - scrollInsets.right, (d.getHeight() - scrollInsets.top) - scrollInsets.bottom);
            }
            d.setSize(d.getWidth() - 20, d.getHeight() - 20);
            desktop.setAllSize(d);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }

    private java.awt.Insets getScrollPaneInsets() {
        javax.swing.JScrollPane scrollPane = getScrollPane();
        if ((scrollPane == null) || (getScrollPane().getBorder() == null)) {
            return new java.awt.Insets(0, 0, 0, 0);
        } else {
            return getScrollPane().getBorder().getBorderInsets(scrollPane);
        }
    }

    public javax.swing.JScrollPane getScrollPane() {
        if (desktop.getParent() instanceof javax.swing.JViewport) {
            javax.swing.JViewport viewPort = ((javax.swing.JViewport) (desktop.getParent()));
            if (viewPort.getParent() instanceof javax.swing.JScrollPane) {
                return ((javax.swing.JScrollPane) (viewPort.getParent()));
            }
        }
        return null;
    }

    protected void resizeDesktop() {
        int x = 0;
        int y = 0;
        javax.swing.JScrollPane scrollPane = getScrollPane();
        java.awt.Insets scrollInsets = getScrollPaneInsets();
        if (scrollPane != null) {
            javax.swing.JInternalFrame allFrames[] = desktop.getAllFrames();
            for (javax.swing.JInternalFrame allFrame : allFrames) {
                if ((allFrame.getX() + allFrame.getWidth()) > x) {
                    x = allFrame.getX() + allFrame.getWidth();
                }
                if ((allFrame.getY() + allFrame.getHeight()) > y) {
                    y = allFrame.getY() + allFrame.getHeight();
                }
            }
            java.awt.Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
                d.setSize((d.getWidth() - scrollInsets.left) - scrollInsets.right, (d.getHeight() - scrollInsets.top) - scrollInsets.bottom);
            }
            if (x <= d.getWidth()) {
                x = ((int) (d.getWidth())) - 20;
            }
            if (y <= d.getHeight()) {
                y = ((int) (d.getHeight())) - 20;
            }
            desktop.setAllSize(x, y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
}