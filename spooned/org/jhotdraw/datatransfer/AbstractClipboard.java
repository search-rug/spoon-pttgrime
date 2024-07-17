/* @(#)AbstractClipboard.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
import java.awt.datatransfer.Transferable;
/**
 * {@code AbstractClipboard} is a wrapper for the system clipboard which can be either the Java AWT
 * Clipboard, the javax.jnlp.AbstractClipboard or native JNI code.
 */
public abstract class AbstractClipboard extends java.awt.datatransfer.Clipboard {
    public AbstractClipboard() {
        super("Clipboard Proxy");
    }

    /**
     * Returns a {@code Transferable} object representing the current contents of the clipboard. If
     * the clipboard currently has no contents, it returns null.
     *
     * @return The current {@code Transferable} object on the clipboard.
     */
    @java.lang.Override
    public abstract java.awt.datatransfer.Transferable getContents(java.lang.Object requestor);

    /**
     * Sets the current contents of the clipboard to the specified {@code Transferable} object.
     *
     * @param contents
     * 		The {@code Transferable} object representing clipboard content.
     */
    @java.lang.Override
    public abstract void setContents(java.awt.datatransfer.Transferable contents, java.awt.datatransfer.ClipboardOwner owner);
}