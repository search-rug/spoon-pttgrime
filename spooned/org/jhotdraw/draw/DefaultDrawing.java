/* @(#)DefaultDrawing.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
/**
 * A default implementation of {@link Drawing} useful for drawings which contain only a few figures.
 *
 * <p>For larger drawings, {@link QuadTreeDrawing} is recommended.
 *
 * <p>FIXME - Maybe we should rename this class to SimpleDrawing or we should get rid of this class
 * altogether.
 */
public class DefaultDrawing extends org.jhotdraw.draw.AbstractDrawing {
    private static final long serialVersionUID = 1L;

    private boolean needsSorting = false;

    public DefaultDrawing() {
    }

    @java.lang.Override
    public void basicAdd(int index, org.jhotdraw.draw.figure.Figure figure) {
        super.basicAdd(index, figure);
        invalidateSortOrder();
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        synchronized(getLock()) {
            ensureSorted();
            java.util.List<org.jhotdraw.draw.figure.Figure> toDraw = new java.util.ArrayList<>(getChildren().size());
            java.awt.Rectangle clipRect = g.getClipBounds();
            double scale = org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g);
            for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
                if (f.getDrawingArea(scale).intersects(clipRect)) {
                    toDraw.add(f);
                }
            }
            draw(g, toDraw);
        }
    }

    public void draw(java.awt.Graphics2D g, java.util.Collection<org.jhotdraw.draw.figure.Figure> children) {
        java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
        double scale = org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g);
        if (clipBounds != null) {
            for (org.jhotdraw.draw.figure.Figure f : children) {
                if (f.isVisible() && f.getDrawingArea(scale).intersects(clipBounds)) {
                    f.draw(g);
                }
            }
        } else {
            for (org.jhotdraw.draw.figure.Figure f : children) {
                if (f.isVisible()) {
                    f.draw(g);
                }
            }
        }
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> sort(java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> c) {
        java.util.Set<org.jhotdraw.draw.figure.Figure> unsorted = new java.util.HashSet<>();
        unsorted.addAll(c);
        java.util.List<org.jhotdraw.draw.figure.Figure> sorted = new java.util.ArrayList<>(c.size());
        for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
            if (unsorted.contains(f)) {
                sorted.add(f);
                unsorted.remove(f);
            }
        }
        for (org.jhotdraw.draw.figure.Figure f : c) {
            if (unsorted.contains(f)) {
                sorted.add(f);
                unsorted.remove(f);
            }
        }
        return sorted;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.geom.Point2D.Double p) {
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (f.isVisible() && f.contains(p)) {
                return f;
            }
        }
        return null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigure(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (f.isVisible() && f.contains(p, scaleDenominator)) {
                return f;
            }
        }
        return null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure ignore) {
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (((f != ignore) && f.isVisible()) && f.contains(p)) {
                return f;
            }
        }
        return null;
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
    public org.jhotdraw.draw.figure.Figure findFigureBehind(java.awt.geom.Point2D.Double p, double scaleDenominator, org.jhotdraw.draw.figure.Figure figure) {
        boolean isBehind = false;
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (isBehind) {
                if (f.isVisible() && f.contains(p, scaleDenominator)) {
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
    public org.jhotdraw.draw.figure.Figure findFigureExcept(java.awt.geom.Point2D.Double p, java.util.Collection<? extends org.jhotdraw.draw.figure.Figure> ignore) {
        for (org.jhotdraw.draw.figure.Figure f : getFiguresFrontToBack()) {
            if (((!ignore.contains(f)) && f.isVisible()) && f.contains(p)) {
                return f;
            }
        }
        return null;
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> findFigures(java.awt.geom.Rectangle2D.Double bounds) {
        java.util.List<org.jhotdraw.draw.figure.Figure> intersection = new java.util.ArrayList<>();
        double scale = org.jhotdraw.draw.AttributeKeys.scaleFromContext(this);
        for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
            if (f.isVisible() && f.getBounds(scale).intersects(bounds)) {
                intersection.add(f);
            }
        }
        return intersection;
    }

    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> findFiguresWithin(java.awt.geom.Rectangle2D.Double bounds) {
        java.util.List<org.jhotdraw.draw.figure.Figure> contained = new java.util.ArrayList<>();
        double scale = org.jhotdraw.draw.AttributeKeys.scaleFromContext(this);
        for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
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
    public org.jhotdraw.draw.figure.Figure findFigureInside(java.awt.geom.Point2D.Double p) {
        org.jhotdraw.draw.figure.Figure f = findFigure(p);
        return f == null ? null : f.findFigureInside(p);
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the children.
     */
    @java.lang.Override
    public java.util.List<org.jhotdraw.draw.figure.Figure> getFiguresFrontToBack() {
        ensureSorted();
        return new org.jhotdraw.util.ReversedList<>(getChildren());
    }

    /**
     * Invalidates the sort order.
     */
    private void invalidateSortOrder() {
        needsSorting = true;
    }

    /**
     * Ensures that the children are sorted in z-order sequence from back to front.
     */
    private void ensureSorted() {
        if (needsSorting) {
            java.util.Collections.sort(CHILDREN, java.util.Comparator.comparing(org.jhotdraw.draw.figure.Figure::getLayer));
            needsSorting = false;
        }
    }

    @java.lang.Override
    public int indexOf(org.jhotdraw.draw.figure.Figure figure) {
        return CHILDREN.indexOf(figure);
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