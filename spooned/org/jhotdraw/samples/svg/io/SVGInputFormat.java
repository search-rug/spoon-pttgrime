/* @(#)SVGInputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * SVGInputFormat. This format is aimed to comply to the Scalable Vector Graphics (SVG) Tiny 1.2
 * Specification supporting the <code>SVG-static</code> feature string. <a
 * href="http://www.w3.org/TR/SVGMobile12/">http://www.w3.org/TR/SVGMobile12/</a>
 *
 * <p>Design pattern:<br>
 * Name: Abstract Factory.<br>
 * Role: Client.<br>
 * Partners: {@link SVGFigureFactory} as Abstract Factory.
 */
public class SVGInputFormat implements org.jhotdraw.draw.io.InputFormat {
    /**
     * The SVGFigure factory is used to create Figure's for the drawing.
     */
    private org.jhotdraw.samples.svg.io.SVGFigureFactory factory;

    /**
     * URL pointing to the SVG input file. This is used as a base URL for resources that are
     * referenced from the SVG file.
     */
    private java.net.URL url;

    // FIXME - Move these maps to SVGConstants or to SVGAttributeKeys.
    /**
     * Maps to all XML elements that are identified by an xml:id.
     */
    private java.util.HashMap<java.lang.String, org.w3c.dom.Element> identifiedElements;

    /**
     * Maps to all drawing objects from the XML elements they were created from.
     */
    private java.util.HashMap<org.w3c.dom.Element, java.lang.Object> elementObjects;

    /**
     * Tokenizer for parsing SVG path expressions.
     */
    private org.jhotdraw.io.StreamPosTokenizer toPathTokenizer;

    /**
     * FontFormatter for parsing font family names.
     */
    private org.jhotdraw.formatter.FontFormatter fontFormatter = new org.jhotdraw.formatter.FontFormatter();

    /**
     * Each SVG element establishes a new Viewport.
     */
    private static class Viewport {
        /**
         * The width of the Viewport.
         */
        public double width = 640.0;

        /**
         * The height of the Viewport.
         */
        public double height = 480.0;

        /**
         * The viewBox specifies the coordinate system within the Viewport.
         */
        public java.awt.geom.Rectangle2D.Double viewBox = new java.awt.geom.Rectangle2D.Double(0.0, 0.0, 640.0, 480.0);

        /**
         * Factor for percent values relative to Viewport width.
         */
        public double widthPercentFactor = 640.0 / 100.0;

        /**
         * Factor for percent values relative to Viewport height.
         */
        public double heightPercentFactor = 480.0 / 100.0;

        /**
         * Factor for number values in the user coordinate system. This is the smaller value of width /
         * viewBox.width and height / viewBox.height.
         */
        public double numberFactor;

        /**
         * http://www.w3.org/TR/SVGMobile12/coords.html#PreserveAspectRatioAttribute XXX - use a more
         * sophisticated variable here
         */
        public boolean isPreserveAspectRatio = true;

