/* @(#)BezierScaleHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A {@link Handle} which allows to interactively scale and rotate a BezierFigure.
 *
 * <p>Pressing the alt key or the shift key while manipulating the handle restricts the handle to
 * rotate the BezierFigure without scaling it.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class BezierScaleHandle extends org.jhotdraw.draw.handle.AbstractHandle {
    private java.awt.Point location;

    private java.lang.Object restoreData;

    private java.awt.geom.AffineTransform transform;

    private java.awt.geom.Point2D.Double center;

    private double startTheta;

    private double startLength;

    public BezierScaleHandle(org.jhotdraw.draw.figure.BezierFigure owner) {
        super(owner);
    }

    @java.lang.Override
    public boolean isCombinableWith(org.jhotdraw.draw.handle.Handle h) {
        return false;
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawCircle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.SCALE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.SCALE_HANDLE_STROKE_COLOR));
    }

    private org.jhotdraw.draw.figure.BezierFigure getBezierFigure() {
        return ((org.jhotdraw.draw.figure.BezierFigure) (getOwner()));
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double getDrawingLocation() {
        // find a nice place to put handle
        // Need to pick a place that will not overlap with point handle
        // and is internal to polygon
        int handlesize = getHandlesize();
        // Try for one handlesize step away from outermost toward center
        java.awt.geom.Point2D.Double outer = getBezierFigure().getOutermostPoint();
        java.awt.geom.Point2D.Double ctr = getBezierFigure().getCenter();
        double len = org.jhotdraw.geom.Geom.length(outer.x, outer.y, ctr.x, ctr.y);
        if (len == 0) {
            // best we can do?
            return new java.awt.geom.Point2D.Double(outer.x - (handlesize / 2), outer.y + (handlesize / 2));
        }
        double u = handlesize / len;
        if (u > 1.0) {
            // best we can do?
            return new java.awt.geom.Point2D.Double(((outer.x * 3) + ctr.x) / 4, ((outer.y * 3) + ctr.y) / 4);
        } else {
            return new java.awt.geom.Point2D.Double((outer.x * (1.0 - u)) + (ctr.x * u), (outer.y * (1.0 - u)) + (ctr.y * u));
        }
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        location = new java.awt.Point(anchor.x, anchor.y);
        restoreData = getBezierFigure().getTransformRestoreData();
        transform = new java.awt.geom.AffineTransform();
        center = getBezierFigure().getCenter();
        java.awt.geom.Point2D.Double anchorPoint = view.viewToDrawing(anchor);
        startTheta = org.jhotdraw.geom.Geom.angle(center.x, center.y, anchorPoint.x, anchorPoint.y);
        startLength = org.jhotdraw.geom.Geom.length(center.x, center.y, anchorPoint.x, anchorPoint.y);
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        location = new java.awt.Point(lead.x, lead.y);
        java.awt.geom.Point2D.Double leadPoint = view.viewToDrawing(lead);
        double stepTheta = org.jhotdraw.geom.Geom.angle(center.x, center.y, leadPoint.x, leadPoint.y);
        double stepLength = org.jhotdraw.geom.Geom.length(center.x, center.y, leadPoint.x, leadPoint.y);
        double scaleFactor = ((modifiersEx & (java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK)) != 0) ? 1.0 : stepLength / startLength;
        transform.setToIdentity();
        transform.translate(center.x, center.y);
        transform.scale(scaleFactor, scaleFactor);
        transform.rotate(stepTheta - startTheta);
        transform.translate(-center.x, -center.y);
        getOwner().willChange();
        getOwner().restoreTransformTo(restoreData);
        getOwner().transform(transform);
        getOwner().changed();
    }

    /* public  void scaleRotate(Point anchor, Polygon originalPolygon, Point p) {
    willChange();
    // use center to determine relative angles and lengths
    Point ctr = center(originalPolygon);
    double anchorLen = Geom.length(ctr.x, ctr.y, anchor.x, anchor.y);
    if (anchorLen > 0.0) {
    double newLen = Geom.length(ctr.x, ctr.y, p.x, p.y);
    double ratio = newLen / anchorLen;
    double anchorAngle = Math.atan2(anchor.y - ctr.y, anchor.x - ctr.x);
    double newAngle = Math.atan2(p.y - ctr.y, p.x - ctr.x);
    double rotation = newAngle - anchorAngle;
    int n = originalPolygon.npoints;
    int[] xs = new int[n];
    int[] ys = new int[n];
    for (int i = 0; i < n; ++i) {
    int x = originalPolygon.xpoints[i];
    int y = originalPolygon.ypoints[i];
    double l = Geom.length(ctr.x, ctr.y, x, y) * ratio;
    double a = Math.atan2(y - ctr.y, x - ctr.x) + rotation;
    xs[i] = (int)(ctr.x + l * Math.cos(a) + 0.5);
    ys[i] = (int)(ctr.y + l * Math.sin(a) + 0.5);
    }
    setInternalPolygon(new Polygon(xs, ys, n));
    }
    changed();
    }
     */
    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        view.getDrawing().fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), restoreData, getOwner().getTransformRestoreData()));
        location = null;
    }
}