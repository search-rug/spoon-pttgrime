/* @(#)DefaultHandleTracker.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * DefaultHandleTracker implements interactions with the handles of a Figure.
 *
 * <p>The <code>DefaultHandleTracker</code> handles one of the three states of the <code>
 * SelectionTool</code>. Iz comes into action, when the user presses the mouse button over a <code>
 * Figure</code>.
 *
 * <p>Design pattern:<br>
 * Name: Chain of Responsibility.<br>
 * Role: Handler.<br>
 * Partners: {@link SelectionTool} as Handler, {@link SelectAreaTracker} as Handler, {@link DragTracker} as Handler, {@link DefaultHandleTracker} as Handler.
 *
 * <p>Design pattern:<br>
 * Name: State.<br>
 * Role: State.<br>
 * Partners: {@link SelectAreaTracker} as State, {@link DragTracker} as State, {@link SelectionTool}
 * as Context.
 *
 * @see SelectionTool
 */
public class DefaultHandleTracker extends org.jhotdraw.draw.tool.AbstractTool implements org.jhotdraw.draw.tool.HandleTracker {
    private static final long serialVersionUID = 1L;

    private class EventHandler implements org.jhotdraw.draw.event.HandleListener {
        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.HandleEvent e) {
            // empty
        }

        @java.lang.Override
        public void handleRequestRemove(org.jhotdraw.draw.event.HandleEvent e) {
            fireToolDone();
        }

        @java.lang.Override
        public void handleRequestSecondaryHandles(org.jhotdraw.draw.event.HandleEvent e) {
            // empty
        }
    }

    private org.jhotdraw.draw.tool.DefaultHandleTracker.EventHandler eventHandler = new org.jhotdraw.draw.tool.DefaultHandleTracker.EventHandler();

    /**
     * Last dragged mouse location. This variable is only non-null when the mouse is being pressed or
     * dragged.
     */
    private java.awt.Point dragLocation;

    private org.jhotdraw.draw.handle.Handle masterHandle;

    private org.jhotdraw.draw.event.HandleMulticaster multicaster;

    /**
     * The hover handles, are the handles of the figure over which the mouse pointer is currently
     * hovering.
     */
    private final java.util.List<org.jhotdraw.draw.handle.Handle> hoverHandles = new java.util.ArrayList<>();

    /**
     * The hover Figure is the figure, over which the mouse is currently hovering.
     */
    private org.jhotdraw.draw.figure.Figure hoverFigure = null;

    public DefaultHandleTracker(org.jhotdraw.draw.handle.Handle handle) {
        masterHandle = handle;
        multicaster = new org.jhotdraw.draw.event.HandleMulticaster(handle);
    }

    public DefaultHandleTracker(org.jhotdraw.draw.handle.Handle master, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        masterHandle = master;
        multicaster = new org.jhotdraw.draw.event.HandleMulticaster(handles);
    }

    public DefaultHandleTracker() {
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        if ((hoverHandles.size() > 0) && (!getView().isFigureSelected(hoverFigure))) {
            for (org.jhotdraw.draw.handle.Handle h : hoverHandles) {
                h.draw(g);
            }
        }
    }

    /* FIXME - The handle should draw itself in selected mode
    public void draw(Graphics2D g) {
    g.setColor(Color.RED);
    g.draw(
    masterHandle.getBounds()
    );
    }
     */
    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
        org.jhotdraw.draw.DrawingView v = getView();
        if (v != null) {
            v.setCursor(masterHandle.getCursor());
            v.setActiveHandle(masterHandle);
        }
        clearHoverHandles();
        masterHandle.addHandleListener(eventHandler);
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        super.deactivate(editor);
        org.jhotdraw.draw.DrawingView v = getView();
        if (v != null) {
            v.setCursor(java.awt.Cursor.getDefaultCursor());
            v.setActiveHandle(null);
        }
        clearHoverHandles();
        dragLocation = null;
        masterHandle.removeHandleListener(eventHandler);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        multicaster.keyPressed(evt);
        if (!evt.isConsumed()) {
            super.keyPressed(evt);
            // Forward key presses to the handler
            if (dragLocation != null) {
                multicaster.trackStep(anchor, dragLocation, evt.getModifiersEx(), getView());
            }
        }
    }

    @java.lang.Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
        multicaster.keyReleased(evt);
        // Forward key releases to the handler
        if (dragLocation != null) {
            multicaster.trackStep(anchor, dragLocation, evt.getModifiersEx(), getView());
        }
    }

    @java.lang.Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        multicaster.keyTyped(evt);
    }

    @java.lang.Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            multicaster.trackDoubleClick(new java.awt.Point(evt.getX(), evt.getY()), evt.getModifiersEx(), getView());
        }
        evt.consume();
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        dragLocation = new java.awt.Point(evt.getX(), evt.getY());
        multicaster.trackStep(anchor, dragLocation, evt.getModifiersEx(), getView());
        clearHoverHandles();
    }

    @java.lang.Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
    }

    @java.lang.Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView view = editor.findView(((java.awt.Container) (evt.getSource())));
        updateHoverHandles(view, null);
        dragLocation = null;
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        java.awt.Point point = evt.getPoint();
        updateCursor(editor.findView(((java.awt.Container) (evt.getSource()))), point);
        org.jhotdraw.draw.DrawingView view = editor.findView(((java.awt.Container) (evt.getSource())));
        updateCursor(view, point);
        if ((view == null) || (editor.getActiveView() != view)) {
            clearHoverHandles();
        } else {
            // Search first, if one of the selected figures contains
            // the current mouse location. Only then search for other
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
                org.jhotdraw.draw.Drawing drawing = view.getDrawing();
                while ((figure != null) && (!figure.isSelectable())) {
                    figure = drawing.findFigureBehind(p, view.getScaleFactor(), figure);
                } 
            }
            updateHoverHandles(view, figure);
        }
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        // handle.mousePressed(evt);
        anchor = new java.awt.Point(evt.getX(), evt.getY());
        multicaster.trackStart(anchor, evt.getModifiersEx(), getView());
        clearHoverHandles();
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        dragLocation = new java.awt.Point(evt.getX(), evt.getY());
        multicaster.trackEnd(anchor, dragLocation, evt.getModifiersEx(), getView());
        // Note: we must not fire "Tool Done" in this method, because then we can not
        // listen to keyboard events for the handle.
        java.awt.Rectangle r = new java.awt.Rectangle(anchor.x, anchor.y, 0, 0);
        r.add(evt.getX(), evt.getY());
        maybeFireBoundsInvalidated(r);
        dragLocation = null;
    }

    protected void clearHoverHandles() {
        updateHoverHandles(null, null);
    }

    protected void updateHoverHandles(org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.Figure f) {
        if (f != hoverFigure) {
            java.awt.Rectangle r = null;
            if ((hoverFigure != null) && hoverFigure.isSelectable()) {
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
            if (hoverFigure != null) {
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
    public void setHandles(org.jhotdraw.draw.handle.Handle handle, java.util.Collection<org.jhotdraw.draw.handle.Handle> compatibleHandles) {
        masterHandle = handle;
        multicaster = new org.jhotdraw.draw.event.HandleMulticaster(handle);
    }
}