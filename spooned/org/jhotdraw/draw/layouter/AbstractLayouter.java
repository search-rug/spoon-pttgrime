/* @(#)AbstractLayouter.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.layouter;
/**
 * This abstract class can be extended to implement a {@link Layouter} which has its own attribute
 * set.
 */
public abstract class AbstractLayouter implements org.jhotdraw.draw.layouter.Layouter {
    public org.jhotdraw.geom.Insets2D.Double getInsets(org.jhotdraw.draw.figure.Figure child) {
        org.jhotdraw.geom.Insets2D.Double value = child.attr().get(org.jhotdraw.draw.figure.CompositeFigure.LAYOUT_INSETS);
        return value == null ? new org.jhotdraw.geom.Insets2D.Double() : ((org.jhotdraw.geom.Insets2D.Double) (value.clone()));
    }
}