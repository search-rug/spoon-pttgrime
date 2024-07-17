/**
 *
 * @(#)PaletteLookAndFeel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * A LookAndFeel for components in the palette windows of a drawing editor.
 */
public class PaletteLookAndFeel extends javax.swing.plaf.basic.BasicLookAndFeel {
    private static final long serialVersionUID = 1L;

    /**
     * Shared instance.
     */
    private static org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel instance;

    /**
     * Cached defaults.
     */
    private javax.swing.UIDefaults cachedDefaults;

    public static org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel getInstance() {
        if (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.instance == null) {
            org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.instance = new org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel();
        }
        return org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.instance;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "Palette Look and Feel";
    }

    @java.lang.Override
    public java.lang.String getID() {
        return "Palette";
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return "A look and feel for palette components";
    }

    @java.lang.Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @java.lang.Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    public javax.swing.UIDefaults getCachedDefaults() {
        if (cachedDefaults == null) {
            cachedDefaults = getDefaults();
        }
        return cachedDefaults;
    }

    /**
     * Looks up up the given key in our map and resolves LazyValues or ActiveValues.
     */
    public java.lang.Object get(java.lang.Object key) {
        javax.swing.UIDefaults defaults = getCachedDefaults();
        /* Quickly handle the common case, without grabbing
        a lock.
         */
        java.lang.Object value = defaults.get(key);
        if ((!(value instanceof javax.swing.UIDefaults.ActiveValue)) && (!(value instanceof javax.swing.UIDefaults.LazyValue))) {
            return value;
        }
        return value;
    }

    public boolean getBoolean(java.lang.String key) {
        return ((java.lang.Boolean) (get(key)));
    }

    public javax.swing.border.Border getBorder(java.lang.String key) {
        return ((javax.swing.border.Border) (get(key)));
    }

    public java.awt.Color getColor(java.lang.String key) {
        return ((java.awt.Color) (get(key)));
    }

    public java.awt.Font getFont(java.lang.String key) {
        return ((java.awt.Font) (get(key)));
    }

    public javax.swing.Icon getIcon(java.lang.String key) {
        return ((javax.swing.Icon) (get(key)));
    }

    public int getInt(java.lang.String key) {
        return ((java.lang.Integer) (get(key)));
    }

    public java.awt.Insets getInsets(java.lang.String key) {
        return ((java.awt.Insets) (get(key)));
    }

    public java.lang.String getString(java.lang.String key) {
        return ((java.lang.String) (get(key)));
    }

    /**
     * Convenience method for initializing a components foreground background and font properties with
     * values from the current defaults table. The properties are only set if the current value is
     * either null or a UIResource.
     *
     * @param c
     * 		the target component for installing default color/font properties
     * @param defaultBgName
     * 		the key for the default background
     * @param defaultFgName
     * 		the key for the default foreground
     * @param defaultFontName
     * 		the key for the default font
     * @see #installColors
     * @see UIManager#getColor
     * @see UIManager#getFont
     */
    public static void installColorsAndFont(javax.swing.JComponent c, java.lang.String defaultBgName, java.lang.String defaultFgName, java.lang.String defaultFontName) {
        java.awt.Font f = c.getFont();
        if ((f == null) || (f instanceof javax.swing.plaf.UIResource)) {
            c.setFont(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getFont(defaultFontName));
        }
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColors(c, defaultBgName, defaultFgName);
    }

    /**
     * Convenience method for installing a component's default Border object on the specified
     * component if either the border is currently null or already an instance of UIResource.
     *
     * @param c
     * 		the target component for installing default border
     * @param defaultBorderName
     * 		the key specifying the default border
     */
    public static void installBorder(javax.swing.JComponent c, java.lang.String defaultBorderName) {
        javax.swing.border.Border b = c.getBorder();
        if ((b == null) || (b instanceof javax.swing.plaf.UIResource)) {
            c.setBorder(org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().getBorder(defaultBorderName));
        }
    }

