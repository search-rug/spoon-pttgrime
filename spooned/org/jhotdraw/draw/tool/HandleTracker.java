/* @(#)HandleTracker.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.handle.Handle;
/**
 * A <em>handle tracker</em> provides the behavior for manipulating a {@link Handle} of a figure to
 * the {@link SelectionTool}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Strategy</em><br>
 * The different behavior states of the selection tool are implemented by trackers.<br>
 * Context: {@link SelectionTool}; State: {@link DragTracker}, {@link HandleTracker}, {@link SelectAreaTracker}. <hr>
 */
public interface HandleTracker extends org.jhotdraw.draw.tool.Tool {
    public void setHandles(org.jhotdraw.draw.handle.Handle handle, java.util.Collection<org.jhotdraw.draw.handle.Handle> compatibleHandles);
}