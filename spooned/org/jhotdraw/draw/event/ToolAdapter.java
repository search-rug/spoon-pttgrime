/* @(#)ToolAdapter.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * An abstract adapter class for receiving {@link ToolEvent}s. This class exists as a convenience
 * for creating {@link ToolListener} objects.
 */
public class ToolAdapter implements org.jhotdraw.draw.event.ToolListener {
    @java.lang.Override
    public void toolStarted(org.jhotdraw.draw.event.ToolEvent event) {
    }

    @java.lang.Override
    public void toolDone(org.jhotdraw.draw.event.ToolEvent event) {
    }

    @java.lang.Override
    public void areaInvalidated(org.jhotdraw.draw.event.ToolEvent e) {
    }

    @java.lang.Override
    public void boundsInvalidated(org.jhotdraw.draw.event.ToolEvent e) {
    }

    @java.lang.Override
    public void figureCreated(org.jhotdraw.draw.event.FigureCreatedEvent e) {
    }
}