/* @(#)JavaxDOMInput.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
public class JavaxDOMInput implements org.jhotdraw.xml.DOMInput {
    /**
     * This map is used to unmarshall references to objects to the XML DOM. A key in this map is a
     * String representing a marshalled reference. A value in this map is an unmarshalled Object.
     */
    private java.util.HashMap<java.lang.String, java.lang.Object> idobjects = new java.util.HashMap<java.lang.String, java.lang.Object>();

    /**
     * The document used for input.
     */
    private org.w3c.dom.Document document;

    /**
     * The current node used for input.
     */
    private org.w3c.dom.Node current;

    /**
     * The factory used to create objects from XML tag names.
     */
    private org.jhotdraw.xml.DOMFactory factory;

    protected static javax.xml.parsers.DocumentBuilder documentBuilder;

    /**
     * Lazily create the document builder and keep a reference to it for performance improvement.
     */
    protected static javax.xml.parsers.DocumentBuilder getBuilder() throws java.io.IOException {
        if (org.jhotdraw.xml.JavaxDOMInput.documentBuilder == null) {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            factory.setXIncludeAware(false);
            try {
                factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
                org.jhotdraw.xml.JavaxDOMInput.documentBuilder = factory.newDocumentBuilder();
            } catch (java.lang.Exception ex) {
                java.lang.InternalError error = new java.lang.InternalError("Unable to create DocumentBuilder");
                error.initCause(ex);
                throw error;
            }
        }
        return org.jhotdraw.xml.JavaxDOMInput.documentBuilder;
    }

    public JavaxDOMInput(org.jhotdraw.xml.DOMFactory factory, java.io.InputStream in) throws java.io.IOException {
        this.factory = factory;
        try {
            document = org.jhotdraw.xml.JavaxDOMInput.getBuilder().parse(in);
            current = document;
        } catch (org.xml.sax.SAXException ex) {
            java.io.IOException e = new java.io.IOException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }

    public JavaxDOMInput(org.jhotdraw.xml.DOMFactory factory, java.io.Reader in) throws java.io.IOException {
        this.factory = factory;
        try {
            document = org.jhotdraw.xml.JavaxDOMInput.getBuilder().parse(new org.xml.sax.InputSource(in));
            current = document;
        } catch (org.xml.sax.SAXException ex) {
            java.io.IOException e = new java.io.IOException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }

    /**
     * Returns the tag name of the current element.
     */
    @java.lang.Override
    public java.lang.String getTagName() {
        return ((org.w3c.dom.Element) (current)).getTagName();
    }

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    @java.lang.Override
    public java.lang.String getAttribute(java.lang.String name, java.lang.String defaultValue) {
        java.lang.String value = ((org.w3c.dom.Element) (current)).getAttribute(name);
        return value.length() == 0 ? defaultValue : value;
    }

    /**
     * Gets the text of the current element of the DOM Document.
     */
    @java.lang.Override
    public java.lang.String getText() {
        return getText(null);
    }

    /**
     * Gets the text of the current element of the DOM Document.
     */
    @java.lang.Override
    public java.lang.String getText(java.lang.String defaultValue) {
        if (current.getChildNodes().getLength() == 0) {
            return defaultValue;
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        org.jhotdraw.xml.JavaxDOMInput.getText(current, buf);
        return buf.toString();
    }

    private static void getText(org.w3c.dom.Node n, java.lang.StringBuilder buf) {
        if (n.getNodeValue() != null) {
            buf.append(n.getNodeValue());
        }
        org.w3c.dom.NodeList children = n.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            org.jhotdraw.xml.JavaxDOMInput.getText(children.item(i), buf);
        }
    }

    /**
     * Gets an attribute of the current element of the DOM Document and of all parent DOM elements.
     */
    @java.lang.Override
    public java.util.List<java.lang.String> getInheritedAttribute(java.lang.String name) {
        java.util.List<java.lang.String> values = new java.util.ArrayList<>();
        org.w3c.dom.Node node = current;
        while (node != null) {
            java.lang.String value = ((org.w3c.dom.Element) (node)).getAttribute(name);
            values.add(0, value);
            node = node.getParentNode();
        } 
        return values;
    }

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    @java.lang.Override
    public int getAttribute(java.lang.String name, int defaultValue) {
        java.lang.String value = ((org.w3c.dom.Element) (current)).getAttribute(name);
        return value.length() == 0 ? defaultValue : java.lang.Long.decode(value).intValue();
    }

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    @java.lang.Override
    public double getAttribute(java.lang.String name, double defaultValue) {
        java.lang.String value = ((org.w3c.dom.Element) (current)).getAttribute(name);
        return value.length() == 0 ? defaultValue : java.lang.Double.parseDouble(value);
    }

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    @java.lang.Override
    public boolean getAttribute(java.lang.String name, boolean defaultValue) {
        java.lang.String value = ((org.w3c.dom.Element) (current)).getAttribute(name);
        return value.length() == 0 ? defaultValue : java.lang.Boolean.valueOf(value).booleanValue();
    }

    /**
     * Returns the number of child elements of the current element.
     */
    @java.lang.Override
    public int getElementCount() {
        int count = 0;
        org.w3c.dom.NodeList list = current.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Node node = list.item(i);
            if (node instanceof org.w3c.dom.Element) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of child elements with the specified tag name of the current element.
     */
    @java.lang.Override
    public int getElementCount(java.lang.String tagName) {
        int count = 0;
        org.w3c.dom.NodeList list = current.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            org.w3c.dom.Node node = list.item(i);
            if ((node instanceof org.w3c.dom.Element) && ((org.w3c.dom.Element) (node)).getTagName().equalsIgnoreCase(tagName)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Opens the element with the specified index and makes it the current node.
     */
    @java.lang.Override
    public void openElement(int index) {
        int count = 0;
        org.w3c.dom.NodeList list = current.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            org.w3c.dom.Node node = list.item(i);
            if (node instanceof org.w3c.dom.Element) {
                if ((count++) == index) {
                    current = node;
                    return;
                }
            }
        }
    }

    /**
     * Opens the last element with the specified name and makes it the current node.
     */
    @java.lang.Override
    public void openElement(java.lang.String tagName) {
        int count = 0;
        org.w3c.dom.NodeList list = current.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            org.w3c.dom.Node node = list.item(i);
            if ((node instanceof org.w3c.dom.Element) && ((org.w3c.dom.Element) (node)).getTagName().equalsIgnoreCase(tagName)) {
                current = node;
                return;
            }
        }
        throw new java.lang.IllegalArgumentException("element not found:" + tagName);
    }

    /**
     * Opens the element with the specified name and index and makes it the current node.
     */
    @java.lang.Override
    public void openElement(java.lang.String tagName, int index) {
        int count = 0;
        org.w3c.dom.NodeList list = current.getChildNodes();
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            org.w3c.dom.Node node = list.item(i);
            if ((node instanceof org.w3c.dom.Element) && ((org.w3c.dom.Element) (node)).getTagName().equalsIgnoreCase(tagName)) {
                if ((count++) == index) {
                    current = node;
                    return;
                }
            }
        }
        throw new java.lang.IllegalArgumentException(((("no such child " + tagName) + "[") + index) + "]");
    }

    /**
     * Closes the current element of the DOM Document. The parent of the current element becomes the
     * current element.
     *
     * @exception IllegalArgumentException
     * 		if the provided tagName does not match the tag name of the
     * 		element.
     */
    @java.lang.Override
    public void closeElement() {
        /* if (! ((Element) current).getTagName().equals(tagName)) {
        throw new IllegalArgumentException("Attempt to close wrong element:"+tagName +"!="+((Element) current).getTagName());
        }
         */
        current = current.getParentNode();
    }

    /**
     * Reads an object from the current element.
     */
    @java.lang.Override
    public java.lang.Object readObject() throws java.io.IOException {
        return readObject(0);
    }

    /**
     * Reads an object from the current element.
     */
    @java.lang.Override
    public java.lang.Object readObject(int index) throws java.io.IOException {
        openElement(index);
        java.lang.Object o;
        java.lang.String ref = getAttribute("ref", null);
        java.lang.String id = getAttribute("id", null);
        if ((ref != null) && (id != null)) {
            throw new java.io.IOException(((((("Element has both an id and a ref attribute: <" + getTagName()) + " id=") + id) + " ref=") + ref) + ">");
        }
        if ((id != null) && idobjects.containsKey(id)) {
            throw new java.io.IOException(((("Duplicate id attribute: <" + getTagName()) + " id=") + id) + ">");
        }
        if ((ref != null) && (!idobjects.containsKey(ref))) {
            throw new java.io.IOException(((("Illegal ref attribute value: <" + getTagName()) + " ref=") + ref) + ">");
        }
        // Keep track of objects which have an ID
        if (ref != null) {
            o = idobjects.get(ref);
        } else {
            o = factory.read(this);
            if (id != null) {
                idobjects.put(id, o);
            }
        }
        closeElement();
        return o;
    }
}