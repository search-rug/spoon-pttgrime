/* @(#)DrawingComponent.java

Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * The DrawingComponent holds the drawing editor used by the DrawingApplet.
 *
 * <p>The DrawingComponent covers the whole content pane of the DrawingApplet. It thus has to
 * provide the user interface elements for saving the drawing and canceling the applet on its own.
 * The DrawingApplet registers with the DrawingComponent as an ActionListener to receive "save" and
 * "cancel" action commands.
 */
public interface DrawingComponent {
    /**
     * Returns the component of the DrawingComponent.
     */
    public javax.swing.JComponent getComponent();

    /**
     * Returns the drawing of the DrawingComponent.
     */
    public org.jhotdraw.draw.Drawing getDrawing();

    /**
     * Sets the drawing of the DrawingComponent.
     */
    public void setDrawing(org.jhotdraw.draw.Drawing newValue);

    /**
     * Adds an ActionListener.
     *
     * <p>The ActionListener receives an ActionEvent with action command "save" when the user clicks
     * at the save button on the drawing component.
     *
     * <p>The ActionListener receives an ActionEvent with action command "cancel" when the user clicks
     * at the cancel button on the drawing component.
     */
    public void addActionListener(java.awt.event.ActionListener listener);

    /**
     * Removes an ActionListener.
     */
    public void removeActionListener(java.awt.event.ActionListener listener);

    /**
     * Returns a summary about the changes made on the drawing.
     */
    public java.lang.String getSummary();
}