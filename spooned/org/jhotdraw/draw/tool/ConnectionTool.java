/* @(#)ConnectionTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.figure.ConnectionFigure;
/**
 * A tool to create a connection between two figures. The {@link ConnectionFigure} to be created is
 * specified by a prototype. The location of the start and end points are controlled by {@link Connector}s.
 *
 * <p>To create a connection using the ConnectionTool, the user does the following mouse gestures on
 * a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button inside of a Figure. If the ConnectionTool can find a Connector at
 *       this location, it uses it as the starting point for the connection.
 *   <li>Drag the mouse while keeping the mouse button pressed, and then release the mouse button.
 *       This defines the end point of the connection. If the ConnectionTool finds a Connector at
 *       this location, it uses it as the end point of the connection and creates a
 *       ConnectionFigure.
 * </ol>
 *
 * <hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * Two figures can be connected using a connection figure. The location of the start or end point of
 * the connection is handled by a connector object at each connected figure.<br>
 * Contract: {@link org.jhotdraw.draw.Figure}, {@link org.jhotdraw.draw.ConnectionFigure}, {@link org.jhotdraw.draw.connector.Connector}, {@link org.jhotdraw.draw.tool.ConnectionTool}.
 */
public class ConnectionTool extends org.jhotdraw.draw.tool.AbstractTool {
    private static final long serialVersionUID = 1L;

    /**
     * FIXME - The ANCHOR_WIDTH value must be retrieved from the DrawingEditor
     */
    private static final int ANCHOR_WIDTH = 6;

    /**
     * Attributes to be applied to the created ConnectionFigure. These attributes override the default
     * attributes of the DrawingEditor.
     */
    private java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> prototypeAttributes;

    /**
     * The Connector at the start point of the connection.
     */
    protected org.jhotdraw.draw.connector.Connector startConnector;

    /**
     * The Connector at the end point of the connection.
     */
    protected org.jhotdraw.draw.connector.Connector endConnector;

    /**
     * The created figure.
     */
    protected org.jhotdraw.draw.figure.ConnectionFigure createdFigure;

    /**
     * the prototypical figure that is used to create new connections.
     */
    protected org.jhotdraw.draw.figure.ConnectionFigure prototype;

    /**
     * The figure for which we enabled drawing of connectors.
     */
    protected org.jhotdraw.draw.figure.Figure targetFigure;

    protected java.util.Collection<org.jhotdraw.draw.connector.Connector> connectors = java.util.Collections.emptyList();

    /**
     * A localized name for this tool. The presentationName is displayed by the UndoableEdit.
     */
    private java.lang.String presentationName;

    /**
     * If this is set to false, the CreationTool does not fire toolDone after a new Figure has been
     * created. This allows to create multiple figures consecutively.
     */
    private boolean isToolDoneAfterCreation = true;

    public ConnectionTool(org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        this(prototype, null, null);
    }

    public ConnectionTool(org.jhotdraw.draw.figure.ConnectionFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        this(prototype, attributes, null);
    }

    public ConnectionTool(org.jhotdraw.draw.figure.ConnectionFigure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String presentationName) {
        this.prototype = prototype;
        this.prototypeAttributes = attributes;
        if (presentationName == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            presentationName = labels.getString("edit.createConnectionFigure.text");
        }
        this.presentationName = presentationName;
    }

    public ConnectionTool(java.lang.String prototypeClassName) {
        this(prototypeClassName, null, null);
    }

    public ConnectionTool(java.lang.String prototypeClassName, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String presentationName) {
        try {
            this.prototype = ((org.jhotdraw.draw.figure.ConnectionFigure) (java.lang.Class.forName(prototypeClassName).newInstance()));
        } catch (java.lang.Exception e) {
            java.lang.InternalError error = new java.lang.InternalError("Unable to create ConnectionFigure from " + prototypeClassName);
            error.initCause(e);
            throw error;
        }
        this.prototypeAttributes = attributes;
        if (presentationName == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            presentationName = labels.getString("edit.createConnectionFigure.text");
        }
        this.presentationName = presentationName;
    }

    public org.jhotdraw.draw.figure.ConnectionFigure getPrototype() {
        return prototype;
    }

    protected int getAnchorWidth() {
        return org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH;
    }

