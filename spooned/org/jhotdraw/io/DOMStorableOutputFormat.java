/* @(#)DOMStorableOutputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * An OutputFormat that can write Drawings with DOMStorable Figure's.
 */
public class DOMStorableOutputFormat implements org.jhotdraw.draw.io.OutputFormat {
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
    public DOMStorableOutputFormat(org.jhotdraw.xml.DOMFactory factory) {
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
    public DOMStorableOutputFormat(org.jhotdraw.xml.DOMFactory factory, java.lang.String description, java.lang.String fileExtension, java.lang.String mimeType) {
        this.factory = factory;
        this.description = description;
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
        try {
            this.dataFlavor = new java.awt.datatransfer.DataFlavor(mimeType);
        } catch (java.lang.ClassNotFoundException ex) {
            java.lang.InternalError error = new java.lang.InternalError("Unable to create data flavor for mime type:" + mimeType);
            error.initCause(ex);
            throw error;
        }
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtension);
    }

    @java.lang.Override
    public java.lang.String getFileExtension() {
        return fileExtension;
    }

    @java.lang.Override
    public void write(java.net.URI uri, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        write(new java.io.File(uri), drawing);
    }

    public void write(java.io.File file, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        try (java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file))) {
            write(out, drawing);
        }
    }

    @java.lang.Override
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        org.jhotdraw.xml.JavaxDOMOutput domo = new org.jhotdraw.xml.JavaxDOMOutput(factory);
        domo.openElement(factory.getName(drawing));
        // drawing.write(domo);
        domo.openElement("figures");
        for (org.jhotdraw.draw.figure.Figure f : drawing.getChildren()) {
            domo.writeObject(f);
        }
        domo.closeElement();
        domo.closeElement();
        domo.save(out);
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor) throws java.io.IOException {
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        org.jhotdraw.xml.JavaxDOMOutput domo = new org.jhotdraw.xml.JavaxDOMOutput(factory);
        domo.openElement("Drawing-Clip");
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            domo.writeObject(f);
        }
        domo.closeElement();
        domo.save(buf);
        return new org.jhotdraw.datatransfer.InputStreamTransferable(new java.awt.datatransfer.DataFlavor(mimeType, description), buf.toByteArray());
    }
}