        private java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("widthPercentFactor:" + widthPercentFactor) + ";") + "heightPercentFactor:") + heightPercentFactor) + ";") + "numberFactor:") + numberFactor) + ";") + attributes;
        }
    }

    /**
     * Each SVG element creates a new Viewport that we store here.
     */
    private java.util.Stack<org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport> viewportStack;

    /**
     * Holds the style manager used for applying cascading style sheet CSS rules to the document.
     */
    private org.jhotdraw.xml.css.StyleManager styleManager;

    /**
     * Holds the figures that are currently being read.
     */
    private java.util.LinkedList<org.jhotdraw.draw.figure.Figure> figures;

    /**
     * Holds the document that is currently being read.
     */
    private org.w3c.dom.Element document;

    public SVGInputFormat() {
        this(new org.jhotdraw.samples.svg.io.DefaultSVGFigureFactory());
    }

    public SVGInputFormat(org.jhotdraw.samples.svg.io.SVGFigureFactory factory) {
        this.factory = factory;
    }

    public void read(java.io.File file, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        this.url = file.toURI().toURL();
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
        try {
            read(in, drawing, replace);
        } finally {
            in.close();
        }
        this.url = null;
    }

    public void read(java.net.URL url, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        this.url = url;
        java.io.InputStream in = url.openStream();
        try {
            read(in, drawing, replace);
        } finally {
            in.close();
        }
        this.url = null;
    }

    /**
     * This is the main reading method.
     *
     * @param in
     * 		The input stream.
     * @param drawing
     * 		The drawing to which this method adds figures.
     * @param replace
     * 		Whether attributes on the drawing object should by changed by this method. Set
     * 		this to false, when reading individual images from the clipboard.
     */
    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        long start;
        this.figures = new java.util.LinkedList<org.jhotdraw.draw.figure.Figure>();
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.SVGInputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            throw new java.io.IOException(ex);
        }
        try {
            document = ((org.w3c.dom.Element) (builder.parse(in)));
        } catch (org.xml.sax.SAXException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.SVGInputFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            throw new java.io.IOException(ex);
        }
        // Search for the first 'svg' element in the XML document
        // in preorder sequence
        org.w3c.dom.Element svg = document;
        java.util.Stack<org.w3c.dom.Element> stack = new java.util.Stack<org.w3c.dom.Element>();
        // LinkedList<Element> ll = new LinkedList<Element>();
        // ll.add(document);
        stack.push(((org.w3c.dom.Element) (document.getFirstChild())));
        while ((!stack.empty()) && (stack.peek().getNextSibling() != null)) {
            org.w3c.dom.Element iter = stack.peek();
            org.w3c.dom.Element node = ((org.w3c.dom.Element) (iter.getNextSibling()));
            stack.set(stack.indexOf(iter), node);
            org.w3c.dom.Element children = ((org.w3c.dom.Element) (node.getFirstChild()));
            if (iter.getNextSibling() == null) {
                stack.pop();
            }
            if ((children != null) && (children.getNextSibling() != null)) {
                stack.push(children);
            }
            if (((node.getLocalName() != null) && node.getLocalName().equals("svg")) && ((node.getPrefix() == null) || node.getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE))) {
                svg = node;
                break;
            }
        } 
        if (((svg.getLocalName() == null) || (!svg.getLocalName().equals("svg"))) || ((svg.getPrefix() != null) && (!svg.getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE)))) {
            throw new java.io.IOException("'svg' element expected: " + svg.getLocalName());
        }
        // long end1 = System.currentTimeMillis();
        // Flatten CSS Styles
        initStorageContext(document);
        flattenStyles(svg);
        // long end2 = System.currentTimeMillis();
        readElement(svg);
        if (replace) {
            drawing.removeAllChildren();
        }
        drawing.addAll(figures);
        if (replace) {
            org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport viewport = viewportStack.firstElement();
            drawing.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL, org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL.get(viewport.attributes));
            drawing.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL_OPACITY, org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL_OPACITY.get(viewport.attributes));
            drawing.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_HEIGHT, org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_HEIGHT.get(viewport.attributes));
            drawing.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_WIDTH, org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_WIDTH.get(viewport.attributes));
        }
        // Get rid of all objects we don't need anymore to help garbage collector.
        identifiedElements.clear();
        elementObjects.clear();
        viewportStack.clear();
        styleManager.clear();
        document = null;
        identifiedElements = null;
        elementObjects = null;
        viewportStack = null;
        styleManager = null;
    }

    private void initStorageContext(org.w3c.dom.Element root) {
        identifiedElements = new java.util.HashMap<java.lang.String, org.w3c.dom.Element>();
        identifyElements(root);
        elementObjects = new java.util.HashMap<org.w3c.dom.Element, java.lang.Object>();
        viewportStack = new java.util.Stack<org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport>();
        viewportStack.push(new org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport());
        styleManager = new org.jhotdraw.xml.css.StyleManager();
    }

    /**
     * Flattens all CSS styles. Styles defined in a "style" attribute and in CSS rules are converted
     * into attributes with the same name.
     */
    private void flattenStyles(org.w3c.dom.Element elem) throws java.io.IOException {
        if ((((elem.getLocalName() != null) && elem.getLocalName().equals("style")) && readAttribute(elem, "type", "").equals("text/css")) && (elem.getTextContent() != null)) {
            org.jhotdraw.xml.css.CSSParser cssParser = new org.jhotdraw.xml.css.CSSParser();
            cssParser.parse(elem.getTextContent(), styleManager);
        } else if ((elem.getPrefix() == null) || elem.getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE)) {
            java.lang.String style = readAttribute(elem, "style", null);
            if (style != null) {
                for (java.lang.String styleProperty : style.split(";")) {
                    java.lang.String[] stylePropertyElements = styleProperty.split(":");
                    if ((stylePropertyElements.length == 2) && (!elem.hasAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, stylePropertyElements[0].trim()))) {
                        // if (DEBUG) System.out.println("flatten:"+Arrays.toString(stylePropertyElements));
                        elem.setAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, stylePropertyElements[0].trim(), stylePropertyElements[1].trim());
                    }
                }
            }
            styleManager.applyStylesTo(elem);
            org.w3c.dom.NodeList list = elem.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
                flattenStyles(child);
            }
        }
    }

    /**
     * Reads an SVG element of any kind.
     *
     * @return Returns the Figure, if the SVG element represents a Figure. Returns null in all other
    cases.
     */
    private org.jhotdraw.draw.figure.Figure readElement(org.w3c.dom.Element elem) throws java.io.IOException {
        org.jhotdraw.draw.figure.Figure f = null;
        if ((elem.getPrefix() == null) || elem.getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE)) {
            java.lang.String name = elem.getLocalName();
            if (name == null) {
                org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.warning("SVGInputFormat warning: skipping nameless element");
            } else if ("a".equals(name)) {
                f = readAElement(elem);
            } else if ("circle".equals(name)) {
                f = readCircleElement(elem);
            } else if ("defs".equals(name)) {
                readDefsElement(elem);
                f = null;
            } else if ("ellipse".equals(name)) {
                f = readEllipseElement(elem);
            } else if ("g".equals(name)) {
                f = readGElement(elem);
            } else if ("image".equals(name)) {
                f = readImageElement(elem);
            } else if ("line".equals(name)) {
                f = readLineElement(elem);
            } else if ("linearGradient".equals(name)) {
                readLinearGradientElement(elem);
                f = null;
            } else if ("path".equals(name)) {
                f = readPathElement(elem);
            } else if ("polygon".equals(name)) {
                f = readPolygonElement(elem);
            } else if ("polyline".equals(name)) {
                f = readPolylineElement(elem);
            } else if ("radialGradient".equals(name)) {
                readRadialGradientElement(elem);
                f = null;
            } else if ("rect".equals(name)) {
                f = readRectElement(elem);
            } else if ("solidColor".equals(name)) {
                readSolidColorElement(elem);
                f = null;
            } else if ("svg".equals(name)) {
                f = readSVGElement(elem);
                // f = readGElement(elem);
            } else if ("switch".equals(name)) {
                f = readSwitchElement(elem);
            } else if ("text".equals(name)) {
                f = readTextElement(elem);
            } else if ("textArea".equals(name)) {
                f = readTextAreaElement(elem);
            } else if ("title".equals(name)) {
                // FIXME - Implement reading of title element
                // f = readTitleElement(elem);
            } else if ("use".equals(name)) {
                f = readUseElement(elem);
            } else if ("style".equals(name)) {
                // Nothing to do, style elements have been already
                // processed in method flattenStyles
            } else {
                org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.info(("SVGInputFormat not implemented for <" + name) + ">");
            }
        }
        if (f instanceof org.jhotdraw.samples.svg.figures.SVGFigure) {
            if (((org.jhotdraw.samples.svg.figures.SVGFigure) (f)).isEmpty()) {
                // if (DEBUG) System.out.println("Empty figure "+f);
                return null;
            }
        } else if (f != null) {
            org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine("SVGInputFormat warning: not an SVGFigure " + f);
        }
        return f;
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.samples.svg.io.SVGInputFormat.class.getName());

    /**
     * Reads an SVG "defs" element.
     */
    private void readDefsElement(org.w3c.dom.Element elem) throws java.io.IOException {
        org.w3c.dom.Element child = ((org.w3c.dom.Element) (elem.getFirstChild()));
        while (child != null) {
            org.jhotdraw.draw.figure.Figure childFigure = readElement(child);
            child = ((org.w3c.dom.Element) (child.getNextSibling()));
        } 
    }

    /**
     * Reads an SVG "g" element.
     */
    private org.jhotdraw.draw.figure.Figure readGElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readOpacityAttribute(elem, a);
        org.jhotdraw.draw.figure.CompositeFigure g = factory.createG(a);
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            org.jhotdraw.draw.figure.Figure childFigure = readElement(child);
            // skip invisible elements
            if (readAttribute(child, "visibility", "visible").equals("visible") && (!readAttribute(child, "display", "inline").equals("none"))) {
                if (childFigure != null) {
                    g.basicAdd(childFigure);
                }
            }
        }
        readTransformAttribute(elem, a);
        if (org.jhotdraw.draw.AttributeKeys.TRANSFORM.get(a) != null) {
            g.transform(org.jhotdraw.draw.AttributeKeys.TRANSFORM.get(a));
        }
        return g;
    }

    /**
     * Reads an SVG "a" element.
     */
    private org.jhotdraw.draw.figure.Figure readAElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        org.jhotdraw.draw.figure.CompositeFigure g = factory.createG(a);
        java.lang.String href = readAttribute(elem, "xlink:href", null);
        if (href == null) {
            href = readAttribute(elem, "href", null);
        }
        java.lang.String target = readAttribute(elem, "target", null);
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            org.jhotdraw.draw.figure.Figure childFigure = readElement(child);
            // skip invisible elements
            if (readAttribute(child, "visibility", "visible").equals("visible") && (!readAttribute(child, "display", "inline").equals("none"))) {
                if (childFigure != null) {
                    g.basicAdd(childFigure);
                }
            }
            if (childFigure != null) {
                childFigure.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK, href);
                childFigure.attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.LINK_TARGET, target);
            } else {
                org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine("SVGInputFormat <a> has no child figure");
            }
        }
        return g.getChildCount() == 1 ? g.getChild(0) : g;
    }

    /**
     * Reads an SVG "svg" element.
     */
    private org.jhotdraw.draw.figure.Figure readSVGElement(org.w3c.dom.Element elem) throws java.io.IOException {
        // Establish a new viewport
        org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport viewport = new org.jhotdraw.samples.svg.io.SVGInputFormat.Viewport();
        java.lang.String widthValue = readAttribute(elem, "width", "100%");
        java.lang.String heightValue = readAttribute(elem, "height", "100%");
        viewport.width = toWidth(elem, widthValue);
        viewport.height = toHeight(elem, heightValue);
        if (readAttribute(elem, "viewBox", "none").equals("none")) {
            viewport.viewBox.width = viewport.width;
            viewport.viewBox.height = viewport.height;
        } else {
            java.lang.String[] viewBoxValues = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(elem, "viewBox", "none"));
            viewport.viewBox.x = toNumber(elem, viewBoxValues[0]);
            viewport.viewBox.y = toNumber(elem, viewBoxValues[1]);
            viewport.viewBox.width = toNumber(elem, viewBoxValues[2]);
            viewport.viewBox.height = toNumber(elem, viewBoxValues[3]);
            // FIXME - Calculate percentages
            if (widthValue.indexOf('%') > 0) {
                viewport.width = viewport.viewBox.width;
            }
            if (heightValue.indexOf('%') > 0) {
                viewport.height = viewport.viewBox.height;
            }
        }
        if (viewportStack.size() == 1) {
            // We always preserve the aspect ratio for to the topmost SVG element.
            // This is not compliant, but looks much better.
            viewport.isPreserveAspectRatio = true;
        } else {
            viewport.isPreserveAspectRatio = !readAttribute(elem, "preserveAspectRatio", "none").equals("none");
        }
        viewport.widthPercentFactor = viewport.viewBox.width / 100.0;
        viewport.heightPercentFactor = viewport.viewBox.height / 100.0;
        viewport.numberFactor = java.lang.Math.min(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
        java.awt.geom.AffineTransform viewBoxTransform = new java.awt.geom.AffineTransform();
        viewBoxTransform.translate(((-viewport.viewBox.x) * viewport.width) / viewport.viewBox.width, ((-viewport.viewBox.y) * viewport.height) / viewport.viewBox.height);
        if (viewport.isPreserveAspectRatio) {
            double factor = java.lang.Math.min(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
            viewBoxTransform.scale(factor, factor);
        } else {
            viewBoxTransform.scale(viewport.width / viewport.viewBox.width, viewport.height / viewport.viewBox.height);
        }
        viewportStack.push(viewport);
        readViewportAttributes(elem, viewportStack.firstElement().attributes);
        // Read the figures
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            org.jhotdraw.draw.figure.Figure childFigure = readElement(child);
            // skip invisible elements
            if (readAttribute(child, "visibility", "visible").equals("visible") && (!readAttribute(child, "display", "inline").equals("none"))) {
                if (childFigure != null) {
                    childFigure.transform(viewBoxTransform);
                    figures.add(childFigure);
                }
            }
        }
        viewportStack.pop();
        return null;
    }

    /**
     * Reads an SVG "rect" element.
     */
    private org.jhotdraw.draw.figure.Figure readRectElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        double x = toNumber(elem, readAttribute(elem, "x", "0"));
        double y = toNumber(elem, readAttribute(elem, "y", "0"));
        double w = toWidth(elem, readAttribute(elem, "width", "0"));
        double h = toHeight(elem, readAttribute(elem, "height", "0"));
        java.lang.String rxValue = readAttribute(elem, "rx", "none");
        java.lang.String ryValue = readAttribute(elem, "ry", "none");
        if ("none".equals(rxValue)) {
            rxValue = ryValue;
        }
        if ("none".equals(ryValue)) {
            ryValue = rxValue;
        }
        double rx = toNumber(elem, rxValue.equals("none") ? "0" : rxValue);
        double ry = toNumber(elem, ryValue.equals("none") ? "0" : ryValue);
        org.jhotdraw.draw.figure.Figure figure = factory.createRect(x, y, w, h, rx, ry, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "circle" element.
     */
    private org.jhotdraw.draw.figure.Figure readCircleElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        double cx = toWidth(elem, readAttribute(elem, "cx", "0"));
        double cy = toHeight(elem, readAttribute(elem, "cy", "0"));
        double r = toWidth(elem, readAttribute(elem, "r", "0"));
        org.jhotdraw.draw.figure.Figure figure = factory.createCircle(cx, cy, r, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "ellipse" element.
     */
    private org.jhotdraw.draw.figure.Figure readEllipseElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        double cx = toWidth(elem, readAttribute(elem, "cx", "0"));
        double cy = toHeight(elem, readAttribute(elem, "cy", "0"));
        double rx = toWidth(elem, readAttribute(elem, "rx", "0"));
        double ry = toHeight(elem, readAttribute(elem, "ry", "0"));
        org.jhotdraw.draw.figure.Figure figure = factory.createEllipse(cx, cy, rx, ry, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "image" element.
     */
    private org.jhotdraw.draw.figure.Figure readImageElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        double x = toNumber(elem, readAttribute(elem, "x", "0"));
        double y = toNumber(elem, readAttribute(elem, "y", "0"));
        double w = toWidth(elem, readAttribute(elem, "width", "0"));
        double h = toHeight(elem, readAttribute(elem, "height", "0"));
        java.lang.String href = readAttribute(elem, "xlink:href", null);
        if (href == null) {
            href = readAttribute(elem, "href", null);
        }
        byte[] imageData = null;
        if (href != null) {
            if (href.startsWith("data:")) {
                int semicolonPos = href.indexOf(';');
                if (semicolonPos != (-1)) {
                    if (href.indexOf(";base64,") == semicolonPos) {
                        imageData = org.jhotdraw.io.Base64.decode(href.substring(semicolonPos + 8));
                    } else {
                        throw new java.io.IOException("Unsupported encoding in data href in image element:" + href);
                    }
                } else {
                    throw new java.io.IOException("Unsupported data href in image element:" + href);
                }
            } else {
                java.net.URL imageUrl = new java.net.URL(url, href);
                // Check whether the imageURL is an SVG image.
                // Load it as a group.
                if (imageUrl.getFile().endsWith("svg")) {
                    org.jhotdraw.samples.svg.io.SVGInputFormat svgImage = new org.jhotdraw.samples.svg.io.SVGInputFormat(factory);
                    org.jhotdraw.draw.Drawing svgDrawing = new org.jhotdraw.draw.DefaultDrawing();
                    svgImage.read(imageUrl, svgDrawing, true);
                    org.jhotdraw.draw.figure.CompositeFigure svgImageGroup = factory.createG(a);
                    for (org.jhotdraw.draw.figure.Figure f : svgDrawing.getChildren()) {
                        svgImageGroup.add(f);
                    }
                    svgImageGroup.setBounds(new java.awt.geom.Point2D.Double(x, y), new java.awt.geom.Point2D.Double(x + w, y + h));
                    return svgImageGroup;
                }
                // Read the image data from the URL into a byte array
                java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
                byte[] buf = new byte[512];
                int len = 0;
                try {
                    java.io.InputStream in = imageUrl.openStream();
                    try {
                        while ((len = in.read(buf)) > 0) {
                            bout.write(buf, 0, len);
                        } 
                        imageData = bout.toByteArray();
                    } finally {
                        in.close();
                    }
                } catch (java.io.FileNotFoundException e) {
                    // Use empty image
                }
            }
        }
        // Create a buffered image from the image data
        java.awt.image.BufferedImage bufferedImage = null;
        if (imageData != null) {
            try {
                bufferedImage = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(imageData));
            } catch (javax.imageio.IIOException e) {
                java.lang.System.err.println("SVGInputFormat warning: skipped unsupported image format.");
                e.printStackTrace();
            }
        }
        // Delete the image data in case of failure
        if (bufferedImage == null) {
            imageData = null;
            // if (DEBUG) System.out.println("FAILED:"+imageUrl);
        }
        // Create a figure from the image data and the buffered image.
        org.jhotdraw.draw.figure.Figure figure = factory.createImage(x, y, w, h, imageData, bufferedImage, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "line" element.
     */
    private org.jhotdraw.draw.figure.Figure readLineElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readLineAttributes(elem, a);
        // Because 'line' elements are single lines and thus are geometrically
        // one-dimensional, they have no interior; thus, 'line' elements are
        // never filled (see the 'fill' property).
        if ((org.jhotdraw.draw.AttributeKeys.FILL_COLOR.get(a) != null) && (org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.get(a) == null)) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, org.jhotdraw.draw.AttributeKeys.FILL_COLOR.get(a));
        }
        if ((org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.get(a) != null) && (org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.get(a) == null)) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.put(a, org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.get(a));
        }
        org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.put(a, null);
        double x1 = toNumber(elem, readAttribute(elem, "x1", "0"));
        double y1 = toNumber(elem, readAttribute(elem, "y1", "0"));
        double x2 = toNumber(elem, readAttribute(elem, "x2", "0"));
        double y2 = toNumber(elem, readAttribute(elem, "y2", "0"));
        org.jhotdraw.draw.figure.Figure figure = factory.createLine(x1, y1, x2, y2, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "polyline" element.
     */
    private org.jhotdraw.draw.figure.Figure readPolylineElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readLineAttributes(elem, a);
        java.awt.geom.Point2D.Double[] points = toPoints(elem, readAttribute(elem, "points", ""));
        org.jhotdraw.draw.figure.Figure figure = factory.createPolyline(points, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "polygon" element.
     */
    private org.jhotdraw.draw.figure.Figure readPolygonElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        java.awt.geom.Point2D.Double[] points = toPoints(elem, readAttribute(elem, "points", ""));
        org.jhotdraw.draw.figure.Figure figure = factory.createPolygon(points, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "path" element.
     */
    private org.jhotdraw.draw.figure.Figure readPathElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        org.jhotdraw.geom.path.BezierPath[] beziers = toPath(elem, readAttribute(elem, "d", ""));
        org.jhotdraw.draw.figure.Figure figure = factory.createPath(beziers, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "text" element.
     */
    private org.jhotdraw.draw.figure.Figure readTextElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        readFontAttributes(elem, a);
        readTextAttributes(elem, a);
        java.lang.String[] xStr = org.jhotdraw.samples.svg.io.SVGInputFormat.toCommaSeparatedArray(readAttribute(elem, "x", "0"));
        java.lang.String[] yStr = org.jhotdraw.samples.svg.io.SVGInputFormat.toCommaSeparatedArray(readAttribute(elem, "y", "0"));
        java.awt.geom.Point2D.Double[] coordinates = new java.awt.geom.Point2D.Double[java.lang.Math.max(xStr.length, yStr.length)];
        double lastX = 0;
        double lastY = 0;
        for (int i = 0; i < coordinates.length; i++) {
            if (xStr.length > i) {
                try {
                    lastX = toNumber(elem, xStr[i]);
                } catch (java.lang.NumberFormatException ex) {
                    // allow empty
                }
            }
            if (yStr.length > i) {
                try {
                    lastY = toNumber(elem, yStr[i]);
                } catch (java.lang.NumberFormatException ex) {
                    // allow empty
                }
            }
            coordinates[i] = new java.awt.geom.Point2D.Double(lastX, lastY);
        }
        java.lang.String[] rotateStr = org.jhotdraw.samples.svg.io.SVGInputFormat.toCommaSeparatedArray(readAttribute(elem, "rotate", ""));
        double[] rotate = new double[rotateStr.length];
        for (int i = 0; i < rotateStr.length; i++) {
            try {
                rotate[i] = toDouble(elem, rotateStr[i]);
            } catch (java.lang.NumberFormatException ex) {
                rotate[i] = 0;
            }
        }
        javax.swing.text.DefaultStyledDocument doc = new javax.swing.text.DefaultStyledDocument();
        try {
            if (elem.getTextContent() != null) {
                doc.insertString(0, toText(elem, elem.getTextContent()), null);
            } else {
                org.w3c.dom.NodeList list = elem.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    org.w3c.dom.Element node = ((org.w3c.dom.Element) (list.item(i)));
                    if (node.getLocalName() == null) {
                        doc.insertString(0, toText(elem, node.getTextContent()), null);
                    } else if ("tspan".equals(node.getLocalName())) {
                        readTSpanElement(node, doc);
                    } else {
                        org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine(("SVGInputFormat unsupported text node <" + node.getLocalName()) + ">");
                    }
                }
            }
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError ex = new java.lang.InternalError(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        org.jhotdraw.draw.figure.Figure figure = factory.createText(coordinates, rotate, doc, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "textArea" element.
     */
    private org.jhotdraw.draw.figure.Figure readTextAreaElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a);
        readShapeAttributes(elem, a);
        readFontAttributes(elem, a);
        readTextAttributes(elem, a);
        readTextFlowAttributes(elem, a);
        double x = toNumber(elem, readAttribute(elem, "x", "0"));
        double y = toNumber(elem, readAttribute(elem, "y", "0"));
        // XXX - Handle "auto" width and height
        double w = toWidth(elem, readAttribute(elem, "width", "0"));
        double h = toHeight(elem, readAttribute(elem, "height", "0"));
        javax.swing.text.DefaultStyledDocument doc = new javax.swing.text.DefaultStyledDocument();
        try {
            if (elem.getTextContent() != null) {
                doc.insertString(0, toText(elem, elem.getTextContent()), null);
            } else {
                org.w3c.dom.NodeList list = elem.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    org.w3c.dom.Element node = ((org.w3c.dom.Element) (list.item(i)));
                    if (node.getLocalName() == null) {
                        doc.insertString(doc.getLength(), toText(elem, node.getTextContent()), null);
                    } else if ("tbreak".equals(node.getLocalName())) {
                        doc.insertString(doc.getLength(), "\n", null);
                    } else if ("tspan".equals(node.getLocalName())) {
                        readTSpanElement(node, doc);
                    } else {
                        org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine("SVGInputFormat unknown  text node " + node.getLocalName());
                    }
                }
            }
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError ex = new java.lang.InternalError(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        org.jhotdraw.draw.figure.Figure figure = factory.createTextArea(x, y, w, h, doc, a);
        elementObjects.put(elem, figure);
        return figure;
    }

    /**
     * Reads an SVG "tspan" element.
     */
    private void readTSpanElement(org.w3c.dom.Element elem, javax.swing.text.DefaultStyledDocument doc) throws java.io.IOException {
        try {
            if (elem.getTextContent() != null) {
                doc.insertString(doc.getLength(), toText(elem, elem.getTextContent()), null);
            } else {
                org.w3c.dom.NodeList list = elem.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    org.w3c.dom.Element node = ((org.w3c.dom.Element) (list.item(i)));
                    if ((node.getLocalName() != null) && node.getLocalName().equals("tspan")) {
                        readTSpanElement(node, doc);
                    } else {
                        org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.warning("SVGInputFormat unknown text node " + node.getLocalName());
                    }
                }
            }
        } catch (javax.swing.text.BadLocationException e) {
            java.lang.InternalError ex = new java.lang.InternalError(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    private static final java.util.HashSet<java.lang.String> SUPPORTED_FEATURES = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(// "http://www.w3.org/Graphics/SVG/feature/1.2/#ExternalResourcesRequired",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Scripting",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Handler",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Listener",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TimedAnimation",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Animation",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Audio",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Video",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Font",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Extensibility",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#MediaAttribute",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TextFlow",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TransformedVideo",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#ComposedVideo",
    new java.lang.String[]{ "http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-static", // "http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-static-DOM",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-animated",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-all",
    "http://www.w3.org/Graphics/SVG/feature/1.2/#CoreAttribute", // "http://www.w3.org/Graphics/SVG/feature/1.2/#NavigationAttribute",
    "http://www.w3.org/Graphics/SVG/feature/1.2/#Structure", "http://www.w3.org/Graphics/SVG/feature/1.2/#ConditionalProcessing", "http://www.w3.org/Graphics/SVG/feature/1.2/#ConditionalProcessingAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#Image", // "http://www.w3.org/Graphics/SVG/feature/1.2/#Prefetch",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Discard",
    "http://www.w3.org/Graphics/SVG/feature/1.2/#Shape", "http://www.w3.org/Graphics/SVG/feature/1.2/#Text", "http://www.w3.org/Graphics/SVG/feature/1.2/#PaintAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#OpacityAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#GraphicsAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#Gradient", "http://www.w3.org/Graphics/SVG/feature/1.2/#SolidColor", "http://www.w3.org/Graphics/SVG/feature/1.2/#Hyperlinking"// "http://www.w3.org/Graphics/SVG/feature/1.2/#XlinkAttribute",
     }// "http://www.w3.org/Graphics/SVG/feature/1.2/#ExternalResourcesRequired",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Scripting",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Handler",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Listener",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TimedAnimation",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Animation",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Audio",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Video",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Font",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#Extensibility",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#MediaAttribute",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TextFlow",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#TransformedVideo",
    // "http://www.w3.org/Graphics/SVG/feature/1.2/#ComposedVideo",
    ));

    /**
     * Evaluates an SVG "switch" element.
     */
    private org.jhotdraw.draw.figure.Figure readSwitchElement(org.w3c.dom.Element elem) throws java.io.IOException {
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            java.lang.String[] requiredFeatures = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(child, "requiredFeatures", ""));
            java.lang.String[] requiredExtensions = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(child, "requiredExtensions", ""));
            java.lang.String[] systemLanguage = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(child, "systemLanguage", ""));
            java.lang.String[] requiredFormats = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(child, "requiredFormats", ""));
            java.lang.String[] requiredFonts = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(readAttribute(child, "requiredFonts", ""));
            boolean isMatch;
            isMatch = ((org.jhotdraw.samples.svg.io.SVGInputFormat.SUPPORTED_FEATURES.containsAll(java.util.Arrays.asList(requiredFeatures)) && (requiredExtensions.length == 0)) && (requiredFormats.length == 0)) && (requiredFonts.length == 0);
            if (isMatch && (systemLanguage.length > 0)) {
                isMatch = false;
                java.util.Locale locale = org.jhotdraw.util.LocaleUtil.getDefault();
                for (java.lang.String lng : systemLanguage) {
                    int p = lng.indexOf('-');
                    if (p == (-1)) {
                        if (locale.getLanguage().equals(lng)) {
                            isMatch = true;
                            break;
                        }
                    } else if (locale.getLanguage().equals(lng.substring(0, p)) && locale.getCountry().toLowerCase().equals(lng.substring(p + 1))) {
                        isMatch = true;
                        break;
                    }
                }
            }
            if (isMatch) {
                org.jhotdraw.draw.figure.Figure figure = readElement(child);
                if (readAttribute(child, "visibility", "visible").equals("visible") && (!readAttribute(child, "display", "inline").equals("none"))) {
                    return figure;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Reads an SVG "use" element.
     */
    @java.lang.SuppressWarnings("unchecked")
    private org.jhotdraw.draw.figure.Figure readUseElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a2 = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readTransformAttribute(elem, a);
        readOpacityAttribute(elem, a2);
        readUseShapeAttributes(elem, a2);
        readFontAttributes(elem, a2);
        java.lang.String href = readAttribute(elem, "xlink:href", null);
        if ((href != null) && href.startsWith("#")) {
            org.w3c.dom.Element refElem = identifiedElements.get(href.substring(1));
            if (refElem == null) {
                org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.warning("SVGInputFormat couldn't find href for <use> element:" + href);
            } else {
                org.jhotdraw.draw.figure.Figure obj = readElement(refElem);
                if (obj != null) {
                    org.jhotdraw.draw.figure.Figure figure = obj.clone();
                    for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : a2.entrySet()) {
                        figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
                    }
                    java.awt.geom.AffineTransform tx = (org.jhotdraw.draw.AttributeKeys.TRANSFORM.get(a) == null) ? new java.awt.geom.AffineTransform() : org.jhotdraw.draw.AttributeKeys.TRANSFORM.get(a);
                    double x = toNumber(elem, readAttribute(elem, "x", "0"));
                    double y = toNumber(elem, readAttribute(elem, "y", "0"));
                    tx.translate(x, y);
                    figure.transform(tx);
                    return figure;
                }
            }
        }
        return null;
    }

    /**
     * Reads an attribute that is inherited.
     */
    private java.lang.String readInheritAttribute(org.w3c.dom.Element elem, java.lang.String attributeName, java.lang.String defaultValue) {
        if (elem.hasAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName)) {
            java.lang.String value = elem.getAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName);
            if ("inherit".equals(value)) {
                return readInheritAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            } else {
                return value;
            }
        } else if (elem.hasAttribute(attributeName)) {
            java.lang.String value = elem.getAttribute(attributeName);
            if ("inherit".equals(value)) {
                return readInheritAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            } else {
                return value;
            }
        } else if ((elem.getParentNode() != null) && ((elem.getParentNode().getPrefix() == null) || elem.getParentNode().getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE))) {
            return readInheritAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads a color attribute that is inherited. This is similar to {@code readInheritAttribute}, but
     * takes care of the "currentColor" magic attribute value.
     */
    private java.lang.String readInheritColorAttribute(org.w3c.dom.Element elem, java.lang.String attributeName, java.lang.String defaultValue) {
        java.lang.String value = null;
        if (elem.hasAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName)) {
            value = elem.getAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName);
            if ("inherit".equals(value)) {
                return readInheritColorAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            }
        } else if (elem.hasAttribute(attributeName)) {
            value = elem.getAttribute(attributeName);
            if ("inherit".equals(value)) {
                return readInheritColorAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            }
        } else if ((elem.getParentNode() != null) && ((elem.getParentNode().getPrefix() == null) || elem.getParentNode().getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE))) {
            value = readInheritColorAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else {
            value = defaultValue;
        }
        if (((value != null) && value.toLowerCase().equals("currentcolor")) && (!attributeName.equals("color"))) {
            // Lets do some magic stuff for "currentColor" attribute value
            value = readInheritColorAttribute(elem, "color", "defaultValue");
        }
        return value;
    }

    /**
     * Reads a font size attribute that is inherited. As specified by
     * http://www.w3.org/TR/SVGMobile12/text.html#FontPropertiesUsedBySVG
     * http://www.w3.org/TR/2006/CR-xsl11-20060220/#font-getChildCount
     */
    private double readInheritFontSizeAttribute(org.w3c.dom.Element elem, java.lang.String attributeName, java.lang.String defaultValue) throws java.io.IOException {
        java.lang.String value = null;
        if (elem.hasAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName)) {
            value = elem.getAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName);
        } else if (elem.hasAttribute(attributeName)) {
            value = elem.getAttribute(attributeName);
        } else if ((elem.getParentNode() != null) && ((elem.getParentNode().getPrefix() == null) || elem.getParentNode().getPrefix().equals(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE))) {
            return readInheritFontSizeAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else {
            value = defaultValue;
        }
        if ("inherit".equals(value)) {
            return readInheritFontSizeAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else if (org.jhotdraw.samples.svg.SVGConstants.SVG_ABSOLUTE_FONT_SIZES.containsKey(value)) {
            return org.jhotdraw.samples.svg.SVGConstants.SVG_ABSOLUTE_FONT_SIZES.get(value);
        } else if (org.jhotdraw.samples.svg.SVGConstants.SVG_RELATIVE_FONT_SIZES.containsKey(value)) {
            return org.jhotdraw.samples.svg.SVGConstants.SVG_RELATIVE_FONT_SIZES.get(value) * readInheritFontSizeAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else if (value.endsWith("%")) {
            double factor = java.lang.Double.valueOf(value.substring(0, value.length() - 1));
            return factor * readInheritFontSizeAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
        } else {
            // return toScaledNumber(elem, value);
            return toNumber(elem, value);
        }
    }

    /**
     * Reads an attribute that is not inherited, unless its value is "inherit".
     */
    private java.lang.String readAttribute(org.w3c.dom.Element elem, java.lang.String attributeName, java.lang.String defaultValue) {
        if (elem.hasAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName)) {
            java.lang.String value = elem.getAttributeNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, attributeName);
            if ("inherit".equals(value)) {
                return readAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            } else {
                return value;
            }
        } else if (elem.hasAttribute(attributeName)) {
            java.lang.String value = elem.getAttribute(attributeName);
            if ("inherit".equals(value)) {
                return readAttribute(((org.w3c.dom.Element) (elem.getParentNode())), attributeName, defaultValue);
            } else {
                return value;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns a value as a width. http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
    private double toWidth(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        // XXX - Compute xPercentFactor from viewport
        return toLength(elem, str, viewportStack.peek().widthPercentFactor);
    }

    /**
     * Returns a value as a height. http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
    private double toHeight(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        // XXX - Compute yPercentFactor from viewport
        return toLength(elem, str, viewportStack.peek().heightPercentFactor);
    }

    /**
     * Returns a value as a number. http://www.w3.org/TR/SVGMobile12/types.html#DataTypeNumber
     */
    private double toNumber(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        return toLength(elem, str, viewportStack.peek().numberFactor);
    }

    /**
     * Returns a value as a length. http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
    private double toLength(org.w3c.dom.Element elem, java.lang.String str, double percentFactor) throws java.io.IOException {
        double scaleFactor = 1.0;
        if (((str == null) || (str.length() == 0)) || str.equals("none")) {
            return 0.0;
        }
        if (str.endsWith("%")) {
            str = str.substring(0, str.length() - 1);
            scaleFactor = percentFactor;
        } else if (str.endsWith("px")) {
            str = str.substring(0, str.length() - 2);
        } else if (str.endsWith("pt")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 1.25;
        } else if (str.endsWith("pc")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 15;
        } else if (str.endsWith("mm")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 3.543307;
        } else if (str.endsWith("cm")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 35.43307;
        } else if (str.endsWith("in")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 90;
        } else if (str.endsWith("em")) {
            str = str.substring(0, str.length() - 2);
            // XXX - This doesn't work
            scaleFactor = toLength(elem, readAttribute(elem, "font-size", "0"), percentFactor);
        } else {
            scaleFactor = 1.0;
        }
        return java.lang.Double.parseDouble(str) * scaleFactor;
    }

    /**
     * Returns a value as a String array. The values are separated by commas with optional white
     * space.
     */
    public static java.lang.String[] toCommaSeparatedArray(java.lang.String str) throws java.io.IOException {
        return str.split("\\s*,\\s*");
    }

    /**
     * Returns a value as a String array. The values are separated by whitespace or by commas with
     * optional white space.
     */
    public static java.lang.String[] toWSOrCommaSeparatedArray(java.lang.String str) throws java.io.IOException {
        java.lang.String[] result = str.split("(\\s*,\\s*|\\s+)");
        if ((result.length == 1) && result[0].equals("")) {
            return new java.lang.String[0];
        } else {
            return result;
        }
    }

    /**
     * Returns a value as a String array. The values are separated by commas with optional quotes and
     * white space.
     */
    public static java.lang.String[] toQuotedAndCommaSeparatedArray(java.lang.String str) throws java.io.IOException {
        java.util.LinkedList<java.lang.String> values = new java.util.LinkedList<java.lang.String>();
        java.io.StreamTokenizer tt = new java.io.StreamTokenizer(new java.io.StringReader(str));
        tt.wordChars('a', 'z');
        tt.wordChars('A', 'Z');
        tt.wordChars(128 + 32, 255);
        tt.whitespaceChars(0, ' ');
        tt.quoteChar('"');
        tt.quoteChar('\'');
        while (tt.nextToken() != java.io.StreamTokenizer.TT_EOF) {
            switch (tt.ttype) {
                case java.io.StreamTokenizer.TT_WORD :
                case '"' :
                case '\'' :
                    values.add(tt.sval);
                    break;
            }
        } 
        return values.toArray(new java.lang.String[values.size()]);
    }

    /**
     * Returns a value as a Point2D.Double array. as specified in
     * http://www.w3.org/TR/SVGMobile12/shapes.html#PointsBNF
     */
    private java.awt.geom.Point2D.Double[] toPoints(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        java.util.StringTokenizer tt = new java.util.StringTokenizer(str, " ,");
        java.awt.geom.Point2D.Double[] points = new java.awt.geom.Point2D.Double[tt.countTokens() / 2];
        for (int i = 0; i < points.length; i++) {
            points[i] = new java.awt.geom.Point2D.Double(toNumber(elem, tt.nextToken()), toNumber(elem, tt.nextToken()));
        }
        return points;
    }

    /**
     * Returns a value as a BezierPath array. as specified in
     * http://www.w3.org/TR/SVGMobile12/paths.html#PathDataBNF
     *
     * <p>Also supports elliptical arc commands 'a' and 'A' as specified in
     * http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
     */
    private org.jhotdraw.geom.path.BezierPath[] toPath(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        java.util.LinkedList<org.jhotdraw.geom.path.BezierPath> paths = new java.util.LinkedList<org.jhotdraw.geom.path.BezierPath>();
        org.jhotdraw.geom.path.BezierPath path = null;
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D.Double c1 = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D.Double c2 = new java.awt.geom.Point2D.Double();
        org.jhotdraw.io.StreamPosTokenizer tt;
        if (toPathTokenizer == null) {
            tt = new org.jhotdraw.io.StreamPosTokenizer(new java.io.StringReader(str));
            tt.resetSyntax();
            tt.parseNumbers();
            tt.parseExponents();
            tt.parsePlusAsNumber();
            tt.whitespaceChars(0, ' ');
            tt.whitespaceChars(',', ',');
            toPathTokenizer = tt;
        } else {
            tt = toPathTokenizer;
            tt.setReader(new java.io.StringReader(str));
        }
        char nextCommand = 'M';
        char command = 'M';
        Commands : while (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_EOF) {
            if (tt.ttype > 0) {
                command = ((char) (tt.ttype));
            } else {
                command = nextCommand;
                tt.pushBack();
            }
            org.jhotdraw.geom.path.BezierPath.Node node;
            switch (command) {
                case 'M' :
                    // absolute-moveto x y
                    if (path != null) {
                        paths.add(path);
                    }
                    path = new org.jhotdraw.geom.path.BezierPath();
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'M' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'M' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.moveTo(p.x, p.y);
                    nextCommand = 'L';
                    break;
                case 'm' :
                    // relative-moveto dx dy
                    if (path != null) {
                        paths.add(path);
                    }
                    path = new org.jhotdraw.geom.path.BezierPath();
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 'm' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 'm' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.moveTo(p.x, p.y);
                    nextCommand = 'l';
                    break;
                case 'Z' :
                case 'z' :
                    // close path
                    p.x = path.nodes().get(0).x[0];
                    p.y = path.nodes().get(0).y[0];
                    // If the last point and the first point are the same, we
                    // can merge them
                    if (path.size() > 1) {
                        org.jhotdraw.geom.path.BezierPath.Node first = path.nodes().get(0);
                        org.jhotdraw.geom.path.BezierPath.Node last = path.nodes().get(path.size() - 1);
                        if ((first.x[0] == last.x[0]) && (first.y[0] == last.y[0])) {
                            if ((last.mask & org.jhotdraw.geom.path.BezierPath.C1_MASK) != 0) {
                                first.mask |= org.jhotdraw.geom.path.BezierPath.C1_MASK;
                                first.x[1] = last.x[1];
                                first.y[1] = last.y[1];
                            }
                            path.remove(path.size() - 1);
                        }
                    }
                    path.setClosed(true);
                    break;
                case 'L' :
                    // absolute-lineto x y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'L' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'L' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'L';
                    break;
                case 'l' :
                    // relative-lineto dx dy
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 'l' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 'l' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'l';
                    break;
                case 'H' :
                    // absolute-horizontal-lineto x
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'H' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'H';
                    break;
                case 'h' :
                    // relative-horizontal-lineto dx
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 'h' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'h';
                    break;
                case 'V' :
                    // absolute-vertical-lineto y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'V' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'V';
                    break;
                case 'v' :
                    // relative-vertical-lineto dy
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 'v' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.lineTo(p.x, p.y);
                    nextCommand = 'v';
                    break;
                case 'C' :
                    // absolute-curveto x1 y1 x2 y2 x y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x1 coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y1 coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.y = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x2 coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y2 coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.y = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'C' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                    nextCommand = 'C';
                    break;
                case 'c' :
                    // relative-curveto dx1 dy1 dx2 dy2 dx dy
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx1 coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.x = p.x + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy1 coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.y = p.y + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx2 coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.x = p.x + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy2 coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.y = p.y + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 'c' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                    nextCommand = 'c';
                    break;
                case 'S' :
                    // absolute-shorthand-curveto x2 y2 x y
                    node = path.nodes().get(path.size() - 1);
                    c1.x = (node.x[0] * 2.0) - node.x[1];
                    c1.y = (node.y[0] * 2.0) - node.y[1];
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x2 coordinate missing for 'S' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y2 coordinate missing for 'S' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.y = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'S' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'S' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                    nextCommand = 'S';
                    break;
                case 's' :
                    // relative-shorthand-curveto dx2 dy2 dx dy
                    node = path.nodes().get(path.size() - 1);
                    c1.x = (node.x[0] * 2.0) - node.x[1];
                    c1.y = (node.y[0] * 2.0) - node.y[1];
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx2 coordinate missing for 's' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.x = p.x + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy2 coordinate missing for 's' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c2.y = p.y + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 's' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 's' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                    nextCommand = 's';
                    break;
                case 'Q' :
                    // absolute-quadto x1 y1 x y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x1 coordinate missing for 'Q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y1 coordinate missing for 'Q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.y = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'Q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'Q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.quadTo(c1.x, c1.y, p.x, p.y);
                    nextCommand = 'Q';
                    break;
                case 'q' :
                    // relative-quadto dx1 dy1 dx dy
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx1 coordinate missing for 'q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.x = p.x + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy1 coordinate missing for 'q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    c1.y = p.y + tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 'q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 'q' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.quadTo(c1.x, c1.y, p.x, p.y);
                    nextCommand = 'q';
                    break;
                case 'T' :
                    // absolute-shorthand-quadto x y
                    node = path.nodes().get(path.size() - 1);
                    c1.x = (node.x[0] * 2.0) - node.x[1];
                    c1.y = (node.y[0] * 2.0) - node.y[1];
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'T' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'T' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.quadTo(c1.x, c1.y, p.x, p.y);
                    nextCommand = 'T';
                    break;
                case 't' :
                    // relative-shorthand-quadto dx dy
                    node = path.nodes().get(path.size() - 1);
                    c1.x = (node.x[0] * 2.0) - node.x[1];
                    c1.y = (node.y[0] * 2.0) - node.y[1];
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dx coordinate missing for 't' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("dy coordinate missing for 't' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.quadTo(c1.x, c1.y, p.x, p.y);
                    nextCommand = 's';
                    break;
                case 'A' :
                    // absolute-elliptical-arc rx ry x-axis-rotation large-arc-flag sweep-flag x y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("rx coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    // If rX or rY have negative signs, these are dropped;
                    // the absolute value is used instead.
                    double rx = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("ry coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    double ry = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x-axis-rotation missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    double xAxisRotation = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("large-arc-flag missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    boolean largeArcFlag = tt.nval != 0;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("sweep-flag missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    boolean sweepFlag = tt.nval != 0;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y = tt.nval;
                    path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, p.x, p.y);
                    nextCommand = 'A';
                    break;
                case 'a' :
                    // absolute-elliptical-arc rx ry x-axis-rotation large-arc-flag sweep-flag x y
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("rx coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    // If rX or rY have negative signs, these are dropped;
                    // the absolute value is used instead.
                    rx = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("ry coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    ry = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x-axis-rotation missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    xAxisRotation = tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("large-arc-flag missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    largeArcFlag = tt.nval != 0;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("sweep-flag missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    sweepFlag = tt.nval != 0;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("x coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException((("y coordinate missing for 'A' at position " + tt.getStartPosition()) + " in ") + str);
                    }
                    p.y += tt.nval;
                    path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, p.x, p.y);
                    nextCommand = 'a';
                    break;
                default :
                    org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine((("SVGInputFormat.toPath aborting after illegal path command: " + command) + " found in path ") + str);
                    break Commands;
                    // throw new IOException("Illegal command: "+command);
            }
        } 
        if (path != null) {
            paths.add(path);
        }
        return paths.toArray(new org.jhotdraw.geom.path.BezierPath[paths.size()]);
    }

    /* Reads core attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#CoreAttribute
     */
    private void readCoreAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        // read "id" or "xml:id"
        // identifiedElements.putx(elem.get("id"), elem);
        // identifiedElements.putx(elem.get("xml:id"), elem);
        // XXX - Add
        // xml:base
        // xml:lang
        // xml:space
        // class
    }

    /**
     * Puts all elments with an "id" or an "xml:id" attribute into the hashtable {@code identifiedElements}.
     */
    private void identifyElements(org.w3c.dom.Element elem) {
        identifiedElements.put(elem.getAttribute("id"), elem);
        identifiedElements.put(elem.getAttribute("xml:id"), elem);
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            identifyElements(child);
        }
    }

    /* Reads object/group opacity as described in
    http://www.w3.org/TR/SVGMobile12/painting.html#groupOpacity
     */
    private void readOpacityAttribute(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
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
        double value = toDouble(elem, readAttribute(elem, "opacity", "1"), 1, 0, 1);
        org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY.put(a, value);
    }

    /* Reads text attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#Text
     */
    private void readTextAttributes(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object value;
        // 'text-anchor'
        // Value:   start | middle | end | inherit
        // Initial:   start
        // Applies to:   'text' Element
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "text-anchor", "start");
        if (org.jhotdraw.samples.svg.SVGConstants.SVG_TEXT_ANCHORS.get(value) != null) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ANCHOR.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_TEXT_ANCHORS.get(value));
        }
        // 'display-align'
        // Value:   auto | before | center | after | inherit
        // Initial:   auto
        // Applies to:   'textArea'
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "display-align", "auto");
        // XXX - Implement me properly
        if (!value.equals("auto")) {
            if ("center".equals(value)) {
                org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ANCHOR.put(a, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.MIDDLE);
            } else if ("before".equals(value)) {
                org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ANCHOR.put(a, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.END);
            }
        }
        // text-align
        // Value:  start | end | center | inherit
        // Initial:  start
        // Applies to:  textArea elements
        // Inherited:  yes
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        value = readInheritAttribute(elem, "text-align", "start");
        // XXX - Implement me properly
        if (!value.equals("start")) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.TEXT_ALIGN.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_TEXT_ALIGNS.get(value));
        }
    }

    /* Reads text flow attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#TextFlow
     */
    private void readTextFlowAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object value;
        // 'line-increment'
        // Value:   auto | <number> | inherit
        // Initial:   auto
        // Applies to:   'textArea'
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "line-increment", "auto");
    }

    /* Reads the transform attribute as specified in
    http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
    private void readTransformAttribute(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.String value;
        value = readAttribute(elem, "transform", "none");
        if (!value.equals("none")) {
            org.jhotdraw.draw.AttributeKeys.TRANSFORM.put(a, org.jhotdraw.samples.svg.io.SVGInputFormat.toTransform(elem, value));
        }
    }

    /* Reads solid color attributes. */
    private void readSolidColorElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        // 'solid-color'
        // Value:  currentColor | <color> | inherit
        // Initial:  black
        // Applies to:  'solidColor' elements
        // Inherited:  no
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        // Computed value:    Specified <color> value, except inherit
        java.awt.Color color = toColor(elem, readAttribute(elem, "solid-color", "black"));
        // 'solid-opacity'
        // Value: <opacity-value> | inherit
        // Initial:  1
        // Applies to:  'solidColor' elements
        // Inherited:  no
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        // Computed value:    Specified value, except inherit
        double opacity = toDouble(elem, readAttribute(elem, "solid-opacity", "1"), 1, 0, 1);
        if (opacity != 1) {
            color = new java.awt.Color((((int) (255 * opacity)) << 24) | (0xffffff & color.getRGB()), true);
        }
        elementObjects.put(elem, color);
    }

    /**
     * Reads shape attributes.
     */
    private void readShapeAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object objectValue;
        java.lang.String value;
        double doubleValue;
        // 'color'
        // Value:   <color> | inherit
        // Initial:    depends on user agent
        // Applies to:   None. Indirectly affects other properties via currentColor
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified <color> value, except inherit
        // value = readInheritAttribute(elem, "color", "black");
        // if (DEBUG) System.out.println("color="+value);
        // 'color-rendering'
        // Value:    auto | optimizeSpeed | optimizeQuality | inherit
        // Initial:    auto
        // Applies to:    container elements , graphics elements and 'animateColor'
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        // value = readInheritAttribute(elem, "color-rendering", "auto");
        // if (DEBUG) System.out.println("color-rendering="+value);
        // 'fill'
        // Value:   <paint> | inherit (See Specifying paint)
        // Initial:    black
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    "none", system paint, specified <color> value or absolute IRI
        objectValue = toPaint(elem, readInheritColorAttribute(elem, "fill", "black"));
        if (objectValue instanceof java.awt.Color) {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, ((java.awt.Color) (objectValue)));
        } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.putClone(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
        } else if (objectValue == null) {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
        } else {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
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
        objectValue = readInheritAttribute(elem, "fill-opacity", "1");
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        // 'fill-rule'
        // Value:  nonzero | evenodd | inherit
        // Initial:   nonzero
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "fill-rule", "nonzero");
        org.jhotdraw.draw.AttributeKeys.WINDING_RULE.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_FILL_RULES.get(value));
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
        objectValue = toPaint(elem, readInheritColorAttribute(elem, "stroke", "none"));
        if (objectValue instanceof java.awt.Color) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, ((java.awt.Color) (objectValue)));
        } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.putClone(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
        } else if (objectValue == null) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, null);
        } else {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, null);
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
        value = readInheritAttribute(elem, "stroke-dasharray", "none");
        if (!value.equals("none")) {
            java.lang.String[] values = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(value);
            double[] dashes = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                dashes[i] = toNumber(elem, values[i]);
            }
            org.jhotdraw.draw.AttributeKeys.STROKE_DASHES.put(a, dashes);
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
        doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-dashoffset", "0"));
        org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE.put(a, doubleValue);
        org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR.put(a, false);
        // 'stroke-linecap'
        // Value:    butt | round | square | inherit
        // Initial:    butt
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linecap", "butt");
        org.jhotdraw.draw.AttributeKeys.STROKE_CAP.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINECAPS.get(value));
        // 'stroke-linejoin'
        // Value:    miter | round | bevel | inherit
        // Initial:    miter
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linejoin", "miter");
        org.jhotdraw.draw.AttributeKeys.STROKE_JOIN.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINEJOINS.get(value));
        // 'stroke-miterlimit'
        // Value:    <miterlimit> | inherit
        // Initial:    4
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        doubleValue = toDouble(elem, readInheritAttribute(elem, "stroke-miterlimit", "4"), 4.0, 1.0, java.lang.Double.MAX_VALUE);
        org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT.put(a, doubleValue);
        org.jhotdraw.draw.AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR.put(a, false);
        // 'stroke-opacity'
        // Value:    <opacity-value> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        objectValue = readInheritAttribute(elem, "stroke-opacity", "1");
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        // 'stroke-width'
        // Value:   <length> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-width", "1"));
        org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.put(a, doubleValue);
    }

    /* Reads shape attributes for the SVG "use" element. */
    private void readUseShapeAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object objectValue;
        java.lang.String value;
        double doubleValue;
        // 'color'
        // Value:   <color> | inherit
        // Initial:    depends on user agent
        // Applies to:   None. Indirectly affects other properties via currentColor
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified <color> value, except inherit
        // value = readInheritAttribute(elem, "color", "black");
        // if (DEBUG) System.out.println("color="+value);
        // 'color-rendering'
        // Value:    auto | optimizeSpeed | optimizeQuality | inherit
        // Initial:    auto
        // Applies to:    container elements , graphics elements and 'animateColor'
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        // value = readInheritAttribute(elem, "color-rendering", "auto");
        // if (DEBUG) System.out.println("color-rendering="+value);
        // 'fill'
        // Value:   <paint> | inherit (See Specifying paint)
        // Initial:    black
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    "none", system paint, specified <color> value or absolute IRI
        objectValue = readInheritColorAttribute(elem, "fill", null);
        if (objectValue != null) {
            objectValue = toPaint(elem, ((java.lang.String) (objectValue)));
            if (objectValue instanceof java.awt.Color) {
                org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, ((java.awt.Color) (objectValue)));
            } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
                org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.put(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
            } else if (objectValue == null) {
                org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
            } else {
                org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
            }
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
        objectValue = readInheritAttribute(elem, "fill-opacity", null);
        if (objectValue != null) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        }
        // 'fill-rule'
        // Value:  nonzero | evenodd | inherit
        // Initial:   nonzero
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "fill-rule", null);
        if (value != null) {
            org.jhotdraw.draw.AttributeKeys.WINDING_RULE.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_FILL_RULES.get(value));
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
        objectValue = toPaint(elem, readInheritColorAttribute(elem, "stroke", null));
        if (objectValue != null) {
            if (objectValue instanceof java.awt.Color) {
                org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, ((java.awt.Color) (objectValue)));
            } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
                org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.put(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
            }
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
        value = readInheritAttribute(elem, "stroke-dasharray", null);
        if ((value != null) && (!value.equals("none"))) {
            java.lang.String[] values = org.jhotdraw.samples.svg.io.SVGInputFormat.toCommaSeparatedArray(value);
            double[] dashes = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                dashes[i] = toNumber(elem, values[i]);
            }
            org.jhotdraw.draw.AttributeKeys.STROKE_DASHES.put(a, dashes);
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
        objectValue = readInheritAttribute(elem, "stroke-dashoffset", null);
        if (objectValue != null) {
            doubleValue = toNumber(elem, ((java.lang.String) (objectValue)));
            org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE.put(a, doubleValue);
            org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR.put(a, false);
        }
        // 'stroke-linecap'
        // Value:    butt | round | square | inherit
        // Initial:    butt
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linecap", null);
        if (value != null) {
            org.jhotdraw.draw.AttributeKeys.STROKE_CAP.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINECAPS.get(value));
        }
        // 'stroke-linejoin'
        // Value:    miter | round | bevel | inherit
        // Initial:    miter
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linejoin", null);
        if (value != null) {
            org.jhotdraw.draw.AttributeKeys.STROKE_JOIN.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINEJOINS.get(value));
        }
        // 'stroke-miterlimit'
        // Value:    <miterlimit> | inherit
        // Initial:    4
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        objectValue = readInheritAttribute(elem, "stroke-miterlimit", null);
        if (objectValue != null) {
            doubleValue = toDouble(elem, ((java.lang.String) (objectValue)), 4.0, 1.0, java.lang.Double.MAX_VALUE);
            org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT.put(a, doubleValue);
            org.jhotdraw.draw.AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR.put(a, false);
        }
        // 'stroke-opacity'
        // Value:    <opacity-value> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        objectValue = readInheritAttribute(elem, "stroke-opacity", null);
        if (objectValue != null) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        }
        // 'stroke-width'
        // Value:   <length> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        objectValue = readInheritAttribute(elem, "stroke-width", null);
        if (objectValue != null) {
            doubleValue = toNumber(elem, ((java.lang.String) (objectValue)));
            org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.put(a, doubleValue);
        }
    }

    /**
     * Reads line and polyline attributes.
     */
    private void readLineAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object objectValue;
        java.lang.String value;
        double doubleValue;
        // 'color'
        // Value:   <color> | inherit
        // Initial:    depends on user agent
        // Applies to:   None. Indirectly affects other properties via currentColor
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified <color> value, except inherit
        // value = readInheritAttribute(elem, "color", "black");
        // if (DEBUG) System.out.println("color="+value);
        // 'color-rendering'
        // Value:    auto | optimizeSpeed | optimizeQuality | inherit
        // Initial:    auto
        // Applies to:    container elements , graphics elements and 'animateColor'
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        // value = readInheritAttribute(elem, "color-rendering", "auto");
        // if (DEBUG) System.out.println("color-rendering="+value);
        // 'fill'
        // Value:   <paint> | inherit (See Specifying paint)
        // Initial:    black
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    "none", system paint, specified <color> value or absolute IRI
        objectValue = toPaint(elem, readInheritColorAttribute(elem, "fill", "none"));
        if (objectValue instanceof java.awt.Color) {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, ((java.awt.Color) (objectValue)));
        } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.putClone(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
        } else if (objectValue == null) {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
        } else {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, null);
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
        objectValue = readInheritAttribute(elem, "fill-opacity", "1");
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        // 'fill-rule'
        // Value:  nonzero | evenodd | inherit
        // Initial:   nonzero
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "fill-rule", "nonzero");
        org.jhotdraw.draw.AttributeKeys.WINDING_RULE.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_FILL_RULES.get(value));
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
        objectValue = toPaint(elem, readInheritColorAttribute(elem, "stroke", "black"));
        if (objectValue instanceof java.awt.Color) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, ((java.awt.Color) (objectValue)));
        } else if (objectValue instanceof org.jhotdraw.samples.svg.Gradient) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.putClone(a, ((org.jhotdraw.samples.svg.Gradient) (objectValue)));
        } else if (objectValue == null) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, null);
        } else {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, null);
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
        value = readInheritAttribute(elem, "stroke-dasharray", "none");
        if (!value.equals("none")) {
            java.lang.String[] values = org.jhotdraw.samples.svg.io.SVGInputFormat.toWSOrCommaSeparatedArray(value);
            double[] dashes = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                dashes[i] = toNumber(elem, values[i]);
            }
            org.jhotdraw.draw.AttributeKeys.STROKE_DASHES.put(a, dashes);
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
        doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-dashoffset", "0"));
        org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE.put(a, doubleValue);
        org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR.put(a, false);
        // 'stroke-linecap'
        // Value:    butt | round | square | inherit
        // Initial:    butt
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linecap", "butt");
        org.jhotdraw.draw.AttributeKeys.STROKE_CAP.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINECAPS.get(value));
        // 'stroke-linejoin'
        // Value:    miter | round | bevel | inherit
        // Initial:    miter
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "stroke-linejoin", "miter");
        org.jhotdraw.draw.AttributeKeys.STROKE_JOIN.put(a, org.jhotdraw.samples.svg.SVGConstants.SVG_STROKE_LINEJOINS.get(value));
        // 'stroke-miterlimit'
        // Value:    <miterlimit> | inherit
        // Initial:    4
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        doubleValue = toDouble(elem, readInheritAttribute(elem, "stroke-miterlimit", "4"), 4.0, 1.0, java.lang.Double.MAX_VALUE);
        org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT.put(a, doubleValue);
        org.jhotdraw.draw.AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR.put(a, false);
        // 'stroke-opacity'
        // Value:    <opacity-value> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        objectValue = readInheritAttribute(elem, "stroke-opacity", "1");
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY.put(a, toDouble(elem, ((java.lang.String) (objectValue)), 1.0, 0.0, 1.0));
        // 'stroke-width'
        // Value:   <length> | inherit
        // Initial:    1
        // Applies to:    shapes and text content elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        doubleValue = toNumber(elem, readInheritAttribute(elem, "stroke-width", "1"));
        org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.put(a, doubleValue);
    }

    /* Reads viewport attributes. */
    private void readViewportAttributes(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        java.lang.Object value;
        java.lang.Double doubleValue;
        // width of the viewport
        value = readAttribute(elem, "width", null);
        org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine((("SVGInputFormat READ viewport w/h factors:" + viewportStack.peek().widthPercentFactor) + ",") + viewportStack.peek().heightPercentFactor);
        if (value != null) {
            doubleValue = toLength(elem, ((java.lang.String) (value)), viewportStack.peek().widthPercentFactor);
            org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_WIDTH.put(a, doubleValue);
        }
        // height of the viewport
        value = readAttribute(elem, "height", null);
        if (value != null) {
            doubleValue = toLength(elem, ((java.lang.String) (value)), viewportStack.peek().heightPercentFactor);
            org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_HEIGHT.put(a, doubleValue);
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
        value = toPaint(elem, readInheritColorAttribute(elem, "viewport-fill", "none"));
        if ((value == null) || (value instanceof java.awt.Color)) {
            org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL.put(a, ((java.awt.Color) (value)));
        }
        // 'viewport-fill-opacity'
        // Value: <opacity-value> | inherit
        // Initial:  1.0
        // Applies to: viewport-creating elements
        // Inherited:  no
        // Percentages:  N/A
        // Media:  visual
        // Animatable:  yes
        // Computed value:    Specified value, except inherit
        doubleValue = toDouble(elem, readAttribute(elem, "viewport-fill-opacity", "1.0"));
        org.jhotdraw.samples.svg.SVGAttributeKeys.VIEWPORT_FILL_OPACITY.put(a, doubleValue);
    }

    /* Reads graphics attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#GraphicsAttribute
     */
    private void readGraphicsAttributes(org.w3c.dom.Element elem, org.jhotdraw.draw.figure.Figure f) throws java.io.IOException {
        java.lang.Object value;
        // 'display'
        // Value:    inline | block | list-item |
        // run-in | compact | marker |
        // table | inline-table | table-row-group | table-header-group |
        // table-footer-group | table-row | table-column-group | table-column |
        // table-cell | table-caption | none | inherit
        // Initial:    inline
        // Applies to:    'svg' , 'g' , 'switch' , 'a' , 'foreignObject' ,
        // graphics elements (including the text content block elements) and text
        // sub-elements (for example, 'tspan' and 'a' )
        // Inherited:    no
        // Percentages:    N/A
        // Media:    all
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readAttribute(elem, "display", "inline");
        // 'image-rendering'
        // Value:    auto | optimizeSpeed | optimizeQuality | inherit
        // Initial:    auto
        // Applies to:    images
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "image-rendering", "auto");
        // 'pointer-events'
        // Value:   boundingBox | visiblePainted | visibleFill | visibleStroke | visible |
        // painted | fill | stroke | all | none | inherit
        // Initial:   visiblePainted
        // Applies to:   graphics elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:   Specified value, except inherit
        value = readInheritAttribute(elem, "pointer-events", "visiblePainted");
        // 'shape-rendering'
        // Value:    auto | optimizeSpeed | crispEdges |
        // geometricPrecision | inherit
        // Initial:    auto
        // Applies to:    shapes
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "shape-rendering", "auto");
        // 'text-rendering'
        // Value:    auto | optimizeSpeed | optimizeLegibility |
        // geometricPrecision | inherit
        // Initial:    auto
        // Applies to:   text content block elements
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "text-rendering", "auto");
        // 'vector-effect'
        // Value:    non-scaling-stroke | none | inherit
        // Initial:    none
        // Applies to:    graphics elements
        // Inherited:    no
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readAttribute(elem, "vector-effect", "none");
        // 'visibility'
        // Value:    visible | hidden | collapse | inherit
        // Initial:    visible
        // Applies to:    graphics elements (including the text content block
        // elements) and text sub-elements (for example, 'tspan' and 'a' )
        // Inherited:    yes
        // Percentages:    N/A
        // Media:    visual
        // Animatable:    yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "visibility", null);
    }

    /**
     * Reads an SVG "linearGradient" element.
     */
    private void readLinearGradientElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        double x1 = toLength(elem, readAttribute(elem, "x1", "0"), 0.01);
        double y1 = toLength(elem, readAttribute(elem, "y1", "0"), 0.01);
        double x2 = toLength(elem, readAttribute(elem, "x2", "1"), 0.01);
        double y2 = toLength(elem, readAttribute(elem, "y2", "0"), 0.01);
        boolean isRelativeToFigureBounds = readAttribute(elem, "gradientUnits", "objectBoundingBox").equals("objectBoundingBox");
        org.w3c.dom.NodeList stops = elem.getElementsByTagNameNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, "stop");
        if (stops.getLength() == 0) {
            stops = elem.getElementsByTagName("stop");
        }
        if (stops.getLength() == 0) {
            // FIXME - Implement xlink support throughouth SVGInputFormat
            java.lang.String xlink = readAttribute(elem, "xlink:href", "");
            if (xlink.startsWith("#") && (identifiedElements.get(xlink.substring(1)) != null)) {
                stops = identifiedElements.get(xlink.substring(1)).getElementsByTagNameNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, "stop");
                if (stops.getLength() == 0) {
                    stops = identifiedElements.get(xlink.substring(1)).getElementsByTagName("stop");
                }
            }
        }
        if (stops.getLength() == 0) {
            org.jhotdraw.samples.svg.io.SVGInputFormat.LOG.fine("SVGInpuFormat: Warning no stops in linearGradient " + elem);
        }
        double[] stopOffsets = new double[stops.getLength()];
        java.awt.Color[] stopColors = new java.awt.Color[stops.getLength()];
        double[] stopOpacities = new double[stops.getLength()];
        for (int i = 0; i < stops.getLength(); i++) {
            org.w3c.dom.Element stopElem = ((org.w3c.dom.Element) (stops.item(i)));
            java.lang.String offsetStr = readAttribute(stopElem, "offset", "0");
            if (offsetStr.endsWith("%")) {
                stopOffsets[i] = toDouble(stopElem, offsetStr.substring(0, offsetStr.length() - 1), 0, 0, 100) / 100.0;
            } else {
                stopOffsets[i] = toDouble(stopElem, offsetStr, 0, 0, 1);
            }
            // 'stop-color'
            // Value:   currentColor | <color> | inherit
            // Initial:   black
            // Applies to:    'stop' elements
            // Inherited:   no
            // Percentages:   N/A
            // Media:   visual
            // Animatable:   yes
            // Computed value:    Specified <color> value, except i
            stopColors[i] = toColor(stopElem, readAttribute(stopElem, "stop-color", "black"));
            if (stopColors[i] == null) {
                stopColors[i] = new java.awt.Color(0x0, true);
                // throw new IOException("stop color missing in "+stopElem);
            }
            // 'stop-opacity'
            // Value:   <opacity-value> | inherit
            // Initial:   1
            // Applies to:    'stop' elements
            // Inherited:   no
            // Percentages:   N/A
            // Media:   visual
            // Animatable:   yes
            // Computed value:    Specified value, except inherit
            stopOpacities[i] = toDouble(stopElem, readAttribute(stopElem, "stop-opacity", "1"), 1, 0, 1);
        }
        java.awt.geom.AffineTransform tx = org.jhotdraw.samples.svg.io.SVGInputFormat.toTransform(elem, readAttribute(elem, "gradientTransform", "none"));
        org.jhotdraw.samples.svg.Gradient gradient = factory.createLinearGradient(x1, y1, x2, y2, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
        elementObjects.put(elem, gradient);
    }

    /**
     * Reads an SVG "radialGradient" element.
     */
    private void readRadialGradientElement(org.w3c.dom.Element elem) throws java.io.IOException {
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
        readCoreAttributes(elem, a);
        double cx = toLength(elem, readAttribute(elem, "cx", "0.5"), 0.01);
        double cy = toLength(elem, readAttribute(elem, "cy", "0.5"), 0.01);
        double fx = toLength(elem, readAttribute(elem, "fx", readAttribute(elem, "cx", "0.5")), 0.01);
        double fy = toLength(elem, readAttribute(elem, "fy", readAttribute(elem, "cy", "0.5")), 0.01);
        double r = toLength(elem, readAttribute(elem, "r", "0.5"), 0.01);
        boolean isRelativeToFigureBounds = readAttribute(elem, "gradientUnits", "objectBoundingBox").equals("objectBoundingBox");
        org.w3c.dom.NodeList stops = elem.getElementsByTagNameNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, "stop");
        if (stops.getLength() == 0) {
            stops = elem.getElementsByTagName("stop");
        }
        if (stops.getLength() == 0) {
            // FIXME - Implement xlink support throughout SVGInputFormat
            java.lang.String xlink = readAttribute(elem, "xlink:href", "");
            if (xlink.startsWith("#") && (identifiedElements.get(xlink.substring(1)) != null)) {
                stops = identifiedElements.get(xlink.substring(1)).getElementsByTagNameNS(org.jhotdraw.samples.svg.SVGConstants.SVG_NAMESPACE, "stop");
                if (stops.getLength() == 0) {
                    stops = identifiedElements.get(xlink.substring(1)).getElementsByTagName("stop");
                }
            }
        }
        double[] stopOffsets = new double[stops.getLength()];
        java.awt.Color[] stopColors = new java.awt.Color[stops.getLength()];
        double[] stopOpacities = new double[stops.getLength()];
        for (int i = 0; i < stops.getLength(); i++) {
            org.w3c.dom.Element stopElem = ((org.w3c.dom.Element) (stops.item(i)));
            java.lang.String offsetStr = readAttribute(stopElem, "offset", "0");
            if (offsetStr.endsWith("%")) {
                stopOffsets[i] = toDouble(stopElem, offsetStr.substring(0, offsetStr.length() - 1), 0, 0, 100) / 100.0;
            } else {
                stopOffsets[i] = toDouble(stopElem, offsetStr, 0, 0, 1);
            }
            // 'stop-color'
            // Value:   currentColor | <color> | inherit
            // Initial:   black
            // Applies to:    'stop' elements
            // Inherited:   no
            // Percentages:   N/A
            // Media:   visual
            // Animatable:   yes
            // Computed value:    Specified <color> value, except i
            stopColors[i] = toColor(stopElem, readAttribute(stopElem, "stop-color", "black"));
            if (stopColors[i] == null) {
                stopColors[i] = new java.awt.Color(0x0, true);
                // throw new IOException("stop color missing in "+stopElem);
            }
            // 'stop-opacity'
            // Value:   <opacity-value> | inherit
            // Initial:   1
            // Applies to:    'stop' elements
            // Inherited:   no
            // Percentages:   N/A
            // Media:   visual
            // Animatable:   yes
            // Computed value:    Specified value, except inherit
            stopOpacities[i] = toDouble(stopElem, readAttribute(stopElem, "stop-opacity", "1"), 1, 0, 1);
        }
        java.awt.geom.AffineTransform tx = org.jhotdraw.samples.svg.io.SVGInputFormat.toTransform(elem, readAttribute(elem, "gradientTransform", "none"));
        org.jhotdraw.samples.svg.Gradient gradient = factory.createRadialGradient(cx, cy, fx, fy, r, stopOffsets, stopColors, stopOpacities, isRelativeToFigureBounds, tx);
        elementObjects.put(elem, gradient);
    }

    /* Reads font attributes as listed in
    http://www.w3.org/TR/SVGMobile12/feature.html#Font
     */
    private void readFontAttributes(org.w3c.dom.Element elem, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
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
        value = readInheritAttribute(elem, "font-family", "Dialog");
        java.lang.String[] familyNames = org.jhotdraw.samples.svg.io.SVGInputFormat.toQuotedAndCommaSeparatedArray(value);
        java.awt.Font font = null;
        // Try to find a font with exactly matching name
        for (int i = 0; i < familyNames.length; i++) {
            try {
                font = ((java.awt.Font) (fontFormatter.stringToValue(familyNames[i])));
                break;
            } catch (java.text.ParseException e) {
                // allow empty
            }
        }
        if (font == null) {
            // Try to create a similar font using the first name in the list
            if (familyNames.length > 0) {
                fontFormatter.setAllowsUnknownFont(true);
                try {
                    font = ((java.awt.Font) (fontFormatter.stringToValue(familyNames[0])));
                } catch (java.text.ParseException e) {
                    // allow empty
                }
                fontFormatter.setAllowsUnknownFont(false);
            }
        }
        if (font == null) {
            // Fallback to the system Dialog font
            font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
        }
        org.jhotdraw.draw.AttributeKeys.FONT_FACE.put(a, font);
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
        doubleValue = readInheritFontSizeAttribute(elem, "font-size", "medium");
        org.jhotdraw.draw.AttributeKeys.FONT_SIZE.put(a, doubleValue);
        // 'font-style'
        // Value:   normal | italic | oblique | inherit
        // Initial:   normal
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "font-style", "normal");
        org.jhotdraw.draw.AttributeKeys.FONT_ITALIC.put(a, value.equals("italic"));
        // 'font-variant'
        // Value:   normal | small-caps | inherit
        // Initial:   normal
        // Applies to:   text content elements
        // Inherited:   yes
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   no
        // Computed value:    Specified value, except inherit
        value = readInheritAttribute(elem, "font-variant", "normal");
        // if (DEBUG) System.out.println("font-variant="+value);
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
        value = readInheritAttribute(elem, "font-weight", "normal");
        org.jhotdraw.draw.AttributeKeys.FONT_BOLD.put(a, ((((((value.equals("bold") || value.equals("bolder")) || value.equals("400")) || value.equals("500")) || value.equals("600")) || value.equals("700")) || value.equals("800")) || value.equals("900"));
        // Note: text-decoration is an SVG 1.1 feature
        // 'text-decoration'
        // Value:   none | [ underline || overline || line-through || blink ] | inherit
        // Initial:   none
        // Applies to:   text content elements
        // Inherited:   no (see prose)
        // Percentages:   N/A
        // Media:   visual
        // Animatable:   yes
        value = readAttribute(elem, "text-decoration", "none");
        org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE.put(a, value.equals("underline"));
    }

    /**
     * Reads a paint style attribute. This can be a Color or a Gradient or null. XXX - Doesn't support
     * url(...) colors yet.
     */
    private java.lang.Object toPaint(org.w3c.dom.Element elem, java.lang.String value) throws java.io.IOException {
        java.lang.String str = value;
        if (str == null) {
            return null;
        }
        str = str.trim().toLowerCase();
        if ("none".equals(str)) {
            return null;
        } else if ("currentcolor".equals(str)) {
            java.lang.String currentColor = readInheritAttribute(elem, "color", "black");
            if ((currentColor == null) || currentColor.trim().toLowerCase().equals("currentColor")) {
                return null;
            } else {
                return toPaint(elem, currentColor);
            }
        } else if (org.jhotdraw.samples.svg.SVGConstants.SVG_COLORS.containsKey(str)) {
            return org.jhotdraw.samples.svg.SVGConstants.SVG_COLORS.get(str);
        } else if (str.startsWith("#") && (str.length() == 7)) {
            return new java.awt.Color(java.lang.Integer.decode(str));
        } else if (str.startsWith("#") && (str.length() == 4)) {
            // Three digits hex value
            int th = java.lang.Integer.decode(str);
            return new java.awt.Color((((((th & 0xf) | ((th & 0xf) << 4)) | ((th & 0xf0) << 4)) | ((th & 0xf0) << 8)) | ((th & 0xf00) << 8)) | ((th & 0xf00) << 12));
        } else if (str.startsWith("rgb")) {
            try {
                java.util.StringTokenizer tt = new java.util.StringTokenizer(str, "() ,");
                tt.nextToken();
                java.lang.String r = tt.nextToken();
                java.lang.String g = tt.nextToken();
                java.lang.String b = tt.nextToken();
                java.awt.Color c = new java.awt.Color(r.endsWith("%") ? ((int) (java.lang.Double.parseDouble(r.substring(0, r.length() - 1)) * 2.55)) : java.lang.Integer.decode(r), g.endsWith("%") ? ((int) (java.lang.Double.parseDouble(g.substring(0, g.length() - 1)) * 2.55)) : java.lang.Integer.decode(g), b.endsWith("%") ? ((int) (java.lang.Double.parseDouble(b.substring(0, b.length() - 1)) * 2.55)) : java.lang.Integer.decode(b));
                return c;
            } catch (java.lang.Exception e) {
                java.lang.System.out.println("SVGInputFormat.toPaint illegal RGB value " + str);
                e.printStackTrace();
                return null;
            }
        } else if (str.startsWith("url(")) {
            java.lang.String href = value.substring(4, value.length() - 1);
            if (identifiedElements.containsKey(href.substring(1)) && elementObjects.containsKey(identifiedElements.get(href.substring(1)))) {
                java.lang.Object obj = elementObjects.get(identifiedElements.get(href.substring(1)));
                return obj;
            }
            // XXX - Implement me
            return null;
        } else {
            return null;
        }
    }

    /**
     * Reads a color style attribute. This can be a Color or null. FIXME - Doesn't support url(...)
     * colors yet.
     */
    private java.awt.Color toColor(org.w3c.dom.Element elem, java.lang.String value) throws java.io.IOException {
        java.lang.String str = value;
        if (str == null) {
            return null;
        }
        str = str.trim().toLowerCase();
        if ("currentcolor".equals(str)) {
            java.lang.String currentColor = readInheritAttribute(elem, "color", "black");
            if ((currentColor == null) || currentColor.trim().toLowerCase().equals("currentColor")) {
                return null;
            } else {
                return toColor(elem, currentColor);
            }
        } else if (org.jhotdraw.samples.svg.SVGConstants.SVG_COLORS.containsKey(str)) {
            return org.jhotdraw.samples.svg.SVGConstants.SVG_COLORS.get(str);
        } else if (str.startsWith("#") && (str.length() == 7)) {
            return new java.awt.Color(java.lang.Integer.decode(str));
        } else if (str.startsWith("#") && (str.length() == 4)) {
            // Three digits hex value
            int th = java.lang.Integer.decode(str);
            return new java.awt.Color((((((th & 0xf) | ((th & 0xf) << 4)) | ((th & 0xf0) << 4)) | ((th & 0xf0) << 8)) | ((th & 0xf00) << 8)) | ((th & 0xf00) << 12));
        } else if (str.startsWith("rgb")) {
            try {
                java.util.StringTokenizer tt = new java.util.StringTokenizer(str, "() ,");
                tt.nextToken();
                java.lang.String r = tt.nextToken();
                java.lang.String g = tt.nextToken();
                java.lang.String b = tt.nextToken();
                java.awt.Color c = new java.awt.Color(r.endsWith("%") ? ((int) (java.lang.Integer.decode(r.substring(0, r.length() - 1)) * 2.55)) : java.lang.Integer.decode(r), g.endsWith("%") ? ((int) (java.lang.Integer.decode(g.substring(0, g.length() - 1)) * 2.55)) : java.lang.Integer.decode(g), b.endsWith("%") ? ((int) (java.lang.Integer.decode(b.substring(0, b.length() - 1)) * 2.55)) : java.lang.Integer.decode(b));
                return c;
            } catch (java.lang.Exception e) {
                return null;
            }
        } else if (str.startsWith("url")) {
            // FIXME - Implement me
            return null;
        } else {
            return null;
        }
    }

    /**
     * Reads a double attribute.
     */
    private double toDouble(org.w3c.dom.Element elem, java.lang.String value) throws java.io.IOException {
        return toDouble(elem, value, 0, java.lang.Double.MIN_VALUE, java.lang.Double.MAX_VALUE);
    }

    /**
     * Reads a double attribute.
     */
    private double toDouble(org.w3c.dom.Element elem, java.lang.String value, double defaultValue, double min, double max) throws java.io.IOException {
        try {
            double d = java.lang.Double.valueOf(value);
            return java.lang.Math.max(java.lang.Math.min(d, max), min);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
            /* IOException ex = new IOException(elem.getTagName()+"@"+elem.getLineNr()+" "+e.getMessage());
            ex.initCause(e);
            throw ex;
             */
        }
    }

    /**
     * Reads a text attribute. This method takes the "xml:space" attribute into account.
     * http://www.w3.org/TR/SVGMobile12/text.html#WhiteSpace
     */
    private java.lang.String toText(org.w3c.dom.Element elem, java.lang.String value) throws java.io.IOException {
        java.lang.String space = readInheritAttribute(elem, "xml:space", "default");
        if ("default".equals(space)) {
            return value.trim().replaceAll("\\s++", " ");
        } else {
            return value;
        }
    }

    /* Converts an SVG transform attribute value into an AffineTransform
    as specified in
    http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
    public static java.awt.geom.AffineTransform toTransform(org.w3c.dom.Element elem, java.lang.String str) throws java.io.IOException {
        java.awt.geom.AffineTransform t = new java.awt.geom.AffineTransform();
        if ((str != null) && (!str.equals("none"))) {
            org.jhotdraw.io.StreamPosTokenizer tt = new org.jhotdraw.io.StreamPosTokenizer(new java.io.StringReader(str));
            tt.resetSyntax();
            tt.wordChars('a', 'z');
            tt.wordChars('A', 'Z');
            tt.wordChars(128 + 32, 255);
            tt.whitespaceChars(0, ' ');
            tt.whitespaceChars(',', ',');
            tt.parseNumbers();
            tt.parseExponents();
            while (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_EOF) {
                if (tt.ttype != org.jhotdraw.io.StreamPosTokenizer.TT_WORD) {
                    throw new java.io.IOException("Illegal transform " + str);
                }
                java.lang.String type = tt.sval;
                if (tt.nextToken() != '(') {
                    throw new java.io.IOException("'(' not found in transform " + str);
                }
                if ("matrix".equals(type)) {
                    double[] m = new double[6];
                    for (int i = 0; i < 6; i++) {
                        if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                            throw new java.io.IOException((((((("Matrix value " + i) + " not found in transform ") + str) + " token:") + tt.ttype) + " ") + tt.sval);
                        }
                        m[i] = tt.nval;
                    }
                    t.concatenate(new java.awt.geom.AffineTransform(m));
                } else if ("translate".equals(type)) {
                    double tx;
                    double ty;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException("X-translation value not found in transform " + str);
                    }
                    tx = tt.nval;
                    if (tt.nextToken() == org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        ty = tt.nval;
                    } else {
                        tt.pushBack();
                        ty = 0;
                    }
                    t.translate(tx, ty);
                } else if ("scale".equals(type)) {
                    double sx;
                    double sy;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException("X-scale value not found in transform " + str);
                    }
                    sx = tt.nval;
                    if (tt.nextToken() == org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        sy = tt.nval;
                    } else {
                        tt.pushBack();
                        sy = sx;
                    }
                    t.scale(sx, sy);
                } else if ("rotate".equals(type)) {
                    double angle;
                    double cx;
                    double cy;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException("Angle value not found in transform " + str);
                    }
                    angle = tt.nval;
                    if (tt.nextToken() == org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        cx = tt.nval;
                        if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                            throw new java.io.IOException("Y-center value not found in transform " + str);
                        }
                        cy = tt.nval;
                    } else {
                        tt.pushBack();
                        cx = cy = 0;
                    }
                    t.rotate((angle * java.lang.Math.PI) / 180.0, cx, cy);
                } else if ("skewX".equals(type)) {
                    double angle;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException("Skew angle not found in transform " + str);
                    }
                    angle = tt.nval;
                    t.concatenate(new java.awt.geom.AffineTransform(1, 0, java.lang.Math.tan((angle * java.lang.Math.PI) / 180), 1, 0, 0));
                } else if ("skewY".equals(type)) {
                    double angle;
                    if (tt.nextToken() != org.jhotdraw.io.StreamPosTokenizer.TT_NUMBER) {
                        throw new java.io.IOException("Skew angle not found in transform " + str);
                    }
                    angle = tt.nval;
                    t.concatenate(new java.awt.geom.AffineTransform(1, java.lang.Math.tan((angle * java.lang.Math.PI) / 180), 0, 1, 0, 0));
                } else if ("ref".equals(type)) {
                    java.lang.System.err.println("SVGInputFormat warning: ignored ref(...) transform attribute in element " + elem);
                    while ((tt.nextToken() != ')') && (tt.ttype != org.jhotdraw.io.StreamPosTokenizer.TT_EOF)) {
                        // ignore tokens between brackets
                    } 
                    tt.pushBack();
                } else {
                    throw new java.io.IOException((((("Unknown transform " + type) + " in ") + str) + " in element ") + elem);
                }
                if (tt.nextToken() != ')') {
                    throw new java.io.IOException("')' not found in transform " + str);
                }
            } 
        }
        return t;
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter("Scalable Vector Graphics (SVG)", "svg");
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return flavor.getPrimaryType().equals("image") && flavor.getSubType().equals("svg+xml");
    }

    @java.lang.Override
    public void read(java.awt.datatransfer.Transferable t, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        java.io.InputStream in = ((java.io.InputStream) (t.getTransferData(new java.awt.datatransfer.DataFlavor("image/svg+xml", "Image SVG"))));
        try {
            read(in, drawing, false);
        } finally {
            in.close();
        }
    }
}