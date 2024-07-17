/* @(#)ODGStylesReader.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg.io;
/**
 * ODGStylesReader reads an ODG &lt;document-styles&gt; element, and creates a map of AttributeKey's
 * and values.
 */
public class ODGStylesReader {
    private static class Style extends java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> {
        private static final long serialVersionUID = 1L;

        public java.lang.String name;

        public java.lang.String family;

        public java.lang.String parentName;
    }

    /**
     * Most office applications support styles within their user interface. Within this specification,
     * the XML representations of such styles are referred to as styles. When a differentiation from
     * the other types of styles is required, they are referred to as common styles. The term common
     * indicates that this is the type of style that an office application user considers to be a
     * style.
     */
    private java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> commonStyles;

    /**
     * A master style is a common style that contains formatting information and additional content
     * that is displayed with the document content when the style is applied. An example of a master
     * style are master pages. Master pages can be used in graphical applications. In this case, the
     * additional content is any drawing shapes that are displayed as the background of the draw page.
     * Master pages can also be used in text documents. In this case, the additional content is the
     * headers and footers. Please note that the content that is contained within master styles is
     * additional content that influences the representation of a document but does not change the
     * content of a document.
     */
    private java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> masterStyles;

    /**
     * An automatic style contains formatting properties that, in the user interface view of a
     * document, are assigned to an object such as a paragraph. The term automatic indicates that the
     * style is generated automatically. In other words, formatting properties that are immediately
     * assigned to a specific object are represented by an automatic style. This way, a separation of
     * content and layout is achieved.
     */
    private java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> automaticStyles;

    public ODGStylesReader() {
        reset();
    }

    public java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> getAttributes(java.lang.String styleName, java.lang.String familyName) {
        // String key = familyName+"-"+styleName;
        java.lang.String key = styleName;
        org.jhotdraw.samples.odg.io.ODGStylesReader.Style style;
        if (commonStyles.containsKey(key)) {
            style = commonStyles.get(key);
        } else if (automaticStyles.containsKey(key)) {
            style = automaticStyles.get(key);
        } else if (masterStyles.containsKey(key)) {
            style = masterStyles.get(key);
        } else {
            style = new org.jhotdraw.samples.odg.io.ODGStylesReader.Style();
        }
        if (style.parentName == null) {
            return style;
        } else {
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a = new java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>();
            java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> parentAttributes = getAttributes(style.parentName, familyName);
            a.putAll(parentAttributes);
            a.putAll(style);
            return a;
        }
    }

