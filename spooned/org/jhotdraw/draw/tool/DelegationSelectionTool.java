/* @(#)DelegationSelectionTool.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.tool;
/**
 * A SelectionTool, which recognizes double clicks and popup menu triggers. If a double click or
 * popup trigger is encountered a hook method is called, which handles the event. This methods can
 * be overriden in subclasse to provide customized behaviour.
 *
 * <p>By default, this Tool delegates mouse events to a specific Tool if the figure which has been
 * double clicked, provides a specialized tool.
 */
public class DelegationSelectionTool extends org.jhotdraw.draw.tool.SelectionTool {
    private static final long serialVersionUID = 1L;

    /**
     * A set of actions which is applied to the drawing.
     */
    private java.util.Collection<javax.swing.Action> drawingActions;

    /**
     * A set of actions which is applied to a selection of figures.
     */
    private java.util.Collection<javax.swing.Action> selectionActions;

    /**
     * We use this timer, to show a popup menu, when the user presses the mouse key for a second
     * without moving the mouse.
     */
    private javax.swing.Timer popupTimer;

    /**
     * When the popup menu is visible, we do not track mouse movements.
     */
    private javax.swing.JPopupMenu popupMenu;

    /**
     * We store the last mouse click here, to support multi-click behavior, that is, a behavior that
     * is invoked, when the user clicks multiple on the same spot, but in a longer interval than
     * needed for a double click.
     */
    private java.awt.event.MouseEvent lastClickEvent;

    /**
     * This variable is set to true, if a mouse pressed event is a popup trigger.
     */
    private boolean isMousePressedPopupTrigger;

    public DelegationSelectionTool() {
        this(new java.util.ArrayList<javax.swing.Action>(), new java.util.ArrayList<javax.swing.Action>());
    }

    public DelegationSelectionTool(java.util.Collection<javax.swing.Action> drawingActions, java.util.Collection<javax.swing.Action> selectionActions) {
        this.drawingActions = drawingActions;
        this.selectionActions = selectionActions;
    }

    public void setDrawingActions(java.util.Collection<javax.swing.Action> drawingActions) {
        this.drawingActions = drawingActions;
    }

    public void setFigureActions(java.util.Collection<javax.swing.Action> selectionActions) {
        this.selectionActions = selectionActions;
    }

