/* @(#)StickyRectangleConnector.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.connector;
/**
 * A StickyRectangleConnector locates connection points by choping the connection between the
 * centers of the two figures at the display box.
 *
 * <p>The location of the connection point is computed once, when the user connects the figure.
 * Moving the figure around will not change the location.
 */
// @Override
// public void read(DOMInput in) throws IOException {
// super.read(in);
// angle = (float) in.getAttribute("angle", 0.0);
// }
// 
// @Override
// public void write(DOMOutput out) throws IOException {
// super.write(out);
// out.addAttribute("angle", angle);
// }
public class StickyRectangleConnector extends org.jhotdraw.draw.connector.ChopRectangleConnector {
    private static final long serialVersionUID = 1L;

    private float angle;

    public StickyRectangleConnector() {
    }

    public StickyRectangleConnector(org.jhotdraw.draw.figure.Figure owner) {
        super(owner);
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        g.setColor(java.awt.Color.blue);
        g.setStroke(new java.awt.BasicStroke());
        g.draw(getBounds());
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public StickyRectangleConnector(org.jhotdraw.draw.figure.Figure owner, java.awt.geom.Point2D.Double p) {
        super(owner);
        this.angle = ((float) (org.jhotdraw.geom.Geom.pointToAngle(owner.getBounds(), p)));
    }

    @java.lang.Override
    public void updateAnchor(java.awt.geom.Point2D.Double p) {
        this.angle = ((float) (org.jhotdraw.geom.Geom.pointToAngle(getOwner().getBounds(), p)));
    }

    @java.lang.Override
    public java.awt.geom.Point2D.Double getAnchor() {
        return org.jhotdraw.geom.Geom.angleToPoint(getOwner().getBounds(), angle);
    }

    @java.lang.Override
    protected java.awt.geom.Point2D.Double chop(org.jhotdraw.draw.figure.Figure target, java.awt.geom.Point2D.Double from) {
        return org.jhotdraw.geom.Geom.angleToPoint(target.getBounds(), angle);
    }

    public java.lang.String getParameters() {
        return java.lang.Float.toString(((float) ((angle / java.lang.Math.PI) * 180)));
    }
}