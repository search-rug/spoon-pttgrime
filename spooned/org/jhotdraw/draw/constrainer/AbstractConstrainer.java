/* @(#)AbstractConstrainer.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.constrainer;
/**
 * This abstract class can be extended to implement a {@link Constrainer}.
 */
public abstract class AbstractConstrainer implements org.jhotdraw.draw.constrainer.Constrainer , org.jhotdraw.draw.constrainer.CoordinateDataReceiver {
    private static final long serialVersionUID = 1L;

    /**
     * The listeners waiting for model changes.
     */
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    /**
     * Only one <code>ChangeEvent</code> is needed per model instance since the event's only
     * (read-only) state is the source property. The source of events generated here is always "this".
     */
    protected transient javax.swing.event.ChangeEvent changeEvent = null;

    public AbstractConstrainer() {
    }

    @java.lang.Override
    public void addChangeListener(javax.swing.event.ChangeListener l) {
        listenerList.add(javax.swing.event.ChangeListener.class, l);
    }

    @java.lang.Override
    public void removeChangeListener(javax.swing.event.ChangeListener l) {
        listenerList.remove(javax.swing.event.ChangeListener.class, l);
    }

    /**
     * Runs each <code>ChangeListener</code>'s <code>stateChanged</code> method.
     *
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        java.lang.Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new javax.swing.event.ChangeEvent(this);
                }
                ((javax.swing.event.ChangeListener) (listeners[i + 1])).stateChanged(changeEvent);
            }
        }
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.AbstractConstrainer clone() {
        org.jhotdraw.draw.constrainer.AbstractConstrainer that;
        try {
            that = ((org.jhotdraw.draw.constrainer.AbstractConstrainer) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new java.lang.InternalError("unable to clone", ex);
        }
        that.listenerList = new javax.swing.event.EventListenerList();
        return that;
    }

    private final java.util.Set<org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension> coordExtension = new java.util.LinkedHashSet<>();

    public void clearCoordConstrainerExtensions() {
        coordExtension.clear();
    }

    public void addCoordConstrainerExtension(org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension ext) {
        coordExtension.add(ext);
    }

    public void removeCoordConstrainerExtension(org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension ext) {
        coordExtension.remove(ext);
    }

    public <T extends org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension> T getCoordConstrainerExtension(java.lang.Class<T> clazz) {
        for (org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension item : coordExtension) {
            if (clazz.isInstance(item)) {
                return ((T) (item));
            }
        }
        return null;
    }

    private org.jhotdraw.draw.constrainer.CoordinateDataSupplier coordinateSupplier = null;

    @java.lang.Override
    public void clearCoordinateSupplier() {
        coordinateSupplier = null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.constrainer.CoordinateDataSupplier getCoordinateSupplier() {
        return coordinateSupplier;
    }

    @java.lang.Override
    public void setCoordinateSupplier(org.jhotdraw.draw.constrainer.CoordinateDataSupplier coordinateSupplier) {
        this.coordinateSupplier = coordinateSupplier;
    }

    /**
     * Constrains this point by extensions. This is used to e.g. contrain angles, distances, ....
     * To activage this, it has to be included in some constrainPoint methods while implementing
     * a new Constrainer.
     *
     * The main procedure here is, that drawing tools are able to provide points while drawing. Mainly
     * all CoordinateDataSuppliers are used. To add one you need to set it using setCoordinateSupplier.
     *
     * @param point
     * @param snapDistance
     * @return  */
    protected java.awt.geom.Point2D.Double constrainPointByExtensions(final java.awt.geom.Point2D.Double point, final double snapDistance) {
        java.awt.geom.Point2D.Double snap = point;
        if ((coordinateSupplier != null) && (!coordExtension.isEmpty())) {
            org.jhotdraw.draw.constrainer.CoordinateData cdata = coordinateSupplier.getConstrainerCoordinatesForExtensions(coordExtension);
            for (org.jhotdraw.draw.constrainer.AbstractCoordinateConstrainerExtension ext : coordExtension) {
                if (ext.isActive()) {
                    snap = ext.constrainPoint(cdata, snapDistance, snap);
                }
            }
        }
        return snap;
    }
}