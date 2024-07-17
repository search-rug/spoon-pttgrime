/* Copyright (C) 2024 JHotDraw.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
 */
package org.jhotdraw.draw.locator;
/**
 * A {@link Locator} which can be used to place a label on the path of a path like structure. {@link LinearLocatorBase}.
 *
 * <p>The point is located at a distance and an angle relative to the total length of the bezier path.
 *
 * <p>The angle should is perpendicular to the path.
 */
public class LinearLabelLocator implements org.jhotdraw.draw.locator.Locator {
    private double relativePosition;

    private double angle;

    private double distance;

    public LinearLabelLocator() {
    }

    /**
     * Creates a new locator.
     *
     * @param relativePosition
     * 		The relative position of the label on the polyline. 0.0 specifies the
     * 		start of the bezier path, 1.0 the end of the polyline. Values between 0.0 and 1.0 are
     * 		relative positions on the bezier path.
     * @param angle
     * 		The angle of the distance vector.
     * @param distance
     * 		The length of the distance vector.
     */
    public LinearLabelLocator(double relativePosition, double angle, double distance) {
        this.relativePosition = relativePosition;
        this.angle = angle;
        this.distance = distance;
    }

    @java.lang.Override
    public org.jhotdraw.draw.locator.Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, double scale) {
        if (owner instanceof org.jhotdraw.draw.locator.LinearLocatorBase path)
            return getRelativePoint(path, scale);
        else
            return org.jhotdraw.draw.locator.LinearLabelLocator.returnBoundsCenter(owner);

    }

    public double getRelativePosition() {
        return relativePosition;
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setRelativePosition(double relativePosition) {
        this.relativePosition = relativePosition;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @java.lang.Override
    public org.jhotdraw.draw.locator.Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.figure.Figure label, double scale) {
        if (owner instanceof org.jhotdraw.draw.locator.LinearLocatorBase path)
            return getRelativeLabelPoint(path, label, scale);
        else
            return org.jhotdraw.draw.locator.LinearLabelLocator.returnBoundsCenter(owner);

    }

    /**
     * Returns the coordinates of the relative point on the path of the specified path.
     */
    protected org.jhotdraw.draw.locator.Locator.Position getRelativePoint(org.jhotdraw.draw.locator.LinearLocatorBase owner, double scale) {
        java.awt.geom.Point2D.Double point = owner.getPointOnPath(relativePosition, 0.1);
        java.awt.geom.Point2D.Double nextPoint = owner.getPointOnPath(relativePosition < 0.5 ? relativePosition + 0.1 : relativePosition - 0.1, 0.1);
        double dir = java.lang.Math.atan2(nextPoint.y - point.y, nextPoint.x - point.x);
        if (relativePosition >= 0.5) {
            dir += java.lang.Math.PI;
        }
        double alpha = dir + angle;
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(point.x + ((distance / scale) * java.lang.Math.cos(alpha)), point.y + ((distance / scale) * java.lang.Math.sin(alpha)));
        if (java.lang.Double.isNaN(p.x)) {
            p = point;
        }
        return new org.jhotdraw.draw.locator.Locator.Position(p, dir);
    }

    /**
     * Returns a Point2D.Double on the polyline that is at the provided
     */
    protected org.jhotdraw.draw.locator.Locator.Position getRelativeLabelPoint(org.jhotdraw.draw.locator.LinearLocatorBase owner, org.jhotdraw.draw.figure.Figure label, double scale) {
        // Get a point on the path an the next point on the path
        java.awt.geom.Point2D.Double point = owner.getPointOnPath(relativePosition, 0.1);
        org.jhotdraw.draw.locator.Locator.Position position = getRelativePoint(owner, scale);
        // If there is a fixed origin, this locator should move the origin the the boundary midth.
        // This should then do the label component.
        if ((label instanceof org.jhotdraw.draw.figure.Origin) && (label instanceof org.jhotdraw.draw.figure.Rotation)) {
            return position;
        }
        java.awt.geom.Point2D.Double p = position.location();
        org.jhotdraw.geom.Dimension2DDouble labelDim = label.getPreferredSize(scale);
        if (((relativePosition == 0.5) && (p.x >= (point.x - (distance / 2)))) && (p.x <= (point.x + (distance / 2)))) {
            if (p.y >= point.y) {
                // South East
                return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x - (labelDim.width / 2), p.y), position.angle());
            } else {
                // North East
                return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x - (labelDim.width / 2), p.y - labelDim.height), position.angle());
            }
        } else if (p.x >= point.x) {
            if (p.y >= point.y) {
                // South East
                return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x, p.y), position.angle());
            } else {
                // North East
                return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x, p.y - labelDim.height), position.angle());
            }
        } else if (p.y >= point.y) {
            // South West
            return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x - labelDim.width, p.y), position.angle());
        } else {
            // North West
            return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(p.x - labelDim.width, p.y - labelDim.height), position.angle());
        }
    }

    public static final org.jhotdraw.draw.locator.Position returnBoundsCenter(org.jhotdraw.draw.figure.Figure owner) {
        org.jhotdraw.draw.locator.var bounds = owner.getBounds();
        return new org.jhotdraw.draw.locator.Locator.Position(new java.awt.geom.Point2D.Double(bounds.getCenterX(), bounds.getCenterY()), 0);
    }
}