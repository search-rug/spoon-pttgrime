/* @(#)SerializationInputOutputFormat.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * {@code SerializationInputOutputFormat} uses Java Serialization for reading and and writing {@code Drawing} objects.
 */
public class SerializationOutputFormat implements org.jhotdraw.draw.io.OutputFormat {
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
    public SerializationOutputFormat() {
        this("Drawing", "ser", new org.jhotdraw.draw.DefaultDrawing());
    }

    /**
     * Creates a new instance using the specified parameters.
     */
    public SerializationOutputFormat(java.lang.String description, java.lang.String fileExtension, org.jhotdraw.draw.Drawing prototype) {
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
        java.io.ObjectOutputStream oout = new java.io.ObjectOutputStream(out);
        oout.writeObject(drawing);
        oout.flush();
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.awt.datatransfer.Transferable createTransferable(org.jhotdraw.draw.Drawing drawing, java.util.List<org.jhotdraw.draw.figure.Figure> figures, double scaleFactor) throws java.io.IOException {
        final org.jhotdraw.draw.Drawing d = ((org.jhotdraw.draw.Drawing) (prototype.clone()));
        java.util.HashMap<org.jhotdraw.draw.figure.Figure, org.jhotdraw.draw.figure.Figure> originalToDuplicateMap = new java.util.HashMap<>(figures.size());
        final java.util.ArrayList<org.jhotdraw.draw.figure.Figure> duplicates = new java.util.ArrayList<>(figures.size());
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            org.jhotdraw.draw.figure.Figure df = f.clone();
            d.add(df);
            duplicates.add(df);
            originalToDuplicateMap.put(f, df);
        }
        for (org.jhotdraw.draw.figure.Figure f : duplicates) {
            f.remap(originalToDuplicateMap, true);
        }
        return new org.jhotdraw.datatransfer.AbstractTransferable(dataFlavor) {
            @java.lang.Override
            public java.lang.Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
                if (isDataFlavorSupported(flavor)) {
                    return d;
                } else {
                    throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
                }
            }
        };
    }
}