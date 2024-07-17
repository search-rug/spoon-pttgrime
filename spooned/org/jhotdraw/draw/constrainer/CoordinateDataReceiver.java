package org.jhotdraw.draw.constrainer;
/**
 * To set a coordinate data supplier. This is used for e.g. points used by constrainers.
 */
public interface CoordinateDataReceiver {
    void clearCoordinateSupplier();

    org.jhotdraw.draw.constrainer.CoordinateDataSupplier getCoordinateSupplier();

    void setCoordinateSupplier(org.jhotdraw.draw.constrainer.CoordinateDataSupplier coordinateSupplier);
}