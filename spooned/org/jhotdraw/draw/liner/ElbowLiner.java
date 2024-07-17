/* @(#)ElbowLiner.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.liner;
/**
 * A {@link Liner} that constrains a connection to orthogonal lines.
 */
public class ElbowLiner implements org.jhotdraw.draw.liner.Liner {
    private double shoulderSize;

    public ElbowLiner() {
        this(20);
    }

    public ElbowLiner(double slantSize) {
        this.shoulderSize = slantSize;
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
            path.nodes().get(1).moveTo(sp.x + shoulderSize, sp.y);
            if ((soutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(1).moveTo(sp.x + shoulderSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(1).moveTo(sp.x - shoulderSize, sp.y);
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(1).moveTo(sp.x, sp.y + shoulderSize);
            } else {
                path.nodes().get(1).moveTo(sp.x, sp.y - shoulderSize);
            }
            if ((eoutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(3).moveTo(ep.x + shoulderSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(3).moveTo(ep.x - shoulderSize, ep.y);
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(3).moveTo(ep.x, ep.y + shoulderSize);
            } else {
                path.nodes().get(3).moveTo(ep.x, ep.y - shoulderSize);
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
        } else {
            java.awt.geom.Point2D.Double sp = start.findStart(figure);
            java.awt.geom.Point2D.Double ep = end.findEnd(figure);
            path.clear();
            path.add(new org.jhotdraw.geom.path.BezierPath.Node(sp.x, sp.y));
            if ((sp.x == ep.x) || (sp.y == ep.y)) {
                path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, ep.y));
            } else {
                java.awt.geom.Rectangle2D.Double sb = start.getBounds();
                sb.x += 5.0;
                sb.y += 5.0;
                sb.width -= 10.0;
                sb.height -= 10.0;
                java.awt.geom.Rectangle2D.Double eb = end.getBounds();
                eb.x += 5.0;
                eb.y += 5.0;
                eb.width -= 10.0;
                eb.height -= 10.0;
                int soutcode = sb.outcode(sp);
                if (soutcode == 0) {
                    soutcode = org.jhotdraw.geom.Geom.outcode(sb, eb);
                }
                int eoutcode = eb.outcode(ep);
                if (eoutcode == 0) {
                    eoutcode = org.jhotdraw.geom.Geom.outcode(eb, sb);
                }
                if (((soutcode & (org.jhotdraw.geom.Geom.OUT_TOP | org.jhotdraw.geom.Geom.OUT_BOTTOM)) != 0) && ((eoutcode & (org.jhotdraw.geom.Geom.OUT_TOP | org.jhotdraw.geom.Geom.OUT_BOTTOM)) != 0)) {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(sp.x, (sp.y + ep.y) / 2));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, (sp.y + ep.y) / 2));
                } else if (((soutcode & (org.jhotdraw.geom.Geom.OUT_LEFT | org.jhotdraw.geom.Geom.OUT_RIGHT)) != 0) && ((eoutcode & (org.jhotdraw.geom.Geom.OUT_LEFT | org.jhotdraw.geom.Geom.OUT_RIGHT)) != 0)) {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node((sp.x + ep.x) / 2, sp.y));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node((sp.x + ep.x) / 2, ep.y));
                } else if ((soutcode == org.jhotdraw.geom.Geom.OUT_BOTTOM) || (soutcode == org.jhotdraw.geom.Geom.OUT_TOP)) {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(sp.x, ep.y));
                } else {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, sp.y));
                }
                path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, ep.y));
            }
        }
        // Ensure all path nodes are straight
        for (org.jhotdraw.geom.path.BezierPath.Node node : path.nodes()) {
            node.setMask(org.jhotdraw.geom.path.BezierPath.C0_MASK);
        }
        path.invalidatePath();
    }

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