/* @(#)JMDIDesktopPane.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * An extension of JDesktopPane that supports often used MDI functionality. This class also handles
 * setting scroll bars for when windows move too far to the left or bottom, providing the
 * JMDIDesktopPane is in a ScrollPane. Note by dnoyeb: I dont know why the container does not fire
 * frame close events when the frames are removed from the container with remove as opposed to
 * simply closed with the "x". so if you say removeAll from container you wont be notified. No
 * biggie.
 *
 * @author Werner Randelshofer Original version by Wolfram Kaiser (adapted from an article in
JavaWorld), C.L.Gilbert &lt;dnoyeb@users.sourceforge.net&gt;
 * @version $Id$
 */
public class JMDIDesktopPane extends javax.swing.JDesktopPane implements org.jhotdraw.api.gui.Arrangeable {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.gui.MDIDesktopManager manager;

    public JMDIDesktopPane() {
        manager = new org.jhotdraw.gui.MDIDesktopManager(this);
        setDesktopManager(manager);
        setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
    }

    @java.lang.Override
    public void setArrangement(org.jhotdraw.api.gui.Arrangeable.Arrangement newValue) {
        org.jhotdraw.api.gui.Arrangeable.Arrangement oldValue = getArrangement();
        switch (newValue) {
            case CASCADE :
                arrangeFramesCascading();
                break;
            case HORIZONTAL :
                arrangeFramesHorizontally();
                break;
            case VERTICAL :
                arrangeFramesVertically();
                break;
        }
        firePropertyChange("arrangement", oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.api.gui.Arrangeable.Arrangement getArrangement() {
        // FIXME Check for the arrangement of the JInternalFrames here
        // and return the true value
        return org.jhotdraw.api.gui.Arrangeable.Arrangement.CASCADE;
    }

    /**
     * Cascade all internal frames
     */
    private void arrangeFramesCascading() {
        javax.swing.JInternalFrame[] allFrames = getAllFrames();
        // do nothing if no frames to work with
        if (allFrames.length == 0) {
            return;
        }
        manager.setNormalSize();
        java.awt.Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int frameOffset = 0;
        for (int i = allFrames.length - 1; i >= 0; i--) {
            java.awt.Point p = javax.swing.SwingUtilities.convertPoint(allFrames[i].getContentPane(), 0, 0, allFrames[i]);
            frameOffset = java.lang.Math.max(frameOffset, java.lang.Math.max(p.x, p.y));
        }
        int frameHeight = ((getBounds().height - insets.top) - insets.bottom) - (allFrames.length * frameOffset);
        int frameWidth = ((getBounds().width - insets.left) - insets.right) - (allFrames.length * frameOffset);
        for (int i = allFrames.length - 1; i >= 0; i--) {
            try {
                allFrames[i].setMaximum(false);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            allFrames[i].setBounds(x, y, frameWidth, frameHeight);
            x = x + frameOffset;
            y = y + frameOffset;
        }
        checkDesktopSize();
    }

    private void tileFramesHorizontally() {
        java.awt.Component[] allFrames = getAllFrames();
        // do nothing if no frames to work with
        if (allFrames.length == 0) {
            return;
        }
        manager.setNormalSize();
        int frameHeight = getBounds().height / allFrames.length;
        int y = 0;
        for (java.awt.Component allFrame : allFrames) {
            try {
                ((javax.swing.JInternalFrame) (allFrame)).setMaximum(false);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            allFrame.setBounds(0, y, getBounds().width, frameHeight);
            y = y + frameHeight;
        }
        checkDesktopSize();
    }

    public void tileFramesVertically() {
        java.awt.Component[] allFrames = getAllFrames();
        // do nothing if no frames to work with
        if (allFrames.length == 0) {
            return;
        }
        manager.setNormalSize();
        int frameWidth = getBounds().width / allFrames.length;
        int x = 0;
        for (java.awt.Component allFrame : allFrames) {
            try {
                ((javax.swing.JInternalFrame) (allFrame)).setMaximum(false);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            allFrame.setBounds(x, 0, frameWidth, getBounds().height);
            x = x + frameWidth;
        }
        checkDesktopSize();
    }

    /**
     * Arranges the frames as efficiently as possibly with preference for keeping vertical size
     * maximal.<br>
     */
    public void arrangeFramesVertically() {
        java.awt.Component[] allFrames = getAllFrames();
        // do nothing if no frames to work with
        if (allFrames.length == 0) {
            return;
        }
        manager.setNormalSize();
        int vertFrames = ((int) (java.lang.Math.floor(java.lang.Math.sqrt(allFrames.length))));
        int horFrames = ((int) (java.lang.Math.ceil(java.lang.Math.sqrt(allFrames.length))));
        // first arrange the windows that have equal size
        int frameWidth = getBounds().width / horFrames;
        int frameHeight = getBounds().height / vertFrames;
        int x = 0;
        int y = 0;
        int frameIdx = 0;
        for (int horCnt = 0; horCnt < (horFrames - 1); horCnt++) {
            y = 0;
            for (int vertCnt = 0; vertCnt < vertFrames; vertCnt++) {
                try {
                    ((javax.swing.JInternalFrame) (allFrames[frameIdx])).setMaximum(false);
                } catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                }
                allFrames[frameIdx].setBounds(x, y, frameWidth, frameHeight);
                frameIdx++;
                y = y + frameHeight;
            }
            x = x + frameWidth;
        }
        // the rest of the frames are tiled down on the last column with equal
        // height
        frameHeight = getBounds().height / (allFrames.length - frameIdx);
        y = 0;
        for (; frameIdx < allFrames.length; frameIdx++) {
            try {
                ((javax.swing.JInternalFrame) (allFrames[frameIdx])).setMaximum(false);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            allFrames[frameIdx].setBounds(x, y, frameWidth, frameHeight);
            y = y + frameHeight;
        }
        checkDesktopSize();
    }

    /**
     * Arranges the frames as efficiently as possibly with preference for keeping horizontal size
     * maximal.<br>
     */
    public void arrangeFramesHorizontally() {
        java.awt.Component[] allFrames = getAllFrames();
        // do nothing if no frames to work with
        if (allFrames.length == 0) {
            return;
        }
        manager.setNormalSize();
        int vertFrames = ((int) (java.lang.Math.ceil(java.lang.Math.sqrt(allFrames.length))));
        int horFrames = ((int) (java.lang.Math.floor(java.lang.Math.sqrt(allFrames.length))));
        // first arrange the windows that have equal size
        int frameWidth = getBounds().width / horFrames;
        int frameHeight = getBounds().height / vertFrames;
        int x = 0;
        int y = 0;
        int frameIdx = 0;
        for (int vertCnt = 0; vertCnt < (vertFrames - 1); vertCnt++) {
            x = 0;
            for (int horCnt = 0; horCnt < horFrames; horCnt++) {
                try {
                    ((javax.swing.JInternalFrame) (allFrames[frameIdx])).setMaximum(false);
                } catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                }
                allFrames[frameIdx].setBounds(x, y, frameWidth, frameHeight);
                frameIdx++;
                x = x + frameWidth;
            }
            y = y + frameHeight;
        }
        // the rest of the frames are tiled down on the last column with equal
        // height
        frameWidth = getBounds().width / (allFrames.length - frameIdx);
        x = 0;
        for (; frameIdx < allFrames.length; frameIdx++) {
            try {
                ((javax.swing.JInternalFrame) (allFrames[frameIdx])).setMaximum(false);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            allFrames[frameIdx].setBounds(x, y, frameWidth, frameHeight);
            x = x + frameWidth;
        }
        checkDesktopSize();
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred) to the given dimension.
     */
    public void setAllSize(java.awt.Dimension d) {
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
        setBounds(0, 0, d.width, d.height);
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred) to the given width and
     * height.
     */
    public void setAllSize(int width, int height) {
        setAllSize(new java.awt.Dimension(width, height));
    }

    private void checkDesktopSize() {
        if ((getParent() != null) && isVisible()) {
            manager.resizeDesktop();
        }
    }
}