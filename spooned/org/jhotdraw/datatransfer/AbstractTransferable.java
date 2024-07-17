/* @(#)AbstractTransferable.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * Base class for transferable objects.
 */
public abstract class AbstractTransferable implements java.awt.datatransfer.Transferable {
    private java.awt.datatransfer.DataFlavor[] flavors;

    public AbstractTransferable(java.awt.datatransfer.DataFlavor flavor) {
        this.flavors = new java.awt.datatransfer.DataFlavor[]{ flavor };
    }

    public AbstractTransferable(java.awt.datatransfer.DataFlavor[] flavors) {
        this.flavors = flavors.clone();
    }

    @java.lang.Override
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return flavors.clone();
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        for (java.awt.datatransfer.DataFlavor f : flavors) {
            if (f.equals(flavor)) {
                return true;
            }
        }
        return false;
    }
}