/* @(#)AbstractAttributedDecoratedFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * This abstract class can be extended to implement a {@link DecoratedFigure} which has an attribute
 * set.
 */
public abstract class AbstractAttributedDecoratedFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure implements org.jhotdraw.draw.figure.DecoratedFigure {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.figure.Figure decorator;

    @java.lang.Override
    public final void draw(java.awt.Graphics2D g) {
        if (decorator != null) {
            drawDecorator(g);
        }
        drawFigure(g);
    }

    protected void drawFigure(java.awt.Graphics2D g) {
        super.draw(g);
    }

    protected void drawDecorator(java.awt.Graphics2D g) {
        updateDecoratorBounds();
        decorator.draw(g);
    }

    @java.lang.Override
    public final java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double r = getFigureDrawingArea(scale);
        if (decorator != null) {
            updateDecoratorBounds();
            r.add(decorator.getDrawingArea(scale));
        }
        return r;
    }

    protected java.awt.geom.Rectangle2D.Double getFigureDrawingArea(double scale) {
        return super.getDrawingArea(scale);
    }

    @java.lang.Override
    public void setDecorator(org.jhotdraw.draw.figure.Figure newValue) {
        willChange();
        decorator = newValue;
        if (decorator != null) {
            decorator.setBounds(getStartPoint(), getEndPoint());
        }
        changed();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getDecorator() {
        return decorator;
    }

    protected void updateDecoratorBounds() {
        if (decorator != null) {
            java.awt.geom.Point2D.Double sp = getStartPoint();
            java.awt.geom.Point2D.Double ep = getEndPoint();
            org.jhotdraw.geom.Insets2D.Double decoratorInsets = attr().get(org.jhotdraw.draw.AttributeKeys.DECORATOR_INSETS);
            sp.x -= decoratorInsets.left;
            sp.y -= decoratorInsets.top;
            ep.x += decoratorInsets.right;
            ep.y += decoratorInsets.bottom;
            decorator.setBounds(sp, ep);
        }
    }

    @java.lang.Override
    public final boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        if (decorator != null) {
            updateDecoratorBounds();
            if (decorator.contains(p, scaleDenominator)) {
                return true;
            }
        }
        return figureContains(p, scaleDenominator);
    }

    protected abstract boolean figureContains(java.awt.geom.Point2D.Double p, double scaleDenominator);

    @java.lang.Override
    public org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure clone() {
        org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure that = ((org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure) (super.clone()));
        if (this.decorator != null) {
            that.decorator = this.decorator.clone();
        }
        return that;
    }
}