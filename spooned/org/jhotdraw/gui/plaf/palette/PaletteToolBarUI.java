/**
 *
 * @(#)PaletteToolBarUI.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * ToolBarUI for palette components.
 *
 * <p>This UI differs from BasicToolBarUI, in that the component holding the toolbar is supposed to
 * use BoxLayout instead of BorderLayout. This allows to have multiple toolbars in the same
 * component. The toolbars can be reorderd in the component, but they are not allowed to float in
 * their own floating window.
 *
 * <p>The JToolBar starts dragging only, if the drag starts over the insets of its border.
 */
public class PaletteToolBarUI extends javax.swing.plaf.ToolBarUI implements javax.swing.SwingConstants {
    private static final boolean IS_FLOATING_ALLOWED = false;

    protected javax.swing.JToolBar toolBar;

    private boolean floating;

    private int floatingX;

    private int floatingY;

    private javax.swing.JFrame floatingFrame;

    private javax.swing.RootPaneContainer floatingToolBar;

    protected org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.DragWindow dragWindow;

    private java.awt.Container dockingSource;

    private int dockingSensitivity = 0;

    protected int focusedCompIndex = -1;

    protected java.awt.Color dockingColor = null;

    protected java.awt.Color floatingColor = null;

    protected java.awt.Color dockingBorderColor = null;

    protected java.awt.Color floatingBorderColor = null;

    protected javax.swing.event.MouseInputListener dockingListener;

    protected java.beans.PropertyChangeListener propertyListener;

    protected java.awt.event.ContainerListener toolBarContListener;

    protected java.awt.event.FocusListener toolBarFocusListener;

    private org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Handler handler;

    protected java.lang.Integer constraintBeforeFloating = 0;

    // Rollover button implementation.
    private static java.lang.String IS_ROLLOVER = "JToolBar.isRollover";

    /* private */
    static java.lang.String IS_DIVIDER_DRAWN = "Palette.ToolBar.isDividerDrawn";

    // client properties
    /* The value of this client property must be an Icon or null. */
    public static final java.lang.String TOOLBAR_ICON_PROPERTY = "Palette.ToolBar.icon";

    /* The value of this client property must be an Integer or null, if it is null, the value 2 is used. */
    public static final java.lang.String TOOLBAR_TEXT_ICON_GAP_PROPERTY = "Palette.ToolBar.textIconGap";

    /* The value of this client property must be an Insets object or null, if it is null, the insets of the toolbar border are used */
    public static final java.lang.String TOOLBAR_INSETS_OVERRIDE_PROPERTY = "Palette.ToolBar.insetsOverride";

    private static javax.swing.border.Border rolloverBorder;

    private static javax.swing.border.Border nonRolloverBorder;

    private static javax.swing.border.Border nonRolloverToggleBorder;

    private boolean rolloverBorders = false;

    private java.util.HashMap<javax.swing.AbstractButton, javax.swing.border.Border> borderTable = new java.util.HashMap<>();

    private java.util.HashMap<javax.swing.AbstractButton, java.lang.Boolean> rolloverTable = new java.util.HashMap<>();

    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no longer used. Key bindings
     * are now defined by the LookAndFeel, please refer to the key bindings specification for further
     * details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @java.lang.Deprecated
    protected javax.swing.KeyStroke upKey;

    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no longer used. Key bindings
     * are now defined by the LookAndFeel, please refer to the key bindings specification for further
     * details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @java.lang.Deprecated
    protected javax.swing.KeyStroke downKey;

    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no longer used. Key bindings
     * are now defined by the LookAndFeel, please refer to the key bindings specification for further
     * details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @java.lang.Deprecated
    protected javax.swing.KeyStroke leftKey;

    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no longer used. Key bindings
     * are now defined by the LookAndFeel, please refer to the key bindings specification for further
     * details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @java.lang.Deprecated
    protected javax.swing.KeyStroke rightKey;