    /**
     * This method is called on the Figure, onto which the user wants to start a new connection.
     *
     * @param f
     * 		The ConnectionFigure.
     * @param startConnector
     * 		The Connector of the start Figure.
     * @return True, if a connection can be made.
     */
    protected boolean canConnect(org.jhotdraw.draw.figure.ConnectionFigure f, org.jhotdraw.draw.connector.Connector startConnector) {
        return f.canConnect(startConnector);
    }

    /**
     * This method is called on the Figure, onto which the user wants to end a new connection.
     *
     * @param f
     * 		The ConnectionFigure.
     * @param startConnector
     * 		The Connector of the start Figure.
     * @param endConnector
     * 		The Connector of the end Figure.
     * @return True, if a connection can be made.
     */
    protected boolean canConnect(org.jhotdraw.draw.figure.ConnectionFigure f, org.jhotdraw.draw.connector.Connector startConnector, org.jhotdraw.draw.connector.Connector endConnector) {
        return f.canConnect(startConnector, endConnector);
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        repaintConnectors(evt);
    }

    /**
     * Updates the list of connectors that we draw when the user moves or drags the mouse over a
     * figure to which can connect.
     */
    public void repaintConnectors(java.awt.event.MouseEvent evt) {
        java.awt.geom.Rectangle2D.Double invalidArea = null;
        java.awt.geom.Point2D.Double targetPoint = viewToDrawing(new java.awt.Point(evt.getX(), evt.getY()));
        org.jhotdraw.draw.figure.Figure aFigure = getDrawing().findFigureExcept(targetPoint, createdFigure);
        if ((aFigure != null) && (!aFigure.isConnectable())) {
            aFigure = null;
        }
        if (targetFigure != aFigure) {
            for (org.jhotdraw.draw.connector.Connector c : connectors) {
                if (invalidArea == null) {
                    invalidArea = c.getDrawingArea();
                } else {
                    invalidArea.add(c.getDrawingArea());
                }
            }
            targetFigure = aFigure;
            if (targetFigure != null) {
                connectors = targetFigure.getConnectors(getPrototype());
                for (org.jhotdraw.draw.connector.Connector c : connectors) {
                    if (invalidArea == null) {
                        invalidArea = c.getDrawingArea();
                    } else {
                        invalidArea.add(c.getDrawingArea());
                    }
                }
            }
        }
        if (invalidArea != null) {
            getView().getComponent().repaint(getView().drawingToView(invalidArea));
        }
    }

