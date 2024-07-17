/* @(#)SVGZOutputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.io;
/**
 * SVGZOutputFormat.
 */
public class SVGZOutputFormat extends org.jhotdraw.samples.svg.io.SVGOutputFormat {
    public SVGZOutputFormat() {
    }

    @java.lang.Override
    public java.lang.String getFileExtension() {
        return "svgz";
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter("Compressed Scalable Vector Graphics (SVGZ)", "svgz");
    }

    @java.lang.Override
    public void write(java.io.OutputStream out, org.jhotdraw.draw.Drawing drawing) throws java.io.IOException {
        java.util.zip.GZIPOutputStream gout = new java.util.zip.GZIPOutputStream(out);
        super.write(gout, drawing, drawing.getChildren());
        gout.finish();
    }
}