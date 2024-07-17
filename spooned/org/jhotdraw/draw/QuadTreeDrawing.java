/* @(#)QuadTreeDrawing.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
/**
 * An implementation of {@link Drawing} which uses a {@link org.jhotdraw.geom.QuadTree} to provide a
 * good responsiveness for drawings which contain many figures.
 */
public class QuadTreeDrawing extends org.jhotdraw.draw.AbstractDrawing {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.geom.QuadTree<org.jhotdraw.draw.figure.Figure> quadTree = new org.jhotdraw.geom.QuadTree<>();

    private boolean needsSorting = false;

    @java.lang.Override
    public int indexOf(org.jhotdraw.draw.figure.Figure figure) {
        return CHILDREN.indexOf(figure);
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        super.basicAdd(index, figure);
        quadTree.add(figure, figure.getDrawingArea());
        needsSorting = true;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure basicRemoveChild(int index) {
        org.jhotdraw.draw.figure.Figure figure = getChild(index);
        quadTree.remove(figure);
        needsSorting = true;
        super.basicRemoveChild(index);
        return figure;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
        if (clipBounds != null) {
            draw(g, sort(quadTree.findIntersects(clipBounds)));
        } else {
            draw(g, CHILDREN);
        }
    }

    /**
     * Implementation note: Sorting can not be done for orphaned children.
     */
    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> sort(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> c) {
        java.util.List<org.jhotdraw.draw.figure.Figure> sorted = new java.util.ArrayList<>(c);
        java.util.Collections.sort(sorted, java.util.Comparator.comparing(org.jhotdraw.draw.figure.Figure::getLayer));
        return sorted;
    }

    public void draw(java.awt.Graphics2D g, java.util.Collection<org.jhotdraw.draw.figure.Figure> c) {
        for (org.jhotdraw.draw.figure.Figure f : c) {
            if (f.isVisible()) {
                f.draw(g);
            }
        }
    }

    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildren(java.awt.geom.Rectangle2D.Double bounds) {
        return new java.util.ArrayList<>(quadTree.findInside(bounds));
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getChildren() {
        return UNMODIFIABLE_CHILDREN;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureInside(java.awt.geom.Point2D.Double p) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack(c)) {
            if (c.contains(f) && f.contains(p)) {
                return f.findFigureInside(p);
            }
        }
        return null;
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the children.
     */
    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getFiguresFrontToBack() {
        ensureSorted();
        return new org.jhotdraw.util.ReversedList<>(CHILDREN);
    }

    protected java.util.List<org.jhotdraw.draw.figure.Figure> getFiguresFrontToBack(java.util.Collection<org.jhotdraw.draw.figure.Figure> smallCollection) {
        java.util.List<org.jhotdraw.draw.figure.Figure> list = new java.util.ArrayList<>(smallCollection);
        java.util.Collections.sort(list, java.util.Comparator.comparing(org.jhotdraw.draw.figure.Figure::getLayer).reversed());
        return list;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.geom.Point2D.Double p) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return f.contains(p) ? f : null;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack(c)) {
                    if (f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure ignore) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return (f == ignore) || (!f.contains(p)) ? null : f;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack(c)) {
                    if ((f2 != ignore) && f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> ignore) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0 :
                return null;
            case 1 :
                org.jhotdraw.draw.figure.Figure f = c.iterator().next();
                return (!ignore.contains(f)) || (!f.contains(p)) ? null : f;
            default :
                for (org.jhotdraw.draw.figure.Figure f2 : getFiguresFrontToBack(c)) {
                    if ((!ignore.contains(f2)) && f2.contains(p)) {
                        return f2;
                    }
                }
                return null;
        }
    }

