/* @(#)NodeFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.net.figures;
/**
 * NodeFigure.
 */
public class NodeFigure extends org.jhotdraw.draw.figure.TextFigure {
    private static final long serialVersionUID = 1L;

    private java.util.LinkedList<org.jhotdraw.draw.connector.Connector> connectors;

    private static org.jhotdraw.draw.connector.LocatorConnector north;

    private static org.jhotdraw.draw.connector.LocatorConnector south;

    private static org.jhotdraw.draw.connector.LocatorConnector east;

    private static org.jhotdraw.draw.connector.LocatorConnector west;

    public NodeFigure() {
        org.jhotdraw.draw.figure.RectangleFigure rf = new org.jhotdraw.draw.figure.RectangleFigure();
        setDecorator(rf);
        createConnectors();
        attr().set(org.jhotdraw.draw.AttributeKeys.DECORATOR_INSETS, new org.jhotdraw.geom.Insets2D.Double(6, 10, 6, 10));
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.net.Labels");
        setText(labels.getString("nodeDefaultName"));
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.DECORATOR_INSETS, false);
    }

    private void createConnectors() {
        connectors = new java.util.LinkedList<>();
        connectors.add(new org.jhotdraw.draw.connector.LocatorConnector(this, org.jhotdraw.draw.locator.RelativeLocator.north()));
        connectors.add(new org.jhotdraw.draw.connector.LocatorConnector(this, org.jhotdraw.draw.locator.RelativeLocator.east()));
        connectors.add(new org.jhotdraw.draw.connector.LocatorConnector(this, org.jhotdraw.draw.locator.RelativeLocator.west()));
        connectors.add(new org.jhotdraw.draw.connector.LocatorConnector(this, org.jhotdraw.draw.locator.RelativeLocator.south()));
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.connector.Connector> getConnectors(org.jhotdraw.draw.figure.ConnectionFigure prototype) {
        return java.util.Collections.unmodifiableList(connectors);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel) {
            case -1 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(getDecorator(), false, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northEast()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southEast()));
                for (org.jhotdraw.draw.connector.Connector c : connectors) {
                    handles.add(new org.jhotdraw.draw.handle.ConnectorHandle(c, new org.jhotdraw.draw.figure.LineConnectionFigure()));
                }
                break;
        }
        return handles;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getFigureDrawingArea(double scale) {
        java.awt.geom.Rectangle2D.Double b = super.getFigureDrawingArea(scale);
        // Grow for connectors
        org.jhotdraw.geom.Geom.grow(b, 10.0, 10.0);
        return b;
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findConnector(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.ConnectionFigure figure) {
        // return closest connector
        double min = java.lang.Double.MAX_VALUE;
        org.jhotdraw.draw.connector.Connector closest = null;
        for (org.jhotdraw.draw.connector.Connector c : connectors) {
            java.awt.geom.Point2D.Double p2 = org.jhotdraw.geom.Geom.center(c.getBounds());
            double d = org.jhotdraw.geom.Geom.length2(p.x, p.y, p2.x, p2.y);
            if (d < min) {
                min = d;
                closest = c;
            }
        }
        return closest;
    }

    @java.lang.Override
    public org.jhotdraw.draw.connector.Connector findCompatibleConnector(org.jhotdraw.draw.connector.Connector c, boolean isStart) {
        if (c instanceof org.jhotdraw.draw.connector.LocatorConnector) {
            org.jhotdraw.draw.connector.LocatorConnector lc = ((org.jhotdraw.draw.connector.LocatorConnector) (c));
            for (org.jhotdraw.draw.connector.Connector cc : connectors) {
                org.jhotdraw.draw.connector.LocatorConnector lcc = ((org.jhotdraw.draw.connector.LocatorConnector) (cc));
                if (lcc.getLocator().equals(lc.getLocator())) {
                    return lcc;
                }
            }
        }
        return connectors.getFirst();
    }

    @java.lang.Override
    public org.jhotdraw.samples.net.figures.NodeFigure clone() {
        org.jhotdraw.samples.net.figures.NodeFigure that = ((org.jhotdraw.samples.net.figures.NodeFigure) (super.clone()));
        that.createConnectors();
        return that;
    }

    @java.lang.Override
    public int getLayer() {
        return -1;// stay below ConnectionFigures

    }
}