/* @(#)ResourceBundleUtil.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * This is a convenience wrapper for accessing resources stored in a ResourceBundle.
 *
 * <p><b>Placeholders</b><br>
 * On top of the functionality provided by ResourceBundle, a property value can include text from
 * another property, by specifying the desired property name and format type between <code>"${"
 * </code> and <code>"}"</code>.
 *
 * <p>For example, if there is a {@code "imagedir"} property with the value {@code "/org/jhotdraw/undo/images"}, then this could be used in an attribute like this: <code>
 * ${imagedir}/editUndo.png</code>. This is resolved at run-time as {@code /org/jhotdraw/undo/images/editUndo.png}.
 *
 * <p>Property names in placeholders can contain modifiers. Modifiers are written between @code
 * "[$"} and {@code "]"}. Each modifier has a fallback chain.
 *
 * <p>For example, if the property name modifier {@code "os"} has the value "win", and its fallback
 * chain is {@code "mac","default"}, then the property name <code>${preferences.text.[$os]}</code>
 * is first evaluted to {@code preferences.text.win}, and - if no property with this name exists -
 * it is evaluated to {@code preferences.text.mac}, and then to {@code preferences.text.default}.
 *
 * <p>The property name modifier "os" is defined by default. It can assume the values "win", "mac"
 * and "other". Its fallback chain is "default".
 *
 * <p>The format type can be optinally specified after a comma. The following format types are
 * supported:
 *
 * <ul>
 *   <li>{@code string} This is the default format.
 *   <li>{@code accelerator} This format replaces all occurences of the keywords shift, control,
 *       ctrl, meta, alt, altGraph by properties which start with {@code accelerator.}. For example,
 *       shift is replaced by {@code accelerator.shift}.
 * </ul>
 */