    @java.lang.Override
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

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureBehind(java.awt.geom.Point2D.Double p, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> children) {
        int inFrontOf = children.size();
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (inFrontOf == 0) {
                if (f.isVisible() && f.contains(p)) {
                    return f;
                }
            } else if (children.contains(f)) {
                inFrontOf--;
            }
        }
        return null;
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> findFigures(java.awt.geom.Rectangle2D.Double r) {
        java.util.List<org.jhotdraw.draw.figure.Figure> c = new java.util.ArrayList<>(quadTree.findIntersects(r));
        switch (c.size()) {
            case 0 :
                // fall through
            case 1 :
                return c;
            default :
                return getFiguresFrontToBack(c);
        }
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> findFiguresWithin(java.awt.geom.Rectangle2D.Double bounds) {
        java.util.List<org.jhotdraw.draw.figure.Figure> contained = new java.util.ArrayList<>();
        double scale = org.jhotdraw.draw.AttributeKeys.scaleFromContext(this);
        for (org.jhotdraw.draw.figure.Figure f : CHILDREN) {
            java.awt.geom.Rectangle2D.Double r = f.getBounds(scale);
            if (f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                java.awt.geom.Rectangle2D rt = f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(r).getBounds2D();
                r = (rt instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rt)) : new java.awt.geom.Rectangle2D.Double(rt.getX(), rt.getY(), rt.getWidth(), rt.getHeight());
            }
            if (f.isVisible() && org.jhotdraw.geom.Geom.contains(bounds, r)) {
                contained.add(f);
            }
        }
        return contained;
    }

    @java.lang.Override
    public void bringToFront(org.jhotdraw.draw.figure.Figure figure) {
        if (CHILDREN.remove(figure)) {
            CHILDREN.add(figure);
            needsSorting = true;
            fireDrawingChanged(figure.getDrawingArea());
        }
    }

    @java.lang.Override
    public void sendToBack(org.jhotdraw.draw.figure.Figure figure) {
        if (CHILDREN.remove(figure)) {
            CHILDREN.add(0, figure);
            needsSorting = true;
            fireDrawingChanged(figure.getDrawingArea());
        }
    }

    /**
     * Ensures that the children are sorted in z-order sequence.
     */
    private void ensureSorted() {
        if (needsSorting) {
            java.util.Collections.sort(CHILDREN, java.util.Comparator.comparing(org.jhotdraw.draw.figure.Figure::getLayer));
            needsSorting = false;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.QuadTreeDrawing clone() {
        org.jhotdraw.draw.QuadTreeDrawing that = ((org.jhotdraw.draw.QuadTreeDrawing) (super.clone()));
        that.quadTree = new org.jhotdraw.geom.QuadTree<>();
        for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
            quadTree.add(f, f.getDrawingArea());
        }
        return that;
    }

    @java.lang.Override
    protected org.jhotdraw.draw.AbstractDrawing.EventHandler createEventHandler() {
        return new org.jhotdraw.draw.QuadTreeDrawing.QuadTreeEventHandler();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        double tolerance = (10 / 2) / scaleDenominator;
        java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double(p.x - tolerance, p.y - tolerance, 2 * tolerance, 2 * tolerance);
        for (org.jhotdraw.draw.figure.Figure figure : findFigures(rect)) {
            if (figure.isVisible() && figure.contains(p, scaleDenominator)) {
                return figure;
            }
        }
        return null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureBehind(java.awt.geom.Point2D.Double p, double scaleDenominator, org.jhotdraw.draw.figure.Figure behindFigure) {
        double tolerance = (10 / 2) / scaleDenominator;
        java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double(p.x - tolerance, p.y - tolerance, 2 * tolerance, 2 * tolerance);
        boolean check = false;
        for (org.jhotdraw.draw.figure.Figure figure : findFigures(rect)) {
            if ((check && figure.isVisible()) && figure.contains(p, scaleDenominator)) {
                return figure;
            } else if (figure == behindFigure) {
                check = true;
            }
        }
        return null;
    }

    /**
     * Handles all figure events fired by Figures contained in the Drawing.
     */
    protected class QuadTreeEventHandler extends org.jhotdraw.draw.AbstractDrawing.EventHandler {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
            if (!isChanging()) {
                quadTree.remove(e.getFigure());
                quadTree.add(e.getFigure(), e.getFigure().getDrawingArea());
                needsSorting = true;
                invalidate();
                fireDrawingChanged(e.getInvalidatedArea());
            }
        }
    }

    @java.lang.Override
    public void drawCanvas(java.awt.Graphics2D g) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH) != null) && (attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT) != null)) {
            // Determine canvas color and opacity
            java.awt.Color canvasColor = attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR);
            java.lang.Double fillOpacity = attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY);
            if ((canvasColor != null) && (fillOpacity > 0)) {
                canvasColor = new java.awt.Color((canvasColor.getRGB() & 0xffffff) | (((int) (fillOpacity * 255)) << 24), true);
                // Fill the canvas
                java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH), attr().get(org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT));
                g.setColor(canvasColor);
                g.fill(r);
            }
        }
    }
}