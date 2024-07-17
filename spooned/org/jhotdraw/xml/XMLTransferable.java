/* XMLTransferable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
/**
 * XMLTransferable for transfering XML documents over the clipboard and with drag and drop.
 */
public class XMLTransferable implements java.awt.datatransfer.Transferable , java.awt.datatransfer.ClipboardOwner {
    private byte[] data;

    private java.awt.datatransfer.DataFlavor flavor;

    /**
     * Creates new XMLTransferable
     */
    public XMLTransferable(byte[] data, java.lang.String mimeType, java.lang.String humanPresentableName) {
        this.data = data;
        this.flavor = new java.awt.datatransfer.DataFlavor(mimeType, humanPresentableName);
    }

    /**
     * Notifies this object that it is no longer the owner of the contents of the clipboard.
     *
     * @param clipboard
     * 		the clipboard that is no longer owned
     * @param contents
     * 		the contents which this owner had placed on the clipboard
     */
    @java.lang.Override
    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable contents) {
    }

    /**
     * Returns an object which represents the data to be transferred. The class of the object returned
     * is defined by the representation class of the flavor.
     *
     * @param flavor
     * 		the requested flavor for the data
     * @see DataFlavor#getRepresentationClass
     * @exception IOException
     * 		if the data is no longer available in the requested flavor.
     * @exception UnsupportedFlavorException
     * 		if the requested data flavor is not supported.
     */
    @java.lang.Override
    public java.lang.Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        if (this.flavor.equals(flavor)) {
            return new java.io.ByteArrayInputStream(data);
        } else {
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }
    }

    /**
     * Returns an array of DataFlavor objects indicating the flavors the data can be provided in. The
     * array should be ordered according to preference for providing the data (from most richly
     * descriptive to least descriptive).
     *
     * @return an array of data flavors in which this data can be transferred
     */
    @java.lang.Override
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return new java.awt.datatransfer.DataFlavor[]{ flavor };
    }

    /**
     * Returns whether or not the specified data flavor is supported for this object.
     *
     * @param flavor
     * 		the requested flavor for the data
     * @return boolean indicating wjether or not the data flavor is supported
     */
    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return this.flavor.equals(flavor);
    }
}