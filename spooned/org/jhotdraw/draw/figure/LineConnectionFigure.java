/* @(#)BezierBezierLineConnection.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
import org.jhotdraw.draw.liner.Liner;
/**
 * A {@link ConnectionFigure} which connects two figures using a bezier path.
 *
 * <p>The bezier path can be laid out manually using bezier handles provided by this figure, or
 * automatically using a {@link Liner} which can be set using the JavaBeans property {@code liner}.
 */
public class LineConnectionFigure extends org.jhotdraw.draw.figure.LineFigure implements org.jhotdraw.draw.figure.ConnectionFigure {
    private static final long serialVersionUID = 1L;

    /**
     * The name of the JaveBeans property {@code liner}.
     */
    public static final java.lang.String LINER_PROPERTY = "liner";

    private org.jhotdraw.draw.connector.Connector startConnector;

    private org.jhotdraw.draw.connector.Connector endConnector;

    private org.jhotdraw.draw.liner.Liner liner;

    /**
     * Handles figure changes in the start and the end figure.
     */
    private org.jhotdraw.draw.figure.LineConnectionFigure.ConnectionHandler connectionHandler = new org.jhotdraw.draw.figure.LineConnectionFigure.ConnectionHandler(this);

    private static class ConnectionHandler extends org.jhotdraw.draw.event.FigureListenerAdapter implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private org.jhotdraw.draw.figure.LineConnectionFigure owner;

        private ConnectionHandler(org.jhotdraw.draw.figure.LineConnectionFigure owner) {
            this.owner = owner;
        }

        @java.lang.Override
        public void figureRemoved(org.jhotdraw.draw.event.FigureEvent evt) {
            // The commented lines below must stay commented out.
            // This is because, we must not set our connectors to null,
            // in order to support reconnection using redo.
            /* if (evt.getFigure() == owner.getStartFigure()
            || evt.getFigure() == owner.getEndFigure()) {
            owner.setStartConnector(null);
            owner.setEndConnector(null);
            }
             */
            owner.fireFigureRequestRemove();
        }

