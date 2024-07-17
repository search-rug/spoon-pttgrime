/* @(#)SVGAttributeKeys.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * SVGAttributeKeys.
 */
public class SVGAttributeKeys extends org.jhotdraw.draw.AttributeKeys {
    private static final org.jhotdraw.util.ResourceBundleUtil LABELS = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

    public enum TextAnchor {

        START,
        MIDDLE,
        END;
    }

    /**
     * Specifies the title of an SVG drawing. This attribute can be null, to indicate that the drawing
     * has no title.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> TITLE = new org.jhotdraw.draw.AttributeKey<java.lang.String>("title", java.lang.String.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the description of an SVG drawing. This attribute can be null, to indicate that the
     * drawing has no description.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> DESCRIPTION = new org.jhotdraw.draw.AttributeKey<java.lang.String>("description", java.lang.String.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the viewport-fill of an SVG viewport. This attribute can be null, to indicate that
     * the viewport has no viewport-fill.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> VIEWPORT_FILL = org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR;

    /**
     * Specifies the viewport-fill-opacity of an SVG viewport.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> VIEWPORT_FILL_OPACITY = org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_OPACITY;

    /**
     * Specifies the width of an SVG viewport.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> VIEWPORT_WIDTH = org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH;

    /**
     * Specifies the height of an SVG viewport.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> VIEWPORT_HEIGHT = org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT;

    /**
     * Specifies the text anchor of a SVGText figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor> TEXT_ANCHOR = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor>("textAnchor", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.class, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.START, false, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    public enum TextAlign {

        START,
        CENTER,
        END;
    }

    /**
     * Specifies the text alignment of a SVGText figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign> TEXT_ALIGN = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign>("textAlign", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign.class, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign.START, false, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the fill gradient of a SVG figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.Gradient> FILL_GRADIENT = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.Gradient>("fillGradient", org.jhotdraw.samples.svg.Gradient.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the fill opacity of a SVG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> FILL_OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("fillOpacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the overall opacity of a SVG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("opacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the stroke gradient of a SVG figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.Gradient> STROKE_GRADIENT = new org.jhotdraw.draw.AttributeKey<org.jhotdraw.samples.svg.Gradient>("strokeGradient", org.jhotdraw.samples.svg.Gradient.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies the stroke opacity of a SVG figure. This is a value between 0 and 1 whereas 0 is
     * translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_OPACITY = new org.jhotdraw.draw.AttributeKey<java.lang.Double>("strokeOpacity", java.lang.Double.class, 1.0, false, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies a link. In an SVG file, the link is stored in a "a" element which encloses the
     * figure. http://www.w3.org/TR/SVGMobile12/linking.html#AElement
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> LINK = new org.jhotdraw.draw.AttributeKey<java.lang.String>("link", java.lang.String.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Specifies a link target. In an SVG file, the link is stored in a "a" element which encloses the
     * figure. http://www.w3.org/TR/SVGMobile12/linking.html#AElement
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> LINK_TARGET = new org.jhotdraw.draw.AttributeKey<java.lang.String>("linkTarget", java.lang.String.class, null, true, org.jhotdraw.samples.svg.SVGAttributeKeys.LABELS);

    /**
     * Gets the fill paint for the specified figure based on the attributes FILL_GRADIENT,
     * FILL_OPACITY, FILL_PAINT and the bounds of the figure. Returns null if the figure is not
     * filled.
     */
    public static java.awt.Paint getFillPaint(org.jhotdraw.draw.figure.Figure f) {
        double opacity = f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_OPACITY);
        if (f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null) {
            return f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT).getPaint(f, opacity);
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
        double opacity = f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_OPACITY);
        if (f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) != null) {
            return f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT).getPaint(f, opacity);
        }
        java.awt.Color color = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR);
        if (color != null) {
            if (opacity != 1) {
                color = new java.awt.Color((color.getRGB() & 0xffffff) | (((int) (opacity * 255)) << 24), true);
            }
        }
        return color;
    }

    /**
     * Sets SVG default values.
     */
    public static void setDefaults(org.jhotdraw.draw.figure.Figure f) {
        // Fill properties
        // http://www.w3.org/TR/SVGMobile12/painting.html#FillProperties
        f.attr().set(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, java.awt.Color.black);
        f.attr().set(org.jhotdraw.draw.AttributeKeys.WINDING_RULE, org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO);
        // Stroke properties
        // http://www.w3.org/TR/SVGMobile12/painting.html#StrokeProperties
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

    /**
     * Returns the distance, that a Rectangle needs to grow (or shrink) to make hit detections on a
     * shape as specified by the FILL_UNDER_STROKE and STROKE_POSITION attributes of a figure. The
     * value returned is the number of units that need to be grown (or shrunk) perpendicular to a
     * stroke on an outline of the shape.
     */
    public static double getPerpendicularHitGrowth(org.jhotdraw.draw.figure.Figure f, double factor) {
        double grow;
        if ((f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) == null) && (f.attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) == null)) {
            grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(f, factor);
        } else {
            double strokeWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(f, factor);
            grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(f, factor) + (strokeWidth / 2.0);
        }
        return grow;
    }
}