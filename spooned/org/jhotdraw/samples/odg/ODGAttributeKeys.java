/* @(#)ODGAttributeKeys.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
/**
 * ODGAttributeKeys.
 *
 * <p>The descriptions of the attributes have been taken from the Open Document Specification
 * version 1.1. <a href="http://docs.oasis-open.org/office/v1.1/OS/OpenDocument-v1.1.pdf">
 * http://docs.oasis-open.org/office/v1.1/OS/OpenDocument-v1.1.pdf</a>
 */
public class ODGAttributeKeys extends org.jhotdraw.draw.AttributeKeys {
    private static final org.jhotdraw.util.ResourceBundleUtil LABELS = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

    /**
     * Prevent instance creation
     */
    private ODGAttributeKeys() {
    }

    /**
     * The attribute draw:name assigns a name to the drawing shape.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> NAME = new org.jhotdraw.draw.AttributeKey<java.lang.String>("name", java.lang.String.class, null, true, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the overall opacity of a ODG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("opacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the fill style of a ODG figure.
     *
     * <p>The attribute draw:fill specifies the fill style for a graphic object. Graphic objects that
     * are not closed, such as a path without a closepath at the end, will not be filled. The fill
     * operation does not automatically close all open subpaths by connecting the last point of the
     * subpath with the first point of the subpath before painting the fill. The attribute has the
     * following values: • none: the drawing object is not filled. • solid: the drawing object is
     * filled with color specified by the draw:fill-color attribute. • bitmap: the drawing object is
     * filled with the bitmap specified by the draw:fill-image- name attribute. • gradient: the
     * drawing object is filled with the gradient specified by the draw:fill- gradient-name attribute.
     * • hatch: the drawing object is filled with the hatch specified by the draw:fill-hatch-name
     * attribute.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.ODGConstants.FillStyle> FILL_STYLE = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.ODGConstants.FillStyle>("fill", org.jhotdraw.samples.odg.ODGConstants.FillStyle.class, org.jhotdraw.samples.odg.ODGConstants.FillStyle.SOLID, false, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the fill gradient of a ODG figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.Gradient> FILL_GRADIENT = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.Gradient>("fillGradient", org.jhotdraw.samples.odg.Gradient.class, null, true, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the fill opacity of a ODG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> FILL_OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("fillOpacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the stroke style of a ODG figure.
     *
     * <p>The attribute draw:stroke specifies the style of the stroke on the current object. The value
     * none means that no stroke is drawn, and the value solid means that a solid stroke is drawn. If
     * the value is dash, the stroke referenced by the draw:stroke-dash property is drawn.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.ODGConstants.StrokeStyle> STROKE_STYLE = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.ODGConstants.StrokeStyle>("stroke", org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.class, org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.SOLID, false, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the stroke gradient of a ODG figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.Gradient> STROKE_GRADIENT = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.odg.Gradient>("strokeGradient", org.jhotdraw.samples.odg.Gradient.class, null, true, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Specifies the stroke opacity of a ODG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("strokeOpacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.odg.ODGAttributeKeys.LABELS);

    /**
     * Gets the fill paint for the specified figure based on the attributes FILL_GRADIENT,
     * FILL_OPACITY, FILL_PAINT and the bounds of the figure. Returns null if the figure is not
     * filled.
     */
    public static java.awt.Paint getFillPaint(org.jhotdraw.draw.figure.Figure f) {
        double opacity = f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_OPACITY);
        if (f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT) != null) {
            return f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT).getPaint(f, opacity);
        }
        java.awt.Color color = f.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
        if (color != null) {
            if (opacity != 1) {
                color = new java.awt.Color((color.getRGB() & 0xffffff) | (((int) (opacity * 255)) << 24), true);
            }
        }
        return color;
    }

    /**
     * Gets the stroke paint for the specified figure based on the attributes STROKE_GRADIENT,
     * STROKE_OPACITY, STROKE_PAINT and the bounds of the figure. Returns null if the figure is not
     * filled.
     */
    public static java.awt.Paint getStrokePaint(org.jhotdraw.draw.figure.Figure f) {
        double opacity = f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_OPACITY);
        if (f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT) != null) {
            return f.attr().get(org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT).getPaint(f, opacity);
        }
        java.awt.Color color = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR);
        if (color != null) {
            if (opacity != 1) {
                color = new java.awt.Color((color.getRGB() & 0xffffff) | (((int) (opacity * 255)) << 24), true);
            }
        }
        return color;
    }

    public static java.awt.Stroke getStroke(org.jhotdraw.draw.figure.Figure f) {
        double strokeWidth = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH);
        if (strokeWidth == 0) {
            strokeWidth = 1;
        }
        return new java.awt.BasicStroke(((float) (strokeWidth)));
    }

    /**
     * Sets ODG default values.
     */
    public static void setDefaults(org.jhotdraw.draw.figure.Figure f) {
        // Fill properties
        f.attr().set(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.black);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.WINDING_RULE, org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO);
        // Stroke properties
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, 1.0);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_CAP, java.awt.BasicStroke.CAP_BUTT);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN, java.awt.BasicStroke.JOIN_MITER);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT, 4.0);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR, false);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_DASHES, null);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE, 0.0);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR, false);
    }
}