        @java.lang.Override
        public void figureChanged(org.jhotdraw.draw.event.FigureEvent e) {
            if (!owner.isChanging()) {
                if ((e.getSource() == owner.getStartFigure()) || (e.getSource() == owner.getEndFigure())) {
                    owner.willChange();
                    owner.updateConnection();
                    owner.changed();
                }
            }
        }
    }

    public LineConnectionFigure() {
    }

    // DRAWING
    // SHAPE AND BOUNDS
    /**
     * Ensures that a connection is updated if the connection was moved.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        super.transform(tx);
        updateConnection();// make sure that we are still connected

    }

    // ATTRIBUTES
    // EDITING
    /**
     * Gets the handles of the figure. It returns the normal PolylineHandles but adds
     * ChangeConnectionHandles at the start and end.
     */
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.ArrayList<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>(getNodeCount());
        switch (detailLevel) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this));
                if (getLiner() == null) {
                    for (int i = 1, n = getNodeCount() - 1; i < n; i++) {
                        handles.add(new org.jhotdraw.draw.handle.BezierNodeHandle(this, i));
                    }
                }
                handles.add(new org.jhotdraw.draw.handle.ConnectionStartHandle(this));
                handles.add(new org.jhotdraw.draw.handle.ConnectionEndHandle(this));
                break;
        }
        return handles;
    }

    // CONNECTING
    /**
     * ConnectionFigures cannot be connected and always sets connectable to false.
     */
    @java.lang.Override
    public void setConnectable(boolean newValue) {
        super.setConnectable(false);
    }

    @java.lang.Override
    public void updateConnection() {
        willChange();
        if (getStartConnector() != null) {
            java.awt.geom.Point2D.Double start = getStartConnector().findStart(this);
            if (start != null) {
                setStartPoint(start);
            }
        }
        if (getEndConnector() != null) {
            java.awt.geom.Point2D.Double end = getEndConnector().findEnd(this);
            if (end != null) {
                setEndPoint(end);
            }
        }
        changed();
    }

    @java.lang.Override
    public void validate() {
        super.validate();
        lineout();
    }

    @java.lang.Override
    public boolean canConnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
        return start.getOwner().isConnectable() && end.getOwner().isConnectable();
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector getEndConnector() {
        return endConnector;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getEndFigure() {
        return endConnector == null ? null : endConnector.getOwner();
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector getStartConnector() {
        return startConnector;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure getStartFigure() {
        return startConnector == null ? null : startConnector.getOwner();
    }

    @java.lang.Override
    public void setEndConnector(org.jhotdraw.draw.connector.Connector newEnd) {
        if (newEnd != endConnector) {
            if (endConnector != null) {
                getEndFigure().removeFigureListener(connectionHandler);
                if (getStartFigure() != null) {
                    if (getDrawing() != null) {
                        handleDisconnect(getStartConnector(), getEndConnector());
                    }
                }
            }
            endConnector = newEnd;
            if (endConnector != null) {
                getEndFigure().addFigureListener(connectionHandler);
                if ((getStartFigure() != null) && (getEndFigure() != null)) {
                    if (getDrawing() != null) {
                        handleConnect(getStartConnector(), getEndConnector());
                        updateConnection();
                    }
                }
            }
        }
    }

    @java.lang.Override
    public void setStartConnector(org.jhotdraw.draw.connector.Connector newStart) {
        if (newStart != startConnector) {
            if (startConnector != null) {
                getStartFigure().removeFigureListener(connectionHandler);
                if (getEndFigure() != null) {
                    handleDisconnect(getStartConnector(), getEndConnector());
                }
            }
            startConnector = newStart;
            if (startConnector != null) {
                getStartFigure().addFigureListener(connectionHandler);
                if ((getStartFigure() != null) && (getEndFigure() != null)) {
                    handleConnect(getStartConnector(), getEndConnector());
                    updateConnection();
                }
            }
        }
    }

    // COMPOSITE FIGURES
    // LAYOUT
    /* public Liner getBezierPathLayouter() {
    return (Liner) get(BEZIER_PATH_LAYOUTER);
    }
    public void setBezierPathLayouter(Liner newValue) {
    set(BEZIER_PATH_LAYOUTER, newValue);
    }
    /**
    Lays out the connection. This is called when the connection
    itself changes. By default the connection is recalculated
    /
    public void layoutConnection() {
    if (getStartConnector() != null && getEndConnector() != null) {
    willChange();
    Liner bpl = getBezierPathLayouter();
    if (bpl != null) {
    bpl.lineout(this);
    } else {
    if (getStartConnector() != null) {
    Point2D.Double start = getStartConnector().findStart(this);
    if(start != null) {
    basicSetStartPoint(start);
    }
    }
    if (getEndConnector() != null) {
    Point2D.Double end = getEndConnector().findEnd(this);
    if(end != null) {
    basicSetEndPoint(end);
    }
    }
    }
    changed();
    }
    }
     */
    // CLONING
    // EVENT HANDLING
    /**
     * This method is invoked, when the Figure is being removed from a Drawing. This method invokes
     * handleConnect, if the Figure is connected.
     *
     * @see #handleConnect
     */
    @java.lang.Override
    public void addNotify(org.jhotdraw.draw.Drawing drawing) {
        super.addNotify(drawing);
        if ((getStartConnector() != null) && (getEndConnector() != null)) {
            handleConnect(getStartConnector(), getEndConnector());
            updateConnection();
        }
    }

    /**
     * This method is invoked, when the Figure is being removed from a Drawing. This method invokes
     * handleDisconnect, if the Figure is connected.
     *
     * @see #handleDisconnect
     */
    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing drawing) {
        if ((getStartConnector() != null) && (getEndConnector() != null)) {
            handleDisconnect(getStartConnector(), getEndConnector());
        }
        // Note: we do not set the connectors to null here, because we
        // need them when we are added back to a drawing again. For example,
        // when an undo is performed, after the LineConnection has been
        // deleted.
        /* setStartConnector(null);
        setEndConnector(null);
         */
        super.removeNotify(drawing);
    }

    /**
     * Handles the disconnection of a connection. Override this method to handle this event.
     *
     * <p>Note: This method is only invoked, when the Figure is part of a Drawing. If the Figure is
     * removed from a Drawing, this method is invoked on behalf of the removeNotify call to the
     * Figure.
     *
     * @see #removeNotify
     */
    protected void handleDisconnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
    }

    /**
     * Handles the connection of a connection. Override this method to handle this event.
     *
     * <p>Note: This method is only invoked, when the Figure is part of a Drawing. If the Figure is
     * added to a Drawing this method is invoked on behalf of the addNotify call to the Figure.
     */
    protected void handleConnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.LineConnectionFigure clone() {
        org.jhotdraw.draw.figure.LineConnectionFigure that = ((org.jhotdraw.draw.figure.LineConnectionFigure) (super.clone()));
        that.connectionHandler = new org.jhotdraw.draw.figure.LineConnectionFigure.ConnectionHandler(that);
        if (this.liner != null) {
            that.liner = this.liner.clone();
        }
        // FIXME - For safety reasons, we clone the connectors, but they would
        // work, if we continued to use them. Maybe we should state somewhere
        // whether connectors should be reusable, or not.
        // To work properly, that must be registered as a figure listener
        // to the connected figures.
        if (this.startConnector != null) {
            that.startConnector = ((org.jhotdraw.draw.connector.Connector) (this.startConnector.clone()));
            that.getStartFigure().addFigureListener(that.connectionHandler);
        }
        if (this.endConnector != null) {
            that.endConnector = ((org.jhotdraw.draw.connector.Connector) (this.endConnector.clone()));
            that.getEndFigure().addFigureListener(that.connectionHandler);
        }
        if ((that.startConnector != null) && (that.endConnector != null)) {
            // that.handleConnect(that.getStartConnector(), that.getEndConnector());
            that.updateConnection();
        }
        return that;
    }

    @java.lang.Override
    public void remap(java.util.Map<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> oldToNew, boolean disconnectIfNotInMap) {
        willChange();
        super.remap(oldToNew, disconnectIfNotInMap);
        org.jhotdraw.draw.figure.Figure newStartFigure = null;
        org.jhotdraw.draw.figure.Figure newEndFigure = null;
        if (getStartFigure() != null) {
            newStartFigure = oldToNew.get(getStartFigure());
            if ((newStartFigure == null) && (!disconnectIfNotInMap)) {
                newStartFigure = getStartFigure();
            }
        }
        if (getEndFigure() != null) {
            newEndFigure = oldToNew.get(getEndFigure());
            if ((newEndFigure == null) && (!disconnectIfNotInMap)) {
                newEndFigure = getEndFigure();
            }
        }
        if (newStartFigure != null) {
            setStartConnector(newStartFigure.findCompatibleConnector(getStartConnector(), true));
        } else if (disconnectIfNotInMap) {
            setStartConnector(null);
        }
        if (newEndFigure != null) {
            setEndConnector(newEndFigure.findCompatibleConnector(getEndConnector(), false));
        } else if (disconnectIfNotInMap) {
            setEndConnector(null);
        }
        updateConnection();
        changed();
    }

    @java.lang.Override
    public boolean canConnect(org.jhotdraw.draw.connector.Connector start) {
        return start.getOwner().isConnectable();
    }

    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        if ((getLiner() == null) && (evt.getClickCount() == 2)) {
            willChange();
            final int index = splitSegment(p, ((float) (5.0F / view.getScaleFactor())));
            if (index != (-1)) {
                final org.jhotdraw.geom.path.BezierPath.Node newNode = getNode(index);
                fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public void redo() throws javax.swing.undo.CannotRedoException {
                        super.redo();
                        willChange();
                        addNode(index, newNode);
                        changed();
                    }

                    @java.lang.Override
                    public void undo() throws javax.swing.undo.CannotUndoException {
                        super.undo();
                        willChange();
                        removeNode(index);
                        changed();
                    }
                });
                changed();
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void setLiner(org.jhotdraw.draw.liner.Liner newValue) {
        org.jhotdraw.draw.liner.Liner oldValue = liner;
        this.liner = newValue;
    }

    @java.lang.Override
    public void setNode(int index, org.jhotdraw.geom.path.BezierPath.Node p) {
        if ((index != 0) && (index != (getNodeCount() - 1))) {
            if (getStartConnector() != null) {
                java.awt.geom.Point2D.Double start = getStartConnector().findStart(this);
                if (start != null) {
                    setStartPoint(start);
                }
            }
            if (getEndConnector() != null) {
                java.awt.geom.Point2D.Double end = getEndConnector().findEnd(this);
                if (end != null) {
                    setEndPoint(end);
                }
            }
        }
        super.setNode(index, p);
    }

    /* public void basicSetPoint(int index, Point2D.Double p) {
    if (index != 0 && index != getNodeCount() - 1) {
    if (getStartConnector() != null) {
    Point2D.Double start = getStartConnector().findStart(this);
    if(start != null) {
    basicSetStartPoint(start);
    }
    }
    if (getEndConnector() != null) {
    Point2D.Double end = getEndConnector().findEnd(this);
    if(end != null) {
    basicSetEndPoint(end);
    }
    }
    }
    super.basicSetPoint(index, p);
    }
     */
    @java.lang.Override
    public void lineout() {
        if (liner != null) {
            liner.lineout(this);
        }
    }

    /**
     * FIXME - Liner must work with API of LineConnection!
     */
    @java.lang.Override
    public org.jhotdraw.geom.path.BezierPath getBezierPath() {
        return path;
    }

    @java.lang.Override
    public org.jhotdraw.draw.liner.Liner getLiner() {
        return liner;
    }

    @java.lang.Override
    public void setStartPoint(java.awt.geom.Point2D.Double p) {
        setPoint(0, p);
    }

    @java.lang.Override
    public void setPoint(int index, java.awt.geom.Point2D.Double p) {
        setPoint(index, 0, p);
    }

    @java.lang.Override
    public void setEndPoint(java.awt.geom.Point2D.Double p) {
        setPoint(getNodeCount() - 1, p);
    }

    public void reverseConnection() {
        if ((startConnector != null) && (endConnector != null)) {
            handleDisconnect(startConnector, endConnector);
            org.jhotdraw.draw.connector.Connector tmpC = startConnector;
            startConnector = endConnector;
            endConnector = tmpC;
            java.awt.geom.Point2D.Double tmpP = getStartPoint();
            setStartPoint(getEndPoint());
            setEndPoint(tmpP);
            handleConnect(startConnector, endConnector);
            updateConnection();
        }
    }
}