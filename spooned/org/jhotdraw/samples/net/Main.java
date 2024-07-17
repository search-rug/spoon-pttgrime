/* @(#)Main.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.net;
import org.jhotdraw.api.app.Application;
/**
 * Main entry point of the Net sample application. Creates an {@link Application} depending on the
 * operating system we run, sets the {@link NetApplicationModel} and then launches the application.
 * The application then creates {@link NetView}s and menu bars as specified by the application
 * model.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class Main {
    public static void main(java.lang.String[] args) {
        org.jhotdraw.api.app.Application app;
        java.lang.String os = java.lang.System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
            app = new org.jhotdraw.app.OSXApplication();
        } else if (os.startsWith("win")) {
            // app = new DefaultMDIApplication();
            app = new org.jhotdraw.app.SDIApplication();
        } else {
            app = new org.jhotdraw.app.SDIApplication();
        }
        org.jhotdraw.app.DefaultApplicationModel model = new org.jhotdraw.samples.net.NetApplicationModel();
        model.setName("JHotDraw Net");
        model.setVersion(org.jhotdraw.samples.net.Main.class.getPackage().getImplementationVersion());
        model.setCopyright("Copyright 2006-2010 (c) by the authors of JHotDraw and all its contributors.\n" + "This software is licensed under LGPL and Creative Commons 3.0 Attribution.");
        model.setViewClassName("org.jhotdraw.samples.net.NetView");
        app.setModel(model);
        app.launch(args);
    }
}