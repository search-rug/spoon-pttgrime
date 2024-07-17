/* @(#)OSXPaletteHandler.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app.osx;
/**
 * Hides all registered floating palettes, if none of the registered view windows have focus
 * anymore.
 */
public class OSXPaletteHandler {
    private java.util.HashSet<java.awt.Window> palettes = new java.util.HashSet<>();

    private java.util.HashMap<java.awt.Window, org.jhotdraw.api.app.View> windows = new java.util.HashMap<>();

    private javax.swing.Timer timer;

    private org.jhotdraw.app.OSXApplication app;

    private java.awt.event.WindowFocusListener focusHandler = new java.awt.event.WindowFocusListener() {
        /**
         * Invoked when the Window is set to be the focused Window, which means that the Window, or
         * one of its subcomponents, will receive keyboard events.
         */
        @java.lang.Override
        public void windowGainedFocus(java.awt.event.WindowEvent e) {
            timer.stop();
            if (windows.get(e.getWindow()) != null) {
                app.setActiveView(windows.get(e.getWindow()));
                showPalettes();
            }
        }

        /**
         * Invoked when the Window is no longer the focused Window, which means that keyboard events
         * will no longer be delivered to the Window or any of its subcomponents.
         */
        @java.lang.Override
        public void windowLostFocus(java.awt.event.WindowEvent e) {
            timer.restart();
        }
    };

    public OSXPaletteHandler(org.jhotdraw.app.OSXApplication app) {
        this.app = app;
        timer = new javax.swing.Timer(60, new java.awt.event.ActionListener() {
            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maybeHidePalettes();
            }
        });
        timer.setRepeats(false);
    }

    public void add(java.awt.Window window, org.jhotdraw.api.app.View view) {
        window.addWindowFocusListener(focusHandler);
        windows.put(window, view);
    }

    public void remove(java.awt.Window window) {
        windows.remove(window);
        window.removeWindowFocusListener(focusHandler);
    }

    public void addPalette(java.awt.Window palette) {
        palette.addWindowFocusListener(focusHandler);
        palettes.add(palette);
    }

    public void removePalette(java.awt.Window palette) {
        palettes.remove(palette);
        palette.removeWindowFocusListener(focusHandler);
    }

    public java.util.Set<java.awt.Window> getPalettes() {
        return java.util.Collections.unmodifiableSet(palettes);
    }

    private void showPalettes() {
        for (java.awt.Window palette : palettes) {
            if (!palette.isVisible()) {
                palette.setVisible(true);
            }
        }
    }

    private boolean isFocused(java.awt.Window w) {
        if (w.isFocused()) {
            return true;
        }
        java.awt.Window[] ownedWindows = w.getOwnedWindows();
        for (java.awt.Window ownedWindow : ownedWindows) {
            if (isFocused(ownedWindow)) {
                return true;
            }
        }
        return false;
    }

    private void maybeHidePalettes() {
        boolean hasFocus = false;
        for (java.awt.Window window : windows.keySet()) {
            if (isFocused(window)) {
                hasFocus = true;
                break;
            }
        }
        if ((!hasFocus) && (windows.size() > 0)) {
            for (java.awt.Window palette : palettes) {
                if (isFocused(palette)) {
                    hasFocus = true;
                    break;
                }
            }
        }
        if (!hasFocus) {
            for (java.awt.Window palette : palettes) {
                palette.setVisible(false);
            }
        }
    }

    public void addWindow(java.awt.Window window) {
        window.addWindowFocusListener(focusHandler);
        windows.put(window, null);
    }

    public void removeWindow(java.awt.Window window) {
        windows.remove(window);
        window.removeWindowFocusListener(focusHandler);
    }
}