/* @(#)LocatorLayouter.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.layouter;
import org.jhotdraw.draw.locator.Locator.Position;
/**
 * A layouter which lays out all children of a CompositeFigure according to their LayoutLocator
 * property..
 */
public class LocatorLayouter implements org.jhotdraw.draw.layouter.Layouter {
    /**
     * LayoutLocator property used by the children to specify their location relative to the
     * compositeFigure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.locator.Locator> LAYOUT_LOCATOR = new org.jhotdraw.draw.AttributeKey<>("layoutLocator", org.jhotdraw.draw.locator.Locator.class, null);

    public LocatorLayouter() {
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double calculateLayout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        java.awt.geom.Rectangle2D.Double bounds = null;
        for (org.jhotdraw.draw.figure.Figure child : extractFiguresToLayout(compositeFigure)) {
            org.jhotdraw.draw.locator.Locator locator = getLocator(child);
            java.awt.geom.Rectangle2D.Double r;
            if (locator == null) {
                r = child.getBounds();
            } else {
                org.jhotdraw.draw.locator.Locator.Position p = locator.locate(extractBaseFigure(compositeFigure), scale);
                org.jhotdraw.geom.Dimension2DDouble d = child.getPreferredSize(scale);
                r = new java.awt.geom.Rectangle2D.Double(p.location().x, p.location().y, d.width, d.height);
            }
            if (!r.isEmpty()) {
                if (bounds == null) {
                    bounds = r;
                } else {
                    bounds.add(r);
                }
            }
        }
        return bounds == null ? new java.awt.geom.Rectangle2D.Double() : bounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double layout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        java.awt.geom.Rectangle2D.Double bounds = null;
        for (org.jhotdraw.draw.figure.Figure child : extractFiguresToLayout(compositeFigure)) {
            org.jhotdraw.draw.locator.Locator locator = getLocator(child);
            java.awt.geom.Rectangle2D.Double r;
            org.jhotdraw.draw.locator.Locator.Position position = null;
            if (locator == null) {
                r = child.getBounds();
            } else {
                position = locator.locate(extractBaseFigure(compositeFigure), child, scale);
                if (java.lang.Double.isNaN(position.location().x)) {
                    continue;
                }
                org.jhotdraw.geom.Dimension2DDouble d = child.getPreferredSize(scale);
                r = new java.awt.geom.Rectangle2D.Double(position.location().x, position.location().y, d.width, d.height);
            }
            child.willChange();
            if ((position != null) && (child instanceof org.jhotdraw.draw.figure.Origin originChild)) {
                originChild.setOrigin(position.location());
            } else {
                child.setBounds(new java.awt.geom.Point2D.Double(r.getMinX(), r.getMinY()), new java.awt.geom.Point2D.Double(r.getMaxX(), r.getMaxY()));
            }
            if (position != null) {
                if (child instanceof org.jhotdraw.draw.figure.Rotation rotateChild) {
                    rotateChild.setRotation(position.angle());
                } else {
                    ((org.jhotdraw.draw.figure.Figure) (child)).transform(java.awt.geom.AffineTransform.getRotateInstance(-position.angle(), position.location().x, position.location().y));
                }
            }
            child.changed();
            if (!r.isEmpty()) {
                if (bounds == null) {
                    bounds = r;
                } else {
                    bounds.add(r);
                }
            }
        }
        return bounds == null ? new java.awt.geom.Rectangle2D.Double() : bounds;
    }

    private org.jhotdraw.draw.locator.Locator getLocator(org.jhotdraw.draw.figure.Figure f) {
        return f.attr().get(org.jhotdraw.draw.layouter.LocatorLayouter.LAYOUT_LOCATOR);
    }

    /**
     * Filters the Elements used as a base to layout subcomponents. Default element is the composite
     * figure itself. However one could use the first element of a composite figure to use as the main
     * element and layout the remaining children.
     *
     * @param compositeFigure
     * @return  */
    public org.jhotdraw.draw.figure.Figure extractBaseFigure(org.jhotdraw.draw.figure.CompositeFigure compositeFigure) {
        return compositeFigure;
    }

    /**
     * Using extractBaseFigure one could mark one child as the main element and layout the remaining
     * elements.
     */
    public java.util.List<org.jhotdraw.draw.figure.Figure> extractFiguresToLayout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure) {
        return compositeFigure.getChildren();
    }

    /**
     * Layout main element is first child. Other childs are layouted.
     */
    public static class LocatorLayouterFirstFigure extends org.jhotdraw.draw.layouter.LocatorLayouter {
        @java.lang.Override
        public java.util.List<org.jhotdraw.draw.figure.Figure> extractFiguresToLayout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure) {
            java.util.List<org.jhotdraw.draw.figure.Figure> figures = compositeFigure.getChildren();
            return figures.subList(1, figures.size());
        }

        @java.lang.Override
        public org.jhotdraw.draw.figure.Figure extractBaseFigure(org.jhotdraw.draw.figure.CompositeFigure compositeFigure) {
            return compositeFigure.getChildren().get(0);
        }
    }
}