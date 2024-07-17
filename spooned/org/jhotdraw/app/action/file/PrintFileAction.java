/* @(#)PrintFileAction.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.action.file;
import org.jhotdraw.app.PrintableView;
/**
 * Presents a printer chooser to the user and then prints the {@link org.jhotdraw.api.app.View}.
 *
 * <p>This action requires that the view implements the {@link PrintableView} interface.
 *
 * <p>This action is called when the user selects the Print item in the File menu. The menu item is
 * automatically created by the application.
 *
 * <p>If you want this behavior in your application, you have to create it and put it in your {@code ApplicationModel} in method {@link org.jhotdraw.app.ApplicationModel#initApplication}.
 *
 * <p>You should also create a {@link PrintFileAction} when you create this action. <hr> <b>Design
 * Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define together the contracts of a smaller framework
 * inside of the JHotDraw framework for document oriented applications.<br>
 * Contract: {@link PrintableView}.<br>
 * Client: {@link org.jhotdraw.app.action.file.PrintFileAction}. <hr>
 */
public class PrintFileAction extends org.jhotdraw.action.AbstractViewAction {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String ID = "file.print";

    public PrintFileAction(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View view) {
        super(app, view);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, org.jhotdraw.app.action.file.PrintFileAction.ID);
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        org.jhotdraw.app.PrintableView view = ((org.jhotdraw.app.PrintableView) (getActiveView()));
        view.setEnabled(false);
        if ("true".equals(java.lang.System.getProperty("apple.awt.graphics.UseQuartz", "false"))) {
            printQuartz(view);
        } else {
            printJava2D(view);
        }
        view.setEnabled(true);
    }

    /* This prints at 72 DPI only. We might need this for some JVM versions on
    Mac OS X.
     */
    public void printJava2D(org.jhotdraw.app.PrintableView v) {
        java.awt.print.Pageable pageable = v.createPageable();
        if (pageable == null) {
            throw new java.lang.InternalError("View does not have a method named java.awt.Pageable createPageable()");
        }
        try {
            java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
            // FIXME - PrintRequestAttributeSet should be retrieved from View
            javax.print.attribute.PrintRequestAttributeSet attr = new javax.print.attribute.HashPrintRequestAttributeSet();
            attr.add(new javax.print.attribute.standard.PrinterResolution(300, 300, javax.print.attribute.standard.PrinterResolution.DPI));
            job.setPageable(pageable);
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (java.awt.print.PrinterException e) {
                    java.lang.String message = (e.getMessage() == null) ? e.toString() : e.getMessage();
                    org.jhotdraw.api.app.View view = getActiveView();
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(view.getComponent(), (((("<html>" + javax.swing.UIManager.getString("OptionPane.css")) + "<b>") + labels.getString("couldntPrint")) + "</b><br>") + (message == null ? "" : message));
                }
            } else {
                java.lang.System.out.println("JOB ABORTED!");
            }
        } catch (java.lang.Throwable t) {
            t.printStackTrace();
        }
    }

    /* This prints at 72 DPI only. We might need this for some JVM versions on
    Mac OS X.
     */
    public void printJava2DAlternative(org.jhotdraw.app.PrintableView v) {
        java.awt.print.Pageable pageable = v.createPageable();
        if (pageable == null) {
            throw new java.lang.InternalError("View does not have a method named java.awt.Pageable createPageable()");
        }
        try {
            final java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
            javax.print.attribute.PrintRequestAttributeSet attr = new javax.print.attribute.HashPrintRequestAttributeSet();
            attr.add(new javax.print.attribute.standard.PrinterResolution(300, 300, javax.print.attribute.standard.PrinterResolution.DPI));
            job.setPageable(pageable);
            if (job.printDialog(attr)) {
                try {
                    job.print();
                } catch (java.awt.print.PrinterException e) {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    org.jhotdraw.gui.JSheet.showMessageSheet(getActiveView().getComponent(), labels.getFormatted("couldntPrint", e));
                }
            } else {
                java.lang.System.out.println("JOB ABORTED!");
            }
        } catch (java.lang.Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * On Mac OS X with the Quartz rendering engine, the following code achieves the best results.
     */
    public void printQuartz(org.jhotdraw.app.PrintableView v) {
        java.awt.Frame frame = ((java.awt.Frame) (javax.swing.SwingUtilities.getWindowAncestor(v.getComponent())));
        final java.awt.print.Pageable pageable = v.createPageable();
        final double resolution = 300.0;
        java.awt.JobAttributes jobAttr = new java.awt.JobAttributes();
        // FIXME - PageAttributes should be retrieved from View
        java.awt.PageAttributes pageAttr = new java.awt.PageAttributes();
        pageAttr.setMedia(java.awt.PageAttributes.MediaType.A4);
        pageAttr.setPrinterResolution(((int) (resolution)));
        final java.awt.PrintJob pj = frame.getToolkit().getPrintJob(frame, "Job Title", jobAttr, pageAttr);
        getActiveView().setEnabled(false);
        new javax.swing.SwingWorker() {
            @java.lang.Override
            protected java.lang.Object doInBackground() throws java.lang.Exception {
                // Compute page format from settings of the print job
                java.awt.print.Paper paper = new java.awt.print.Paper();
                paper.setSize((pj.getPageDimension().width / resolution) * 72.0, (pj.getPageDimension().height / resolution) * 72.0);
                paper.setImageableArea(64.0, 32.0, paper.getWidth() - 96.0, paper.getHeight() - 64);
                java.awt.print.PageFormat pageFormat = new java.awt.print.PageFormat();
                pageFormat.setPaper(paper);
                // Print the job
                try {
                    for (int i = 0, n = pageable.getNumberOfPages(); i < n; i++) {
                        java.awt.print.PageFormat pf = pageable.getPageFormat(i);
                        pf = pageFormat;
                        java.awt.Graphics g = pj.getGraphics();
                        if (g instanceof java.awt.Graphics2D) {
                            pageable.getPrintable(i).print(g, pf, i);
                        } else {
                            java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(((int) ((pf.getImageableWidth() * resolution) / 72.0)), ((int) ((pf.getImageableHeight() * resolution) / 72.0)), java.awt.image.BufferedImage.TYPE_INT_RGB);
                            java.awt.Graphics2D bufG = buf.createGraphics();
                            bufG.setBackground(java.awt.Color.WHITE);
                            bufG.fillRect(0, 0, buf.getWidth(), buf.getHeight());
                            bufG.scale(resolution / 72.0, resolution / 72.0);
                            bufG.translate(-pf.getImageableX(), -pf.getImageableY());
                            pageable.getPrintable(i).print(bufG, pf, i);
                            bufG.dispose();
                            g.drawImage(buf, ((int) ((pf.getImageableX() * resolution) / 72.0)), ((int) ((pf.getImageableY() * resolution) / 72.0)), null);
                            buf.flush();
                        }
                        g.dispose();
                    }
                } finally {
                    pj.end();
                }
                return null;
            }

            @java.lang.Override
            protected void done() {
                getActiveView().setEnabled(true);
            }
        }.execute();
    }

    /**
     * Returns true if the action is enabled. The enabled state of the action depends on the state
     * that has been set using setEnabled() and on the enabled state of the application.
     *
     * @return true if the action is enabled, false otherwise
     * @see Action#isEnabled
     */
    @java.lang.Override
    public boolean isEnabled() {
        return super.isEnabled() && (getActiveView() instanceof org.jhotdraw.app.PrintableView);
    }
}