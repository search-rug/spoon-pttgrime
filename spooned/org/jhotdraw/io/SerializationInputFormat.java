/* @(#)SerializationInputOutputFormat.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * {@code SerializationInputOutputFormat} uses Java Serialization for reading and and writing {@code Drawing} objects.
 */
public class SerializationInputFormat implements org.jhotdraw.draw.io.InputFormat {
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

    private org.jhotdraw.draw.Drawing prototype;

    /**
     * Creates a new instance with format name "Drawing", file extension "xml" and mime type
     * "image/x-jhotdraw".
     */
    public SerializationInputFormat() {
        this("Drawing", "ser", new org.jhotdraw.draw.DefaultDrawing());
    }

    /**
     * Creates a new instance using the specified parameters.
     */
    public SerializationInputFormat(java.lang.String description, java.lang.String fileExtension, org.jhotdraw.draw.Drawing prototype) {
        this.description = description;
        this.fileExtension = fileExtension;
        this.mimeType = java.awt.datatransfer.DataFlavor.javaSerializedObjectMimeType;
        this.prototype = prototype;
        this.dataFlavor = new java.awt.datatransfer.DataFlavor(prototype.getClass(), description);
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtension);
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        try {
            java.io.ObjectInputStream oin = new java.io.ObjectInputStream(in);
            org.jhotdraw.draw.Drawing d = ((org.jhotdraw.draw.Drawing) (oin.readObject()));
            if (replace) {
                for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> e : d.attr().getAttributes().entrySet()) {
                    drawing.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (e.getKey())), e.getValue());
                }
            }
            for (org.jhotdraw.draw.figure.Figure f : d.getChildren()) {
                drawing.add(f);
            }
        } catch (java.lang.ClassNotFoundException ex) {
            java.io.IOException ioe = new java.io.IOException("Couldn't read drawing.");
            ioe.initCause(ex);
            throw ioe;
        }
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public void read(java.awt.datatransfer.Transferable t, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        try {
            org.jhotdraw.draw.Drawing d = ((org.jhotdraw.draw.Drawing) (t.getTransferData(dataFlavor)));
            if (replace) {
                for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> e : d.attr().getAttributes().entrySet()) {
                    drawing.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (e.getKey())), e.getValue());
                }
            }
            for (org.jhotdraw.draw.figure.Figure f : d.getChildren()) {
                drawing.add(f);
            }
        } catch (java.lang.Throwable th) {
            th.printStackTrace();
        }
    }
}