/* @(#)CompositeFigureEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.figure.CompositeFigure;
/**
 * An {@code EventObject} sent to {@link CompositeFigureListener}s.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * Changes in the composition of a composite figure can be observed.<br>
 * Subject: {@link CompositeFigure}; Observer: {@link CompositeFigureListener}; Event: {@link CompositeFigureEvent}. <hr>
 */
public class CompositeFigureEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Rectangle2D.Double invalidatedArea;

    private org.jhotdraw.draw.figure.Figure child;

    private int index;

    /**
     * Constructs an event for the provided CompositeFigure.
     *
     * @param source
     * 		The composite figure.
     * @param child
     * 		The changed figure.
     * @param invalidatedArea
     * 		The bounds of the invalidated area on the drawing.
     */
    public CompositeFigureEvent(org.jhotdraw.draw.figure.CompositeFigure source, org.jhotdraw.draw.figure.Figure child, java.awt.geom.Rectangle2D.Double invalidatedArea, int zIndex) {
        super(source);
        this.child = child;
        this.invalidatedArea = invalidatedArea;
        this.index = 0;
    }

    /**
     * Gets the changed drawing.
     */
    public org.jhotdraw.draw.figure.CompositeFigure getCompositeFigure() {
        return ((org.jhotdraw.draw.figure.CompositeFigure) (getSource()));
    }

    /**
     * Gets the changed child figure.
     */
    public org.jhotdraw.draw.figure.Figure getChildFigure() {
        return child;
    }

    /**
     * Gets the bounds of the invalidated area on the drawing.
     */
    public java.awt.geom.Rectangle2D.Double getInvalidatedArea() {
        return invalidatedArea;
    }

    /**
     * Returns the z-index of the child figure.
     */
    public int getIndex() {
        return index;
    }
}