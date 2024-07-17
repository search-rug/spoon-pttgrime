/* @(#)SelectionTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
import org.jhotdraw.draw.handle.Handle;
/**
 * Tool to select and manipulate figures.
 *
 * <p>A selection tool is in one of three states: 1) area selection, 2) figure dragging, 3) handle
 * manipulation. The different states are handled by different tracker objects: the <code>
 * DefaultSelectAreaTracker</code>, the <code>DefaultDragTracker</code> and the <code>
 * DefaultHandleTracker</code>.
 *
 * <p>A Figure can be selected by clicking at it. Holding the alt key or the ctrl key down, selects
 * the Figure behind it. <hr> <b>Design Patterns</b>
 *
 * <p><em>Strategy</em><br>
 * The different behavior states of the selection tool are implemented by trackers.<br>
 * Context: {@link SelectionTool}; State: {@link DragTracker}, {@link HandleTracker}, {@link SelectAreaTracker}.
 *
 * <p><em>Chain of responsibility</em><br>
 * Mouse and keyboard events of the user occur on the drawing view, and are preprocessed by the
 * {@code DragTracker} of a {@code SelectionTool}. In turn {@code DragTracker} invokes "track"
 * methods on a {@code Handle} which in turn changes an aspect of a figure.<br>
 * Client: {@link SelectionTool}; Handler: {@link DragTracker}, {@link Handle}. <hr>
 */
public class SelectionTool extends org.jhotdraw.draw.tool.AbstractTool {
    private static final long serialVersionUID = 1L;

    /**
     * The tracker encapsulates the current state of the SelectionTool.
     */
    private org.jhotdraw.draw.tool.Tool tracker;

    /**
     * The tracker encapsulates the current state of the SelectionTool.
     */
    private org.jhotdraw.draw.tool.HandleTracker handleTracker;

    /**
     * The tracker encapsulates the current state of the SelectionTool.
     */
    private org.jhotdraw.draw.tool.SelectAreaTracker selectAreaTracker;

    /**
     * The tracker encapsulates the current state of the SelectionTool.
     */
    private org.jhotdraw.draw.tool.DragTracker dragTracker;

    private int pixelTolerance = 10;

    private class TrackerHandler extends org.jhotdraw.draw.event.ToolAdapter {
        @java.lang.Override
        public void toolDone(org.jhotdraw.draw.event.ToolEvent event) {
            // Empty
            org.jhotdraw.draw.tool.Tool newTracker = getSelectAreaTracker();
            if (newTracker != null) {
                if (tracker != null) {
                    tracker.deactivate(getEditor());
                    tracker.removeToolListener(this);
                }
                tracker = newTracker;
                tracker.activate(getEditor());
                tracker.addToolListener(this);
            }
            fireToolDone();
        }

        /**
         * Sent when an area of the drawing view needs to be repainted.
         */
        @java.lang.Override
        public void areaInvalidated(org.jhotdraw.draw.event.ToolEvent e) {
            fireAreaInvalidated(e.getInvalidatedArea());
        }

        /**
         * Sent when the bounds need to be revalidated.
         */
        @java.lang.Override
        public void boundsInvalidated(org.jhotdraw.draw.event.ToolEvent e) {
            fireBoundsInvalidated(e.getInvalidatedArea());
        }
    }

    private org.jhotdraw.draw.tool.SelectionTool.TrackerHandler trackerHandler;

    /**
     * Constant for the name of the selectBehindEnabled property.
     */
    public static final java.lang.String SELECT_BEHIND_ENABLED_PROPERTY = "selectBehindEnabled";

    /**
     * Represents the state of the selectBehindEnabled property. By default, this property is set to
     * true.
     */
    private boolean isSelectBehindEnabled = true;

    public SelectionTool() {
        tracker = getSelectAreaTracker();
        trackerHandler = new org.jhotdraw.draw.tool.SelectionTool.TrackerHandler();
        tracker.addToolListener(trackerHandler);
    }

    /**
     * Sets the selectBehindEnabled property. This is a bound property.
     *
     * @param newValue
     * 		The new value.
     */
    public void setSelectBehindEnabled(boolean newValue) {
        boolean oldValue = isSelectBehindEnabled;
        isSelectBehindEnabled = newValue;
        firePropertyChange(org.jhotdraw.draw.tool.SelectionTool.SELECT_BEHIND_ENABLED_PROPERTY, oldValue, newValue);
    }

    /**
     * Returns the value of the selectBehindEnabled property. This is a bound property.
     *
     * @return The property value.
     */
    public boolean isSelectBehindEnabled() {
        return isSelectBehindEnabled;
    }

