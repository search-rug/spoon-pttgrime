/* @(#)BezierTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.BezierFigure;
/**
 * A {@link Tool} which allows to create a new {@link BezierFigure} by drawing its path.
 *
 * <p>To creation of the BezierFigure can be finished by adding a segment which closes the path, or
 * by double clicking on the drawing area, or by selecting a different tool in the DrawingEditor.
 */
public class BezierTool extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.constrainer.CoordinateDataSupplier {
    private static final long serialVersionUID = 1L;

    private java.lang.Boolean finishWhenMouseReleased;

    protected java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes;

    private boolean isToolDoneAfterCreation;

    /**
     * The prototype for new figures.
     */
    private org.jhotdraw.draw.figure.BezierFigure prototype;

    /**
     * The created figure.
     */
    protected org.jhotdraw.draw.figure.BezierFigure createdFigure;

    protected org.jhotdraw.draw.figure.Figure addedFigure;

    private int nodeCountBeforeDrag;

    /**
     * A localized name for this tool. The presentationName is displayed by the UndoableEdit.
     */
    private java.lang.String presentationName;

    private java.awt.Point mouseLocation;

    /**
     * Holds the view on which we are currently creating a figure.
     */
    private org.jhotdraw.draw.DrawingView creationView;

    private final boolean calculateFittedCurveAfterCreation;

    public BezierTool(org.jhotdraw.draw.figure.BezierFigure prototype) {
        this(prototype, null);
    }

    public BezierTool(org.jhotdraw.draw.figure.BezierFigure prototype, boolean calculateFittedCurveAfterCreation) {
        this(prototype, null, null, calculateFittedCurveAfterCreation);
    }

    public BezierTool(org.jhotdraw.draw.figure.BezierFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        this(prototype, attributes, null);
    }

    public BezierTool(org.jhotdraw.draw.figure.BezierFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name) {
        this(prototype, attributes, null, true);
    }

    public BezierTool(org.jhotdraw.draw.figure.BezierFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name, boolean calculateFittedCurveAfterCreation) {
        this.prototype = prototype;
        this.attributes = attributes;
        if (name == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            name = labels.getString("edit.createFigure.text");
        }
        this.presentationName = name;
        this.calculateFittedCurveAfterCreation = calculateFittedCurveAfterCreation;
    }

    public java.lang.String getPresentationName() {
        return presentationName;
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
        getView().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        if (getView().getConstrainer() instanceof org.jhotdraw.draw.constrainer.CoordinateDataReceiver receiver) {
            receiver.setCoordinateSupplier(this);
        }
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        super.deactivate(editor);
        getView().setCursor(java.awt.Cursor.getDefaultCursor());
        if (createdFigure != null) {
            if ((anchor != null) && (mouseLocation != null)) {
                java.awt.Rectangle r = new java.awt.Rectangle(anchor);
                r.add(mouseLocation);
                if ((createdFigure.getNodeCount() > 0) && createdFigure.isClosed()) {
                    r.add(getView().drawingToView(createdFigure.getStartPoint()));
                }
                fireAreaInvalidated(r);
            }
            finishCreation(createdFigure, creationView);
            createdFigure = null;
        }
        if ((getView().getConstrainer() != null) && (getView().getConstrainer() instanceof org.jhotdraw.draw.constrainer.CoordinateDataReceiver receiver)) {
            receiver.clearCoordinateSupplier();
        }
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        if (mouseLocation != null) {
            java.awt.Rectangle r = new java.awt.Rectangle(mouseLocation);
            r.add(evt.getPoint());
            r.grow(1, 1);
            fireAreaInvalidated(r);
        }
        mouseLocation = evt.getPoint();
        super.mousePressed(evt);
        if ((createdFigure != null) && (creationView != getView())) {
            finishCreation(createdFigure, creationView);
            createdFigure = null;
        }
        if (createdFigure == null) {
            creationView = getView();
            creationView.clearSelection();
            finishWhenMouseReleased = null;
            createdFigure = createFigure();
            createdFigure.addNode(new org.jhotdraw.geom.path.BezierPath.Node(creationView.getConstrainer() == null ? creationView.viewToDrawing(anchor) : creationView.getConstrainer().constrainPoint(creationView.viewToDrawing(anchor), createdFigure)));
            getDrawing().add(processCreatedFigureBeforeAddingToDocument(createdFigure));
        } else if (evt.getClickCount() == 1) {
            addPointToFigure(creationView.getConstrainer() == null ? creationView.viewToDrawing(anchor) : creationView.getConstrainer().constrainPoint(creationView.viewToDrawing(anchor), createdFigure));
        }
        nodeCountBeforeDrag = createdFigure.getNodeCount();
    }

