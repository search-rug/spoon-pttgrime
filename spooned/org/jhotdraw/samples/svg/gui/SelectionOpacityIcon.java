/* @(#)SelectionOpacityIcon.java

Copyright (c) 2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;
import org.jhotdraw.draw.figure.Figure;
/**
 * {@code SelectionOpacityIcon} visualizes an opacity attribute of the selected {@code Figure}(s) in
 * the active {@code DrawingView} of a {@code DrawingEditor}.
 */
public class SelectionOpacityIcon extends javax.swing.ImageIcon {
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
    public SelectionOpacityIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.lang.Double> opacityKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> fillColorKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColorKey, java.net.URL imageLocation, java.awt.Shape fillShape, java.awt.Shape strokeShape) {
        super(imageLocation);
        this.editor = editor;
        this.opacityKey = opacityKey;
        this.fillColorKey = fillColorKey;
        this.strokeColorKey = strokeColorKey;
        this.fillShape = fillShape;
        this.strokeShape = strokeShape;
    }

    public SelectionOpacityIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.lang.Double> opacityKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> fillColorKey, org.jhotdraw.draw.AttributeKey<java.awt.Color> strokeColorKey, java.awt.Image image, java.awt.Shape fillShape, java.awt.Shape strokeShape) {
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
        java.lang.Double opacity;
        java.awt.Color fillColor;
        java.awt.Color strokeColor;
        org.jhotdraw.draw.DrawingView view = (editor == null) ? null : editor.getActiveView();
        if ((view != null) && (view.getSelectedFigures().size() == 1)) {
            org.jhotdraw.draw.figure.Figure f = view.getSelectedFigures().iterator().next();
            opacity = f.attr().get(opacityKey);
            fillColor = (fillColorKey == null) ? null : f.attr().get(fillColorKey);
            strokeColor = (strokeColorKey == null) ? null : f.attr().get(strokeColorKey);
        } else if (editor != null) {
            opacity = opacityKey.get(editor.getDefaultAttributes());
            fillColor = (fillColorKey == null) ? null : fillColorKey.get(editor.getDefaultAttributes());
            strokeColor = (strokeColorKey == null) ? null : strokeColorKey.get(editor.getDefaultAttributes());
        } else {
            opacity = opacityKey.getDefaultValue();
            fillColor = (fillColorKey == null) ? null : fillColorKey.getDefaultValue();
            strokeColor = (strokeColorKey == null) ? null : strokeColorKey.getDefaultValue();
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