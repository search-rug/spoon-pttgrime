/* @(#)DefaultSelectAreaTracker.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * <code>DefaultSelectAreaTracker</code> implements interactions with the background area of a
 * <code>Drawing</code>.
 *
 * <p>The <code>DefaultSelectAreaTracker</code> handles one of the three states of the <code>
 * SelectionTool</code>. It comes into action, when the user presses the mouse button over the
 * background of a <code>Drawing</code>.
 *
 * <p>Design pattern:<br>
 * Name: Chain of Responsibility.<br>
 * Role: Handler.<br>
 * Partners: {@link SelectionTool} as Handler, {@link DragTracker} as Handler, {@link HandleTracker}
 * as Handler.
 *
 * <p>Design pattern:<br>
 * Name: State.<br>
 * Role: State.<br>
 * Partners: {@link SelectionTool} as Context, {@link DragTracker} as State, {@link HandleTracker}
 * as State.
 *
 * @see SelectionTool
 */
public class DefaultSelectAreaTracker extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.tool.SelectAreaTracker {
    private static final long serialVersionUID = 1L;

    /**
     * The bounds of the rubberband.
     */
    private java.awt.Rectangle rubberband = new java.awt.Rectangle();

    /**
     * Rubberband color. When this is null, the tracker does not draw the rubberband.
     */
    private java.awt.Color rubberbandColor = java.awt.Color.BLACK;

    /**
     * Rubberband stroke.
     */
    private java.awt.Stroke rubberbandStroke = new java.awt.BasicStroke();

    /**
     * The hover handles, are the handles of the figure over which the mouse pointer is currently
     * hovering.
     */
    private final java.util.List<org.jhotdraw.draw.handle.Handle> hoverHandles = new java.util.ArrayList<>();

    /**
     * The hover Figure is the figure, over which the mouse is currently hovering.
     */
    private org.jhotdraw.draw.figure.Figure hoverFigure = null;

    public DefaultSelectAreaTracker() {
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        super.mousePressed(evt);
        clearRubberBand();
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        selectGroup(evt.isShiftDown());
        clearRubberBand();
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        java.awt.Rectangle invalidatedArea = ((java.awt.Rectangle) (rubberband.clone()));
        rubberband.setBounds(java.lang.Math.min(anchor.x, evt.getX()), java.lang.Math.min(anchor.y, evt.getY()), java.lang.Math.abs(anchor.x - evt.getX()), java.lang.Math.abs(anchor.y - evt.getY()));
        if (invalidatedArea.isEmpty()) {
            invalidatedArea = ((java.awt.Rectangle) (rubberband.clone()));
        } else {
            invalidatedArea = invalidatedArea.union(rubberband);
        }
        fireAreaInvalidated(invalidatedArea);
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        clearRubberBand();
        java.awt.Point point = evt.getPoint();
        org.jhotdraw.draw.DrawingView view = editor.findView(((java.awt.Container) (evt.getSource())));
        updateCursor(view, point);
        if ((view == null) || (editor.getActiveView() != view)) {
            clearHoverHandles();
        } else {
            // Search first, if one of the selected figures contains
            // the current mouse location, and is selectable.
            // Only then search for other
            // figures. This search sequence is consistent with the
            // search sequence of the SelectionTool.
            org.jhotdraw.draw.figure.Figure figure = null;
            java.awt.geom.Point2D.Double p = view.viewToDrawing(point);
            for (org.jhotdraw.draw.figure.Figure f : view.getSelectedFigures()) {
                if (f.contains(p, view.getScaleFactor())) {
                    figure = f;
                }
            }
            if (figure == null) {
                figure = view.findFigure(point);
                while ((figure != null) && (!figure.isSelectable())) {
                    figure = view.getDrawing().findFigureBehind(p, view.getScaleFactor(), figure);
                } 
            }
            updateHoverHandles(view, figure);
        }
    }

    @java.lang.Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView view = editor.findView(((java.awt.Container) (evt.getSource())));
        updateHoverHandles(view, null);
    }

    private void clearRubberBand() {
        if (!rubberband.isEmpty()) {
            fireAreaInvalidated(rubberband);
            rubberband.width = -1;
        }
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        g.setStroke(rubberbandStroke);
        g.setColor(rubberbandColor);
        g.drawRect(rubberband.x, rubberband.y, rubberband.width - 1, rubberband.height - 1);
        if ((hoverHandles.size() > 0) && (!getView().isFigureSelected(hoverFigure))) {
            for (org.jhotdraw.draw.handle.Handle h : hoverHandles) {
                h.draw(g);
            }
        }
    }

    private void selectGroup(boolean toggle) {
        java.util.Collection<org.jhotdraw.draw.figure.Figure> figures = getView().findFiguresWithin(rubberband);
        for (org.jhotdraw.draw.figure.Figure f : figures) {
            if (f.isSelectable()) {
                getView().addToSelection(f);
            }
        }
    }

    protected void clearHoverHandles() {
        updateHoverHandles(null, null);
    }

    protected void updateHoverHandles(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.Figure f) {
        if (f != hoverFigure) {
            java.awt.Rectangle r = null;
            if (hoverFigure != null) {
                for (org.jhotdraw.draw.handle.Handle h : hoverHandles) {
                    if (r == null) {
                        r = h.getDrawingArea();
                    } else {
                        r.add(h.getDrawingArea());
                    }
                    h.setView(null);
                    h.dispose();
                }
                hoverHandles.clear();
            }
            hoverFigure = f;
            if ((hoverFigure != null) && f.isSelectable()) {
                hoverHandles.addAll(hoverFigure.createHandles(-1));
                for (org.jhotdraw.draw.handle.Handle h : hoverHandles) {
                    h.setView(view);
                    if (r == null) {
                        r = h.getDrawingArea();
                    } else {
                        r.add(h.getDrawingArea());
                    }
                }
            }
            if (r != null) {
                r.grow(1, 1);
                fireAreaInvalidated(r);
            }
        }
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
        clearHoverHandles();
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        super.deactivate(editor);
        clearHoverHandles();
    }
}