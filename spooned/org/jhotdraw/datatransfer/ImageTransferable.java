/* @(#)ImageTransferable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * A Transferable with an Image as its transfer class.
 */
public class ImageTransferable implements java.awt.datatransfer.Transferable {
    private java.awt.Image image;

    public static final java.awt.datatransfer.DataFlavor IMAGE_PNG_FLAVOR;

    static {
        try {
            IMAGE_PNG_FLAVOR = new java.awt.datatransfer.DataFlavor("image/png");
        } catch (java.lang.Exception e) {
            java.lang.InternalError error = new java.lang.InternalError("Unable to crate image/png data flavor");
            error.initCause(e);
            throw error;
        }
    }

    public ImageTransferable(java.awt.Image image) {
        this.image = image;
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return flavor.equals(java.awt.datatransfer.DataFlavor.imageFlavor) || flavor.equals(org.jhotdraw.datatransfer.ImageTransferable.IMAGE_PNG_FLAVOR);
    }

    @java.lang.Override
    public java.lang.Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        /* if (! isDataFlavorSupported(flavor)) {
        throw new UnsupportedFlavorException(flavor);
        }
         */
        if (flavor.equals(java.awt.datatransfer.DataFlavor.imageFlavor)) {
            return image;
        } else if (flavor.equals(org.jhotdraw.datatransfer.ImageTransferable.IMAGE_PNG_FLAVOR)) {
            java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(org.jhotdraw.util.Images.toBufferedImage(image), "PNG", buf);
            return new java.io.ByteArrayInputStream(buf.toByteArray());
        } else {
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }
    }

    @java.lang.Override
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return new java.awt.datatransfer.DataFlavor[]{ java.awt.datatransfer.DataFlavor.imageFlavor, org.jhotdraw.datatransfer.ImageTransferable.IMAGE_PNG_FLAVOR };
    }
}