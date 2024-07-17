/* @(#)SVGOutputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * An output format for storing drawings as Scalable Vector Graphics SVG Tiny 1.2.
 */
public class SVGOutputFormat implements org.jhotdraw.draw.io.OutputFormat {
    /**
     * This is a counter used to create the next unique identification.
     */
    private int nextId;

    /**
     * In this hash map we store all elements to which we have assigned an id.
     */
    private java.util.HashMap<org.w3c.dom.Element, java.lang.String> identifiedElements;

    /**
     * This element holds all definitions of the SVG file.
     */
    private org.w3c.dom.Element defs;

    /**
     * Holds the document that is currently being written.
     */
    private org.w3c.dom.Element document;

    /**
     * Maps gradients to ID's. We use this, so that we need to store the same gradient only once.
     */
    private java.util.HashMap<org.jhotdraw.samples.svg.Gradient, java.lang.String> gradientToIDMap;

    /**
     * Set this to true for pretty printing.
     */
    private boolean isPrettyPrint;

    private static final java.util.HashMap<java.lang.Integer, java.lang.String> STROKE_LINEJOIN;

    static {
        STROKE_LINEJOIN = new java.util.HashMap<java.lang.Integer, java.lang.String>();
        STROKE_LINEJOIN.put(java.awt.BasicStroke.JOIN_MITER, "miter");
        STROKE_LINEJOIN.put(java.awt.BasicStroke.JOIN_ROUND, "round");
        STROKE_LINEJOIN.put(java.awt.BasicStroke.JOIN_BEVEL, "bevel");
    }

    private static final java.util.HashMap<java.lang.Integer, java.lang.String> STROKE_LINECAP;

    static {
        STROKE_LINECAP = new java.util.HashMap<java.lang.Integer, java.lang.String>();
        STROKE_LINECAP.put(java.awt.BasicStroke.CAP_BUTT, "butt");
        STROKE_LINECAP.put(java.awt.BasicStroke.CAP_ROUND, "round");
        STROKE_LINECAP.put(java.awt.BasicStroke.CAP_SQUARE, "square");
    }

    /**
     * Set this variable to true if values should be written with float precision instead with double
     * precision. Float precision is less accurate then double precision, but it uses less storage
     * space.
     */
    private static final boolean IS_FLOAT_PRECISION = true;