    /**
     * Reads a &lt;document-styles&gt; element from the specified XML file.
     *
     * @param file
     * 		A XML file with a &lt;document&gt; root element or with a &lt;document-styles&gt;
     * 		root element.
     */
    // public void read(File file) throws IOException {
    // BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    // try {
    // read(in);
    // } finally {
    // in.close();
    // }
    // }
    /**
     * Reads a &lt;document-styles&gt; element from the specified input stream.
     *
     * @param in
     * 		A input stream with a &lt;document&gt; root element or with a &lt;document-styles&gt;
     * 		root element.
     */
    public void read(java.io.InputStream in) throws java.io.IOException {
        org.w3c.dom.Element document;
        javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        try {
            javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(in);
            document = doc.getDocumentElement();
        } catch (javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException ex) {
            java.io.IOException e = new java.io.IOException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
        read(document);
    }

    private void reset() {
        commonStyles = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style>();
        automaticStyles = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style>();
        masterStyles = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style>();
    }

    /**
     * Reads a &lt;document-styles&gt; element from the specified XML element.
     *
     * @param root
     * 		A &lt;document&gt; element or a &lt;document-styles&gt; element.
     */
    public void read(org.w3c.dom.Element root) throws java.io.IOException {
        java.lang.String name = root.getLocalName();
        java.lang.String ns = root.getPrefix();
        if ("document-content".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
            readDocumentContentElement(root);
        } else if ("document-styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
            readDocumentStylesElement(root);
        } else {
            org.jhotdraw.samples.odg.io.ODGStylesReader.LOG.warning("ODGStylesReader unsupported root element " + root);
        }
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.samples.odg.io.ODGStylesReader.class.getName());

    /**
     * Reads a &lt;default-style&gt; element from the specified XML element.
     *
     * <p>A default style specifies default formatting properties for a certain style family. These
     * defaults are used if a formatting property is neither specified by an automatic nor a common
     * style. Default styles exist for all style families that are represented by the
     * &lt;style:style&gt; element specified in section 14.1. Default styles are represented by the
     * &lt;style:default-style&gt; element. The only attribute supported by this element is
     * style:family. Its meaning equals the one of the same attribute for the &lt;style:style&gt;
     * element, and the same properties child elements are supported depending on the style family.
     *
     * @param elem
     * 		A &lt;default-style&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readDefaultStyleElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
        java.lang.String styleName = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "family");
        java.lang.String family = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "family");
        java.lang.String parentStyleName = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "parent-style-name");
        if (styleName != null) {
            org.jhotdraw.samples.odg.io.ODGStylesReader.Style a = styles.get(styleName);
            if (a == null) {
                a = new org.jhotdraw.samples.odg.io.ODGStylesReader.Style();
                a.name = styleName;
                a.family = family;
                a.parentName = parentStyleName;
                styles.put(styleName, a);
            }
            org.w3c.dom.NodeList list = elem.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
                java.lang.String ns = child.getPrefix();
                java.lang.String name = child.getLocalName();
                if ("drawing-page-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readDrawingPagePropertiesElement(child, a);
                } else if ("graphic-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readGraphicPropertiesElement(child, a);
                } else if ("paragraph-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readParagraphPropertiesElement(child, a);
                } else if ("text-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readTextPropertiesElement(child, a);
                } else {
                    org.jhotdraw.samples.odg.io.ODGStylesReader.LOG.warning((("ODGStylesReader unsupported <" + elem.getLocalName()) + "> child ") + child);
                }
            }
        }
    }

    /**
     * Reads a &lt;document-content&gt; element from the specified XML element.
     *
     * @param elem
     * 		A &lt;document-content&gt; element.
     */
    private void readDocumentContentElement(org.w3c.dom.Element elem) throws java.io.IOException {
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            java.lang.String ns = child.getPrefix();
            java.lang.String name = child.getLocalName();
            if ("automatic-styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readAutomaticStylesElement(child);
            } else if ("master-styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readStylesElement(child);
            } else if ("styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readStylesElement(child);
            }
        }
    }

