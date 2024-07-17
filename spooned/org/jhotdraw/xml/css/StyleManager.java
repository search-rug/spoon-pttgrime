/* @(#)StyleManager.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.

Original code taken from article "Swing and CSS" by Joshua Marinacci 10/14/2003
http://today.java.net/pub/a/today/2003/10/14/swingcss.html
 */
package org.jhotdraw.xml.css;
/**
 * StyleManager applies styling Rules to an XML DOM. This class supports net.n3.nanoxml as well as
 * org.w3c.dom.
 */
public class StyleManager {
    private java.util.List<org.jhotdraw.xml.css.CSSRule> rules;

    public StyleManager() {
        rules = new java.util.ArrayList<org.jhotdraw.xml.css.CSSRule>();
    }

    public void add(org.jhotdraw.xml.css.CSSRule rule) {
        rules.add(rule);
    }

    public void applyStylesTo(org.w3c.dom.Element elem) {
        for (org.jhotdraw.xml.css.CSSRule rule : rules) {
            if (rule.matches(elem)) {
                rule.apply(elem);
            }
        }
    }

    public void clear() {
        rules.clear();
    }
}