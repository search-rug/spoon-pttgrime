/* @(#)HandleEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.handle.Handle;
/**
 * An {@code EventObject} sent to {@link HandleListener}s.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * State changes of handles can be observed by other objects. Specifically {@code DrawingView}
 * observes area invalidations and remove requests of handles.<br>
 * Subject: {@link Handle}; Observer: {@link HandleListener}; Event: {@link HandleEvent}; Concrete
 * Observer: {@link DrawingView}. <hr>
 */
public class HandleEvent extends java.util.EventObject {
    private java.awt.Rectangle invalidatedArea;

    private static final long serialVersionUID = 1L;

    public HandleEvent(org.jhotdraw.draw.handle.Handle src, java.awt.Rectangle invalidatedArea) {
        super(src);
        this.invalidatedArea = invalidatedArea;
    }

    public org.jhotdraw.draw.handle.Handle getHandle() {
        return ((org.jhotdraw.draw.handle.Handle) (getSource()));
    }

    /**
     * Gets the bounds of the invalidated area on the drawing view.
     */
    public java.awt.Rectangle getInvalidatedArea() {
        return invalidatedArea;
    }
}