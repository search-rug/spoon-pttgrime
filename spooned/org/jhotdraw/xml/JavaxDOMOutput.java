/* @(#)DOMOutput.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
public class JavaxDOMOutput implements org.jhotdraw.xml.DOMOutput {
    /**
     * The doctype of the XML document.
     */
    private java.lang.String doctype;

    /**
     * This map is used to marshall references to objects to the XML DOM. A key in this map is a Java
     * Object, a value in this map is String representing a marshalled reference to that object.
     */
    private java.util.HashMap<java.lang.Object, java.lang.String> objectids;

    /**
     * This map is used to cache prototype objects.
     */
    private java.util.HashMap<java.lang.String, java.lang.Object> prototypes;

    /**
     * The document used for output.
     */
    private org.w3c.dom.Document document;

    /**
     * The current node used for output.
     */
    private org.w3c.dom.Node current;

    /**
     * The factory used to create objects.
     */
    private org.jhotdraw.xml.DOMFactory factory;

    public JavaxDOMOutput(org.jhotdraw.xml.DOMFactory factory) throws java.io.IOException {
        this.factory = factory;
        reset();
    }

    protected void reset() throws java.io.IOException {
        try {
            objectids = new java.util.HashMap<java.lang.Object, java.lang.String>();
            document = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            current = document;
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            java.io.IOException error = new java.io.IOException(e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Writes the contents of the DOMOutput into the specified output stream.
     */
    public void save(java.io.OutputStream out) throws java.io.IOException {
        try {
            if (doctype != null) {
                java.io.OutputStreamWriter w = new java.io.OutputStreamWriter(out, "UTF8");
                w.write("<!DOCTYPE ");
                w.write(doctype);
                w.write(">\n");
                w.flush();
            }
            javax.xml.transform.Transformer t = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            t.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(out));
        } catch (javax.xml.transform.TransformerException e) {
            java.io.IOException error = new java.io.IOException(e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Writes the contents of the DOMOutput into the specified output stream.
     */
    public void save(java.io.Writer out) throws java.io.IOException {
        try {
            if (doctype != null) {
                out.write("<!DOCTYPE ");
                out.write(doctype);
                out.write(">\n");
            }
            javax.xml.transform.Transformer t = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            t.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(out));
        } catch (javax.xml.transform.TransformerException e) {
            java.io.IOException error = new java.io.IOException(e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Puts a new element into the DOM Document. The new element is added as a child to the current
     * element in the DOM document. Then it becomes the current element. The element must be closed
     * using closeElement.
     */
    @java.lang.Override
    public void openElement(java.lang.String tagName) {
        org.w3c.dom.Element newElement = document.createElement(tagName);
        current.appendChild(newElement);
        current = newElement;
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
        /* if (! ((Element) current).getName().equals(tagName)) {
        throw new IllegalArgumentException("Attempt to close wrong element:"+tagName +"!="+((Element) current).getName());
        }
         */
        current = current.getParentNode();
    }

    /**
     * Adds a comment to the current element of the DOM Document.
     */
    @java.lang.Override
    public void addComment(java.lang.String comment) {
        current.appendChild(document.createComment(comment));
    }

    /**
     * Adds a text to current element of the DOM Document. Note: Multiple consecutives texts will be
     * merged.
     */
    @java.lang.Override
    public void addText(java.lang.String text) {
        current.appendChild(document.createTextNode(text));
    }

    /**
     * Adds an attribute to current element of the DOM Document.
     */
    @java.lang.Override
    public void addAttribute(java.lang.String name, java.lang.String value) {
        if (value != null) {
            ((org.w3c.dom.Element) (current)).setAttribute(name, value);
        }
    }

    /**
     * Adds an attribute to current element of the DOM Document.
     */
    @java.lang.Override
    public void addAttribute(java.lang.String name, int value) {
        ((org.w3c.dom.Element) (current)).setAttribute(name, java.lang.Integer.toString(value));
    }

    /**
     * Adds an attribute to current element of the DOM Document.
     */
    @java.lang.Override
    public void addAttribute(java.lang.String name, boolean value) {
        ((org.w3c.dom.Element) (current)).setAttribute(name, java.lang.Boolean.toString(value));
    }

    /**
     * Adds an attribute to current element of the DOM Document.
     */
    @java.lang.Override
    public void addAttribute(java.lang.String name, float value) {
        // Remove the awkard .0 at the end of each number
        java.lang.String str = java.lang.Float.toString(value);
        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }
        ((org.w3c.dom.Element) (current)).setAttribute(name, str);
    }

    /**
     * Adds an attribute to current element of the DOM Document.
     */
    @java.lang.Override
    public void addAttribute(java.lang.String name, double value) {
        // Remove the awkard .0 at the end of each number
        java.lang.String str = java.lang.Double.toString(value);
        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }
        ((org.w3c.dom.Element) (current)).setAttribute(name, str);
    }

    @java.lang.Override
    public void writeObject(java.lang.Object o) throws java.io.IOException {
        java.lang.String tagName = factory.getName(o);
        if (tagName == null) {
            throw new java.lang.IllegalArgumentException("no tag name for:" + o);
        }
        openElement(tagName);
        if (objectids.containsKey(o)) {
            addAttribute("ref", objectids.get(o));
        } else {
            java.lang.String id = java.lang.Integer.toString(objectids.size(), 16);
            objectids.put(o, id);
            addAttribute("id", id);
            factory.write(this, o);
        }
        closeElement();
    }

    @java.lang.Override
    public void addAttribute(java.lang.String name, float value, float defaultValue) {
        if (value != defaultValue) {
            addAttribute(name, value);
        }
    }

    @java.lang.Override
    public void addAttribute(java.lang.String name, int value, int defaultValue) {
        if (value != defaultValue) {
            addAttribute(name, value);
        }
    }

    @java.lang.Override
    public void addAttribute(java.lang.String name, double value, double defaultValue) {
        if (value != defaultValue) {
            addAttribute(name, value);
        }
    }

    @java.lang.Override
    public void addAttribute(java.lang.String name, boolean value, boolean defaultValue) {
        if (value != defaultValue) {
            addAttribute(name, value);
        }
    }

    @java.lang.Override
    public void addAttribute(java.lang.String name, java.lang.String value, java.lang.String defaultValue) {
        if (!value.equals(defaultValue)) {
            addAttribute(name, value);
        }
    }

    @java.lang.Override
    public java.lang.Object getPrototype() {
        if (prototypes == null) {
            prototypes = new java.util.HashMap<java.lang.String, java.lang.Object>();
        }
        if (!prototypes.containsKey(current.getNodeName())) {
            prototypes.put(current.getNodeName(), factory.createPrototype(current.getNodeName()));
        }
        return prototypes.get(current.getNodeName());
    }

    @java.lang.Override
    public void setDoctype(java.lang.String doctype) {
        this.doctype = doctype;
    }
}