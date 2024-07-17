/* @(#)JavaNumberFormatter.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.formatter;
import java.text.ParseException;
/**
 * {@code ScaledNumberFormatter} is used to format numbers written in the Java programming syntax.
 */
public class JavaNumberFormatter extends javax.swing.text.DefaultFormatter {
    private static final long serialVersionUID = 1L;

    /**
     * Specifies whether the formatter allows null values.
     */
    private boolean allowsNullValue = false;

    @java.lang.SuppressWarnings("rawtypes")
    private java.lang.Comparable min;

    @java.lang.SuppressWarnings("rawtypes")
    private java.lang.Comparable max;

    private java.lang.String unit;

    private java.text.DecimalFormat decimalFormat;

    private java.text.DecimalFormat scientificFormat;

    private double multiplier = 1;

    private int minIntDigits;

    private int maxIntDigits;

    private int minFractionDigits;

    private int maxFractionDigits;

    private int minNegativeExponent = -3;

    private int minPositiveExponent = 8;

    private boolean usesScientificNotation = true;

    /**
     * Creates a <code>NumberFormatter</code> with the a default <code>NumberFormat</code> instance
     * obtained from <code>NumberFormat.getNumberInstance()</code>.
     */
    public JavaNumberFormatter() {
        super();
        initFormats();
    }

    /**
     * Creates a NumberFormatter with the specified Format instance.
     */
    public JavaNumberFormatter(double min, double max, double multiplier) {
        this(min, max, multiplier, false, null);
    }

    /**
     * Creates a NumberFormatter with the specified Format instance.
     */
    public JavaNumberFormatter(double min, double max, double multiplier, boolean allowsNullValue) {
        this(min, max, multiplier, allowsNullValue, null);
    }

    /**
     * Creates a NumberFormatter with the specified Format instance.
     */
    public JavaNumberFormatter(double min, double max, double multiplier, boolean allowsNullValue, java.lang.String unit) {
        super();
        initFormats();
        setMinimum(min);
        setMaximum(max);
        setMultiplier(multiplier);
        setAllowsNullValue(allowsNullValue);
        setOverwriteMode(false);
        setUnit(unit);
    }

    private void initFormats() {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(java.util.Locale.ENGLISH);
        decimalFormat = new java.text.DecimalFormat("#################0.#################", s);
        scientificFormat = new java.text.DecimalFormat("0.0################E0", s);
    }

    /**
     * Sets the minimum permissible value. If the <code>valueClass</code> has not been specified, and
     * <code>minimum</code> is non null, the <code>valueClass</code> will be set to that of the class
     * of <code>minimum</code>.
     *
     * @param minimum
     * 		Minimum legal value that can be input
     * @see #setValueClass
     */
    @java.lang.SuppressWarnings("rawtypes")
    public void setMinimum(java.lang.Comparable minimum) {
        if ((getValueClass() == null) && (minimum != null)) {
            setValueClass(minimum.getClass());
        }
        min = minimum;
    }

    /**
     * Returns the minimum permissible value.
     *
     * @return Minimum legal value that can be input
     */
    @java.lang.SuppressWarnings("rawtypes")
    public java.lang.Comparable getMinimum() {
        return min;
    }

    /**
     * Sets the maximum permissible value. If the <code>valueClass</code> has not been specified, and
     * <code>max</code> is non null, the <code>valueClass</code> will be set to that of the class of
     * <code>max</code>.
     *
     * @param max
     * 		Maximum legal value that can be input
     * @see #setValueClass
     */
    @java.lang.SuppressWarnings("rawtypes")
    public void setMaximum(java.lang.Comparable max) {
        if ((getValueClass() == null) && (max != null)) {
            setValueClass(max.getClass());
        }
        this.max = max;
    }

    /**
     * Returns the maximum permissible value.
     *
     * @return Maximum legal value that can be input
     */
    @java.lang.SuppressWarnings("rawtypes")
    public java.lang.Comparable getMaximum() {
        return max;
    }

    /**
     * Sets the multiplier for use in percent, per mille, and similar formats.
     */
    public void setMultiplier(double newValue) {
        multiplier = newValue;
    }

    /**
     * Gets the multiplier for use in percent, per mille, and similar formats.
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Allows/Disallows null values.
     *
     * @param newValue
     */
    public void setAllowsNullValue(boolean newValue) {
        allowsNullValue = newValue;
    }

    /**
     * Returns true if null values are allowed.
     */
    public boolean getAllowsNullValue() {
        return allowsNullValue;
    }

    /**
     * Specifies whether ".0" is appended to double and float values. By default this is true.
     *
     * @param newValue
     */
    public void setMinimumFractionDigits(int newValue) {
        minFractionDigits = newValue;
        decimalFormat.setMinimumFractionDigits(newValue);
    }

    /**
     * Returns true if null values are allowed.
     */
    public int getMinimumFractionDigits() {
        return minFractionDigits;
    }

