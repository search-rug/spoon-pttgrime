/* @(#)ActionUtil.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * Provides constants and static operations on <code>Action</code> objects.
 */
public class ActionUtil {
    /**
     * Key for the selected state of an action. The value must be a Boolean.
     */
    public static final java.lang.String SELECTED_KEY = javax.swing.Action.SELECTED_KEY;

    /**
     * All actions with equal value are put into the same submenu. The value must be a String or an
     * array of Strings. Each element of the array represents a menu.
     */
    public static final java.lang.String SUBMENU_KEY = "submenu";

    /**
     * All actions with equal value are created as a radio button and put into the same group. The
     * value must be an object.
     */
    public static final java.lang.String BUTTON_GROUP_KEY = "buttonGroup";

    /**
     * UndoableEdit presentation name key.
     *
     * @see javax.swing.undo.UndoableEdit#getPresentationName
     */
    public static final java.lang.String UNDO_PRESENTATION_NAME_KEY = "undoPresentationName";

    /**
     * Prevent instance creation.
     */
    private ActionUtil() {
    }

    /**
     * Configures a JCheckBoxMenuItem for an Action.
     */
    public static void configureJCheckBoxMenuItem(final javax.swing.JCheckBoxMenuItem mi, final javax.swing.Action a) {
        /* mi.setSelected((Boolean) a.getValue(ActionUtil.SELECTED_KEY));
        PropertyChangeListener propertyHandler = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ActionUtil.SELECTED_KEY)) {
        mi.setSelected((Boolean) a.getValue(ActionUtil.SELECTED_KEY));
        }
        }
        };
        a.addPropertyChangeListener(propertyHandler);
        mi.putClientProperty("actionPropertyHandler", propertyHandler);
         */
        mi.setAction(a);
    }

    /**
     * Unconfigures a JCheckBoxMenuItem for an Action.
     */
    public static void unconfigureJCheckBoxMenuItem(javax.swing.JCheckBoxMenuItem mi, javax.swing.Action a) {
        /* PropertyChangeListener propertyHandler = (PropertyChangeListener) mi.getClientProperty("actionPropertyHandler");
        if (propertyHandler != null) {
        a.removePropertyChangeListener(propertyHandler);
        mi.putClientProperty("actionPropertyHandler", null);
        }
         */
        mi.setAction(null);
    }
}