/* @(#)JNLPClipboard.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * {@code JNLPClipboard} acts as a proxy to a JNLP {@code ClipboardService} object.
 *
 * <p>Uses Reflection to access the JNLP object, because JNLP is not available in J2SE 5.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Proxy</em><br>
 * {@code JNLPClipboard} acts as a proxy to a JNLP {@code ClipboardService} object.<br>
 * Proxy: {@link JNLPClipboard}; Target: {@code javax.jnlp.ClipboardService}.
 */
public class JNLPClipboard extends org.jhotdraw.datatransfer.AbstractClipboard {
    /**
     * The proxy target.
     */
    private java.lang.Object target;

    /**
     * Creates a new proxy for the specified target object. The target object must have a getContent
     * and a setContent method as specified by the {@code javax.jnlp.ClipboardService} interface.
     *
     * @param target
     * 		A Clipboard object.
     */
    public JNLPClipboard(java.lang.Object target) {
        this.target = target;
    }

    /**
     * Returns the proxy target.
     */
    public java.lang.Object getTarget() {
        return target;
    }

    @java.lang.Override
    public java.awt.datatransfer.Transferable getContents(java.lang.Object requestor) {
        try {
            return ((java.awt.datatransfer.Transferable) (target.getClass().getMethod("getContents").invoke(target)));
        } catch (java.lang.Exception ex) {
            java.lang.InternalError error = new java.lang.InternalError("Failed to invoke getContents() on " + target);
            error.initCause(ex);
            throw error;
        }
    }

    @java.lang.Override
    public void setContents(java.awt.datatransfer.Transferable contents, java.awt.datatransfer.ClipboardOwner owner) {
        try {
            target.getClass().getMethod("setContents", java.awt.datatransfer.Transferable.class).invoke(target, contents);
        } catch (java.lang.Exception ex) {
            java.lang.InternalError error = new java.lang.InternalError("Failed to invoke setContents(Transferable) on " + target);
            error.initCause(ex);
            throw error;
        }
    }
}