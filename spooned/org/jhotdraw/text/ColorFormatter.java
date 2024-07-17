/* @(#)ColorFormatter.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.text;
import java.awt.Color;
import java.awt.color.ColorSpace;
/**
 * {@code ColorFormatter} is used to format colors into a textual representation which can be edited
 * in an entry field.
 *
 * <p>The following formats are supported:
 *
 * <ul>
 *   <li><b>Format.RGB_HEX</b> - {@code "#"rrggbb} or {@code "#"rgb}.<br>
 *       If 6 digits are entered, each pair of hexadecimal digits, in the range 0 to F, represents
 *       one sRGB color component in the order red, green and blue. The digits A to F may be given
 *       in either uppercase or lowercase.<br>
 *       If only 3 digits are entered, they are expanded to 6 digits by replicating each digit.<br>
 *       This syntactical form can represent 16777216 colors. Examples: {@code #9400D3} (i.e. a dark
 *       violet), {@code #FFD700} (i.e. a golden color), {@code #000} (i.e. black) {@code #fff}
 *       (i.e. white).
 *   <li><b>Format.RGB_INTEGER_SHORT</b> - {@code red green blue}, or {@code red green blue}
 *       optionally separated by commas.<br>
 *       Each value represents one sRGB color component. Each value is in the range 0 to 255. This
 *       syntactical form can represent 16777216 colors. Examples: {@code 233 150 122} (i.e. a
 *       salmon pink), {@code 255 165 0} (i.e. an orange).
 *   <li><b>Format.RGB_INTEGER</b> - {@code "rgb" red green blue}, or {@code red green blue}
 *       optionally separated by commas.<br>
 *       Each value represents one sRGB color component. Each value is in the range 0 to 255. This
 *       syntactical form can represent 16777216 colors. Examples: {@code rgb 233 150 122} (i.e. a
 *       salmon pink), {@code rgb 255 165 0} (i.e. an orange).
 *   <li><b>Format.RGB_PERCENTAGE</b> - {@code "rgb%" red green blue}, or {@code red"%" green"%"
 *       blue"%"} optionally separated by commas.<br>
 *       Each value represents one sRGB color component. Each value is in the range 0.0 to 100.0.
 *       This syntactical form can represent 10^9 colors.
 *   <li><b>Format.GRAY_PERCENTAGE</b> - {@code "gray" brightness}.<br>
 *       The value represents the brightness in the range from 0.0 to 100.0.
 *   <li><b>Format.HSB_PERCENTAGE</b> - {@code "hsb" hue saturation brightness}.<br>
 *       Each integer represents one HSV component in the order hue, saturation and value, separated
 *       by a comma and optionally by white space. Hue is in the range from 0.0 to 359.0, saturation
 *       and brightness in the range from 0.0 to 100.0.
 * </ul>
 *
 * <p>By default, the formatter is adaptive, meaning that the format depends on the {@code ColorSpace} of the current {@code Color} value.
 *
 * <p>FIXME - This class does too much work. It should be split up into individual classes for each
 * of the supported formats.
 */
public class ColorFormatter extends javax.swing.text.DefaultFormatter {
    private static final long serialVersionUID = 1L;

    /**
     * Specifies the formats supported by ColorFormatter.
     */
    public enum Format {

        RGB_HEX,
        RGB_INTEGER_SHORT,
        RGB_INTEGER,
        RGB_PERCENTAGE,
        HSB_PERCENTAGE,
        GRAY_PERCENTAGE,
        CMYK_PERCENTAGE;
    }

    /**
     * Specifies the preferred output format.
     */
    protected org.jhotdraw.text.ColorFormatter.Format outputFormat = org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER;

    /**
     * Specifies the last used input format.
     */
    protected org.jhotdraw.text.ColorFormatter.Format lastUsedInputFormat = null;

    /**
     * This regular expression is used for parsing the RGB_HEX format.
     */
    protected static final java.util.regex.Pattern RGB_HEX_PATTERN = java.util.regex.Pattern.compile("^\\s*(?:[rR][gG][bB]\\s*#|#)\\s*([0-9a-fA-F]{3,6})\\s*$");

