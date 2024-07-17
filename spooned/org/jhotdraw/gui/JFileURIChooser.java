/* @(#)JFileURIChooser.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * JFileURIChooser.
 */
public class JFileURIChooser extends javax.swing.JFileChooser implements org.jhotdraw.api.gui.URIChooser {
    private static final long serialVersionUID = 1L;

    @java.lang.Override
    public void setSelectedURI(java.net.URI uri) {
        setSelectedFile(uri == null ? null : new java.io.File(uri));
    }

    @java.lang.Override
    public java.net.URI getSelectedURI() {
        return getSelectedFile() == null ? null : getSelectedFile().toURI();
    }

    @java.lang.Override
    public javax.swing.JComponent getComponent() {
        return this;
    }
}