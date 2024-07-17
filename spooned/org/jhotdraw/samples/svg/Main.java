/* @(#)Main.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
import org.jhotdraw.api.app.Application;
/**
 * Main entry point of the SVG sample application. Creates an {@link Application} depending on the
 * operating system we run, sets the {@link SVGApplicationModel} and then launches the application.
 * The application then creates {@link SVGView}s and menu bars as specified by the application
 * model.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class Main {
    public static void main(java.lang.String[] args) {
        // Debug resource bundle
        org.jhotdraw.util.ResourceBundleUtil.setVerbose(true);
        org.jhotdraw.api.app.Application app;
        java.lang.String os = java.lang.System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
            app = new org.jhotdraw.app.OSXApplication();
        } else if (os.startsWith("win")) {
            // app = new MDIApplication();
            app = new org.jhotdraw.app.SDIApplication();
        } else {
            app = new org.jhotdraw.app.SDIApplication();
        }
        org.jhotdraw.samples.svg.SVGApplicationModel model = new org.jhotdraw.samples.svg.SVGApplicationModel();
        model.setName("JHotDraw SVG");
        model.setVersion(org.jhotdraw.samples.svg.Main.class.getPackage().getImplementationVersion());
        model.setCopyright("Copyright 2006-2010 (c) by the authors of JHotDraw.\n" + "This software is licensed under LGPL and Creative Commons 3.0 Attribution.");
        model.setViewClassName("org.jhotdraw.samples.svg.SVGView");
        app.setModel(model);
        app.launch(args);
    }
}