    private static java.lang.String FOCUSED_COMP_INDEX = "JToolBar.focusedCompIndex";

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI();
    }

    @java.lang.Override
    public void installUI(javax.swing.JComponent c) {
        toolBar = ((javax.swing.JToolBar) (c));
        // Set defaults
        installDefaults();
        installComponents();
        installListeners();
        installKeyboardActions();
        // Initialize instance vars
        dockingSensitivity = 0;
        floating = false;
        floatingX = floatingY = 0;
        floatingToolBar = null;
        setOrientation(toolBar.getOrientation());
        javax.swing.LookAndFeel.installProperty(c, "opaque", java.lang.Boolean.TRUE);
        if (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.FOCUSED_COMP_INDEX) != null) {
            focusedCompIndex = ((java.lang.Integer) (c.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.FOCUSED_COMP_INDEX)));
        }
    }

    @java.lang.Override
    public void uninstallUI(javax.swing.JComponent c) {
        // Clear defaults
        uninstallDefaults();
        uninstallComponents();
        uninstallListeners();
        uninstallKeyboardActions();
        // Clear instance vars
        if (isFloating() == true) {
            setFloating(false, null);
        }
        floatingToolBar = null;
        dragWindow = null;
        dockingSource = null;
        c.putClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.FOCUSED_COMP_INDEX, focusedCompIndex);
    }

    protected void installDefaults() {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installBorder(toolBar, "ToolBar.border");
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(toolBar, "ToolBar.background", "ToolBar.foreground", "ToolBar.font");
        // Toolbar specific defaults
        if ((dockingColor == null) || (dockingColor instanceof javax.swing.plaf.UIResource)) {
            dockingColor = javax.swing.UIManager.getColor("ToolBar.dockingBackground");
        }
        if ((floatingColor == null) || (floatingColor instanceof javax.swing.plaf.UIResource)) {
            floatingColor = javax.swing.UIManager.getColor("ToolBar.floatingBackground");
        }
        if ((dockingBorderColor == null) || (dockingBorderColor instanceof javax.swing.plaf.UIResource)) {
            dockingBorderColor = javax.swing.UIManager.getColor("ToolBar.dockingForeground");
        }
        if ((floatingBorderColor == null) || (floatingBorderColor instanceof javax.swing.plaf.UIResource)) {
            floatingBorderColor = javax.swing.UIManager.getColor("ToolBar.floatingForeground");
            // ToolBar rollover button borders
        }
        java.lang.Object rolloverProp = toolBar.getClientProperty(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.IS_ROLLOVER);
        if (rolloverProp == null) {
            rolloverProp = javax.swing.UIManager.get("ToolBar.isRollover");
        }
        if (rolloverProp != null) {
            rolloverBorders = ((java.lang.Boolean) (rolloverProp));
        }
        if (org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.rolloverBorder == null) {
            org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.rolloverBorder = createRolloverBorder();
        }
        if (org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverBorder == null) {
            org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverBorder = createNonRolloverBorder();
        }
        if (org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverToggleBorder == null) {
            org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverToggleBorder = createNonRolloverToggleBorder();
        }
        setRolloverBorders(isRolloverBorders());
    }

    protected void uninstallDefaults() {
        javax.swing.LookAndFeel.uninstallBorder(toolBar);
        dockingColor = null;
        floatingColor = null;
        dockingBorderColor = null;
        floatingBorderColor = null;
        installNormalBorders(toolBar);
        org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.rolloverBorder = null;
        org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverBorder = null;
        org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverToggleBorder = null;
    }

    protected void installComponents() {
    }

    protected void uninstallComponents() {
    }

    protected void installListeners() {
        dockingListener = createDockingListener();
        if (dockingListener != null) {
            toolBar.addMouseMotionListener(dockingListener);
            toolBar.addMouseListener(dockingListener);
        }
        propertyListener = createPropertyListener();// added in setFloating

        if (propertyListener != null) {
            toolBar.addPropertyChangeListener(propertyListener);
        }
        toolBarContListener = createToolBarContListener();
        if (toolBarContListener != null) {
            toolBar.addContainerListener(toolBarContListener);
        }
        toolBarFocusListener = createToolBarFocusListener();
        if (toolBarFocusListener != null) {
            // Put focus listener on all components in toolbar
            java.awt.Component[] components = toolBar.getComponents();
            for (int i = 0; i < components.length; ++i) {
                components[i].addFocusListener(toolBarFocusListener);
            }
        }
    }

    protected void uninstallListeners() {
        if (dockingListener != null) {
            toolBar.removeMouseMotionListener(dockingListener);
            toolBar.removeMouseListener(dockingListener);
            dockingListener = null;
        }
        if (propertyListener != null) {
            toolBar.removePropertyChangeListener(propertyListener);
            propertyListener = null;// removed in setFloating

        }
        if (toolBarContListener != null) {
            toolBar.removeContainerListener(toolBarContListener);
            toolBarContListener = null;
        }
        if (toolBarFocusListener != null) {
            // Remove focus listener from all components in toolbar
            java.awt.Component[] components = toolBar.getComponents();
            for (int i = 0; i < components.length; ++i) {
                components[i].removeFocusListener(toolBarFocusListener);
            }
            toolBarFocusListener = null;
        }
        handler = null;
    }

    protected void installKeyboardActions() {
        javax.swing.InputMap km = getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        javax.swing.SwingUtilities.replaceUIInputMap(toolBar, javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, km);
        org.jhotdraw.gui.plaf.palette.PaletteLazyActionMap.installLazyActionMap(toolBar, org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.class, "ToolBar.actionMap");
    }

    javax.swing.InputMap getInputMap(int condition) {
        if (condition == javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
            return ((javax.swing.InputMap) (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().get("ToolBar.ancestorInputMap")));
        }
        return null;
    }

    static void loadActionMap(org.jhotdraw.gui.plaf.palette.PaletteLazyActionMap map) {
        map.put(new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_RIGHT));
        map.put(new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_LEFT));
        map.put(new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_UP));
        map.put(new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_DOWN));
    }

    protected void uninstallKeyboardActions() {
        javax.swing.SwingUtilities.replaceUIActionMap(toolBar, null);
        javax.swing.SwingUtilities.replaceUIInputMap(toolBar, javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
    }

    protected void navigateFocusedComp(int direction) {
        int nComp = toolBar.getComponentCount();
        int j;
        switch (direction) {
            case javax.swing.SwingConstants.EAST :
            case javax.swing.SwingConstants.SOUTH :
                if ((focusedCompIndex < 0) || (focusedCompIndex >= nComp)) {
                    break;
                }
                j = focusedCompIndex + 1;
                while (j != focusedCompIndex) {
                    if (j >= nComp) {
                        j = 0;
                    }
                    java.awt.Component comp = toolBar.getComponentAtIndex(j++);
                    if (((comp != null) && comp.isFocusable()) && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                } 
                break;
            case javax.swing.SwingConstants.WEST :
            case javax.swing.SwingConstants.NORTH :
                if ((focusedCompIndex < 0) || (focusedCompIndex >= nComp)) {
                    break;
                }
                j = focusedCompIndex - 1;
                while (j != focusedCompIndex) {
                    if (j < 0) {
                        j = nComp - 1;
                    }
                    java.awt.Component comp = toolBar.getComponentAtIndex(j--);
                    if (((comp != null) && comp.isFocusable()) && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                } 
                break;
            default :
                break;
        }
    }

    /**
     * Creates a rollover border for toolbar components. The rollover border will be installed if
     * rollover borders are enabled.
     *
     * <p>Override this method to provide an alternate rollover border.
     *
     * @since 1.4
     */
    protected javax.swing.border.Border createRolloverBorder() {
        java.lang.Object border = javax.swing.UIManager.get("ToolBar.rolloverBorder");
        if (border != null) {
            return ((javax.swing.border.Border) (border));
        }
        return new javax.swing.border.EmptyBorder(0, 0, 0, 0);
    }

    /**
     * Creates the non rollover border for toolbar components. This border will be installed as the
     * border for components added to the toolbar if rollover borders are not enabled.
     *
     * <p>Override this method to provide an alternate rollover border.
     *
     * @since 1.4
     */
    protected javax.swing.border.Border createNonRolloverBorder() {
        java.lang.Object border = javax.swing.UIManager.get("ToolBar.nonrolloverBorder");
        if (border != null) {
            return ((javax.swing.border.Border) (border));
        }
        return new javax.swing.border.EmptyBorder(0, 0, 0, 0);
    }

    /**
     * Creates a non rollover border for Toggle buttons in the toolbar.
     */
    private javax.swing.border.Border createNonRolloverToggleBorder() {
        return new javax.swing.border.EmptyBorder(0, 0, 0, 0);
    }

    /**
     * No longer used, use PaletteToolBarUI.createFloatingWindow(JToolBar)
     *
     * @see #createFloatingWindow
     */
    protected javax.swing.JFrame createFloatingFrame(javax.swing.JToolBar toolbar) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(toolbar);
        javax.swing.JFrame frame = new javax.swing.JFrame(toolbar.getName(), window != null ? window.getGraphicsConfiguration() : null) {
            private static final long serialVersionUID = 1L;

            // Override createRootPane() to automatically resize
            // the frame when contents change
            @java.lang.Override
            protected javax.swing.JRootPane createRootPane() {
                javax.swing.JRootPane rootPane = new javax.swing.JRootPane() {
                    private static final long serialVersionUID = 1L;

                    private boolean packing = false;

                    @java.lang.Override
                    public void validate() {
                        super.validate();
                        if (!packing) {
                            packing = true;
                            pack();
                            packing = false;
                        }
                    }
                };
                rootPane.setOpaque(true);
                return rootPane;
            }
        };
        frame.getRootPane().setName("ToolBar.FloatingFrame");
        frame.setResizable(false);
        java.awt.event.WindowListener wl = createFrameListener();
        frame.addWindowListener(wl);
        return frame;
    }

    /**
     * Creates a window which contains the toolbar after it has been dragged out from its container
     *
     * @return a <code>RootPaneContainer</code> object, containing the toolbar.
     */
    protected javax.swing.RootPaneContainer createFloatingWindow(javax.swing.JToolBar toolbar) {
        class ToolBarDialog extends javax.swing.JDialog {
            private static final long serialVersionUID = 1L;

            public ToolBarDialog(java.awt.Frame owner, java.lang.String title, boolean modal) {
                super(owner, title, modal);
            }

            public ToolBarDialog(java.awt.Dialog owner, java.lang.String title, boolean modal) {
                super(owner, title, modal);
            }

            // Override createRootPane() to automatically resize
            // the frame when contents change
            @java.lang.Override
            protected javax.swing.JRootPane createRootPane() {
                javax.swing.JRootPane rootPane = new javax.swing.JRootPane() {
                    private static final long serialVersionUID = 1L;

                    private boolean packing = false;

                    @java.lang.Override
                    public void validate() {
                        super.validate();
                        if (!packing) {
                            packing = true;
                            pack();
                            packing = false;
                        }
                    }
                };
                rootPane.setOpaque(true);
                return rootPane;
            }
        }
        javax.swing.JDialog dialog;
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(toolbar);
        if (window instanceof java.awt.Frame) {
            dialog = new ToolBarDialog(((java.awt.Frame) (window)), toolbar.getName(), false);
        } else if (window instanceof java.awt.Dialog) {
            dialog = new ToolBarDialog(((java.awt.Dialog) (window)), toolbar.getName(), false);
        } else {
            dialog = new ToolBarDialog(((java.awt.Frame) (null)), toolbar.getName(), false);
        }
        dialog.getRootPane().setName("ToolBar.FloatingWindow");
        dialog.setTitle(toolbar.getName());
        dialog.setResizable(false);
        java.awt.event.WindowListener wl = createFrameListener();
        dialog.addWindowListener(wl);
        return dialog;
    }

    protected org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.DragWindow createDragWindow(javax.swing.JToolBar toolbar) {
        java.awt.Window frame = null;
        if (toolBar != null) {
            java.awt.Container p;
            for (p = toolBar.getParent(); (p != null) && (!(p instanceof java.awt.Window)); p = p.getParent()) {
            }
            frame = ((java.awt.Window) (p));
        }
        if (floatingToolBar == null) {
            floatingToolBar = createFloatingWindow(toolBar);
        }
        if (floatingToolBar instanceof java.awt.Window) {
            frame = ((java.awt.Window) (floatingToolBar));
        }
        org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.DragWindow w = new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.DragWindow(frame);
        javax.swing.JRootPane rp = ((javax.swing.RootPaneContainer) (w)).getRootPane();
        rp.putClientProperty("Window.alpha", 0.6F);
        return w;
    }

    /**
     * Returns a flag to determine whether rollover button borders are enabled.
     *
     * @return true if rollover borders are enabled; false otherwise
     * @see #setRolloverBorders
     * @since 1.4
     */
    public boolean isRolloverBorders() {
        return rolloverBorders;
    }

    /**
     * Sets the flag for enabling rollover borders on the toolbar and it will also install the
     * apropriate border depending on the state of the flag.
     *
     * @param rollover
     * 		if true, rollover borders are installed. Otherwise non-rollover borders are
     * 		installed
     * @see #isRolloverBorders
     * @since 1.4
     */
    public void setRolloverBorders(boolean rollover) {
        rolloverBorders = rollover;
        if (rolloverBorders) {
            installRolloverBorders(toolBar);
        } else {
            installNonRolloverBorders(toolBar);
        }
    }

    /**
     * Installs rollover borders on all the child components of the JComponent.
     *
     * <p>This is a convenience method to call <code>setBorderToRollover</code> for each child
     * component.
     *
     * @param c
     * 		container which holds the child components (usally a JToolBar)
     * @see #setBorderToRollover
     * @since 1.4
     */
    protected void installRolloverBorders(javax.swing.JComponent c) {
        // Put rollover borders on buttons
        java.awt.Component[] components = c.getComponents();
        for (int i = 0; i < components.length; ++i) {
            if (components[i] instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) (components[i])).updateUI();
                setBorderToRollover(components[i]);
            }
        }
    }

    /**
     * Installs non-rollover borders on all the child components of the JComponent. A non-rollover
     * border is the border that is installed on the child component while it is in the toolbar.
     *
     * <p>This is a convenience method to call <code>setBorderToNonRollover</code> for each child
     * component.
     *
     * @param c
     * 		container which holds the child components (usally a JToolBar)
     * @see #setBorderToNonRollover
     * @since 1.4
     */
    protected void installNonRolloverBorders(javax.swing.JComponent c) {
        // Put non-rollover borders on buttons. These borders reduce the margin.
        java.awt.Component[] components = c.getComponents();
        for (int i = 0; i < components.length; ++i) {
            if (components[i] instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) (components[i])).updateUI();
                setBorderToNonRollover(components[i]);
            }
        }
    }

    /**
     * Installs normal borders on all the child components of the JComponent. A normal border is the
     * original border that was installed on the child component before it was added to the toolbar.
     *
     * <p>This is a convenience method to call <code>setBorderNormal</code> for each child component.
     *
     * @param c
     * 		container which holds the child components (usally a JToolBar)
     * @see #setBorderToNonRollover
     * @since 1.4
     */
    protected void installNormalBorders(javax.swing.JComponent c) {
        // Put back the normal borders on buttons
        java.awt.Component[] components = c.getComponents();
        for (int i = 0; i < components.length; ++i) {
            setBorderToNormal(components[i]);
        }
    }

    /**
     * Sets the border of the component to have a rollover border which was created by <code>
     * createRolloverBorder</code>.
     *
     * @param c
     * 		component which will have a rollover border installed
     * @see #createRolloverBorder
     * @since 1.4
     */
    protected void setBorderToRollover(java.awt.Component c) {
        if (true) {
            return;
        }
        if (c instanceof javax.swing.AbstractButton) {
            javax.swing.AbstractButton b = ((javax.swing.AbstractButton) (c));
            javax.swing.border.Border border = borderTable.get(b);
            if ((border == null) || (border instanceof javax.swing.plaf.UIResource)) {
                borderTable.put(b, b.getBorder());
            }
            // Only set the border if its the default border
            if (b.getBorder() instanceof javax.swing.plaf.UIResource) {
                b.setBorder(getRolloverBorder(b));
            }
            rolloverTable.put(b, b.isRolloverEnabled());
            b.setRolloverEnabled(true);
        }
    }

    private javax.swing.border.Border getRolloverBorder(javax.swing.AbstractButton b) {
        java.lang.Object borderProvider = javax.swing.UIManager.get("ToolBar.rolloverBorderProvider");
        if (borderProvider == null) {
            return org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.rolloverBorder;
        }
        // return ((BorderProvider) borderProvider).getRolloverBorder(b);
        return null;
    }

    /**
     * Sets the border of the component to have a non-rollover border which was created by <code>
     * createNonRolloverBorder</code>.
     *
     * @param c
     * 		component which will have a non-rollover border installed
     * @see #createNonRolloverBorder
     * @since 1.4
     */
    protected void setBorderToNonRollover(java.awt.Component c) {
        if (true) {
            return;
        }
        if (c instanceof javax.swing.AbstractButton) {
            javax.swing.AbstractButton b = ((javax.swing.AbstractButton) (c));
            javax.swing.border.Border border = borderTable.get(b);
            if ((border == null) || (border instanceof javax.swing.plaf.UIResource)) {
                borderTable.put(b, b.getBorder());
            }
            // Only set the border if its the default border
            if (b.getBorder() instanceof javax.swing.plaf.UIResource) {
                if (b instanceof javax.swing.JToggleButton) {
                    ((javax.swing.JToggleButton) (b)).setBorder(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverToggleBorder);
                } else {
                    b.setBorder(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.nonRolloverBorder);
                }
            }
            rolloverTable.put(b, b.isRolloverEnabled() ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
            b.setRolloverEnabled(false);
        }
    }

    /**
     * Sets the border of the component to have a normal border. A normal border is the original
     * border that was installed on the child component before it was added to the toolbar.
     *
     * @param c
     * 		component which will have a normal border re-installed
     * @see #createNonRolloverBorder
     * @since 1.4
     */
    protected void setBorderToNormal(java.awt.Component c) {
        if (true) {
            return;
        }
        if (c instanceof javax.swing.AbstractButton) {
            javax.swing.AbstractButton b = ((javax.swing.AbstractButton) (c));
            javax.swing.border.Border border = borderTable.remove(b);
            b.setBorder(border);
            java.lang.Boolean value = rolloverTable.remove(b);
            if (value != null) {
                b.setRolloverEnabled(value);
            }
        }
    }

    public void setFloatingLocation(int x, int y) {
        floatingX = x;
        floatingY = y;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean b, java.awt.Point p) {
        if (toolBar.isFloatable() == true) {
            if (dragWindow != null) {
                dragWindow.setVisible(false);
            }
            this.floating = b;
            if (b && org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.IS_FLOATING_ALLOWED) {
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                    dockingSource.remove(toolBar);
                }
                constraintBeforeFloating = calculateConstraint();
                if (propertyListener != null) {
                    javax.swing.UIManager.addPropertyChangeListener(propertyListener);
                }
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                floatingToolBar.getContentPane().add(toolBar, java.awt.BorderLayout.CENTER);
                if (floatingToolBar instanceof java.awt.Window) {
                    ((java.awt.Window) (floatingToolBar)).pack();
                }
                if (floatingToolBar instanceof java.awt.Window) {
                    ((java.awt.Window) (floatingToolBar)).setLocation(floatingX, floatingY);
                }
                if (floatingToolBar instanceof java.awt.Window) {
                    ((java.awt.Window) (floatingToolBar)).setVisible(true);
                }
            } else {
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                if (floatingToolBar instanceof java.awt.Window) {
                    ((java.awt.Window) (floatingToolBar)).setVisible(false);
                }
                floatingToolBar.getContentPane().remove(toolBar);
                java.lang.Integer constraint = getDockingConstraint(dockingSource, p);
                if (constraint == null) {
                    constraint = 0;
                }
                int orientation = mapConstraintToOrientation(constraint);
                setOrientation(orientation);
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                if (propertyListener != null) {
                    javax.swing.UIManager.removePropertyChangeListener(propertyListener);
                }
                dockingSource.add(toolBar, constraint.intValue());
            }
            dockingSource.invalidate();
            java.awt.Container dockingSourceParent = dockingSource.getParent();
            if (dockingSourceParent != null) {
                dockingSourceParent.validate();
            }
            dockingSource.repaint();
        }
    }

    private int mapConstraintToOrientation(java.lang.Object constraint) {
        int orientation = toolBar.getOrientation();
        if (constraint != null) {
            if (constraint.equals(java.awt.BorderLayout.EAST) || constraint.equals(java.awt.BorderLayout.WEST)) {
                orientation = javax.swing.JToolBar.VERTICAL;
            } else if (constraint.equals(java.awt.BorderLayout.NORTH) || constraint.equals(java.awt.BorderLayout.SOUTH)) {
                orientation = javax.swing.JToolBar.HORIZONTAL;
            }
        }
        return orientation;
    }

    public void setOrientation(int orientation) {
        toolBar.setOrientation(orientation);
        if (dragWindow != null) {
            dragWindow.setOrientation(orientation);
        }
    }

    /**
     * Gets the color displayed when over a docking area
     */
    public java.awt.Color getDockingColor() {
        return dockingColor;
    }

    /**
     * Sets the color displayed when over a docking area
     */
    public void setDockingColor(java.awt.Color c) {
        this.dockingColor = c;
    }

    /**
     * Gets the color displayed when over a floating area
     */
    public java.awt.Color getFloatingColor() {
        return floatingColor;
    }

    /**
     * Sets the color displayed when over a floating area
     */
    public void setFloatingColor(java.awt.Color c) {
        this.floatingColor = c;
    }

    private boolean isBlocked(java.awt.Component comp, java.lang.Object constraint) {
        if (comp instanceof java.awt.Container) {
            java.awt.Container cont = ((java.awt.Container) (comp));
            java.awt.LayoutManager lm = cont.getLayout();
            if (lm instanceof java.awt.BorderLayout) {
                java.awt.BorderLayout blm = ((java.awt.BorderLayout) (lm));
                java.awt.Component c = blm.getLayoutComponent(cont, constraint);
                return (c != null) && (c != toolBar);
            }
        }
        return false;
    }

    public boolean canDock(java.awt.Component c, java.awt.Point p) {
        return (p != null) && (getDockingConstraint(c, p) != null);
    }

    private java.lang.Integer calculateConstraint() {
        java.lang.Integer constraint = null;
        java.awt.LayoutManager lm = dockingSource.getLayout();
        if (lm instanceof javax.swing.BoxLayout) {
            for (int i = 0, n = dockingSource.getComponentCount(); i < n; i++) {
                if (dockingSource.getComponent(i) == toolBar) {
                    constraint = i;
                    break;
                }
            }
        }
        return constraint != null ? constraint : constraintBeforeFloating;
    }

    private java.lang.Integer getDockingConstraint(java.awt.Component c, java.awt.Point p) {
        if (p == null) {
            return constraintBeforeFloating;
        }
        if (c.contains(p)) {
            for (int i = 0, n = dockingSource.getComponentCount(); i < n; i++) {
                java.awt.Component child = dockingSource.getComponent(i);
                java.awt.Point childP = new java.awt.Point(p.x - child.getX(), p.y - child.getY());
                if (child.contains(childP)) {
                    return java.lang.Math.min(n - 1, childP.x <= child.getWidth() ? i : i + 1);
                }
            }
            if ((dockingSource.getComponentCount() == 0) || (p.x < dockingSource.getComponent(0).getX())) {
                return 0;
            }
            return dockingSource.getComponentCount() - 1;
        }
        return null;
    }

    protected void dragTo(java.awt.Point position, java.awt.Point origin) {
        if (toolBar.isFloatable() == true) {
            try {
                if (dragWindow == null) {
                    dragWindow = createDragWindow(toolBar);
                }
                java.awt.Point offset = dragWindow.getOffset();
                if (offset == null) {
                    // Dimension size = toolBar.getPreferredSize();
                    java.awt.Dimension size = toolBar.getSize();
                    offset = new java.awt.Point(size.width / 2, size.height / 2);
                    dragWindow.setOffset(offset);
                }
                java.awt.Point global = new java.awt.Point(origin.x + position.x, origin.y + position.y);
                java.awt.Point dragPoint = new java.awt.Point(global.x - offset.x, global.y - offset.y);
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                constraintBeforeFloating = calculateConstraint();
                java.awt.Point dockingPosition = dockingSource.getLocationOnScreen();
                java.awt.Point comparisonPoint = new java.awt.Point(global.x - dockingPosition.x, global.y - dockingPosition.y);
                if (canDock(dockingSource, comparisonPoint)) {
                    dragWindow.setBackground(getDockingColor());
                    java.lang.Object constraint = getDockingConstraint(dockingSource, comparisonPoint);
                    int orientation = mapConstraintToOrientation(constraint);
                    dragWindow.setOrientation(orientation);
                    dragWindow.setBorderColor(dockingBorderColor);
                } else {
                    dragWindow.setBackground(getFloatingColor());
                    dragWindow.setBorderColor(floatingBorderColor);
                }
                dragWindow.setLocation(dragPoint.x, dragPoint.y);
                if (dragWindow.isVisible() == false) {
                    // Dimension size = toolBar.getPreferredSize();
                    java.awt.Dimension size = toolBar.getSize();
                    dragWindow.setSize(size.width, size.height);
                    dragWindow.setVisible(true);
                }
            } catch (java.awt.IllegalComponentStateException e) {
                // allowed empty
            }
        }
    }

    protected void floatAt(java.awt.Point position, java.awt.Point origin) {
        if (toolBar.isFloatable() == true) {
            try {
                java.awt.Point offset = dragWindow.getOffset();
                if (offset == null) {
                    offset = position;
                    dragWindow.setOffset(offset);
                }
                java.awt.Point global = new java.awt.Point(origin.x + position.x, origin.y + position.y);
                setFloatingLocation(global.x - offset.x, global.y - offset.y);
                if (dockingSource != null) {
                    java.awt.Point dockingPosition = dockingSource.getLocationOnScreen();
                    java.awt.Point comparisonPoint = new java.awt.Point(global.x - dockingPosition.x, global.y - dockingPosition.y);
                    if (canDock(dockingSource, comparisonPoint)) {
                        setFloating(false, comparisonPoint);
                    } else {
                        setFloating(true, null);
                    }
                } else {
                    setFloating(true, null);
                }
                dragWindow.setOffset(null);
            } catch (java.awt.IllegalComponentStateException e) {
                // allowed empty
            }
        }
    }

    private org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Handler getHandler() {
        if (handler == null) {
            handler = new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Handler();
        }
        return handler;
    }

    protected java.awt.event.ContainerListener createToolBarContListener() {
        return getHandler();
    }

    protected java.awt.event.FocusListener createToolBarFocusListener() {
        return getHandler();
    }

    protected java.beans.PropertyChangeListener createPropertyListener() {
        return getHandler();
    }

    protected javax.swing.event.MouseInputListener createDockingListener() {
        getHandler().tb = toolBar;
        return getHandler();
    }

    protected java.awt.event.WindowListener createFrameListener() {
        return new org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.FrameListener();
    }

    /**
     * Paints the contents of the window used for dragging.
     *
     * @param g
     * 		Graphics to paint to.
     * @throws NullPointerException
     * 		is <code>g</code> is null
     * @since 1.5
     */
    protected void paintDragWindow(java.awt.Graphics g) {
        int w = dragWindow.getWidth();
        int h = dragWindow.getHeight();
        g.setColor(dragWindow.getBackground());
        g.fillRect(0, 0, w, h);
        boolean wasDoubleBuffered = false;
        if (toolBar.isDoubleBuffered()) {
            wasDoubleBuffered = true;
            toolBar.setDoubleBuffered(false);
        }
        java.awt.Graphics g2 = g.create();
        toolBar.paintAll(g2);
        g2.dispose();
        g.setColor(dragWindow.getBorderColor());
        g.drawRect(0, 0, w - 1, h - 1);
        if (wasDoubleBuffered) {
            toolBar.setDoubleBuffered(true);
        }
    }

    /* UI */
    private static class Actions extends javax.swing.AbstractAction {
        private static final long serialVersionUID = 1L;

        private static final java.lang.String NAVIGATE_RIGHT = "navigateRight";

        private static final java.lang.String NAVIGATE_LEFT = "navigateLeft";

        private static final java.lang.String NAVIGATE_UP = "navigateUp";

        private static final java.lang.String NAVIGATE_DOWN = "navigateDown";

        public Actions(java.lang.String name) {
            super(name);
        }

        public java.lang.String getName() {
            return ((java.lang.String) (getValue(javax.swing.Action.NAME)));
        }

        @java.lang.Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            java.lang.String key = getName();
            javax.swing.JToolBar toolBar = ((javax.swing.JToolBar) (evt.getSource()));
            org.jhotdraw.gui.plaf.palette.PaletteToolBarUI ui = ((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI) (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getUIOfType(toolBar.getUI(), org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.class)));
            if (((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_RIGHT == null) && (key == null)) || ((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_RIGHT != null) && org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_RIGHT.equals(key))) {
                ui.navigateFocusedComp(javax.swing.SwingConstants.EAST);
            } else if (((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_LEFT == null) && (key == null)) || ((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_LEFT != null) && org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_LEFT.equals(key))) {
                ui.navigateFocusedComp(javax.swing.SwingConstants.WEST);
            } else if (((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_UP == null) && (key == null)) || ((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_UP != null) && org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_UP.equals(key))) {
                ui.navigateFocusedComp(javax.swing.SwingConstants.NORTH);
            } else if (((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_DOWN == null) && (key == null)) || ((org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_DOWN != null) && org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.Actions.NAVIGATE_DOWN.equals(key))) {
                ui.navigateFocusedComp(javax.swing.SwingConstants.SOUTH);
            }
        }
    }

    private class Handler implements java.awt.event.ContainerListener , java.awt.event.FocusListener , javax.swing.event.MouseInputListener , java.beans.PropertyChangeListener {
        // ContainerListener
        @java.lang.Override
        public void componentAdded(java.awt.event.ContainerEvent evt) {
            java.awt.Component c = evt.getChild();
            if (toolBarFocusListener != null) {
                c.addFocusListener(toolBarFocusListener);
            }
            if (isRolloverBorders()) {
                setBorderToRollover(c);
            } else {
                setBorderToNonRollover(c);
            }
        }

        @java.lang.Override
        public void componentRemoved(java.awt.event.ContainerEvent evt) {
            java.awt.Component c = evt.getChild();
            if (toolBarFocusListener != null) {
                c.removeFocusListener(toolBarFocusListener);
            }
            // Revert the button border
            setBorderToNormal(c);
        }

        // FocusListener
        @java.lang.Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            java.awt.Component c = evt.getComponent();
            focusedCompIndex = toolBar.getComponentIndex(c);
        }

        @java.lang.Override
        public void focusLost(java.awt.event.FocusEvent evt) {
        }

        // MouseInputListener (DockingListener)
        javax.swing.JToolBar tb;

        boolean isDragging = false;

        java.awt.Point origin = null;

        boolean isArmed = false;

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent evt) {
            if (!tb.isEnabled()) {
                return;
            }
            isDragging = false;
            if (evt.getSource() instanceof javax.swing.JToolBar) {
                javax.swing.JComponent c = ((javax.swing.JComponent) (evt.getSource()));
                java.awt.Insets insets;
                if (c.getBorder() instanceof org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder) {
                    insets = ((org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder) (c.getBorder())).getDragInsets(c);
                } else {
                    insets = c.getInsets();
                }
                isArmed = !((((evt.getX() > insets.left) && (evt.getX() < (c.getWidth() - insets.right))) && (evt.getY() > insets.top)) && (evt.getY() < (c.getHeight() - insets.bottom)));
            }
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            if (!tb.isEnabled()) {
                return;
            }
            if (isDragging == true) {
                java.awt.Point position = evt.getPoint();
                if (origin == null) {
                    origin = evt.getComponent().getLocationOnScreen();
                }
                floatAt(position, origin);
            }
            origin = null;
            isDragging = false;
        }

        @java.lang.Override
        public void mouseDragged(java.awt.event.MouseEvent evt) {
            if (!tb.isEnabled()) {
                return;
            }
            if (!isArmed) {
                return;
            }
            isDragging = true;
            java.awt.Point position = evt.getPoint();
            if (origin == null) {
                origin = evt.getComponent().getLocationOnScreen();
            }
            dragTo(position, origin);
        }

        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
        }

        @java.lang.Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        @java.lang.Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        @java.lang.Override
        public void mouseMoved(java.awt.event.MouseEvent evt) {
        }

        // PropertyChangeListener
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String propertyName = evt.getPropertyName();
            if ("lookAndFeel".equals(propertyName)) {
                toolBar.updateUI();
            } else if ("orientation".equals(propertyName)) {
                // Search for JSeparator components and change it's orientation
                // to match the toolbar and flip it's orientation.
                java.awt.Component[] components = toolBar.getComponents();
                int orientation = ((java.lang.Integer) (evt.getNewValue()));
                javax.swing.JToolBar.Separator separator;
                for (int i = 0; i < components.length; ++i) {
                    if (components[i] instanceof javax.swing.JToolBar.Separator) {
                        separator = ((javax.swing.JToolBar.Separator) (components[i]));
                        if (orientation == javax.swing.JToolBar.HORIZONTAL) {
                            separator.setOrientation(javax.swing.JSeparator.VERTICAL);
                        } else {
                            separator.setOrientation(javax.swing.JSeparator.HORIZONTAL);
                        }
                        java.awt.Dimension size = separator.getSeparatorSize();
                        if ((size != null) && (size.width != size.height)) {
                            // Flip the orientation.
                            java.awt.Dimension newSize = new java.awt.Dimension(size.height, size.width);
                            separator.setSeparatorSize(newSize);
                        }
                    }
                }
            } else if (((propertyName == null) && (org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.IS_ROLLOVER == null)) || ((propertyName != null) && propertyName.equals(org.jhotdraw.gui.plaf.palette.PaletteToolBarUI.IS_ROLLOVER))) {
                installNormalBorders(toolBar);
                setRolloverBorders(((java.lang.Boolean) (evt.getNewValue())));
            }
        }
    }

    protected class FrameListener extends java.awt.event.WindowAdapter {
        @java.lang.Override
        public void windowClosing(java.awt.event.WindowEvent w) {
            if (toolBar.isFloatable() == true) {
                if (dragWindow != null) {
                    dragWindow.setVisible(false);
                }
                floating = false;
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                if (floatingToolBar instanceof java.awt.Window) {
                    ((java.awt.Window) (floatingToolBar)).setVisible(false);
                }
                floatingToolBar.getContentPane().remove(toolBar);
                java.lang.Integer constraint = constraintBeforeFloating;
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                if (propertyListener != null) {
                    javax.swing.UIManager.removePropertyChangeListener(propertyListener);
                }
                dockingSource.add(toolBar, constraint.intValue());
                dockingSource.invalidate();
                java.awt.Container dockingSourceParent = dockingSource.getParent();
                if (dockingSourceParent != null) {
                    dockingSourceParent.validate();
                }
                dockingSource.repaint();
            }
        }
    }

    protected class ToolBarContListener implements java.awt.event.ContainerListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @java.lang.Override
        public void componentAdded(java.awt.event.ContainerEvent e) {
            getHandler().componentAdded(e);
        }

        @java.lang.Override
        public void componentRemoved(java.awt.event.ContainerEvent e) {
            getHandler().componentRemoved(e);
        }
    }

    protected class ToolBarFocusListener implements java.awt.event.FocusListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @java.lang.Override
        public void focusGained(java.awt.event.FocusEvent e) {
            getHandler().focusGained(e);
        }

        @java.lang.Override
        public void focusLost(java.awt.event.FocusEvent e) {
            getHandler().focusLost(e);
        }
    }

    protected class PropertyListener implements java.beans.PropertyChangeListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent e) {
            getHandler().propertyChange(e);
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class. Instantiate it only within
     * subclasses of PaletteToolBarUI.
     */
    public class DockingListener implements javax.swing.event.MouseInputListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        protected javax.swing.JToolBar toolBar;

        protected boolean isDragging = false;

        protected java.awt.Point origin = null;

        public DockingListener(javax.swing.JToolBar t) {
            this.toolBar = t;
            getHandler().tb = t;
        }

        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            getHandler().mouseClicked(e);
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            getHandler().tb = toolBar;
            getHandler().mousePressed(e);
            isDragging = getHandler().isDragging;
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            getHandler().tb = toolBar;
            getHandler().isDragging = isDragging;
            getHandler().origin = origin;
            getHandler().mouseReleased(e);
            isDragging = getHandler().isDragging;
            origin = getHandler().origin;
        }

        @java.lang.Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            getHandler().mouseEntered(e);
        }

        @java.lang.Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            getHandler().mouseExited(e);
        }

        @java.lang.Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            getHandler().tb = toolBar;
            getHandler().origin = origin;
            getHandler().mouseDragged(e);
            isDragging = getHandler().isDragging;
            origin = getHandler().origin;
        }

        @java.lang.Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
            getHandler().mouseMoved(e);
        }
    }

    protected class DragWindow extends javax.swing.JWindow {
        private static final long serialVersionUID = 1L;

        java.awt.Color borderColor = java.awt.Color.gray;

        int orientation = toolBar.getOrientation();

        java.awt.Point offset;// offset of the mouse cursor inside the DragWindow


        DragWindow(java.awt.Window w) {
            super(w);
            getContentPane().add(new javax.swing.JPanel() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void paintComponent(java.awt.Graphics g) {
                    paintDragWindow(g);
                }
            });
        }

        public void setOrientation(int o) {
            if (isShowing()) {
                if (o == this.orientation) {
                    return;
                }
                this.orientation = o;
                java.awt.Dimension size = getSize();
                setSize(new java.awt.Dimension(size.height, size.width));
                if (offset != null) {
                    if (toolBar.getComponentOrientation().isLeftToRight()) {
                        setOffset(new java.awt.Point(offset.y, offset.x));
                    } else if (o == javax.swing.JToolBar.HORIZONTAL) {
                        setOffset(new java.awt.Point(size.height - offset.y, offset.x));
                    } else {
                        setOffset(new java.awt.Point(offset.y, size.width - offset.x));
                    }
                }
                repaint();
            }
        }

        public java.awt.Point getOffset() {
            return offset;
        }

        public void setOffset(java.awt.Point p) {
            this.offset = p;
        }

        public void setBorderColor(java.awt.Color c) {
            if (this.borderColor == c) {
                return;
            }
            this.borderColor = c;
            repaint();
        }

        public java.awt.Color getBorderColor() {
            return this.borderColor;
        }

        @java.lang.Override
        public java.awt.Insets getInsets() {
            return new java.awt.Insets(1, 1, 1, 1);
        }
    }
}