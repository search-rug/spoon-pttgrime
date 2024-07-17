/* @(#)FontFormatter.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.formatter;
/**
 * {@code FontFormatter} is used to format fonts into a textual representation which can be edited
 * in an entry field.
 *
 * <p>
 */
public class FontFormatter extends javax.swing.text.DefaultFormatter {
    private static final long serialVersionUID = 1L;

    /**
     * Specifies whether the formatter allows null values.
     */
    private boolean allowsNullValue = false;

    /**
     * Specifies whether the formatter allows unknown fonts.
     */
    private boolean allowsUnknownFont = false;

    /**
     * Map of generic font families. By default, holds a map of HTML generic font families. <a
     * href="http://www.w3.org/TR/CSS2/fonts.html#generic-font-families">
     * http://www.w3.org/TR/CSS2/fonts.html#generic-font-families</a>.
     */
    private java.util.HashMap<java.lang.String, java.awt.Font> genericFontFamilies = new java.util.HashMap<>();

    public FontFormatter() {
        this(true);
    }

    public FontFormatter(boolean allowsNullValue) {
        this.allowsNullValue = allowsNullValue;
        setOverwriteMode(false);
        // Map of HTML generic font families.
        // @see http://www.w3.org/TR/CSS2/fonts.html#generic-font-families
        putGenericFontFamily("serif", new java.awt.Font("Serif", java.awt.Font.PLAIN, 12));
        putGenericFontFamily("sans-serif", new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        putGenericFontFamily("cursive", new java.awt.Font("SansSerif", java.awt.Font.ITALIC, 12));
        putGenericFontFamily("fantasy", new java.awt.Font("Serif", java.awt.Font.PLAIN, 12));
        putGenericFontFamily("monospace", new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
    }

    /**
     * Sets whether a null value is allowed.
     *
     * @param newValue
     */
    public void setAllowsNullValue(boolean newValue) {
        allowsNullValue = newValue;
    }

    /**
     * Returns true, if null value is allowed.
     */
    public boolean getAllowsNullValue() {
        return allowsNullValue;
    }

    /**
     * Sets whether unknown font names are allowed.
     *
     * @param newValue
     */
    public void setAllowsUnknownFont(boolean newValue) {
        allowsUnknownFont = newValue;
    }

    /**
     * Returns true, if unknown font names are allowed.
     */
    public boolean getAllowsUnknownFont() {
        return allowsUnknownFont;
    }

    /**
     * Clears the generic font families map.
     */
    public void clearGenericFontFamilies() {
        genericFontFamilies = null;
    }

    /**
     * Adds a generic font family.
     */
    public void putGenericFontFamily(java.lang.String familyName, java.awt.Font font) {
        genericFontFamilies.put(familyName.toLowerCase(), font);
    }

    @java.lang.Override
    public java.lang.Object stringToValue(java.lang.String str) throws java.text.ParseException {
        // Handle null and empty case
        if ((str == null) || (str.trim().length() == 0)) {
            if (allowsNullValue) {
                return null;
            } else {
                throw new java.text.ParseException("Null value is not allowed.", 0);
            }
        }
        java.lang.String strLC = str.trim().toLowerCase();
        java.awt.Font f = null;
        f = genericFontFamilies.get(strLC);
        if (f == null) {
            f = java.awt.Font.decode(str);
            if (f == null) {
                throw new java.text.ParseException(str, 0);
            }
            if (!allowsUnknownFont) {
                java.lang.String fontName = f.getFontName().toLowerCase();
                java.lang.String family = f.getFamily().toLowerCase();
                if (((!fontName.equals(strLC)) && (!family.equals(strLC))) && (!fontName.equals(strLC + "-derived"))) {
                    throw new java.text.ParseException(str, 0);
                }
            }
        }
        return f;
    }

    @java.lang.Override
    public java.lang.String valueToString(java.lang.Object value) throws java.text.ParseException {
        java.lang.String str = null;
        if (value == null) {
            if (allowsNullValue) {
                str = "";
            } else {
                throw new java.text.ParseException("Null value is not allowed.", 0);
            }
        } else {
            if (!(value instanceof java.awt.Font)) {
                throw new java.text.ParseException("Value is not a font " + value, 0);
            }
            java.awt.Font f = ((java.awt.Font) (value));
            str = f.getFontName();
        }
        return str;
    }

    /**
     * Convenience method for creating a formatter factory with a {@code FontFormatter}. Uses the
     * RGB_INTEGER format and disallows null values.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory() {
        return org.jhotdraw.formatter.FontFormatter.createFormatterFactory(false);
    }

    /**
     * Convenience method for creating a formatter factory with a 8@code FontFormatter}.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory(boolean allowsNullValue) {
        return new javax.swing.text.DefaultFormatterFactory(new org.jhotdraw.formatter.FontFormatter(allowsNullValue));
    }
}