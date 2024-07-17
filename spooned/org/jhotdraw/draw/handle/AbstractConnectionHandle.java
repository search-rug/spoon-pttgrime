/* @(#)AbstractConnectionHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.ConnectionFigure;
/**
 * This abstract class can be extended to implement a {@link Handle} the start or end point of a
 * {@link ConnectionFigure}.
 *
 * <p>XXX - Undo/Redo is not implemented yet.
 *
 * @author Werner Randelshofer
 * @version $Id: AbstractConnectionHandle.java 527 2009-06-07 14:28:19Z rawcoder $
 */
public abstract class AbstractConnectionHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private org.jhotdraw.draw.connector.Connector savedTarget;

    // private Connector connectableConnector;
    private org.jhotdraw.draw.figure.Figure connectableFigure;

    // private Point start;
    /**
     * We temporarily remove the Liner from the connection figure, while the handle is being moved. We
     * store the Liner here, and add it back when the user has finished the interaction.
     */
    private org.jhotdraw.draw.liner.Liner savedLiner;

    /**
     * All connectors of the connectable Figure.
     */
    protected java.util.Collection<org.jhotdraw.draw.connector.Connector> connectors = java.util.Collections.emptyList();

    /**
     * Initializes the change connection handle.
     */
    protected AbstractConnectionHandle(org.jhotdraw.draw.figure.ConnectionFigure owner) {
        super(owner);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.ConnectionFigure getOwner() {
        return ((org.jhotdraw.draw.figure.ConnectionFigure) (super.getOwner()));
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle handle) {
        return false;
    }

    /**
     * Returns the connector of the change.
     */
    protected abstract org.jhotdraw.draw.connector.Connector getTarget();

    /**
     * Disconnects the connection.
     */
    protected abstract void disconnect();

    /**
     * Connect the connection with the given figure.
     */
    protected abstract void connect(org.jhotdraw.draw.connector.Connector c);

    /**
     * Gets the side of the connection that is unaffected by the change.
     */
    protected org.jhotdraw.draw.connector.Connector getSource() {
        if (getTarget() == getOwner().getStartConnector()) {
            return getOwner().getEndConnector();
        }
        return getOwner().getStartConnector();
    }

    /**
     * Disconnects the connection.
     */
    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        savedTarget = getTarget();
        // start = anchor;
        savedLiner = getOwner().getLiner();
        getOwner().setLiner(null);
        // disconnect();
        fireHandleRequestSecondaryHandles();
    }

    /**
     * Finds a new connectableConnector of the connection.
     */
    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        if (view.getConstrainer() != null) {
            p = view.getConstrainer().constrainPoint(p);
        }
        connectableFigure = findConnectableFigure(p, view.getDrawing());
        if (connectableFigure != null) {
            org.jhotdraw.draw.connector.Connector aTarget = findConnectionTarget(p, view.getDrawing());
            if (aTarget != null) {
                p = aTarget.getAnchor();
            }
        }
        getOwner().willChange();
        setDrawingLocation(p);
        getOwner().changed();
        repaintConnectors();
    }

    /**
     * Connects the figure to the new connectableConnector. If there is no new connectableConnector
     * the connection reverts to its original one.
     */
    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.ConnectionFigure f = getOwner();
        // Change node type
        if (((modifiersEx & (((java.awt.event.InputEvent.META_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK) | java.awt.event.InputEvent.ALT_DOWN_MASK) | java.awt.event.InputEvent.SHIFT_DOWN_MASK)) != 0) && ((modifiersEx & java.awt.event.InputEvent.BUTTON2_DOWN_MASK) == 0)) {
            f.willChange();
            int index = getBezierNodeIndex();
            org.jhotdraw.geom.path.BezierPath.Node v = f.getNode(index);
            if ((index > 0) && (index < f.getNodeCount())) {
                v.mask = (v.mask + 3) % 4;
            } else if (index == 0) {
                v.mask = ((v.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) == 0) ? org.jhotdraw.geom.path.BezierPath.C2_MASK : 0;
            } else {
                v.mask = ((v.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) ? org.jhotdraw.geom.path.BezierPath.C1_MASK : 0;
            }
            f.setNode(index, v);
            f.changed();
            fireHandleRequestSecondaryHandles();
        }
        java.awt.geom.Point2D.Double p = view.viewToDrawing(lead);
        if (view.getConstrainer() != null) {
            p = view.getConstrainer().constrainPoint(p);
        }
        org.jhotdraw.draw.connector.Connector target = findConnectionTarget(p, view.getDrawing());
        if (target == null) {
            target = savedTarget;
        }
        setDrawingLocation(p);
        if (target != savedTarget) {
            disconnect();
            connect(target);
        }
        getOwner().setLiner(savedLiner);
        getOwner().updateConnection();
        // connectableConnector = null;
        connectors = java.util.Collections.emptyList();
    }

    private org.jhotdraw.draw.connector.Connector findConnectionTarget(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.Drawing drawing) {
        org.jhotdraw.draw.figure.Figure targetFigure = findConnectableFigure(p, drawing);
        if ((getSource() == null) && (targetFigure != null)) {
            return findConnector(p, targetFigure, getOwner());
        } else if (targetFigure != null) {
            org.jhotdraw.draw.connector.Connector target = findConnector(p, targetFigure, getOwner());
            if ((((targetFigure != null) && targetFigure.isConnectable()) && (!targetFigure.includes(getOwner()))) && canConnect(getSource(), target)) {
                return target;
            }
        }
        return null;
    }

    protected abstract boolean canConnect(org.jhotdraw.draw.connector.Connector existingEnd, org.jhotdraw.draw.connector.Connector targetEnd);

    protected org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure f, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return f.findConnector(p, prototype);
    }

    protected void setDrawingLocation(java.awt.geom.Point2D.Double p) {
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.Graphics2D gg = ((java.awt.Graphics2D) (g.create()));
        gg.transform(view.getDrawingToViewTransform());
        for (org.jhotdraw.draw.connector.Connector c : connectors) {
            c.draw(gg);
        }
        gg.dispose();
        if (getTarget() == null) {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.DISCONNECTED_CONNECTION_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.DISCONNECTED_CONNECTION_HANDLE_STROKE_COLOR));
        } else {
            drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTION_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.CONNECTED_CONNECTION_HANDLE_STROKE_COLOR));
        }
    }

    private org.jhotdraw.draw.figure.Figure findConnectableFigure(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.Drawing drawing) {
        for (org.jhotdraw.draw.figure.Figure f : drawing.getFiguresFrontToBack()) {
            if (((!f.includes(getOwner())) && f.isConnectable()) && f.contains(p)) {
                return f;
            }
        }
        return null;
    }

    protected org.jhotdraw.draw.figure.BezierFigure getBezierFigure() {
        return ((org.jhotdraw.draw.figure.BezierFigure) (getOwner()));
    }

    protected abstract int getBezierNodeIndex();

    @java.lang.Override
    public final java.util.Collection<org.jhotdraw.draw.handle.Handle> createSecondaryHandles() {
        java.util.Collection<org.jhotdraw.draw.handle.Handle> list = new java.util.ArrayList<>();
        if ((getOwner().getLiner() == null) && (savedLiner == null)) {
            int index = getBezierNodeIndex();
            org.jhotdraw.draw.figure.BezierFigure f = getBezierFigure();
            org.jhotdraw.geom.path.BezierPath.Node v = f.getNode(index);
            if (((v.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) && ((index != 0) || f.isClosed())) {
                list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, index, 1));
            }
            if (((v.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) && ((index < (f.getNodeCount() - 1)) || f.isClosed())) {
                list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, index, 2));
            }
            if ((index > 0) || f.isClosed()) {
                int i = (index == 0) ? f.getNodeCount() - 1 : index - 1;
                v = f.getNode(i);
                if ((v.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) {
                    list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, i, 2));
                }
            }
            if ((index < (f.getNodeCount() - 2)) || f.isClosed()) {
                int i = (index == (f.getNodeCount() - 1)) ? 0 : index + 1;
                v = f.getNode(i);
                if ((v.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) {
                    list.add(new org.jhotdraw.draw.handle.BezierControlPointHandle(f, i, 1));
                }
            }
        }
        return list;
    }

    protected org.jhotdraw.geom.path.BezierPath.Node getBezierNode() {
        int index = getBezierNodeIndex();
        return getBezierFigure().getNodeCount() > index ? getBezierFigure().getNode(index) : null;
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        org.jhotdraw.draw.figure.ConnectionFigure f = getOwner();
        if ((f.getLiner() == null) && (savedLiner == null)) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            org.jhotdraw.geom.path.BezierPath.Node node = getBezierNode();
            return node == null ? null : labels.getFormatted("handle.bezierNode.toolTipText", labels.getFormatted(node.getMask() == 0 ? "bezierNode.linearNode" : node.getMask() == org.jhotdraw.geom.path.BezierPath.C1C2_MASK ? "bezierNode.cubicNode" : "bezierNode.quadraticNode"));
        } else {
            return null;
        }
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
        connectors = (connectableFigure == null) ? java.util.Collections.emptyList() : connectableFigure.getConnectors(getOwner());
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