    /**
     * This regular expression is used for parsing the RGB_INTEGER format.
     */
    protected static final java.util.regex.Pattern RGB_INTEGER_SHORT_PATTERN = java.util.regex.Pattern.compile("^\\s*([0-9]{1,3})(?:\\s*,\\s*|\\s+)([0-9]{1,3})(?:\\s*,\\s*|\\s+)([0-9]{1,3})\\s*$");

    /**
     * This regular expression is used for parsing the RGB_INTEGER format.
     */
    protected static final java.util.regex.Pattern RGB_INTEGER_PATTERN = java.util.regex.Pattern.compile("^\\s*(?:[rR][gG][bB])?\\s*([0-9]{1,3})(?:\\s*,\\s*|\\s+)([0-9]{1,3})(?:\\s*,\\s*|\\s+)([0-9]{1,3})\\s*$");

    /**
     * This regular expression is used for parsing the RGB_PERCENTAGE format.
     */
    protected static final java.util.regex.Pattern RGB_PERCENTAGE_PATTERN = java.util.regex.Pattern.compile("^\\s*(?:[rR][gG][bB][%])?\\s*([0-9]{1,3}(?:\\.[0-9]+)?)(?:\\s*,\\s*|\\s+)([0-9]{1,3}(?:\\.[0-9]+)?)(?:\\s*,\\s*|\\s+)([0-9]{1,3}(?:\\.[0-9]+)?)\\s*$");

    /**
     * This regular expression is used for parsing the HSB_PERCENTAGE format. This format is
     * recognized when the degree sign is present.
     */
    protected static final java.util.regex.Pattern HSB_PERCENTAGE_PATTERN = java.util.regex.Pattern.compile("^\\s*(?:[hH][sS][bB])?\\s*([0-9]{1,3}(?:\\.[0-9]+)?)(?:\\s*,\\s*|\\s+)([0-9]{1,3}(?:\\.[0-9]+)?)(?:\\s*,\\s*|\\s+)([0-9]{1,3}(?:\\.[0-9]+)?)\\s*$");

    /**
     * This regular expression is used for parsing the GRAY_PERCENTAGE format. This format is
     * recognized when the degree sign is present.
     */
    protected static final java.util.regex.Pattern GRAY_PERCENTAGE_PATTERN = java.util.regex.Pattern.compile("^\\s*(?:[gG][rR][aA][yY])?\\s*([0-9]{1,3}(?:\\.[0-9]+)?)\\s*$");

    /**
     * Specifies whether the formatter allows null values.
     */
    protected boolean allowsNullValue = true;

    /**
     * Specifies whether the formatter should adaptively change its output format depending on the
     * last input format used by the user.
     */
    protected boolean isAdaptive = true;

    /**
     * Preferences used for storing the last used input format.
     */
    protected java.util.prefs.Preferences prefs;

    protected java.text.DecimalFormat numberFormat;

    public ColorFormatter() {
        this(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER, true, true);
    }

    public ColorFormatter(org.jhotdraw.text.ColorFormatter.Format outputFormat, boolean allowsNullValue, boolean isAdaptive) {
        this.outputFormat = outputFormat;
        this.allowsNullValue = allowsNullValue;
        this.isAdaptive = isAdaptive;
        numberFormat = new java.text.DecimalFormat("#.#");
        numberFormat.setDecimalSeparatorAlwaysShown(false);
        numberFormat.setMaximumFractionDigits(1);
        java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        numberFormat.setDecimalFormatSymbols(dfs);
        // Retrieve last used input format from preferences
        prefs = org.jhotdraw.util.prefs.PreferencesUtil.userNodeForPackage(getClass());
        try {
            lastUsedInputFormat = org.jhotdraw.text.ColorFormatter.Format.valueOf(prefs.get("ColorFormatter.lastUsedInputFormat", org.jhotdraw.text.ColorFormatter.Format.RGB_HEX.name()));
        } catch (java.lang.IllegalArgumentException e) {
            // leave lastUsedInputFormat as null
        }
        if (isAdaptive && (lastUsedInputFormat != null)) {
            this.outputFormat = lastUsedInputFormat;
        }
        setOverwriteMode(false);
    }

    /**
     * Sets the output format.
     *
     * @param newValue
     */
    public void setOutputFormat(org.jhotdraw.text.ColorFormatter.Format newValue) {
        if (newValue == null) {
            throw new java.lang.NullPointerException("outputFormat may not be null");
        }
        outputFormat = newValue;
    }

