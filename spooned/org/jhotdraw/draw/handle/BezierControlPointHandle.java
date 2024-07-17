/* @(#)BezierControlPointHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A {@link Handle} which allows to interactively change a control point of a bezier path.
 */
public class BezierControlPointHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    protected int index;

    protected int controlPointIndex;

    private org.jhotdraw.undo.CompositeEdit edit;

    private org.jhotdraw.draw.figure.Figure transformOwner;

    private org.jhotdraw.geom.path.BezierPath.Node oldNode;

    public BezierControlPointHandle(org.jhotdraw.draw.figure.BezierFigure owner, int index, int coord) {
        this(owner, index, coord, owner);
    }

    public BezierControlPointHandle(org.jhotdraw.draw.figure.BezierFigure owner, int index, int coord, org.jhotdraw.draw.figure.Figure transformOwner) {
        super(owner);
        this.index = index;
        this.controlPointIndex = coord;
        this.transformOwner = transformOwner;
        transformOwner.addFigureListener(FIGURE_LISTENER);
    }

    @java.lang.Override
    public void dispose() {
        super.dispose();
        transformOwner.removeFigureListener(FIGURE_LISTENER);
        transformOwner = null;
    }

    protected org.jhotdraw.draw.figure.BezierFigure getBezierFigure() {
        return getOwner();
    }

    protected org.jhotdraw.draw.figure.Figure getTransformOwner() {
        return transformOwner;
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        if (getBezierFigure().getNodeCount() > index) {
            java.awt.geom.Point2D.Double p = getBezierFigure().getPoint(index, controlPointIndex);
            if (getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
            }
            return p;
        } else {
            return null;
        }
    }

    protected org.jhotdraw.geom.path.BezierPath.Node getBezierNode() {
        return getBezierFigure().getNodeCount() > index ? getBezierFigure().getNode(index) : null;
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        org.jhotdraw.draw.figure.BezierFigure f = getBezierFigure();
        if (f.getNodeCount() > index) {
            org.jhotdraw.geom.path.BezierPath.Node v = f.getNode(index);
            java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(v.x[0], v.y[0]);
            java.awt.geom.Point2D.Double pc = new java.awt.geom.Point2D.Double(v.x[controlPointIndex], v.y[controlPointIndex]);
            org.jhotdraw.draw.figure.Figure tOwner = getTransformOwner();
            if (tOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                tOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p0, p0);
                tOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(pc, pc);
            }
            java.awt.Color handleFillColor;
            java.awt.Color handleStrokeColor;
            java.awt.Stroke stroke1;
            java.awt.Color strokeColor1;
            java.awt.Stroke stroke2;
            java.awt.Color strokeColor2;
            if (getEditor().getTool().supportsHandleInteraction()) {
                handleFillColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_CONTROL_POINT_HANDLE_FILL_COLOR);
                handleStrokeColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_CONTROL_POINT_HANDLE_STROKE_COLOR);
                stroke1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_STROKE_1);
                strokeColor1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_COLOR_1);
                stroke2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_STROKE_2);
                strokeColor2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_COLOR_2);
            } else {
                handleFillColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_CONTROL_POINT_HANDLE_FILL_COLOR_DISABLED);
                handleStrokeColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_CONTROL_POINT_HANDLE_STROKE_COLOR_DISABLED);
                stroke1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_STROKE_1_DISABLED);
                strokeColor1 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_COLOR_1_DISABLED);
                stroke2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_STROKE_2_DISABLED);
                strokeColor2 = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_TANGENT_COLOR_2_DISABLED);
            }
            if ((stroke1 != null) && (strokeColor1 != null)) {
                g.setStroke(stroke1);
                g.setColor(strokeColor1);
                g.draw(new java.awt.geom.Line2D.Double(view.drawingToView(p0), view.drawingToView(pc)));
            }
            if ((stroke2 != null) && (strokeColor2 != null)) {
                g.setStroke(stroke2);
                g.setColor(strokeColor2);
                g.draw(new java.awt.geom.Line2D.Double(view.drawingToView(p0), view.drawingToView(pc)));
            }
            if ((v.keepColinear && (v.mask == org.jhotdraw.geom.path.BezierPath.C1C2_MASK)) && (((index > 0) && (index < (f.getNodeCount() - 1))) || f.isClosed())) {
                drawCircle(g, handleStrokeColor, handleFillColor);
            } else {
                drawCircle(g, handleFillColor, handleStrokeColor);
            }
        }
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.draw.figure.BezierFigure figure = getOwner();
        view.getDrawing().fireUndoableEditHappened(edit = new org.jhotdraw.undo.CompositeEdit("Punkt verschieben"));
        oldNode = figure.getNode(index);
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.BezierFigure figure = getBezierFigure();
        java.awt.geom.Point2D.Double p = (view.getConstrainer() == null) ? view.viewToDrawing(lead) : view.getConstrainer().constrainPoint(view.viewToDrawing(lead));
        org.jhotdraw.geom.path.BezierPath.Node v = figure.getNode(index);
        fireAreaInvalidated(v);
        figure.willChange();
        org.jhotdraw.draw.figure.Figure tOwner = getTransformOwner();
        if (tOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                tOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        if (!v.keepColinear) {
            // move control point independently
            figure.setPoint(index, controlPointIndex, p);
        } else {
            // move control point and opposite control point on same line
            double a = java.lang.Math.PI + java.lang.Math.atan2(p.y - v.y[0], p.x - v.x[0]);
            int c2 = (controlPointIndex == 1) ? 2 : 1;
            double r = java.lang.Math.sqrt(((v.x[c2] - v.x[0]) * (v.x[c2] - v.x[0])) + ((v.y[c2] - v.y[0]) * (v.y[c2] - v.y[0])));
            double sina = java.lang.Math.sin(a);
            double cosa = java.lang.Math.cos(a);
            java.awt.geom.Point2D.Double p2 = new java.awt.geom.Point2D.Double((r * cosa) + v.x[0], (r * sina) + v.y[0]);
            figure.setPoint(index, controlPointIndex, p);
            figure.setPoint(index, c2, p2);
        }
        figure.changed();
        fireAreaInvalidated(figure.getNode(index));
    }

    private void fireAreaInvalidated(org.jhotdraw.geom.path.BezierPath.Node v) {
        java.awt.geom.Rectangle2D.Double dr = new java.awt.geom.Rectangle2D.Double(v.x[0], v.y[0], 0, 0);
        for (int i = 1; i < 3; i++) {
            dr.add(v.x[i], v.y[i]);
        }
        java.awt.Rectangle vr = view.drawingToView(dr);
        vr.grow(getHandlesize(), getHandlesize());
        fireAreaInvalidated(vr);
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        final org.jhotdraw.draw.figure.BezierFigure figure = getBezierFigure();
        org.jhotdraw.geom.path.BezierPath.Node oldValue = ((org.jhotdraw.geom.path.BezierPath.Node) (oldNode.clone()));
        org.jhotdraw.geom.path.BezierPath.Node newValue = figure.getNode(index);
        if ((modifiersEx & (((java.awt.event.InputEvent.META_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK) | java.awt.event.InputEvent.ALT_DOWN_MASK) | java.awt.event.InputEvent.SHIFT_DOWN_MASK)) != 0) {
            figure.willChange();
            newValue.keepColinear = !newValue.keepColinear;
            if (newValue.keepColinear) {
                // move control point and opposite control point on same line
                java.awt.geom.Point2D.Double p = figure.getPoint(index, controlPointIndex);
                double a = java.lang.Math.PI + java.lang.Math.atan2(p.y - newValue.y[0], p.x - newValue.x[0]);
                int c2 = (controlPointIndex == 1) ? 2 : 1;
                double r = java.lang.Math.sqrt(((newValue.x[c2] - newValue.x[0]) * (newValue.x[c2] - newValue.x[0])) + ((newValue.y[c2] - newValue.y[0]) * (newValue.y[c2] - newValue.y[0])));
                double sina = java.lang.Math.sin(a);
                double cosa = java.lang.Math.cos(a);
                java.awt.geom.Point2D.Double p2 = new java.awt.geom.Point2D.Double((r * cosa) + newValue.x[0], (r * sina) + newValue.y[0]);
                newValue.x[c2] = p2.x;
                newValue.y[c2] = p2.y;
            }
            figure.setNode(index, newValue);
            figure.changed();
        }
        view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(figure, index, oldValue, newValue) {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                fireHandleRequestSecondaryHandles();
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                fireHandleRequestSecondaryHandles();
            }
        });
        view.getDrawing().fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle h) {
        if (super.isCombinableWith(h)) {
            org.jhotdraw.draw.handle.BezierControlPointHandle that = ((org.jhotdraw.draw.handle.BezierControlPointHandle) (h));
            return ((that.index == this.index) && (that.controlPointIndex == this.controlPointIndex)) && (that.getBezierFigure().getNodeCount() == this.getBezierFigure().getNodeCount());
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.geom.path.BezierPath.Node node = getBezierNode();
        if (node == null) {
            return null;
        }
        if (node.mask == org.jhotdraw.geom.path.BezierPath.C1C2_MASK) {
            return labels.getFormatted("handle.bezierControlPoint.toolTipText", labels.getFormatted(node.keepColinear ? "handle.bezierControlPoint.cubicColinear.value" : "handle.bezierControlPoint.cubicUnconstrained.value"));
        } else {
            return labels.getString("handle.bezierControlPoint.quadratic.toolTipText");
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.BezierFigure getOwner() {
        return ((org.jhotdraw.draw.figure.BezierFigure) (super.getOwner()));
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        final org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        org.jhotdraw.geom.path.BezierPath.Node oldNode = f.getNode(index);
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP :
                f.willChange();
                f.setPoint(index, controlPointIndex, new java.awt.geom.Point2D.Double(oldNode.x[controlPointIndex], oldNode.y[controlPointIndex] - 1.0));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DOWN :
                f.willChange();
                f.setPoint(index, controlPointIndex, new java.awt.geom.Point2D.Double(oldNode.x[controlPointIndex], oldNode.y[controlPointIndex] + 1.0));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_LEFT :
                f.willChange();
                f.setPoint(index, controlPointIndex, new java.awt.geom.Point2D.Double(oldNode.x[controlPointIndex] - 1.0, oldNode.y[controlPointIndex]));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_RIGHT :
                f.willChange();
                f.setPoint(index, controlPointIndex, new java.awt.geom.Point2D.Double(oldNode.x[controlPointIndex] + 1.0, oldNode.y[controlPointIndex]));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DELETE :
            case java.awt.event.KeyEvent.VK_BACK_SPACE :
                evt.consume();
                break;
        }
    }
}