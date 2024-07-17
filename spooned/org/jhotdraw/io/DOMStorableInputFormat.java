/* @(#)DOMStorableOutputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * An OutputFormat that can write Drawings with DOMStorable Figure's.
 */
public class DOMStorableInputFormat implements org.jhotdraw.draw.io.InputFormat {
    private org.jhotdraw.xml.DOMFactory factory;

    /**
     * Format description used for the file filter.
     */
    private java.lang.String description;

    /**
     * File name extension used for the file filter.
     */
    private java.lang.String fileExtension;

    /**
     * Image IO image format name.
     */
    private java.lang.String formatName;

    /**
     * The mime type is used for clipboard access.
     */
    private java.lang.String mimeType;

    /**
     * The data flavor constructed from the mime type.
     */
    private java.awt.datatransfer.DataFlavor dataFlavor;

    /**
     * Creates a new instance with format name "Drawing", file extension "xml" and mime type
     * "image/x-jhotdraw".
     */
    public DOMStorableInputFormat(org.jhotdraw.xml.DOMFactory factory) {
        this(factory, "Drawing", "xml", "image/x-jhotdraw");
    }

    /**
     * Creates a new instance using the specified parameters.
     *
     * @param factory
     * 		The factory for creating Figures from XML elements.
     * @param description
     * 		The format description to be used for the file filter.
     * @param fileExtension
     * 		The file extension to be used for file filter.
     * @param mimeType
     * 		The Mime Type is used for clipboard access.
     */
    public DOMStorableInputFormat(org.jhotdraw.xml.DOMFactory factory, java.lang.String description, java.lang.String fileExtension, java.lang.String mimeType) {
        this.factory = factory;
        this.description = description;
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
        try {
            this.dataFlavor = new java.awt.datatransfer.DataFlavor(mimeType);
        } catch (java.lang.ClassNotFoundException ex) {
            throw new java.lang.InternalError("Unable to create data flavor for mime type:" + mimeType, ex);
        }
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtension);
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        org.jhotdraw.xml.JavaxDOMInput domi = new org.jhotdraw.xml.JavaxDOMInput(factory, in);
        domi.openElement(factory.getName(drawing));
        domi.openElement("figures");
        if (replace) {
            drawing.removeAllChildren();
        }
        for (int i = 0; i < domi.getElementCount(); i++) {
            drawing.add(((org.jhotdraw.draw.figure.Figure) (domi.readObject(i))));
        }
        domi.closeElement();
        domi.closeElement();
    }

    @java.lang.Override
    public void read(java.awt.datatransfer.Transferable t, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        java.util.List<org.jhotdraw.draw.figure.Figure> figures = new java.util.ArrayList<>();
        java.io.InputStream in = ((java.io.InputStream) (t.getTransferData(new java.awt.datatransfer.DataFlavor(mimeType, description))));
        org.jhotdraw.xml.JavaxDOMInput domi = new org.jhotdraw.xml.JavaxDOMInput(factory, in);
        domi.openElement("Drawing-Clip");
        for (int i = 0, n = domi.getElementCount(); i < n; i++) {
            figures.add(((org.jhotdraw.draw.figure.Figure) (domi.readObject(i))));
        }
        domi.closeElement();
        if (replace) {
            drawing.removeAllChildren();
        }
        drawing.addAll(figures);
    }
}