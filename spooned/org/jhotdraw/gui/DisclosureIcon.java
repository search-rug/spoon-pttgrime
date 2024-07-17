/**
 *
 * @(#)DisclosureIcon.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * DisclosureIcon.
 */
public class DisclosureIcon implements javax.swing.Icon {
    /**
     * This client property needs to be set on the component. The value must be a positive Integer.
     */
    public static final java.lang.String STATE_COUNT_PROPERTY = "DisclosureIcon.numberOfStates";

    /**
     * This client property needs to be set on the component. The value must be a positive Integer.
     */
    public static final java.lang.String CURRENT_STATE_PROPERTY = "DisclosureIcon.currentState";

    @java.lang.Override
    public void paintIcon(java.awt.Component component, java.awt.Graphics g, int x, int y) {
        javax.swing.JComponent c = ((javax.swing.JComponent) (component));
        int nbOfStates = (c.getClientProperty(org.jhotdraw.gui.DisclosureIcon.STATE_COUNT_PROPERTY) instanceof java.lang.Integer) ? ((java.lang.Integer) (c.getClientProperty(org.jhotdraw.gui.DisclosureIcon.STATE_COUNT_PROPERTY))) : 2;
        int currentState = (c.getClientProperty(org.jhotdraw.gui.DisclosureIcon.CURRENT_STATE_PROPERTY) instanceof java.lang.Integer) ? ((java.lang.Integer) (c.getClientProperty(org.jhotdraw.gui.DisclosureIcon.CURRENT_STATE_PROPERTY))) : 1;
        g.setColor(java.awt.Color.black);
        g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
        g.setColor(new java.awt.Color(0x666666));
        g.fillRect(x + 1, y + 1, 1 + (((getIconWidth() - 3) * currentState) / (nbOfStates - 1)), getIconHeight() - 2);
    }

    @java.lang.Override
    public int getIconWidth() {
        return 10;
    }

    @java.lang.Override
    public int getIconHeight() {
        return 8;
    }
}