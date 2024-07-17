/* @(#)BezierFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
import org.jhotdraw.draw.connector.ChopBezierConnector;
import org.jhotdraw.geom.path.BezierPath;
/**
 * A {@link Figure} which draws an opened or a closed bezier path.
 *
 * <p>A bezier figure can be used to draw arbitrary shapes using a {@link BezierPath}. It can be
 * used to draw an open path or a closed shape.
 *
 * <p>A BezierFigure can have straight path segments and curved segments. A straight path segment
 * can be added by clicking on the drawing area. Curved segments can be added by dragging the mouse
 * pointer over the drawing area.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Decorator</em><br>
 * The start and end point of a {@code BezierFigure} can be decorated with a line decoration.<br>
 * Component: {@link BezierFigure}; Decorator: {@link org.jhotdraw.draw.decoration.LineDecoration}.
 * <hr>
 *
 * @see org.jhotdraw.geom.path.BezierPath
 */
public class BezierFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    protected org.jhotdraw.geom.path.BezierPath path;

    /**
     * The cappedPath BezierPath is derived from variable path. We cache it to increase the drawing
     * speed of the figure. The factor could influence the cappedPath due to Arrow sizes.
     */
    private transient org.jhotdraw.geom.path.BezierPath cappedPath;

    private transient double cappedPathFactor;

    /**
     * Creates an empty <code>BezierFigure</code>, for example without any <code>BezierPath.Node
     * </code>s. The BezierFigure will not draw anything, if at least two nodes are added to it. The
     * <code>BezierPath</code> created by this constructor is not closed.
     */
    public BezierFigure() {
        this(false);
    }

    /**
     * Creates an empty BezierFigure, for example without any <code>BezierPath.Node</code>s. The
     * BezierFigure will not draw anything, unless at least two nodes are added to it.
     *
     * @param isClosed
     * 		Specifies whether the <code>BezierPath</code> shall be closed.
     */
    public BezierFigure(boolean isClosed) {
        path = new org.jhotdraw.geom.path.BezierPath();
        attr().set(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED, isClosed);
    }

    /**
     * Returns the Figures connector for the specified location. By default a {@link ChopBezierConnector} is returned.
     */
    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopBezierConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStart) {
        return new org.jhotdraw.draw.connector.ChopBezierConnector(this);
    }

    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        if (isClosed()) {
            double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
            if (grow == 0.0) {
                g.draw(path);
            } else {
                org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
                g.draw(gs.createStrokedShape(path));
            }
        } else {
            g.draw(getCappedPath(org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)));
        }
        drawCaps(g);
    }

    protected void drawCaps(java.awt.Graphics2D g) {
        if (getNodeCount() > 1) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION) != null) {
                org.jhotdraw.geom.path.BezierPath cp = getCappedPath(org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
                java.awt.geom.Point2D.Double p1 = path.get(0, 0);
                java.awt.geom.Point2D.Double p2 = cp.get(0, 0);
                if (p2.equals(p1)) {
                    p2 = path.get(1, 0);
                }
                attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION).draw(g, this, p1, p2);
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION) != null) {
                org.jhotdraw.geom.path.BezierPath cp = getCappedPath(org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
                java.awt.geom.Point2D.Double p1 = path.get(path.size() - 1, 0);
                java.awt.geom.Point2D.Double p2 = cp.get(path.size() - 1, 0);
                if (p2.equals(p1)) {
                    p2 = path.get(path.size() - 2, 0);
                }
                attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION).draw(g, this, p1, p2);
            }
        }
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        if (isClosed() || attr().get(org.jhotdraw.draw.AttributeKeys.UNCLOSED_PATH_FILLED)) {
            double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
            if (grow == 0.0) {
                g.fill(path);
            } else {
                org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
                g.fill(gs.createStrokedShape(path));
            }
        }
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        double tolerance = java.lang.Math.max(1.0F, 2 * org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleDenominator));
        if (isClosed() || ((attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR) != null) && attr().get(org.jhotdraw.draw.AttributeKeys.UNCLOSED_PATH_FILLED))) {
            if (path.contains(p)) {
                return true;
            }
            double grow = tolerance;
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scaleDenominator) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
            if (gs.createStrokedShape(path).contains(p)) {
                return true;
            } else if (isClosed()) {
                return false;
            }
        }
        if (!isClosed()) {
            if (getCappedPath(scaleDenominator).outlineContains(p, tolerance)) {
                return true;
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION) != null) {
                org.jhotdraw.geom.path.BezierPath cp = getCappedPath(scaleDenominator);
                java.awt.geom.Point2D.Double p1 = path.get(0, 0);
                java.awt.geom.Point2D.Double p2 = cp.get(0, 0);
                // FIXME - Check here, if caps path contains the point
                if (org.jhotdraw.geom.Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, p.x, p.y, tolerance)) {
                    return true;
                }
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION) != null) {
                org.jhotdraw.geom.path.BezierPath cp = getCappedPath(scaleDenominator);
                java.awt.geom.Point2D.Double p1 = path.get(path.size() - 1, 0);
                java.awt.geom.Point2D.Double p2 = cp.get(path.size() - 1, 0);
                // FIXME - Check here, if caps path contains the point
                if (org.jhotdraw.geom.Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, p.x, p.y, tolerance)) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
        switch (detailLevel % 2) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.BezierOutlineHandle(this));
                for (int i = 0, n = path.size(); i < n; i++) {
                    handles.add(new org.jhotdraw.draw.handle.BezierNodeHandle(this, i));
                }
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                handles.add(new org.jhotdraw.draw.handle.BezierScaleHandle(this));
                break;
        }
        return handles;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Rectangle2D.Double bounds = path.getBounds2D();
        return bounds;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double factor) {
        java.awt.geom.Rectangle2D.Double r = super.getDrawingArea(factor);
        if (getNodeCount() > 1) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION) != null) {
                java.awt.geom.Point2D.Double p1 = getPoint(0, 0);
                java.awt.geom.Point2D.Double p2 = getPoint(1, 0);
                r.add(attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION).getDrawingArea(this, p1, p2, factor));
            }
            if (attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION) != null) {
                java.awt.geom.Point2D.Double p1 = getPoint(getNodeCount() - 1, 0);
                java.awt.geom.Point2D.Double p2 = getPoint(getNodeCount() - 2, 0);
                r.add(attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION).getDrawingArea(this, p1, p2, factor));
            }
        }
        return r;
    }

    @java.lang.Override
    protected void validate() {
        super.validate();
        path.invalidatePath();
        cappedPath = null;
    }

    /**
     * Returns a clone of the bezier path of this figure.
     */
    public org.jhotdraw.geom.path.BezierPath getBezierPath() {
        return path.clone();
    }

    public void setBezierPath(org.jhotdraw.geom.path.BezierPath newValue) {
        path = newValue.clone();
        this.setClosed(newValue.isClosed());
    }

    public java.awt.geom.Point2D.Double getPointOnPath(double relative, double flatness) {
        return path.getPointOnPath(relative, flatness);
    }

    public boolean isClosed() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED);
    }

    public void setClosed(boolean newValue) {
        attr().set(org.jhotdraw.draw.AttributeKeys.PATH_CLOSED, newValue);
        setConnectable(newValue);
    }

    @java.lang.Override
    protected <T> void fireAttributeChanged(org.jhotdraw.draw.AttributeKey<T> attribute, T oldValue, T newValue) {
        if (attribute == org.jhotdraw.draw.AttributeKeys.PATH_CLOSED) {
            path.setClosed(((java.lang.Boolean) (newValue)));
        } else if (attribute == org.jhotdraw.draw.AttributeKeys.WINDING_RULE) {
            path.setWindingRule(newValue == org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD ? java.awt.geom.Path2D.Double.WIND_EVEN_ODD : java.awt.geom.Path2D.Double.WIND_NON_ZERO);
        }
        invalidate();
        super.fireAttributeChanged(attribute, oldValue, newValue);
    }

    /**
     * Sets the location of the first and the last <code>BezierPath.Node</code> of the BezierFigure.
     * If the BezierFigure has not at least two nodes, nodes are added to the figure until the
     * BezierFigure has at least two nodes.
     */
    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        setStartPoint(anchor);
        setEndPoint(lead);
        invalidate();
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        path.transform(tx);
        invalidate();
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        path.invalidatePath();
        cappedPath = null;
    }

    /**
     * Returns a path which is cappedPath at the ends, to prevent it from drawing under the end caps.
     */
    protected org.jhotdraw.geom.path.BezierPath getCappedPath(double factor) {
        if ((cappedPath == null) || (factor != cappedPathFactor)) {
            cappedPath = path.clone();
            cappedPathFactor = factor;
            if (isClosed()) {
                cappedPath.setClosed(true);
            } else if (cappedPath.size() > 1) {
                if (attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION) != null) {
                    org.jhotdraw.geom.path.BezierPath.Node p0 = cappedPath.nodes().get(0);
                    org.jhotdraw.geom.path.BezierPath.Node p1 = cappedPath.nodes().get(1);
                    java.awt.geom.Point2D.Double pp;
                    if ((p0.getMask() & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) {
                        pp = p0.getControlPoint(2);
                    } else if ((p1.getMask() & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) {
                        pp = p1.getControlPoint(1);
                    } else {
                        pp = p1.getControlPoint(0);
                    }
                    double radius = attr().get(org.jhotdraw.draw.AttributeKeys.START_DECORATION).getDecorationRadius(this, factor);
                    double lineLength = org.jhotdraw.geom.Geom.length(p0.getControlPoint(0), pp);
                    cappedPath.set(0, 0, org.jhotdraw.geom.Geom.cap(pp, p0.getControlPoint(0), -java.lang.Math.min(radius, lineLength)));
                }
                if (attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION) != null) {
                    org.jhotdraw.geom.path.BezierPath.Node p0 = cappedPath.nodes().get(cappedPath.size() - 1);
                    org.jhotdraw.geom.path.BezierPath.Node p1 = cappedPath.nodes().get(cappedPath.size() - 2);
                    java.awt.geom.Point2D.Double pp;
                    if ((p0.getMask() & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) {
                        pp = p0.getControlPoint(1);
                    } else if ((p1.getMask() & org.jhotdraw.geom.path.BezierPath.C2_MASK) != 0) {
                        pp = p1.getControlPoint(2);
                    } else {
                        pp = p1.getControlPoint(0);
                    }
                    double radius = attr().get(org.jhotdraw.draw.AttributeKeys.END_DECORATION).getDecorationRadius(this, factor);
                    double lineLength = org.jhotdraw.geom.Geom.length(p0.getControlPoint(0), pp);
                    cappedPath.set(cappedPath.size() - 1, 0, org.jhotdraw.geom.Geom.cap(pp, p0.getControlPoint(0), -java.lang.Math.min(radius, lineLength)));
                }
                cappedPath.invalidatePath();
            }
        }
        return cappedPath;
    }

    public void layout() {
    }

    /**
     * Adds a control point.
     */
    public void addNode(org.jhotdraw.geom.path.BezierPath.Node p) {
        addNode(getNodeCount(), p);
    }

    /**
     * Adds a node to the list of points.
     */
    public void addNode(final int index, org.jhotdraw.geom.path.BezierPath.Node p) {
        path.add(index, p);
        invalidate();
    }

    /**
     * Sets a control point.
     */
    public void setNode(int index, org.jhotdraw.geom.path.BezierPath.Node p) {
        path.set(index, p);
        invalidate();
    }

    /**
     * Gets a control point.
     */
    public org.jhotdraw.geom.path.BezierPath.Node getNode(int index) {
        return ((org.jhotdraw.geom.path.BezierPath.Node) (path.nodes().get(index).clone()));
    }

    /**
     * Convenience method for getting the point coordinate of the first control point of the specified
     * node.
     */
    public java.awt.geom.Point2D.Double getPoint(int index) {
        return path.nodes().get(index).getControlPoint(0);
    }

    /**
     * Gets the point coordinate of a control point.
     */
    public java.awt.geom.Point2D.Double getPoint(int index, int coord) {
        return path.nodes().get(index).getControlPoint(coord);
    }

    /**
     * Sets the point coordinate of control point 0 at the specified node.
     */
    public void setPoint(int index, java.awt.geom.Point2D.Double p) {
        org.jhotdraw.geom.path.BezierPath.Node node = path.nodes().get(index);
        double dx = p.x - node.x[0];
        double dy = p.y - node.y[0];
        for (int i = 0; i < node.x.length; i++) {
            node.x[i] += dx;
            node.y[i] += dy;
        }
        invalidate();
    }

    /**
     * Sets the point coordinate of a control point.
     */
    public void setPoint(int index, int ctrlPntIndex, java.awt.geom.Point2D.Double p) {
        org.jhotdraw.geom.path.BezierPath.Node cp = new org.jhotdraw.geom.path.BezierPath.Node(path.nodes().get(index));
        cp.setControlPoint(ctrlPntIndex, p);
        setNode(index, cp);
    }

    /**
     * Convenience method for setting the point coordinate of the start point. If the BezierFigure has
     * not at least two nodes, nodes are added to the figure until the BezierFigure has at least two
     * nodes.
     */
    public void setStartPoint(java.awt.geom.Point2D.Double p) {
        // Add two nodes if we haven't at least two nodes
        for (int i = getNodeCount(); i < 2; i++) {
            addNode(0, new org.jhotdraw.geom.path.BezierPath.Node(p.x, p.y));
        }
        setPoint(0, p);
    }

    /**
     * Convenience method for setting the point coordinate of the end point. If the BezierFigure has
     * not at least two nodes, nodes are added to the figure until the BezierFigure has at least two
     * nodes.
     */
    public void setEndPoint(java.awt.geom.Point2D.Double p) {
        // Add two nodes if we haven't at least two nodes
        for (int i = getNodeCount(); i < 2; i++) {
            addNode(0, new org.jhotdraw.geom.path.BezierPath.Node(p.x, p.y));
        }
        setPoint(getNodeCount() - 1, p);
    }

    /**
     * Convenience method for getting the start point.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double getStartPoint() {
        return getPoint(0, 0);
    }

    /**
     * Convenience method for getting the end point.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double getEndPoint() {
        return getPoint(getNodeCount() - 1, 0);
    }

    /**
     * Finds a control point index. Returns -1 if no control point could be found. FIXME - Move this
     * to BezierPath
     */
    public int findNode(java.awt.geom.Point2D.Double p) {
        org.jhotdraw.geom.path.BezierPath tp = path;
        for (int i = 0; i < tp.size(); i++) {
            org.jhotdraw.geom.path.BezierPath.Node p2 = tp.nodes().get(i);
            if ((p2.x[0] == p.x) && (p2.y[0] == p.y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the segment of the polyline that is hit by the given Point2D.Double.
     *
     * @param find
     * 		a Point on the bezier path
     * @param tolerance
     * 		a tolerance, tolerance should take into account the line width, plus 2 divided
     * 		by the zoom factor.
     * @return the index of the segment or -1 if no segment was hit.
     */
    public int findSegment(java.awt.geom.Point2D.Double find, double tolerance) {
        return path.findSegment(find, tolerance);
    }

    /**
     * Joins two segments into one if the given Point2D.Double hits a node of the polyline.
     *
     * @return true if the two segments were joined.
     * @param join
     * 		a Point at a node on the bezier path
     * @param tolerance
     * 		a tolerance, tolerance should take into account the line width, plus 2 divided
     * 		by the zoom factor.
     */
    public boolean joinSegments(java.awt.geom.Point2D.Double join, double tolerance) {
        int i = findSegment(join, tolerance);
        if ((i != (-1)) && (i > 1)) {
            removeNode(i);
            return true;
        }
        return false;
    }

    /**
     * Splits the segment at the given Point2D.Double if a segment was hit.
     *
     * @return the index of the segment or -1 if no segment was hit.
     * @param split
     * 		a Point on (or near) a line segment on the bezier path
     * @param tolerance
     * 		a tolerance, tolerance should take into account the line width, plus 2 divided
     * 		by the zoom factor.
     */
    public int splitSegment(java.awt.geom.Point2D.Double split, double tolerance) {
        int i = findSegment(split, tolerance);
        if (i != (-1)) {
            addNode(i + 1, new org.jhotdraw.geom.path.BezierPath.Node(split));
        }
        return i + 1;
    }

    /**
     * Removes the Node at the specified index.
     */
    public org.jhotdraw.geom.path.BezierPath.Node removeNode(int index) {
        return path.remove(index);
    }

    /**
     * Removes the Point2D.Double at the specified index.
     */
    protected void removeAllNodes() {
        path.clear();
    }

    /**
     * Gets the node count.
     */
    public int getNodeCount() {
        return path.size();
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.BezierFigure clone() {
        org.jhotdraw.draw.figure.BezierFigure that = ((org.jhotdraw.draw.figure.BezierFigure) (super.clone()));
        that.path = this.path.clone();
        that.invalidate();
        return that;
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        path.setTo(((org.jhotdraw.geom.path.BezierPath) (geometry)));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return path.clone();
    }

    public java.awt.geom.Point2D.Double chop(java.awt.geom.Point2D.Double p) {
        if (isClosed()) {
            double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
            if (grow == 0.0) {
                return path.chop(p);
            } else {
                org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this)) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
                return org.jhotdraw.geom.Geom.chop(gs.createStrokedShape(path), p);
            }
        } else {
            return path.chop(p);
        }
    }

    public java.awt.geom.Point2D.Double getCenter() {
        return path.getCenter();
    }

    public java.awt.geom.Point2D.Double getOutermostPoint() {
        return path.nodes().get(path.indexOfOutermostNode()).getControlPoint(0);
    }

    /**
     * Joins two segments into one if the given Point2D.Double hits a node of the polyline.
     *
     * @return true if the two segments were joined.
     */
    public int joinSegments(java.awt.geom.Point2D.Double join, float tolerance) {
        return path.joinSegments(join, tolerance);
    }

    /**
     * Splits the segment at the given Point2D.Double if a segment was hit.
     *
     * @return the index of the segment or -1 if no segment was hit.
     */
    public int splitSegment(java.awt.geom.Point2D.Double split, float tolerance) {
        return path.splitSegment(split, tolerance);
    }

    /**
     * Handles a mouse click.
     */
    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        if ((evt.getClickCount() == 2) && ((view.getHandleDetailLevel() % 2) == 0)) {
            willChange();
            final int index = splitSegment(p, 5.0F / view.getScaleFactor());
            if (index != (-1)) {
                final org.jhotdraw.geom.path.BezierPath.Node newNode = getNode(index);
                fireUndoableEditHappened(new javax.swing.undo.AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @java.lang.Override
                    public java.lang.String getPresentationName() {
                        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                        return labels.getString("edit.bezierPath.splitSegment.text");
                    }

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
                evt.consume();
                return true;
            }
        }
        return false;
    }
}