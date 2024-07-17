/* @(#)SVGPathFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGPath is a composite Figure which contains one or more SVGBezierFigures as its children.
 */
public class SVGPathFigure extends org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure implements org.jhotdraw.samples.svg.figures.SVGFigure {
    private static final long serialVersionUID = 1L;

    /**
     * This cached path is used for drawing.
     */
    private transient java.awt.geom.Path2D.Double cachedPath;

    // private transient Rectangle2D.Double cachedDrawingArea;
    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;

    public SVGPathFigure() {
        add(new org.jhotdraw.samples.svg.figures.SVGBezierFigure());
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
    }

    public SVGPathFigure(boolean isEmpty) {
        if (!isEmpty) {
            add(new org.jhotdraw.samples.svg.figures.SVGBezierFigure());
        }
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        double opacity = attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY);
        opacity = java.lang.Math.min(java.lang.Math.max(0.0, opacity), 1.0);
        if (opacity != 0.0) {
            if (opacity != 1.0) {
                java.awt.geom.Rectangle2D.Double drawingArea = getDrawingArea();
                java.awt.geom.Rectangle2D clipBounds = g.getClipBounds();
                if (clipBounds != null) {
                    java.awt.geom.Rectangle2D.intersect(drawingArea, clipBounds, drawingArea);
                }
                if (!drawingArea.isEmpty()) {
                    java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(java.lang.Math.max(1, ((int) ((2 + drawingArea.width) * g.getTransform().getScaleX()))), java.lang.Math.max(1, ((int) ((2 + drawingArea.height) * g.getTransform().getScaleY()))), java.awt.image.BufferedImage.TYPE_INT_ARGB);
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

    @java.lang.Override
    public void drawFigure(java.awt.Graphics2D g) {
        java.awt.geom.AffineTransform savedTransform = null;
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            savedTransform = g.getTransform();
            g.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
        }
        java.awt.Paint paint = org.jhotdraw.samples.svg.SVGAttributeKeys.getFillPaint(this);
        if (paint != null) {
            g.setPaint(paint);
            drawFill(g);
        }
        paint = org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokePaint(this);
        if (paint != null) {
            g.setPaint(paint);
            g.setStroke(org.jhotdraw.samples.svg.SVGAttributeKeys.getStroke(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
            drawStroke(g);
        }
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            g.setTransform(savedTransform);
        }
    }

    @java.lang.Override
    protected void drawChildren(java.awt.Graphics2D g) {
        // empty
    }

    @java.lang.Override
    public void drawFill(java.awt.Graphics2D g) {
        g.fill(getPath());
    }

    @java.lang.Override
    public void drawStroke(java.awt.Graphics2D g) {
        g.draw(getPath());
    }

    @java.lang.Override
    protected void invalidate() {
        super.invalidate();
        cachedPath = null;
        cachedDrawingArea = null;
        cachedHitShape = null;
    }

    protected java.awt.geom.Path2D.Double getPath() {
        if (cachedPath == null) {
            cachedPath = new java.awt.geom.Path2D.Double();
            cachedPath.setWindingRule(attr().get(org.jhotdraw.draw.AttributeKeys.WINDING_RULE) == org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD ? java.awt.geom.Path2D.Double.WIND_EVEN_ODD : java.awt.geom.Path2D.Double.WIND_NON_ZERO);
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                org.jhotdraw.samples.svg.figures.SVGBezierFigure b = ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child));
                cachedPath.append(b.getBezierPath(), false);
            }
        }
        return cachedPath;
    }

