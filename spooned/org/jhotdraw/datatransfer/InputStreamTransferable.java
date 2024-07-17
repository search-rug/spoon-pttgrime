/* @(#)InputStreamTransferable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * A Transferable with an InputStream as its transfer class.
 */
public class InputStreamTransferable extends org.jhotdraw.datatransfer.AbstractTransferable {
    private byte[] data;

    public InputStreamTransferable(java.awt.datatransfer.DataFlavor flavor, byte[] data) {
        this(new java.awt.datatransfer.DataFlavor[]{ flavor }, data);
    }

    /**
     * Note: For performance reasons this method stores a reference to the data array instead of
     * cloning it. Do not modify the data array after invoking this method.
     *
     * @param flavors
     * @param data
     */
    public InputStreamTransferable(java.awt.datatransfer.DataFlavor[] flavors, byte[] data) {
        super(flavors);
        this.data = data;
    }

    @java.lang.Override
    public java.lang.Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }
        return new java.io.ByteArrayInputStream(data);
    }
}