public class ResourceBundleUtil implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private static final java.util.HashSet<java.lang.String> ACCELERATOR_KEYS = new java.util.HashSet<java.lang.String>(java.util.Arrays.asList(new java.lang.String[]{ "shift", "control", "ctrl", "meta", "alt", "altGraph" }));

    /**
     * The wrapped resource bundle.
     */
    private transient java.util.ResourceBundle resource;

    /**
     * The locale.
     */
    private java.util.Locale locale;

    /**
     * The base class
     */
    private java.lang.Class<?> baseClass = getClass();

    /**
     * The base name of the resource bundle.
     */
    private java.lang.String baseName;

    /**
     * The global verbose property.
     */
    private static boolean isVerbose = false;

    /**
     * The global map of property name modifiers. The key of this map is the name of the property name
     * modifier, the value of this map is a fallback chain.
     */
    private static java.util.HashMap<java.lang.String, java.lang.String[]> propertyNameModifiers = new java.util.HashMap<java.lang.String, java.lang.String[]>();

    static {
        java.lang.String osName = java.lang.System.getProperty("os.name").toLowerCase();
        java.lang.String os;
        if (osName.startsWith("mac os x")) {
            os = "mac";
        } else if (osName.startsWith("windows")) {
            os = "win";
        } else {
            os = "other";
        }
        propertyNameModifiers.put("os", new java.lang.String[]{ os, "default" });
    }

    /**
     * Creates a new ResouceBundleUtil which wraps the provided resource bundle.
     */
    public ResourceBundleUtil(java.lang.String baseName, java.util.Locale locale) {
        this.locale = locale;
        this.baseName = baseName;
        this.resource = java.util.ResourceBundle.getBundle(baseName, locale);
    }

    /**
     * Returns the wrapped resource bundle.
     *
     * @return The wrapped resource bundle.
     */
    public java.util.ResourceBundle getWrappedBundle() {
        return resource;
    }

    /**
     * Get a String from the ResourceBundle. <br>
     * Convenience method to save casting.
     *
     * @param key
     * 		The key of the property.
     * @return The value of the property. Returns the key if the property is missing.
     */
    public java.lang.String getString(java.lang.String key) {
        try {
            java.lang.String value = getStringRecursive(key);
            // System.out.println("ResourceBundleUtil "+baseName+" get("+key+"):"+value);
            return value;
        } catch (java.util.MissingResourceException e) {
            // System.out.println("ResourceBundleUtil "+baseName+" get("+key+"):***MISSING***");
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + "\" not found.");
                // e.printStackTrace();
            }
            return key;
        }
    }

    /**
     * Recursive part of the getString method.
     *
     * @param key
     * @throws java.util.MissingResourceException
     */
    private java.lang.String getStringRecursive(java.lang.String key) throws java.util.MissingResourceException {
        java.lang.String value = resource.getString(key);
        // Substitute placeholders in the value
        for (int p1 = value.indexOf("${"); p1 != (-1); p1 = value.indexOf("${")) {
            int p2 = value.indexOf('}', p1 + 2);
            if (p2 == (-1)) {
                break;
            }
            java.lang.String placeholderKey = value.substring(p1 + 2, p2);
            java.lang.String placeholderFormat;
            int p3 = placeholderKey.indexOf(',');
            if (p3 != (-1)) {
                placeholderFormat = placeholderKey.substring(p3 + 1);
                placeholderKey = placeholderKey.substring(0, p3);
            } else {
                placeholderFormat = "string";
            }
            java.util.ArrayList<java.lang.String> fallbackKeys = new java.util.ArrayList<>();
            generateFallbackKeys(placeholderKey, fallbackKeys);
            java.lang.String placeholderValue = null;
            for (java.lang.String fk : fallbackKeys) {
                try {
                    placeholderValue = getStringRecursive(fk);
                    break;
                } catch (java.util.MissingResourceException e) {
                    // empty allowed
                }
            }
            if (placeholderValue == null) {
                throw new java.util.MissingResourceException((("\"" + key) + "\" not found in ") + baseName, baseName, key);
            }
            // Do post-processing depending on placeholder format
            if ("accelerator".equals(placeholderFormat)) {
                // Localize the keywords shift, control, ctrl, meta, alt, altGraph
                java.lang.StringBuilder b = new java.lang.StringBuilder();
                for (java.lang.String s : placeholderValue.split(" ")) {
                    if (org.jhotdraw.util.ResourceBundleUtil.ACCELERATOR_KEYS.contains(s)) {
                        b.append(getString("accelerator." + s));
                    } else {
                        b.append(s);
                    }
                }
                placeholderValue = b.toString();
            }
            // Insert placeholder value into value
            value = (value.substring(0, p1) + placeholderValue) + value.substring(p2 + 1);
        }
        return value;
    }

    /**
     * Generates fallback keys by processing all property name modifiers in the key.
     */
    private void generateFallbackKeys(java.lang.String key, java.util.ArrayList<java.lang.String> fallbackKeys) {
        int p1 = key.indexOf("[$");
        if (p1 == (-1)) {
            fallbackKeys.add(key);
        } else {
            int p2 = key.indexOf(']', p1 + 2);
            if (p2 == (-1)) {
                return;
            }
            java.lang.String modifierKey = key.substring(p1 + 2, p2);
            java.lang.String[] modifierValues = org.jhotdraw.util.ResourceBundleUtil.propertyNameModifiers.get(modifierKey);
            if (modifierValues == null) {
                modifierValues = new java.lang.String[]{ "default" };
            }
            for (java.lang.String mv : modifierValues) {
                generateFallbackKeys((key.substring(0, p1) + mv) + key.substring(p2 + 1), fallbackKeys);
            }
        }
    }

    /**
     * Returns a formatted string using javax.text.MessageFormat.
     *
     * @param key
     * @param arguments
     * @return formatted String
     */
    public java.lang.String getFormatted(java.lang.String key, java.lang.Object... arguments) {
        return java.text.MessageFormat.format(getString(key), arguments);
    }

    /**
     * Returns a formatted string using java.util.Formatter().
     *
     * @param key
     * @param arguments
     * @return formatted String
     */
    public java.lang.String format(java.lang.String key, java.lang.Object... arguments) {
        // return String.format(resource.getLocale(), getString(key), arguments);
        return new java.util.Formatter(resource.getLocale()).format(getString(key), arguments).toString();
    }

    /**
     * Get an Integer from the ResourceBundle. <br>
     * Convenience method to save casting.
     *
     * @param key
     * 		The key of the property.
     * @return The value of the property. Returns -1 if the property is missing.
     */
    public java.lang.Integer getInteger(java.lang.String key) {
        try {
            return java.lang.Integer.valueOf(getStringRecursive(key));
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + "\" not found.");
                // e.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Get a small image icon from the ResourceBundle for use on a {@code JMenuItem}. <br>
     * Convenience method .
     *
     * @param key
     * 		The key of the property. This method appends ".smallIcon" to the key.
     * @return The value of the property. Returns null if the property is missing.
     */
    public javax.swing.ImageIcon getSmallIconProperty(java.lang.String key, java.lang.Class<?> baseClass) {
        javax.swing.ImageIcon icon = getIconProperty(key, ".smallIcon", baseClass);
        if (icon == null) {
            icon = getIconProperty(key, ".icon", baseClass);
        }
        return icon;
    }

    /**
     * Get a large image icon from the ResourceBundle for use on a {@code JButton}. <br>
     * Convenience method .
     *
     * @param key
     * 		The key of the property. This method appends ".largeIcon" to the key.
     * @return The value of the property. Returns null if the property is missing.
     */
    public javax.swing.ImageIcon getLargeIconProperty(java.lang.String key, java.lang.Class<?> baseClass) {
        javax.swing.ImageIcon icon = getIconProperty(key, ".largeIcon", baseClass);
        if (icon == null) {
            icon = getIconProperty(key, ".icon", baseClass);
        }
        return icon;
    }

    private javax.swing.ImageIcon getIconProperty(java.lang.String key, java.lang.String suffix, java.lang.Class<?> baseClass) {
        try {
            java.lang.String rsrcName = getStringRecursive(key + suffix);
            if ("".equals(rsrcName)) {
                return null;
            }
            java.net.URL url = baseClass.getResource(rsrcName);
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose && (url == null)) {
                java.lang.System.err.println((((((("Warning ResourceBundleUtil[" + baseName) + "].getIconProperty \"") + key) + suffix) + "\" resource:") + rsrcName) + " not found.");
            }
            return url == null ? null : new javax.swing.ImageIcon(url);
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println((((("Warning ResourceBundleUtil[" + baseName) + "].getIconProperty \"") + key) + suffix) + "\" not found.");
                // e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get a Mnemonic from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property.
     * @return The first char of the value of the property. Returns '\0' if the property is missing.
     */
    public char getMnemonic(java.lang.String key) {
        java.lang.String s = getStringRecursive(key);
        return (s == null) || (s.length() == 0) ? '\u0000' : s.charAt(0);
    }

    /**
     * Gets a char for a JavaBeans "mnemonic" property from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property. This method appends ".mnemonic" to the key.
     * @return The first char of the value of the property. Returns '\0' if the property is missing.
     */
    public char getMnemonicProperty(java.lang.String key) {
        java.lang.String s;
        try {
            s = getStringRecursive(key + ".mnemonic");
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + ".mnemonic\" not found.");
                // e.printStackTrace();
            }
            s = null;
        }
        return (s == null) || (s.length() == 0) ? '\u0000' : s.charAt(0);
    }

    /**
     * Get a String for a JavaBeans "toolTipText" property from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property. This method appends ".toolTipText" to the key.
     * @return The ToolTip. Returns null if no tooltip is defined.
     */
    public java.lang.String getToolTipTextProperty(java.lang.String key) {
        try {
            java.lang.String value = getStringRecursive(key + ".toolTipText");
            return value;
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + ".toolTipText\" not found.");
                // e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get a String for a JavaBeans "text" property from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property. This method appends ".text" to the key.
     * @return The ToolTip. Returns null if no tooltip is defined.
     */
    public java.lang.String getTextProperty(java.lang.String key) {
        try {
            java.lang.String value = getStringRecursive(key + ".text");
            return value;
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + ".text\" not found.");
                // e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get a KeyStroke from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property.
     * @return <code>javax.swing.KeyStroke.getKeyStroke(value)</code>. Returns null if the property is
    missing.
     */
    public javax.swing.KeyStroke getKeyStroke(java.lang.String key) {
        javax.swing.KeyStroke ks = null;
        try {
            java.lang.String s = getStringRecursive(key);
            ks = (s == null) ? ((javax.swing.KeyStroke) (null)) : javax.swing.KeyStroke.getKeyStroke(s);
        } catch (java.util.NoSuchElementException e) {
            // empty allowed
        }
        return ks;
    }

    /**
     * Gets a KeyStroke for a JavaBeans "accelerator" property from the ResourceBundle. <br>
     * Convenience method.
     *
     * @param key
     * 		The key of the property. This method adds ".accelerator" to the key.
     * @return <code>javax.swing.KeyStroke.getKeyStroke(value)</code>. Returns null if the property is
    missing.
     */
    public javax.swing.KeyStroke getAcceleratorProperty(java.lang.String key) {
        javax.swing.KeyStroke ks = null;
        try {
            java.lang.String s;
            s = getStringRecursive(key + ".accelerator");
            ks = (s == null) ? ((javax.swing.KeyStroke) (null)) : javax.swing.KeyStroke.getKeyStroke(s);
        } catch (java.util.MissingResourceException e) {
            if (org.jhotdraw.util.ResourceBundleUtil.isVerbose) {
                java.lang.System.err.println(((("Warning ResourceBundleUtil[" + baseName) + "] \"") + key) + ".accelerator\" not found.");
                // e.printStackTrace();
            }
        } catch (java.util.NoSuchElementException e) {
            // empty allowed
        }
        return ks;
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @see java.util.ResourceBundle
     */
    public static org.jhotdraw.util.ResourceBundleUtil getBundle(java.lang.String baseName) throws java.util.MissingResourceException {
        return org.jhotdraw.util.ResourceBundleUtil.getBundle(baseName, org.jhotdraw.util.LocaleUtil.getDefault());
    }

    public void setBaseClass(java.lang.Class<?> baseClass) {
        this.baseClass = baseClass;
    }

    public java.lang.Class<?> getBaseClass() {
        return baseClass;
    }

    public void configureAction(javax.swing.Action action, java.lang.String argument) {
        configureAction(action, argument, getBaseClass());
    }

    public void configureAction(javax.swing.Action action, java.lang.String argument, java.lang.Class<?> baseClass) {
        action.putValue(javax.swing.Action.NAME, getTextProperty(argument));
        java.lang.String shortDescription = getToolTipTextProperty(argument);
        if ((shortDescription != null) && (shortDescription.length() > 0)) {
            action.putValue(javax.swing.Action.SHORT_DESCRIPTION, shortDescription);
        }
        action.putValue(javax.swing.Action.ACCELERATOR_KEY, getAcceleratorProperty(argument));
        action.putValue(javax.swing.Action.MNEMONIC_KEY, java.lang.Integer.valueOf(getMnemonicProperty(argument)));
        action.putValue(javax.swing.Action.SMALL_ICON, getSmallIconProperty(argument, baseClass));
        action.putValue(javax.swing.Action.LARGE_ICON_KEY, getLargeIconProperty(argument, baseClass));
    }

    public void configureButton(javax.swing.AbstractButton button, java.lang.String argument) {
        configureButton(button, argument, getBaseClass());
    }

    public void configureButton(javax.swing.AbstractButton button, java.lang.String argument, java.lang.Class<?> baseClass) {
        button.setText(getTextProperty(argument));
        // button.setACCELERATOR_KEY, getAcceleratorProperty(argument));
        // action.putValue(Action.MNEMONIC_KEY, new Integer(getMnemonicProperty(argument)));
        button.setIcon(getLargeIconProperty(argument, baseClass));
        button.setToolTipText(getToolTipTextProperty(argument));
    }

    public void configureToolBarButton(javax.swing.AbstractButton button, java.lang.String argument) {
        configureToolBarButton(button, argument, getBaseClass());
    }

    public void configureToolBarButton(javax.swing.AbstractButton button, java.lang.String argument, java.lang.Class<?> baseClass) {
        javax.swing.Icon icon = getLargeIconProperty(argument, baseClass);
        if (icon != null) {
            button.setIcon(getLargeIconProperty(argument, baseClass));
            button.setText(null);
        } else {
            button.setIcon(null);
            button.setText(getTextProperty(argument));
        }
        button.setToolTipText(getToolTipTextProperty(argument));
    }

    /**
     * Configures a menu item with a text, an accelerator, a mnemonic and a menu icon.
     */
    public void configureMenu(javax.swing.JMenuItem menu, java.lang.String argument) {
        menu.setText(getTextProperty(argument));
        if (!(menu instanceof javax.swing.JMenu)) {
            menu.setAccelerator(getAcceleratorProperty(argument));
        }
        menu.setMnemonic(getMnemonicProperty(argument));
        menu.setIcon(getLargeIconProperty(argument, baseClass));
    }

    public javax.swing.JMenuItem createMenuItem(javax.swing.Action a, java.lang.String baseName) {
        javax.swing.JMenuItem mi = new javax.swing.JMenuItem();
        mi.setAction(a);
        configureMenu(mi, baseName);
        return mi;
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @see java.util.ResourceBundle
     */
    public static org.jhotdraw.util.ResourceBundleUtil getBundle(java.lang.String baseName, java.util.Locale locale) throws java.util.MissingResourceException {
        org.jhotdraw.util.ResourceBundleUtil r;
        r = new org.jhotdraw.util.ResourceBundleUtil(baseName, locale);
        return r;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((super.toString() + "[") + baseName) + ", ") + resource) + "]";
    }

    public static void setVerbose(boolean newValue) {
        org.jhotdraw.util.ResourceBundleUtil.isVerbose = newValue;
    }

    public static boolean isVerbose() {
        return org.jhotdraw.util.ResourceBundleUtil.isVerbose;
    }

    /**
     * Puts a property name modifier along with a fallback chain.
     *
     * @param name
     * 		The name of the modifier.
     * @param fallbackChain
     * 		The fallback chain of the modifier.
     */
    public static void putPropertyNameModifier(java.lang.String name, java.lang.String... fallbackChain) {
        org.jhotdraw.util.ResourceBundleUtil.propertyNameModifiers.put(name, fallbackChain);
    }

    /**
     * Removes a property name modifier.
     */
    public static void removePropertyNameModifier(java.lang.String name) {
        org.jhotdraw.util.ResourceBundleUtil.propertyNameModifiers.remove(name);
    }

    /**
     * Read object from ObjectInputStream and re-establish ResourceBundle.
     */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        // re-establish the "resource" variable
        this.resource = java.util.ResourceBundle.getBundle(baseName, locale);
    }
}