/* @(#)DrawingPageable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.print;
/**
 * {@code DrawingPageable} can be used to print a {@link Drawing} using the java.awt.print API.
 *
 * <p>Usage:
 *
 * <pre>
 * Pageable pageable = new DrawingPageable(aDrawing);
 * PrinterJob job = PrinterJob.getPrinterJob();
 * job.setPageable(pageable);
 * if (job.printDialog()) {
 *     try {
 *         job.print();
 *      } catch (PrinterException e) {
 *          ...inform the user that we couldn't print...
 *      }
 * }
 * </pre>
 *
 * @see org.jhotdraw.app.action.file.PrintFileAction
 */
public class DrawingPageable implements java.awt.print.Pageable {
    private org.jhotdraw.draw.Drawing drawing;

    private java.awt.print.PageFormat pageFormat;

    private boolean isAutorotate = false;

    public DrawingPageable(org.jhotdraw.draw.Drawing drawing) {
        this.drawing = drawing;
        java.awt.print.Paper paper = new java.awt.print.Paper();
        pageFormat = new java.awt.print.PageFormat();
        pageFormat.setPaper(paper);
    }

    @java.lang.Override
    public int getNumberOfPages() {
        return 1;
    }

    @java.lang.Override
    public java.awt.print.PageFormat getPageFormat(int pageIndex) throws java.lang.IndexOutOfBoundsException {
        return pageFormat;
    }

    @java.lang.Override
    public java.awt.print.Printable getPrintable(int pageIndex) throws java.lang.IndexOutOfBoundsException {
        if ((pageIndex < 0) || (pageIndex >= getNumberOfPages())) {
            throw new java.lang.IndexOutOfBoundsException("Invalid page index:" + pageIndex);
        }
        return new java.awt.print.Printable() {
            @java.lang.Override
            public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
                return printPage(graphics, pageFormat, pageIndex);
            }
        };
    }

    public int printPage(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
        if ((pageIndex < 0) || (pageIndex >= getNumberOfPages())) {
            return java.awt.print.Printable.NO_SUCH_PAGE;
        }
        if (drawing.getChildCount() > 0) {
            java.awt.Graphics2D g = ((java.awt.Graphics2D) (graphics));
            setRenderingHints(g);
            // Determine the draw bounds of the drawing
            java.awt.geom.Rectangle2D.Double drawBounds = null;
            double scale = org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g);
            for (org.jhotdraw.draw.figure.Figure f : drawing.getChildren()) {
                if (drawBounds == null) {
                    drawBounds = f.getDrawingArea(scale);
                } else {
                    drawBounds.add(f.getDrawingArea(scale));
                }
            }
            // Setup a transformation for the drawing
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            // Maybe rotate drawing
            if ((isAutorotate && (drawBounds.width > drawBounds.height)) && (pageFormat.getImageableWidth() < pageFormat.getImageableHeight())) {
                double scaleFactor = java.lang.Math.min(pageFormat.getImageableWidth() / drawBounds.height, pageFormat.getImageableHeight() / drawBounds.width);
                tx.scale(scaleFactor, scaleFactor);
                tx.translate(drawBounds.height, 0.0);
                tx.rotate(java.lang.Math.PI / 2.0, 0, 0);
                tx.translate(-drawBounds.x, -drawBounds.y);
            } else {
                double scaleFactor = java.lang.Math.min(pageFormat.getImageableWidth() / drawBounds.width, pageFormat.getImageableHeight() / drawBounds.height);
                tx.scale(scaleFactor, scaleFactor);
                tx.translate(-drawBounds.x, -drawBounds.y);
            }
            g.transform(tx);
            // Draw the drawing
            drawing.draw(g);
        }
        return java.awt.print.Printable.PAGE_EXISTS;
    }

    protected void setRenderingHints(java.awt.Graphics2D g) {
        g.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}