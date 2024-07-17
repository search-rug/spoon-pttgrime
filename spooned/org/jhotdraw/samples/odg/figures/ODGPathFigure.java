/* @(#)ODGPathFigure.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;
/**
 * ODGPath is a composite Figure which contains one or more ODGBezierFigures as its children.
 */
public class ODGPathFigure extends org.jhotdraw.draw.figure.AbstractAttributedCompositeFigure implements org.jhotdraw.samples.odg.figures.ODGFigure {
    private static final long serialVersionUID = 1L;

    /**
     * This cachedPath is used for drawing.
     */
    private transient java.awt.geom.Path2D.Double cachedPath;

    public ODGPathFigure() {
        add(new org.jhotdraw.samples.odg.figures.ODGBezierFigure());
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
    public void drawFill(java.awt.Graphics2D g) {
        boolean isClosed = getChild(0).attr().get(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED);
        if (isClosed) {
            g.fill(getPath());
        }
    }

    @java.lang.Override
    public void drawStroke(java.awt.Graphics2D g) {
        g.draw(getPath());
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedPath = null;
        cachedDrawingArea = null;
    }

    protected java.awt.geom.Path2D.Double getPath() {
        if (cachedPath == null) {
            cachedPath = new java.awt.geom.Path2D.Double();
            cachedPath.setWindingRule(attr().get(org.jhotdraw.draw.AttributeKeys.WINDING_RULE) == org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD ? java.awt.geom.Path2D.Double.WIND_EVEN_ODD : java.awt.geom.Path2D.Double.WIND_NON_ZERO);
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                org.jhotdraw.samples.odg.figures.ODGBezierFigure b = ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child));
                cachedPath.append(b.getBezierPath(), false);
            }
        }
        return cachedPath;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            double strokeTotalWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2.0;
            if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                width *= attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
            } else if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP) != java.awt.BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            java.awt.geom.Path2D.Double gp = getPath();
            java.awt.geom.Rectangle2D strokeRect = new java.awt.geom.Rectangle2D.Double(0, 0, width, width);
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                gp = ((java.awt.geom.Path2D.Double) (gp.clone()));
                gp.transform(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM));
                strokeRect = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(strokeRect).getBounds2D();
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
        /* return cachedPath.contains(p2); */
        boolean isClosed = getChild(0).attr().get(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED);
        double tolerance = java.lang.Math.max(2.0F, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, 1.0) / 2.0);
        if (isClosed) {
            if (getPath().contains(p)) {
                return true;
            }
            double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2.0;
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
            org.jhotdraw.samples.odg.figures.ODGBezierFigure b = getChild(0);
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
            if ((attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.odg.Gradient g = org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT, g);
            }
            if ((attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.odg.Gradient g = org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT, g);
            }
        }
        invalidate();
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void restoreTransformTo(java.lang.Object geometry) {
        invalidate();
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        java.util.ArrayList<org.jhotdraw.geom.path.BezierPath> paths = ((java.util.ArrayList<org.jhotdraw.geom.path.BezierPath>) (restoreData[0]));
        for (int i = 0, n = getChildCount(); i < n; i++) {
            getChild(i).setBezierPath(paths.get(i));
        }
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.odg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.odg.Gradient) (restoreData[3])));
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.lang.Object getTransformRestoreData() {
        java.util.ArrayList<org.jhotdraw.geom.path.BezierPath> paths = new java.util.ArrayList<org.jhotdraw.geom.path.BezierPath>(getChildCount());
        for (int i = 0, n = getChildCount(); i < n; i++) {
            paths.add(getChild(i).getBezierPath());
        }
        return new java.lang.Object[]{ paths, org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    @java.lang.Override
    public boolean isEmpty() {
        for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
            org.jhotdraw.samples.odg.figures.ODGBezierFigure b = ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child));
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
            case 0 :
                handles.add(new org.jhotdraw.samples.odg.figures.ODGPathOutlineHandle(this));
                for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                    handles.addAll(((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child)).createHandles(this, detailLevel));
                }
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
        final org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.odg.Labels");
        java.util.LinkedList<javax.swing.Action> actions = new java.util.LinkedList<javax.swing.Action>();
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.TRANSFORM.setUndoable(ODGPathFigure.this, null));
                    changed();
                }
            });
            actions.add(new javax.swing.AbstractAction(labels.getString("edit.flattenTransform.text")) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    // CompositeEdit edit = new CompositeEdit(labels.getString("flattenTransform"));
                    // TransformEdit edit = new TransformEdit(ODGPathFigure.this, )
                    final java.lang.Object restoreData = getTransformRestoreData();
                    javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                        private static final long serialVersionUID = 1L;

                        @java.lang.Override
                        public java.lang.String getPresentationName() {
                            return labels.getString("flattenTransform");
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
        actions.add(new javax.swing.AbstractAction(labels.getString("closePath")) {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                    willChange();
                    getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED.setUndoable(child, true));
                    changed();
                }
            }
        });
        actions.add(new javax.swing.AbstractAction(labels.getString("openPath")) {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                    willChange();
                    getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED.setUndoable(child, false));
                    changed();
                }
            }
        });
        actions.add(new javax.swing.AbstractAction(labels.getString("windingRule.evenOdd")) {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                willChange();
                getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.WINDING_RULE.setUndoable(ODGPathFigure.this, org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD));
                changed();
            }
        });
        actions.add(new javax.swing.AbstractAction(labels.getString("windingRule.nonZero")) {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ODGPathFigure.this.willChange();
                getDrawing().fireUndoableEditHappened(org.jhotdraw.draw.AttributeKeys.WINDING_RULE.setUndoable(ODGPathFigure.this, org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO));
                ODGPathFigure.this.changed();
            }
        });
        return actions;
    }

    // CONNECTING
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return null;// ODG does not support connectors

    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        return null;// ODG does not support connectors

    }

    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        if ((evt.getClickCount() == 2) && ((view.getHandleDetailLevel() % 2) == 0)) {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                org.jhotdraw.samples.odg.figures.ODGBezierFigure bf = ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child));
                int index = bf.getBezierPath().findSegment(p, 5.0F / view.getScaleFactor());
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
        super.add(index, ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (figure)));
    }

    @java.lang.Override
    public org.jhotdraw.samples.odg.figures.ODGBezierFigure getChild(int index) {
        return ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (super.getChild(index)));
    }

    @java.lang.Override
    public org.jhotdraw.samples.odg.figures.ODGPathFigure clone() {
        org.jhotdraw.samples.odg.figures.ODGPathFigure that = ((org.jhotdraw.samples.odg.figures.ODGPathFigure) (super.clone()));
        return that;
    }

    public void flattenTransform() {
        willChange();
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            for (org.jhotdraw.draw.figure.Figure child : getChildren()) {
                ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child)).transform(tx);
                ((org.jhotdraw.samples.odg.figures.ODGBezierFigure) (child)).flattenTransform();
            }
        }
        attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, null);
        changed();
    }
}