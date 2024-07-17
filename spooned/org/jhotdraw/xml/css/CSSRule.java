/* @(#)CSSRule.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.

Original code taken from article "Swing and CSS" by Joshua Marinacci 10/14/2003
http://today.java.net/pub/a/today/2003/10/14/swingcss.html
 */
package org.jhotdraw.xml.css;
/**
 * CSSRule matches on a CSS selector.
 *
 * <p>Supported selectors:
 *
 * <ul>
 *   <li><code>*</code> matches all objects.
 *   <li><code>name</code> matches an element name.
 *   <li><code>.name</code> matches the value of the attribute "class".
 *   <li><code>#name</code> matches the value of the attribute "id".
 * </ul>
 *
 * This class supports net.n3.nanoxml as well as org.w3c.dom.
 */
public class CSSRule {
    private java.lang.String selector;

    private static enum SelectorType {

        ALL,
        ELEMENT_NAME,
        CLASS_ATTRIBUTE,
        ID_ATTRIBUTE;
    }

    private org.jhotdraw.xml.css.CSSRule.SelectorType type;

    protected java.util.Map<java.lang.String, java.lang.String> properties;

    public CSSRule(java.lang.String name, java.lang.String value) {
        properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        properties.put(name, value);
    }

    public CSSRule(java.lang.String selector, java.lang.String propertyName, java.lang.String propertyValue) {
        setSelector(selector);
        properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        properties.put(propertyName, propertyValue);
    }

    public CSSRule(java.lang.String selector, java.util.Map<java.lang.String, java.lang.String> properties) {
        setSelector(selector);
        this.properties = properties;
    }

    public void setSelector(java.lang.String selector) {
        switch (selector.charAt(0)) {
            case '*' :
                type = org.jhotdraw.xml.css.CSSRule.SelectorType.ALL;
                break;
            case '.' :
                type = org.jhotdraw.xml.css.CSSRule.SelectorType.CLASS_ATTRIBUTE;
                break;
            case '#' :
                type = org.jhotdraw.xml.css.CSSRule.SelectorType.ID_ATTRIBUTE;
                break;
            default :
                type = org.jhotdraw.xml.css.CSSRule.SelectorType.ELEMENT_NAME;
                break;
        }
        this.selector = (type == org.jhotdraw.xml.css.CSSRule.SelectorType.ELEMENT_NAME) ? selector : selector.substring(1);
    }

    public boolean matches(org.w3c.dom.Element elem) {
        boolean isMatch = false;
        switch (type) {
            case ALL :
                isMatch = true;
                break;
            case ELEMENT_NAME :
                java.lang.String name = elem.getLocalName();
                isMatch = name.equals(selector);
                break;
            case CLASS_ATTRIBUTE :
                java.lang.String value = elem.getAttribute("class");
                if (value != null) {
                    java.lang.String[] clazzes = value.split(" ");
                    for (java.lang.String clazz : clazzes) {
                        if (clazz.equals(selector)) {
                            isMatch = true;
                            break;
                        }
                    }
                }
                break;
            case ID_ATTRIBUTE :
                name = elem.getAttribute("id");
                isMatch = (name != null) && name.equals(selector);
                break;
        }
        return isMatch;
    }

    public void apply(org.w3c.dom.Element elem) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
            if (!elem.hasAttribute(property.getKey())) {
                elem.setAttribute(property.getKey(), property.getValue());
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("CSSRule[" + selector) + properties) + "]";
    }
}