/* @(#)AWTClipboard.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
/**
 * {@code AWTClipboard} acts as a proxy to an AWT {@code Clipboard} object.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Proxy</em><br>
 * {@code AWTClipboard} acts as a proxy to an AWT {@code Clipboard} object.<br>
 * Proxy: {@link AWTClipboard}; Target: {@code java.awt.datatransfer.Clipboard}.
 */
public class AWTClipboard extends org.jhotdraw.datatransfer.AbstractClipboard {
    /**
     * The proxy target.
     */
    private java.awt.datatransfer.Clipboard target;

    /**
     * Creates a new proxy for the specified target object.
     *
     * @param target
     * 		A Clipboard object.
     */
    public AWTClipboard(java.awt.datatransfer.Clipboard target) {
        this.target = target;
    }

    /**
     * Returns the proxy target.
     */
    public java.awt.datatransfer.Clipboard getTarget() {
        return target;
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable getContents(java.lang.Object requestor) {
        return target.getContents(requestor);
    }

    /**
     * Sets the current contents of the clipboard to the specified {@code Transferable} object.
     *
     * @param contents
     * 		The {@code Transferable} object representing clipboard content.
     */
    @java.lang.Override
    public void setContents(java.awt.datatransfer.Transferable contents, java.awt.datatransfer.ClipboardOwner owner) {
        target.setContents(contents, owner);
    }
}