    protected java.awt.Shape getHitShape() {
        if (cachedHitShape == null) {
            cachedHitShape = getPath();
            if ((attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) == null) && (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) == null)) {
                cachedHitShape = org.jhotdraw.samples.svg.SVGAttributeKeys.getHitStroke(this, 1.0).createStrokedShape(cachedHitShape);
            }
        }
        return cachedHitShape;
    }

    // int count;
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            double strokeTotalWidth = java.lang.Math.max(1.0, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0));
            double width = strokeTotalWidth / 2.0;
            if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                width *= attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
            } else if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP) != java.awt.BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            java.awt.Shape gp = getPath();
            java.awt.geom.Rectangle2D strokeRect = new java.awt.geom.Rectangle2D.Double(0, 0, width, width);
            java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
            if (tx != null) {
                // We have to use the (rectangular) bounds of the path here,
                // because we draw a rectangular handle over the shape of the figure
                gp = tx.createTransformedShape(gp.getBounds2D());
                strokeRect = tx.createTransformedShape(strokeRect).getBounds2D();
            }
            java.awt.geom.Rectangle2D rx = gp.getBounds2D();
            java.awt.geom.Rectangle2D.Double r = (rx instanceof java.awt.geom.Rectangle2D.Double) ? ((java.awt.geom.Rectangle2D.Double) (rx)) : new java.awt.geom.Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
            org.jhotdraw.geom.Geom.grow(r, strokeRect.getWidth(), strokeRect.getHeight());
            cachedDrawingArea = r;
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p) {
        getPath();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                p = ((java.awt.geom.Point2D.Double) (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, new java.awt.geom.Point2D.Double())));
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        boolean isClosed = getChild(0).attr().get(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED);
        if ((isClosed && (attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) == null)) && (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) == null)) {
            return getHitShape().contains(p);
        }
        /* return cachedPath.contains(p2); */
        double tolerance = java.lang.Math.max(2.0F, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0) / 2.0);
        if ((isClosed || (attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) != null)) || (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null)) {
            if (getPath().contains(p)) {
                return true;
            }
            /**
             * 2d
             */
            double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, 1.0);
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
            if (gs.createStrokedShape(getPath()).contains(p)) {
                return true;
            } else if (isClosed) {
                return false;
            }
        }
        if (!isClosed) {
            if (org.jhotdraw.geom.Shapes.outlineContains(getPath(), p, tolerance)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        if ((getChildCount() == 1) && (getChild(0).getNodeCount() <= 2)) {
            org.jhotdraw.samples.svg.figures.SVGBezierFigure b = getChild(0);
            b.setBounds(anchor, lead);
            invalidate();
        } else {
            super.setBounds(anchor, lead);
        }
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || ((tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION) != tx.getType())) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, tx);
            } else {
                java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, t);
            }
        } else {
            for (org.jhotdraw.draw.figure.Figure f : getChildren()) {
                f.transform(tx);
            }
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT, g);
            }
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT, g);
            }
        }
        invalidate();
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        invalidate();
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        java.util.ArrayList<java.lang.Object> paths = ((java.util.ArrayList<java.lang.Object>) (restoreData[0]));
        for (int i = 0, n = getChildCount(); i < n; i++) {
            getChild(i).restoreTransformTo(paths.get(i));
        }
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[3])));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        java.util.ArrayList<java.lang.Object> paths = new java.util.ArrayList<java.lang.Object>(getChildCount());
        for (int i = 0, n = getChildCount(); i < n; i++) {
            paths.add(getChild(i).getTransformRestoreData());
        }
        return new java.lang.Object[]{ paths, org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    @java.lang.Override
    public boolean isEmpty() {
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            org.jhotdraw.samples.svg.figures.SVGBezierFigure b = ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child));
            if (b.getNodeCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.samples.svg.figures.SVGPathOutlineHandle(this, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.samples.svg.figures.SVGPathOutlineHandle(this));
                for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                    handles.addAll(((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child)).createHandles(this, detailLevel));
                }
                handles.add(new org.jhotdraw.samples.svg.figures.LinkHandle(this));
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                break;
            default :
                break;
        }
        return handles;
    }

    @java.lang.Override
    public java.util.Collection<javax.swing.Action> getActions(java.awt.geom.Point2D.Double p) {
        final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        java.util.LinkedList<javax.swing.Action> actions = new java.util.LinkedList<javax.swing.Action>();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.TRANSFORM.setUndoable(SVGPathFigure.this, null));
                    changed();
                }
            });
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.flattenTransform.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    // CompositeEdit edit = new CompositeEdit(labels.getString("flattenTransform"));
                    // TransformEdit edit = new TransformEdit(SVGPathFigure.this, )
                    final java.lang.Object restoreData = getTransformRestoreData();
                    javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                        private static final long serialVersionUID = 1L;

                        @java.lang.Override
                        public java.lang.String getPresentationName() {
                            return labels.getString("edit.flattenTransform.text");
                        }

                        @java.lang.Override
                        public void undo() throws javax.swing.undo.CannotUndoException {
                            super.undo();
                            willChange();
                            restoreTransformTo(restoreData);
                            changed();
                        }

                        @java.lang.Override
                        public void redo() throws javax.swing.undo.CannotRedoException {
                            super.redo();
                            willChange();
                            restoreTransformTo(restoreData);
                            flattenTransform();
                            changed();
                        }
                    };
                    willChange();
                    flattenTransform();
                    changed();
                    fireUndoableEditHappened(edit);
                }
            });
        }
        if (getChild(getChildCount() - 1).attr().get(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED)) {
            actions.add(new javax.swing.AbstractAction(labels.getString("attribute.openPath.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                        getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED.setUndoable(child, false));
                    }
                    changed();
                }
            });
        } else {
            actions.add(new javax.swing.AbstractAction(labels.getString("attribute.closePath.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                        getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED.setUndoable(child, true));
                    }
                    changed();
                }
            });
        }
        if (attr().get(org.jhotdraw.draw.AttributeKeys.WINDING_RULE) != org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD) {
            actions.add(new javax.swing.AbstractAction(labels.getString("attribute.windingRule.evenOdd.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.WINDING_RULE.setUndoable(SVGPathFigure.this, org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD));
                    changed();
                }
            });
        } else {
            actions.add(new javax.swing.AbstractAction(labels.getString("attribute.windingRule.nonZero.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    attr().set(org.jhotdraw.draw.AttributeKeys.WINDING_RULE, org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO);
                    changed();
                    getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.WINDING_RULE.setUndoable(SVGPathFigure.this, org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO));
                }
            });
        }
        return actions;
    }

    // CONNECTING
    // EDITING
    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        if ((evt.getClickCount() == 2) && ((view.getHandleDetailLevel() % 2) == 0)) {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                org.jhotdraw.samples.svg.figures.SVGBezierFigure bf = ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child));
                int index = bf.findSegment(p, 5.0F / view.getScaleFactor());
                if (index != (-1)) {
                    bf.handleMouseClick(p, evt, view);
                    evt.consume();
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public void add(final int index, final org.jhotdraw.draw.figure.Figure figure) {
        super.add(index, ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (figure)));
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGBezierFigure getChild(int index) {
        return ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (super.getChild(index)));
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGPathFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGPathFigure that = ((org.jhotdraw.samples.svg.figures.SVGPathFigure) (super.clone()));
        return that;
    }

    public void flattenTransform() {
        willChange();
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                // ((SVGBezierFigure) child).transform(tx);
                ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child)).flattenTransform();
            }
        }
        if (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null) {
            attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT).transform(tx);
        }
        if (attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) != null) {
            attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT).transform(tx);
        }
        attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, null);
        changed();
    }
}