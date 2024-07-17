/* @(#)ClipboardUtil.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.datatransfer;
/**
 * {@code ClipboardUtil} provides utility methods for the Java Clipboard API.
 */
public class ClipboardUtil {
    /**
     * Holds the clipboard service instance.
     */
    private static java.awt.datatransfer.Clipboard instance;

    /**
     * Returns the ClipboardService instance. If none is set, creates a new one which tries to access
     * the system clipboard. If this fails, an instance with a JVM local clipboard is created.
     *
     * @return system clipboard or a proxy.
     */
    @java.lang.SuppressWarnings("unchecked")
    public static java.awt.datatransfer.Clipboard getClipboard() {
        if (org.jhotdraw.datatransfer.ClipboardUtil.instance != null) {
            return org.jhotdraw.datatransfer.ClipboardUtil.instance;
        }
        // Try to access the system clipboard
        try {
            // instance = new AWTClipboard(Toolkit.getDefaultToolkit().getSystemClipboard());
            org.jhotdraw.datatransfer.ClipboardUtil.instance = new org.jhotdraw.datatransfer.OSXClipboard(java.awt.Toolkit.getDefaultToolkit().getSystemClipboard());
        } catch (java.lang.SecurityException e1) {
            // Fall back to JNLP ClipboardService
            try {
                java.lang.Class<?> serviceManager = java.lang.Class.forName("javax.jnlp.ServiceManager");
                org.jhotdraw.datatransfer.ClipboardUtil.instance = new org.jhotdraw.datatransfer.JNLPClipboard(serviceManager.getMethod("lookup", java.lang.String.class).invoke(null, "javax.jnlp.ClipboardService"));
            } catch (java.lang.Exception e2) {
                // Fall back to JVM local clipboard
                org.jhotdraw.datatransfer.ClipboardUtil.instance = new org.jhotdraw.datatransfer.AWTClipboard(new java.awt.datatransfer.Clipboard("JVM Local Clipboard"));
            }
        }
        return org.jhotdraw.datatransfer.ClipboardUtil.instance;
    }

    /**
     * Sets the Clipboard singleton used by the JHotDraw framework.
     *
     * <p>If you set this null, the next call to getClipboard will create a new singleton.
     */
    public static void setClipboard(java.awt.datatransfer.Clipboard instance) {
        org.jhotdraw.datatransfer.ClipboardUtil.instance = instance;
    }
}