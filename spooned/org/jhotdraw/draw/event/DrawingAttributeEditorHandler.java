/* @(#)FigureAttributeEditorHandler.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * FigureAttributeEditorHandler mediates between an AttributeEditor and the currently selected
 * Figure's in a DrawingEditor.
 */
public class DrawingAttributeEditorHandler<T> extends org.jhotdraw.draw.event.AbstractAttributeEditorHandler<T> {
    private org.jhotdraw.draw.Drawing drawing;

    public DrawingAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor) {
        super(key, attributeEditor, drawingEditor, false);
    }

    public void setDrawing(org.jhotdraw.draw.Drawing newValue) {
        drawing = newValue;
        updateAttributeEditor();
    }

    public org.jhotdraw.draw.Drawing getDrawing() {
        return drawing;
    }

    @java.lang.Override
    protected java.util.Set<org.jhotdraw.draw.figure.Figure> getEditedFigures() {
        java.util.HashSet<org.jhotdraw.draw.figure.Figure> s = new java.util.HashSet<>();
        if (drawing != null) {
            s.addAll(drawing.getChildren());
        } else if (activeView != null) {
            s.addAll(activeView.getDrawing().getChildren());
        }
        return s;
    }
}