/* @(#)ReversedList.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * A ReversedList provides in unmodifiable view on a List in reverse order.
 *
 * @author wrandels
 */
public class ReversedList<T> extends java.util.AbstractList<T> {
    private java.util.List<T> target;

    /**
     * Creates a new instance of ReversedList
     */
    public ReversedList(java.util.List<T> target) {
        this.target = target;
    }

    @java.lang.Override
    public T get(int index) {
        return target.get((target.size() - 1) - index);
    }

    @java.lang.Override
    public int size() {
        return target.size();
    }
}