/* @(#)SlantedLiner.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.liner;
/**
 * SlantedLiner.
 */
public class SlantedLiner implements org.jhotdraw.draw.liner.Liner {
    private double slantSize;

    public SlantedLiner() {
        this(20);
    }

    public SlantedLiner(double slantSize) {
        this.slantSize = slantSize;
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(org.jhotdraw.geom.path.BezierPath path) {
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public void lineout(org.jhotdraw.draw.figure.ConnectionFigure figure) {
        org.jhotdraw.geom.path.BezierPath path = ((org.jhotdraw.draw.figure.LineConnectionFigure) (figure)).getBezierPath();
        org.jhotdraw.draw.connector.Connector start = figure.getStartConnector();
        org.jhotdraw.draw.connector.Connector end = figure.getEndConnector();
        if (((start == null) || (end == null)) || (path == null)) {
            return;
        }
        // Special treatment if the connection connects the same figure
        if (figure.getStartFigure() == figure.getEndFigure()) {
            // Ensure path has exactly four nodes
            while (path.size() < 5) {
                path.add(1, new org.jhotdraw.geom.path.BezierPath.Node(0, 0));
            } 
            while (path.size() > 5) {
                path.remove(1);
            } 
            java.awt.geom.Point2D.Double sp = start.findStart(figure);
            java.awt.geom.Point2D.Double ep = end.findEnd(figure);
            java.awt.geom.Rectangle2D.Double sb = start.getBounds();
            java.awt.geom.Rectangle2D.Double eb = end.getBounds();
            int soutcode = sb.outcode(sp);
            if (soutcode == 0) {
                soutcode = org.jhotdraw.geom.Geom.outcode(sb, eb);
            }
            int eoutcode = eb.outcode(ep);
            if (eoutcode == 0) {
                eoutcode = org.jhotdraw.geom.Geom.outcode(sb, eb);
            }
            path.nodes().get(0).moveTo(sp);
            path.nodes().get(path.size() - 1).moveTo(ep);
            switch (soutcode) {
                case org.jhotdraw.geom.Geom.OUT_TOP :
                    eoutcode = org.jhotdraw.geom.Geom.OUT_LEFT;
                    break;
                case org.jhotdraw.geom.Geom.OUT_RIGHT :
                    eoutcode = org.jhotdraw.geom.Geom.OUT_TOP;
                    break;
                case org.jhotdraw.geom.Geom.OUT_BOTTOM :
                    eoutcode = org.jhotdraw.geom.Geom.OUT_RIGHT;
                    break;
                case org.jhotdraw.geom.Geom.OUT_LEFT :
                    eoutcode = org.jhotdraw.geom.Geom.OUT_BOTTOM;
                    break;
                default :
                    eoutcode = org.jhotdraw.geom.Geom.OUT_TOP;
                    soutcode = org.jhotdraw.geom.Geom.OUT_RIGHT;
                    break;
            }
            path.nodes().get(1).moveTo(sp.x + slantSize, sp.y);
            if ((soutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(1).moveTo(sp.x + slantSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(1).moveTo(sp.x - slantSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(1).moveTo(sp.x, sp.y + slantSize);
            } else {
                path.nodes().get(1).moveTo(sp.x, sp.y - slantSize);
            }
            if ((eoutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(3).moveTo(ep.x + slantSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(3).moveTo(ep.x - slantSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(3).moveTo(ep.x, ep.y + slantSize);
            } else {
                path.nodes().get(3).moveTo(ep.x, ep.y - slantSize);
            }
            switch (soutcode) {
                case org.jhotdraw.geom.Geom.OUT_RIGHT :
                    path.nodes().get(2).moveTo(path.nodes().get(1).x[0], path.nodes().get(3).y[0]);
                    break;
                case org.jhotdraw.geom.Geom.OUT_TOP :
                    path.nodes().get(2).moveTo(path.nodes().get(1).y[0], path.nodes().get(3).x[0]);
                    break;
                case org.jhotdraw.geom.Geom.OUT_LEFT :
                    path.nodes().get(2).moveTo(path.nodes().get(1).x[0], path.nodes().get(3).y[0]);
                    break;
                case org.jhotdraw.geom.Geom.OUT_BOTTOM :
                default :
                    path.nodes().get(2).moveTo(path.nodes().get(1).y[0], path.nodes().get(3).x[0]);
                    break;
            }
            // Regular treatment if the connection connects to two different figures
        } else {
            // Ensure path has exactly four nodes
            while (path.size() < 4) {
                path.add(1, new org.jhotdraw.geom.path.BezierPath.Node(0, 0));
            } 
            while (path.size() > 4) {
                path.remove(1);
            } 
            java.awt.geom.Point2D.Double sp = start.findStart(figure);
            java.awt.geom.Point2D.Double ep = end.findEnd(figure);
            java.awt.geom.Rectangle2D.Double sb = start.getBounds();
            java.awt.geom.Rectangle2D.Double eb = end.getBounds();
            int soutcode = sb.outcode(sp);
            if (soutcode == 0) {
                if (sp.x <= sb.x) {
                    soutcode = org.jhotdraw.geom.Geom.OUT_LEFT;
                } else if (sp.y <= sb.y) {
                    soutcode = org.jhotdraw.geom.Geom.OUT_TOP;
                } else if (sp.x >= (sb.x + sb.width)) {
                    soutcode = org.jhotdraw.geom.Geom.OUT_RIGHT;
                } else if (sp.y >= (sb.y + sb.height)) {
                    soutcode = org.jhotdraw.geom.Geom.OUT_BOTTOM;
                } else {
                    soutcode = org.jhotdraw.geom.Geom.outcode(sb, eb);
                }
            }
            int eoutcode = eb.outcode(ep);
            if (eoutcode == 0) {
                if (ep.x <= eb.x) {
                    eoutcode = org.jhotdraw.geom.Geom.OUT_LEFT;
                } else if (ep.y <= eb.y) {
                    eoutcode = org.jhotdraw.geom.Geom.OUT_TOP;
                } else if (ep.x >= (eb.x + eb.width)) {
                    eoutcode = org.jhotdraw.geom.Geom.OUT_RIGHT;
                } else if (ep.y >= (eb.y + eb.height)) {
                    eoutcode = org.jhotdraw.geom.Geom.OUT_BOTTOM;
                } else {
                    eoutcode = org.jhotdraw.geom.Geom.outcode(sb, eb);
                }
            }
            path.nodes().get(0).moveTo(sp);
            path.nodes().get(path.size() - 1).moveTo(ep);
            if ((soutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(1).moveTo(sp.x + slantSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(1).moveTo(sp.x - slantSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(1).moveTo(sp.x, sp.y + slantSize);
            } else {
                path.nodes().get(1).moveTo(sp.x, sp.y - slantSize);
            }
            if ((eoutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(2).moveTo(ep.x + slantSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(2).moveTo(ep.x - slantSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(2).moveTo(ep.x, ep.y + slantSize);
            } else {
                path.nodes().get(2).moveTo(ep.x, ep.y - slantSize);
            }
        }
        // Ensure all path nodes are straight
        for (org.jhotdraw.geom.path.BezierPath.Node node : path.nodes()) {
            node.setMask(org.jhotdraw.geom.path.BezierPath.C0_MASK);
        }
        path.invalidatePath();
    }

    // @Override
    // public void read(DOMInput in) {
    // slantSize = in.getAttribute("slant", 20d);
    // }
    // 
    // @Override
    // public void write(DOMOutput out) {
    // out.addAttribute("slant", slantSize);
    // }
    @java.lang.Override
    public org.jhotdraw.draw.liner.Liner clone() {
        try {
            return ((org.jhotdraw.draw.liner.Liner) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            java.lang.InternalError error = new java.lang.InternalError(ex.getMessage());
            error.initCause(ex);
            throw error;
        }
    }
}