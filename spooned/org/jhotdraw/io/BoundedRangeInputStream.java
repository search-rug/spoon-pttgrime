/* @(#)BoundedRangeModel.java

Copyright (c) 1999-2008 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * This input stream implements the BoundedRangeModel and allows the observation of the input
 * reading process. <br>
 * history 1.0.1 02.05.1999 #setMaximum overrides the size information from the file. <br>
 * history 1.0 20.03.1999 Derived from javax.swing.ProgressMonitorInputStream.
 */
public class BoundedRangeInputStream extends java.io.FilterInputStream implements javax.swing.BoundedRangeModel {
    private int nread = 0;

    private int size = 0;

    private boolean valueIsAdjusting;

    /**
     * Only one ChangeEvent is needed per model instance since the event's only (read-only) state is
     * the source property. The source of events generated here is always "this".
     */
    protected transient javax.swing.event.ChangeEvent changeEvent = null;

    /**
     * The listeners waiting for model changes.
     */
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    /**
     * Create a new instance.
     */
    public BoundedRangeInputStream(java.io.InputStream in) {
        super(in);
        try {
            size = in.available();
        } catch (java.io.IOException ioe) {
            size = 0;
        }
    }

    /**
     * Overrides {@code FilterInputStream.read} to update the value after the read.
     */
    @java.lang.Override
    public int read() throws java.io.IOException {
        int c = in.read();
        if (c >= 0) {
            incrementValue(1);
        }
        return c;
    }

    /**
     * Overrides {@code FilterInputStream.read} to update the value after the read.
     */
    @java.lang.Override
    public int read(byte[] b) throws java.io.IOException {
        int nr = in.read(b);
        incrementValue(nr);
        return nr;
    }

    /**
     * Overrides {@code FilterInputStream.read} to update the value after the read.
     */
    @java.lang.Override
    public int read(byte[] b, int off, int len) throws java.io.IOException {
        int nr = in.read(b, off, len);
        incrementValue(nr);
        return nr;
    }

    /**
     * Overrides {@code FilterInputStream.skip} to update the value after the skip.
     */
    @java.lang.Override
    public long skip(long n) throws java.io.IOException {
        long nr = in.skip(n);
        incrementValue(((int) (nr)));
        return nr;
    }

    /**
     * Overrides {@code FilterInputStream.reset} to reset the progress monitor as well as the stream.
     */
    @java.lang.Override
    public synchronized void reset() throws java.io.IOException {
        in.reset();
        nread = size - in.available();
        fireStateChanged();
    }

    /**
     * Increments the extent by the indicated value. Negative Increments are ignored.
     *
     * @param inc
     * 		The incrementValue value.
     */
    private void incrementValue(int inc) {
        if (inc > 0) {
            nread += inc;
            if (nread > size) {
                size = nread;
            }
            fireStateChanged();
        }
    }

    /**
     * Returns the minimum acceptable value.
     *
     * @return the value of the minimum property
     * @see #setMinimum
     */
    @java.lang.Override
    public int getMinimum() {
        return 0;
    }

    /**
     * Ignored: The minimum of an input stream is always zero.
     *
     * <p>Sets the model's minimum to <I>newMinimum</I>. The other three properties may be changed as
     * well, to ensure that:
     *
     * <pre>
     * minimum &lt;= value &lt;= value+extent &lt;= maximum
     * </pre>
     *
     * <p>Notifies any listeners if the model changes.
     *
     * @param newMinimum
     * 		the model's new minimum
     * @see #getMinimum
     * @see #addChangeListener
     */
    @java.lang.Override
    public void setMinimum(int newMinimum) {
    }

    /**
     * Returns the model's maximum. Note that the upper limit on the model's value is (maximum -
     * extent).
     *
     * @return the value of the maximum property.
     * @see #setMaximum
     * @see #setExtent
     */
    @java.lang.Override
    public int getMaximum() {
        return size;
    }

