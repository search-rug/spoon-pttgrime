/* @(#)ListFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A ListFigure consists of a list of Figures and a RectangleFigure.
 */
public class ListFigure extends org.jhotdraw.draw.figure.GraphicalCompositeFigure {
    private static final long serialVersionUID = 1L;

    public ListFigure() {
        this(null);
    }

    /**
     * Creates a new instance with the specified presentation figure and layout insets of
     * [top=4,left=8,right=4,bottom=8].
     */
    public ListFigure(org.jhotdraw.draw.figure.Figure presentationFigure) {
        super(presentationFigure);
        setLayouter(new org.jhotdraw.draw.layouter.VerticalLayouter());
        attr().set(org.jhotdraw.draw.figure.CompositeFigure.LAYOUT_INSETS, new org.jhotdraw.geom.Insets2D.Double(4, 8, 4, 8));
    }
}