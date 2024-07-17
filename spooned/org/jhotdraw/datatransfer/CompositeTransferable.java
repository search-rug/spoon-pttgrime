/* @(#)CompositeTransferable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
/**
 * ComoositeTransferable.
 *
 * @author Werner Randelshofer
 */
public class CompositeTransferable implements java.awt.datatransfer.Transferable , java.awt.datatransfer.ClipboardOwner {
    private java.util.HashMap<java.awt.datatransfer.DataFlavor, java.awt.datatransfer.Transferable> transferables = new java.util.HashMap<>();

    private java.util.List<java.awt.datatransfer.DataFlavor> flavors = new java.util.ArrayList<>();

    /**
     * Creates a new instance of CompositeTransferable
     */
    public CompositeTransferable() {
    }

    public void add(java.awt.datatransfer.Transferable t) {
        java.awt.datatransfer.DataFlavor[] f = t.getTransferDataFlavors();
        for (java.awt.datatransfer.DataFlavor f1 : f) {
            if (!transferables.containsKey(f1)) {
                flavors.add(f1);
            }
            transferables.put(f1, t);
        }
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
        java.awt.datatransfer.Transferable t = transferables.get(flavor);
        if (t == null) {
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }
        return t.getTransferData(flavor);
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
        return flavors.toArray(new java.awt.datatransfer.DataFlavor[transferables.size()]);
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
        return transferables.containsKey(flavor);
    }

    @java.lang.Override
    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable contents) {
    }
}