    /**
     * Gets the output format.
     */
    public org.jhotdraw.text.ColorFormatter.Format getOutputFormat() {
        return outputFormat;
    }

    /**
     * Gets the last used input format.
     */
    public org.jhotdraw.text.ColorFormatter.Format getLastUsedInputFormat() {
        return lastUsedInputFormat;
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
     * Sets whether the color formatter adaptively selects its output format depending on the last
     * input format used by the user.
     *
     * @param newValue
     */
    public void setAdaptive(boolean newValue) {
        isAdaptive = newValue;
        if (newValue && (lastUsedInputFormat != null)) {
            outputFormat = lastUsedInputFormat;
        }
    }

    /**
     * Returns true, if the color formatter is adaptive.
     */
    public boolean isAdaptive() {
        return isAdaptive;
    }

    private void setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format newValue) {
        lastUsedInputFormat = newValue;
        if (isAdaptive) {
            outputFormat = lastUsedInputFormat;
        }
        prefs.put("ColorFormatter.lastUsedInputFormat", newValue.name());
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
        // Format RGB_HEX
        java.util.regex.Matcher matcher = org.jhotdraw.text.ColorFormatter.RGB_HEX_PATTERN.matcher(str);
        if (matcher.matches()) {
            setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.RGB_HEX);
            try {
                java.lang.String group1 = matcher.group(1);
                if (group1.length() == 3) {
                    return new java.awt.Color(java.lang.Integer.parseInt(((((("" + group1.charAt(0)) + group1.charAt(0)) + group1.charAt(1)) + group1.charAt(1)) + group1.charAt(2)) + group1.charAt(2), 16));
                } else if (group1.length() == 6) {
                    return new java.awt.Color(java.lang.Integer.parseInt(group1, 16));
                } else {
                    throw new java.text.ParseException("Hex color must have 3 or 6 digits.", 1);
                }
            } catch (java.lang.NumberFormatException nfe) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(nfe);
                throw pe;
            }
        }
        // Format RGB_INTEGER_SHORT and RGB_INTEGER
        matcher = org.jhotdraw.text.ColorFormatter.RGB_INTEGER_SHORT_PATTERN.matcher(str);
        if (matcher.matches()) {
            setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER_SHORT);
        } else {
            matcher = org.jhotdraw.text.ColorFormatter.RGB_INTEGER_PATTERN.matcher(str);
            if (matcher.matches()) {
                setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER);
            }
        }
        if (matcher.matches()) {
            try {
                return new java.awt.Color(java.lang.Integer.parseInt(matcher.group(1)), java.lang.Integer.parseInt(matcher.group(2)), java.lang.Integer.parseInt(matcher.group(3)));
            } catch (java.lang.NumberFormatException nfe) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(nfe);
                throw pe;
            } catch (java.lang.IllegalArgumentException iae) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(iae);
                throw pe;
            }
        }
        // Format RGB_PERCENTAGE
        matcher = org.jhotdraw.text.ColorFormatter.RGB_PERCENTAGE_PATTERN.matcher(str);
        if (matcher.matches()) {
            setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.RGB_PERCENTAGE);
            try {
                return new java.awt.Color(numberFormat.parse(matcher.group(1)).floatValue() / 100.0F, numberFormat.parse(matcher.group(2)).floatValue() / 100.0F, numberFormat.parse(matcher.group(3)).floatValue() / 100.0F);
            } catch (java.lang.NumberFormatException nfe) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(nfe);
                throw pe;
            } catch (java.lang.IllegalArgumentException iae) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(iae);
                throw pe;
            }
        }
        // Format HSB_PERCENTAGE
        matcher = org.jhotdraw.text.ColorFormatter.HSB_PERCENTAGE_PATTERN.matcher(str);
        if (matcher.matches()) {
            setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.HSB_PERCENTAGE);
            try {
                return new java.awt.Color(org.jhotdraw.color.HSBColorSpace.getInstance(), new float[]{ matcher.group(1) == null ? 0.0F : numberFormat.parse(matcher.group(1)).floatValue() / 360.0F, matcher.group(2) == null ? 1.0F : numberFormat.parse(matcher.group(2)).floatValue() / 100.0F, matcher.group(3) == null ? 1.0F : numberFormat.parse(matcher.group(3)).floatValue() / 100.0F }, 1.0F);
            } catch (java.lang.NumberFormatException nfe) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(nfe);
                throw pe;
            } catch (java.lang.IllegalArgumentException iae) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(iae);
                throw pe;
            }
        }
        // Format GRAY_PERCENTAGE
        matcher = org.jhotdraw.text.ColorFormatter.GRAY_PERCENTAGE_PATTERN.matcher(str);
        if (matcher.matches()) {
            setLastUsedInputFormat(org.jhotdraw.text.ColorFormatter.Format.GRAY_PERCENTAGE);
            try {
                return org.jhotdraw.color.ColorUtil.toColor(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY), new float[]{ matcher.group(1) == null ? 0.0F : numberFormat.parse(matcher.group(1)).floatValue() / 100.0F });
            } catch (java.lang.NumberFormatException nfe) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(nfe);
                throw pe;
            } catch (java.lang.IllegalArgumentException iae) {
                java.text.ParseException pe = new java.text.ParseException(str, 0);
                pe.initCause(iae);
                throw pe;
            }
        }
        throw new java.text.ParseException(str, 0);
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
            if (!(value instanceof java.awt.Color)) {
                throw new java.text.ParseException("Value is not a color " + value, 0);
            }
            java.awt.Color c = ((java.awt.Color) (value));
            org.jhotdraw.text.ColorFormatter.Format f = outputFormat;
            if (isAdaptive) {
                switch (c.getColorSpace().getType()) {
                    case java.awt.color.ColorSpace.TYPE_HSV :
                        f = org.jhotdraw.text.ColorFormatter.Format.HSB_PERCENTAGE;
                        break;
                    case java.awt.color.ColorSpace.TYPE_GRAY :
                        f = org.jhotdraw.text.ColorFormatter.Format.GRAY_PERCENTAGE;
                        break;
                    case java.awt.color.ColorSpace.TYPE_RGB :
                    default :
                        f = org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER_SHORT;
                }
            }
            switch (f) {
                case RGB_HEX :
                    str = "000000" + java.lang.Integer.toHexString(c.getRGB() & 0xffffff);
                    str = "#" + str.substring(str.length() - 6);
                    break;
                case RGB_INTEGER_SHORT :
                    str = (((c.getRed() + " ") + c.getGreen()) + " ") + c.getBlue();
                    break;
                case RGB_INTEGER :
                    str = (((("rgb " + c.getRed()) + " ") + c.getGreen()) + " ") + c.getBlue();
                    break;
                case RGB_PERCENTAGE :
                    str = ((((("rgb% " + numberFormat.format(c.getRed() / 255.0F)) + " ") + numberFormat.format(c.getGreen() / 255.0F)) + " ") + numberFormat.format(c.getBlue() / 255.0F)) + "";
                    break;
                case HSB_PERCENTAGE :
                    float[] components;
                    if (c.getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_HSV) {
                        components = c.getComponents(null);
                    } else {
                        components = java.awt.Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), new float[3]);
                    }
                    str = ((((("hsb " + numberFormat.format(components[0] * 360)) + " ") + numberFormat.format(components[1] * 100)) + " ") + numberFormat.format(components[2] * 100)) + "";
                    break;
                case GRAY_PERCENTAGE :
                    if (c.getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_GRAY) {
                        components = c.getComponents(null);
                    } else {
                        components = c.getColorComponents(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY), null);
                    }
                    str = ("gray " + numberFormat.format(components[0] * 100)) + "";
                    break;
            }
        }
        return str;
    }

    /**
     * Convenience method for creating a formatter factory with a {@code ColorFormatter}. Uses the
     * RGB_INTEGER_SHORT format, allows null values and is adaptive.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory() {
        return org.jhotdraw.text.ColorFormatter.createFormatterFactory(org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER_SHORT, true, true);
    }

    /**
     * Convenience method for creating a formatter factory with a 8@code ColorFormatter}.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory(org.jhotdraw.text.ColorFormatter.Format outputFormat, boolean allowsNullValue, boolean isAdaptive) {
        return new javax.swing.text.DefaultFormatterFactory(new org.jhotdraw.text.ColorFormatter(outputFormat, allowsNullValue, isAdaptive));
    }
}