/* @(#)DependencyFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert.figures;
/**
 * DependencyFigure.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class DependencyFigure extends org.jhotdraw.draw.figure.LineConnectionFigure {
    private static final long serialVersionUID = 1L;

    public DependencyFigure() {
        attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, new java.awt.Color(0x99));
        attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, 1.0);
        attr().set(org.jhotdraw.draw.AttributeKeys.END_DECORATION, new org.jhotdraw.draw.decoration.ArrowTip());
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.END_DECORATION, false);
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.START_DECORATION, false);
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_DASHES, false);
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FONT_ITALIC, false);
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE, false);
    }

    /**
     * Checks if two figures can be connected. Implement this method to constrain the allowed
     * connections between figures.
     */
    @java.lang.Override
    public boolean canConnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
        if ((start.getOwner() instanceof org.jhotdraw.samples.pert.figures.TaskFigure) && (end.getOwner() instanceof org.jhotdraw.samples.pert.figures.TaskFigure)) {
            org.jhotdraw.samples.pert.figures.TaskFigure sf = ((org.jhotdraw.samples.pert.figures.TaskFigure) (start.getOwner()));
            org.jhotdraw.samples.pert.figures.TaskFigure ef = ((org.jhotdraw.samples.pert.figures.TaskFigure) (end.getOwner()));
            // Disallow multiple connections to same dependent
            if (ef.getPredecessors().contains(sf)) {
                return false;
            }
            // Disallow cyclic connections
            return !sf.isDependentOf(ef);
        }
        return false;
    }

    @java.lang.Override
    public boolean canConnect(org.jhotdraw.draw.connector.Connector start) {
        return start.getOwner() instanceof org.jhotdraw.samples.pert.figures.TaskFigure;
    }

    /**
     * Handles the disconnection of a connection. Override this method to handle this event.
     */
    @java.lang.Override
    protected void handleDisconnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
        org.jhotdraw.samples.pert.figures.TaskFigure sf = ((org.jhotdraw.samples.pert.figures.TaskFigure) (start.getOwner()));
        org.jhotdraw.samples.pert.figures.TaskFigure ef = ((org.jhotdraw.samples.pert.figures.TaskFigure) (end.getOwner()));
        sf.removeDependency(this);
        ef.removeDependency(this);
    }

    /**
     * Handles the connection of a connection. Override this method to handle this event.
     */
    @java.lang.Override
    protected void handleConnect(org.jhotdraw.draw.connector.Connector start, org.jhotdraw.draw.connector.Connector end) {
        org.jhotdraw.samples.pert.figures.TaskFigure sf = ((org.jhotdraw.samples.pert.figures.TaskFigure) (start.getOwner()));
        org.jhotdraw.samples.pert.figures.TaskFigure ef = ((org.jhotdraw.samples.pert.figures.TaskFigure) (end.getOwner()));
        sf.addDependency(this);
        ef.addDependency(this);
    }

    @java.lang.Override
    public org.jhotdraw.samples.pert.figures.DependencyFigure clone() {
        org.jhotdraw.samples.pert.figures.DependencyFigure that = ((org.jhotdraw.samples.pert.figures.DependencyFigure) (super.clone()));
        return that;
    }

    @java.lang.Override
    public int getLayer() {
        return 1;
    }

    @java.lang.Override
    public void removeNotify(org.jhotdraw.draw.Drawing d) {
        if (getStartFigure() != null) {
            ((org.jhotdraw.samples.pert.figures.TaskFigure) (getStartFigure())).removeDependency(this);
        }
        if (getEndFigure() != null) {
            ((org.jhotdraw.samples.pert.figures.TaskFigure) (getEndFigure())).removeDependency(this);
        }
        super.removeNotify(d);
    }
}