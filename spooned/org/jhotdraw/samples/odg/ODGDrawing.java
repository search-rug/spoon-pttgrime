/* @(#)ODGDrawing.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
/**
 * ODGDrawing.
 *
 * <p>XXX - This class is going away in future versions: We don't need to subclass QuadTreeDrawing
 * for ODG since we can represent all ODG-specific AttributeKey's instead of using JavaBeans
 * properties.
 */
public class ODGDrawing extends org.jhotdraw.draw.QuadTreeDrawing {
    private static final long serialVersionUID = 1L;

    private java.lang.String title;

    private java.lang.String description;

    public ODGDrawing() {
    }

    public void setTitle(java.lang.String newValue) {
        java.lang.String oldValue = title;
        title = newValue;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setDescription(java.lang.String newValue) {
        java.lang.String oldValue = description;
        description = newValue;
    }

    public java.lang.String getDescription() {
        return description;
    }
}