/* @(#)OSXClipboard.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * OSXClipboard.
 */
public class OSXClipboard extends org.jhotdraw.datatransfer.AWTClipboard {
    public OSXClipboard(java.awt.datatransfer.Clipboard target) {
        super(target);
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable getContents(java.lang.Object requestor) {
        java.awt.datatransfer.Transferable t = super.getContents(requestor);
        try {
            java.lang.Class<?> c = java.lang.Class.forName("ch.randelshofer.quaqua.osx.OSXClipboardTransferable");
            @java.lang.SuppressWarnings("unchecked")
            boolean isAvailable = ((java.lang.Boolean) (c.getMethod("isNativeCodeAvailable").invoke(null)));
            if (isAvailable) {
                org.jhotdraw.datatransfer.CompositeTransferable ct = new org.jhotdraw.datatransfer.CompositeTransferable();
                ct.add(t);
                ct.add(((java.awt.datatransfer.Transferable) (c.newInstance())));
                t = ct;
            }
        } catch (java.lang.Throwable ex) {
            // silently suppress
        }
        return t;
    }
}