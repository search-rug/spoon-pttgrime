/**
 *
 * @(#)SelectionComponentRepainter.java <p>Copyright (c) 2008-2010 The authors and contributors of JHotDraw. You may not use, copy or
modify this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.event;
/**
 * Calls repaint on components, which show attributes of the drawing editor and of its views based
 * on the current selection.
 */
public class SelectionComponentRepainter extends org.jhotdraw.draw.event.DrawingListenerAdapter implements java.beans.PropertyChangeListener , org.jhotdraw.draw.event.FigureSelectionListener , org.jhotdraw.api.app.Disposable {
    private org.jhotdraw.draw.DrawingEditor editor;

    private javax.swing.JComponent component;

    public SelectionComponentRepainter(org.jhotdraw.draw.DrawingEditor editor, javax.swing.JComponent component) {
        this.editor = editor;
        this.component = component;
        if (editor != null) {
            if (editor.getActiveView() != null) {
                org.jhotdraw.draw.DrawingView view = editor.getActiveView();
                view.addPropertyChangeListener(this);
                view.addFigureSelectionListener(this);
                if (view.getDrawing() != null) {
                    view.getDrawing().addDrawingListener(this);
                }
            }
            editor.addPropertyChangeListener(this);
        }
    }

    @java.lang.Override
    public void drawingAttributeChanged(org.jhotdraw.draw.event.DrawingEvent evt) {
        component.repaint();
    }

    @java.lang.Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        java.lang.String name = evt.getPropertyName();
        if (((name == null) && (org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY))) {
            org.jhotdraw.draw.DrawingView view = ((org.jhotdraw.draw.DrawingView) (evt.getOldValue()));
            if (view != null) {
                view.removePropertyChangeListener(this);
                view.removeFigureSelectionListener(this);
                if (view.getDrawing() != null) {
                    view.getDrawing().removeDrawingListener(this);
                }
            }
            view = ((org.jhotdraw.draw.DrawingView) (evt.getNewValue()));
            if (view != null) {
                view.addPropertyChangeListener(this);
                view.addFigureSelectionListener(this);
                if (view.getDrawing() != null) {
                    view.getDrawing().addDrawingListener(this);
                }
            }
            component.repaint();
        } else if (((name == null) && (org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.draw.DrawingView.DRAWING_PROPERTY))) {
            org.jhotdraw.draw.Drawing drawing = ((org.jhotdraw.draw.Drawing) (evt.getOldValue()));
            if (drawing != null) {
                drawing.removeDrawingListener(this);
            }
            drawing = ((org.jhotdraw.draw.Drawing) (evt.getNewValue()));
            if (drawing != null) {
                drawing.addDrawingListener(this);
            }
            component.repaint();
        } else {
            component.repaint();
        }
    }

    @java.lang.Override
    public void selectionChanged(org.jhotdraw.draw.event.FigureSelectionEvent evt) {
        component.repaint();
    }

    @java.lang.Override
    public void dispose() {
        if (editor != null) {
            if (editor.getActiveView() != null) {
                org.jhotdraw.draw.DrawingView view = editor.getActiveView();
                view.removePropertyChangeListener(this);
                view.removeFigureSelectionListener(this);
                if (view.getDrawing() != null) {
                    view.getDrawing().removeDrawingListener(this);
                }
            }
            editor.removePropertyChangeListener(this);
            editor = null;
        }
        component = null;
    }
}