    /**
     * Manipulates connections in a context dependent way. If the mouse down hits a figure start a new
     * connection. If the mousedown hits a connection split a segment or join two segments.
     */
    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        super.mousePressed(evt);
        getView().clearSelection();
        java.awt.geom.Point2D.Double startPoint = viewToDrawing(anchor);
        org.jhotdraw.draw.figure.Figure startFigure = getDrawing().findFigure(startPoint, getView().getScaleFactor());
        startConnector = (startFigure == null) ? null : startFigure.findConnector(startPoint, prototype);
        if ((startConnector != null) && canConnect(prototype, startConnector)) {
            java.awt.geom.Point2D.Double anchor = startConnector.getAnchor();
            createdFigure = createFigure();
            createdFigure.setStartPoint(anchor);
            createdFigure.setEndPoint(anchor);
            getDrawing().add(createdFigure);
            java.awt.Rectangle r = new java.awt.Rectangle(getView().drawingToView(anchor));
            r.grow(org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH);
            fireAreaInvalidated(r);
        } else {
            startConnector = null;
            createdFigure = null;
        }
        endConnector = null;
    }

    /**
     * Adjust the created connection.
     */
    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        repaintConnectors(e);
        if (createdFigure != null) {
            createdFigure.willChange();
            java.awt.geom.Point2D.Double endPoint = viewToDrawing(new java.awt.Point(e.getX(), e.getY()));
            if (getView().getConstrainer() != null) {
                endPoint = getView().getConstrainer().constrainPoint(endPoint, createdFigure);
            }
            org.jhotdraw.draw.figure.Figure endFigure = getDrawing().findFigureExcept(endPoint, createdFigure);
            endConnector = (endFigure == null) ? null : endFigure.findConnector(endPoint, prototype);
            if ((endConnector != null) && canConnect(createdFigure, startConnector, endConnector)) {
                endPoint = endConnector.getAnchor();
            }
            java.awt.Rectangle r = new java.awt.Rectangle(getView().drawingToView(createdFigure.getEndPoint()));
            createdFigure.setEndPoint(endPoint);
            r.add(getView().drawingToView(endPoint));
            r.grow(org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH + 2, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH + 2);
            getView().getComponent().repaint(r);
            createdFigure.changed();
        }
    }

    /**
     * Connects the figures if the mouse is released over another figure.
     */
    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if ((((createdFigure != null) && (startConnector != null)) && (endConnector != null)) && createdFigure.canConnect(startConnector, endConnector)) {
            createdFigure.willChange();
            createdFigure.setStartConnector(startConnector);
            createdFigure.setEndConnector(endConnector);
            createdFigure.updateConnection();
            createdFigure.changed();
            final org.jhotdraw.draw.figure.Figure addedFigure = createdFigure;
            final org.jhotdraw.draw.Drawing addedDrawing = getDrawing();
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
                    addedDrawing.add(addedFigure);
                }
            });
            targetFigure = null;
            java.awt.geom.Point2D.Double anchor = startConnector.getAnchor();
            java.awt.Rectangle r = new java.awt.Rectangle(getView().drawingToView(anchor));
            r.grow(org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH);
            fireAreaInvalidated(r);
            anchor = endConnector.getAnchor();
            r = new java.awt.Rectangle(getView().drawingToView(anchor));
            r.grow(org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH);
            fireAreaInvalidated(r);
            startConnector = endConnector = null;
            org.jhotdraw.draw.figure.Figure finishedFigure = createdFigure;
            createdFigure = null;
            creationFinished(finishedFigure);
        } else if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        if (createdFigure != null) {
            getDrawing().remove(createdFigure);
            createdFigure = null;
        }
        targetFigure = null;
        startConnector = endConnector = null;
        super.deactivate(editor);
    }

    /**
     * Creates the ConnectionFigure. By default the figure prototype is cloned.
     */
    @java.lang.SuppressWarnings("unchecked")
    protected org.jhotdraw.draw.figure.ConnectionFigure createFigure() {
        org.jhotdraw.draw.figure.ConnectionFigure f = ((org.jhotdraw.draw.figure.ConnectionFigure) (prototype.clone()));
        getEditor().applyDefaultAttributesTo(f);
        if (prototypeAttributes != null) {
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : prototypeAttributes.entrySet()) {
                f.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
        }
        return f;
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        java.awt.Graphics2D gg = ((java.awt.Graphics2D) (g.create()));
        gg.transform(getView().getDrawingToViewTransform());
        if (targetFigure != null) {
            for (org.jhotdraw.draw.connector.Connector c : targetFigure.getConnectors(getPrototype())) {
                c.draw(gg);
            }
        }
        if (createdFigure != null) {
            createdFigure.draw(gg);
            java.awt.Point p = getView().drawingToView(createdFigure.getStartPoint());
            java.awt.geom.Ellipse2D.Double e = new java.awt.geom.Ellipse2D.Double(p.x - (org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH / 2), p.y - (org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH / 2), org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH);
            g.setColor(java.awt.Color.GREEN);
            g.fill(e);
            g.setColor(java.awt.Color.BLACK);
            g.draw(e);
            p = getView().drawingToView(createdFigure.getEndPoint());
            e = new java.awt.geom.Ellipse2D.Double(p.x - (org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH / 2), p.y - (org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH / 2), org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH, org.jhotdraw.draw.tool.ConnectionTool.ANCHOR_WIDTH);
            g.setColor(java.awt.Color.GREEN);
            g.fill(e);
            g.setColor(java.awt.Color.BLACK);
            g.draw(e);
        }
        gg.dispose();
    }

    /**
     * This method allows subclasses to do perform additonal user interactions after the new figure
     * has been created. The implementation of this class just invokes fireToolDone.
     */
    protected void creationFinished(org.jhotdraw.draw.figure.Figure createdFigure) {
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    /**
     * If this is set to false, the CreationTool does not fire toolDone after a new Figure has been
     * created. This allows to create multiple figures consecutively.
     */
    public void setToolDoneAfterCreation(boolean newValue) {
        boolean oldValue = isToolDoneAfterCreation;
        isToolDoneAfterCreation = newValue;
    }

    /**
     * Returns true, if this tool fires toolDone immediately after a new figure has been created.
     */
    public boolean isToolDoneAfterCreation() {
        return isToolDoneAfterCreation;
    }
}