    /**
     * MouseListener method for mousePressed events. If the popup trigger has been activated, then the
     * appropriate hook method is called.
     */
    @java.lang.Override
    public void mousePressed(final java.awt.event.MouseEvent evt) {
        if (popupTimer != null) {
            popupTimer.stop();
            popupTimer = null;
        }
        // XXX - When we want to support multiple views, we have to
        // implement this:
        // setView((DrawingView)e.getSource());
        isMousePressedPopupTrigger = evt.isPopupTrigger();
        if (isMousePressedPopupTrigger) {
            getView().requestFocus();
            handlePopupMenu(evt);
        } else {
            super.mousePressed(evt);
            popupTimer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent aevt) {
                    handlePopupMenu(evt);
                    popupTimer = null;
                }
            });
            popupTimer.setRepeats(false);
            popupTimer.start();
        }
    }

    /**
     * MouseListener method for mouseReleased events. If the popup trigger has been activated, then
     * the appropriate hook method is called.
     */
    @java.lang.Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        if (popupTimer != null) {
            popupTimer.stop();
            popupTimer = null;
        }
        if (isMousePressedPopupTrigger) {
            isMousePressedPopupTrigger = false;
        } else if (evt.isPopupTrigger()) {
            handlePopupMenu(evt);
        } else {
            super.mouseReleased(evt);
        }
    }

    @java.lang.Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        if (popupTimer != null) {
            popupTimer.stop();
            popupTimer = null;
        }
        if ((popupMenu == null) || (!popupMenu.isVisible())) {
            super.mouseDragged(evt);
        }
    }

    @java.lang.Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        super.mouseClicked(evt);
        if (!evt.isConsumed()) {
            if ((evt.getClickCount() >= 2) && (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)) {
                handleDoubleClick(evt);
            } else if (((((((evt.getClickCount() == 1) && (evt.getModifiersEx() == 0)) && (lastClickEvent != null)) && (lastClickEvent.getClickCount() == 1)) && (lastClickEvent.getModifiersEx() == 0)) && (lastClickEvent.getX() == evt.getX())) && (lastClickEvent.getY() == evt.getY())) {
                handleMultiClick(evt);
            }
        }
        lastClickEvent = evt;
    }

    /**
     * Hook method which can be overriden by subclasses to provide specialised behaviour in the event
     * of a popup trigger.
     */
    protected void handlePopupMenu(java.awt.event.MouseEvent evt) {
        java.awt.Point p = new java.awt.Point(evt.getX(), evt.getY());
        org.jhotdraw.draw.figure.Figure figure = getView().findFigure(p);
        if ((figure != null) || (drawingActions.size() > 0)) {
            showPopupMenu(figure, p, evt.getComponent());
        } else {
            popupMenu = null;
        }
    }

    protected void showPopupMenu(org.jhotdraw.draw.figure.Figure figure, java.awt.Point p, java.awt.Component c) {
        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        popupMenu = menu;
        javax.swing.JMenu submenu = null;
        java.lang.String submenuName = null;
        java.util.List<javax.swing.Action> popupActions = new java.util.ArrayList<>();
        if (figure != null) {
            java.util.List<javax.swing.Action> figureActions = new java.util.ArrayList<>(figure.getActions(viewToDrawing(p)));
            if ((popupActions.size() != 0) && (figureActions.size() != 0)) {
                popupActions.add(null);
            }
            popupActions.addAll(figureActions);
            if ((popupActions.size() != 0) && (selectionActions.size() != 0)) {
                popupActions.add(null);
            }
            popupActions.addAll(selectionActions);
        }
        if ((popupActions.size() != 0) && (drawingActions.size() != 0)) {
            popupActions.add(null);
        }
        popupActions.addAll(drawingActions);
        java.util.HashMap<java.lang.Object, javax.swing.ButtonGroup> buttonGroups = new java.util.HashMap<>();
        for (javax.swing.Action a : popupActions) {
            if ((a != null) && (a.getValue(org.jhotdraw.util.ActionUtil.SUBMENU_KEY) != null)) {
                if ((submenuName == null) || (!submenuName.equals(a.getValue(org.jhotdraw.util.ActionUtil.SUBMENU_KEY)))) {
                    submenuName = ((java.lang.String) (a.getValue(org.jhotdraw.util.ActionUtil.SUBMENU_KEY)));
                    submenu = new javax.swing.JMenu(submenuName);
                    menu.add(submenu);
                }
            } else {
                submenuName = null;
                submenu = null;
            }
            if (a == null) {
                if (submenu != null) {
                    submenu.addSeparator();
                } else {
                    menu.addSeparator();
                }
            } else {
                javax.swing.AbstractButton button;
                if (a.getValue(org.jhotdraw.util.ActionUtil.BUTTON_GROUP_KEY) != null) {
                    javax.swing.ButtonGroup bg = buttonGroups.get(a.getValue(org.jhotdraw.util.ActionUtil.BUTTON_GROUP_KEY));
                    if (bg == null) {
                        bg = new javax.swing.ButtonGroup();
                        buttonGroups.put(a.getValue(org.jhotdraw.util.ActionUtil.BUTTON_GROUP_KEY), bg);
                    }
                    button = new javax.swing.JRadioButtonMenuItem(a);
                    bg.add(button);
                    button.setSelected(a.getValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY) == java.lang.Boolean.TRUE);
                } else if (a.getValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY) != null) {
                    button = new javax.swing.JCheckBoxMenuItem(a);
                    button.setSelected(a.getValue(org.jhotdraw.util.ActionUtil.SELECTED_KEY) == java.lang.Boolean.TRUE);
                } else {
                    button = new javax.swing.JMenuItem(a);
                }
                if (submenu != null) {
                    submenu.add(button);
                } else {
                    menu.add(button);
                }
            }
        }
        menu.show(c, p.x, p.y);
    }

    /**
     * Hook method which can be overriden by subclasses to provide specialised behaviour in the event
     * of a double click.
     */
    protected void handleDoubleClick(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView v = getView();
        java.awt.Point pos = new java.awt.Point(evt.getX(), evt.getY());
        org.jhotdraw.draw.handle.Handle handle = v.findHandle(pos);
        if (handle != null) {
            handle.trackDoubleClick(pos, evt.getModifiersEx());
        } else {
            java.awt.geom.Point2D.Double p = viewToDrawing(pos);
            // Note: The search sequence used here, must be
            // consistent with the search sequence used by the
            // HandleTracker, the SelectAreaTracker and SelectionTool.
            // If possible, continue to work with the current selection
            org.jhotdraw.draw.figure.Figure figure = null;
            if (isSelectBehindEnabled()) {
                for (org.jhotdraw.draw.figure.Figure f : v.getSelectedFigures()) {
                    if (f.contains(p)) {
                        figure = f;
                        break;
                    }
                }
            }
            // If the point is not contained in the current selection,
            // search for a figure in the drawing.
            if (figure == null) {
                figure = v.findFigure(pos);
            }
            org.jhotdraw.draw.figure.Figure outerFigure = figure;
            if ((figure != null) && figure.isSelectable()) {
                org.jhotdraw.draw.tool.Tool figureTool = figure.getTool(p);
                if (figureTool == null) {
                    figure = getDrawing().findFigureInside(p);
                    if (figure != null) {
                        figureTool = figure.getTool(p);
                    }
                }
                if (figureTool != null) {
                    setTracker(figureTool);
                    figureTool.mousePressed(evt);
                } else if (outerFigure.handleMouseClick(p, evt, getView())) {
                    v.clearSelection();
                    v.addToSelection(outerFigure);
                } else {
                    v.clearSelection();
                    v.addToSelection(outerFigure);
                    v.setHandleDetailLevel(v.getHandleDetailLevel() + 1);
                }
            }
        }
        evt.consume();
    }

    /**
     * Hook method which can be overriden by subclasses to provide specialised behaviour in the event
     * of a multi-click.
     */
    protected void handleMultiClick(java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.DrawingView v = getView();
        java.awt.Point pos = new java.awt.Point(evt.getX(), evt.getY());
        org.jhotdraw.draw.handle.Handle handle = v.findHandle(pos);
        if (handle == null) {
            v.setHandleDetailLevel(v.getHandleDetailLevel() + 1);
        }
    }

    @java.lang.Override
    public java.lang.String getToolTipText(org.jhotdraw.draw.DrawingView view, java.awt.event.MouseEvent evt) {
        org.jhotdraw.draw.handle.Handle handle = view.findHandle(evt.getPoint());
        if (handle != null) {
            return handle.getToolTipText(evt.getPoint());
        }
        org.jhotdraw.draw.figure.Figure figure = view.findFigure(evt.getPoint());
        if (figure != null) {
            return figure.getToolTipText(viewToDrawing(evt.getPoint()));
        }
        return null;
    }
}