    /**
     * Ignored: The maximum of an input stream can not be changed. # Sets the model's maximum to
     * <I>newMaximum</I>. The other three properties may be changed as well, to ensure that
     *
     * <pre>
     * minimum &lt;= value &lt;= value+extent &lt;= maximum
     * </pre>
     *
     * <p>Notifies any listeners if the model changes.
     *
     * @param newMaximum
     * 		the model's new maximum
     * @see #getMaximum
     * @see #addChangeListener
     */
    @java.lang.Override
    public void setMaximum(int newMaximum) {
        size = newMaximum;
        fireStateChanged();
    }

    /**
     * Returns the current read position.
     *
     * <p>Returns the model's current value. Note that the upper limit on the model's value is {@code maximum - extent} and the lower limit is {@code minimum}.
     *
     * @return the model's value
     * @see #setValue
     */
    @java.lang.Override
    public int getValue() {
        return nread;
    }

    /**
     * Ignored: The value is always zero.
     *
     * <p>Sets the model's current value to {@code newValue} if {@code newValue} satisfies the model's
     * constraints. Those constraints are:
     *
     * <pre>
     * minimum &lt;= value &lt;= value+extent &lt;= maximum
     * </pre>
     *
     * Otherwise, if {@code newValue} is less than {@code minimum} it's set to {@code minimum}, if its
     * greater than {@code maximum} then it's set to {@code maximum}, and if it's greater than {@code value+extent} then it's set to {@code value+extent}.
     *
     * <p>When a BoundedRange model is used with a scrollbar the value specifies the origin of the
     * scrollbar knob (aka the "thumb" or "elevator"). The value usually represents the origin of the
     * visible part of the object being scrolled.
     *
     * <p>Notifies any listeners if the model changes.
     *
     * @param newValue
     * 		the model's new value
     * @see #getValue
     */
    @java.lang.Override
    public void setValue(int newValue) {
    }

    /**
     * This attribute indicates that any upcoming changes to the value of the model should be
     * considered a single event. This attribute will be set to true at the start of a series of
     * changes to the value, and will be set to false when the value has finished changing. Normally
     * this allows a listener to only take action when the final value change in committed, instead of
     * having to do updates for all intermediate values.
     *
     * <p>Sliders and scrollbars use this property when a drag is underway.
     *
     * @param b
     * 		true if the upcoming changes to the value property are part of a series
     */
    @java.lang.Override
    public void setValueIsAdjusting(boolean b) {
        valueIsAdjusting = b;
    }

    /**
     * Returns true if the current changes to the value property are part of a series of changes.
     *
     * @return the valueIsAdjustingProperty.
     * @see #setValueIsAdjusting
     */
    @java.lang.Override
    public boolean getValueIsAdjusting() {
        return valueIsAdjusting;
    }

    /**
     * Returns the model's extent, the length of the inner range that begins at the model's value.
     *
     * @return the value of the model's extent property
     * @see #setExtent
     * @see #setValue
     */
    @java.lang.Override
    public int getExtent() {
        return 0;
    }

    /**
     * Ignored: The extent is always zero.
     */
    @java.lang.Override
    public void setExtent(int newExtent) {
    }

    /**
     * Ignored: All values depend on the input stream.
     */
    @java.lang.Override
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
    }

    /**
     * Adds a ChangeListener to the model's listener list.
     *
     * @param l
     * 		the ChangeListener to add
     * @see #removeChangeListener
     */
    @java.lang.Override
    public void addChangeListener(javax.swing.event.ChangeListener l) {
        listenerList.add(javax.swing.event.ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener.
     *
     * @param l
     * 		the ChangeListener to remove
     * @see #addChangeListener
     * @see BoundedRangeModel#removeChangeListener
     */
    @java.lang.Override
    public void removeChangeListener(javax.swing.event.ChangeListener l) {
        listenerList.remove(javax.swing.event.ChangeListener.class, l);
    }

    /**
     * Run each ChangeListeners stateChanged() method.
     *
     * @see #setRangeProperties
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
}