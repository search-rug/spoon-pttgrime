/* @(#)NumberedViewFactory.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.

Original code (c) Stanislav Lapitsky
http://www.developer.com/java/other/article.php/3318421
 */
package org.jhotdraw.samples.teddy.text;
/**
 * NumberedViewFactory.
 */
public class NumberedViewFactory implements javax.swing.text.ViewFactory {
    private boolean isLineNumbersVisible;

    public void setLineNumbersVisible(boolean newValue) {
        boolean oldValue = isLineNumbersVisible;
        isLineNumbersVisible = newValue;
    }

    public boolean isLineNumbersVisible() {
        return isLineNumbersVisible;
    }

    public javax.swing.text.View create(javax.swing.text.Element elem) {
        java.lang.String kind = elem.getName();
        if (kind != null) {
            if (kind.equals(javax.swing.text.AbstractDocument.ContentElementName)) {
                return new javax.swing.text.LabelView(elem);
            } else if (kind.equals(javax.swing.text.AbstractDocument.ParagraphElementName)) {
                // if (isLineNumbersVisible()) {
                return new org.jhotdraw.samples.teddy.text.NumberedParagraphView(elem, this);
                // } else {
                // return new ParagraphView(elem);
                // }
            } else if (kind.equals(javax.swing.text.AbstractDocument.SectionElementName)) {
                return new javax.swing.text.BoxView(elem, javax.swing.text.View.Y_AXIS);
            } else if (kind.equals(javax.swing.text.StyleConstants.ComponentElementName)) {
                return new javax.swing.text.ComponentView(elem);
            } else if (kind.equals(javax.swing.text.StyleConstants.IconElementName)) {
                return new javax.swing.text.IconView(elem);
            }
        }
        // default to text display
        return new javax.swing.text.LabelView(elem);
    }
}