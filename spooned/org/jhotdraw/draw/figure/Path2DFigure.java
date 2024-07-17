/* @(#)LineFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
public class Path2DFigure extends org.jhotdraw.draw.figure.AbstractAttributedFigure {
    private static final long serialVersionUID = 1L;

    private final org.jhotdraw.geom.path.MutablePath2D path = new org.jhotdraw.geom.path.MutablePath2D();

    public Path2DFigure() {
        setConnectable(false);
    }

    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        return path.contains(p);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        if ((detailLevel == (-1)) || (detailLevel == 0)) {
            return super.createHandles(detailLevel);
        } else {
            java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.ArrayList<>();
            switch (detailLevel) {
                case 1 :
                    for (int i = 0; i < path.size(); i++) {
                        int idx = i;
                        handles.add(new org.jhotdraw.draw.handle.TrackingHandle(this, () -> path.getNodePoint(idx), p -> path.changeNode(idx, node -> node.withPoint(p.x, p.y))));
                    }
                    break;
            }
            return handles;
        }
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (path.getBounds2D()));
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");// Generated from

        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @java.lang.Override
    public boolean handleMouseClick(java.awt.geom.Point2D.Double p, java.awt.event.MouseEvent evt, org.jhotdraw.draw.DrawingView view) {
        // if (evt.getClickCount() == 2 && view.getHandleDetailLevel() == 0) {
        // willChange();
        // final int index = splitSegment(p, (float) (5f / view.getScaleFactor()));
        // if (index != -1) {
        // final BezierPath.Node newNode = getNode(index);
        // fireUndoableEditHappened(
        // new AbstractUndoableEdit() {
        // private static final long serialVersionUID = 1L;
        // 
        // @Override
        // public void redo() throws CannotRedoException {
        // super.redo();
        // willChange();
        // addNode(index, newNode);
        // changed();
        // }
        // 
        // @Override
        // public void undo() throws CannotUndoException {
        // super.undo();
        // willChange();
        // removeNode(index);
        // changed();
        // }
        // });
        // changed();
        // return true;
        // }
        // }
        return false;
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object restoreData) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");// Generated from

        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");// Generated from

        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        // if (isClosed()) {
        double grow = org.jhotdraw.draw.AttributeKeys.getPerpendicularDrawGrowth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g));
        if (grow == 0.0) {
            g.draw(path);
        } else {
            org.jhotdraw.geom.GrowStroke gs = new org.jhotdraw.geom.GrowStroke(grow, org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth(this, org.jhotdraw.draw.AttributeKeys.getScaleFactorFromGraphics(g)) * attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_MITER_LIMIT));
            g.draw(gs.createStrokedShape(path));
        }
        // } else {
        // g.draw(getCappedPath());
        // }
        // drawCaps(g);
    }
}