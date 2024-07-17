/* @(#)LabelFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A LabelFigure can be used to provide more double clickable area for a TextHolderFigure.
 */
public class LabelFigure extends org.jhotdraw.draw.figure.TextFigure {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.TextHolderFigure target;

    public LabelFigure() {
        this("Label");
    }

    public LabelFigure(java.lang.String text) {
        setText(text);
        setEditable(false);
    }

    public void setLabelFor(org.jhotdraw.draw.figure.TextHolderFigure target) {
        if (this.target != null) {
            this.target.removeFigureListener(FIGURE_LISTENER);
        }
        this.target = target;
        if (this.target != null) {
            this.target.addFigureListener(FIGURE_LISTENER);
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextHolderFigure getLabelFor() {
        return target == null ? this : target;
    }

    /**
     * Returns a specialized tool for the given coordinate.
     *
     * <p>
     *
     * <p>Returns null, if no specialized tool is available.
     */
    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool(java.awt.geom.Point2D.Double p) {
        return (target != null) && contains(p) ? new org.jhotdraw.draw.tool.TextEditingTool(target) : null;
    }

    private final org.jhotdraw.draw.event.FigureListener FIGURE_LISTENER = new org.jhotdraw.draw.event.FigureListenerAdapter() {
        @java.lang.Override
        public void figureRemoved(org.jhotdraw.draw.event.FigureEvent e) {
            if (e.getFigure() == target) {
                target.removeFigureListener(this);
                target = null;
            }
        }

        @java.lang.Override
        public void figureRequestRemove(org.jhotdraw.draw.event.FigureEvent e) {
        }
    };

    @java.lang.Override
    public void remap(java.util.Map<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> oldToNew, boolean disconnectIfNotInMap) {
        super.remap(oldToNew, disconnectIfNotInMap);
        if (target != null) {
            org.jhotdraw.draw.figure.Figure newTarget = oldToNew.get(target);
            if (newTarget != null) {
                target.removeFigureListener(FIGURE_LISTENER);
                target = ((org.jhotdraw.draw.figure.TextHolderFigure) (newTarget));
                newTarget.addFigureListener(FIGURE_LISTENER);
            }
        }
    }
}