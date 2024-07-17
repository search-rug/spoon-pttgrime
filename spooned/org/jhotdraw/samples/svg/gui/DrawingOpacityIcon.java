/* @(#)DrawingOpacityIcon.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
/**
 * {@code DrawingOpacityIcon} visualizes an opacity attribute of the {@code Drawing} object which is
 * in the active {@code DrawingView} of a {@code DrawingEditor}.
 */
public class DrawingOpacityIcon extends javax.swing.ImageIcon {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingEditor editor;

    private org.jhotdraw.draw.AttributeKey<java.lang.Double> opacityKey;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> fillColorKey;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColorKey;

    private java.awt.Shape fillShape;

    private java.awt.Shape strokeShape;

    /**
     * Creates a new instance.
     *
     * @param editor
     * 		The drawing editor.
     * @param opacityKey
     * 		The opacityKey of the default attribute
     * @param imageLocation
     * 		the icon image
     * @param fillShape
     * 		The shape to be drawn with the fillColor of the default attribute.
     */
    public DrawingOpacityIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.lang.Double> opacityKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> fillColorKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColorKey, java.net.URL imageLocation, java.awt.Shape fillShape, java.awt.Shape strokeShape) {
        super(imageLocation);
        this.editor = editor;
        this.opacityKey = opacityKey;
        this.fillColorKey = fillColorKey;
        this.strokeColorKey = strokeColorKey;
        this.fillShape = fillShape;
        this.strokeShape = strokeShape;
    }

    public DrawingOpacityIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.lang.Double> opacityKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> fillColorKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColorKey, java.awt.Image image, java.awt.Shape fillShape, java.awt.Shape strokeShape) {
        super(image);
        this.editor = editor;
        this.opacityKey = opacityKey;
        this.fillColorKey = fillColorKey;
        this.strokeColorKey = strokeColorKey;
        this.fillShape = fillShape;
        this.strokeShape = strokeShape;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics gr, int x, int y) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        super.paintIcon(c, g, x, y);
        java.lang.Double opacity = 0.0;
        java.awt.Color fillColor = null;
        java.awt.Color strokeColor = null;
        if (editor != null) {
            org.jhotdraw.draw.DrawingView view = editor.getActiveView();
            if ((view != null) && (view.getDrawing() != null)) {
                org.jhotdraw.draw.Drawing d = view.getDrawing();
                opacity = d.attr().get(opacityKey);
                fillColor = (fillColorKey == null) ? null : d.attr().get(fillColorKey);
                strokeColor = (strokeColorKey == null) ? null : d.attr().get(strokeColorKey);
            } else {
                opacity = opacityKey.get(editor.getDefaultAttributes());
                fillColor = (fillColorKey == null) ? null : fillColorKey.get(editor.getDefaultAttributes());
                strokeColor = (strokeColorKey == null) ? null : strokeColorKey.get(editor.getDefaultAttributes());
            }
        }
        if ((fillColorKey != null) && (fillShape != null)) {
            if (opacity != null) {
                if (fillColor == null) {
                    fillColor = java.awt.Color.BLACK;
                }
                g.setColor(new java.awt.Color((((int) (opacity * 255)) << 24) | (fillColor.getRGB() & 0xffffff), true));
                g.translate(x, y);
                g.fill(fillShape);
                g.translate(-x, -y);
            }
        }
        if ((strokeColorKey != null) && (strokeShape != null)) {
            if (opacity != null) {
                if (strokeColor == null) {
                    strokeColor = java.awt.Color.BLACK;
                }
                g.setColor(new java.awt.Color((((int) (opacity * 255)) << 24) | (strokeColor.getRGB() & 0xffffff), true));
                g.translate(x, y);
                g.draw(strokeShape);
                g.translate(-x, -y);
            }
        }
    }
}