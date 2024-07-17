/* @(#)ConnectorHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.figure.ConnectionFigure;
/**
 * A {@link Handle} associated to a {@link Connector} which allows to create a new {@link ConnectionFigure} by dragging the handle to another connector.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ConnectorHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    /**
     * Holds the ConnectionFigure which is currently being created.
     */
    private org.jhotdraw.draw.figure.ConnectionFigure createdConnection;

    /**
     * The prototype for the ConnectionFigure to be created
     */
    private org.jhotdraw.draw.figure.ConnectionFigure prototype;

    /**
     * The Connector.
     */
    private org.jhotdraw.draw.connector.Connector connector;

    /**
     * The current connectable Figure.
     */
    private org.jhotdraw.draw.figure.Figure connectableFigure;

    /**
     * The current connectable Connector.
     */
    private org.jhotdraw.draw.connector.Connector connectableConnector;

    /**
     * All connectors of the connectable Figure.
     */
    protected java.util.Collection<org.jhotdraw.draw.connector.Connector> connectors = java.util.Collections.emptyList();

    public ConnectorHandle(org.jhotdraw.draw.connector.Connector connector, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        super(connector.getOwner());
        this.connector = connector;
        this.prototype = prototype;
    }

    public java.awt.geom.Point2D.Double getLocationOnDrawing() {
        return connector.getAnchor();
    }

    @java.lang.Override
    public java.awt.Point getScreenLocation() {
        return view.drawingToView(connector.getAnchor());
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.Graphics2D gg = ((java.awt.Graphics2D) (g.create()));
        gg.transform(view.getDrawingToViewTransform());
        for (org.jhotdraw.draw.connector.Connector c : connectors) {
            c.draw(gg);
        }
        if (createdConnection == null) {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.DISCONNECTED_CONNECTOR_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.DISCONNECTED_CONNECTOR_HANDLE_STROKE_COLOR));
        } else {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTOR_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTOR_HANDLE_STROKE_COLOR));
            java.awt.Point p = view.drawingToView(createdConnection.getEndPoint());
            g.setColor(getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTOR_HANDLE_FILL_COLOR));
            int width = getHandlesize();
            g.fillOval(p.x - (width / 2), p.y - (width / 2), width, width);
            g.setColor(getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTOR_HANDLE_STROKE_COLOR));
            g.drawOval(p.x - (width / 2), p.y - (width / 2), width, width);
        }
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        setConnection(createConnection());
        java.awt.geom.Point2D.Double p = getLocationOnDrawing();
        getConnection().setStartPoint(p);
        getConnection().setEndPoint(p);
        view.getDrawing().add(getConnection());
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        // updateConnectors(lead);
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        fireAreaInvalidated(getDrawingArea());
        org.jhotdraw.draw.figure.Figure figure = findConnectableFigure(p, view.getDrawing());
        if (figure != connectableFigure) {
            connectableFigure = figure;
            repaintConnectors();
        }
        connectableConnector = findConnectableConnector(figure, p);
        if (connectableConnector != null) {
            p = connectableConnector.getAnchor();
        }
        getConnection().willChange();
        getConnection().setEndPoint(p);
        getConnection().changed();
        fireAreaInvalidated(getDrawingArea());
    }

    @java.lang.Override
    public java.awt.Rectangle getDrawingArea() {
        if (getConnection() != null) {
            java.awt.Rectangle r = new java.awt.Rectangle(view.drawingToView(getConnection().getEndPoint()));
            r.grow(getHandlesize(), getHandlesize());
            return r;
        } else {
            return new java.awt.Rectangle();// empty rectangle

        }
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        if (view.getConstrainer() != null) {
            p = view.getConstrainer().constrainPoint(p);
        }
        org.jhotdraw.draw.figure.Figure f = findConnectableFigure(p, view.getDrawing());
        connectableConnector = findConnectableConnector(f, p);
        if (connectableConnector != null) {
            final org.jhotdraw.draw.Drawing drawing = view.getDrawing();
            final org.jhotdraw.draw.figure.ConnectionFigure c = getConnection();
            getConnection().setStartConnector(connector);
            getConnection().setEndConnector(connectableConnector);
            getConnection().updateConnection();
            view.clearSelection();
            view.addToSelection(c);
            view.getDrawing().fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    return labels.getString("edit.createConnectionFigure.text");
                }

                @java.lang.Override
                public void undo() throws javax.swing.undo.CannotUndoException {
                    super.undo();
                    drawing.remove(c);
                }

                @java.lang.Override
                public void redo() throws javax.swing.undo.CannotRedoException {
                    super.redo();
                    drawing.add(c);
                    view.clearSelection();
                    view.addToSelection(c);
                }
            });
        } else {
            view.getDrawing().remove(getConnection());
            fireAreaInvalidated(getDrawingArea());
        }
        connectableConnector = null;
        connectors = java.util.Collections.emptyList();
        setConnection(null);
        setTargetFigure(null);
    }

    /**
     * Creates the ConnectionFigure. By default the figure prototype is cloned.
     */
    protected org.jhotdraw.draw.figure.ConnectionFigure createConnection() {
        return ((org.jhotdraw.draw.figure.ConnectionFigure) (prototype.clone()));
    }

    protected void setConnection(org.jhotdraw.draw.figure.ConnectionFigure newConnection) {
        createdConnection = newConnection;
    }

    protected org.jhotdraw.draw.figure.ConnectionFigure getConnection() {
        return createdConnection;
    }

    protected org.jhotdraw.draw.figure.Figure getTargetFigure() {
        return connectableFigure;
    }

    protected void setTargetFigure(org.jhotdraw.draw.figure.Figure newTargetFigure) {
        connectableFigure = newTargetFigure;
    }

    private org.jhotdraw.draw.figure.Figure findConnectableFigure(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.Drawing drawing) {
        for (org.jhotdraw.draw.figure.Figure figure : drawing.getFiguresFrontToBack()) {
            if (((!figure.includes(getConnection())) && figure.isConnectable()) && figure.contains(p)) {
                return figure;
            }
        }
        return null;
    }

    /**
     * Finds a connection end figure.
     */
    protected org.jhotdraw.draw.connector.Connector findConnectableConnector(org.jhotdraw.draw.figure.Figure connectableFigure, java.awt.geom.Point2D.Double p) {
        org.jhotdraw.draw.connector.Connector target = (connectableFigure == null) ? null : connectableFigure.findConnector(p, getConnection());
        if ((((connectableFigure != null) && connectableFigure.isConnectable()) && (!connectableFigure.includes(getOwner()))) && getConnection().canConnect(connector, target)) {
            return target;
        }
        return null;
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle handle) {
        return false;
    }

    /**
     * Updates the list of connectors that we draw when the user moves or drags the mouse over a
     * figure to which can connect.
     */
    public void repaintConnectors() {
        java.awt.geom.Rectangle2D.Double invalidArea = null;
        for (org.jhotdraw.draw.connector.Connector c : connectors) {
            if (invalidArea == null) {
                invalidArea = c.getDrawingArea();
            } else {
                invalidArea.add(c.getDrawingArea());
            }
        }
        connectors = (connectableFigure == null) ? java.util.Collections.emptyList() : connectableFigure.getConnectors(prototype);
        for (org.jhotdraw.draw.connector.Connector c : connectors) {
            if (invalidArea == null) {
                invalidArea = c.getDrawingArea();
            } else {
                invalidArea.add(c.getDrawingArea());
            }
        }
        if (invalidArea != null) {
            view.getComponent().repaint(view.drawingToView(invalidArea));
        }
    }
}