    /**
     * Reads a &lt;document-styles&gt; element from the specified XML element.
     *
     * <p>The document-styles element contains all named styles of a document, along with the
     * automatic styles needed for the named styles.
     *
     * @param elem
     * 		A &lt;document-styles&gt; element.
     */
    private void readDocumentStylesElement(org.w3c.dom.Element elem) throws java.io.IOException {
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            java.lang.String ns = child.getPrefix();
            java.lang.String name = child.getLocalName();
            if ("styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readStylesElement(child);
            } else if ("automatic-styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readAutomaticStylesElement(child);
            } else if ("master-styles".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.OFFICE_NAMESPACE))) {
                readMasterStylesElement(child);
            } else {
                org.jhotdraw.samples.odg.io.ODGStylesReader.LOG.warning((("ODGStylesReader unsupported <" + elem.getLocalName()) + "> child ") + child);
            }
        }
    }

    /**
     * Reads a &lt;style:drawing-page-properties&gt; element from the specified XML element.
     *
     * <p>
     *
     * @param elem
     * 		A &lt;style:drawing-page-properties&gt; element.
     */
    private void readDrawingPagePropertiesElement(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
    }

    /**
     * Reads a &lt;style:graphic-properties&gt; element from the specified XML element.
     *
     * <p>
     *
     * @param elem
     * 		A &lt;style:graphic-properties&gt; element.
     */
    private void readGraphicPropertiesElement(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        // The attribute draw:stroke specifies the style of the stroke on the current object. The value
        // none means that no stroke is drawn, and the value solid means that a solid stroke is drawn.
        // If
        // the value is dash, the stroke referenced by the draw:stroke-dash property is drawn.
        if (elem.hasAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "stroke")) {
            org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_STYLE.put(a, org.jhotdraw.samples.odg.ODGConstants.STROKE_STYLES.getOrDefault(elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "stroke"), null));
        }
        // The attribute svg:stroke-width specifies the width of the stroke on
        // the current object.
        if (elem.hasAttributeNS(org.jhotdraw.samples.odg.ODGConstants.SVG_NAMESPACE, "stroke-width")) {
            org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH.put(a, toLength(elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.SVG_NAMESPACE, "stroke-width")));
        }
        // The attribute svg:stroke-color specifies the color of the stroke on
        // the current object.
        if (elem.hasAttributeNS(org.jhotdraw.samples.odg.ODGConstants.SVG_NAMESPACE, "stroke-color")) {
            org.jhotdraw.draw.AttributeKeys.STROKE_COLOR.put(a, toColor(elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.SVG_NAMESPACE, "stroke-color")));
        }
        // FIXME read draw:marker-start-width, draw:marker-start-center, draw:marker-end-width,
        // draw:marker-end-centre
        // The attribute draw:fill specifies the fill style for a graphic
        // object. Graphic objects that are not closed, such as a path without a
        // closepath at the end, will not be filled. The fill operation does not
        // automatically close all open subpaths by connecting the last point of
        // the subpath with the first point of the subpath before painting the
        // fill. The attribute has the following values:
        // • none:     the drawing object is not filled.
        // • solid:    the drawing object is filled with color specified by the
        // draw:fill-color attribute.
        // • bitmap:   the drawing object is filled with the bitmap specified
        // by the draw:fill-image-name attribute.
        // • gradient: the drawing object is filled with the gradient specified
        // by the draw:fill-gradient-name attribute.
        // • hatch:    the drawing object is filled with the hatch specified by
        // the draw:fill-hatch-name attribute.
        if (elem.hasAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "fill")) {
            org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_STYLE.put(a, org.jhotdraw.samples.odg.ODGConstants.FILL_STYLES.getOrDefault(elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "fill"), null));
        }
        // The attribute draw:fill-color specifies the color of the fill for a
        // graphic object. It is used only if the draw:fill attribute has the
        // value solid.
        if (elem.hasAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "fill-color")) {
            org.jhotdraw.draw.AttributeKeys.FILL_COLOR.put(a, toColor(elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE, "fill-color")));
        }
        // FIXME read fo:padding-top, fo:padding-bottom, fo:padding-left,
        // fo:padding-right
        // FIXME read draw:shadow, draw:shadow-offset-x, draw:shadow-offset-y,
        // draw:shadow-color
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            java.lang.String ns = child.getPrefix();
            java.lang.String name = child.getLocalName();
            // if (DEBUG) System.out.println("ODGStylesReader unsupported <"+elem.getName()+"> child
            // <"+child.getName()+" ...>...</>");
        }
    }

    /**
     * Reads a &lt;styles&gt; element from the specified XML element.
     *
     * <p>The &lt;style:style&gt; element can represent paragraph, text, and graphic styles.
     *
     * @param elem
     * 		A &lt;style&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readStyleElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
        // The style:name attribute identifies the name of the style. This attribute, combined with the
        // style:family attribute, uniquely identifies a style. The <office:styles>,
        // <office:automatic-styles> and <office:master-styles> elements each must not
        // contain two styles with the same family and the same name.
        // For automatic styles, a name is generated during document export. If the document is exported
        // several times, it cannot be assumed that the same name is generated each time.
        // In an XML document, the name of each style is a unique name that may be independent of the
        // language selected for an office applications user interface. Usually these names are the ones
        // used
        // for the English version of the user interface.
        java.lang.String styleName = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "name");
        java.lang.String family = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "family");
        java.lang.String parentStyleName = elem.getAttributeNS(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE, "parent-style-name");
        if (styleName != null) {
            org.jhotdraw.samples.odg.io.ODGStylesReader.Style a = styles.get(styleName);
            if (a == null) {
                a = new org.jhotdraw.samples.odg.io.ODGStylesReader.Style();
                a.name = styleName;
                a.family = family;
                a.parentName = parentStyleName;
                styles.put(styleName, a);
            }
            org.w3c.dom.NodeList list = elem.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
                java.lang.String ns = child.getPrefix();
                java.lang.String name = child.getLocalName();
                if ("drawing-page-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readDrawingPagePropertiesElement(child, a);
                } else if ("graphic-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readGraphicPropertiesElement(child, a);
                } else if ("paragraph-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readParagraphPropertiesElement(child, a);
                } else if ("text-properties".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                    readTextPropertiesElement(child, a);
                } else {
                    org.jhotdraw.samples.odg.io.ODGStylesReader.LOG.warning((("ODGStylesReader unsupported <" + elem.getLocalName()) + "> child ") + child);
                }
            }
        }
    }

    /**
     * Reads a &lt;styles&gt; element from the specified XML element.
     *
     * <p>The styles element contains common styles.
     *
     * @param elem
     * 		A &lt;styles&gt; element.
     */
    private void readStylesElement(org.w3c.dom.Element elem) throws java.io.IOException {
        readStylesChildren(elem, commonStyles);
    }

    /**
     * Reads the children of a styles element.
     *
     * @param elem
     * 		A &lt;styles&gt;, &lt;automatic-styles&gt;, &lt;document-styles&gt; or a
     * 		&lt;master-styles&gt; element.
     * @param styles
     * 		Styles to be filled in by this method.
     */
    private void readStylesChildren(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
        org.w3c.dom.NodeList list = elem.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Element child = ((org.w3c.dom.Element) (list.item(i)));
            java.lang.String ns = child.getPrefix();
            java.lang.String name = child.getLocalName();
            if ("default-style".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                readDefaultStyleElement(child, styles);
            } else if ("layer-set".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE))) {
                readLayerSetElement(child, styles);
            } else if ("list-style".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.TEXT_NAMESPACE))) {
                readListStyleElement(child, styles);
            } else if ("marker".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.DRAWING_NAMESPACE))) {
                readMarkerElement(child, styles);
            } else if ("master-page".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                readMasterPageElement(child, styles);
            } else if ("page-layout".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                readPageLayoutElement(child, styles);
                // } else if ("paragraph-properties".equals(name) && (ns == null ||
                // ns.equals(STYLE_NAMESPACE))) {
                // readParagraphPropertiesElement(child, styles);
            } else if ("style".equals(name) && ((ns == null) || ns.equals(org.jhotdraw.samples.odg.ODGConstants.STYLE_NAMESPACE))) {
                readStyleElement(child, styles);
                // } else if ("text-properties".equals(name) && (ns == null || ns.equals(STYLE_NAMESPACE)))
                // {
                // readTextPropertiesElement(child, styles);
            } else {
                org.jhotdraw.samples.odg.io.ODGStylesReader.LOG.warning((("ODGStylesReader unsupported <" + elem.getLocalName()) + "> child: ") + child);
            }
        }
    }

    /**
     * Reads a &lt;automatic-styles&gt; element from the specified XML element.
     *
     * <p>The automatic-styles element contains automatic styles.
     *
     * @param elem
     * 		A &lt;automatic-styles&gt; element.
     */
    private void readAutomaticStylesElement(org.w3c.dom.Element elem) throws java.io.IOException {
        readStylesChildren(elem, automaticStyles);
    }

    /**
     * Reads a &lt;draw:layer-put&gt; element from the specified XML element.
     *
     * <p>
     *
     * @param elem
     * 		A &lt;layer-put&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readLayerSetElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
    }

    /**
     * Reads a &lt;text:list-style&gt; element from the specified XML element.
     *
     * <p>
     *
     * @param elem
     * 		A &lt;list-style&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readListStyleElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
    }

    /**
     * Reads a &lt;master-styles&gt; element from the specified XML element.
     *
     * <p>The master-styles element contains master styles.
     *
     * @param elem
     * 		A &lt;master-styles&gt; element.
     */
    private void readMasterStylesElement(org.w3c.dom.Element elem) throws java.io.IOException {
        readStylesChildren(elem, masterStyles);
    }

    /**
     * Reads a &lt;draw:marker&gt; element from the specified XML element.
     *
     * <p>The element &lt;draw:marker&gt; represents a marker, which is used to draw polygons at the
     * start and end points of strokes. Markers are not available as automatic styles.
     *
     * @param elem
     * 		A &lt;master-styles&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readMarkerElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
        // if (DEBUG) System.out.println("ODGStylesReader unsupported <"+elem.getName()+"> element.");
    }

    /**
     * Reads a &lt;style:master-page&gt; element from the specified XML element.
     *
     * <p>
     *
     * @param elem
     * 		A &lt;page-layout&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readMasterPageElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
    }

    /**
     * Reads a &lt;style:page-layout&gt; element from the specified XML element.
     *
     * <p>The &lt;style:page-layout&gt; element specifies the physical properties of a page. This
     * element contains a &lt;style:page-layout-properties&gt; element which specifies the formatting
     * properties of the page and two optional elements that specify the properties of headers and
     * footers.
     *
     * @param elem
     * 		A &lt;page-layout&gt; element.
     * @param styles
     * 		Style attributes to be filled in by this method.
     */
    private void readPageLayoutElement(org.w3c.dom.Element elem, java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.io.ODGStylesReader.Style> styles) throws java.io.IOException {
        // if (DEBUG) System.out.println("ODGStylesReader unsupported <"+elem.getName()+"> element.");
    }

    /**
     * Reads a &lt;style:paragraph-properties&gt; element from the specified XML element.
     *
     * <p>The properties described in this section can be contained within paragraph styles (see
     * section 14.8.2), but also within other styles, like cell styles (see section 14.12.4) They are
     * contained in a &lt;style:paragraph-properties&gt; element.
     *
     * @param elem
     * 		A &lt;paragraph-properties&gt; element.
     * @param a
     * 		Style attributes to be filled in by this method.
     */
    private void readParagraphPropertiesElement(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        // if (DEBUG) System.out.println("ODGStylesReader unsupported <"+elem.getName()+"> element.");
    }

    /**
     * Reads a &lt;style:text-properties&gt; element from the specified XML element.
     *
     * <p>The properties described in this section can be contained within text styles (see section
     * 14.8.1), but also within other styles, like paragraph styles (see section 14.8.2) or cell
     * styles (see section 14.12.4) They are contained in a &lt;style:text-properties&gt; element.
     *
     * @param elem
     * 		A &lt;paragraph-properties&gt; element.
     * @param a
     * 		Style attributes to be filled in by this method.
     */
    private void readTextPropertiesElement(org.w3c.dom.Element elem, java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> a) throws java.io.IOException {
        // if (DEBUG) System.out.println("ODGStylesReader unsupported <"+elem.getName()+"> element.");
    }

    /**
     * Returns a value as a length.
     *
     * <p>&lt;define name="length"&gt; &lt;data type="string"&gt; &lt;param
     * name="pattern"&gt;-?([0-9]+(\.[0-9]*)?|\.[0-9]+)((cm)|(mm)|(in)| (pt)|(pc)|(px))&lt;/param&gt;
     */
    private double toLength(java.lang.String str) throws java.io.IOException {
        double scaleFactor = 1.0;
        if ((str == null) || (str.length() == 0)) {
            return 0.0;
        }
        if (str.endsWith("cm")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 35.43307;
        } else if (str.endsWith("mm")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 3.543307;
        } else if (str.endsWith("in")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 90;
        } else if (str.endsWith("pt")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 1.25;
        } else if (str.endsWith("pc")) {
            str = str.substring(0, str.length() - 2);
            scaleFactor = 15;
        } else if (str.endsWith("px")) {
            str = str.substring(0, str.length() - 2);
        }
        return java.lang.Double.parseDouble(str) * scaleFactor;
    }

    /**
     * Reads a color style attribute. &lt;define name="color"&gt; &lt;data type="string"&gt; &lt;param
     * name="pattern"&gt;#[0-9a-fA-F]{6}&lt;/param&gt; &lt;/data&gt; &lt;/define&gt;
     */
    private java.awt.Color toColor(java.lang.String value) throws java.io.IOException {
        java.lang.String str = value;
        if (str == null) {
            return null;
        }
        if (str.startsWith("#") && (str.length() == 7)) {
            return new java.awt.Color(java.lang.Integer.decode(str));
        } else {
            return null;
        }
    }
}