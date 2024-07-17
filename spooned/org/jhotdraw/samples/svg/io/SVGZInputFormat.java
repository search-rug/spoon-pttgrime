/* @(#)SVGZInputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * SVGZInputFormat supports reading of uncompressed and compressed SVG images.
 */
public class SVGZInputFormat extends org.jhotdraw.samples.svg.io.SVGInputFormat {
    public SVGZInputFormat() {
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter("Scalable Vector Graphics (SVG, SVGZ)", new java.lang.String[]{ "svg", "svgz" });
    }

    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        java.io.BufferedInputStream bin = (in instanceof java.io.BufferedInputStream) ? ((java.io.BufferedInputStream) (in)) : new java.io.BufferedInputStream(in);
        bin.mark(2);
        int magic = (bin.read() & 0xff) | ((bin.read() & 0xff) << 8);
        bin.reset();
        if (magic == java.util.zip.GZIPInputStream.GZIP_MAGIC) {
            super.read(new java.util.zip.GZIPInputStream(bin), drawing, replace);
        } else {
            super.read(bin, drawing, replace);
        }
    }
}