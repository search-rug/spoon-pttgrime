/* @(#)ODGGroupFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * ODGGroupFigure.
 */
public class ODGGroupFigure extends org.jhotdraw.draw.figure.GroupFigure implements org.jhotdraw.samples.odg.figures.ODGFigure {
    private static final long serialVersionUID = 1L;

    private java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();

    public ODGGroupFigure() {
        org.jhotdraw.samples.odg.ODGAttributeKeys.setDefaults(this);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        double opacity = attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.OPACITY);
        opacity = java.lang.Math.min(java.lang.Math.max(0.0, opacity), 1.0);
        if (opacity != 0.0) {
            if (opacity != 1.0) {
                java.awt.geom.Rectangle2D.Double drawingArea = getDrawingArea();
                java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
                if (clipBounds != null) {
                    java.awt.geom.Rectangle2D.intersect(drawingArea, clipBounds, drawingArea);
                }
                if (!drawingArea.isEmpty()) {
                    java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(((int) ((2 + drawingArea.width) * g.getTransform().getScaleX())), ((int) ((2 + drawingArea.height) * g.getTransform().getScaleY())), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D gr = buf.createGraphics();
                    gr.scale(g.getTransform().getScaleX(), g.getTransform().getScaleY());
                    gr.translate(((int) (-drawingArea.x)), ((int) (-drawingArea.y)));
                    gr.setRenderingHints(g.getRenderingHints());
                    super.draw(gr);
                    gr.dispose();
                    java.awt.Composite savedComposite = g.getComposite();
                    g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, ((float) (opacity))));
                    g.drawImage(buf, ((int) (drawingArea.x)), ((int) (drawingArea.y)), 2 + ((int) (drawingArea.width)), 2 + ((int) (drawingArea.height)), null);
                    g.setComposite(savedComposite);
                }
            } else {
                super.draw(g);
            }
        }
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        if (cachedBounds == null) {
            if (getChildCount() == 0) {
                cachedBounds = new java.awt.geom.Rectangle2D.Double();
            } else {
                for (org.jhotdraw.draw.figure.Figure f : children) {
                    java.awt.geom.Rectangle2D.Double bounds = f.getBounds(scale);
                    if (f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                        bounds.setRect(f.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds).getBounds2D());
                    }
                    if ((cachedBounds == null) || cachedBounds.isEmpty()) {
                        cachedBounds = bounds;
                    } else {
                        cachedBounds.add(bounds);
                    }
                }
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedBounds.clone()));
    }

    @java.lang.Override
    public java.util.LinkedList<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        if (detailLevel == 0) {
            org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
        }
        return handles;
    }

    @java.lang.Override
    public boolean isEmpty() {
        return getChildCount() == 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        buf.append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
        buf.append('@');
        buf.append(hashCode());
        if (getChildCount() > 0) {
            buf.append('(');
            for (java.util.Iterator<org.jhotdraw.draw.figure.Figure> i = getChildren().iterator(); i.hasNext();) {
                org.jhotdraw.draw.figure.Figure child = i.next();
                buf.append(child);
                if (i.hasNext()) {
                    buf.append(',');
                }
            }
            buf.append(')');
        }
        return buf.toString();
    }

    public org.jhotdraw.samples.odg.figures.ODGGroupFigure clone() {
        org.jhotdraw.samples.odg.figures.ODGGroupFigure that = ((org.jhotdraw.samples.odg.figures.ODGGroupFigure) (super.clone()));
        that.attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>(this.attributes);
        return that;
    }
}