/* @(#)DrawingColorIcon.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.action;
/**
 * DrawingColorIcon draws a shape with the specified color for the drawing in the current drawing
 * view.
 *
 * <p>The behavior for choosing the drawn color matches with {@link DrawingColorChooserAction}.
 */
public class DrawingColorIcon extends javax.swing.ImageIcon {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingEditor editor;

    private org.jhotdraw.draw.AttributeKey<java.awt.Color> key;

    private java.awt.Shape colorShape;

    /**
     * Creates a new instance.
     *
     * @param editor
     * 		The drawing editor.
     * @param key
     * 		The key of the default attribute
     * @param imageLocation
     * 		the icon image
     * @param colorShape
     * 		The shape to be drawn with the color of the default attribute.
     */
    public DrawingColorIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.net.URL imageLocation, java.awt.Shape colorShape) {
        super(imageLocation);
        this.editor = editor;
        this.key = key;
        this.colorShape = colorShape;
    }

    public DrawingColorIcon(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> key, java.awt.Image image, java.awt.Shape colorShape) {
        super(image);
        this.editor = editor;
        this.key = key;
        this.colorShape = colorShape;
    }

    @java.lang.Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics gr, int x, int y) {
        java.awt.Graphics2D g = ((java.awt.Graphics2D) (gr));
        super.paintIcon(c, g, x, y);
        if (editor != null) {
            java.awt.Color color;
            org.jhotdraw.draw.DrawingView view = editor.getActiveView();
            if (view != null) {
                color = view.getDrawing().attr().get(key);
            } else {
                color = key.getDefaultValue();
            }
            if (color != null) {
                g.setColor(color);
                g.translate(x, y);
                g.fill(colorShape);
                g.translate(-x, -y);
            }
        }
    }
}