    @java.lang.Override
    protected org.jhotdraw.draw.figure.Figure processCreatedFigureBeforeAddingToDocument(org.jhotdraw.draw.figure.Figure figure) {
        addedFigure = figure;
        return figure;
    }

    @java.lang.SuppressWarnings("unchecked")
    protected org.jhotdraw.draw.figure.BezierFigure createFigure() {
        org.jhotdraw.draw.figure.BezierFigure f = prototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        if (attributes != null) {
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : attributes.entrySet()) {
                f.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
        }
        return f;
    }

    protected org.jhotdraw.draw.figure.Figure getCreatedFigure() {
        return createdFigure;
    }

    protected org.jhotdraw.draw.figure.Figure getAddedFigure() {
        return createdFigure;
    }

    protected void addPointToFigure(java.awt.geom.Point2D.Double newPoint) {
        int pointCount = createdFigure.getNodeCount();
        createdFigure.willChange();
        if (pointCount < 2) {
            createdFigure.addNode(new org.jhotdraw.geom.path.BezierPath.Node(newPoint));
        } else {
            java.awt.geom.Point2D.Double endPoint = createdFigure.getEndPoint();
            java.awt.geom.Point2D.Double secondLastPoint = (pointCount <= 1) ? endPoint : createdFigure.getPoint(pointCount - 2, 0);
            if (newPoint.equals(endPoint)) {
                // nothing to do
            } else if ((pointCount > 1) && org.jhotdraw.geom.Geom.lineContainsPoint(newPoint.x, newPoint.y, secondLastPoint.x, secondLastPoint.y, endPoint.x, endPoint.y, 0.9F / getView().getScaleFactor())) {
                createdFigure.setPoint(pointCount - 1, 0, newPoint);
            } else {
                createdFigure.addNode(new org.jhotdraw.geom.path.BezierPath.Node(newPoint));
            }
        }
        createdFigure.changed();
    }