    public SVGOutputFormat() {
    }

    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter("Scalable Vector Graphics (SVG)", "svg");
    }

    public javax.swing.JComponent getOutputFormatAccessory() {
        return null;
    }

    public void setPrettyPrint(boolean newValue) {
        isPrettyPrint = newValue;
    }

    public boolean isPrettyPrint() {
        return isPrettyPrint;
    }

    protected void writeElement(org.w3c.dom.Element parent, org.jhotdraw.draw.figure.Figure f) throws java.io.IOException {
        // Write link attribute as encosing "a" element
        if ((f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK) != null) && (f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK).trim().length() > 0)) {
            org.w3c.dom.Element aElement = parent.getOwnerDocument().createElement("a");
            aElement.setAttribute("xlink:href", f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK));
            if ((f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET) != null) && (f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK).trim().length() > 0)) {
                aElement.setAttribute("target", f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET));
            }
            parent.appendChild(aElement);
            parent = aElement;
        }
        // Write the actual element
        if (f instanceof org.jhotdraw.samples.svg.figures.SVGEllipseFigure) {
            org.jhotdraw.samples.svg.figures.SVGEllipseFigure ellipse = ((org.jhotdraw.samples.svg.figures.SVGEllipseFigure) (f));
            if (ellipse.getWidth() == ellipse.getHeight()) {
                writeCircleElement(parent, ellipse);
            } else {
                writeEllipseElement(parent, ellipse);
            }
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGGroupFigure) {
            writeGElement(parent, ((org.jhotdraw.samples.svg.figures.SVGGroupFigure) (f)));
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGImageFigure) {
            writeImageElement(parent, ((org.jhotdraw.samples.svg.figures.SVGImageFigure) (f)));
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGPathFigure) {
            org.jhotdraw.samples.svg.figures.SVGPathFigure path = ((org.jhotdraw.samples.svg.figures.SVGPathFigure) (f));
            if (path.getChildCount() == 1) {
                org.jhotdraw.draw.figure.BezierFigure bezier = ((org.jhotdraw.draw.figure.BezierFigure) (path.getChild(0)));
                boolean isLinear = true;
                for (int i = 0, n = bezier.getNodeCount(); i < n; i++) {
                    if (bezier.getNode(i).getMask() != 0) {
                        isLinear = false;
                        break;
                    }
                }
                if (isLinear) {
                    if (bezier.isClosed()) {
                        writePolygonElement(parent, path);
                    } else if (bezier.getNodeCount() == 2) {
                        writeLineElement(parent, path);
                    } else {
                        writePolylineElement(parent, path);
                    }
                } else {
                    writePathElement(parent, path);
                }
            } else {
                writePathElement(parent, path);
            }
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGRectFigure) {
            writeRectElement(parent, ((org.jhotdraw.samples.svg.figures.SVGRectFigure) (f)));
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGTextFigure) {
            writeTextElement(parent, ((org.jhotdraw.samples.svg.figures.SVGTextFigure) (f)));
        } else if (f instanceof org.jhotdraw.samples.svg.figures.SVGTextAreaFigure) {
            writeTextAreaElement(parent, ((org.jhotdraw.samples.svg.figures.SVGTextAreaFigure) (f)));
        } else {
            java.lang.System.out.println("Unable to write: " + f);
        }
    }

    protected void writeCircleElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGEllipseFigure f) throws java.io.IOException {
        parent.appendChild(createCircle(document, f.getX() + (f.getWidth() / 2.0), f.getY() + (f.getHeight() / 2.0), f.getWidth() / 2.0, f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createCircle(org.w3c.dom.Element doc, double cx, double cy, double r, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("circle");
        writeAttribute(elem, "cx", cx, 0.0);
        writeAttribute(elem, "cy", cy, 0.0);
        writeAttribute(elem, "r", r, 0.0);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected org.w3c.dom.Element createG(org.w3c.dom.Element doc, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("g");
        writeOpacityAttribute(elem, attributes);
        return elem;
    }

    protected org.w3c.dom.Element createLinearGradient(org.w3c.dom.Element doc, double x1, double y1, double x2, double y2, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform transform) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("linearGradient");
        writeAttribute(elem, "x1", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(x1), "0");
        writeAttribute(elem, "y1", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(y1), "0");
        writeAttribute(elem, "x2", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(x2), "1");
        writeAttribute(elem, "y2", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(y2), "0");
        writeAttribute(elem, "gradientUnits", isRelativeToFigureBounds ? "objectBoundingBox" : "userSpaceOnUse", "objectBoundingBox");
        writeAttribute(elem, "gradientTransform", org.jhotdraw.samples.svg.io.SVGOutputFormat.toTransform(transform), "none");
        for (int i = 0; i < stopOffsets.length; i++) {
            org.w3c.dom.Element stop = doc.getOwnerDocument().createElement("stop");
            writeAttribute(stop, "offset", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(stopOffsets[i]), null);
            writeAttribute(stop, "stop-color", org.jhotdraw.samples.svg.io.SVGOutputFormat.toColor(stopColors[i]), null);
            writeAttribute(stop, "stop-opacity", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(stopOpacities[i]), "1");
            elem.appendChild(stop);
        }
        return elem;
    }

    protected org.w3c.dom.Element createRadialGradient(org.w3c.dom.Element doc, double cx, double cy, double fx, double fy, double r, double[] stopOffsets, java.awt.Color[] stopColors, double[] stopOpacities, boolean isRelativeToFigureBounds, java.awt.geom.AffineTransform transform) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("radialGradient");
        writeAttribute(elem, "cx", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(cx), "0.5");
        writeAttribute(elem, "cy", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(cy), "0.5");
        writeAttribute(elem, "fx", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(fx), org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(cx));
        writeAttribute(elem, "fy", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(fy), org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(cy));
        writeAttribute(elem, "r", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(r), "0.5");
        writeAttribute(elem, "gradientUnits", isRelativeToFigureBounds ? "objectBoundingBox" : "userSpaceOnUse", "objectBoundingBox");
        writeAttribute(elem, "gradientTransform", org.jhotdraw.samples.svg.io.SVGOutputFormat.toTransform(transform), "none");
        for (int i = 0; i < stopOffsets.length; i++) {
            org.w3c.dom.Element stop = doc.getOwnerDocument().createElement("stop");
            writeAttribute(stop, "offset", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(stopOffsets[i]), null);
            writeAttribute(stop, "stop-color", org.jhotdraw.samples.svg.io.SVGOutputFormat.toColor(stopColors[i]), null);
            writeAttribute(stop, "stop-opacity", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(stopOpacities[i]), "1");
            elem.appendChild(stop);
        }
        return elem;
    }

    protected void writeEllipseElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGEllipseFigure f) throws java.io.IOException {
        parent.appendChild(createEllipse(document, f.getX() + (f.getWidth() / 2.0), f.getY() + (f.getHeight() / 2.0), f.getWidth() / 2.0, f.getHeight() / 2.0, f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createEllipse(org.w3c.dom.Element doc, double cx, double cy, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("ellipse");
        writeAttribute(elem, "cx", cx, 0.0);
        writeAttribute(elem, "cy", cy, 0.0);
        writeAttribute(elem, "rx", rx, 0.0);
        writeAttribute(elem, "ry", ry, 0.0);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writeGElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGGroupFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = createG(document, f.attr().getAttributes());
        for (org.jhotdraw.draw.figure.Figure child : f.getChildren()) {
            writeElement(elem, child);
        }
        parent.appendChild(elem);
    }

    protected void writeImageElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGImageFigure f) throws java.io.IOException {
        parent.appendChild(createImage(document, f.getX(), f.getY(), f.getWidth(), f.getHeight(), f.getImageData(), f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createImage(org.w3c.dom.Element doc, double x, double y, double w, double h, byte[] imageData, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("image");
        writeAttribute(elem, "x", x, 0.0);
        writeAttribute(elem, "y", y, 0.0);
        writeAttribute(elem, "width", w, 0.0);
        writeAttribute(elem, "height", h, 0.0);
        writeAttribute(elem, "xlink:href", "data:image;base64," + org.jhotdraw.io.Base64.encodeBytes(imageData), "");
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writePathElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.jhotdraw.geom.path.BezierPath[] beziers = new org.jhotdraw.geom.path.BezierPath[f.getChildCount()];
        for (int i = 0; i < beziers.length; i++) {
            beziers[i] = ((org.jhotdraw.draw.figure.BezierFigure) (f.getChild(i))).getBezierPath();
        }
        parent.appendChild(createPath(document, beziers, f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createPath(org.w3c.dom.Element doc, org.jhotdraw.geom.path.BezierPath[] beziers, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("path");
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        writeAttribute(elem, "d", org.jhotdraw.samples.svg.io.SVGOutputFormat.toPath(beziers), null);
        return elem;
    }

    protected void writePolygonElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        java.util.LinkedList<java.awt.geom.Point2D.Double> points = new java.util.LinkedList<java.awt.geom.Point2D.Double>();
        for (int i = 0, n = f.getChildCount(); i < n; i++) {
            org.jhotdraw.geom.path.BezierPath bezier = ((org.jhotdraw.draw.figure.BezierFigure) (f.getChild(i))).getBezierPath();
            for (org.jhotdraw.geom.path.BezierPath.Node node : bezier.nodes()) {
                points.add(new java.awt.geom.Point2D.Double(node.x[0], node.y[0]));
            }
        }
        parent.appendChild(createPolygon(document, points.toArray(new java.awt.geom.Point2D.Double[points.size()]), f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createPolygon(org.w3c.dom.Element doc, java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("polygon");
        writeAttribute(elem, "points", org.jhotdraw.samples.svg.io.SVGOutputFormat.toPoints(points), null);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writePolylineElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        java.util.LinkedList<java.awt.geom.Point2D.Double> points = new java.util.LinkedList<java.awt.geom.Point2D.Double>();
        for (int i = 0, n = f.getChildCount(); i < n; i++) {
            org.jhotdraw.geom.path.BezierPath bezier = ((org.jhotdraw.draw.figure.BezierFigure) (f.getChild(i))).getBezierPath();
            for (org.jhotdraw.geom.path.BezierPath.Node node : bezier.nodes()) {
                points.add(new java.awt.geom.Point2D.Double(node.x[0], node.y[0]));
            }
        }
        parent.appendChild(createPolyline(document, points.toArray(new java.awt.geom.Point2D.Double[points.size()]), f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createPolyline(org.w3c.dom.Element doc, java.awt.geom.Point2D.Double[] points, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("polyline");
        writeAttribute(elem, "points", org.jhotdraw.samples.svg.io.SVGOutputFormat.toPoints(points), null);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writeLineElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.jhotdraw.draw.figure.BezierFigure bezier = ((org.jhotdraw.draw.figure.BezierFigure) (f.getChild(0)));
        parent.appendChild(createLine(document, bezier.getNode(0).x[0], bezier.getNode(0).y[0], bezier.getNode(1).x[0], bezier.getNode(1).y[0], f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createLine(org.w3c.dom.Element doc, double x1, double y1, double x2, double y2, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("line");
        writeAttribute(elem, "x1", x1, 0.0);
        writeAttribute(elem, "y1", y1, 0.0);
        writeAttribute(elem, "x2", x2, 0.0);
        writeAttribute(elem, "y2", y2, 0.0);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writeRectElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGRectFigure f) throws java.io.IOException {
        parent.appendChild(createRect(document, f.getX(), f.getY(), f.getWidth(), f.getHeight(), f.getArcWidth(), f.getArcHeight(), f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createRect(org.w3c.dom.Element doc, double x, double y, double width, double height, double rx, double ry, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("rect");
        writeAttribute(elem, "x", x, 0.0);
        writeAttribute(elem, "y", y, 0.0);
        writeAttribute(elem, "width", width, 0.0);
        writeAttribute(elem, "height", height, 0.0);
        writeAttribute(elem, "rx", rx, 0.0);
        writeAttribute(elem, "ry", ry, 0.0);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        return elem;
    }

    protected void writeTextElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGTextFigure f) throws java.io.IOException {
        javax.swing.text.DefaultStyledDocument styledDoc = new javax.swing.text.DefaultStyledDocument();
        try {
            styledDoc.insertString(0, f.getText(), null);
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
        parent.appendChild(createText(document, f.getCoordinates(), f.getRotates(), styledDoc, f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createText(org.w3c.dom.Element doc, java.awt.geom.Point2D.Double[] coordinates, double[] rotate, javax.swing.text.StyledDocument text, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("text");
        java.lang.StringBuilder bufX = new java.lang.StringBuilder();
        java.lang.StringBuilder bufY = new java.lang.StringBuilder();
        for (int i = 0; i < coordinates.length; i++) {
            if (i != 0) {
                bufX.append(',');
                bufY.append(',');
            }
            bufX.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(coordinates[i].getX()));
            bufY.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(coordinates[i].getY()));
        }
        java.lang.StringBuilder bufR = new java.lang.StringBuilder();
        if (rotate != null) {
            for (int i = 0; i < rotate.length; i++) {
                if (i != 0) {
                    bufR.append(',');
                }
                bufR.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(rotate[i]));
            }
        }
        writeAttribute(elem, "x", bufX.toString(), "0");
        writeAttribute(elem, "y", bufY.toString(), "0");
        writeAttribute(elem, "rotate", bufR.toString(), "");
        java.lang.String str;
        try {
            str = text.getText(0, text.getLength());
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
        elem.setTextContent(str);
        writeShapeAttributes(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeTransformAttribute(elem, attributes);
        writeFontAttributes(elem, attributes);
        return elem;
    }

    protected void writeTextAreaElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGTextAreaFigure f) throws java.io.IOException {
        javax.swing.text.DefaultStyledDocument styledDoc = new javax.swing.text.DefaultStyledDocument();
        try {
            styledDoc.insertString(0, f.getText(), null);
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
        java.awt.geom.Rectangle2D.Double bounds = f.getBounds();
        parent.appendChild(createTextArea(document, bounds.x, bounds.y, bounds.width, bounds.height, styledDoc, f.attr().getAttributes()));
    }

    protected org.w3c.dom.Element createTextArea(org.w3c.dom.Element doc, double x, double y, double w, double h, javax.swing.text.StyledDocument text, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) throws java.io.IOException {
        org.w3c.dom.Element elem = doc.getOwnerDocument().createElement("textArea");
        writeAttribute(elem, "x", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(x), "0");
        writeAttribute(elem, "y", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(y), "0");
        writeAttribute(elem, "width", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(w), "0");
        writeAttribute(elem, "height", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(h), "0");
        java.lang.String str;
        try {
            str = text.getText(0, text.getLength());
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e);
            throw error;
        }
        java.lang.String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i != 0) {
                elem.appendChild(doc.getOwnerDocument().createElement("tbreak"));
            }
            org.w3c.dom.Element contentElement = doc.getOwnerDocument().createElement(null);
            contentElement.setTextContent(lines[i]);
            elem.appendChild(contentElement);
        }
        writeShapeAttributes(elem, attributes);
        writeTransformAttribute(elem, attributes);
        writeOpacityAttribute(elem, attributes);
        writeFontAttributes(elem, attributes);
        return elem;
    }

    // ------------
    // Attributes
    // ------------
    /* Writes shape attributes. */
    protected void writeShapeAttributes(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> m) throws java.io.IOException {
        java.awt.Color color;
        java.lang.String value;
        int intValue;
        // 'color'
        // Value:   <color> | inherit
        // Initial:    depends on user agent
        // Applies to:   None. Indirectly affects other properties via currentColor
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified <color> value, except inherit
        // Nothing to do: Attribute 'color' is not needed.
        // 'color-rendering'
        // Value:    auto | optimizeSpeed | optimizeQuality | inherit
        // Initial:    auto
        // Applies to:    container elements , graphics elements and 'animateColor'
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        // Nothing to do: Attribute 'color-rendering' is not needed.
        // 'fill'
        // Value:   <paint> | inherit (See Specifying paint)
        // Initial:    black
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    "none", system paint, specified <color> value or absolute IRI
        org.jhotdraw.samples.svg.Gradient gradient = org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.get(m);
        if (gradient != null) {
            java.lang.String id;
            if (gradientToIDMap.containsKey(gradient)) {
                id = gradientToIDMap.get(gradient);
            } else {
                org.w3c.dom.Element gradientElem;
                if (gradient instanceof org.jhotdraw.samples.svg.LinearGradient) {
                    org.jhotdraw.samples.svg.LinearGradient lg = ((org.jhotdraw.samples.svg.LinearGradient) (gradient));
                    gradientElem = createLinearGradient(document, lg.getX1(), lg.getY1(), lg.getX2(), lg.getY2(), lg.getStopOffsets(), lg.getStopColors(), lg.getStopOpacities(), lg.isRelativeToFigureBounds(), lg.getTransform());
                } else {
                    org.jhotdraw.samples.svg.RadialGradient rg = ((org.jhotdraw.samples.svg.RadialGradient) (gradient));
                    gradientElem = createRadialGradient(document, rg.getCX(), rg.getCY(), rg.getFX(), rg.getFY(), rg.getR(), rg.getStopOffsets(), rg.getStopColors(), rg.getStopOpacities(), rg.isRelativeToFigureBounds(), rg.getTransform());
                }
                id = getId(gradientElem);
                gradientElem.setAttributeNS("xml", "id", id);
                defs.appendChild(gradientElem);
                gradientToIDMap.put(gradient, id);
            }
            writeAttribute(elem, "fill", ("url(#" + id) + ")", "#000");
        } else {
            writeAttribute(elem, "fill", org.jhotdraw.samples.svg.io.SVGOutputFormat.toColor(org.jhotdraw.draw.AttributeKeys.FILL_COLOR.get(m)), "#000");
        }
        // 'fill-opacity'
        // Value:    <opacity-value> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "fill-opacity", org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY.get(m), 1.0);
        // 'fill-rule'
        // Value:  nonzero | evenodd | inherit
        // Initial:   nonzero
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        if (org.jhotdraw.draw.AttributeKeys.WINDING_RULE.get(m) != org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO) {
            writeAttribute(elem, "fill-rule", "evenodd", "nonzero");
        }
        // 'stroke'
        // Value:   <paint> | inherit (See Specifying paint)
        // Initial:    none
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    "none", system paint, specified <color> value
        // or absolute IRI
        gradient = org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.get(m);
        if (gradient != null) {
            java.lang.String id;
            if (gradientToIDMap.containsKey(gradient)) {
                id = gradientToIDMap.get(gradient);
            } else {
                org.w3c.dom.Element gradientElem;
                if (gradient instanceof org.jhotdraw.samples.svg.LinearGradient) {
                    org.jhotdraw.samples.svg.LinearGradient lg = ((org.jhotdraw.samples.svg.LinearGradient) (gradient));
                    gradientElem = createLinearGradient(document, lg.getX1(), lg.getY1(), lg.getX2(), lg.getY2(), lg.getStopOffsets(), lg.getStopColors(), lg.getStopOpacities(), lg.isRelativeToFigureBounds(), lg.getTransform());
                } else {
                    org.jhotdraw.samples.svg.RadialGradient rg = ((org.jhotdraw.samples.svg.RadialGradient) (gradient));
                    gradientElem = createRadialGradient(document, rg.getCX(), rg.getCY(), rg.getFX(), rg.getFY(), rg.getR(), rg.getStopOffsets(), rg.getStopColors(), rg.getStopOpacities(), rg.isRelativeToFigureBounds(), rg.getTransform());
                }
                id = getId(gradientElem);
                gradientElem.setAttributeNS("xml", "id", id);
                defs.appendChild(gradientElem);
                gradientToIDMap.put(gradient, id);
            }
            writeAttribute(elem, "stroke", ("url(#" + id) + ")", "none");
        } else {
            writeAttribute(elem, "stroke", org.jhotdraw.samples.svg.io.SVGOutputFormat.toColor(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.get(m)), "none");
        }
        // 'stroke-dasharray'
        // Value:    none | <dasharray> | inherit
        // Initial:    none
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes (non-additive)
        // Computed value:    Specified value, except inherit
        double[] dashes = org.jhotdraw.draw.AttributeKeys.STROKE_DASHES.get(m);
        if (dashes != null) {
            java.lang.StringBuilder buf = new java.lang.StringBuilder();
            for (int i = 0; i < dashes.length; i++) {
                if (i != 0) {
                    buf.append(',');
                }
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(dashes[i]));
            }
            writeAttribute(elem, "stroke-dasharray", buf.toString(), null);
        }
        // 'stroke-dashoffset'
        // Value:   <length> | inherit
        // Initial:    0
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-dashoffset", org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE.get(m), 0.0);
        // 'stroke-linecap'
        // Value:    butt | round | square | inherit
        // Initial:    butt
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-linecap", org.jhotdraw.samples.svg.io.SVGOutputFormat.STROKE_LINECAP.get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP.get(m)), "butt");
        // 'stroke-linejoin'
        // Value:    miter | round | bevel | inherit
        // Initial:    miter
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-linejoin", org.jhotdraw.samples.svg.io.SVGOutputFormat.STROKE_LINEJOIN.get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN.get(m)), "miter");
        // 'stroke-miterlimit'
        // Value:    <miterlimit> | inherit
        // Initial:    4
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-miterlimit", org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT.get(m), 4.0);
        // 'stroke-opacity'
        // Value:    <opacity-value> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-opacity", org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY.get(m), 1.0);
        // 'stroke-width'
        // Value:   <length> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "stroke-width", org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.get(m), 1.0);
    }

    /* Writes the opacity attribute. */
    protected void writeOpacityAttribute(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> m) throws java.io.IOException {
        // 'opacity'
        // Value:   <opacity-value> | inherit
        // Initial:   1
        // Applies to:    'image' element
        // Inherited:   no
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        // <opacity-value>
        // The uniform opacity setting must be applied across an entire object.
        // Any values outside the range 0.0 (fully transparent) to 1.0
        // (fully opaque) shall be clamped to this range.
        // (See Clamping values which are restricted to a particular range.)
        writeAttribute(elem, "opacity", org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY.get(m), 1.0);
    }

    /* Writes the transform attribute as specified in
    http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
    protected void writeTransformAttribute(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.get(a);
        if (t != null) {
            writeAttribute(elem, "transform", org.jhotdraw.samples.svg.io.SVGOutputFormat.toTransform(t), "none");
        }
    }

    /* Writes font attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#Font
     */
    private void writeFontAttributes(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.String value;
        double doubleValue;
        // 'font-family'
        // Value:   [[ <family-name> |
        // <generic-family> ],]* [<family-name> |
        // <generic-family>] | inherit
        // Initial:   depends on user agent
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "font-family", org.jhotdraw.draw.AttributeKeys.FONT_FACE.get(a).getFontName(), "Dialog");
        // 'font-getChildCount'
        // Value:   <absolute-getChildCount> | <relative-getChildCount> |
        // <length> | inherit
        // Initial:   medium
        // Applies to:   text content elements
        // Inherited:   yes, the computed value is inherited
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Absolute length
        writeAttribute(elem, "font-size", org.jhotdraw.draw.AttributeKeys.FONT_SIZE.get(a), 0.0);
        // 'font-style'
        // Value:   normal | italic | oblique | inherit
        // Initial:   normal
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "font-style", org.jhotdraw.draw.AttributeKeys.FONT_ITALIC.get(a) ? "italic" : "normal", "normal");
        // 'font-variant'
        // Value:   normal | small-caps | inherit
        // Initial:   normal
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   no
        // Computed value:    Specified value, except inherit
        // XXX - Implement me
        writeAttribute(elem, "font-variant", "normal", "normal");
        // 'font-weight'
        // Value:   normal | bold | bolder | lighter | 100 | 200 | 300
        // | 400 | 500 | 600 | 700 | 800 | 900 | inherit
        // Initial:   normal
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    one of the legal numeric values, non-numeric
        // values shall be converted to numeric values according to the rules
        // defined below.
        writeAttribute(elem, "font-weight", org.jhotdraw.draw.AttributeKeys.FONT_BOLD.get(a) ? "bold" : "normal", "normal");
        // Note: text-decoration is an SVG 1.1 feature
        // 'text-decoration'
        // Value:   none | [ underline || overline || line-through || blink ] | inherit
        // Initial:   none
        // Applies to:   text content elements
        // Inherited:   no (see prose)
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        writeAttribute(elem, "text-decoration", org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE.get(a) ? "underline" : "none", "none");
    }

    /* Writes viewport attributes. */
    private void writeViewportAttributes(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object value;
        java.lang.Double doubleValue;
        if ((org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_WIDTH.get(a) != null) && (org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_HEIGHT.get(a) != null)) {
            // width of the viewport
            writeAttribute(elem, "width", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_WIDTH.get(a)), null);
            // height of the viewport
            writeAttribute(elem, "height", org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_HEIGHT.get(a)), null);
        }
        // 'viewport-fill'
        // Value:  "none" | <color> | inherit
        // Initial:  none
        // Applies to: viewport-creating elements
        // Inherited:  no
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        // Computed value:    "none" or specified <color> value, except inherit
        writeAttribute(elem, "viewport-fill", org.jhotdraw.samples.svg.io.SVGOutputFormat.toColor(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL.get(a)), "none");
        // 'viewport-fill-opacity'
        // Value: <opacity-value> | inherit
        // Initial:  1.0
        // Applies to: viewport-creating elements
        // Inherited:  no
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        // Computed value:    Specified value, except inherit
        writeAttribute(elem, "viewport-fill-opacity", org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL_OPACITY.get(a), 1.0);
    }

    protected void writeAttribute(org.w3c.dom.Element elem, java.lang.String name, java.lang.String value, java.lang.String defaultValue) {
        writeAttribute(elem, name, org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, value, defaultValue);
    }

    protected void writeAttribute(org.w3c.dom.Element elem, java.lang.String name, java.lang.String namespace, java.lang.String value, java.lang.String defaultValue) {
        if (!value.equals(defaultValue)) {
            elem.setAttribute(name, value);
        }
    }

    protected void writeAttribute(org.w3c.dom.Element elem, java.lang.String name, double value, double defaultValue) {
        writeAttribute(elem, name, org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, value, defaultValue);
    }

    protected void writeAttribute(org.w3c.dom.Element elem, java.lang.String name, java.lang.String namespace, double value, double defaultValue) {
        if (value != defaultValue) {
            elem.setAttribute(name, org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(value));
        }
    }

    /**
     * Returns a value as a SVG Path attribute. as specified in
     * http://www.w3.org/TR/SVGMobile12/paths.html#PathDataBNF
     */
    public static java.lang.String toPath(org.jhotdraw.geom.path.BezierPath[] paths) {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        for (int j = 0; j < paths.length; j++) {
            org.jhotdraw.geom.path.BezierPath path = paths[j];
            if (path.size() == 0) {
                // nothing to do
            } else if (path.size() == 1) {
                org.jhotdraw.geom.path.BezierPath.Node current = path.nodes().get(0);
                buf.append("M ");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                buf.append(' ');
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                // buf.append(" L ");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                buf.append(' ');
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0] + 1));
            } else {
                org.jhotdraw.geom.path.BezierPath.Node previous;
                org.jhotdraw.geom.path.BezierPath.Node current;
                previous = current = path.nodes().get(0);
                buf.append("M ");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                buf.append(' ');
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                char nextCommand = 'L';
                for (int i = 1, n = path.size(); i < n; i++) {
                    previous = current;
                    current = path.nodes().get(i);
                    if ((previous.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) == 0) {
                        if ((current.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) {
                            if (nextCommand != 'L') {
                                buf.append(" L ");
                                nextCommand = 'L';
                            } else {
                                buf.append(' ');
                            }
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                        } else {
                            if (nextCommand != 'Q') {
                                buf.append(" Q ");
                                nextCommand = 'Q';
                            } else {
                                buf.append(' ');
                            }
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[1]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[1]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                        }
                    } else if ((current.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) {
                        if (nextCommand != 'Q') {
                            buf.append(" Q ");
                            nextCommand = 'Q';
                        } else {
                            buf.append(' ');
                        }
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.x[2]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.y[2]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                    } else {
                        if (nextCommand != 'C') {
                            buf.append(" C ");
                            nextCommand = 'C';
                        } else {
                            buf.append(' ');
                        }
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.x[2]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.y[2]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[1]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[1]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                        buf.append(' ');
                        buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                    }
                }
                if (path.isClosed()) {
                    if (path.size() > 1) {
                        previous = path.nodes().get(path.size() - 1);
                        current = path.nodes().get(0);
                        if ((previous.mask & org.jhotdraw.geom.path.BezierPath.C2_MASK) == 0) {
                            if ((current.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) {
                                if (nextCommand != 'L') {
                                    buf.append(" L ");
                                    nextCommand = 'L';
                                } else {
                                    buf.append(' ');
                                }
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                                buf.append(' ');
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                            } else {
                                if (nextCommand != 'Q') {
                                    buf.append(" Q ");
                                    nextCommand = 'Q';
                                } else {
                                    buf.append(' ');
                                }
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[1]));
                                buf.append(' ');
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[1]));
                                buf.append(' ');
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                                buf.append(' ');
                                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                            }
                        } else if ((current.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) == 0) {
                            if (nextCommand != 'Q') {
                                buf.append(" Q ");
                                nextCommand = 'Q';
                            } else {
                                buf.append(' ');
                            }
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.x[2]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.y[2]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                        } else {
                            if (nextCommand != 'C') {
                                buf.append(" C ");
                                nextCommand = 'C';
                            } else {
                                buf.append(' ');
                            }
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.x[2]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(previous.y[2]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[1]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[1]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.x[0]));
                            buf.append(' ');
                            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(current.y[0]));
                        }
                    }
                    buf.append(" Z");
                    nextCommand = '\u0000';
                }
            }
        }
        return buf.toString();
    }

    /**
     * Returns a double array as a number attribute value.
     */
    public static java.lang.String toNumber(double number) {
        java.lang.String str = (org.jhotdraw.samples.svg.io.SVGOutputFormat.IS_FLOAT_PRECISION) ? java.lang.Float.toString(((float) (number))) : java.lang.Double.toString(number);
        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    /**
     * Returns a Point2D.Double array as a Points attribute value. as specified in
     * http://www.w3.org/TR/SVGMobile12/shapes.html#PointsBNF
     */
    public static java.lang.String toPoints(java.awt.geom.Point2D.Double[] points) throws java.io.IOException {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        for (int i = 0; i < points.length; i++) {
            if (i != 0) {
                buf.append(", ");
            }
            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(points[i].x));
            buf.append(',');
            buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(points[i].y));
        }
        return buf.toString();
    }

    /* Converts an AffineTransform into an SVG transform attribute value as specified in
    http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
    public static java.lang.String toTransform(java.awt.geom.AffineTransform t) throws java.io.IOException {
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        switch (t.getType()) {
            case java.awt.geom.AffineTransform.TYPE_IDENTITY :
                buf.append("none");
                break;
            case java.awt.geom.AffineTransform.TYPE_TRANSLATION :
                // translate(<tx> [<ty>]), specifies a translation by tx and ty.
                // If <ty> is not provided, it is assumed to be zero.
                buf.append("translate(");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(t.getTranslateX()));
                if (t.getTranslateY() != 0.0) {
                    buf.append(' ');
                    buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(t.getTranslateY()));
                }
                buf.append(')');
                break;
                /* case AffineTransform.TYPE_GENERAL_ROTATION :
                case AffineTransform.TYPE_QUADRANT_ROTATION :
                case AffineTransform.TYPE_MASK_ROTATION :
                // rotate(<rotate-angle> [<cx> <cy>]), specifies a rotation by
                // <rotate-angle> degrees about a given point.
                // If optional parameters <cx> and <cy> are not supplied, the
                // rotate is about the origin of the current user coordinate
                // system. The operation corresponds to the matrix
                // [cos(a) sin(a) -sin(a) cos(a) 0 0].
                // If optional parameters <cx> and <cy> are supplied, the rotate
                // is about the point (<cx>, <cy>). The operation represents the
                // equivalent of the following specification:
                // translate(<cx>, <cy>) rotate(<rotate-angle>)
                // translate(-<cx>, -<cy>).
                buf.append("rotate(");
                buf.append(toNumber(t.getScaleX()));
                buf.append(')');
                break;
                 */
            case java.awt.geom.AffineTransform.TYPE_UNIFORM_SCALE :
                // scale(<sx> [<sy>]), specifies a scale operation by sx
                // and sy. If <sy> is not provided, it is assumed to be equal
                // to <sx>.
                buf.append("scale(");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(t.getScaleX()));
                buf.append(')');
                break;
            case java.awt.geom.AffineTransform.TYPE_GENERAL_SCALE :
            case java.awt.geom.AffineTransform.TYPE_MASK_SCALE :
                // scale(<sx> [<sy>]), specifies a scale operation by sx
                // and sy. If <sy> is not provided, it is assumed to be equal
                // to <sx>.
                buf.append("scale(");
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(t.getScaleX()));
                buf.append(' ');
                buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(t.getScaleY()));
                buf.append(')');
                break;
            default :
                // matrix(<a> <b> <c> <d> <e> <f>), specifies a transformation
                // in the form of a transformation matrix of six values.
                // matrix(a,b,c,d,e,f) is equivalent to applying the
                // transformation matrix [a b c d e f].
                buf.append("matrix(");
                double[] matrix = new double[6];
                t.getMatrix(matrix);
                for (int i = 0; i < matrix.length; i++) {
                    if (i != 0) {
                        buf.append(' ');
                    }
                    buf.append(org.jhotdraw.samples.svg.io.SVGOutputFormat.toNumber(matrix[i]));
                }
                buf.append(')');
                break;
        }
        return buf.toString();
    }

    public static java.lang.String toColor(java.awt.Color color) {
        if (color == null) {
            return "none";
        }
        java.lang.String value;
        value = "000000" + java.lang.Integer.toHexString(color.getRGB());
        value = "#" + value.substring(value.length() - 6);
        if (((value.charAt(1) == value.charAt(2)) && (value.charAt(3) == value.charAt(4))) && (value.charAt(5) == value.charAt(6))) {
            value = (("#" + value.charAt(1)) + value.charAt(3)) + value.charAt(5);
        }
        return value;
    }

    @java.lang.Override
    public java.lang.String getFileExtension() {
        return "svg";
    }

    @java.lang.Override
    public void write(java.net.URI uri, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        write(new java.io.File(uri), drawing);
    }

    public void write(java.io.File file, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file));
        try {
            write(out, drawing);
        } finally {
            out.close();
        }
    }

    @java.lang.Override
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        write(out, drawing, drawing.getChildren());
    }

    /**
     * All other write methods delegate their work to here.
     */
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures) throws java.io.IOException {
        javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.ImageMapOutputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            throw new java.io.IOException(ex);
        }
        org.w3c.dom.Document doc = dBuilder.newDocument();
        document = doc.createElementNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, "svg");
        document.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        document.setAttribute("version", "1.2");
        document.setAttribute("baseProfile", "tiny");
        writeViewportAttributes(document, drawing.attr().getAttributes());
        initStorageContext(document);
        defs = doc.createElement("defs");
        document.appendChild(defs);
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            writeElement(document, f);
        }
        // Write XML prolog
        java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(out, "UTF-8"));
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        // Write XML content
        javax.xml.transform.Transformer t;
        try {
            t = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            if (isPrettyPrint) {
                t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            }
            t.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(out));
        } catch (javax.xml.transform.TransformerException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.SVGOutputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // Flush writer
        writer.flush();
    }

    private void initStorageContext(org.w3c.dom.Element root) {
        identifiedElements = new java.util.HashMap<org.w3c.dom.Element, java.lang.String>();
        gradientToIDMap = new java.util.HashMap<org.jhotdraw.samples.svg.Gradient, java.lang.String>();
    }

    /**
     * Gets a unique ID for the specified element.
     */
    public java.lang.String getId(org.w3c.dom.Element element) {
        if (identifiedElements.containsKey(element)) {
            return identifiedElements.get(element);
        } else {
            java.lang.String id = java.lang.Integer.toString(nextId++, java.lang.Character.MAX_RADIX);
            identifiedElements.put(element, id);
            return id;
        }
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor) throws java.io.IOException {
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        write(buf, drawing, figures);
        return new org.jhotdraw.datatransfer.InputStreamTransferable(new java.awt.datatransfer.DataFlavor(org.jhotdraw.samples.svg.SVGConstants.SVG_MIMETYPE, "Image SVG"), buf.toByteArray());
    }
}