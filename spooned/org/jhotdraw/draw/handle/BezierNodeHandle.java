/* @(#)BezierNodeHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A {@link Handle} which allows to interactively change a node of a bezier path.
 */
public class BezierNodeHandle extends org.jhotdraw.draw.handle.AbstractHandle implements org.jhotdraw.draw.constrainer.CoordinateDataSupplier {
    protected int index;

    private org.jhotdraw.undo.CompositeEdit edit;

    private org.jhotdraw.geom.path.BezierPath.Node oldNode;

    private org.jhotdraw.draw.figure.Figure transformOwner;

    public BezierNodeHandle(org.jhotdraw.draw.figure.BezierFigure owner, int index) {
        this(owner, index, owner);
    }

    public BezierNodeHandle(org.jhotdraw.draw.figure.BezierFigure owner, int index, org.jhotdraw.draw.figure.Figure transformOwner) {
        super(owner);
        this.index = index;
        this.transformOwner = transformOwner;
        transformOwner.addFigureListener(FIGURE_LISTENER);
    }

    @java.lang.Override
    public void dispose() {
        super.dispose();
        if (transformOwner != null) {
            transformOwner.removeFigureListener(FIGURE_LISTENER);
            transformOwner = null;
        }
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        int size = f.getNodeCount();
        boolean isClosed = f.isClosed();
        java.awt.Color fillColor;
        java.awt.Color strokeColor;
        if (getEditor().getTool().supportsHandleInteraction()) {
            fillColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_NODE_HANDLE_FILL_COLOR);
            strokeColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_NODE_HANDLE_STROKE_COLOR);
        } else {
            fillColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_NODE_HANDLE_FILL_COLOR_DISABLED);
            strokeColor = getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.BEZIER_NODE_HANDLE_STROKE_COLOR_DISABLED);
        }
        if (size > index) {
            org.jhotdraw.geom.path.BezierPath.Node v = f.getNode(index);
            if (((v.mask == 0) || (((index == 0) && (v.mask == org.jhotdraw.geom.path.BezierPath.C1_MASK)) && (!isClosed))) || (((index == (size - 1)) && (v.mask == org.jhotdraw.geom.path.BezierPath.C2_MASK)) && (!isClosed))) {
                drawRectangle(g, fillColor, strokeColor);
            } else if ((((v.mask == org.jhotdraw.geom.path.BezierPath.C1_MASK) || (v.mask == org.jhotdraw.geom.path.BezierPath.C2_MASK)) || ((index == 0) && (!isClosed))) || ((index == (size - 1)) && (!isClosed))) {
                drawDiamond(g, fillColor, strokeColor);
            } else {
                drawCircle(g, fillColor, strokeColor);
            }
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.BezierFigure getOwner() {
        return ((org.jhotdraw.draw.figure.BezierFigure) (super.getOwner()));
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        if (getOwner().getNodeCount() > index) {
            java.awt.geom.Point2D.Double p = getOwner().getPoint(index, 0);
            if (getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).transform(p, p);
            }
            return p;
        } else {
            return null;
        }
    }

    protected org.jhotdraw.geom.path.BezierPath.Node getBezierNode() {
        return getOwner().getNodeCount() > index ? getOwner().getNode(index) : null;
    }

    protected org.jhotdraw.draw.figure.Figure getTransformOwner() {
        return transformOwner;
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.draw.figure.BezierFigure figure = getOwner();
        view.getDrawing().fireUndoableEditHappened(edit = new org.jhotdraw.undo.CompositeEdit("Punkt verschieben"));
        oldNode = figure.getNode(index);
        fireHandleRequestSecondaryHandles();
        if ((view.getConstrainer() != null) && (view.getConstrainer() instanceof org.jhotdraw.draw.constrainer.CoordinateDataReceiver receiver)) {
            receiver.setCoordinateSupplier(this);
        }
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.BezierFigure figure = getOwner();
        figure.willChange();
        java.awt.geom.Point2D.Double p = (view.getConstrainer() == null) ? view.viewToDrawing(lead) : view.getConstrainer().constrainPoint(view.viewToDrawing(lead));
        if (getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                getTransformOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        org.jhotdraw.geom.path.BezierPath.Node n = figure.getNode(index);
        // fireAreaInvalidated(n);
        n.moveTo(p);
        // fireAreaInvalidated(n);
        figure.setNode(index, n);
        figure.changed();
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
        final org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        org.jhotdraw.geom.path.BezierPath.Node oldValue = ((org.jhotdraw.geom.path.BezierPath.Node) (oldNode.clone()));
        org.jhotdraw.geom.path.BezierPath.Node newValue = f.getNode(index);
        // Change node type
        if (((modifiersEx & (((java.awt.event.InputEvent.META_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK) | java.awt.event.InputEvent.ALT_DOWN_MASK) | java.awt.event.InputEvent.SHIFT_DOWN_MASK)) != 0) && ((modifiersEx & java.awt.event.InputEvent.BUTTON2_MASK) == 0)) {
            f.willChange();
            if (((index > 0) && (index < f.getNodeCount())) || f.isClosed()) {
                newValue.mask = (newValue.mask + 3) % 4;
            } else if (index == 0) {
                newValue.mask = ((newValue.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) == 0) ? org.jhotdraw.geom.path.BezierPath.C2_MASK : 0;
            } else {
                newValue.mask = ((newValue.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) ? org.jhotdraw.geom.path.BezierPath.C1_MASK : 0;
            }
            f.setNode(index, newValue);
            f.changed();
            fireHandleRequestSecondaryHandles();
        }
        view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldValue, newValue) {
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
        if ((view.getConstrainer() != null) && (view.getConstrainer() instanceof org.jhotdraw.draw.constrainer.CoordinateDataReceiver receiver)) {
            receiver.clearCoordinateSupplier();
        }
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle h) {
        /* if (super.isCombinableWith(h)) {
        BezierNodeHandle that = (BezierNodeHandle) h;
        return that.index == this.index &&
        that.getOwner().getNodeCount() ==
        this.getOwner().getNodeCount();
        }
         */
        return false;
    }

    @java.lang.Override
    public void trackDoubleClick(java.awt.Point p, int modifiersEx) {
        final org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        if ((f.getNodeCount() > 2) && ((modifiersEx & ((java.awt.event.InputEvent.META_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK) | java.awt.event.InputEvent.ALT_DOWN_MASK)) == 0)) {
            java.awt.Rectangle invalidatedArea = getDrawingArea();
            f.willChange();
            final org.jhotdraw.geom.path.BezierPath.Node removedNode = f.removeNode(index);
            f.changed();
            fireHandleRequestRemove(invalidatedArea);
            fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    return labels.getString("edit.bezierPath.joinSegments.text");
                }

                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    view.removeFromSelection(f);
                    f.willChange();
                    f.removeNode(index);
                    f.changed();
                    view.addToSelection(f);
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    super.undo();
                    view.removeFromSelection(f);
                    f.willChange();
                    f.addNode(index, removedNode);
                    f.changed();
                    view.addToSelection(f);
                }
            });
        }
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createSecondaryHandles() {
        org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        java.util.Collection<org.jhotdraw.draw.handle.Handle> list = new java.util.ArrayList<>();
        org.jhotdraw.geom.path.BezierPath.Node v = f.getNode(index);
        if (((v.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) && ((index != 0) || f.isClosed())) {
            list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, index, 1, getTransformOwner()));
        }
        if (((v.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) && ((index < (f.getNodeCount() - 1)) || f.isClosed())) {
            list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, index, 2, getTransformOwner()));
        }
        if ((index > 0) || f.isClosed()) {
            int i = (index == 0) ? f.getNodeCount() - 1 : index - 1;
            v = f.getNode(i);
            if ((v.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) {
                list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, i, 2, getTransformOwner()));
            }
        }
        if ((index < (f.getNodeCount() - 1)) || f.isClosed()) {
            int i = (index == (f.getNodeCount() - 1)) ? 0 : index + 1;
            v = f.getNode(i);
            if ((v.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) {
                list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, i, 1, getTransformOwner()));
            }
        }
        return list;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.geom.path.BezierPath.Node node = getBezierNode();
        return node == null ? null : labels.getFormatted("handle.bezierNode.toolTipText", labels.getFormatted(node.getMask() == 0 ? "handle.bezierNode.linear.value" : node.getMask() == org.jhotdraw.geom.path.BezierPath.C1C2_MASK ? "handle.bezierNode.cubic.value" : "handle.bezierNode.quadratic.value"));
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        final org.jhotdraw.draw.figure.BezierFigure f = getOwner();
        oldNode = f.getNode(index);
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP :
                f.willChange();
                f.setPoint(index, new java.awt.geom.Point2D.Double(oldNode.x[0], oldNode.y[0] - 1.0));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DOWN :
                f.willChange();
                f.setPoint(index, new java.awt.geom.Point2D.Double(oldNode.x[0], oldNode.y[0] + 1.0));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_LEFT :
                f.willChange();
                f.setPoint(index, new java.awt.geom.Point2D.Double(oldNode.x[0] - 1.0, oldNode.y[0]));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_RIGHT :
                f.willChange();
                f.setPoint(index, new java.awt.geom.Point2D.Double(oldNode.x[0] + 1.0, oldNode.y[0]));
                f.changed();
                view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.BezierNodeEdit(f, index, oldNode, f.getNode(index)));
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DELETE :
            case java.awt.event.KeyEvent.VK_BACK_SPACE :
                java.awt.Rectangle invalidatedArea = getDrawingArea();
                f.willChange();
                final org.jhotdraw.geom.path.BezierPath.Node removedNode = f.removeNode(index);
                f.changed();
                fireHandleRequestRemove(invalidatedArea);
                fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public java.lang.String getPresentationName() {
                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                        return labels.getString("edit.bezierPath.joinSegments.text");
                    }

                    @java.lang.Override
                    public void redo() throws javax.swing.undo.CannotRedoException {
                        super.redo();
                        view.removeFromSelection(f);
                        f.willChange();
                        f.removeNode(index);
                        f.changed();
                        view.addToSelection(f);
                    }

                    @java.lang.Override
                    public void undo() throws javax.swing.undo.CannotUndoException {
                        super.undo();
                        view.removeFromSelection(f);
                        f.willChange();
                        f.addNode(index, removedNode);
                        f.changed();
                        view.addToSelection(f);
                    }
                });
                evt.consume();
                // At this point, the handle is no longer valid, and
                // handles at higher node indices have become invalid too.
                fireHandleRequestRemove(invalidatedArea);
                break;
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.CoordinateData getConstrainerCoordinates(int before, int after) {
        if (this.getOwner() == null)
            return null;

        java.util.List<java.awt.geom.Point2D.Double> list = new java.util.ArrayList<>();
        for (int idx = 0; idx < this.getOwner().getNodeCount(); idx++) {
            list.add(this.getOwner().getPoint(idx));
        }
        return new org.jhotdraw.draw.constrainer.CoordinateData(list.toArray(java.awt.geom.Point2D.Double[]::new), index);
    }
}