/* @(#)ToolEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.tool.Tool;
/**
 * An event sent to ToolListener's. <hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * State changes of tools can be observed by other objects. Specifically {@code DrawingEditor}
 * observes area invalidations of tools and repaints its active drawing view accordingly.<br>
 * Subject: {@link Tool}; Observer: {@link ToolListener}; Event: {@link ToolEvent}; Concrete
 * Observer: {@link DrawingEditor}. <hr>
 */
public class ToolEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;

    private final java.awt.Rectangle invalidatedArea;

    private final org.jhotdraw.draw.DrawingView view;

    public ToolEvent(org.jhotdraw.draw.tool.Tool src, org.jhotdraw.draw.DrawingView view, java.awt.Rectangle invalidatedArea) {
        super(src);
        this.view = view;
        this.invalidatedArea = invalidatedArea;
    }

    /**
     * Gets the tool which is the source of the event.
     */
    public org.jhotdraw.draw.tool.Tool getTool() {
        return ((org.jhotdraw.draw.tool.Tool) (getSource()));
    }

    /**
     * Gets the drawing view of the tool.
     */
    public org.jhotdraw.draw.DrawingView getView() {
        return view;
    }

    /**
     * Gets the bounds of the invalidated area on the drawing view.
     */
    public java.awt.Rectangle getInvalidatedArea() {
        return invalidatedArea;
    }
}