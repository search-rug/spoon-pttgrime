package org.jhotdraw.draw.constrainer;
/**
 * Additional constrainer using in realtime supplied points from active tools. These type of
 * Constrainers work on a pointlist including an actual index.
 *
 * <p>c1 , c2 , c3 , c4 , c5 idx
 *
 * <p>With this anglesnap or distance snap will be possible.
 *
 * <p>The constrainer needs to tell, how many points are needed.
 *
 * @author tw
 */
public abstract class AbstractCoordinateConstrainerExtension implements org.jhotdraw.draw.constrainer.CoordinateDataRangeProvider {
    final int pointsBefore;

    final int pointsAfter;

    private boolean active = true;

    public AbstractCoordinateConstrainerExtension(int pointsBefore, int pointsAfter) {
        this.pointsBefore = pointsBefore;
        this.pointsAfter = pointsAfter;
    }

    @java.lang.Override
    public final int needsPointsBefore() {
        return pointsBefore;
    }

    @java.lang.Override
    public final int needsPointsAfter() {
        return pointsAfter;
    }

    public java.awt.geom.Point2D.Double constrainPoint(final org.jhotdraw.draw.constrainer.CoordinateData coordData, double snapDistance, final java.awt.geom.Point2D.Double p) {
        return p;
    }

    public final boolean isActive() {
        return active;
    }

    public final void setActive(boolean active) {
        this.active = active;
    }
}