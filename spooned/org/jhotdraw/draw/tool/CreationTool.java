/* @(#)CreationTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.figure.Figure;
/**
 * A {@link Tool} to create a new figure by drawing its bounds. The figure to be created is
 * specified by a prototype.
 *
 * <p>To create a figure using the {@code CreationTool}, the user does the following mouse gestures
 * on a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button over the DrawingView. This defines the start point of the Figure
 *       bounds.
 *   <li>Drag the mouse while keeping the mouse button pressed, and then release the mouse button.
 *       This defines the end point of the Figure bounds.
 * </ol>
 *
 * The CreationTool works well with most figures that fit into a rectangular shape or that concist
 * of a single straight line. For figures that need additional editing after these mouse gestures,
 * the use of a specialized creation tool is recommended. For example the TextTool allows to enter
 * the text into a TextFigure after the user has performed the mouse gestures.
 *
 * <p>Alltough the mouse gestures might be fitting for the creation of a connection, the
 * CreationTool is not suited for the creation of a ConnectionFigure. Use the ConnectionTool for
 * this type of figures instead.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Prototype</em><br>
 * The creation tool creates new figures by cloning a prototype figure object. That's the reason why
 * {@code Figure} extends the {@code Cloneable} interface. <br>
 * Prototype: {@link Figure}; Client: {@link CreationTool}. <hr>
 */
public class CreationTool extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.constrainer.CoordinateDataSupplier {
    private static final long serialVersionUID = 1L;

    /**
     * Attributes to be applied to the created ConnectionFigure. These attributes override the default
     * attributes of the DrawingEditor.
     */
    protected java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> prototypeAttributes;

    /**
     * A localized name for this tool. The presentationName is displayed by the UndoableEdit.
     */
    protected java.lang.String presentationName;

    /**
     * Treshold for which we create a larger shape of a minimal size.
     */
    protected java.awt.Dimension minimalSizeTreshold = new java.awt.Dimension(2, 2);

    /**
     * We set the figure to this minimal size, if it is smaller than the minimal size treshold.
     */
    protected java.awt.Dimension minimalSize = new java.awt.Dimension(40, 40);

    /**
     * The prototype for new figures.
     */
    protected org.jhotdraw.draw.figure.Figure prototype;

    /**
     * The created figure.
     */
    protected org.jhotdraw.draw.figure.Figure createdFigure;

    /**
     * If this is set to false, the CreationTool does not fire toolDone after a new Figure has been
     * created. This allows to create multiple figures consecutively.
     */
    private boolean isToolDoneAfterCreation = true;

    public CreationTool(java.lang.String prototypeClassName) {
        this(prototypeClassName, null, null);
    }

    public CreationTool(java.lang.String prototypeClassName, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        this(prototypeClassName, attributes, null);
    }

    public CreationTool(java.lang.String prototypeClassName, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name) {
        try {
            this.prototype = ((org.jhotdraw.draw.figure.Figure) (java.lang.Class.forName(prototypeClassName).getDeclaredConstructor().newInstance()));
        } catch (java.lang.Exception e) {
            java.lang.InternalError error = new java.lang.InternalError("Unable to create Figure from " + prototypeClassName);
            error.initCause(e);
            throw error;
        }
        this.prototypeAttributes = attributes;
        if (name == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            name = labels.getString("edit.createFigure.text");
        }
        this.presentationName = name;
    }

    /**
     * Creates a new instance with the specified prototype but without an attribute set. The
     * CreationTool clones this prototype each time a new Figure needs to be created. When a new
     * Figure is created, the CreationTool applies the default attributes from the DrawingEditor to
     * it.
     *
     * @param prototype
     * 		The prototype used to create a new Figure.
     */
    public CreationTool(org.jhotdraw.draw.figure.Figure prototype) {
        this(prototype, null, null);
    }

    /**
     * Creates a new instance with the specified prototype but without an attribute set. The
     * CreationTool clones this prototype each time a new Figure needs to be created. When a new
     * Figure is created, the CreationTool applies the default attributes from the DrawingEditor to
     * it, and then it applies the attributes to it, that have been supplied in this constructor.
     *
     * @param prototype
     * 		The prototype used to create a new Figure.
     * @param attributes
     * 		The CreationTool applies these attributes to the prototype after having
     * 		applied the default attributes from the DrawingEditor.
     */
    public CreationTool(org.jhotdraw.draw.figure.Figure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        this(prototype, attributes, null);
    }

