/* @(#)AttributeKeys.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
import org.jhotdraw.draw.figure.Figure;
/**
 * Defines a put of well known {@link Figure} attributes.
 *
 * <p>If you are developing an applications that uses a different put or an extended put of
 * attributes, it is recommended to create a new AttributeKeys class, and to define all needed
 * AttributeKeys as static variables in there.
 */
public class AttributeKeys {
    private static final org.jhotdraw.util.ResourceBundleUtil LABELS = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");

    /**
     * Canvas fill color. The value of this attribute is a Color object. This attribute is used by a
     * Drawing object to specify the fill color of the drawing. The default value is white.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> CANVAS_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("canvasFillColor", java.awt.Color.class, java.awt.Color.white, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Canvas fill opacity. The value of this attribute is a Double object. This is a value between 0
     * and 1 whereas 0 is translucent and 1 is fully opaque.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> CANVAS_FILL_OPACITY = new org.jhotdraw.draw.AttributeKey<>("canvasFillOpacity", java.lang.Double.class, 1.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The width of the canvas. The value of this attribute is a Double object. This is a value
     * between 1 and Double.MAX_VALUE. If the value is null, the width is dynamically adapted to the
     * content of the drawing.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> CANVAS_WIDTH = new org.jhotdraw.draw.AttributeKey<>("canvasWidth", java.lang.Double.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The height of the canvas. The value of this attribute is a Double object. This is a value
     * between 1 and Double.MAX_VALUE. If the value is null, the height is dynamically adapted to the
     * content of the drawing.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> CANVAS_HEIGHT = new org.jhotdraw.draw.AttributeKey<>("canvasHeight", java.lang.Double.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Figure fill color. The value of this attribute is a Color object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("fillColor", java.awt.Color.class, java.awt.Color.white, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Whether to path a BezierFigure is closed. The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> PATH_CLOSED = new org.jhotdraw.draw.AttributeKey<>("pathClosed", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Whether an unclosed path of a BezierFigure is filled. The value of this attribute is a Boolean
     * object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> UNCLOSED_PATH_FILLED = new org.jhotdraw.draw.AttributeKey<>("unclosedPathFilled", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static enum WindingRule {

        /**
         * If WINDING_RULE is put to this value, an even-odd winding rule is used for determining the
         * interior of a path.
         */
        EVEN_ODD,
        /**
         * If WINDING_RULE is put to this value, a non-zero winding rule is used for determining the
         * interior of a path.
         */
        NON_ZERO;
    }

    /**
     * Fill under stroke. The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.WindingRule> WINDING_RULE = new org.jhotdraw.draw.AttributeKey<>("windingRule", org.jhotdraw.draw.AttributeKeys.WindingRule.class, org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static enum Underfill {

        /**
         * If FILL_UNDER_STROKE is put to this value, the area under the stroke will not be filled.
         */
        NONE,
        /**
         * If FILL_UNDER_STROKE is put to this value, the area under the stroke is filled to the center
         * of the stroke. This is the default behavior of Graphics2D.fill(Shape), Graphics2D.draw(Shape)
         * when using the same shape object.
         */
        CENTER,
        /**
         * If FILL_UNDER_STROKE is put to this value, the area under the stroke will be filled.
         */
        FULL;
    }

    /**
     * Fill under stroke. The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.Underfill> FILL_UNDER_STROKE = new org.jhotdraw.draw.AttributeKey<>("fillUnderStroke", org.jhotdraw.draw.AttributeKeys.Underfill.class, org.jhotdraw.draw.AttributeKeys.Underfill.CENTER, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Stroke color. The value of this attribute is a Color object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("strokeColor", java.awt.Color.class, java.awt.Color.black, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Stroke width. A double used to construct a BasicStroke or the outline of a DoubleStroke.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_WIDTH = new org.jhotdraw.draw.AttributeKey<>("strokeWidth", java.lang.Double.class, 1.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Factor for the stroke inner width. This is a double. The default value is 2.
     *
     * <p>FIXME - This is not flexible enough. Lets replace this with a
     * STROKE_STRIPES_ARRAY&lt;Double[]&gt; and a IS_STROKE_STRIPES_FACTOR.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_INNER_WIDTH_FACTOR = new org.jhotdraw.draw.AttributeKey<>("innerStrokeWidthFactor", java.lang.Double.class, 2.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Stroke join. One of the BasicStroke.JOIN_... values used to construct a BasicStroke.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Integer> STROKE_JOIN = new org.jhotdraw.draw.AttributeKey<>("strokeJoin", java.lang.Integer.class, java.awt.BasicStroke.JOIN_MITER, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Stroke join. One of the BasicStroke.CAP_... values used to construct a BasicStroke.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Integer> STROKE_CAP = new org.jhotdraw.draw.AttributeKey<>("strokeCap", java.lang.Integer.class, java.awt.BasicStroke.CAP_BUTT, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Stroke miter limit factor. A double multiplied by total stroke width, used to construct the
     * miter limit of a BasicStroke.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_MITER_LIMIT = new org.jhotdraw.draw.AttributeKey<>("strokeMiterLimitFactor", java.lang.Double.class, 3.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * A boolean used to indicate whether STROKE_MITER_LIMIT is a factor of STROKE_WIDTH, or whether
     * it represents an absolute value.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> IS_STROKE_MITER_LIMIT_FACTOR = new org.jhotdraw.draw.AttributeKey<>("isStrokeMiterLimitFactor", java.lang.Boolean.class, true, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * An array of doubles used to specify the dash pattern in a BasicStroke;
     */
    public static final org.jhotdraw.draw.AttributeKey<double[]> STROKE_DASHES = new org.jhotdraw.draw.AttributeKey<>("strokeDashes", double[].class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * A double used to specify the starting phase of the stroke dashes.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> STROKE_DASH_PHASE = new org.jhotdraw.draw.AttributeKey<>("strokeDashPhase", java.lang.Double.class, 0.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * A boolean used to indicate whether STROKE_DASHES and STROKE_DASH_PHASE shall be interpreted as
     * factors of STROKE_WIDTH, or whether they are absolute values.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> IS_STROKE_DASH_FACTOR = new org.jhotdraw.draw.AttributeKey<>("isStrokeDashFactor", java.lang.Boolean.class, true, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Are stroke values pixel values or should they be transformed as well.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> IS_STROKE_PIXEL_VALUE = new org.jhotdraw.draw.AttributeKey<>("isStrokePixelValue", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static enum StrokeType {

        /**
         * If STROKE_TYPE is put to this value, a BasicStroke instance is used for stroking.
         */
        BASIC,
        /**
         * If STROKE_TYPE is put to this value, a DoubleStroke instance is used for stroking.
         */
        DOUBLE;
    }

    /**
     * Stroke type. The value of this attribute is either VALUE_STROKE_TYPE_BASIC or
     * VALUE_STROKE_TYPE_DOUBLE. FIXME - Type should be an enumeration.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.StrokeType> STROKE_TYPE = new org.jhotdraw.draw.AttributeKey<>("strokeType", org.jhotdraw.draw.AttributeKeys.StrokeType.class, org.jhotdraw.draw.AttributeKeys.StrokeType.BASIC, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static enum StrokePlacement {

        /**
         * If STROKE_PLACEMENT is put to this value, the stroke is centered on the path.
         */
        CENTER,
        /**
         * If STROKE_PLACEMENT is put to this value, the stroke is placed inside of a closed path.
         */
        INSIDE,
        /**
         * If STROKE_PLACEMENT is put to this value, the stroke is placed outside of a closed path.
         */
        OUTSIDE;
    }

    /**
     * Stroke placement. The value is either StrokePlacement.CENTER, StrokePlacement.INSIDE or
     * StrokePlacement.OUTSIDE. This only has effect for closed paths. On open paths, the stroke is
     * always centered on the path.
     *
     * <p>The default value is StrokePlacement.CENTER.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.StrokePlacement> STROKE_PLACEMENT = new org.jhotdraw.draw.AttributeKey<>("strokePlacement", org.jhotdraw.draw.AttributeKeys.StrokePlacement.class, org.jhotdraw.draw.AttributeKeys.StrokePlacement.CENTER, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a String object, which is used to display the text of the
     * figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.String> TEXT = new org.jhotdraw.draw.AttributeKey<>("text", java.lang.String.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Text color. The value of this attribute is a Color object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TEXT_COLOR = new org.jhotdraw.draw.AttributeKey<>("textColor", java.awt.Color.class, java.awt.Color.BLACK, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Text shadow color. The value of this attribute is a Color object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TEXT_SHADOW_COLOR = new org.jhotdraw.draw.AttributeKey<>("textShadowColor", java.awt.Color.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Text shadow offset. The value of this attribute is a Dimension2DDouble object.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.geom.Dimension2DDouble> TEXT_SHADOW_OFFSET = new org.jhotdraw.draw.AttributeKey<>("textShadowOffset", org.jhotdraw.geom.Dimension2DDouble.class, new org.jhotdraw.geom.Dimension2DDouble(1.0, 1.0), false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static enum Alignment {

        /**
         * align on the left or the top
         */
        LEADING,
        /**
         * align on the right or the bottom
         */
        TRAILING,
        /**
         * align in the center
         */
        CENTER,
        /**
         * stretch to fill horizontally, or vertically
         */
        BLOCK;
    }

    /**
     * Text alignment. The value of this attribute is a Alignment enum.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.Alignment> TEXT_ALIGNMENT = new org.jhotdraw.draw.AttributeKey<>("textAlignment", org.jhotdraw.draw.AttributeKeys.Alignment.class, org.jhotdraw.draw.AttributeKeys.Alignment.LEADING, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Font object, which is used as a prototype to create the font
     * for the text.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Font> FONT_FACE = new org.jhotdraw.draw.AttributeKey<>("fontFace", java.awt.Font.class, new java.awt.Font("VERDANA", java.awt.Font.PLAIN, 10), false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a double object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> FONT_SIZE = new org.jhotdraw.draw.AttributeKey<>("fontSize", java.lang.Double.class, 12.0, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> FONT_BOLD = new org.jhotdraw.draw.AttributeKey<>("fontBold", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> FONT_ITALIC = new org.jhotdraw.draw.AttributeKey<>("fontItalic", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Boolean object.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Boolean> FONT_UNDERLINE = new org.jhotdraw.draw.AttributeKey<>("fontUnderline", java.lang.Boolean.class, false, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Liner object.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.liner.Liner> BEZIER_PATH_LAYOUTER = new org.jhotdraw.draw.AttributeKey<>("bezierPathLayouter", org.jhotdraw.draw.liner.Liner.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.decoration.LineDecoration> END_DECORATION = new org.jhotdraw.draw.AttributeKey<>("endDecoration", org.jhotdraw.draw.decoration.LineDecoration.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.decoration.LineDecoration> START_DECORATION = new org.jhotdraw.draw.AttributeKey<>("startDecoration", org.jhotdraw.draw.decoration.LineDecoration.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Insets2D.Double object.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.geom.Insets2D.Double> DECORATOR_INSETS = new org.jhotdraw.draw.AttributeKey<>("decoratorInsets", org.jhotdraw.geom.Insets2D.Double.class, new org.jhotdraw.geom.Insets2D.Double(), false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Insets2D.Double object.
     *
     * <p>This attribute can be put on a CompositeFigure, which uses a Layouter to lay out its
     * children.
     *
     * <p>The insets are used to determine the insets between the bounds of the CompositeFigure and
     * its children.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.geom.Insets2D.Double> LAYOUT_INSETS = new org.jhotdraw.draw.AttributeKey<>("borderInsets", org.jhotdraw.geom.Insets2D.Double.class, new org.jhotdraw.geom.Insets2D.Double(), false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Alignment object.
     *
     * <p>This attribute can be put on a CompositeFigure, which uses a Layouter to lay out its
     * children.
     *
     * <p>The insets are used to determine the default alignment of the children of the
     * CompositeFigure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.Alignment> COMPOSITE_ALIGNMENT = new org.jhotdraw.draw.AttributeKey<>("layoutAlignment", org.jhotdraw.draw.AttributeKeys.Alignment.class, org.jhotdraw.draw.AttributeKeys.Alignment.BLOCK, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * The value of this attribute is a Alignment object.
     *
     * <p>This attribute can be put on a child of a CompositeFigure, which uses a Layouter to lay out
     * its children.
     *
     * <p>Layouters should use this attribute, to determine the default alignment of the child figures
     * contained in the CompositeFigure which they lay out.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.Alignment> CHILD_ALIGNMENT = new org.jhotdraw.draw.AttributeKey<>("layoutAlignment", org.jhotdraw.draw.AttributeKeys.Alignment.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * Specifies the transform of a Figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.geom.AffineTransform> TRANSFORM = new org.jhotdraw.draw.AttributeKey<>("transform", java.awt.geom.AffineTransform.class, null, true, org.jhotdraw.draw.AttributeKeys.LABELS);

    /**
     * For point objects the origin is somewhere within the boundary. With this 0 to 1 number you are
     * able to move the origin and therefore the position of the figure relative amount of the
     * boundary x size.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> ALIGN_RELATIVE_X = new org.jhotdraw.draw.AttributeKey<>("alignRelativeX", java.lang.Double.class, 0.0, false);

    /**
     * For point objects the origin is somewhere within the boundary. With this 0 to 1 number you are
     * able to move the origin and therefore the position of the figure relative amount of the
     * boundary y size.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Double> ALIGN_RELATIVE_Y = new org.jhotdraw.draw.AttributeKey<>("alignRelativeY", java.lang.Double.class, 0.0, false);

    public static enum Orientation {

        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST;
    }

    /**
     * Specifies the orientation of a Figure.
     */
    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.Orientation> ORIENTATION = new org.jhotdraw.draw.AttributeKey<>("orientation", org.jhotdraw.draw.AttributeKeys.Orientation.class, org.jhotdraw.draw.AttributeKeys.Orientation.NORTH, false, org.jhotdraw.draw.AttributeKeys.LABELS);

    public static final org.jhotdraw.draw.AttributeKey<org.jhotdraw.draw.AttributeKeys.ScaleProvider> SCALE_PROVIDER = new org.jhotdraw.draw.AttributeKey<>("scaleProvider", org.jhotdraw.draw.AttributeKeys.ScaleProvider.class, org.jhotdraw.draw.AttributeKeys.ScaleProvider.from(() -> 1.0));

    /**
     * Helper class due to Java is not able to infere type into AttributeKey.
     */
    public static class ScaleProvider {
        private final java.util.function.Supplier<java.lang.Double> scaleProvider;

        public ScaleProvider(java.util.function.Supplier<java.lang.Double> scaleProvider) {
            java.util.Objects.requireNonNull(scaleProvider);
            this.scaleProvider = scaleProvider;
        }

        public java.lang.Double scale() {
            return scaleProvider.get();
        }

        public static final org.jhotdraw.draw.AttributeKeys.ScaleProvider from(java.util.function.Supplier<java.lang.Double> scaleProvider) {
            return new org.jhotdraw.draw.AttributeKeys.ScaleProvider(scaleProvider);
        }
    }

    /**
     * A put with all attributes defined by this class.
     */
    public static final java.util.Set<org.jhotdraw.draw.AttributeKey<?>> SUPPORTED_ATTRIBUTES;

    public static final java.util.Map<java.lang.String, org.jhotdraw.draw.AttributeKey<?>> SUPPORTED_ATTRIBUTES_MAP;

    static {
        java.util.HashSet<org.jhotdraw.draw.AttributeKey<?>> as = new java.util.HashSet<>();
        as.addAll(java.util.Arrays.asList(new org.jhotdraw.draw.AttributeKey<?>[]{ FILL_COLOR, FILL_UNDER_STROKE, STROKE_COLOR, STROKE_WIDTH, STROKE_INNER_WIDTH_FACTOR, STROKE_JOIN, STROKE_CAP, STROKE_MITER_LIMIT, STROKE_DASHES, STROKE_DASH_PHASE, STROKE_TYPE, STROKE_PLACEMENT, TEXT, TEXT_COLOR, TEXT_SHADOW_COLOR, TEXT_SHADOW_OFFSET, TRANSFORM, FONT_FACE, FONT_SIZE, FONT_BOLD, FONT_ITALIC, FONT_UNDERLINE, BEZIER_PATH_LAYOUTER, END_DECORATION, START_DECORATION, DECORATOR_INSETS, ORIENTATION, WINDING_RULE, IS_STROKE_PIXEL_VALUE, ALIGN_RELATIVE_X, ALIGN_RELATIVE_Y }));
        SUPPORTED_ATTRIBUTES = java.util.Collections.unmodifiableSet(as);
        java.util.HashMap<java.lang.String, org.jhotdraw.draw.AttributeKey<?>> am = new java.util.HashMap<>();
        for (org.jhotdraw.draw.AttributeKey<?> a : as) {
            am.put(a.getKey(), a);
        }
        // XXX Redundant cast needed, becaues Collections.unmodifiableMap loses the <?>
        @java.lang.SuppressWarnings("cast")
        java.util.Map<java.lang.String, org.jhotdraw.draw.AttributeKey<?>> sam = ((java.util.Map<java.lang.String, org.jhotdraw.draw.AttributeKey<?>>) (java.util.Collections.unmodifiableMap(am)));
        SUPPORTED_ATTRIBUTES_MAP = sam;
    }

    public static double scaleFromContext(org.jhotdraw.draw.Drawing drawing) {
        return drawing.attr().get(org.jhotdraw.draw.AttributeKeys.SCALE_PROVIDER).scale();
    }

    /**
     * Scaling from attributes or drawings attributes.
     */
    public static double scaleFromContext(org.jhotdraw.draw.figure.Figure f) {
        if (f instanceof org.jhotdraw.draw.figure.AbstractAttributedFigure attributedFigure) {
            // the figures attributes are not checked by design
            return java.util.Optional.ofNullable(attributedFigure.getDrawing()).map(myDrawing -> myDrawing.attr().get(org.jhotdraw.draw.AttributeKeys.SCALE_PROVIDER).scale()).orElse(1.0);
        }
        return 1.0;
    }

    /**
     * Computing a global scale factor derived from pixel with or different measures.
     */
    public static double getGlobalValueFactor(org.jhotdraw.draw.figure.Figure f, double factor) {
        if (f.attr().get(org.jhotdraw.draw.AttributeKeys.IS_STROKE_PIXEL_VALUE)) {
            if ((factor == 1.0) || (factor == 0.0)) {
                factor = org.jhotdraw.draw.AttributeKeys.scaleFromContext(f);
            }
            return factor != 0.0 ? factor : 1.0;
        }
        return 1.0;
    }

    /**
     * Returns a scale factor derived from a Graphics2D context.
     *
     * @param g
     * @return  */
    public static double getScaleFactorFromGraphics(java.awt.Graphics2D g) {
        return org.jhotdraw.draw.AttributeKeys.getScaleFactor(g.getTransform());
    }

    /**
     * Returns a scale factor derived from a AffineTransform.
     *
     * @param transform
     * @return  */
    public static double getScaleFactor(java.awt.geom.AffineTransform transform) {
        if (transform == null) {
            return 1.0;
        }
        double scale = 0.0;
        double sx = transform.getScaleX();
        double shx = transform.getShearX();
        if ((sx != 0) || (shx != 0)) {
            scale = java.lang.Math.sqrt((sx * sx) + (shx * shx));
        }
        return scale != 0 ? scale : 1.0;
    }

    /**
     * Convenience method for computing the total stroke width from the STROKE_WIDTH,
     * STROKE_INNER_WIDTH and STROKE_TYPE attributes.
     */
    public static double getStrokeTotalWidth(org.jhotdraw.draw.figure.Figure f, double factor) {
        switch (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE)) {
            case BASIC :
            default :
                return f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor);
            case DOUBLE :
                return (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) * (1.0 + f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR))) / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor);
        }
    }

    /**
     * Convenience method for computing the total stroke miter limit from the STROKE_MITER_LIMIT, and
     * IS_STROKE_MITER_LIMIT factor.
     */
    public static double getStrokeTotalMiterLimit(org.jhotdraw.draw.figure.Figure f, double factor) {
        if (f.attr().get(org.jhotdraw.draw.AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR)) {
            return (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT) * f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH)) / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor);
        } else {
            return f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
        }
    }

    public static java.awt.Stroke getStroke(org.jhotdraw.draw.figure.Figure f, double factor) {
        double strokeWidth = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) / org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor);
        float miterLimit = ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalMiterLimit(f, factor)));
        double dashFactor = (f.attr().get(org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR)) ? strokeWidth : 1.0;
        double dashPhase = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE);
        double[] ddashes = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_DASHES);
        float[] dashes = null;
        boolean isAllZeroes = true;
        if (ddashes != null) {
            dashes = new float[ddashes.length];
            double dashSize = 0.0F;
            for (int i = 0; i < dashes.length; i++) {
                dashes[i] = java.lang.Math.max(0.0F, ((float) (ddashes[i] * dashFactor)));
                dashSize += dashes[i];
                if (isAllZeroes && (dashes[i] != 0)) {
                    isAllZeroes = false;
                }
            }
            if ((dashes.length % 2) == 1) {
                dashSize *= 2;
            }
            if (dashPhase < 0) {
                dashPhase = dashSize + (dashPhase % dashSize);
            }
        }
        if (isAllZeroes) {
            // don't draw dashes, if all values are 0.
            dashes = null;
        }
        switch (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE)) {
            case BASIC :
            default :
                return new java.awt.BasicStroke(((float) (strokeWidth)), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN), java.lang.Math.max(1, miterLimit), dashes, java.lang.Math.max(0, ((float) (dashPhase * dashFactor))));
                // not reached
            case DOUBLE :
                return new org.jhotdraw.geom.DoubleStroke(((float) (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR) * strokeWidth)), ((float) (strokeWidth)), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN), java.lang.Math.max(1, miterLimit), dashes, java.lang.Math.max(0, ((float) (dashPhase * dashFactor))));
                // not reached
        }
    }

    /**
     * Returns a stroke which is useful for hit-testing. The stroke reflects the stroke width, but not
     * the stroke dashes attribute.
     *
     * @param f
     * @return A stroke suited for creating a shape for hit testing.
     */
    public static java.awt.Stroke getHitStroke(org.jhotdraw.draw.figure.Figure f, double factor) {
        double strokeWidth = java.lang.Math.max(1, f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) * org.jhotdraw.draw.AttributeKeys.getGlobalValueFactor(f, factor));
        float miterLimit = ((float) (org.jhotdraw.draw.AttributeKeys.getStrokeTotalMiterLimit(f, factor)));
        double dashFactor = (f.attr().get(org.jhotdraw.draw.AttributeKeys.IS_STROKE_DASH_FACTOR)) ? strokeWidth : 1.0;
        switch (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE)) {
            case BASIC :
            default :
                return new java.awt.BasicStroke(((float) (strokeWidth)), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN), miterLimit, null, java.lang.Math.max(0, ((float) (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE) * dashFactor))));
                // not reached
            case DOUBLE :
                return new org.jhotdraw.geom.DoubleStroke(((float) (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR) * strokeWidth)), ((float) (strokeWidth)), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP), f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN), miterLimit, null, java.lang.Math.max(0, ((float) (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_DASH_PHASE).floatValue() * dashFactor))));
                // not reached
        }
    }

    public static java.awt.Font getFont(org.jhotdraw.draw.figure.Figure f) {
        java.awt.Font prototype = f.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_FACE);
        if (prototype == null) {
            return null;
        }
        if (org.jhotdraw.draw.AttributeKeys.getFontStyle(f) != java.awt.Font.PLAIN) {
            return prototype.deriveFont(org.jhotdraw.draw.AttributeKeys.getFontStyle(f), f.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE).floatValue());
        } else {
            return prototype.deriveFont(f.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE).floatValue());
        }
    }

    public static int getFontStyle(org.jhotdraw.draw.figure.Figure f) {
        int style = java.awt.Font.PLAIN;
        if (f.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_BOLD)) {
            style |= java.awt.Font.BOLD;
        }
        if (f.attr().get(org.jhotdraw.draw.AttributeKeys.FONT_ITALIC)) {
            style |= java.awt.Font.ITALIC;
        }
        return style;
    }

    /**
     * Returns the distance, that a Rectangle needs to grow (or shrink) to fill its shape as specified
     * by the FILL_UNDER_STROKE and STROKE_POSITION attributes of a figure. The value returned is the
     * number of units that need to be grown (or shrunk) perpendicular to a stroke on an outline of
     * the shape.
     */
    public static double getPerpendicularFillGrowth(org.jhotdraw.draw.figure.Figure f, double factor) {
        double grow;
        double strokeWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(f, factor);
        org.jhotdraw.draw.AttributeKeys.StrokePlacement placement = f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT);
        switch (f.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE)) {
            case FULL :
                switch (placement) {
                    case INSIDE :
                        grow = 0.0F;
                        break;
                    case OUTSIDE :
                        grow = strokeWidth;
                        break;
                    case CENTER :
                    default :
                        grow = strokeWidth / 2.0;
                        break;
                }
                break;
            case NONE :
                switch (placement) {
                    case INSIDE :
                        grow = -strokeWidth;
                        break;
                    case OUTSIDE :
                        grow = 0.0F;
                        break;
                    case CENTER :
                    default :
                        grow = strokeWidth / (-2.0);
                        break;
                }
                break;
            case CENTER :
            default :
                switch (placement) {
                    case INSIDE :
                        grow = strokeWidth / (-2.0);
                        break;
                    case OUTSIDE :
                        grow = strokeWidth / 2.0;
                        break;
                    case CENTER :
                    default :
                        grow = 0.0;
                        break;
                }
                break;
        }
        return grow;
    }

    /**
     * Returns the distance, that a Rectangle needs to grow (or shrink) to draw (aka stroke) its shape
     * as specified by the FILL_UNDER_STROKE and STROKE_POSITION attributes of a figure. The value
     * returned is the number of units that need to be grown (or shrunk) perpendicular to a stroke on
     * an outline of the shape.
     */
    public static double getPerpendicularDrawGrowth(org.jhotdraw.draw.figure.Figure f, double factor) {
        double grow;
        double strokeWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(f, factor);
        switch (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT)) {
            case INSIDE :
                grow = strokeWidth / (-2.0);
                break;
            case OUTSIDE :
                grow = strokeWidth / 2.0;
                break;
            case CENTER :
            default :
                grow = 0.0F;
                break;
        }
        return grow;
    }

    /**
     * Returns the distance, that a Rectangle needs to grow (or shrink) to make hit detections on a
     * shape as specified by the FILL_UNDER_STROKE and STROKE_POSITION attributes of a figure. The
     * value returned is the number of units that need to be grown (or shrunk) perpendicular to a
     * stroke on an outline of the shape.
     */
    public static double getPerpendicularHitGrowth(org.jhotdraw.draw.figure.Figure f, double scale) {
        double grow;
        if (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) == null) {
            grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularFillGrowth(f, scale);
        } else {
            double strokeWidth = org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(f, scale);
            double width = strokeWidth / 2;
            if (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN) == java.awt.BasicStroke.JOIN_MITER) {
                width *= f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT);
            } else if (f.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_CAP) != java.awt.BasicStroke.CAP_BUTT) {
                width += strokeWidth * 2;
            }
            width++;
            grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(f, scale) + width;
        }
        return grow;
    }
}