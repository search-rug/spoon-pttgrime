/* @(#)SVGFigureFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * Creates Figures for SVG elements.
 *
 * <p>Design pattern:<br>
 * Name: Abstract Factory.<br>
 * Role: Abstract Factory.<br>
 * Partners: {@link SVGInputFormat} as Client.
 */
public interface SVGFigureFactory {
    public org.jhotdraw.draw.figure.Figure createRect(double x, double y, double width, double height, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createCircle(double cx, double cy, double r, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createEllipse(double cx, double cy, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createLine(double x1, double y1, double x2, double y2, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createPolyline(java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createPolygon(java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createPath(org.jhotdraw.geom.path.BezierPath[] beziers, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.CompositeFigure createG(java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createText(java.awt.geom.Point2D.Double[] coordinates, double[] rotate, javax.swing.text.StyledDocument text, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.draw.figure.Figure createTextArea(double x, double y, double w, double h, javax.swing.text.StyledDocument doc, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    /**
     * Creates a Figure from an image element.
     *
     * @param x
     * 		The x coordinate.
     * @param y
     * 		The y coordinate.
     * @param width
     * 		The width.
     * @param height
     * 		The height.
     * @param imageData
     * 		Holds the image data. Can be null, if the buffered image has not been created
     * 		from a file.
     * @param bufferedImage
     * 		Holds the buffered image. Can be null, if the image data has not been
     * 		interpreted.
     * @param attributes
     * 		Figure attributes.
     */
    public org.jhotdraw.draw.figure.Figure createImage(double x, double y, double width, double height, byte[] imageData, java.awt.image.BufferedImage bufferedImage, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes);

    public org.jhotdraw.samples.svg.Gradient createLinearGradient(double x1, double y1, double x2, double y2, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx);

    public org.jhotdraw.samples.svg.Gradient createRadialGradient(double cx, double cy, double fx, double fy, double r, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform tx);
}