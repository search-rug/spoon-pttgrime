/* @(#)HorizontalLayouter.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.layouter;
import org.jhotdraw.draw.figure.CompositeFigure;
/**
 * A {@link Layouter} which lays out all children of a {@link CompositeFigure} in horizontal
 * direction.
 *
 * <p>The preferred size of the figures is used to determine the layout. This may cause some figures
 * to resize.
 *
 * <p>The HorizontalLayouter honors the LAYOUT_INSETS and the COMPOSITE_ALIGNMENT AttributeKey when
 * laying out a CompositeFigure.
 *
 * <p>If COMPOSITE_ALIGNMENT is not set on the composite figure, the layout assigns the same height
 * to all figures.
 */
public class HorizontalLayouter extends org.jhotdraw.draw.layouter.AbstractLayouter {
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double calculateLayout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        org.jhotdraw.geom.Insets2D.Double layoutInsets = compositeFigure.attr().get(org.jhotdraw.draw.AttributeKeys.LAYOUT_INSETS);
        java.awt.geom.Rectangle2D.Double layoutBounds = new java.awt.geom.Rectangle2D.Double(anchor.x, anchor.y, 0, 0);
        for (org.jhotdraw.draw.figure.Figure child : compositeFigure.getChildren()) {
            if (child.isVisible()) {
                org.jhotdraw.geom.Dimension2DDouble preferredSize = child.getPreferredSize(scale);
                org.jhotdraw.geom.Insets2D.Double ins = getInsets(child);
                layoutBounds.height = java.lang.Math.max(layoutBounds.height, (preferredSize.height + ins.top) + ins.bottom);
                layoutBounds.width += (preferredSize.width + ins.left) + ins.right;
            }
        }
        layoutBounds.width += layoutInsets.left + layoutInsets.right;
        layoutBounds.height += layoutInsets.top + layoutInsets.bottom;
        return layoutBounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double layout(org.jhotdraw.draw.figure.CompositeFigure compositeFigure, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        org.jhotdraw.geom.Insets2D.Double layoutInsets = compositeFigure.attr().get(org.jhotdraw.draw.AttributeKeys.LAYOUT_INSETS);
        org.jhotdraw.draw.AttributeKeys.Alignment compositeAlignment = compositeFigure.attr().get(org.jhotdraw.draw.AttributeKeys.COMPOSITE_ALIGNMENT);
        java.awt.geom.Rectangle2D.Double layoutBounds = calculateLayout(compositeFigure, anchor, lead, scale);
        double x = layoutBounds.x + layoutInsets.left;
        for (org.jhotdraw.draw.figure.Figure child : compositeFigure.getChildren()) {
            if (child.isVisible()) {
                org.jhotdraw.geom.Insets2D.Double insets = getInsets(child);
                double width = child.getPreferredSize(scale).width;
                double height = child.getPreferredSize(scale).height;
                // --
                switch (compositeAlignment) {
                    case LEADING :
                        child.setBounds(new java.awt.geom.Point2D.Double(x + insets.left, (layoutBounds.y + layoutInsets.top) + insets.top), new java.awt.geom.Point2D.Double((x + insets.left) + width, ((layoutBounds.y + layoutInsets.top) + insets.top) + height));
                        break;
                    case TRAILING :
                        child.setBounds(new java.awt.geom.Point2D.Double(x + insets.left, (((layoutBounds.y + layoutBounds.height) - layoutInsets.bottom) - insets.bottom) - height), new java.awt.geom.Point2D.Double((x + insets.left) + width, ((layoutBounds.y + layoutBounds.height) - layoutInsets.bottom) - insets.bottom));
                        break;
                    case CENTER :
                        child.setBounds(new java.awt.geom.Point2D.Double(x + insets.left, (layoutBounds.y + layoutInsets.top) + ((layoutBounds.height - height) / 2.0)), new java.awt.geom.Point2D.Double((x + insets.left) + width, (layoutBounds.y + layoutInsets.top) + ((layoutBounds.height + height) / 2.0)));
                        break;
                    case BLOCK :
                    default :
                        child.setBounds(new java.awt.geom.Point2D.Double(x + insets.left, (layoutBounds.y + layoutInsets.top) + insets.top), new java.awt.geom.Point2D.Double((x + insets.left) + width, ((layoutBounds.y + layoutBounds.height) - layoutInsets.bottom) - insets.bottom));
                        break;
                }
                // ---
                x += (width + insets.left) + insets.right;
            }
        }
        return layoutBounds;
    }
}