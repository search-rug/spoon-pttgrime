/* @(#)FigureAdapter.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * An abstract adapter class for receiving {@link FigureEvent}s. This class exists as a convenience
 * for creating {@link FigureListener} objects.
 */
public class FigureListenerAdapter implements org.jhotdraw.draw.event.FigureListener {
    @java.lang.Override
    public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void attributeChanged(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void figureAdded(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void figureRemoved(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void figureRequestRemove(org.jhotdraw.draw.event.FigureEvent e) {
    }

    @java.lang.Override
    public void figureHandlesChanged(org.jhotdraw.draw.event.FigureEvent e) {
    }
}