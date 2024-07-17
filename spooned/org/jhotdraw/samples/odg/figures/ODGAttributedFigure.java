/* @(#)ODGAttributedFigure.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * ODGAttributedFigure.
 */
public abstract class ODGAttributedFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure implements org.jhotdraw.samples.odg.figures.ODGFigure {
    private static final long serialVersionUID = 1L;

    public ODGAttributedFigure() {
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
                    drawFigure(gr);
                    gr.dispose();
                    java.awt.Composite savedComposite = g.getComposite();
                    g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, ((float) (opacity))));
                    g.drawImage(buf, ((int) (drawingArea.x)), ((int) (drawingArea.y)), 2 + ((int) (drawingArea.width)), 2 + ((int) (drawingArea.height)), null);
                    g.setComposite(savedComposite);
                }
            } else {
                drawFigure(g);
            }
        }
    }

    /**
     * This method is invoked before the rendered image of the figure is composited.
     */
    public void drawFigure(java.awt.Graphics2D g) {
        java.awt.geom.AffineTransform savedTransform = null;
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            savedTransform = g.getTransform();
            g.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
        }
        if (attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_STYLE) != org.jhotdraw.samples.odg.ODGConstants.FillStyle.NONE) {
            java.awt.Paint paint = org.jhotdraw.samples.odg.ODGAttributeKeys.getFillPaint(this);
            if (paint != null) {
                g.setPaint(paint);
                drawFill(g);
            }
        }
        if (attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_STYLE) != org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.NONE) {
            java.awt.Paint paint = org.jhotdraw.samples.odg.ODGAttributeKeys.getStrokePaint(this);
            if (paint != null) {
                g.setPaint(paint);
                g.setStroke(org.jhotdraw.samples.odg.ODGAttributeKeys.getStroke(this));
                drawStroke(g);
            }
        }
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            g.setTransform(savedTransform);
        }
    }

    @java.lang.Override
    public java.util.Collection<javax.swing.Action> getActions(java.awt.geom.Point2D.Double p) {
        java.util.LinkedList<javax.swing.Action> actions = new java.util.LinkedList<javax.swing.Action>();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.TRANSFORM.setUndoable(ODGAttributedFigure.this, null));
                    changed();
                }
            });
        }
        return actions;
    }
}