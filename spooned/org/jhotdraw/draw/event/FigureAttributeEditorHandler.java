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
public class FigureAttributeEditorHandler<T> extends org.jhotdraw.draw.event.AbstractAttributeEditorHandler<T> {
    public FigureAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor) {
        super(key, attributeEditor, drawingEditor);
    }

    public FigureAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor, boolean updateDrawingEditorDefaults) {
        super(key, attributeEditor, drawingEditor, updateDrawingEditorDefaults);
    }

    public FigureAttributeEditorHandler(org.jhotdraw.draw.AttributeKey<T> key, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, org.jhotdraw.api.gui.AttributeEditor<T> attributeEditor, org.jhotdraw.draw.DrawingEditor drawingEditor, boolean updateDrawingEditorDefaults) {
        super(key, defaultAttributes, attributeEditor, drawingEditor, updateDrawingEditorDefaults);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    protected java.util.Set<org.jhotdraw.draw.figure.Figure> getEditedFigures() {
        return ((java.util.Set<org.jhotdraw.draw.figure.Figure>) (activeView == null ? java.util.Collections.emptySet() : activeView.getSelectedFigures()));
    }
}