    /**
     * Convenience method for initializing a component's foreground and background color properties
     * with values from the current defaults table. The properties are only set if the current value
     * is either null or a UIResource.
     *
     * @param c
     * 		the target component for installing default color/font properties
     * @param defaultBgName
     * 		the key for the default background
     * @param defaultFgName
     * 		the key for the default foreground
     * @see #installColorsAndFont
     * @see UIManager#getColor
     */
    public static void installColors(javax.swing.JComponent c, java.lang.String defaultBgName, java.lang.String defaultFgName) {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel plaf = org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance();
        java.awt.Color bg = c.getBackground();
        if ((bg == null) || (bg instanceof javax.swing.plaf.UIResource)) {
            c.setBackground(plaf.getColor(defaultBgName));
        }
        java.awt.Color fg = c.getForeground();
        if ((fg == null) || (fg instanceof javax.swing.plaf.UIResource)) {
            c.setForeground(plaf.getColor(defaultFgName));
        }
    }

    @java.lang.Override
    protected void initComponentDefaults(javax.swing.UIDefaults table) {
        super.initComponentDefaults(table);
        // Add resource bundle to the table.
        // Since this does not seem to work in sandboxed environments, we check
        // whether we succeeded and - in case of failure - put the values in
        // by ourselves.
        table.addResourceBundle("org.jhotdraw.gui.Labels");
        if (table.getString("ColorChooser.rgbSliders") == null) {
            java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("org.jhotdraw.gui.Labels");
            for (java.lang.String key : rb.keySet()) {
                table.put(key, rb.getObject(key));
            }
        }
        // *** Shared Fonts
        java.lang.Integer fontPlain = java.awt.Font.PLAIN;
        java.lang.Integer fontBold = java.awt.Font.BOLD;
        java.lang.Object dialogPlain11 = new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource", null, new java.lang.Object[]{ "Dialog Sans", fontPlain, 11 });
        java.lang.Object dialogPlain12 = new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource", null, new java.lang.Object[]{ "Dialog Sans", fontPlain, 12 });
        java.lang.Object fieldPlain12 = new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource", null, new java.lang.Object[]{ "Verdana", fontPlain, 12 });
        java.lang.Object dialogBold12 = new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource", null, new java.lang.Object[]{ "Dialog", fontBold, 12 });
        // *** Shared Colors
        javax.swing.plaf.ColorUIResource black = new javax.swing.plaf.ColorUIResource(java.awt.Color.black);
        javax.swing.plaf.ColorUIResource control = new javax.swing.plaf.ColorUIResource(0xf0f0f0);
        javax.swing.plaf.ColorUIResource controlText = black;
        javax.swing.plaf.ColorUIResource selectionBackground = new javax.swing.plaf.ColorUIResource(0xb5d5ff);
        javax.swing.plaf.ColorUIResource selectionForeground = black;
        javax.swing.plaf.ColorUIResource listSelectionBackground = new javax.swing.plaf.ColorUIResource(0x3875d7);
        java.lang.Object focusCellHighlightBorder = // null,
        new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new java.lang.Object[]{ listSelectionBackground });
        // *** Shared Insets
        javax.swing.plaf.InsetsUIResource zeroInsets = new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0);
        // *** Shared Borders
        /* Object buttonBorder =
        new ProxyLazyValue(
        "org.jhotdraw.gui.plaf.palette.BackdropBorder$UIResource",
        new Object[] {new PaletteButtonBorder()});
         */
        java.lang.Object buttonBorder = new org.jhotdraw.gui.plaf.palette.BackdropBorder.UIResource(new org.jhotdraw.gui.plaf.palette.PaletteButtonBorder());
        java.lang.Object textBorder = new org.jhotdraw.gui.plaf.palette.BackdropBorder.UIResource(new org.jhotdraw.gui.plaf.palette.PaletteTextComponentBorder());
        java.lang.Object[] defaults = // "ToolBar.separatorSize", toolBarSeparatorSize,
        new java.lang.Object[]{ // *** Fonts
        "SmallSystemFont", dialogPlain11, // *** Buttons
        "Button.font", dialogPlain12, "Button.background", control, "Button.foreground", controlText, "Button.border", buttonBorder, "Button.margin", zeroInsets, // *** FontChooser
        "Button.background", control, "Button.foreground", controlText, "Button.border", buttonBorder, "Button.margin", zeroInsets, // *** ColorChooser
        // class names of default choosers
        "ColorChooser.font", dialogPlain11, "ColorChooser.defaultChoosers", new java.lang.String[]{ "org.jhotdraw.gui.plaf.palette.colorchooser.PaletteSwatchesChooser", "org.jhotdraw.gui.plaf.palette.colorchooser.PaletteColorWheelChooser", "org.jhotdraw.gui.plaf.palette.colorchooser.PaletteColorSlidersChooser" }, "ColorChooser.textSliderGap", 3, // *** FormattedTextField
        "FormattedTextField.font", fieldPlain12, "FormattedTextField.background", control, "FormattedTextField.foreground", controlText, "FormattedTextField.border", textBorder, "FormattedTextField.margin", zeroInsets, "FormattedTextField.opaque", java.lang.Boolean.TRUE, "FormattedTextField.errorIndicatorForeground", new javax.swing.plaf.ColorUIResource(0xfe4a41), "FormattedTextField.selectionBackground", selectionBackground, "FormattedTextField.selectionForeground", selectionForeground, // *** Labels
        "Label.font", dialogPlain12, "Label.border", new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new java.lang.Object[]{ 0, 0, 0, 0 }), // *** Lists
        "List.focusCellHighlightBorder", focusCellHighlightBorder, "List.cellRenderer", new javax.swing.UIDefaults.ProxyLazyValue("org.jhotdraw.gui.plaf.palette.PaletteListCellRenderer"), // *** Panels
        "Panel.background", control, "Panel.foreground", controlText, "Panel.opaque", java.lang.Boolean.TRUE, // *** Ribbons
        "Ribbon.border", new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.border.MatteBorder", new java.lang.Object[]{ new java.awt.Insets(1, 0, 0, 0), new java.awt.Color(0x777777) }), // *** ScrollPane
        "ScrollPane.border", new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.border.MatteBorder", new java.lang.Object[]{ new java.awt.Insets(1, 1, 1, 1), new java.awt.Color(0xa5a5a5) }), // *** Slider
        "Slider.background", control, "Slider.foreground", controlText, "Slider.horizontalSize", new javax.swing.plaf.DimensionUIResource(100, 20), "Slider.verticalSize", new javax.swing.plaf.DimensionUIResource(20, 100), "Slider.northThumb.small", new javax.swing.UIDefaults.ProxyLazyValue("org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon", new java.lang.Object[]{ "/org/jhotdraw/gui/plaf/palette/images/Slider.northThumbs.small.png", 6, true }), "Slider.westThumb.small", new javax.swing.UIDefaults.ProxyLazyValue("org.jhotdraw.gui.plaf.palette.PaletteSliderThumbIcon", new java.lang.Object[]{ "/org/jhotdraw/gui/plaf/palette/images/Slider.westThumbs.small.png", 6, true }), // *** TabbedPane
        "TabbedPane.font", dialogPlain12, "TabbedPane.selectedFont", dialogBold12, "TabbedPane.background", control, "TabbedPane.contentAreaColor", control, "TabbedPane.foreground", controlText, "TabbedPane.highlight", new javax.swing.plaf.ColorUIResource(0xa5a5a5), "TabbedPane.lightHighlight", new javax.swing.plaf.ColorUIResource(0xa5a5a5), "TabbedPane.shadow", new javax.swing.plaf.ColorUIResource(0xa5a5a5), "TabbedPane.darkShadow", new javax.swing.plaf.ColorUIResource(0x333333), // *** TextArea
        "TextArea.selectionBackground", selectionBackground, "TextArea.selectionForeground", selectionForeground, // *** TextField
        "TextField.font", fieldPlain12, "TextField.background", control, "TextField.foreground", controlText, "TextField.border", textBorder, "TextField.margin", zeroInsets, "TextField.opaque", java.lang.Boolean.TRUE, "TextField.selectionBackground", selectionBackground, "TextField.selectionForeground", selectionForeground, // *** ToolBar
        "ToolBar.font", dialogPlain12, "ToolBar.background", control, "ToolBar.foreground", controlText, "ToolBar.dockingBackground", control, // "ToolBar.dockingForeground", red,
        "ToolBar.floatingBackground", control, // "ToolBar.floatingForeground", darkGray,
        // "ToolBar.border", etchedBorder,
        "ToolBar.border", new javax.swing.UIDefaults.ProxyLazyValue("org.jhotdraw.gui.plaf.palette.PaletteToolBarBorder$UIResource")// 
         }// "ToolBar.separatorSize", toolBarSeparatorSize,
        ;
        table.putDefaults(defaults);
    }

    /**
     * Returns the ui that is of type <code>klass</code>, or null if one can not be found.
     */
    static java.lang.Object getUIOfType(javax.swing.plaf.ComponentUI ui, java.lang.Class<?> klass) {
        if (klass.isInstance(ui)) {
            return ui;
        }
        return null;
    }
}