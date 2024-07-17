/* @(#)Main.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy;
import org.jhotdraw.api.app.Application;
/**
 * Main entry point of the Teddy sample application. Creates an {@link Application} depending on the
 * operating system we run, sets the {@link TeddyApplicationModel} and then launches the
 * application. The application then creates {@link TeddyView}s and menu bars as specified by the
 * application model.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class Main {
    public static final java.lang.String NAME = "JHotDraw Teddy";

    public static final java.lang.String COPYRIGHT = "© 1996-2013 by the original authors of JHotDraw and all its contributors.";

    /**
     * Launches the application.
     *
     * <p>Supported command line parameters:
     *
     * <pre>
     * -app osx|mdi|sdi|cross     // Application type
     * </pre>
     *
     * @param args
     * 		the command line arguments
     */
    public static void main(java.lang.String[] args) throws java.lang.Exception {
        org.jhotdraw.samples.teddy.TeddyApplicationModel tam = new org.jhotdraw.samples.teddy.TeddyApplicationModel();
        tam.setCopyright(org.jhotdraw.samples.teddy.Main.COPYRIGHT);
        tam.setName(org.jhotdraw.samples.teddy.Main.NAME);
        tam.setViewClassName("org.jhotdraw.samples.teddy.TeddyView");
        tam.setVersion(org.jhotdraw.samples.teddy.Main.class.getPackage().getImplementationVersion());
        java.util.HashMap<java.lang.String, java.lang.String> types = new java.util.HashMap<java.lang.String, java.lang.String>();
        types.put("mdi", "org.jhotdraw.app.MDIApplication");
        types.put("sdi", "org.jhotdraw.app.SDIApplication");
        types.put("osx", "org.jhotdraw.app.OSXApplication");
        types.put("cross", "org.jhotdraw.app.CrossPlatformApplication");
        java.lang.String type;
        if (java.lang.System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
            type = "osx";
        } else {
            type = "sdi";
        }
        for (int i = 0; i < args.length; i++) {
            if ("-app".equals(args[i]) && (i < (args.length - 1))) {
                type = args[++i];
                if (!types.containsKey(type)) {
                    throw new java.lang.IllegalArgumentException("-app " + args[i]);
                }
            }
        }
        org.jhotdraw.api.app.Application app = ((org.jhotdraw.api.app.Application) (java.lang.Class.forName(types.get(type)).newInstance()));
        app.setModel(tam);
        app.launch(args);
    }
}