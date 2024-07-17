/* @(#)VerticalLayouter.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.layouter;
import org.jhotdraw.draw.figure.CompositeFigure;
/**
 * A {@link Layouter} which lays out all children of a {@link CompositeFigure} in vertical
 * direction.
 *
 * <p>The preferred size of the figures is used to determine the layout. This may cause some figures
 * to resize.
 *
 * <p>The VerticalLayouter honors the LAYOUT_INSETS and the COMPOSITE_ALIGNMENT AttributeKey when
 * laying out a CompositeFigure.
 *
 * <p>If COMPOSITE_ALIGNMENT is not set on the composite figure, the layout assigns the same width
 * to all figures.
 */
public class VerticalLayouter extends org.jhotdraw.draw.layouter.AbstractLayouter {
    /**
     * This alignment is used, when
     */
    private org.jhotdraw.draw.AttributeKeys.Alignment defaultAlignment = org.jhotdraw.draw.AttributeKeys.Alignment.BLOCK;

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double calculateLayout(org.jhotdraw.draw.figure.CompositeFigure layoutable, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        org.jhotdraw.geom.Insets2D.Double layoutInsets = layoutable.attr().get(org.jhotdraw.draw.AttributeKeys.LAYOUT_INSETS);
        if (layoutInsets == null) {
            layoutInsets = new org.jhotdraw.geom.Insets2D.Double(0, 0, 0, 0);
        }
        java.awt.geom.Rectangle2D.Double layoutBounds = new java.awt.geom.Rectangle2D.Double(anchor.x, anchor.y, 0, 0);
        for (org.jhotdraw.draw.figure.Figure child : layoutable.getChildren()) {
            if (child.isVisible()) {
                org.jhotdraw.geom.Dimension2DDouble preferredSize = child.getPreferredSize(scale);
                org.jhotdraw.geom.Insets2D.Double ins = getInsets(child);
                layoutBounds.width = java.lang.Math.max(layoutBounds.width, (preferredSize.width + ins.left) + ins.right);
                layoutBounds.height += (preferredSize.height + ins.top) + ins.bottom;
            }
        }
        layoutBounds.width += layoutInsets.left + layoutInsets.right;
        layoutBounds.height += layoutInsets.top + layoutInsets.bottom;
        return layoutBounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double layout(org.jhotdraw.draw.figure.CompositeFigure layoutable, java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead, double scale) {
        org.jhotdraw.geom.Insets2D.Double layoutInsets = layoutable.attr().get(org.jhotdraw.draw.AttributeKeys.LAYOUT_INSETS);
        org.jhotdraw.draw.AttributeKeys.Alignment compositeAlignment = layoutable.attr().get(org.jhotdraw.draw.AttributeKeys.COMPOSITE_ALIGNMENT);
        if (layoutInsets == null) {
            layoutInsets = new org.jhotdraw.geom.Insets2D.Double();
        }
        java.awt.geom.Rectangle2D.Double layoutBounds = calculateLayout(layoutable, anchor, lead, scale);
        double y = layoutBounds.y + layoutInsets.top;
        for (org.jhotdraw.draw.figure.Figure child : layoutable.getChildren()) {
            if (child.isVisible()) {
                org.jhotdraw.geom.Insets2D.Double insets = getInsets(child);
                double height = child.getPreferredSize(scale).height;
                double width = child.getPreferredSize(scale).width;
                switch (compositeAlignment) {
                    case LEADING :
                        child.setBounds(new java.awt.geom.Point2D.Double((layoutBounds.x + layoutInsets.left) + insets.left, y + insets.top), new java.awt.geom.Point2D.Double(((layoutBounds.x + (+layoutInsets.left)) + insets.left) + width, (y + insets.top) + height));
                        break;
                    case TRAILING :
                        child.setBounds(new java.awt.geom.Point2D.Double((((layoutBounds.x + layoutBounds.width) - layoutInsets.right) - insets.right) - width, y + insets.top), new java.awt.geom.Point2D.Double(((layoutBounds.x + layoutBounds.width) - layoutInsets.right) - insets.right, (y + insets.top) + height));
                        break;
                    case CENTER :
                        child.setBounds(new java.awt.geom.Point2D.Double(layoutBounds.x + ((layoutBounds.width - width) / 2.0), y + insets.top), new java.awt.geom.Point2D.Double(layoutBounds.x + ((layoutBounds.width + width) / 2.0), (y + insets.top) + height));
                        break;
                    case BLOCK :
                    default :
                        child.setBounds(new java.awt.geom.Point2D.Double((layoutBounds.x + layoutInsets.left) + insets.left, y + insets.top), new java.awt.geom.Point2D.Double(((layoutBounds.x + layoutBounds.width) - layoutInsets.right) - insets.right, (y + insets.top) + height));
                        break;
                }
                y += (height + insets.top) + insets.bottom;
            }
        }
        return layoutBounds;
    }
}