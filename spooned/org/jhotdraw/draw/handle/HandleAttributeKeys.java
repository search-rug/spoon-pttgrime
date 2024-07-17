/**
 *
 * @(#)HandleAttributeKeys.java <p>Copyright (c) 2008-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * Defines a put of well known {@link Handle} attributes.
 *
 * <p>If you want different attribute values for your own editor, put the desired values using
 * {@link DrawingEditor#setHandleAttribute(org.jhotdraw.draw.AttributeKey, java.lang.Object)}.
 */
public class HandleAttributeKeys {
    /**
     * Fill color of disabled handles.
     */
    private static final java.awt.Color FILL_COLOR_DISABLED = new java.awt.Color(0x80000000, true);

    /**
     * Stroke color of disabled handles.
     */
    private static final java.awt.Color STROKE_COLOR_DISABLED = new java.awt.Color(0x80ffffff, true);

    /**
     * General handle size.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.lang.Integer> HANDLE_SIZE = new org.jhotdraw.draw.AttributeKey<>("handleSize", java.lang.Integer.class, 7);

    /**
     * General handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("handleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * General handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("handleFillColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * General handle stroke.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> HANDLE_STROKE = new org.jhotdraw.draw.AttributeKey<>("handleStroke", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    /**
     * General handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("handleStrokeColor", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * General handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("handleFillColor", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    // --
    /**
     * Rotate handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ROTATE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("rotateHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Rotate handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ROTATE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("rotateHandleFillColor", java.awt.Color.class, java.awt.Color.MAGENTA);

    // --
    /**
     * Rotate handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ROTATE_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("rotateHandleStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Rotate handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ROTATE_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("rotateHandleFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    // --
    /**
     * Bezier control point handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_CONTROL_POINT_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Bezier control point handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_CONTROL_POINT_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointHandleFillColor", java.awt.Color.class, java.awt.Color.BLUE);

    /**
     * Bezier tangent line stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_TANGENT_COLOR_1 = new org.jhotdraw.draw.AttributeKey<>("bezierTangentColor1", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Bezier tangent line stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_TANGENT_COLOR_2 = new org.jhotdraw.draw.AttributeKey<>("bezierTangentColor1", java.awt.Color.class, java.awt.Color.BLUE);

    /**
     * Bezier tangent line stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_TANGENT_STROKE_1 = new org.jhotdraw.draw.AttributeKey<>("bezierTangentStroke1", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 5.0F, 5.0F }, 0.0F));

    /**
     * Bezier tangent line stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_TANGENT_STROKE_2 = new org.jhotdraw.draw.AttributeKey<>("bezierTangentStroke2", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 5.0F, 5.0F }, 5.0F));

    /**
     * Bezier node handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_NODE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Bezier node handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_NODE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointFillColor", java.awt.Color.class, new java.awt.Color(0xa8ff));

    /**
     * Bezier path stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_1 = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor1", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Bezier path stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_2 = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor2", java.awt.Color.class, new java.awt.Color(0xa8ff));

    /**
     * Bezier path stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_1 = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke1", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL));

    /**
     * Bezier path stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_2 = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke2", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Bezier path hover stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor1Hover", java.awt.Color.class, null);

    /**
     * Bezier path hover stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor2Hover", java.awt.Color.class, new java.awt.Color(0xa8ff));

    /**
     * Bezier path hover stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke1Hover", java.awt.Stroke.class, null);

    /**
     * Bezier path hover stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke2Hover", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Bezier control point handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_CONTROL_POINT_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointHandleStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Bezier control point handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_CONTROL_POINT_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointHandleFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Bezier tangent line stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_TANGENT_COLOR_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierTangentColor1Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Bezier tangent line stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_TANGENT_COLOR_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierTangentColor1Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Bezier tangent line stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_TANGENT_STROKE_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierTangentStroke1Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 5.0F, 5.0F }, 0.0F));

    /**
     * Bezier tangent line stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_TANGENT_STROKE_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierTangentStroke2Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 5.0F, 5.0F }, 5.0F));

    /**
     * Bezier node handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_NODE_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Bezier node handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_NODE_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierControlPointFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Bezier path stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor1Disabled", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Bezier path stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> BEZIER_PATH_COLOR_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierPathColor2Disabled", java.awt.Color.class, new java.awt.Color(0x0));

    /**
     * Bezier path stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke1Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL));

    /**
     * Bezier path stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> BEZIER_PATH_STROKE_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("bezierPathStroke2Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Scale handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> SCALE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("scaleHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Scale handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> SCALE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("scaleHandleFillColor", java.awt.Color.class, java.awt.Color.ORANGE.darker());

    /**
     * Resize handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("resizeHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Resize handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("resizeHandleFillColor", java.awt.Color.class, java.awt.Color.BLUE);

    /**
     * Resize bounds stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_1 = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor1", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Resize bounds stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_2 = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor2", java.awt.Color.class, java.awt.Color.BLUE);

    /**
     * Resize bounds stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_1 = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke1", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL));

    /**
     * Resize bounds stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_2 = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke2", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // 
    /**
     * Disabled resize bounds stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor1Hover", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Disabled resize bounds hover color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor2Hover", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Disabled resize bounds stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke1Hover", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F));

    /**
     * Disabled bounds stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke2Hover", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    // 
    /**
     * Handle bounds hover stroke color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor1Hover", java.awt.Color.class, null);

    /**
     * Resize bounds hover stroke color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> RESIZE_BOUNDS_COLOR_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsColor2Hover", java.awt.Color.class, java.awt.Color.BLUE);

    /**
     * Resize bounds hover stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke1Hover", java.awt.Stroke.class, null);

    /**
     * Resize bounds hover stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> RESIZE_BOUNDS_STROKE_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("resizeBoundsStroke2Hover", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Transform handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("transformHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Transform handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("transformHandleFillColor", java.awt.Color.class, java.awt.Color.MAGENTA);

    // ---
    /**
     * Transform handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformHandleStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Transform handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformHandleFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Transform bounds color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_1 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Transform bounds color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_2 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2", java.awt.Color.class, java.awt.Color.MAGENTA);

    /**
     * Transform bounds stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_1 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL));

    /**
     * Transform bounds stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_2 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Transform bounds hover color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1Hover", java.awt.Color.class, null);

    /**
     * Transform bounds hover color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2Hover", java.awt.Color.class, java.awt.Color.MAGENTA);

    /**
     * Transform bounds hover stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1Hover", java.awt.Stroke.class, null);

    /**
     * Transform bounds hover stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2Hover", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    // ---
    /**
     * Transform bounds disabled color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Transform bounds disabled color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> TRANSFORM_BOUNDS_COLOR_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Transform bounds disabled stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F));

    /**
     * Transform bounds disabled stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> TRANSFORM_BOUNDS_STROKE_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    // ---
    /**
     * Group handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("transformHandleStrokeColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Group handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("transformHandleFillColor", java.awt.Color.class, java.awt.Color.MAGENTA);

    // ---
    /**
     * Group handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformHandleStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Group handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformHandleFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Group bounds color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_1 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Group bounds color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_2 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2", java.awt.Color.class, java.awt.Color.MAGENTA);

    /**
     * Group bounds stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_1 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL));

    /**
     * Group bounds stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_2 = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 1.0F, new float[]{ 2.0F, 2.0F }, 0.0F));

    // ---
    /**
     * Group bounds hover color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1Hover", java.awt.Color.class, null);

    /**
     * Group bounds hover color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2Hover", java.awt.Color.class, java.awt.Color.MAGENTA);

    /**
     * Group bounds hover stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_1_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1Hover", java.awt.Stroke.class, null);

    /**
     * Group bounds hover stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_2_HOVER = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2Hover", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 1.0F, new float[]{ 2.0F, 2.0F }, 0.0F));

    // ---
    // ---
    /**
     * Group bounds disabled color 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor1Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Group bounds disabled color 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> GROUP_BOUNDS_COLOR_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsColor2Disabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);

    /**
     * Group bounds disabled stroke 1.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_1_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke1Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(3.0F));

    /**
     * Group bounds disabled stroke 2.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Stroke> GROUP_BOUNDS_STROKE_2_DISABLED = new org.jhotdraw.draw.AttributeKey<>("transformBoundsStroke2Disabled", java.awt.Stroke.class, new java.awt.BasicStroke(1.0F));

    // ---
    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> CONNECTED_CONNECTION_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("connectedConnectionHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> CONNECTED_CONNECTION_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("connectedConnectionHandleFillColor", java.awt.Color.class, java.awt.Color.GREEN);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> DISCONNECTED_CONNECTION_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("disconnectedConnectionHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> DISCONNECTED_CONNECTION_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("disconnectedConnectionHandleFillColor", java.awt.Color.class, java.awt.Color.RED);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> CONNECTED_CONNECTOR_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("connectedConnectorHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> CONNECTED_CONNECTOR_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("connectedConnectorHandleFillColor", java.awt.Color.class, java.awt.Color.GREEN);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> DISCONNECTED_CONNECTOR_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("disconnectedConnectorHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> DISCONNECTED_CONNECTOR_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("disconnectedConnectorHandleFillColor", java.awt.Color.class, java.awt.Color.RED);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> MOVE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("moveHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> MOVE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("moveHandleFillColor", java.awt.Color.class, java.awt.Color.WHITE);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> NULL_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("nullHandleStrokeColor", java.awt.Color.class, java.awt.Color.DARK_GRAY);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> NULL_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("nullHandleFillColor", java.awt.Color.class, null);

    /**
     * Handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> OVERFLOW_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("overlfowHandleStrokeColor", java.awt.Color.class, java.awt.Color.RED);

    /**
     * Handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> OVERFLOW_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("overflowHandleFillColor", java.awt.Color.class, null);

    // --
    /**
     * Attribute handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ATTRIBUTE_HANDLE_STROKE_COLOR = new org.jhotdraw.draw.AttributeKey<>("attributeHandleStrokeColor", java.awt.Color.class, java.awt.Color.BLACK);

    /**
     * Attribute handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ATTRIBUTE_HANDLE_FILL_COLOR = new org.jhotdraw.draw.AttributeKey<>("attributeSizeHandleFillColor", java.awt.Color.class, java.awt.Color.YELLOW);

    // --
    /**
     * Attribute handle stroke color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ATTRIBUTE_HANDLE_STROKE_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("attributeHandleStrokeColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.STROKE_COLOR_DISABLED);

    /**
     * Attribute handle fill color.
     */
    public static final org.jhotdraw.draw.AttributeKey<java.awt.Color> ATTRIBUTE_HANDLE_FILL_COLOR_DISABLED = new org.jhotdraw.draw.AttributeKey<>("attributeSizeHandleFillColorDisabled", java.awt.Color.class, org.jhotdraw.draw.handle.HandleAttributeKeys.FILL_COLOR_DISABLED);
}