/* @(#)CurvedLiner.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.liner;
/**
 * A {@link Liner} that constrains a connection to a curved line.
 */
public class CurvedLiner implements org.jhotdraw.draw.liner.Liner {
    private double shoulderSize;

    public CurvedLiner() {
        this(20);
    }

    public CurvedLiner(double slantSize) {
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
            // Ensure path has exactly 4 nodes
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
            // path.nodes().get(0).moveTo(sp.x + shoulderSize, sp.y);
            path.nodes().get(0).mask = org.jhotdraw.geom.path.BezierPath.C2_MASK;
            if ((soutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(0).x[2] = sp.x + shoulderSize;
                path.nodes().get(0).y[2] = sp.y;
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(0).x[2] = sp.x - shoulderSize;
                path.nodes().get(0).y[2] = sp.y;
            } else if ((soutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(0).x[2] = sp.x;
                path.nodes().get(0).y[2] = sp.y + shoulderSize;
            } else {
                path.nodes().get(0).x[2] = sp.x;
                path.nodes().get(0).y[2] = sp.y - shoulderSize;
            }
            path.nodes().get(1).mask = org.jhotdraw.geom.path.BezierPath.C2_MASK;
            path.nodes().get(1).moveTo(sp.x + shoulderSize, (sp.y + ep.y) / 2);
            path.nodes().get(1).x[2] = sp.x + shoulderSize;
            path.nodes().get(1).y[2] = ep.y - shoulderSize;
            path.nodes().get(2).mask = org.jhotdraw.geom.path.BezierPath.C1_MASK;
            path.nodes().get(2).moveTo((sp.x + ep.x) / 2, ep.y - shoulderSize);
            path.nodes().get(2).x[1] = sp.x + shoulderSize;
            path.nodes().get(2).y[1] = ep.y - shoulderSize;
            path.nodes().get(3).mask = org.jhotdraw.geom.path.BezierPath.C1_MASK;
            if ((eoutcode & org.jhotdraw.geom.Geom.OUT_RIGHT) != 0) {
                path.nodes().get(3).x[1] = ep.x + shoulderSize;
                path.nodes().get(3).y[1] = ep.y;
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_LEFT) != 0) {
                path.nodes().get(3).x[1] = ep.x - shoulderSize;
                path.nodes().get(3).y[1] = ep.y;
            } else if ((eoutcode & org.jhotdraw.geom.Geom.OUT_BOTTOM) != 0) {
                path.nodes().get(3).x[1] = ep.x;
                path.nodes().get(3).y[1] = ep.y + shoulderSize;
            } else {
                path.nodes().get(3).x[1] = ep.x;
                path.nodes().get(3).y[1] = ep.y - shoulderSize;
            }
        } else {
            java.awt.geom.Point2D.Double sp = start.findStart(figure);
            java.awt.geom.Point2D.Double ep = end.findEnd(figure);
            path.clear();
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
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C2_MASK, sp.x, sp.y, sp.x, sp.y, sp.x, (sp.y + ep.y) / 2));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C1_MASK, ep.x, ep.y, ep.x, (sp.y + ep.y) / 2, ep.x, ep.y));
                } else if (((soutcode & (org.jhotdraw.geom.Geom.OUT_LEFT | org.jhotdraw.geom.Geom.OUT_RIGHT)) != 0) && ((eoutcode & (org.jhotdraw.geom.Geom.OUT_LEFT | org.jhotdraw.geom.Geom.OUT_RIGHT)) != 0)) {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C2_MASK, sp.x, sp.y, sp.x, sp.y, (sp.x + ep.x) / 2, sp.y));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C1_MASK, ep.x, ep.y, (sp.x + ep.x) / 2, ep.y, ep.x, ep.y));
                } else if ((soutcode == org.jhotdraw.geom.Geom.OUT_BOTTOM) || (soutcode == org.jhotdraw.geom.Geom.OUT_TOP)) {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C2_MASK, sp.x, sp.y, sp.x, sp.y, sp.x, ep.y));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, ep.y));
                } else {
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(org.jhotdraw.geom.path.BezierPath.C2_MASK, sp.x, sp.y, sp.x, sp.y, ep.x, sp.y));
                    path.add(new org.jhotdraw.geom.path.BezierPath.Node(ep.x, ep.y));
                }
            }
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