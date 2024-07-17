/* @(#)ImageMapOutputFormat.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * ImageMapOutputFormat exports a SVG drawing as an HTML 4.01 <code>MAP</code> element. For more
 * information see: http://www.w3.org/TR/html401/struct/objects.html#h-13.6.2
 */
public class ImageMapOutputFormat implements org.jhotdraw.draw.io.OutputFormat {
    /**
     * The affine transformation for the output. This is used to create scaled image maps.
     */
    private java.awt.geom.AffineTransform drawingTransform = new java.awt.geom.AffineTransform();

    /**
     * Set this to true, if AREA elements with <code>nohref="true"</code> shall e included in the
     * image map.
     */
    private boolean isIncludeNohref = false;

    /**
     * Image dimension. We only include AREA elements which are within the image dimension.
     */
    private java.awt.Rectangle bounds = new java.awt.Rectangle(0, 0, java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE);

    public ImageMapOutputFormat() {
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter("HTML Image Map", "html");
    }

    @java.lang.Override
    public java.lang.String getFileExtension() {
        return "html";
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
        write(out, drawing.getChildren());
    }

    /**
     * Writes the drawing to the specified output stream. This method applies the specified
     * drawingTransform to the drawing, and draws it on an image of the specified getChildCount.
     */
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing, java.awt.geom.AffineTransform drawingTransform, java.awt.Dimension imageSize) throws java.io.IOException {
        write(out, drawing.getChildren(), drawingTransform, imageSize);
    }

    /**
     * Writes the figures to the specified output stream. This method applies the specified
     * drawingTransform to the drawing, and draws it on an image of the specified getChildCount.
     *
     * <p>All other write methods delegate their work to here.
     */
    public void write(java.io.OutputStream out, java.util.List<org.jhotdraw.draw.figure.Figure> figures, java.awt.geom.AffineTransform drawingTransform, java.awt.Dimension imageSize) throws java.io.IOException {
        this.drawingTransform = (drawingTransform == null) ? new java.awt.geom.AffineTransform() : drawingTransform;
        this.bounds = (imageSize == null) ? new java.awt.Rectangle(0, 0, java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE) : new java.awt.Rectangle(0, 0, imageSize.width, imageSize.height);
        javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.ImageMapOutputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            throw new java.io.IOException(ex);
        }
        org.w3c.dom.Element document = dBuilder.newDocument().createElement("map");
        // Note: Image map elements need to be written from front to back
        for (org.jhotdraw.draw.figure.Figure f : new org.jhotdraw.util.ReversedList<org.jhotdraw.draw.figure.Figure>(figures)) {
            writeElement(document, f);
        }
        // Strip AREA elements with "nohref" attributes from the end of the
        // map
        if (!isIncludeNohref) {
            org.w3c.dom.NodeList list = document.getChildNodes();
            for (int i = list.getLength() - 1; i >= 0; i--) {
                org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
                if (child.hasAttribute("nohref")) {
                    document.removeChild(child);
                }
            }
        }
        try {
            // Write XML content
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer t = tf.newTransformer();
            org.w3c.dom.NodeList list = document.getChildNodes();
            for (int i = list.getLength() - 1; i >= 0; i--) {
                org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
                t.transform(new javax.xml.transform.dom.DOMSource(child), new javax.xml.transform.stream.StreamResult(out));
            }
        } catch (javax.xml.transform.TransformerException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.ImageMapOutputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            throw new java.io.IOException(ex);
        }
    }

    /**
     * All other write methods delegate their work to here.
     */
    public void write(java.io.OutputStream out, java.util.List<org.jhotdraw.draw.figure.Figure> figures) throws java.io.IOException {
        java.awt.geom.Rectangle2D.Double drawingRect = null;
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            if (drawingRect == null) {
                drawingRect = f.getBounds();
            } else {
                drawingRect.add(f.getBounds());
            }
        }
        java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
        tx.translate(-java.lang.Math.min(0, drawingRect.x), -java.lang.Math.min(0, drawingRect.y));
        write(out, figures, tx, new java.awt.Dimension(((int) (java.lang.Math.abs(drawingRect.x) + drawingRect.width)), ((int) (java.lang.Math.abs(drawingRect.y) + drawingRect.height))));
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor) throws java.io.IOException {
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        write(buf, figures);
        return new org.jhotdraw.datatransfer.InputStreamTransferable(new java.awt.datatransfer.DataFlavor("text/html", "HTML Image Map"), buf.toByteArray());
    }

    protected void writeElement(org.w3c.dom.Element parent, org.jhotdraw.draw.figure.Figure f) throws java.io.IOException {
        if (f instanceof org.jhotdraw.samples.svg.figures.SVGEllipseFigure) {
            writeEllipseElement(parent, ((org.jhotdraw.samples.svg.figures.SVGEllipseFigure) (f)));
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

    /**
     * Writes the <code>shape</code>, <code>coords</code>, <code>href</code>, <code>nohref</code>
     * Attribute for the specified figure and ellipse.
     *
     * @return Returns true, if the circle is inside of the image bounds.
     */
    private boolean writeCircleAttributes(org.w3c.dom.Element elem, org.jhotdraw.samples.svg.figures.SVGFigure f, java.awt.geom.Ellipse2D.Double ellipse) {
        java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(f);
        if (t == null) {
            t = drawingTransform;
        } else {
            t.preConcatenate(drawingTransform);
        }
        if (((t.getType() & (java.awt.geom.AffineTransform.TYPE_UNIFORM_SCALE | java.awt.geom.AffineTransform.TYPE_TRANSLATION)) == t.getType()) && (ellipse.width == ellipse.height)) {
            java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(ellipse.x, ellipse.y);
            java.awt.geom.Point2D.Double end = new java.awt.geom.Point2D.Double(ellipse.x + ellipse.width, ellipse.y + ellipse.height);
            t.transform(start, start);
            t.transform(end, end);
            ellipse.x = java.lang.Math.min(start.x, end.x);
            ellipse.y = java.lang.Math.min(start.y, end.y);
            ellipse.width = java.lang.Math.abs(start.x - end.x);
            ellipse.height = java.lang.Math.abs(start.y - end.y);
            elem.setAttribute("shape", "circle");
            elem.setAttribute("coords", (((((int) (ellipse.x + (ellipse.width / 2.0))) + ",") + ((int) (ellipse.y + (ellipse.height / 2.0)))) + ",") + ((int) (ellipse.width / 2.0)));
            writeHrefAttribute(elem, f);
            return bounds.intersects(ellipse.getBounds());
        } else {
            return writePolyAttributes(elem, f, ((java.awt.Shape) (ellipse)));
        }
    }

    /**
     * Writes the <code>shape</code>, <code>coords</code>, <code>href</code>, <code>nohref</code>
     * Attribute for the specified figure and rectangle.
     *
     * @return Returns true, if the rect is inside of the image bounds.
     */
    private boolean writeRectAttributes(org.w3c.dom.Element elem, org.jhotdraw.samples.svg.figures.SVGFigure f, java.awt.geom.Rectangle2D.Double rect) {
        java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(f);
        if (t == null) {
            t = drawingTransform;
        } else {
            t.preConcatenate(drawingTransform);
        }
        if ((t.getType() & (java.awt.geom.AffineTransform.TYPE_UNIFORM_SCALE | java.awt.geom.AffineTransform.TYPE_TRANSLATION)) == t.getType()) {
            java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(rect.x, rect.y);
            java.awt.geom.Point2D.Double end = new java.awt.geom.Point2D.Double(rect.x + rect.width, rect.y + rect.height);
            t.transform(start, start);
            t.transform(end, end);
            java.awt.Rectangle r = new java.awt.Rectangle(((int) (java.lang.Math.min(start.x, end.x))), ((int) (java.lang.Math.min(start.y, end.y))), ((int) (java.lang.Math.abs(start.x - end.x))), ((int) (java.lang.Math.abs(start.y - end.y))));
            elem.setAttribute("shape", "rect");
            elem.setAttribute("coords", (((((r.x + ",") + r.y) + ",") + (r.x + r.width)) + ",") + (r.y + r.height));
            writeHrefAttribute(elem, f);
            return bounds.intersects(r);
        } else {
            return writePolyAttributes(elem, f, ((java.awt.Shape) (rect)));
        }
    }

    private void writeHrefAttribute(org.w3c.dom.Element elem, org.jhotdraw.samples.svg.figures.SVGFigure f) {
        java.lang.String link = f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK);
        if ((link != null) && (link.trim().length() > 0)) {
            elem.setAttribute("href", link);
            elem.setAttribute("title", link);
            elem.setAttribute("alt", link);
            java.lang.String linkTarget = f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET);
            if ((linkTarget != null) && (linkTarget.trim().length() > 0)) {
                elem.setAttribute("target", linkTarget);
            }
        } else {
            elem.setAttribute("nohref", "true");
        }
    }

    /**
     * Writes the <code>shape</code>, <code>coords</code>, <code>href</code>, <code>nohref</code>
     * Attribute for the specified figure and shape.
     *
     * @return Returns true, if the polygon is inside of the image bounds.
     */
    private boolean writePolyAttributes(org.w3c.dom.Element elem, org.jhotdraw.samples.svg.figures.SVGFigure f, java.awt.Shape shape) {
        java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(f);
        if (t == null) {
            t = drawingTransform;
        } else {
            t.preConcatenate(drawingTransform);
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        float[] coords = new float[6];
        java.awt.geom.Path2D.Double path = new java.awt.geom.Path2D.Double();
        for (java.awt.geom.PathIterator i = shape.getPathIterator(t, 1.5F); !i.isDone(); i.next()) {
            switch (i.currentSegment(coords)) {
                case java.awt.geom.PathIterator.SEG_MOVETO :
                    if (buf.length() != 0) {
                        throw new java.lang.IllegalArgumentException("Illegal shape " + shape);
                    }
                    if (buf.length() != 0) {
                        buf.append(',');
                    }
                    buf.append(((int) (coords[0])));
                    buf.append(',');
                    buf.append(((int) (coords[1])));
                    path.moveTo(coords[0], coords[1]);
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO :
                    if (buf.length() != 0) {
                        buf.append(',');
                    }
                    buf.append(((int) (coords[0])));
                    buf.append(',');
                    buf.append(((int) (coords[1])));
                    path.lineTo(coords[0], coords[1]);
                    break;
                case java.awt.geom.PathIterator.SEG_CLOSE :
                    path.closePath();
                    break;
                default :
                    throw new java.lang.InternalError("Illegal segment type " + i.currentSegment(coords));
            }
        }
        elem.setAttribute("shape", "poly");
        elem.setAttribute("coords", buf.toString());
        writeHrefAttribute(elem, f);
        return path.intersects(new java.awt.geom.Rectangle2D.Float(bounds.x, bounds.y, bounds.width, bounds.height));
    }

    private void writePathElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.jhotdraw.geom.GrowStroke growStroke = new org.jhotdraw.geom.GrowStroke(org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0) / 2.0, org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0));
        java.awt.BasicStroke basicStroke = new java.awt.BasicStroke(((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0))));
        for (org.jhotdraw.draw.figure.Figure child : f.getChildren()) {
            org.jhotdraw.samples.svg.figures.SVGBezierFigure bezier = ((org.jhotdraw.samples.svg.figures.SVGBezierFigure) (child));
            org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
            if (bezier.isClosed()) {
                writePolyAttributes(elem, f, growStroke.createStrokedShape(bezier.getBezierPath()));
            } else {
                writePolyAttributes(elem, f, basicStroke.createStrokedShape(bezier.getBezierPath()));
            }
            parent.appendChild(elem);
        }
    }

    private void writePolygonElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
        if (writePolyAttributes(elem, f, new org.jhotdraw.geom.GrowStroke(org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0) / 2.0, org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0)).createStrokedShape(f.getChild(0).getBezierPath()))) {
            parent.appendChild(elem);
        }
    }

    private void writePolylineElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
        if (writePolyAttributes(elem, f, new java.awt.BasicStroke(((float) (org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0)))).createStrokedShape(f.getChild(0).getBezierPath()))) {
            parent.appendChild(elem);
        }
    }

    private void writeLineElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGPathFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
        if (writePolyAttributes(elem, f, new org.jhotdraw.geom.GrowStroke(org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0) / 2.0, org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0)).createStrokedShape(new java.awt.geom.Line2D.Double(f.getStartPoint(), f.getEndPoint())))) {
            parent.appendChild(elem);
        }
    }

    private void writeRectElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGRectFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("AREA");
        boolean isContained;
        if ((f.getArcHeight() == 0) && (f.getArcWidth() == 0)) {
            java.awt.geom.Rectangle2D.Double rect = f.getBounds();
            double grow = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(f, 1.0);
            rect.x -= grow;
            rect.y -= grow;
            rect.width += grow;
            rect.height += grow;
            isContained = writeRectAttributes(elem, f, rect);
        } else {
            isContained = writePolyAttributes(elem, f, new org.jhotdraw.geom.GrowStroke(org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0) / 2.0, org.jhotdraw.samples.svg.SVGAttributeKeys.getStrokeTotalWidth(f, 1.0)).createStrokedShape(new java.awt.geom.RoundRectangle2D.Double(f.getX(), f.getY(), f.getWidth(), f.getHeight(), f.getArcWidth(), f.getArcHeight())));
        }
        if (isContained) {
            parent.appendChild(elem);
        }
    }

    private void writeTextElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGTextFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("AREA");
        java.awt.geom.Rectangle2D.Double rect = f.getBounds();
        double grow = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(f, 1.0);
        rect.x -= grow;
        rect.y -= grow;
        rect.width += grow;
        rect.height += grow;
        if (writeRectAttributes(elem, f, rect)) {
            parent.appendChild(elem);
        }
    }

    private void writeTextAreaElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGTextAreaFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("AREA");
        java.awt.geom.Rectangle2D.Double rect = f.getBounds();
        double grow = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(f, 1.0);
        rect.x -= grow;
        rect.y -= grow;
        rect.width += grow;
        rect.height += grow;
        if (writeRectAttributes(elem, f, rect)) {
            parent.appendChild(elem);
        }
    }

    private void writeEllipseElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGEllipseFigure f) throws java.io.IOException {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
        java.awt.geom.Rectangle2D.Double r = f.getBounds();
        double grow = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(f, 1.0);
        java.awt.geom.Ellipse2D.Double ellipse = new java.awt.geom.Ellipse2D.Double(r.x - grow, r.y - grow, r.width + grow, r.height + grow);
        if (writeCircleAttributes(elem, f, ellipse)) {
            parent.appendChild(elem);
        }
    }

    private void writeGElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGGroupFigure f) throws java.io.IOException {
        // Note: Image map elements need to be written from front to back
        for (org.jhotdraw.draw.figure.Figure child : new org.jhotdraw.util.ReversedList<org.jhotdraw.draw.figure.Figure>(f.getChildren())) {
            writeElement(parent, child);
        }
    }

    private void writeImageElement(org.w3c.dom.Element parent, org.jhotdraw.samples.svg.figures.SVGImageFigure f) {
        org.w3c.dom.Element elem = parent.getOwnerDocument().createElement("area");
        java.awt.geom.Rectangle2D.Double rect = f.getBounds();
        writeRectAttributes(elem, f, rect);
        parent.appendChild(elem);
    }
}