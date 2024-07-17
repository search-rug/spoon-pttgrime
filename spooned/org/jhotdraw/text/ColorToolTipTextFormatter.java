/* @(#)ColorFormatter.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.text;
import java.awt.Color;
import java.awt.color.ColorSpace;
/**
 * {@code ColorFormatter} is used to format colors into a textual representation which can be
 * displayed as a tooltip.
 *
 * <p>By default, the formatter is adaptive, meaning that the format depends on the {@code ColorSpace} of the current {@code Color} value.
 *
 * <p>
 *
 * @author Werner Randelshofer
 * @version $Id: ColorFormatter.java 632 2010-01-21 16:06:59Z rawcoder $
 */
public class ColorToolTipTextFormatter extends org.jhotdraw.text.ColorFormatter {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    public ColorToolTipTextFormatter() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
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
                if (c.getColorSpace().equals(org.jhotdraw.color.HSBColorSpace.getInstance())) {
                    f = org.jhotdraw.text.ColorFormatter.Format.HSB_PERCENTAGE;
                } else if (c.getColorSpace().equals(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY))) {
                    f = org.jhotdraw.text.ColorFormatter.Format.GRAY_PERCENTAGE;
                } else {
                    f = org.jhotdraw.text.ColorFormatter.Format.RGB_INTEGER;
                }
            }
            switch (f) {
                case RGB_HEX :
                    str = "000000" + java.lang.Integer.toHexString(c.getRGB() & 0xffffff);
                    str = labels.getFormatted("attribute.color.rgbHexComponents.toolTipText", str.substring(str.length() - 6));
                    break;
                case RGB_INTEGER :
                    str = labels.getFormatted("attribute.color.rgbComponents.toolTipText", numberFormat.format(c.getRed()), numberFormat.format(c.getGreen()), numberFormat.format(c.getBlue()));
                    break;
                case RGB_PERCENTAGE :
                    str = labels.getFormatted("attribute.color.rgbPercentageComponents.toolTipText", numberFormat.format(c.getRed() / 255.0F), numberFormat.format(c.getGreen() / 255.0F), numberFormat.format(c.getBlue() / 255.0F));
                    break;
                case HSB_PERCENTAGE :
                    float[] components;
                    if (c.getColorSpace().equals(org.jhotdraw.color.HSBColorSpace.getInstance())) {
                        components = c.getComponents(null);
                    } else {
                        components = java.awt.Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), new float[3]);
                    }
                    str = labels.getFormatted("attribute.color.hsbComponents.toolTipText", numberFormat.format(components[0] * 360), numberFormat.format(components[1] * 100), numberFormat.format(components[2] * 100));
                    break;
                case GRAY_PERCENTAGE :
                    if (c.getColorSpace().equals(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY))) {
                        components = c.getComponents(null);
                    } else {
                        components = c.getColorComponents(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY), null);
                    }
                    str = labels.getFormatted("attribute.color.grayComponents.toolTipText", numberFormat.format(components[0] * 100));
                    break;
            }
        }
        return str;
    }
}