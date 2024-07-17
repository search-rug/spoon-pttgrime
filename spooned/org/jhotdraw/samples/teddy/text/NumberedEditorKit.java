/* @(#)NumberedEditorKit.java

Copyright (c) 2005 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.

Original version (c) Stanislav Lapitsky
http://www.developer.com/java/other/article.php/3318421
 */
package org.jhotdraw.samples.teddy.text;
/**
 * NumberedEditorKit.
 *
 * <p>Usage:
 *
 * <pre>
 * JEditorPane edit = new JEditorPane();
 * edit.setEditorKit(new NumberedEditorKit());
 * </pre>
 */
public class NumberedEditorKit extends javax.swing.text.StyledEditorKit {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.samples.teddy.text.NumberedViewFactory viewFactory;

    @java.lang.Override
    public javax.swing.text.ViewFactory getViewFactory() {
        if (viewFactory == null) {
            viewFactory = new org.jhotdraw.samples.teddy.text.NumberedViewFactory();
        }
        return viewFactory;
    }
}