    @java.lang.Override
    public void activate(org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);
        tracker.activate(editor);
    }

    @java.lang.Override
    public void deactivate(org.jhotdraw.draw.DrawingEditor editor) {
        super.deactivate(editor);
        tracker.deactivate(editor);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.keyPressed(e);
        }
    }

    @java.lang.Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.keyReleased(evt);
        }
    }

    @java.lang.Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.keyTyped(evt);
        }
    }

    @java.lang.Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.mouseClicked(evt);
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.mouseDragged(evt);
        }
    }

    @java.lang.Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        super.mouseEntered(evt);
        tracker.mouseEntered(evt);
    }

    @java.lang.Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        super.mouseExited(evt);
        tracker.mouseExited(evt);
    }

    @java.lang.Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
        tracker.mouseMoved(evt);
    }

    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            tracker.mouseReleased(evt);
        }
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        tracker.draw(g);
    }

    @java.lang.Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        if ((getView() != null) && getView().isEnabled()) {
            super.mousePressed(evt);
            org.jhotdraw.draw.DrawingView view = getView();
            org.jhotdraw.draw.handle.Handle handle = view.findHandle(anchor);
            org.jhotdraw.draw.tool.Tool newTracker = null;
            if (handle != null) {
                newTracker = getHandleTracker(handle);
            } else {
                org.jhotdraw.draw.figure.Figure figure;
                org.jhotdraw.draw.Drawing drawing = view.getDrawing();
                java.awt.geom.Point2D.Double p = view.viewToDrawing(anchor);
                if (isSelectBehindEnabled() && ((evt.getModifiersEx() & (java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK)) != 0)) {
                    figure = drawing.findFigure(p, view.getScaleFactor());
                    while ((figure != null) && (!figure.isSelectable())) {
                        figure = drawing.findFigureBehind(p, view.getScaleFactor(), figure);
                    } 
                    java.util.HashSet<org.jhotdraw.draw.figure.Figure> ignoredFigures = new java.util.HashSet<>(view.getSelectedFigures());
                    ignoredFigures.add(figure);
                    org.jhotdraw.draw.figure.Figure figureBehind = view.getDrawing().findFigureBehind(view.viewToDrawing(anchor), ignoredFigures);
                    if (figureBehind != null) {
                        figure = figureBehind;
                    }
                } else {
                    // Note: The search sequence used here, must be
                    // consistent with the search sequence used by the
                    // DefaultHandleTracker, the DefaultSelectAreaTracker and DelegationSelectionTool.
                    // If possible, continue to work with the current selection
                    figure = null;
                    if (isSelectBehindEnabled()) {
                        // this will never happen
                        for (org.jhotdraw.draw.figure.Figure f : view.getSelectedFigures()) {
                            if (f.contains(p, view.getScaleFactor())) {
                                figure = f;
                                break;
                            }
                        }
                    }
                    // If the point is not contained in the current selection,
                    // search for a figure in the drawing.
                    if (figure == null) {
                        figure = drawing.findFigure(p, view.getScaleFactor());
                        while ((figure != null) && (!figure.isSelectable())) {
                            figure = drawing.findFigureBehind(p, view.getScaleFactor(), figure);
                        } 
                    }
                }
                if ((figure != null) && figure.isSelectable()) {
                    newTracker = getDragTracker(figure);
                } else {
                    if (!evt.isShiftDown()) {
                        view.clearSelection();
                        view.setHandleDetailLevel(0);
                    }
                    newTracker = getSelectAreaTracker();
                }
            }
            if (newTracker != null) {
                setTracker(newTracker);
            }
            tracker.mousePressed(evt);
        }
    }

    protected void setTracker(org.jhotdraw.draw.tool.Tool newTracker) {
        if (tracker != null) {
            tracker.deactivate(getEditor());
            tracker.removeToolListener(trackerHandler);
        }
        tracker = newTracker;
        if (tracker != null) {
            tracker.activate(getEditor());
            tracker.addToolListener(trackerHandler);
        }
    }

    /**
     * Method to get a {@code HandleTracker} which handles user interaction for the specified handle.
     */
    protected org.jhotdraw.draw.tool.HandleTracker getHandleTracker(org.jhotdraw.draw.handle.Handle handle) {
        if (handleTracker == null) {
            handleTracker = new org.jhotdraw.draw.tool.DefaultHandleTracker();
        }
        handleTracker.setHandles(handle, getView().getCompatibleHandles(handle));
        return handleTracker;
    }

    /**
     * Method to get a {@code DragTracker} which handles user interaction for dragging the specified
     * figure.
     */
    protected org.jhotdraw.draw.tool.DragTracker getDragTracker(org.jhotdraw.draw.figure.Figure f) {
        if (dragTracker == null) {
            dragTracker = new org.jhotdraw.draw.tool.DefaultDragTracker();
        }
        dragTracker.setDraggedFigure(f);
        return dragTracker;
    }

    /**
     * Method to get a {@code SelectAreaTracker} which handles user interaction for selecting an area
     * on the drawing.
     */
    protected org.jhotdraw.draw.tool.SelectAreaTracker getSelectAreaTracker() {
        if (selectAreaTracker == null) {
            selectAreaTracker = new org.jhotdraw.draw.tool.DefaultSelectAreaTracker();
        }
        return selectAreaTracker;
    }

    /**
     * Method to set a {@code HandleTracker}. If you specify null, the {@code SelectionTool} uses the
     * {@code DefaultHandleTracker}.
     */
    public void setHandleTracker(org.jhotdraw.draw.tool.HandleTracker newValue) {
        handleTracker = newValue;
    }

    /**
     * Method to set a {@code SelectAreaTracker}. If you specify null, the {@code SelectionTool} uses
     * the {@code DefaultSelectAreaTracker}.
     */
    public void setSelectAreaTracker(org.jhotdraw.draw.tool.SelectAreaTracker newValue) {
        selectAreaTracker = newValue;
    }

    /**
     * Method to set a {@code DragTracker}. If you specify null, the {@code SelectionTool} uses the
     * {@code DefaultDragTracker}.
     */
    public void setDragTracker(org.jhotdraw.draw.tool.DragTracker newValue) {
        dragTracker = newValue;
    }

    /**
     * Returns true, if this tool lets the user interact with handles.
     *
     * <p>Handles may draw differently, if interaction is not possible.
     *
     * @return True, if this tool supports interaction with the handles.
     */
    @java.lang.Override
    public boolean supportsHandleInteraction() {
        return true;
    }
}