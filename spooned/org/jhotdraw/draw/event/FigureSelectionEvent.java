/* @(#)FigureSelectionEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * An {@code EventObject} sent to {@link FigureSelectionListener}s.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * Selection changes of {@code DrawingView} are observed by user interface components which act on
 * selected figures.<br>
 * Subject: {@link org.jhotdraw.draw.DrawingView}; Observer: {@link FigureSelectionListener}; Event:
 * {@link FigureSelectionEvent}. <hr>
 */
public class FigureSelectionEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;

    private java.util.Set<org.jhotdraw.draw.figure.Figure> oldValue;

    private java.util.Set<org.jhotdraw.draw.figure.Figure> newValue;

    public FigureSelectionEvent(org.jhotdraw.draw.DrawingView source, java.util.Set<org.jhotdraw.draw.figure.Figure> oldValue, java.util.Set<org.jhotdraw.draw.figure.Figure> newValue) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public org.jhotdraw.draw.DrawingView getView() {
        return ((org.jhotdraw.draw.DrawingView) (source));
    }

    public java.util.Set<org.jhotdraw.draw.figure.Figure> getOldSelection() {
        return oldValue;
    }

    public java.util.Set<org.jhotdraw.draw.figure.Figure> getNewSelection() {
        return newValue;
    }
}