    /**
     * Returns a String representation of the Object <code>value</code>. This invokes <code>format
     * </code> on the current <code>Format</code>.
     *
     * @throws ParseException
     * 		if there is an error in the conversion
     * @param value
     * 		Value to convert
     * @return String representation of value
     */
    @java.lang.Override
    public java.lang.String valueToString(java.lang.Object value) throws java.text.ParseException {
        if ((value == null) && allowsNullValue) {
            return "";
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        if (value instanceof java.lang.Double) {
            double v = ((java.lang.Double) (value));
            v = v * multiplier;
            java.lang.String str;
            java.math.BigDecimal big = new java.math.BigDecimal(v);
            int exponent = (big.scale() >= 0) ? big.precision() - big.scale() : -big.scale();
            if ((!usesScientificNotation) || ((exponent > minNegativeExponent) && (exponent < minPositiveExponent))) {
                str = decimalFormat.format(v);
            } else {
                str = scientificFormat.format(v);
            }
            buf.append(str);
        } else if (value instanceof java.lang.Float) {
            float v = ((java.lang.Float) (value));
            v = ((float) (v * multiplier));
            java.lang.String str;// = Float.toString(v);

            java.math.BigDecimal big = new java.math.BigDecimal(v);
            int exponent = (big.scale() >= 0) ? big.precision() - big.scale() : -big.scale();
            if ((!usesScientificNotation) || ((exponent > minNegativeExponent) && (exponent < minPositiveExponent))) {
                str = decimalFormat.format(v);
            } else {
                str = scientificFormat.format(v);
            }
            buf.append(str);
        } else if (value instanceof java.lang.Long) {
            long v = ((java.lang.Long) (value));
            v = ((long) (v * multiplier));
            buf.append(java.lang.Long.toString(v));
        } else if (value instanceof java.lang.Integer) {
            int v = ((java.lang.Integer) (value));
            v = ((int) (v * multiplier));
            buf.append(java.lang.Integer.toString(v));
        } else if (value instanceof java.lang.Byte) {
            byte v = ((java.lang.Byte) (value));
            v = ((byte) (v * multiplier));
            buf.append(java.lang.Byte.toString(v));
        } else if (value instanceof java.lang.Short) {
            short v = ((java.lang.Short) (value));
            v = ((short) (v * multiplier));
            buf.append(java.lang.Short.toString(v));
        }
        if (buf.length() != 0) {
            if (unit != null) {
                buf.append(unit);
            }
            return buf.toString();
        }
        throw new java.text.ParseException("Value is of unsupported class " + value, 0);
    }

    /**
     * Returns the <code>Object</code> representation of the <code>String</code> <code>text</code>.
     *
     * @param text
     * 		<code>String</code> to convert
     * @return <code>Object</code> representation of text
     * @throws ParseException
     * 		if there is an error in the conversion
     */
    @java.lang.Override
    public java.lang.Object stringToValue(java.lang.String text) throws java.text.ParseException {
        if (((text == null) || (text.length() == 0)) && getAllowsNullValue()) {
            return null;
        }
        // Remove unit from text
        if (unit != null) {
            int p = text.lastIndexOf(unit);
            if (p != (-1)) {
                text = text.substring(0, p);
            }
        }
        java.lang.Class<?> valueClass = getValueClass();
        java.lang.Object value;
        if (valueClass != null) {
            try {
                if (valueClass == java.lang.Integer.class) {
                    int v = java.lang.Integer.parseInt(text);
                    v = ((int) (v / multiplier));
                    value = v;
                } else if (valueClass == java.lang.Long.class) {
                    long v = java.lang.Long.parseLong(text);
                    v = ((long) (v / multiplier));
                    value = v;
                } else if (valueClass == java.lang.Float.class) {
                    float v = java.lang.Float.parseFloat(text);
                    v = ((float) (v / multiplier));
                    value = v;
                } else if (valueClass == java.lang.Double.class) {
                    double v = java.lang.Double.parseDouble(text);
                    v = v / multiplier;
                    value = v;
                } else if (valueClass == java.lang.Byte.class) {
                    byte v = java.lang.Byte.parseByte(text);
                    v = ((byte) (v / multiplier));
                    value = v;
                } else if (valueClass == java.lang.Short.class) {
                    short v = java.lang.Short.parseShort(text);
                    v = ((short) (v / multiplier));
                    value = v;
                } else {
                    throw new java.text.ParseException("Unsupported value class " + valueClass, 0);
                }
            } catch (java.lang.NumberFormatException e) {
                throw new java.text.ParseException(e.getMessage(), 0);
            }
        } else {
            throw new java.text.ParseException("Unsupported value class " + valueClass, 0);
        }
        try {
            if (!isValidValue(value, true)) {
                throw new java.text.ParseException("Value not within min/max range", 0);
            }
        } catch (java.lang.ClassCastException cce) {
            throw new java.text.ParseException("Class cast exception comparing values: " + cce, 0);
        }
        return value;
    }

    /**
     * Returns true if <code>value</code> is between the min/max.
     *
     * @param wantsCCE
     * 		If false, and a ClassCastException is thrown in comparing the values, the
     * 		exception is consumed and false is returned.
     */
    @java.lang.SuppressWarnings("unchecked")
    boolean isValidValue(java.lang.Object value, boolean wantsCCE) {
        try {
            if ((min != null) && (min.compareTo(((java.lang.Number) (value))) > 0)) {
                return false;
            }
        } catch (java.lang.ClassCastException cce) {
            if (wantsCCE) {
                throw cce;
            }
            return false;
        }
        try {
            if ((max != null) && (max.compareTo(((java.lang.Number) (value))) < 0)) {
                return false;
            }
        } catch (java.lang.ClassCastException cce) {
            if (wantsCCE) {
                throw cce;
            }
            return false;
        }
        return true;
    }

    /**
     * If non-null the unit string is appended to the value.
     */
    public void setUnit(java.lang.String value) {
        unit = value;
    }

    /**
     * If non-null the unit string is appended to the value.
     */
    public java.lang.String getUnit() {
        return unit;
    }

    /**
     * Gets the minimum number of digits allowed in the integer portion of a number.
     */
    public int getMinimumIntegerDigits() {
        return minIntDigits;
    }

    /**
     * Sets the minimum number of digits allowed in the integer portion of a number.
     */
    public void setMinimumIntegerDigits(int newValue) {
        decimalFormat.setMinimumIntegerDigits(newValue);
        scientificFormat.setMinimumIntegerDigits(newValue);
        this.minIntDigits = newValue;
    }

    /**
     * Gets the maximum number of digits allowed in the integer portion of a number.
     */
    public int getMaximumIntegerDigits() {
        return maxIntDigits;
    }

    /**
     * Sets the maximum number of digits allowed in the integer portion of a number.
     */
    public void setMaximumIntegerDigits(int newValue) {
        decimalFormat.setMaximumIntegerDigits(newValue);
        scientificFormat.setMaximumIntegerDigits(newValue);
        this.maxIntDigits = newValue;
    }

    /**
     * Gets the maximum number of digits allowed in the fraction portion of a number.
     */
    public int getMaximumFractionDigits() {
        return maxFractionDigits;
    }

    /**
     * Sets the maximum number of digits allowed in the fraction portion of a number.
     */
    public void setMaximumFractionDigits(int newValue) {
        decimalFormat.setMaximumFractionDigits(newValue);
        scientificFormat.setMaximumFractionDigits(newValue);
        this.maxFractionDigits = newValue;
    }

    /**
     * Gets the minimum negative exponent value for scientific notation.
     */
    public int getMinimumNegativeExponent() {
        return minNegativeExponent;
    }

    /**
     * Sets the minimum negative exponent value for scientific notation.
     */
    public void setMinimumNegativeExponent(int newValue) {
        this.minNegativeExponent = newValue;
    }

    /**
     * Gets the minimum positive exponent value for scientific notation.
     */
    public int getMinimumPositiveExponent() {
        return minPositiveExponent;
    }

    /**
     * Sets the minimum positive exponent value for scientific notation.
     */
    public void setMinimumPositiveExponent(int newValue) {
        this.minPositiveExponent = newValue;
    }

    /**
     * Returns true if scientific notation is used.
     */
    public boolean isUsesScientificNotation() {
        return usesScientificNotation;
    }

    /**
     * Sets whether scientific notation is used.
     */
    public void setUsesScientificNotation(boolean newValue) {
        this.usesScientificNotation = newValue;
    }

    /**
     * Convenience method for creating a formatter factory with a {@code ScalableNumberFormatter} and
     * a Java-style DecimalFormat. Doesn't allow null values and doesn't append ".0" to double and
     * float values.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory(double min, double max, double multiplier) {
        return org.jhotdraw.formatter.JavaNumberFormatter.createFormatterFactory(min, max, multiplier, false, null);
    }

    /**
     * Convenience method for creating a formatter factory with a {@code ScalableNumberFormatter} and
     * a Java-style DecimalFormat.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory(double min, double max, double multiplier, boolean allowsNullValue) {
        org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(min, max, multiplier, allowsNullValue, null);
        return new javax.swing.text.DefaultFormatterFactory(formatter);
    }

    /**
     * Convenience method for creating a formatter factory with a {@code ScalableNumberFormatter} and
     * a Java-style DecimalFormat.
     */
    public static javax.swing.JFormattedTextField.AbstractFormatterFactory createFormatterFactory(double min, double max, double multiplier, boolean allowsNullValue, java.lang.String unit) {
        org.jhotdraw.formatter.JavaNumberFormatter formatter = new org.jhotdraw.formatter.JavaNumberFormatter(min, max, multiplier, allowsNullValue, unit);
        return new javax.swing.text.DefaultFormatterFactory(formatter);
    }
}