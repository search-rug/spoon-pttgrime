/* @(#)TriangleFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
import org.jhotdraw.draw.handle.OrientationHandle;
/**
 * Implements a {@link Figure} with a triangular shape.
 *
 * <p>The tip of the triangle points in the direction specified by the attribute {@link org.jhotdraw.draw.AttributeKeys#ORIENTATION}.
 *
 * <p>This figure creates a {@link OrientationHandle} which allows to interactively change the
 * orientation of the triangle.
 */
public class TriangleFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    /**
     * The bounds of the triangle figure.
     */
    private java.awt.geom.Rectangle2D.Double rectangle;

    public TriangleFigure() {
        this(0, 0, 0, 0);
    }

    public TriangleFigure(org.jhotdraw.draw.AttributeKeys.Orientation direction) {
        this(0, 0, 0, 0, direction);
    }

    public TriangleFigure(double x, double y, double width, double height) {
        this(x, y, width, height, org.jhotdraw.draw.AttributeKeys.Orientation.NORTH);
    }

    public TriangleFigure(double x, double y, double width, double height, org.jhotdraw.draw.AttributeKeys.Orientation direction) {
        rectangle = new java.awt.geom.Rectangle2D.Double(x, y, width, height);
        attr().set(org.jhotdraw.draw.AttributeKeys.ORIENTATION, direction);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return new org.jhotdraw.draw.connector.ChopTriangleConnector(this);
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStartConnector) {
        return new org.jhotdraw.draw.connector.ChopTriangleConnector(this);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        java.awt.geom.Rectangle2D.Double bounds = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        return bounds;
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        double scaleFactor = org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g);
        java.awt.Shape triangle = getBezierPath();
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(this, scaleFactor);
        if (grow != 0.0) {
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(((float) (grow)), ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scaleFactor) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT))));
            triangle = gs.createStrokedShape(triangle);
        }
        g.fill(triangle);
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        double scaleFactor = org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g);
        java.awt.Shape triangle = getBezierPath();
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, scaleFactor);
        if (grow != 0.0) {
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(((float) (grow)), ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scaleFactor) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT))));
            triangle = gs.createStrokedShape(triangle);
        }
        g.draw(triangle);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        var handles = super.createHandles(detailLevel);
        if (detailLevel == 0) {
            handles.add(new org.jhotdraw.draw.handle.OrientationHandle(this));
        }
        return handles;
    }

    public org.jhotdraw.geom.path.BezierPath getBezierPath() {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (rectangle.clone()));
        org.jhotdraw.geom.path.BezierPath triangle = new org.jhotdraw.geom.path.BezierPath();
        switch (attr().get(org.jhotdraw.draw.AttributeKeys.ORIENTATION)) {
            case NORTH :
            default :
                triangle.moveTo(((float) (r.x + (r.width / 2))), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y + r.height)));
                break;
            case NORTH_EAST :
                triangle.moveTo(((float) (r.x)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y + r.height)));
                break;
            case EAST :
                triangle.moveTo(((float) (r.x)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y + (r.height / 2.0))));
                triangle.lineTo(((float) (r.x)), ((float) (r.y + r.height)));
                break;
            case SOUTH_EAST :
                triangle.moveTo(((float) (r.x + r.width)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y + r.height)));
                break;
            case SOUTH :
                triangle.moveTo(((float) (r.x + (r.width / 2))), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y)));
                break;
            case SOUTH_WEST :
                triangle.moveTo(((float) (r.x + r.width)), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y)));
                break;
            case WEST :
                triangle.moveTo(((float) (r.x)), ((float) (r.y + (r.height / 2))));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y + r.height)));
                break;
            case NORTH_WEST :
                triangle.moveTo(((float) (r.x)), ((float) (r.y + r.height)));
                triangle.lineTo(((float) (r.x)), ((float) (r.y)));
                triangle.lineTo(((float) (r.x + r.width)), ((float) (r.y)));
                break;
        }
        triangle.setClosed(true);
        return triangle;
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        java.awt.Shape triangle = getBezierPath();
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, scaleDenominator);
        if (grow != 0.0) {
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(((float) (grow)), ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scaleDenominator) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT))));
            triangle = gs.createStrokedShape(triangle);
        }
        return triangle.contains(p);
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        rectangle.x = java.lang.Math.min(anchor.x, lead.x);
        rectangle.y = java.lang.Math.min(anchor.y, lead.y);
        rectangle.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        rectangle.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        double totalStrokeWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, scale);
        double width = 0.0;
        if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) != null) {
            switch (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT)) {
                case INSIDE :
                    width = 0.0;
                    break;
                case OUTSIDE :
                    if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                        width = totalStrokeWidth * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
                    } else {
                        width = totalStrokeWidth;
                    }
                    break;
                case CENTER :
                    if (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                        width = (totalStrokeWidth / 2.0) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
                    } else {
                        width = totalStrokeWidth / 2.0;
                    }
                    break;
            }
        }
        width++;
        java.awt.geom.Rectangle2D.Double r = getBounds();
        org.jhotdraw.geom.Geom.grow(r, width, width);
        return r;
    }

    public java.awt.geom.Point2D.Double chop(java.awt.geom.Point2D.Double p) {
        java.awt.Shape triangle = getBezierPath();
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularHitGrowth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this));
        if (grow != 0.0) {
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(((float) (grow)), ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.scaleFromContext(this)) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT))));
            triangle = gs.createStrokedShape(triangle);
        }
        return org.jhotdraw.geom.Geom.chop(triangle, p);
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TriangleFigure clone() {
        org.jhotdraw.draw.figure.TriangleFigure that = ((org.jhotdraw.draw.figure.TriangleFigure) (super.clone()));
        that.rectangle = ((java.awt.geom.Rectangle2D.Double) (this.rectangle.clone()));
        return that;
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (geometry));
        rectangle.x = r.x;
        rectangle.y = r.y;
        rectangle.width = r.width;
        rectangle.height = r.height;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return rectangle.clone();
    }
}