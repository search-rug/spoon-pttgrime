/* @(#)DefaultSVGFigureFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * DefaultSVGFigureFactory.
 */
public class DefaultSVGFigureFactory implements org.jhotdraw.samples.svg.io.SVGFigureFactory {
    public DefaultSVGFigureFactory() {
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createRect(double x, double y, double w, double h, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGRectFigure figure = new org.jhotdraw.samples.svg.figures.SVGRectFigure();
        figure.setBounds(new java.awt.geom.Point2D.Double(x, y), new java.awt.geom.Point2D.Double(x + w, y + h));
        figure.setArc(rx, ry);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createCircle(double cx, double cy, double r, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        return createEllipse(cx, cy, r, r, a);
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createEllipse(double cx, double cy, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGEllipseFigure figure = new org.jhotdraw.samples.svg.figures.SVGEllipseFigure(cx - rx, cy - ry, rx * 2.0, ry * 2.0);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createLine(double x1, double y1, double x2, double y2, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGPathFigure figure = new org.jhotdraw.samples.svg.figures.SVGPathFigure();
        figure.removeAllChildren();
        org.jhotdraw.samples.svg.figures.SVGBezierFigure bf = new org.jhotdraw.samples.svg.figures.SVGBezierFigure();
        bf.addNode(new org.jhotdraw.geom.path.BezierPath.Node(x1, y1));
        bf.addNode(new org.jhotdraw.geom.path.BezierPath.Node(x2, y2));
        figure.add(bf);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createPolyline(java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGPathFigure figure = new org.jhotdraw.samples.svg.figures.SVGPathFigure();
        figure.removeAllChildren();
        org.jhotdraw.samples.svg.figures.SVGBezierFigure bf = new org.jhotdraw.samples.svg.figures.SVGBezierFigure();
        for (int i = 0; i < points.length; i++) {
            bf.addNode(new org.jhotdraw.geom.path.BezierPath.Node(points[i].x, points[i].y));
        }
        figure.add(bf);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createPolygon(java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGPathFigure figure = new org.jhotdraw.samples.svg.figures.SVGPathFigure();
        figure.removeAllChildren();
        org.jhotdraw.samples.svg.figures.SVGBezierFigure bf = new org.jhotdraw.samples.svg.figures.SVGBezierFigure();
        for (int i = 0; i < points.length; i++) {
            bf.addNode(new org.jhotdraw.geom.path.BezierPath.Node(points[i].x, points[i].y));
        }
        bf.setClosed(true);
        figure.add(bf);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createPath(org.jhotdraw.geom.path.BezierPath[] beziers, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGPathFigure figure = new org.jhotdraw.samples.svg.figures.SVGPathFigure();
        figure.removeAllChildren();
        for (int i = 0; i < beziers.length; i++) {
            org.jhotdraw.samples.svg.figures.SVGBezierFigure bf = new org.jhotdraw.samples.svg.figures.SVGBezierFigure();
            bf.setBezierPath(beziers[i]);
            figure.add(bf);
        }
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.CompositeFigure createG(java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGGroupFigure figure = new org.jhotdraw.samples.svg.figures.SVGGroupFigure();
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createImage(double x, double y, double w, double h, byte[] imageData, java.awt.image.BufferedImage bufferedImage, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGImageFigure figure = new org.jhotdraw.samples.svg.figures.SVGImageFigure();
        figure.setBounds(new java.awt.geom.Point2D.Double(x, y), new java.awt.geom.Point2D.Double(x + w, y + h));
        figure.setImage(imageData, bufferedImage);
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createTextArea(double x, double y, double w, double h, javax.swing.text.StyledDocument doc, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        org.jhotdraw.samples.svg.figures.SVGTextAreaFigure figure = new org.jhotdraw.samples.svg.figures.SVGTextAreaFigure();
        figure.setBounds(new java.awt.geom.Point2D.Double(x, y), new java.awt.geom.Point2D.Double(x + w, y + h));
        try {
            figure.setText(doc.getText(0, doc.getLength()));
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError ex = new java.lang.InternalError(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        figure.attr().setAttributes(attributes);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.Figure createText(java.awt.geom.Point2D.Double[] coordinates, double[] rotates, javax.swing.text.StyledDocument text, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) {
        org.jhotdraw.samples.svg.figures.SVGTextFigure figure = new org.jhotdraw.samples.svg.figures.SVGTextFigure();
        figure.setCoordinates(coordinates);
        figure.setRotates(rotates);
        try {
            figure.setText(text.getText(0, text.getLength()));
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError ex = new java.lang.InternalError(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        figure.attr().setAttributes(a);
        return figure;
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.Gradient createRadialGradient(double cx, double cy, double fx, double fy, double r, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx) {
        return new org.jhotdraw.samples.svg.RadialGradient(cx, cy, fx, fy, r, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.Gradient createLinearGradient(double x1, double y1, double x2, double y2, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx) {
        return new org.jhotdraw.samples.svg.LinearGradient(x1, y1, x2, y2, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
    }
}