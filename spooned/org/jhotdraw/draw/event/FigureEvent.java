/* @(#)FigureChangeEvent.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
import org.jhotdraw.draw.figure.Figure;
/**
 * An {@code EventObject} sent to {@link FigureListener}s.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Observer</em><br>
 * State changes of figures can be observed by other objects. Specifically {@code CompositeFigure}
 * observes area invalidations and remove requests of its child figures. {@link DrawingView} also
 * observes area invalidations of its drawing object. Subject: {@link Figure}; Observer: {@link FigureListener}; Event: {@link FigureEvent}; Concrete Observer: {@link org.jhotdraw.draw.CompositeFigure}, {@link DrawingView}. <hr>
 */
public class FigureEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Rectangle2D.Double invalidatedArea;

    private org.jhotdraw.draw.AttributeKey<?> attribute;

    private java.lang.Object oldValue;

    private java.lang.Object newValue;

    /**
     * Constructs an event for the given source Figure.
     *
     * @param invalidatedArea
     * 		The bounds of the invalidated area on the drawing.
     */
    public FigureEvent(org.jhotdraw.draw.figure.Figure source, java.awt.geom.Rectangle2D.Double invalidatedArea) {
        super(source);
        this.invalidatedArea = invalidatedArea;
    }

    /**
     * Constructs an event for the given source Figure.
     */
    public FigureEvent(org.jhotdraw.draw.figure.Figure source, org.jhotdraw.draw.AttributeKey<?> attribute, java.lang.Object oldValue, java.lang.Object newValue) {
        super(source);
        this.attribute = attribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the changed figure
     */
    public org.jhotdraw.draw.figure.Figure getFigure() {
        return ((org.jhotdraw.draw.figure.Figure) (getSource()));
    }

    /**
     * Gets the bounds of the invalidated area on the drawing.
     */
    public java.awt.geom.Rectangle2D.Double getInvalidatedArea() {
        return invalidatedArea;
    }

    public org.jhotdraw.draw.AttributeKey<?> getAttribute() {
        return attribute;
    }

    public java.lang.Object getOldValue() {
        return oldValue;
    }

    public java.lang.Object getNewValue() {
        return newValue;
    }
}