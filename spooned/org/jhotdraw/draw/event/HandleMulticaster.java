/* @(#)HandleMulticaster.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
public class HandleMulticaster {
    private java.util.List<org.jhotdraw.draw.handle.Handle> handles;

    public HandleMulticaster(org.jhotdraw.draw.handle.Handle handle) {
        this.handles = new java.util.ArrayList<>();
        this.handles.add(handle);
    }

    public HandleMulticaster(java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        this.handles = new java.util.ArrayList<>(handles);
    }

    public void draw(java.awt.Graphics2D g) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.draw(g);
        }
    }

    public void keyPressed(java.awt.event.KeyEvent e) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.keyPressed(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    public void keyReleased(java.awt.event.KeyEvent e) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.keyReleased(e);
        }
    }

    public void keyTyped(java.awt.event.KeyEvent e) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.keyTyped(e);
        }
    }

    public void trackEnd(java.awt.Point current, java.awt.Point anchor, int modifiersEx, org.jhotdraw.draw.DrawingView view) {
        for (org.jhotdraw.draw.handle.Handle h : new org.jhotdraw.util.ReversedList<>(handles)) {
            h.trackEnd(current, anchor, modifiersEx);
        }
    }

    public void trackStart(java.awt.Point anchor, int modifiersEx, org.jhotdraw.draw.DrawingView view) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.trackStart(anchor, modifiersEx);
        }
    }

    public void trackDoubleClick(java.awt.Point p, int modifiersEx, org.jhotdraw.draw.DrawingView view) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.trackDoubleClick(p, modifiersEx);
        }
    }

    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx, org.jhotdraw.draw.DrawingView view) {
        for (org.jhotdraw.draw.handle.Handle h : handles) {
            h.trackStep(anchor, lead, modifiersEx);
        }
    }
}