    /**
     * Creates a new instance with the specified prototype and attribute set.
     *
     * @param prototype
     * 		The prototype used to create a new Figure.
     * @param attributes
     * 		The CreationTool applies these attributes to the prototype after having
     * 		applied the default attributes from the DrawingEditor.
     * @param name
     * 		The name parameter is currently not used.
     * @deprecated This constructor might go away, because the name parameter is not used.
     */
    @java.lang.Deprecated
    public CreationTool(org.jhotdraw.draw.figure.Figure prototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes, java.lang.String name) {
        this.prototype = prototype;
        this.prototypeAttributes = attributes;
        if (name == null) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            name = labels.getString("edit.createFigure.text");
        }
        this.presentationName = name;
    }

    public org.jhotdraw.draw.figure.Figure getPrototype() {
        return prototype;
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
            if (createdFigure instanceof org.jhotdraw.draw.figure.CompositeFigure) {
                ((org.jhotdraw.draw.figure.CompositeFigure) (createdFigure)).layout(getView().getScaleFactor());
            }
            createdFigure = null;
        }
        if ((getView().getConstrainer() != null) && (getView().getConstrainer() instanceof org.jhotdraw.draw.constrainer.CoordinateDataReceiver receiver)) {
            receiver.clearCoordinateSupplier();
        }
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        super.mousePressed(evt);
        if (getView() == null) {
            return;
        }
        getView().clearSelection();
        createdFigure = createFigure();
        java.awt.geom.Point2D.Double p = constrainPoint(viewToDrawing(anchor), createdFigure);
        anchor.x = evt.getX();
        anchor.y = evt.getY();
        createdFigure.setBounds(p, p);
        getDrawing().add(createdFigure);
        fireFigureCreated(createdFigure);
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        if (createdFigure != null) {
            java.awt.geom.Point2D.Double p = constrainPoint(new java.awt.Point(evt.getX(), evt.getY()), createdFigure);
            createdFigure.willChange();
            createdFigure.setBounds(constrainPoint(new java.awt.Point(anchor.x, anchor.y), createdFigure), p);
            createdFigure.changed();
        }
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        if (createdFigure != null) {
            java.awt.geom.Rectangle2D.Double bounds = createdFigure.getBounds();
            if ((bounds.width == 0) && (bounds.height == 0)) {
                getDrawing().remove(createdFigure);
                if (isToolDoneAfterCreation()) {
                    fireToolDone();
                }
            } else {
                if ((java.lang.Math.abs(anchor.x - evt.getX()) < minimalSizeTreshold.width) && (java.lang.Math.abs(anchor.y - evt.getY()) < minimalSizeTreshold.height)) {
                    createdFigure.willChange();
                    createdFigure.setBounds(constrainPoint(new java.awt.Point(anchor.x, anchor.y), createdFigure), constrainPoint(new java.awt.Point(anchor.x + ((int) (java.lang.Math.max(bounds.width, minimalSize.width))), anchor.y + ((int) (java.lang.Math.max(bounds.height, minimalSize.height)))), createdFigure));
                    createdFigure.changed();
                }
                if (createdFigure instanceof org.jhotdraw.draw.figure.CompositeFigure) {
                    ((org.jhotdraw.draw.figure.CompositeFigure) (createdFigure)).layout(getView().getScaleFactor());
                }
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
                java.awt.Rectangle r = new java.awt.Rectangle(anchor.x, anchor.y, 0, 0);
                r.add(evt.getX(), evt.getY());
                maybeFireBoundsInvalidated(r);
                creationFinished(createdFigure);
                createdFigure = null;
            }
        } else if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    protected org.jhotdraw.draw.figure.Figure createFigure() {
        org.jhotdraw.draw.figure.Figure f = prototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        if (prototypeAttributes != null) {
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : prototypeAttributes.entrySet()) {
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

    /**
     * This method allows subclasses to do perform additonal user interactions after the new figure
     * has been created. The implementation of this class just invokes fireToolDone.
     */
    protected void creationFinished(org.jhotdraw.draw.figure.Figure createdFigure) {
        if (createdFigure.isSelectable()) {
            getView().addToSelection(createdFigure);
        }
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

    @java.lang.Override
    public void updateCursor(org.jhotdraw.draw.DrawingView view, java.awt.Point p) {
        if (view.isEnabled()) {
            view.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        } else {
            view.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.CoordinateData getConstrainerCoordinates(int before, int after) {
        if (this.createdFigure == null) {
            return null;
        }
        org.jhotdraw.draw.tool.var b = this.createdFigure.getBounds();
        java.util.List<java.awt.geom.Point2D.Double> list = new java.util.ArrayList<>();
        list.add(new java.awt.geom.Point2D.Double(b.x, b.y));
        if ((b.width != 0) || (b.height != 0)) {
            list.add(new java.awt.geom.Point2D.Double(b.x + b.width, b.y + b.height));
        }
        return new org.jhotdraw.draw.constrainer.CoordinateData(list.toArray(java.awt.geom.Point2D.Double[]::new), list.size());
    }
}