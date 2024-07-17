/* @(#)QuadTreeCompositeFigure.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * QuadTreeCompositeFigure.
 */
public abstract class QuadTreeCompositeFigure extends org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure {
    private static final long serialVersionUID = 1L;

    private final org.jhotdraw.geom.QuadTree<org.jhotdraw.draw.figure.Figure> quadTree = new org.jhotdraw.geom.QuadTree<>();

    private boolean needsSorting = false;

    private final org.jhotdraw.draw.figure.QuadTreeCompositeFigure.FigureHandler figureHandler = new org.jhotdraw.draw.figure.QuadTreeCompositeFigure.FigureHandler();

    private org.jhotdraw.geom.Dimension2DDouble canvasSize;

    public QuadTreeCompositeFigure() {
    }

    @java.lang.Override
    public int indexOf(org.jhotdraw.draw.figure.Figure figure) {
        return children.indexOf(figure);
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        children.add(index, figure);
        quadTree.add(figure, figure.getDrawingArea());
        figure.addFigureListener(figureHandler);
        needsSorting = true;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure basicRemoveChild(int index) {
        org.jhotdraw.draw.figure.Figure figure = children.get(index);
        children.remove(index);
        quadTree.remove(figure);
        figure.removeFigureListener(figureHandler);
        needsSorting = true;
        return figure;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
        if (clipBounds != null) {
            java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findIntersects(clipBounds);
            java.util.Collection<org.jhotdraw.draw.figure.Figure> toDraw = sort(c);
            draw(g, toDraw);
        } else {
            draw(g, children);
        }
    }

    /**
     * Implementation note: Sorting can not be done for orphaned children.
     */
    public java.util.List<org.jhotdraw.draw.figure.Figure> sort(java.util.Collection<org.jhotdraw.draw.figure.Figure> c) {
        ensureSorted();
        java.util.ArrayList<org.jhotdraw.draw.figure.Figure> sorted = new java.util.ArrayList<>(c.size());
        for (org.jhotdraw.draw.figure.Figure f : children) {
            if (c.contains(f)) {
                sorted.add(f);
            }
        }
        return sorted;
    }

    public void draw(java.awt.Graphics2D g, java.util.Collection<org.jhotdraw.draw.figure.Figure> c) {
        for (org.jhotdraw.draw.figure.Figure f : c) {
            f.draw(g);
        }
    }

    // public Collection<Figure> getFigures(Rectangle2D.Double bounds) {
    // return new ArrayList<>(quadTree.findInside(bounds));
    // }
    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildren() {
        return java.util.Collections.unmodifiableList(children);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureInside(java.awt.geom.Point2D.Double p) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (c.contains(f) && f.contains(p)) {
                return f.findFigureInside(p);
            }
        }
        return null;
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the children.
     */
    public java.util.List<org.jhotdraw.draw.figure.Figure> getFiguresFrontToBack() {
        ensureSorted();
        return new org.jhotdraw.util.ReversedList<>(children);
    }

    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.geom.Point2D.Double p) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return f.contains(p) ? f : null;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack()) {
                    if (c.contains(f2) && f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure ignore) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return (f == ignore) || (!f.contains(p)) ? null : f;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack()) {
                    if ((f2 != ignore) && f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, java.util.Collection<org.jhotdraw.draw.figure.Figure> ignore) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return (!ignore.contains(f)) || (!f.contains(p)) ? null : f;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack()) {
                    if ((!ignore.contains(f2)) && f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    public org.jhotdraw.draw.figure.Figure findFigureBehind(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure figure) {
        boolean isBehind = false;
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (isBehind) {
                if (f.isVisible() && f.contains(p)) {
                    return f;
                }
            } else {
                isBehind = figure == f;
            }
        }
        return null;
    }

    public org.jhotdraw.draw.figure.Figure findFigureBehind(java.awt.geom.Point2D.Double p, java.util.Collection<org.jhotdraw.draw.figure.Figure> figures) {
        int inFrontOf = figures.size();
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (inFrontOf == 0) {
                if (f.isVisible() && f.contains(p)) {
                    return f;
                }
            } else if (figures.contains(f)) {
                inFrontOf--;
            }
        }
        return null;
    }

    public java.util.List<org.jhotdraw.draw.figure.Figure> findFigures(java.awt.geom.Rectangle2D.Double r) {
        java.util.List<org.jhotdraw.draw.figure.Figure> c = new java.util.ArrayList<>(quadTree.findIntersects(r));
        switch (c.size()) {
            case 0 :
                // fall through
            case 1 :
                return c;
            default :
                return sort(c);
        }
    }

    public java.util.List<org.jhotdraw.draw.figure.Figure> findFiguresWithin(java.awt.geom.Rectangle2D.Double bounds) {
        java.util.List<org.jhotdraw.draw.figure.Figure> contained = new java.util.ArrayList<>();
        for (org.jhotdraw.draw.figure.Figure f : children) {
            java.awt.geom.Rectangle2D r = f.getBounds();
            if (f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                r = f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(r).getBounds2D();
            }
            if (f.isVisible() && bounds.contains(r)) {
                contained.add(f);
            }
        }
        return contained;
    }

    @java.lang.Override
    public void bringToFront(org.jhotdraw.draw.figure.Figure figure) {
        if (children.remove(figure)) {
            children.add(figure);
            needsSorting = true;
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    @java.lang.Override
    public void sendToBack(org.jhotdraw.draw.figure.Figure figure) {
        if (children.remove(figure)) {
            children.add(0, figure);
            needsSorting = true;
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    @java.lang.Override
    public boolean contains(org.jhotdraw.draw.figure.Figure f) {
        return children.contains(f);
    }

    /**
     * Ensures that the children are sorted in z-order sequence.
     */
    private void ensureSorted() {
        if (needsSorting) {
            java.util.Collections.sort(children, java.util.Comparator.comparing(org.jhotdraw.draw.figure.Figure::getLayer));
            needsSorting = false;
        }
    }

    public void setCanvasSize(org.jhotdraw.geom.Dimension2DDouble newValue) {
        org.jhotdraw.geom.Dimension2DDouble oldValue = canvasSize;
        canvasSize = newValue;
    }

    public org.jhotdraw.geom.Dimension2DDouble getCanvasSize() {
        return canvasSize;
    }

    /**
     * Handles all figure events fired by Figures contained in the Drawing.
     */
    protected class FigureHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements javax.swing.event.UndoableEditListener {
        /**
         * We propagate all edit events from our children to undoable edit listeners, which have
         * registered with us.
         */
        @java.lang.Override
        public void undoableEditHappened(javax.swing.event.UndoableEditEvent e) {
            fireUndoableEditHappened(e.getEdit());
        }

        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.FigureEvent e) {
            fireAreaInvalidated(e.getInvalidatedArea());
        }

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
            quadTree.remove(e.getFigure());
            quadTree.add(e.getFigure(), e.getFigure().getDrawingArea());
            needsSorting = true;
            if (!isChanging()) {
                fireAreaInvalidated(e.getInvalidatedArea());
            }
        }

        @java.lang.Override
        public void figureRequestRemove(org.jhotdraw.draw.event.FigureEvent e) {
            remove(e.getFigure());
        }
    }
}