    @java.lang.Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (createdFigure != null) {
            switch (evt.getClickCount()) {
                case 1 :
                    if (createdFigure.getNodeCount() > 2) {
                        java.awt.Rectangle r = new java.awt.Rectangle(getView().drawingToView(createdFigure.getStartPoint()));
                        r.grow(2, 2);
                        if (r.contains(evt.getX(), evt.getY())) {
                            createdFigure.setClosed(true);
                            finishCreation(createdFigure, creationView);
                            createdFigure = null;
                            if (isToolDoneAfterCreation) {
                                fireToolDone();
                            }
                        }
                    }
                    break;
                case 2 :
                    finishWhenMouseReleased = null;
                    finishCreation(createdFigure, creationView);
                    createdFigure = null;
                    break;
            }
        }
    }

    protected void fireUndoEvent(org.jhotdraw.draw.figure.Figure createdFigure, org.jhotdraw.draw.DrawingView creationView) {
        final org.jhotdraw.draw.figure.Figure addedFigure = this.addedFigure;
        final org.jhotdraw.draw.Drawing addedDrawing = creationView.getDrawing();
        final org.jhotdraw.draw.DrawingView addedView = creationView;
        getDrawing().fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                return presentationName;
            }

            @java.lang.Override
            public void undo() throws javax.swing.undo.CannotUndoException {
                super.undo();
                addedDrawing.remove(addedFigure);
            }

            @java.lang.Override
            public void redo() throws javax.swing.undo.CannotRedoException {
                super.redo();
                addedView.clearSelection();
                addedDrawing.add(addedFigure);
                addedView.addToSelection(addedFigure);
            }
        });
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        isWorking = false;
        if (createdFigure.getNodeCount() > (nodeCountBeforeDrag + 1)) {
            createdFigure.willChange();
            org.jhotdraw.geom.path.BezierPath figurePath = createdFigure.getBezierPath();
            org.jhotdraw.geom.path.BezierPath digitizedPath = new org.jhotdraw.geom.path.BezierPath();
            for (int i = nodeCountBeforeDrag - 1, n = figurePath.size(); i < n; i++) {
                digitizedPath.add(figurePath.nodes().get(nodeCountBeforeDrag - 1));
                figurePath.remove(nodeCountBeforeDrag - 1);
            }
            org.jhotdraw.geom.path.BezierPath fittedPath = calculateFittedCurve(digitizedPath);
            // figurePath.addPolyline(digitizedPath);
            figurePath.addAll(fittedPath);
            createdFigure.setBezierPath(figurePath);
            createdFigure.changed();
            nodeCountBeforeDrag = createdFigure.getNodeCount();
        }
        if (finishWhenMouseReleased == java.lang.Boolean.TRUE) {
            if (createdFigure.getNodeCount() > 1) {
                java.awt.Rectangle r = new java.awt.Rectangle(anchor.x, anchor.y, 0, 0);
                r.add(evt.getX(), evt.getY());
                maybeFireBoundsInvalidated(r);
                finishCreation(createdFigure, creationView);
                createdFigure = null;
                finishWhenMouseReleased = null;
                return;
            }
        } else if (finishWhenMouseReleased == null) {
            finishWhenMouseReleased = java.lang.Boolean.FALSE;
        }
        // repaint dotted line
        java.awt.Rectangle r = new java.awt.Rectangle(anchor);
        r.add(mouseLocation);
        r.add(evt.getPoint());
        r.grow(1, 1);
        fireAreaInvalidated(r);
        anchor.x = evt.getX();
        anchor.y = evt.getY();
        mouseLocation = evt.getPoint();
    }

    protected void finishCreation(org.jhotdraw.draw.figure.BezierFigure createdFigure, org.jhotdraw.draw.DrawingView creationView) {
        fireUndoEvent(createdFigure, creationView);
        creationView.addToSelection(createdFigure);
        if (isToolDoneAfterCreation) {
            fireToolDone();
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        if (finishWhenMouseReleased == null) {
            finishWhenMouseReleased = java.lang.Boolean.TRUE;
        }
        int x = evt.getX();
        int y = evt.getY();
        addPointToFigure(getView().viewToDrawing(new java.awt.Point(x, y)));
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if ((((createdFigure != null) && (anchor != null)) && (mouseLocation != null)) && (getView() == creationView)) {
            g.setColor(java.awt.Color.BLACK);
            g.setStroke(new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 1.0F, 5.0F }, 0.0F));
            g.drawLine(anchor.x, anchor.y, mouseLocation.x, mouseLocation.y);
            if (((!isWorking) && createdFigure.isClosed()) && (createdFigure.getNodeCount() > 1)) {
                java.awt.Point p = creationView.drawingToView(createdFigure.getStartPoint());
                g.drawLine(mouseLocation.x, mouseLocation.y, p.x, p.y);
            }
        }
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        if (((createdFigure != null) && (anchor != null)) && (mouseLocation != null)) {
            if ((creationView != null) && (evt.getSource() == creationView.getComponent())) {
                java.awt.Rectangle r = new java.awt.Rectangle(anchor);
                r.add(mouseLocation);
                r.add(evt.getPoint());
                if (createdFigure.isClosed() && (createdFigure.getNodeCount() > 0)) {
                    r.add(creationView.drawingToView(createdFigure.getStartPoint()));
                }
                r.grow(1, 1);
                fireAreaInvalidated(r);
                mouseLocation = evt.getPoint();
            }
        }
    }

    protected org.jhotdraw.geom.path.BezierPath calculateFittedCurve(org.jhotdraw.geom.path.BezierPath path) {
        if (calculateFittedCurveAfterCreation) {
            return org.jhotdraw.geom.path.Bezier.fitBezierPath(path, 1.5 / getView().getScaleFactor());
        } else {
            return path;
        }
    }

    public void setToolDoneAfterCreation(boolean b) {
        isToolDoneAfterCreation = b;
    }

    public boolean isToolDoneAfterCreation() {
        return isToolDoneAfterCreation;
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.CoordinateData getConstrainerCoordinates(int before, int after) {
        if (this.createdFigure == null) {
            return null;
        }
        java.util.List<java.awt.geom.Point2D.Double> list = new java.util.ArrayList<>();
        for (int idx = 0; idx < this.createdFigure.getNodeCount(); idx++) {
            list.add(this.createFigure().getPoint(idx));
        }
        return new org.jhotdraw.draw.constrainer.CoordinateData(list.toArray(java.awt.geom.Point2D.Double[]::new), list.size());
    }
}