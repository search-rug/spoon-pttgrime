/* @(#)BackgroundTask.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * A utility class for dialogs.
 */
public class Dialogs {
    /**
     * Prevent instance creation.
     */
    private Dialogs() {
    }

    public static java.awt.Color showColorChooserDialog(javax.swing.JColorChooser chooser, java.awt.Component component, java.lang.String title, java.awt.Color initialColor) throws java.awt.HeadlessException {
        final javax.swing.JColorChooser pane = chooser;
        org.jhotdraw.gui.Dialogs.ColorTracker ok = new org.jhotdraw.gui.Dialogs.ColorTracker(pane);
        javax.swing.JDialog dialog = javax.swing.JColorChooser.createDialog(component, title, true, pane, ok, null);
        dialog.setVisible(true);// blocks until user brings dialog down...

        return ok.getColor();
    }

    private static class ColorTracker implements java.awt.event.ActionListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        javax.swing.JColorChooser chooser;

        java.awt.Color color;

        public ColorTracker(javax.swing.JColorChooser c) {
            chooser = c;
        }

        @java.lang.Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            color = chooser.getColor();
        }

        public java.awt.Color getColor() {
            return color;
        }
    }
}