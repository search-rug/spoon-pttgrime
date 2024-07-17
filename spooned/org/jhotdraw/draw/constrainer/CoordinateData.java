package org.jhotdraw.draw.constrainer;
/**
 * data of additional coordinates for constrainer.
 *
 * @author tw
 */
public final class CoordinateData {
    private final java.awt.geom.Point2D.Double[] coords;

    private final int acutalIndex;

    public CoordinateData(java.awt.geom.Point2D.Double[] coords, int acutalIndex) {
        this.coords = coords;
        this.acutalIndex = acutalIndex;
    }

    public java.awt.geom.Point2D.Double[] getCoords() {
        return coords;
    }

    public int getActualIndex() {
        return acutalIndex;
    }
}