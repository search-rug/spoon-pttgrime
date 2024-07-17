/* @(#)DOMInput.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
/**
 * DOMInput.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Abstract Factory</em><br>
 * {@code DOMFactory} is used by {@code DOMInput} and {@code DOMOutput} for creating Java objects
 * and DOM elements. Abstract Factory: {@link DOMFactory}<br>
 * Client: {@link DOMInput}, {@link DOMOutput}.
 *
 * <p><em>Strategy</em><br>
 * {@code DOMFactory} is used by {@code DOMInput} and {@code DOMOutput} for reading and writing
 * objects. Client: {@link DOMInput}, {@link DOMOutput}.<br>
 * Strategy: {@link DOMFactory}.<br>
 */
public interface DOMInput {
    /**
     * Returns the tag name of the current element.
     */
    public java.lang.String getTagName();

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    public java.lang.String getAttribute(java.lang.String name, java.lang.String defaultValue);

    /**
     * Gets the text of the current element of the DOM Document.
     */
    public java.lang.String getText();

    /**
     * Gets the text of the current element of the DOM Document.
     */
    public java.lang.String getText(java.lang.String defaultValue);

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    public int getAttribute(java.lang.String name, int defaultValue);

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    public double getAttribute(java.lang.String name, double defaultValue);

    /**
     * Gets an attribute of the current element of the DOM Document.
     */
    public boolean getAttribute(java.lang.String name, boolean defaultValue);

    /**
     * Gets an attribute of the current element of the DOM Document and of all parent DOM elements.
     */
    public java.util.List<java.lang.String> getInheritedAttribute(java.lang.String name);

    /**
     * Returns the number of child elements of the current element.
     */
    public int getElementCount();

    /**
     * Returns the number of child elements with the specified tag name of the current element.
     */
    public int getElementCount(java.lang.String tagName);

    /**
     * Opens the element with the specified index and makes it the current node.
     */
    public void openElement(int index) throws java.io.IOException;

    /**
     * Opens the last element with the specified name and makes it the current node.
     */
    public void openElement(java.lang.String tagName) throws java.io.IOException;

    /**
     * Opens the element with the specified name and index and makes it the current node.
     */
    public void openElement(java.lang.String tagName, int index) throws java.io.IOException;

    /**
     * Closes the current element of the DOM Document. The parent of the current element becomes the
     * current element.
     *
     * @exception IllegalArgumentException
     * 		if the provided tagName does not match the tag name of the
     * 		element.
     */
    public void closeElement();

    /**
     * Reads an object from the current element.
     */
    public java.lang.Object readObject() throws java.io.IOException;

    /**
     * Reads an object from the current element.
     */
    public java.lang.Object readObject(int index) throws java.io.IOException;
}